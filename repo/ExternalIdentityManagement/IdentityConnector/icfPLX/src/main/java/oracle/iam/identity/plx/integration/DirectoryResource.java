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
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DirectoryResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.plx.integration;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.ITResourceAttribute;

import oracle.iam.identity.connector.integration.TargetResource;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryResource</code> implements the base functionality of a
 ** LDAP Directory <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryResource extends TargetResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specifiy that all path entries are treated as relative to the naming
   ** context.
   */
  public static final String DISTINGUISHED_NAME_RELATIVE = "Distinguished Name Relative";

  /** the array with the attributes for the <code>IT Resource</code> */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(SERVER_NAME,                 ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,                 ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,               ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,                ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_NAME,              ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD,          ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_FEATURE,              ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(DISTINGUISHED_NAME_RELATIVE, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_COUNTRY,              ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,             ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,             ITResourceAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryResource</code> which is associated
   ** with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** specified {@link Map} and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DirectoryResource</code> configuration
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
  public DirectoryResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryResource</code> which is associated
   ** with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** the Identity Manager. Only instance key and instance name are obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DirectoryResource</code> configuration
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
   **                            in the Identity Manager meta entries.
   */
  public DirectoryResource(final Loggable loggable, final Long instance)
    throws TaskException {

    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryResource</code> which is associated
   ** with the specified {@link Loggable} and belongs to the
   ** <code>IT Resource</code> specified by the given instance name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of the
   ** Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DirectoryResource</code> configuration
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
  public DirectoryResource(final Loggable loggable, final String instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given parameter.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DirectoryResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverName         the host name or IP address of the target
   **                            system on which the LDAP Server is deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
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
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
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
   **                            <br>
   **                            Allowed object is {@link String}.
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
  public DirectoryResource(final Loggable loggable, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(SERVER_NAME,                 serverName);
    attribute(SERVER_PORT,                 serverPort);
    attribute(ROOT_CONTEXT,                rootContext);
    attribute(SERVER_FEATURE,              feature);
    attribute(PRINCIPAL_NAME,              principalName);
    attribute(PRINCIPAL_PASSWORD,          principalPassword);
    attribute(SECURE_SOCKET,               secureSocket ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(DISTINGUISHED_NAME_RELATIVE, relativeDN   ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(LOCALE_LANGUAGE,             localeLanguage);
    attribute(LOCALE_COUNTRY,              localeCountry);
    attribute(LOCALE_TIMEZONE,             localeTimeZone);

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNameRelative
  /**
   ** Returns whether all pathes are treated as relative to the naming context
   ** of the connected LDAP Server.
   ** Context.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #DISTINGUISHED_NAME_RELATIVE}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** If <code>null</code> or an empty string is passed for
   ** <code>rootContext</code> this parameter will be ignored and all pathes
   ** will be treated as absolute.
   **
   ** @return                    <code>true</code> if all pathes are treated as
   **                            relative in the connectec LDAP Server;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean distinguishedNameRelative() {
    return StringUtility.isEmpty(rootContext()) ? false : booleanValue(DISTINGUISHED_NAME_RELATIVE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstrcatConnector)
  /**
   ** Returns the array with names which should be populated from the
   ** <code>IT Resource</code> definition of Identity Manager.
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
    return Directory.RESOURCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given identifier.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>DirectoryResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>DirectoryResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given key or one or more attributes are missing
   **                            on the <code>IT Resource</code> instance.
   */
  public static DirectoryResource build(final Loggable loggable, final Long instance)
    throws TaskException {

    return new DirectoryResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryResource</code> which is
   ** associated with the specified task and belongs to the IT Resource
   ** specified by the given name.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> name.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>DirectoryResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the name of the <code>IT Resource</code>
   **                            instance where this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>DirectoryResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  public static DirectoryResource build(final Loggable loggable, final String instance)
    throws TaskException {

    return new DirectoryResource(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryResource</code> which is
   ** associated with the specified task and belongs to the
   ** <code>IT Resource</code> specified by the given parameter.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>DirectoryResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverName         the host name or IP address of the target
   **                            system on which the LDAP Server is deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
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
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
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
   **                            wrapped as <code>DirectoryResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryResource</code>.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static DirectoryResource build(final Loggable loggable, final String serverName, final String serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    return new DirectoryResource(loggable, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, localeLanguage, localeCountry, localeTimeZone, feature);
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