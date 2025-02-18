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

    File        :   FileEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FileEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.io.File;

import java.net.URL;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLTextField;

////////////////////////////////////////////////////////////////////////////////
// class FileEditor
// ~~~~~ ~~~~~~~~~~
/**
 ** A concret implementation of en editor that handles <code>File</code>
 ** and {@link File} properties.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class FileEditor extends URLTextField {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7917944183093118545")
  private static final long  serialVersionUID = 332676060571172965L;

  //////////////////////////////////////////////////////////////////////////////
  // static instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean            dirty;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor

  /**
   ** Constructs an empty <code>FileEditor</code> that is configured to
   ** handle a directory.
   */
  public FileEditor() {
    // ensure inheritance
    super();

    // initialize instance
    addMarkDirtyListener();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>FileEditor</code> whose displayed value
   ** reflects the specified {@link File}, which is interpreted as representing
   ** a directory.
   ** <p>
   ** If the {@link File} is <code>null</code>, then the text field will be
   ** initially empty. If the {@link File} that the text field displays is for a
   ** file, you must use the {@link #FileEditor(File, boolean)} constructor
   ** instead.
   **
   ** @param  file               the initial {@link File} to show in the text
   **                            field. This may be <code>null</code>, which
   **                            means the text field will be initially empty.
   */
  public FileEditor(final File file) {
    // ensure inheritance
    this(file, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>FileEditor</code> whose displayed value
   ** reflects the specified {@link File}, which is interpreted as representing
   ** a directory if <code>isDir</code> specifies <code>true</code>.
   ** <p>
   ** If the URL is <code>null</code>, then the text field will be initially
   ** empty. If the {@link File} that the text field displays is for a file, you
   ** must specify <code>false</code> for parameter <code>isDir</code>.
   **
   ** @param  file               the initial {@link File} to show in the text
   **                            field. This may be <code>null</code>, which
   **                            means the text field will be initially empty.
   ** @param  isDir              <code>true</code> if the URL returned by the
   **                            {@link #getURL()} method will be an {@link URL}
   **                            for a directory; otherwise, it will be an
   **                            {@link URL} for a file.
   */
  public FileEditor(final File file, final boolean isDir) {
    // ensure inheritance
    this(file, isDir, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>FileEditor</code> whose displayed value
   ** reflects the specified {@link File}, which is interpreted as representing
   ** a directory if <code>isDir</code> specifies <code>true</code>.
   ** <p>
   ** If the URL is <code>null</code>, then the text field will be initially
   ** empty. If the {@link File} that the text field displays is for a file, you
   ** must specify <code>false</code> for parameter <code>isDir</code>.
   **
   ** @param  file               the initial {@link File} to show in the text
   **                            field. This may be <code>null</code>, which
   **                            means the text field will be initially empty.
   ** @param  isDir              <code>true</code> if the URL returned by the
   **                            {@link #getURL()} method will be an {@link URL}
   **                            for a directory; otherwise, it will be an
   **                            {@link URL} for a file.
   ** @param  showJarsAsDirs     <code>true</code> the file chooser will show
   **                            jar files in the directory list.
   */
  public FileEditor(final File file, final boolean isDir, final boolean showJarsAsDirs) {
    // ensure inheritance
    super(URLFactory.newFileURL(file), isDir, showJarsAsDirs);

    // initialize instance
    addMarkDirtyListener();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dirty
  /**
   ** Sets the state of the text field.
   **
   ** @param  state              <code>true</code> if the text field was changed
   **                            by user action, otherwise <code>false</code>.
   */
  public final void dirty(final boolean state) {
    this.dirty = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dirty
  /**
   ** Returns whether the text field was changed by user action.
   **
   ** @return                    <code>true</code> the text field was changed by
   **                            user action, otherwise <code>false</code>.
   */
  public final boolean dirty() {
    return this.dirty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setURL (overridden)
  /**
   ** Sets the {@link URL} displayed in the URLTextField.
   **
   ** @param  url              <code>true</code> if the text field was changed
   **                            by user action, otherwise <code>false</code>.
   */
  @Override
  public void setURL(final URL url) {
    // ensure inheritance
    super.setURL(url);

    // reset the change tracker
    dirty(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methdods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMarkDirtyListener
  /**
   ** Install a {@link DocumentListener} to track the changes on the text field.
   */
  private void addMarkDirtyListener() {
    getDocument().addDocumentListener(new DocumentListener() {
      public void insertUpdate(final DocumentEvent event) {
        markEditorAsDirty();
      }

      public void removeUpdate(final DocumentEvent event) {
        markEditorAsDirty();
      }

      public void changedUpdate(final DocumentEvent event) {
        markEditorAsDirty();
      }

      private void markEditorAsDirty() {
        dirty(true);
      }
    });
  }
}