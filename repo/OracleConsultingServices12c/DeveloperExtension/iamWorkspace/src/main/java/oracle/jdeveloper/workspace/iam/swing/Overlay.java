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

    File        :   Overlay.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Overlay.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.awt.Insets;

import javax.swing.JComponent;

////////////////////////////////////////////////////////////////////////////////
// interface Overlay
// ~~~~~~~~~ ~~~~~~~
/**
 ** <code>Overlay</code> provides a way to add a number of components on top of
 ** another component as the overlay components.
 ** <p>
 ** Usually we make a component implementing {@link Overlay} interface
 ** although it is not required. This interface will allow user to add/remove
 ** other components as overlay components and set their location independently.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface Overlay {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Client property.
   ** <p>
   ** If a component has this property, the property will be an Overlay. The
   ** component is the actual component of the Overlay.
   */
  static final String CLIENT_PROPERTY = "overlayed";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverlayComponent
  /**
   ** Adds an overlay component to the center.
   **
   ** @param  component          the overlay component.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   */
  void addOverlayComponent(final JComponent component);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverlayComponent
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
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   ** @param  location           the overlay location.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void addOverlayComponent(final JComponent component, final int location);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOverlayComponent
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
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   ** @param  location           the overlay location.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  index              the overlay index.
   **                            <code>0</code> means the first overlay
   **                            component. <code>-1</code> means the last
   **                            overlay component.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void addOverlayComponent(final JComponent component, final int location, final int index);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeOverlayComponent
  /**
   ** Removes an overlay component that was added before.
   **
   ** @param  component          the {@link Overlay} component to remove.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   */
  void removeOverlayComponent(final JComponent component);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOverlayComponents
  /**
   ** Returns the overlay component.
   **
   ** @return                    the overlay component.
   **                            <br>
   **                            Possible object is array of {@link JComponent}.
   */
  JComponent[] getOverlayComponents();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverlayLocation
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
   ** @param  component          the {@link JComponent} the location of the
   **                            overlay has to set for.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   ** @param  location           the overlay component location.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void setOverlayLocation(final JComponent component, final int location);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOverlayLocation
  /**
   ** Returns the overlay component location.
   ** <p>
   ** If <code>-1</code> is returned, it means the component doesn't exit.
   **
   ** @param  component          the {@link JComponent} the overlay component
   **                            location has to returned for.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   **
   ** @return                    the overlay component location.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int getOverlayLocation(final JComponent component);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverlayLocationInsets
  /**
   ** Sets the insets of the overlay component relative to the border of the
   ** component. This will affect the actual location of the overlay component
   ** except CENTER. If an edge of the insets is greater than 0, it will move
   ** the overlay component outwards on that edge. On the opposite, if the value
   ** is negative, it will move inward.
   **
   ** @param  insets             the insets of the overlay component
   **                            relative to the border of the component.
   **                            <br>
   **                            Allowed object is {@link Insets}.
   */
  void setOverlayLocationInsets(final Insets insets);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOverlayLocationInsets
  /**
   ** Returns the insets of the overlay component relative to the border of the
   ** component.
   **
   ** @return                    the insets of the overlay component relative to
   **                            the border of the component.
   **                            <br>
   **                            Possible object is {@link Insets}.
   */
  Insets getOverlayLocationInsets();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOverlayVisible
  /**
   ** Sets all the overlay components visible or invisible. If you want to set
   ** one overlay component visible/invisible, you just need to call setVisible
   ** of that component.
   **
   ** @param  visible            <code>true</code> to set it visible.
   **                            <code>false</code> to invisible.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  void setOverlayVisible(final boolean visible);
}