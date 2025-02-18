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

    File        :   Member.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Member.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.util.Map;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// final class Member
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** The REST membership entity representation of a user.
 ** <p>
 ** This is a utility class to fetch all role mappings from the Service Provider
 ** and does not belong to ICF data transfer.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Member implements Resource<Member> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The collection of roles granted to the user have at realm level.
   */
  @JsonProperty("realmMappings")
  @Attribute(multiValueClass=Role.class)
  private List<Role>           realm;
  /**
   ** The collection of roles the user have in clients.
   */
  @JsonProperty("clientMappings")
  @Attribute(multiValueClass=Client.class)
  private Map<String, Client>  client;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Member</code> REST representation that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Member() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   realm
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
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public final Member realm(final List<Role> value) {
    this.realm = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   realm
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
  public final List<Role> realm() {
    return this.realm;
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
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final Member client(final Map<String, Client> value) {
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
  public final Map<String, Client> client() {
    return this.client;
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
    return Objects.hash(this.realm, this.client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Resource)
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
    return this.realm == null ? "<null>" : this.realm.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Member</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Member</code>s may be different even though they contain the same
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
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Member that = (Member)other;
    return Objects.equals(this.realm, that.client);
  }
}