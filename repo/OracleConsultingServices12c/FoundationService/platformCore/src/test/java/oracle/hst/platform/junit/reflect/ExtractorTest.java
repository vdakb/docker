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

    File        :   ExtractorTest.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExtractorTest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.junit.reflect;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

import java.time.Instant;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

import org.junit.Test;
import org.junit.Assert;

import org.junit.runner.JUnitCore;

import oracle.hst.platform.core.reflect.Extractor;

import oracle.hst.platform.core.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class ExtractorTest
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Example use of value extraction using Reflection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExtractorTest {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class MyEntity
  // ~~~~~ ~~~~~~~~
  /**
   ** The test subject.
   ** <p>
   ** Some remarks:
   ** <br>
   ** Fluent interfaces for POJO's like this returning the current instatnce
   ** from any setter the POJO provides. This allows to chain method to
   ** initialize an instance of that POJO.
   ** <br>
   ** If reflection comes in the shape such designs raise issues to discover and
   ** distinct between setters and getters; especially if BeanInfo and
   ** BeanInfo.getPropertyDescriptors() will be used to figure out method the
   ** inject or extract property values.
   ** <p>
   ** The only way how to get refelction/introspection to work is to prefix each
   ** method either with <code>set</code> or <code>get/is</code>.
   ** <p>
   ** <b>Note</b>:
   ** Be careful if JSF and/or Expressions are used somehow.
   ** <br>
   ** In such technologies, fluent interfaces are <b>not</b> applicable because
   ** setters are <b>not</b> allowed to have return values.
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
    // Method: setId
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
    public final MyEntity setId(final Long value) {
      this.id = value;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   getId

    /**
     ** Returns the <code>id</code> property of the <code>MyEntity</code>.
     **
     ** @return                  the <code>id</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link Long}.
     */
    public final Long getId() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
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
    public final void setName(final String value) {
      this.name = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getName
    /**
     ** Returns the <code>name</code> property of the <code>MyEntity</code>.
     **
     ** @return                  the <code>name</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String getName() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setVersion
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
    public final MyEntity setVersion(final Long value) {
      this.version = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getVersion
    /**
     ** Returns the <code>version</code> property of the
     ** <code>MyEntity</code>.
     **
     ** @return                  the <code>version</code> property of the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@Link Long}.
     */
    public final Long getVersion() {
      return this.version;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setCreatedBy
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
    public final MyEntity setCreatedBy(final String value) {
      this.createdBy = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getCreatedBy
    /**
     ** Returns the property of an identity whom created the
     ** <code>MyEntity</code>.
     **
     ** @return                  the property of an identity who created the
     **                          <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String getCreatedBy() {
      return this.createdBy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setCreatedOn
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
    public final MyEntity setCreatedOn(final Instant value) {
      this.createdOn = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getCreatedOn
    /**
     ** Returns the property of the time the <code>MyEntity</code> was created.
     **
     ** @return                  the property of the time the
     **                          <code>MyEntity</code> was created.
     **                          <br>
     **                          Possible object is {@link Instant}.
     */
    public final Instant getCreatedOn() {
      return this.createdOn;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setUpdatedBy
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
    public final MyEntity setUpdatedBy(final String value) {
      this.updatedBy = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getUpdatedBy
    /**
     ** Returns the property of an identity whom last recently modified the
     ** <code>MyEntity</code>.
     **
     ** @return                  the property of an identity whom last
     **                          recently modified the <code>MyEntity</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String getUpdatedBy() {
      return this.updatedBy;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setUpdatedOn
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
    public final MyEntity setUpdatedOn(final Instant value) {
      this.updatedOn = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getUpdatedOn
    /**
     ** Returns he property <code>updatedOn</code> to the time the entity was
     ** last recently modified.
     **
     ** @return                  the property <code>updatedOn</code> to the
     **                          time the entity was last recently modified.
     **                          <br>
     **                          Possible object is {@link Instant}.
     */
    public final Instant getUpdatedOn() {
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
   ** Constructs a <code>ExtractorTest</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExtractorTest() {
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
    final String[] parameter = { ExtractorTest.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRefelction
  /**
   ** Test to native refelection.
   */
  @Test
  public void testRefelction() {
    final MyEntity entity = new MyEntity();
    entity.setName("Ali Baba");
    
    BeanInfo beanInfo = null;
    try {
      beanInfo = Introspector.getBeanInfo(MyEntity.class);
    }
    catch (Throwable t) {
      Assert.fail(t.getLocalizedMessage());
    }
    Assert.assertTrue(beanInfo != null);

    final PropertyDescriptor[] descriptor = beanInfo.getPropertyDescriptors();
    Assert.assertTrue(descriptor.length > 0);

    for (PropertyDescriptor cursor : descriptor) {
      if (cursor.getReadMethod() == null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testExtractName
  /**
   ** Test to extract the property name only.
   */
  @Test
  public void testExtractName() {
    final MyEntity entity = new MyEntity();
    entity.setName("Ali Baba");
    Assert.assertEquals("Ali Baba", entity.getName());
    Assert.assertTrue(StringUtility.startsWith(entity.getName(), "Ali"));

    final Map<Extractor<MyEntity>, Object> criteria = new HashMap<>();
    criteria.put(MyEntity::getName, StringUtility.startsWith(entity.getName(), "Ali"));
    
    final Map<String, Object> required = new HashMap<>();
    criteria.forEach((extractor, value) -> required.put(extractor.propertyName(), value));
    Assert.assertTrue((Boolean)required.get("name"));
  }
}