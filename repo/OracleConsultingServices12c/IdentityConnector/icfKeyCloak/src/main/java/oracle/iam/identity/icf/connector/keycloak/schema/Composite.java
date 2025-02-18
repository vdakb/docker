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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Composite.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Composite.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// final class Composite
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** The descriptor to handle structural composite entities of a role.
 ** <p>
 ** Any realm or client level role can be turned into a composite role. A
 ** composite role is a role that has one or more additional roles associated
 ** with it. When a composite role is mapped to the user, the user also gains
 ** the roles associated with that composite. This inheritance is recursive so
 ** any composite of composites also gets inherited.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Composite {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the composite resource */
  @JsonIgnore
  public static final String ENTITY = "Composite";
  /**
   ** The name of the role containerId attribute.
   */
  @JsonIgnore
  public static final String REALM  = "realm";
  /**
   ** The name of the role name attribute.
   */
  @JsonIgnore
  public static final String CLIENT = "client";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Realm-level roles are a global namespace to define your roles.
   */
  @JsonProperty(REALM)
  @Attribute(value=REALM, multiValueClass=String.class)
  private Set<String>               realm;
  /**
   ** Client roles are basically a namespace dedicated to a client. Each
   ** client gets its own namespace. Client roles are managed under the Roles
   ** tab under each individual client. You interact with this UI the same way
   ** you do for realm-level roles.
   */
  @JsonProperty(CLIENT)
  @Attribute(CLIENT)
  private Map<String, List<String>> client;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Composite</code> REST representation that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Composite() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   realm
  /**
   ** Sets the Realm-level of the <code>Composite</code>.
   **
   ** @param  value              the Realm-level of the <code>Composite</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Composite</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Composite</code>.
   */
  public final Composite realm(final Set<String> value) {
    this.realm = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   realm
  /**
   ** Returns the Realm-level of the <code>Composite</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a
   ** snapshot. Therefore any modification you make to the returned list wil
   ** be present
   ** inside the JSON object.
   **
   ** @return                    the Realm-level of the <code>Composite</code>.
   **                            <br>
   **                            Possible  object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> realm() {
    return this.realm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Sets the namespace dedicated to a client of the <code>Composite</code>.
   **
   ** @param  value              the namespace dedicated to a client of the
   **                            <code>Composite</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link List} of strings as the value.
   **
   ** @return                    the <code>Composite</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Composite</code>.
   */
  public final Composite client(final Map<String, List<String>> value) {
    this.client = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Returns the namespace dedicated to a client of the
   ** <code>Composite</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a
   ** snapshot. Therefore any modification you make to the returned list wil
   ** be present
   ** inside the JSON object.
   **
   ** @return                    the namespace dedicated to a client of the
   **                            <code>Composite</code>.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link List} of strings as the value.
   */
  public final Map<String, List<String>> client() {
    return this.client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overidden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.realm, this.client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Composite</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Composite</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Composite that = (Composite)other;
    return Objects.equals(this.realm,  that.realm)
        && Objects.equals(this.client, that.client);
  }
}