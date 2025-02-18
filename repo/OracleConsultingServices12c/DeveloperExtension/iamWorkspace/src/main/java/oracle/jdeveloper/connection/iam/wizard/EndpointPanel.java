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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   EndpointPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import oracle.ide.controls.WholeNumberField;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.Endpoint;

import oracle.jdeveloper.workspace.iam.swing.widget.AbstractPanel;
import oracle.jdeveloper.workspace.iam.swing.widget.ReadOnlyScrollPane;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointPanel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the Connection dialog for editing the
 ** data stored in the model.
 ** <p>
 ** In general, connection panels are not supposed to be published APIs, so we
 ** enforce that. Even though the panel is constructed by the IDE framework
 ** using reflection, the IDE framework does not require that it is public (only
 ** that it has a no-argument constructor).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class EndpointPanel extends AbstractPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String             CONNECTION_NAME  = "ConnectionNameKey";
  public static final String             CONNECTION_MODEL = "ConnectionDescriptor";
  public static final String             CONNECTION_STATE = "ConnectionStateKey";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8060821409507497188")
  private static final long              serialVersionUID = -6007842501293818487L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the general properties
  protected transient JTextField         connectionName;
  // the properties to configure the server properties
  protected transient JTextField         serverHost;
  protected transient WholeNumberField   serverPort;
  // the properties to configure the account properties
  protected transient JTextField         principalUsername;
  protected transient JPasswordField     principalPassword;

  protected transient JButton            testConnection;
  protected transient ReadOnlyScrollPane testResult;
  protected transient Endpoint           model     = null;

  private transient   TraversableContext context   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointPanel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  protected final boolean connectionNew() {
    return (this.context == null || (Boolean)context.get(CONNECTION_STATE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (AbstractPanel)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    this.connectionName    = new JTextField();
    this.serverHost        = new JTextField();
    this.serverPort        = new WholeNumberField(6);
    this.principalUsername = new JTextField();
    this.principalPassword = new JPasswordField();
    this.testConnection    = new JButton();
    this.testResult        = new ReadOnlyScrollPane();

    this.testConnection.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent event) {
          testConnection();
        }
      }
    );
    final JTextArea area  = this.testResult.textArea();
    area.setPreferredSize(new Dimension(350, 60));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
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
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   */
  @Override
  public void onEntry(final TraversableContext context) {
    this.context = context;
    this.model = ((Endpoint)context.get(CONNECTION_MODEL));
    this.model.name((String)context.get(CONNECTION_NAME));
    // On entering the panel, we need to populate all fields with properties
    // from the model object.
    initializePage();
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
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
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

    // this will raise a commit of the data to the traversable context
    // this will not do anything with the artifacts that this page configures
    // if any error occurs during this phase the wizard will stay on this page
    commit();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializePage
  /**
   ** On entering the panel, we need to populate all fields with properties from
   ** the model object.
   */
  protected abstract void initializePage();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testConnection
  /**
   ** Performs all activities to test a connection.
   */
  protected abstract void testConnection();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Called to have this {@link DefaultTraversablePanel} perform the commit
   ** action.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  protected abstract void commit()
    throws TraversalException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>name</code> must be evaluate to a valid
   ** string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  protected void commitName()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.connectionName.getText();

    // check the rules and validate
    validateString(CONNECTION_NAME, value, Bundle.string(Bundle.CONNECTION_NAME_ERROR));
    // store the provided value in the context
    this.context.put(CONNECTION_NAME, value.trim());
    // store the provided value in the context
    this.model.name(value.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitServerSocketSSL
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>socketSSL</code> and <code>socketTLS</code>
   ** must be evaluate to a valid combination.
   ** <br>
   ** Either socketSSL or socketTLS can be specified; not both
   **
   ** @param  socketSSL          the state to commit for socketSSL.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  socketTLS          the state to commit for socketTLS.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  protected void commitServerSocket(final boolean socketSSL, final boolean socketTLS)
    throws TraversalException {

    // check the rules and validate
    if (socketSSL && socketTLS)
      throw new TraversalException(Bundle.string(Bundle.CONTEXT_ERROR_NETWORK_SECURITY), ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, "ssl"));

    // store the provided value in the model
    this.model.serverSocketSSL(socketSSL);
    this.model.serverSocketTLS(socketTLS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateString
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>value</code> must be evaluate to a
   ** valid string.
   **
   ** @param  option             the option that is subject of the violation.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the string to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the human readable string of the exception
   **                            if a violation occurs.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final void validateString(final String option, final String value, final String message)
    throws TraversalException {

    if (StringUtility.empty(value))
      throw new TraversalException(message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateRange
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>value</code> must be evaluate to a
   ** non-empty string.
   **
   ** @param  option             the option that is subject of the violation.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value to validate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  lowerBound         the lower bound value.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  higherBound        the higher bound value.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  message            the human readable string of the exception
   **                            if a range violation occurs.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final void validateRange(final String option, final int value, final int lowerBound, final int higherBound, final String message)
    throws TraversalException {

    if ((value < lowerBound) || (value > higherBound))
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integer
  /**
   ** Converts the given String value to an native <code>int</code> value.
   **
   ** @param  option             the option that is subject of the violation.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the human readable string of the exception
   **                            if a format violation occurs.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the converted value.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final int integer(final String option, final String value, final String message)
    throws TraversalException {

    // checking valid integer using parseInt() method
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException e) {
      // notify the user about an unanticipated condition that prevents the task
      // from completing successfully
      throw new TraversalException(message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option));
    }
  }
}