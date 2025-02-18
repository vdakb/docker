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

    File        :   OptionTreeSelectionModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    OptionTreeSelectionModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.util.Set;
import java.util.HashSet;

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeSelectionModel;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.EventListenerList;

////////////////////////////////////////////////////////////////////////////////
// class OptionTreeSelectionModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>OptionTreeSelectionModel</code> is a selection model based on
 ** {@link DefaultTreeSelectionModel} and used in {@link OptionTree} to keep
 ** track of the selected tree paths.
 ** <p>
 ** Alterations of a node state may propagate to descendants/ancestors,
 ** according to the behaviour of the propagation model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionTreeSelectionModel extends    DefaultTreeSelectionModel
                                      implements OptionTreePropagationModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2746104180650959236")
  private static final long              serialVersionUID = 8075386628985953293L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient OptionTree           tree;
  private transient TreeModel            delegate;

  private transient final Set<TreePath>  disabledPath     = new HashSet<TreePath>();
  private transient final Set<TreePath>  partialPath      = new HashSet<TreePath>();

  transient OptionTreePropagationAdapter adapter          = null;
  /** Event listener list. */
  transient EventListenerList            listenerList     = new EventListenerList();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class None
  // ~~~~~ ~~~~
  /**
   ** <code>None</code> defines a {@link OptionTreePropagationAdapter} without
   ** recursion. In this mode the selection state always changes only the
   ** current node: no recursion.
   */
  protected class None extends OptionTreePropagationAdapter {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    protected None(final OptionTreePropagationModel model) {
      // ensure inheritance
      super(model);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesInserted (TreeModelListener)
    /**
     ** Invoked after nodes have been inserted into the tree.
     ** <p>
     ** Use <code>event.getPath()</code> to get the parent of the new node(s).
     ** <code>event.getChildIndices()</code> returns the index(es) of the new
     ** node(s) in ascending order.
     */
    @Override
    public void treeNodesInserted(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesRemoved (TreeModelListener)
    /**
     ** Invoked after nodes have been removed from the tree. Note that if a
     ** subtree is removed from the tree, this method may only be invoked once for
     ** the root of the removed subtree, not once for each individual set of
     ** siblings removed.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the former parent of the deleted
     ** node(s). <code>event.getChildIndices()</code> returns, in ascending order,
     ** the index(es) the node(s) had before being deleted.
     */
    @Override
    public void treeNodesRemoved(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeStructureChanged (TreeModelListener)
    /**
     ** Invoked after a node (or a set of siblings) has changed in some way.
     ** <p>
     ** The node(s) have not changed locations in the tree or altered their
     ** children arrays, but other attributes have changed and may affect
     ** presentation. <b>Example</b>:
     ** <br>
     ** The name of a file has changed, but it is in the same location in the
     ** file system. To indicate the root has changed, childIndices and children
     ** will be <code>null</code>.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the parent of the changed
     ** node(s). <code>event.getChildIndices()</code> returns the index(es) of the
     ** changed node(s).
     */
    @Override
    public void treeStructureChanged(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: selectPath (OptionTreePropagationAdapter)
    /**
     ** Selects the specified path and propagates the selection according to the
     ** strategy
     **
     ** @param  path             the path to be added.
     */
    @Override
    public void selectPath(final TreePath path) {
      this.model.addSelectionPath(path);
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
      // notify listeners
      final OptionTreeSelectionEvent event = new OptionTreeSelectionEvent(this, path, true);
      fireValueChanged(event);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unselectPath (OptionTreePropagationAdapter)
    /**
     ** Unselects the specified path and propagates the selection according to
     ** the strategy
     **
     ** @param  path               the path to be removed.
     */
    @Override
    public void unselectPath(final TreePath path) {
      this.model.removeSelectionPath(path);
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Single
  // ~~~~~ ~~~~~~
  /**
   ** <code>Single</code> defines a {@link OptionTreePropagationAdapter} without
   ** recursion. In this mode the selection state always changes only the
   ** current node: no recursion. Also, only a single node of the tree is
   ** allowed to have a selection at a given time.
   */
  private class Single extends None {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    private Single(OptionTreeSelectionModel model) {
      // ensure inheritance
      super(model);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: selectPath (OptionTreePropagationAdapter)
    /**
     ** Selects the specified path and propagates the selection according to the
     ** strategy
     **
     ** @param  path             the path to be added.
     */
    @Override
    public void selectPath(final TreePath path) {
      this.model.clearSelection();
      this.model.addSelectionPath(path);
//      this.model.updatePartiality(path);
//      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unselectPath (OptionTreePropagationAdapter)
    /**
     ** Unselects the specified path and propagates the selection according to
     ** the strategy.
     **
     ** @param  path               the path to be removed.
     */
    @Override
    public void unselectPath(final TreePath path) {
      this.model.removeSelectionPath(path);
//      this.model.updatePartiality(path);
//      this.model.updateAncestors(path);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Default
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Default</code> defines a {@link OptionTreePropagationAdapter} with
   ** down recursion of the selection when nodes are clicked. It toggles the
   ** just-clicked hotspot and propagates the change down.
   ** <p>
   ** In other words, if the clicked hotspot is selected all the descendants
   ** will be selected; otherwise all the descendants will be unselected.
   */
  protected class Default extends OptionTreePropagationAdapter {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    protected Default(final OptionTreePropagationModel model) {
      // ensure inheritance
      super(model);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesInserted (TreeModelListener)
    /**
     ** Invoked after nodes have been inserted into the tree.
     ** <p>
     ** Use <code>event.getPath()</code> to get the parent of the new node(s).
     ** <code>event.getChildIndices()</code> returns the index(es) of the new
     ** node(s) in ascending order.
     */
    @Override
    public void treeNodesInserted(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path))
        this.model.selectDescendant(path);
      else
        this.model.unselectDescendant(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesRemoved (TreeModelListener)
    /**
     ** Invoked after nodes have been removed from the tree. Note that if a
     ** subtree is removed from the tree, this method may only be invoked once for
     ** the root of the removed subtree, not once for each individual set of
     ** siblings removed.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the former parent of the deleted
     ** node(s). <code>event.getChildIndices()</code> returns, in ascending order,
     ** the index(es) the node(s) had before being deleted.
     */
    @Override
    public void treeNodesRemoved(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeStructureChanged (TreeModelListener)
    /**
     ** Invoked after a node (or a set of siblings) has changed in some way.
     ** <p>
     ** The node(s) have not changed locations in the tree or altered their
     ** children arrays, but other attributes have changed and may affect
     ** presentation. <b>Example</b>:
     ** <br>
     ** The name of a file has changed, but it is in the same location in the
     ** file system. To indicate the root has changed, childIndices and children
     ** will be <code>null</code>.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the parent of the changed
     ** node(s). <code>event.getChildIndices()</code> returns the index(es) of the
     ** changed node(s).
     */
    @Override
    public void treeStructureChanged(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPartiallySelected(path))
        this.model.selectDescendant(path);
      else
        this.model.unselectDescendant(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: selectPath (OptionTreeSelectionAdapter)
    /**
     ** Selects the specified path and propagates the selection according to the
     ** strategy
     **
     ** @param  path             the path to be added.
     */
    @Override
    public void selectPath(final TreePath path) {
      this.model.selectDescendant(path);
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unselectPath (OptionTreeSelectionAdapter)
    /**
     ** Unselects the specified path and propagates the selection according to
     ** the strategy.
     **
     ** @param  path               the path to be removed.
     */
    @Override
    public void unselectPath(final TreePath path) {
      this.model.unselectDescendant(path);
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class DefaultPreserveSelected
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
  /**
   ** <code>PropagateSelected</code> defines a
   ** {@link OptionTreePropagationAdapter} with down and up recursion of the
   ** selection when nodes are clicked. It propagates the change not only to
   ** descendants but also to ancestors. With regard to descendants this mode
   ** behaves exactly like the {@link Default} adapter. With regard to
   ** ancestors it selects/unselects them as needed so that a node is selected
   ** if and only if all of its children are selected.
   */
  protected class DefaultPreserveSelected extends OptionTreePropagationAdapter {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    protected DefaultPreserveSelected(final OptionTreePropagationModel model) {
      // ensure inheritance
      super(model);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesInserted (TreeModelListener)
    /**
     ** Invoked after nodes have been inserted into the tree.
     ** <p>
     ** Use <code>event.getPath()</code> to get the parent of the new node(s).
     ** <code>event.getChildIndices()</code> returns the index(es) of the new
     ** node(s) in ascending order.
     */
    @Override
    public void treeNodesInserted(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path))
        selectPath(path);
      else
        unselectPath(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesRemoved (TreeModelListener)
    /**
     ** Invoked after nodes have been removed from the tree. Note that if a
     ** subtree is removed from the tree, this method may only be invoked once for
     ** the root of the removed subtree, not once for each individual set of
     ** siblings removed.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the former parent of the deleted
     ** node(s). <code>event.getChildIndices()</code> returns, in ascending order,
     ** the index(es) the node(s) had before being deleted.
     */
    @Override
    public void treeNodesRemoved(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (!this.model.isPathSelected(path)) {
        if (childPath(path).length != 0) {
          if (!childMatch(path, false)) {
            selectPath(path);
          }
        }
      }
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeStructureChanged (TreeModelListener)
    /**
     ** Invoked after a node (or a set of siblings) has changed in some way.
     ** <p>
     ** The node(s) have not changed locations in the tree or altered their
     ** children arrays, but other attributes have changed and may affect
     ** presentation. <b>Example</b>:
     ** <br>
     ** The name of a file has changed, but it is in the same location in the
     ** file system. To indicate the root has changed, childIndices and children
     ** will be <code>null</code>.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the parent of the changed
     ** node(s). <code>event.getChildIndices()</code> returns the index(es) of the
     ** changed node(s).
     */
    @Override
    public void treeStructureChanged(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path))
        selectPath(path);
      else
        unselectPath(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: selectPath (OptionTreeSelectionAdapter)
    /**
     ** Selects the specified path and propagates the selection according to the
     ** strategy
     **
     ** @param  path             the path to be added.
     */
    @Override
    public void selectPath(final TreePath path) {
      // TODO: implement
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unselectPath (OptionTreeSelectionAdapter)
    /**
     ** Unselects the specified path and propagates the selection according to
     ** the strategy.
     **
     ** @param  path               the path to be removed.
     */
    @Override
    public void unselectPath(final TreePath path) {
      // unselect is propagated to children
      this.model.unselectDescendant(path);
      TreePath parentPath = path.getParentPath();
      // uncheck is propagated to parents, too
      while (parentPath != null) {
        this.model.removeSelectionPath(parentPath);
        this.model.updatePartiality(path);
        parentPath = parentPath.getParentPath();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class DefaultPreserveUnselected
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   ** <code>DefaultPreserveUnselected</code> defines a
   ** {@link OptionTreePropagationAdapter}  with down and up recursion of the
   ** selection when nodes are clicked. It propagates the change not only to
   ** descendants but also to ancestors. With regard to descendants this mode
   ** behaves exactly like the Propagate mode. With regard to ancestors it
   ** selects/unselects them as needed so that a node is unselecteded if and
   ** only if all of its children are unselected.
   */
  protected class DefaultPreserveUnselected extends OptionTreePropagationAdapter {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    protected DefaultPreserveUnselected(final OptionTreePropagationModel model) {
      // ensure inheritance
      super(model);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesInserted (TreeModelListener)
    /**
     ** Invoked after nodes have been inserted into the tree.
     ** <p>
     ** Use <code>event.getPath()</code> to get the parent of the new node(s).
     ** <code>event.getChildIndices()</code> returns the index(es) of the new
     ** node(s) in ascending order.
     */
    @Override
    public void treeNodesInserted(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path))
        this.model.selectDescendant(path);
      else
        this.model.unselectDescendant(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesRemoved (TreeModelListener)
    /**
     ** Invoked after nodes have been removed from the tree. Note that if a
     ** subtree is removed from the tree, this method may only be invoked once for
     ** the root of the removed subtree, not once for each individual set of
     ** siblings removed.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the former parent of the deleted
     ** node(s). <code>event.getChildIndices()</code> returns, in ascending order,
     ** the index(es) the node(s) had before being deleted.
     */
    @Override
    public void treeNodesRemoved(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path)) {
        if (childPath(path).length != 0) {
          if (!childMatch(path, true))
            unselectPath(path);
        }
      }
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeStructureChanged (TreeModelListener)
    /**
     ** Invoked after a node (or a set of siblings) has changed in some way.
     ** <p>
     ** The node(s) have not changed locations in the tree or altered their
     ** children arrays, but other attributes have changed and may affect
     ** presentation. <b>Example</b>:
     ** <br>
     ** The name of a file has changed, but it is in the same location in the
     ** file system. To indicate the root has changed, childIndices and children
     ** will be <code>null</code>.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the parent of the changed
     ** node(s). <code>event.getChildIndices()</code> returns the index(es) of the
     ** changed node(s).
     */
    @Override
    public void treeStructureChanged(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path))
        selectPath(path);
      else
        unselectPath(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: selectPath (OptionTreeSelectionAdapter)
    /**
     ** Selects the specified path and propagates the selection according to the
     ** strategy
     **
     ** @param  path             the path to be added.
     */
    @Override
    public void selectPath(final TreePath path) {
      // TODO: implement
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unselectPath (OptionTreeSelectionAdapter)
    /**
     ** Unselects the specified path and propagates the selection according to
     ** the strategy.
     **
     ** @param  path               the path to be removed.
     */
    @Override
    public void unselectPath(final TreePath path) {
      // TODO: implement
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class DefaultPreserveUnselected
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   ** <code>PropagateUnselectAncestor</code> defines a
   ** {@link OptionTreePropagationAdapter} with down recursion of the selection
   ** when nodes are clicked and up only when unselected. The selection is
   ** propagated, like the {@link Default} adapter to descendants. If a user
   ** unselects a hotspot the unselect will also be propagated to ancestors.
   */
  protected class PropagateUnselectAncestor extends OptionTreePropagationAdapter {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    protected PropagateUnselectAncestor(final OptionTreePropagationModel model) {
      // ensure inheritance
      super(model);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesInserted (TreeModelListener)
    /**
     ** Invoked after nodes have been inserted into the tree.
     ** <p>
     ** Use <code>event.getPath()</code> to get the parent of the new node(s).
     ** <code>event.getChildIndices()</code> returns the index(es) of the new
     ** node(s) in ascending order.
     */
    @Override
    public void treeNodesInserted(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path))
        selectPath(path);
      else
        unselectPath(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeNodesRemoved (TreeModelListener)
    /**
     ** Invoked after nodes have been removed from the tree. Note that if a
     ** subtree is removed from the tree, this method may only be invoked once for
     ** the root of the removed subtree, not once for each individual set of
     ** siblings removed.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the former parent of the deleted
     ** node(s). <code>event.getChildIndices()</code> returns, in ascending order,
     ** the index(es) the node(s) had before being deleted.
     */
    @Override
    public void treeNodesRemoved(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (!this.model.isPartiallySelected(path)) {
        // TODO: ugly design works only if its an inner class
        if (childPath(path).length != 0) {
          if (!childMatch(path, false))
            selectPath(path);
        }
      }
      this.model.updatePartiality(path);
      this.model.updateAncestors(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   treeStructureChanged (TreeModelListener)
    /**
     ** Invoked after a node (or a set of siblings) has changed in some way.
     ** <p>
     ** The node(s) have not changed locations in the tree or altered their
     ** children arrays, but other attributes have changed and may affect
     ** presentation. <b>Example</b>:
     ** <br>
     ** The name of a file has changed, but it is in the same location in the
     ** file system. To indicate the root has changed, childIndices and children
     ** will be <code>null</code>.
     ** <p>
     ** Use <code>event.getPath()</code>  to get the parent of the changed
     ** node(s). <code>event.getChildIndices()</code> returns the index(es) of the
     ** changed node(s).
     */
    @Override
    public void treeStructureChanged(final TreeModelEvent event) {
      final TreePath path = event.getTreePath();
      if (this.model.isPathSelected(path))
        selectPath(path);
      else
        unselectPath(path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: selectPath (OptionTreeSelectionAdapter)
    /**
     ** Selects the specified path and propagates the selection according to the
     ** strategy
     **
     ** @param  path             the path to be added.
     */
    @Override
    public void selectPath(final TreePath path) {
      // select is propagated to children
      this.model.selectDescendant(path);

      // unselect is propagated to parents
      TreePath parentPath = path.getParentPath();
      while (parentPath != null) {
        this.model.updatePartiality(parentPath);
        parentPath = parentPath.getParentPath();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unselectPath (OptionTreeSelectionAdapter)
    /**
     ** Unselects the specified path and propagates the selection according to
     ** the strategy.
     **
     ** @param  path               the path to be removed.
     */
    @Override
    public void unselectPath(final TreePath path) {
      // unselect is propagated to children
      this.model.unselectDescendant(path);
      TreePath parentPath = path.getParentPath();
      // unselect is propagated to parents
      while (parentPath != null) {
        this.model.removeSelectionPath(parentPath);
        this.model.updatePartiality(parentPath);
        parentPath = parentPath.getParentPath();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a {@link DefaultTreeSelectionModel} with
   ** {@link OptionTreePropagationModel.Mode#DEFAULT} as the propagation
   ** strategy.
   **
   ** @param  tree               the {@link OptionTree} this model controls.
   */
  public OptionTreeSelectionModel(final OptionTree tree) {
    // ensure inheritance
    this(tree, Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a {@link DefaultTreeSelectionModel} with
   ** {@link OptionTreePropagationModel.Mode#DEFAULT} as the propagation
   ** strategy.
   **
   ** @param  tree               the {@link OptionTree} this model controls.
   ** @param  mode               the selection mode to set.
   */
  public OptionTreeSelectionModel(final OptionTree tree, final Mode mode) {
    // ensure inheritance
    super();

    setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

    // initialize instance
    this.tree = tree;
    model(this.tree.getModel());
    mode(mode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSelectionPath (overridden)
  /**
   ** Selects the specified path.
   **
   ** @param  path               the tree to be selected.
   */
  @Override
  public void setSelectionPath(final TreePath path) {
    clearSelection();
    this.adapter.selectPath(path);
    OptionTreeSelectionEvent event = new OptionTreeSelectionEvent(this, path, true);
    fireValueChanged(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSelectionPaths (overridden)
  /**
   ** Sets the selection to the specified paths.
   **
   ** @param  paths              the paths to be selected.
   */
  @Override
  public void setSelectionPaths(final TreePath[] paths) {
    clearSelection();
    for (TreePath path : paths)
      this.adapter.selectPath(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRowSelected (overridden)
  /**
   ** Returns <code>true</code> if the row identified by <code>row</code> is
   ** selected.
   **
   ** @return                    <code>true</code> if the row identified by
   **                            <code>row</code> is selected.
   */
  @Override
  public boolean isRowSelected(final int row) {
    return super.isPathSelected(this.tree.getPathForRow(row));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model (OptionTreePropagationModel)
  /**
   ** Sets the {@link TreeModel} that will provide the data.
   ** <p>
   ** The current selection set is cleared.
   **
   ** @param  model              the {@link TreeModel} that is to provide the
   **                            data.
   */
  public void model(final TreeModel model) {
    if (this.delegate != model) {
      if (this.delegate != null)
        this.delegate.removeTreeModelListener(this.adapter);

      this.delegate = model;

      if (this.delegate != null)
        this.delegate.addTreeModelListener(this.adapter);

      clearSelection();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model (OptionTreePropagationModel)
  /**
   ** Returns the {@link TreeModel} that is providing the data.
   **
   ** @return                    the {@link TreeModel} that is to provide the
   **                            data.
   */
  public TreeModel model() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode (OptionTreePropagationModel)
  /**
   ** Sets the propagation mode.
   ** <p>
   ** If the <code>OptionTree</code> is in other modes as
   ** {@link OptionTreePropagationModel.Mode#NONE} or
   ** {@link OptionTreePropagationModel.Mode#SINGLE}
   ** <p>
   ** The consistence of the existing selection is not enforced nor controlled.
   **
   ** @param  mode               the selection mode to set.
   */
  @Override
  public void mode(final OptionTreePropagationModel.Mode mode) {
    // Mode implements togglePath method (Strategy Pattern)
    switch (mode) {
     case NONE                        : this.adapter = new None(this);
                                        break;
     case SINGLE                      : this.adapter = new Single(this);
                                        break;
     case DEFAULT                     : this.adapter = new Default(this);
                                        break;
     case DEFAULT_PRESERVE_SELECTED   : this.adapter = new DefaultPreserveSelected(this);
                                        break;
     case DEFAULT_PRESERVE_UNSELECTED : this.adapter = new DefaultPreserveSelected(this);
                                        break;
     case PROPAGATE_UNSELECT_ANCESTOR : this.adapter = new PropagateUnselectAncestor(this);
     default                          : break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode (OptionTreePropagationModel)
  /**
   ** Returns the propagation mode.
   ** <p>
   ** If the <code>OptionTree</code> is in other modes as
   ** {@link OptionTreePropagationModel.Mode#NONE} or
   ** {@link OptionTreePropagationModel.Mode#SINGLE}
   **
   ** @return                    the propagation mode.
   */
  @Override
  public OptionTreePropagationModel.Mode mode() {
    if (this.adapter instanceof None)
      return OptionTreePropagationModel.Mode.NONE;
    else if (this.adapter instanceof Single)
      return OptionTreePropagationModel.Mode.SINGLE;
    else if (this.adapter instanceof Default)
      return OptionTreePropagationModel.Mode.DEFAULT;
    else if (this.adapter instanceof DefaultPreserveSelected)
      return OptionTreePropagationModel.Mode.DEFAULT_PRESERVE_SELECTED;
    else if (this.adapter instanceof DefaultPreserveUnselected)
      return OptionTreePropagationModel.Mode.DEFAULT_PRESERVE_UNSELECTED;
    else if (this.adapter instanceof PropagateUnselectAncestor)
      return OptionTreePropagationModel.Mode.PROPAGATE_UNSELECT_ANCESTOR;
    else
      return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPathEnabled (OptionTreePropagationModel)
  /**
   ** Sets the specified path can be toggled.
   **
   ** @param  path               the path to enable.
   */
  @Override
  public void addPathEnabled(final TreePath path) {
    this.disabledPath.remove(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPathEnabled (OptionTreePropagationModel)
  /**
   ** Sets the specified paths can be toggled.
   **
   ** @param  paths              the paths to enable.
   */
  @Override
  public void addPathsEnabled(final TreePath[] paths) {
    for (TreePath path : paths)
      addPathEnabled(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePathEnabled (OptionTreePropagationModel)
  /**
   ** Sets the specified path can not be toggled.
   **
   ** @param  path              the path to disable.
   */
  @Override
  public void removePathEnabled(final TreePath path) {
    this.disabledPath.add(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePathsEnabled (OptionTreePropagationModel)
  /**
   ** Sets the specified paths can not be toggled.
   **
   ** @param  paths              the path to disable.
   */
  @Override
  public void removePathsEnabled(final TreePath[] paths) {
    for (TreePath path : paths)
      removePathEnabled(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectDescendant (OptionTreePropagationModel)
  /**
   ** Selects the subtree with root path.
   **
   ** @param  path               root of the tree to be selected
   */
  @Override
  public void selectDescendant(final TreePath path) {
    addSelectedPath(path);
    removePartialPath(path);
    final OptionTreeNode node = (OptionTreeNode)path.getLastPathComponent();
    // propagate the state downward to the child nodes
    int childrenNumber = this.delegate.getChildCount(node);
    for (int childIndex = 0; childIndex < childrenNumber; childIndex++) {
      TreePath childPath = path.pathByAddingChild(this.delegate.getChild(node, childIndex));
      selectDescendant(childPath);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unselectDescendant (OptionTreePropagationModel)
  /**
   ** Unselects the subtree rooted at path.
   **
   ** @param  path               root of the tree to be unselected
   */
  @Override
  public void unselectDescendant(final TreePath path) {
    // this ivokes the method of the super class
    removeSelectedPath(path);
    removePartialPath(path);
    final OptionTreeNode node = (OptionTreeNode)path.getLastPathComponent();
    // propagate the state downward to the child nodes
    int childrenNumber = this.delegate.getChildCount(node);
    for (int childIndex = 0; childIndex < childrenNumber; childIndex++) {
      TreePath childPath = path.pathByAddingChild(this.delegate.getChild(node, childIndex));
      unselectDescendant(childPath);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateAncestors (OptionTreePropagationModel)
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
  @Override
  public void updateAncestors(final TreePath path) {
    TreePath[] parents = new TreePath[path.getPathCount()];
    parents[0] = path;
    boolean partial = isPartiallySelected(path);
    for (int i = 1; i < parents.length; i++) {
      parents[i] = parents[i - 1].getParentPath();
      if (partial)
        addPathPartial(parents[i]);
      else {
        updatePartiality(parents[i]);
        partial = isPartiallySelected(parents[i]);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePartiality (OptionTreePropagationModel)
  /**
   ** Updates the partial value value for the given path if there are children
   ** with different values.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The partiality and selection of the other nodes (not ancestors) MUST BE
   ** consistent.
   **
   ** @param  ancestor           the path to be partiality-updated.
   */
  @Override
  public void updatePartiality(final TreePath ancestor) {
    final boolean value = isPathSelected(ancestor);
    final Object  node  = ancestor.getLastPathComponent();
    final int     count = this.delegate.getChildCount(node);
    for (int i = 0; i < count; i++) {
      final Object   childNode = this.delegate.getChild(node, i);
      final TreePath childPath = ancestor.pathByAddingChild(childNode);
      if (isPartiallySelected(childPath)) {
        addPathPartial(ancestor);
        return;
      }
      if (isPathSelected(childPath) != value) {
        addPathPartial(ancestor);
        return;
      }
    }
    removePartialPath(ancestor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTreePartiality (OptionTreePropagationModel)
  /**
   ** Updates the partiality state of the entire tree.
   */
  @Override
  public void updateTreePartiality() {
    if (this.delegate.getRoot() != null)
      updatePathPartiality(new TreePath(this.delegate.getRoot()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updatePathPartiality (OptionTreePropagationModel)
  /**
   ** Updates the partiality of sub-tree starting at path.
   **
   ** @param  path               the root of the sub-tree to
   **                            be partiality-updated.
   */
  @Override
  public void updatePathPartiality(final TreePath path) {
    if (childMatch(path, !isPathSelected(path)))
      addPathPartial(path);
    else
      removePartialPath(path);

    if (isPartiallySelected(path)) {
      for (TreePath childPath : childPath(path)) {
        updatePathPartiality(childPath);
      }
      return;
    }
    else
      removePartialPath(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPartiallySelected (OptionTreePropagationModel)
  /**
   ** Tests whether there is any unselected node in the subtree of given path.
   **
   ** @param  path               the path to check for partially selection.
   **
   ** @return                    <code>true</code> the specified path is
   **                            partially selected; otherwise
   **                            <code>false</code>.
   */
  @Override
  public boolean isPartiallySelected(final TreePath path) {
    return this.partialPath.contains(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPathEnabled (OptionTreeSelectionModel)
  /**
   ** Tests whether given path is enabled.
   **
   ** @param  path               the path to check for whether its enabled or
   **                            not.
   **
   ** @return                    <code>true</code> if the path is enabled;
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean isPathEnabled(final TreePath path) {
    return !this.disabledPath.contains(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearSelection (overridden)
  /**
   ** Clears the selection.
   ** <p>
   ** Overrides the method in DefaultTreeSelectionModel to consider propagateion
   ** strategy.
   */
  @Override
  public void clearSelection() {
    this.partialPath.clear();
    super.clearSelection();
    if (this.delegate != null && this.delegate.getRoot() != null)
      fireValueChanged(new OptionTreeSelectionEvent(this, new TreePath(this.delegate.getRoot()), false));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedPath (overridden)
  /**
   ** Adds path to the current selection.
   ** <p>
   ** If path is not currently in the selection the TreeSelectionListeners are
   ** notified. This has no effect if <code>path</code> is <code>null</code>.
   **
   ** @param  path               the new path to add to the current selection.
      */
  private void addSelectedPath(final TreePath path) {
    // ensure inheritance
    super.addSelectionPath(path);

    // notify listeners
    final OptionTreeSelectionEvent event = new OptionTreeSelectionEvent(this, path, true);
    fireValueChanged(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSelectedPath (overridden)
  /**
   ** Removes path from the selection.
   ** <p>
   ** If path is not currently in the selection the TreeSelectionListeners are
   ** notified. This has no effect if <code>path</code> is <code>null</code>.
   **
   ** @param  path               the path to remove from the selection.
      */
  private void removeSelectedPath(final TreePath path) {
    // ensure inheritance
    super.removeSelectionPath(path);

    // notify listeners
    final OptionTreeSelectionEvent event = new OptionTreeSelectionEvent(this, path, false);
    fireValueChanged(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addTreeSelectionListener
  /**
   ** Add a listener for <code>TreeSelection</code> events.
   **
   ** @param  listener           the {@link OptionTreeSelectionListener} that
   **                            will be notified when a nodes hotspot is
   **                            selected/unselected.
   */
  @Override
  public void addTreeSelectionListener(final OptionTreeSelectionListener listener) {
    this.listenerList.add(OptionTreeSelectionListener.class, listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeTreeSelectionListener
  /**
   ** Removes a listener for <code>TreeSelection</code> events.
   **
   ** @param  listener           the {@link OptionTreeSelectionListener} that
   **                            will no longer be notified when a nodes hotspot
   **                            is selected/unselected.
   */
  public void removeTreeSelectionListener(final OptionTreeSelectionListener listener) {
    this.listenerList.remove(OptionTreeSelectionListener.class, listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Ugly design how to make this better to let it the propgation adpater see
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   childMatch
  /**
   ** @param  path               the root of the subtree to be searched.
   ** @param  value              the value to be found.
   **
   **
   ** @return                    <code>true</code> if exists a node with the
   **                            expected value in the subtree of path.
   */
  protected boolean childMatch(final TreePath path, final boolean value) {
    final Object node  = path.getLastPathComponent();
    final int    count = this.delegate.getChildCount(node);
    for (int i = 0; i < count; i++) {
      final TreePath childPath = path.pathByAddingChild(this.delegate.getChild(node, i));
      if (isPathSelected(childPath) == value)
        return true;
    }

    for (int i = 0; i < count; i++) {
      final TreePath childPath = path.pathByAddingChild(this.delegate.getChild(node, i));
      if (childMatch(childPath, value))
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   childPath
  /**
   ** Return the paths that are children of path, using methods of TreeModel.
   ** <p>
   ** Nodes don't have to be of type TreeNode.
   **
   ** @param  path               the parent path
   **
   ** @return                    the array of children path
   */
  protected TreePath[] childPath(final TreePath path) {
    Object node = path.getLastPathComponent();
    int childrenNumber = this.delegate.getChildCount(node);
    TreePath[] childrenPath = new TreePath[childrenNumber];
    for (int childIndex = 0; childIndex < childrenNumber; childIndex++) {
      childrenPath[childIndex] = path.pathByAddingChild(this.delegate.getChild(node, childIndex));
    }
    return childrenPath;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fireValueChanged
  /**
   ** Notifies all listeners that are registered for tree selection events on
   ** this object.
   **
   ** @param  event              the @link OptionTreeSelectionEvent} object that
   **                            characterizes the change.
   **                            <br>
   **                            Allowed object is
   **                            {@link OptionTreeSelectionEvent}.
   **
   ** @see   EventListenerList
   ** @see   #addTreeSelectionListener(OptionTreeSelectionListener)
   ** @see   #removeTreeSelectionListener(OptionTreeSelectionListener)
   */
  protected void fireValueChanged(final OptionTreeSelectionEvent event) {
    // Guaranteed to return a non-null array
    Object[] listeners = this.listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == OptionTreeSelectionListener.class) {
        ((OptionTreeSelectionListener)listeners[i + 1]).valueChanged(event);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPathPartial
  private void addPathPartial(final TreePath path) {
    this.partialPath.add(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePartialPath
  private void removePartialPath(final TreePath path) {
    this.partialPath.remove(path);
  }
}