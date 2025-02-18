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

    File        :   DirectoryContextImport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContextImport.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.io.File;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.javatools.dialogs.MessageDialog;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.net.URLChooser;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.swing.LabelFactory;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;
import oracle.jdeveloper.connection.iam.support.LDAPFile;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryContextImport
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the information about to import
 ** entries into a Directory Service.
 ** <p>
 ** The class is package protected to ensure that only the appropriate command
 ** is able to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class DirectoryContextImport extends DirectoryContextFile {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The DATA key should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   */
  public static final String           ERROR            = "ods/error";
  public static final String           STOP             = "ods/stopOnEror";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6076443022060330632")
  private static final long            serialVersionUID = 4039221445107314031L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the components to specify the file location of the input
  private final transient JTextField   importFile;
  private final transient JButton      importBrowse;

  // the component to specify the file location of the errors
  private final transient JTextField   errorFile;
  private final transient JButton      errorBrowse;

  // the component to specify if any error should stop the entire process
  private final transient JCheckBox    errorStop;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContextExport</code> panel that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  DirectoryContextImport() {
    // ensure inheritance
    super();

    // the components to specify the file location of the input
    this.importFile    = new JTextField();
    this.importBrowse  = new JButton();
    this.importBrowse.setIcon(ComponentBundle.icon(ComponentBundle.FILE_ICON));
    this.importBrowse.setToolTipText(Bundle.string(Bundle.IMPORT_FILENAME_HINT));
    this.importBrowse.addActionListener(importFileListener());
    IconicButtonUI.install(this.importBrowse);

    // the component to specify the file location of the errors
    this.errorFile     = new JTextField();
    this.errorBrowse   = new JButton();
    this.errorBrowse.setIcon(ComponentBundle.icon(ComponentBundle.FILE_ICON));
    this.errorBrowse.setToolTipText(Bundle.string(Bundle.IMPORT_FILEERROR_HINT));
    this.errorBrowse.addActionListener(errorFileListener());
    IconicButtonUI.install(this.errorBrowse);

    // the component to specify if any error should stop the entire process
    this.errorStop = new JCheckBox();
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

    context.put(FILE,  new File(this.importFile.getText()));
    context.put(ERROR, new File(this.errorFile.getText()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

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

    if (JEWTDialog.isDialogClosingEvent(event)) {
      final String importFile = this.importFile.getText();
      if (StringUtility.empty(importFile)) {
        MessageDialog.error(this, Bundle.string(Bundle.IMPORT_FILENAME_REQUIRED), Bundle.string(Bundle.IMPORT_TITLE), null);
        // the exceptions is swallowed at the dialog so no harm to make it
        // meaningfull
        throw new PropertyVetoException("", event);
      }
      // verify that the import file is readable
      readable(event, Bundle.string(Bundle.IMPORT_FILENAME_TITLE), new File(importFile));

      final String errorFile = this.errorFile.getText();
      // verify that the import file and error file are not the same
      if (importFile.equals(errorFile)) {
        MessageDialog.error(this, Bundle.string(Bundle.IMPORT_FILEERROR_CONFLICT), Bundle.string(Bundle.IMPORT_TITLE), null);
        // the exceptions is swallowed at the dialog so no harm to make it
        // meaningfull
        throw new PropertyVetoException("", event);
      }

      if (StringUtility.empty(errorFile)) {
        MessageDialog.error(this, Bundle.string(Bundle.IMPORT_FILEERROR_REQUIRED), Bundle.string(Bundle.IMPORT_TITLE), null);
        // the exceptions is swallowed at the dialog so no harm to make it
        // meaningfull
        throw new PropertyVetoException("", event);
      }
      // verify that the error file is writable
      writable(event, Bundle.string(Bundle.IMPORT_FILEERROR_TITLE), new File(errorFile));
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
    return this.importFile;
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

    this.errorStop.setSelected((Boolean)context.get(STOP));

    // arm the option items to track change
    this.errorStop.addItemListener(new Option(context, STOP));

    // initialize the layout of the panel
    final FormLayout         layout    = new FormLayout(
    //   |     2     |     4     |     6      |     8     |    10     |    12     |    14     |   16      |     18         |     19     |
      "4dlu, pref, 3dlu, pref,  2dlu, pref,  6dlu, pref, 2dlu, pref, 6dlu, pref, 2dlu, pref, 6dlu, pref, 2dlu, pref:grow, 1dlu, 15dlu, 0dlu"
    , "1dlu, pref, 3dlu, pref, 12dlu, pref, 12dlu, pref, 3dlu, pref, 1dlu"
    //   |     2     |     4     |     6      |     8      |    10     |
    );

    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    builder.add(LabelFactory.label(Bundle.string(Bundle.IMPORT_CONTEXT_LABEL)), constraint.xy(2, 2));
    builder.add(LabelFactory.build().prompt(this.entry.name().toString()).bold().label(), constraint.xyw(4, 2, 15));

    builder.add(this.errorStop, constraint.xy(4, 4));
    builder.add(LabelFactory.label(Bundle.string(Bundle.IMPORT_STOP_LABEL), this.errorStop), constraint.xyw(6, 4, 11));

    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_LABEL)), constraint.xy(2, 6));
    builder.add(this.xml2, constraint.xy(4, 6));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_XML2), this.xml2), constraint.xy(6, 6));
    builder.add(this.xml1, constraint.xy(8, 6));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_XML1), this.xml1), constraint.xy(10, 6));
    builder.add(this.ldif, constraint.xy(12, 6));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_LDIF), this.ldif), constraint.xy(14, 6));
    builder.add(this.json, constraint.xy(16, 6));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_JSON), this.json), constraint.xy(18, 6));

    builder.add(LabelFactory.label(Bundle.string(Bundle.IMPORT_FILENAME_LABEL), this.importFile), constraint.xy(2, 8));
    builder.add(this.importFile,   constraint.xyw(4, 8, 15));
    builder.add(this.importBrowse, constraint.xy(20, 8));

    builder.add(LabelFactory.label(Bundle.string(Bundle.IMPORT_FILEERROR_LABEL), this.errorFile), constraint.xy(2, 10));
    builder.add(this.errorFile,   constraint.xyw(4, 10, 15));
    builder.add(this.errorBrowse, constraint.xy(20, 10));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importFileListener
  private ActionListener importFileListener() {
    return new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent event) {
        // the format is hold at the ButtonGroup initialized at the constructor
        // ButtonGroup tells Java that only on of the JRadioButtons of the group
        // should be selected
        final LDAPFile.Format format  = LDAPFile.Format.from(DirectoryContextImport.this.format.getSelection().getActionCommand());
        final URLChooser      chooser = chooser(Bundle.string(Bundle.IMPORT_FILTER_FILE), format.file);
        if (chooseSource(Bundle.string(Bundle.IMPORT_FILENAME_TITLE), chooser)) {
          DirectoryContextImport.this.importFile.setText(selected.getAbsolutePath());
        }
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorFileListener
  private ActionListener errorFileListener() {
    return new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent event) {
        // the format is hold at the ButtonGroup initialized at the constructor
        // ButtonGroup tells Java that only on of the JRadioButtons of the group
        // should be selected
        final LDAPFile.Format format  = LDAPFile.Format.from(DirectoryContextImport.this.format.getSelection().getActionCommand());
        final URLChooser      chooser = chooser(Bundle.string(Bundle.IMPORT_FILTER_ERROR), format.file);
        if (chooseTarget(Bundle.string(Bundle.IMPORT_FILEERROR_TITLE), chooser, DirectoryContextImport.this.errorFile.getText())) {
          DirectoryContextImport.this.errorFile.setText(selected.getAbsolutePath());
        }
      }
    };
  }
}