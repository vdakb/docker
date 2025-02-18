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

    File        :   DirectoryContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import java.util.Set;
import java.util.Properties;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.UnknownHostException;

import java.security.Provider;
import java.security.Security;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateException;

import javax.naming.Name;
import javax.naming.Context;
import javax.naming.NamingException;

import javax.naming.ldap.LdapContext;
import javax.naming.ldap.InitialLdapContext;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryContext
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>DirectoryContext</code> a generic server and runtime implementation
 ** that can adjust its behaviour by a server type definition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DirectoryContext extends AbstractContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String      CONTEXT_TYPE              = "directory";

  /** the standard LDAP port  */
  static final int                PORT_DEFAULT              = 389;

  /** the default port number for secure LDAP connections. */
  static final int                PORT_DEFAULT_SECURE       = 636;

  /** the protocol each LDAP server is using */
  static final String             PROTOCOL_DEFAULT          = "ldap";

  /** the protocol each LDAP server is using over SSL */
  static final String             PROTOCOL_DEFAULT_SECURE   = "ldaps";

  /** the property the security protocol to use. */
  static final String             SECURITY_PROTOCOL         = "ssl";

  /** the type of certificate used to secure a connection with SSL. */
  static final String             CERTIFICATE_TYPE          = "X.509";

  /** Default value of the URL encoding. */
  static final String             URL_ENCODING_DEFAULT      = "UTF-8";

  /** Default value of the initial context factory. */
  static final String             CONTEXT_FACTORY_DEFAULT   = "com.sun.jndi.ldap.LdapCtxFactory";

  /** Default value of the SSL security provider. */
  static final String             SECURITY_PROVIDER_DEFAULT = "com.sun.net.ssl.internal.ssl.Provider";

  /**
   ** Default value indicating the format of a timestamp values in the target
   ** Directory Service
   */
  static final String             TIMESTAMP_FORMAT_DEFAULT   = "yyyyMMddHHmmss.SSS'Z'";

  /**
   ** Default value of the timeout period the LDAP provider doesn't get an LDAP
   ** response.
   */
  static final int                TIMEOUT_DEFAULT_RESPONSE   = 10000;

  /**
   ** Array of the characters that may be escaped in a DN or RDN.
   ** From RFC 2253 and the / character for JNDI
   */
  static final char[]             ESCAPED_CHAR               = {',', '+', '"', '\\', '/', '<', '>', ';'};

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** factory used to create the secure socket layer */
  private static SSLSocketFactory socketFactory = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private DirectoryType           type              = null;

  private int                     timeoutResponse   = TIMEOUT_DEFAULT_RESPONSE;
  private boolean                 secure            = false;
  private String                  binaryAttribute   = null;
  private String                  urlEncoding       = URL_ENCODING_DEFAULT;
  private String                  timestampFormat   = TIMESTAMP_FORMAT_DEFAULT;
  private String                  contextFactory    = CONTEXT_FACTORY_DEFAULT;
  private String                  securityProvider  = SECURITY_PROVIDER_DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryContext</code> for the specified
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
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link DirectoryType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public DirectoryContext(final DirectoryType type, final String protocol, final String host, final String port, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, TIMEOUT_DEFAULT_RESPONSE, username, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryContext</code> for the specified
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
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link DirectoryType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            LDAP provider connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  timeoutResponse    the timeout period for reading data on an
   **                            already established connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public DirectoryContext(final DirectoryType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, timeoutConnection, timeoutResponse, new SecurityPrincipal(username, password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link DirectoryType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public DirectoryContext(final DirectoryType type, final String protocol, final String host, final String port, final SecurityPrincipal principal) {
    // ensure inheritance
    this(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, TIMEOUT_DEFAULT_RESPONSE, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link DirectoryType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the LDAP provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            LDAP provider connection.
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
  public DirectoryContext(final DirectoryType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final SecurityPrincipal principal) {
    // ensure inheritance
    super(protocol, host, port, timeoutConnection, principal);

    // initialize instance attributes
    this.type            = type;
    this.timeoutResponse = timeoutResponse;
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
    if (this.contextFactory != null)
      handleAttributeError("refid");

    // ensure inheritance
    super.setRefid(reference);

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof DirectoryContext) {
      DirectoryContext that = (DirectoryContext)other;
      this.setTimeOutConnection(that.timeoutConnection());
      this.timeoutResponse   = that.timeoutResponse;
      this.binaryAttribute   = that.binaryAttribute;
      this.timestampFormat   = that.timestampFormat;
      this.contextFactory    = that.contextFactory;
    }
    else
      handleReferenceError(reference, CONTEXT_TYPE, other.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>type</code>.
   **
   ** @param  type               the value for the attribute
   **                            <code>type</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link DirectoryType}.
   **
   ** @throws BuildException     if the <code>type</code> is already specified
   **                            by a reference.
   */
  public final void setType(final DirectoryType type)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("type");

    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the server used to connect to.
   **
   ** @return                    the type of the server used to connect to.
   **                            <br>
   **                            Possible object is {@link DirectoryType}.
   */
  public final DirectoryType type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeoutResponse
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>timeoutResponse</code>.
   **
   ** @param  timeoutResponse  the value for the attribute
   **                            <code>timeoutResponse</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws BuildException     if the <code>timeoutResponse</code> is already
   **                            specified by a reference.
   */
  public final void setTimeoutResponse(final int timeoutResponse)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("timeoutResponse");

    this.timeoutResponse = timeoutResponse;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeoutResponse
  /**
   ** Returns the timeout period for establishment of the LDAP response.
   ** <p>
   ** This property affects the TCP timeout when opening a response to an LDAP
   ** server. When response pooling has been requested, this property also
   ** specifies the maximum wait time or a response when all responses in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the LDAP provider will wait
   ** indefinitely for a pooled response to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new response.
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP response.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int timeoutResponse() {
    return this.timeoutResponse;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecure
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>secure</code>.
   **
   ** @param  secure             <code>true</code> if the communication with the
   **                            value LDAP server will be secured; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws BuildException     if the <code>secure</code> is already specified
   **                            by a reference.
   */
  public final void setSecure(final boolean secure)
    throws BuildException {

    checkAttributesAllowed();
    this.secure = secure;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secure
  /**
   ** Returns the timeout period for establishment of the LDAP response.
   **
   ** @return                    <code>true</code> if the communication with the
   **                            value LDAP server will be secured; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean secure() {
    return this.secure;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBinaryAttribute
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>binaryAttribute</code>.
   **
   ** @param  binaryAttribute    the value for the attribute
   **                            <code>binaryAttribute</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>type</code> is already specified
   **                            by a reference.
   */
  public final void setBinaryAttribute(final String binaryAttribute)
    throws BuildException {

    // prevent bogus input
    checkAttributesAllowed();
    if (this.getRefid() != null)
      handleAttributeError("binaryObjectAttribute");

    this.binaryAttribute = binaryAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryAttribute
  /**
   ** Returns the name of the object attribute that is a binary attribute
   ** of an entry in a LDAP server.
   ** <p>
   ** The toolkit is programmed to recognize the following set of common LDAP
   ** binary attributes:
   ** <table border="0" cellspacing="10" cellpadding="5" summary="">
   ** <tr><th align="left">Attribute ID</th><th align="left">OID</th><th align="left">Reference</th></tr>
   ** <tr><td>photo</td><td>0.9.2342.19200300.100.1.7</td><td><a href="http://www.ietf.org/rfc/rfc1274.txt">RFC 1274</a></td></tr>
   ** <tr><td>personalSignature</td><td>0.9.2342.19200300.100.1.53</td><td><a href="http://www.ietf.org/rfc/rfc1274.txt">RFC 1274</a></td></tr>
   ** <tr><td>audio</td><td>0.9.2342.19200300.100.1.55</td><td><a href="http://www.ietf.org/rfc/rfc1274.txt">RFC 1274</a></td></tr>
   ** <tr><td>jpegPhoto</td><td>0.9.2342.19200300.100.1.60</td><td><a href="http://www.ietf.org/rfc/rfc2798.txt">RFC 2798</a></td></tr>
   ** <tr><td>javaSerializedData</td><td>1.3.6.1.4.1.42.2.27.4.1.7</td><td><a href="http://www.ietf.org/rfc/rfc2713.txt">RFC 2713</a></td></tr>
   ** <tr><td>thumbnailPhoto</td><td>2.16.128.113533.1.1400.1</td><td><a href="http://www.netapps.org/">NAC LIP Schema</a></td></tr>
   ** <tr><td>thumbnailLogo</td><td>2.16.128.113533.1.1400.2</td><td><a href="http://www.netapps.org/">NAC LIP Schema</a></td></tr>
   ** <tr><td>userPassword</td><td>2.5.4.35</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>userCertificate</td><td>2.5.4.36</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>cACertificate</td><td>2.5.4.37</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>authorityRevocationList</td><td>2.5.4.38</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>certificateRevocationList</td><td>2.5.4.38</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>crossCertificatePair</td><td>2.5.4.39</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** <tr><td>x500UniqueIdentifier</td><td>2.5.4.45</td><td><a href="http://www.ietf.org/rfc/rfc2256.txt">RFC 2256</a></td></tr>
   ** </table>
   **
   ** @return                    the names of the object attributes that are
   **                            treated as binaries.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String binaryAttribute() {
    return this.binaryAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimestampFormat
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>timestampFormat</code>.
   **
   ** @param  timestampFormat    the value for the attribute
   **                            <code>timestampFormat</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>timestampFormat</code> is already
   **                            specified by a reference.
   */
  public final void setTimestampFormat(final String timestampFormat)
    throws BuildException {

    // prevent bogus input
    checkAttributesAllowed();
    if (this.getRefid() != null)
      handleAttributeError("timestampFormat");

    this.timestampFormat = timestampFormat;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Returns the timestamp format used by the LDAP provider.
   **
   ** @return                    the timestamp format used by the LDAP provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String timestampFormat() {
    return this.timestampFormat;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setURLEncoding
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>urlEncoding</code>.
   **
   ** @param  urlEncoding        the value for the attribute
   **                            <code>urlEncoding</code> used as the LDAP
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>urlEncoding</code> is already
   **                            specified by a reference.
   */
  public final void setURLEncoding(final String urlEncoding)
    throws BuildException {

    // prevent bogus input
    checkAttributesAllowed();
    if (this.getRefid() != null)
      handleAttributeError("urlEncoding");

    this.urlEncoding = urlEncoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the url encoding.
   **
   ** @return                    the url encoding.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String urlEncoding() {
    return this.urlEncoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextFactory
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>contextFactory</code>.
   **
   ** @param  contextFactory     the context factory to bind to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setContextFactory(final String contextFactory)
    throws BuildException {

    // prevent bogus input
    checkAttributesAllowed();
    if (this.getRefid() != null)
      handleAttributeError("contextFactory");

    this.contextFactory = contextFactory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextFactory
  /**
   ** Returns the class name of the initial context factory.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextFactory() {
    return this.contextFactory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecurityProvider
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>securityProvider</code>.
   **
   ** @param  securityProvider   the security provider to bind to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setSecurityProvider(final String securityProvider)
    throws BuildException {

    // prevent bogus input
    checkAttributesAllowed();
    if (this.getRefid() != null)
      handleAttributeError("securityProvider");

    this.securityProvider = securityProvider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityProvider
  /**
   ** Returns the class name of the security provider.
   **
   ** @return                    the class name of the security provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String securityProvider() {
    return this.securityProvider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the instance attributes.
   **
   ** @param  environment        environment used to create the initial
   **                            {@link InitialLdapContext}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the {@link LdapContext} this task use to
   **                            communicate with the LDAP server.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   **
   ** @throws ServiceException   if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public final LdapContext connect(final Properties environment)
    throws ServiceException {

    LdapContext context = null;
    try {
      // be optimistic
      this.established(true);
      // Constructs an LDAP context object using environment properties but
      // none connection request controls.
      // See javax.naming.ldap.InitialLdapContext for a discussion of environment
      // properties.
      context = new InitialLdapContext(environment, null);
    }
    catch (NamingException e) {
      this.established(false);
      throw new ServiceException(ServiceError.CONTEXT_INITIALIZE, e);
    }
    return context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of this task that
   ** afterwards can be passed to establish a connection to the target system.
   **
   ** @param  contextPath        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   **
   ** @throws ServiceException   if the transport security could not be
   **                            created.
   */
  public final Properties environment(final String contextPath)
    throws ServiceException {

    final Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY,      contextFactory());
    environment.put(Context.PROVIDER_URL,                 contextURL(contextPath));
    environment.put("java.naming.ldap.version",           "3");
    environment.put(Context.SECURITY_PRINCIPAL,           this.username());
    environment.put(Context.SECURITY_CREDENTIALS,         this.password());
//    environment.put("com.sun.jndi.ldap.connect.timeout",  this.timeoutConnection());
//    environment.put("com.sun.jndi.ldap.read.timeout",     this.timeoutResponse());
    // Enable or disable connection pooling
    environment.put("com.sun.jndi.ldap.connect.pool",     SystemConstant.FALSE);

    // Register the binary objects in transport layer
    if (!StringUtility.isEmpty(binaryAttribute())) {
      // The toolkit is programmed to recognize the following set of common
      // LDAP binary attributes see binaryObjectAttribute() for further
      // explanation
      environment.put("java.naming.ldap.attributes.binary", binaryAttribute());
    }

    if (this.secure()) {
      validateCertificates();
      environment.put(Context.SECURITY_PROTOCOL, SECURITY_PROTOCOL);
      invalidateSSLSession();

      Provider provider = Security.getProvider(securityProvider());
      try {
        if (provider == null) {
          @SuppressWarnings("unchecked")
          Class<Provider> clazz = (Class<Provider>)Class.forName(SECURITY_PROVIDER_DEFAULT);
          Provider provider1 = clazz.newInstance();
          Security.addProvider(provider1);
        }
      }
      catch (ClassNotFoundException e) {
        throw new BuildException(ServiceResourceBundle.format(ServiceError.CLASSNOTFOUND, SECURITY_PROVIDER_DEFAULT));
      }
      catch (InstantiationException e) {
        throw new BuildException(ServiceResourceBundle.format(ServiceError.CLASSNOTCREATE, SECURITY_PROVIDER_DEFAULT));
      }
      catch (IllegalAccessException e) {
        throw new BuildException(ServiceResourceBundle.format(ServiceError.CLASSCONSTRUCTOR, SECURITY_PROVIDER_DEFAULT));
      }
    }
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeName
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the composed name
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String composeName(final String prefix, final String value) {
    String[] parameter = { prefix, escape(value) };
    return composeName(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeName
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  component          the attribute prefix, e.g <code>cn</code> and
   **                            the value
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the composed name
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String composeName(final Object[] component) {
    return String.format("%s=%s", component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
  /**
   ** This method gives the filter condition value for the given prefix and
   ** value string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the filter condition
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String composeFilter(final String prefix, final String value) {
    return composeFilter(prefix, "=", value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeFilter
  /**
   ** This method gives the filter condition value for the given prefix and
   ** value string.
   **
   ** @param  prefix             the attribute prefix, e.g <code>cn</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  predicate          the ....
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the composed filter condition embraced in
   **                            brackets.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String composeFilter(final String prefix, final String predicate, final String value) {
    return String.format("(%s%s%s)", prefix, predicate, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escape
  /**
   ** Returns the RDN after escaping any escaped characters.
   ** <p>
   ** For a list of characters that are typically escaped in a DN, see
   ** {@link #ESCAPED_CHAR}.
   **
   ** @param  value              the RDN path of the object (relative to the
   **                            connected hierarchy), usually it is just the
   **                            objects name (e.g. cn=Admin).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unescaped RDN.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #unescape(java.lang.String)
   */
  public static String escape(final String value) {
    final StringBuilder buffer = new StringBuilder();

    // Positional characters - see RFC 2253
    String escaped = value.replaceAll("^#", "\\\\#");
    escaped = escaped.replaceAll("^ | $", "\\\\ ");

    for (int i = 0; i < escaped.length(); i++) {
      for (int j = 0; j < ESCAPED_CHAR.length; j++) {
        if (escaped.charAt(i) == ESCAPED_CHAR[j])
          buffer.append('\\');
      }
      buffer.append(escaped.charAt(i));
    }

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescape
  /**
   ** Returns the RDN after unescaping any escaped characters.
   ** <p>
   ** For a list of characters that are typically escaped in a DN, see
   ** {@link #ESCAPED_CHAR}.
   **
   ** @param  value              the value of the RDN to unescape
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unescaped RDN.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    #ESCAPED_CHAR
   ** @see    #escape(java.lang.String)
   */
  public static String unescape(String value) {
    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      if (c  == '\\') {
        buffer.setCharAt(i, 'x');
        if (i < buffer.length() - 1)
          buffer.setCharAt(i + 1, 'x');
        else
          return null;
      }
    }

    // second pass, disable quoted sequences
    boolean quoteOn = false;
    for (int i = 0; i < buffer.length(); i++) {
      if (buffer.charAt(i) == '"') {
        quoteOn = !quoteOn;
        continue;
      }
      if (quoteOn)
        buffer.setCharAt(i, 'x');
    }
    return quoteOn ? null : buffer.toString();
  }

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

    if (this.type == null)
      handleAttributeMissing("DirectoryType");

    // a principal is required for this context type ...
    if (principal() == null)
      handleAttributeMissing("principal");

    // ... and therefore needs also be validated
    principal().validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Directory Information Tree.
   ** <p>
   ** The URL consists of the server part of the ldap url, ldap://host:port and
   ** the absolute path to the entry. The entry is post fixed with the context
   ** root of the connection.
   **
   ** @param  distinguishedName  a component of a Directory Information Tree.
   **                            <br>
   **                            Allowed object is {@link Name}.
   **
   ** @return                    the full qualified LDAP URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BuildException     if the given distinguished name cannot
   **                            converted to a <code>application/x-www-form-urlencoded</code>
   **                            MIME format.
   */
  public final String contextURL(final Name distinguishedName) {
    return contextURL(distinguishedName.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Directory Information Tree.
   ** <p>
   ** The URL consists of the server part of the ldap url, ldap://host:port and
   ** the absolute path to the entry. The entry is post fixed with the context
   ** root of the connection.
   **
   ** @param  distinguishedName  a component of a Directory Information Tree.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the full qualified LDAP URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BuildException     if the given distinguished name cannot
   **                            converted to a <code>application/x-www-form-urlencoded</code>
   **                            MIME format.
   */
  public final String contextURL(final String distinguishedName) {

    // create the service url (server name and port) prefixed with the
    // protocol check if the service url end already with a slash '/'
    final StringBuilder url = new StringBuilder(serviceURL());
    if (url.charAt(url.length() - 1) != SystemConstant.SLASH)
      url.append(SystemConstant.SLASH);

    // return the resulting url by escaping all space characters that may
    // contained by the appropriate encoded character
    return url.append(distinguishedName).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidateSSLSession
  /**
   ** Invalidates the SSL session.
   ** <br>
   ** Future connections will not be able to resume or join this session.
   ** However, any existing connection using this session can continue to use
   ** the session until the connection is closed.
   **
   ** @throws ServiceException   if the operation fails
   */
  protected void invalidateSSLSession()
    throws ServiceException {

    try {
      // if not already done obtain the default SSL socket factory
      if (socketFactory == null)
        socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

      // creates a SSL socket and connect it to the remote host at the remote
      // port.
      final SSLSocket  socket  = (SSLSocket)socketFactory.createSocket(this.host(), Integer.parseInt(this.port()));
      final SSLSession session = socket.getSession();
      session.invalidate();
      socket.close();
    }
    // if the host is not known
    catch (UnknownHostException e) {
      throw new ServiceException(ServiceError.LDAP_CONNECTION_UNKNOWN_HOST, this.host(), e);
    }
    // if an I/O error occurs when creating the socket
    catch (IOException e) {
      String[] parameter = {this.host(), this.port()};
      throw new ServiceException(ServiceError.LDAP_CONNECTION_CREATE_SOCKET, parameter, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateCertificates
  /**
   ** Get the certificate path from the <code>java.library.path</code> and check
   ** whether the critical extension is supported in the certificate or not.
   **
   ** @return                    <code>true</code> if the critical extension is
   **                            supported in the certificate or not
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws ServiceException   if the operation fails
   */
  protected boolean validateCertificates()
    throws ServiceException {

    boolean extentionSupported = true;
    String libraryPath         = System.getProperty("java.library.path");
    String certificateFile     = SystemConstant.EMPTY;
    if (libraryPath != null && libraryPath.length() > 0) {
      String parts[] = libraryPath.split(File.separator);
      if (parts != null && parts.length > 0)
        certificateFile = parts[parts.length - 1];
    }
    try {
      if (certificateFile != null && certificateFile.length() > 0 && certificateFile.indexOf(".cer") >= 0) {
        final FileInputStream stream = new FileInputStream(certificateFile);

        // Generates a certificate factory object that implements the X.509 type.
        // If the default provider package provides an implementation of the
        // requested certificate type, an instance of certificate factory
        // containing that implementation is returned.
        // If the type is not available in the default package, other packages
        // are searched.
        final CertificateFactory factory     = CertificateFactory.getInstance(CERTIFICATE_TYPE);
        final X509Certificate    certificate = (X509Certificate)factory.generateCertificate(stream);
        stream.close();
        Set<String> set = certificate.getCriticalExtensionOIDs();
        if (set != null && !set.isEmpty())
          info(ServiceResourceBundle.string(ServiceError.LDAP_CONTROL_EXTENSION_EXISTS));
        else
          info(ServiceResourceBundle.string(ServiceError.LDAP_CONTROL_EXTENSION_NOT_EXISTS));

        extentionSupported = !certificate.hasUnsupportedCriticalExtension();
      }
    }
    // if the requested certificate type is not available in the default
    // provider package or any of the other provider packages that were
    // searched.
    catch (CertificateException e) {
      throw new ServiceException(ServiceError.LDAP_CERTIFICATE_TYPE_NOT_AVAILABLE, CERTIFICATE_TYPE,  e);
    }
    // if the certificate file is not available in the default provider package
    // or any of the other provider packages that were searched.
    catch (FileNotFoundException e) {
      throw new ServiceException(ServiceError.LDAP_CERTIFICATE_FILE_NOT_FOUND, certificateFile,  e);
    }
    catch (IOException e) {
      throw new ServiceException(e);
    }
    return extentionSupported;
  }
}