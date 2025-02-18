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

    Copyright © 2021. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   ContentPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ContentPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-04-10  DSteding    First release version
*/

package oracle.jdeveloper.deployment.oim.editor;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;

import oracle.javatools.ui.TransparentPanel;

import oracle.javatools.ui.plaf.FlatTabbedPaneUI;

////////////////////////////////////////////////////////////////////////////////
// class EndpointContent
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A {@link TransparentPanel} to display the content of an <code>Editor</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class ContentPanel extends TransparentPanel {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final JTabbedPane tabbed = new JTabbedPane();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ContentPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ContentPanel() {
    // ensure inheritance
    super();

    // initialize instance
    setLayout(new BorderLayout());

    // Use the standard "finger tab" visual appearance for category tabs in a
    // flat editor.
    this.tabbed.setUI(new FlatTabbedPaneUI());
    this.tabbed.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    add(tabbed, BorderLayout.CENTER);
  }
}