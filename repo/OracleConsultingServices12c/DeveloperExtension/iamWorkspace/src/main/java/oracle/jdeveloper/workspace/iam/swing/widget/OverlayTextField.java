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

    File        :   OverlayTextField.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OverlayTextField.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.JTextField;

import javax.swing.text.Document;

import oracle.jdeveloper.workspace.iam.swing.OverlayUtility;

////////////////////////////////////////////////////////////////////////////////
// class OverlayCheckBox
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An implementation of a text field that can be overlayed to another
 ** component.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OverlayTextField extends JTextField {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1403302501498096088")
  private static final long serialVersionUID = 1342807584964600045L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new empty text field.
   ** <p>
   ** A default model is set, the initial string is <code>null</code>, and the
   ** number of columns is set to <code>0</code>.
   */
  public OverlayTextField() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text fieldinitialized with the specified text.
   ** <p>
   ** A default model is created and the number of columns is <code>0</code>.
   **
   ** @param  text               the text to be displayed, or <code>null</code>.
   */
  public OverlayTextField(final String text) {
    // ensure inheritance
    super(text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new empty text field with the specified number of columns.
   ** <p>
   ** A default model is created and the initial string is set to
   ** <code>null</code>.
   **
   ** @param  columns            the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **
   ** @throws IllegalArgumentException if the columns arguments are negative.
   */
  public OverlayTextField(final int columns) {
    // ensure inheritance
    super(columns);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text field with the specified text and number of columns.
   ** <p>
   ** A default model is created.
   **
   ** @param  text               the text to be displayed, or <code>null</code>.
   ** @param  columns            the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **
   ** @throws IllegalArgumentException if the columns arguments are negative.
   */
  public OverlayTextField(final String text, final int columns) {
    // ensure inheritance
    super(text, columns);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text field that uses the given text storage model and the
   ** given number of columns.
   **
   ** @param  document           the {@link Document}model to use, or create a
   **                            default one if <code>null</code>.
   ** @param  text               the text to be displayed, or <code>null</code>.
   ** @param  columns            the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **
   ** @throws IllegalArgumentException if the columns arguments are negative.
   */
  public OverlayTextField(final Document document, final String text, final int columns) {
    // ensure inheritance
    super(document, text, columns);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repaint (overridden)
  /**
   ** Adds the specified region to the dirty region list if the component is
   ** showing. The component will be repainted after all of the currently
   ** pending events have been dispatched.
   **
   ** @param  tm                 this parameter is not used
   ** @param  x                  the x value of the dirty region
   ** @param  y                  the y value of the dirty region
   ** @param  width              the width of the dirty region
   ** @param  height             the height of the dirty region
   */
  @Override
  public void repaint(long tm, int x, int y, int width, int height) {
    // ensure inheritance
    super.repaint(tm, x, y, width, height);
    // repaint the overlay
    OverlayUtility.repaint(this);
  }
}