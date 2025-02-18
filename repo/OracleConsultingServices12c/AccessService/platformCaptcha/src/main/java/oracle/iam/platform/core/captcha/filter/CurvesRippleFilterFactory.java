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

    File        :   CurvesRippleFilterFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CurvesRippleFilterFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.filter;

import java.util.List;
import java.util.ArrayList;

import java.awt.image.BufferedImageOp;

import oracle.iam.platform.core.captcha.color.ColorFactory;

import oracle.iam.platform.core.captcha.filter.library.CurvesImageOp;

////////////////////////////////////////////////////////////////////////////////
// class CurvesRippleFilterFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>CurvesRippleFilterFactory</code> applies a ripple filter on a
 ** {@code BufferedImage} leveraging {@link CurvesImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CurvesRippleFilterFactory extends RippleFilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final CurvesImageOp ripple = CurvesImageOp.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CurvesRippleFilterFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private CurvesRippleFilterFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Set the {@link ColorFactory} property.
   **
   ** @param  value              the {@link ColorFactory} used for rendering.
   **                            <br>
   **                            Allowed object is {@link ColorFactory}.
   **
   ** @return                    the <code>CurvesRippleFilterFactory</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>CurvesRippleFilterFactory</code>.
   */
  public CurvesRippleFilterFactory color(final ColorFactory value) {
    this.ripple.color(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before (overridden)
  /**
   ** Returns the collection of operations to apply onto a {@code BufferedImage}
   ** <b>before</b> the intended operations are applied.
   **
   ** @return                    the collection of operations to apply on a
   **                            {@code BufferedImage} <b>before</b> the
   **                            intended operations.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@code BufferedImage}.
   */
  @Override
  protected List<BufferedImageOp> before() {
    final List<BufferedImageOp> list = new ArrayList<BufferedImageOp>();
    list.add(this.ripple);
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>CurvesRippleFilterFactory</code>.
   **
   ** @return                    the created
   **                            <code>CurvesRippleFilterFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>CurvesRippleFilterFactory</code>.
   */
  public static CurvesRippleFilterFactory build() {
    return new CurvesRippleFilterFactory();
  }
}