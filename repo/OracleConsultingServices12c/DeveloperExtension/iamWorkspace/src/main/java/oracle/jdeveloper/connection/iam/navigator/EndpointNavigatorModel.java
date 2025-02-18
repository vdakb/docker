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

    File        :   EndpointNavigatorModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointNavigatorModel.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Icon;

import oracle.ide.model.Element;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointFolder;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointNavigatorModel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A connection navigator model navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointNavigatorModel extends    EndpointFolder
                                             implements Manageable.Composite {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5638916834265231535")
  private static final long           serialVersionUID = 3615787967069377653L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  transient Icon                      icon;
  private boolean                     initialized      = false;
  private List<EndpointNavigatorRoot> connection       = new ArrayList<EndpointNavigatorRoot>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointNavigatorModel</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected EndpointNavigatorModel() {
    // ensure inheritance
    super(null, null);

    // initialize instance state
//    manageable(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (overridden)
  /**
   ** Returns the {@link Icon} that represents the
   ** <code>EndpointNavigatorModel</code> in the UI.
   **
   ** @return                    the {@link Icon} of the
   **                            <code>EndpointNavigatorModel</code>.
   */
  @Override
  public Icon getIcon() {
    if (this.icon == null) {
      synchronized (this) {
        if (this.icon == null) {
          this.icon = Manifest.icon(Manifest.DIRECTORY_SERVER_ICON);
        }
      }
    }
    return this.icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
  /**
   ** Provides the label that represents the Wizard in the Object Gallery.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Wizard and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   */
  @Override
  public String getShortLabel() {
    return Bundle.string(Bundle.DIRECTORY_NODE_LABEL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (overridden)
  /**
   ** Returns the current number of children in the folder.
   **
   ** @return                    the current number of children in the folder.
   */
  @Override
  public int size() {
    return base().size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   global
  public EndpointNavigatorRoot global() {
    synchronized (this.connection) {
      for (EndpointNavigatorRoot cursor : base()) {
        if (cursor.isIdeDefault()) {
          return cursor;
        }
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   base
  /**
   ** Returns the navigable roots that has been discovered as an unmodifiable
   ** Collection.
   **
   ** @return                    the unmodifiable Collection of discovered
   **                            {@link EndpointNavigatorRoot}s.
   */
  public List<EndpointNavigatorRoot> base() {
    synchronized (this.connection) {
      if (!this.initialized) {
        this.initialized = initialize();
      }
    }
    return Collections.unmodifiableList(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManageables (Manageable.Composite)
  /**
   ** Returns the collection of {@link Manageable} attached to this composite.
   **
   ** @return                    the collection of {@link Manageable} attached
   **                            to this composite.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Manageable}.
   */
  @Override
  public List<Manageable> getManageables() {
    final List<Manageable> manageables = new ArrayList<Manageable>();
    for (EndpointNavigatorRoot cursor : this.connection) {
      manageables.add(cursor.manageable());
    }
    return manageables;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  public void add(final EndpointNavigatorRoot connection) {
    synchronized (this.connection) {
      assert (!this.connection.contains(connection));
      this.connection.add(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  public void remove(final EndpointNavigatorRoot connection) {
    synchronized (this.connection) {
      assert (!this.connection.contains(connection));
      this.connection.remove(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  protected abstract boolean initialize();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate (overridden)
  /**
   ** Performs all actions to load child elements.
   **
   ** @return                    the collection of child elemments belonging to
   **                            this {@link EndpointFolder}.
   */
  @Override
  protected List<Element> populate() {
    return new ArrayList<Element>(base());
  }
}