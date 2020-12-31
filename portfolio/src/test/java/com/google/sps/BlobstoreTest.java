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

package com.google.sps;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.sps.servlets.BlobstoreUploadUrlServlet;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.util.List;
import org.junit.Assert;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.inject.Injector;
import com.google.inject.Guice;
import com.google.inject.AbstractModule; 
@RunWith(JUnit4.class)
public final class BlobstoreTest {
  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalBlobstoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
  @Test
  public void correstPhotoUrl() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    // HttpServletRequest request = mock(HttpServletRequest.class);
    // HttpServletResponse response = mock(HttpServletResponse.class);

    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(BlobstoreService.class).toInstance(mock(BlobstoreService.class));
      }
    });

    BlobstoreUploadUrlServlet servlet = injector.getInstance(BlobstoreUploadUrlServlet.class);
    servlet.doGet(request, response);

    Assert.assertEquals("text/html", response.getContentType());

  }
}
