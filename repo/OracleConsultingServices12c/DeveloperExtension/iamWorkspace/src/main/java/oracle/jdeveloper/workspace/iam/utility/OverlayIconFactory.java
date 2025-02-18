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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   OverlayIconFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OverlayIconFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import javax.swing.ImageIcon;

////////////////////////////////////////////////////////////////////////////////
// class OverlayIconFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A helper class to contain icons for the overlayable components.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OverlayIconFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ATTENTION = "overlay/attention.png";
  public static final String CORRECT   = "overlay/correct.png";
  public static final String ERROR     = "overlay/error.png";
  public static final String FILTER    = "overlay/filter.png";
  public static final String INFO      = "overlay/info.png";
  public static final String LINK      = "overlay/reference.png";
  public static final String LOCKED    = "overlay/locked.png";
  public static final String QUESTION  = "overlay/question.png";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Returns {@link ImageIcon} by passing a relative image file path.
   ** <p>
   ** Please note, create will print out error message to stderr if image is not
   ** found. The reason we did so is because we want you to make sure all image
   ** files are there in your application. If you ever see the error message,
   ** you should correct it before shipping the product.
   ** <p>
   ** The specified file path must be relative to this class
   **
   ** @param  fileName           the relative file name to this
   **                            <code>Class</code>.
   **
   ** @return                    the {@link ImageIcon}
   */
  public static ImageIcon create(final String fileName) {
    return (fileName != null) ? IconFactory.create(OverlayIconFactory.class, fileName) : null;
  }
}