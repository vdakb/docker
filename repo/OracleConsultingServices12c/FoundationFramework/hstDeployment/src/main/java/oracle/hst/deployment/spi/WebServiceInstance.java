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

    File        :   WebServiceInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WebServiceInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import javax.xml.bind.JAXBContext;

import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPEnvelope;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.HandlerResolver;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class WebServiceInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String WSSE_PREFIX_DEFAULT        = "wsse";
  static final String WSSE_NAMESPACE_DEFAULT     = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
  static final String WSU_PREFIX_DEFAULT         = "xmlns:wsu";
  static final String WSU_NAMESPACE_DEFAULT      = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
  static final String UTP_NAMESPACE_DEFAULT      = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0";
  static final String PASSWORD_TEXTVALUE_DEFAULT = "PasswordText";

  static final String ELEMENT_SECURITY           = "Security";
  static final String ELEMENT_USERNAME_TOKEN     = "UsernameToken";
  static final String ELEMENT_USERNAME           = "Username";
  static final String ELEMENT_PASSWORD           = "Password";
  static final String ATTRIBUTE_PASSWORD_TYPE    = "Type";

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  protected JAXBContext context;

  protected String      username;
  protected String      password;

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
          final SOAPElement security      = header.addChildElement(ELEMENT_SECURITY, WSSE_PREFIX_DEFAULT, WSSE_NAMESPACE_DEFAULT);
          final SOAPElement usernameToken = security.addChildElement(ELEMENT_USERNAME_TOKEN, WSSE_PREFIX_DEFAULT);
          usernameToken.addAttribute(new QName(WSU_PREFIX_DEFAULT), WSU_NAMESPACE_DEFAULT);
          usernameToken.addChildElement(ELEMENT_USERNAME, WSSE_PREFIX_DEFAULT).addTextNode(WebServiceInstance.this.username);

          final SOAPElement password = usernameToken.addChildElement(ELEMENT_PASSWORD, WSSE_PREFIX_DEFAULT);
          //http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
          password.setAttribute(ATTRIBUTE_PASSWORD_TYPE, String.format("%s#%s", UTP_NAMESPACE_DEFAULT, PASSWORD_TEXTVALUE_DEFAULT));
          password.addTextNode(WebServiceInstance.this.password);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      else {
        // this handler does nothing with the response from the Web Service so
        ;
      }
      return outbound;
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
   ** Constructs a <code>WebServiceInstance</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public WebServiceInstance() {
    // ensure inheritance
    super();

/*
    // the JAXBContext instance is initialized from a list of colon separated
    // Java package names.
    // Each java package contains JAXB mapped classes, schema-derived classes
    // and/or user annotated classes. Additionally, the java package may
    // contain JAXB package annotations that must be processed.
    // (see JLS 3rd Edition, Section 7.4.1. Package Annotations).
    try {
      this.context = JAXBContext.newInstance(packageName, classLoader);
    }
    catch (JAXBException e) {
      throw new RuntimeException(e);
    }
*/
  }
}
