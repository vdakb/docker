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
    Subsystem   :   Identity Manager Facility

    File        :   IdentityNavigatorRoot.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityNavigatorRoot.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.navigator;

import java.util.Map;

import javax.swing.Icon;

import javax.naming.Referenceable;
import javax.naming.NamingException;

import oracle.adf.rc.core.RCSession;
import oracle.adf.rc.core.RCInstance;

import oracle.adf.rc.exception.CatalogException;

import oracle.adf.share.jndi.AdfJndiContext;
import oracle.adf.share.jndi.ConnectionException;

import oracle.ide.util.Assert;

import oracle.ide.model.Element;

import oracle.ide.controls.WaitCursor;

import oracle.jdeveloper.connection.iam.adapter.EndpointContextAdapter;

import oracle.jdeveloper.connection.iam.navigator.EndpointNavigatorRoot;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointException;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointFolder.State;
import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.connection.oim.model.IdentityServer;

import oracle.jdeveloper.connection.oim.adapter.IdentityContextAdapter;
import oracle.jdeveloper.connection.oim.service.IdentityServiceException;

////////////////////////////////////////////////////////////////////////////////
// class IdentityNavigatorRoot
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The element representing the Root of the Identity Service Navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityNavigatorRoot extends EndpointNavigatorRoot {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2064508046418361762")
  private static final long serialVersionUID = 4885054241561093672L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final RCSession  session;
  volatile boolean created = false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryNavigatorRoot</code> with name that belongs
   ** to the specified {@link RCSession}.
   **
   ** @param   session           the session context to obrtain the connections
   **                            from
   */
  public IdentityNavigatorRoot(final RCSession session) {
    // ensure inheritance
    super(Bundle.string(Bundle.IDENTITY_NODE_LABEL));

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
    return Manifest.icon(Manifest.IDENTITY_SERVER_ICON);
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
    return IdentityNavigatorManager.instance().waitCursor();
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
  /**
   ** Initialize the context of the navigator root node by population the
   ** connection descriptors.
   */
  @Override
  protected void createContext() {
    if (this.created) {
      return;
    }

    final RCInstance instance = this.session.getRCInstance();
    synchronized (this.contexts) {
      try {
        final AdfJndiContext         context = instance.getConnectionContext();
        final EndpointContextAdapter adapter = new IdentityContextAdapter(context);
        for (Map.Entry<String, Referenceable> e : adapter.connectionProviders()) {
          final IdentityServiceRoot root = new IdentityServiceRoot((IdentityServer)e.getValue(), this);
          this.contexts.put(e.getKey(), root);
        }
      }
      catch (CatalogException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_ABORT), e);
        this.contexts.put("name", ee);
      }
      catch (ConnectionException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_GENERAL), e);
        this.contexts.put("name", ee);
      }
      catch (IdentityServiceException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_ABORT), e);
        this.contexts.put("name", ee);
      }
      this.created = true;
      fireStructureChanged();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (EndpointNavigatorRoot)
  /**
   ** Performs all action to add an {@link IdentityServiceRoot} into the JNDI
   ** context.
   **
   ** @param  name               the {@link IdentityServiceRoot} to add.
   */
  @Override
  protected void add(final String name) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (EndpointNavigatorRoot)
  /**
   ** Performs all action to remove an {@link IdentityServiceRoot} from the JNDI
   ** context.
   **
   ** @param  name               the {@link IdentityServiceRoot} to remove.
   */
  @Override
  protected void remove(final String name) {
    final RCInstance instance = this.session.getRCInstance();
    synchronized (this.contexts) {
      try {
        // this action will trigger the listener
        instance.getConnectionContext().unbind(name);
        final Element e = this.contexts.remove(name);
        Assert.check(e != null);
        fireStructureChanged();
      }
      catch (CatalogException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_ABORT), e);
        this.contexts.put(name, ee);
      }
      catch (NamingException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_GENERAL), e);
        this.contexts.put(name, ee);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (overridden)
  /**
   ** Performs all action to refresh an element.
   */
  @Override
  public void refresh() {
    this.created = false;
    createContext();
    // ensure inheritance
    super.refresh();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reload
  /**
   ** Performs all action to refresh an element.
   */
  protected void reload() {
    state(State.NOTLOADED, State.LOADED);
    populateForeground();
  }
}