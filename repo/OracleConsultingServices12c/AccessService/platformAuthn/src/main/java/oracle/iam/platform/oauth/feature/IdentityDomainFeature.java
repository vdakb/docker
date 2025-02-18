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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityDomainFeature.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityDomainFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.feature;

import java.io.IOException;

import javax.annotation.Priority;

import javax.ws.rs.Priorities;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientRequestContext;

///////////////////////////////////////////////////////////////////////////////
// class IdentityDomainFeature
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** OAuth client filter feature registers the support for performing
 ** token requests to the Authorization Server.
 ** <br>
 ** The feature does not perform Authorization Flow (see AuthorizationFlow for
 ** details how to use Authorization Flow and retrieve Access Token). The
 ** feature uses access to initialize the internal
 ** {@link javax.ws.rs.container.ContainerRequestFilter filter} which will add
 ** <code>X-OAUTH-IDENTITY-DOMAIN-NAME</code> http header containing identity
 ** domain information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityDomainFeature implements Feature {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String IDENTITY_DOMAIN_HEADER   = "X-OAUTH-IDENTITY-DOMAIN-NAME";

  /**
   ** Key of the property that can be attached to the
   ** {@link javax.ws.rs.client.ClientRequestContext client request} using
   ** {@link javax.ws.rs.client.ClientRequestContext#setProperty(String, Object)}
   ** and that defines access token that should be used when generating OAuth
   ** <code>IdentityDomain</code> http header.
   ** <br>
   ** The property will override the setting of the internal
   ** {@link javax.ws.rs.client.ClientRequestFilter filter} for the current
   ** request only. This property can be used only when
   ** {@link javax.ws.rs.core.Feature OAuth 2 filter feature} is registered into
   ** the {@link javax.ws.rs.client.Client} instance.
   ** <p>
   ** The value of the property must be a {@link String}.
   */
  public static final String IDENTITY_DOMAIN_PROPERTY = "ocs.oauth.identity.domain";

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

    private final String tenant;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Filter</code> initialized for the identity domain.
     **
     ** @param  token              the identity domain.
     **                            <br>
     **                            Allowed object is {@link String}.
     */
    private Filter(final String tenant) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.tenant = tenant;
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

      String       tenant   = this.tenant;
      final String override = (String)request.getProperty(IDENTITY_DOMAIN_PROPERTY);
      if (override != null) {
        tenant = override;
      }
      request.removeProperty(IDENTITY_DOMAIN_PROPERTY);
      if (tenant == null)
        return;

      if (!request.getHeaders().containsKey(IDENTITY_DOMAIN_HEADER)) {
        request.getHeaders().add(IDENTITY_DOMAIN_HEADER, tenant);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityDomainFeature</code> initialized for the access
   ** token.
   **
   ** @param  tenant             the identity domain.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private IdentityDomainFeature(final String tenant) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.filter = new Filter(tenant);
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
  // Method:   of
  /**
   ** Build the {@link Feature client filter feature} from the
   ** <code>accessToken</code> that will add <code>Authorization</code> http
   ** header to the request with the OAuth authorization information.
   **
   ** @param  domain           the Identity Domain under which the token is
   **                          being requested.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the client feature.
   **                          <br>
   **                          Possible object is {@link Feature}.
   */
  public static Feature of(final String domain) {
    return new IdentityDomainFeature(domain);
  }
}