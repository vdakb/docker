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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Identity Governance Connector

    File        :   ServiceResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.jes.integration.oig;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.ITResourceAttribute;

import oracle.iam.identity.connector.integration.TargetResource;

import oracle.iam.identity.icf.jes.ServerEndpoint;

////////////////////////////////////////////////////////////////////////////////
// class ServiceResource
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>ServiceResource</code> implements the base functionality of an
 ** Identity Governance Managed Service IT Resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceResource extends TargetResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the target system where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String SERVER_HOST                = "Server Host";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the type of
   ** the Identity Governance Managed Server where this IT Resource will be
   ** working on.
   */
  public static final String SERVER_TYPE                = "Server Type";

  /**
   ** Attribute tag which might be defined on an IT Resource to hold the
   ** location of JASS configuration for the Identity Manager Managed Server
   ** where this IT Resource will be working on.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This attribute becomes required if the bundle is deployed on a connector
   ** server.
   */
  public static final String SECURITY_CONFIG            = "Security Config";

  /**
   ** Attribute tag which might be defined on an IT Resource to specify the user
   ** name of the target domain account to be used to establish a connection to
   ** the Managed Server.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This attribute becomes required if the bundle is deployed on a connector
   ** server.
   */
  public static final String DOMAIN_PRINCIPAL           = "Domain Principal";

  /**
   ** Attribute tag which might be defined on an IT Resource to specify the user
   ** name of the target domain account to be used to establish a connection to
   ** the Managed Server.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This attribute becomes required if the bundle is deployed on a connector
   ** server.
   */
  public static final String DOMAIN_PASSWORD            = "Domain Password";

  /** The array with the attributes for the <code>IT Resource</code>. */
  private static final ITResourceAttribute[] attribute  = {
    ITResourceAttribute.build(SERVER_HOST,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_NAME,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_TYPE,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_FEATURE,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,      ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_NAME,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD, ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(DOMAIN_PRINCIPAL,   ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(DOMAIN_PASSWORD,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(SECURITY_CONFIG,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_COUNTRY,     ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(CONNECTION_TIMEOUT, CONNECTION_TIMEOUT_DEFAULT)
  , ITResourceAttribute.build(RESPONSE_TIMEOUT,   RESPONSE_TIMEOUT_DEFAULT)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} for logging purpose.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public ServiceResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** Identity Manager. Only instance key and instance name are obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
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
   **                            in the Identity manager meta entries.
   */
  private ServiceResource(final Loggable loggable, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);

    populateAttributes(this.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of
   ** Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
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
   **                            in the Identity Manager meta entries or one or
   **                            more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private ServiceResource(final Loggable loggable, final String instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the database is deployed.
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the database is listening on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverName         the name of the Managed Server the connection
   **                            will established to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverType         the type of the Identity Governance Managed
   **                            Server where this IT Resource will be working
   **                            on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rootContext        the name of application context.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  securityConfig     the location of JAAS configuration for the
   **                            Identity Governance Managed Server where this
   **                            IT Resource will be working on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPrincipal    the name of the domain principal this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPassword     the password of the domain system account
   **                            specified by the <code>domainPrincipal</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the name corresponding to the administrator
   **                            with System Adminsitrator permissions this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private ServiceResource(final Loggable loggable, final String serverHost, final String serverPort, final String serverName, final String serverType, final String rootContext, final boolean secureSocket, final String securityConfig, final String domainPrincipal, final String domainPassword, final String principalName, final String principalPassword, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(SERVER_HOST,                serverHost);
    attribute(SERVER_PORT,                serverPort);
    attribute(SERVER_NAME,                serverName);
    attribute(SERVER_TYPE,                serverType);
    attribute(SERVER_FEATURE,             feature);
    attribute(ROOT_CONTEXT,               rootContext);
    attribute(SECURITY_CONFIG,            securityConfig);
    attribute(DOMAIN_PRINCIPAL,           domainPrincipal);
    attribute(DOMAIN_PASSWORD,            domainPassword);
    attribute(PRINCIPAL_NAME,             principalName);
    attribute(PRINCIPAL_PASSWORD,         principalPassword);
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
  // Method:   serverHost
  /**
   ** Returns the name of the server where the target system is deployed on and
   ** this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_HOST}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the name of the server where the target system
   **                            is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String serverHost() {
    return stringValue(SERVER_HOST);
  }

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
   ** {@link ServerEndpoint#SERVER_TYPE_WEBLOGIC}.
   **
   ** @return                    the type of the server where Identity
   **                            Governance is deployed on.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String serverType() {
    return stringValue(SERVER_TYPE, ServerEndpoint.SERVER_TYPE_WEBLOGIC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPrincipal
  /**
   ** Returns the name of the principal of a target domain account to establish
   ** a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #DOMAIN_PRINCIPAL}.
   **
   ** @return                    the name of the principal of a target domain
   **                            account to establish a connection.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String domainPrincipal() {
    return stringValue(DOMAIN_PRINCIPAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPassword
  /**
   ** Returns the password for the principal of a target domain account to
   ** establish a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #DOMAIN_PASSWORD}.
   **
   ** @return                    the password for the principal of a target
   **                            domain account to establish a connection.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String domainPassword() {
    return stringValue(DOMAIN_PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractResource)
  /**
   ** Returns the array with names which should be populated from the
   ** IT Resource definition of Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  public AbstractAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registry (TargetResource)
  /**
   ** Returns the mapping of attribute names defined on this
   ** <code>IT Resource</code> and the parameter names expected by connector
   ** bundle configuration.
   **
   ** @return                    the mapping of attribute names defined
   **                            on this <code>IT Resource</code> and the
   **                            parameter names expected by connector bundle
   **                            configuration.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   */
  @Override
  public final Map<String, String> registry() {
    return Service.RESOURCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given identifier.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given key or one or more attributes are missing
   **                            on the <code>IT Resource</code> instance.
   */
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public static ServiceResource build(final Loggable loggable, final Long instance)
    throws TaskException {

    return new ServiceResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given name.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> name.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public static ServiceResource build(final Loggable loggable, final String instance)
    throws TaskException {

    return new ServiceResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given parameter.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceResource</code>.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServiceResource build(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    return new ServiceResource(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServiceResource</code> which is
   ** associated with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given parameter.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the database is deployed.
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the database is listening on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverName         the name of the Managed Server the connection
   **                            will established to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverType         the type of the Identity Governance Managed
   **                            Server where this IT Resource will be working
   **                            on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rootContext        the name of application context.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  securityConfig     the location of JAAS configuration for the
   **                            Identity Governance Managed Server where this
   **                            IT Resource will be working on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPrincipal    the name of the domain principal this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domainPassword     the password of the domain system account
   **                            specified by the <code>domainPrincipal</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the name corresponding to the administrator
   **                            with System Adminsitrator permissions this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeLanguage     Language code of the target system
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localeCountry      Country code of the target system
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  feature            the name of the Metadata Descriptor providing
   **                            the target system specific features like
   **                            objectClasses, attribute id's etc.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceResource</code>.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServiceResource build(final Loggable loggable, final String serverHost, final String serverPort, final String serverName, final String serverType, final String rootContext, final boolean secureSocket, final String securityConfig, final String domainPrincipal, final String domainPassword, final String principalName, final String principalPassword, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    return new ServiceResource(loggable, serverHost, serverPort, serverName, serverType, rootContext, secureSocket, securityConfig, domainPrincipal, domainPassword, principalName, principalPassword, localeCountry, localeLanguage, localeTimeZone, feature);
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
    return StringUtility.formatCollection(this);
  }
}