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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AbstractRESTfulTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractRESTfulTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.ServerContext;
import oracle.hst.deployment.type.APIServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractRESTfulTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant RESTful API tasks.
 ** Implementations of <code>Service</code> inherit its attributes (see below)
 ** for connecting to the HTTP server.
 ** <p>
 ** Refer to the user documentation for more information and examples on how to
 ** use this task.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractRESTfulTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected APIServerContext  server      = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs aa <code>AbstractRESTfulTask</code> Ant task that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractRESTfulTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractRESTfulTask</code> that use the specified
   ** {@link APIServerContext} <code>server</code> as the runtime environment.
   **
   ** @param  server             the {@link APIServerContext} used as the
   **                            runtime environment.
   */
  protected AbstractRESTfulTask(final APIServerContext server) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.server = server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link APIServerContext} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to an
   **                            {@link APIServerContext}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link APIServerContext}.
   */
  public void setContextRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof ServerContext)) {
      final String[] parameter = {reference.getRefId(), APIServerContext.CONTEXT_TYPE, reference.getRefId(), object.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter));
    }
    this.server = (APIServerContext)object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeOutConnection
  /**
   ** Sets the maximum time for establishment of the HTTP connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an HTTP
   ** server. When connection pooling has been requested, this property also
   ** specifies the maximum wait time or a connection when all connections in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the HTTP provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   ** <p>
   ** The default setting is ten seconds (10000).
   **
   ** @param  timeout            the maximum time for a connection to be
   **                            established.
   **                            Allowed object is <code>int</code>.
   */
  public final void setTimeOutConnection(final int timeout) {
    this.server.setTimeOutConnection(timeout);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeoutConnection
  /**
   ** Returns the maximum time for establishment of the HTTP connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an HTTP
   ** server. When connection pooling has been requested, this property also
   ** specifies the maximum wait time or a connection when all connections in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the HTTP provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   ** <p>
   ** The default setting is ten seconds (10000).
   **
   ** @return                    the maximum time for establishment of the
   **                            HTTP connection.
   **                            Possible object is <code>int</code>.
   */
  public final int timeoutConnection() {
    return this.server.timeoutConnection();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeoutResponse
  /**
   ** Set the maximum time for reading data on an already established HTTP
   ** connection.
   ** <p>
   ** A non-zero value specifies the timeout when reading from Input stream when
   ** a connection is established to a resource. If the timeout expires before
   ** there is data available for read, a java.net.SocketTimeoutException is
   ** raised. A timeout of zero is interpreted as an infinite timeout.
   ** <p>
   ** The default setting is ten seconds (10000).
   **
   ** @param  timeout            the maximum time between establishing a
   **                            connection and receiving data from the
   **                            connection.
   **                            Allowed object is <code>int</code>.
   */
  public final void setTimeoutResponse(final int timeout) {
    this.server.setTimeOutResponse(timeout);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeoutResponse
  /**
   ** Returns the maximum time for reading data on an already established HTTP
   ** connection.
   ** <p>
   ** A non-zero value specifies the timeout when reading from Input stream when
   ** a connection is established to a resource. If the timeout expires before
   ** there is data available for read, a java.net.SocketTimeoutException is
   ** raised. A timeout of zero is interpreted as an infinite timeout.
   ** <p>
   ** The default setting is ten seconds (10000).
   **
   ** @return                    the maximum maximum time establishing a
   **                            connection and receiving data from the
   **                            connection.
   **                            Possible object is <code>int</code>.
   */
  public final int timeoutResponse() {
    return this.server.timeoutResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUseTimestamp
  /**
   ** If <code>true</code>, conditionally download a file based on the timestamp
   ** of the local copy.
   ** <p>
   ** In this situation, the if-modified-since header is set so that the file is
   ** only fetched if it is newer than the local file (or there is no local file).
   ** This flag is only valid on HTTP connections, it is ignored in other cases.
   ** When the flag is set, the local copy of the downloaded file will also have
   ** its timestamp set to the remote file time.
   ** <p>
   ** <b>Note</b>:
   ** Remote files of date 1/1/1970 (GMT) are treated as 'no timestamp', and web
   ** servers often serve files with a timestamp in the future by replacing their
   ** timestamp with that of the current time. Also, inter-computer clock
   ** differences can cause no end of grief.
   ** <p>
   ** The default setting is <code>true</code>.
   **
   ** @param  useTimestamp       whether to enable file time fetching or not.
   */
  public void setUseTimestamp(final boolean useTimestamp) {
    this.server.setUseTimestamp(useTimestamp);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   useTimestamp
  /**
   ** Whether to enable file time fetching or not.
   ** <p>
   ** The default setting is <code>true</code>.
   **
   ** @return                    to enable file time fetching or not.
   */
  public final boolean useTimestamp() {
    return this.server.useTimestamp();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUseCaching
  /**
   ** Sets the value of the <code>useCaching</code> property of this context to
   ** the specified value.
   ** <p>
   ** Some protocols do caching of documents. Occasionally, it is important to
   ** be able to "tunnel through" and ignore the caches (e.g., the "reload"
   ** button in a browser). If the UseCaches flag on a HTTP connection is
   ** <code>true</code>, the connection is allowed to use whatever caches it
   ** can. If <code>false</code>, caches are to be ignored.
   ** <p>
   ** Defaults to <code>true</code> (allow caching, which is also the
   ** <code>HTTP Connection</code> default value..
   **
   ** @param  allowed            whether caching on the
   **                            <code>HTTP Connection</code> is allowed or not.
   */
  public void setUseCaching(final boolean allowed) {
    this.server.setUseCaching(allowed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   useCaching
  /**
   ** Whether the value of the <code>useCaching</code> property of this context to
   ** the specified value.
   ** <p>
   ** Defaults to <code>true</code> (allow caching, which is also the
   ** <code>HTTP Connection</code> default value..
   **
   ** @return                    whether caching on the
   **                            <code>HTTP Connection</code> is allowed or not.
   */
  public final boolean useCaching() {
    return this.server.useCaching();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserAgent
  /**
   ** Set the user-agent to be used when communicating with remote server.
   ** <br>
   ** If <code>null</code>, then the value is considered unset and the behaviour
   ** falls back to the default of the http API.
   **
   ** @param  userAgent          the user-agent to be used when communicating
   **                            with remote server.
   */
  public final void setUserAgent(final String userAgent) {
    this.server.setUserAgent(userAgent);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userAgent
  /**
   ** Return the user-agent to be used when communicating with remote server.
   ** <br>
   ** If <code>null</code>, then the value is considered unset and the behaviour
   ** falls back to the default of the http API.
   **
   ** @return                    the user-agent to be used when communicating
   **                            with remote server.
   */
  public final String userAgent() {
    return this.server.userAgent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the context object of this task.
   **
   ** @return                    the context object of this task.
   */
  public final APIServerContext context() {
    return this.server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractTask)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate() {
    if (this.server == null)
      handleAttributeError("server");

    this.server.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected (AbstractTask)
  /**
   ** Returns the state of connection.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** HTTP conversations are always stateless hence this method return always
   ** <code>true</code>
   **
   ** @return                    the state of connection by nature always
   **                            <code>true</code>.
   */
  @Override
  protected final boolean connected() {
    return this.server.established();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractTask)
  /**
   ** Establish a connection to the HTTP server.
   ** <p>
   ** <b>Note</b>: By nature a HTTP request is always stateless hence there
   ** isn't something that we need to handle as a connection like other context
   ** instances.
   ** <br>
   ** The current implementation does nothing else to simulate a successful
   ** connection by set the connection established flag in the conext instance
   ** to avoid unnecessary roundtrips.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  protected void connect()
    throws ServiceException {

    final String[] parameter = {this.server.host(), this.server.port()};
    info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONNECTING, parameter));
    try {
      this.server.connect();
      info(ServiceResourceBundle.string(ServiceMessage.SERVER_CONNECTED));
    }
    catch (ServiceException e) {
      error(ServiceResourceBundle.format(ServiceError.CONTEXT_CONNECTION, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractTask)
  /**
   ** Close a connection to the target system.
   ** <p>
   ** <b>Note</b>: By nature a HTTP request is always stateless hence there
   ** isn't something that we need to handle as a connection like other context
   ** instances.
   ** <br>
   ** The current implementation does nothing else to simulate a successful
   ** connection by reset the connection established flag in the conext instance
   ** to avoid unnecessary roundtrips.
   **
   ** @throws ServiceException      in case an error does occur.
   */
  @Override
  protected void disconnect()
    throws ServiceException {

    this.server.disconnect();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs a {@link HttpURLConnection} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @param   contextPath       the path on the Web Server to access.
   **
   ** @return                    the context this connector use to communicate
   **                            with the OIM server.
   **
   ** @throws ServiceException   if the {@link HttpURLConnection} could not be
   **                            created  at the first time this method is
   **                            invoked.
   */
  public final HttpURLConnection formRequest(final String contextPath)
    throws ServiceException {

    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection)this.server.contextURL(contextPath).openConnection();
      // use basic authentication if requested
      connection.setRequestProperty("Authorization", this.server.basicAuthentication());
          connection.setRequestProperty("Accept",       "application/json");
          connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      // use post mode
      connection.setDoOutput(true);
      connection.setAllowUserInteraction(false);
    }
    catch (MalformedURLException e) {
      disconnect();
      throw new ServiceException(ServiceError.HTTP_CONNECTION_MALFORMED_URL, contextPath);
    }
    catch (IOException e) {
      if (connection != null)
        connection.disconnect();
      throw new ServiceException(e);
    }
    return connection;
  }
}