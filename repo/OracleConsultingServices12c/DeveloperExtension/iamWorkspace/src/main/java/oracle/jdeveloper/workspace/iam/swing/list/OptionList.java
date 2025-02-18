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

    File        :   OptionList.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionList.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import java.awt.Dimension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Map;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import javax.swing.text.Position;

import oracle.jdeveloper.workspace.iam.swing.Utilitiy;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class OptionList
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>OptionList</code> is a special JList which uses check boxes or radio
 ** button as the list renderer.
 ** <p>
 ** In addition to regular JList's features, it also allows you select any
 ** number of elements in the list  by selecting the botspot buttons.
 ** <p>
 ** To select an element, user can mouse click on the botspot button, or select
 ** one or several elements and press SPACE key to toggle the botspot button
 ** selection for all selected elements.
 ** <p>
 ** The selection state is kept in a ListSelectionModel called
 ** {@link OptionListSelectionModel}, which you can get using
 ** {@link #selectionModel()}.
 ** <p>
 ** In order to retrieve which elementss are selected, you need to call
 ** {@link #selectionModel()}. It will return the selection model that
 ** keeps track of which list elements have been selected. For example
 ** {@link OptionListSelectionModel#isSelectionEmpty()} will give the list of
 ** paths which have been selected.
 ** <p>
 ** We used cell renderer feature in JList to add the hotspot in each row.
 ** However you can still set your own cell renderer just like before using
 ** {@link #setCellRenderer(ListCellRenderer)}. OptionList will use your cell
 ** renderer and automatically put a hotspot before it.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionList extends JList {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String         HOTSPOT_ENABLED  = "hotspotEnabled";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2104027665271475064")
  private static final long          serialVersionUID = 7433209993060837677L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  transient OptionListCellRenderer   renderer;
  transient ContentListCellRenderer  defaultRenderer;

  transient OptionListSelectionModel optionSelection;

  transient PropertyChangeListener   modelListener;
  transient boolean                  hotspotEnabled = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionList</code> with a sample model.
   */
  public OptionList() {
    // ensure inheritance
    super();

    // initialize instance
    init();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionList</code> that displays the elements in the
   ** specified array. This constructor creates a read-only model for the given
   ** array, and then delegates to the constructor that takes a
   ** {@code ListModel}.
   ** <p>
   ** Attempts to pass a <code>null</code> value to this constructor results in
   ** undefined behavior and, most likely, exceptions. The created model
   ** references the given array directly. Attempts to modify the array after
   ** constructing the list results in undefined behavior.
   **
   ** @param  value              the array of Objects to be loaded into the data
   **                            model, must be non-<code>null</code>.
   */
  public OptionList(final Object[] value) {
    // ensure inheritance
    super(value);

    // initialize instance
    init();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionList</code> that displays the elements in the
   ** specified {@code Vector}. This constructor creates a read-only model for
   ** the given {@code Vector}, and then delegates to the constructor that takes
   ** a {@code ListModel}.
   ** <p>
   ** Attempts to pass a <code>null</code> value to this constructor results in
   ** undefined behavior and, most likely, exceptions. The created model
   ** references the given {@code Vector} directly. Attempts to modify the
   ** {@code Vector} after constructing the list results in undefined behavior.
   **
   ** @param  value              the {@code Vector} to be loaded into the data
   **                            model, must be non-<code>null</code>.
   */
  public OptionList(final Vector<?> value) {
    // ensure inheritance
    super(value);

    // initialize instance
    init();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionList</code> that displays the elements from the
   ** specified, non-<code>null</code>, model.
   ** <p>
   ** This constructor registers the list with the {@code ToolTipManager},
   ** allowing for tooltips to be provided by the cell renderers.
   **
   ** @param  model              the {@link ListModel} to use as the data model.
   **
   ** @throws IllegalArgumentException if the model is <code>null</code>.
   */
  public OptionList(final ListModel model) {
    // ensure inheritance
    super(model);

    // initialize instance
    init();
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
   ** @param  index              the row index.
   **
   ** @return                    <code>true</code>, user can click on hotspot
   **                            buttons on each tree node to select and
   **                            deselect. If <code>false</code>, user can't
   **                            click but you as developer can programmatically
   **                            call API to select/deselect it.
   */
  public boolean hotspotEnabled(final int index) {
    return true;
    /*
    Object object = path.getLastPathComponent();
    if (!(object instanceof OptionTreeNode))
      return false;

    OptionTreeData data = ((OptionTreeNode)object).model();
    return data.hotspotEnabled();
    */
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectionModel
  /**
   ** Sets the selection model for the botspot buttons.
   ** <p>
   ** To retrieve the state of botspot buttons, you should use this selection
   ** model.
   **
   ** @param  model              the selection model for the botspot buttons to
   **                            use.
   */
  public void selectionModel(final OptionListSelectionModel model) {
    this.optionSelection = model;
    this.optionSelection.model(getModel());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectionModel
  /**
   ** Returns the selection model for the botspot buttons.
   ** <p>
   ** To retrieve the state of botspot buttons, you should use this selection
   ** model.
   **
   ** @return                    the selection model for the botspot buttons.
   */
  public OptionListSelectionModel selectionModel() {
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
   ** {@link #setCellRenderer(javax.swing.ListCellRenderer)}.
   **
   ** @param  renderer           the <code>ListCellRenderer</code> that is to
   **                            render each cell except the botspot button.
   */
  @Override
  public void setCellRenderer(ListCellRenderer renderer) {
    // prevent bogus input
    if (renderer == null)
      renderer = defaultRenderer();

    // ensure inheritance
    super.setCellRenderer(renderer);

    if (this.renderer != null)
      this.renderer.content((ContentListCellRenderer)renderer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCellRenderer (overridden)
  /**
   ** Returns the cell renderer with botspot button.
   **
   ** @return                    OptionList's own cell renderer which has the
   **                            botspot button. The content cell renderer you
   **                            set by {@link #setCellRenderer(ListCellRenderer)}
   **                            can be accessed by using
   **                            {@link #contentRenderer()}.
   */
  @Override
  public ListCellRenderer getCellRenderer() {
    ContentListCellRenderer cellRenderer = contentRenderer();
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
  // Method:   selectedValue
  /**
   ** Selects the specified object from the list and clear all other selections.
   **
   ** @param  element            the element object to select
   ** @param  shouldScroll       <code>true</code> if the list should scroll to
   **                            display the selected object, if one exists;
   **                            otherwise <code>false</code>.
   */
  public void selectedValue(final Object element, boolean shouldScroll) {
    if (element == null)
      setSelectedIndex(-1);
    else {
      int i, c;
      ListModel model = getModel();
      for (i = 0, c = model.getSize(); i < c; i++)
        if (element.equals(model.getElementAt(i))) {
          selectedIndex(i);
          if (shouldScroll)
            ensureIndexIsVisible(i);
          repaint(); /** FIX-ME setSelectedIndex does not redraw all the time with the basic l&f */
          return;
        }
      selectedIndex(-1);
    }
    repaint(); /** FIX-ME setSelectedIndex does not redraw all the time with the basic l&f */
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedValue
  /**
   ** Returns the first selected value, or <code>null</code> if the selection is
   ** empty.
   **
   ** @return                    the first selected value
   **
   * @see     #getModel
   ** @see    #getMinSelectionIndex
   ** @see    #addListSelectionListener
   */
  public Object selectedValue() {
    int i = selectionModel().getMinSelectionIndex();
    return (i == -1) ? null : getModel().getElementAt(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedObjects
  /**
   ** Sets the selected elements.
   **
   ** @param  elements           the elements set to select.
   **                            All the rows that have the value in the array
   **                            will be selected.
   */
  public void selectedObjects(final Object[] elements) {
    Map<Object, String> selected = new HashMap<Object, String>();
    for (Object element : elements)
      selected.put(element, StringUtility.EMPTY);

    selectedObjects(selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedObjects
  /**
   ** Sets the selected elements.
   **
   ** @param  elements           the elements set to select.
   **                            All the rows that have the value in the
   **                            {@link Vector} will be selected.
   */
  public void setSelectedObjects(Vector<?> elements) {
    Map<Object, String> selected = new HashMap<Object, String>();
    for (Object element : elements) {
      selected.put(element, StringUtility.EMPTY);
    }
    selectedObjects(selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedObjects
  /**
   ** Sets the selected elements.
   **
   ** @param selected            the elements set to select.
   **                            All the rows that have the value in the
   **                            {@link Map} will be selected.
   */
  private void selectedObjects(Map<Object, String> selected) {
    List<Integer> match = new ArrayList<Integer>();
    for (int i = 0; i < getModel().getSize(); i++) {
      Object elementAt = getModel().getElementAt(i);
      if (selected.get(elementAt) != null)
        match.add(i);
    }

    int   size    = match.size();
    int[] indices = new int[size];
    for (int i = 0; i < size; i++)
      indices[i] = match.get(i).intValue();

    selectedIndices(indices);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedIndex
  /**
   ** Selects a single cell and clear all other selections.
   **
   ** @param  index              the index of the one cell to select.
   **
   ** @see    #isSelectedIndex
   ** @see    #addListSelectionListener
   ** @see    ListSelectionModel#setSelectionInterval
   */
  public void selectedIndex(final int index) {
    if (index >= 0 && index < getModel().getSize())
      selectionModel().setSelectionInterval(index, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedIndex
  /**
   ** Returns the first selected index; returns -1 if there is no selected item.
   **
   ** @return                    the value of <code>getMinSelectionIndex</code>
   **
   ** @see    #getMinSelectionIndex
   ** @see    #addListSelectionListener
   */
  public int selectedIndex() {
    return selectionModel().getMinSelectionIndex();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedIndices
  /**
   ** Selects a set of cells.
   **
   ** @param  indices              an array of the indices of the cells to
   **                              select.
   **
   ** @see    #isSelectedIndex
   ** @see    #addListSelectionListener
   ** @see    ListSelectionModel#setSelectionInterval
   */
  public void selectedIndices(final int[] indices) {
    final ListSelectionModel model = selectionModel();
    try {
      model.setValueIsAdjusting(true);
      model.clearSelection();
      int size = getModel().getSize();
      for (int indice : indices) {
        if (indice >= 0 && indice < size)
          model.addSelectionInterval(indice, indice);
      }
    }
    finally {
      model.setValueIsAdjusting(false);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedIndices
  /**
   ** Returns an array of all of the selected indices in increasing order.
   **
   ** @return                    all of the selected indices, in increasing
   **                            order.
   **
   ** @see    #removeSelectionInterval
   ** @see    #addListSelectionListener
   */
  public int[] selectedIndices() {
    final ListSelectionModel model = selectionModel();

    final int min = model.getMinSelectionIndex();
    final int max = model.getMaxSelectionIndex();
    if ((min < 0) || (max < 0))
      return new int[0];

    int[] temp = new int[1 + (max - min)];
    int n = 0;
    for (int i = min; i <= max; i++) {
      if (model.isSelectedIndex(i)) {
        temp[n] = i;
        n++;
      }
    }
    final int[] indices = new int[n];
    System.arraycopy(temp, 0, indices, 0, n);
    return indices;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectedValues
  /**
   ** Returns an array of the values for the selected cells.
   ** <p>
   ** The returned values are sorted in increasing index order.
   **
   ** @return                    the selected values or an empty list if nothing
   **                            is selected
   **
   ** @see    #getModel
   ** @see    #isSelectedIndex
   ** @see    #addListSelectionListener
   */
   public Object[] selectedValues() {
    final ListSelectionModel selection = selectionModel();
    final int min = selection.getMinSelectionIndex();
    final int max = selection.getMaxSelectionIndex();
    if ((min < 0) || (max < 0))
      return new Object[0];

    ListModel model = getModel();
    Object[] temp = new Object[1 + (max - min)];
    int n = 0;
    for (int i = min; i <= max; i++) {
      if (selection.isSelectedIndex(i)) {
        temp[n] = model.getElementAt(i);
         n++;
      }
    }
    Object[] indices = new Object[n];
    System.arraycopy(temp, 0, indices, 0, n);
    return indices;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentRenderer
  /**
   ** Returns the content cell renderer.
   ** <p>
   ** Since OptionList has its own botspot button cell renderer, this method
   ** will give you access to the actual cell renderer which is either the
   ** default tree cell renderer or the cell renderer you set using
   ** {@link #setCellRenderer(ListCellRenderer)}.
   **
   ** @return                    the content cell renderer.
   */
  public ContentListCellRenderer contentRenderer() {
    return (this.renderer != null)  ? this.renderer.content() : (ContentListCellRenderer)super.getCellRenderer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNextMatch (overridden)
  /**
   ** Returns the row index to the next list element that begins with the
   ** specified string <code>prefix</code>.
   **
   ** @param  prefix             the string to test for a match
   ** @param  startRow           the row for starting the search
   ** @param  bias               the search direction, either
   **                            <ul>
   **                              <li>Position.Bias.Forward
   **                              <li>Position.Bias.Backward
   **                            </ul>.
   ** @return                    the row index of the next list element that
   **                            starts with the prefix; otherwise
   **                            <code>-1</code>.
   */
  @Override
  public int getNextMatch(final String prefix, final int startRow, final Position.Bias bias) {
    return -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredScrollableViewportSize
  /**
   ** Computes the size of viewport needed to display the rows of the list.
   **
   ** @return                    a dimension containing the size of the viewport
   **                            needed to display the rows of the list.
   **
   ** @see    #getPreferredScrollableViewportSize
   ** @see    #setPrototypeCellValue
   */
  @Override
  public Dimension getPreferredScrollableViewportSize() {
    return Utilitiy.adjustPreferredScrollableViewportSize(this, super.getPreferredScrollableViewportSize());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearSelection
  /**
   ** Change the selection to the empty set.
   ** <p>
   ** If this represents a change to the current selection then notify each
   ** ListSelectionListener.
   **
   ** @see    #addListSelectionListener
   */
  @Override
  public void clearSelection() {
    selectionModel().clearSelection();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectAll
  /**
   ** Selects all objects in this list except those are disabled.
   */
  public void selectAll() {
    selectionModel().setSelectionInterval(0, getModel().getSize() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectNone
  /**
   ** Unselects all objects in this list except those are disabled.
   */
  public void selectNone() {
    selectionModel().removeIndexInterval(0, getModel().getSize() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedValue
  /**
   ** Selects the specified object from the list and keep all previous
   ** selections.
   **
   ** @param  element            the element object to select
   ** @param  shouldScroll       <code>true</code> if the list should scroll to
   **                            display the selected object, if one exists;
   **                            otherwise <code>false</code>.
   */
  public void addSelectedValue(final Object element, final boolean shouldScroll) {
    if (element == null)
      return;

    final ListModel model = getModel();
    final int       size  = model.getSize();
    for (int i = 0; i < size; i++) {
      if (element.equals(model.getElementAt(i))) {
        addSelectedIndex(i);
        if (shouldScroll)
          ensureIndexIsVisible(i);

        // FIXME: selectedIndex does not redraw all the time with the basic l&f
        repaint();
        return;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedValue
  /**
   ** Unselects the specified object from the list.
   **
   ** @param  element            the element object to unselect
   ** @param  shouldScroll       <code>true</code> if the list should scroll to
   **                            display the selected object, if one exists;
   **                            otherwise <code>false</code>.
   */
  public void removeSelectedValue(final Object element, final boolean shouldScroll) {
    if (element == null)
      return;

    final ListModel model = getModel();
    final int       size  = model.getSize();
    for (int i = 0; i < size; i++) {
      if (element.equals(model.getElementAt(i))) {
        removeSelectedIndex(i);
        if (shouldScroll)
          ensureIndexIsVisible(i);

        // FIXME: selectedIndex does not redraw all the time with the basic l&f
        repaint();
        return;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedValue
  /**
   ** Selects the specified objects from the list and keep all previous selections.
   **
   ** @param  elements           the objects to be selected
   */
  public void addSelectedValues(final Object[] elements) {
    if (elements == null)
      return;

    Map<Object, String> map = new HashMap<Object, String>();
    for (Object o : elements)
      map.put(o, StringUtility.EMPTY);

    final ListModel model   = getModel();
    final int       size    = model.getSize();
    boolean         changed = false;
    for (int i = 0; i < size; i++) {
      if (map.get(model.getElementAt(i)) != null) {
        selectedIndex(i);
        changed = true;
      }
      if (changed)
        repaint();

      map.clear();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelectedIndex
  /**
   ** Selects a single cell and keeps all previous selections.
   **
   ** @param  index              the index of the one cell to select.
   **
   ** @see    #isSelectedIndex
   ** @see    #addListSelectionListener
   ** @see    ListSelectionModel#setSelectionInterval
   */
  public void addSelectedIndex(final int index) {
    if (index >= 0 && index < getModel().getSize())
      selectionModel().addSelectionInterval(index, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSelectedIndex
  /**
   ** Unselects a single cell and keeps all previous selections.
   **
   ** @param  index              the index of the one cell to unselect.
   **
   ** @see    #isSelectedIndex
   ** @see    #addListSelectionListener
   ** @see    ListSelectionModel#setSelectionInterval
   */
  public void removeSelectedIndex(final int index) {
    if (index >= 0 && index < getModel().getSize())
      selectionModel().removeSelectionInterval(index, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init
  /**
   ** Initialize the <code>OptionList</code>.
   */
  protected void init() {
    this.optionSelection = createSelectionModel();

    OptionListHandler handler = createHandler();
    Utilitiy.insertMouseListener(this, handler, 0);
    addKeyListener(handler);
    this.optionSelection.addListSelectionListener(handler);

    applyChangeListener();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSelectionModel
  /**
   ** Creates the {@link OptionListSelectionModel}.
   ** <p>
   ** Subclasses may override this method if they are providing their own
   ** selection model implementation.
   **
   ** @return                    the {@link OptionListSelectionModel}.
   */
  protected OptionListSelectionModel createSelectionModel() {
    OptionListSelectionModel selectionModel = new OptionListSelectionModel(this);
    return selectionModel;
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
   **     option = new CheckBoxListCellRenderer(renderer);
   **   else
   **     option = new RadioButtonListCellRenderer(renderer);
   ** </pre>
   ** But as decribed above it's not possible. So we are creating always a
   ** {@link CheckBoxListCellRenderer} which will be the normal usage of an
   ** <code>OptionTree</code> we assume.
   **
   ** @param  renderer           the actual renderer for the tree node.
   **                            This method will return a cell renderer that
   **                            use a botspot button and put the actual renderer
   **                            inside it.
   **
   ** @return                    the cell renderer.
   */
  protected OptionListCellRenderer createCellRenderer(final ContentListCellRenderer renderer) {
    final OptionListCellRenderer optionRenderer = new CheckBoxListCellRenderer(renderer);
    addPropertyChangeListener("cellRenderer", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        ContentListCellRenderer contentRenderer = (ContentListCellRenderer)event.getNewValue();
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
   ** <code>OptionList</code>.
   **
   ** @return                    the OptionListHandler.
   */
  protected OptionListHandler createHandler() {
    return new OptionListHandler(this);
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
          if ("model".equals(event.getPropertyName()) && event.getNewValue() instanceof ListModel)
            OptionList.this.optionSelection.model((ListModel)event.getNewValue());
        }
      };
      addPropertyChangeListener("model", this.modelListener);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultRenderer
  /**
   ** Create a {@link DefaultContentListCellRenderer} to render the tree cells.
   **
   ** @return                    a {@link ContentListCellRenderer} to render the
   **                            tree cells.
   */
  private ContentListCellRenderer defaultRenderer() {
    if (this.defaultRenderer == null)
      this.defaultRenderer = new DefaultContentListCellRenderer();

    return this.defaultRenderer;
  }
}