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

    Copyright © 2021. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   InventoryNavigatorRoot.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    InventoryNavigatorRoot.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-04-10  DSteding    First release version
*/

package oracle.jdeveloper.deployment.oim.navigator;

import javax.swing.Icon;

import oracle.adf.rc.core.RCInstance;
import oracle.adf.rc.core.RCSession;

import oracle.ide.util.Assert;

import oracle.ide.controls.WaitCursor;

import oracle.ide.model.Element;

import oracle.jdeveloper.connection.iam.navigator.EndpointNavigatorRoot;

import oracle.jdeveloper.deployment.oim.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class InventoryNavigatorRoot
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The element representing the Root of the Inventory Navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class InventoryNavigatorRoot extends EndpointNavigatorRoot {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3316266951868454675")
  private static final long serialVersionUID = -4892173163200227268L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final RCSession  session;
  volatile boolean created = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryNavigatorRoot</code> with name that belongs
   ** to the specified {@link RCSession}.
   **
   ** @param   session           the session context to obrtain the connections
   **                            from
   */
  public InventoryNavigatorRoot(final RCSession session) {
    // ensure inheritance
    super(Bundle.string(Bundle.INVENTORY_NODE_LABEL));

    // initialize instance attributes
    this.session = session;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isIdeDefault (overridden)
  /**
   ** Determines if this context is a default context belonging to the IDE or
   ** if its a application specific context.
   ** <br>
   ** Return always <code>true</code>.
   **
   ** @return                  <code>true</code> if this context is a default
   **                          context belonging to the IDE or if its a
   **                          application specific context.
   */
  @Override
  public boolean isIdeDefault() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (overridden)
  /**
   ** Returns the {@link Icon} that represents the <code>EndpointRoot</code> in
   ** the UI.
   **
   ** @return                    the {@link Icon} of the
   **                            <code>EndpointNavigatorModel</code>.
   */
  @Override
  public Icon getIcon() {
    return Bundle.icon(Bundle.INVENTORY_HEADER_ICON);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waitCursor (EndpointElement)
  /**
   ** Returns the appropriate wait cursor shape.
   **
   ** @return                    the appropriate wait cursor shape.
   */
  @Override
  protected final WaitCursor waitCursor() {
    return InventoryNavigatorManager.instance().waitCursor();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session (EndpointNavigatorRoot)
  /**
   ** Returns the session established to the Resource Palette.
   **
   ** @return                    the session established to the Resource
   **                            Palette.
   */
  @Override
  public RCSession session() {
    return this.session;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContext (EndpointNavigatorRoot)
  @Override
  protected void createContext() {
    if (this.created) {
      return;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (EndpointNavigatorRoot)
  @Override
  protected void add(final String name) {
    final RCInstance instance = this.session.getRCInstance();
    synchronized (this.contexts) {
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (EndpointNavigatorRoot)
  @Override
  protected void remove(final String name) {
    synchronized (this.contexts) {
      final Element e = this.contexts.remove(name);
      Assert.check(e != null);
    }
  }
}
