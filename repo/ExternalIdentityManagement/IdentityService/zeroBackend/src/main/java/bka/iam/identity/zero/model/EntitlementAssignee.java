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
public final class EntitlementAssignee {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String RESOURCE_NAME = "Assignee";
  
  // Names of the JSON attributes
  private final String JSON_ATTR_IDENTITY      = "identity";
  private final String JSON_ATTR_ORGANIZATION = "organization";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(JSON_ATTR_IDENTITY)
  private String identity;

  @JsonProperty(JSON_ATTR_ORGANIZATION)
  private String organization;

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
  public EntitlementAssignee() {
    // ensure inheritance
    super();
  }

  public EntitlementAssignee(String identity, String organization) {
    // ensure inheritance
    super();
    
    this.identity = identity;
    this.organization = organization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Sets the identity of the entitlement.
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
  public EntitlementAssignee identity(final String value) {
    this.identity = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Returns the identity of the entitlement.
   **
   ** @return                    the key of the entitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String identity() {
    return this.identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accounts
  /**
   ** Sets the accounts of the entitlement.
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
  public EntitlementAssignee organizations(final String value) {
    this.organization = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Returns the organization of this EntitlementAssignee.
   **
   ** @return                    the code of the entitlement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String organization() {
    return this.organization;
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
    StringBuffer result = new StringBuffer("Identity object: ");
    result.append(" identity=\"").append(this.identity);
    result.append("\" organization=\"").append(this.organization);
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
    return Objects.hash(this.identity,
                        this.organization);
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

    final EntitlementAssignee that = (EntitlementAssignee)other;
    return Objects.equals(this.identity,               that.identity)
        && Objects.equals(this.organization,           that.organization);
  }
}