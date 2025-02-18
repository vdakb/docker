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

    File        :   IdentityServerResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityServerResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerResource
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityServerResource</code> implements the base functionality
 ** of a Identity Manager Managed Server IT Resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServerResource extends AbstractResource {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the type of
   ** the Identity Manager Managed Server where this IT Resource will be working
   ** on.
   */
  public static final String SERVER_TYPE              = "Server Type";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the property
   ** value to set.
   */
  public static final String SERVER_TYPE_PROPERTY      = "Server Type Property";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the location
   ** of JASS configuration for the Identity Manager Managed Server where this
   ** IT Resource will be working on.
   */
  public static final String SECURITY_CONFIG           = "Security Config";

  /**
   ** Attribute tag which must be defined on an IT Resource to specify the user
   ** name of the target system account to be used to establish a connection to
   ** the Managed Server.
   */
  public static final String SERVER_PRINCIPAL_NAME     = "Server Principal Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to specify the user
   ** name of the target system account to be used to establish a connection to
   ** the Managed Server.
   */
  public static final String SERVER_PRINCIPAL_PASSWORD  = "Server Principal Password";

  /**
   ** Attribute tag which must be defined on an IT Resource to specify the user
   ** name of the target system account to be used to establish a connection.
   */
  public static final String CONTEXT_PRINCIPAL_NAME     = "Context Principal Name";

  /**
   ** Attribute tag which must be defined on an IT Resource to specify the
   ** password of the target system account specified by the
   ** #PRINCIPAL_NAME parameter.
   */
  public static final String CONTEXT_PRINCIPAL_PASSWORD = "Context Principal Password";

  /** the array with the attributes for the IT Resource */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(SERVER_NAME,                ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,                ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_TYPE,                ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURITY_CONFIG,            ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,              ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,               ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PRINCIPAL_NAME,      ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PRINCIPAL_PASSWORD,  ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CONTEXT_PRINCIPAL_NAME,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CONTEXT_PRINCIPAL_PASSWORD, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,            ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_COUNTRY,             ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,            ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_TYPE_PROPERTY,       ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(SERVER_FEATURE,             ITResourceAttribute.OPTIONAL)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServerResource</code> which is associated
   ** with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            IT Resource instance where this wrapper belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element if of type {@link String} for the key
   **                            and {@link String} for the value.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private IdentityServerResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServerResource</code> which is associated
   ** with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given instance key.
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
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries.
   */
  private IdentityServerResource(final Loggable loggable, final Long instance)
    throws TaskException {

    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServerResource</code> which is associated
   ** with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the public identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries or
   **                            one or more attributes are missing on the
   **                            <code>IT Resource</code> Definition.
   */
  private IdentityServerResource(final Loggable loggable, final String instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServerResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The IT Resource will be populated from the specified parameters.
   **
   ** @param  loggable                 the {@link Loggable} which has
   **                                  instantiated this
   **                                  <code>IT Resource</code> configuration
   **                                  wrapper.
   **                                  <br>
   **                                  Allowed object is {@link Loggable}.
   ** @param  serverName               the host name or IP address of the
   **                                  Managed Server Identity Manager is
   **                                  deployed on.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverPort               the port the Managed Server is listening
   **                                  on.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverType               the type the Managed Server.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  securityConfig           the location of JASS configuration for
   **                                  the Identity Manager Managed Server where
   **                                  this IT Resource will be working on.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  rootContext              the name of application context.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverPrincipalName      the name of the administrator with
   **                                  administrator permissions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverPrincipalPassword  the password of the administrator account
   **                                  that is used
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  contextPrincipalName     the name corresponding to the administrator
   **                                  with System Adminsitrator permissions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  contextPrincipalPassword the password of the administrator
   **                                  account to is used
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  secureSocket             specifies whether or not to use SSL to
   **                                  secure communication between Oracle
   **                                  Identity Manager and the Managed Server.
   **                                  <br>
   **                                  Allowed object <code>boolean</code>.
   ** @param  localeLanguage           Language code of the target system
   **                                  <br>
   **                                  Default value: en
   **                                  <br>
   **                                  Note: You must specify the value in
   **                                  lowercase.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  localeCountry            Country code of the target system
   **                                  <br>
   **                                  Default value: US
   **                                  <br>
   **                                  Note: You must specify the value in
   **                                  uppercase.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  localeTimeZone           use this parameter to specify the time
   **                                  zone of the target system. For example:
   **                                  GMT-08:00 and GMT+05:30.
   **                                  <br>
   **                                  During a provisioning operation, the
   **                                  connector uses this time zone information
   **                                  to convert date-time values entered on
   **                                  the process form to date-time values
   **                                  relative to the time zone of the target
   **                                  system.
   **                                  <br>
   **                                  Default value: GMT
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  feature                  the name of the Metadata Descriptor
   **                                  providing the target system specific
   **                                  features like objectClasses, attribute
   **                                  id's etc.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @throws TaskException            if one or more attributes are missing on
   **                                  the <code>IT Resource</code> instance.
   */
  private IdentityServerResource(final Loggable loggable, final String serverName, final String serverPort, final String serverType, final String securityConfig, final String rootContext, final String serverPrincipalName, final String serverPrincipalPassword, final String contextPrincipalName, final String contextPrincipalPassword, final boolean secureSocket, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(SERVER_NAME,                serverName);
    attribute(SERVER_PORT,                serverPort);
    attribute(SERVER_TYPE,                serverType);
    attribute(SECURITY_CONFIG,            securityConfig);
    attribute(ROOT_CONTEXT,               rootContext);
    attribute(SERVER_FEATURE,             feature);
    attribute(SERVER_PRINCIPAL_NAME,      serverPrincipalName);
    attribute(SERVER_PRINCIPAL_PASSWORD,  serverPrincipalPassword);
    attribute(CONTEXT_PRINCIPAL_NAME,     contextPrincipalName);
    attribute(CONTEXT_PRINCIPAL_PASSWORD, contextPrincipalPassword);
    attribute(SECURE_SOCKET,              secureSocket ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(LOCALE_LANGUAGE,            localeLanguage);
    attribute(LOCALE_COUNTRY,             localeCountry);
    attribute(LOCALE_TIMEZONE,            localeTimeZone);

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverType
  /**
   ** Returns the type of the server where the Identity Manager is running and
   ** this IT Resource is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_TYPE}.
   ** <p>
   ** If {@link #SERVER_TYPE} is not mapped this method returns
   ** {@link IdentityServerConstant#SERVER_TYPE_WEBLOGIC}.
   **
   ** @return                    the type of the server where Identity Manager
   **                            is deployed on.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverType() {
    return stringValue(SERVER_TYPE, IdentityServerConstant.SERVER_TYPE_WEBLOGIC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverTypeProperty
  /**
   ** Returns the type of the server where the Identity Manager is running and
   ** this IT Resource is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_TYPE_PROPERTY}.
   **
   ** @return                    the property value to set for the server type
   **                            Identity Manager is deployed on
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverTypeProperty() {
    return stringValue(SERVER_TYPE_PROPERTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityConfig
  /**
   ** Returns the security configuration of the Managed Server used to connect
   ** to.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURITY_CONFIG}.
   **
   ** @return                    the security configuration of the Managed
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String securityConfig() {
    return stringValue(SECURITY_CONFIG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPrincipalName
  /**
   ** Returns the username of the principal of a Managed Server to establish a
   ** connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PRINCIPAL_NAME}.
   **
   ** @return                    the distinguished name of the principal to
   **                            establish a connection.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverPrincipalName() {
    return stringValue(SERVER_PRINCIPAL_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPrincipalPassword
  /**
   ** Returns the password of the principal of a Managed Server to establish
   ** a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the principal to establish a
   **                            connection.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverPrincipalPassword() {
    return stringValue(SERVER_PRINCIPAL_PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextPrincipalName
  /**
   ** Returns the username of the principal of a Application Context to
   ** establish a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CONTEXT_PRINCIPAL_NAME}.
   **
   ** @return                    the distinguished name of the principal to
   **                            establish a connection.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextPrincipalName() {
    return stringValue(CONTEXT_PRINCIPAL_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextPrincipalPassword
  /**
   ** Returns the password of the principal of a Application Context to
   ** establish a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #CONTEXT_PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the principal to establish a
   **                            connection.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextPrincipalPassword() {
    return stringValue(CONTEXT_PRINCIPAL_PASSWORD);
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
   **                            <br>
   **                            Possible object is array of
   **                            {@link AbstractAttribute}.
   */
  @Override
  public AbstractAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>IdentityServerResource</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            IT Resource instance where this wrapper belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element if of type {@link String} for the key
   **                            and {@link String} for the value.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as
   **                            <code>IdentityServerResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityServerResource</code>.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static IdentityServerResource build(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    return new IdentityServerResource(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>IdentityServerResource</code> which is
   ** associated with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given instance key.
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
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as
   **                            <code>IdentityServerResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityServerResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries.
   */
  public static IdentityServerResource build(final Loggable loggable, final Long instance)
    throws TaskException {

    return new IdentityServerResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>IdentityServerResource</code> which is
   ** associated with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the public identifier of an
   **                            <code>IT Resource</code> in Identity Manager
   **                            where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as
   **                            <code>IdentityServerResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityServerResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Oracle Identity manager meta entries or
   **                            one or more attributes are missing on the
   **                            <code>IT Resource</code> Definition.
   */
  public static IdentityServerResource build(final Loggable loggable, final String instance)
    throws TaskException {

    return new IdentityServerResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>IdentityServerResource</code> which is
   ** associated with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given parameter.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable                 the {@link Loggable} which has
   **                                  instantiated this
   **                                  <code>IT Resource</code> configuration
   **                                  wrapper.
   **                                  <br>
   **                                  Allowed object is {@link Loggable}.
   ** @param  serverName               the host name or IP address of the
   **                                  Managed Server Identity Manager is
   **                                  deployed on.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverPort               the port the Managed Server is listening
   **                                  on.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverType               the type the Managed Server.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  securityConfig           the location of JASS configuration for
   **                                  the Identity Manager Managed Server where
   **                                  this IT Resource will be working on.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  rootContext              the name of application context.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverPrincipalName      the name of the administrator with
   **                                  administrator permissions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  serverPrincipalPassword  the password of the administrator account
   **                                  that is used
   ** @param  contextPrincipalName     the name corresponding to the administrator
   **                                  with System Adminsitrator permissions.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  contextPrincipalPassword the password of the administrator
   **                                  account to is used
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  secureSocket             specifies whether or not to use SSL to
   **                                  secure communication between Oracle
   **                                  Identity Manager and the Managed Server.
   **                                  <br>
   **                                  Allowed object <code>boolean</code>.
   ** @param  localeLanguage           Language code of the target system
   **                                  <br>
   **                                  Default value: en
   **                                  <br>
   **                                  Note: You must specify the value in
   **                                  lowercase.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  localeCountry            Country code of the target system
   **                                  <br>
   **                                  Default value: US
   **                                  <br>
   **                                  Note: You must specify the value in
   **                                  uppercase.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  localeTimeZone           use this parameter to specify the time
   **                                  zone of the target system. For example:
   **                                  GMT-08:00 and GMT+05:30.
   **                                  <br>
   **                                  During a provisioning operation, the
   **                                  connector uses this time zone information
   **                                  to convert date-time values entered on
   **                                  the process form to date-time values
   **                                  relative to the time zone of the target
   **                                  system.
   **                                  <br>
   **                                  Default value: GMT
   **                                  <br>
   **                                  Allowed object is {@link String}.
   ** @param  feature                  the name of the Metadata Descriptor
   **                                  providing the target system specific
   **                                  features like objectClasses, attribute
   **                                  id's etc.
   **                                  <br>
   **                                  Allowed object is {@link String}.
   **
   ** @return                          a populated <code>IT Resource</code>
   **                                  instance wrapped as
   **                                  <code>IdentityServerResource</code>.
   **                                  <br>
   **                                  Possible object is
   **                                  <code>IdentityServerResource</code>.
   **
   ** @throws TaskException            if one or more attributes are missing on
   **                                  the <code>IT Resource</code> instance.
   */
  public static IdentityServerResource build(final Loggable loggable, final String serverName, final String serverPort, final String serverType, final String securityConfig, final String rootContext, final String serverPrincipalName, final String serverPrincipalPassword, final String contextPrincipalName, final String contextPrincipalPassword, final boolean secureSocket, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    return new IdentityServerResource(loggable, serverName, serverPort, serverType, securityConfig, rootContext, serverPrincipalName, serverPrincipalPassword, contextPrincipalName, contextPrincipalPassword, secureSocket, localeLanguage, localeCountry, localeTimeZone, feature);
  }
}