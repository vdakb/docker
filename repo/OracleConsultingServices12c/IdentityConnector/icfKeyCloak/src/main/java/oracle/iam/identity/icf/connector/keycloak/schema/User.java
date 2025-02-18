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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;
import oracle.iam.identity.icf.schema.Schema;

import java.util.*;

////////////////////////////////////////////////////////////////////////////////
// final class User
// ~~~~~ ~~~~~ ~~~~
/**
 ** The base REST user entity representation.
 ** <br>
 ** This object contains all of the attributes required of Keycloak objects.
 ** <p>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
@JsonIgnoreProperties({"credentials"})
public final class  User extends Structural<User> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The link to the user self reference.
   */
  @Attribute
  @JsonProperty
  private String               self;
  /**
   ** The origin for the user.
   */
  @Attribute
  @JsonProperty
  private String               origin;
  /**
   ** Unique identifier for the User typically used by the user to directly
   ** authenticate to the service provider.
   */
  @JsonProperty
  @Attribute(value=Attribute.UNIQUE, required=true, mutability=Mutability.IMMUTABLE)
  private String               username;
  /**
   ** A Boolean value indicating the User's administrative status.
   ** <p>
   ** Per-design its defaults to <code>true</code>.
   */
  @JsonProperty
  @Attribute(value=Attribute.STATUS, required=true)
  private Boolean              enabled  = Boolean.TRUE;
  /**
   ** New users are not automatically verified by default.
   ** Unverified users can be created by specifying verified: <code>false</code>.
   ** Becomes <code>true</code> when the user verifies their email address.
   ** <p>
   ** Per-design its defaults to <code>true</code>.
   */
  @Attribute(required=true)
  @JsonProperty("emailVerified")
  private Boolean              verified;
  /**
   ** A Long value indicating the User's created timestamp.
   */
  @Attribute(operational=true)
  @JsonProperty("createdTimestamp")
  private Long                 createdOn;
  /**
   ** The first name for the user.
   */
  @Attribute
  @JsonProperty
  private String               firstName;
  /**
   ** The last name for the user.
   */
  @Attribute
  @JsonProperty
  private String               lastName;
  /**
   ** E-mail addresse for the user.
   ** The value SHOULD be canonicalized by the Service Provider, e.g.,
   ** bjensen@example.com instead of bjensen@example.com.
   */
  @Attribute
  @JsonProperty
  private String               email;
  /**
   ** A Integer value indicating the User's created timestamp.
   */
  @Attribute
  @JsonProperty
  private Integer              notBefore;
  /**
   ** New users are not automatically verified by default.
   ** Unverified users can be created by specifying verified: <code>false</code>.
   ** Becomes <code>true</code> when the user verifies their email address.
   */
  @Attribute
  @JsonProperty("totp")
  private Boolean              oneTimePassword;
  /**
   ** The type of credentials disable for the user.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** <code>disabled</code> is a complex attribute, so it is represented by the
   ** {@link CredentialType} class.
   ** The attribute <code>disabled</code> cannot be set or modified
   ** through the user profile.
   */
  @JsonProperty("disableableCredentialTypes")
  @Attribute(multiValueClass=CredentialType.class)
  private List<CredentialType> disabled = new ArrayList<CredentialType>();
  /**
   ** Required Actions are tasks that a user must finish before they are allowed
   ** to log in. A user must provide their credentials before required actions
   ** are executed. Once a required action is completed, the user will not have
   ** to perform the action again.
   ** <ul>
   **   <li><b>Verify Email</b>
   **       <br>
   **       When set, a user must verify that they have a valid email account.
   **       An email will be sent to the user with a link they have to click.
   **       Once this workflow is successfully completed, they will be allowed
   **       to log in.
   **   <li><b>Update Profile</b> requires user to enter in new personal
   **       information.
   **   <li><b>Update Password</b>
   **       <br>
   **       When set, a user must change their password.
   **   <li><b>Configure OTP</b>
   **       <br>
   **       When set, a user must configure a one-time password generator on
   **       their mobile device using either the <i>Free OTP</i> or
   **       <i>Google Authenticator</i> application.
   ** </ul>
   */
  @JsonProperty("requiredActions")
  @Attribute(multiValueClass=ActionType.class)
  private List<ActionType>     action = new ArrayList<>();
  /**
   ** The collection of groups.
   */
  @Attribute(multiValueClass=Group.class)
  private List<Group>          group;
  /**
   ** The collection of roles the user have in the realm.
   */
  @Attribute(multiValueClass=Role.class)
  private List<Role>           role;
  /**
   ** The collection of roles the user have in clients.
   */
  @Attribute(multiValueClass=Client.class)
  private List<Client>  client;

  /**
   ** The collection of access permissions.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The attribute <code>access</code> cannot be set or modified
   ** through the user profile.
   ** The values of the attribute <code>access</code>
   ** are evaluated by the assigned roles and realm settings.
   */
  @JsonProperty("access")
  @Attribute(multiValueClass=Permission.class)
  private Map<Permission, Boolean> access = new LinkedHashMap<>();

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
  // Method:   self
  /**
   ** Sets the link to the reference of the <code>User</code>.
   **
   ** @param  value              the link to the reference of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User self(final String value) {
    this.self = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   self
  /**
   ** Returns the link to the reference of the <code>User</code>.
   **
   ** @return                    the link to the reference of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String self() {
    return this.self;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Sets the origin reference of the <code>User</code>.
   **
   ** @param  value              the origin reference of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User origin(final String value) {
    this.origin = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Returns the origin reference of the <code>User</code>.
   **
   ** @return                    the origin reference of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String origin() {
    return this.origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdOn
  /**
   ** Returns the created timestamp of the <code>User</code>.
   **
   ** @return                    the created timestamp of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long createdOn() {
    return this.createdOn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
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
  public final User username(final String value) {
    this.username = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Returns the unique name of the <code>User</code>.
   **
   ** @return                    the unique name of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String username() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
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
  public final User enabled(final Boolean value) {
    this.enabled = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
  /**
   ** Returns the activation status of the <code>User</code>.
   **
   ** @return                    the activation status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean enabled() {
    return this.enabled == null ? false : this.enabled;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oneTimePassword
  /**
   ** Sets the One Time Password (OTP) status of the <code>User</code>.
   **
   ** @param  value              the One Time Password (OTP) status of the
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User oneTimePassword(final Boolean value) {
    this.oneTimePassword = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oneTimePassword
  /**
   ** Returns the One Time Password (OTP) status of the <code>User</code>.
   **
   ** @return                    the One Time Password (OTP) status of the
   **                            <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean oneTimePassword() {
    return this.oneTimePassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verified
  /**
   ** Sets the value indicating the <code>User</code>'s e-Mail verification
   ** status.
   **
   ** @param  value              the value indicating the <code>User</code>'s
   **                            e-Mail verification status.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User verified(final Boolean value) {
    this.verified = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verified
  /**
   ** Returns the value indicating the <code>User</code>'s e-Mail verification
   ** status.
   **
   ** @return                    the value indicating the <code>User</code>'s
   **                            e-Mail verification status.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean verified() {
    return this.verified == null ? false : this.verified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Sets the first name of the <code>User</code>.
   **
   ** @param  value              the first name of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User firstName(final String value) {
    this.firstName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Returns the first name of the <code>User</code>.
   **
   ** @return                    the first name of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Sets the last name of the <code>User</code>.
   **
   ** @param  value              the last name of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User lastName(final String value) {
    this.lastName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Returns the last name of the <code>User</code>.
   **
   ** @return                    the last name of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String lastName() {
    return this.lastName;
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
  // Method:   notBefore
  /**
   ** Sets the not before status of the <code>User</code>.
   **
   ** @param  value              the not before status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User notBefore(final Integer value) {
    this.notBefore = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notBefore
  /**
   ** Returns the not before status of the <code>User</code>.
   **
   ** @return                    the not before status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer notBefore() {
    return this.notBefore;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the required action of the <code>User</code>.
   **
   ** @param  value              the required action of the <code>User</code>.
   **                            <br>
   **                            Allowed object is array of {@link ActionType}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User action(final ActionType... value) {
    return action(Arrays.asList(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the required action of the <code>User</code>.
   **
   ** @param  value              the required action of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link ActionType}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User action(final List<ActionType> value) {
    this.action = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the required action of the <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the required action of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link ActionType}.
   */
  public final List<ActionType> action() {
    return this.action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Sets the access permission of the <code>User</code>.
   **
   ** @param  value              the access permission of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Map} which is a
   **                            {@link Map} where each element is of type
   **                            {@link String} for the key and {@link Boolean}
   **                            as the value.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User access(final Map<Permission, Boolean> value) {
    this.access = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Returns the access permission of the <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the access permission of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Map} which is a
   **                            {@link Map} where each element is of type
   **                            {@link String} for the key and {@link Boolean}
   **                            as the value.
   */
  public final Map<Permission, Boolean> access() {
    return this.access;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
  /**
   ** Sets the collection of {@link Credential} types disabled for to the
   ** <code>User</code>.
   **
   ** @param  value              the collection of {@link Credential} types
   **                            disabled for to the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link CredentialType}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User disabled(final List<CredentialType> value) {
    this.disabled = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
  /**
   ** Returns the collection of {@link Credential} types disabled for to the
   ** <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of {@link Credential} types
   **                            disabled for to the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link CredentialType}.
   */
  public final List<CredentialType> disabled() {
    return this.disabled;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Sets the collection of realm {@link Role}s belonging to the
   ** <code>User</code>.
   **
   ** @param  value              the collection of realm {@link Role}s belonging
   **                            to the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Role}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User role(final List<Role> value) {
    this.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the collection of realm {@link Role}s belonging to the
   ** <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of realm {@link Role}s belonging
   **                            to the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Role}.
   */
  public final List<Role> role() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Sets the collection of client {@link Role}s belonging to the
   ** <code>User</code>.
   **
   ** @param  value              the collection of client {@link Role}s
   **                            belonging to the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Client} as the value.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User client(final List<Client> value) {
    this.client = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Returns the collection of client {@link Role}s belonging to the
   ** <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of client {@link Role}s
   **                            belonging to the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Client} as the value.
   */
  public final List<Client> client() {
    return this.client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Sets the collection of {@link Group}s belonging to the
   ** <code>User</code>.
   **
   ** @param  value              the collection of {@link Group}s belonging to
   **                            the <code>User</code>.
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
   ** Returns the collection of {@link Group}s belonging to the
   ** <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of {@link Group}s belonging to
   **                            the <code>User</code>.
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
    return Objects.hash(
      this.id
    , this.self
    , this.origin
    , this.createdOn
    , this.username
    , this.enabled
    , this.oneTimePassword
    , this.verified
    , this.firstName
    , this.lastName
    , this.email
    , this.attribute
    , this.role
    , this.client
    , this.group);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>User</code> resource.
   **
   ** @param  username           the unique identifier of the <code>User</code>
   **                            (usually the value of the <code>NAME</code> for
   **                            the account in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> populated with
   **                            the given <code>username</code>.
   **                            <br>
   **                            Possible object is <code>User</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static User build(final String username) {
    return new User().username(username);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>User</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two givenUser<code>User</code>s may
   ** be different even though they contain theUsersame set of names with the
   ** same values, but in a different order.
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
    return Objects.equals(this.id,              that.id)
        && Objects.equals(this.self,            that.self)
        && Objects.equals(this.origin,          that.origin)
        && Objects.equals(this.createdOn,       that.createdOn)
        && Objects.equals(this.username,        that.username)
        && Objects.equals(this.enabled,         that.enabled)
        && Objects.equals(this.oneTimePassword, that.oneTimePassword)
        && Objects.equals(this.verified,        that.verified)
        && Objects.equals(this.firstName,       that.firstName)
        && Objects.equals(this.lastName,        that.lastName)
        && Objects.equals(this.email,           that.email)
        && Objects.equals(this.attribute,       that.attribute)
        && Objects.equals(this.role,            that.role)
        && Objects.equals(this.client,          that.client)
        && Objects.equals(this.group,           that.group)
    ;
  }
}