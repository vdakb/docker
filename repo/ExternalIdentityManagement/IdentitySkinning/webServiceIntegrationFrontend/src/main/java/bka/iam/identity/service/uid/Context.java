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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Services Integration

    File        :   Context.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    Context.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.08.2022  SBernet     First release version
*/

package bka.iam.identity.service.uid;

import bka.iam.identity.service.ServiceException;
import bka.iam.identity.service.rest.ServiceClient;
import bka.iam.identity.service.rest.ServiceResource;
import bka.iam.identity.service.uid.request.Register;
import bka.iam.identity.service.uid.request.Request;

import javax.ws.rs.client.WebTarget;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is service consumer specific context class that extends Jersey's client
 ** interface.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends ServiceClient {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type Unique Identifier provided by the Service Provider.
   */
  static final String     ENDPOINT_UID        = "uid";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the endpoint to access the Anonymous Identifier Generator API **/
  private final WebTarget target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   * Constructs a <code>Context</code> which is associated with the specified
   * {@link ServiceClient} for configuration purpose.
   *
   * @param loggable                the {@link Loggable} which has
   *                                instantiated this {@link ServiceClient}.
   *                                <br>
   *                                Allowed object is {@link Loggable}.  
   * @param endpoint                the {@link ServiceResource}
   *                                <code>IT Resource</code> instance where this
   *                                connector client is associated with.
   *                                <br>
   *                                Allowed object is {@link ServiceResource} .
   *
   * @throws TaskException          if the authentication/authorization process
   *                                fails.
   */
  private Context(final Loggable loggable, final ServiceResource endpoint)
    throws ServiceException {

    // ensure inheritance
    super(loggable, endpoint);

    // initialize instance attributes
    this.target = client.target(endpoint.contextURL());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   * Factory method to create a default context.
   *
   * @param loggable                the {@link Loggable} which has
   *                                instantiated this {@link ServiceClient}.
   *                                <br>
   *                                Allowed object is {@link Loggable}.
   * @param endpoint                the {@link ServiceResource}
   *                                <code>IT Resource</code> instance where this
   *                                connector client is associated with.
   *                                <br>
   *                                Allowed object is {@link ServiceResource} .
   *
   * @return                        a default context.
   *                                <br>
   *                                Possible object is <code>Context</code>.
   *
   * @throws ServiceException       if the authentication/authorization process
   *                                fails.
   */
  public static Context build(final Loggable loggable, final ServiceResource endpoint)
    throws ServiceException {
    return new Context(loggable, endpoint);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerUniqueIdentifier
  /**
   ** Build and executes the request to register the provided uid to the Service
   ** Provider.
   **
   ** @param  uid                the uid of the user to be register.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully registered uid.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ServiceException   if the REST service provider responded with an
   **                            error.
   */
  public String registerUniqueIdentifier(final String uid)
    throws ServiceException {

    final String method = "registerUniqueIdentifier";
    entering(method);
    try {
      return registerRequest(ENDPOINT_UID).invoke(uid);
    }
    finally {
      exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestUniqueIdentifier
  /**
   ** Build and executes the request to generate an uid from the Service
   ** Provider.
   **
   ** @param  participantType    the identifier of the 
   **                            <code>Participant Type</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the identifier of the <code>Country</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  state              the identifier of the <code>State</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  participant        the identifier of the <code>Participant</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  idType             the identifier of the <code>Type</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully registered uid.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ServiceException   if the REST service provider responded with an
   **                            error.
   */
  public String requestUniqueIdentifier(final String participantType, final String country, final String state, final String participant, final String idType)
    throws ServiceException {
    
    final String method = "requestUniqueIdentifier";
    entering(method);
    try {
      return generateRequest(ENDPOINT_UID).invoke(participantType, country, state, participant, idType);
    }
    finally {
      exiting(method);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerRequest
  /**
   ** Build a request to register the provided uid to the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>uid</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  protected Register registerRequest(final String context) {
    return Register.build(this.target.path(context)); 
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateRequest
  /**
   ** Build a request to request a uid to the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>uid</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  protected Request generateRequest(final String context) {
    return Request.build(this.target.path(context));
  }
}
