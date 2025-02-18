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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   UserResult.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserResult.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.system.simulation.scim.schema.Support;
import oracle.iam.system.simulation.rest.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class UserResult
// ~~~~~ ~~~~~~~~~~
/**
 ** The REST entity of a Google API Gateway seach result for <code>User</code>s.
 ** <br>
 ** Apigee provides a very limited API to search for <code>User</code>s. There
 ** isn't anything that allows:
 ** <ol>
 **  <li>Apply paginiation on a result set.
 **  <li>Apply filter criteria for obtaining deltas only
 ** </ol>
 ** Furthermore in searching for <code>User</code>s only the list of provisioned
 ** user id's is returned and a follow up request is required to get the details
 ** for each <code>User</code> REST resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UserResult implements Resource<UserResult> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("user")
  private List<Entry> list;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  // ~~~~~ ~~~~~
  /**
   ** A certain entry in the result set of the REST resource <code>User</code>s.
   */
  public static class Entry implements Resource<Entry>{

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty
    private String name;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Entry</code> REST Resource that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Entry() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the <code>Entry</code>.
     **
     ** @param  value            the name of the <code>Entry</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Entry</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Entry</code>.
     */
    public final Entry name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the <code>Entry</code>.
     **
     ** @return                  the name of the <code>Entry</code>.
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
      return 31 * (this.name != null ? this.name.hashCode() : 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Entry</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Entry</code>s may be different even though they contain the same
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

      final Entry that = (Entry)other;
      return !(this.name != null ? !this.name.equals(that.name) : that.name != null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder("{");
      builder.append("\"name\":\"").append(this.name).append("\"");
      builder.append("}");
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UserResult</code> REST Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserResult() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Sets the user result set.
   **
   ** @param  value              the user result set.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Entry}.
   **
   ** @return                    the <code>UserResult</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResult</code>.
   */
  public final UserResult list(final List<Entry> value) {
    this.list = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns the user result set.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the user result set.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Entry}.
   */
  public final List<Entry> list() {
    return this.list;
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
    return 31 * (this.list != null ? this.list.hashCode() : 0);
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

    final UserResult that = (UserResult)other;
    return !(this.list != null ? !this.list.equals(that.list) : that.list != null);
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
    try {
      return Support.objectWriter().writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}