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

    File        :   RandomWordFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RandomWordFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.word;

import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.platform.captcha.core.type.Limit;

////////////////////////////////////////////////////////////////////////////////
// class RandomWordFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RandomWordFactory implements WordFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String alphabet = "absdegkmnopwx23456789";;

  private Limit    length   = new Limit();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RandomWordFactory</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected RandomWordFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthLower
  /**
   ** Sets the lower limit for the length of challenge text to generate.
   **
   ** @param  value              the lower limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>RandomWordFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomWordFactory</code>.
   */
  public RandomWordFactory lengthLower(final Integer value) {
    this.length.lower(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthLower
  /**
   ** Return the lower limit of the font size of challenge text to generate.
   **
   ** @return                    the lower limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer lengthLower() {
    return this.length.lower();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthUpper
  /**
   ** Sets the upper limit for the length of challenge text to generate.
   **
   ** @param  value              the upper limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>RandomWordFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomWordFactory</code>.
   */
  public RandomWordFactory lengthUpper(final Integer value) {
    this.length.upper(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthUpper
  /**
   ** Return the upper limit of the font size of challenge text to generate.
   **
   ** @return                    the upper limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer lengthUpper() {
    return this.length.upper();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   length
  /**
   ** Sets the limits for the length of challenge text to generate.
   **
   ** @param  value              the limits for the length of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   **
   ** @return                    the <code>RandomWordFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomWordFactory</code>.
   */
  public RandomWordFactory length(final Limit value) {
    this.length = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   length
  /**
   ** Return the limits for the length of challenge text to generate.
   **
   ** @return                    the limits for the length of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   */
  public Limit length() {
    return this.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   alphabet
  /**
   ** Set the alphabet the letters randomly choose from.
   **
   ** @param  value              the alphabet the letters randomly choose from.
   **                            <br>
   **                            Allowed object is @{link String}.
   **
   ** @return                    the <code>RandomWordFactory</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomWordFactory</code>.
   */
  public RandomWordFactory alphabet(final String value) {
    this.alphabet = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next (WordFactory)
  /**
   ** Returns a random character sequence from an alphabet.
   **
   ** @return                    a random character sequence from an alphabet.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String next() {
    final int           d = this.length.upper() - this.length.lower();
    final int           l = this.length.lower() + (d > 0 ? Digester.instance.nextInt(d) : 0);
    final StringBuilder b = new StringBuilder();
    for (int i = 0; i < l; i++) {
      int j = Digester.instance.nextInt(this.alphabet.length());
      b.append(this.alphabet.charAt(j));
    }
    return b.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RandomWordFactory</code>.
   **
   ** @return                    the created <code>RandomWordFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomWordFactory</code>.
   */
  public static RandomWordFactory build() {
    return new RandomWordFactory();
  }
}