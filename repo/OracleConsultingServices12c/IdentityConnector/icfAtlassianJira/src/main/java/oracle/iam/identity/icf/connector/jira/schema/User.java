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

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 ** The base REST result user entity.
 ** <br>
 ** This object contains all of the attributes required of jira objects.
 ** <p>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class User extends Adressable<User>{

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

  @JsonProperty
  @Attribute(value=Attribute.PASSWORD, sensitive=true)
  private String             password;

  @Attribute
  @JsonProperty
  private String             displayName;

  @Attribute
  @JsonProperty("emailAddress")
  private String             email;

  @Attribute
  @JsonProperty
  private String             locale;

  @JsonProperty
  @Attribute
  private String             timeZone;

  @JsonProperty("avatarUrls")
  private Avatar             avatar;

  @JsonProperty("roles")
  @Attribute(multiValueClass=Group.class)
  private List<Group>        group;

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
  public User() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the identifier of the <code>User</code>.
   **
   ** @param  value              the identifier of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the <code>User</code>.
   **
   ** @return                    the identifier of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Sets the activation status of the <code>User</code>.
   **
   ** @param  value              the activation status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User active(final Boolean value) {
    this.active = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Returns the activation status of the <code>User</code>.
   **
   ** @return                    the activation status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean active() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleted
  /**
   ** Sets the deletion status of the <code>User</code>.
   **
   ** @param  value              the deletion status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User deleted(final Boolean value) {
    this.deleted = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleted
  /**
   ** Returns the deletion status of the <code>User</code>.
   **
   ** @return                    the deletion status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean deleted() {
    return this.deleted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Sets the unique name of the <code>User</code>.
   **
   ** @param  value              the unique name of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User userName(final String value) {
    this.userName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Returns the unique name of the <code>User</code>.
   **
   ** @return                    the unique name of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userName() {
    return this.userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Sets the password of the <code>User</code>.
   **
   ** @param  value              the password of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User password(final String value) {
    this.password = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the password of the <code>User</code>.
   **
   ** @return                    the password of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String password() {
    return this.password;
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User displayName(final String value) {
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
  // Method:   email
  /**
   ** Sets the email address of the <code>User</code>.
   **
   ** @param  value              the email address
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User email(final String value) {
    this.email = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Returns the email address of the <code>User</code>.
   **
   ** @return                    the email address of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String email() {
    return this.email;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Sets the locale of the <code>User</code>.
   **
   ** @param  value              the locale of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User locale(final String value) {
    this.locale = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns the locale of the <code>User</code>.
   **
   ** @return                    the locale of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String locale() {
    return this.locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Sets the time zone of the <code>User</code>.
   **
   ** @param  value              the time zone of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User timeZone(final String value) {
    this.timeZone = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Returns the time zone of the <code>User</code>.
   **
   ** @return                    the time zone of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String timeZone() {
    return this.timeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Sets the {@link Avatar}s that the <code>User</code> is associated with.
   **
   ** @param  value              the {@link Avatar}s that the <code>User</code>
   **                            is associated with.
   **                            <br>
   **                            Allowed object is {@link Avatar}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User avatar(final Avatar value) {
    this.avatar = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Returns the {@link Avatar}s that the <code>User</code> is associated
   ** with.
   **
   ** @return                    the {@link Avatar}s that the <code>User</code>
   **                            is associated with.
   **                            <br>
   **                            Possible object is {@link Avatar}.
   */
  public final Avatar avatar() {
    return this.avatar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Sets the {@link List} of {@link Group}s the <code>User</code> belongs to.
   **
   ** @param  value              the {@link List} of {@link Group}s the
   **                            <code>User</code> belongs to.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Group}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User group(final List<Group> value) {
    this.group = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Returns the {@link List} of {@link Group}s the <code>User</code> belongs
   ** to.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the {@link List} of {@link Group}s the
   **                            <code>User</code> belongs to.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Group}.
   */
  public final List<Group> group() {
    return this.group;
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
    result = 31 * result + (this.self         != null ? this.self.hashCode()         : 0);
    result = 31 * result + (this.active       != null ? this.active.hashCode()       : 0);
    result = 31 * result + (this.deleted      != null ? this.deleted.hashCode()      : 0);
    result = 31 * result + (this.userName     != null ? this.userName.hashCode()     : 0);
    result = 31 * result + (this.displayName  != null ? this.displayName.hashCode()  : 0);
    result = 31 * result + (this.email        != null ? this.email.hashCode()        : 0);
    result = 31 * result + (this.displayName  != null ? this.displayName.hashCode()  : 0);
    result = 31 * result + (this.locale       != null ? this.locale.hashCode()       : 0);
    result = 31 * result + (this.timeZone     != null ? this.timeZone.hashCode()     : 0);
    result = 31 * result + (this.avatar       != null ? this.avatar.hashCode()       : 0);
    result = 31 * result + (this.group        != null ? this.group.hashCode()        : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Entity)
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
    builder.append("{\"id\":\"").append(this.id).append("\"");
    builder.append(",\"active\":\"").append(this.active).append("\"");
    builder.append(",\"deleted\":\"").append(this.deleted).append("\"");
    builder.append(",\"userName\":\"").append(this.userName).append("\"");
    builder.append(",\"displayName\":\"").append(this.displayName).append("\"");
    builder.append(",\"email\":\"").append(this.email).append("\"");
    builder.append(",\"locale\":\"").append(this.locale).append("\"");
    builder.append(",\"timeZone\":\"").append(this.timeZone).append("\"");
    builder.append(",\"avatar\":[").append(this.avatar).append("]");
    builder.append(",\"role\":[").append(this.group).append("]");
    builder.append("}");
    return builder.toString();
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
   ** Two <code>User</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>User</code>s may be
   ** different even though they contain the same set of names with the same
   ** values, but in a different
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

    final User that = (User)other;
    if (this.id != null ? !this.id.equals(that.id) : that.id != null)
      return false;

    if (this.active != null ? !this.active.equals(that.active) : that.active != null)
      return false;

    if (this.deleted != null ? !this.deleted.equals(that.deleted) : that.deleted != null)
      return false;

    if (this.userName != null ? !this.userName.equals(that.userName) : that.userName != null)
      return false;

    if (this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName != null)
      return false;

    if (this.email != null ? !this.email.equals(that.email) : that.email != null)
      return false;

    if (this.locale != null ? !this.locale.equals(that.locale) : that.locale != null)
      return false;

    if (this.timeZone != null ? !this.timeZone.equals(that.timeZone) : that.timeZone != null)
      return false;

    return !(this.group != null ? !this.group.equals(that.group) : that.group != null);
  }
}