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

    System      :   Identity Service Library
    Subsystem   :   ZeRo Backend

    File        :   OIMEntitlement.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    OIMEntitlement.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// final class OIMEntitlement
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Stores OIM Entitlement object.
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Account {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String RESOURCE_NAME = "Account";
  
  // Names of the JSON attributes
  private final String JSON_ATTR_NAME = "name";
  private final String JSON_ATTR_TYPE = "type";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(JSON_ATTR_NAME)
  private String name;

  @JsonProperty(JSON_ATTR_TYPE)
  private oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OIMEntitlement</code> Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Account() {
    // ensure inheritance
    super();
  }

  public Account(String name, oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE type) {
    // ensure inheritance
    super();
    this.name = name;
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the entitlement.
   **
   ** @param  value              the 'key' of the entitlement.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public Account name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the entitlement.
   **
   ** @return                    the key of the entitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type of the entitlement.
   **
   ** @param  value              the 'code' of the entitlement.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public Account type(final oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the entitlement.
   **
   ** @return                    the code of the entitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public oracle.iam.provisioning.vo.Account.ACCOUNT_TYPE type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this object.
   **
   ** @return                    a string representation of this object.
   **                            <br>
   **                            Possible object is <code>String</code>.
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer("Account object: ");
    result.append(" name=\"").append(this.name);
    result.append("\" type=\"").append(this.type);
    result.append("\"");
    return result.toString();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
    return Objects.hash(this.name,
                        this.type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entitlement</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entitlement</code>s may be different even though they contain the
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

    final Account that = (Account)other;
    return Objects.equals(this.name,                   that.name)
        && Objects.equals(this.type,                   that.type);
  }
}