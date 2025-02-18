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

    File        :   ContentEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ContentEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-04-10  DSteding    First release version
*/

package oracle.jdeveloper.deployment.oim.editor;

import oracle.ide.editor.Editor;

import java.awt.Component;

import javax.swing.UIManager;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import oracle.ide.Context;

import oracle.ide.util.Namespace;

import oracle.ide.editor.Editor;

import oracle.ide.model.Node;
import oracle.ide.model.UpdateMessage;

////////////////////////////////////////////////////////////////////////////////
// class ContentEditor
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>EndpointEditor</code> is the integration layer between the IDE and
 ** the editor components inside the IDE.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class ContentEditor extends Editor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected       Node         node    = null;
  protected       ContentPanel content = null;
  protected final Namespace    data    = new Namespace();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ContentEditor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ContentEditor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContext (overridden)
  /**
   ** Set the context behind this editor.
   */
  @Override
  public synchronized void setContext(final Context context) {
    // ensure inheritance to avoid NPE
    super.setContext(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (Observer)
  @Override
  public void update(final Object observed, final UpdateMessage change) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGui (View)
  /**
   ** Returns the root graphical user interface component.
   **
   ** @return                    the root graphical user interface component.
   */
  @Override
  public Component getGUI() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open (Editor)
  /**
   ** Open this editor on the context set by a prior call to setContext.
   ** <br>
   ** If the editor cannot be open (file not found for example), this method
   ** should throw an OpenAbortedException.
   */
  @Override
  public void open() {
    this.content.requestFocus();
  }
}