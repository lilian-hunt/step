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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
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
}
