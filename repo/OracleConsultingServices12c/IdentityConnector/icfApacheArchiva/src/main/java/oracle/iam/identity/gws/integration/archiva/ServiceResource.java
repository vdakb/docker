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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   ServiceResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.apigee;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.ITResourceAttribute;

import oracle.iam.identity.connector.integration.TargetResource;

import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.TYPE_ACCEPT;
import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.TYPE_CONTENT;
import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.RESOURCE_OWNER;
import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.CLIENT_SECRET;
import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.CLIENT_IDENTIFIER;
import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.RESOURCE_CREDENTIAL;
import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.AUTHENTICATION_SCHEME;
import static oracle.iam.identity.gws.integration.apigee.Service.OIM.Resource.AUTHORIZATION_SERVER;

////////////////////////////////////////////////////////////////////////////////
// class ServiceResource
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>ServiceResource</code> implements the base functionality of a
 ** Service <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceResource extends TargetResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the array with the attributes for the <code>IT Resource</code> */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(SERVER_NAME,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,          ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(TYPE_CONTENT,          ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(TYPE_ACCEPT,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,         ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(AUTHENTICATION_SCHEME, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CLIENT_IDENTIFIER,     ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(CLIENT_SECRET,         ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_NAME,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_OWNER,        ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(RESOURCE_CREDENTIAL,   ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(AUTHORIZATION_SERVER,  ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_COUNTRY,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_FEATURE,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(CONNECTION_TIMEOUT,    CONNECTION_TIMEOUT_DEFAULT)
  , ITResourceAttribute.build(RESPONSE_TIMEOUT,      RESPONSE_TIMEOUT_DEFAULT)
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
   ** @param  serverName         the host name or IP address of the target
   **                            system on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Service Provider is listening on.
   **                            Allowed object is {@link String}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  typeContent        the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  typeAccept         the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authentication     the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authorization      the <code>Authorization Server</code> attribute
   **                            exposed by the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientIdentifier   the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the credential of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the name of the Service Provider this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourceOwner      the resource owner account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourceCredential the password of the resource owner account
   **                            specified by the <code>resourceUsername</code>
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
  private ServiceResource(final Loggable loggable, final String serverName, final String serverPort, final String rootContext, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final String authorization, final String clientIdentifier, final String clientSecret, final String principalName, final String principalPassword, final String resourceOwner, final String resourceCredential, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(SERVER_NAME,           serverName);
    attribute(SERVER_PORT,           serverPort);
    attribute(ROOT_CONTEXT,          rootContext);
    attribute(SECURE_SOCKET,         secureSocket  ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(TYPE_CONTENT,          typeContent);
    attribute(TYPE_ACCEPT,           typeAccept);
    attribute(AUTHENTICATION_SCHEME, authentication);
    attribute(AUTHORIZATION_SERVER,  authorization);
    attribute(CLIENT_IDENTIFIER,     clientIdentifier);
    attribute(CLIENT_SECRET,         clientSecret);
    attribute(PRINCIPAL_NAME,        principalName);
    attribute(PRINCIPAL_PASSWORD,    principalPassword);
    attribute(RESOURCE_OWNER,        resourceOwner);
    attribute(RESOURCE_CREDENTIAL,   resourceCredential);
    attribute(LOCALE_LANGUAGE,       localeLanguage);
    attribute(LOCALE_COUNTRY,        localeCountry);
    attribute(LOCALE_TIMEZONE,       localeTimeZone);
    attribute(SERVER_FEATURE,        feature);

    validateAttributes(attribute);
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
   **                            <br>
   **                            Possible object is array of
   **                            {@link AbstractAttribute} where each.
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
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given key or one or more attributes are missing
   **                            on the <code>IT Resource</code> instance.
   */
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
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServiceResource</code>.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
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
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverName         the host name or IP address of the target
   **                            system on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Service Provider is listening on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  typeContent        the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  typeAccept         the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authentication     the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientIdentifier   the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the credential of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the target system account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourceUsername   the resource owner account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourcePassword   the password of the resource owner account
   **                            specified by the <code>resourceUsername</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authorization      the <code>Authorization Server</code> attribute
   **                            exposed by the Service Provider endpoint for
   **                            authentication purpose.
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
   **                            Possible object is {@link ServiceResource}.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServiceResource build(final Loggable loggable, final String serverName, final String serverPort, final String rootContext, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final String clientIdentifier, final String clientSecret, final String principalName, final String principalPassword, final String resourceUsername, final String resourcePassword, final String authorization, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    return new ServiceResource(loggable, serverName, serverPort, rootContext, secureSocket, typeContent, typeAccept, authentication, clientIdentifier, clientSecret, principalName, principalPassword, resourceUsername, resourcePassword, authorization, localeCountry, localeLanguage, localeTimeZone, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return StringUtility.formatCollection(this);
  }
}