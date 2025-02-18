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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   Panel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Panel.


    Revisions          Date        Editor      Comment
    -------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13   2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69   2017-31-01  DSteding    Changed compatibility annotation
                                                accordingliy to the build.
    12.2.1.3.42.60.74   2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94   2021-03-09  DSteding    Support for Maven Project Object
                                                Model
    12.2.1.3.42.60.94   2021-03-09  DSteding    Support for Maven Project Object
                                                Model
    12.2.1.3.42.60.101  2022-06-11  DSteding    Support for Foundation Services
                                                Library Generation
*/

package oracle.jdeveloper.workspace.iam.preference;

import java.util.Set;

import java.net.URL;

import java.io.IOException;

import java.awt.EventQueue;

import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.util.ResourceUtils;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.URLTextField;

import oracle.ide.controls.WaitCursor;

import oracle.ide.controller.IdeAction;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.Bundle;
import oracle.jdeveloper.workspace.iam.Library;

import oracle.jdeveloper.workspace.iam.command.ManageLibrary;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.swing.LabelFactory;

import oracle.jdeveloper.workspace.iam.swing.list.MappedListCellRenderer;

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
 ** @version 12.2.1.3.42.60.101
 ** @since   11.1.1.3.37.56.13
 */
public final class Panel extends PreferencePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5542200649236453143")
  private static final long           serialVersionUID = -5488870480314324615L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the global Workspace
  private transient URLTextField      workspaceEdit;
  private transient JButton           workspaceBrowse;

  // the properties to configure the Oracle Consulting Foundation Framework
  private transient URLTextField      foundationEdit;
  private transient JButton           foundationBrowse;

  // the properties to configure the Oracle Consulting Foundation Framework
  private transient URLTextField      headstartEdit;
  private transient JButton           headstartBrowse;

  // the properties to configure the Oracle Consulting Service Framework
  private transient URLTextField      servicesEdit;
  private transient JButton           servicesBrowse;

  // the properties to configure the Oracle Platform Services Framework
  private transient URLTextField      platformEdit;
  private transient JButton           platformBrowse;

  // the action elements to maintain the library artifacts inside of JDeveloper
  private transient JComboBox<String> releaseList;
  private transient JCheckBox         libraryMaven;
  private transient JButton           libraryGenerate;
  private transient JButton           libraryManager;

  private transient Provider          provider;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class NameListener
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** Observer of application name field to propagate the changes to the folder
   ** field
   */
  private final class NameListener implements KeyListener
                                   ,          DocumentListener {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String       feature;
    private final URLTextField observed;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>NameListener</code> that applies changes of the
     ** foundadtion root directory to the observed {@link URLTextField}.
     */
    private NameListener(final URLTextField textField, final String feature) {
      // ensure inheritance
      super();

      // initialize instance
      this.feature  = feature;
      this.observed = textField;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   keyTyped (KeyListener)
    /**
     ** Invoked when a key has been typed.
     ** <p>
     ** See the class description for {@link KeyEvent} for a definition of a key
     ** typed event.
     */
    @Override
    public void keyTyped(final KeyEvent event) {
      char typed = event.getKeyChar();
      if (('*' == typed) || ('?' == typed) || ('|' == typed))
        event.consume();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   keyPressed (KeyListener)
    /**
     ** Invoked when a key has been pressed.
     ** <p>
     ** See the class description for {@link KeyEvent} for a definition of a key
     ** pressed event.
     **
     ** @param  event            a {@link KeyEvent} object describing the
     **                          event source and the property that has changed.
     */
    @Override
    public void keyPressed(final KeyEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   keyReleased (KeyListener)
    /**
     ** Invoked when a key has been released.
     ** <p>
     **  See the class description for {@link KeyEvent} for a definition of a
     **  key released event.
     **
     ** @param  event            a {@link KeyEvent} object describing the
     **                          event source and the property that has changed.
     */
    @Override
    public void keyReleased(final KeyEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   insertUpdate (DocumentListener)
    /**
     ** Gives notification that there was an insert into the document.
     ** <p>
     ** The  range given by the DocumentEvent bounds the freshly inserted
     ** region.
     **
     ** @param  event            a {@link DocumentEvent} object describing the
     **                          event source and the property that has changed.
     */
    @Override
    public void insertUpdate(final DocumentEvent event) {
      updateControl();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   removeUpdate (DocumentListener)
    /**
     ** Gives notification that a portion of the document has been removed.
     ** <p>
     ** The range is given in terms of what the view last saw (that is, before
     ** updating sticky positions).
     **
     ** @param  event            a {@link DocumentEvent} object describing the
     **                          event source and the property that has changed.
     */
    @Override
    public void removeUpdate(final DocumentEvent event) {
      updateControl();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   changedUpdate (DocumentListener)
    /**
     ** Gives notification that an attribute or set of attributes changed.
     **
     ** @param  event            a {@link DocumentEvent} object describing the
     **                          event source and the property that has changed.
     */
    @Override
    public void changedUpdate(final DocumentEvent event) {
      updateControl();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updateControl
    /**
     ** Synchronize the directory field with the foundation directory
     ** accordingly to the characters the user type in the foundation field.
     */
    private void updateControl() {
      NameListener.this.observed.setText(evaluatePath(NameListener.this.feature));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   evaluatePath
    /**
     ** Build a new pathname for the specified feature.
     **
     ** @param  feature          the feature pathname to append to the
     **                          foundation path.
     **
     ** @return                  the {@link URL} as a string belonging to the
     **                          specified feature.
     */
    private String evaluatePath(final String feature) {
      final URL foundationURL = URLFactory.newDirURL(Panel.this.foundationEdit.getText());
      return URLFileSystem.getPlatformPathName(URLFactory.newDirURL(foundationURL, feature));
    }
  }

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
  // Method:   initializeComponent (PreferencePanel)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected final void initializeComponent() {
    // the properties to configure the global Workspace
    this.workspaceEdit    = new URLTextField();
    this.workspaceBrowse  = new JButton();
    // the properties to configure the Oracle Consulting Base
    this.foundationEdit   = new URLTextField();
    this.foundationBrowse = new JButton();

    // the properties to configure the Oracle Consulting Foundation Framework
    this.headstartEdit    = new URLTextField();
    this.headstartBrowse  = new JButton();

    // the properties to configure the Oracle Consulting Service Framework
    this.servicesEdit     = new URLTextField();
    this.servicesBrowse   = new JButton();

    // the properties to configure the Oracle Platform Service Framework
    this.platformEdit     = new URLTextField();
    this.platformBrowse   = new JButton();

    // the action elements to maintain the library artifacts inside of
    // JDeveloper
    this.releaseList      = new JComboBox<String>();
    this.libraryMaven     = new JCheckBox();
    this.libraryGenerate  = new JButton();
    this.libraryManager   = new JButton();

    this.workspaceBrowse.addActionListener(this.workspaceEdit);
    this.foundationBrowse.addActionListener(this.foundationEdit);
    this.headstartBrowse.addActionListener(this.headstartEdit);
    this.servicesBrowse.addActionListener(this.servicesEdit);
    this.platformBrowse.addActionListener(this.platformEdit);

    // add the listeners always after the field content is initialized
    final NameListener headstartListener = new NameListener(this.headstartEdit, Library.HEADSTART);
    this.foundationEdit.getDocument().addDocumentListener(headstartListener);
    this.foundationEdit.addKeyListener(headstartListener);

    // add the listeners always after the field content is initialized
    final NameListener servicesListener = new NameListener(this.servicesEdit, Library.SERVICES);
    this.foundationEdit.getDocument().addDocumentListener(servicesListener);
    this.foundationEdit.addKeyListener(servicesListener);

    // add the listeners always after the field content is initialized
    final NameListener platformListener = new NameListener(this.platformEdit, Library.PLATFORM);
    this.foundationEdit.getDocument().addDocumentListener(platformListener);
    this.foundationEdit.addKeyListener(platformListener);

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
              generateLibraries(Library.HEADSTART_DESCRIPTOR, Panel.this.provider.headstart(), null);
              generateLibraries(Library.PLATFORM_DESCRIPTOR,  Panel.this.provider.services(),  null);
              generateLibraries(Library.SECURITY_DESCRIPTOR,  Panel.this.provider.platform(),  Panel.this.provider.release());
              generateLibraries(Library.METADATA_DESCRIPTOR,  Panel.this.provider.platform(),  Panel.this.provider.release());
            }
            catch (IOException e) {
              // TODO: provide better solution of exception handling
              e.printStackTrace();
            }
        }
      }
    );
    final Set<String> releaseSet = Library.RELEASE.keySet();
    final String[]    release    = releaseSet.toArray(new String[0]);
    this.releaseList.setModel(new DefaultComboBoxModel<String>(release));
    this.releaseList.setRenderer(new MappedListCellRenderer(ComponentBundle.icon(ComponentBundle.VENDOR_ORACLE), Library.RELEASE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (PreferencePanel)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    this.workspaceBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.workspaceBrowse.setToolTipText(Bundle.string(Bundle.WKS_WORKSPACE_HINT));
    this.foundationBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.foundationBrowse.setToolTipText(Bundle.string(Bundle.OCS_WORKSPACE_HINT));
    this.headstartBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.headstartBrowse.setToolTipText(Bundle.string(Bundle.HST_FRAMEWORK_HINT));
    this.servicesBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.servicesBrowse.setToolTipText(Bundle.string(Bundle.HST_PLATFORM_HINT));
    this.platformBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.platformBrowse.setToolTipText(Bundle.string(Bundle.OPS_FRAMEWORK_HINT));

    ResourceUtils.resButton(this.libraryMaven,    Bundle.string(Bundle.LIBRARIES_MAVEN));
    ResourceUtils.resButton(this.libraryGenerate, Bundle.string(Bundle.LIBRARIES_GENERATE));
    ResourceUtils.resButton(this.libraryManager,  Bundle.string(Bundle.LIBRARIES_MANAGE));

    IconicButtonUI.install(this.workspaceBrowse);
    IconicButtonUI.install(this.foundationBrowse);
    IconicButtonUI.install(this.headstartBrowse);
    IconicButtonUI.install(this.servicesBrowse);
    IconicButtonUI.install(this.platformBrowse);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (PreferencePanel)
  /**
   ** Layout the panel.
   */
  @Override
  protected final void initializeLayout() {
    final FormLayout      layout     = new FormLayout(
    //   |     1     |     2     |       3        |     4      |     5      |     6      |
      "4dlu, pref, 6dlu, pref, 3dlu, pref:grow, 3dlu, 55dlu, 3dlu, 39dlu, 1dlu, 15dlu, 4dlu"
    , "6dlu, pref, 6dlu, pref, 1dlu, pref, 4dlu, pref, 1dlu, pref, 4dlu, pref, 1dlu, pref, 4dlu, pref, 1dlu, pref,  1dlu, pref, 1dlu, pref, 6dlu, pref, 4dlu, pref, 4dlu, pref"
    //   |     1     |     2    |     3      |     4     |     5     |     6     |     7     |     8     |    9      |    10     |    11     |    12    |     13 |     14
    );

    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.GENERAL_HEADER), constraint.xyw(2, row, 12));

    // the 2nd logical row of the layout
    row += 2;
    builder.add(LabelFactory.label(Bundle.string(Bundle.WKS_WORKSPACE_HOME), this.workspaceEdit), constraint.xyw(4, row++, 7));
    builder.add(this.workspaceEdit,   constraint.xyw( 4, ++row, 7));
    builder.add(this.workspaceBrowse, constraint.xy (12,   row));

    // the 3rd logical row of the layout
    row += 2;
    builder.add(LabelFactory.label(Bundle.string(Bundle.OCS_WORKSPACE_HOME), this.foundationEdit), constraint.xyw(4, row++, 7));
    builder.add(this.foundationEdit,   constraint.xyw( 4, ++row, 7));
    builder.add(this.foundationBrowse, constraint.xy (12,   row));

    // the 4th logical row of the layout
    row += 2;
    builder.add(LabelFactory.label(Bundle.string(Bundle.HST_FRAMEWORK_HOME), this.headstartEdit), constraint.xyw(4, row++, 7));
    builder.add(this.headstartEdit,   constraint.xyw( 4, ++row, 7));
    builder.add(this.headstartBrowse, constraint.xy (12,   row));

    // the 5th logical row of the layout
    row += 2;
    builder.add(LabelFactory.label(Bundle.string(Bundle.HST_PLATFORM_HOME), this.servicesEdit), constraint.xyw(4, row++, 7));
    builder.add(this.servicesEdit,   constraint.xyw( 4, ++row, 7));
    builder.add(this.servicesBrowse, constraint.xy (12,   row));

    // the 6th logical row of the layout
    row += 2;
    builder.add(LabelFactory.label(Bundle.string(Bundle.OPS_FRAMEWORK_HOME), this.platformEdit), constraint.xyw(4, row++, 7));
    builder.add(this.platformEdit,    constraint.xyw( 4, ++row, 7));
    builder.add(this.platformBrowse,  constraint.xy (12,   row));

    // the 7th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.LIBRARIES_HEADER), constraint.xyw(2, row, 12));

    // the 8th logical row of the layout
    row += 2;
    builder.add(LabelFactory.label(Bundle.string(Bundle.LIBRARIES_RELEASE), this.releaseList), constraint.xy(4, row));
    builder.add(this.releaseList,     constraint.xy(  6, row));
    builder.add(this.libraryMaven,    constraint.xyw( 8, row, 4));

    // the 9th logical row of the layout
    row += 2;
    builder.add(this.libraryGenerate, constraint.xy ( 8, row));
    builder.add(this.libraryManager,  constraint.xyw(10, row, 3));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (PreferencePanel)
  /**
   ** Called to have this {@link FeaturePreferencePanel} perform the commit
   ** action.
   **
   ** @param  invasive           if the operation the confirmation is for is
   **                            invasive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if all changes are applied
   **                            succesfully; <code>false</code> otherwise.
   */
  @Override
  protected boolean commit(final boolean invasive) {
    try {
      commitWorkspace(invasive);
      commitFoundation(invasive);
      commitHeadstart(invasive);
      commitPlatform(invasive);
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

    // This gets a defensive copy of the preferences being used in the Tools ->
    // Preferences dialog. The IDE framework takes care of making this copy, and
    // applying it back to the real preferences store, or abandoning it if the
    // the user clicks Cancel.
    this.provider = Provider.instance(context.getPropertyStorage());

    if (this.provider.headstart() == null) {
      if (this.provider.foundation() != null) {
        final URL headstart = URLFactory.newDirURL(this.provider.foundation(), Library.HEADSTART);
        this.provider.headstart(headstart);
      }
    }

    if (this.provider.platform() == null) {
      if (this.provider.foundation() != null) {
        final URL platform = URLFactory.newDirURL(this.provider.platform(), Library.PLATFORM);
        this.provider.platform(platform);
      }
    }

    this.workspaceEdit.setURL(this.provider.workspace());
    this.foundationEdit.setURL(this.provider.foundation());
    this.headstartEdit.setURL(this.provider.headstart());
    this.servicesEdit.setURL(this.provider.services());
    this.platformEdit.setURL(this.provider.platform());
    this.releaseList.setSelectedItem(this.provider.release());
    // trigger the item listener the state of the library action items
    this.libraryMaven.setSelected(this.provider.mavenSupport());
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

    if (commit(false))
      // ensure inheritance
      super.onExit(context);
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
  private void generateLibraries(final String descriptor, final URL root, final String release)
    throws IOException {

    // attach a WaitCursor to the RootPaneContainer of this panel
    final WaitCursor waitCursor = new WaitCursor(this);
    // schedules the wait cursor to be shown after 400 milliseconds has
    // elapsed
    waitCursor.show(400);
    try {
      // schedule the build of the libraries
      EventQueue.invokeLater(Library.instance(descriptor, root, release));
    }
    finally {
      // hides the wait cursor if visible.
      waitCursor.hide();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitWorkspace
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>workspace</code> must be evaluate to a
   ** valid directory.
   **
   ** @param  invasive           if the operation the confirmation is for is
   **                            invasive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitWorkspace(final boolean invasive)
    throws TraversalException {

    URL value = this.workspaceEdit.getURL();
    if (!validateFolder(value)) {
      if (!confirmViolation(FeaturePreference.WORKSPACE, Bundle.string(Bundle.WKS_WORKSPACE_FAILURE), invasive))
        // notify the user about an unanticipated condition that prevents the
        // task from completing successfully
        throw new TraversalException(FeaturePreference.WORKSPACE);
    }

    if (!URLFileSystem.equals(this.provider.workspace(), value))
      this.provider.workspace(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitFoundation
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>foundation</code> must be a evaluate to a
   ** valid directory.
   **
   ** @param  invasive           if the operation the confirmation is for is
   **                            invasive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitFoundation(final boolean invasive)
    throws TraversalException {

    URL value = this.foundationEdit.getURL();
    if (!validateFolder(value)) {
      if (!confirmViolation(FeaturePreference.FOUNDATION, Bundle.string(Bundle.OCS_WORKSPACE_FAILURE), invasive))
        // notify the user about an unanticipated condition that prevents the
        // task from completing successfully
        throw new TraversalException(FeaturePreference.FOUNDATION);
    }

    if (!URLFileSystem.equals(this.provider.foundation(), value))
      this.provider.foundation(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitHeadstart
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>headstart</code> must be evaluate to a
   ** valid directory.
   **
   ** @param  invasive           if the operation the confirmation is for is
   **                            invasive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitHeadstart(final boolean invasive)
    throws TraversalException {

    final URL newHeadstart = this.headstartEdit.getURL();
    if (!validateFolder(newHeadstart)) {
      // notify the user about an unanticipated condition that prevents the
      // task from completing successfully
      if (!confirmViolation(Provider.HEADSTART, Bundle.string(Bundle.HST_FRAMEWORK_FAILURE), invasive))
        throw new TraversalException(Provider.HEADSTART);
    }

    if (!URLFileSystem.equals(this.provider.headstart(), newHeadstart))
      this.provider.headstart(newHeadstart);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitPlatform
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>foundation</code> must be evaluate to a
   ** valid directory.
   **
   ** @param  invasive           if the operation the confirmation is for is
   **                            invasive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitPlatform(final boolean invasive)
    throws TraversalException {

    final URL newPlatform = this.platformEdit.getURL();
    if (!validateFolder(newPlatform)) {
      // notify the user about an unanticipated condition that prevents the
      // task from completing successfully
      if (!confirmViolation(Provider.PLATFORM, Bundle.string(Bundle.OPS_FRAMEWORK_FAILURE), invasive))
        throw new TraversalException(Provider.PLATFORM);
    }

    if (!URLFileSystem.equals(this.provider.platform(), newPlatform))
      this.provider.platform(newPlatform);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitRelease
  /**
   ** Stores the release information a feature use.
   **
   ** @param  value              the value obtained from a UI to store.
   */
  protected final void commitRelease(final String value) {
    this.provider.release(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitMavenSupport
  /**
   ** Stores the maven support a feature use.
   **
   ** @param  value              the value obtained from a UI to store.
   */
  protected final void commitMavenSupport(final boolean value) {
    this.provider.mavenSupport(value);
  }
}