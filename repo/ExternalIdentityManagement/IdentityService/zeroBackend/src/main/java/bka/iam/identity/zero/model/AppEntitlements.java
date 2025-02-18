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

import java.util.List;
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
public final class AppEntitlements {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String RESOURCE_NAME = "AppEntitlements";
  
  // Names of the JSON attributes
  private final String JSON_ATTR_APP_INSTANCE_NAME = "appInstanceName";
  private final String JSON_ATTR_ENT_LIST = "entitlements";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(JSON_ATTR_APP_INSTANCE_NAME)
  private String appInstanceName;

  @JsonProperty(JSON_ATTR_ENT_LIST)
  private List<OIMEntitlement> entitlements;

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
  public AppEntitlements() {
    // ensure inheritance
    super();
  }
  public AppEntitlements(String appInstanceName, List<OIMEntitlement> entitlements) {
    // ensure inheritance
    super();
    this.appInstanceName = appInstanceName;
    this.entitlements = entitlements;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appInstanceName
  /**
   ** Sets the associated Application Instance name.
   **
   ** @param  value              the Application Instance name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public AppEntitlements appInstanceName(final String value) {
    this.appInstanceName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appInstanceName
  /**
   ** Returns the name of the associated Application Instance.
   **
   ** @return                    the name of the associated Application Instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String appInstanceName() {
    return this.appInstanceName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlements
  /**
   ** Sets the associated Application Instance display name.
   **
   ** @param  value              the Application Instance display name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entitlement</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entitlement</code>.
   */
  public AppEntitlements entitlements(final List<OIMEntitlement> value) {
    this.entitlements = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appInstanceDisplayName
  /**
   ** Returns the display name of the associated Application Instance.
   **
   ** @return                    the display name of the associated Application
   **                            Instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public List<OIMEntitlement> entitlements() {
    return this.entitlements;
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
    StringBuffer result = new StringBuffer("OIMEntitlement object: ");
    result.append(" appInstanceName=\"").append(this.appInstanceName);
    result.append("\" entitlements=\"").append(this.entitlements);
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
    return Objects.hash(this.appInstanceName,
                        this.entitlements);
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

    final AppEntitlements that = (AppEntitlements)other;
    return Objects.equals(this.appInstanceName, that.appInstanceName)
        && Objects.equals(this.entitlements,    that.entitlements);
  }
}