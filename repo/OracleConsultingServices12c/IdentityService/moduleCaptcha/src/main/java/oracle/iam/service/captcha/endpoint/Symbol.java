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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Symbol.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Symbol.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.endpoint;

import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;

import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.service.captcha.symbol.Error;
import oracle.iam.service.captcha.symbol.Captcha;
import oracle.iam.service.captcha.symbol.Repository;

import oracle.iam.service.captcha.resources.SymbolBundle;

////////////////////////////////////////////////////////////////////////////////
// class Symbol
// ~~~~~ ~~~~~~
/**
 ** The service <code>Symbol</code> to render captcha symbols.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("symbol")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Symbol extends Abstract<Symbol> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Symbol</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Symbol() {
    // ensure inheritance
    super(Symbol.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Called by the server (via the service method) to allow this servlet to
   ** handle a GET request.
   **
   ** @param  request            the {@link HttpServletRequest} issued by a
   **                            client.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  count              the number of imagas to evaluate as the
   **                            challenge.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the JSON response send to the client.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @GET
  @Path("start/{count}")
  public Response start(final @Context HttpServletRequest request, final @PathParam("count") String count) {
    final String method = "start";
    entering(method);
    int option = Repository.OPTION_DEFAULT;
    try {
      option = Integer.parseInt(count);
    }
    catch (NumberFormatException e) {
      error(method, SymbolBundle.format("start.option.invalid", count, Repository.OPTION_DEFAULT));
      option = Repository.OPTION_DEFAULT;
    }
    final String             salt      = Digester.instance.randomUUID();
    final List<Captcha.Tile> tile      = Repository.instance.randomize(option, salt);
    // randomly pick a tile as the solution
    final Captcha.Tile       solution  = tile.get(Digester.instance.nextInt(option));
    final String             fieldName = Digester.instance.hash(Digester.instance.randomUUID(), salt);
    session(request, new Captcha(fieldName, solution.obfuscated(), tile));

    List<String> symbol = new ArrayList<>(tile.size());
    for(Captcha.Tile choice : tile) {
      symbol.add(choice.obfuscated());
    }
    final String            label     = SymbolBundle.string(String.format("captcha.image.%s", solution.name()));
    final Captcha.Challenge challenge = Captcha.challenge(fieldName, label, symbol);
    exiting(method);
    return Response.status(Response.Status.OK).entity(challenge).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Stream image file given an index in the session captcha images array
   **
   ** @param  request            the {@link HttpServletRequest} issued by a
   **                            client.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  index              the of the image in the session images array to
   **                            stream.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the image response send to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @GET
  @Path("image/{index}")
  public Response image(final @Context HttpServletRequest request, final @PathParam("index") int index) {
    return image(request, index ,"md");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Stream image file given an index in the session captcha images array
   **
   ** @param  request            the {@link HttpServletRequest} issued by a
   **                            client.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  index              the of the image in the session images array to
   **                            stream.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  size               <code>true</code> if the larger image is to be
   **                            streamed.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the image response send to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @GET
  @Path("image/{index}/size/{size}")
  public Response image(final @Context HttpServletRequest request, final @PathParam("index") int index, final @PathParam("size") String size) {
    final String method = "image";
    entering(method);

    final Captcha captcha = session(request);
    if (captcha == null)
      return Response.status(Response.Status.BAD_REQUEST).build();

    final List<Captcha.Tile> tile = captcha.tile();
    if (index >= tile.size())
      return Response.status(Response.Status.BAD_REQUEST).entity(Error.build("stream.request.invalid", String.valueOf(index))).type(MediaType.APPLICATION_JSON).build();

    // build the absolute path to the image resource
    final String      path   = Repository.instance.image() + size + "/" + tile.get(index).image();
    final InputStream source = this.context.getResourceAsStream(path);
    if (source == null)
      return Response.status(Response.Status.NOT_FOUND).entity(Error.build("stream.source.invalid")).type(MediaType.APPLICATION_JSON).build();
    try {
      return Response.status(Response.Status.OK).entity(new ByteArrayInputStream(Repository.instance.stream(source))).type("image/png").build();
    }
    catch (IOException e) {
      return Response.status(Response.Status.GONE).entity(Error.build("stream.source.invalid")).type(MediaType.APPLICATION_JSON).build();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session
  /**
   ** Stores the {@link Captcha} at session scope.
   **
   ** @param  request            the {@link HttpServletRequest}.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    the {@link Captcha} challenge/response.
   **                            <br>
   **                            Possible object is {@link Captcha}.
   */
  private void session(final HttpServletRequest request, final Captcha captcha) {
    request.getSession(true).setAttribute(Captcha.class.getName(), captcha);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session
  /**
   ** Returns the {@link Captcha} stored at session scope.
   **
   ** @param  request            the {@link HttpServletRequest}.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    the {@link Captcha} challenge/response.
   **                            <br>
   **                            Possible object is {@link Captcha}.
   */
  private Captcha session(final HttpServletRequest request) {
    return (Captcha)request.getSession(true).getAttribute(Captcha.class.getName());
  }
}