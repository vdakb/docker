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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   WebServiceResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    WebServiceResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.soap;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import java.io.IOException;

import java.net.URL;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import javax.xml.namespace.QName;

import javax.xml.bind.JAXBContext;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import javax.xml.transform.Source;

import javax.xml.ws.Service;
import javax.xml.ws.Dispatch;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.HandlerResolver;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.ws.client.ClientTransportException;
import com.sun.xml.internal.ws.client.BindingProviderProperties;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLoggable;
import oracle.iam.identity.foundation.AbstractMetadataTask;

////////////////////////////////////////////////////////////////////////////////
// class WebServiceConnector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>WebServiceConnector</code> implements the base functionality
 ** of an Oracle Identity Manager Connector for Oracle Identity WebService.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class WebServiceConnector extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String               WSDL_SUFFIX                = "wsdl";

  static final String               DEFAULT_WSSE_PREFIX        = "wsse";
  static final String               DEFAULT_WSSE_NAMESPACE     = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
  static final String               DEFAULT_WSU_PREFIX         = "xmlns:wsu";
  static final String               DEFAULT_WSU_NAMESPACE      = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
  static final String               DEFAULT_UTP_NAMESPACE      = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0";
  static final String               DEFAULT_PASSWORD_TEXTVALUE = "PasswordText";

  static final String               ELEMENT_SECURITY           = "Security";
  static final String               ELEMENT_USERNAME_TOKEN     = "UsernameToken";
  static final String               ELEMENT_USERNAME           = "Username";
  static final String               ELEMENT_PASSWORD           = "Password";
  static final String               ATTRIBUTE_PASSWORD_TYPE    = "Type";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final boolean           secureSocket;
  protected final String            serverName;
  protected final int               serverPort;
  protected final String            rootContext;
  protected final String            principalName;
  protected final String            principalPassword;
  protected final String            localeLanguage;
  protected final String            localeCountry;
  protected final String            localeTimeZone;

  /**
   ** the wrapper of target specific features where this connector is attached
   ** to
   */
  protected final WebServiceFeature feature;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Authenticator
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Sets a userid and password pair to the SOAP header, and should be deployed
   ** at the client side only.
   */
  final class Authenticator implements SOAPHandler<SOAPMessageContext> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   handleMessage (Handler)
    /**
     ** The <code>handleMessage</code> method is invoked for normal processing of
     ** inbound and outbound messages. Refer to the description of the handler
     **  framework in the JAX-WS specification for full details.
     **
     ** @param  context            the message context.
     **
     ** @return                    an indication of whether handler processing
     **                            should continue for the current message
     **                            <ul>
     **                              <li>Return <code>true</code> to continue
     **                                  processing.
     **                              <li>Return <code>false</code> to block
     **                                  processing.
     **                            </ul>
     **
     ** @throws RuntimeException   causes the JAX-WS runtime to cease handler
     **                            processing and generate a fault.
     */
    @Override
    public boolean handleMessage(final SOAPMessageContext context) {
      final SOAPMessage message  = context.getMessage();
      final Boolean     outbound = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
      if (outbound.booleanValue()) {
        try {
          // add a header with the authentication info into the SOAP message:
          // <soap:Header>
          //   <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
          //     <wsse:UsernameToken xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
          //       <wsse:Username>username</wsse:Username>
          //       <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">password</wsse:Password>
          //     </wsse:UsernameToken>
          //   </wsse:Security>
          // </soap:Header>

          // get SOAP envelope from SOAP message
          final SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
          SOAPHeader header = envelope.getHeader();
          if (header == null)
            header = envelope.addHeader();
          // create SOAP elements specifying prefix and URI
          final SOAPElement security      = header.addChildElement(ELEMENT_SECURITY, DEFAULT_WSSE_PREFIX, DEFAULT_WSSE_NAMESPACE);
          final SOAPElement usernameToken = security.addChildElement(ELEMENT_USERNAME_TOKEN, DEFAULT_WSSE_PREFIX);
          usernameToken.addAttribute(new QName(DEFAULT_WSU_PREFIX), DEFAULT_WSU_NAMESPACE);
          usernameToken.addChildElement(ELEMENT_USERNAME, DEFAULT_WSSE_PREFIX).addTextNode(WebServiceConnector.this.principalName());

          final SOAPElement password = usernameToken.addChildElement(ELEMENT_PASSWORD, DEFAULT_WSSE_PREFIX);
          //http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
          password.setAttribute(ATTRIBUTE_PASSWORD_TYPE, String.format("%s#%s", DEFAULT_UTP_NAMESPACE, DEFAULT_PASSWORD_TEXTVALUE));
          password.addTextNode(WebServiceConnector.this.principalPassword());
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      else {
        // this handler does nothing with the response from the Web Service so
        ;
      }
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   handleFault (Handler)
    /**
     ** The <code>handleFault</code> method is invoked for fault message
     ** processing. Refer to the description of the handler framework in the
     ** JAX-WS specification for full details.
     **
     ** @param  context            the message context.
     **
     ** @return                    an indication of whether handler fault
     **                            processing should continue for the current
     **                            message
     **                            <ul>
     **                              <li>Return <code>true</code> to continue
     **                                  processing.
     **                              <li>Return <code>false</code> to block
     **                                  processing.
     **                            </ul>
     **
     ** @throws RuntimeException causes the JAX-WS runtime to cease handler
     **                          fault processing and dispatch the fault.
     **/
    public boolean handleFault(final SOAPMessageContext context) {
      //throw new UnsupportedOperationException("Not supported yet.");
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   close (Handler)
    /**
     ** Called at the conclusion of a message exchange pattern just prior to the
     ** JAX-WS runtime disptaching a message, fault or exception.
     ** <br>
     ** Refer to the description of the handler framework in the JAX-WS
     ** specification for full details.
     **
     ** @param  context            the message context
     */
    public void close(final MessageContext context) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   getHeaders (SOAPHandler)
    /**
     ** Returns the header blocks that can be processed by this
     ** {@link SOAPHandler} instance.
     **
     ** @return                    the {@link Set} of {@link QName}s of header
     **                            blocks processed by this handler instance.
     **                            {@link QName} is the qualified name of the
     **                            outermost element of the {@link SOAPHandler}
     **                            block.
     **/
    @Override
    public Set<QName> getHeaders() {
      //throw new UnsupportedOperationException("Not supported yet.");
      return new HashSet<QName>(0);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Resolver
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>Resolver</code> implements the control over the handler chain set on
   ** proxy/dispatch objects at the time of their creation.
   ** <p>
   ** A {@link HandlerResolver} may be set on a <code>Service</code> using the
   ** <code>setHandlerResolver</code> method.
   ** <p>
   ** When the runtime invokes a {@link HandlerResolver}, it will pass it a
   ** {@link PortInfo} object containing information about the port that the
   ** proxy/dispatch object will be accessing.
   **
   **  @see javax.xml.ws.Service#setHandlerResolver
   */
  class Resolver implements HandlerResolver {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handleMessage (HandlerResolver)
    /**
     ** Returns the handler chain for the specified port.
     **
     ** @param  portInfo           providing information about the port being
     **                            accessed.
     **
     ** @return                    the {@link List} of {@link Handler}s to chain.
     */
    public List<Handler> getHandlerChain(final PortInfo portInfo) {
      final List<Handler> chain = new ArrayList<Handler>();
      chain.add(new Authenticator());
      return chain;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceConnector</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  resource           the {@link WebServiceResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public WebServiceConnector(final AbstractMetadataTask task, final WebServiceResource resource)
    throws TaskException {

    this(task, resource.serverName(), resource.serverPort(), resource.rootContext(), resource.principalName(), resource.principalPassword(), resource.secureSocket(), resource.localeLanguage(), resource.localeCountry(), resource.localeTimeZone(), resource.feature());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceConnector</code> which is associated with the
   ** specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  resource           the {@link WebServiceResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   ** @param  feature            the Metadata Descriptor providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   */
  public WebServiceConnector(final Loggable loggable, final WebServiceResource resource, final WebServiceFeature feature) {
    this(loggable, resource.serverName(), resource.serverPort(), resource.rootContext(), resource.principalName(), resource.principalPassword(), resource.secureSocket(), resource.localeLanguage(), resource.localeCountry(), resource.localeTimeZone(), feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceConnector</code> task adapter.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the SOAP Server is installed
   ** @param  serverPort         the port the SOAP server is listening on.
   ** @param  rootContext        the name of the context URI.
   **                            <br>
   **                            Sample value: <code>/oia/ws</code>
   ** @param  principalName      the name corresponding to the user with
   **                            administrator privileges.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the SOAP Server.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public WebServiceConnector(final AbstractMetadataTask task, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    this(task, serverName, Integer.parseInt(serverPort), rootContext, principalName, principalPassword, secureSocket, localeLanguage, localeCountry, localeTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceConnector</code> task adapter.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the SOAP Server is installed
   ** @param  serverPort         the port the SOAP server is listening on.
   ** @param  rootContext        the name of the context URI.
   **                            <br>
   **                            Sample value: <code>/oia/ws</code>
   ** @param  principalName      the name corresponding to the user with
   **                            administrator privileges.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the SOAP Server.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public WebServiceConnector(final AbstractMetadataTask task, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    this(task, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, localeLanguage, localeCountry, localeTimeZone, unmarshal(task, feature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceConnector</code> task adapter.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLoggable}.
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the SOAP Server is installed
   ** @param  serverPort         the port the SOAP server is listening on.
   ** @param  rootContext        the name of the context URI.
   **                            <br>
   **                            Sample value: <code>/oia/ws</code>
   ** @param  principalName      the name corresponding to the user with
   **                            administrator privileges.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the SOAP Server.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   ** @param  localeTimeZone     use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   ** @param  features           the {@link WebServiceFeature} providing the
   **                            target system specific features like
   **                            objectClasses, attribute id's etc.
   */
  public WebServiceConnector(final Loggable loggable, final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final String localeLanguage, final String localeCountry, final String localeTimeZone, final WebServiceFeature features) {
    // ensure inheritance
    super(loggable);

    this.serverName        = serverName;
    this.serverPort        = serverPort;
    this.rootContext       = rootContext;
    this.principalName     = principalName;
    this.principalPassword = principalPassword;
    this.secureSocket      = secureSocket;
    this.localeLanguage    = localeLanguage;
    this.localeCountry     = localeCountry;
    this.localeTimeZone    = localeTimeZone;

    // create the property mapping for the LDAP control
    this.feature           = features;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the SOAP Server used to connect to.
   **
   ** @return                    name of the SOAP Server used to connect to.
   */
  public final String serverName() {
    return this.serverName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the port of the SOAP Server used to connect to.
   **
   ** @return                    port of the SOAP Server used to connect to.
   */
  public final int serverPort() {
    return this.serverPort;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns whether the connection to the SOAP Server is secured by SSL.
   **
   ** @return                    <code>true</code> if the connection to the
   **                            SOAP Server is secured by SSL,
   **                            <code>false</code> otherwise.
   */
  public final boolean secureSocket() {
    return this.secureSocket;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Returns the root context of the SOAP Server used to connect to.
   **
   ** @return                    root context of the SOAP Server used to connect
   **                            to.
   */
  public final String rootContext() {
    return this.rootContext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the name of the security principal of the SOAP Server used to
   ** connect to.
   **
   ** @return                    the name of the security principal SOAP Server
   **                            used to connect to.
   */
  public final String principalName() {
    return this.principalName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password of the security principal of the SOAP Server used to
   ** connect to.
   **
   ** @return                    the password of the security principal SOAP
   **                            Server used to connect to.
   */
  public final String principalPassword() {
    return this.principalPassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeCountry
  /**
   ** Returns the country code of the SOAP Server used to connect to.
   **
   ** @return                    the country code of the SOAP Server used to
   **                            connect to.
   */
  public final String localeCountry() {
    return this.localeCountry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeLanguage
  /**
   ** Returns the language code of the SOAP Server used to connect to.
   **
   ** @return                    the language code of the SOAP Server used to
   **                            connect to.
   */
  public final String localeLanguage() {
    return this.localeLanguage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeTimeZone
  /**
   ** Returns the time zone of the SOAP Server used to connect to.
   **
   ** @return                    the time tone of the SOAP Server used to
   **                            connect to.
   */
  public final String localeTimeZone() {
    return this.localeTimeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator sign for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link WebServiceConstant#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link WebServiceConstant#MULTIVALUE_SEPARATOR} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link WebServiceConstant#MULTIVALUE_SEPARATOR_DEFAULT}
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String multiValueSeparator() {
    return this.feature.multiValueSeparator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectTimeout
  /**
   ** Returns the timeout period for establishment of the HTTP connection.
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
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link WebServiceConstant#CONNECT_TIMEOUT}.
   ** <p>
   ** If {@link WebServiceConstant#CONNECT_TIMEOUT} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link WebServiceConstant#CONNECT_TIMEOUT_DEFAULT}.
   **
   ** @return                    the timeout period for establishment of the
   **                            HTTP connection.
   */
  public final String connectTimeout() {
    return this.feature.connectTimeout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestTimeout
  /**
   ** Returns the timeout period for reading data on an already established HTTP
   ** connection.
   ** <p>
   ** A non-zero value specifies the timeout when reading from Input stream when
   ** a connection is established to a resource. If the timeout expires before
   ** there is data available for read, a java.net.SocketTimeoutException is
   ** raised. A timeout of zero is interpreted as an infinite timeout.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link WebServiceConstant#REQUEST_TIMEOUT}.
   ** <p>
   ** If {@link WebServiceConstant#REQUEST_TIMEOUT} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link WebServiceConstant#REQUEST_TIMEOUT_DEFAULT}.
   **
   ** @return                    the maximum time between establishing a
   **                            connection and receiving data from the
   **                            connection.
   */
  public final String requestTimeout() {
    return this.feature.requestTimeout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server part of the ldap url, http://host:port for the specified
   ** properties
   **
   ** @param  protocol           the protocol of the url to build.
   ** @param  host               the name of the host the service is running on.
   ** @param  port               the port the service is listining on.
   **
   ** @return                    the server part of the ldap url,
   **                            http://host:port
   */
  public static String serviceURL(final String protocol, final String host, final int port) {
    final StringBuilder service = new StringBuilder(protocol);
    service.append("://");
    if (host != null)
      service.append(host).append(SystemConstant.COLON).append(port);

    return service.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    final TableFormatter table = new TableFormatter()
    .header(SystemBundle.string(SystemMessage.PROPERTY_NAME))
    .header(SystemBundle.string(SystemMessage.PROPERTY_VALUE))
    ;
    StringUtility.formatValuePair(table, WebServiceResource.SERVER_NAME,        this.serverName());
    StringUtility.formatValuePair(table, WebServiceResource.SERVER_PORT,        this.serverPort());
    StringUtility.formatValuePair(table, WebServiceResource.ROOT_CONTEXT,       this.rootContext());
    StringUtility.formatValuePair(table, WebServiceResource.PRINCIPAL_NAME,     this.principalName());
    StringUtility.formatValuePair(table, WebServiceResource.PRINCIPAL_PASSWORD, this.principalPassword());
    StringUtility.formatValuePair(table, WebServiceResource.SECURE_SOCKET,      this.secureSocket());
    StringUtility.formatValuePair(table, WebServiceResource.LOCALE_COUNTRY,     this.localeCountry());
    StringUtility.formatValuePair(table, WebServiceResource.LOCALE_LANGUAGE,    this.localeLanguage());
    StringUtility.formatValuePair(table, WebServiceResource.LOCALE_TIMEZONE,    this.localeTimeZone());

    StringBuilder builder = new StringBuilder();
    table.print(builder);
    builder.append(this.feature.toString());
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the SOAP context.
   ** <p>
   ** The URL consists of the server part of the http url, http://host:port and
   ** the absolute path to the entry. The entry is pre fixed with the context
   ** root of the connection.
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the IT Resource definition associated with this instance.
   **
   ** @return                    the context URl the context root configured in
   **                            the IT Resource definition associated with this
   **                            instance.
   **
   ** @throws WebServiceException if the given distinguished name cannot
   **                             converted to a <code>application/x-www-form-urlencoded</code>
   **                             MIME format.
   */
  public URL contextURL()
    throws WebServiceException {

    return contextURL(this.secureSocket, SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextWSDL
  /**
   ** Return the fullqualified URL to the SOAP context WSDL.
   ** <p>
   ** The URL consists of the server part of the http url, http://host:port and
   ** the absolute path to the WSDL. The entry is pre fixed with the context
   ** root of the connection.
   **
   ** @param  contextPath        a component of a SOAP query
   **
   ** @return                    the full qualified SOAP URL.
   **
   ** @throws WebServiceException if the given context path cannot converted to a
   **                             <code>application/x-www-form-urlencoded</code>
   **                             MIME format.
   */
  public URL contextWSDL(final String contextPath)
    throws WebServiceException {

    // this is a key point:
    // We must not initialize service with target URL, because then the WSDL
    // would be downloaded from the HTTPS server, before we initialize our
    // context properties.
    return contextURL(false, String.format("%s?%s", contextPath, WSDL_SUFFIX));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the SOAP context.
   ** <p>
   ** The URL consists of the server part of the http url, http://host:port and
   ** the absolute path to the entry. The entry is pre fixed with the context
   ** root of the connection.
   **
   ** @param  secure             <code>true</code> if the transport protocol
   **                            needs encryption (SSL or TLS).
   ** @param  contextPath        a component of a SOAP query.
   **
   ** @return                    the full qualified SOAP URL.
   **
   ** @throws WebServiceException if the given context path cannot converted to a
   **                             <code>application/x-www-form-urlencoded</code>
   **                             MIME format.
   */
  public URL contextURL(final boolean secure, final String contextPath)
    throws WebServiceException {

    // create the service url (server name and port) prefixed with the
    // protocol check if the service url ends already with a slash '/'
    final StringBuilder url = new StringBuilder(serviceURL(secure ? WebServiceConstant.PROTOCOL_DEFAULT_SECURE : WebServiceConstant.PROTOCOL_DEFAULT, this.serverName, this.serverPort));
    String encodedPath = denormalizePath(contextPath);
    if (encodedPath.charAt(0) != SystemConstant.SLASH)
        url.append(SystemConstant.SLASH);
      url.append(encodedPath);

    // return the resulting url by escaping all space characters that may
    // contained by the appropriate encoded character
    try {
      return new URL(url.toString());
    }
    catch (MalformedURLException e) {
      throw new WebServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   denormalizePath
  /**
   ** Forms the basis of building the hierarchical path to the SOAP context.
   **
   ** @param  context            Contains the elements in the SOAP context path,
   **                            deepest one last. The String must be of format
   **
   ** @return                    String of the canonical path (including the
   **                            root context), e.g. oia/ws/roleHarvester.
   */
  public String denormalizePath(final String context) {
    final StringBuilder path = new StringBuilder();
    // if context is empty or null
    if (StringUtility.isEmpty(context)) {
      path.append(this.rootContext);
    }
    // if context value is treated as a absolute path of the context
    else if (context.charAt(0) == SystemConstant.SLASH)
      path.append(context);
    // if context value is treated as a relative path of the context
    else {
      path.append(this.rootContext);
      if (!(this.rootContext.charAt(this.rootContext.length() - 1) == SystemConstant.SLASH))
        path.append(SystemConstant.SLASH);
      path.append(context);
    }
    return path.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createService
  /**
   ** Creates a {@link Service} instance.
   ** <p>
   ** The created instance is configured without any web service features. The
   ** specified WSDL document location and service qualified name MUST uniquely
   ** identify a wsdl:service element.
   **
   ** @param  contextPath        the relative context path of the service to
   **                            obtain the WSDL document location from.
   ** @param  namespace          the Namespace URI of the {@link QName} for the
   **                            service to create.
   ** @param  serviceName        the local part of the {@link QName} for the
   **                            service to create.
   **
   ** @return                     a {@link Service} instance for use with
   **                             objects of the user's choosing.
   **
   ** @throws WebServiceException if any error in creation of the specified
   **                             service.
   */
  public Service createService(final String contextPath, final String namespace, final String serviceName)
    throws WebServiceException {

    final QName service = new QName(namespace, serviceName);
    return createService(contextPath, service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createService
  /**
   ** Creates a {@link Service} instance.
   ** <p>
   ** The created instance is configured without any web service features. The
   ** specified WSDL document location and service qualified name MUST uniquely
   ** identify a wsdl:service element.
   **
   ** @param  contextPath        the relative context path of the service to
   **                            obtain the WSDL document location from.
   ** @param  serviceName        the local part of the {@link QName} for the
   **                            service to create.
   **
   ** @return                     a {@link Service} instance for use with
   **                             objects of the user's choosing.
   **
   ** @throws WebServiceException if any error in creation of the specified
   **                             service.
   */
  public Service createService(final String contextPath, final QName serviceName)
    throws WebServiceException {

    Service endpoint = null;
    try {
      // this is a key point:
      // We must not initialize service with target URL, because then the WSDL
      // would be downloaded from the HTTPS server, before we initialize our
      // context properties.
      endpoint = Service.create(contextWSDL(contextPath), serviceName);
      endpoint.setHandlerResolver(new Resolver());
    }
    catch (javax.xml.ws.WebServiceException e) {
      final Throwable t = e.getCause();
      // if an UnknownHostException is the cause than something is misconfigured
      if (t instanceof UnknownHostException) {
        throw new WebServiceException(WebServiceError.CONNECTION_UNKNOWN_HOST, this.serverName);
      }
      // if an ConnectException either port is misconfigured or the requested
      // service is not available in general
      else if (t instanceof ConnectException) {
        // we need to upcast the exception to create the exception wrapper
        // correctly
        final ConnectException c = (ConnectException)t;
        throw new WebServiceException(c.getLocalizedMessage(), c);
      }
      // if an IOException a general erro occured in connection
      else if (t instanceof IOException) {
        throw new WebServiceException(t);
      }
      throw new WebServiceException(e);
    }
    return endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDispatch
  /** Create the proxy.
   ** <p>
   ** Parameter endpointInterface specifies the service endpoint interface that
   ** is supported by the returned proxy the parameter endpointReference
   ** specifies the endpoint that will be invoked by the returned proxy.
   ** <br>
   ** If there are any reference parameters in the endpointReference, then those
   ** reference parameters MUST appear as SOAP headers, indicating them to be
   ** reference parameters, on all messages sent to the endpoint. The
   ** endpointInterface's address MUST be used for invocations on the endpoint.
   ** <br>
   ** The interface specifies the service endpoint interface that is supported
   ** by the returned proxy. In the implementation of this method, the JAX-WS
   ** runtime system takes the responsibility of selecting a protocol binding
   ** (and a port) and configuring the proxy accordingly from the WSDL
   ** associated with this Service instance or from the metadata from the
   ** servicePort.
   ** <br>
   ** If the Service instance has a WSDL and the endpointReference metadata also
   ** has a WSDL, then the WSDL from this instance MUST be used. If this Service
   ** instance does not have a WSDL and the endpointReference does have a WSDL,
   ** then the WSDL from the servicePort MAY be used.
   **
   ** @param  endpointReference  the {@link URL} the service WSDL is provided.
   ** @param  endpointInterface  the qualified name of the service endpoint in
   **                            the WSDL service description.
   ** @param  mode               controls whether the created dispatch instance
   **                            is message or payload oriented, i.e. whether
   **                            the client will work with complete protocol
   **                            messages or message payloads. E.g. when using
   **                            the SOAP protocol, this parameter controls
   **                            whether the client will work with SOAP messages
   **                            or the contents of a SOAP body.
   **
   ** @return                    a {@link Dispatch} instance for use with
   **                            objects of the user's choosing.
   **
   ** @throws WebServiceException <ul>
   **                               <li>If there is any missing WSDL metadata as
   **                                   required by this method.
   **                               <li>If the endpointReference metadata does
   **                                   not match the serviceName or portName of
   **                                   a WSDL associated with this Service
   **                                   instance.
   **                               <li>If the portName cannot be determined
   **                                   from the endpointReference metadata.
   **                               <li>If any error in the creation of the
   **                                   {@link Dispatch} object.
   **                               <li>If a feature is enabled that is not
   **                                   compatible with this port or is
   **                                   unsupported.
   **                             </ul>
   */
  public Dispatch<Source> createDispatch(final Service endpointReference, final String endpointInterface, final Service.Mode mode)
    throws WebServiceException {

    final String           namespace  = endpointReference.getServiceName().getNamespaceURI();
    final QName            port       = new QName(namespace, endpointInterface);
    return createDispatch(endpointReference, port, mode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDispatch
  /** Create the proxy.
   ** <p>
   ** Parameter endpointInterface specifies the service endpoint interface that
   ** is supported by the returned proxy the parameter endpointReference
   ** specifies the endpoint that will be invoked by the returned proxy.
   ** <br>
   ** If there are any reference parameters in the endpointReference, then those
   ** reference parameters MUST appear as SOAP headers, indicating them to be
   ** reference parameters, on all messages sent to the endpoint. The
   ** endpointInterface's address MUST be used for invocations on the endpoint.
   ** <br>
   ** The interface specifies the service endpoint interface that is supported
   ** by the returned proxy. In the implementation of this method, the JAX-WS
   ** runtime system takes the responsibility of selecting a protocol binding
   ** (and a port) and configuring the proxy accordingly from the WSDL
   ** associated with this Service instance or from the metadata from the
   ** servicePort.
   ** <br>
   ** If the Service instance has a WSDL and the endpointReference metadata also
   ** has a WSDL, then the WSDL from this instance MUST be used. If this Service
   ** instance does not have a WSDL and the endpointReference does have a WSDL,
   ** then the WSDL from the servicePort MAY be used.
   **
   ** @param  endpointReference  the {@link URL} the service WSDL is provided.
   ** @param  endpointInterface  the qualified name of the target service
   **                            endpoint in the WSDL service description.
   ** @param  mode               controls whether the created dispatch instance
   **                            is message or payload oriented, i.e. whether
   **                            the client will work with complete protocol
   **                            messages or message payloads. E.g. when using
   **                            the SOAP protocol, this parameter controls
   **                            whether the client will work with SOAP messages
   **                            or the contents of a SOAP body.
   **
   ** @return                    a {@link Dispatch} instance for use with
   **                            objects of the user's choosing.
   **
   ** @throws WebServiceException <ul>
   **                               <li>If there is any missing WSDL metadata as
   **                                   required by this method.
   **                               <li>If the endpointReference metadata does
   **                                   not match the serviceName or portName of
   **                                   a WSDL associated with this Service
   **                                   instance.
   **                               <li>If the portName cannot be determined
   **                                   from the EndpointReference metadata.
   **                               <li>If any error in the creation of the
   **                                   {@link Dispatch} object.
   **                               <li>If a feature is enabled that is not
   **                                   compatible with this port or is
   **                                   unsupported.
   **                             </ul>
   */
  public Dispatch<Source> createDispatch(final Service endpointReference, final QName endpointInterface, final Service.Mode mode)
    throws WebServiceException {

    Dispatch<Source> dispatcher = null;
    try {
      dispatcher = endpointReference.createDispatch(endpointInterface, Source.class, mode);
      // configure the request context of the generated proxy properly
      final Map<String, Object> context = dispatcher.getRequestContext();
      context.put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.valueOf(connectTimeout()));
      context.put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.valueOf(requestTimeout()));
    }
    catch (javax.xml.ws.WebServiceException e) {
      if (e instanceof ClientTransportException) {
        handleTransportException((ClientTransportException)e);
      }
      throw new WebServiceException(e);
    }
    return dispatcher;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDispatch
  /** Create the proxy instance for use with JAXB generated objects.
   **
   ** @param  endpointReference  the {@link URL} the service WSDL is provided.
   ** @param  endpointInterface  the qualified name of the target service
   **                            endpoint in the WSDL service description.
   ** @param  context            the JAXB context used to marshall and
   **                            unmarshall messages or message payloads.
   ** @param  mode               controls whether the created dispatch instance
   **                            is message or payload oriented, i.e. whether
   **                            the client will work with complete protocol
   **                            messages or message payloads. E.g. when using
   **                            the SOAP protocol, this parameter controls
   **                            whether the client will work with SOAP messages
   **                            or the contents of a SOAP body.
   **
   ** @return                    a {@link Dispatch} instance for use with
   **                            objects of the user's choosing.
   **
   ** @throws WebServiceException <ul>
   **                               <li>If there is any missing WSDL metadata as
   **                                   required by this method.
   **                               <li>If the endpointReference metadata does
   **                                   not match the serviceName or portName of
   **                                   a WSDL associated with this Service
   **                                   instance.
   **                               <li>If the portName cannot be determined
   **                                   from the EndpointReference metadata.
   **                               <li>If any error in the creation of the
   **                                   {@link Dispatch} object.
   **                               <li>If a feature is enabled that is not
   **                                   compatible with this port or is
   **                                   unsupported.
   **                             </ul>
   **/
  public Dispatch<Object> createDispatch(final Service endpointReference, final QName endpointInterface, JAXBContext context, final Service.Mode mode)
    throws WebServiceException {

    Dispatch<Object> dispatcher = null;
    try {
      dispatcher = endpointReference.createDispatch(endpointInterface, context, mode);
      // configure the request context of the generated proxy properly
      final Map<String, Object> request = dispatcher.getRequestContext();
      request.put(BindingProviderProperties.CONNECT_TIMEOUT, Integer.valueOf(connectTimeout()));
      request.put(BindingProviderProperties.REQUEST_TIMEOUT, Integer.valueOf(requestTimeout()));
    }
    catch (javax.xml.ws.WebServiceException e) {
      if (e instanceof ClientTransportException) {
        handleTransportException((ClientTransportException)e);
      }
      throw new WebServiceException(e);
    }
    return dispatcher;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokePayload
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  namespace          the Namespace URI of the {@link QName} the
   **                            <code>serviceName</code> and
   **                            <code>servicePort</code> belonging to.
   ** @param  serviceName        the name for the service to call.
   ** @param  servicePort        the name for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @return                    the response message or message payload to the
   **                            operation invocation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public Source serviceInvokePayload(final String contextPath, final String namespace, final String serviceName, final String servicePort, final Source request)
    throws WebServiceException {

    final QName service = new QName(namespace, serviceName);
    final QName port    = new QName(namespace, servicePort);
    return serviceInvokePayload(contextPath, service, port, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokePayload
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  service            the {@link QName} for the service to call.
   ** @param  port               the {@link QName} for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @return                    the response message or message payload to the
   **                            operation invocation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public Source serviceInvokePayload(final String contextPath, final QName service, final QName port, final Source request)
    throws WebServiceException {

    final Service           endpoint    = Service.create(contextWSDL(contextPath), service);
    final Dispatch<Source>  dispatcher  = endpoint.createDispatch(port, Source.class, Service.Mode.PAYLOAD);
    // invoke service operation, adding appropriate credentials
    return serviceInvoke(dispatcher, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokePayload
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  service            the {@link QName} for the service to call.
   ** @param  port               the {@link QName} for the port in the service
   **                            to call.
   ** @param  context            the {@link JAXBContext} used to marshall and
   **                            unmarshall the message payload.
   ** @param  payload            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @return                    the response message or message payload to the
   **                            operation invocation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public Object serviceInvokePayload(final String contextPath, final QName service, final QName port, final JAXBContext context, final Object payload)
    throws WebServiceException {

    final Service           endpoint    = Service.create(contextWSDL(contextPath), service);
    final Dispatch<Object>  dispatcher  = endpoint.createDispatch(port, context, Service.Mode.PAYLOAD);
    // invoke service operation, adding appropriate credentials
    return serviceInvoke(dispatcher, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeMessage
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  namespace          the Namespace URI of the {@link QName} the
   **                            <code>serviceName</code> and
   **                            <code>servicePort</code> belonging to.
   ** @param  serviceName        the name for the service to call.
   ** @param  servicePort        the name for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @return                    the response message or message payload to the
   **                            operation invocation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public Source serviceInvokeMessage(final String contextPath, final String namespace, final String serviceName, final String servicePort, final Source request)
    throws WebServiceException {

    final QName service = new QName(namespace, serviceName);
    final QName port    = new QName(namespace, servicePort);
    return serviceInvokeMessage(contextPath, service, port, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeMessage
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  service            the {@link QName} for the service to call.
   ** @param  port               the {@link QName} for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @return                    the response message or message payload to the
   **                            operation invocation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public Source serviceInvokeMessage(final String contextPath, final QName service, final QName port, final Source request)
    throws WebServiceException {

    final Service           endpoint    = Service.create(contextWSDL(contextPath), service);
    final Dispatch<Source>  dispatcher  = endpoint.createDispatch(port, Source.class, Service.Mode.MESSAGE);
    // invoke service operation, adding appropriate credentials
    return serviceInvoke(dispatcher, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvoke
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  proxy              the {@link Dispatch} interface providing
   **                            support for the dynamic invocation of the
   **                            service endpoint operation.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @return                    the response message or message payload to the
   **                            operation invocation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public Source serviceInvoke(final Dispatch<Source> proxy, final Source request)
    throws WebServiceException {

    try {
      // invoke the servive
      return proxy.invoke(request);
    }
    catch (javax.xml.ws.WebServiceException e) {
      if (e instanceof ClientTransportException) {
        handleTransportException((ClientTransportException)e);
      }
      Throwable t = e.getCause();
      // if a timeout is the cause of the wrapping exception the expected
      // response time is exceeded
      if (t instanceof SocketTimeoutException) {
        throw new WebServiceException((SocketTimeoutException)t);
      }
      // if an IOException is the cause of the wrapping exception an issue in
      // connectivity happend to figure out the real root cause we will digg in
      // the hierarchy to find the correct one
      if (t instanceof IOException) {
        if (t.getCause() != null)
          t = t.getCause();

        if (t instanceof ConnectException)
          throw new WebServiceException((ConnectException)t);

        throw new WebServiceException(t);
      }
      else
        throw new WebServiceException(e);
    }
    catch (Exception e) {
      throw new WebServiceException(e);
    }
    catch (Throwable t) {
      throw new WebServiceException(t);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvoke
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  proxy              the {@link Dispatch} interface providing
   **                            support for the dynamic invocation of the
   **                            service endpoint operation.
   ** @param  payload            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @return                    the response message or message payload to the
   **                            operation invocation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public Object serviceInvoke(final Dispatch<Object> proxy, final Object payload)
    throws WebServiceException {

    try {
      // invoke the servive
      return proxy.invoke(payload);
    }
    catch (javax.xml.ws.WebServiceException e) {
      if (e instanceof ClientTransportException) {
        handleTransportException((ClientTransportException)e);
      }
      Throwable t = e.getCause();
      // if a timeout is the cause of the wrapping exception the expected
      // response time is exceeded
      if (t instanceof SocketTimeoutException) {
        throw new WebServiceException((SocketTimeoutException)t);
      }
      // if an IOException is the cause of the wrapping exception an issue in
      // connectivity happend to figure out the real root cause we will digg in
      // the hierarchy to find the correct one
      if (t instanceof IOException) {
        if (t.getCause() != null)
          t = t.getCause();

        if (t instanceof ConnectException)
          throw new WebServiceException((ConnectException)t);

        throw new WebServiceException(t);
      }
      else
        throw new WebServiceException(e);
    }
    catch (Exception e) {
      throw new WebServiceException(e);
    }
    catch (Throwable t) {
      throw new WebServiceException(t);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWayPayload
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  namespace          the Namespace URI of the {@link QName} the
   **                            <code>serviceName</code> and
   **                            <code>servicePort</code> belonging to.
   ** @param  serviceName        the name for the service to call.
   ** @param  servicePort        the name for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWayPayload(final String contextPath, final String namespace, final String serviceName, final String servicePort, final Source request)
    throws WebServiceException {

    final QName service = new QName(namespace, serviceName);
    final QName port    = new QName(namespace, servicePort);
    serviceInvokeOneWayPayload(contextPath, service, port, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWayPayload
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  service            the {@link QName} for the service to call.
   ** @param  port               the {@link QName} for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWayPayload(final String contextPath, final QName service, final QName port, final Source request)
    throws WebServiceException {

    final Service           endpoint    = Service.create(contextWSDL(contextPath), service);
    final Dispatch<Source>  dispatcher  = endpoint.createDispatch(port, Source.class, Service.Mode.PAYLOAD);
    // invoke service operation, adding appropriate credentials
    serviceInvokeOneWay(dispatcher, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWayPayload
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  service            the {@link QName} for the service to call.
   ** @param  port               the {@link QName} for the port in the service
   **                            to call.
   ** @param  context            the {@link JAXBContext} used to marshall and
   **                            unmarshall the message payload.
   ** @param  payload            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWayPayload(final String contextPath, final QName service, final QName port, final JAXBContext context, final Object payload)
    throws WebServiceException {

    final Service           endpoint    = Service.create(contextWSDL(contextPath), service);
    final Dispatch<Object>  dispatcher  = endpoint.createDispatch(port, context, Service.Mode.PAYLOAD);
    // invoke service operation, adding appropriate credentials
    serviceInvokeOneWay(dispatcher, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWayMessage
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  namespace          the Namespace URI of the {@link QName} the
   **                            <code>serviceName</code> and
   **                            <code>servicePort</code> belonging to.
   ** @param  serviceName        the name for the service to call.
   ** @param  servicePort        the name for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWayMessage(final String contextPath, final String namespace, final String serviceName, final String servicePort, final Source request)
    throws WebServiceException {

    final QName service = new QName(namespace, serviceName);
    final QName port    = new QName(namespace, servicePort);
    serviceInvokeOneWayMessage(contextPath, service, port, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWayMessage
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  service            the {@link QName} for the service to call.
   ** @param  port               the {@link QName} for the port in the service
   **                            to call.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWayMessage(final String contextPath, final QName service, final QName port, final Source request)
    throws WebServiceException {

    final Service           endpoint    = Service.create(contextWSDL(contextPath), service);
    final Dispatch<Source>  dispatcher  = endpoint.createDispatch(port, Source.class, Service.Mode.MESSAGE);
    // invoke service operation, adding appropriate credentials
    serviceInvokeOneWay(dispatcher, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWay
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  contextPath        a component of a SOAP query.
   ** @param  service            the {@link QName} for the service to call.
   ** @param  port               the {@link QName} for the port in the service
   **                            to call.
   ** @param  context            the {@link JAXBContext} used to marshall and
   **                            unmarshall the message payload.
   ** @param  payload            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWay(final String contextPath, final QName service, final QName port, final JAXBContext context, final Object payload)
    throws WebServiceException {

    final Service           endpoint    = Service.create(contextWSDL(contextPath), service);
    final Dispatch<Object>  dispatcher  = endpoint.createDispatch(port, context, Service.Mode.PAYLOAD);
    // invoke service operation, adding appropriate credentials
    serviceInvokeOneWay(dispatcher, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWay
  /**
   ** Invoke a service operation synchronously.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  proxy              the {@link Dispatch} interface providing
   **                            support for the dynamic invocation of the
   **                            service endpoint operation.
   ** @param  request            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWay(final Dispatch<Source> proxy, final Source request)
    throws WebServiceException {

    try {
      // invoke the servive
      proxy.invoke(request);
    }
    catch (javax.xml.ws.WebServiceException e) {
      if (e instanceof ClientTransportException) {
        handleTransportException((ClientTransportException)e);
      }
      Throwable t = e.getCause();
      // if a timeout is the cause of the wrapping exception the expected
      // response time is exceeded
      if (t instanceof SocketTimeoutException) {
        throw new WebServiceException((SocketTimeoutException)t);
      }
      // if an IOException is the cause of the wrapping exception an issue in
      // connectivity happend to figure out the real root cause we will digg in
      // the hierarchy to find the correct one
      if (t instanceof IOException) {
        if (t.getCause() != null)
          t = t.getCause();

        if (t instanceof ConnectException)
          throw new WebServiceException((ConnectException)t);

        throw new WebServiceException(t);
      }
      else
        throw new WebServiceException(e);
    }
    catch (Exception e) {
      throw new WebServiceException(e);
    }
    catch (Throwable t) {
      throw new WebServiceException(t);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceInvokeOneWay
  /**
   ** Invoke a service operation synchronously one way.
   ** <p>
   ** The caller is responsible for ensuring that the <code>request</code>
   ** object when marshalled is formed according to the requirements of the
   ** protocol binding in use.
   **
   ** @param  proxy              the {@link Dispatch} interface providing
   **                            support for the dynamic invocation of the
   **                            service endpoint operation.
   ** @param  payload            an object that will form the message or payload
   **                            of the message used to invoke the operation.
   **
   ** @throws WebServiceException if a fault occurs during communication with the
   **                             the service
   */
  public void serviceInvokeOneWay(final Dispatch<Object> proxy, final Object payload)
    throws WebServiceException {

    try {
      // invoke the servive in one way
      proxy.invokeOneWay(payload);
    }
    catch (javax.xml.ws.WebServiceException e) {
      if (e instanceof ClientTransportException) {
        handleTransportException((ClientTransportException)e);
      }
      Throwable t = e.getCause();
      // if a timeout is the cause of the wrapping exception the expected
      // response time is exceeded
      if (t instanceof SocketTimeoutException) {
        throw new WebServiceException((SocketTimeoutException)t);
      }
      // if an IOException is the cause of the wrapping exception an issue in
      // connectivity happend to figure out the real root cause we will digg in
      // the hierarchy to find the correct one
      if (t instanceof IOException) {
        if (t.getCause() != null)
          t = t.getCause();

        if (t instanceof ConnectException)
          throw new WebServiceException((ConnectException)t);

        throw new WebServiceException(t);
      }
      else
        throw new WebServiceException(e);
    }
    catch (Exception e) {
      throw new WebServiceException(e);
    }
    catch (Throwable t) {
      throw new WebServiceException(t);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link WebServiceFeature} from a path.
   **
   ** @param  task               the {@link AbstractMetadataTask} where the
   **                            object to create will belong to.
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **
   ** @return                    the {@link WebServiceFeature} created from the
   **                            specified propertyFile.
   **
   ** @throws WebServiceException in the event of misconfiguration (such as
   **                             failure to set an essential property) or if
   **                             initialization fails.
   */
  protected static WebServiceFeature unmarshal(final AbstractMetadataTask task, final String path)
    throws WebServiceException {

    WebServiceFeature feature = new WebServiceFeature(task);
    try {
      final MDSSession session  = task.createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      WebServiceFeatureFactory.configure(feature, document);
    }
    catch (ReferenceException e) {
      throw new WebServiceException(e);
    }
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleTransportException
  protected void handleTransportException(final ClientTransportException e)
    throws WebServiceException {

    final String key  = e.getKey();
    if ("http.status.code".equals(key)) {
      final Object[] code = e.getArguments();
      switch (((Integer)code[0]).intValue()) {
        case HttpServletResponse.SC_FORBIDDEN             : throw new WebServiceException(WebServiceError.CONNECTION_AUTHORIZATION,  this.principalName);
        case HttpServletResponse.SC_UNAUTHORIZED          : throw new WebServiceException(WebServiceError.CONNECTION_AUTHENTICATION, this.principalName);
        case HttpServletResponse.SC_INTERNAL_SERVER_ERROR : throw new WebServiceException(WebServiceError.CONNECTION_UNAVAILABLE);
      }
    }
  }
}