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

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// abstract class Entity
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The base class of REST resources.
 **
 ** @param  <T>                  the type of the <code>Entity</code>
 **                              implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>T</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class Entity<T extends Entity> implements Resource<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final
  //////////////////////////////////////////////////////////////////////////////

  static final int PRIME = 31;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Pair
  // ~~~~~ ~~~~
  /**
   ** Stores extended attribute named-value <code>Pair</code>s of an
   ** <code>Entity</code>.
   */
  public static class Pair implements Resource<Pair> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an new attribute named-value <code>Pair</code> for an
     ** <code>Entity</code> with the specified properties.
     **
     ** @param  name             the name of the named-value <code>Pair</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value of the named-value <code>Pair</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    @JsonCreator
    public Pair(final @JsonProperty(value="name") String name, final @JsonProperty(value="value") String value) {
      // ensure inheritance
      super();

      // initialize instance
      this.name  = name;
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the named-value <code>Pair</code> attribute.
     **
     ** @param  value            the name of the named-value <code>Pair</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Pair</code> attribute to allow
     **                          method chaining.
     **                          <br>
     **                          Possible object is <code>Extended</code>.
     */
    public final Pair name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the of the named-value <code>Pair</code> attribute.
     **
     ** @return                  the name of the named-value <code>Pair</code>
     **                          attribute.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the value of the named-value <code>Pair</code>.
     **
     ** @param  value            the value of the named-value <code>Pair</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Pair</code> attribute to allow
     **                          method chaining.
     **                          <br>
     **                          Possible object is <code>Pair</code>.
     */
    public final Pair value(final String value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the named-value <code>Pair</code>
     **
     ** @return                  the value of the named-value <code>Pair</code>
     **                          attribute.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String value() {
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
      int result = (this.name != null ? this.name.hashCode() : 0);
      result = PRIME * (this.value != null ? this.value.hashCode() : 0);
      return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Pair</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Pair</code>s may be different even though they contain the same
     ** set of names with the same values, but in a different order.
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

      final Pair that = (Pair)other;
      if (this.name != null ? !this.name.equals(that.name) : that.name != null)
        return false;

      return !(this.value != null ? !this.value.equals(that.value) : that.value != null);
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
      builder.append("\"value\":\"").append(this.value).append("\"");
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
   ** Constructs an empty <code>Entity</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  Entity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   quote
  /**
   ** Constructs a tag-value pair.
   **
   ** @param  tag                the name of the JSON attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the JSON attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the quoted string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static String quote(final String tag, final String value) {
    return "\"" + tag + "\":\"" + value + "\"";
  }
}