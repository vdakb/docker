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

    File        :   DirectoryContextExport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContextExport.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.io.File;

import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.ide.net.URLChooser;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.javatools.dialogs.MessageDialog;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.jdeveloper.workspace.iam.swing.LabelFactory;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;
import oracle.jdeveloper.connection.iam.support.LDAPFile;
import oracle.jdeveloper.connection.iam.wizard.DirectoryContextFile.Option;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryContextExport
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the information about to export
 ** entries from a Directory Service.
 ** <p>
 ** The class is package protected to ensure that only the appropriate command
 ** is able to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class DirectoryContextExport extends DirectoryContextFile {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The DATA key should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   */
  public static final String            SUB              = "ods/subEntry";
  public static final String            OPR              = "ods/omitOperational";
  public static final String            ATT              = "ods/attributeOnly";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2943307237317473033")
  private static final long             serialVersionUID = -877317952630064154L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the components to specify the file location of the output
  private final transient JTextField   exportFile;
  private final transient JButton      exportBrowse;

  // the components to specify which entries should be exported
  private final transient JCheckBox    exportSubEntry;
  private final transient JCheckBox    omitOperational;
  private final transient JCheckBox    attributesOnly;

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
  DirectoryContextExport() {
    // ensure inheritance
    super();

    // the components to specify the file location of the output
    this.exportFile     = new JTextField();
    this.exportBrowse   = new JButton();
    this.exportBrowse.setIcon(ComponentBundle.icon(ComponentBundle.FILE_ICON));
    this.exportBrowse.setToolTipText(Bundle.string(Bundle.EXPORT_FILENAME_HINT));
    this.exportBrowse.addActionListener(exportFileListener());
    IconicButtonUI.install(this.exportBrowse);

    // the components to specify which entries should be exported
    this.exportSubEntry  = new JCheckBox();
    this.omitOperational = new JCheckBox();
    this.attributesOnly  = new JCheckBox();
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

    context.put(FILE, new File(this.exportFile.getText()));
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

    // verify that the file is accessible
    if (JEWTDialog.isDialogClosingEvent(event)) {
      if (StringUtility.empty(this.exportFile.getText())) {
        MessageDialog.error(this, Bundle.string(Bundle.EXPORT_FILENAME_REQUIRED), Bundle.string(Bundle.EXPORT_TITLE), null);
        // the exceptions is swallowed at the dialog so no harm to make it
        // meaningfull
        throw new PropertyVetoException("", event);
      }
      // verify that the file is accessible
      writable(event, Bundle.string(Bundle.EXPORT_FILENAME_TITLE), new File(this.exportFile.getText()));
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
    return this.exportFile;
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

    this.exportSubEntry.setSelected((Boolean)context.get(SUB));
    this.omitOperational.setSelected((Boolean)context.get(OPR));
    this.attributesOnly.setSelected((Boolean)context.get(ATT));

    // arm the option items to track change
    this.exportSubEntry.addItemListener(new Option(context,  SUB));
    this.omitOperational.addItemListener(new Option(context, OPR));
    this.attributesOnly.addItemListener(new Option(context,  ATT));

    // initialize the layout of the panel
    final FormLayout         layout    = new FormLayout(
    //   |     2     |     4     |     6     |     8     |    10     |      12     |    14     |   16      |     18         |     19     |
      "4dlu, pref, 3dlu, pref, 2dlu, pref, 6dlu, pref, 2dlu, pref, 6dlu,   pref, 2dlu, pref, 6dlu, pref, 2dlu, pref:grow, 1dlu, 15dlu, 0dlu"
    , "1dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 12dlu, pref, 12dlu, pref, 1dlu"
    //   |     2     |     4     |     6     |     8     |    10     |      11     |
    );

    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    builder.add(LabelFactory.label(Bundle.string(Bundle.EXPORT_CONTEXT_LABEL)), constraint.xy(2, 2));
    builder.add(LabelFactory.build().prompt(this.entry.name().toString()).bold().label(), constraint.xyw(4, 2, 15));

    builder.add(this.exportSubEntry, constraint.xy(4, 4));
    builder.add(LabelFactory.label(Bundle.string(Bundle.EXPORT_SUBENTRY_LABEL), this.exportSubEntry), constraint.xyw(6, 4, 11));
    builder.add(this.omitOperational, constraint.xy(4, 6));
    builder.add(LabelFactory.label(Bundle.string(Bundle.EXPORT_OPERATIONAL_LABEL), this.omitOperational), constraint.xyw(6, 6, 11));
    builder.add(this.attributesOnly, constraint.xy(4, 8));
    builder.add(LabelFactory.label(Bundle.string(Bundle.EXPORT_ATTRIBUTE_LABEL), this.attributesOnly), constraint.xyw(6, 8, 11));

    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_LABEL)), constraint.xy(2, 10));
    builder.add(this.xml2, constraint.xy(4, 10));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_XML2), this.xml2), constraint.xy(6, 10));
    builder.add(this.xml1, constraint.xy(8, 10));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_XML1), this.xml1), constraint.xy(10, 10));
    builder.add(this.ldif, constraint.xy(12, 10));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_LDIF), this.ldif), constraint.xy(14, 10));
    builder.add(this.json, constraint.xy(16, 10));
    builder.add(LabelFactory.label(Bundle.string(Bundle.FILE_FORMAT_JSON), this.json), constraint.xy(18, 10));

    builder.add(LabelFactory.label(Bundle.string(Bundle.EXPORT_FILENAME_LABEL), this.exportFile), constraint.xy(2, 12));
    builder.add(this.exportFile,   constraint.xyw(4, 12, 15));
    builder.add(this.exportBrowse, constraint.xy(20, 12));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportFileListener
  private ActionListener exportFileListener() {
    return new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent event) {
        // the format is hold at the ButtonGroup initialized at the constructor
        // ButtonGroup tells Java that only on of the JRadioButtons of the group
        // should be selected
        final LDAPFile.Format format  = LDAPFile.Format.from(DirectoryContextExport.this.format.getSelection().getActionCommand());
        final URLChooser chooser = chooser(Bundle.string(Bundle.EXPORT_FILTER_FILE), format.file);
        if (chooseTarget(Bundle.string(Bundle.EXPORT_FILENAME_TITLE), chooser, DirectoryContextExport.this.exportFile.getText())) {
          DirectoryContextExport.this.exportFile.setText(selected.getAbsolutePath());
        }
      }
    };
  }
}