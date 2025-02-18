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

    File        :   MarbleRippleFilterFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MarbleRippleFilterFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.filter;

import java.util.List;
import java.util.ArrayList;

import java.awt.image.BufferedImageOp;

import oracle.iam.service.captcha.core.filter.library.MarbleImageOp;

////////////////////////////////////////////////////////////////////////////////
// class MarbleRippleFilterFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DoubleRippleFilterFactory</code> applies a ripple filter on a
 ** {@link BufferedImageOp} leveraging {@link MarbleImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// https://github.com/ppiastucki/patchca/blob/master/patchca/src/org/patchca/filter/predefined/MarbleRippleFilterFactory.java
public class MarbleRippleFilterFactory extends RippleFilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected MarbleImageOp ripple = MarbleImageOp.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MarbleRippleFilterFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private MarbleRippleFilterFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before (overridden)
  /**
   ** Returns the collection of operations to apply on a {@link BufferedImageOp}
   ** <b>before</b> the intended operations are applied.
   **
   ** @return                    the collection of operations to apply on a
   **                            {@link BufferedImageOp} <b>before</b> the
   **                            intended operations.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link BufferedImageOp}.
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
   ** Factory method to create an <code>MarbleRippleFilterFactory</code>.
   **
   ** @return                    the created
   **                            <code>MarbleRippleFilterFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>MarbleRippleFilterFactory</code>.
   */
  public static MarbleRippleFilterFactory build() {
    return new MarbleRippleFilterFactory();
  }
}