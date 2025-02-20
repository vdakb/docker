/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ServiceClient.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class ServiceClient.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-11-26  SBernet     First release version
*/
package bka.iam.identity.scim.extension.rest;

import bka.iam.identity.scimold.extension.utils.WLSUtil;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import oracle.hst.foundation.logging.AbstractLoggable;
import oracle.hst.foundation.logging.Loggable;
import oracle.hst.platform.core.SystemException;

import org.glassfish.jersey.client.ClientProperties;
///////////////////////////////////////////////////////////////////////////////
// class ServiceClient
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This class provides an abstraction for connecting over HTTP Client
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ServiceClient extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // The Jersey client instance used for HTTP operations.
  protected final Client          client;
  
  // The base URL for the SCIM service.
  protected final String          serviceURL;
  
  // Connection timeout in milliseconds.
  private static Integer          TIMEOUT_CONNECT  = 150000;
  
  // Response timeout in milliseconds.
  private static Integer          TIMEOUT_RESPONSE = 150000;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceClient</code> instance for managing SCIM services.
   **
   ** @param loggable        The logging component used for managing logs.
   **                        Allowed object is {@link Loggable}.
   */
  protected ServiceClient(final Loggable loggable) {
    // ensure inheritance
    super(loggable);

    this.serviceURL = WLSUtil.getOIMUrl();
    this.client     = createClient(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Returns the {@link Client} this context is using to connect and perform
   ** operations on the Service Provider.
   **
   ** @return                    the {@link Client} this context is using to
   **                            connect and perform operations on the Service
   **                            Provider.
   **                            <br>
   **                            Possible object {@link Client}.
   */
  public final Client client() {
    return this.client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Web Service.
   ** <p>
   ** The URL consists of the server part of the HTTP url, http(s)://host:port
   ** and the absolute path to the resource. The entry is post fixed with the
   ** context root of the connection.
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the <code>IT Resource</code> definition associated with this
   ** instance.
   **
   ** @return                    the context URl the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String contextURL() {
    final StringBuilder context = new StringBuilder(serviceURL());
    if (this.rootContext() != null) {
      if (context.charAt(context.length() - 1) != '/' && this.rootContext().charAt(0) != '/')
        context.append('/');
      context.append(this.rootContext());
    }
    return context.toString();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server parts of the HTTP url, http(s)://host:port
   **
   ** @return                    the server part of the HTTP url,
   **                            http(s)://host:port
   */
  public String serviceURL() {
    // lazy initialization of the configured service URL's
    return serviceURL;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Retrieve the root context path of the SCIM service.
   **
   ** @return                The root context path for SCIM services.
   **                        Possible object is {@link String}.
   */
  public abstract String rootContext();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Factory method to build a new web resource target.
   **
   ** @param  uri                the web resource {@link URI}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the web resource target bound to the provided
   **                            {@link URI}.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public WebTarget target(final String uri) {
    return this.client.target(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Factory method to build a new web resource target.
   **
   ** @param  uri                the web resource {@link URI}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the web resource target bound to the provided
   **                            {@link URI}.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public WebTarget target(final URI uri) {
    return this.client.target(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createClient
  /**
   ** Factory method to create the Jersey {@link Client} instance.
   ** <p>
   ** The property {@link ClientProperties#SUPPRESS_HTTP_COMPLIANCE_VALIDATION}
   ** is necessary to skip the regular validation of the HTTP method performed
   ** by JAX-RS. This hack is required for services that are not RFC compliant
   ** such as: B. <i>Red Hat Keycloack</i>. For example, such services expect a
   ** payload in a <code>DELETE</code> method that is usually not permitted.
   **
   ** @param  compliant          <code>true</code> to keep the validation of the
   **                            HTTP method performed by JAX-RS, as it is
   **                            out-of-the-box; otherwise <code>false</code> to
   **                            skip the validation.
   **
   ** @return                    a fully-configured Jersey {@link Client}.
   **                            <br>
   **                            Possible object is {@link Client}.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  private Client createClient(final boolean compliant) {

    final ClientBuilder builder  = ClientBuilder.newBuilder()
      .property(ClientProperties.CONNECT_TIMEOUT, TIMEOUT_CONNECT)
      .property(ClientProperties.READ_TIMEOUT,    TIMEOUT_RESPONSE)
       // violating our own rules that only standard
      .property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, !compliant);
    
    // register the logging feature as the last one
    builder.register(new LoggerFeature(this, LoggerFeature.Verbosity.PAYLOAD_ANY));
    return builder.build();
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   buildTarget
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   ** <p>
   ** Create a JAX-RS web target whose URI refers to the
   ** <code>ServiceEndpoint.contextURL()</code> the JAX-RS / Jersey application
   ** is deployed at.
   ** <p>
   ** This method is an equivalent of calling
   ** <code>client().target(endpoint.contextURL())</code>.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  protected WebTarget buildTarget() {
    return this.client.target(this.contextURL());
  }
}