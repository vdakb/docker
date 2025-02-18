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

    Copyright © 2021. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ModelFolder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ModelFolder.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

import javax.swing.Icon;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNodeData;

////////////////////////////////////////////////////////////////////////////////
// class ModelFolder
// ~~~~~ ~~~~~~~~~~~
/**
 ** Utility used to maintain our custom Maven Project Object Model build file
 ** hierarchy.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class ModelFolder extends OptionTreeNodeData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4913759599242369357")
  private static final long serialVersionUID    = -3744175358332267140L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final     String            id;
  private final     File              folder;
  
  private transient List<ModelFolder> hierarchy = new ArrayList<ModelFolder>();
  private transient List<ModelData>   dependant = new ArrayList<ModelData>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ModelFolder</code> with the specified abstract pathname
   ** of the folder.
   **
   ** @param  folder             the {@link File} path of the folder.
   */
  public ModelFolder(final File folder) {
    // ensure inheritance
    super(folder.getPath());

    // initialize instance
    this.id      = folder.getPath();
    this.folder  = folder;
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
   ** Returns the {@link File} to the folder where the build file should be
   ** exist and/or will be created.
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
   ** Returns the hierarchy nodes of the  Maven Project Object Model build file.
   **
   ** @return                    the dependant nodes of the  Maven Project
   **                            Object Model build file.
   */
  public final List<ModelFolder> hierarchy() {
    return this.hierarchy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependant
  /**
   ** Returns the dependant nodes of the Maven Project Object Model build file.
   **
   ** @return                    the dependant nodes of the  Maven Project
   **                            Object Model build file.
   */
  public final List<ModelData> dependant() {
    return this.dependant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the icon that represents an Maven Project Object Model file.
   **
   ** @return                    the {@link Icon} identifiying the displayed
   **                            node as an Maven Project Object Model file.
   */
  public final Icon icon() {
    return ModelBundle.icon(ModelBundle.NODE_FOLDER_ICON);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Creates a new <code>ModelData</code> with the specified project
   ** name and base dir and adds it as a child to this
   ** <code>ModelData</code>.
   **
   ** @param  file               the {@link File} path of the file.
   **
   ** @return                    the created {@link ModelFolder} for method
   **                            chaining purpose.
   */
  public ModelFolder add(final File file) {
    return add(new ModelFolder(file));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Creates a new <code>ModelFolder</code> with the specified project
   ** name and base dir and adds it as a child to this
   ** <code>ModelData</code>.
   **
   ** @param  folder             the {@link ModelFolder} to add to the
   **                            hierarchy.
   **
   ** @return                    the given {@link ModelFolder} for method
   **                            chaining purpose.
   */
  public ModelFolder add(final ModelFolder folder) {
    this.hierarchy.add(folder);
    return folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified {@link ModelStream} as a child to this
   ** <code>ModelFolder</code>.
   **
   ** @param  node               the {@link ModelStream} as a child to
   **                            this <code>ModelFolder</code>.
   **
   ** @return                    this <code>node</code> for method chaining
   **                            purpose.
   */
  public ModelStream add(final ModelStream node) {
    this.dependant.add(node);
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified {@link ModelData} as a child to this
   ** <code>ModelFolder</code>.
   **
   ** @param  node               the {@link ModelData} as a child to this
   **                            <code>ModelFolder</code>.
   **
   ** @return                    this <code>node</code> for method chaining
   **                            purpose.
   */
  public ModelData add(final ModelData node) {
    this.dependant.add(node);
    return node;
  }
}
