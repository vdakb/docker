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

    File        :   Tenant.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Tenant.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.schema;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.system.simulation.scim.schema.Support;

public class Tenant extends Entity<Tenant> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  @JsonProperty
  private String       type;

  @JsonProperty
  private String       name;

  @JsonProperty
  private String       displayName;

  @JsonProperty
  private Property     properties  = new Property();

  /**
   ** The collection of environment names in an organization.
   */
  @JsonProperty("environments")
  private List<String> environment = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Tenant</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Tenant() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type of the <code>Tenant</code>.
   **
   ** @param  value              the type of the <code>Tenant</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Tenant</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Tenant</code>.
   */
  public final Tenant type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the <code>Tenant</code>.
   **
   ** @return                    the identifier of the <code>Tenant</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the identifier of the <code>Tenant</code>.
   **
   ** @param  value              the identifier of the <code>Tenant</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Tenant</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Tenant</code>.
   */
  public final Tenant name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the identifier of the <code>Tenant</code>.
   **
   ** @return                    the identifier of the <code>Tenant</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the full name of the <code>Tenant</code>.
   **
   ** @param  value              the full name of the <code>Tenant</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Tenant</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Tenant</code>.
   */
  public Tenant displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the full name of the <code>Tenant</code>.
   **
   ** @return                    the full name of the <code>Tenant</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Sets the properties of the <code>Tenant</code>.
   **
   ** @param  value              the properties of the <code>Tenant</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Tenant</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Tenant</code>.
   */
  public Tenant properties(final Property value) {
    this.properties = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Returns the properties of the <code>Tenant</code>.
   **
   ** @return                    the properties of the <code>Tenant</code>.
   **                            <br>
   **                            Possible object is {@link Property}.
   */
  public Property properties() {
    return this.properties;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Sets the environments associated with the <code>Tenant</code>.
   **
   ** @param  value              the environments associated with the
   **                            <code>Tenant</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Tenant</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Tenant</code>.
   */
  public Tenant environment(final List<String> value) {
    this.environment = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Returns the environments associated with the <code>Tenant</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the environments associated with the
   **                            <code>Tenant</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> environment() {
    return this.environment;
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
    int result = super.hashCode();
    result = PRIME * result + (this.name        != null ? this.name.hashCode()        : 0);
    result = PRIME * result + (this.displayName != null ? this.displayName.hashCode() : 0);
    result = PRIME * result + (this.properties  != null ? this.properties.hashCode()  : 0);
    result = PRIME * result + (this.environment != null ? this.environment.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Tenant</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Tenant</code>s may be different even though they contain the same
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

    final Tenant that = (Tenant)other;
    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName != null)
      return false;

    if (this.properties != null ? !this.properties.equals(that.properties) : that.properties != null)
      return false;

    if (this.environment != null ? !this.environment.equals(that.environment) : that.environment != null)
      return false;

    return super.equals(other);
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