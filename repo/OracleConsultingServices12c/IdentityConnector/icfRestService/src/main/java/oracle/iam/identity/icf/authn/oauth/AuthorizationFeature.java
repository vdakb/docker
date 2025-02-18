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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic REST Library

    File        :   AuthorizationFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AuthorizationFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.authn.oauth;

import java.io.IOException;

import javax.annotation.Priority;

import javax.ws.rs.Priorities;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.FeatureContext;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientRequestContext;

///////////////////////////////////////////////////////////////////////////////
// class AuthorizationFeature
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** OAuth client filter feature registers the support for performing
 ** authenticated requests to the Service Provider.
 ** <br>
 ** The feature does not perform Authorization Flow (see AuthorizationFlow for
 ** details how to use Authorization Flow and retrieve Access Token). The
 ** feature uses access to initialize the internal
 ** {@link javax.ws.rs.container.ContainerRequestFilter filter} which will add
 ** <code>Authorization</code> http header containing OAuth authorization
 ** information including (based on <code>bearer</code> tokens).
 ** <p>
 ** The internal filter can be controlled by properties put into the
 ** {@link javax.ws.rs.client.ClientRequestContext client request} using
 ** {@link javax.ws.rs.client.ClientRequestContext#setProperty(String, Object)}
 ** method. The property key is defined in this class as a static variables
 ** ({@link #PROPERTY_ACCESS_TOKEN} (see its javadoc for usage).
 ** <br>
 ** Using the property a specific access token can be defined for each request.
 ** <p>
 ** Example of using specific access token for one request:
 ** <pre>
 **   final Response response = client.target("foo")
 **     .request()
 **     .property(PROPERTY_ACCESS_TOKEN, "6ab45ab465e46f54d771a")
 **     .get();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AuthorizationFeature implements Feature {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Key of the property that can be attached to the
   ** {@link javax.ws.rs.client.ClientRequestContext client request} using
   ** {@link javax.ws.rs.client.ClientRequestContext#setProperty(String, Object)}
   ** and that defines access token that should be used when generating OAuth
   ** <code>Authorization</code> http header.
   ** <br>
   ** The property will override the setting of the internal
   ** {@link javax.ws.rs.client.ClientRequestFilter filter} for the current
   ** request only. This property can be used only when
   ** {@link javax.ws.rs.core.Feature OAuth 2 filter feature} is registered into
   ** the {@link javax.ws.rs.client.Client} instance.
   ** <p>
   ** The value of the property must be a {@link String}.
   */
  public static final String PROPERTY_ACCESS_TOKEN = "ocs.connector.oauth.access.token";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Filter filter;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////////////////
  // class Filter
  // ~~~~~ ~~~~~~
  /**
   ** Client filter that adds access token to the {@code Authorization} http
   ** header.
   ** <br>
   ** The filter uses <code>bearer</code> token specification.
   */
  @Priority(Priorities.AUTHENTICATION)
  public class Filter implements ClientRequestFilter {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String token;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Filter</code> initialized for the access token.
     **
     ** @param  token              the access token.
     **                            <br>
     **                            Allowed object is {@link String}.
     */
    private Filter(final String token) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.token = token;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (ClientRequestFilter)
    /**
     ** Filter method called before a request has been dispatched to a client
     ** transport layer.
     ** <p>
     ** Filters in the filter chain are ordered according to their
     ** {@link Priority} class-level annotation value.
     **
     ** @param  request            the request context.
     **
     ** @throws IOException        if an I/O exception occurs.
     */
    @Override
    public void filter(final ClientRequestContext request)
      throws IOException {

      String       token    = this.token;
      final String override = (String)request.getProperty(PROPERTY_ACCESS_TOKEN);
      if (override != null) {
        token = override;
      }
      request.removeProperty(PROPERTY_ACCESS_TOKEN);
      if (token == null)
        return;

      if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthorizationFeature</code> initialized for the access
   ** token.
   **
   ** @param  token              the access token.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private AuthorizationFeature(final String token) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.filter = new Filter(token);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (Feature)
  /**
   ** A call-back method called when the feature is to be enabled in a given
   ** runtime configuration scope. The responsibility of the feature is to
   ** properly update the supplied runtime configuration context and return
   ** <code>true</code> if the feature was successfully enabled or
   ** <code>false</code> otherwise.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Under some circumstances the feature may decide not to enable itself,
   ** which is indicated by returning <code>false</code>. In such case the
   ** configuration context does not add the feature to the collection of
   ** enabled features and a subsequent call to Configuration.isEnabled(Feature)
   ** or Configuration.isEnabled(Class) method would return <code>false</code>.
   **
   ** @param  context            the configurable context in which the feature
   **                            should be enabled.
   **
   ** @return                    <code>true</code> if the feature was
   **                            successfully enabled, <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean configure(final FeatureContext context) {
    context.register(this.filter);
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Build the {@link Feature client filter feature} from the
   ** <code>accessToken</code> that will add <code>Authorization</code> http
   ** header to the request with the OAuth authorization information.
   **
   ** @param  token              the access token to be used in the
   **                            authorization header or <code>null</code> if no
   **                            default access token should be defined. In this
   **                            case the token will have to be set for each
   **                            request using {@link #PROPERTY_ACCESS_TOKEN}
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Token}.
   **
   ** @return                    the client feature.
   */
  public static Feature build(final Token token) {
    return new AuthorizationFeature(token.accessToken());
  }
}