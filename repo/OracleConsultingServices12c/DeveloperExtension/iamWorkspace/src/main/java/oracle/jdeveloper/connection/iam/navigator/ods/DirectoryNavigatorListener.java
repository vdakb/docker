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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryNavigatorListener.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryNavigatorListener.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.ods;

import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JList;

import javax.swing.tree.TreePath;

import oracle.ide.model.Element;

import oracle.ide.view.ViewSelectionEvent;
import oracle.ide.view.ViewSelectionListener;

import oracle.ideimpl.explorer.CustomTree;

import oracle.jdevimpl.wizard.tree.NavigatorTreeNode;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryNavigatorListener
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to control
 ** explorer nodes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryNavigatorListener implements ViewSelectionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The <code>DirectoryNavigatorListener</code> implements the singleton
   ** pattern.
   ** <br>
   ** The static attribute {@link #instance} holds this single instance.
   */
  static DirectoryNavigatorListener instance = new DirectoryNavigatorListener();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean         rightClick;
  private boolean         mousePressed;
  private boolean         mouseReleased;
  private boolean         selectionChanged;
  private EndpointElement selectionChange;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // class Adapter
  // ~~~~~ ~~~~~~~
  /**
   ** We override the DefaultNavigatorWindow just so that we can install a
   ** custom controller to handle the standard IDE Delete action. Arguably, the
   ** IDE should make it easier for us to override this behavior without having
   ** to create a subclass of DefaultNavigatorWindow.
   */
  public class Adapter extends MouseAdapter {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented innterfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: mouseClicked (MouseAdapter)
    /**
     ** Invoked when the mouse button has been clicked (pressed and released) on
     ** a component.
     ** <p>
     ** See the class description for {@link MouseEvent} for a definition of a
     ** mouse clicked event.
     **
     ** @param  event            the data that characterizes the event.
     **                          <br>
     **                          Allowed object is {@link MouseEvent}.
     */
    @Override
    public void mouseClicked(final MouseEvent event) {
      if (event.isConsumed()) {
        return;
      }
      Boolean singleClick = false;//DBConfig.getInstance().getBoolean("OPEN_ON_SINGLE_CLICK");
      if (!singleClick.booleanValue()) {
        if (event.getClickCount() != 2)
          return;
      }
      Object          object  = event.getSource();
      EndpointElement element = null;
      if (object instanceof CustomTree) {
        final CustomTree tree = (CustomTree)object;
        final TreePath   path = tree.getPathForLocation(event.getX(), event.getY());
        if (path != null) {
          element = DirectoryNavigatorListener.this.lastObject(tree);
        }
      }
      else if (object instanceof JList) {
        element = DirectoryNavigatorListener.this.lastObject(event.getPoint(), (JList)object);
      }
      if (element == null)
        return;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mousePressed (MouseAdapter)
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
     ** @param  event            the data that characterizes the event.
     **                          <br>
     **                          Allowed object is {@link MouseEvent}.
     */
    @Override
    public void mousePressed(final MouseEvent event) {
      DirectoryNavigatorListener.this.rightClick = event.getModifiers() == 4 || event.getModifiers() == 8;
      DirectoryNavigatorListener.this.doPendingChanges();
      DirectoryNavigatorListener.this.mousePressed = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mouseReleased (MouseAdapter)
    /**
     ** Invoked when a mouse button has been released on a component.
     ** <p>
     ** See the class description for {@link MouseEvent} for a definition of a
     ** mouse released event.
     **
     ** @param  event            the data that characterizes the event.
     **                          <br>
     **                          Allowed object is {@link MouseEvent}.
     */
    @Override
    public void mouseReleased(final MouseEvent event) {
      Object          object  = event.getSource();
      EndpointElement element = null;
      boolean         changed = false;
      if (object instanceof CustomTree) {
        CustomTree customTree = (CustomTree)event.getSource();
        element = DirectoryNavigatorListener.this.lastObject(customTree);
        changed = customTree.getPathForLocation(event.getX(), event.getY()) != null;
      }
      else if (object instanceof JList) {
        element = DirectoryNavigatorListener.this.lastObject(event.getPoint(), (JList)object);
        changed = element != null;
      }
      if (element != null) {
        DirectoryNavigatorListener.this.selectionChange = element;
      }
      if (DirectoryNavigatorListener.this.selectionChanged && changed) {
        DirectoryNavigatorListener.this.doPendingChanges();
        return;
      }
      DirectoryNavigatorListener.this.mouseReleased = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: mouseDragged (overridden)
    /**
     ** Invoked when a mouse button is pressed on a component and then dragged. 
     ** <code>MOUSE_DRAGGED</code> events will continue to be delivered to the
     ** component where the drag originated until the mouse button is released
     ** (regardless of whether the mouse position is within the bounds of the
     ** component).
     ** <p>
     ** Due to platform-dependent Drag&amp;Drop implementations,
     ** <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     ** Drag&amp;Drop operation.
     **
     ** @param  event            the data that characterizes the event.
     **                          <br>
     **                          Allowed object is {@link MouseEvent}.
     */
    @Override
    public void mouseDragged(final MouseEvent event) {
      DirectoryNavigatorListener.this.doPendingChanges();
      DirectoryNavigatorListener.this.mousePressed = true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryNavigatorListener</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private DirectoryNavigatorListener() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewSelectionChanged (ViewSelectionListener)
  /**
   ** This method is called every time the selection changes in a view.
   ** <br>
   ** The {@link ViewSelectionEvent} object has detailed information of the
   ** objects selected in the view.
   **
   ** @param  event              the {@link ViewSelectionEvent} that
   **                            characterizes the event.
   **                            <br>
   **                            Allowed object is {@link ViewSelectionEvent}.
   */
  @Override
  public void viewSelectionChanged(final ViewSelectionEvent event) {
    final Element[] selection = event.getSelection();
    if (selection == null)
      return;

    if (selection.length != 1)
      return;

//    if (DBConfig.getInstance().getBoolean("OPEN_ON_SINGLE_CLICK") == false)
//      return;

    final Element element = selection[0];
    if (!endpointNode(element))
      return;
    this.selectionChange = (EndpointElement)element;
    boolean toggle = false;
    if (!this.selectionChanged && this.mousePressed && (!this.mouseReleased || this.rightClick)) {
      toggle = true;
    }
    if (toggle) {
      this.selectionChanged = true;
      return;
    }
    doPendingChanges();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>DirectoryNavigatorListener</code> is a singleton class.
   ** This method gets this manager's single instance.
   **
   ** @return                    the <code>DirectoryNavigatorListener</code>
   **                            single instance.
   **                            <br>
   **                            Possible object is
   **                            {@link DirectoryNavigatorListener}.
   */
  public static synchronized DirectoryNavigatorListener instance() {
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointNode
  /**
   ** Determines if the given {@link Element} falls in the range of supported
   ** elements.
   **
   ** @param  element            the {@link Element} to test.
   **
   ** @return                    <code>true</code> if the given {@link Element}
   **                            falls in the range of supported elements;
   **                            otherwise <code>false</code>.
   */
  private boolean endpointNode(final Element element) {
    return element instanceof EndpointElement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doPendingChanges
  private void doPendingChanges() {
    this.mousePressed     = false;
    this.mouseReleased    = false;
    this.selectionChange  = null;
    this.selectionChanged = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastObject
  private EndpointElement lastObject(final CustomTree control) {
    if (control.getSelectionCount() <= 0)
      return null;

    final NavigatorTreeNode node = (NavigatorTreeNode)control.getLastSelectedPathComponent();
    final Element           elem = (Element)node.getUserObject();
    return (elem instanceof EndpointElement) ? (EndpointElement)elem : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastObject
  private EndpointElement lastObject(final Point point, final JList control) {
    EndpointElement element = null;
    int n = control.locationToIndex(point);
    if (n == -1)
      return element;

    final Rectangle rectangle = control.getCellBounds(n, n);
    if (!rectangle.contains(point))
      return element;

    Object e = control.getModel().getElementAt(n);
    if (!(e instanceof EndpointElement))
      return element;

    return (EndpointElement)e;
  }
}