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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/delete-data")
public class DeleteDataServlet extends HttpServlet {
  /** Delete comment servlet, allow a user to delete their own comments. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the key of which comment to delete.
    Key key = KeyFactory.stringToKey(request.getParameter("id"));
    String commentUserEmail = request.getParameter("userEmail");

    // Get the email of the user currently logged in.
    UserService userService = UserServiceFactory.getUserService();
    String currentUserEmail = null;
    if (userService.getCurrentUser() != null) {
      currentUserEmail = userService.getCurrentUser().getEmail();
    }

    // Users can only delete their own comments.
    if (currentUserEmail == null || !commentUserEmail.equals(currentUserEmail)) {
      response.getWriter().println("<html><body>");
      response.getWriter().println("<script type=\"text/javascript\">");
      response.getWriter().println("alert('You can only delete your own comments');");
      response.getWriter().println("window.location.href = 'index.html'");
      response.getWriter().println("</script>");
      return;
    } else if (key != null) {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.delete(key);
    }
    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }
}
