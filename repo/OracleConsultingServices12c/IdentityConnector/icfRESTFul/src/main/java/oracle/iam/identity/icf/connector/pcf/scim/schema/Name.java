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

    File        :   Name.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Name.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.scim.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Name
// ~~~~~ ~~~~
/**
 ** The components of the user's name.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Name {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX = "name";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("familyName")
  @Attribute(description="The family name of the User, or Last Name in most Western languages (for example, Jensen given the full name Ms. Barbara J Jensen, III.).")
  private String familyName;

  @JsonProperty("givenName")
  @Attribute(description="The given name of the User, or First Name in most Western languages (for example, Barbara given the full name Ms. Barbara J Jensen, III.).")
  private String givenName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Name</code> SCIM Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Name() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   familyName
  /**
   ** Sets the family name of the User, or Last Name in most Western languages
   ** (for example, Jensen given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the family name of the User, or Last Name in
   **                            most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Name} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Name}.
   */
  public Name familyName(final String value) {
    this.familyName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   familyName
  /**
   ** Returns the family name of the User, or Last Name in most Western
   ** languages (for example, Jensen given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the family name of the User, or Last Name in
   **                            most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String familyName() {
    return this.familyName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   givenName
  /**
   ** Sets the given name of the User, or First Name in most Western languages
   ** (for example, Barbara given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the given name of the User, or First Name in
   **                            most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Name} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Name}.
   */
  public Name givenName(final String value) {
    this.givenName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   givenName
  /**
   ** Returns the given name of the User, or First Name in most Western
   ** languages (for example, Barbara given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the given name of the User, or First Name in
   **                            most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String givenName() {
    return this.givenName;
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
    int result = this.familyName != null ? this.familyName.hashCode() : 0;
    result = 31 * result + (this.givenName != null ? this.givenName.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Name</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Name</code>s may be
   ** different even though they contain the same set of names with the same
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

    final Name that = (Name)other;
    if (this.familyName != null ? !this.familyName.equals(that.familyName) : that.familyName != null)
      return false;

    return !(this.givenName != null ? !this.givenName.equals(that.givenName) : that.givenName != null);
  }
}