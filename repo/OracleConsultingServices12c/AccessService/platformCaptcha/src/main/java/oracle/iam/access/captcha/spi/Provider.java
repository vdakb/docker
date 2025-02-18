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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Provider.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Provider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.access.captcha.spi;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import oracle.iam.access.captcha.api.Endpoint;

import oracle.iam.config.captcha.Config;
import oracle.iam.config.captcha.type.Canvas;

import oracle.iam.config.captcha.ConfigService;

import oracle.iam.platform.core.captcha.Captcha;
import oracle.iam.platform.core.captcha.Challenge;

import oracle.iam.platform.core.captcha.color.Space;
import oracle.iam.platform.core.captcha.color.SingleColorFactory;

import oracle.iam.platform.core.captcha.filter.NullFilterFactory;

import oracle.iam.platform.core.captcha.canvas.SingleColorCanvasFactory;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~
/**
 ** A JAX-RS servive provider implementation servicing the Captcha endpoint.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("captcha")
@Consumes(MediaType.APPLICATION_JSON)
public class Provider implements Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Context
  private HttpServletRequest request;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Provider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Stream image file given a {@link Canvas} configuration and stores the
   ** {@link Captcha} challenge in the session context.
   **
   ** @return                    the image response send back to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response generate() {
    final Config       config    = ConfigService.config();
    final Challenge    challenge = Challenge.simple(
      SingleColorCanvasFactory.build(SingleColorFactory.build(Space.awt(255, 255, 255)))
    , SingleColorFactory.build(Space.awt(0, 0, 0))
    , NullFilterFactory.build()
    )
    .size(config.canvas().size())
    .margin(config.canvas().margin())
    ;
    final Captcha captcha = challenge.captcha();
    challenge(request, captcha.challenge());
    return Response.status(Response.Status.OK).entity(captcha.image()).type("image/png").build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify
  /**
   ** Verifies the challenge response of a client.
   **
   ** @param  request            the {@link HttpServletRequest} issued by the
   **                            container.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  payload            the {@link Captcha} challenge response posted
   **                            the client.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the response send back to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @POST
  public Response verify(final @Context HttpServletRequest request, final String payload) {
    final String challenge = challenge(request);
    if (challenge == null)
      return Response.status(Response.Status.EXPECTATION_FAILED).build();

    if (challenge.equalsIgnoreCase(payload))
      return Response.status(Response.Status.OK).build();
    else
      return Response.status(Response.Status.EXPECTATION_FAILED).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Stores the captcha challenge at session scope.
   **
   ** @param  request            the {@link HttpServletRequest}.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  challenge          the captcha challenge.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void challenge(final HttpServletRequest request, final String challenge) {
    final HttpSession session = request.getSession();
    session.setAttribute(Captcha.class.getName(), challenge);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Returns the captcha challenge stored at session scope.
   ** <p>
   ** After this call the sesion (if there is one) is invalid.
   **
   ** @param  request            the {@link HttpServletRequest}.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    the captcha challenge.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String challenge(final HttpServletRequest request) {
    final HttpSession session = request.getSession(false);
    if (session == null)
      return null;

    final String answer =(String)session.getAttribute(Captcha.class.getName());
    session.invalidate();
    return answer;
  }
}