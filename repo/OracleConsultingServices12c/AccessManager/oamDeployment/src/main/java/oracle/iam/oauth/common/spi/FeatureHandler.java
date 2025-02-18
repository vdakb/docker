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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   FeatureHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;

import java.net.HttpURLConnection;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;

import oracle.hst.foundation.json.simple.JsonObject;
import oracle.hst.foundation.json.simple.JsonContext;

import oracle.hst.deployment.type.APIServerContext;

import oracle.hst.deployment.spi.AbstractRESTfulHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureInstance
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>FeatureHandler</code> provides common task to manage OAuth artifacts
 ** in Oracle Access Manager that might be created, deleted or configured during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FeatureHandler extends AbstractRESTfulHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String>          flatten  = new HashSet<String>();


  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DomainHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  contextURI         the URI of the REST API.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  protected FeatureHandler(final ServiceFrontend frontend, final String contextURI, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend, contextURI, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonSerialize
  /**
   ** Serialize the given {@link Map} <code>payload</code> to a
   ** {@link JsonObject}.
   **
   ** @param  payload            the {@link Map} to serialize.
   **
   ** @return                    a {@link JsonObject}.
   */
  protected static JsonObject jsonSerialize(final Map<String, Object> payload) {
    final JsonObject json = (payload != null && payload.size() > 0) ? JsonContext.builder().objectBuilder().build() : null;
    return jsonSerialize(json, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonSerialize
  /**
   ** Serialize the given {@link Map} <code>payload</code> to the given
   ** {@link JsonObject}.
   **
   ** @param  json               the {@link JsonObject} the provided value pairs
   **                            needs to be serialized to.
   ** @param  payload            the {@link Map} to serialize.
   **
   ** @return                    a {@link JsonObject}.
   */
  protected static JsonObject jsonSerialize(final JsonObject json, final Map<String, Object> payload) {
    if (json != null && payload != null && payload.size() > 0) {
      for (Map.Entry<String, Object> cursor : payload.entrySet()) {
        final Object value = cursor.getValue();
        json.add(cursor.getKey(), (value == null) ? null : value.toString());
      }
    }
    return json;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Factory method to create a {@link HttpURLConnection} for the purpose of
   ** form-urlencoded requests.
   **
   ** @param  context            the {@link APIServerContext} providing
   **                            information how to locate the requested
   **                            resources and the credentials required to
   **                            authenticate.
   ** @param  requestMethod      the HTTP method.
   **                            <ul>
   **                              <li>GET
   **                              <li>DELETE
   **                            </ul>
   **                            are legal, subject to protocol restrictions.
   **
   ** @return                    a common {@link HttpURLConnection}.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  protected HttpURLConnection request(final APIServerContext context, final String requestMethod, final FeatureInstance instance)
    throws ServiceException {

    HttpURLConnection request = null;
    if (StringUtility.isEmpty(instance.id()))
      request = connection(context, requestMethod, DomainProperty.NAME.id(), instance.name());
    else
      request = connection(context, requestMethod, DomainProperty.NAME.id(), instance.id());
    return request;
  }
}