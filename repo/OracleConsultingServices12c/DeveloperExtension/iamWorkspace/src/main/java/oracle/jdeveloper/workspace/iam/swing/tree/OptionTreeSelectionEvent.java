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

    File        :   OptionTreeSelectionEvent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionTreeSelectionEvent.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.util.EventObject;

import javax.swing.tree.TreePath;

////////////////////////////////////////////////////////////////////////////////
// class OptionTreeSelectionEvent
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** An event that characterizes a change in the current selection. The change is
 ** related to a single checked/unchecked path.
 ** <p>
 ** {@link OptionTreeSelectionListener} will generally query the source of the
 ** event for the new selection status of each potentially changed row.
 **
 ** @see OptionTreeSelectionListener
 ** @see OptionTreeSelectionModel
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionTreeSelectionEvent extends EventObject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7676855402955930684")
  private static final long serialVersionUID = 7289984156091361430L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the path related to this event */
  private final TreePath    path;

  /** the state of the selection */
  private final boolean     selected;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Cosntructss a change in the selection of a OptionTreeSelectionModel.
   ** <p>
   ** The specified path identifies the path that have been either selected or
   ** unselected.
   **
   ** @param  source             the object on which the Event initially
   **                            occurred.
   ** @param  path               the path that has changed in the selection.
   ** @param  selected           whether or not the path is selected,
   **                            <code>false</code> means that path was removed
   **                            from the selection.
   */
  public OptionTreeSelectionEvent(final Object source, final TreePath path, final boolean selected) {
    // ensure inheritance
    super(source);

    this.path     = path;
    this.selected = selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  path
  /**
   ** Returns the path that was added or removed from the selection.
   **
   ** @return                    the path that was added or removed from the
   **                            selection.
   */
  public TreePath path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  selected
  /**
   ** Returns whether the path related to the event is selected or unselected.
   **
   ** @return                    <code>true</code> if the path related to the
   **                            event is selected. A return value of
   **                            <code>false</code> means that the path has been
   **                            removed from the selection.
   */
  public boolean selected() {
    return selected;
  }
}