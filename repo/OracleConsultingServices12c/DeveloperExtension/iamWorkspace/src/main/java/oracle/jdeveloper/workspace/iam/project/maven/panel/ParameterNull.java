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
    Subsystem   :   Identity and Access Management Facility

    File        :   ParameterNull.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ParameterNull.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven.panel;

import java.awt.Component;

import javax.swing.JLabel;

import javax.swing.tree.TreePath;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.ide.net.URLTextField;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.jdeveloper.workspace.iam.Bundle;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNode;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNodeData;

import oracle.jdeveloper.workspace.iam.project.maven.ModelBundle;

////////////////////////////////////////////////////////////////////////////////
// class ParameterNull
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the {@link Configurator} dialog for
 ** display a non-data node.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
class ParameterNull extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4357652861047188755")
  private static final long            serialVersionUID = -4025969888830079986L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient OptionTreeNodeData data;

  // the properties to configure the folder location of the generated build file
  private transient URLTextField       folderEdit;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ParameterNull</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ParameterNull() {
    // ensure inheritance
    super();

    // initialize UI components
    initializeComponent();
    initializeLayout();
    localizeComponent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultFocusComponent
  /**
   ** Returns the component that should be focued this panel is vesible for the
   ** first time.
   **
   ** @return                    the component that should be focued this panel
   **                            is vesible for the first time.
   */
  public Component defaultFocusComponent() {
    return this.folderEdit;
 }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onEntry (overridden)
  /**
   ** This method is called when the <code>Traversable</code> is being entered.
   ** <p>
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the specified {@link TraversableContext}.
   ** <p>
   ** When the same <code>Traversable</code> is entered more than once in the
   ** course of interacting with the user, the <code>Traversable</code> needs to
   ** reload the data directly from the {@link TraversableContext} rather than
   ** caching data objects. Some reasons for this include:
   ** <ul>
   **   <li>Other <code>Traversable</code> may edit the data objects or even
   **       replace them.
   **   <li>The same <code>Traversable</code> instance may be used for editing
   **       multiple different instances of the same object type.
   ** </ul>
   ** Loading data directly from the {@link TraversableContext} is the best way
   ** to ensure that the <code>Traversable</code> will not be editing the wrong
   ** data.
   ** <p>
   ** The <code>Traversable</code> should not even cache references to data
   ** objects between invocations of onEntry and
   ** {@link #onExit(TraversableContext)} because the UI container is not
   ** required to guarantee that the references will be identical.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   */
  @Override
  public void onEntry(final TraversableContext context) {
    // ensure inheritance
    super.onEntry(context);

    // On entering the panel, we need to populate all fields with properties
    // from the model object.
    this.data = model(context);

    this.folderEdit.setText(this.data.text());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExit (overridden)
  /**
   ** This method is called when the <code>Traversable</code> is being exited.
   ** <p>
   ** At this point, the <code>Traversable</code> should copy the data from its
   ** associated UI back into the data structures in the
   ** {@link TraversableContext}.
   ** <p>
   ** If the <code>Traversable</code> should not be exited because the user has
   ** entered either incomplete, invalid, or inconsistent data, then this method
   ** can throw a {@link TraversalException} to indicate to the property dialog
   ** or wizard that validation failed and that the user should not be allowed
   ** to exit the current <code>Traversable</code>. Refer to the
   ** {@link TraversalException} javadoc for details on passing the error
   ** message that should be shown to the user.
   **
   ** @param  context            the data object where changes made in the UI
   **                            should be copied so that the changes can be
   **                            accessed by other <code>Traversable</code>s.
   **
   ** @throws TraversalException if the user has entered either incomplete,
   **                            invalid, or inconsistent data.
   **                            <p>
   **                            This exception prevents the property dialog or
   **                            wizard from continuing and forces the user to
   **                            stay on the current <code>Traversable</code>
   **                            until the data entered is valid or the user
   **                            cancels. The exception class itself is capable
   **                            of carrying an error message that will be shown
   **                            to the user. Refer to its javadoc for details.
   */
  @Override
  public void onExit(final TraversableContext context)
    throws TraversalException {

    // ensure inheritance
    super.onExit(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** This gets a defensive copy of the preferences being used.
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
  protected OptionTreeNodeData model(final TraversableContext context) {
    // This gets a defensive copy of the data container being used.
    TreePath       path = (TreePath)context.getDesignTimeObject(Configurator.SELECTION_PATH);
    OptionTreeNode node = (OptionTreeNode)path.getLastPathComponent();
    return (OptionTreeNodeData)node.getUserObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  private void initializeComponent() {
    // the properties to configure the folder whare the file will be generated
    this.folderEdit = new URLTextField();
    this.folderEdit.setEditable(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (DefaultPreferencePanel)
  /**
   ** Localizes human readable text for all controls.
   */
  private void localizeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (DefaultPreferencePanel)
  /**
   ** Layout the panel.
   */
  private void initializeLayout() {
    FormLayout      layout     = new FormLayout(
    //   1     2     3       4        5     6    7
      "4dlu, pref, 6dlu, pref:grow, 3dlu, pref, 4dlu"
    , "4dlu, pref, 6dlu, pref, 1dlu, pref, 4dlu"
    //   1     2     3     4    5
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.GENERAL_HEADER), constraint.xyw(2, row, 5));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(ModelBundle.string(ModelBundle.BUILDFILE_FOLDER_LABEL), constraint.xyw(4, row++, 4));
    label.setLabelFor(this.folderEdit);
    builder.add(this.folderEdit, constraint.xy(4, row));
  }
}
