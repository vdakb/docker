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

    File        :   WWWServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WWWServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import java.net.URL;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceException;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class WWWServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>WWWServerContext</code> server is a special server and runtime
 ** implementation of {@link ServerContext} tooling that can adjust its
 ** behaviour by a server type definition file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WWWServerContext extends APIServerContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE           = "www";

  /** the request method each HTTP server is supporting */
  public static final String REQUEST_METHOD_DEFAULT = "POST";

  /**
   ** As part of the HTTP specification, servers can send redirects - an
   ** instruction that the browser should try to get the content from another
   ** URL.
   ** <br>
   ** Because one URL could redirect to another and the other could redirect to
   ** the first, causing an infinite loop, a limit is placed on how many
   ** redirects can occur on one request.
   */
  static final int           REDIRECT_LIMIT         = 25;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             contextPath            = null;

  private boolean            gzipEncoding           = false;
  private boolean            followRedirect         = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WWWServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public WWWServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WWWServerContext</code> for the specified
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
   ** @param  path               the path in the request to the remote
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public WWWServerContext(final ServerType type, final String protocol, final String host, final String port, final String username, final String password, final String path) {
    // ensure inheritance
    this(type, protocol, host, port, new SecurityPrincipal(username, password), path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WWWServerContext</code> for the specified
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
   ** @param  path               the path in the request to the remote
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public WWWServerContext(final ServerType type, final String protocol, final String host, final String port, final SecurityPrincipal principal, final String path) {
    // ensure inheritance
    this(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, TIMEOUT_DEFAULT_RESPONSE, principal, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WWWServerContext</code> for the specified
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
   **                            HTTP provider connection.
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
   ** @param  path               the path in the request to the remote
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public WWWServerContext(final ServerType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final SecurityPrincipal principal, final String path) {
    // ensure inheritance
     super(type, protocol, host, port, timeoutConnection, timeoutResponse, principal);

    // initialize instance attributes
    this.contextPath = path;
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

    // prevent bogus input
    if (!StringUtility.isEmpty(this.contextPath))
      handleAttributeError("refid");

    // ensure inheritance
    super.setRefid(reference);

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof WWWServerContext) {
      WWWServerContext that = (WWWServerContext)other;
      this.contextPath      = that.contextPath;
    }
    else
      handleReferenceError(reference, CONTEXT_TYPE, other.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPath
  /**
   ** Set the path in the request to the remote server.
   **
   ** @param  path               the path in the request to the remote
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setPath(final String path) {
    this.contextPath = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextPath
  /**
   ** Return the path in the request to the remote server.
   **
   ** @return                    the path in the request to the remote
   **                            server.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextPath() {
    return this.contextPath;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the {@link URL} for the request to the remote server.
   **
   ** @return                    the URL tring for the request to the remote
   **                            server.
   **                            <br>
   **                            Possible object is {@link URL}.
   **
   ** @throws ServiceException   if an error occurs evaluating the {@link URL}.
   */
  public final URL contextURL()
    throws ServiceException {

    return contextURL(this.contextPath);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setGzipEncoding
  /**
   ** Whether to transparently reduce bandwidth by telling the server that
   ** the context would support gzip encoding.
   ** <p>
   ** The default setting is <code>false</code>.
   **
   ** @param  gzipEncoding       whether to transparently reduce bandwidth by
   **                            telling the server that the context would
   **                            support gzip encoding.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void setGzipEncoding(final boolean gzipEncoding) {
    this.gzipEncoding = gzipEncoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gzipEncoding
  /**
   ** Whether to transparently reduce bandwidth by telling the server that
   ** the context would support gzip encoding.
   ** <p>
   ** The default setting is <code>false</code>.
   **
   ** @return                    whether to transparently reduce bandwidth by
   **                            telling the server that the context would
   **                            support gzip encoding.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean gzipEncoding() {
    return this.gzipEncoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFollowRedirect
  /**
   ** Whether to follow HTTP 302 redirects or not.
   ** <br>
   ** "Following" a redirect means that the HTTP response will be that of the
   ** redirected-to page, not the redirect.
   ** <p>
   ** The default setting is <code>false</code>.
   **
   ** @param  follow             whether to follow HTTP 302 redirects or not.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void setFollowRedirect(final boolean follow) {
    this.followRedirect = follow;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   followRedirect
  /**
   ** Whether to follow HTTP 302 redirects or not.
   ** <br>
   ** "Following" a redirect means that the HTTP response will be that of the
   ** redirected-to page, not the redirect.
   ** <p>
   ** The default setting is <code>false</code>.
   **
   ** @return                    whether to follow HTTP 302 redirects or not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean followRedirect() {
    return this.followRedirect;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType (overridden)
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to perform.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    if (principal() != null)
      principal().validate();

    if (StringUtility.isEmpty(this.contextPath))
      handleAttributeMissing("contextPath");

    // ensure inheritance
    super.validate();
  }
}