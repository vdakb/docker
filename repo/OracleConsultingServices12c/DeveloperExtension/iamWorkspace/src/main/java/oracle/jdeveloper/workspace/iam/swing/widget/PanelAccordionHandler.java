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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   PanelAccordionHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PanelAccordionHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

////////////////////////////////////////////////////////////////////////////////
// class PanelAccordionHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** A <code>PanelAccordionHandler</code> performs the action to expand/collpase
 ** panels that are controled by a {@link PanelAccordion}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class PanelAccordionHandler implements MouseListener {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final PanelAccordion accordion;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates an instance of the general-purpose handler for the specified
   ** {@link PanelAccordionHandler}.
   **
   ** @param  accordion               the {@link PanelAccordion}.
   */
  public PanelAccordionHandler(final PanelAccordion accordion) {
    // ensure inheritance
    super();

    // initilaize istance
    this.accordion = accordion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented innterfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mousePressed (MouseListener)
  /**
   ** Invoked when a mouse button has been pressed on a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse pressed event.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This inner class is actually the Swing way, but since JTree is still based
   ** on the AWT mechanism for event handling (what causes the last added
   ** listener to be invoked last) its absolutly necessary that this listener
   ** is the first in the chain of mouse listeners'. This can be achieved by:
   ** <pre>
   **   this.handler = createHandler();
   **   Utilities.insertMouseListener(this, this.handler, 0);
   ** </pre>
   ** We use the mousePressed method instead of mouseClicked for performance
   ** reason.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mousePressed(final MouseEvent event) {
    this.accordion.toggle();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseReleased (MouseListener)
  /**
   ** Invoked when a mouse button has been released on a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse released event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseReleased(final MouseEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseClicked (MouseListener)
  /**
   ** Invoked when the mouse button has been clicked (pressed and released) on a
   ** component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse clicked event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseClicked(final MouseEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseEntered (MouseListener)
  /**
   ** Invoked when the mouse enters a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse entered event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseEntered(final MouseEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mouseExited (MouseListener)
  /**
   ** Invoked when the mouse exits a component.
   ** <p>
   ** See the class description for {@link MouseEvent} for a definition of a
   ** mouse exits event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link MouseEvent}.
   */
  @Override
  public void mouseExited(final MouseEvent event) {
    // intentionally left blank
  }
}