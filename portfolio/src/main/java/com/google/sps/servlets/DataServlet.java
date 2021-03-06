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
import com.google.gson.Gson;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;


import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<String> quotes;

  @Override
  public void init() {
    quotes = new ArrayList<>();
  }
  private class Comment {
    String text;
    String email;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String text = (String) entity.getProperty("text");
      String email = (String) entity.getProperty("email");
      Comment comment = new Comment();
      comment.text = text;
      comment.email = email;
      comments.add(comment);
    }
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    response.setContentType("text/html;");
    response.getWriter().println(json);
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String userEmail;
    if (userService.isUserLoggedIn()) {
      userEmail = userService.getCurrentUser().getEmail();
    } else {
      userEmail = "";
    }
    // Get the input from the form.
    String text = getParameter(request, "text-input", "");
    long timestamp = System.currentTimeMillis();

    Entity taskEntity = new Entity("Comment");
    taskEntity.setProperty("text", text);
    taskEntity.setProperty("timestamp", timestamp);
    taskEntity.setProperty("email", userEmail);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);
    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }


  private String convertToJsonUsingGson(ArrayList<String> quotes) {
    Gson gson = new Gson();
    String json = gson.toJson(quotes);
    return json;
  }
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

}