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
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.drupal.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import oracle.iam.identity.icf.connector.drupal.schema.UserJson.DateValue;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson.PathValue;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson.Role;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson.SimpleValue;
import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;

import java.util.Date;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 ** The <code>User</code> REST resource in Google Drupal.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class User extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ROLE               = "role";

  /** The OIM attribute name of the <code>uid</code> Drupal attribute. */
  public static final String UID                = "uid";

  /** The OIM attribute name of the <code>uuid</code> (internal ID) attribute. */
  public static final String UUID               = "uuid";

  /** The OIM attribute name of the <code>name</code> (as in "username") attribute. */
  public static final String NAME               = "name";

  /** The OIM attribute name of the <code>mail</code> Drupal attribute. */
  public static final String EMAIL              = "email";

  /** The OIM attribute name of the <code>lastName</code> Drupal attribute. */
  public static final String LASTNAME           = "lastName";

  /** The OIM attribute name of the <code>firstName</code> Drupal attribute. */
  public static final String FIRSTNAME          = "firstName";

  /** The OIM attribute name of the <code>status</code> Drupal attribute. */
  public static final String STATUS             = "status";

  /** The OIM attribute name of the <code>roles</code> Drupal attribute. */
  public static final String ROLES              = "roles";

  /** The OIM attribute name of the <code>langcode</code> Drupal attribute. */
  public static final String LANG               = "langCode";

  /** The OIM attribute name of the <code>default_langcode</code> Drupal attribute. */
  public static final String DEFAULT_LANG       = "defaultLangCode";

  /** The OIM attribute name of the <code>preferred_langcode</code> Drupal attribute. */
  public static final String PREFERRED_LANG     = "preferredLangCode";

  /** The OIM attribute name of the <code>preferred_admin_langcode</code> Drupal attribute. */
  public static final String PREFERRED_ADM_LANG = "preferredAdminLangCode";

  /** The OIM attribute name of the <code>timezone</code> Drupal attribute. */
  public static final String TIMEZONE           = "timeZone";

  /** The OIM attribute name of the <code>created</code> Drupal attribute. */
  public static final String CREATED            = "created";

  /** The OIM attribute name of the <code>changed</code> Drupal attribute. */
  public static final String CHANGED            = "changed";

  /** The OIM attribute name of the <code>access</code> Drupal attribute. */
  public static final String ACCESS             = "access";

  /** The OIM attribute name of the <code>login</code> Drupal attribute. */
  public static final String LOGIN              = "login";

  /** The OIM attribute name of the <code>init</code> Drupal attribute. */
  public static final String INIT               = "init";

  /** The OIM attribute name of the <code>apigee_edge_developer_id</code> Drupal attribute. */
  public static final String APIGEE_EDGE_DEV_ID = "apigeeEdgeDeveloperId";

  /** The OIM attribute name of the <code>customer_profiles</code> Drupal attribute. */
  public static final String CUST_PROFILES      = "customerProfiles";

  /** The OIM attribute name of the <code>path</code> Drupal attribute. */
  public static final String PATH               = "path";

  /** The OIM attribute name of the <code>commerce_remote_id</code> Drupal attribute. */
  public static final String COMMERCE_REMOTE_ID = "commerceRemoteId";

  /** The OIM attribute name of the <code>user_picture</code> Drupal attribute. */
  public static final String USER_PICTURE       = "userPicture";

  /** The OIM attribute name of the <code>field_behoerde</code> Drupal attribute. */
  public static final String FIELD_BEHOERDE     = "behoerde";

 
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

 
  @Attribute(value=Attribute.IDENTIFIER, mutability=Mutability.IMMUTABLE)
  private String         uid;

  private String         uuid;

  @Attribute(value=Attribute.UNIQUE, required=true)
  private String         name;

  @Attribute(required=true)
  private String         email;

  @Attribute
  private String         firstName;

  @Attribute
  private String         lastName;

  private List<String>   roles;

  /**
   ** The developer status: true or false (Boolean type).
   ** <br>
   ** Defaults to true if nothing else is specified.
   */
  @Attribute(Attribute.STATUS)
  private Boolean        status = true;
  
  @Attribute
  private String         langCode;

  @Attribute
  private Boolean        defaultLangCode;

  @Attribute
  private String         preferredLangCode;

  @Attribute
  private String         preferredAdminLangCode;

  @Attribute
  private String         timeZone;

  @Attribute
  private Date           created;

  @Attribute
  private Date           changed;

  @Attribute
  private Date           access;

  @Attribute
  private Date           login;

  @Attribute
  private String         init;

  @Attribute
  private String         apigeeEdgeDeveloperId;

  @Attribute
  private String         customerProfiles;

  @Attribute
  private String         path;

  @Attribute
  private String         commerceRemoteId;

  private String         userPicture;

  @Attribute
  private String         behoerde;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Role
  // ~~~~~ ~~~~
  /**
   ** Stores role membership a user belongs to, either through direct
   ** membership.
   ** <br>
   ** The values are meant to enable expression of common group or role based
   ** access control models, although no explicit authorization model is
   ** defined. It is intended that the semantics of group membership and any
   ** behavior or authorization granted as a result of membership are defined by
   ** the Service Provider.
   */
  public static class Role {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected String name;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Role</code> REST Resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Role() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the <code>Role</code>.
     **
     ** @param  value            the name of the <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MemberOf</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MemberOf</code>.
     */
    public final Role name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the <code>Role</code>.
     **
     ** @return                  the name of the <code>Role</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = super.hashCode();
      result = PRIME * result + (this.name   != null ? this.name.hashCode()   : 0);
      return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>MemberOf</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>MemberOf</code>s may be different even though they contain the same
     ** set of names with the same values, but in a different
     ** order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Role that = (Role)other;
      if (this.name != null ? !this.name.equals(that.name) : that.name != null)
        return false;

      return super.equals(other);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Roles
  // ~~~~~ ~~~~~
  /**
   ** Stores role membership a user belongs to, either through direct
   ** membership.
   ** <br>
   ** The values are meant to enable expression of common group or role based
   ** access control models, although no explicit authorization model is
   ** defined. It is intended that the semantics of group membership and any
   ** behavior or authorization granted as a result of membership are defined by
   ** the Service Provider.
   */
  public static class Roles {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty(ROLE)
    private List<Role>     role;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Roles</code> REST Resource that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Roles() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: role
    /**
     * Sets the list of role that the user belongs to, either through direct
     * membership.
     *
     * @param value the list of role names that the user belongs to.
     * <br>
     * Allowed object is {@link List} of {@link Role}.
     *
     * @return the <code>User</code> to allow method chaining.
     * <br>
     * Possible object is <code>User</code> for type
     * <code>T</code>.
     */
    public final Roles role(final List<Role> value) {
      this.role = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   role
    /**
     * Returns the list of roles that the user belongs to through direct
     * membership.
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JSON object.
     *
     * @return the {@link List} of {@link Role } objects for the User.
     * <br>
     * Possible object is {@link List} of {@link Role}.
     */
    public final List<Role> role() {
      return this.role;
    }

  }

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
    final String method = "User#constructor()";
  }
  public User(UserJson userJson) {
    // ensure inheritance
    super();
    final String method = "User#constructor(userJson)";

    this.uid                    = (userJson.uid()                    == null || userJson.uid().length                    == 0) ? null : userJson.uid()[0].value();
    this.uuid                   = (userJson.uuid()                   == null || userJson.uuid().length                   == 0) ? null : userJson.uuid()[0].value();
    this.name                   = (userJson.name()                   == null || userJson.name().length                   == 0) ? null : userJson.name()[0].value();
    this.email                  = (userJson.email()                  == null || userJson.email().length                  == 0) ? null : userJson.email()[0].value();
    this.lastName               = (userJson.lastName()               == null || userJson.lastName().length               == 0) ? null : userJson.lastName()[0].value();
    this.firstName              = (userJson.firstName()              == null || userJson.firstName().length              == 0) ? null : userJson.firstName()[0].value();
    this.langCode               = (userJson.langCode()               == null || userJson.langCode().length               == 0) ? null : userJson.langCode()[0].value();
    this.defaultLangCode        = (userJson.defaultLangCode()        == null || userJson.defaultLangCode().length        == 0) ? null : userJson.defaultLangCode()[0].value();
    this.preferredLangCode      = (userJson.preferredLangCode()      == null || userJson.preferredLangCode().length      == 0) ? null : userJson.preferredLangCode()[0].value();
    this.preferredAdminLangCode = (userJson.preferredAdminLangCode() == null || userJson.preferredAdminLangCode().length == 0) ? null : userJson.preferredAdminLangCode()[0].value();
    this.timeZone               = (userJson.timeZone()               == null || userJson.timeZone().length               == 0) ? null : userJson.timeZone()[0].value();
    this.status                 = (userJson.status()                 == null || userJson.status().length                 == 0) ? null : userJson.status()[0].value();
    try {
      this.created = (userJson.created() == null || userJson.created().length == 0) ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(userJson.created()[0].value());
      this.changed = (userJson.changed() == null || userJson.changed().length == 0) ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(userJson.changed()[0].value());
      this.access = (userJson.access()   == null || userJson.access().length  == 0) ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(userJson.access()[0].value());
      this.login = (userJson.login()     == null || userJson.login().length   == 0) ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(userJson.login()[0].value());
    } catch (ParseException e) {
      System.out.println(method + " Exception caught while parsing dates: " + e.getMessage());
    }
    this.init                   = (userJson.init()                   == null || userJson.init().length                   == 0) ? null : userJson.init()[0].value();
    this.apigeeEdgeDeveloperId  = (userJson.apigeeEdgeDeveloperId()  == null || userJson.apigeeEdgeDeveloperId().length  == 0) ? null : userJson.apigeeEdgeDeveloperId()[0].value();
    this.customerProfiles       = (userJson.customerProfiles()       == null || userJson.customerProfiles().length       == 0) ? null : userJson.customerProfiles()[0].value();
//    this.path                   = (userJson.path()                   == null || userJson.path().length                   == 0) ? null : userJson.path()[0].value();
    this.commerceRemoteId       = (userJson.commerceRemoteId()       == null || userJson.commerceRemoteId().length       == 0) ? null : userJson.commerceRemoteId()[0].value();
    this.userPicture            = (userJson.userPicture()            == null || userJson.userPicture().length            == 0) ? null : userJson.userPicture()[0].value();
    this.behoerde               = (userJson.behoerde()               == null || userJson.behoerde().length               == 0) ? null : userJson.behoerde()[0].value();
//    System.out.println(method + ": userJson.roleNames(): " + userJson.roleNames());
    this.roles                  = (userJson.roleNames()              == null || userJson.roleNames().size()              == 0) ? null : userJson.roleNames();
  }
  public User(UserListJson userListJson) {
    // ensure inheritance
    super();
    final String method = "User#constructor(userListJson)";

    this.uid                    = userListJson.uid();
    this.name                   = userListJson.name();
    this.email                  = userListJson.email();
    this.lastName               = userListJson.lastName();
    this.firstName              = userListJson.firstName();
    this.behoerde               = userListJson.behoerde();
    List<String> tmpList = new ArrayList<>();
    tmpList.add(userListJson.roles());
    this.roles                  = tmpList;
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
  public final User uid(final String value) {
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
  // Method:   uuid
  /**
   ** Sets the uuid of the <code>User</code>.
   **
   ** @param  value              the uuid of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User uuid(final String value) {
    this.uuid = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uuid
  /**
   ** Returns the uuid of the <code>User</code>.
   **
   ** @return                    the uuid of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String uuid() {
    return this.uuid;
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
  public final User name(final String value) {
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
  public final User email(final String value) {
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
  public final User lastName(final String value) {
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
  public final User firstName(final String value) {
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
  // Method:   name
  /**
   ** Sets the status of the <code>User</code>.
   **
   ** @param  value              the status of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User status(final Boolean value) {
    this.status = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of the <code>User</code>.
   **
   ** @return                    the status of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Boolean status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roles
  /**
   * Sets the collection of role that the user belongs to through direct
   * membership.
   *
   * @param value the list of role that the user belongs to.
   * <br>
   * Allowed object is {@link List} of {@link String}.
   *
   * @return the <code>User</code> to allow method chaining.
   * <br>
   * Possible object is <code>User</code> for type <code>T</code>.
   */
  public final User roles(final List<String> value) {
    this.roles = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roles
  /**
   * Returns the collection of role that the user belongs to through direct
   * membership.
   *
   * @return the collection of roles for the User.
   * <br>
   * Possible object is {@link List} of
   * {@link Role} .
   */
  public final List<String> roles() {
    return this.roles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   langCode
  /**
   ** Sets the langCode of the <code>User</code>.
   **
   ** @param  value              the langCode of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User langCode(final String value) {
    this.langCode = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   langCode
  /**
   ** Returns the langCode of the <code>User</code>.
   **
   ** @return                    the langCode of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String langCode() {
    return this.langCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultLangCode
  /**
   ** Sets the defaultLangCode of the <code>User</code>.
   **
   ** @param  value              the defaultLangCode of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User defaultLangCode(final Boolean value) {
    this.defaultLangCode = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultLangCode
  /**
   ** Returns the defaultLangCode of the <code>User</code>.
   **
   ** @return                    the defaultLangCode of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Boolean defaultLangCode() {
    return this.defaultLangCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preferredLangCode
  /**
   ** Sets the preferredLangCode of the <code>User</code>.
   **
   ** @param  value              the preferredLangCode of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User preferredLangCode(final String value) {
    this.preferredLangCode = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preferredLangCode
  /**
   ** Returns the preferredLangCode of the <code>User</code>.
   **
   ** @return                    the preferredLangCode of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String preferredLangCode() {
    return this.preferredLangCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preferredAdminLangCode
  /**
   ** Sets the preferredAdminLangCode of the <code>User</code>.
   **
   ** @param  value              the preferredAdminLangCode of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User preferredAdminLangCode(final String value) {
    this.preferredAdminLangCode = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preferredAdminLangCode
  /**
   ** Returns the preferredAdminLangCode of the <code>User</code>.
   **
   ** @return                    the preferredAdminLangCode of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String preferredAdminLangCode() {
    return this.preferredAdminLangCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Sets the timeZone of the <code>User</code>.
   **
   ** @param  value              the timeZone of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
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
   ** Returns the timeZone of the <code>User</code>.
   **
   ** @return                    the timeZone of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String timeZone() {
    return this.timeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Sets the created of the <code>User</code>.
   **
   ** @param  value              the created of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User created(final Date value) {
    this.created = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Returns the created of the <code>User</code>.
   **
   ** @return                    the created of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Date created() {
    return this.created;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Sets the changed of the <code>User</code>.
   **
   ** @param  value              the changed of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User changed(final Date value) {
    this.changed = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Returns the changed of the <code>User</code>.
   **
   ** @return                    the changed of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Date changed() {
    return this.changed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Sets the access of the <code>User</code>.
   **
   ** @param  value              the access of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User access(final Date value) {
    this.access = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Returns the access of the <code>User</code>.
   **
   ** @return                    the access of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Date access() {
    return this.access;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login
  /**
   ** Sets the login of the <code>User</code>.
   **
   ** @param  value              the login of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User login(final Date value) {
    this.login = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login
  /**
   ** Returns the login of the <code>User</code>.
   **
   ** @return                    the login of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final Date login() {
    return this.login;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init
  /**
   ** Sets the init of the <code>User</code>.
   **
   ** @param  value              the init of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User init(final String value) {
    this.init = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init
  /**
   ** Returns the init of the <code>User</code>.
   **
   ** @return                    the init of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String init() {
    return this.init;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apigeeEdgeDeveloperId
  /**
   ** Sets the apigeeEdgeDeveloperId of the <code>User</code>.
   **
   ** @param  value              the apigeeEdgeDeveloperId of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User apigeeEdgeDeveloperId(final String value) {
    this.apigeeEdgeDeveloperId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apigeeEdgeDeveloperId
  /**
   ** Returns the apigeeEdgeDeveloperId of the <code>User</code>.
   **
   ** @return                    the apigeeEdgeDeveloperId of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String apigeeEdgeDeveloperId() {
    return this.apigeeEdgeDeveloperId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   customerProfiles
  /**
   ** Sets the customerProfiles of the <code>User</code>.
   **
   ** @param  value              the customerProfiles of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User customerProfiles(final String value) {
    this.customerProfiles = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   customerProfiles
  /**
   ** Returns the customerProfiles of the <code>User</code>.
   **
   ** @return                    the customerProfiles of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String customerProfiles() {
    return this.customerProfiles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Sets the path of the <code>User</code>.
   **
   ** @param  value              the path of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User path(final String value) {
    this.path = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path of the <code>User</code>.
   **
   ** @return                    the path of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commerceRemoteId
  /**
   ** Sets the commerceRemoteId of the <code>User</code>.
   **
   ** @param  value              the commerceRemoteId of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User commerceRemoteId(final String value) {
    this.commerceRemoteId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commerceRemoteId
  /**
   ** Returns the commerceRemoteId of the <code>User</code>.
   **
   ** @return                    the commerceRemoteId of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String commerceRemoteId() {
    return this.commerceRemoteId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userPicture
  /**
   ** Sets the userPicture of the <code>User</code>.
   **
   ** @param  value              the userPicture of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User userPicture(final String value) {
    this.userPicture = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userPicture
  /**
   ** Returns the userPicture of the <code>User</code>.
   **
   ** @return                    the userPicture of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userPicture() {
    return this.userPicture;
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
  public final User behoerde(final String value) {
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
    result = PRIME * result * (this.name                   != null ? this.name.hashCode()                   : 0);
    result = PRIME * result + (this.email                  != null ? this.email.hashCode()                  : 0);
    result = PRIME * result + (this.lastName               != null ? this.lastName.hashCode()               : 0);
    result = PRIME * result + (this.firstName              != null ? this.firstName.hashCode()              : 0);
    result = PRIME * result + (this.status                 != null ? this.status.hashCode()                 : 0);
    result = PRIME * result + (this.langCode               != null ? this.langCode.hashCode()               : 0);
    result = PRIME * result + (this.defaultLangCode        != null ? this.defaultLangCode.hashCode()        : 0);
    result = PRIME * result + (this.preferredLangCode      != null ? this.preferredLangCode.hashCode()      : 0);
    result = PRIME * result + (this.preferredAdminLangCode != null ? this.preferredAdminLangCode.hashCode() : 0);
    result = PRIME * result + (this.timeZone               != null ? this.timeZone.hashCode()               : 0);
    result = PRIME * result + (this.created                != null ? this.lastName.hashCode()               : 0);
    result = PRIME * result + (this.changed                != null ? this.changed.hashCode()                : 0);
    result = PRIME * result + (this.access                 != null ? this.access.hashCode()                 : 0);
    result = PRIME * result + (this.login                  != null ? this.login.hashCode()                  : 0);
    result = PRIME * result + (this.init                   != null ? this.init.hashCode()                   : 0);
    result = PRIME * result + (this.apigeeEdgeDeveloperId  != null ? this.apigeeEdgeDeveloperId.hashCode()  : 0);
    result = PRIME * result + (this.customerProfiles       != null ? this.customerProfiles.hashCode()       : 0);
    result = PRIME * result + (this.path                   != null ? this.path.hashCode()                   : 0);
    result = PRIME * result + (this.commerceRemoteId       != null ? this.commerceRemoteId.hashCode()       : 0);
    result = PRIME * result + (this.userPicture            != null ? this.userPicture.hashCode()            : 0);
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

    final User that = (User)other;
    
    if (this.uid                    != null ? !this.uid.                   equals(that.uid)                    : that.uid                    != null)
      return false;

    if (this.uuid                   != null ? !this.uuid.                  equals(that.uuid)                   : that.uuid                   != null)
      return false;

    if (this.name                   != null ? !this.name.                  equals(that.name)                   : that.name                   != null)
      return false;

    if (this.email                  != null ? !this.email.                 equals(that.email)                  : that.email                  != null)
      return false;

    if (this.lastName               != null ? !this.lastName.              equals(that.lastName)               : that.lastName               != null)
      return false;

    if (this.firstName              != null ? !this.firstName.             equals(that.firstName)              : that.firstName              != null)
      return false;

    if (this.langCode               != null ? !this.langCode.              equals(that.langCode)               : that.langCode               != null)
      return false;

    if (this.defaultLangCode        != null ? !this.defaultLangCode.       equals(that.defaultLangCode)        : that.defaultLangCode        != null)
      return false;

    if (this.preferredLangCode      != null ? !this.preferredLangCode.     equals(that.preferredLangCode)      : that.preferredLangCode      != null)
      return false;

    if (this.preferredAdminLangCode != null ? !this.preferredAdminLangCode.equals(that.preferredAdminLangCode) : that.preferredAdminLangCode != null)
      return false;

    if (this.timeZone               != null ? !this.timeZone.              equals(that.timeZone)               : that.timeZone               != null)
      return false;

    if (this.created                != null ? !this.created.               equals(that.created)                : that.created                != null)
      return false;

    if (this.changed                != null ? !this.changed.               equals(that.changed)                : that.changed                != null)
      return false;

    if (this.access                 != null ? !this.access.                equals(that.access)                 : that.access                 != null)
      return false;

    if (this.login                  != null ? !this.login.                 equals(that.login)                  : that.login                  != null)
      return false;

    if (this.init                   != null ? !this.init.                  equals(that.init)                   : that.init                   != null)
      return false;

    if (this.apigeeEdgeDeveloperId  != null ? !this.apigeeEdgeDeveloperId. equals(that.apigeeEdgeDeveloperId)  : that.apigeeEdgeDeveloperId  != null)
      return false;

    if (this.customerProfiles       != null ? !this.customerProfiles.      equals(that.customerProfiles)       : that.customerProfiles       != null)
      return false;

    if (this.path                   != null ? !this.path.                  equals(that.path)                   : that.path                   != null)
      return false;

    if (this.commerceRemoteId       != null ? !this.commerceRemoteId.      equals(that.commerceRemoteId)       : that.commerceRemoteId       != null)
      return false;

    if (this.userPicture            != null ? !this.userPicture.           equals(that.userPicture)            : that.userPicture            != null)
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
    final String method = "User#toString()";
    StringBuilder sb = new StringBuilder(method + " User object dump:");
    sb.append(" uid=").                    append(this.uid() );
    sb.append(" uuid=").                   append(this.uuid() );
    sb.append(" name=").                   append(this.name() );
    sb.append(" email=").                  append(this.email() );
    sb.append(" lastName=").               append(this.lastName() );
    sb.append(" firstName=").              append(this.firstName() );
    sb.append(" langCode=").               append(this.langCode() );
    sb.append(" defaultLangCode=").        append(this.defaultLangCode() );
    sb.append(" preferredLangCode=").      append(this.preferredLangCode() );
    sb.append(" preferredAdminLangCode="). append(this.preferredAdminLangCode() );
    sb.append(" timeZone=").               append(this.timeZone() );
    sb.append(" status=").                 append(this.status() );
    sb.append(" created=").                append(this.created() );
    sb.append(" changed=").                append(this.changed() );
    sb.append(" access=").                 append(this.access() );
    sb.append(" login=").                  append(this.login() );
    sb.append(" init=").                   append(this.init() );
    sb.append(" apigeeEdgeDeveloperId=").  append(this.apigeeEdgeDeveloperId() );
    sb.append(" customerProfiles=").       append(this.customerProfiles() );
    sb.append(" path=").                   append(this.path() );
    sb.append(" commerceRemoteId=").       append(this.commerceRemoteId() );
    sb.append(" userPicture=").            append(this.userPicture() );
    sb.append(" behoerde=").               append(this.behoerde() );
    sb.append(" roles=").                  append(this.roles() );

    return(sb.toString());
  }
}