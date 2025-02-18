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

    File        :   DirectoryServerPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryServerPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

import javax.swing.ButtonGroup;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.ide.Ide;
import oracle.ide.controls.WaitCursor;

import oracle.ide.util.ResourceUtils;

import oracle.ide.panels.TraversalException;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.DirectoryServer;

import oracle.jdeveloper.connection.iam.service.DirectoryService;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryServerPanel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the configuration dialog to create and
 ** modify a Directory Service connection.
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
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryServerPanel extends EndpointPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9110586105486971088")
  private static final long           serialVersionUID = -6250828324554593676L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the server properties
  private transient JTextField        serverContext;
  private transient ButtonGroup       authentication;
  private transient JRadioButton      simple;
  private transient JRadioButton      gssApi;
  private transient JCheckBox         secureSSL;
  private transient JCheckBox         secureTLS;
  private transient JCheckBox         secureSASL;
  private transient JCheckBox         anonymous;


  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Authentication
  // ~~~~~ ~~~~~~~~~~~~~~
  /**
   ** Implementation of data classes which can be integrated with the IDE
   ** framework.
   */
  class Authentication implements ActionListener {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Authentication</code> action listener.
     */
    Authentication() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   actionPerformed (ActionListener)
    /**
     ** Invoked when an action occurs.
     **
     ** @param  event            a {@link ActionEvent} object describing the
     **                          event source and the property that has changed.
     **                          <br>
     **                          Allowed object is {@link ActionEvent}.
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
      // if condition to check if simple autenthication is selected.
      if (simple.isSelected()) {
        serverSectionStatus(true);
      }
      else if (gssApi.isSelected()) {
        serverSectionStatus(false);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class AnonymousBind
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Implementation of data classes which can be integrated with the IDE
   ** framework.
   */
  class AnonymousBind implements ActionListener {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AnonymousBind</code> action listener.
     */
    AnonymousBind() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   actionPerformed (ActionListener)
    /**
     ** Invoked when an action occurs.
     **
     ** @param  event            a {@link ActionEvent} object describing the
     **                          event source and the property that has changed.
     **                          <br>
     **                          Allowed object is {@link ActionEvent}.
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
      final JCheckBox source = (JCheckBox)event.getSource();
      // code to handle click event goes here...
      accountSectionStatus(!source.isSelected());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryServerPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryServerPanel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (EndpointPanel)
  /**
   ** Layout the panel.
   */
  @Override
  protected void initializeLayout() {
    final FormLayout         layout     = new FormLayout(
    //   |         1           |       2        |        4        |     5      |     6     |
      "4dlu, r:max(50dlu;p), 3dlu, pref:grow, 3dlu, pref, 3dlu, 15dlu, 3dlu, 15dlu, 4dlu"
    , "6dlu, pref, 6dlu, pref, 6dlu, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref, 6dlu, fill:pref:grow, 6dlu"
    //   |    1     |     2     |     3      |    4      |     5    |      6    |      7     |     8     |     9    |     10     |    11     |    12     |        13          |
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.DIRECTORY_GENERAL_HEADER), constraint.xyw(2, row, 9));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(Bundle.string(Bundle.CONNECTION_NAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.connectionName);
    builder.add(this.connectionName, constraint.xyw(4, row, 7));

    // the 3rd logical row of the layout
    row += 2;
    builder.addSeparator(ComponentBundle.string(ComponentBundle.SERVER_GENERAL_HEADER), constraint.xyw(2, row, 9));

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
    label = builder.addLabel(Bundle.string(Bundle.DIRECTORY_CONTEXT_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverContext);
    builder.add(this.serverContext, constraint.xyw(4, row, 7));

    // the 6th logical row of the layout
    row += 2;
    builder.add(this.simple,    constraint.xy (4, row));
    builder.add(this.secureSSL, constraint.xy (6, row));
    builder.add(this.secureTLS, constraint.xyw(8, row, 3));

    // the 7th logical row of the layout
    row += 2;
    builder.add(this.gssApi,     constraint.xy(4, row));
    builder.add(this.secureSASL, constraint.xy(6, row));

    // the 8th logical row of the layout
    row += 2;
    builder.addSeparator(ComponentBundle.string(ComponentBundle.SERVER_ACCOUNT_HEADER), constraint.xyw(2, row, 9));

    // the 9th logical row of the layout
    row += 2;
    builder.add(this.anonymous, constraint.xyw(4, row, 7));

    // the 10th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_USERNAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.principalUsername);
    builder.add(this.principalUsername, constraint.xyw(4, row, 7));

    // the 11th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.principalPassword);
    builder.add(this.principalPassword, constraint.xyw(4, row, 7));

    // the 12th logical row of the layout
    row += 2;
    builder.add(this.testConnection, constraint.xyw(6, row, 5));

    // the 13th logical row of the layout
    row += 2;
    builder.add(this.testResult,     constraint.xyw(2, row, 9));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (EndpointPanel)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    ResourceUtils.resButton(this.simple,         Bundle.string(Bundle.DIRECTORY_SIMPLE_LABEL));
    ResourceUtils.resButton(this.gssApi,         Bundle.string(Bundle.DIRECTORY_GSSAPI_LABEL));
    ResourceUtils.resButton(this.secureSSL,      Bundle.string(Bundle.DIRECTORY_SSL_LABEL));
    ResourceUtils.resButton(this.secureTLS,      Bundle.string(Bundle.DIRECTORY_TLS_LABEL));
    ResourceUtils.resButton(this.secureSASL,     Bundle.string(Bundle.DIRECTORY_SASL_LABEL));
    ResourceUtils.resButton(this.anonymous,      Bundle.string(Bundle.DIRECTORY_ANONYMOUS_LABEL));
    ResourceUtils.resButton(this.testConnection, Bundle.string(Bundle.DIRECTORY_TEST_LABEL));
    this.testConnection.setToolTipText(Bundle.string(Bundle.DIRECTORY_TEST_HINT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializePage (EndpointPanel)
  /**
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the <code>TraversableContext</code> passed to this
   ** page on method {@link #onEntry(TraversableContext)}.
   */
  @Override
  protected final void initializePage() {
    this.connectionName.setText(this.model.name());
    this.serverHost.setText(this.model.serverName());
    this.serverPort.setValue(this.model.serverPort());
    this.serverContext.setText(this.model.serverContext());
    this.principalUsername.setText(this.model.principalName());
    this.principalPassword.setText(this.model.principalPassword());
    if (StringUtility.equal(((DirectoryServer)this.model).authentication(), DirectoryServer.AUTHENTICATION_SIMPLE)) {
      this.simple.setSelected(true);
      this.secureSASL.setSelected(false);
      this.secureSASL.setEnabled(false);
    }
    else {
      this.gssApi.setSelected(true);
      this.secureSSL.setSelected(false);
      this.secureSSL.setEnabled(false);
      this.secureTLS.setSelected(false);
      this.secureTLS.setEnabled(false);
    }
    if (StringUtility.empty(this.model.principalName()) && StringUtility.empty(this.model.principalPassword())) {
      this.anonymous.setSelected(true);
      accountSectionStatus(false);
    }
    else {
      this.anonymous.setSelected(false);
      accountSectionStatus(true);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testConnection (EndpointPanel)
  /**
   ** Performs all activities to test a connection.
   */
  @Override
  public void testConnection() {
    String           status  = null;
    DirectoryService service = null;
    final WaitCursor cursor  = new WaitCursor(this);
    // schedules the wait cursor to be shown after 100 milliseconds has
    // elapsed
    cursor.show(100);
    try {
      // this will raise a commit of the data to the traversable context
      // this will not do anything with the artifacts that this page configures
      // if any error occurs during this phase the wizard will stay on this page
      commitProperty();
      this.testResult.setText(Bundle.string(Bundle.DIRECTORY_TEST_WORKING), false);
      service = DirectoryService.build((DirectoryServer)this.model);
      service.connect();
    }
    catch (Throwable e) {
      if (e.getCause() != null)
        status = Bundle.format(Bundle.DIRECTORY_TEST_CANCELLED, e.getCause().getLocalizedMessage());
      else
        status = Bundle.format(Bundle.DIRECTORY_TEST_CANCELLED, e.getLocalizedMessage());
    }
    finally {
      // hides the wait cursor if visible.
      cursor.hide();
      if (status == null)
        status = Bundle.string(Bundle.DIRECTORY_TEST_SUCCESS);
      this.testResult.setText(status, false);
      if (service != null)
        try {
          service.disconnect();
        }
        catch (Throwable e) {
          status = e.getLocalizedMessage();
          MessageDialog.error(Ide.getMainWindow(), e.getMessage(), Bundle.string(Bundle.DIRECTORY_TEST_HINT), null);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit  (EndpointPanel)
  /**
   ** Called to have this {@link EndpointPanel} perform the commit action.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  @Override
  protected void commit()
    throws TraversalException {
    
    commitName();
    commitProperty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (overridden)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    // ensure inheritance
    super.initializeComponent();
    this.serverContext = new JTextField();
    this.simple        = new JRadioButton();
    this.gssApi        = new JRadioButton();
    this.secureSSL     = new JCheckBox();
    this.secureTLS     = new JCheckBox();
    this.secureSASL    = new JCheckBox();
    this.anonymous     = new JCheckBox();

    this.authentication = new ButtonGroup();
    this.authentication.add(this.simple);
    this.authentication.add(this.gssApi);
    
    final Authentication authentication = new Authentication();
    
    this.anonymous.addActionListener(new AnonymousBind());
    this.simple.addActionListener(authentication);
    this.gssApi.addActionListener(authentication);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   commitProperty
  /**
   ** Checks if the instance satisfies all requirements.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitProperty()
    throws TraversalException {
    
    commitHost();
    commitPort();
    commitContext();
    commitAuthentication();
    commitUsername();
    commitPassword();
    commitServerSocket(this.secureSSL.isSelected(), this.secureTLS.isSelected());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   commitHost
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>host</code> must be evaluate to a valid
   ** string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitHost()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverHost.getText();

    // check the rules and validate
    validateString(DirectoryServer.SERVER_NAME, value, ComponentBundle.string(ComponentBundle.SERVER_HOST_ERROR));
    // store the provided value in the model
    this.model.serverName(value.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitPort
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>port</code> must be evaluate to a valid
   ** number and must be in range from <code>0</code> till <code>65535</code>.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitPort()
    throws TraversalException {

    // obtained the value from the UI to store.
    final int value = this.serverPort.getValue();

    // check the rules and validate
    validateRange(DirectoryServer.SERVER_PORT, value, 1, 65535, ComponentBundle.format(ComponentBundle.SERVER_PORT_RANGE, 1, 65535));

    // store the provided value in the model
    this.model.serverPort(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitUsername
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>username</code> must be evaluate to a
   ** valid string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitUsername()
    throws TraversalException {

    if (this.anonymous.isSelected()) {
      // store the provided value in the model
      this.model.principalName(null);
    }
    else {
      // obtained the value from the UI to store.
      final String value = this.principalUsername.getText();

      // check the rules and validate
      validateString(DirectoryServer.PRINCIPAL_NAME, value, ComponentBundle.string(ComponentBundle.SERVER_USERNAME_ERROR));
      // store the provided value in the model
      this.model.principalName(value.trim());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitPassword
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>password</code> must be evaluate to a
   ** valid string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitPassword()
    throws TraversalException {

    if (this.anonymous.isSelected()) {
      // store the provided value in the model
      this.model.principalPassword((String)null);
    }
    else {
      // obtained the value from the UI to store.
      final String value = new String(this.principalPassword.getPassword());

      // check the rules and validate
      validateString(DirectoryServer.PRINCIPAL_PASSWORD, value, ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_ERROR));
      // store the provided value in the model
      this.model.principalPassword(value.trim());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitAuthentication
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>authentication</code> must be evaluate to a
   ** valid string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitAuthentication()
    throws TraversalException {

    ((DirectoryServer)this.model).authentication(this.simple.isSelected() ? DirectoryServer.AUTHENTICATION_SIMPLE : DirectoryServer.AUTHENTICATION_GSSAPI);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   commitContext
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>context</code> must be evaluate to a valid
   ** string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitContext()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverContext.getText();
    // store the provided value in the model
    this.model.serverContext(value.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSectionStatus
  /**
   ** Sets the UI status of the server property section.
   **
   ** @param  state              <code>true</code> id the UI component are
   **                            enabled for input; otherwise
   **                            <code>false>/code>.
   */
  private void serverSectionStatus(final boolean state) {
    this.secureSSL.setEnabled(state);
    this.secureTLS.setEnabled(state);
    this.secureSASL.setEnabled(!state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSectionStatus
  /**
   ** Sets the UI status of the account property section.
   **
   ** @param  state              <code>true</code> id the UI component are
   **                            enabled for input; otherwise
   **                            <code>false>/code>.
   */
  private void accountSectionStatus(final boolean state) {
    this.principalUsername.setEnabled(state);
    this.principalPassword.setEnabled(state);
  }
}