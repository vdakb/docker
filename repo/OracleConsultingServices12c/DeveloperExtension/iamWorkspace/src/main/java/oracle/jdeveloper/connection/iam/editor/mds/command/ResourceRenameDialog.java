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

    File        :   ResourceRenameDialog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ResourceRenameDialog.


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

import oracle.jdeveloper.connection.iam.editor.NameInputDialog;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ResourceRenameDialog
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the information about to rename a
 ** document in a Metadata Service.
 ** <p>
 ** The class is package protected to ensure that only the appropriate command
 ** is able to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
class ResourceRenameDialog extends NameInputDialog {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5206154510111266401")
  private static final long serialVersionUID = 3167836714103906913L;

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
  ResourceRenameDialog(final String origin) {
    // ensure inheritance
    super(Bundle.string(Bundle.RESOURCE_RENAME_TITLE));

    // initialize instance
    value(origin);

    final JPanel panel = new JPanel(new GridBagLayout());
    // put the label on the left side of its display area, centered vertically
    // and allow resizing the component horizontally but not vertically
    panel.add(new JLabel(Bundle.string(Bundle.RESOURCE_RENAME_LABEL)), new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));
    // put the label on the left side of its display area, centered vertically
    // and allow resizing the component horizontally but not vertically
    panel.add(this.name, new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));
    setContent(panel);
    setPreferredSize(new Dimension(500, 130));
    setResizable(true);
    setOKButtonEnabled(false);
  }
}