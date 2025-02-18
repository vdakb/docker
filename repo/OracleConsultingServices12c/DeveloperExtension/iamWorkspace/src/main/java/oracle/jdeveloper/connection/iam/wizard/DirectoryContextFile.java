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

    File        :   DirectoryContextFile.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContextFile.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.io.File;

import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import oracle.ide.util.Namespace;

import oracle.ide.net.URLChooser;
import oracle.ide.net.DefaultURLFilter;

import oracle.ide.dialogs.DialogUtil;

import oracle.ide.panels.TraversableContext;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.support.LDAPFile;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryContextFile
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the information about to export or
 ** import data from a Directory Service.
 ** <p>
 ** The class is package protected to ensure that only the appropriate command
 ** is able to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public abstract class DirectoryContextFile extends DirectoryContextPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The DATA key should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   */
  public static final String             FILE             = "ods/file";
  public static final String             FMT              = "ods/format";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5206891318362688419")
  private static final long              serialVersionUID = -2420914598772119942L;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The abstract path previously used in a choose file dialog to give the user
   ** the experience of a <code>remeber me</code>.
   */
  static File                            selected;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the component to specify the file format to produce
  protected final transient ButtonGroup  format;
  protected final transient JRadioButton xml2;
  protected final transient JRadioButton xml1;
  protected final transient JRadioButton ldif;
  protected final transient JRadioButton json;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Listener
  // ~~~~~ ~~~~~~~~
  /**
   ** A listener to obtain information from a UI component that transfers the
   ** data entered to the encapsulted namespace storage.
   */
  private static class Listener {

    ////////////////////////////////////////////////////////////////////////////
    // instances attributes
    ////////////////////////////////////////////////////////////////////////////

    final String    path;
    final Namespace data;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Listener</code> that maintains <code>path</code> in
     ** the {@link Namespace} of the panel associated with.
     **
     ** @param  data             the {@link Namespace} to observe and commit.
     **                          <br>
     **                          Allowed object is {@link Namespace}.
     ** @param  path             the identifier of a {@link HashStructure} that
     **                          is part of the {@link TraversableContext} and
     **                          belongs to <code>namespace</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public Listener(final Namespace data, final String path) {
      // ensure inheritance
      super();

      // initialize instance
      this.data = data;
      this.path = path;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   apply
    /**
     ** Invoked when an action occurs.
     **
     ** @param  value            an {@link Object} value instance to apply as
     **                          the property that has changed.
     **                          <br>
     **                          Allowed object is {@link Object}.
     */
    protected final void apply(final Object value) {
      this.data.put(this.path, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Option
  // ~~~~~ ~~~~~~
  /**
   ** An {@link ItemListener} capturing changes on the export option.
   */
  static class Option extends    Listener
                      implements ItemListener {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Option</code> that maintains <code>contextKey</code>
     ** in the {@link TraversableContext} of the panel associated with.
     **
     ** @param  data             the {@link Namespace} to observe and commit.
     **                          <br>
     **                          Allowed object is {@link Namespace}.
     ** @param  path             the identifier of a {@link HashStructure} that
     **                          is part of the {@link TraversableContext} and
     **                          belongs to <code>namespace</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Option(final Namespace data, final String path) {
      // ensure inheritance
      super(data, path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: itemStateChanged (ItemListener)
    /**
     ** Invoked when an item has been selected or deselected by the user.
     ** <p>
     ** The code written for this method performs the operations that need to
     ** occur when an item is selected (or deselected).
     **
     ** @param  event            a {@link ItemEvent} object describing the
     **                          event source and what has changed.
     **                          <br>
     **                          Allowed object is {@link ActionEvent}.
     */
    @Override
    public void itemStateChanged(final ItemEvent event) {
      apply((event.getStateChange() == ItemEvent.SELECTED));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Format
  // ~~~~~ ~~~~~~
  /**
   ** An {@link ActionListener} capturing changes on the file format.
   */
  static class Format extends    Listener
                      implements ActionListener {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Format</code> that maintains <code>contextKey</code>
     ** in the {@link TraversableContext} of the panel associated with.
     **
     ** @param  data             the {@link Namespace} to observe and commit.
     **                          <br>
     **                          Allowed object is {@link Namespace}.
     ** @param  path             the identifier of a {@link HashStructure} that
     **                          is part of the {@link TraversableContext} and
     **                          belongs to <code>namespace</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Format(final Namespace data, final String path) {
      // ensure inheritance
      super(data, path);
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
      apply(event.getActionCommand());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContextFile</code> panel that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected DirectoryContextFile() {
    // ensure inheritance
    super();

    // the components to specify which file format is in use
    this.xml2 = new JRadioButton();
    this.xml2.setMnemonic(KeyEvent.VK_2);
    this.xml2.setActionCommand(LDAPFile.Format.DSML2.type);

    this.xml1 = new JRadioButton();
    this.xml1.setMnemonic(KeyEvent.VK_1);
    this.xml1.setActionCommand(LDAPFile.Format.DSML1.type);

    this.ldif = new JRadioButton();
    this.ldif.setMnemonic(KeyEvent.VK_L);
    this.ldif.setActionCommand(LDAPFile.Format.LDIF.type);

    this.json = new JRadioButton();
    this.json.setMnemonic(KeyEvent.VK_J);
    this.json.setActionCommand(LDAPFile.Format.JSON.type);

    // group the radio buttons
    this.format = new ButtonGroup();
    this.format.add(this.xml2);
    this.format.add(this.xml1);
    this.format.add(this.ldif);
    this.format.add(this.json);
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

    this.entry = this.context.entry();

    final String value = (String)context.get(FMT);
    this.xml2.setSelected(value.equals(this.xml2.getActionCommand()));
    this.xml1.setSelected(value.equals(this.xml1.getActionCommand()));
    this.ldif.setSelected(value.equals(this.ldif.getActionCommand()));
    this.json.setSelected(value.equals(this.json.getActionCommand()));

    // arm the format items to track changes
    // arm the format items to track changes
    final ActionListener format = new Format(context, FMT);
    this.xml2.addActionListener(format);
    this.xml1.addActionListener(format);
    this.ldif.addActionListener(format);
    this.json.addActionListener(format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readable
  /**
   ** Verifies that the specified abstract path <code>file</code> is readable
   ** by the process.
   **
   ** @param  event              a {@link PropertyChangeEvent} object
   **                            describing the event source and the property
   **                            that has changed.
   **                            <br>
   **                            Allowed object is {@link PropertyChangeEvent}.
   ** @param  title              the title text of the message popup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  file               the abstract path to test.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws PropertyVetoException if abstract path <code>file</code> does
   **                               not meet the requirements.
   */
  protected void readable(final PropertyChangeEvent event, final String title, final File file)
    throws PropertyVetoException {

    // verify that the parent file is accessible
    final File path = file.getParentFile();
    if (!path.isDirectory() || !path.canRead()) {
      MessageDialog.error(this, Bundle.format(Bundle.FILE_NOTREADABLE, path.getAbsolutePath()), title, null);
      // the exceptions is swallowed at the dialog so no harm to make it
      // meaningfull
      throw new PropertyVetoException("", event);
    }

    // verify that the file itself is accessible
    if (!file.exists()) {
      MessageDialog.error(this, Bundle.format(Bundle.FILE_MISSING, file.getAbsolutePath()), title, null);
      // the exceptions is swallowed at the dialog so no harm to make it
      // meaningfull
      throw new PropertyVetoException("", event);
    }
    else {
      if (!file.canRead()) {
        MessageDialog.error(this, Bundle.format(Bundle.FILE_NOTREADABLE, file.getAbsolutePath()), title, null);
        // the exceptions is swallowed at the dialog so no harm to make it
        // meaningfull
        throw new PropertyVetoException("", event);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writebale
  /**
   ** Verifies that the specified abstract path <code>file</code> is writable
   ** by the process.
   **
   ** @param  event              a {@link PropertyChangeEvent} object
   **                            describing the event source and the property
   **                            that has changed.
   **                            <br>
   **                            Allowed object is {@link PropertyChangeEvent}.
   ** @param  title              the title text of the message popup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  file               the abstract path to test.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws PropertyVetoException if abstract path <code>file</code> does
   **                               not meet the requirements.
   */
  protected void writable(final PropertyChangeEvent event, final String title, final File file)
    throws PropertyVetoException {

    // verify that the parent file is accessible
    final File path = file.getParentFile();
    if (!path.isDirectory() || !path.canWrite()) {
      MessageDialog.error(this, Bundle.format(Bundle.FILE_NOTWRITABLE, path.getAbsolutePath()), title, null);
      // the exceptions is swallowed at the dialog so no harm to make it
      // meaningfull
      throw new PropertyVetoException("", event);
    }
     // verify that the file itself is accessible
    if (file.exists()) {
      if (!MessageDialog.confirm(this, Bundle.format(Bundle.FILE_OVERRIDE, file.getName(), file.getParent()), title, null)) {
        // the exceptions is swallowed at the dialog so no harm to make it
        // meaningfull
        throw new PropertyVetoException("", event);
      }
    }
    else {
      try {
        if (!file.createNewFile()) {
          MessageDialog.error(this, Bundle.format(Bundle.FILE_NOTWRITABLE, file.getAbsolutePath()), title, null);
          // the exceptions is swallowed at the dialog so no harm to make it
          // meaningfull
          throw new PropertyVetoException("", event);
        }
      }
      catch (IOException e) {
        MessageDialog.error(this, e.getLocalizedMessage(), title, null);
        // the exceptions is swallowed at the dialog so no harm to make it
        // meaningfull
        throw new PropertyVetoException(e.getLocalizedMessage(), event);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooser
  /**
   ** Factory method to create a {@link URLChooser}.
   **
   ** @param  label              the label of the file filter extension.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  extension          the file extension.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link URLChooser} ready to use.
   **                            <br>
   **                            Possible object is {@link URLChooser}.
   */
  protected static URLChooser chooser(final String label, final String extension) {
    URL base = null;
    try {
      if (selected != null && selected.exists())
        base = selected.isDirectory() ? selected.toURI().toURL() : selected.getParentFile().toURI().toURL();
    }
    catch (MalformedURLException e) {
      ;
    }
    final DefaultURLFilter filter  = new DefaultURLFilter(label, extension);
    final URLChooser       chooser = DialogUtil.newURLChooser(base);
    chooser.setShowJarsAsDirs(false);
    chooser.setSelectionMode(URLChooser.SINGLE_SELECTION);
    chooser.setSelectionScope(URLChooser.FILES_ONLY);
    chooser.clearChooseableURLFilters();
    chooser.addChooseableURLFilter(filter);
    return chooser;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooseTarget
  /**
   ** Select a file that will be the target of the export process.
   **
   ** @param  title              the title of the dialog box to display.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  chooser            the {@link URLChooser} configured to select a
   **                            new or existing file.
   **                            <br>
   **                            Allowed object is {@link URLChooser}.
   ** @param  fileName           the pre-selected filename.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the response of the user applied on the control
   **                            component of the dialog.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected boolean chooseTarget(final String title, final URLChooser chooser, final String fileName) {
    chooser.setFileNameURL(fileName);
    final int response = chooser.showSaveDialog(this, title);
    if (response == URLChooser.APPROVE_OPTION)
     selected = new File(chooser.getSelectedURL().getPath());
    return (response == URLChooser.APPROVE_OPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooseSource
  /**
   ** Select a file that will be the source of the import process.
   **
   ** @param  title              the title of the dialog box to display.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  chooser            the {@link URLChooser} configured to select a
   **                            new or existing file.
   **                            <br>
   **                            Allowed object is {@link URLChooser}.
   **
   ** @return                    the response of the user applied on the control
   **                            component of the dialog.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected boolean chooseSource(final String title, final URLChooser chooser) {
    final int response = chooser.showOpenDialog(this, title);
    if (response == URLChooser.APPROVE_OPTION) {
      selected = new File(chooser.getSelectedURL().getPath());
    }
    return (response == URLChooser.APPROVE_OPTION);
  }
}