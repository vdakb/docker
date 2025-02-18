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

    File        :   ReadOnlyScrollPane.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ReadOnlyScrollPane.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

////////////////////////////////////////////////////////////////////////////////
// class ReadOnlyScrollPane
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An implementation of a scroll pane that contains a readonly text area.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ReadOnlyScrollPane extends JScrollPane {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8121070464508351861")
  private static final long serialVersionUID = 2604659480273470671L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final JTextArea textArea = new ReadOnlyTextArea(true);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty (no viewport view) scoll pane where a horizontal
   ** scrollbar never and a vertical scrollbars appear when needed.
   */
  public ReadOnlyScrollPane() {
    // ensure inheritance
    super();

    // initialize instance
    setViewportView(this.textArea);
    setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   textArea
  /**
   ** Return the embedded text area component.
   **
   ** @return                    the embedded text area component.
   */
  public JTextArea textArea() {
    return this.textArea;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPreferredSize (overridden)
  /**
   ** Sets the preferred size of the embedded <code>TextComponent</code>.
   ** <br>
   ** If <code>preferredSize</code> is <code>null</code>, the UI will be asked
   ** for the preferred size.
   **
   ** @param  dimension          the preferred size of the embedded
   **                            <code>TextComponent</code>.
   */
  @Override
  public void setPreferredSize(final Dimension dimension) {
    this.textArea.setPreferredSize(dimension);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setText
  /**
   ** Sets the text of the embedded <code>TextComponent</code> to the specified
   ** text.
   **
   ** @param  text               the new text to be set.
   ** @param  focus              <code>true</code> if the emebedded text area
   **                            should gain the focus.
   */
  public void setText(final String text, final boolean focus) {
    this.textArea.setText(text);
    if (focus) {
      this.textArea.requestFocusInWindow();
    }
  }
}