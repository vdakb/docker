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

    File        :   ScrollAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ScrollAdapter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.Serializable;

import java.awt.Point;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.KeyboardFocusManager;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

////////////////////////////////////////////////////////////////////////////////
// class ScrollAdapter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>ScrollAdapter</code> will ensure that when a component gains focus
 ** it will always be visible within the viewport of the scrollpane.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ScrollAdapter implements PropertyChangeListener
                           ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5412364937422223299")
  private static final long serialVersionUID = 1536336995361974280L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private JScrollPane       pane;
  private Type              type;
  private Insets            insets          = new Insets(0, 0, 0, 0);
  private Point             location        = new Point();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  public enum Type { COMPONENT, PARENT, CHILD; }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>ScrollAdapter</code> with the default
   ** {@link Type#COMPONENT}.
   **
   ** @param  scrollPane         the {@link JScrollPane} this adapater belongs
   **                            to.
   */
  public ScrollAdapter(final JScrollPane scrollPane) {
    // ensure inhertitance
    this(scrollPane, Type.COMPONENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>ScrollAdapter</code> with the default
   ** {@link Type#COMPONENT}.
   **
   ** @param  scrollPane         the {@link JScrollPane} this adapater belongs
   **                            to.
   ** @param  type               the {@link Type} of the {@link JScrollPane}
   **                            component.
   */
  public ScrollAdapter(final JScrollPane scrollPane, final Type type) {
    // ensure inhertitance
    super();

    // initialize instance
    this.pane = scrollPane;
    this.type = type;
    trackFocus(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableType
  /**
   ** Returns the component Type enum
   **
   ** @return                    the component Type enum
   */
  public Type scrollableType() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableType
  /**
   ** Sets the Type of scrolling to be done by the scroller
   **
   ** @param  type               controls scrolling of the viewport (values
   **                            given above)
   */
  public void scrollableType(final Type type) {
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableInsets
  /**
   ** Sets the scroll insets.
   ** <p>
   ** The scroller will attempt to leave a gap between the scrolled Container
   ** and the edge of the scrollpane.
   **
   ** @param  insets             the {@link Insets} for the gap for each edge
   **                            of the scrollpane
   */
  public void scrollableInsets(final Insets insets) {
    this.insets = insets;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableInsets
  /**
   ** Returns the scroll insets.
   ** <p>
   ** The scroller will attempt to leave a gap between the scrolled Container
   ** and the edge of the scrollpane.
   **
   ** @return                    the {@link Insets} for the gap for each edge
   **                            of the scrollpane
   */
  public Insets scrollableInsets() {
    return this.insets;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyChange (PropertyChangeListener)
  /**
   ** This method gets called when a bound property is changed.
   **
   ** @param  event              a {@link PropertyChangeEvent} object describing
   **                            the event source  and the property that has
   **                            changed.
   */
  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    Component component = (Component)event.getNewValue();
    if (component == null)
      return;

    //  Make sure the component with focus is in the viewport
    JComponent view = (JComponent)this.pane.getViewport().getView();
    if (!SwingUtilities.isDescendingFrom(component, view))
      return;

    //  Scroll the viewport
    Rectangle bounds = determineScrollBounds(component, view);
    view.scrollRectToVisible(bounds);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trackFocus
  /**
   ** Enable or disables automatic scrolling on the form by tracking the input
   ** focus.
   **
   ** @param  state              enable/disable scrolling
   */
  public void trackFocus(final boolean state) {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("permanentFocusOwner", this);
    if (state)
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   determineScrollBounds
  /**
   ** Determine the bounds that must fit into the viewport of the scrollpane
   **
   ** @param  component          the component that currently has focus
   ** @param  view               the component added to the viewport of the
   **                            associated scrollpane.
   **
   ** @return                    a {@link Rectangle} representing the bounds to
   **                            be scrolled.
   */
  protected Rectangle determineScrollBounds(final Component component, final JComponent view) {
    //  Determine the scroll bounds based on the scroll Type
    Rectangle bounds = null;
    if (this.type == Type.COMPONENT)
      bounds = determineComponentBounds(component, view);
    else if (this.type == Type.PARENT)
      bounds = determineParentBounds(component, view);
    else
      bounds = determineChildBounds(component, view);

    //  Adjust bounds to take scroll insets into consideration
    Point location = component.getLocation();
    location = SwingUtilities.convertPoint(component.getParent(), location, view);

    if (location.x < this.location.x)
      bounds.x -= this.insets.left;
    else
      bounds.width += this.insets.right;

    if (location.y < this.location.y)
      bounds.y -= this.insets.top;
    else
      bounds.height += this.insets.bottom;

    this.location = location;
    return bounds;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   determineComponentBounds
  /**
   ** Determine the bounds that must fit into the viewport of the scrollpane.
   **
   ** @param  component          the component that currently has focus
   ** @param  view               the component added to the viewport of the
   **                            associated scrollpane.
   **
   ** @return                    a {@link Rectangle} representing the bounds of
   **                            the specified component.
   */
  private Rectangle determineComponentBounds(final Component component, final JComponent view) {
    // Use the bounds of the focused component
    Rectangle bounds = component.getBounds();
    bounds = SwingUtilities.convertRectangle(component.getParent(), bounds, view);
    return bounds;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   determineParentBounds
  /**
   ** Determine the bounds of the parent component that must fit into the
   ** viewport of the scrollpane.
   **
   ** @param  component          the component that currently has focus
   ** @param  view               the component added to the viewport of the
   **                            associated scrollpane.
   **
   ** @return                    a {@link Rectangle} representing the bounds of
   **                            the specified component.
   */
  private Rectangle determineParentBounds(final Component component, final JComponent view) {
    //  Use the bound of the parent Container
    Component parent = component.getParent();
    Rectangle bounds = parent.getBounds();
    bounds = SwingUtilities.convertRectangle(parent.getParent(), bounds, view);

    //  Make sure the Container will fit into the viewport
    if (rectangleFits(bounds))
      return bounds;
    else
      return determineComponentBounds(component, view);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   determineChildBounds
  /**
   ** Determine the bounds of the child components that must fit into the
   ** viewport of the scrollpane.
   **
   ** @param  component          the component that currently has focus
   ** @param  view               the component added to the viewport of the
   **                            associated scrollpane.
   **
   ** @return                    a {@link Rectangle} representing the bounds of
   **                            the specified component.
   */
  private Rectangle determineChildBounds(final Component component, final JComponent view) {
    //  Search each child Component of the view to find the Container which
    //  contains the component which current has focus
    Component child = null;

    for (Component c : view.getComponents()) {
      if (SwingUtilities.isDescendingFrom(component, c)) {
        child = c;
        break;
      }
    }

    Rectangle bounds = child.getBounds();
    bounds = SwingUtilities.convertRectangle(child.getParent(), bounds, view);
    //  Make sure this container will fit into the viewport
    if (rectangleFits(bounds))
      return bounds;
    else
      return determineParentBounds(component, view);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rectangleFits
  private boolean rectangleFits(final Rectangle bounds) {
    Dimension viewport = this.pane.getViewport().getSize();
    if (bounds.width > viewport.width || bounds.height > viewport.height)
      return false;
    else
      return true;
  }
}