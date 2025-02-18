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
                    UserJson.


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
 ** The <code>UserJson</code> REST resource in Google Drupal.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class UserJson extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The JSON tag name of the <code>uid</code> attribute. */
  public static final String UID                = "uid";

  /** The JSON tag name of the <code>uuid</code> (internal ID) attribute. */
  public static final String UUID               = "uuid";

  /** The JSON tag name of the <code>name</code> (as in "username") attribute. */
  public static final String NAME               = "name";

  /** The JSON tag name of the <code>mail</code> attribute. */
  public static final String EMAIL              = "mail";

  /** The JSON tag name of the <code>lastName</code> attribute. */
  public static final String LASTNAME           = "last_name";

  /** The JSON tag name of the <code>firstName</code> attribute. */
  public static final String FIRSTNAME          = "first_name";

  /** The JSON tag name of the <code>status</code> attribute. */
  public static final String STATUS             = "status";

  /** The JSON tag name of the <code>roles</code> attribute. */
  public static final String ROLES              = "roles";

  /** The JSON tag name of the <code>langcode</code> attribute. */
  public static final String LANG               = "langcode";

  /** The JSON tag name of the <code>default_langcode</code> attribute. */
  public static final String DEFAULT_LANG       = "default_langcode";

  /** The JSON tag name of the <code>preferred_langcode</code> attribute. */
  public static final String PREFERRED_LANG     = "preferred_langcode";

  /** The JSON tag name of the <code>preferred_admin_langcode</code> attribute. */
  public static final String PREFERRED_ADM_LANG = "preferred_admin_langcode";

  /** The JSON tag name of the <code>timezone</code> attribute. */
  public static final String TIMEZONE           = "timezone";

  /** The JSON tag name of the <code>created</code> attribute. */
  public static final String CREATED            = "created";

  /** The JSON tag name of the <code>changed</code> attribute. */
  public static final String CHANGED            = "changed";

  /** The JSON tag name of the <code>access</code> attribute. */
  public static final String ACCESS             = "access";

  /** The JSON tag name of the <code>login</code> attribute. */
  public static final String LOGIN              = "login";

  /** The JSON tag name of the <code>init</code> attribute. */
  public static final String INIT               = "init";

  /** The JSON tag name of the <code>apigee_edge_developer_id</code> attribute. */
  public static final String APIGEE_EDGE_DEV_ID = "apigee_edge_developer_id";

  /** The JSON tag name of the <code>customer_profiles</code> attribute. */
  public static final String CUST_PROFILES      = "customer_profiles";

  /** The JSON tag name of the <code>path</code> attribute. */
  public static final String PATH               = "path";

  /** The JSON tag name of the <code>commerce_remote_id</code> attribute. */
  public static final String COMMERCE_REMOTE_ID = "commerce_remote_id";

  /** The JSON tag name of the <code>user_picture</code> attribute. */
  public static final String USER_PICTURE       = "user_picture";

  /** The JSON tag name of the <code>field_behoerde</code> attribute. */
  public static final String FIELD_BEHOERDE     = "field_behoerde";

  /** The JSON tag name of the <code>value</code> attribute. */
  public static final String VALUE = "value";

  /** The JSON tag name of the <code>format</code> attribute. */
  public static final String FORMAT = "format";

  /** The JSON tag name of the <code>target_id</code> attribute. */
  public static final String ROLES_TARGET_ID = "target_id";

  /** The JSON tag name of the <code>target_type</code> attribute. */
  public static final String ROLES_TARGET_TYPE = "target_type";

  /** The JSON tag name of the <code>target_uuid</code> attribute. */
  public static final String ROLES_TARGET_UUID = "target_uuid";

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
  private SimpleValue[]         uid;

  @Attribute
  @JsonProperty(UUID)
  private SimpleValue[]         uuid;

  @Attribute
  @JsonProperty(NAME)
  private SimpleValue[]         name;

  @Attribute
  @JsonProperty(EMAIL)
  private SimpleValue[]         email;

  @Attribute
  @JsonProperty(LASTNAME)
  private SimpleValue[]         lastName;

  @Attribute
  @JsonProperty(FIRSTNAME)
  private SimpleValue[]         firstName;

  @Attribute
  @JsonProperty(STATUS)
  private BooleanValue[]        status;

  @Attribute
  @JsonProperty(ROLES)
  private List<Role>            roles;
  
  @Attribute
  @JsonProperty(LANG)
  private SimpleValue[]         langCode;

  @Attribute
  @JsonProperty(DEFAULT_LANG)
  private BooleanValue[]        defaultLangCode;

  @Attribute
  @JsonProperty(PREFERRED_LANG)
  private SimpleValue[]         preferredLangCode;

  @Attribute
  @JsonProperty(PREFERRED_ADM_LANG)
  private SimpleValue[]         preferredAdminLangCode;

  @Attribute
  @JsonProperty(TIMEZONE)
  private SimpleValue[]         timeZone;

  @Attribute
  @JsonProperty(CREATED)
  private DateValue[]           created;

  @Attribute
  @JsonProperty(CHANGED)
  private DateValue[]           changed;

  @Attribute
  @JsonProperty(ACCESS)
  private DateValue[]           access;

  @Attribute
  @JsonProperty(LOGIN)
  private DateValue[]           login;

  @Attribute
  @JsonProperty(INIT)
  private SimpleValue[]         init;

  @Attribute
  @JsonProperty(APIGEE_EDGE_DEV_ID)
  private SimpleValue[]         apigeeEdgeDeveloperId;

  @Attribute
  @JsonProperty(CUST_PROFILES)
  private SimpleValue[]         customerProfiles;

  @Attribute
  @JsonProperty(PATH)
  private PathValue[]           path;

  @Attribute
  @JsonProperty(COMMERCE_REMOTE_ID)
  private SimpleValue[]         commerceRemoteId;

  @Attribute
  @JsonProperty(USER_PICTURE)
  private SimpleValue[]         userPicture;

  @Attribute
  @JsonProperty(FIELD_BEHOERDE)
  private SimpleValue[]         behoerde;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class SimpleValue
  // ~~~~~ ~~~~
  /**
   ** For some weird reason Google Drupal passes all (well, most) attribute
   ** values enclosed in a SimpleValue object, this class represents the wrapping
   ** object.
   */
  public static class SimpleValue {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty(VALUE)
    private String value;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs an empty <code>SimpleValue</code> REST Resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public SimpleValue() {
    }
    public SimpleValue(String val) {
      this.value = val;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Sets the value of the <code>SimpleValue</code>.
     **
     ** @param  val                the value of the <code>SimpleValue</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>SimpleValue</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>SimpleValue</code>.
     */
    public final SimpleValue value(final String val) {
      this.value = val;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   name
    /**
     ** Returns the value of the <code>SimpleValue</code>.
     **
     ** @return                    the value of the <code>SimpleValue</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String value() {
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
     **                            <br>
     **                            Possible object is <code>int</code>.
     */
    @Override
    public final int hashCode() {
      int result = super.hashCode();
      result = PRIME * result + (this.value != null ? this.value.hashCode() : 0);
      return result;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>SimpleValue</code>s are considered equal if and only if they represent
     ** the same properties. As a consequence, two given <code>SimpleValue</code>s may be
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

      final SimpleValue that = (SimpleValue)other;
      if (this.value != null ? !this.value.equals(that.value) : that.value != null)
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
      StringBuilder sb = new StringBuilder("SimpleValue object dump:");
      sb.append(" value=").append(this.value);
      return(sb.toString());
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // class BooleanValue
  // ~~~~~ ~~~~
  /**
   ** Some objects contain of another set of attributes, this class is to
   ** represent these.
   */
  public static class BooleanValue {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty(VALUE)
    private Boolean value;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs an empty <code>BooleanValue</code> REST Resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public BooleanValue() {
    }
    public BooleanValue(Boolean val) {
      this.value = val;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Sets the value of the <code>BooleanValue</code>.
     **
     ** @param  val                the value of the <code>BooleanValue</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>BooleanValue</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>BooleanValue</code>.
     */
    public final BooleanValue value(final Boolean val) {
      this.value = val;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   name
    /**
     ** Returns the value of the <code>BooleanValue</code>.
     **
     ** @return                    the value of the <code>BooleanValue</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final Boolean value() {
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
     **                            <br>
     **                            Possible object is <code>int</code>.
     */
    @Override
    public final int hashCode() {
      int result = super.hashCode();
      result = PRIME * result + (this.value != null ? this.value.hashCode() : 0);
      return result;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>SimpleValue</code>s are considered equal if and only if they represent
     ** the same properties. As a consequence, two given <code>SimpleValue</code>s may be
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

      final BooleanValue that = (BooleanValue)other;
      if (this.value != null ? !this.value.equals(that.value) : that.value != null)
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
      StringBuilder sb = new StringBuilder("BooleanValue object dump:");
      sb.append(" value=").append(this.value);
      return(sb.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class PathValue
  // ~~~~~ ~~~~
  /**
   ** Some objects contain of another set of attributes, this class is to
   ** represent these.
   */
  public static class PathValue {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty(PATH_ALIAS)
    private String alias;

    @Attribute
    @JsonProperty(PATH_PID)
    private String pid;

    @Attribute
    @JsonProperty(PATH_LANG)
    private String langCode;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs an empty <code>SimpleValue</code> REST Resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public PathValue() {
    }
    public PathValue(String alias, String pid, String langCode) {
      this.alias = alias;
      this.pid = pid;
      this.langCode = langCode;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   alias
    /**
     ** Sets the alias of the <code>PathValue</code>.
     **
     ** @param  val                the alias of the <code>PathValue</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>SimpleValue</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>SimpleValue</code>.
     */
    public final PathValue alias(final String val) {
      this.alias = val;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   alias
    /**
     ** Returns the alias of the <code>PathValue</code>.
     **
     ** @return                    the alias of the <code>PathValue</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String alias() {
      return this.alias;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   pid
    /**
     ** Sets the pid of the <code>PathValue</code>.
     **
     ** @param  val                the pid of the <code>PathValue</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>SimpleValue</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>SimpleValue</code>.
     */
    public final PathValue pid(final String val) {
      this.pid = val;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   pid
    /**
     ** Returns the pid of the <code>PathValue</code>.
     **
     ** @return                    the pid of the <code>PathValue</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String pid() {
      return this.pid;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   langCode
    /**
     ** Sets the langCode of the <code>PathValue</code>.
     **
     ** @param  val                the langCode of the <code>PathValue</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>SimpleValue</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>SimpleValue</code>.
     */
    public final PathValue langCode(final String val) {
      this.langCode = val;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   langCode
    /**
     ** Returns the langCode of the <code>PathValue</code>.
     **
     ** @return                    the langCode of the <code>PathValue</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String langCode() {
      return this.langCode;
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
      int result = super.hashCode();
      result = PRIME * result + (this.alias != null ? this.alias.hashCode() : 0);
      result = PRIME * result + (this.pid != null ? this.pid.hashCode() : 0);
      result = PRIME * result + (this.langCode != null ? this.langCode.hashCode() : 0);
      return result;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>PathValue</code>s are considered equal if and only if they represent
     ** the same properties. As a consequence, two given <code>PathValue</code>s may be
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

      final PathValue that = (PathValue)other;
      if (this.alias != null ? !this.alias.equals(that.alias) : that.alias != null)
        return false;
      if (this.pid != null ? !this.pid.equals(that.pid) : that.pid != null)
        return false;
      if (this.langCode != null ? !this.langCode.equals(that.langCode) : that.langCode != null)
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
      StringBuilder sb = new StringBuilder("PathValue object dump:");
      sb.append(" alias=").append(this.alias);
      sb.append(" pid=").append(this.pid);
      sb.append(" langCode=").append(this.langCode);
      return(sb.toString());
    }
  }
  
  /**
   ** Some of the attributes (dates) also supply a format of the actual attribute
   ** value (i.e. the date) to allow machine parsing. It's contained in the
   ** format attribute and this class represents it.
   */
  public static class DateValue {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty(FORMAT)
    private String format;

    @Attribute
    @JsonProperty(VALUE)
    private String value;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs an empty <code>DateValue</code> REST Resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public DateValue() {
    }
    public DateValue(String val) {
      this.value = val;
      this.format = "Y-m-d\\\\TH:i:sP";
    }
    public DateValue(String val, String format) {
      this.value = val;
      this.format = format;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Sets the value of the <code>DateValue</code>.
     **
     ** @param  val                the value of the <code>DateValue</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>DateValue</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>User</code>.
     */
    public final DateValue value(final String val) {
      this.value = val;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the value of the <code>DateValue</code>.
     **
     ** @return                    the value of the <code>DateValue</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String value() {
      return this.value;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   format
    /**
     ** Sets the format of the <code>DateValue</code>.
     **
     ** @param  val                the format of the <code>DateValue</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>DateValue</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>User</code>.
     */
    public final DateValue format(final String val) {
      this.format = val;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   format
    /**
     ** Returns the format of the <code>DateValue</code>.
     **
     ** @return                    the format of the <code>DateValue</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String format() {
      return this.format;
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
      int result = super.hashCode();
      result = PRIME * result + (this.value != null ? this.value.hashCode() : 0);
      result = PRIME * result + (this.format != null ? this.format.hashCode() : 0);
      return result;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>DateValue</code>s are considered equal if and only if they represent
     ** the same properties. As a consequence, two given <code>DateValue</code>s may be
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

      final DateValue that = (DateValue)other;
      if (this.format != null ? !this.format.equals(that.format) : that.format != null)
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
      StringBuilder sb = new StringBuilder("DateValue object dump:");
      sb.append(" value=").append(this.value);
      sb.append(" format=").append(this.format);
      return(sb.toString());
    }
  }
  
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

    @JsonProperty(ROLES_TARGET_ID)
    protected String targetId;

    @JsonProperty(ROLES_TARGET_TYPE)
    protected String targetType;

    @JsonProperty(ROLES_TARGET_UUID)
    protected String targetUuid;

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
    public Role(String roleName) {
      this.targetId = roleName;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: targetId
    /**
     ** Sets the targetId of the <code>Role</code>.
     **
     ** @param  value            the targetId of the <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MemberOf</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MemberOf</code>.
     */
    public final Role targetId(final String value) {
      this.targetId = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: targetId
    /**
     ** Returns the targetId of the <code>Role</code>.
     **
     ** @return                  the targetId of the <code>Role</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String targetId() {
      return this.targetId;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: targetType
    /**
     ** Sets the targetType of the <code>Role</code>.
     **
     ** @param  value            the targetType of the <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MemberOf</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MemberOf</code>.
     */
    public final Role targetType(final String value) {
      this.targetType = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: targetType
    /**
     ** Returns the targetId of the <code>Role</code>.
     **
     ** @return                  the targetId of the <code>Role</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String targetType() {
      return this.targetType;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: targetUuid
    /**
     ** Sets the targetUuid of the <code>Role</code>.
     **
     ** @param  value            the targetUuid of the <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MemberOf</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MemberOf</code>.
     */
    public final Role targetUuid(final String value) {
      this.targetUuid = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: targetId
    /**
     ** Returns the targetUuid of the <code>Role</code>.
     **
     ** @return                  the targetUuid of the <code>Role</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String targetUuid() {
      return this.targetUuid;
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
      result = PRIME * result + (this.targetId != null ? this.targetId.hashCode() : 0);
      result = PRIME * result + (this.targetType != null ? this.targetType.hashCode() : 0);
      result = PRIME * result + (this.targetUuid != null ? this.targetUuid.hashCode() : 0);
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
      if (this.targetId != null ? !this.targetId.equals(that.targetId) : that.targetId != null)
        return false;
      if (this.targetType != null ? !this.targetType.equals(that.targetType) : that.targetType != null)
        return false;
      if (this.targetUuid != null ? !this.targetUuid.equals(that.targetUuid) : that.targetUuid != null)
        return false;

      return super.equals(other);
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
      StringBuilder sb = new StringBuilder("Role object dump:");
      sb.append(" targetId=").append(this.targetId);
      sb.append(" targetType=").append(this.targetType);
      sb.append(" targetUuid=").append(this.targetUuid);
      return(sb.toString());
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
  public UserJson() {
    final String method = "UserJson#constructor()";
  }

  public UserJson(User user) {
    final String method = "UserJson#constructor(User)";

    if (user.uid() != null) {
      SimpleValue[] uidArr = { new SimpleValue(user.uid()) };
      this.uid = uidArr;
    }
    if (user.uuid() != null) {
      SimpleValue[] uuidArr = { new SimpleValue(user.uuid()) };
      this.uuid = uuidArr;
    }
    if (user.name() != null) {
      SimpleValue[] nameArr = { new SimpleValue(user.name()) };
      this.name = nameArr;
    }
    if (user.email() != null) {
      SimpleValue[] mailArr = { new SimpleValue(user.email()) };
      this.email = mailArr;
    }
    if (user.status() != null) {
      BooleanValue[] statusArr = { new BooleanValue(user.status()) };
      this.status = statusArr;
    }
    if (user.firstName() != null) {
      SimpleValue[] firstNameArr = { new SimpleValue(user.firstName()) };
      this.firstName = firstNameArr;
    }
    if (user.lastName() != null) {
      SimpleValue[] lastNameArr = { new SimpleValue(user.lastName()) };
      this.lastName = lastNameArr;
    }
    if (user.langCode() != null) {
      SimpleValue[] langCodeArr = { new SimpleValue(user.langCode()) };
      this.langCode = langCodeArr;
    }
    if (user.preferredLangCode() != null) {
      SimpleValue[] preferredLangCodeArr = { new SimpleValue(user.preferredLangCode()) };
      this.preferredLangCode = preferredLangCodeArr;
    }
    if (user.preferredAdminLangCode() != null) {
      SimpleValue[] preferredAdminLangCodeArr = { new SimpleValue(user.preferredAdminLangCode()) };
      this.preferredAdminLangCode = preferredAdminLangCodeArr;
    }
    if (user.timeZone() != null) {
      SimpleValue[] timeZoneArr = { new SimpleValue(user.timeZone()) };
      this.timeZone = timeZoneArr;
    }
    if (user.created() != null) {
      DateValue[] createdArr = { new DateValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(user.created())) };
      this.created = createdArr;
    }
    if (user.changed() != null) {
      DateValue[] changedArr = { new DateValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(user.changed())) };
      this.changed = changedArr;
    }
    if (user.access() != null) {
      DateValue[] accessArr = { new DateValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(user.access())) };
      this.access = accessArr;
    }
    if (user.login() != null) {
      DateValue[] loginArr = { new DateValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(user.login())) };
      this.login = loginArr;
    }
    if (user.init() != null) {
      SimpleValue[] initArr = { new SimpleValue(user.init()) };
      this.init = initArr;
    }
    if (user.defaultLangCode() != null) {
      BooleanValue[] defaultLangCodeArr = { new BooleanValue(user.defaultLangCode()) };
      this.defaultLangCode = defaultLangCodeArr;
    }
    if (user.apigeeEdgeDeveloperId() != null) {
      SimpleValue[] apigeeEdgeDeveloperIdArr = { new SimpleValue(user.apigeeEdgeDeveloperId()) };
      this.apigeeEdgeDeveloperId = apigeeEdgeDeveloperIdArr;
    }
    if (user.customerProfiles() != null) {
      SimpleValue[] customerProfilesArr = { new SimpleValue(user.customerProfiles()) };
      this.customerProfiles = customerProfilesArr;
    }
    if (user.path() != null) {
      PathValue[] pathArr = { new PathValue(user.path(), null, "en") };
      this.path = pathArr;
    }
    if (user.commerceRemoteId() != null) {
      SimpleValue[] commerceRemoteIdArr = { new SimpleValue(user.commerceRemoteId()) };
      this.commerceRemoteId = commerceRemoteIdArr;
    }
    if (user.behoerde() != null) {
      SimpleValue[] behoerdeArr = { new SimpleValue(user.behoerde()) };
      this.behoerde = behoerdeArr;
    }
    if (user.userPicture() != null) {
      SimpleValue[] userPictureArr = { new SimpleValue(user.userPicture()) };
      this.userPicture = userPictureArr;
    }
  }

  public UserJson(UserListJson user) {
    final String method = "UserJson#constructor(UserListJson)";

    if (user.uid() != null) {
      SimpleValue[] uidArr = { new SimpleValue(user.uid()) };
      this.uid = uidArr;
    }
    if (user.name() != null) {
      SimpleValue[] nameArr = { new SimpleValue(user.name()) };
      this.name = nameArr;
    }
    if (user.email() != null) {
      SimpleValue[] mailArr = { new SimpleValue(user.email()) };
      this.email = mailArr;
    }
    // TODO: Ask Anton to add !
//    if (user.status() != null) {
//      BooleanValue[] statusArr = { new BooleanValue(user.status()) };
//      this.status = statusArr;
//    }
    if (user.firstName() != null) {
      SimpleValue[] firstNameArr = { new SimpleValue(user.firstName()) };
      this.firstName = firstNameArr;
    }
    if (user.lastName() != null) {
      SimpleValue[] lastNameArr = { new SimpleValue(user.lastName()) };
      this.lastName = lastNameArr;
    }
    if (user.behoerde() != null) {
      SimpleValue[] behoerdeArr = { new SimpleValue(user.behoerde()) };
      this.behoerde = behoerdeArr;
    }
    if (user.roles() != null) {
      this.roles = new ArrayList<>();
      for (String roleComponent : user.roles().split(", ")) {
        this.roles.add(new Role(roleComponent));
      }
    }
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
  public final UserJson uid(final SimpleValue[] value) {
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
  public final SimpleValue[] uid() {
    return this.uid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uuid
  /**
   ** Sets the uuid of the <code>User</code>.
   **
   ** @param  value              the uuid of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final UserJson uuid(final SimpleValue[] value) {
    this.uuid = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the uuid of the <code>User</code>.
   **
   ** @return                    the uuid of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final SimpleValue[] uuid() {
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
  public final UserJson name(final SimpleValue[] value) {
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
  public final SimpleValue[] name() {
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
  public final UserJson email(final SimpleValue[] value) {
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
  public final SimpleValue[] email() {
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
  public final UserJson lastName(final SimpleValue[] value) {
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
  public final SimpleValue[] lastName() {
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
  public final UserJson firstName(final SimpleValue[] value) {
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
  public final SimpleValue[] firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
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
  public final UserJson status(final BooleanValue[] value) {
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
  public final BooleanValue[] status() {
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
   * Allowed object is {@link Role} .
   *
   * @return the <code>User</code> to allow method chaining.
   * <br>
   * Possible object is <code>User</code> for type
   * <code>T</code>.
   */
  public final UserJson roles(final List<Role> value) {
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
  public final List<Role> roles() {
    return this.roles;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleNames
  /**
   * Convenience method to return only the role names from the Role() objects.
   *
   * @return the collection of role names for the User.
   * <br>
   * Possible object is {@link List} of
   * {@link Role} .
   */
  public final List<String> roleNames() {
    List<String> resultList = new ArrayList<>();
//    System.out.println("UserJson#roleNames(): this.roles: " + this.roles);
    if (this.roles != null) {
      for (Role role : this.roles) {
        resultList.add(role.targetId());
      }
    }
//    System.out.println("UserJson#roleNames(): resultList: " + resultList);
    return resultList;
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
  public final UserJson langCode(final SimpleValue[] value) {
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
  public final SimpleValue[] langCode() {
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
  public final UserJson defaultLangCode(final BooleanValue[] value) {
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
  public final BooleanValue[] defaultLangCode() {
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
  public final UserJson preferredLangCode(final SimpleValue[] value) {
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
  public final SimpleValue[] preferredLangCode() {
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
  public final UserJson preferredAdminLangCode(final SimpleValue[] value) {
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
  public final SimpleValue[] preferredAdminLangCode() {
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
  public final UserJson timeZone(final SimpleValue[] value) {
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
  public final SimpleValue[] timeZone() {
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
  public final UserJson created(final DateValue[] value) {
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
  public final DateValue[] created() {
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
  public final UserJson changed(final DateValue[] value) {
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
  public final DateValue[] changed() {
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
  public final UserJson access(final DateValue[] value) {
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
  public final DateValue[] access() {
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
  public final UserJson login(final DateValue[] value) {
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
  public final DateValue[] login() {
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
  public final UserJson init(final SimpleValue[] value) {
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
  public final SimpleValue[] init() {
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
  public final UserJson apigeeEdgeDeveloperId(final SimpleValue[] value) {
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
  public final SimpleValue[] apigeeEdgeDeveloperId() {
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
  public final UserJson customerProfiles(final SimpleValue[] value) {
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
  public final SimpleValue[] customerProfiles() {
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
  public final UserJson path(final PathValue[] value) {
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
  public final PathValue[] path() {
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
  public final UserJson commerceRemoteId(final SimpleValue[] value) {
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
  public final SimpleValue[] commerceRemoteId() {
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
  public final UserJson userPicture(final SimpleValue[] value) {
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
  public final SimpleValue[] userPicture() {
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
  public final UserJson behoerde(final SimpleValue[] value) {
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
  public final SimpleValue[] behoerde() {
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
    result = PRIME * result * (this.uuid                   != null ? this.uuid.hashCode()                   : 0);
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
    final UserJson that = (UserJson)other;
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
    final String method = "UserJson#toString()";
    StringBuilder sb = new StringBuilder(method).append(" UserJson object dump:");
    sb.append(" uid=");
    if (uid                    == null || uid.length                    == 0) { sb.append("null"); } else { sb.append(uid[0]);                    }
    sb.append(" uuid=");
    if (uuid                   == null || uuid.length                   == 0) { sb.append("null"); } else { sb.append(uuid[0]);                   }
    sb.append(" roles=");
    if (roles                  == null || roles.size()                  == 0) { sb.append("null"); } else { sb.append(roles.toString());          }
    sb.append(" roleNames(virtual)=");
    if (roles                  == null || roles.size()                  == 0) { sb.append("null"); } else { sb.append(roleNames().toString());    }
//    if (roleNames()            == null || roleNames().size()            == 0) { sb.append("null"); } else { sb.append(roleNames().toString());    }
    sb.append(" name=");
    if (name                   == null || name.length                   == 0) { sb.append("null"); } else { sb.append(name[0]);                   }
    sb.append(" email=");
    if (email                  == null || email.length                  == 0) { sb.append("null"); } else { sb.append(email[0]);                  }
    sb.append(" lastName=");
    if (lastName               == null || lastName.length               == 0) { sb.append("null"); } else { sb.append(lastName[0]);               }
    sb.append(" firstName=");
    if (firstName              == null || firstName.length              == 0) { sb.append("null"); } else { sb.append(firstName[0]);              }
    sb.append(" langCode=");
    if (langCode               == null || langCode.length               == 0) { sb.append("null"); } else { sb.append(langCode[0]);               }
    sb.append(" defaultLangCode=");
    if (defaultLangCode        == null || defaultLangCode.length        == 0) { sb.append("null"); } else { sb.append(defaultLangCode[0]);        }
    sb.append(" preferredLangCode=");
    if (preferredLangCode      == null || preferredLangCode.length      == 0) { sb.append("null"); } else { sb.append(preferredLangCode[0]);      }
    sb.append(" preferredAdminLangCode=");
    if (preferredAdminLangCode == null || preferredAdminLangCode.length == 0) { sb.append("null"); } else { sb.append(preferredAdminLangCode[0]); }
    sb.append(" timeZone=");
    if (timeZone               == null || timeZone.length               == 0) { sb.append("null"); } else { sb.append(timeZone[0]);               }
    sb.append(" status=");
    if (status                 == null || status.length                 == 0) { sb.append("null"); } else { sb.append(status[0]);                 }
    sb.append(" created=");
    if (created                == null || created.length                == 0) { sb.append("null"); } else { sb.append(created[0]);                }
    sb.append(" changed=");
    if (changed                == null || changed.length                == 0) { sb.append("null"); } else { sb.append(changed[0]);                }
    sb.append(" access=");
    if (access                 == null || access.length                 == 0) { sb.append("null"); } else { sb.append(access[0]);                 }
    sb.append(" login=");
    if (login                  == null || login.length                  == 0) { sb.append("null"); } else { sb.append(login[0]);                  }
    sb.append(" init=");
    if (init                   == null || init.length                   == 0) { sb.append("null"); } else { sb.append(init[0]);                   }
    sb.append(" apigeeEdgeDeveloperId=");
    if (apigeeEdgeDeveloperId  == null || apigeeEdgeDeveloperId.length  == 0) { sb.append("null"); } else { sb.append(apigeeEdgeDeveloperId[0]);  }
    sb.append(" customerProfiles=");
    if (customerProfiles       == null || customerProfiles.length       == 0) { sb.append("null"); } else { sb.append(customerProfiles[0]);       }
    sb.append(" path=");
    if (path                   == null || path.length                   == 0) { sb.append("null"); } else { sb.append(path[0]);                   }
    sb.append(" commerceRemoteId=");
    if (commerceRemoteId       == null || commerceRemoteId.length       == 0) { sb.append("null"); } else { sb.append(commerceRemoteId[0]);       }
    sb.append(" userPicture=");
    if (userPicture            == null || userPicture.length            == 0) { sb.append("null"); } else { sb.append(userPicture[0]);            }
    sb.append(" behoerde=");
    if (behoerde               == null || behoerde.length               == 0) { sb.append("null"); } else { sb.append(behoerde[0]);               }
    return(sb.toString());
  }
}