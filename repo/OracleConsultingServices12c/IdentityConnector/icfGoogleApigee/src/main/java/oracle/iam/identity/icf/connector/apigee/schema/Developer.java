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
    Subsystem   :   Google Apigee Edge Connector

    File        :   Developer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Developer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;

import oracle.iam.identity.icf.connector.apigee.Marshaller;

////////////////////////////////////////////////////////////////////////////////
// class Developer
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>Developer</code> REST resource in Google Apigee Edge.
 ** <p>
 ** Developers must register with an organization on Apigee Edge.
 ** <br>
 ** After they are registered, developers register their apps, choose the APIs
 ** they want to use, and receive the unique API credentials (consumer keys and
 ** secrets) needed to access your APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Marshaller.DEVELOPER_NAME)
public class Developer extends Entity<Developer> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The JSON tag name of the <code>developerId</code> attribute */
  public static final String ID        = "developerId";

  /** The JSON tag name of the <code>email</code> attribute */
  public static final String EMAIL     = "email";

  /** The JSON tag name of the <code>status</code> attribute */
  public static final String STATUS    = "status";

  /** The JSON tag name of the <code>tenant</code> attribute */
  public static final String TENANT    = "organizationName";

  /** The JSON tag name of the <code>userName</code> attribute */
  public static final String USERNAME  = "userName";

  /** The JSON tag name of the <code>lastName</code> attribute */
  public static final String LASTNAME  = "lastName";

  /** The JSON tag name of the <code>firstName</code> attribute */
  public static final String FIRSTNAME = "firstName";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Read-only system GUID of a <code>Developer</code>.
   ** <br>
   ** Useful when querying developer analytics.
   */
  @JsonProperty(ID)
  @Attribute(value=Attribute.IDENTIFIER, mutability=Mutability.IMMUTABLE)
  private String             id;

  /** The developer's email, used to unique identify the developer in Edge. */
  @JsonProperty(EMAIL)
  @Attribute(value=Attribute.UNIQUE, required=true)
  private String             email;

  /** Read-only Apigee organization where the developer is registered. */
  @JsonProperty(TENANT)
  @Attribute(mutability=Mutability.IMMUTABLE, required=true)
  private String             tenant;

  /**
   ** The developer status: 'active' or 'inactive'.
   ** <br>
   ** Defaults to 'active' if nothing else is specified.
   */
  @JsonProperty(STATUS)
  @Attribute(Attribute.STATUS)
  private String             status = "active";

  /** The developer's username. Not used in Apigee */
  @Attribute
  @JsonProperty(USERNAME)
  private String             userName;

  /** The first name of the developer. */
  @Attribute
  @JsonProperty(FIRSTNAME)
  private String             firstName;

  /** The last name of the developer. */
  @Attribute
  @JsonProperty(LASTNAME)
  private String             lastName;

  /**
   ** UNIX time when the <code>Developer</code> was created.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty
  protected Long             createdAt;

  /**
   ** The username of the user who created the <code>Developer</code>.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty
  protected String           createdBy;

  /**
   ** UNIX time when the <code>Developer</code> was most recently updated.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("lastModifiedAt")
  protected Long             modifiedAt;

  /**
   ** The username of the user who most recently updated the
   ** <code>Developer</code>.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("lastModifiedBy")
  protected String           modifiedBy;

  /**
   ** The collection of companies the <code>Developer</code> belongs to.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("companies")
  protected List<String>     company;

  /**
   ** The collection of applications the <code>Developer</code> belongs to.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("apps")
  protected List<String>     application;

  /**
   ** The collection of extended attributes for the <code>Developer</code>.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("attributes")
  protected List<Pair>       attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Developer</code> REST Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Developer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the id of the <code>Developer</code> generated by Google Apigee Edge.
   **
   ** @param  value              the id of the <code>Developer</code> generated
   **                            by Google Apigee Edge.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the id of the <code>Developer</code> generated by Google API
   ** Gateway.
   **
   ** @return                    the id of the <code>Developer</code> generated
   **                            by Google Apigee Edge.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Sets the email of the <code>Developer</code>.
   **
   ** @param  value              the email of the <code>Developer</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer email(final String value) {
    this.email = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Returns the email of the <code>Developer</code>.
   **
   ** @return                    the email of the <code>Developer</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String email() {
    return this.email;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Sets the family name of the Developer, or Last Name in most Western
   ** languages (for example, Jensen given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @param  value              the family name of the Developer, or Last Name
   **                            in most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer lastName(final String value) {
    this.lastName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Returns the family name of the Developer, or Last Name in most Western
   ** languages (for example, Jensen given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the family name of the Developer, or Last Name
   **                            in most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String lastName() {
    return this.lastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Sets the given name of the Developer, or First Name in most Western
   ** languages (for example, Barbara given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @param  value              the given name of the Developer, or First Name
   **                            in most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer firstName(final String value) {
    this.firstName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Returns the given name of the Developer, or First Name in most Western
   ** languages (for example, Barbara given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the given name of the Developer, or First Name
   **                            in most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Sets the user name of the <code>Developer</code>.
   **
   ** @param  value              the user name of the Developer.
   **                            <br>
   **                            Not used in apigee.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer userName(final String value) {
    this.userName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Returns the family name of the Developer, or Last Name in most Western
   ** languages (for example, Jensen given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the user name of the Developer.
   **                            <br>
   **                            Not used in apigee.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userName() {
    return this.userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Sets the organization where the <code>Developer</code> is registered.
   **
   ** @param  value              the organization where the
   **                            <code>Developer</code> is registered.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer tenant(final String value) {
    this.tenant = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Returns the organization where the <code>Developer</code> is registered.
   **
   ** @return                    the organization where the
   **                            <code>Developer</code> is registered.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String tenant() {
    return this.tenant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Sets the status of the <code>Developer</code>.
   **
   ** @param  value              the status of the <code>Developer</code>
   **                            either
   **                            <ul>
   **                              <li><code>active</code>
   **                              <li><code>inactive</code>
   **                            </ul>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer status(final String value) {
    this.status = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of the <code>Developer</code>
   **
   ** @return                    the status of the <code>Developer</code>
   **                            either
   **                            <ul>
   **                              <li><code>active</code>
   **                              <li><code>inactive</code>
   **                            </ul>
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdAt
  /**
   ** Sets the timestamp of when the REST object was created.
   **
   ** @param  value              the date and time the REST object was created.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer createdAt(final Long value) {
    this.createdAt = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdAt
  /**
   ** Returns the timestamp of when the REST object was created.
   **
   ** @return                    the date and time the REST object was created.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long createdAt() {
    return this.createdAt;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdBy
  /**
   ** Sets the account identifier whom created the REST object.
   **
   ** @param  value              the account identifier whom created the REST
   **                            object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer createdBy(final String value) {
    this.createdBy = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdBy
  /**
   ** Returns the account identifier whom created the REST object.
   **
   ** @return                    the account identifier whom created the REST
   **                            object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String createdBy() {
    return this.createdBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedAt
  /**
   ** Sets the timestamp of when the REST object was last modified.
   **
   ** @param  value              the date and time the REST object was last
   **                            modified.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer modifiedAt(final Long value) {
    this.modifiedAt = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedAt
  /**
   ** Returns the timestamp of when the REST object was last modified.
   **
   ** @return                    the date and time the REST object was last
   **                            modified.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long modifiedAt() {
    return this.modifiedAt;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedBy
  /**
   ** Sets the account identifier whom modified the REST object last time.
   **
   ** @param  value              the account identifier whom modified the REST
   **                            object last time.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer modifiedBy(final String value) {
    this.modifiedBy = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedBy
  /**
   ** Returns the account identifier whom modified the REST object last time.
   **
   ** @return                    the account identifier whom modified the REST
   **                            object last time.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String modifiedBy() {
    return this.modifiedBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   company
  /**
   ** Sets the {@link List} of companies the <code>Developer</code> belongs
   ** too.
   **
   ** @param  value              the {@link List} of companies.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer company(final List<String> value) {
    this.company = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   company
  /**
   ** Returns the {@link List} of companies the <code>Developer</code> belongs
   ** too.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of companies.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public final List<String> company() {
    return this.company;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Sets the {@link List} of applications the <code>Developer</code> belongs
   ** too.
   **
   ** @param  value              the {@link List} of applications.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer application(final List<String> value) {
    this.application = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the {@link List} of applications the <code>Developer</code>
   ** belongs to.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of applications.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public final List<String> application() {
    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets the {@link List} of named-value <code>Pair</code> attributes for the
   ** <code>Developer</code>.
   **
   ** @param  value              the {@link List} of named-value
   **                            <code>Pair</code> attributes.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            <code>Pair</code>.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer attribute(final List<Pair> value) {
    this.attribute = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link List} of named-value <code>Pair</code> attributes for
   ** the <code>Developer</code>.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of named-value
   **                            <code>Pair</code> attributes.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>Pair</code>.
   */
  public final List<Pair> attribute() {
    return this.attribute;
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
    int result = this.id != null ? this.id.hashCode() : 0;
    result = PRIME * result + (this.email      != null ? this.email.hashCode()      : 0);
    result = PRIME * result + (this.tenant     != null ? this.tenant.hashCode()     : 0);
    result = PRIME * result + (this.status     != null ? this.status.hashCode()     : 0);
    result = PRIME * result + (this.userName   != null ? this.userName.hashCode()   : 0);
    result = PRIME * result + (this.lastName   != null ? this.lastName.hashCode()   : 0);
    result = PRIME * result + (this.firstName  != null ? this.firstName.hashCode()  : 0);
    result = PRIME * result + (this.createdAt  != null ? this.createdAt.hashCode()  : 0);
    result = PRIME * result + (this.createdBy  != null ? this.createdBy.hashCode()  : 0);
    result = PRIME * result + (this.modifiedAt != null ? this.modifiedAt.hashCode() : 0);
    result = PRIME * result + (this.modifiedBy != null ? this.modifiedBy.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Developer</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Developer</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
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

    final Developer that = (Developer)other;
    if (this.id != null ? !this.id.equals(that.id) : that.id != null)
      return false;

    if (this.email != null ? !this.email.equals(that.email) : that.email != null)
      return false;

    if (this.tenant != null ? !this.tenant.equals(that.tenant) : that.tenant != null)
      return false;

    if (this.status != null ? !this.status.equals(that.status) : that.status != null)
      return false;

    if (this.lastName != null ? !this.lastName.equals(that.lastName) : that.lastName != null)
      return false;

    if (this.firstName != null ? !this.firstName.equals(that.firstName) : that.firstName != null)
      return false;

    if (this.userName != null ? !this.userName.equals(that.userName) : that.userName != null)
      return false;

    if (this.createdAt != null ? !this.createdAt.equals(that.createdAt) : that.createdAt != null)
      return false;

    if (this.createdBy != null ? !this.createdBy.equals(that.createdBy) : that.createdBy != null)
      return false;

    if (this.modifiedAt != null ? !this.modifiedAt.equals(that.modifiedAt) : that.modifiedAt != null)
      return false;

    return !(this.modifiedBy != null ? !this.modifiedBy.equals(that.modifiedBy) : that.modifiedBy != null);
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
    return this.email;
  }
}