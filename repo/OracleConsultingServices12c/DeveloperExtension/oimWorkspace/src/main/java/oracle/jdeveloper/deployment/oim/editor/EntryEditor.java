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

    File        :   EntryEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EntryEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-04-10  DSteding    First release version
*/

package oracle.jdeveloper.deployment.oim.editor;

import javax.swing.Icon;

import oracle.jdeveloper.deployment.oim.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class EntryEditor
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>EntryEditor</code> is the integration layer between the IDE and
 ** the editor components to provide an entry editor inside the IDE.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class EntryEditor extends ContentEditor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                label          = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntryEditor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntryEditor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTabLabel (overridden)
  /**
   ** This method is called to get the title to display in the tab hosting this
   ** editor by a prior call to {@link #setContext(Context)}.
   **
   ** @return                    the title to display in the tab hosting this
   **                            editor.
   */
  @Override
  public String getTabLabel() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTabIcon (overridden)
  /**
   ** This method is called to get the icon to display in the tab hosting this
   ** editor by a prior call to {@link #setContext(Context)}.
   **
   ** @return                    the icon to display in the tab hosting this
   **                            editor.
   */
  @Override
  public Icon getTabIcon() {
    return Bundle.icon(Bundle.INVENTORY_HEADER_ICON);
  }
}