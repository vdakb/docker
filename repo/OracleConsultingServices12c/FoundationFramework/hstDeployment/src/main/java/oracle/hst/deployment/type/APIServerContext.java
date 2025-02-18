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
    Subsystem   :   Deployment Utilities

    File        :   APIServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    APIServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.MalformedURLException;

import oracle.hst.deployment.ServiceError;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.Base64Encoder;
import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class APIServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>APIServerContext</code> server is a special server and runtime
 ** implementation of {@link ServerContext} tooling that can adjust its
 ** behaviour by a server type definition file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class APIServerContext extends ServerContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE           = "api";

  /** the standard HTTP port  */
  public static final int    PORT_DEFAULT            = 80;

  /** the default port number for secure HTTP connections. */
  public static final int    PORT_DEFAULT_SECURE     = 443;

  /** the protocol each HTTP server is using */
  public static final String PROTOCOL_DEFAULT        = "http";

  /** the protocol each HTTP server is using over SSL */
  public static final String PROTOCOL_DEFAULT_SECURE = "https";

  /** the language each HTTP accepts */
  public static final String LANGUAGE_DEFAULT        = "en-US,en;q=0.5";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             userAgent              = null;
  private String             language               = LANGUAGE_DEFAULT;
  private boolean            useCaching             = true;
  private boolean            useTimestamp           = true;

  private URL                serviceURL             = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>APIServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public APIServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>APIServerContext</code> for the specified
   ** <code>type</code>, <code>protocol</code>, <code>host</code> and
   ** <code>port</code>.
   ** <br>
   ** The required security context is provided by <code>username</code> and
   ** <code>password</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the HTTP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected APIServerContext(final ServerType type, final String protocol, final String host, final String port, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, new SecurityPrincipal(username, password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>APIServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the HTTP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  protected APIServerContext(final ServerType type, final String protocol, final String host, final String port, final SecurityPrincipal principal) {
    // ensure inheritance
    super(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, TIMEOUT_DEFAULT_RESPONSE, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>APIServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the HTTP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the HTTP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            content provider connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  timeoutResponse    the timeout period for reading data on an
   **                            already established connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  protected APIServerContext(final ServerType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final SecurityPrincipal principal) {
    // ensure inheritance
    super(type, protocol, host, port, timeoutConnection, timeoutResponse, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>refid</code>.
   **
   ** @param  reference          the id of this instance.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    // ensure inheritance
    super.setRefid(reference);

    Object other = reference.getReferencedObject(getProject());
    if (!(other instanceof APIServerContext)) {
      handleReferenceError(reference, CONTEXT_TYPE, other.getClass());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the {@link URL} for the request to the remote server.
   **
   ** @param   path              the path on the Web Server to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the URL tring for the request to the remote
   **                            server.
   **                            <br>
   **                            Possible object is {@link URL}.
   **
   ** @throws ServiceException   if an error occurs evaluating the {@link URL}.
   */
  public final URL contextURL(final String path)
    throws ServiceException {

    // ensure the service URL is evaluated
    connect();

    // if an empty path ist givven the returning URL does not need different
    if (StringUtility.isEmpty(path))
      return this.serviceURL;

    // return the resulting url by escaping all space characters that may
    // contained by the appropriate encoded character
    try {
      return new URL(this.serviceURL, path);
    }
    catch (MalformedURLException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_MALFORMED_URL, path);
    }
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
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void setUseTimestamp(final boolean useTimestamp) {
    this.useTimestamp = useTimestamp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   useTimestamp
  /**
   ** Whether to enable file time fetching or not.
   ** <p>
   ** The default setting is <code>true</code>.
   **
   ** @return                    to enable file time fetching or not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean useTimestamp() {
    return this.useTimestamp;
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
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void setUseCaching(final boolean allowed) {
    this.useCaching = allowed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   useCaching
  /**
   ** Whether the value of the <code>useCaching</code> property of this context
   ** to the specified value.
   ** <p>
   ** Defaults to <code>true</code> (allow caching, which is also the
   ** <code>HTTP Connection</code> default value..
   **
   ** @return                    whether caching on the
   **                            <code>HTTP Connection</code> is allowed or not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean useCaching() {
    return this.useCaching;
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setUserAgent(final String userAgent) {
    this.userAgent = userAgent;
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userAgent() {
    return this.userAgent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserAgent
  /**
   ** Set the language to be used when communicating with remote server.
   ** <br>
   ** If <code>null</code>, then the value is considered unset and the behaviour
   ** falls back to the default of the http API.
   **
   ** @param  language           the language to be used when communicating
   **                            with remote server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setLangauge(final String language) {
    this.language = StringUtility.isEmpty(language) ? LANGUAGE_DEFAULT : language;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   language
  /**
   ** Return the language to be used when communicating with remote server.
   ** <br>
   ** If <code>null</code>, then the value is considered unset and the behaviour
   ** falls back to the default of the http API.
   **
   ** @return                    the language to be used when communicating
   **                            with remote server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String language() {
    return this.language;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   basicAuthentication
  /**
   ** Return the credentials used in the context of Preemptive Basic
   ** Authentication.
   **
   ** @return                    the credentials used in the context of
   **                            Preemptive Basic Authentication.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ServiceException   if the credentials could not be encoded
   **                            correctly.
   */
  public final String basicAuthentication()
    throws ServiceException {

    try {
      final String credential    = String.format("%s:%s", username(), password());
      final String authorization = StringUtility.bytesToString(Base64Encoder.encode(credential.getBytes("UTF-8")));
      return String.format("Basic %s", authorization);
    }
    catch (UnsupportedEncodingException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_ENCODING, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType (AbstractContext)
  /**
   ** Returns the specific type of the implemented context.
   **
   ** @return                    the specific type of the implemented context.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String contextType() {
    return CONTEXT_TYPE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractContext)
  /**
   ** Establish a connection to the HTTP Server and creates the connection.
   ** <p>
   ** <b>Note</b>: By nature a HTTP request is always stateless hence there
   ** isn't something that we need to handle as a connection like other context
   ** instances.
   ** <br>
   ** The current implementation does nothing else to simulate a successful
   ** connection by set the connection established flag to avoid unnecessary
   ** roundtrips.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  public void connect()
    throws ServiceException {

    if (this.established())
      return;

    try {
      this.serviceURL = new URL(serviceURL());
      this.established(true);
    }
    catch (MalformedURLException e) {
      disconnect();
      throw new ServiceException(ServiceError.HTTP_CONNECTION_MALFORMED_URL, serviceURL());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractContext)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void disconnect()
    throws ServiceException {

    this.established(false);
  }
}