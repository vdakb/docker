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

    File        :   ModelGenerator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ModelGenerator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import oracle.ide.Ide;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import org.apache.maven.model.Model;

////////////////////////////////////////////////////////////////////////////////
// class ModelGenerator
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Utility used to maintain our custom Maven Project Object Model build file
 ** hierarchy.
 ** <p>
 ** The <code>pom.xml</code> file is the core of a project's configuration in
 ** Maven. It is a single configuration file that contains the majority of
 ** information required to build a project in just the way you want.
 ** <br>
 ** The POM is huge and can be daunting in its complexity, but it is not
 ** necessary to understand all of the intricacies.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class ModelGenerator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the class loader to stream the template files. */
  private static final ClassLoader LOADER = ModelGenerator.class.getClassLoader();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ModelFolder root;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>ModelGenerator</code> the will generate the
   ** buildfile hierarchy starting at the {@link ModelFolder} <code>root</code>.
   */
  public ModelGenerator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>ModelGenerator</code> the will generate the
   ** Maven Project Object Model buil file hierarchy starting at th
   ** {@link ModelFolder} <code>root</code>.
   **
   ** @param  root               the descriptors of the Oracle JDeveloper
   **                            libraries to generate.
   */
  public ModelGenerator(final ModelFolder root) {
    // ensure inheritance
    this();

    // initalize instance attributes
    this.root = root;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preview
  /**
   ** Generates a Maven Project Object Model build file for preview.
   **
   ** @param  data               the {@link ModelStream} path to preview.
   **
   ** @return                    the string with the substituted palceholders.
   */
  public static final Model preview(final ModelStream data) {
    final ModelGenerator generator = new ModelGenerator();
    data.read(LOADER.getResourceAsStream(data.template()));
    // substitute all expressions and store it in the target file location
    return data.model();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preview
  /**
   ** Generates a Maven Project Object Model descriptor file for preview.
   **
   ** @param  data               the {@link ModelFile} path to preview.
   **
   ** @return                    the template string containing palceholders.
   */
  public static final Model preview(final ModelFile data) {
    final ModelGenerator generator = new ModelGenerator();
//    data.read(new File(data.template()));
    // substitute all expressions and store it in the target file location
    return data.model();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates the Maven Project Object Model build folder.
   **
   ** @param  folder             the Maven Project Object Model build folder
   **                            provider to generate.
   */
  protected void generate(final ModelFolder folder) {
    // iterate over all templates that depend from the folder
    for (ModelData dependant : folder.dependant())
      generate(dependant);

    // iterate over the hierarchy of folders
    for (ModelFolder hierarchy : folder.hierarchy())
      generate(hierarchy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates the Maven Project Object Model build file.
   **
   ** @param  data             the Maven Project Object Model build file data
   **                          provider to generate.
   */
  protected void generate(final ModelData data) {
    // don't touch the file if it's already exists and it has not to be
    // maintained manually
    if (data.hotspotSelected()) {
      if (data instanceof ModelStream) {
        // check the resource stream at first to avoid exceptions if the
        // template is not packaged correctly in the underlying java archive
        final InputStream stream = LOADER.getResourceAsStream(data.template());
        if (stream != null)
          generate((ModelStream)data, stream);
        else
          // display an error alert about that the template could not be loaded
          // from the resources.
          MessageDialog.error(
            Ide.getMainWindow()
          , ComponentBundle.format(ComponentBundle.TEMPALTE_INTERNAL_FAILED, data.template())
          , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
          , null
          );
      }
      else {
        try {
          // check the resource stream at first to avoid exceptions if the template
          // is not packaged correctly in the underlying java archive
          final InputStream stream = new FileInputStream(data.template());
          generate((ModelFile)data, stream);
        }
        catch (FileNotFoundException e) {
          // display an error alert about that the template could not be loaded
          // from the resources.
          MessageDialog.error(
            Ide.getMainWindow()
          , ComponentBundle.format(ComponentBundle.TEMPALTE_EXTERNAL_FAILED, data.template())
          , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
          , null
          );
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a Maven Project Object Model build file for the project.
   **
   ** @param  data               the {@link ModelStream} path to create.
   ** @param  template           the {@link InputStream} to the file that will
   **                            be used as the template for the file to
   **                            generate.
   */
  private void generate(final ModelStream data, final InputStream template) {
    data.read(template);
    write(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a Maven Project Object Model build file for the project.
   **
   ** @param  data               the {@link ModelFile} path to create.
   ** @param  template           the {@link InputStream} to the file that will
   **                            be used as the template for the file to
   **                            generate.
   */
  private void generate(final ModelFile data, final InputStream template) {
    data.read(template);
    write(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Store the template in the local file system.
   **
   ** @param  data               the {@link ModelData} to the file in the
   **                            underlying file system.
   ** @param  content            the content of the template.
   */
  private void write(final ModelData data) {
    // if the requested path to the file location does not exist yet create the
    // path
    File folder = data.folder().folder();
    if (!folder.exists())
      folder.mkdirs();

    OutputStream stream = null;
    try {
      // create a byte buffer that is huge enough to take the entire file content
      stream = new BufferedOutputStream(new FileOutputStream(data.file()));
      data.write(stream);
    }
    catch (IOException e) {
      // display an error alert about that the template could not be written
      // to the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_WRITING_ERROR, e.getLocalizedMessage())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
    }
    finally {
      if (stream != null)
        try {
          stream.flush();
          stream.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
    }
  }
}