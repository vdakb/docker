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

    System      :   Foundation Service Extension
    Subsystem   :   Ligth-Weight HTTP Tracer

    File        :   Tracer.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Tracer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.http;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;

import javax.net.ServerSocketFactory;

import javax.net.ssl.SSLSocket;

public class Tracer {

  //////////////////////////////////////////////////////////////////////////////
  // intstance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected volatile int                   port;
  protected volatile ServerSocket          instance;
  protected volatile int                   timeout = 10000;
  protected volatile ServerSocketFactory   socketFactory;

  protected volatile Executor              executor;

  //////////////////////////////////////////////////////////////////////////////
  // class Handler
  // ~~~~~ ~~~~~~~
  /**
   ** The <code>Handler</code> handles accepted sockets.
   */
  protected class SocketHandler extends Thread {
    @Override @SuppressWarnings("oracle.jdeveloper.java.insufficient-catch-block")
    public void run() {
      setName(getClass().getSimpleName() + "-" + port);
      try {
        // keep local to avoid NPE when stopped
        ServerSocket instance = Tracer.this.instance;
        while (instance != null && !instance.isClosed()) {
          final Socket socket = instance.accept();
          Tracer.this.executor.execute(
            new Runnable() {
              public void run() {
                try {
                  try {
                    socket.setSoTimeout(Tracer.this.timeout);
                    // we buffer anyway, so improve latency
                    socket.setTcpNoDelay(true);
                    Support.transfer(socket.getInputStream(), socket.getOutputStream(), -1);
                  }
                  finally {
                    try {
                      // RFC7230#6.6 - close socket gracefully
                      // (except SSL socket which doesn't support half-closing)
                      if (!(socket instanceof SSLSocket)) {
                        // half-close socket (only output)
                        socket.shutdownOutput();
                        // consume input
                        Support.transfer(socket.getInputStream(), null, -1);
                      }
                    }
                    finally {
                      // and finally close socket fully
                      socket.close();
                    }
                  }
                }
                catch (IOException e) {
                  // intentionally left balnk
                }
              }
            }
          );
        }
      }
      catch (IOException e) {
        // intentionally left balnk
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Tracer</code> which can accept connections on the given
   ** port.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The {@link #start()} method must be called to start accepting connections.
   **
   ** @param  port               the port on which this server will accept
   **                            connections.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public Tracer(final int port) {
    // ensure inheritance
    super();

    // initialize intenace attributes
    this.port = port;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Starts a stand-alone HTTP server, serving files from disk.
   **
   ** @param  args               the command line arguments
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  public static void main(String[] args) {
    Tracer server = null;
    try {
      if (args.length == 0) {
        System.err.printf("Usage: java [-options] %s [port]%n To enable SSL: specify options -Djavax.net.ssl.keyStore, -Djavax.net.ssl.keyStorePassword, etc.%n", Server.class.getName());
        return;
      }

      int port = args.length < 1 ? 80 : (int)Support.parseUnsignedLong(args[0], 10);
      server = new Tracer(port);
      server.start();
    }
    catch (Exception e) {
      System.err.println("error: " + e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Starts this server.
   ** <br>
   ** If it is already started, does nothing.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Once the server is started, configuration-altering methods of the server
   ** and its virtual hosts must not be used. To modify the configuration, the
   ** server must first be stopped.
   **
   ** @throws IOException        if the server cannot begin accepting
   **                            connections.
   */
  public synchronized void start()
    throws IOException {

    if (this.instance != null)
      return;

    // assign default server socket factory if needed
    if (this.socketFactory == null)
      // plain sockets
      this.socketFactory = ServerSocketFactory.getDefault();

    this.instance = this.socketFactory.createServerSocket();
    this.instance.setReuseAddress(true);
    this.instance.bind(new InetSocketAddress(this.port));

    // assign default executor if needed
    if (this.executor == null)
      // consumes no resources when idle
      this.executor = Executors.newCachedThreadPool();

    // start handling incoming connections
    new SocketHandler().start();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stops this server. If it is already stopped, does nothing.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** If an {@link #executor(Executor) Executor} was set, it must be closed
   ** separately.
   */
  public synchronized void stop() {
    try {
      if (this.instance != null)
        this.instance.close();
    }
    catch (IOException e) {
      // intentionally left blank
      ;
    }
    this.instance = null;
  }
}
