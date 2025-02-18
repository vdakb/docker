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

    File        :   JEEServerPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    JEEServerPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.32  2012-10-20  DSteding    Cloning of an existing file
                                               implemented.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.DefaultComboBoxModel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.util.ResourceUtils;

import oracle.ide.controls.WholeNumberField;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.workspace.iam.model.JEEServer;
import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// class JEEServerPage
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the New Application Wizard dialog for
 ** entering the connection data to the JEE server.
 ** <p>
 ** In general, wizard pages are not supposed to be published APIs, so we enforce
 ** that. Even though the panel is constructed by the IDE framework
 ** using reflection, the IDE framework does not require that it is public (only
 ** that it has a no-argument constructor).
 ** <p>
 ** This panel implements the operations that must be supported by a GUI
 ** component added to the project-from-template and application-from-template
 ** wizards.
 ** <p>
 ** The GUI component is associated with a particular technology scope and is
 ** registered declaratively using the <code>technology-hook</code> hook in the
 ** extension manifest.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class JEEServerPage extends TemplateFeaturePage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3465062822906975930")
  private static final long           serialVersionUID = 987734370039891624L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the element to take over an existing configuration
  private transient JCheckBox         enforceOverride;
  private transient JButton           cloneBrowse;

  // the properties to configure the server properties
  private transient JComboBox<String> serverPlatform;
  private transient JTextField        serverHost;
  private transient WholeNumberField  serverPort;
  private transient JTextField        serverName;
  private transient JTextField        serverHome;
  private transient JTextField        serverUsername;
  private transient JPasswordField    serverPassword;
  private transient JPasswordField    serverConfirm;

  private transient JEEServer         provider         = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JEEServerPage</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JEEServerPage() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyStorage (TemplateFeaturePage)
  /**
   ** Returns the designtime object of this wizard page
   **
   ** @return                    the designtime object of this wizard page.
   */
  @Override
  public final DataStructureAdapter propertyStorage() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (TemplateFeaturePage)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    this.enforceOverride = new JCheckBox();
    this.cloneBrowse     = new JButton();
    this.cloneBrowse.addActionListener(new FeatureClone(this, Manifest.JEE_SERVER));

    this.serverPlatform  = new JComboBox<String>();
    this.serverHost      = new JTextField();
    this.serverPort      = new WholeNumberField(6);
    this.serverName      = new JTextField();
    this.serverHome      = new JTextField();
    this.serverUsername  = new JTextField();
    this.serverPassword  = new JPasswordField();
    this.serverConfirm   = new JPasswordField();

    this.serverPlatform.setModel(new DefaultComboBoxModel<String>(PLATFORM.keySet().toArray(new String[0])));
    this.serverPlatform.setRenderer(new JEEPlatformRenderer(PLATFORM));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (TemplateFeaturePage)
  /**
   ** Layout the panel.
   */
  @Override
  protected void initializeLayout() {
    final FormLayout         layout     = new FormLayout(
    //   |         1           |       2        |        3        |     4      |     5      |
      "4dlu, r:max(50dlu;p), 3dlu, pref:grow, 3dlu, right:pref, 3dlu, 15dlu, 3dlu, 15dlu, 4dlu"
    , "pref, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref"
    //   1     2     |     3     |     4     |     5     |     6     |     7     |     8     |     9     |    10
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 1;
    builder.add(this.enforceOverride, constraint.xyw(1, row, 3));
    builder.add(this.cloneBrowse,     constraint.xyw(10, row, 1));

    // the 2nd logical row of the layout
    builder.addSeparator(ComponentBundle.string(ComponentBundle.SERVER_GENERAL_HEADER), constraint.xywh(2, ++row, 9, 1));

    // the 3rd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PLATFORM_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverPlatform);
    builder.add(this.serverPlatform, constraint.xyw(4, row, 7));

    // the 4th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_HOST_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverHost);
    builder.add(this.serverHost, constraint.xyw(4, row, 1));
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PORT_LABEL), constraint.xy(6, row));
    label.setLabelFor(this.serverPort);
    builder.add(this.serverPort, constraint.xyw(8, row, 3));

    // the 5th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_HOME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverHome);
    builder.add(this.serverHome, constraint.xyw(4, row, 7));

    // the 6th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_NAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverName);
    builder.add(this.serverName, constraint.xyw(4, row, 7));

    // the 7th logical row of the layout
    row += 2;
    builder.addSeparator(ComponentBundle.string(ComponentBundle.SERVER_ACCOUNT_HEADER), constraint.xywh(2, row, 9, 1));

    // the 8th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_USERNAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverUsername);
    builder.add(this.serverUsername, constraint.xyw(4, row, 7));

    // the 9th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverPassword);
    builder.add(this.serverPassword, constraint.xyw(4, row, 7));

    // the 10th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_CONFIRM_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverConfirm);
    builder.add(this.serverConfirm, constraint.xyw(4, row, 7));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (TemplateFeaturePage)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    ResourceUtils.resButton(this.enforceOverride, ComponentBundle.string(ComponentBundle.CONFIG_OVERRIDE_LABEL));
    this.cloneBrowse.setIcon(ComponentBundle.icon(ComponentBundle.CONFIG_CLONEFROM_ICON));
    this.cloneBrowse.setToolTipText(ComponentBundle.string(ComponentBundle.CONFIG_CLONEFROM_HINT));
    IconicButtonUI.install(this.cloneBrowse);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializePage (TemplateFeaturePage)
  /**
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the {@link TraversableContext} passed to this page
   ** on method {@link #onEntry(TraversableContext)}.
   */
  @Override
  protected final void initializePage() {
    this.enforceOverride.setSelected(this.provider.override());

    this.serverPlatform.setSelectedItem(this.provider.platform());
    this.serverHost.setText(this.provider.host());
    this.serverPort.setText(this.provider.port());
    this.serverHome.setText(this.provider.home());
    this.serverName.setText(this.provider.server());
    this.serverUsername.setText(this.provider.username());
    this.serverPassword.setText(this.provider.password());
    this.serverConfirm.setText(this.provider.password());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (DefaultPreferencePanel)
  /**
   ** Called to have this {@link TemplateFeaturePage} perform the commit action.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  @Override
  protected void commit(final boolean validate)
    throws TraversalException {

    // commit the simple things that do not need any validation first
    this.provider.platform((String)this.serverPlatform.getSelectedItem());
    this.provider.override(this.enforceOverride.isSelected());

    commitHost(validate);
    commitPort(validate);
    commitHome(validate);
    commitServer(validate);
    commitUsername(validate);
    commitPassword(validate);
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
    // On entering the panel, we need to populate all fields with properties
    // from the model object.
    this.provider =  JEEServer.instance(context);

    initializePage();

    super.onEntry(context);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   commitHost
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>host</code> must be evaluate to a valid
   ** string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitHost(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverHost.getText();

    // check the rules and validate
    if (validate)
      validateString(value, ComponentBundle.string(ComponentBundle.SERVER_HOST_ERROR));

    // store the provided value in the context
    if (!this.provider.host().equals(value))
      this.provider.host(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitPort
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>port</code> must be evaluate to a valid
   ** number and must be between <code>0</code> and <code>65535</code>.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitPort(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverPort.getText();

    // check the rules and validate
    if (validate) {
      validateString(value, ComponentBundle.string(ComponentBundle.SERVER_PORT_ERROR));
      validateRange(integer(value, ComponentBundle.string(ComponentBundle.SERVER_PORT_FORMAT)), 0, 65535, ComponentBundle.format(ComponentBundle.SERVER_PORT_RANGE, 0, 65535));
    }

    // store the provided value in the context
    if (!this.provider.port().equals(value))
      this.provider.port(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitHome
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>home</code> must be evaluate to a valid
   ** string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitHome(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverHome.getText();

    // check the rules and validate
    if (validate)
       validateString(value, ComponentBundle.string(ComponentBundle.SERVER_HOME_ERROR));

    // store the provided value in the context
    if (!this.provider.home().equals(value))
      this.provider.home(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitServer
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>server</code> must be evaluate to a valid
   ** string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitServer(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverName.getText();

    // check the rules and validate
    if (validate)
       validateString(value, ComponentBundle.string(ComponentBundle.SERVER_NAME_ERROR));

    // store the provided value in the context
    if (!this.provider.server().equals(value))
      this.provider.server(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitUsername
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>username</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitUsername(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverUsername.getText();

    // check the rules and validate
    if (validate)
      validateString(value, ComponentBundle.string(ComponentBundle.SERVER_USERNAME_ERROR));

    // store the provided value in the context
    if (!this.provider.username().equals(value))
      this.provider.username(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitPassword
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>password</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitPassword(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String password = new String(this.serverPassword.getPassword());

    // check the rules and validate
    if (validate) {
      validateString(password, ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_ERROR));
      // obtained the value from the UI to store.
      final String confirmation = new String(this.serverConfirm.getPassword());
      validateString(confirmation, ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_CONFIRM_ERROR));
      if (!password.equals(confirmation))
        throw new TraversalException(ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_MISMATCH));
    }

    // store the provided value in the context
    if (!this.provider.password().equals(password))
      this.provider.password(password);
  }
}