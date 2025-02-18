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

import java.util.List;
import java.util.ArrayList;

import javax.swing.Icon;

import oracle.ide.model.Node;
import oracle.ide.model.Element;

import oracle.ide.controls.WaitCursor;

import oracle.jdeveloper.connection.iam.navigator.EndpointServiceRoot;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.oim.model.IdentityServer;

import oracle.jdeveloper.connection.oim.wizard.IdentityServerWizard;

import oracle.jdeveloper.connection.oim.service.IdentityService;
import oracle.jdeveloper.connection.oim.service.IdentityServiceException;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServiceRoot
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The navigator RootDSE of the connected Identity Service navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityServiceRoot extends    EndpointServiceRoot
                                 implements Manageable.Modifiable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:665569267786506168")
  private static final long serialVersionUID = -5200486140861982038L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>IdentityServiceRoot</code> that allows use as a
   ** JavaBean.
   **
   ** @param  server             the connection descriptor.
   **                            <br>
   **                            Allowed object is {@link IdentityServer}.
   ** @param  navigator          the Initial Context that owns this context.
   **                            <br>
   **                            Allowed object is {@link IdentityNavigatorRoot}.
   **
   ** @throws IdentityServiceException in the event of misconfiguration (such as
   **                                  failure to set an essential property) or
   **                                  if initialization fails.
   */
  public IdentityServiceRoot(final IdentityServer server, final IdentityNavigatorRoot navigator)
    throws IdentityServiceException {

    // ensure inheritance
    super(navigator, new IdentityService(server));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manageable (overridden)
  /**
   ** Returns the {@link Manageable} element.
   **
   ** @return                    the {@link Manageable} element.
   */
  @Override
  public IdentityService manageable() {
    return (IdentityService)super.manageable();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (overridden)
  /**
   ** Returns the {@link Icon} that represents the {@link Node} in the UI.
   **
   ** @return                    the {@link Icon} of the {@link Node}.
   */
  @Override
  public final Icon getIcon() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
  /**
   ** Provides the label that represents the {@link Node} in the navigator.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this {@link Node} and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the {@link Node}.
   */
  @Override
  public final String getShortLabel() {
    return id();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (overridden)
  /**
   ** Provides the long label that represents the connection element.
   **
   ** @return                    the human readable long label of the connection
   **                            element.
   */
  @Override
  public final String getLongLabel() {
    return manageable().serverContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the connection identifier.
   **
   ** @return                    the connection identifier.
   */
  public final String id() {
    return manageable().name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManageables (Manageable.Composite)
  @Override
  public List<Manageable> getManageables() {
    final List<Manageable> list = new ArrayList<Manageable>();
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Manageable.Removeable)
  /**
   ** Performs all action to remove this {@link Node}.
   */
  @Override
  public final void remove() {
    release();
    this.navigator.remove(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (Manageable.Modifiable)
  /**
   ** Performs all action to modify an element.
   **
   ** @return                  <code>true</code> if the modify activity
   **                          succeeded; <code>false</code> if the modify
   **                          activity was canceled.
   */
  @Override
  public final boolean modify() {
    if (IdentityServerWizard.modify(manageable().resource())) {
      release();
      return true;
    }
    return false;
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate (overridden)
  /**
   ** Performs all actions to load child elements.
   **
   ** @return                    the collection of child elemments belonging to
   **                            this {@link EndpointServiceRoot}.
   */
  @Override
  protected List<Element> populate() {
    final List<Element> element = new ArrayList<Element>();
    element.add(new IdentityServiceAdapter(this, manageable()));
    element.add(new IdentityServiceLookup(this, manageable()));
    element.add(new IdentityServiceITResource(this, manageable()));
    element.add(new IdentityServiceResource(this, manageable()));
    element.add(new IdentityServiceProcess(this, manageable()));
    return element;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Callback method to free up all allocated resources by this {@link Node}.
   */
  public final void release() {
  }
}