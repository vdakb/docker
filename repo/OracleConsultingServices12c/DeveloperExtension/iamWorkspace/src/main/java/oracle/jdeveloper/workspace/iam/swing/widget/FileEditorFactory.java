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

    File        :   FileEditorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FileEditorFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.io.File;

import oracle.bali.inspector.PropertyEditorFactory2;

import oracle.bali.inspector.editor.EditorComponentInfo;
import oracle.bali.inspector.editor.EditorComponentFactory;

import oracle.ide.net.URLTextField;

////////////////////////////////////////////////////////////////////////////////
// class FileEditorFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class FileEditorFactory extends EditorComponentFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String IDENTIFIER = "file-chooser";

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canCreateEditorFrom (EditorComponentFactory)
  /**
   ** Determines whether the requested editor can be created by this
   ** {@link EditorComponentFactory} or not.
   ** <p>
   ** The <code>FileEditorFactory</code> is expecting that an {@link File}
   ** is passed by <code>creationInfo</code> or the identifier match
   ** {@link #IDENTIFIER} if <code>creationInfo</code> is a
   ** {@link EditorComponentInfo}.
   **
   ** @param  creationInfo       the object instance describing the request to
   **                            create an inline editor for a property grid.
   **
   ** @return                    <code>true</code> if one on the requirements
   **                            described above are satisfied,
   **                            <code>false</code> otherwiese.
   */
  @Override
  public boolean canCreateEditorFrom(final Object creationInfo) {
    if ((creationInfo instanceof EditorComponentInfo))
      return ((EditorComponentInfo)creationInfo).getComponentIdentifier().equals(IDENTIFIER);

    return (creationInfo instanceof File);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createInlineEditor (EditorComponentFactory)
  /**
   ** Creates the inline editor {@link FileEditor}.
   **
   ** @param  propertyEditor     the {@link PropertyEditorFactory2} for backing
   **                            of the created inline editor.
   **
   ** @return                    the inline editor {@link FileEditor}.
   */
  @Override
  public URLTextField createInlineEditor(final PropertyEditorFactory2 propertyEditor) {
    final Object value  = propertyEditor.getValue();
    return canCreateEditorFrom(value) ? new FileEditor((File)value) : new FileEditor();
  }
}