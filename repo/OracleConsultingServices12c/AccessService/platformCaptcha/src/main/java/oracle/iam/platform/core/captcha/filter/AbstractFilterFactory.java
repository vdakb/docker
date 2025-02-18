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

    File        :   AbstractFilterFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractFilterFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.filter;

import java.util.List;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractFilterFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractFilterFactory</code> applies a collection of operations on a
 ** {@link BufferedImage} leveraging {@link BufferedImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractFilterFactory implements FilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractFilterFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractFilterFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the collection of operations to apply onto a
   ** {@link BufferedImage}.
   **
   ** @return                    the collection of operations to apply onto a
   **                            {@link BufferedImage}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link BufferedImage}.
   */
  protected abstract List<BufferedImageOp> filter();

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (FilterFactory)
  /**
   ** Applies a filter onto the given {@link BufferedImage}.
   **
   ** @param  source             the {@link BufferedImage} to manipulate.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   **
   ** @return                    the manipulated {@link BufferedImage}.
   **                            <br>
   **                            Possible object is {@link BufferedImage}.
   */
  @Override
  public final BufferedImage apply(BufferedImage source) {
    BufferedImage plate = source;
    for (BufferedImageOp filter : filter())
      plate = filter.filter(plate, null);

    final int x = (source.getWidth()  - plate.getWidth())  / 2;
    final int y = (source.getHeight() - plate.getHeight()) / 2;
    source = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
    source.getGraphics().drawImage(plate, x, y, null);
    return source;
  }
}