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

    File        :   DirectoryNavigatorRoot.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryNavigatorRoot.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.ods;

import java.util.Map;

import javax.naming.Referenceable;

import javax.swing.Icon;

import oracle.adf.rc.core.RCSession;
import oracle.adf.rc.core.RCInstance;

import oracle.adf.rc.exception.CatalogException;

import oracle.adf.share.jndi.AdfJndiContext;
import oracle.adf.share.jndi.ConnectionException;

import oracle.ide.util.Assert;

import oracle.ide.controls.WaitCursor;

import oracle.ide.model.Element;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.adapter.DirectoryNodeAdapter;
import oracle.jdeveloper.connection.iam.adapter.EndpointContextAdapter;

import oracle.jdeveloper.connection.iam.model.DirectoryServer;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

import oracle.jdeveloper.connection.iam.navigator.EndpointNavigatorRoot;

import oracle.jdeveloper.connection.iam.navigator.context.DirectoryContext;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointException;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryNavigatorRoot
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The element representing the Root of the Directory Service Navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryNavigatorRoot extends EndpointNavigatorRoot {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7706580390024470579")
  private static final long serialVersionUID = -9050770416912225871L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final transient RCSession session;
  volatile boolean          created = false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryNavigatorRoot</code> with name that belongs
   ** to the specified {@link RCSession}.
   **
   ** @param   session           the session context to obrtain the connections
   **                            from
   */
  public DirectoryNavigatorRoot(final RCSession session) {
    // ensure inheritance
    super(Bundle.string(Bundle.DIRECTORY_NODE_LABEL));

    // initialize instance attributes
    this.session = session;
    attachListener();
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
    return Manifest.icon(Manifest.DIRECTORY_SERVER_ICON);
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
    return DirectoryNavigatorManager.instance().waitCursor();
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
    // any connection is stored and managed by the resource catalog and requires
    // a session to this catalog to populate the connections
    final RCInstance instance = this.session.getRCInstance();
    synchronized (this.contexts) {
      try {
        final AdfJndiContext         context = instance.getConnectionContext();
        final EndpointContextAdapter adapter = new DirectoryNodeAdapter(context);
        for (Map.Entry<String, Referenceable> e : adapter.connectionProviders()) {
          final DirectoryServer      srv  = (DirectoryServer)e.getValue();
          final DirectoryServiceRoot root = new DirectoryServiceRoot(this, srv.name(), DirectoryContext.build(srv));
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
      catch (DirectoryException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
        this.contexts.put("name", ee);
      }
      this.created = true;
      fireStructureChanged();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (EndpointNavigatorRoot)
  @Override
  protected void add(final String name) {
    final RCInstance instance = this.session.getRCInstance();
    synchronized (this.contexts) {
      try {
        final DirectoryServer      srv  = (DirectoryServer)instance.getConnectionContext().lookup(name);
        final DirectoryServiceRoot root = new DirectoryServiceRoot(this, srv.name(), DirectoryContext.build(srv));
        this.contexts.put(name, root);
      }
      catch (CatalogException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_ABORT), e);
        this.contexts.put(name, ee);
      }
      catch (ConnectionException e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_GENERAL), e);
        this.contexts.put(name, ee);
      }
      catch (Exception e) {
        Assert.printStackTrace(e);
        final EndpointException ee = new EndpointException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
        this.contexts.put(name, ee);
      }
    }
    fireStructureChanged();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (EndpointNavigatorRoot)
  @Override
  protected void remove(final String name) {
    synchronized (this.contexts) {
      final Element e = this.contexts.remove(name);
      Assert.check(e != null);
//      if ((e instanceof DirectoryServiceRoot))
//        ((DirectoryServiceRoot)e).release();
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
  // Method:   attachListener
  private synchronized void attachListener() {
    final RCInstance instance = this.session.getRCInstance();
    try {
      final AdfJndiContext         context = instance.getConnectionContext();
      final EndpointContextAdapter adapter = new DirectoryNodeAdapter(context);
      adapter.addContextListener(this.listener);
    }
    catch (CatalogException e) {
      e.printStackTrace();
    }
  }
}