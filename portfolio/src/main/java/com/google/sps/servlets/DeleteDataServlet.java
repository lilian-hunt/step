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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {
  private final static Logger LOGGER = Logger.getLogger(DataServlet.class.getName());

  // To do: determine if this is the user's comment
  // Allow them to delete their own comments
  // Need something to identify the comment
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the info about which post to delete
    String feedback = request.getParameter("text-input");
    String key = request.getParameter("id");
    key = key.replaceAll("[^\\d.]", "");
    System.out.println(key);

    String remoteAddr = request.getRemoteAddr();
    Filter identifyUser = new FilterPredicate("remoteAddr", FilterOperator.EQUAL, remoteAddr);
    // Filter based on key
    Filter identifyComment = new FilterPredicate("key", FilterOperator.EQUAL, key);
    // Query query = new Query("Comment").setFilter(identifyUser).addSort("timestamp",
    // SortDirection.DESCENDING);
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    System.out.println("**" + results);
    // Only add feedback if valid input
    if (results == null) {
      LOGGER.warning("User does not own this comment");
      return;
    }

    else {
      for (Entity commentEntity : results.asIterable()) {
        // datastore.delete(commentEntity);

        // remote address is returning null -- need to fix then can deletegit
        String comment = (String) commentEntity.getProperty("comment");
        System.out.println("Key: " + commentEntity.getKey());
        System.out.println("Remote addr: " + commentEntity.getProperty("remoteAddr"));
        System.out.println("COMMENT: " + comment);
      }
      System.out.println("deleted comment");
    }

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }
}
