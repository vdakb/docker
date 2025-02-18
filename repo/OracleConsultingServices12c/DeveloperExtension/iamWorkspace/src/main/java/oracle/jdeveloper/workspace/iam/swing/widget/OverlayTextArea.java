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

    File        :   OverlayTextArea.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OverlayTextArea.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.JTextArea;

import javax.swing.text.Document;

import oracle.jdeveloper.workspace.iam.swing.OverlayUtility;

////////////////////////////////////////////////////////////////////////////////
// class OverlayTextArea
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An implementation of a text area that can be overlayed to another component.
 */
public class OverlayTextArea extends JTextArea {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3891539804499700056")
  private static final long serialVersionUID = 6937115096368760887L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text area.
   ** <p>
   ** A default model is set, the initial string is <code>null</code>, and
   ** rows/columns are set to <code>0</code>.
   */
  public OverlayTextArea() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text area with the specified text displayed.
   ** <p>
   ** A default model is set and rows/columns are set to <code>0</code>.
   **
   ** @param  text               the text to be displayed, or <code>null</code>.
   */
  public OverlayTextArea(final String text) {
    // ensure inheritance
    super(text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new empty text area with the specified number of rows and
   ** columns.
   ** <p>
   ** A default model is createdand the initial string is <code>null</code>.
   **
   ** @param  rows               the number of rows &gt;= 0
   ** @param  columns            the number of columns &gt;= 0
   **
   ** @throws IllegalArgumentException if the rows or columns arguments are
   **                                  negative.
   */
  public OverlayTextArea(final int rows, final int columns) {
    // ensure inheritance
    super(rows, columns);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text area with the specified text and number of rows and
   ** columns.
   ** <p>
   ** A default model is created.
   **
   ** @param  text               the text to be displayed, or <code>null</code>.
   ** @param  rows               the number of rows &gt;= 0
   ** @param  columns            the number of columns &gt;= 0
   **
   ** @throws IllegalArgumentException if the rows or columns arguments are
   **                                  negative.
   */
  public OverlayTextArea(final String text, final int rows, final int columns) {
    // ensure inheritance
    super(text, rows, columns);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text area with the given document model, and defaults for
   ** all of the other arguments (text--&gt; null, rows --&gt; 0, columns --&gt; 0).
   **
   ** @param  document           the {@link Document}model to use, or create a
   **                            default one if <code>null</code>.
   */
  public OverlayTextArea(final Document document) {
    // ensure inheritance
    super(document);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text area with the specified number of rows and columns,
   ** and the given model.
   **
   ** @param  document           the {@link Document}model to use, or create a
   **                            default one if <code>null</code>.
   ** @param  text               the text to be displayed, or <code>null</code>.
   ** @param  rows               the number of rows &gt;= 0
   ** @param  columns            the number of columns &gt;= 0
   **
   ** @throws IllegalArgumentException if the rows or columns arguments are
   **                                  negative.
   */
  public OverlayTextArea(final Document document, final String text, final int rows, final int columns) {
    // ensure inheritance
    super(document, text, rows, columns);
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