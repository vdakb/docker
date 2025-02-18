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
    Subsystem   :   Access Manager Facility

    File        :   Panel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Panel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.project.agent;

import oracle.ide.Context;

import oracle.ide.panels.TraversableContext;

import oracle.ide.model.panels.ProjectSettingsTraversablePanel;

import oracle.jdeveloper.workspace.oam.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class Panel
// ~~~~~ ~~~~~
/**
 ** The {@link ProjectSettingsTraversablePanel} for customization of the project
 ** specific properties.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Panel extends ProjectSettingsTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:919183057498861427")
  private static final long     serialVersionUID = -3021673142644248237L;

  private static final String[] KEYS             = {
    "iam.deploymentdir"
  , "iam.platform"
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean            initialized         = false;
  private transient Context  context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Panel</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Panel() {
    // ensure inheritance
    super();

    // initialize UI components
    initializeLayout();
    localizeComponent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataKey (ProjectSettingsTraversablePanel)
  /**
   ** Returns the single key used to identify the project properties.
   **
   ** @return                    the unique key used to store and retrieve
   **                            project properties
   */
  @Override
  public String getDataKey() {
    return Manifest.KEY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPropertyKeys (ProjectSettingsTraversablePanel)
  /**
   ** Returns the array of property keys for this properties panel.
   ** <p>
   ** the panel contains UI for properties that span more than one
   ** HashStructure.
   **
   ** @return                    a array of property keys for this panel,
   **
   ** @see    #getDataKey
   */
  @Override
  public String[] getPropertyKeys() {
    return KEYS.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onProjectPanelEntry (ProjectSettingsTraversablePanel)
  /**
   ** Invoked when the panel is about to be displayed.
   **
   **  @param  context            the shared data context
   */
  @Override
  public void onProjectPanelEntry(final TraversableContext context) {
    this.context = context.get(Context.class);
    initializeComponent(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (PanelDelegate)
  /**
   ** Layout the panel.
   */
  private void initializeLayout() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  private void localizeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Callback method to initialize the UI components.
   **
   ** @param  context            the data object where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI or store the
   **                            changes made in the UI so that the changes can
   **                            be accessed by other <code>Traversable</code>s.
   */
  private void initializeComponent(final TraversableContext context) {
    if (!this.initialized) {
      this.initialized = true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** This gets a defensive copy of the preferences being used in the
   ** <b>Tools -&amp; Preferences</b> dialog.
   ** <p>
   ** The IDE framework takes care of making this copy, and applying it back to
   ** the real preferences store, or abandoning it if the user clicks
   ** <b>Cancel</b>.
   **
   ** @param  context            the data object where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI or store the
   **                            changes made in the UI so that the changes can
   **                            be accessed by other <code>Traversable</code>s.
   **
   ** @return                    the copy of the preferences being used by this
   **                            <code>Traversable</code>.
   */
  private Provider properties(final TraversableContext context) {
    return Provider.instance(context.getPropertyStorage());
  }
}