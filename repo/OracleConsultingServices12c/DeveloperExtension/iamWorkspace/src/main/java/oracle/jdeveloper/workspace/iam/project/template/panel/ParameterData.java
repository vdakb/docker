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

    File        :   ParameterData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ParameterData.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template.panel;

import java.io.File;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import javax.swing.tree.TreePath;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.dialogs.MessageDialog;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.util.ResourceUtils;

import oracle.ide.net.URLTextField;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.jdeveloper.workspace.iam.Bundle;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.model.AbstractProperty;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.swing.widget.ScrollAdapter;
import oracle.jdeveloper.workspace.iam.swing.widget.ParameterForm;
import oracle.jdeveloper.workspace.iam.swing.widget.ParameterGroup;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNode;

import oracle.jdeveloper.workspace.iam.project.template.TemplateData;
import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;

////////////////////////////////////////////////////////////////////////////////
// class ParameterData
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the {@link Configurator} dialog for
 ** editing the properties stored in the {@link TemplateData} model.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class ParameterData extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8315925832795289780")
  private static final long        serialVersionUID = 1677516039126208526L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the folder location of the generated build file
  private transient URLTextField   folderEdit;
  private transient JButton        folderBrowse;

  // the properties to configure the name of the generated build file
  private transient JTextField     fileEdit;

  // the properties to configure the substituton values
  private transient ParameterForm  parameterForm;

  private transient TemplateData   data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ParameterData</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ParameterData() {
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
    return this.fileEdit;
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnabled
  /**
   ** Sets whether or not this component is enabled.
   ** <p>
   ** A component that is enabled may respond to user input, while a component
   ** that is not enabled cannot respond to  user input. Some components may
   ** alter their visual representation when they are disabled in order to
   ** provide feedback to the user that they cannot take input.
   **
   ** @param  state              <code>true</code> if this component should be
   **                            enabled, <code>false</code> otherwise
   */
  @Override
  public void setEnabled(final boolean state) {
    // disable its children
    this.folderEdit.setEditable(state);
    this.folderBrowse.setEnabled(state);
    this.fileEdit.setEditable(state);
    this.parameterForm.setEnabled(state);
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
    this.data = template(context);

    this.folderEdit.setText(this.data.folder().folder().getAbsolutePath());
    this.fileEdit.setText(this.data.name());

    this.parameterForm.clearCanvas();

    if (this.data instanceof TemplateStream) {
      TemplateStream stream = (TemplateStream)this.data;
      stream.evaluateIncludes();
      for (AbstractProperty property : stream.property())
        this.parameterForm.addProperty(property);
    }

    refresh();
    this.parameterForm.revalidate();
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

    commit(false);
    // ensure inheritance
    super.onExit(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Updates the state of the panel due to changes in the underlying data
   ** model.
   */
  public void refresh() {
    setEnabled(this.data.hotspotSelected());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
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
  protected TemplateData template(final TraversableContext context) {
    // this gets a defensive copy of the data container being used.
    TreePath       path = (TreePath)context.getDesignTimeObject(Configurator.SELECTION_PATH);
    OptionTreeNode node = (OptionTreeNode)path.getLastPathComponent();
    return (TemplateData)node.getUserObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Called to have this {@link DefaultTraversablePanel} perform the commit
   ** action.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if all changes are applied
   **                            succesfully; <code>false</code> otherwise.
   */
  protected void commit(final boolean destructive)
    throws TraversalException {

    commitFolder(destructive);
    commitName(destructive);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitFolder
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>folder</code> must be evaluate to a
   ** valid directory.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitFolder(final boolean destructive)
    throws TraversalException {

    final File value = new File(this.folderEdit.getText());
    if (!validateFolder(value)) {
      // notify the user about an unanticipated condition that prevents the
      // task from completing successfully
      if (!confirmViolation(TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_TITLE), TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_FAILURE), destructive))
        throw new TraversalException(TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_TITLE));
    }

    if (!this.data.hotspotSelected() && !value.exists()) {
      // notify the user about an unanticipated condition that prevents the
      // task from completing successfully
      if (!confirmViolation(TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_TITLE), TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_FAILURE), destructive))
        throw new TraversalException(TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_TITLE));
    }

    if (!this.data.folder().folder().equals(value))
      this.data.folder().folder(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>name</code> must be evaluate to a valid
   ** filename.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitName(final boolean destructive)
    throws TraversalException {

    final String value = this.fileEdit.getText().trim();
    if (StringUtility.empty(value)) {
      if (!confirmViolation(TemplateBundle.string(TemplateBundle.BUILDFILE_NAME_TITLE), TemplateBundle.string(TemplateBundle.BUILDFILE_NAME_FAILURE), destructive))
        throw new TraversalException(TemplateBundle.string(TemplateBundle.BUILDFILE_NAME_TITLE));
    }

    if (!this.data.name().equals(value))
      this.data.name(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmViolation
  /**
   ** Notifies the user the a validation violation has to be confirmed.
   ** <p>
   ** The confirmation alert is used to warn the user about the consequences of
   ** the action, giving the user a chance to accept the consequences, avoid
   ** them, or resolve them in some other fashion.
   **
   ** @param  option             the option that is subject of the violation
   ** @param  message            a detaied maesage that explain the violation deeper
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if the user clicked
   **                            <i>Yes</i> option, <code>false</code> otherwise.
   */
  private final boolean confirmViolation(final String option, final String message, final boolean destructive)  {
    // notify the user about an unanticipated condition that prevents the task
    // from completing successfully
    if (destructive) {
      MessageDialog.critical(this, message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option), null);
      return false;
    }
    else
      return MessageDialog.confirm(this, message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option), null, destructive, ComponentBundle.string(ComponentBundle.IGNORE), ComponentBundle.string(ComponentBundle.CANCEL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  private void initializeComponent() {
    // the properties to configure the folder whare the file will be generated
    this.folderEdit    = new URLTextField();
    this.folderBrowse  = new JButton();

    // the properties to configure the name of the build file
    this.fileEdit      = new JTextField();

    // the properties to configure the substituton values
    this.parameterForm = new ParameterGroup();

    this.folderBrowse.addActionListener(this.folderEdit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (DefaultPreferencePanel)
  /**
   ** Localizes human readable text for all controls.
   */
  private void localizeComponent() {
    ResourceUtils.resButton(this.folderBrowse, TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_BROWSE));
    IconicButtonUI.install(this.folderBrowse);
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
    , "4dlu, pref, 6dlu, pref, 1dlu, pref, 4dlu, pref, 1dlu, pref, 4dlu, pref, top:pref:grow, 4dlu"
    //   1     2     3     4    5     6     7      8     9    10    11    12          13          14
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.GENERAL_HEADER), constraint.xyw(2, row, 5));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(TemplateBundle.string(TemplateBundle.BUILDFILE_FOLDER_LABEL), constraint.xyw(4, row++, 4));
    label.setLabelFor(this.folderEdit);
    builder.add(this.folderEdit,   constraint.xy( 4, ++row));
    builder.add(this.folderBrowse, constraint.xy( 6,   row));

    // the 3rd logical row of the layout
    row += 2;
    label = builder.addLabel(TemplateBundle.string(TemplateBundle.BUILDFILE_NAME_LABEL), constraint.xyw(4, row++, 4));
    label.setLabelFor(this.fileEdit);
    builder.add(this.fileEdit,      constraint.xy( 4, ++row));

    // the 5th logical row of the layout
    row += 2;
    builder.addSeparator(TemplateBundle.string(TemplateBundle.BUILDFILE_PROPERTIES), constraint.xyw(2, row, 5));

    // the 6th logical row of the layout
    JScrollPane scroller = new JScrollPane(this.parameterForm);
    scroller.setBorder(BorderFactory.createLineBorder(Color.RED));

    ScrollAdapter adapter = new ScrollAdapter(scroller);
    adapter.scrollableInsets(new Insets(5, 0, 5, 0));
    builder.add(this.parameterForm, constraint.xy(4, ++row));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateFolder
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>folder</code> must be evaluate to a valid
   ** directory that is readable.
   **
   ** @param  folder             the {@link File} of the folder to validate.
   **
   ** @return                    <code>true</code> if the specified {@link File}
   **                            meets all requirements of an accessible folder.
   */
  private boolean validateFolder(final File folder) {
    if (folder == null)
      return false;

    return (folder.exists() && folder.isDirectory() && folder.canRead());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateFile
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>folder</code> must be evaluate to a valid
   ** directory that is readable.
   **
   ** @param  file               the {@link File} of the file to validate.
   **
   ** @return                    <code>true</code> if the specified {@link File}
   **                            meets all requirements of an accessible file.
   */
  private boolean validateFile(final File file) {
    if (file == null)
      return false;

    return (file.exists() && file.isFile() && file.canRead());
  }
}