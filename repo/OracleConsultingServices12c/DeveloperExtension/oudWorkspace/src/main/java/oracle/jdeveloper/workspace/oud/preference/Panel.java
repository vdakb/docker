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
    Subsystem   :   Unified Directory Facility

    File        :   Panel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Panel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    11.1.1.3.37.60.70  2017-01-02  DSteding    Fixed behavior in onEntry method
                                               of the panel which overrides the
                                               URL to the foundation path every
                                               time because of the wrong if
                                               statement.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oud.preference;

import java.io.IOException;

import java.net.URL;

import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.util.ResourceUtils;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.URLTextField;

import oracle.ide.controller.IdeAction;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.command.ManageLibrary;

import oracle.jdeveloper.workspace.iam.preference.Provider;
import oracle.jdeveloper.workspace.iam.preference.FeaturePreference;
import oracle.jdeveloper.workspace.iam.preference.FeaturePreferencePanel;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;
import oracle.jdeveloper.workspace.iam.swing.list.MappedListCellRenderer;

import oracle.jdeveloper.workspace.oud.Bundle;
import oracle.jdeveloper.workspace.oud.Library;

////////////////////////////////////////////////////////////////////////////////
// class Panel
// ~~~~~ ~~~~~
/**
 ** The UI panel that provides support in the Preference dialog for editing the
 ** preferences stored in the {@link Provider} model.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 ** <p>
 ** In general, preferences panels are not supposed to be published APIs, so we
 ** enforce that. Even though the panel is constructed by the IDE framework
 ** using reflection, the IDE framework does not require that it is public (only
 ** that it has a no-argument constructor).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
final class Panel extends FeaturePreferencePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1091801055528354037")
  private static final long      serialVersionUID = -6011785401247516262L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the Default Workspace Folder
  private transient JTextField   workspaceEdit;

  // the properties to configure the Oracle Consulting Foundation Framework
  private transient URLTextField foundationEdit;
  private transient JButton      foundationBrowse;

  // the action elements to maintain the library artifacts inside of JDeveloper
  private transient JComboBox    releaseList;
  private transient JCheckBox    libraryMaven;
  private transient JButton      libraryGenerate;
  private transient JButton      libraryManager;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Panel</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Panel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preference (DefaultPreferencePanel)
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
  @Override
  protected FeaturePreference preference(final TraversableContext context) {
    return Store.instance(context.getPropertyStorage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    // the properties to configure the Default Workspace Folder
    this.workspaceEdit    = new JTextField();

    // the properties to configure the Oracle Consulting Foundation Framework
    this.foundationEdit   = new URLTextField();
    this.foundationBrowse = new JButton();

    // the action elements to maintain the artifacts inside of Oracle JDeveloper
    this.releaseList      = new JComboBox();
    this.libraryMaven     = new JCheckBox();
    this.libraryGenerate  = new JButton();
    this.libraryManager   = new JButton();

    this.foundationBrowse.addActionListener(this.foundationEdit);

    // the item listener to toggle the state of the library action items
    this.libraryMaven.addItemListener(
      new ItemListener() {
        public void itemStateChanged(final ItemEvent event) {
          final int state = event.getStateChange();
          Panel.this.libraryGenerate.setEnabled(state != ItemEvent.SELECTED);
          Panel.this.libraryManager.setEnabled(state  != ItemEvent.SELECTED);
        }
      }
    );

    this.libraryManager.addActionListener(IdeAction.find(ManageLibrary.action()));
    this.libraryGenerate.addActionListener(
      new ActionListener() {
        public void actionPerformed(final ActionEvent event) {
          if (commit(true))
            try {
              generateLibraries(Panel.this.provider.foundation(), Panel.this.provider.release());
            }
            catch (IOException e) {
              // TODO: provide better solution of exception handling
              e.printStackTrace();
            }
        }
      }
    );

    this.releaseList.setModel(new DefaultComboBoxModel(Library.RELEASE.keySet().toArray(new String[0])));
    this.releaseList.setRenderer(new MappedListCellRenderer(ComponentBundle.icon(ComponentBundle.VENDOR_ORACLE), Library.RELEASE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (DefaultPreferencePanel)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected void localizeComponent() {
    this.foundationBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.foundationBrowse.setToolTipText(Bundle.string(Bundle.FRAMEWORK_HINT));

    ResourceUtils.resButton(this.libraryMaven,    Bundle.string(Bundle.LIBRARIES_MAVEN));
    ResourceUtils.resButton(this.libraryGenerate, Bundle.string(Bundle.LIBRARIES_GENERATE));
    ResourceUtils.resButton(this.libraryManager,  Bundle.string(Bundle.LIBRARIES_MANAGE));

    IconicButtonUI.install(this.foundationBrowse);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (DefaultPreferencePanel)
  /**
   ** Layout the panel.
   */
  @Override
  protected void initializeLayout() {
    final FormLayout        layout      = new FormLayout(
    //   |     1     |     2     |       4        |     5      |     6      |     7      |
      "4dlu, pref, 6dlu, pref, 3dlu, pref:grow, 3dlu, 55dlu, 3dlu, 39dlu, 1dlu, 15dlu, 4dlu"
    , "6dlu, pref, 6dlu, pref, 1dlu, pref, 4dlu, pref, 1dlu, pref, 6dlu, pref, 4dlu, pref, 4dlu, pref"
    //   |     1     |     2    |     3      |     4     |     5     |     6     |     7     |     8
    );

    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.GENERAL_HEADER), constraint.xyw(2, row, 12));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(Bundle.string(Bundle.WORKSPACE_FOLDER), constraint.xyw(4, row++, 9));
    label.setLabelFor(this.workspaceEdit);
    builder.add(this.workspaceEdit,    constraint.xyw( 4, ++row, 9));

    // the 3rd logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.FRAMEWORK_HOME), constraint.xyw(4, row++, 7));
    label.setLabelFor(this.foundationEdit);
    builder.add(this.foundationEdit,   constraint.xyw( 4, ++row, 7));
    builder.add(this.foundationBrowse, constraint.xy (12,   row));

    // the 4th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.LIBRARIES_HEADER), constraint.xyw(2, row, 12));

    // the 5th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.LIBRARIES_RELEASE), constraint.xy(4, row));
    label.setLabelFor(this.releaseList);
    builder.add(this.releaseList,     constraint.xy(  6, row));
    builder.add(this.libraryMaven,    constraint.xyw( 8, row, 4));

    // the 6th logical row of the layout
    row += 2;
    builder.add(this.libraryGenerate, constraint.xy ( 8, row));
    builder.add(this.libraryManager,  constraint.xyw(10, row, 3));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (DefaultPreferencePanel)
  /**
   ** Called to have this {@link FeaturePreferencePanel} perform the commit
   ** action.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if all changes are applied
   **                            succesfully; <code>false</code> otherwise.
   */
  @Override
  protected boolean commit(final boolean destructive) {
    try {
      commitWorkspace(this.workspaceEdit.getText(),  Bundle.string(Bundle.WORKSPACE_FAILURE), destructive);
      commitFoundation(this.foundationEdit.getURL(), Bundle.string(Bundle.FRAMEWORK_FAILURE), destructive);
      commitRelease(((String)this.releaseList.getSelectedItem()).trim());
      commitMavenSupport(this.libraryMaven.isSelected());
      return true;
    }
    catch (TraversalException e) {
      return false;
    }
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

    this.workspaceEdit.setText(this.provider.workspace());

    // set the configured value for the OCS foundation framework
    final URL foundation = this.provider.foundation();

    // check if we have any valid configuration for that value
    if (!URLFileSystem.isValid(foundation)) {
      Provider iam = Provider.instance(context.getPropertyStorage());
      if (URLFileSystem.isValid(iam.foundation())) {
        // make a best guess on the configured values of the general preferences
        this.provider.foundation(URLFactory.newDirURL(iam.foundation(), Library.DEFAULT_FOLDER));
      }
    }

    this.foundationEdit.setURL(this.provider.foundation());
    this.releaseList.setSelectedItem(this.provider.release());
    // trigger the item listener the state of the library action items
    this.libraryMaven.setSelected(this.provider.mavenSupport());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateLibraries
  /**
   ** Generates the libraries of this product.
   **
   ** @param  root               the {@link URL} path to the base directory of
   **                            the Java Archives to maintain.
   ** @param  release            the path to the Java Archives specific for a
   **                            releass of a featured component.
   **
   ** @throws IOException        if the library descriptor couldn't be opened as
   **                            an <code>InputStream</code>.
   */
  private void generateLibraries(final URL root, final String release)
    throws IOException {

    // schedule the build of the libraries
    super.generateLibraries(Library.instance(root, release));
  }
}