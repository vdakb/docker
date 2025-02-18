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

    File        :   MetadataServerPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataServerPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;

import oracle.ide.controls.WaitCursor;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;

import oracle.ide.util.ResourceUtils;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.MetadataServer;

import oracle.jdeveloper.connection.iam.service.MetadataService;

////////////////////////////////////////////////////////////////////////////////
// class MetadataServerPanel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the configuration dialog to create and
 ** modify a Metadata Service connection.
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
public class MetadataServerPanel extends EndpointPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4018093152936435502")
  private static final long           serialVersionUID = -5601726687650143284L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the server properties
  private transient JTextField        databaseService;

  private transient JComboBox<String> partitionPicker;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class PartitonPopulate
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** Implementation of data classes which can be integrated with the IDE
   ** framework.
   */
  class PartitonPopulate implements PopupMenuListener {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>PartitonPopulate</code>.
     */
    PartitonPopulate() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: popupMenuWillBecomeVisible (PopupMenuListener)
    /**
     ** This method is called before the popup menu becomes visible
     */
    @Override
    public final void popupMenuWillBecomeVisible(final PopupMenuEvent event) {
      populatePartition();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: popupMenuWillBecomeInvisible (PopupMenuListener)
    /**
     ** This method is called before the popup menu becomes invisible
     ** Note that a JPopupMenu can become invisible any time
     */
    @Override
    public final void popupMenuWillBecomeInvisible(final PopupMenuEvent event) {
      
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: popupMenuCanceled (PopupMenuListener)
    /**
     ** This method is called when the popup menu is canceled
     */
    @Override
    public final void popupMenuCanceled(final PopupMenuEvent event) {
      
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MetadataServerPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MetadataServerPanel() {
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
    //   |         1           |       2        |        3        |     4      |     5      |
      "4dlu, r:max(50dlu;p), 3dlu, pref:grow, 3dlu, right:pref, 3dlu, 15dlu, 3dlu, 15dlu, 4dlu"
    , "6dlu, pref, 6dlu, pref, 6dlu, pref, 6dlu, pref, 3dlu, pref, 6dlu, pref, 6dlu, pref, 3dlu, pref, 6dlu, pref, 6dlu, pref, 6dlu, pref, 6dlu, fill:pref:grow, 6dlu"
    //   |    1     |     2     |     3      |     4     |     5     |     6     |     7     |     8     |     9    |     10    |     11     |        12          |
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.METADATA_GENERAL_HEADER), constraint.xyw(2, row, 9));

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
    label = builder.addLabel(Bundle.string(Bundle.METADATA_CONTEXT_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.databaseService);
    builder.add(this.databaseService, constraint.xyw(4, row, 7));

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
    builder.addSeparator(Bundle.string(Bundle.METADATA_PARTITION_HEADER), constraint.xyw(2, row, 9));

    // the 10th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.METADATA_PARTITION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.partitionPicker);
    builder.add(this.partitionPicker,   constraint.xyw( 4, row, 7));

    // the 11th logical row of the layout
    row += 2;
    builder.add(this.testConnection, constraint.xyw(6, row, 5));

    // the 12th logical row of the layout
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
    ResourceUtils.resButton(this.testConnection, Bundle.string(Bundle.METADATA_TEST_LABEL));
    this.testConnection.setToolTipText(Bundle.string(Bundle.METADATA_TEST_HINT));
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
    this.databaseService.setText(this.model.serverContext());
    this.principalUsername.setText(this.model.principalName());
    this.principalPassword.setText(this.model.principalPassword());

    if (!connectionNew()) {
      this.connectionName.setEditable(false);
      populatePartition();
      this.partitionPicker.setSelectedItem(((MetadataServer)this.model).partition());
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
    MetadataService  service = null;
    final WaitCursor cursor  = new WaitCursor(this);
    // schedules the wait cursor to be shown after 100 milliseconds has
    // elapsed
    cursor.show(100);
    try {
      // this will raise a commit of the data to the traversable context
      // this will not do anything with the artifacts that this page configures
      // if any error occurs during this phase the wizard will stay on this page
      commitProperty();
      this.testResult.setText(Bundle.string(Bundle.METADATA_TEST_WORKING), false);
      service = new MetadataService((MetadataServer)this.model);
      service.connect();
    }
    catch (Throwable e) {
      if (e.getCause() != null)
        status = Bundle.format(Bundle.METADATA_TEST_CANCELLED, e.getCause().getLocalizedMessage());
      else
        status = Bundle.format(Bundle.METADATA_TEST_CANCELLED, e.getLocalizedMessage());
    }
    finally {
      // hides the wait cursor if visible.
      cursor.hide();
      if (status == null)
        status = Bundle.string(Bundle.METADATA_TEST_SUCCESS);
      this.testResult.setText(status, false);
      if (service != null)
        try {
          service.disconnect();
        }
        catch (Throwable e) {
          status = e.getLocalizedMessage();
          MessageDialog.error(Ide.getMainWindow(), e.getMessage(), Bundle.string(Bundle.METADATA_TEST_HINT), null);
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
    this.databaseService = new JTextField();
    this.partitionPicker = new JComboBox<String>(new DefaultComboBoxModel<String>());

    this.partitionPicker.addPopupMenuListener(new PartitonPopulate());
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
    commitUsername();
    commitPassword();
    commitPartition();
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
    validateString(MetadataServer.SERVER_NAME, value, ComponentBundle.string(ComponentBundle.SERVER_HOST_ERROR));
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
    validateRange(MetadataServer.SERVER_PORT, value, 1, 65535, ComponentBundle.format(ComponentBundle.SERVER_PORT_RANGE, 1, 65535));

    // store the provided value in the model
    this.model.serverPort(String.valueOf(value));
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
  private final void commitContext()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.databaseService.getText();

    // check the rules and validate
    validateString(MetadataServer.SERVER_CONTEXT, value, Bundle.string(Bundle.METADATA_CONTEXT_ERROR));
    // store the provided value in the model
    this.model.serverContext(value.trim());
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

    // obtained the value from the UI to store.
    final String value = this.principalUsername.getText();

    // check the rules and validate
    validateString(MetadataServer.PRINCIPAL_NAME, value, ComponentBundle.string(ComponentBundle.SERVER_USERNAME_ERROR));
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
  private void commitPassword()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = new String(this.principalPassword.getPassword());

    // check the rules and validate
    validateString(MetadataServer.PRINCIPAL_PASSWORD, value, ComponentBundle.string(ComponentBundle.SERVER_PASSWORD_ERROR));
    // store the provided value in the model
    this.model.principalPassword(value.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitPartition
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>partition</code> must be evaluate to a
   ** valid string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitPartition()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = (String)this.partitionPicker.getSelectedItem();

    // check the rules and validate
    validateString(MetadataServer.PARTITION_NAME, value, Bundle.string(Bundle.METADATA_PARTITION_ERROR));
    // store the provided value in the context
    ((MetadataServer)this.model).partition(value.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populatePartition
  /**
   ** Populates the partion picke model by queriying the repostitory.
   */
  private void populatePartition() {
    try {
      // this will raise a commit of the data to the traversable context
      // this will not do anything with the artifacts that this page configures
      // if any error occurs during this phase the wizard will stay on this page
      commitHost();
      commitPort();
      commitContext();
      commitUsername();
      commitPassword();
    }
    catch (TraversalException e) {
      this.testResult.setText(e.getLocalizedMessage(), false);
      return;
    }

    // attach a WaitCursor to the RootPaneContainer of this panel
    final WaitCursor cursor = new WaitCursor(this.partitionPicker);
    // schedules the wait cursor to be shown after 10 milliseconds has elapsed
    cursor.show(10);

    this.testResult.setText(null, false);
    final DefaultComboBoxModel<String> partition = (DefaultComboBoxModel<String>)this.partitionPicker.getModel();
    partition.removeAllElements();
    try {
      final MetadataService service = new MetadataService((MetadataServer)this.model);
      for (String element : service.partitions()) {
        partition.addElement(element);
      }
    }
    catch (Throwable e) {
      if (e.getCause() != null)
        this.testResult.setText(e.getCause().getLocalizedMessage(), false);
      else
        this.testResult.setText(e.getLocalizedMessage(), false);
    }
    finally {
      // hides the wait cursor if visible.
      cursor.hide();
    }
  }
}