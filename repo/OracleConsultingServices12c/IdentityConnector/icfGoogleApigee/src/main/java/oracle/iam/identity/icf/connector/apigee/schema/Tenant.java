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

    File        :   Tenant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Tenant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;

import oracle.iam.identity.icf.connector.apigee.Marshaller;

////////////////////////////////////////////////////////////////////////////////
// class Tenant
// ~~~~~ ~~~~~~
/**
 ** The <code>Tenant</code> REST resource in Google Apigee Edge.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Marshaller.TENANT_NAME)
public class Tenant extends Entity<Tenant> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The JSON tag name of the <code>type</code> attribute */
  public static final String TYPE        = "type";

  /** The JSON tag name of the <code>name</code> attribute */
  public static final String NAME        = "name";

  /** The JSON tag name of the <code>firstName</code> attribute */
  public static final String DISPLAYNAME = "displayName";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty(TYPE)
  private String              type;

  @Attribute
  @JsonProperty(NAME)
  private String              name;

  @Attribute
  @JsonProperty(DISPLAYNAME)
  private String              displayName;

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
   ** @return                    the type of the <code>Tenant</code>.
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
  public final Tenant displayName(final String value) {
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
  public final int hashCode() {
    int result = this.type != null ? this.type.hashCode()    : 0;
    result = 31 * result + (this.name         != null ? this.name.hashCode()         : 0);
    result = 31 * result + (this.displayName  != null ? this.displayName.hashCode()  : 0);
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
  public final boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Tenant that = (Tenant)other;
    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    return !(this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName != null);
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
  public final String toString() {
    return this.name;
  }
}