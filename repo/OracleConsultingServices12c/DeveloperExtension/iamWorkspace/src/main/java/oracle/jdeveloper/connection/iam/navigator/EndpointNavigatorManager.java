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

    File        :   EndpointNavigatorManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointNavigatorManager.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator;

import oracle.ide.IdeConstants;

import oracle.ide.controls.WaitCursor;

import oracle.ide.docking.DockStation;
import oracle.ide.docking.DockingParam;

import oracle.ide.navigator.NavigatorWindow;
import oracle.ide.navigator.NavigatorManager;

import oracle.ideri.navigator.DefaultNavigatorManager;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointNavigatorManager
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A navigator manager for the custom "Endpoint" navigator.
 ** <br>
 ** The <code>EndpointNavigatorManager</code> is responsible for managing the
 ** creation of the system navigator and transient navigators opened on selected
 ** nodes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointNavigatorManager extends DefaultNavigatorManager {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                 view;
  private final String                 name;
  private final EndpointNavigatorModel model;

  private WaitCursor                   waitCursor = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointNavigatorManager</code> that allows use
   ** as a JavaBean.
   **
   ** @param  view               the name of the View type managed by this
   **                            manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the View type managed by this
   **                            manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  model              the {@link EndpointNavigatorModel}
   **                            attached to this navigator.
   **                            <br>
   **                            Allowed object is
   **                            {@link EndpointNavigatorModel}.
   */
  protected EndpointNavigatorManager(final String view, final String name, final EndpointNavigatorModel model) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.view  = view;
    this.name  = name;
    this.model = model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getViewCategory (MultiManager)
  /**
   ** Return the name of the View type managed by this manager as it would
   ** appear in a ViewId instance.
   ** <p>
   ** The view category for dockable views created by this navigator manager.
   ** <br>
   ** Even if there are multiple navigators, they all share the same view#
   ** category.
   **
   ** @return                    the name of the View type managed by this
   **                            manager.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected final String getViewCategory() {
    return this.view;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultName (MultiManager)
  /**
   ** Returns the name of the default View instance managed by this manager as
   ** it would appear in a ViewId instance.
   **
   ** @return                    the name of the default View instance managed
   **                            by this manager.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  protected final String getDefaultName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNavigatorWindow (NavigatorManager)
  /**
   ** This is the method that actually creates a new instance of
   ** DefaultNavigatorWindow.
   ** <br>
   ** We override it here to create our custom subclass (required in order to
   ** use a different controller to handle the standard Delete action).
   **
   ** @return                    a new instance of DefaultNavigatorWindow.
   **                            <br>
   **                            Possible object is {@link NavigatorWindow}.
   */
  @Override
  protected final NavigatorWindow createNavigatorWindow() {
    return createNavigatorWindow(this.model, false, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waitCursor
  /**
   ** Returns the appropriate wait cursor shape.
   **
   ** @return                    the appropriate wait cursor shape.
   **                            <br>
   **                            Possible object is {@link WaitCursor}.
   */
  public WaitCursor waitCursor() {
    if (this.waitCursor == null) {
      synchronized (this) {
        if (this.waitCursor == null) {
          // attach a WaitCursor to the window UI of this navigator
          this.waitCursor = new WaitCursor(getNavigatorWindow().getViewWithoutDecoration().getGUI());
        }
      }
    }
    return this.waitCursor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureNavigator (overridden)
  /**
   ** Decorates the navigator window with the toolbar.
   **
   ** @param  window             the navigator window to decorate.
   **                            <br>
   **                            Allowed object is {@link NavigatorWindow}.
   */
  @Override
  protected void configureNavigator(final NavigatorWindow window) {
    // ensure inheritance
    super.configureNavigator(window);

    // set the toolbar visisble by default
    window.setToolbarVisible(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNavigatorDockingParam (overridden)
  /**
   ** We <b>must</b> override this to tell the docking subsystem where to dock
   ** this navigator by default. (it's arguably a bug in the ide that it throws
   ** an exception if you don't).
   **
   ** @return                    the the parameter to dock the navigator.
   **                            <br>
   **                            Possible object is {@link DockingParam}.
   */
  @Override
  protected DockingParam createNavigatorDockingParam() {
    final NavigatorManager mgr = NavigatorManager.getApplicationNavigatorManager();
    final DockingParam     prm = super.createNavigatorDockingParam();
    prm.setPosition(DockStation.getDockStation().getDockable(mgr.getDefaultViewId()), IdeConstants.TABBED, IdeConstants.WEST);
    return prm;
  }
}