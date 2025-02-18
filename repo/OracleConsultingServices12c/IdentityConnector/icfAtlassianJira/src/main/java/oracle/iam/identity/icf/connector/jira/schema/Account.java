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
    Subsystem   :   Atlassian Jira Server Connector

    File        :   Account.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Account.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Account
// ~~~~~ ~~~~~~~
/**
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class Account extends Adressable<Account>{

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("key")
  @Attribute(Attribute.IDENTIFIER)
  private String             id;

  @JsonProperty
  @Attribute(Attribute.STATUS)
  private Boolean            active;

  @Attribute
  @JsonProperty
  private Boolean            deleted;

  @JsonProperty("name")
  @Attribute(Attribute.UNIQUE)
  private String             userName;

  @Attribute
  @JsonProperty
  private String             firstName;

  @Attribute
  @JsonProperty
  private String             lastName;

  @Attribute
  @JsonProperty
  private String             displayName;

  @Attribute
  @JsonProperty
  private String             locale;

  @Attribute
  @JsonProperty
  private String             timeZone;

  @Attribute
  @JsonProperty("avatarUrls")
  private Avatar             avatar;

  @Attribute
  @JsonProperty
  private List<Project.Role> roles;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Account</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Account() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the identifier of the <code>Account</code>.
   **
   ** @param  value              the identifier of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the <code>Account</code>.
   **
   ** @return                    the identifier of the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Sets the activation status of the <code>Account</code>.
   **
   ** @param  value              the activation status of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account active(final Boolean value) {
    this.active = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Returns the activation status of the <code>Account</code>.
   **
   ** @return                    the activation status of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean active() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleted
  /**
   ** Sets the deletion status of the <code>Account</code>.
   **
   ** @param  value              the deletion status of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account deleted(final Boolean value) {
    this.deleted = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleted
  /**
   ** Returns the deletion status of the <code>Account</code>.
   **
   ** @return                    the deletion status of the
   **                            <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean deleted() {
    return this.deleted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Sets the user name of the <code>Account</code>.
   **
   ** @param  value              the name of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account userName(final String value) {
    this.userName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Returns the name of the <code>Account</code>.
   **
   ** @return                    the name of the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userName() {
    return this.userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Sets the family name of the User, or Last Name in most Western languages
   ** (for example, Jensen given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the family name of the User, or Last Name in
   **                            most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public Account lastName(final String value) {
    this.lastName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
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
  public String lastName() {
    return this.lastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Sets the given name of the User, or First Name in most Western languages
   ** (for example, Barbara given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the given name of the User, or First Name in
   **                            most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public Account firstName(final String value) {
    this.firstName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
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
  public String firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the display name, including all middle names, titles, and suffixes as
   ** appropriate, formatted for display (for example, Ms. Barbara J Jensen,
   ** III.).
   **
   ** @param  value              the display name, including all middle names,
   **                            titles, and suffixes as appropriate, formatted
   **                            for display.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public Account displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the full name, including all middle names, titles, and suffixes as
   ** appropriate, formatted for display
   ** (for example, Ms. Barbara J Jensen, III.).
   **
   ** @return                    the display name, including all middle names,
   **                            titles, and suffixes as appropriate, formatted
   **                            for display.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Sets the locale of the <code>Account</code>.
   **
   ** @param  value              the locale of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account locale(final String value) {
    this.locale = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns the locale of the <code>Account</code>.
   **
   ** @return                    the locale of the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String locale() {
    return this.locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Sets the time zone of the <code>Account</code>.
   **
   ** @param  value              the time zone of the <code>Account</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account timeZone(final String value) {
    this.timeZone = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Returns the time zone of the <code>Account</code>.
   **
   ** @return                    the time zone of the <code>Account</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String timeZone() {
    return this.timeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Sets the {@link List} of {@link Avatar}s that the <code>Account</code>
   ** belongs to.
   **
   ** @param  value              the {@link Avatar}s that the
   **                            <code>Account</code> belongs to.
   **                            <br>
   **                            Allowed object is {@link Avatar}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account avatar(final Avatar value) {
    this.avatar = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Returns the {@link Avatar}s that the <code>Account</code> belongs to.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the {@link Avatar}s that the
   **                            <code>Account</code> belongs to.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Avatar}.
   */
  public final Avatar avatar() {
    return this.avatar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Sets the {@link List} of {@link Project.Role}s the <code>Account</code>
   ** belongs to.
   **
   ** @param  value              the {@link List} of {@link Project.Role}s the
   **                            <code>Account</code> belongs to.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Project.Role}.
   **
   ** @return                    the <code>Account</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Account</code>.
   */
  public final Account role(final List<Project.Role> value) {
    this.roles = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the {@link List} of {@link Project.Role}s the <code>Account</code>
   ** belongs to.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the {@link List} of {@link Project.Role}s the
   **                            <code>Account</code> belongs to.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Project.Role}.
   */
  public final List<Project.Role> role() {
    return this.roles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Entity)
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
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.self        != null ? this.self.hashCode()        : 0);
    result = 31 * result + (this.active      != null ? this.active.hashCode()      : 0);
    result = 31 * result + (this.deleted     != null ? this.deleted.hashCode()     : 0);
    result = 31 * result + (this.userName    != null ? this.userName.hashCode()    : 0);
    result = 31 * result + (this.firstName   != null ? this.firstName.hashCode()   : 0);
    result = 31 * result + (this.lastName    != null ? this.lastName.hashCode()    : 0);
    result = 31 * result + (this.displayName != null ? this.displayName.hashCode() : 0);
    result = 31 * result + (this.locale      != null ? this.locale.hashCode()      : 0);
    result = 31 * result + (this.timeZone    != null ? this.timeZone.hashCode()    : 0);
    result = 31 * result + (this.avatar      != null ? this.avatar.hashCode()      : 0);
    result = 31 * result + (this.roles       != null ? this.roles.hashCode()       : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Account</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Account</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different order.
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

    final Account that = (Account)other;
    if (this.id != null ? !this.id.equals(that.id) : that.id != null)
      return false;

    if (this.active != null ? !this.active.equals(that.active) : that.active != null)
      return false;

    if (this.deleted != null ? !this.deleted.equals(that.deleted) : that.deleted != null)
      return false;

    if (this.userName != null ? !this.userName.equals(that.userName) : that.userName != null)
      return false;

    if (this.firstName != null ? !this.firstName.equals(that.firstName) : that.firstName != null)
      return false;

    if (this.lastName != null ? !this.lastName.equals(that.lastName) : that.lastName != null)
      return false;

    if (this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName != null)
      return false;

    if (this.locale != null ? !this.locale.equals(that.locale) : that.locale != null)
      return false;

    if (this.timeZone != null ? !this.timeZone.equals(that.timeZone) : that.timeZone != null)
      return false;

    return !(this.roles != null ? !this.roles.equals(that.roles) : that.roles != null);
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
    Entity.jsonify("id",        this.id,        builder);
    Entity.jsonify("active",    this.active,    builder);
    Entity.jsonify("deleted",   this.deleted,   builder);
    Entity.jsonify("userName",  this.userName,  builder);
    Entity.jsonify("firstName", this.firstName, builder);
    Entity.jsonify("lastName",  this.lastName,  builder);
    Entity.jsonify("locale",    this.locale,    builder);
    Entity.jsonify("timeZone",  this.timeZone,  builder);
    builder.append("}");
    return builder.toString();
  }
}