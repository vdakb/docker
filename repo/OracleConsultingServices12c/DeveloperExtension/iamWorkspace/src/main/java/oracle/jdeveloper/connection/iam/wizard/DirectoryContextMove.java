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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryContextMove.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContextMove.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.awt.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import javax.swing.JTextField;

import javax.naming.NamingException;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.swing.GridBagBuilder;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.model.DirectoryName;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryContextMove
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the information about to move an
 ** entry in a Directory Service.
 ** <p>
 ** The class is private to ensure that only the appropriate command is able
 ** to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class DirectoryContextMove extends DirectoryContextPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1115239867367856421")
  private static final long serialVersionUID = 6570248965094468522L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure entry location
  private JTextField        entryPrefix;
  private JTextField        entrySuffix;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContextMove</code> panel that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  DirectoryContextMove() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExit (Traversable)
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

    final String name = String.format("%s,%s", this.entryPrefix.getText(), this.entrySuffix.getText());
    try {
      context.put(NAME, DirectoryName.build(name));
    }
    catch (NamingException e) {
      // if a naming excpetion occures the entry name could not be parsed
      // in this case we hope that the string returned si the unparsed text so
      // we can use it to formulate the message text
      throw new TraversalException(Bundle.format(Bundle.ENTRY_RENAME_NAME_INVALID, name));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   vetoableChange (VetoableChangeListener)
  /**
   ** This method gets called when a constrained property is changed.
   **
   ** @param  event            a {@link PropertyChangeEvent} object
   **                          describing the event source and the property
   **                          that has changed.
   **                          <br>
   **                          Allowed object is {@link PropertyChangeEvent}.
   **
   ** @throws PropertyVetoException if the recipient wishes the property
   **                               change to be rolled back.
   */
  @Override
  public void vetoableChange(final PropertyChangeEvent event)
    throws PropertyVetoException {

    if (StringUtility.empty(this.entryPrefix.getText())) {
      // notify the user about an unanticipated condition that prevents the task
      // from completing successfully
      MessageDialog.error(this, Bundle.string(Bundle.ENTRY_RENAME_PREFIX_REQUIRED), Bundle.string(Bundle.ENTRY_MOVE_TITLE), null);
      throw new PropertyVetoException("", event);
    }

    if (StringUtility.empty(this.entrySuffix.getText())) {
      // notify the user about an unanticipated condition that prevents the task
      // from completing successfully
      MessageDialog.error(this, Bundle.string(Bundle.ENTRY_RENAME_PREFIX_REQUIRED), Bundle.string(Bundle.ENTRY_MOVE_TITLE), null);
      throw new PropertyVetoException("", event);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialFocus (DirectoryContextPanel)
  /**
   ** Returns the {@link Component} that will get the focus if the dialog window
   ** containing this panel is opened.
   **
   ** @return                    the {@link Component} that will get the focus.
   **                            <br>
   **                            Allowed object is {@link Component}.
   */
  public final Component initialFocus() {
    return this.entrySuffix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
    // ensure inheritance
    super.onEntry(context);

    final DirectoryName name = (DirectoryName)context.get(NAME);

    // create a object of JTextField with 20 columns that hold the entry prefix
    this.entryPrefix = new JTextField(20);
    this.entryPrefix.setText(name.prefix().toString());
    this.entryPrefix.setSelectionEnd(-1);
    this.entryPrefix.setSelectionStart(-1);
    this.entryPrefix.setCaretPosition(this.entryPrefix.getText().length());

    // create a object of JTextField with 20 columns that hold the entry suffix
    this.entrySuffix = new JTextField(20);
    this.entrySuffix.setText(name.suffix().toString());
    this.entrySuffix.setSelectionEnd(-1);
    this.entrySuffix.setSelectionStart(-1);
    this.entrySuffix.setCaretPosition(this.entrySuffix.getText().length());

    // initialize layout
    final GridBagBuilder layout = GridBagBuilder.build(this);
    layout.append(Bundle.string(Bundle.ENTRY_TARGET_LABEL), this.entrySuffix);
    layout.append(Bundle.string(Bundle.ENTRY_ORIGIN_LABEL), this.entryPrefix);
  }
}