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

    File        :   OptionTreeSelectionAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionTreeSelectionAdapter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import javax.swing.tree.TreePath;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

////////////////////////////////////////////////////////////////////////////////
// class OptionTreePropagationAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The adapter implementation for selecting/unselecting the nodes.
 ** <p>
 ** Alterations of a node state may propagate on descendants/ascendants,
 ** according to the behavior of the model. Several default behavioral modes are
 ** defined. The models must use the methods addPathSelected and
 ** removePathSelected from DefaultOptionTreeSelectionModel to add/remove the
 ** single paths from the selection set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class OptionTreePropagationAdapter implements TreeModelListener {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected OptionTreePropagationModel model;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum PathSelection
  // ~~~~ ~~~~~~~~~~~~~
  /**
   ** This is a type-safe enumerated type
   */
  protected enum PathSelection { EMPTY, SELECTED, UNSELECTED, PARTIAL }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>OptionTreePropagationAdapter</code> that use the
   ** supplied {@link OptionTreePropagationModel} to synchronize the nodes.
   **
   ** @param  model              the {@link OptionTreePropagationModel} is used
   **                             to synchronize the nodes.
   */
  public OptionTreePropagationAdapter(final OptionTreePropagationModel model) {
    super();

    this.model = model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Returns the propagation model for the botspot buttons.
   ** <p>
   ** To retrieve the state of botspot buttons, you should use this propagation
   ** model.
   **
   ** @return                    the propagation model for the botspot buttons.
   */
  public OptionTreePropagationModel model() {
    return this.model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: treeNodesChanged (TreeModelListener)
  /**
   ** Invoked after the tree has drastically changed structure from a given
   ** node down. If the path returned by <code>event.getPath()</code> is of
   ** length one and the first element does not identify the current root node
   ** the first element should become the new root of the tree.
   ** <p>
   ** Use <code>event.getPath()</code> to get the path to the node.
   ** <code>event.getChildIndices()</code> returns null.
   */
  @Override
  public void treeNodesChanged(final TreeModelEvent event) {
    TreePath path = event.getTreePath();
    this.model.updatePathPartiality(path);
    this.model.updateAncestors(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   togglePath (OptionTreePropagationModel)
  /**
   ** Toggles (select/unselect) the selection state of the specified path, if
   ** this is enabled, and possibly propagate the change, according to the
   ** selection mode.
   **
   ** @param  path               the path to be toggled.
   */
  public void togglePath(final TreePath path) {
    if (!this.model.isPathEnabled(path))
      return;

    if (this.model.isPathSelected(path))
      unselectPath(path);
    else
      selectPath(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectPath
  /**
   ** Selects the specified path and propagates the state of selection according
   ** to the strategy.
   **
   ** @param  path               the path to be added.
   */
  public abstract void selectPath(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unselectPath
  /**
   ** Unselects the specified path and propagates the state of selection
   ** according to the strategy.
   **
   ** @param  path               the path to be removed.
   */
  public abstract void unselectPath(final TreePath path);
}