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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Generic REST Library

    File        :   DomainFeature.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DomainFeature.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package oracle.hst.platform.rest.plugin;

import java.io.IOException;

import javax.annotation.Priority;

import javax.ws.rs.Priorities;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientRequestContext;

///////////////////////////////////////////////////////////////////////////////
// class DomainFeature
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This feature injects the header required by Access Manager to know the
 ** <code>Identity Domain</code> the client belongs to on the context's.
 ** <p>
 ** The feature may be register programmatically like other features by calling
 ** any of {@link javax.ws.rs.core.Configurable} {@code register(...)} method,
 ** i.e. {@link javax.ws.rs.core.Configurable#register(Class)} or by setting any
 ** of the configuration property listed bellow.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DomainFeature implements Feature {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Key of the property that can be attached to the
   ** {@link javax.ws.rs.client.ClientRequestContext client request} using
   ** {@link javax.ws.rs.client.ClientRequestContext#setProperty(String, Object)}
   ** and that defines a domain name that should be used when generating OAuth
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
  public static final String PROPERTY_IDENTITY_DOMAIN = "ocs.platform.oauth.identity.domain";

  public static final String PROPERTY_IDENTITY_HEADER = "x-oauth-identity-domain-name";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Filter      filter;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////////////////
  // class Filter
  // ~~~~~ ~~~~~~
  /**
   ** Client filter that adds header values to the {@code DomainFeature} http
   ** header.
   ** <br>
   ** The filter uses <code>x-oauth-identity-domain-name</code> specification.
   */
  @Priority(Priorities.AUTHENTICATION)
  public class Filter implements ClientRequestFilter {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String domain;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Filter</code> initialized for the
     ** <code>Identity Domain</code> <code>domain</code>.
     **
     ** @param  domain             the Access Manager
     **                            <code>Identity Domain</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     */
    private Filter(final String domain) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.domain = domain;
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

      String domain   = this.domain;
      String override = (String)request.getProperty(PROPERTY_IDENTITY_DOMAIN);
      if (override != null) {
        request.removeProperty(PROPERTY_IDENTITY_DOMAIN);
        domain = override;
      }
      if (domain == null)
        return;

      if (!request.getHeaders().containsKey(PROPERTY_IDENTITY_HEADER)) {
        request.getHeaders().add(PROPERTY_IDENTITY_HEADER, domain);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DomainFeature</code> initialized for the header value.
   **
   ** @param  domain             the header value.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private DomainFeature(final String domain) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.filter = new Filter(domain);
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
   ** <code>domain</code> name that will add
   ** <code>x-oauth-identity-domain-name</code> http header to the request of a
   ** OAuth access token.
   **
   ** @param  domain             the <code>Identity Domain</code> the client
   **                            retrieving or refreshicng an access token
   **                            belongs to. In this case the token will have to
   **                            be set to create a token only using
   **                            {@link #PROPERTY_IDENTITY_DOMAIN} property.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the client feature.
   **                            <br>
   **                            Possible object is {@link Feature}.
   */
  public static Feature build(final String domain) {
    return new DomainFeature(domain);
  }
}