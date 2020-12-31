// // Copyright 2019 Google LLC
// //
// // Licensed under the Apache License, Version 2.0 (the "License");
// // you may not use this file except in compliance with the License.
// // You may obtain a copy of the License at
// //
// //     https://www.apache.org/licenses/LICENSE-2.0
// //
// // Unless required by applicable law or agreed to in writing, software
// // distributed under the License is distributed on an "AS IS" BASIS,
// // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// // See the License for the specific language governing permissions and
// // limitations under the License.

// package com.google.sps;

// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;

// import com.google.sps.servlets.DataServlet;
// import java.io.PrintWriter;
// import java.io.StringWriter;
// import java.util.List;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import org.junit.Assert;
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.junit.runners.JUnit4;
// import org.springframework.mock.web.MockHttpServletRequest;
// import org.springframework.mock.web.MockHttpServletResponse;
// /** */
// @RunWith(JUnit4.class)
// public final class DatastoreTest {
//   private DataServlet servlet;
//   private MockHttpServletRequest request;
//   private MockHttpServletResponse response;

//   @Before
//   public void setUp() {
//     servlet = new DataServlet();
//     request = new MockHttpServletRequest();
//     response = new MockHttpServletResponse();
//   }

//   @Test
//   public void correctUsernameInRequest() throws Exception {
//     when(request.getParameter("username")).thenReturn("me");
//     when(request.getParameter("password")).thenReturn("secret");

//     StringWriter sw = new StringWriter();
//     PrintWriter pw = new PrintWriter(sw);

//     when(response.getWriter()).thenReturn(pw);
//     servlet.doPost(request, response);

//     String result = sw.getBuffer().toString().trim();
//     System.out.println(result);
//     Assert.assertEquals("application/json", response.getContentType());
//   }
// }
