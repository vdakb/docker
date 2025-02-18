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

import java.util.Map;

import java.net.URL;
import java.net.MalformedURLException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class WebServiceResource
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>WebServiceResource</code> implements the base functionality of a
 ** HTTP Server IT Resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class WebServiceResource extends AbstractResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the array with the attributes for the IT Resource */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(SERVER_NAME,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,      ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_FEATURE,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_NAME,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_COUNTRY,     ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,    ITResourceAttribute.OPTIONAL)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            IT Resource instance where this wrapper belongs
   **                            to.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public WebServiceResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** the Oracle Identity Manager. Only instance key and instance name are
   ** obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            Allowed object {@link Long}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries.
   */
  public WebServiceResource(final Loggable loggable, final Long instance)
    throws TaskException {

    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  instance           the public identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            Allowed object {@link String}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries or
   **                            one or more attributes are missing on the
   **                            <code>IT Resource</code> Definition.
   */
  public WebServiceResource(final Loggable loggable, final String instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The IT Resource will be populated from the specified parameters.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   ** @param  serverPort         the port the Web server is listening on.
   ** @param  rootContext        the name of the context URI.
   **                            <br>
   **                            Sample value: <code>/oia/ws</code>
   ** @param  principalName      the name corresponding to the user with
   **                            administrator privileges.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the Web Server.
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
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public WebServiceResource(final Loggable loggable, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final String localeLanguage, final String localeCountry, final String localeTimeZone)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(SERVER_NAME,        serverName);
    attribute(SERVER_PORT,        serverPort);
    attribute(ROOT_CONTEXT,       rootContext);
    attribute(PRINCIPAL_NAME,     principalName);
    attribute(PRINCIPAL_PASSWORD, principalPassword);
    attribute(SECURE_SOCKET,      secureSocket ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(LOCALE_LANGUAGE,    localeLanguage);
    attribute(LOCALE_COUNTRY,     localeCountry);
    attribute(LOCALE_TIMEZONE,    localeTimeZone);

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceWSDL
  /**
   ** Constructs an service WSDL URL to bind to.
   **
   ** @return                    the service WSDL URL to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @throws MalformedURLException if an unknown protocol is specified.
   */
  public URL serviceWSDL()
    throws MalformedURLException {

    // Create a URL from the parts describe by host, port and root context
    return serviceURL(this.rootContext() + "?wsdl");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Constructs an service URL to bind to.
   **
   ** @param  rootContext        the name of the context URI.
   **                            <br>
   **                            Sample value: <code>/oia/ws</code>
   **
   ** @return                    the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **
   ** @throws MalformedURLException if an unknown protocol is specified.
   */
  protected URL serviceURL(final String rootContext)
    throws MalformedURLException {

    // Create a URL from the parts describe by host, port and root context
    return new URL(this.secureSocket() ? "https" : "http", this.serverName(), this.serverPort(), rootContext);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstrcatConnector)
  /**
   ** Returns the array with names which should be populated from the
   ** IT Resource definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  public AbstractAttribute[] attributes() {
    return attribute;
  }
}