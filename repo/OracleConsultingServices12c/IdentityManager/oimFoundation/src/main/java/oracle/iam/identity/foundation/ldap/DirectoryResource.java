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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryResource</code> implements the base functionality of a
 ** LDAP Directory <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class DirectoryResource extends AbstractResource {

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
    ITResourceAttribute.build(SERVER_NAME,                  ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_PORT,                  ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,                ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(ROOT_CONTEXT,                 ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_NAME,               ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD,           ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVER_FEATURE,               ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(DISTINGUISHED_NAME_RELATIVE, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_COUNTRY,               ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,              ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,              ITResourceAttribute.MANDATORY)
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
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  parameter          the {@link Map} providing the parameter of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
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
   **                            in the Identity manager meta entries.
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
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   **                            Allowed object {@link Loggable}.
   ** @param  serverName         the host name or IP address of the target
   **                            system on which the LDAP Server is deployed.
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
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
   */
  @Override
  public AbstractAttribute[] attributes() {
    return attribute;
  }
}