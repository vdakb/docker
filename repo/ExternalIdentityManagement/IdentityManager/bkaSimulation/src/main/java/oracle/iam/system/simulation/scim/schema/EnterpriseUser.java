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

    File        :   EnterpriseUser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EnterpriseUser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.rest.schema.Discoverable;
import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class EnterpriseUser
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This represents a SCIM schema extension commonly used in representing users
 ** that belong to, or act on behalf of a business or enterprise.
 ** <br>
 ** The core schema for <code>User</code> is identified using the URI's:
 ** <code>urn:scim:schemas:extension:enterprise:1.0</code>
 ** <code>urn:ietf:params:scim:schemas:extension:enterprise:2.0:User</code>
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EnterpriseUser<T extends EnterpriseUser> implements Discoverable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("employeeNumber")
  @Attribute(description="Numeric or alphanumeric identifier assigned to a person, typically based on order of hire or association with an organization.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  employeeNumber;

  @JsonProperty("costCenter")
  @Attribute(description="Identifies the name of a cost center.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  costCenter;

  @JsonProperty("organization")
  @Attribute(description="Identifies the name of an organization.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  organization;

  @JsonProperty("division")
  @Attribute(description="Identifies the name of a division.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  division;

  @JsonProperty("department")
  @Attribute(description="Identifies the name of a department.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  department;

  @JsonProperty("manager")
  @Attribute(description="The User's manager.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT)
  private Manager manager;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EnterpriseUser</code> SCIM Resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected EnterpriseUser() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   employeeNumber
  /**
   ** Sets the numeric or alphanumeric identifier assigned to a person.
   **
   ** @param  value              the numeric or alphanumeric identifier assigned
   **                            to a person.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>EnterpriseUser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>EnterpriseUser</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T employeeNumber(final String value) {
    this.employeeNumber = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   employeeNumber
  /**
   ** Returns the numeric or alphanumeric identifier assigned to a person.
   **
   ** @return                    the numeric or alphanumeric identifier assigned
   **                            to a person.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String employeeNumber() {
    return this.employeeNumber;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   costCenter
  /**
   ** Sets the name of a cost center.
   **
   ** @param  value              the name of a cost center.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>EnterpriseUser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>EnterpriseUser</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T costCenter(final String value) {
    this.costCenter = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   costCenter
  /**
   ** Returns the name of a cost center.
   **
   ** @return                    the name of a cost center.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String costCenter() {
    return this.costCenter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Sets the name of an organization.
   **
   ** @param  value              the name of an organization.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>EnterpriseUser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>EnterpriseUser</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T organization(final String value) {
    this.organization = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Returns the name of an organization.
   **
   ** @return                    the name of an organization.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String organization() {
    return this.organization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   division
  /**
   ** Sets the name of a division.
   **
   ** @param  value              the name of a division.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   **
   ** @return                    the <code>EnterpriseUser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>EnterpriseUser</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T division(final String value) {
    this.division = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   division
  /**
   ** Returns the name of a division.
   **
   ** @return                    the name of a division.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String division() {
    return this.division;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   department
  /**
   ** Sets the name of a department.
   **
   ** @param  value              the name of a department.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   **
   ** @return                    the <code>EnterpriseUser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>EnterpriseUser</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T department(final String value) {
    this.department = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   department
  /**
   ** Returns the name of a department.
   **
   ** @return                    the name of a department.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String department() {
    return this.department;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manager
  /**
   ** Sets the User's manager.
   **
   ** @param  value              the User's manager.
   **                            <br>
   **                            Allowed object is {@link Manager}.
   **
   **
   ** @return                    the <code>EnterpriseUser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>EnterpriseUser</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T manager(final Manager value) {
    this.manager = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manager
  /**
   ** Returns the User's manager.
   **
   ** @return                    the User's manager.
   **                            <br>
   **                            Possible object is {@link Manager}.
   */
  public Manager manager() {
    return this.manager;
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
    int result = this.employeeNumber != null ? this.employeeNumber.hashCode()        : 0;
    result = 31 * result + (this.costCenter   != null ? this.costCenter.hashCode()   : 0);
    result = 31 * result + (this.organization != null ? this.organization.hashCode() : 0);
    result = 31 * result + (this.division     != null ? this.division.hashCode()     : 0);
    result = 31 * result + (this.department   != null ? this.department.hashCode()   : 0);
    result = 31 * result + (this.manager      != null ?this.manager.hashCode()       : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>EnterpriseUserExtension</code>s are considered equal if and only
   ** if they represent the same properties. As a consequence, two given
   ** <code>EnterpriseUserExtension</code>s may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
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

    // ensure inheritance
    if (!super.equals(other))
      return false;

    final EnterpriseUser that = (EnterpriseUser)other;
    if (this.employeeNumber != null ? !this.employeeNumber.equals(that.employeeNumber) : that.employeeNumber != null)
      return false;

    if (this.costCenter != null ? !this.costCenter.equals(that.costCenter) : that.costCenter != null)
      return false;

    if (this.organization != null ? !this.organization.equals(that.organization) : that.organization != null)
      return false;

    if (this.division != null ? !this.division.equals(that.division) : that.division != null)
      return false;

    if (this.department != null ? !this.department.equals(that.department) : that.department != null)
      return false;

    return !(this.manager != null ? !this.manager.equals(that.manager) : that.manager != null);
  }
}