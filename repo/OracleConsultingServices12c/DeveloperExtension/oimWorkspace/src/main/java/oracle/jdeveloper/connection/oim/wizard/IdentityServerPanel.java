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

    File        :   IdentityServerPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityServerPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.wizard;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.ide.controls.WaitCursor;
import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.ide.util.ResourceUtils;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.wizard.JEEPlatformRenderer;

import oracle.jdeveloper.connection.iam.wizard.EndpointPanel;

import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.connection.oim.model.IdentityServer;

import oracle.jdeveloper.connection.oim.service.IdentityService;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerPanel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the configuration dialog to create and
 ** modify a Identity server connection.
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
public class IdentityServerPanel extends EndpointPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String               WEBLOGIC          = "weblogic";
  protected static final String               WEBSPHERE         = "websphere";

  protected static final Map<String, String>  PLATFORM          = new LinkedHashMap<String, String>();

  protected static final Map<String, Integer> SYMBOL            = new HashMap<String, Integer>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:904317496885547446")
  private static final long                   serialVersionUID = 6876535597795959634L;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    PLATFORM.put(WEBLOGIC,  "Oracle WebLogic Server");
    PLATFORM.put(WEBSPHERE, "WebSphere Application Server");

    SYMBOL.put(WEBLOGIC,  ComponentBundle.VENDOR_ORACLE);
    SYMBOL.put(WEBSPHERE, ComponentBundle.VENDOR_IBM);
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the server platform
  private transient JComboBox<String>  serverPlatform;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityServerPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityServerPanel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (EndpointPanel)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    // ensure inheritance
    super.initializeComponent();

    // overide the status area dimension
    final JTextArea area = this.testResult.textArea();
    area.setPreferredSize(new Dimension(450, 60));

    this.serverPlatform = new JComboBox<String>();
    this.serverPlatform.setModel(new DefaultComboBoxModel<String>(PLATFORM.keySet().toArray(new String[0])));
    this.serverPlatform.setRenderer(new JEEPlatformRenderer(PLATFORM));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (EndpointPanel)
  /**
   ** Layout the panel.
   */
  @Override
  protected void initializeLayout() {
    final FormLayout         layout     = new FormLayout(
    //   |         1           |       2        |        3        |     4      |     5      |
      "4dlu, r:max(50dlu;p), 3dlu, pref:grow, 3dlu, right:pref, 3dlu, 15dlu, 3dlu, 15dlu, 4dlu"
    , "pref, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref, 4dlu, fill:pref:grow, 6dlu"
    //   1     2     |     3     |     4     |     5     |     6     |    7      |     8     |     9     |    10     |         11
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.IDENTITY_SERVER_HEADER), constraint.xyw(2, row, 9));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(Bundle.string(Bundle.CONNECTION_NAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.connectionName);
    builder.add(this.connectionName, constraint.xyw(4, row, 7));

    // the 3rd logical row of the layout
    row += 2;
    builder.addSeparator(ComponentBundle.string(ComponentBundle.SERVER_GENERAL_HEADER), constraint.xywh(2, row, 9, 1));

    // the 4th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PLATFORM_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverPlatform);
    builder.add(this.serverPlatform, constraint.xyw(4, row, 7));

    // the 5th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_HOST_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serverHost);
    builder.add(this.serverHost, constraint.xyw(4, row, 1));
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PORT_LABEL), constraint.xy(6, row));
    label.setLabelFor(this.serverPort);
    builder.add(this.serverPort, constraint.xyw(8, row, 3));

    // the 6th logical row of the layout
    row += 2;
    builder.addSeparator(ComponentBundle.string(ComponentBundle.SERVER_ACCOUNT_HEADER), constraint.xyw(2, row, 9));

    // the 7th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_USERNAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.principalUsername);
    builder.add(this.principalUsername, constraint.xyw(4, row, 7));

    // the 8th logical row of the layout
    row += 2;
    label = builder.addLabel(ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.principalPassword);
    builder.add(this.principalPassword, constraint.xyw(4, row, 7));

    // the 9th logical row of the layout
    row += 2;
    builder.add(this.testConnection, constraint.xyw(6, row, 5));

    // the 10th logical row of the layout
    row += 2;
    builder.add(this.testResult,     constraint.xywh(2, row, 9, 2));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (EndpointPanel)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    ResourceUtils.resButton(this.testConnection, Bundle.string(Bundle.IDENTITY_TEST_LABEL));
    this.testConnection.setToolTipText(Bundle.string(Bundle.IDENTITY_TEST_HINT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializePage (EndpointPanel)
  /**
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the {@link TraversableContext} passed to this page
   ** on method {@link #onEntry(TraversableContext)}.
   */
  @Override
  protected final void initializePage() {
    this.connectionName.setText(this.model.name());
    this.serverHost.setText(this.model.serverName());
    this.serverPort.setValue(this.model.serverPort());
    this.principalUsername.setText(this.model.principalName());
    this.principalPassword.setText(this.model.principalPassword());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testConnection (EndpointPanel)
  /**
   ** Performs all activities to test a connection.
   */
  @Override
  public void testConnection() {
    String           status  = null;
    IdentityService  service = null;
    final WaitCursor cursor  = new WaitCursor(this);
    // schedules the wait cursor to be shown after 100 milliseconds has
    // elapsed
    cursor.show(100);
    try {
      // this will raise a commit of the data to the traversable context
      // this will not do anything with the artifacts that this page configures
      // if any error occurs during this phase the wizard will stay on this page
      commitProperty();

      this.testResult.setText(Bundle.string(Bundle.IDENTITY_TEST_WORKING), false);
      service = new IdentityService((IdentityServer)this.model);
      service.connect();
    }
    catch (Throwable e) {
      if (e.getCause() != null)
        status = Bundle.format(Bundle.IDENTITY_TEST_CANCELLED, e.getCause().getLocalizedMessage());
      else
        status = Bundle.format(Bundle.IDENTITY_TEST_CANCELLED, e.getLocalizedMessage());
    }
    finally {
      // hides the wait cursor if visible.
      cursor.hide();
      if (status == null)
        status = Bundle.string(Bundle.IDENTITY_TEST_SUCCESS);

      this.testResult.setText(status, false);
      if (service != null)
        service.disconnect();
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
      commitUsername();
      commitPassword();
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
  private final void commitHost()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serverHost.getText();

    // check the rules and validate
    validateString(IdentityServer.SERVER_NAME, value, ComponentBundle.string(ComponentBundle.SERVER_HOST_ERROR));
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
  private final void commitPort()
    throws TraversalException {

    // obtained the value from the UI to store.
    final int value = this.serverPort.getValue();

    // check the rules and validate
    validateRange(IdentityServer.SERVER_PORT, value, 0, 65535, ComponentBundle.format(ComponentBundle.SERVER_PORT_RANGE, 0, 65535));

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
  private final void commitUsername()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.principalUsername.getText();

    // check the rules and validate
    validateString(IdentityServer.PRINCIPAL_NAME, value, ComponentBundle.string(ComponentBundle.SERVER_USERNAME_ERROR));
    // store the provided value in the model
    this.model.principalName(value.trim());
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
  private final void commitPassword()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = new String(this.principalPassword.getPassword());

    // check the rules and validate
    validateString(IdentityServer.PRINCIPAL_PASSWORD, value, ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_ERROR));
    // store the provided value in the model
    this.model.principalPassword(value.trim());
  }
}