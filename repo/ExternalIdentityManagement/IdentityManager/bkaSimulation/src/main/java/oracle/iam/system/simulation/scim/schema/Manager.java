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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   Manager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Manager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class Manager
// ~~~~~ ~~~~~~~
/**
 ** This represents a SCIM complex type that optionally allows
 ** <code>Service Provider</code>s to represent organizational hierarchy by
 ** referencing the 'id' attribute of another User.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Manager {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX = "manager";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("value")
  @Attribute(description="The id of the SCIM resource representing the User's manager.", required=true, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String value;

  @JsonProperty("displayName")
  @Attribute(description="The displayName of the User's manager.", required=false, caseExact=false, mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String displayName;

  @JsonProperty("$ref")
  @Attribute(description="The URI of the SCIM resource representing the User's manager.", required=true, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE, reference={"User"} )
  private URI    ref;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Manager</code> SCIM Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Manager() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the identifier of the SCIM resource representing the User's manager.
   **
   ** @param  value              the identifier of the SCIM resource
   **                            representing the User's manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Manager</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Manager</code>.
   */
  public Manager value(final String value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the identifier of the SCIM resource representing the User's
   ** manager.
   **
   ** @return                    the identifier of the SCIM resource
   **                            representing the User's manager.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the displayName of the User's manager.
   **
   ** @param  value             the displayName of the User's manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Manager</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Manager</code>.
   */
  public Manager displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the displayName of the User's manager.
   **
   ** @return                    the displayName of the User's manager.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ref
  /**
   ** Sets the URI of the corresponding Manager resource to which the user
   ** belongs.
   **
   ** @param  value              the URI of the corresponding Manager resource to
   **                            which the user belongs.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>Manager</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Manager</code>.
   */
  public Manager ref(final URI value) {
    this.ref = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ref
  /**
   ** Returns the URI of the corresponding Manager resource to which the user
   ** belongs.
   **
   ** @return                    the URI of the corresponding Manager resource to
   **                            which the user belongs.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public URI ref() {
    return this.ref;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   */
  @Override
  public int hashCode() {
    int result = this.value != null ? this.value.hashCode() : 0;
    result = 31 * result + (this.ref         != null ? this.ref.hashCode()         : 0);
    result = 31 * result + (this.displayName != null ? this.displayName.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Manager</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Manager</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Manager that = (Manager)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    if (this.ref != null ? !this.ref.equals(that.ref) : that.ref != null)
      return false;

    return !(this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName != null);
  }
}