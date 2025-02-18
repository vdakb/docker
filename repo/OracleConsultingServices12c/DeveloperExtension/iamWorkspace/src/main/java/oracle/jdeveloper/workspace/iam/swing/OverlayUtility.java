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

    File        :   OverlayUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OverlayUtility.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.util.List;
import java.util.ArrayList;

import java.awt.Component;
import java.awt.Container;

import javax.swing.Icon;
import javax.swing.JComponent;

import oracle.jdeveloper.workspace.iam.utility.OverlayIconFactory;

////////////////////////////////////////////////////////////////////////////////
// class OverlayUtility
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This util class has several methods related to {@link Overlay}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OverlayUtility {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   first
  /**
   ** Returns the Overlay associated with the specified component and its
   ** parents. This method will find the first {@link Overlay} that contains the
   ** component or its parents.
   **
   ** @param  component          the component to decorate.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   **
   ** @return                    the decorated {@link Overlay}.
   **                            <br>
   **                            Possible object is {@link Overlay}.
   */
  public static Overlay first(final JComponent component) {
    Container cursor = component;
    while (true) {
      Object o = ((JComponent)cursor).getClientProperty(Overlay.CLIENT_PROPERTY);
      if (o instanceof Overlay)
        return (Overlay)o;
      cursor = cursor.getParent();
      if (!(cursor instanceof JComponent))
        break;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   all
  /**
   ** Returns all {@link Overlay}s associated with the specified component and
   ** its parents. Different from {@link #first(JComponent)}, this method will
   ** return all {@link Overlay}s that contain the component or its parents.
   **
   ** @param  component          the component that has an Overlay
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   **
   ** @return                    all the {@link Overlay}s.
   **                            <br>
   **                            Possible object is array of {@link Overlay}.
   */
  public static Overlay[] all(final JComponent component) {
    final List<Overlay> list = new ArrayList<Overlay>();
    Container cursor = component;
    while (true) {
      Object o = ((JComponent)cursor).getClientProperty(Overlay.CLIENT_PROPERTY);
      if (o instanceof Overlay) {
        if (!list.contains(o))
          list.add((Overlay)o);
      }
      cursor = cursor.getParent();
      if (cursor == null)
        break;
    }
    return list.toArray(new Overlay[list.size()]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repaint
  /**
   ** Repaints the {@link Overlay} component associated with component.
   ** <p>
   ** Because the overlay component is shown above the component and its child
   ** components, if any of the components repaint, the overlay component will
   ** be covered if the overlay component doesn't know to repaint immediately.
   ** Due to way Swing repaintManager works, there isn't any other better way to
   ** solve the issue other than the component has code to trigger the repaint
   ** of the overlay component. That's one reason we provide this way to repaint
   ** the overlay component easily.
   ** <p>
   ** See below for an example of how to prepare the component to be ready for
   ** the Overlay.
   ** <pre>
   **   public OverlayTextField() {
   **     ...
   **     public void repaint(long tm, int x, int y, int width, int height) {
   **       super.repaint(tm, x, y, width, height);
   *        OverlayUtils.repaintOverlay(this);
   *      }
   **     ...
   ** </pre>
   **
   ** @param  component          the component that has an {@link Overlay} and
   **                            needs to repaint the overlay.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   */
  public static void repaint(final JComponent component) {
    Overlay overlay = first(component);
    if (overlay != null && overlay instanceof Component)
      ((Component)overlay).repaint();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repaintAll
  /**
   ** Repaints all the Overlays associated with the component or its parents.
   **
   ** @param  component          the component that has an {@link Overlay} and
   **                            needs to repaint the overlays.
   **                            <br>
   **                            Allowed object is {@link JComponent}.
   */
  public static void repaintAll(final JComponent component) {
    Overlay[] overlays = all(component);
    for (Overlay cursor : overlays) {
      if (cursor != null && cursor instanceof Component)
        ((Component)cursor).repaint();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   predefinedIcon
  /**
   ** Returns the predefined icon that can be used as the overlay icon for the
   ** Swing component. Available icon names are
   ** <ul>
   **   <li>{@link OverlayIconFactory#ATTENTION}
   **   <li>{@link OverlayIconFactory#CORRECT}
   **   <li>{@link OverlayIconFactory#ERROR}
   **   <li>{@link OverlayIconFactory#FILTER}
   **   <li>{@link OverlayIconFactory#INFO}
   **   <li>{@link OverlayIconFactory#LINK}
   **   <li>{@link OverlayIconFactory#LOCKED}
   **   <li>{@link OverlayIconFactory#QUESTION}
   ** </ul>
   **
   ** @param  name               the name defined in {@link OverlayIconFactory}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the icon.
   **                            <br>
   **                            Possible object is {@link Icon}.
   */
  public static Icon predefinedIcon(final String name) {
    return OverlayIconFactory.create(name);
  }
}