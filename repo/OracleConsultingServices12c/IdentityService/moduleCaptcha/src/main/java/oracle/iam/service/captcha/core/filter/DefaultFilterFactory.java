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

    File        :   DefaultFilterFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DefaultFilterFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.filter;

import java.util.List;
import java.util.Arrays;

import java.awt.image.BufferedImageOp;

////////////////////////////////////////////////////////////////////////////////
// class DefaultFilterFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractFilterFactory</code> applies a collection of operations on a
 ** {@code BufferedImage} leveraging {@link BufferedImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DefaultFilterFactory extends AbstractFilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<BufferedImageOp> filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DefaultFilterFactory</code> that applies the specified
   ** operation onto an image.
   **
   ** @param  filter             the collection of operations to apply onto a
   **                            {@link BufferedImage}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link BufferedImageOp}.
   */
  private DefaultFilterFactory(final List<BufferedImageOp> filter) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (AbstractFilterFactory)
  /**
   ** Returns the collection of operations to apply on a {@code BufferedImage}.
   **
   ** @return                    the collection of operations to apply on a
   **                            {@code BufferedImage}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link BufferedImageOp}.
   */
  @Override
  public final List<BufferedImageOp> filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DefaultFilterFactory</code>.
   **
   ** @param  filter             the operation to apply onto a
   **                            {@code BufferedImage}.
   **                            <br>
   **                            Allowed object is array of
   **                            {@link BufferedImageOp}.
   **
   ** @return                    the created
   **                            <code>DefaultFilterFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DefaultFilterFactory</code>.
   */
  public static DefaultFilterFactory build(final BufferedImageOp... filter) {
    return new DefaultFilterFactory(Arrays.asList(filter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DefaultFilterFactory</code>.
   **
   ** @param  filter             the collection of operations to apply onto a
   **                            {@code BufferedImage}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link BufferedImageOp}.
   **
   ** @return                    the created
   **                            <code>DefaultFilterFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DefaultFilterFactory</code>.
   */
  public static DefaultFilterFactory build(final List<BufferedImageOp> filter) {
    return new DefaultFilterFactory(filter);
  }
}