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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Filter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Filter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.visual.type;

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
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  Marble   marble   = new Marble();
  Wobble   wobble   = new Wobble();
  Diffusor diffusor = new Diffusor();

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
  // Method:   marble
  /**
   ** Sets the marble filter properties of challenge text to generate.
   **
   ** @param  value              the marble filter properties of challenge text
   **                            to generate.
   **                            <br>
   **                            Allowed object is {@link Marble}.
   **
   ** @return                    the <code>Filter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Filter</code>.
   */
  public Filter marble(final Marble value) {
    this.marble = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marble
  /**
   ** Return the marble filter properties of challenge text to generate.
   **
   ** @return                    the marble filter properties of challenge text
   **                            to generate.
   **                            <br>
   **                            Allowed object is {@link Marble}.
   */
  public Marble marble() {
    return this.marble;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wobble
  /**
   ** Sets the wobble filter properties of challenge text to generate.
   **
   ** @param  value              the wobble filter properties of challenge text
   **                            to generate.
   **                            <br>
   **                            Allowed object is {@link Wobble}.
   **
   ** @return                    the <code>Filter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Filter</code>.
   */
  public Filter wobble(final Wobble value) {
    this.wobble = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wobble
  /**
   ** Return the wobble filter properties of challenge text to generate.
   **
   ** @return                    the wobble filter properties of challenge text
   **                            to generate.
   **                            <br>
   **                            Allowed object is {@link Wobble}.
   */
  public Wobble wobble() {
    return this.wobble;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   diffusor
  /**
   ** Sets the diffusor filter properties of challenge text to generate.
   **
   ** @param  value              the diffusor filter properties of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Diffusor}.
   **
   ** @return                    the <code>Filter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Filter</code>.
   */
  public Filter diffusor(final Diffusor value) {
    this.diffusor = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   diffusor
  /**
   ** Return the diffusor filter properties of challenge text to generate.
   **
   ** @return                    the diffusor filter properties of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Diffusor}.
   */
  public Diffusor diffusor() {
    return this.diffusor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    int result = this.marble != null ? this.marble.hashCode() : 0;
    result = 31 * result + (this.wobble   != null ? this.wobble.hashCode()   : 0);
    result = 31 * result + (this.diffusor != null ? this.diffusor.hashCode() : 0);
    return result;
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
    if (this.marble != null ? !this.marble.equals(that.marble) : that.marble != null)
      return false;

    if (this.wobble != null ? !this.wobble.equals(that.wobble) : that.wobble != null)
      return false;

    if (this.diffusor != null ? !this.diffusor.equals(that.diffusor) : that.diffusor != null)
      return false;

    return true;
  }
}