/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   TokenAsserterConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TokenAsserterConfiguration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.jmx;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;

import java.util.logging.Logger;

import java.util.stream.Collectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.lang.management.ManagementFactory;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonException;
import javax.json.JsonObjectBuilder;

import javax.annotation.PreDestroy;

import javax.management.ObjectName;
import javax.management.MBeanServer;

import javax.enterprise.event.Observes;

import javax.enterprise.context.Initialized;
import javax.enterprise.context.ApplicationScoped;

////////////////////////////////////////////////////////////////////////////////
// abstract class TokenAsserterConfiguration
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The base calass of configuration provider as a JMX management
 ** implementation.
 ** <p>
 ** Subclasses needs to be annotated by <code>@Singleton</code> to get observed
 ** by a CDI implementation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TokenAsserterConfiguration implements TokenAsserterConfigurationMBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String THIS             = TokenAsserterConfiguration.class.getName();
  private static final Logger LOGGER           = Logger.getLogger(THIS);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3557241773073819452")
  private static final long   serialVersionUID = 1334801674968491664L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String      realm;

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private Map<String, Object> property    = null;
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private MBeanServer         mbeanServer = null;
  private ObjectName          objectName  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenAsserterConfiguration</code> that use the
   ** specified <code>realm</code> parameter later for realm that will be
   ** sent via the <code>WWW-Authenticate</code> header.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** This realm name <b>does not</b> couple a named identity store
   ** configuration to the authentication mechanism.
   ** <p>
   ** The parameter <code>realm</code> its also used to maintain the persisted
   ** configuration.
   **
   ** @param  realm              the realm that will be sent via the
   **                            <code>WWW-Authenticate</code> header and used
   **                            as the name of the file placed in the domain
   **                            instance configuration directory to persist
   **                            the configuration.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public TokenAsserterConfiguration(final String realm) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.realm = realm;

    // initialize instance
    refresh();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extend
  /**
   ** Extends the property {@link Map} with the named-value pair specified.
   **
   ** @param  name               the name of the named-value pair to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the named-value pair to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public void extend(final String name, final List<String> value) {
    this.property.put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extend
  /**
   ** Extends the property {@link Map} with the named-value pair specified.
   **
   ** @param  name               the name of the named-value pair to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the named-value pair to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void extend(final String name, final String value) {
    this.property.put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extend
  /**
   ** Extends the property {@link Map} with the named-value pairs specified.
   **
   ** @param  value              the named-value pairs to add.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  public void extend(final Map<String, Object> value) {
    this.property.putAll(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyString (TokenAsserterConfigurationMBean)
  /**
   ** Returns the string value of the associated property mapping for the
   ** specified <code>name</code>.
   ** <br>
   ** If the value is not found <code>null</code> is returned.
   **
   ** @param  name               the mapping name whose associated value is to
   **                            be returned as {@link String}
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string value of the associated property
   **                            mapping for <code>name</code>, or
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String propertyString(final String name) {
    return propertyString(name, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyString (TokenAsserterConfigurationMBean)
  /**
   ** Returns the string value of the associated property mapping for the
   ** specified <code>name</code>.
   ** <br>
   ** If the value is not found the specified default value is returned.
   **
   ** @param  name               the mapping name whose associated value is to
   **                            be returned as {@link String}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       a default value to be returned if the
   **                            associated property mapping is not found.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string value of the associated property
   **                            mapping for <code>name</code>, or the default
   **                            value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String propertyString(final String name, final String defaultValue) {
    final Object value = this.property.get(name);
    return value == null ? defaultValue : (String)value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyList (TokenAsserterConfigurationMBean)
  /**
   ** Returns the string list of the associated property mapping for the
   ** specified <code>name</code>.
   ** <br>
   ** If the value is not found an empty list is returned.
   **
   ** @param  name               the mapping name whose associated value is to
   **                            be returned as {@link String}
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string value of the associated property
   **                            mapping for <code>name</code>, or
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  @Override
  public List<String> propertyList(final String name) {
    return propertyList(name, Collections.<String>emptyList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyList (TokenAsserterConfigurationMBean)
  /**
   ** Returns the string list of the associated property mapping for the
   ** specified <code>name</code>.
   ** <br>
   ** If the value is not found the specified default list is returned.
   **
   ** @param  name               the mapping name whose associated value is to
   **                            be returned as {@link String}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       a default list to be returned if the
   **                            associated property mapping is not found.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the string value of the associated property
   **                            mapping for <code>name</code>, or the default
   **                            value.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<String> propertyList(final String name, final List<String> defaultValue) {
    final Object value = this.property.get(name);
    return value == null ? defaultValue : (List<String>)value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRealm (TokenAsserterConfigurationMBean)
  /**
   ** Returns the realm that will be sent via the <code>WWW-Authenticate</code>
   ** header.
   **
   ** @return                    the realm that will be sent via the
   **                            <code>WWW-Authenticate</code> header.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getRealm() {
    return this.realm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains (TokenAsserterConfigurationMBean)
  /**
   ** Tests if the specified object is a key in the associated storage.
   **
   ** @param  key                the possible key value to test.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if and only if the specified
   **                            object is a key in the associated storage, as
   **                            determined by the <code>equals</code> method;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws NullPointerException if the key is <code>null</code>.
   */
  @Override
  public boolean contains(final Object key) {
    return this.property.containsKey(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAssertionType (TokenAsserterConfigurationMBean)
  /**
   ** Sets the type of the assertion send by the authenticating authority.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #TYPE assertionType}.
   **
   ** @param  value              the type of the assertion send by the
   **                            authenticating authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setAssertionType(final String value) {
    extend(TYPE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAssertionType (TokenAsserterConfigurationMBean)
  /**
   ** Returns the type of the assertion send by the authenticating authority.
   **
   ** @return                    the type of the assertion send by the
   **                            authenticating authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getAssertionType() {
    return propertyString(TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAssertionHeader (TokenAsserterConfigurationMBean)
  /**
   ** Sets the request header providing the identity of an authenticated user.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #HEADER assertionHeader}.
   **
   ** @param  value              the request header providing the identity of an
   **                            authenticated user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setAssertionHeader(final String value) {
    extend(HEADER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAssertionHeader (TokenAsserterConfigurationMBean)
  /**
   ** Returns the request header providing the identity of an authenticated user.
   **
   ** @return                    the request header providing the identity of an
   **                            authenticated user.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getAssertionHeader() {
    return propertyString(HEADER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaterial (TokenAsserterConfigurationMBean)
  /**
   ** Sets  the PublicKey Material of the signing authority.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #SIGNING_MATERIAL signingMaterial}.
   **
   ** @param  value              the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setMaterial(final String value) {
    this.property.put(SIGNING_MATERIAL, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaterial (TokenAsserterConfigurationMBean)
  /**
   ** Returns the PublicKey Material of the signing authority.
   **
   ** @return                    the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getMaterial() {
    return propertyString(SIGNING_MATERIAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation (TokenAsserterConfigurationMBean)
  /**
   ** Sets the PublicKey local filesystem location of the signing authority.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #SIGNING_LOCATION signingLocation}.
   **
   ** @param  value              the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setLocation(final String value) {
    this.property.put(SIGNING_LOCATION, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocation (TokenAsserterConfigurationMBean)
  /**
   ** Returns the PublicKey local filesystem location of the signing authority.
   **
   ** @return                    the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getLocation() {
    return propertyString(SIGNING_LOCATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDataSource (TokenAsserterConfigurationMBean)
  /**
   ** Sets the JNDI name of the JDBC DataSource for the Authentication
   ** Provider.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #DATASOURCE dataSource}.
   **
   ** @param  value              the JNDI name of the JDBC DataSource for the
   **                            Authentication Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setDataSource(final String value) {
    this.property.put(DATASOURCE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataSource (TokenAsserterConfigurationMBean)
  /**
   ** Returns the JNDI name of the JDBC DataSource for the Authentication
   ** Provider.
   **
   ** @return                    the JNDI name of the JDBC DataSource for the
   **                            Authentication Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getDataSource() {
    return propertyString(DATASOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalQuery (TokenAsserterConfigurationMBean)
  /**
   ** Sets the authentication query used to authenticate users based on
   ** specific key types.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #PRINCIPAL_QUERY principalQuery}.
   **
   ** @param  value              the authentication query used to authenticate
   **                            users based on specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setPrincipalQuery(final String value) {
    this.property.put(PRINCIPAL_QUERY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalQuery (TokenAsserterConfigurationMBean)
  /**
   ** Returns the authentication query used to authenticate users based on
   ** specific key types.
   **
   ** @return                    the authentication query used to authenticate
   **                            users based on specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getPrincipalQuery() {
    return propertyString(PRINCIPAL_QUERY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPermissionQuery (TokenAsserterConfigurationMBean)
  /**
   ** Sets the authorization query used to authorize users based on specific key
   ** types.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #PERMISSION_QUERY permissionQuery}.
   **
   ** @param  value              the authorization query used to authorize users
   **                            based on specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setPermissionQuery(final String value) {
    this.property.put(PERMISSION_QUERY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPermissionQuery (TokenAsserterConfigurationMBean)
  /**
   ** Returns the authorization query used to authorize users based on specific
   ** key types.
   **
   ** @return                    the authorization query used to authorize users
   **                            based on specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getPermissionQuery() {
    return propertyString(PERMISSION_QUERY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (TokenAsserterConfigurationMBean)
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Override
  public final void refresh() {
    final File path = path();
    // create all default properties
    initialize();
    // if a configuration file exists override the defualt properties with
    // the configuration provided by the file
    if (path.exists()) {
      try {
        // override any settings
        Json.createReader(new FileInputStream(path)).readObject().forEach((k, v) -> this.property.put(k, jsonValue(v)));
      }
      catch (FileNotFoundException e) {
        LOGGER.severe(e.getLocalizedMessage());
      }
      catch (JsonException e) {
        LOGGER.severe(e.getLocalizedMessage());
      }
      // validate the consistency of the configuration at all
      validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save (TokenAsserterConfigurationMBean)
  /**
   ** Save the configuration to the file system.
   */
  @Override
  public final void save() {
    // validate the consistency of the configuration before its persisted
    validate();
    
    final JsonObjectBuilder builder = Json.createObjectBuilder();
    this.property.forEach((k, v) -> builder.add(k, v.toString()));
    try {
      Json.createWriter(new FileOutputStream(path())).writeObject(builder.build());
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      LOGGER.severe(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (TokenAsserterConfigurationMBean)
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Override
  public void validate() {
    validateAssertion();
    validateIdentityStore();
    // validate the signing key configuration only if its required
    if (!"plain".equals(getAssertionType()))
      validateKeyMaterial();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startup
  @SuppressWarnings("unused")
  public void startup(final @Observes @Initialized(ApplicationScoped.class) Object unused) {
    final String method = "startup";
    LOGGER.entering(THIS, method);
    try {
      this.objectName  = new ObjectName("bka.iam.identity.service:type=" + this.realm);
      this.mbeanServer = ManagementFactory.getPlatformMBeanServer();
      this.mbeanServer.registerMBean(this, this.objectName);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      LOGGER.severe("Problem during registration of Monitoring into JMX:" + e);
      throw new IllegalStateException("Problem during registration of Monitoring into JMX:" + e);
    }
    finally {
      LOGGER.exiting(THIS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdown
  @PreDestroy
  public void shutdown() {
    final String method = "shutdown";
    LOGGER.entering(THIS, method);
    try {
      this.mbeanServer.unregisterMBean(this.objectName);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      LOGGER.severe("Problem during unregistration of Monitoring into JMX:" + e);
      throw new IllegalStateException("Problem during unregistration of Monitoring into JMX:" + e);
    }
    finally {
      LOGGER.exiting(THIS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for the
   ** <code>TokenAsserterConfiguration</code> in its minimal form.
   **
   ** @return                    a string representation that represents this
   **                            <code>TokenAsserterConfiguration</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return this.property.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateAssertion
  /**
   ** Validates the assertion configuration for consistency to ensure that all
   ** properties are set.
   **
   ** @throws IllegalStateException if any rule is violated.
   */
  protected void validateAssertion() {
    if (getAssertionType() == null || getAssertionType().isEmpty()) {
      LOGGER.severe("Property [assertionType] must be defined");
      throw new IllegalStateException("Property [assertionType] must be defined");
    }
    if (getAssertionHeader() == null || getAssertionHeader().isEmpty()) {
      LOGGER.severe("Property [assertionHeader] must be defined");
      throw new IllegalStateException("Property [assertionHeader] must be defined");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateIdentityStore
  /**
   ** Validates the database configuration for consistency to ensure that a
   ** JDBC DateSource is set and both queries to authenticate/authorize a user
   ** are set.
   **
   ** @throws IllegalStateException if any rule is violated.
   */
  protected void validateIdentityStore() {
    if (getDataSource() == null || getDataSource().isEmpty()) {
      LOGGER.severe("Property [dataSource] must be defined");
      throw new IllegalStateException("Property [dataSource] must be defined");
    }
    if (getPrincipalQuery() == null || getPrincipalQuery().isEmpty()) {
      LOGGER.severe("Property [principalQuery] must be defined");
      throw new IllegalStateException("Property [principalQuery] must be defined");
    }
    if (getPermissionQuery() == null || getPermissionQuery().isEmpty()) {
      LOGGER.severe("Property [permissionQuery] must be defined");
      throw new IllegalStateException("Property [permissionQuery] must be defined");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateKeyMaterial
  /**
   ** Validates the configuration for consistency to ensure that only a
   ** PublicKey location or the PublicKey material (Base64 encoded) is provided
   ** at the same time.
   **
   ** @throws IllegalStateException if the rule is violated.
   */
  protected void validateKeyMaterial() {
    final String location = getLocation();
    final String material = getMaterial();
    if (location != null && !location.isEmpty() && material != null && !material.isEmpty()) {
      LOGGER.severe("Both properties [signingMaterial] and [signingLocation] must not be defined");
      throw new IllegalStateException("Both properties [signingMaterial] and [signingLocation] must not be definec");
    }
    if ((location == null || location.isEmpty()) && (material == null || material.isEmpty())) {
      LOGGER.severe("One of properties [signingMaterial] or [signingLocation] must be defined");
      throw new IllegalStateException("One of properties [signingMaterial] or [signingLocation] must be defined");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Factory method to create the abstract path to the configuration file.
   **
   ** @return                    the abstract path to the configuration file in
   **                            the domain configuration.
   **                            <br>
   **                            Possible object is {@link File}.
   */
  protected final File path() {
    return new File(String.format("%s/config/%s.json", System.getProperty("com.sun.aas.instanceRoot"), this.realm));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the configuration properties with default values.
   */
  protected void initialize() {
    this.property = new HashMap<>();
    this.property.put(TYPE,             "plain");
    this.property.put(HEADER,           "oam_remote_user");
    this.property.put(DATASOURCE,       "jdbc/idsDS");
    this.property.put(PRINCIPAL_QUERY,  "SELECT usr.id FROM igt_users usr WHERE usr.username = ?");
    this.property.put(PERMISSION_QUERY, "SELECT url.rol_id FROM igt_users usr,igt_userroles url WHERE url.usr_id = usr.id AND UPPER(usr.username) = UPPER(?)");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonValue
  /**
   ** Return the appropriate Java representation of a {@link JsonValue}.
   **
   ** @param  value              the {@link JsonValue} provided.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    the Java representation of the
   **                            {@link JsonValue} provided.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  private static Object jsonValue(final JsonValue value)
    throws JsonException {

    if (value != null) {
      switch (value.getValueType()) {
        case NULL   : return null;
        case TRUE   : return Boolean.TRUE;
        case FALSE  : return Boolean.FALSE;
        case STRING : return ((JsonString)value).getString();
//        case NUMBER : return ((JsonNumber)value).numberValue();
        case ARRAY  : return ((JsonArray)value).stream().map(e -> jsonValue(e)).collect(Collectors.toList());
      }
    }
    return null;
  }
}