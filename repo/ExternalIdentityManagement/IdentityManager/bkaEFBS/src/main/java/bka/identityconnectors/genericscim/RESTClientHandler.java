/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   EFBS Connector

    File        :   EFBSSCIMConnector.java

    Compiler    :   JDK 1.8

    Author      :   richard.x.rutter@oracle.com

    Purpose     :   This custom connector implementation wraps the standard
                    Oracle Generic SCIM connector in order to fix the following
                    issues when intergating with the Rola EFBS SCIM service:

                    1. The SCIM specification defines the status attribute
                    'active' as optional. The Rola service defines this as
                    mandatory on create.
                    2. THe Rola SCIM service defines a csutom schema extension
                    called 'EFBSUSer'. The Standard SCIM connector does not
                    process this connector correctly, so requires a custom
                    schema parser to be created and invoked by the connector
                    class (this class) when initilizing its configuration.

                    Note the Rola SCIM service also does not correctly handle
                    PATCH replace operations for extension schemas. Some code
                    has been included in this class which could be used to
                    support replacement of the PATCH replace operations with
                    PUT operations in the event that the Rola Service cannot
                    be fixed. This code is commented out and is not fully
                    completed and tested.

                    Note also that the Rola SCIM service does not
                    support PATCH remove of multi-valued attributes (child tables)
                    and substitutes PATCH add operations with PATCH replace.
                    While this is not ideal it can be tolerated in our case
                    if child table delete operations are implemented as no-ops
                    and if only one child table entry is permitted.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  rirutter    First release version
*/
package bka.identityconnectors.genericscim;

import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;

import org.apache.http.util.EntityUtils;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.impl.client.CloseableHttpClient;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import org.identityconnectors.common.logging.Log;

import org.identityconnectors.restcommon.ClientHandler;

import org.identityconnectors.restcommon.utils.RESTCommonUtils;
import org.identityconnectors.restcommon.utils.MessageLocalizer;
import org.identityconnectors.restcommon.utils.RESTCommonConstants;

public class RESTClientHandler extends ClientHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Log         log         = Log.getLog(ClientHandler.class);
  private static final ContentType contentScim = ContentType.create("application/scim+json", StandardCharsets.UTF_8);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RESTClientHandler</code> connector wrapping the
   ** standard <code>GenericSCIMConnector</code>.
   ** <br>
   ** Zero argument constructor required by the connector framework.
   ** <br>
   ** Default Constructor
   */
  public RESTClientHandler() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeRequest
  public static String executeRequest(final CloseableHttpClient httpClient, final String url, final RESTCommonConstants.HTTPOperationType operation, final String payload) {
    log.ok("Making a \"{0}\" call using the URL \"{1}\"", new Object[] {operation, url});
    CloseableHttpResponse response = null;
    HttpRequestBase       request  = null;
    try {
      switch (operation) {
        case GET    : request = new HttpGet(url);
                      break;
        case PUT    : request = new HttpPut(url);
                      // build a custom HttpEntity to enforce UTF-8 and satisfy SCIM requirements
                      ((HttpPut)request).setEntity(new StringEntity(payload, contentScim));
                      break;
        case POST   : request = new HttpPost(url);
                      // build a custom HttpEntity to enforce UTF-8 and satisfy SCIM requirements
                      ((HttpPost)request).setEntity(new StringEntity(payload, contentScim));
                      break;
        case PATCH  : request = new HttpPatch(url);
                      // build a custom HttpEntity to enforce UTF-8 and satisfy SCIM requirements
                      ((HttpPatch)request).setEntity(new StringEntity(payload, contentScim));
                      break;
        case DELETE : request = new HttpDelete(url);
                      break;
      }

      String       jsonResp = null;
      response = httpClient.execute(request);
      if (response.getEntity() != null) {
        final HttpEntity  entity      = response.getEntity();
        final ContentType contentType = ContentType.get(entity);
        log.ok("Got a \"{0}\" encoded response with length {1} for mimeType {2}", new Object[] {contentType == null ? "null" : contentType.getCharset(), entity.getContentLength(), contentType.getMimeType()});

        Charset charSet = contentType == null || (contentType != null && contentScim.getMimeType().equals(contentType.getMimeType()) ) ? StandardCharsets.UTF_8 : contentType.getCharset();
        // if it wasn't possible to guess the charset fallback to http default which is ISO-8859-1
        if (charSet == null)
          charSet = StandardCharsets.ISO_8859_1;
        log.ok("Using charSet \"{0}\" for encoding", new Object[] {charSet.name()});
        jsonResp = EntityUtils.toString(entity, charSet);
      }
      RESTCommonUtils.checkStatus(response, jsonResp);
      return jsonResp;
    }
    catch (IOException e) {
      final String message = MessageLocalizer.getMessage("ex.restCallInternalFailure", "Error occurred while executing a {0} REST call on the target. {1}", new Object[] {operation, e.getLocalizedMessage()});
      log.error(message);
      throw new ConnectorException(message, e);
    }
    finally {
      if (request != null) {
        request.releaseConnection();
      }
      if (response != null)
        try {
          response.close();
        }
        catch (IOException e) {
          final String message = MessageLocalizer.getMessage("ex.resourceCloseFailure", "Failed to close the resource CloseableResponse. {0}", new Object[] {e.getLocalizedMessage()});
          log.error(message);
          throw new ConnectorException(message, e);
        }
    }
  }
}