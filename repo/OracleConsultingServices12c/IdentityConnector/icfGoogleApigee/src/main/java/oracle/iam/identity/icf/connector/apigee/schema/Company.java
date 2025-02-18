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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   Company.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Company.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Company
// ~~~~~ ~~~~~~~
/**
 ** The <code>Company</code> REST resource in Google Apigee Edge.
 ** A company is a collection of developers managed as a single entity.
 ** <p>
 ** A company can be any grouping that is appropriate to an organization, for
 ** example, business unit, product line, or division. Grouping developers into
 ** companies is useful when the goal is to work with multiple developers
 ** associated under a single corporate entity for billing purposes, for
 ** example.
 ** <p>
 ** Companies are optional.
 ** <br>
 ** It not required that the developers in an organization are associated with a
 ** company.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** A developer is always a single entity, uniquely identified by the email
 ** element.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Company extends Entity<Company> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Read-only Apigee organization name of the company. */
  @Attribute
  @JsonProperty("organization")
  private String              tenant;

  /**
   ** The <code>Company</code> name.
   */
  @Attribute
  @JsonProperty("name")
  private String              name;

  /**
   ** The status of the credential for the <code>Company</code> either
   ** <ul>
   **   <li>active
   **   <li>inactive
   ** </ul>
   */
  @Attribute
  @JsonProperty("status")
  private String              status;

  /**
   ** The <code>Company</code> display name.
   */
  @Attribute
  @JsonProperty("displayName")
  private String              displayName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Company</code> REST Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Company() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Sets the tenant identifier of the <code>Company</code>.
   **
   ** @param  value              the tenant identifier of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Company</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Company</code>.
   */
  public final Company tenant(final String value) {
    this.tenant = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Returns the tenant identifier of the <code>Company</code>.
   **
   ** @return                    the tenant identifier of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String tenant() {
    return this.tenant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the unique identifier of the <code>Company</code>.
   **
   ** @param  value              the unique identifier of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Company</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Company</code>.
   */
  public final Company name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the unique identifier of the <code>Company</code>.
   **
   ** @return                    the unique identifier of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Sets the activation status of the <code>Company</code>.
   **
   ** @param  value              the activation status of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Company</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Company</code>.
   */
  public Company status(final String value) {
    this.status = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the activation status of the <code>Company</code>.
   **
   ** @return                    the activation status of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the display name of the <code>Company</code>.
   **
   ** @param  value              the display name of the <code>Company</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Company</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Company</code>.
   */
  public final Company displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the display name of the <code>Company</code>.
   **
   ** @return                    the display name of the <code>Company</code>.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String displayName() {
    return this.displayName;
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.tenant != null ? this.tenant.hashCode() : 0;
    result = PRIME * result + (this.name  != null ? this.name.hashCode()  : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Companies</code> are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Companies</code> may be different even though they contain the
   ** same set of names with the same values, but in a different
   ** order.
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

    final Company that = (Company)other;
    if (this.tenant != null ? !this.tenant.equals(that.tenant) : that.tenant != null)
      return false;

    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName != null)
      return false;

    return !(this.status != null ? !this.status.equals(that.status) : that.status != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{\"tenant\":[\"").append(this.tenant).append("]}");
    builder.append("{\"name\":[\"").append(this.name).append("]}");
    return builder.toString();
  }
}