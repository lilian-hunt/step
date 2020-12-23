// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that allows users to comment on the portfolio. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final static Logger LOGGER = Logger.getLogger(DataServlet.class.getName());
  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private UserService userService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get all the comments from the database.
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    Map<String, Map<String, String>> comments = new HashMap<>();
    for (Entity entity : results.asIterable()) {
      String id = KeyFactory.keyToString(entity.getKey());
      Map<String, String> commentAttributes = new HashMap<>();
      commentAttributes.put("comment", (String) entity.getProperty("comment"));
      commentAttributes.put("userEmail", (String) entity.getProperty("userEmail"));
      commentAttributes.put("imageUrl", (String) entity.getProperty("imageUrl"));
      comments.put(id, commentAttributes);
    }

    response.setContentType("application/json");
    response.getWriter().println(toJSONString(comments, "comments"));
  }

  /*
   * Helper function to store a map in JSON format.
   */
  private String toJSONString(Map<String, Map<String, String>> map, String name) {
    Gson gson = new Gson();
    if (map != null) {
      String json = "{ \"" + name + "\" :" + gson.toJson(map).toString() + "}";
      return json;
    }
    return null;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String feedback = request.getParameter("text-input");
    // Get the URL of the image that the user uploaded to Blobstore.
    String imageUrl = getUploadedFileUrl(request, "image");
    long timestamp = System.currentTimeMillis();
    String userEmail = userService.getCurrentUser().getEmail();

    // Only add feedback if valid input.
    if (feedback != "" || imageUrl != null) {
      Entity commentEntity = new Entity("Comment");

      commentEntity.setProperty("comment", feedback);
      commentEntity.setProperty("timestamp", timestamp);
      commentEntity.setProperty("userEmail", userEmail);

      if (imageUrl != null) {
        commentEntity.setProperty("imageUrl", imageUrl);
      } else {
        commentEntity.setProperty("imageUrl", "null");
      }
      datastore.put(commentEntity);
    }

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }
  /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
  private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    // User submitted form without selecting a file, so we can't get a URL. (dev server)
    if (blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // Our form only contains a single file input, so get the first index.
    BlobKey blobKey = blobKeys.get(0);

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    // Use ImagesService to get a URL that points to the uploaded file.
    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);

    // Use the relative path to the image.
    try {
      URL url = new URL(imagesService.getServingUrl(options));
      return url.getPath();
    } catch (MalformedURLException e) {
      return imagesService.getServingUrl(options);
    }
  }
}
