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

    File        :   OptionTree.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionTree.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Vector;
import java.util.Hashtable;

import javax.swing.JTree;

import javax.swing.text.Position;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import oracle.jdeveloper.workspace.iam.swing.Utilitiy;

////////////////////////////////////////////////////////////////////////////////
// class OptionTree
// ~~~~~ ~~~~~~~~~~
/**
 ** OptionTree is a special JTree which uses check boxes or radio button as the
 ** tree renderer.
 ** <p>
 ** In addition to regular JTree's features, it also allows you select any
 ** number of tree nodes in the tree by selecting the botspot buttons.
 ** <p>
 ** To select an element, user can mouse click on the botspot button, or select
 ** one or several tree nodes and press SPACE key to toggle the botspot button
 ** selection for all selected tree nodes.
 ** <p>
 ** The selection state is kept in a TreeSelectionModel called
 ** {@link OptionTreeSelectionModel}, which you can get using
 ** {@link #optionTreeSelectionModel()}.
 ** <p>
 ** In order to retrieve which tree paths are selected, you need to call
 ** {@link #optionTreeSelectionModel()}. It will return the selection model that
 ** keeps track of which tree paths has been selected. For example
 ** {@link OptionTreeSelectionModel#getSelectionPaths()} will give the list of
 ** paths which have been selected.
 ** <p>
 ** You can also add/remove a listener of a {@link OptionTreeSelectionEvent} in
 ** this way:
 ** <pre>
 **   OptionTree.addTreeSelectionListener(new OptionTreeSelectionListener() {
 **     public void valueChanged(final OptionTreeSelectionEvent event) {
 **       System.out.println(&quot;Selected paths changed: user clicked on &quot; + (e.getLeadingPath().getLastPathComponent()));
 **     }
 **   });
 ** </pre>
 ** We used cell renderer feature in JTree to add the hotspot in each row.
 ** However you can still set your own cell renderer just like before using
 ** {@link #setCellRenderer(TreeCellRenderer)}. OptionTree will use your cell
 ** renderer and automatically put a hotspot before it.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionTree extends JTree {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String         HOTSPOT_ENABLED  = "hotspotEnabled";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6341766555088661186")
  private static final long          serialVersionUID = 1990041940427171932L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  transient OptionTreeCellRenderer   renderer;
  transient ContentTreeCellRenderer  defaultRenderer;

  transient OptionTreeSelectionModel optionSelection;

  transient PropertyChangeListener   modelListener;
  transient boolean                  hotspotEnabled = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> with a sample model.
   ** <p>
   ** The default model used by the tree defines a leaf node as any node without
   ** children.
   */
  public OptionTree() {
    // ensure inheritance
    super();

    // initialize instance
    init(OptionTreePropagationModel.Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> with each element of the specified
   ** array as the child of a new root node which is not displayed. By default,
   ** the tree defines a leaf node as any node without children.
   ** <p>
   ** The tree is created using the
   ** {@link OptionTreePropagationModel.Mode#DEFAULT}.
   ** <p>
   ** Attempts to pass a <code>null</code> value to this constructor results in
   ** undefined behavior and, most likely, exceptions. The created model
   ** references the given array directly. Attempts to modify the array after
   ** constructing the list results in undefined behavior.
   **
   ** @param  value              the array of Objects to be loaded into the data
   **                            model, must be non-<code>null</code>.
   */
  public OptionTree(final Object[] value) {
    // ensure inheritance
    this(value, OptionTreePropagationModel.Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> with each element of the specified
   ** array as the child of a new root node which is not displayed. By default,
   ** the tree defines a leaf node as any node without children.
   ** <p>
   ** The tree is created using the specified
   ** {@link OptionTreePropagationModel.Mode}.
   ** <p>
   ** Attempts to pass a <code>null</code> value to this constructor results in
   ** undefined behavior and, most likely, exceptions. The created model
   ** references the given array directly. Attempts to modify the array after
   ** constructing the list results in undefined behavior.
   **
   ** @param  value              the array of Objects to be loaded into the data
   **                            model, must be non-<code>null</code>.
   ** @param  propagationMode    the propagation mode to use
   */
  public OptionTree(final Object[] value, final OptionTreePropagationModel.Mode propagationMode) {
    // ensure inheritance
    super(value);

    // initialize instance
    init(propagationMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> created from a {@link Vector} with
   ** each element of the specified {@link Vector} as the child of a new root
   ** node which is not displayed. By default, the tree defines a leaf node as
   ** any node without children.
   ** <p>
   ** Attempts to pass a <code>null</code> value to this constructor results in
   ** undefined behavior and, most likely, exceptions. The created model
   ** references the given {@code Vector} directly. Attempts to modify the
   ** {@code Vector} after constructing the list results in undefined behavior.
   ** <p>
   ** The tree is created using the
   ** {@link OptionTreePropagationModel.Mode#DEFAULT}.
   **
   ** @param  value              the {@code Vector} to be loaded into the data
   **                            model, must be non-<code>null</code>.
   */
  public OptionTree(final Vector<?> value) {
    // ensure inheritance
    this(value, OptionTreePropagationModel.Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> created from a {@link Vector} with
   ** each element of the specified {@link Vector} as the child of a new root
   ** node which is not displayed. By default, the tree defines a leaf node as
   ** any node without children.
   ** <p>
   ** Attempts to pass a <code>null</code> value to this constructor results in
   ** undefined behavior and, most likely, exceptions. The created model
   ** references the given {@code Vector} directly. Attempts to modify the
   ** {@code Vector} after constructing the list results in undefined behavior.
   ** <p>
   ** The tree is created using the specified
   ** {@link OptionTreePropagationModel.Mode}.
   **
   ** @param  value              the {@code Vector} to be loaded into the data
   **                            model, must be non-<code>null</code>.
   ** @param  propagationMode    the propagation mode to use
   */
  public OptionTree(final Vector<?> value, final OptionTreePropagationModel.Mode propagationMode) {
    // ensure inheritance
    super(value);

    // initialize instance
    init(propagationMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> created from a {@link Hashtable}
   ** which does not display with root.
   ** <p>
   ** Each value-half of the key/value pairs in the {@link Hashtable} becomes a
   ** child of the new root node. By default, the tree defines a leaf node as
   ** any node without children.
   ** <p>
   ** The tree is created using the
   ** {@link OptionTreePropagationModel.Mode#DEFAULT}.
   **
   ** @param  value              a {@link Hashtable}
   */
  public OptionTree(final Hashtable<?, ?> value) {
    // ensure inheritance
    this(value, OptionTreePropagationModel.Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> created from a {@link Hashtable}
   ** which does not display with root.
   ** <p>
   ** Each value-half of the key/value pairs in the {@link Hashtable} becomes a
   ** child of the new root node. By default, the tree defines a leaf node as
   ** any node without children.
   ** <p>
   ** The tree is created using the specified
   ** {@link OptionTreePropagationModel.Mode}.
   **
   ** @param  value              a {@link Hashtable}
   ** @param  propagationMode    the propagation mode to use
   */
  public OptionTree(final Hashtable<?, ?> value, final OptionTreePropagationModel.Mode propagationMode) {
    // ensure inheritance
    super(value);

    // initialize instance
    init(propagationMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> in which any node can have
   ** children.
   **
   ** @param  root               a {@link TreeNode} object that is the root of
   **                            the tree.
   */
  public OptionTree(final TreeNode root) {
    // ensure inheritance
    this(root, OptionTreePropagationModel.Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> in which any node can have
   ** children.
   ** <p>
   ** The tree is created using the specified
   ** {@link OptionTreePropagationModel.Mode}.
   **
   ** @param  root               a {@link TreeNode} object that is the root of
   **                            the tree.
   ** @param  propagationMode    the propagation mode to use
   */
  public OptionTree(final TreeNode root, final OptionTreePropagationModel.Mode propagationMode) {
    // ensure inheritance
    super(root);

    // initialize instance
    init(propagationMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> specifying whether any node can
   ** have children, or whether only certain nodes can have children.
   **
   ** @param  root               a {@link TreeNode} object that is the root of
   **                            the tree.
   ** @param  asksAllowsChildren a boolean, <code>false</code> if any node can
   **                            have children, <code>true</code> if each node
   **                            is asked to see if it can have children
   */
  public OptionTree(final TreeNode root, boolean asksAllowsChildren) {
    // ensure inheritance
    this(root, asksAllowsChildren, OptionTreePropagationModel.Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> specifying whether any node can
   ** have children, or whether only certain nodes can have children.
   ** <p>
   ** The tree is created using the specified
   ** {@link OptionTreePropagationModel.Mode}.
   **
   ** @param  root               a {@link TreeNode} object that is the root of
   **                            the tree.
   ** @param  asksAllowsChildren a boolean, <code>false</code> if any node can
   **                            have children, <code>true</code> if each node
   **                            is asked to see if it can have children
   ** @param  propagationMode    the propagation mode to use
   */
  public OptionTree(final TreeNode root, boolean asksAllowsChildren, final OptionTreePropagationModel.Mode propagationMode) {
    // ensure inheritance
    super(root, asksAllowsChildren);

    // initialize instance
    init(propagationMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> which displays the root node.
   ** <p>
   ** The tree is created using the specified data model and the propagation
   ** mode {@link OptionTreePropagationModel.Mode#DEFAULT} to propagate
   ** changes in the tree up and down.
   **
   ** @param  model              the {@link TreeModel} to use as the data model.
   */
  public OptionTree(final TreeModel model) {
    // ensure inheritance
    this(model, OptionTreePropagationModel.Mode.DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTree</code> which displays the root node.
   ** <p>
   ** The tree is created using the specified data model and the specified
   ** {@link OptionTreePropagationModel.Mode}.
   **
   ** @param  model              the {@link TreeModel} to use as the data model.
   ** @param  propagationMode    the propagation mode to use
   */
  public OptionTree(final TreeModel model, final OptionTreePropagationModel.Mode propagationMode) {
    // ensure inheritance
    super(model);

    // initialize instance
    init(propagationMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotEnabled
  /**
   ** Sets the value of property <code>hotspotEnabled</code>.
   **
   ** @param  state              <code>true</code> to allow to select the
   **                            hotspot. <code>false</code> to disable it which
   **                            means user can see whether a row is selected or
   **                            not but they cannot change it.
   */
  public void hotspotEnabled(final boolean state) {
    if (state != this.hotspotEnabled) {
      boolean old = this.hotspotEnabled;
      this.hotspotEnabled = state;

      firePropertyChange(HOTSPOT_ENABLED, old, state);
      repaint();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotEnabled
  /**
   ** Returns the value of property <code>hotspotEnabled</code>.
   ** <p>
   ** If <code>true</code>, user can click on hotspot buttons on each tree node
   ** to select and deselect. If <code>false</code>, user can't click but you as
   ** developer can programmatically call API to select/deselect it.
   **
   ** @return                    the value of property
   **                            <code>hotspotEnabled</code>.
   */
  public boolean hotspotEnabled() {
    return this.hotspotEnabled;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotEnabled
  /**
   ** Checks if botspot is enabled.
   ** <p>
   ** However, in all selection modes, user can still select the disabled node
   ** by selecting all children nodes of that node. Also if user selects the
   ** parent node, the disabled children nodes will be selected too.
   **
   ** @param  path               the tree path.
   **
   ** @return                    <code>true</code>, user can click on hotspot
   **                            buttons on each tree node to select and
   **                            deselect. If <code>false</code>, user can't
   **                            click but you as developer can programmatically
   **                            call API to select/deselect it.
   */
  public boolean hotspotEnabled(final TreePath path) {
    Object object = path.getLastPathComponent();
    if (!(object instanceof OptionTreeNode))
      return false;

    OptionTreeNodeData data = ((OptionTreeNode)object).data();
    return data.hotspotEnabled();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagationMode
  /**
   ** Sets the propagation mode.
   **
   ** @param  mode               the propagation mode to set.
   */
  public void propagationMode(final OptionTreeSelectionModel.Mode mode) {
    if (mode != this.optionSelection.mode()) {
      this.optionSelection.mode(mode);
      this.renderer = mode == OptionTreeSelectionModel.Mode.SINGLE ? new RadioButtonTreeCellRenderer(contentRenderer()) : new CheckBoxTreeCellRenderer(contentRenderer());
      // TODO: may be we have to call invalidate ???
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagationMode
  /**
   ** Returns the propagation mode for the botspot buttons.
   ** <p>
   ** To retrieve the state of botspot buttons, you should use this propagation
   ** model.
   **
   ** @return                    the propagation model for the botspot buttons.
   */
  public OptionTreeSelectionModel.Mode propagationMode() {
    return this.optionSelection.mode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionTreeSelectionModel
  /**
   ** Returns the selection model for the botspot buttons.
   ** <p>
   ** To retrieve the state of botspot buttons, you should use this selection
   ** model.
   **
   ** @return                    the selection model for the botspot buttons.
   */
  public OptionTreeSelectionModel optionTreeSelectionModel() {
    return this.optionSelection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCellRenderer (overridden)
  /**
   ** Sets the cell renderer that will be used to draw each cell.
   ** <p>
   ** Since OptionTree has its own botspot button cell renderer, this method will
   ** give you access to the actual cell renderer which is either the default
   ** tree cell renderer or the cell renderer you set using
   ** {@link #setCellRenderer(javax.swing.tree.TreeCellRenderer)}.
   **
   ** @param  renderer           the <code>TreeCellRenderer</code> that is to
   **                            render each cell except the botspot button.
   */
  @Override
  public void setCellRenderer(TreeCellRenderer renderer) {
    // prevent bogus input
    if (renderer == null)
      renderer = defaultRenderer();

    // ensure inheritance
    super.setCellRenderer(renderer);

    if (this.renderer != null)
      this.renderer.content((ContentTreeCellRenderer)renderer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCellRenderer (overridden)
  /**
   ** Returns the cell renderer with botspot button.
   **
   ** @return                    OptionTree's own cell renderer which has the
   **                            botspot button. The content cell renderer you
   **                            set by {@link #setCellRenderer(TreeCellRenderer)}
   **                            can be accessed by using
   **                            {@link #contentRenderer()}.
   */
  @Override
  public ContentTreeCellRenderer getCellRenderer() {
    ContentTreeCellRenderer cellRenderer = contentRenderer();
    if (cellRenderer == null) {
      cellRenderer = defaultRenderer();
    }

    if (this.renderer == null) {
      this.renderer = createCellRenderer(cellRenderer);
    }
    else {
      this.renderer.content(cellRenderer);
    }
    return this.renderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNextMatch (overridden)
  /**
   ** Returns the {@link TreePath} to the next tree element that  begins with a
   ** prefix. To handle the conversion of a {@link TreePath} into a String,
   ** <code>convertValueToText</code> is used.
   **
   ** @param  prefix             the string to test for a match
   ** @param  startingRow        the row for starting the search
   ** @param  bias               the search direction, either
   **                            <ul>
   **                              <li>Position.Bias.Forward
   **                              <li>Position.Bias.Backward
   **                            </ul>.
   ** @return                    the {@link TreePath} of the next tree element
   **                            that starts with the prefix; otherwise
   **                            <code>null</code>
   */
  @Override
  public TreePath getNextMatch(final String prefix, final int startingRow, final Position.Bias bias) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPathEditable (overridden)
  /**
   ** Returns <code>isEditable</code>.
   ** <p>
   ** This is invoked from the UI before editing begins to insure that the given
   ** path can be edited. This is provided as an entry point for subclassers to
   ** add filtered editing without having to resort to creating a new editor.
   **
   ** @return                    <code>true</code> if every parent node and the
   **                            node itself is editable.
   */
  @Override
  public boolean isPathEditable(final TreePath path) {
    final OptionTreeNode node = (OptionTreeNode)path.getLastPathComponent();
    return node.data.textEditable();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentRenderer
  /**
   ** Returns the content cell renderer.
   ** <p>
   ** Since OptionTree has its own botspot button cell renderer, this method
   ** will give you access to the actual cell renderer which is either the
   ** default tree cell renderer or the cell renderer you set using
   ** {@link #setCellRenderer(javax.swing.tree.TreeCellRenderer)}.
   **
   ** @return                    the content cell renderer.
   */
  public ContentTreeCellRenderer contentRenderer() {
    return (this.renderer != null)  ? this.renderer.content() : (ContentTreeCellRenderer)super.getCellRenderer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertValueToText (overridden)
  /**
   ** Called by the renderers to convert the specified value to text. This
   ** implementation returns <code>value.toString</code>, ignoring all other
   ** arguments. To control the conversion, subclass this  method and use any of
   ** the arguments you need.
   **
   ** @param  value            the value of the node to be rendered.
   ** @param  selected         <code>true</code> if the node is to be rendered
   **                          with the selection highlighted; otherwise
   **                          <code>false</code>.
   ** @param  expanded         <code>true</code> if the node is to be rendered
   **                          expanded; otherwise <code>false</code>.
   ** @param  leaf             <code>true</code> if the node is to be rendered
   **                          does not have children; otherwise
   **                          <code>false</code>.
   ** @param  row              the row index of the node being drawn. When
   **                          drawing the header, the value of
   **                          <code>row</code> is <code>-1</code>.
   ** @param  focused          if <code>true</code>, render node
   **                          appropriately. For example, put a special
   **                          border on the node, if the node can be edited,
   **                          render in the color used to indicate editing.
   **
   ** @return                  the <code>String</code> representation of the
   **                          node's value
   */
  @Override
  public String convertValueToText(final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean focused) {
    if (!(value instanceof OptionTreeNodeData))
      return super.convertValueToText(value, selected, expanded, leaf, row, focused);

    OptionTreeNodeData data = (OptionTreeNodeData)value;
    return (data == null) ? super.convertValueToText(value, selected, expanded, leaf, row, focused) : data.text();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOptionTreeSelectionListener
  /**
   ** Add a listener for <code>TreeSelection</code> events.
   **
   ** @param  listener           the {@link OptionTreeSelectionListener} that
   **                            will be notified when a nodes hotspot is
   **                            selected/unselected.
   */
  public void addOptionTreeSelectionListener(final OptionTreeSelectionListener listener) {
    this.optionSelection.addTreeSelectionListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeOptionTreeSelectionListener
  /**
   ** Removes a listener for <code>TreeSelection</code> events.
   **
   ** @param  listener           the {@link OptionTreeSelectionListener} that
   **                            will no longer be notified when a nodes hotspot
   **                            is selected/unselected.
   */
  public void removeOptionTreeSelectionListener(final OptionTreeSelectionListener listener) {
    this.optionSelection.removeTreeSelectionListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init
  /**
   ** Initialize the <code>OptionTree</code>.
   **
   ** @param  propagationMode      the propagation mode to use
   */
  protected void init(final OptionTreePropagationModel.Mode propagationMode) {
    this.optionSelection = createSelectionModel(propagationMode);

    OptionTreeHandler handler = createHandler();
    Utilitiy.insertMouseListener(this, handler, 0);
    addKeyListener(handler);
    this.optionSelection.addTreeSelectionListener(handler);

    applyChangeListener();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSelectionModel
  /**
   ** Creates the {@link OptionTreeSelectionModel}.
   ** <p>
   ** Subclasses may override this method if they are providing their own
   ** selection model implementation.
   **
   ** @param  propagationMode    the propagation mode to set
   **
   ** @return                    the {@link OptionTreeSelectionModel}.
   */
  protected OptionTreeSelectionModel createSelectionModel(final OptionTreePropagationModel.Mode propagationMode) {
    if (this.optionSelection == null)
      this.optionSelection = new OptionTreeSelectionModel(this, propagationMode);
    else
      this.optionSelection.mode(propagationMode);

    return this.optionSelection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCellRenderer
  /**
   ** Creates the cell renderer.
   ** <p>
   ** Unfortunately we can not create the appropriate renderer here according to
   ** the selection model because we don't know it at the time this method is
   ** invoked. The constructor of a JTree is calling setModel which will
   ** invalidate the container. Invalidating a Swing Component enforce an
   ** <code>updateUI</code> and JTree is than asking for a cell renderer. This
   ** action sequence is enclosed in the contruction phase of the instance where
   ** we are not able to plugin our own stuff. A sophisticate approach to create
   ** the appropriate cell renderer would look like:
   ** <pre>
   **   if (this.selectionModel.mode() != OptionTreeSelectionModel.Mode.RADIO)
   **     option = new CheckBoxTreeCellRenderer(renderer);
   **   else
   **     option = new RadioButtonTreeCellRenderer(renderer);
   ** </pre>
   ** But as decribed above it's not possible. So we are creating always a
   ** {@link CheckBoxTreeCellRenderer} which will be the normal usage of an
   ** <code>OptionTree</code> we assume.
   **
   ** @param  renderer           the actual renderer for the tree node.
   **                            This method will return a cell renderer that
   **                            use a botspot button and put the actual renderer
   **                            inside it.
   **
   ** @return                    the cell renderer.
   */
  protected OptionTreeCellRenderer createCellRenderer(ContentTreeCellRenderer renderer) {
    final OptionTreeCellRenderer optionRenderer = new CheckBoxTreeCellRenderer(renderer);
    addPropertyChangeListener(CELL_RENDERER_PROPERTY, new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        ContentTreeCellRenderer contentRenderer = (ContentTreeCellRenderer)event.getNewValue();
        if (contentRenderer != optionRenderer) {
          optionRenderer.content(contentRenderer);
        }
        else {
          optionRenderer.content(null);
        }
      }
    });
    return optionRenderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createHandler
  /**
   ** Creates the mouse listener and key listener used by
   ** <code>OptionTree</code>.
   **
   ** @return                    the OptionTreeHandler.
   */
  protected OptionTreeHandler createHandler() {
    return new OptionTreeHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultRenderer
  /**
   ** Create a {@link DefaultTreeCellRenderer} to render the tree cells.
   **
   ** @return                    a {@link ContentTreeCellRenderer} to render the
   **                            tree cells.
   */
  private ContentTreeCellRenderer defaultRenderer() {
    if (this.defaultRenderer == null)
      this.defaultRenderer = new DefaultContentTreeCellRenderer();

    return this.defaultRenderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyChangeListener
  /**
   ** Change listener are necessary to follow.
   */
  private void applyChangeListener() {
    if (this.modelListener == null) {
      this.modelListener = new PropertyChangeListener() {
        public void propertyChange(final PropertyChangeEvent event) {
          if (JTree.SELECTION_MODEL_PROPERTY.equals(event.getPropertyName()))
            updateRowMapper();

          if (JTree.TREE_MODEL_PROPERTY.equals(event.getPropertyName()) && event.getNewValue() instanceof TreeModel)
            OptionTree.this.optionSelection.model((TreeModel)event.getNewValue());
        }
      };
      addPropertyChangeListener(JTree.SELECTION_MODEL_PROPERTY, this.modelListener);
      addPropertyChangeListener(JTree.TREE_MODEL_PROPERTY,      this.modelListener);
    }
    updateRowMapper();
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateRowMapper
  /**
   ** RowMapper is necessary for contiguous selection.
   */
  private void updateRowMapper() {
    this.optionSelection.setRowMapper(getSelectionModel().getRowMapper());
  }
}