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

    File        :   Name.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Name.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

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

  @JsonProperty("formatted")
  @Attribute(description="The full name, including all middle names, titles, and suffixes as appropriate, formatted for display (for example, Ms. Barbara J Jensen, III.).", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String formatted;

  @JsonProperty("familyName")
  @Attribute(description="The family name of the User, or Last Name in most Western languages (for example, Jensen given the full name Ms. Barbara J Jensen, III.).", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String familyName;

  @JsonProperty("givenName")
  @Attribute(description="The given name of the User, or First Name in most Western languages (for example, Barbara given the full name Ms. Barbara J Jensen, III.).", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String givenName;

  @JsonProperty("middleName")
  @Attribute(description="The middle name(s) of the User (for example, Robert given the full name Ms. Barbara J Jensen, III.).", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String middleName;

  @JsonProperty("honorificPrefix")
  @Attribute(description="The honorific prefix(es) of the User, or Title in most Western languages (for example, Ms. given the full name Ms. Barbara J Jensen, III.).", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String honorificPrefix;

  @JsonProperty("honorificSuffix")
  @Attribute(description="The honorific suffix(es) of the User, or Suffix in most Western languages (for example, III. given the full name Ms. Barbara J Jensen, III.)", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String honorificSuffix;

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
  // Method:   formatted
  /**
   ** Sets the full name, including all middle names, titles, and suffixes as
   ** appropriate, formatted for display
   ** (for example, Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the full name, including all middle names,
   **                            titles, and suffixes as appropriate, formatted
   **                            for display.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Name</code>.
   */
  public Name formatted(final String value) {
    this.formatted = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Returns the full name, including all middle names, titles, and suffixes as
   ** appropriate, formatted for display
   ** (for example, Ms. Barbara J Jensen, III.).
   **
   ** @return                    the full name, including all middle names,
   **                            titles, and suffixes as appropriate, formatted
   **                            for display.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String formatted() {
    return this.formatted;
  }

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
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Name</code>.
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
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Name</code>.
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
  // Method:   middleName
  /**
   ** Sets the middle name(s) of the User (for example, Robert given the full
   ** name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the middle name(s) of the User.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Name</code>.
   */
  public Name middleName(final String value) {
    this.middleName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   middleName
  /**
   ** Returns the middle name(s) of the User (for example, Robert given the full
   ** name Ms. Barbara J Jensen, III.).
   **
   ** @return                    the middle name(s) of the User.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String middleName() {
    return this.middleName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   honorificPrefix
  /**
   ** Sets the honorific prefix(es) of the User, or Title in most Western
   ** languages (for example, Ms. given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @param  value              the honorific prefix(es) of the User, or Title
   **                            in most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Name</code>.
   */
  public Name honorificPrefix(final String value) {
    this.honorificPrefix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   honorificPrefix
  /**
   ** Returns the honorific prefix(es) of the User, or Title in most Western
   ** languages (for example, Ms. given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the honorific prefix(es) of the User, or Title
   **                            in most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String honorificPrefix() {
    return this.honorificPrefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   honorificSuffix
  /**
   ** Sets the honorific suffix(es) of the User, or Suffix in most Western
   ** languages (for example, III. given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @param  value              the honorific suffix(es) of the User, or Suffix
   **                            in most Western  languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Name</code>.
   */
  public Name honorificSuffix(final String value) {
    this.honorificSuffix = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   honorificSuffix
  /**
   ** Returns the honorific suffix(es) of the User, or Suffix in most Western
   ** languages (for example, III. given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the honorific suffix(es) of the User, or Suffix
   **                            in most Western  languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String honorificSuffix() {
    return this.honorificSuffix;
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
    int result = this.formatted != null ? this.formatted.hashCode() : 0;
    result = 31 * result + (this.familyName      != null ? this.familyName.hashCode()      : 0);
    result = 31 * result + (this.givenName       != null ? this.givenName.hashCode()       : 0);
    result = 31 * result + (this.middleName      != null ? this.middleName.hashCode()      : 0);
    result = 31 * result + (this.honorificPrefix != null ? this.honorificPrefix.hashCode() : 0);
    result = 31 * result + (this.honorificSuffix != null ? this.honorificSuffix.hashCode() : 0);
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
    if (this.formatted != null ? !this.formatted.equals(that.formatted) : that.formatted != null)
      return false;

    if (this.familyName != null ? !this.familyName.equals(that.familyName) : that.familyName != null)
      return false;

    if (this.givenName != null ? !this.givenName.equals(that.givenName) : that.givenName != null)
      return false;

    if (this.middleName != null ? !this.middleName.equals(that.middleName) : that.middleName != null)
      return false;

    if (this.honorificPrefix != null ? !this.honorificPrefix.equals(that.honorificPrefix) : that.honorificPrefix != null)
      return false;

    return !(this.honorificSuffix != null ? !this.honorificSuffix.equals(that.honorificSuffix) : that.honorificSuffix != null);
  }
}