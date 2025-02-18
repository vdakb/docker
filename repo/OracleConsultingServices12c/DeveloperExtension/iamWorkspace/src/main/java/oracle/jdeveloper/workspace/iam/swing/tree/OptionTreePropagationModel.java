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

    File        :   OptionTreePropagationModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    OptionTreePropagationModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.io.Serializable;

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;

////////////////////////////////////////////////////////////////////////////////
// interface OptionTreePropagationModel
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The interface of a model for selecting/unselecting the nodes of an
 ** {@link OptionTree}.
 ** <p>
 ** Alterations of a node state may propagate to descendants/ancestors,
 ** according to the behaviour of the selection model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface OptionTreePropagationModel extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-946017851481023672")
  static final long serialVersionUID = -6494448263064895811L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Mode
  // ~~~~ ~~~~
  /**
   ** This is a type-safe enumerated type
   */
  enum Mode {
    /**
     ** The selection is not propagated at all, toggles the clicked hotspot
     ** only.
     */
    NONE,
    /**
     ** The selection is not propagated at all, toggles the clicked hotspot
     ** only.
     ** <p>
     ** Only one hotspot is allowed to be selected at any given time.
     */
    SINGLE,
    /**
     ** Toggles the clicked hotspot and propagates the change down.
     ** <p>
     ** In other words, if the clicked hotspot becomes selected, all the
     ** descendants will be selected; otherwise, all the descendants will be
     ** unselected.
     */
    DEFAULT,
    /**
     ** Propagates the change not only to descendants but also to ancestors.
     ** <p>
     ** With regard to descendants this mode behaves exactly like the
     ** DEFAULT mode. With regard to ancestors it selects/unselects them as
     ** needed so that a node is selected if and only if all of its children are
     ** selected.
     */
    DEFAULT_PRESERVE_SELECTED,
    /**
     ** Propagates the change not only to descendants but also to ancestors.
     ** <p>
     ** With regard to descendants this mode behaves exactly like the
     ** DEFAULT mode. With regard to ancestors it selects/unselects them as
     ** needed so that a node is unselected if and only if all of its children
     ** are unselected.
     */
    DEFAULT_PRESERVE_UNSELECTED,
    /**
     ** The change is propagated to descendants like in the DEFAULT mode.
     ** <p>
     ** Moreover, if the hotspot becomes unselected, all the ancestors will be
     ** unselected.
     */
    PROPAGATE_UNSELECT_ANCESTOR;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode
  /**
   ** Sets the propagation mode.
   **
   ** @param  mode               the propagation mode to set.
   */
  void mode(final Mode mode);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagation
  /**
   ** Returns the propagation mode.
   **
   ** @return                    the propagation mode.
   */
  Mode mode();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Sets the {@link TreeModel} that will provide the data.
   ** <p>
   ** The current selection set is cleared.
   **
   ** @param  model              the {@link TreeModel} that is to provide the
   **                            data.
   */
 void model(final TreeModel model);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Returns the {@link TreeModel} that is providing the data.
   **
   ** @return                    the {@link TreeModel} that is to provide the
   **                            data.
   */
  TreeModel model();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearSelection
  /**
   ** Clears the selection.
   ** <p>
   ** Metgod added here to underline that this has to be implemented.
   */
  void clearSelection();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectionPath
  /**
   ** Adds path to the current selection.
   ** <p>
   ** If path is not currently in the selection the TreeSelectionListeners are
   ** notified. This has no effect if <code>path</code> is <code>null</code>.
   **
   ** @param  path               the new path to add to the current selection.
   */
  void addSelectionPath(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSelectionPath
  /**
   ** Removes path from the selection.
   ** <p>
   ** If path is in the selection the TreeSelectionListeners are notified. This
   ** has no effect if <code>path</code> is <code>null</code>.
   **
   ** @param  path               the path to remove from the selection.
   */
  void removeSelectionPath(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPathEnabled
  /**
   ** Sets the specified path can be toggled.
   **
   ** @param  path               the path to enable.
   */
  void addPathEnabled(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPathsEnabled
  /**
   ** Sets the specified paths can be toggled.
   **
   ** @param  paths              the paths to enable.
   */
  void addPathsEnabled(TreePath[] paths);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePathEnabled
  /**
   ** Sets the specified path can not be toggled.
   **
   ** @param  path               the path to disable.
   */
  void removePathEnabled(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePathsEnabled
  /**
   ** Sets the specified paths can not be toggled.
   **
   ** @param  paths              the paths to disable.
   */
  void removePathsEnabled(TreePath[] paths);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectDescendant
  /**
   ** Selects the subtree with root path.
   **
   ** @param  path               root of the tree to be selected
   */
  void selectDescendant(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unselectDescendant
  /**
   ** Unselects the subtree rooted at path.
   **
   ** @param  path               root of the tree to be unselected
   */
  void unselectDescendant(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateAncestors
  /**
   ** Updates the partial value of the parents of path.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The partiality and selection of the other nodes (not ancestors) MUST BE
   ** consistent.
   **
   ** @param  path               the treepath containing the ancestors to be
   **                            partiality-updated.
   */
  void updateAncestors(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePartiality
  /**
   ** Updates the partial value for the given path if there are children with
   ** different values.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The partiality and selection of the other nodes (not ancestors) MUST BE
   ** consistent.
   **
   ** @param  ancestor           the path to be partiality-updated.
   */
  void updatePartiality(final TreePath ancestor);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTreePartiality
  /**
   ** Updates the partiality state of the entire tree.
   */
  void updateTreePartiality();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePathPartiality
  /**
   ** Updates the partiality of sub-tree starting at path.
   **
   ** @param  path               the root of the sub-tree to
   **                            be partiality-updated.
   */
  void updatePathPartiality(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPathSelected
  /**
   ** Tests whether there the specified path is selected.
   **
   ** @param  path               the path to check for selection.
   **
   ** @return                    <code>true</code> the specified path is
   **                            selected; otherwise <code>false</code>.
   */
  boolean isPathSelected(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPpartiallySelected
  /**
   ** Tests whether there is any unselected node in the subtree of given path.
   **
   ** @param  path               the path to check for partially selection.
   **
   ** @return                    <code>true</code> the specified path is
   **                            partially selected; otherwise
   **                            <code>false</code>.
   */
  boolean isPartiallySelected(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPathEnabled
  /**
   ** Tests whether given path is enabled.
   **
   ** @param  path               the path to check for whether its enabled or
   **                            not.
   **
   ** @return                    <code>true</code> if the path is enabled;
   **                            otherwise <code>false</code>.
   */
  boolean isPathEnabled(final TreePath path);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addTreeSelectionListener
  /**
   ** Add a listener for <code>TreeSelection</code> events.
   **
   ** @param  listener           the {@link OptionTreeSelectionListener} that
   **                            will be notified when a nodes hotspot is
   **                            selected/unselected.
   */
  void addTreeSelectionListener(final OptionTreeSelectionListener listener);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeTreeSelectionListener
  /**
   ** Removes a listener for <code>TreeSelection</code> events.
   **
   ** @param  listener           the {@link OptionTreeSelectionListener} that
   **                            will no longer be notified when a nodes hotspot
   **                            is selected/unselected.
   */
  void removeTreeSelectionListener(final OptionTreeSelectionListener listener);
}