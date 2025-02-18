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

    File        :   DatabaseResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.Map;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseResource</code> implements the base functionality of a
 ** Database IT Resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class DatabaseResource extends AbstractResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an IT Resource to specify the JDBC
   ** URL for the target system database.
   */
  public static final String DATABASE_URL       = "Database URL";

  /**
   ** Attribute tag which may be defined on an IT Resource to specify the name
   ** of the database as specified in the {@link #DATABASE_URL} parameter.
   */
  public static final String DATABASE_NAME      = "Database Name";

  /**
   ** Attribute tag which may be defined on an IT Resource to specify the
   ** cataloge schema name to be used.
   */
  public static final String DATABASE_SCHEMA    = "Database Schema";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the path to the Metadata Descriptor which is specifying the feature
   ** descriptor of the <code>IT Resource</code>.
   */
  public static final String DATABASE_FEATURE   = "Database Feature";

  /** the array with the attributes for the IT Resource */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(DATABASE_URL,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(DATABASE_NAME,      ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(DATABASE_SCHEMA,    ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(DATABASE_FEATURE,   ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(PRINCIPAL_NAME,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(PRINCIPAL_PASSWORD, ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURE_SOCKET,      ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(POOL_SUPPORTED,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_LANGUAGE,    ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_COUNTRY,     ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(LOCALE_TIMEZONE,    ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(DATABASE_FEATURE,   ITResourceAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseResource</code> which is associated with the
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
  public DatabaseResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseResource</code> which is associated with the
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
  public DatabaseResource(final Loggable loggable, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);

    populateAttributes(this.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseResource</code> which is associated with the
   ** specified task and belongs to the <code>IT Resource</code> specified by
   ** the given name.
   ** <br>
   ** The IT Resource will be populated from the repository of the Oracle
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
   **                            in the Oracle Identity manager meta entries or
   **                            one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public DatabaseResource(final Loggable loggable, final String instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The IT Resource will be populated from the specified parameters.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>IT Resource</code> configuration
   **                            wrapper.
   ** @param  databaseUrl        the JDBC URL for the target system database.
   ** @param  databaseName       the name of the database as specified in the
   **                            <code>databaseUrl</code> parameter.
   ** @param  principalName      the user name of the target system account to
   **                            be used to establish a connection.
   ** @param  principalPassword  the password of the target system account
   **                            specified by the <code>principalName</code>
   **                            parameter.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the target system.
   ** @param  poolSupported      <code>true</code> if you want to enable
   **                            connection pooling for this target system
   **                            installation.
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
  public DatabaseResource(final Loggable loggable, final String databaseUrl, final String databaseName, final String principalName, final String principalPassword, final boolean secureSocket, final boolean poolSupported, final String localeLanguage, final String localeCountry, final String localeTimeZone, final String feature)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(DATABASE_URL,       databaseUrl);
    attribute(DATABASE_NAME,      databaseName);
    attribute(DATABASE_FEATURE,   feature);
    attribute(PRINCIPAL_NAME,     principalName);
    attribute(PRINCIPAL_PASSWORD, principalPassword);
    attribute(SECURE_SOCKET,      secureSocket  ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(POOL_SUPPORTED,     poolSupported ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(LOCALE_LANGUAGE,    localeLanguage);
    attribute(LOCALE_COUNTRY,     localeCountry);
    attribute(LOCALE_TIMEZONE,    localeTimeZone);

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseURL
  /**
   ** Returns the name of the JDBC database URL this <code>IT Resource</code> is
   ** configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #DATABASE_URL}.
   **
   ** @return                    the name of the JDBC database URL this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String databaseURL() {
    return stringValue(DATABASE_URL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseName
  /**
   ** Returns the name of the database this <code>IT Resource</code> is
   ** configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #DATABASE_NAME}.
   **
   ** @return                    the name of the database this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String databaseName() {
    return stringValue(DATABASE_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseSchema
  /**
   ** Returns the name of the cataloge schema of the database database.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #DATABASE_SCHEMA}.
   **
   ** @return                    the name of the cataloge schema of the
   **                            database.
   **                            <br>
   **                            Possible object {@link String}.
   */
  public final String databaseSchema() {
    return stringValue(DATABASE_SCHEMA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   features (overridden)
  /**
   ** Returns the name of the feature mapping of the target system where
   ** this <code>IT Resource</code> will be working on.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #DATABASE_FEATURE}.
   **
   ** @return                    the name of the feature mapping of the
   **                            target system.
   **                            <br>
   **                            Possible object {@link String}.
   */
  @Override
  public final String feature() {
    return stringValue(DATABASE_FEATURE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractResource)
  /**
   ** Returns the array with names which should be populated from the
   ** IT Resource definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  public AbstractAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    StringUtility.formatValuePair(table, DATABASE_NAME,      stringValue(DATABASE_NAME));
    StringUtility.formatValuePair(table, DATABASE_URL,       stringValue(DATABASE_URL));
    StringUtility.formatValuePair(table, DATABASE_SCHEMA,    stringValue(DATABASE_NAME));
    StringUtility.formatValuePair(table, PRINCIPAL_NAME,     stringValue(PRINCIPAL_NAME));
    StringUtility.formatValuePair(table, PRINCIPAL_PASSWORD, stringValue(PRINCIPAL_PASSWORD));
    StringUtility.formatValuePair(table, SECURE_SOCKET,      booleanValue(SECURE_SOCKET));
    StringUtility.formatValuePair(table, POOL_SUPPORTED,     booleanValue(POOL_SUPPORTED));
    StringUtility.formatValuePair(table, LOCALE_COUNTRY,     stringValue(LOCALE_COUNTRY));
    StringUtility.formatValuePair(table, LOCALE_LANGUAGE,    stringValue(LOCALE_LANGUAGE));
    StringUtility.formatValuePair(table, LOCALE_TIMEZONE,    stringValue(LOCALE_TIMEZONE));

    StringBuilder builder = new StringBuilder();
    table.print(builder);
    return builder.toString();
  }
}