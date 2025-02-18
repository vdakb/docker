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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   JEE Server

    File        :   Server.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Server.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.uid.service;

import java.io.IOException;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

////////////////////////////////////////////////////////////////////////////////
// class Server
// ~~~~~ ~~~~~~
/**
 ** Provides a simple high-level HTTP server API, which can be used to build
 ** embedded HTTP servers.
 ** <p>
 ** This class implements a simple HTTP server.
 ** <br>
 ** A {@link HttpServer} is bound to an IP address and port number and listens
 ** for incoming TCP connections from clients on this address.
 ** <br>
 ** The sub-class <code>HttpsServer</code> implements a server which handles
 ** HTTPS requests.
 ** <p>
 ** One or more <code>HttpHandler</code> objects are be associated with the
 ** server in order to process requests. Each such <code>HttpHandler</code> is
 ** registered with a root URI path which represents the location of the
 ** application or service on this server. The mapping of a handler to a
 ** {@link HttpServer} is encapsulated by a <code>HttpContext</code> object.
 ** <code>HttpContext</code>s are created by calling
 ** createContext(String, HttpHandler).
 ** <p>
 ** Any request for which no handler can be found is rejected with a 404
 ** response. Management of threads can be done external to this object by
 ** providing a Executor object. If none is provided a default implementation is
 ** used.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Server {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ROOT                = "/uid";
  public static final String CONTEXT_DEBUG       = "sysadmin";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the one and only instance of the <code>Server</code>
   ** <p>
   ** Singleton Pattern
   **
   ** Yes I know it should never be public but I'm lazy
   */
  static HttpServer instance;

  static String     context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Server</code> handler that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Server() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextUserURI
  /**
   ** Build a <code>Context URI</code> for resource type <code>Debug</code>.
   **
   ** @return                    the <code>Context URI</code> for resource type
   **                            <code>Debug</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String contextDebugURI() {
    return contextURI(ROOT, CONTEXT_DEBUG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURI
  /**
   ** Build a <code>Context URI</code> string based on the information provided.
   **
   ** @param  base               the base part of the <code>Context URI</code>
   **                            to build,
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the user's password
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Context URI</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String contextURI(final String base, final String context) {
    return String.format("%s/%s", base, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Starts the server in a new background thread.
   ** <br>
   ** The background thread inherits the priority, thread group and context
   ** class loader of the caller.
   **
   ** @param  port               the port number to expose the services.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws IOException        if the port specified could not be bound.
   */
  public static void start(final int port)
    throws IOException {

    instance = HttpServer.create(new InetSocketAddress(port), 0);
    // creates a default executor
    instance.setExecutor(null);

    // attach context handler
    DebugHandler.attach(instance,  contextDebugURI());

    instance.start();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stops the server by closing the listening socket and disallowing any new
   ** exchanges from being processed.
   ** <br>
   ** The method will then block until all current exchange handlers have
   ** completed or else when approximately <code>1</code> seconds have elapsed
   ** (whichever happens sooner). Then, all open TCP connections are closed, the
   ** background thread created by start() exits, and the method returns.
   ** <p>
   ** Once stopped, a HttpServer cannot be re-used.
   */
  public static void stop() {
    stop(1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stops the server by closing the listening socket and disallowing any new
   ** exchanges from being processed.
   ** <br>
   ** The method will then block until all current exchange handlers have
   ** completed or else when approximately <code>delay</code> seconds have
   ** elapsed (whichever happens sooner). Then, all open TCP connections are
   ** closed, the background thread created by start() exits, and the method
   ** returns.
   ** <p>
   ** Once stopped, a HttpServer cannot be re-used.
   **
   ** @param  delay              the delay the maximum time in seconds to wait
   **                            until exchanges have finished.
   **
   ** @throws IllegalArgumentException if delay is less than zero.
   */
  public static void stop(final int delay) {
    instance.stop(delay);
  }
}