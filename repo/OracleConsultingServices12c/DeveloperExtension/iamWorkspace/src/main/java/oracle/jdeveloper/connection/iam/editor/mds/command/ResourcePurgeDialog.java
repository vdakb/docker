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

    File        :   ResourcePurgeDialog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ResourcePurgeDialog.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.mds.command;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.bali.ewt.text.NumberTextField;

import oracle.ide.Ide;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ResourcePurgeDialog
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the number of seconds which all
 ** unlabeled documents will be purged if they are older than.
 ** <p>
 ** The class is package protected to ensure that only the appropriate command
 ** is able to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
class ResourcePurgeDialog extends JEWTDialog {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1316325593788391111")
  private static final long serialVersionUID = -8660550704274120895L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final NumberTextField olderThan = new NumberTextField(Long.valueOf(0L));

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourceRenameDialog</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  ResourcePurgeDialog() {
    // ensure inheritance
    super(Ide.getMainWindow(), Bundle.string(Bundle.RESOURCE_PURGE_TITLE), JEWTDialog.BUTTON_OK | JEWTDialog.BUTTON_CANCEL);

    final JPanel panel = new JPanel(new GridBagLayout());
    // put the label on the left side of its display area, centered vertically
    // and allow resizing the component horizontally but not vertically
    panel.add(new JLabel(Bundle.string(Bundle.RESOURCE_PURGE_MESSAGE)), new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));
    // put the label on the left side of its display area, centered vertically
    // and allow resizing the component horizontally but not vertically
    panel.add(new JLabel(Bundle.string(Bundle.RESOURCE_PURGE_LABEL)), new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));
    // put the value field on the left side of its display area, centered
    // vertically and allow resizing the component horizontally but not vertically
    panel.add(this.olderThan, new GridBagConstraints(0, 2, 1, 1, 1.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));
    setContent(panel);
    setPreferredSize(new Dimension(500, 130));
    setResizable(true);
    setOKButtonEnabled(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the value a user entered in the input component.
   **
   ** @param  value              the value a user entered in the input
   **                            component.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void value(final Number value) {
    this.olderThan.setNumber(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value a user entered in the input component.
   **
   ** @return                    the value a user entered in the input
   **                            component.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public Number value() {
    return this.olderThan.getNumber();
  }
}