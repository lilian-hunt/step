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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private final static Logger LOGGER = Logger.getLogger(DataServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get all the comments from the database
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    HashMap<String, ArrayList<String>> comments = new HashMap<String, ArrayList<String>>();
    for (Entity entity : results.asIterable()) {
      String id = entity.getKey().toString();
      ArrayList<String> commentEmail = new ArrayList<String>();
      commentEmail.add((String) entity.getProperty("comment"));
      commentEmail.add((String) entity.getProperty("userEmail"));
      comments.put(id, commentEmail);
    }

    response.setContentType("application/json");
    response.getWriter().println(toJSONString(comments, "comments"));
  }

  /*
   * Helper function to store an array in JSON format
   */
  private String toJSONString(Map<String, ArrayList<String>> map, String name) {
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
    long timestamp = System.currentTimeMillis();
    UserService userService = UserServiceFactory.getUserService();
    String userEmail = userService.getCurrentUser().getEmail();

    // Only add feedback if valid input
    if (feedback == "") {
      LOGGER.warning("No input");
      return;
    } else {
      Entity commentEntity = new Entity("Comment");

      commentEntity.setProperty("comment", feedback);
      commentEntity.setProperty("timestamp", timestamp);
      commentEntity.setProperty("userEmail", userEmail);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
    }

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }
}
