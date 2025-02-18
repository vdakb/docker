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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Captcha Platform Feature

    File        :   Filter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Filter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha.type;

import oracle.iam.platform.core.captcha.color.Space;

import oracle.iam.platform.core.captcha.filter.FilterFactory;
import oracle.iam.platform.core.captcha.filter.DiffuseRippleFilterFactory;

////////////////////////////////////////////////////////////////////////////////
// class Filter
// ~~~~~ ~~~~~~
/**
 ** Bean implementation for managing filter effects of Visual Captcha Service
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Filter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The default color used by visual captcha (lightBlue). */
  private static final FilterFactory danger = DiffuseRippleFilterFactory.build();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  FilterFactory[] factory = {danger};

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Filter</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Filter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   factory
  /**
   ** Sets the filter properties of challenge text to generate.
   **
   ** @param  value              the filter properties of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is array of
   **                            {@link FilterFactory}.
   **
   ** @return                    the <code>Filter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Filter</code>.
   */
  public Filter factory(final FilterFactory[] value) {
    this.factory = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   factory
  /**
   ** Return the filter factories to be applied on challenge text.
   **
   ** @return                    the filter factories to be applied on challenge
   **                            text.
   **                            <br>
   **                            Possible object is array of
   **                            {@link FilterFactory}.
   */
  public FilterFactory[] factory() {
    return this.factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   factory
  /**
   ** Returns the foreground colors at the specified index used by visual
   ** captcha.
   **
   ** @param  index              the index for the desired
   **                            {@link FilterFactory}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the foreground colors at the specified index
   **                            used by visual captcha.
   **                            <br>
   **                            Possible object is {@link FilterFactory}.
   */
  public final FilterFactory factory(int index) {
    if (index < 0)
      index = 0;

    return this.factory == null ? danger : index > this.factory.length ? this.factory[this.factory.length - 1] : this.factory[index];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Filter</code> configuration populated
   ** with the default values.
   **
   ** @return                    an newly created instance of
   **                            <code>Filter</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code>.
   */
  public static Filter build() {
    return new Filter();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode
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
    return this.factory != null ? this.factory.hashCode() : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Renderer</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Renderer</code>s may be different even though they contain the same
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

    final Filter that = (Filter)other;
    return (this.factory != null ? !this.factory.equals(that.factory) : that.factory != null);
  }
}