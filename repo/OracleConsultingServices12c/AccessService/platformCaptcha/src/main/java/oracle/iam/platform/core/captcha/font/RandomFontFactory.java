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

    File        :   RandomFontFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RandomFontFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.font;

import java.util.List;
import java.util.ArrayList;

import java.awt.Font;

import oracle.iam.config.captcha.type.Limit;

import oracle.iam.platform.core.captcha.Digester;

////////////////////////////////////////////////////////////////////////////////
// class RandomFontFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>RandomFontFactory</code> returns the next pseudorandom, uniformly
 ** distributed {@link Font} value between <code>lower</code> (inclusive) and
 ** the specified <code>upper</code> (exclusive) limit value {@link Font},
 ** drawn from the random number generator's sequence.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// https://github.com/ppiastucki/patchca/blob/master/patchca/src/org/patchca/font/RandomFontFactory.java
public class RandomFontFactory implements FontFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected List<String> family = new ArrayList<String>();
  protected Limit        size   = Limit.build(32, 32);
  protected boolean      style  = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RandomFontFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private RandomFontFactory() {
    // ensure inheritance
    super();

    // initialize intstance attributes
    this.family.add("Verdana");
    this.family.add("Tahoma");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   family
  /**
   ** Set the {@link Font} families from which one will be randomly choosen.
   **
   ** @param  value              the {@link Font} families from which one will
   **                            be randomly choosen.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>RandomFontFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomFontFactory</code>.
   */
  public RandomFontFactory family(final List<String> value) {
    this.family = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lower
  /**
   ** Set the lower limit for the {@link Font} size from which one will be
   ** randomly choosen.
   **
   ** @param  value              the lower limit for the {@link Font} size from
   **                            which one will be randomly choosen.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>RandomFontFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomFontFactory</code>.
   */
  public RandomFontFactory lower(final int value) {
    this.size.lower(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upper
  /**
   ** Set the upper limit for the {@link Font} size from which one will be
   ** randomly choosen.
   **
   ** @param  value              the upper limit for the {@link Font} size from
   **                            which one will be randomly choosen.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>RandomFontFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomFontFactory</code>.
   */
  public RandomFontFactory upper(final int value) {
    this.size.upper(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   style
  /**
   ** Set <code>true</code> if the {@link Font} style is randomly choosen.
   **
   ** @param  value              <code>true</code> if the {@link Font} style is
   **                            randomly choosen; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>RandomFontFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomFontFactory</code>.
   */
  public RandomFontFactory style(final boolean value) {
    this.style = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   avarage (FontFactory)
  /**
   ** Returns the avarage size of the {@link Font} from this factory.
   **
   ** @return                    the the avarage size of the {@link Font} from
   **                            this factory.
   **                            <br>
   **                            Possible object is {@link Font}.
   */
  @Override
  public final int avarage() {
    return (this.size.lower() +(this.size.upper() - this.size.lower() / 2)) / 2;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get (FontFactory)
  /**
   ** Returns the {@link Font} mutation at the specified <code>step</code> for
   ** a gradient element.
   **
   ** @param  index              the index of the {@link Font}.
   **                            <br>
   **                            Allowed object is {@link Font}.
   **
   ** @return                    the step {@link Font} in the gradient.
   **                            <br>
   **                            Possible object is {@link Font}.
   */
  @Override
  public Font get(final int index) {
    final String  family = this.family.get(Digester.instance.nextInt(this.family.size()));
    final boolean bold   = this.style && (Digester.instance.nextInt(1) == 1);
    int           size   = this.size.lower();
    if (this.size.upper() - this.size.lower() > 0) {
      size += Digester.instance.nextInt(this.size.upper()  - this.size.lower());
    }
    return new Font(family, bold ? Font.BOLD : Font.PLAIN, size);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RandomFontFactory</code>.
   **
   ** @return                    the created <code>RandomFontFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomFontFactory</code>.
   */
  public static RandomFontFactory build() {
    return new RandomFontFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RandomFontFactory</code>.
   **
   ** @param  family             the {@link Font} families from which one will
   **                            be randomly choosen.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the created <code>RandomFontFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomFontFactory</code>.
   */
  public static RandomFontFactory build(final List<String> family) {
    return new RandomFontFactory().family(family);
  }
}