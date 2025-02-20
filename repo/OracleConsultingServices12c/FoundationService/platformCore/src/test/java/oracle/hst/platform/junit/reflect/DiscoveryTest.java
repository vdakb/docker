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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   DiscoveryTest.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DiscoveryTest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.junit.reflect;

import java.util.Objects;

import java.time.Instant;

import org.junit.Test;
import org.junit.Assert;

import org.junit.runner.JUnitCore;

////////////////////////////////////////////////////////////////////////////////
// class DiscoveryTest
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Example use of value extraction using Reflection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DiscoveryTest {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class MyEntity
  // ~~~~~ ~~~~~~~~
  /**
   ** The test subject.
   */
  private static class MyEntity {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
    
    private Long    id;
    private String  name;
    private Long    version;
    private String  createdBy;
    private Instant createdOn;
    private String  updatedBy;
    private Instant updatedOn;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>MyEntity</code> database resource that allows
     ** use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public MyEntity() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Set the <code>id</code> property of the <code>MyEntity</code>.
     **
     ** @param  value            the <code>id</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Allowed object is {@link Long}.
     **
     ** @return                  the <code>MyEntity</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MyEntity</code> of
     **                          type <code>E</code>.
     */
    public final MyEntity id(final Long value) {
      this.id = value;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   id

    /**
     ** Returns the <code>id</code> property of the <code>MyEntity</code>.
     **
     ** @return                  the <code>id</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link Long}.
     */
    public final Long id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the <code>name</code> property of the <code>MyEntity</code>.
     **
     ** @param  value            the <code>name</code> property of the
     **                          <code>MyEntity</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MyEntity</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MyEntity</code>.
     */
    public final void name(final String value) {
      this.name = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the <code>name</code> property of the <code>MyEntity</code>.
     **
     ** @return                  the <code>name</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: version
    /**
     ** Set the <code>version</code> property of the <code>MyEntity</code>.
     **
     ** @param  value            the <code>version</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Allowed object is {@Link Long}.
     **
     ** @return                  the <code>MyEntity</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MyEntity</code>.
     */
    public final MyEntity version(final Long value) {
      this.version = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: version
    /**
     ** Returns the <code>version</code> property of the
     ** <code>MyEntity</code>.
     **
     ** @return                  the <code>version</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@Link Long}.
     */
    public final Long version() {
      return this.version;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createdBy
    /**
     ** Set the property of an identity whom have created the
     ** <code>MyEntity</code>.
     **
     ** @param  value            the property of an identity who created the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MyEntity</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MyEntity</code>
     **                          of type <code>MyEntity</code>.
     */
    public final MyEntity createdBy(final String value) {
      this.createdBy = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createdBy
    /**
     ** Returns the property of an identity whom created the
     ** <code>MyEntity</code>.
     **
     ** @return                  the property of an identity who created the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String createdBy() {
      return this.createdBy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createdOn
    /**
     ** Set the property <code>createdOn</code> to the time the entity was
     ** created.
     **
     ** @param  value            the property <code>createdOn</code> to the
     **                          time the entity was created.
     **                          <br>
     **                          Allowed object is {@link Instant}.
     **
     ** @return                  the <code>MyEntity</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MyEntity</code>
     **                          of type <code>MyEntity</code>.
     */
    public final MyEntity createdOn(final Instant value) {
      this.createdOn = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createdOn
    /**
     ** Returns the property of the time the <code>MyEntity</code> was created.
     **
     ** @return                  the property of the time the
     **                          <code>MyEntity</code> was created.
     **                          <br>
     **                          Possible object is {@link Instant}.
     */
    public final Instant createdOn() {
      return this.createdOn;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updatedBy
    /**
     ** Set the property of an identity whom last recently modified the
     ** <code>MyEntity</code>.
     **
     ** @param  value            the property of an identity whom last
     **                          recently modified the <code>MyEntity</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MyEntity</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MyEntity</code>
     **                          of type <code>MyEntity</code>.
     */
    public final MyEntity updatedBy(final String value) {
      this.updatedBy = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updatedBy
    /**
     ** Returns the property of an identity whom last recently modified the
     ** <code>MyEntity</code>.
     **
     ** @return                  the property of an identity whom last
     **                          recently modified the <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String updatedBy() {
      return this.updatedBy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updatedOn
    /**
     ** Set the property <code>updatedOn</code> to the time the entity was
     ** last recently modified.
     **
     ** @param  value            the property <code>updatedOn</code> to the
     **                          time the entity was last recently modified.
     **                          <br>
     **                          Allowed object is {@link Instant}.
     **
     ** @return                  the <code>MyEntity</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MyEntity</code>
     **                          of type <code>MyEntity</code>.
     */
    public final MyEntity updatedOn(final Instant value) {
      this.updatedOn = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updatedOn
    /**
     ** Returns he property <code>updatedOn</code> to the time the entity was
     ** last recently modified.
     **
     ** @return                  the property <code>updatedOn</code> to the
     **                          time the entity was last recently modified.
     **                          <br>
     **                          Possible object is {@link Instant}.
     */
    public final Instant updatedOn() {
      return this.updatedOn;
    }

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
     ** The implementation use the identifier of the persisted entity if available
     ** only. This is sufficient because the identifier is the primary key of the
     ** entity.
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public final int hashCode() {
      return (this.id != null) ? Objects.hash(this.id) : super.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Entities</code> are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Entities</code>s may be different even though they contain the
     ** same set of names with the same values, but in a different order.
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
    public final boolean equals(final Object other) {
      return (this.id != null && getClass().isInstance(other) && other.getClass().isInstance(this)) ? this.id.equals(((MyEntity)other).id) : (other == this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DiscoveryTest</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DiscoveryTest() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = { DiscoveryTest.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDummy
  /**
   ** Test ...
   */
  @Test
  public void testDummy() {
  }
}