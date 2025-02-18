/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information"). You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2025. All Rights reserved.

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   HeaderFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class HeaderFilter.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-21-01  SBernet     First release version
*/

package bka.iam.identity.scim.extension.spi.requestfilter;

import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.spi.AbstractEndpoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import javax.ws.rs.container.PreMatching;

/**
 ** Filter implementation for adding headers to incoming HTTP requests.
 ** <p>
 ** This filter intercepts all requests and ensures the `X-Requested-By` header
 ** is set. If the header is missing, it extracts the username from the `Authorization`
 ** header and sets it as the value of `X-Requested-By`.
 ** </p>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@PreMatching
@WebFilter("/*")
public class HeaderFilter extends AbstractEndpoint implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@code HeaderFilter} instance.
   ** <p>
   ** Default constructor required by the servlet framework.
   ** </p>
   */
  public HeaderFilter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doFilter
  /**
   ** Filters incoming HTTP requests to ensure the `X-Requested-By` header is
   ** present.
   **
   ** @param request  the incoming {@link ServletRequest}.
   ** @param response the outgoing {@link ServletResponse}.
   ** @param chain    the {@link FilterChain} for passing control to the next filter.
   **
   ** @throws IOException      if an I/O error occurs during filtering.
   ** @throws ServletException if a servlet-specific error occurs.
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
  throws IOException, ServletException {
    final String method = "filter";
    this.entering(method);

    if (request instanceof HttpServletRequest) {
      HeaderWrapper wrappedRequest = new HeaderWrapper((HttpServletRequest) request);

      this.error(method, wrappedRequest.getHeader(HTTPContext.X_REQUESTED_BY_HEADER));

      // Add the X-Requested-By header if it's missing
      if (wrappedRequest.getHeader(HTTPContext.X_REQUESTED_BY_HEADER) == null) {
        String remoteUser = getUserNameFromAuthorizationHeader(wrappedRequest.getHeader("Authorization"));
        this.error(method, remoteUser);
        wrappedRequest.addHeader(HTTPContext.X_REQUESTED_BY_HEADER, remoteUser);
      }

      this.error(method, wrappedRequest.getHeader(HTTPContext.X_REQUESTED_BY_HEADER));

      // Pass the wrapped request to the next filter in the chain
      chain.doFilter(wrappedRequest, response);
    } else {
      chain.doFilter(request, response);
    }

    this.exiting(method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init
  /**
   ** Initializes the filter.
   **
   ** @param filterConfig the {@link FilterConfig} for this filter.
   **
   ** @throws ServletException if an error occurs during initialization.
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    final String method = "init";
    this.entering(method);
    // Nothing to initialize
    this.exiting(method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destroy
  /**
   ** Destroys the filter.
   ** <p>
   ** This method is invoked by the servlet container to indicate that this filter
   ** is being taken out of service.
   ** </p>
   */
  @Override
  public void destroy() {
    final String method = "destroy";
    this.entering(method);
    // Nothing to clean up
    this.exiting(method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserNameFromAuthorizationHeader
  /**
   ** Extracts the username from the `Authorization` header.
   **
   ** @param authorizationHeader the `Authorization` header value.
   **
   ** @return the extracted username, or "anonymous" if extraction fails.
   */
  private String getUserNameFromAuthorizationHeader(final String authorizationHeader) {
    if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
      // Remove "Basic " prefix
      String base64Credentials = authorizationHeader.substring(6);

      // Decode Base64 to get the username:password pair
      byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
      String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

      // Split the credentials into username and password
      int colonIndex = credentials.indexOf(':');
      if (colonIndex > 0) {
        return credentials.substring(0, colonIndex);
      }
    }
    return "anonymous";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Inner Class: HeaderWrapper
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A wrapper class for adding custom headers to HTTP requests.
   **/
  private class HeaderWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> headers = new HashMap<>();
    private final byte[] requestBody;

    /**
     ** Constructs a new {@code HeaderWrapper} instance.
     **
     ** @param request the original {@link HttpServletRequest}.
     */
    public HeaderWrapper(HttpServletRequest request)
      throws IOException {
      super(request);
      
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int bytesRead;
      try (InputStream inputStream = request.getInputStream()) {
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
      }
      this.requestBody = byteArrayOutputStream.toByteArray();
    }

    /**
     ** Adds a custom header.
     **
     ** @param name  the name of the header.
     ** @param value the value of the header.
     */
    public void addHeader(String name, String value) {
      headers.put(name, value);
    }

    /**
     ** Retrieves the value of a header.
     **
     ** @param name the name of the header.
     **
     ** @return the header value, or {@code null} if not found.
     */
    @Override
    public String getHeader(String name) {
      String headerValue = headers.get(name);
      if (headerValue != null) {
        return headerValue;
      }
      return super.getHeader(name);
    }

    /**
     ** Retrieves all header names.
     **
     ** @return an {@link Enumeration} of header names.
     */
    @Override
    public Enumeration<String> getHeaderNames() {
      Set<String> combinedHeaders = new HashSet<>(headers.keySet());
      Enumeration<String> originalHeaderNames = super.getHeaderNames();
      while (originalHeaderNames.hasMoreElements()) {
        combinedHeaders.add(originalHeaderNames.nextElement());
      }
      return Collections.enumeration(combinedHeaders);
    }
    
    @Override
    public ServletInputStream getInputStream() {
      if ("DELETE".equalsIgnoreCase(super.getMethod())) {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() {
                return -1; // End of stream
            }
        };
    }
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
      return new ServletInputStream() {
        @Override
        public boolean isFinished() {
          return byteArrayInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
          return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
          throw new UnsupportedOperationException();
        }

        @Override
        public int read() {
          return byteArrayInputStream.read();
        }
      };
    }
  }
}