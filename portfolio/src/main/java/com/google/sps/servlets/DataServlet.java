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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException { 
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        List<String> comments = new ArrayList<String>();
        for (Entity entity : results.asIterable()) {
            String comment = (String) entity.getProperty("comment");
            comments.add(comment);
        }

        response.setContentType("application/json");
        response.getWriter().println(toJSONString(comments));
    }

    /*
    * Helper function to store comments in JSON format
    */
    private String toJSONString(List<String> array) {
            String json = "{ \"comments\" :" + "\""+ array.toString() + "\""+ "}";
            return json;
        }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the input from the form.
        String feedback = request.getParameter("text-input");
        long timestamp = System.currentTimeMillis();
        
        // Only add feedback if valid input
        if (feedback == "") {
            System.err.println("No input");
            return;
        }

        else {
            Entity commentEntity = new Entity("Comment");
            
            commentEntity.setProperty("comment", feedback);
            commentEntity.setProperty("timestamp", timestamp);
            
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(commentEntity);
        }

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");
  }
}
