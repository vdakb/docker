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

    File        :   TokenAsserterConfigurationMBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TokenAsserterConfigurationMBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.jmx;

import java.util.List;

import java.io.Serializable;

import org.glassfish.gmbal.ManagedAttribute;

////////////////////////////////////////////////////////////////////////////////
// interface TokenAsserterConfigurationMBean
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** MXBean declaration for managing token asserter configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface TokenAsserterConfigurationMBean extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The Microprofile Config key for the type of the assertion send by the
   ** authenticating authority is <code>{@value}</code>.
   */
  static final String TYPE             = "assertionType";

  /**
   ** The Microprofile Config key for the request header providing the identity
   ** of an authenticated user is <code>{@value}</code>.
   */
  static final String HEADER           = "assertionHeader";

  /**
   ** The Microprofile Config key for the PublicKey Material of the signing
   ** authority is <code>{@value}</code>.
   */
  static final String SIGNING_MATERIAL = "signingMaterial";
  
  /**
   ** The Microprofile Config key for the PublicKey location of the signing
   ** authority is <code>{@value}</code>.
   */
  static final String SIGNING_LOCATION = "signingLocation";

  /**
   ** The Microprofile Config key for the JNDI name of the JDBC DataSource used
   ** for authentication and authotization purpose is <code>{@value}</code>.
   */
  static final String DATASOURCE       = "dataSource";

  /**
   ** The Microprofile Config key for the authentication query used to
   ** authenticate users based on specific key types is <code>{@value}</code>.
   */
  static final String PRINCIPAL_QUERY  = "principalQuery";

  /**
   ** The Microprofile Config key for the authentication query used to
   ** authorize users based on specific key types is <code>{@value}</code>.
   */
  static final String PERMISSION_QUERY = "permissionQuery";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
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
  boolean contains(final Object key);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyString
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
  String propertyString(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyString
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
  String propertyString(final String name, final String defaultValue);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyList
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
  List<String> propertyList(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyList
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
  List<String> propertyList(final String name, final List<String> defaultValue);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRealm
  /**
   ** Returns the realm that will be sent via the <code>WWW-Authenticate</code>
   ** header.
   **
   ** @return                    the realm that will be sent via the
   **                            <code>WWW-Authenticate</code> header.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getRealm();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAssertionType
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
  @ManagedAttribute(id=TYPE)
  void setAssertionType(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAssertionType
  /**
   ** Returns the type of the assertion send by the authenticating authority.
   **
   ** @return                    the type of the assertion send by the
   **                            authenticating authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getAssertionType();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAssertionHeader
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
  @ManagedAttribute(id=HEADER)
  void setAssertionHeader(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAssertionHeader
  /**
   ** Returns the request header providing the identity of an authenticated user.
   **
   ** @return                    the request header providing the identity of an
   **                            authenticated user.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getAssertionHeader();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaterial
  /**
   ** Sets the PEM encoded certificate as the source of the signing
   ** authority.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #SIGNING_MATERIAL signingMaterial}.
   **
   ** @param  value              the PEM encoded certificate as the source of
   **                            the signing authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id=SIGNING_MATERIAL)
  void setMaterial(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaterial
  /**
   ** Returns the PEM encoded certificate as the source of the signing
   ** authority.
   **
   ** @return                    the PEM encoded certificate as the source of
   **                            the signing authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getMaterial();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation
  /**
   ** Sets the location of the <code>PublicKey</code> of the signing authority.
   ** <p>
   ** The value <b>must</b> represent a URL using either <code>https</code> in
   ** the case using a Remote JSON Web Key (JWK) as the source, or
   ** <code>file</code> when using a PEM encoded certificate as the source to
   ** retrieve the public key material.
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
  @ManagedAttribute(id=SIGNING_LOCATION)
  void setLocation(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocation
  /**
   ** Returns the PublicKey local filesystem location of the signing authority.
   **
   ** @return                    the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getLocation();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDataSource
  /**
   ** Sets the JNDI name of the JDBC DataSource used for authentication and
   ** authotization purpose.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #DATASOURCE dataSource}.
   **
   ** @param  value              the JNDI name of the JDBC DataSource used for
   **                            authentication and authotization purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id=DATASOURCE)
  void setDataSource(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataSource
  /**
   ** Returns the JNDI name of the JDBC DataSource used for authentication and
   ** authotization purpose.
   **
   ** @return                    the JNDI name of the JDBC DataSource used for
   **                            authentication and authotization purpose.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getDataSource();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalQuery
  /**
   ** Sets the query used to authenticate users based on specific key types.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #PRINCIPAL_QUERY principalQuery}.
   **
   ** @param  value              the query used to authenticate users based on
   **                            specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id=PRINCIPAL_QUERY)
  void setPrincipalQuery(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalQuery
  /**
   ** Returns the authentication query used to authenticate users based on
   ** specific key types.
   **
   ** @return                    the query used to authenticate users based on
   **                            specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getPrincipalQuery();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPermissionQuery
  /**
   ** Sets the query used to authorize users based on specific key types.
   ** <p>
   ** Required property.
   ** <p>
   ** To set this property use {@link #PERMISSION_QUERY permissionQuery}.
   **
   ** @param  value              the query used to authorize users based on
   **                            specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id=PERMISSION_QUERY)
  void setPermissionQuery(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPermissionQuery
  /**
   ** Returns the query used to authorize users based on specific key types.
   **
   ** @return                    the query used to authorize users based on
   **                            specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getPermissionQuery();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refresh the configuration from the fileystem.
   */
  void refresh();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save
  /**
   ** Save the configuration to the file system.
   */
  void save();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validate the consistency of the configuration.
   */
  void validate();
}