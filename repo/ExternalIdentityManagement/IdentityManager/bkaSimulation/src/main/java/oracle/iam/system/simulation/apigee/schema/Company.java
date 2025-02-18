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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   Company.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Company.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.schema;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.system.simulation.scim.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// class Company
// ~~~~~ ~~~~~~~
/**
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
  @JsonProperty("organization")
  private String               tenant;

  /**
   ** The <code>Company</code> name.
   */
  @JsonProperty("name")
  private String                name;

  /**
   ** The status of the credential for the <code>Company</code> either
   ** <ul>
   **   <li>active
   **   <li>inactive
   ** </ul>
   */
  @JsonProperty("status")
  private String                status;

  /**
   ** The <code>Company</code> display name.
   */
  @JsonProperty("displayName")
  private String                displayName;

  /**
   ** The extended attributes of the <code>Company</code>.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("attributes")
  private Collection<Attribute> attribute    = new ArrayList<Attribute>();

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
  // Method:   attribute
  /**
   ** Sets the extended attribute of the <code>Company</code> mapped at
   ** <code>name</code>.
   **
   ** @param  name               the name of the  attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the  attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Company</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Company</code>.
   */
  public final Company attribute(final String name, final String value) {
    if (this.attribute == null) {
      this.attribute = new ArrayList<Attribute>();
    }
    this.attribute.add(new Attribute(name, value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Retusn the extended attribute of the <code>Company</code> mapped at
   ** <code>name</code>.
   **
   ** @param  name               the name of the  attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the extended attribute of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String attribute(final String name) {
    String value = null;
    if (this.attribute != null) {
      for (Attribute cursor : this.attribute) {
        if (cursor.name().equals(name)) {
          value = cursor.value();
          break;
        }
      }
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Return the extended attribute of the <code>Company</code>.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the extended attribute of the
   **                            <code>Company</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Attribute}.
   */
  public final Collection<Attribute> attribute() {
    return this.attribute;
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
  public final int hashCode() {
    int result = super.hashCode();
    result = PRIME * result + (this.tenant      != null ? this.tenant.hashCode()      : 0);
    result = PRIME * result + (this.status      != null ? this.status.hashCode()      : 0);
    result = PRIME * result + (this.name        != null ? this.name.hashCode()        : 0);
    result = PRIME * result + (this.displayName != null ? this.displayName.hashCode() : 0);
    return result;
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
    try {
      return Support.objectWriter().writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}