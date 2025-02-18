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

    File        :   Marble.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Marble.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha.type;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Marble
// ~~~~~ ~~~~~~
/**
 ** Bean implementation for managing marble effects of Visual Captcha Service
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marble implements Serializable {
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-152947968616006929")
  private static final long serialVersionUID = 2207659559783233767L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private double scale      = 15.0;
  private double amount     = 1.1;
  private double turbulence = 6.2;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Marble</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Marble() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scale
  /**
   ** Set the scale property.
   **
   ** @param  value              the scale property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Marble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Marble</code>.
   */
  public Marble scale(final double value) {
    this.scale = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scale
  /**
   ** Returns the scale property.
   **
   ** @return                    the scale property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double scale() {
    return this.scale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amount
  /**
   ** Set the amount property.
   **
   ** @param  value              the amount property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Marble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Marble</code>.
   */
  public Marble amount(final double value) {
    this.amount = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amount
  /**
   ** Returns the amount property.
   **
   ** @return                    the amount property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double amount() {
    return this.amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   turbulence
  /**
   ** Set the turbulence property.
   **
   ** @param  value              the turbulence property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Marble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Marble</code>.
   */
  public Marble turbulence(final double value) {
    this.turbulence = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   turbulence
  /**
   ** Returns the turbulence property.
   **
   ** @return                    the turbulence property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double turbulence() {
    return this.turbulence;
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
    int result = (int)this.scale;
    result = 31 * result + (int)this.amount;
    result = 31 * result + (int)this.turbulence;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Marble</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Marble</code>s may be different even though they contain the same
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

    final Marble that = (Marble)other;
    if (!(this.scale == that.scale))
      return false;

    if (!(this.amount == that.amount))
      return false;

    return (this.turbulence == that.turbulence);
  }
}