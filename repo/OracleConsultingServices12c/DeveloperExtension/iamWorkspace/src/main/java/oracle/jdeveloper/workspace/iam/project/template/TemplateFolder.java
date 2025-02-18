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

    File        :   TemplateFolder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateFolder.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descroptor files
                                               itself in the previewer.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

import javax.swing.Icon;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNodeData;

////////////////////////////////////////////////////////////////////////////////
// class TemplateFolder
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Utility used to maintain our custom ANT build file hierarchy.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TemplateFolder extends OptionTreeNodeData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5204517634077530206")
  private static final long              serialVersionUID = 6368972567098286383L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient String               id;
  private transient File                 folder;

  private transient List<TemplateFolder> hierarchy        = new ArrayList<TemplateFolder>();
  private transient List<TemplateData>   dependant        = new ArrayList<TemplateData>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TemplateFolder</code> with the specified abstract
   ** pathname of the folder.
   **
   ** @param  folder             the {@link File} path of the folder.
   */
  public TemplateFolder(final File folder) {
    // ensure inheritance
    super(folder.getPath());

    // initialize instance
    this.id     = folder.getPath();
    this.folder = folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the file to build.
   **
   ** @return                    the identifier of the file to build.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   folder
  /**
   ** Sets the {@link File} to the folder where the build file should be exist
   ** and/or will be created
   **
   ** @param  folder             the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   */
  public final void folder(final File folder) {
    this.folder = folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   folder
  /**
   ** Returns the {@link File} to the folder where the build file should be
   ** exist and/or will be created
   **
   ** @return                    the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   */
  public final File folder() {
    return this.folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hierarchy
  /**
   ** Returns the hierarchy nodes of the ANT build file.
   **
   ** @return                    the dependant nodes of the ANT build file.
   */
  public final List<TemplateFolder> hierarchy() {
    return this.hierarchy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependant
  /**
   ** Returns the dependant nodes of the ANT build file.
   **
   ** @return                    the dependant nodes of the ANT build file.
   */
  public final List<TemplateData> dependant() {
    return this.dependant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the icon that represents an ANT file.
   **
   ** @return                    the {@link Icon} identifiying the displayed
   **                            node as an ANT file.
   */
  public final Icon icon() {
    return TemplateBundle.icon(TemplateBundle.NODE_FOLDER_ICON);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Creates a new <code>TemplateData</code> with the specified project
   ** name and base dir and adds it as a child to this
   ** <code>TemplateData</code>.
   **
   ** @param  file               the {@link File} path of the file.
   **
   ** @return                    the created {@link TemplateFolder} for method
   **                            chaining purpose.
   */
  public TemplateFolder add(final File file) {
    return add(new TemplateFolder(file));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Creates a new <code>TemplateFolder</code> with the specified project
   ** name and base dir and adds it as a child to this
   ** <code>TemplateData</code>.
   **
   ** @param  folder             the {@link TemplateFolder} to add to the
   **                            hierarchy.
   **
   ** @return                    the given {@link TemplateFolder} for method
   **                            chaining purpose.
   */
  public TemplateFolder add(final TemplateFolder folder) {
    this.hierarchy.add(folder);
    return folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified {@link TemplateStream} as a child to this
   ** <code>TemplateFolder</code>.
   **
   ** @param  node               the {@link TemplateStream} as a child to
   **                            this <code>TemplateFolder</code>.
   **
   ** @return                    this <code>node</code> for method chaining
   **                            purpose.
   */
  public TemplateStream add(final TemplateStream node) {
    this.dependant.add(node);
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified {@link TemplateFile} as a child to this
   ** <code>TemplateFolder</code>.
   **
   ** @param  node               the {@link TemplateFile} as a child to
   **                            this <code>TemplateFolder</code>.
   **
   ** @return                    this <code>node</code> for method chaining
   **                            purpose.
   */
  public TemplateFile add(final TemplateFile node) {
    this.dependant.add(node);
    return node;
  }
}