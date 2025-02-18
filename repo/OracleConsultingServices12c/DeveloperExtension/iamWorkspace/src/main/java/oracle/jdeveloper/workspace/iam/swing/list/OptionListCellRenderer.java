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

    File        :   OptionListCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionListCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import java.awt.Point;
import java.awt.BorderLayout;

import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JComponent;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import oracle.jdeveloper.workspace.iam.swing.widget.TriStateButton;

////////////////////////////////////////////////////////////////////////////////
// abstract class OptionListCellRenderer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Displays an option entry (CheckBox/RadioButton) in a list.
 ** <p>
 ** <code>OptionListCellRenderer</code> is not opaque and unless you subclass
 ** paint you should not change this.
 ** <p>
 ** See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Lists</a>
 ** in <em>The Java Tutorial</em> for examples of customizing node display using
 ** such classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class OptionListCellRenderer extends DefaultContentListCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final Border      noFocusBorder    = new EmptyBorder(1, 1, 1, 1);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:64399682007118497")
  private static final long          serialVersionUID = -7390363110931683277L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The hotspot button that is used to paint the radio button in cell renderer
   */
  protected final TriStateButton    hotspot;

  /**
   ** The component that is used to paint the hotspot button in cell renderer if
   ** it is visible
   */
  protected final JComponent        prototype;

  /**
   ** The component that is used to paint the hotspot button in cell renderer if
   ** it is hidden
   */
  protected final JComponent        empty;

  /**
   ** The content which appears after the hotspot.
   */
  protected ContentListCellRenderer content;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>OptionListCellRenderer</code> that has a only a label.
   */
  public OptionListCellRenderer() {
    // ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>OptionListCellRenderer</code> that has a toggle
   ** button and a label.
   ** <p>
   ** The supplied {@link ContentListCellRenderer} is used to render the content
   ** of the cell.
   **
   ** @param  renderer           the {@link ContentListCellRenderer} is used to
   **                            render the content of the cell.
   */
  public OptionListCellRenderer(final ContentListCellRenderer renderer) {
    // ensure inheritance
    super();

    // initialize instance
    this.hotspot   = createHotspot();
    this.prototype = createPrototype();
    this.empty     = (JComponent)Box.createHorizontalStrut(this.prototype.getPreferredSize().width);
    this.content   = renderer;

    // initialize UI
    setLayout(new BorderLayout(0, 0));
    setBorder(noFocusBorder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   content
  /**
   ** Sets the cell renderer that will be used to draw each cell.
   ** <p>
   ** Since OptionList has its own hotspot cell renderer, this method will
   ** give you access to the actual cell renderer which is either the default
   ** tree cell renderer or the cell renderer you set using
   ** {@link JList#setCellRenderer(ListCellRenderer)}.
   **
   ** @param  renderer           the {@link ContentListCellRenderer} that is to
   **                            render each cell except the hotspot.
   */
  public void content(final ContentListCellRenderer renderer) {
    this.content = renderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   content
  /**
   ** Returns the actual cell content renderer.
   ** <p>
   ** Since OptionList has its own hotspot cell renderer, this method will
   ** give you access to the actual cell renderer which is either the default
   ** tree cell renderer or the cell renderer you set using.
   **
   ** @return                    the content cell renderer
   */
  public ContentListCellRenderer content() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotHit
  /**
   ** Determines whether the specified relative coordinates insist on the
   ** intended hotspot. It is used by the OptionListHandler to figure out
   ** whether to toggle a node or not.
   ** <p>
   ** This declaration underlines that this method has to be reimlemented by all
   ** subclasses.
   **
   ** @param  x                  the relative coordinates of a mouse action
   **                            along the X axis.
   ** @param  y                  the relative coordinates of a mouse action.
   **                            along the Y axis.
   **
   ** @return                    <code>true</code> is the specified relative
   **                            coordinates are inside the hotspot area,
   **                            <code>false</code> otherwise.
   */
  public abstract boolean hotspotHit(final int x, final int y);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createHotspot
  /**
   ** Creates the hotspot button this rendere will use.
   **
   ** @return                    the hotspot button this rendere will use.
   */
  protected abstract TriStateButton createHotspot();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPrototype
  /**
   ** Creates the prototyp button this rendere will use.
   **
   ** @return                    the hotspot button this rendere will use.
   */
  protected abstract JComponent createPrototype();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toolTipText
  /**
   ** Returns the string to be used as the tooltip for <code>event</code>.
   ** <p>
   ** By default this returns any string set using <code>setToolTipText</code>.
   ** If a component provides more extensive API to support differing tooltips
   ** at different locations, this method should be overridden.
   **
   ** @param  event              the {@link MouseEvent} describing the UI
   **                            context.
   ** @param  hotspotWidth       the correcttion of the mouse coordinates.
   **
   ** @return                    the string to be used as the tooltip for
   **                            <code>event</code>.
   */
  protected String toolTipText(final MouseEvent event, final int hotspotWidth) {
    if (this.content instanceof JComponent) {
      final Point p = event.getPoint();
      p.translate(-hotspotWidth, 0);
      MouseEvent newEvent = new MouseEvent((this.content), event.getID(), event.getWhen(), event.getModifiers(), p.x, p.y, event.getClickCount(), event.isPopupTrigger());

      final String tooltip = (this.content).getToolTipText(newEvent);
      if (tooltip != null)
        return tooltip;
    }
    // ensure inheritance
    return super.getToolTipText(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateState
  /**
   ** Updates the hotspot state based on the selection in the selection model.
   ** By default, we check if the path is selected. If yes, we mark the hotspot
   ** as TriStateButton.State.SELECTED. If not, we will check if the path is
   ** partially selected, if yes, we set the hotspot as <code>null</code> or
   ** TriStateButton.State.PARTIAL_SELECTED or
   ** TriStateButton.State.PARTIAL_UNSELECTED respecivly to indicate the path is
   ** partially selected. Otherwise, we set it to
   ** TriStateButton.State.UNSELECTED.
   **
   ** @param  hotspot            the {@link TriStateButton} for the particular
   **                            tree path.
   ** @param  index              the row index of the cell being drawn. When
   **                            drawing the header, the value of
   **                            <code>index</code> is <code>-1</code>.
   ** @param  selectionModel     the {@link OptionListSelectionModel}.
   */
  protected void updateState(final TriStateButton hotspot, final int index, final OptionListSelectionModel selectionModel) {
    final boolean selected = selectionModel.isSelectedIndex(index);
    hotspot.state(selected ? TriStateButton.State.SELECTED : TriStateButton.State.UNSELECTED);
  }
}