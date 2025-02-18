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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   CreateFolderDialog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CreateFolderDialog.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.ide.Ide;

import oracle.jdeveloper.workspace.iam.swing.widget.FixedTextField;
import oracle.jdeveloper.workspace.iam.swing.widget.FixedLengthDocument;

////////////////////////////////////////////////////////////////////////////////
// class NameInputDialog
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the information about a name for a
 ** particular resource in a Metadata Service.
 ** <p>
 ** The class is package protected to ensure that only the appropriate command
 ** is able to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class NameInputDialog extends JEWTDialog {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4409291444700116957")
  private static final long      serialVersionUID = -1822488542894799700L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final FixedTextField name = FixedTextField.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>NameInputDialog</code> that use the specified
   ** string <code>title</code> for the title text of the dialog to display.
   **
   ** @param  title              for the title text of the dialog to display.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected NameInputDialog(final String title) {
    // ensure inheritance
    super(Ide.getMainWindow(), title, JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL);

    // initialize instance
    this.name.setDocument(FixedLengthDocument.build());
    this.name.getDocument().addDocumentListener(
      new DocumentListener() {
        public void removeUpdate(final DocumentEvent event) {
          setOKButtonEnabled(NameInputDialog.this.name.getText().length() > 0);
        }
        public void insertUpdate(final DocumentEvent event) {
          setOKButtonEnabled(NameInputDialog.this.name.getText().length() > 0);
        }
        public void changedUpdate(final DocumentEvent event) {
          setOKButtonEnabled(NameInputDialog.this.name.getText().length() > 0);
        }
      }
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the text a user entered in the input component.
   **
   ** @param  value              the text a user entered in the input component.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void value(final String value) {
    this.name.setText(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the text a user entered in the input component.
   **
   ** @return                    the text a user entered in the input component.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String value() {
    return this.name.getText().trim();
  }
}