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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Email.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Email.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.scim.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Email
// ~~~~~ ~~~~~
/**
 ** Stores email address for the user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Email {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX = "emails";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("primary")
  @Attribute(description="A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred mailing address or primary e-mail address. The primary attribute value 'true' MUST appear no more than once.")
  private Boolean primary;

  @JsonProperty("value")
  @Attribute(description="E-mail addresses for the user. The value\nSHOULD be canonicalized by the Service Provider, e.g.\nbjensen@example.com instead of bjensen@EXAMPLE.COM. Canonical Type\nvalues of work, home, and other.")
  private String  value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Email</code> SCIM Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Email() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Email</code> SCIM Resource from the provided value.
   **
   ** @param  value              the e-Mail Address.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @JsonCreator
  public Email(final String value) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @param  value              the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the {@link Email} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Email}.
   */
  public Email primary(final Boolean value) {
    this.primary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @return                    the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the email addresses for the user.
   **
   ** @param  value              the email addresses for the user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Email} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Email}.
   */
  public Email value(final String value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the email addresses for the user.
   **
   ** @return                    the email addresses for the user.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String value() {
    return this.value;
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
    result = 31 * result + (this.primary != null ? this.primary.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Email</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Email</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different order.
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

    final Email that = (Email)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    return !(this.primary != null ? !this.primary.equals(that.primary) : that.primary != null);
  }
}