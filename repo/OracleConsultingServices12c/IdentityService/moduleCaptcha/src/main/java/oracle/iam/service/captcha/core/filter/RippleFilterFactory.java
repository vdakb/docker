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

    File        :   RippleFilterFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RippleFilterFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.filter;

import java.util.List;
import java.util.ArrayList;

import java.awt.image.BufferedImageOp;

import oracle.iam.service.captcha.core.filter.library.RippleImageOp;

////////////////////////////////////////////////////////////////////////////////
// class RippleFilterFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>RippleFilterFactory</code> applies a filter onto a
 ** {@link BufferedImageOp} leveraging {@link RippleImageOp}s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RippleFilterFactory extends AbstractFilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected List<BufferedImageOp> filter;
  protected RippleImageOp         base   = RippleImageOp.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RippleFilterFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected RippleFilterFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
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
  protected List<BufferedImageOp> before() {
    return new ArrayList<BufferedImageOp>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   after
  /**
   ** Returns the collection of operations to apply on a {@link BufferedImageOp}
   ** <b>after</b> the intended operations are applied.
   **
   ** @return                    the collection of operations to apply on a
   **                            {@link BufferedImageOp} <b>after</b> the
   **                            intended operations.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link BufferedImageOp}.
   */
  protected List<BufferedImageOp> after() {
    return new ArrayList<BufferedImageOp>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the collection of operations to apply on a {@link BufferedImageOp}.
   **
   ** @return                    the collection of operations to apply on a
   **                            {@link BufferedImageOp}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link BufferedImageOp}.
   */
  @Override
  public List<BufferedImageOp> filter() {
    if (this.filter == null) {
      this.filter = new ArrayList<BufferedImageOp>();
      this.filter.addAll(before());
      this.filter.add(this.base);
      this.filter.addAll(after());
    }
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RippleFilterFactory</code>.
   **
   ** @return                    the created <code>RippleFilterFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RippleFilterFactory</code>.
   */
  public static RippleFilterFactory build() {
    return new RippleFilterFactory();
  }
}