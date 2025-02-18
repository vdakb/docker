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
    Subsystem   :   Apache Archiva Connector

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class User implements Resource<User> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("username")
  @Attribute(Attribute.IDENTIFIER)
  private String id;

  @Attribute
  @JsonProperty
  private String email;

  @Attribute
  @JsonProperty
  private String fullName;

  @Attribute
  @JsonProperty("userManagerId")
  private String userManager;

  @Attribute
  @JsonProperty
  private String password;

  @Attribute
  @JsonProperty("confirmPassword")
  private String confirmation;

  @Attribute
  @JsonProperty
  private Boolean locked;

  @Attribute
  @JsonProperty
  private Boolean permanent;

  @Attribute
  @JsonProperty
  private Boolean readOnly;

  @Attribute
  @JsonProperty
  private Boolean validated;

  @Attribute
  @JsonProperty
  private Boolean passwordChangeRequired;

  @Attribute
  @JsonProperty("assignedRoles")
  private List<String> role;

  @JsonProperty("timestampLastLogin")
  private String lastLogin;

  @JsonProperty("timestampAccountCreation")
  private String accountCreation;

  @JsonProperty("timestampLastPasswordChange")
  private String lastPasswordChange;

  @JsonProperty
  private String previousPassword;

  @JsonProperty
  private String validationToken;

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
  // Method:   email
  /**
   ** Sets the email address of the <code>User</code>.
   **
   ** @param  value              the email address of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User email(final String value) {
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
  public final String email() {
    return this.email;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fullName
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public User fullName(final String value) {
    this.fullName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fullName
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
  public String fullName() {
    return this.fullName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userManager
  /**
   ** Sets the userManager of the <code>User</code>.
   **
   ** @param  value              the userManager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User userManager(final String value) {
    this.userManager = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userManager
  /**
   ** Returns the userManager of the <code>User</code>.
   **
   ** @return                    the userManager of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String userManager() {
    return this.userManager;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Sets the password of the <code>User</code>.
   **
   ** @param  value              the password.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User password(final String value) {
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
  public String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmation
  /**
   ** Sets the confirmation of the <code>User</code>.
   **
   ** @param  value              the confirmation.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User confirmation(final String value) {
    this.confirmation = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmation
  /**
   ** Returns the confirmation of the <code>User</code>.
   **
   ** @return                    the confirmation of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String confirmation() {
    return this.confirmation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locked
  /**
   ** Sets the locked status of the <code>User</code>.
   **
   ** @param  value              the locked status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User locked(final Boolean value) {
    this.locked = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locked
  /**
   ** Returns the locked status of the <code>User</code>.
   **
   ** @return                    the locked status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean locked() {
    return this.locked;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permanent
  /**
   ** Sets the permanent status of the <code>User</code>.
   **
   ** @param  value              the permanent status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User permanent(final Boolean value) {
    this.permanent = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permanent
  /**
   ** Returns the permanent status of the <code>User</code>.
   **
   ** @return                    the permanent status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean permanent() {
    return this.permanent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readOnly
  /**
   ** Sets the readOnly status of the <code>User</code>.
   **
   ** @param  value              the readOnly status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User readOnly(final Boolean value) {
    this.readOnly = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readOnly
  /**
   ** Returns the readOnly status of the <code>User</code>.
   **
   ** @return                    the readOnly status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean readOnly() {
    return this.readOnly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validated
  /**
   ** Sets the validated status of the <code>User</code>.
   **
   ** @param  value              the validated status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User validated(final Boolean value) {
    this.validated = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validated
  /**
   ** Returns the validated status of the <code>User</code>.
   **
   ** @return                    the validated status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean validated() {
    return this.validated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordChangeRequired
  /**
   ** Sets the passwordChangeRequired status of the <code>User</code>.
   **
   ** @param  value              the passwordChangeRequired status of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User passwordChangeRequired(final Boolean value) {
    this.passwordChangeRequired = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordChangeRequired
  /**
   ** Returns the passwordChangeRequired status of the <code>User</code>.
   **
   ** @return                    the passwordChangeRequired status of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean passwordChangeRequired() {
    return this.passwordChangeRequired;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastLogin
  /**
   ** Sets the timestamp the user have last logged in.
   **
   ** @param  value              the timestamp the user have last logged in.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public User lastLogin(final String value) {
    this.lastLogin = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastLogin
  /**
   ** Returns the timestamp the user have last logged in.
   **
   ** @return                    the timestamp the user have last logged in.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String lastLogin() {
    return this.lastLogin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreation
  /**
   ** Sets the timestamp the user was created.
   **
   ** @param  value              the timestamp the user was created.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public User accountCreation(final String value) {
    this.accountCreation = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
   // Method:   accountCreation
   /**
   ** Returns the timestamp the user changed the password last time.
   **
   ** @return                    the timestamp the user was created.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String accountCreation() {
    return this.accountCreation;
  }

  //////////////////////////////////////////////////////////////////////////////
   // Method:   lastPasswordChange
   /**
   ** Sets the timestamp the user changed the password last time.
   **
   ** @param  value              the timestamp the user changed the password
   **                            last time.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public User lastPasswordChange(final String value) {
    this.lastPasswordChange = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
   // Method:   lastPasswordChange
   /**
   ** Returns the timestamp the user changed the password last time.
   **
   ** @return                    the timestamp the user changed the password
   **                            last time.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String lastPasswordChange() {
    return this.lastPasswordChange;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   previousPassword
  /**
   ** Sets the previous password the user.
   **
   ** @param  value              the previous password the user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public User previousPassword(final String value) {
    this.previousPassword = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
   // Method:   previousPassword
   /**
   ** Returns the previous password the user.
   **
   ** @return                    the previous password the user.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String previousPassword() {
    return this.previousPassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationToken
  /**
   ** Sets the token the user for validation purpose.
   **
   ** @param  value              the token the user for validation purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Name</code> to allow method chaining.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public User validationToken(final String value) {
    this.validationToken = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
   // Method:   validationToken
   /**
   ** Returns the token the user for validation purpose.
   **
   ** @return                    the token the user for validation purpose.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String validationToken() {
    return this.validationToken;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Sets the role associated with the <code>User</code>.
   **
   ** @param  value              the role associated with the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>User</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User role(final List<String> value) {
    this.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the role associated with the <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the role associated with the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> role() {
    return this.role;
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
    int result = this.id != null ? this.id.hashCode() : 0;
    result = 31 * result + (this.email                  != null ? this.email.hashCode()                  : 0);
    result = 31 * result + (this.fullName               != null ? this.fullName.hashCode()               : 0);
    result = 31 * result + (this.password               != null ? this.password.hashCode()               : 0);
    result = 31 * result + (this.confirmation           != null ? this.confirmation.hashCode()           : 0);
    result = 31 * result + (this.locked                 != null ? this.locked.hashCode()                 : 0);
    result = 31 * result + (this.permanent              != null ? this.permanent.hashCode()              : 0);
    result = 31 * result + (this.readOnly               != null ? this.readOnly.hashCode()               : 0);
    result = 31 * result + (this.validated              != null ? this.validated.hashCode()              : 0);
    result = 31 * result + (this.lastLogin              != null ? this.lastLogin.hashCode()              : 0);
    result = 31 * result + (this.accountCreation        != null ? this.accountCreation.hashCode()        : 0);
    result = 31 * result + (this.validationToken        != null ? this.validationToken.hashCode()        : 0);
    result = 31 * result + (this.lastPasswordChange     != null ? this.lastPasswordChange.hashCode()     : 0);
    result = 31 * result + (this.passwordChangeRequired != null ? this.passwordChangeRequired.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>UserResult</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>UserResult</code>s may be different even though they contain the
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

    final User that = (User)other;
    if (this.id != null ? !this.id.equals(that.id) : that.id != null)
      return false;

    if (this.email != null ? !this.email.equals(that.email) : that.email != null)
      return false;

    if (this.fullName != null ? !this.fullName.equals(that.fullName) : that.fullName != null)
      return false;

    if (this.password != null ? !this.password.equals(that.password) : that.password != null)
      return false;

    if (this.confirmation != null ? !this.confirmation.equals(that.confirmation) : that.confirmation != null)
      return false;

    if (this.locked != null ? !this.locked.equals(that.locked) : that.locked != null)
      return false;

    if (this.permanent != null ? !this.permanent.equals(that.permanent) : that.permanent != null)
      return false;

    if (this.validated != null ? !this.validated.equals(that.validated) : that.validated != null)
      return false;

    if (this.lastLogin != null ? !this.lastLogin.equals(that.lastLogin) : that.lastLogin != null)
      return false;

    if (this.accountCreation != null ? !this.accountCreation.equals(that.accountCreation) : that.accountCreation != null)
      return false;

    if (this.validationToken != null ? !this.validationToken.equals(that.validationToken) : that.validationToken != null)
      return false;

    if (this.lastPasswordChange != null ? !this.lastPasswordChange.equals(that.lastPasswordChange) : that.lastPasswordChange != null)
      return false;

    return !(this.passwordChangeRequired != null ? !this.passwordChangeRequired.equals(that.passwordChangeRequired) : that.passwordChangeRequired != null);
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
    builder.append("{\"username\":\"").append(this.id).append("\"");
    builder.append(",\"email\":\"").append(this.email).append("\"");
    builder.append(",\"fullName\":\"").append(this.fullName).append("\"");
    builder.append("}");
    return builder.toString();
  }
}