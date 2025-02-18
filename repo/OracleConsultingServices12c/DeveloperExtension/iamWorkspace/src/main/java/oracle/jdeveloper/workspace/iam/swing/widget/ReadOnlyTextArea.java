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

    File        :   ReadOnlyTextArea.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ReadOnlyTextArea.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;

import javax.swing.JTextArea;

import javax.swing.text.Caret;

////////////////////////////////////////////////////////////////////////////////
// class ReadOnlyTextArea
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** An implementation of a text area that cannot be modified.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ReadOnlyTextArea extends JTextArea {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5000628543203260077")
  private static final long serialVersionUID = 7942115561194548586L;

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
  public ReadOnlyTextArea() {
    // ensure inheritance
    this(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new text area with.
   **
   ** @param  border             <code>true</code> if the text area has to have
   **                            a border.
   */
  public ReadOnlyTextArea(final boolean border) {
    // ensure inheritance
    super();

    // initialize instance
    setLineWrap(true);
    setWrapStyleWord(true);
    setEditable(false);
    setOpaque(true);

    if (!border) {
      setBorder(null);
    }

    addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        Caret caret = getCaret();
        if (caret != null) {
          caret.setVisible(true);
        }
      }
    });
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setText (overridden)
  /**
   ** Sets the text of this <code>TextComponent</code> to the specified text.
   ** <br>
   ** If the text is <code>null</code> or empty, has the effect of simply
   ** deleting the old text. When text has been inserted, the resulting caret
   ** location is always resetted to <code>0</code>.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** That text is not a bound property, so no <code>PropertyChangeEvent</code>
   ** is fired when it changes. To listen for changes to the text, use
   ** <code>DocumentListener</code>.
   **
   ** @param  text               the new text to be set.
   */
  @Override
  public void setText(final String text) {
    // ensure inheritance
    super.setText(text);
    getAccessibleContext().setAccessibleDescription(text);
    // rest the caret position
    setCaretPosition(0);
  }
}