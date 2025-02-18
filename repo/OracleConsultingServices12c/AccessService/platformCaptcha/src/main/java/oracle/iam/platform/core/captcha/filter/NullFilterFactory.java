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

    File        :   NullFilterFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    NullFilterFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.filter;

import java.awt.image.BufferedImage;

////////////////////////////////////////////////////////////////////////////////
// final class NullFilterFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>NullFilterFactory</code> applies nothing onto a {@link BufferedImage}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class NullFilterFactory implements FilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NullFilterFactory</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private NullFilterFactory() {
    super();
  }

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
    return source;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>NullFilterFactory</code>.
   **
   ** @return                    the created <code>NullFilterFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>NullFilterFactory</code>.
   */
  public static NullFilterFactory build() {
    return new NullFilterFactory();
  }
}