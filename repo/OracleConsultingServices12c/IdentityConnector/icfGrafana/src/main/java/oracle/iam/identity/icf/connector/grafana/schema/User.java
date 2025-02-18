/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.schema;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Returned;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;

////////////////////////////////////////////////////////////////////////////////
// final class User
// ~~~~~ ~~~~~ ~~~~
/**
 ** The Grafana REST user entity representation.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
@JsonIgnoreProperties({"theme", "isExternal", "isExternallySynced", "isGrafanaAdminExternallySynced", "authLabels", "lastSeenAt", "lastSeenAtAge", "createdAt", "updatedAt"})
public final class User extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The unique identifier for the <code>User</code> typically used as a
   ** label for the <code>User</code> by Service Provider.
   */
  @JsonProperty(LOGIN)
  @Attribute(value=Attribute.UNIQUE, required=true, mutability=Mutability.MUTABLE, returned=Returned.ALWAYS)
  protected String           login;
  /**
   ** The activation status of the <code>User</code>.
   ** <p>
   ** Per-design its defaults to <code>false</code>.
   */
  @Attribute
  @JsonProperty(DISABLED)
  private Boolean            disabled = Boolean.FALSE;
  /**
   ** The credential for the User typically used by the <code>User</code> to
   ** directly authenticated by the Service Provider.
   */
  @JsonProperty(PASSWORD)
  @Attribute(returned=Returned.NEVER)
  private String             password;
  /**
   ** The public identifier for the <code>User</code> typically used identify
   ** the <code>User</code>.
   */
  @Attribute
  @JsonProperty(NAME)
  private String             displayName;
  /**
   ** The E-mail addresse for the <code>User</code>.
   */
  @Attribute
  @JsonProperty(EMAIL)
  private String             email;
  /**
   ** The home organization of the <code>User</code>.
   */
  @Attribute
  @JsonProperty(ORGANIZATION)
  private Long               home;
  /**
   ** The administration status of the <code>User</code>.
   ** <p>
   ** Per-design its defaults to <code>false</code>.
   */
  @Attribute
  @JsonProperty(ADMIN)
  @JsonAlias(ADMIN_ALIAS)
  private Boolean            admin   = Boolean.FALSE;
  /**
   ** The relative path to the avatar of the <code>User</code>.
   */
  @Attribute
  @JsonProperty(AVATAR)
  private String             avatar;
  /**
   ** The roles granted to the <code>User</code>.
   */
  @Attribute(multiValueClass=RoleMember.class)
  private List<RoleMember>   role;
  /**
   ** The teams assigned to the <code>User</code>.
   */
  @Attribute(multiValueClass=Team.class)
  private List<Team>         team;
  /**
   ** The organizations assigned to the <code>User</code>.
   */
  @Attribute(multiValueClass=Organization.class)
  private List<Organization> organization;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Permission
  // ~~~~~ ~~~~~ ~~~~~~~~~~
  /**
   ** The Grafana administrator permission REST entity representation.
   */
  public static final class Permission {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The rafana administrator permission of the <code>User</code>.
     ** <p>
     ** Per-design its defaults to <code>false</code>.
     */
    @JsonProperty(ADMIN)
    private Boolean value = Boolean.FALSE;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Permission</code> REST representation with the
     ** specified value.
     **
     ** @param  value            the status of the <code>Permission</code>.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     */
    private Permission(final Boolean value) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Permission</code>.
     **
     ** @return                    the value of the <code>Permission</code>.
     **                            <br>
     **                            Possible object is {@link Boolean}.
     */
    public final Boolean value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Credential
  // ~~~~~ ~~~~~ ~~~~~~~~~~
  /**
   ** The Grafana credential REST entity representation.
   */
  public static final class Credential {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The credential for a User typically used to be directly authenticated by
     ** the Service Provider.
     */
    @JsonProperty("password")
    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Credential</code> REST representation with the
     ** specified value.
     **
     ** @param  value            the password of the <code>Credential</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Credential(final String value) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Credential</code>.
     **
     ** @return                    the value of the <code>Credential</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>User</code> REST representation that allows use
   ** as a JavaBean.
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
  // Method:   login
  /**
   ** Sets the unique identifier of the <code>User</code>.
   **
   ** @param  value              the unique identifier of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   **
   ** @throws NullPointerException if the unique identifier <code>value</code>
   **                              is <code>null</code>.
   */
  public final User login(final String value) {
    this.login = Objects.requireNonNull(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login
  /**
   ** Returns the unique identifier of the <code>User</code>.
   **
   ** @return                    the unique identifier of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String login() {
    return this.login;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
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
  public final User disabled(final Boolean value) {
    this.disabled = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
  /**
   ** Returns the activation status of the <code>User</code>.
   **
   ** @return                    the activation status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean disabled() {
    return this.disabled == null ? false : this.disabled;
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
   ** Sets the display name of the <code>User</code>.
   **
   ** @param  value              the display name of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the display name of the <code>Role</code>.
   **
   ** @return                    the display name of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String displayName() {
    return this.displayName;
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
  // Method:   home
  /**
   ** Sets the home organization of the <code>User</code>.
   **
   ** @param  value              the home organization of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User home(final Long value) {
    this.home = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   home
  /**
   ** Returns the home organization of the <code>User</code>.
   **
   ** @return                    the home organization of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long home() {
    return this.home;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Sets the administrator status of the <code>User</code>.
   **
   ** @param  value              the administrator status of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User administrator(final Boolean value) {
    this.admin = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Returns the administrator status of the <code>User</code>.
   **
   ** @return                    the administrator status of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean administrator() {
    return this.admin == null ? false : this.admin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Sets the relative path to the avatar of the <code>User</code>.
   **
   ** @param  value              the relative path to the avatar of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User avatar(final String value) {
    this.avatar = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avatar
  /**
   ** Returns the relative path to the avatar of the <code>User</code>.
   **
   ** @return                    the relative path to the avatar of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String avatar() {
    return this.avatar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Sets the membership in {@link Role}s for the user.
   **
   ** @param  value              the membership in {@link Role}s for the user.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link RoleMember}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User role(final List<RoleMember> value) {
    this.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the membership in {@link Role}s for the user.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the membership in {@link Role}s for the user.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link RoleMember}.
   */
  public final List<RoleMember> role() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Sets the membership in {@link Team}s for the user.
   **
   ** @param  value              the membership in {@link Team}s for the user.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Team}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User team(final List<Team> value) {
    this.team = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   team
  /**
   ** Returns the membership in {@link Team}s for the user.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the membership in {@link Team}s for the user.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Team}.
   */
  public final List<Team> team() {
    return this.team;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Sets the membership in {@link Organization}s for the user.
   **
   ** @param  value              the membership in {@link Organization}s for the
   **                            user.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Organization}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User organization(final List<Organization> value) {
    this.organization = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Returns the membership in {@link Organization}s for the user.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the membership in {@link Organization}s for the
   **                            user.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Organization}.
   */
  public final List<Organization> organization() {
    return this.organization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Resource)
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
    return Objects.hash(this.id, this.login);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>User</code> resource.
   **
   ** @param  loginName          the unique identifier of the <code>User</code>
   **                            (usually the value of the <code>NAME</code> for
   **                            the account in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> populated with
   **                            the given <code>loginName</code>.
   **                            <br>
   **                            Possible object is <code>User</code>.
   **
   ** @throws NullPointerException if the public identifier
   **                              <code>loginName</code> is <code>null</code>.
   */
  public static User build(final String loginName) {
    return new User().login(loginName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Factory method to create a minimal <code>Permission</code> resource.
   **
   ** @param  value              the status of the <code>Permission</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Permission</code> populated with
   **                            the given <code>value</code>.
   **                            <br>
   **                            Possible object is <code>Permission</code>.
   **
   ** @throws NullPointerException if <code>password</code> is
   **                              <code>null</code>.
   */
  public static Permission permission(final Boolean value) {
    return new Permission(Objects.requireNonNull(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Factory method to create a minimal <code>Credential</code> resource.
   **
   ** @param  value              the password of the <code>Credential</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Credential</code> populated with
   **                            the given <code>value</code>.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   **
   ** @throws NullPointerException if <code>password</code> is
   **                              <code>null</code>.
   */
  public static Credential credential(final String value) {
    return new Credential(Objects.requireNonNull(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entities</code> are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entities</code> may be different even though they contain the same
   ** set of properties with the same values, but in a different order.
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

    final User that = (User)other;
    return Objects.equals(this.id, that.id) && Objects.equals(this.login, that.login);
  }
}