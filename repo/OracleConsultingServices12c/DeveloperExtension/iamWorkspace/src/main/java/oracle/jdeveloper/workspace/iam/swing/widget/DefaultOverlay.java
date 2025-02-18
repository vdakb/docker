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

    File        :   DefaultOverlay.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultOverlay.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.util.Map;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;

import java.awt.Insets;
import java.awt.Container;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Dimension;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;

import oracle.jdeveloper.workspace.iam.swing.Overlay;

////////////////////////////////////////////////////////////////////////////////
// class DefaultOverlay
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>DefaultOverlay</code> is the default implementation of {@link Overlay}
 ** using {@link JPanel} as the base component.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class DefaultOverlay extends    JPanel
                            implements Overlay
                            ,          ComponentListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4279661613874187301")
  private static final long       serialVersionUID = -8187181017266257904L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private JComponent               topLevel;
  private Insets                   insets         = new Insets(0, 0, 0, 0);
  private List<JComponent>         overlays       = new Vector<JComponent>();
  private Map<JComponent, Integer> locations      = new Hashtable<JComponent, Integer>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  public DefaultOverlay() {
    setLayout(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  public DefaultOverlay(final JComponent component) {
    setLayout(null);
    setActualComponent(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  public DefaultOverlay(final JComponent actualComponent, final JComponent overlayComponent) {
    this(actualComponent, overlayComponent, SwingConstants.CENTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  public DefaultOverlay(final JComponent actualComponent, final JComponent overlayComponent, final int overlayLocation) {
    setLayout(null);

    setActualComponent(actualComponent);
    addOverlayComponent(overlayComponent, overlayLocation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredSize (overridden)
  /**
   ** If the UI delegate's <code>getPreferredSize</code> method returns a non
   ** <code>null</code> value then consider the overlayLocationInsets.
   ** <p>
   ** If the overlayLocationInsets's edges are positive number, we will increase
   ** the preferred size so that the overlay component can be shown. If they are
   ** negative, we will still keep the super.getPreferredSize.
   **
   ** @return                    the preferred size of the DefaultOverlayable.
   */
  @Override
  public Dimension getPreferredSize() {
    Dimension size   = getActualComponent() == null ? new Dimension(0, 0) : getActualComponent().getPreferredSize();
    Insets    insets = getOverlayLocationInsets();
    if (insets != null) {
      size.width += Math.max(0, insets.left) + Math.max(0, insets.right);
      size.height += Math.max(0, insets.top) + Math.max(0, insets.bottom);
    }
    return size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBounds (overridden)
  /**
   ** Moves and resizes this component.
   ** <p>
   ** The new location of the top-left corner is specified by <code>x</code> and
   ** <code>y</code>, and the new size is specified by <code>width</code> and
   ** <code>height</code>.
   **
   ** @param  x                  the new <i>x</i>-coordinate of this component
   ** @param  y                  the new <i>y</i>-coordinate of this component
   ** @param  width              the new <code>width</code> of this component
   ** @param  height             the new <code>height</code> of this component
   */
  @Override
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x, y, width, height);

    Insets insets = getOverlayLocationInsets();
    x       = Math.max(0, insets.left);
    y       = Math.max(0, insets.top);
    width  -= Math.max(0, insets.left) + Math.max(0, insets.right);
    height -= Math.max(0, insets.top)  + Math.max(0, insets.bottom);

    getActualComponent().setBounds(x, y, width, height);
    updateLocation();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setActualComponent
  public void setActualComponent(final JComponent actualComponent) {
    if (this.topLevel != null) {
      remove(this.topLevel);
      this.topLevel.putClientProperty(CLIENT_PROPERTY, null);
    }
    this.topLevel = actualComponent;
    this.topLevel.putClientProperty(CLIENT_PROPERTY, this);
    add(this.topLevel);
    Container container = getParent();
    if (container != null) {
      invalidate();
      container.validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActualComponent
  /**
   ** Returns the UI delegate.
   **
   ** @return                    the UI delegate.
   */
  public JComponent getActualComponent() {
    return this.topLevel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverlayComponent (Overlayable)
  /**
   ** Adds an overlay component to the center.
   **
   ** @param  component          the overlay component.
   */
  @Override
  public void addOverlayComponent(final JComponent component) {
    addOverlayComponent(component, SwingConstants.CENTER, -1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverlayComponent (Overlayable)
  /**
   ** Adds an overlay component at the specified location.
   ** <p>
   ** The location could be one of the following values.
   ** <ul>
   **   <li>{@link javax.swing.SwingConstants#CENTER}
   **   <li>{@link javax.swing.SwingConstants#SOUTH}
   **   <li>{@link javax.swing.SwingConstants#NORTH}
   **   <li>{@link javax.swing.SwingConstants#WEST}
   **   <li>{@link javax.swing.SwingConstants#EAST}
   **   <li>{@link javax.swing.SwingConstants#NORTH_EAST}
   **   <li>{@link javax.swing.SwingConstants#NORTH_WEST}
   **   <li>{@link javax.swing.SwingConstants#SOUTH_EAST}
   **   <li>{@link javax.swing.SwingConstants#SOUTH_WEST}
   ** </ul>
   **
   ** @param  component          the overlay component.
   ** @param  location           the overlay location.
   */
  @Override
  public void addOverlayComponent(final JComponent component, final int location) {
    addOverlayComponent(component, location, -1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverlayComponent (Overlayable)
  /**
   ** Adds an overlay component at the specified location.
   ** <p>
   ** The location could be one of the following values.
   ** <ul>
   **   <li>{@link javax.swing.SwingConstants#CENTER}
   **   <li>{@link javax.swing.SwingConstants#SOUTH}
   **   <li>{@link javax.swing.SwingConstants#NORTH}
   **   <li>{@link javax.swing.SwingConstants#WEST}
   **   <li>{@link javax.swing.SwingConstants#EAST}
   **   <li>{@link javax.swing.SwingConstants#NORTH_EAST}
   **   <li>{@link javax.swing.SwingConstants#NORTH_WEST}
   **   <li>{@link javax.swing.SwingConstants#SOUTH_EAST}
   **   <li>{@link javax.swing.SwingConstants#SOUTH_WEST}
   ** </ul>
   **
   ** @param  component          the overlay component.
   ** @param  location           the overlay location.
   ** @param  index              the overlay index.
   **                            <code>0</code> means the first overlay
   **                            component. <code>-1</code> means the last
   **                            overlay component.
   */
  @Override
  public void addOverlayComponent(final JComponent component, final int location, final int index) {
    addOverlayComponent(component, null, location, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeOverlayComponent (Overlayable)
  /**
   ** Removes an overlay component that was added before.
   **
   ** @param  component          the {@link Overlay} component to remove.
   */
  @Override
  public void removeOverlayComponent(final JComponent component) {
    if (this.overlays.contains(component)) {
      this.overlays.remove(component);
      this.locations.remove(component);
      remove(component);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOverlayComponents (Overlayable)
  /**
   ** Returns the overlay component.
   **
   ** @return                    the overlay component.
   */
  @Override
  public JComponent[] getOverlayComponents() {
    return this.overlays.toArray(new JComponent[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverlayLocation (Overlayable)
  /**
   * Sets the overlay component location. The valid values are defined in
   * SwingConstants. They are
   ** <ul>
   **   <li>{@link javax.swing.SwingConstants#CENTER}
   **   <li>{@link javax.swing.SwingConstants#SOUTH}
   **   <li>{@link javax.swing.SwingConstants#NORTH}
   **   <li>{@link javax.swing.SwingConstants#WEST}
   **   <li>{@link javax.swing.SwingConstants#EAST}
   **   <li>{@link javax.swing.SwingConstants#NORTH_EAST}
   **   <li>{@link javax.swing.SwingConstants#NORTH_WEST}
   **   <li>{@link javax.swing.SwingConstants#SOUTH_EAST}
   **   <li>{@link javax.swing.SwingConstants#SOUTH_WEST}
   ** </ul>
   **
   ** @param  location           the overlay component location.
   */
  @Override
  public void setOverlayLocation(final JComponent component, final int location) {
    setOverlayLocation(component, null, location);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOverlayLocation (Overlayable)
  /**
   ** Returns the overlay component location.
   ** <p>
   ** If <code>-1</code> is returned, it means the component doesn't exit.
   **
   ** @return                    the overlay component location.
   */
  @Override
  public int getOverlayLocation(final JComponent component) {
    Integer location = this.locations.get(component);
    return (location != null) ? location : -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverlayLocationInsets (Overlayable)
  /**
   ** Sets the insets of the overlay component relative to the border of the
   ** component. This will affect the actual location of the overlay component
   ** except CENTER. If an edge of the insets is greater than 0, it will move
   ** the overlay component outwards on that edge. On the opposite, if the value
   ** is negative, it will move inward.
   **
   ** @param  insets             the insets of the overlay component
   **                            relative to the border of the component.
   */
  @Override
  public void setOverlayLocationInsets(final Insets insets) {
    this.insets = insets;
    Container container = getParent();
    if (container != null) {
      invalidate();
      container.validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOverlayLocationInsets (Overlayable)
  /**
   ** Returns the insets of the overlay component relative to the border of the
   ** component.
   **
   ** @return                    the insets of the overlay component relative to
   **                            the border of the component.
   */
  @Override
  public Insets getOverlayLocationInsets() {
    return this.insets;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverlayVisible (Overlayable)
  /**
   ** Sets all the overlay components visible or invisible. If you want to set
   ** one overlay component visible/invisible, you just need to call setVisible
   ** of that component.
   **
   ** @param  visible            <code>true</code> to set it visible.
   **                            <code>false</code> to invisible.
   */
  @Override
  public void setOverlayVisible(boolean visible) {
    JComponent[] components = getOverlayComponents();
    for (JComponent component : components) {
      component.setVisible(visible);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   componentResized
  public void componentResized(final ComponentEvent event) {
    updateLocation();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   componentMoved
  public void componentMoved(final ComponentEvent event) {
    updateLocation();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   componentShown
  public void componentShown(final ComponentEvent event) {
    updateLocation();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   componentHidden
  public void componentHidden(final ComponentEvent event) {
    updateLocation();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverlayComponent
  private void addOverlayComponent(final JComponent component, final Component relativeComponent, final int location, final int index) {
    if (this.overlays.contains(component))
      this.overlays.remove(component);

    if (index == -1) {
      this.overlays.add(component);
      // add it before the actual component
      add(component, getComponentCount() - 1);
    }
    else {
      this.overlays.add(index, component);
      add(component, index);
    }
    setOverlayLocation(component, relativeComponent, location);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverlayLocation
  private void setOverlayLocation(final JComponent component, final Component relativeComponent, final int location) {
    int old = getOverlayLocation(component);
    if (old != location) {
      this.locations.put(component, location);
      updateLocation();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateLocation
  private void updateLocation() {
    JComponent[] components = getOverlayComponents();
    for (JComponent c : components) {
      if (c == null)
        return;

      if (c.isVisible()) {
        Rectangle r = getOverlayComponentBounds(c);
        c.setBounds(r);
      }
    }
  }

  private Rectangle getOverlayComponentBounds(JComponent component) {
    Component relativeComponent = getActualComponent();

    Rectangle bounds = relativeComponent.getBounds();
    if (relativeComponent != getActualComponent())
      bounds = SwingUtilities.convertRectangle(relativeComponent.getParent(), bounds, getActualComponent());

    Rectangle overlayBounds = new Rectangle(bounds);
    Insets insets = getOverlayLocationInsets();
    overlayBounds.x -= insets.left;
    overlayBounds.y -= insets.top;
    overlayBounds.width += insets.left + insets.right;
    overlayBounds.height += insets.top + insets.bottom;

    int cx = 0;
    int cy = 0;

    Dimension size = component.getPreferredSize();
    int cw = size.width;
    int ch = size.height;

    switch (getOverlayLocation(component)) {
      case SwingConstants.CENTER     : cx = bounds.x + (bounds.width - cw) / 2;
                                       cy = bounds.y + (bounds.height - ch) / 2;
                                       break;
      case SwingConstants.NORTH      : cx = bounds.x + (bounds.width - cw) / 2;
                                       cy = overlayBounds.y;
                                       break;
      case SwingConstants.SOUTH      : cx = bounds.x + (bounds.width - cw) / 2;
                                       cy = overlayBounds.y + overlayBounds.height - ch;
                                       break;
      case SwingConstants.WEST       : cx = overlayBounds.x;
                                       cy = bounds.y + (bounds.height - ch) / 2;
                                       break;
      case SwingConstants.EAST       : cx = overlayBounds.x + overlayBounds.width - cw;
                                       cy = bounds.y + (bounds.height - ch) / 2;
                                       break;
      case SwingConstants.NORTH_WEST : cx = overlayBounds.x;
                                       cy = overlayBounds.y;
                                       break;
      case SwingConstants.NORTH_EAST : cx = overlayBounds.x + overlayBounds.width - cw;
                                       cy = overlayBounds.y;
                                       break;
      case SwingConstants.SOUTH_WEST : cx = overlayBounds.x;
                                       cy = overlayBounds.y + overlayBounds.height - ch;
                                       break;
      case SwingConstants.SOUTH_EAST : cx = overlayBounds.x + overlayBounds.width - cw;
                                       cy = overlayBounds.y + overlayBounds.height - ch;
                                       break;
    }
    return new Rectangle(cx, cy, cw, ch);
  }
}