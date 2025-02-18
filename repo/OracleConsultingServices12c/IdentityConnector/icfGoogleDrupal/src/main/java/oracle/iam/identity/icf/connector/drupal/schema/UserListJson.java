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
    Subsystem   :   Google Drupal Connector

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserListJson.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.drupal.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 ** The <code>UserListJson</code> REST resource in Google Drupal.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class UserListJson extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // We only will be using UID, actually, then fetch each account individually, but let's describe the whole JSON
  /** The JSON tag name of the <code>uid</code> attribute. */
  public static final String UID                = "uid";

  /** The JSON tag name of the <code>name</code> (as in "username") attribute. */
  public static final String NAME               = "name";

  /** The JSON tag name of the <code>mail</code> attribute. */
  public static final String EMAIL              = "mail";

  /** The JSON tag name of the <code>lastName</code> attribute. */
  public static final String LASTNAME           = "last_name";

  /** The JSON tag name of the <code>firstName</code> attribute. */
  public static final String FIRSTNAME          = "first_name";

  /** The JSON tag name of the <code>roles</code> attribute. */
  public static final String ROLES              = "role_alias";

  /** The JSON tag name of the <code>field_behoerde</code> attribute. */
  public static final String FIELD_BEHOERDE     = "field_behoerde";

  /** The JSON tag name of the <code>value</code> attribute. */
  public static final String VALUE = "value";

  /** The JSON tag name of the <code>format</code> attribute. */
  public static final String FORMAT = "format";

  /** The JSON tag name of the <code>alias</code> attribute. */
  public static final String PATH_ALIAS = "alias";

  /** The JSON tag name of the <code>pid</code> attribute. */
  public static final String PATH_PID = "pid";

  /** The JSON tag name of the <code>langcode</code> attribute. */
  public static final String PATH_LANG = "langcode";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty(UID)
  private String         uid;

  @Attribute
  @JsonProperty(NAME)
  private String         name;

  @Attribute
  @JsonProperty(EMAIL)
  private String         email;

  @Attribute
  @JsonProperty(LASTNAME)
  private String         lastName;

  @Attribute
  @JsonProperty(FIRSTNAME)
  private String         firstName;

  @Attribute
  @JsonProperty(ROLES)
  // This API returns a (prety useless) list of comma-space separated role names.
  // We're not using it anyway, this is just for offer complete model.
  private String         roles;
  
  @Attribute
  @JsonProperty(FIELD_BEHOERDE)
  private String         behoerde;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>User</code> REST Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserListJson() {
    final String method = "UserListJson#constructor()";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Sets the uid of the <code>User</code>.
   **
   ** @param  value              the uid of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final UserListJson uid(final String value) {
    this.uid = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Returns the uid of the <code>User</code>.
   **
   ** @return                    the uid of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String uid() {
    return this.uid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the <code>User</code>.
   **
   ** @param  value              the (user)name of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final UserListJson name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>User</code>.
   **
   ** @return                    the (user)name of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Sets the email of the <code>User</code>.
   **
   ** @param  value              the email of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final UserListJson email(final String value) {
    this.email = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Returns the email of the <code>User</code>.
   **
   ** @return                    the email of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String email() {
    return this.email;
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final UserListJson lastName(final String value) {
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
  public final String lastName() {
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final UserListJson firstName(final String value) {
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
  public final String firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roles
  /**
   * Sets the collection of role that the user belongs to through direct
   * membership.
   *
   * @param value the list of role that the user belongs to.
   * <br>
   * Allowed object is {@link String} .
   *
   * @return the <code>User</code> to allow method chaining.
   * <br>
   * Possible object is <code>User</code> for type
   * <code>T</code>.
   */
  public final UserListJson roles(final String value) {
    this.roles = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roles
  /**
   * Returns the collection of role that the user belongs to through direct
   * membership.
   *
   * @return the comma-space separated collection of roles for the User.
   * <br>
   * Possible object is {@link String}.
   */
  public final String roles() {
    return this.roles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   behoerde
  /**
   ** Sets the behoerde of the <code>User</code>.
   **
   ** @param  value              the behoerde of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final UserListJson behoerde(final String value) {
    this.behoerde = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   behoerde
  /**
   ** Returns the behoerde of the <code>User</code>.
   **
   ** @return                    the behoerde of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String behoerde() {
    return this.behoerde;
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
    int result = 0;
    result = PRIME * result * (this.uid                    != null ? this.uid.hashCode()                    : 0);
    result = PRIME * result * (this.name                   != null ? this.name.hashCode()                   : 0);
    result = PRIME * result + (this.email                  != null ? this.email.hashCode()                  : 0);
    result = PRIME * result + (this.lastName               != null ? this.lastName.hashCode()               : 0);
    result = PRIME * result + (this.firstName              != null ? this.firstName.hashCode()              : 0);
    result = PRIME * result + (this.behoerde               != null ? this.behoerde.hashCode()               : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>User</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>User</code>s may be
   ** different even though they contain the same set of names with the same
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
  public final boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;
    final UserListJson that = (UserListJson)other;
    if (this.uid                    != null ? !this.uid.                   equals(that.uid)                    : that.uid                    != null)
      return false;
    if (this.name                   != null ? !this.name.                  equals(that.name)                   : that.name                   != null)
      return false;
    if (this.email                  != null ? !this.email.                 equals(that.email)                  : that.email                  != null)
      return false;
    if (this.lastName               != null ? !this.lastName.              equals(that.lastName)               : that.lastName               != null)
      return false;
    if (this.firstName              != null ? !this.firstName.             equals(that.firstName)              : that.firstName              != null)
      return false;
    if (this.behoerde               != null ? !this.behoerde.              equals(that.behoerde)               : that.behoerde               != null)
      return false;
    return true;
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
    final String method = "UserJsonList#toString()";
    StringBuilder sb = new StringBuilder(method).append(" UserJsonList object dump:");
    sb.append(" uid=").append(uid);
//    if (uid                    == null || uid.length                    == 0) { sb.append("null"); } else { sb.append(uid);                    }
    sb.append(" roles=").append(roles);
//    if (roles                  == null || roles.size()                  == 0) { sb.append("null"); } else { sb.append(roles);          }
    sb.append(" name=").append(name);
//    if (name                   == null || name.length                   == 0) { sb.append("null"); } else { sb.append(name);                   }
    sb.append(" email=").append(email);
//    if (email                  == null || email.length                  == 0) { sb.append("null"); } else { sb.append(email);                  }
    sb.append(" lastName=").append(lastName);
//    if (lastName               == null || lastName.length               == 0) { sb.append("null"); } else { sb.append(lastName);               }
    sb.append(" firstName=").append(firstName);
//    if (firstName              == null || firstName.length              == 0) { sb.append("null"); } else { sb.append(firstName);              }
    sb.append(" behoerde=").append(behoerde);
//    if (behoerde               == null || behoerde.length               == 0) { sb.append("null"); } else { sb.append(behoerde);               }
    return(sb.toString());
  }
}