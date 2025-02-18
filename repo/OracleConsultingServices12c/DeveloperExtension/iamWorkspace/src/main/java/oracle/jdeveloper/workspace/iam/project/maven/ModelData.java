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

    File        :   ModelData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ModelData.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven;

import java.io.File;
import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;

import javax.swing.Icon;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;

import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.utility.FileSystem;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNodeData;

////////////////////////////////////////////////////////////////////////////////
// abstract class ModelData
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
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
public abstract class ModelData extends OptionTreeNodeData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2312272782287362711")
  private static final long serialVersionUID = -5246901637042364168L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient String      id;
  private transient File        file;
  private transient String      template;

  private transient ModelFolder folder;

  private transient Model       model;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ModelData</code> with the specified project name
   ** and base dir.
   **
   ** @param  folder             the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   ** @param  name               the name of the file
   */
  public ModelData(final ModelFolder folder, final String name) {
    // ensure inheritance
    super(name);

    // initialize instance
    this.id     = name;
    this.folder = folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the identifier of the file to build.
   **
   ** @param  value              the identifier of the file to build.
   */
  public final void id(final String value) {
    this.id = value;
  }

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
   ** Sets the {@link ModelFolder} to the folder where the build file should
   ** be exist and/or will be created.
   **
   ** @param  folder             the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   */
  public final void folder(final ModelFolder folder) {
    this.folder = folder;
    // reset instance cache
    this.file   = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   folder
  /**
   ** Returns the {@link ModelFolder} to the folder where the build file
   ** should be exist and/or will be created.
   **
   ** @return                    the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   */
  public final ModelFolder folder() {
    return this.folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file
  /**
   ** Creates an abstract {@link File} path the {@link #folder} attribute
   ** and the {@link #id()} attribute.
   **
   ** @return                    the abstract {@link File} path created from
   **                            the {@link #folder} attribute and the
   **                            {@link #id()} attribute.
   */
  public final File file() {
    if (this.file == null)
      this.file = new File(this.folder.folder(), this.id);

    return this.file;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
  /**
   ** Sets the name of the template used to generate the Maven Project Object
   ** Model build file.
   **
   ** @param  template           the name of the template used to generate the
   **                            Maven Project Object Model build file.
   */
  public final void template(final String template) {
    this.template = template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
  /**
   ** Returns the name of the template used to generate the Maven Project Object
   ** Model build file.
   **
   ** @return                    the name of the template used to generate the
   **                            Maven Project Object Model build file.
   */
  public final String template() {
    return this.template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the icon that represents an Maven Project Object Model file.
   **
   ** @return                    the {@link Icon} identifiying the displayed
   **                            node as an Maven Project Object Model file.
   */
  public abstract Icon icon();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Tests whether the file or directory denoted by this template file exists.
   **
   ** @return                    <code>true</code> if and only if the file or
   **                            directory denoted by this template file exists;
   **                            <code>false</code> otherwise
   */
  public final boolean exists() {
    return file().exists();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absolutePath
  /**
   ** Returns the absolute pathname string of this template file.
   ** <p>
   ** If this template file is already absolute, then the pathname string is
   ** simply returned as if by the <code>{@link File#getPath}</code> method. If
   ** this template file is the empty abstract pathname then the pathname string
   ** of the current user directory, which is named by the system property
   ** <code>user.dir</code>, is returned. Otherwise this pathname is resolved in
   ** a system-dependent way. On UNIX systems, a relative pathname is made
   ** absolute by resolving it against the current user directory. On Microsoft
   ** Windows systems, a relative pathname is made absolute by resolving it
   ** against the current directory of the drive named by the pathname, if any;
   ** if not, it is resolved against the current user directory.
   **
   ** @return                    the absolute pathname string denoting the same
   **                            file or directory as this template file.
   */
  public final String absolutePath() {
    return file().getAbsolutePath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Sets the {@link Model} of the build file content.
   **
   ** @param  model              the {@link Model} of the build file content.
   */
  public final void model(final Model model) {
    this.model = model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Returns the {@link Model} of the build file content.
   **
   ** @return                    {@link Model} of the build file content.
   */
  public final Model model() {
    return this.model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Sets the {@link Model} of the build file content.
   **
   ** @param  model              the {@link Model} of the build file content.
   */
  public final void parent(final Model model) {
    this.model = model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parentReference
  /**
   ** Factory method to create a {@link Parent} form a {@link Model}
   **
   ** @param  model              the {@link Model} providing the metadata.
   ** @param  path               the abstract patch name to the parent.
   **
   ** @return                    the parent
   */
  public static Parent parentReference(final Model model, final String path) {
    final Parent parent = new Parent();
    parent.setGroupId(model.getGroupId());
    parent.setArtifactId(model.getGroupId());
    parent.setVersion(model.getVersion());
    parent.setRelativePath(path);
    return parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Reads the importable element definitions from the file.
   **
   ** @param  stream                  the {@link InputStream} to load the data.
   */
  public void read(final InputStream stream) {
    // prevent bogus input
    if (stream == null)
      return;

    try {
      this.model = readTemplate(new InputStreamReader(stream, "UTF-8"));
    }
    catch (UnsupportedEncodingException e) {
      // display an error alert about that the template could not be loaded
      // from the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_ENCODING_ERROR, e.getLocalizedMessage())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the importable element definitions to the file.
   **
   ** @param  stream                  the {@link InputStream} to write the data.
   */
  public void write(final OutputStream stream) {
    try {
      new MavenXpp3Writer().write(stream, this.model);
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
          stream.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absoluteFile
  /**
   ** Converts a path into a {@link File} object with an absolute path.
   ** If path is absolute than a {@link File}
   ** object constructed from new File(path) is returned, otherwise {@link File}
   ** object is returned from new File(folder, path) if folder is not
   ** <code>null</code>, otherwise <code>null</code> is returned
   **
   ** @param  path               the path to evaluate.
   **
   ** @return                    the absolute {@link File} path of the specified
   **                            {@link URL}.
   */
  public File absoluteFile(final String path) {
    return toFile(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absoluteFile
  /**
   ** Converts a {@link URL} into a {@link File} object with an absolute path.
   ** If path is absolute than a {@link File}
   ** object constructed from new File(path) is returned, otherwise {@link File}
   ** object is returned from new File(root, path) if root is not
   ** <code>null</code>, otherwise <code>null</code> is returned
   **
   ** @param  path               the path to evaluate.
   **
   ** @return                    the absolute {@link File} path of the specified
   **                            {@link URL}.
   */
  public File absoluteFile(final URL path) {
    return toFile(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativeFile
  /**
   ** Return the relative path of the specified file to the folder where
   ** the {@link File} path <code>this.folder.folder()</code> is located.
   **
   ** @param  path               the path to evaluate.
   **
   ** @return                    the relative distance of the specified
   **                            {@link File} path <code>file</code> to the
   **                            folder where the {@link File} path
   **                            <code>this.folder.folder()</code> is located.
   */
  public File relativeFile(final URL path) {
    return new File(relativePath(toFile(path)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativeFile
  /**
   ** Return the relative path of the specified path to the folder where
   ** the {@link File} path <code>this.folder.folder()</code> is located.
   **
   ** @param  path               the path to evaluate.
   **
   ** @return                    the relative distance of the specified
   **                            {@link File} path <code>file</code> to the
   **                            folder where the {@link File} path
   **                            <code>this.folder.folder()</code> is located.
   */
  public File relativeFile(final String path) {
    return new File(relativePath(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativePath
  /**
   ** Return the relative path of the specified file to the folder where
   ** the {@link File} path <code>this.folder.folder()</code> is located.
   **
   ** @param  path               the path to evaluate.
   **
   ** @return                    the relative distance of the specified
   **                            {@link File} path <code>file</code> to the
   **                            folder where the {@link File} path
   **                            <code>this.folder.folder()</code> is located.
   */
  public String relativePath(final String path) {
    return relativePath(new File(this.folder.folder(), path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativePath
  /**
   ** Returns the relative path of the specified {@link File} with respect to
   ** the <code>folder</code> attribute of this instance. For Example:
   ** <pre>
   **   folder     = D:/Project/IdentityManager
   **   file       = D:/Project/iam-preferences.xml
   **   distance() = ../iam-preferences.xml
   ** </pre>
   ** <p>
   ** If the superordinated <code>TemplateData</code> <code>file</code> isn't
   ** relative to the own folder of this instance the absolute path to the
   ** superordinated file will be returned instead.
   **
   ** @param  file               the {@link File} to relativize the path to the
   **                            owned folder of this instance.
   **
   ** @return                    the relative path of the subordinated
   **                            <code>TemplateData</code> <code>file</code> or
   **                            the absolute path to that file if no
   **                            relativness is possible.
   */
  protected String relativePath(final File file) {
    String distance = null;
    try {
      distance = FileSystem.relativePath(file, this.folder.folder());
    }
    catch (IOException e) {
      distance = file.toURI().getPath();
    }
    // normalize the resulting path to the common *nix format
    return FileSystem.normalize(distance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toFile
  /**
   ** Creates an abstract {@link File} path from the specified {@link URL}.
   **
   ** @param  url                the {@link URL} to convert to a abstract
   **                            {@link File} path.
   **                            Must never be <code>null</code>.
   **
   ** @return                    the abstract {@link File} path converted from
   **                            the specified {@link URL}.
   **                            May be <code>null</code> if <code>url</code> id
   **                            not formatted strictly according to to
   **                            RFC 2396 and cannot be converted to a URI.
   */
  private File toFile(final URL url) {
    if (url == null)
      return toFile(StringUtility.EMPTY);

    try {
      // this is the step that can fail, and so it should be this step that
      // should be fixed
      return new File(URLFactory.encodeURL(url).toURI());
    }
    catch (URISyntaxException e) {
      // OK if we are here, then obviously the URL did not comply with RFC 2396.
      // This can only happen if we have illegal unescaped characters. If we
      // have one unescaped character, then the only automated fix we can apply,
      // is to assume all characters are unescaped. If we want to construct a
      // URI from unescaped characters, then we have to use the component
      // constructors:
      try {
        final URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        return new File(uri);
      }
      catch (URISyntaxException e1) {
        // the URL is broken beyond automatic repair
        throw new IllegalArgumentException("broken URL: " + url);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toFile
  /**
   ** Creates an abstract {@link File} path from the specified {@link URL}.
   **
   ** @param  path               the path name to convert to a abstract
   **                            {@link File} path.
   **                            Must never be <code>null</code>.
   **
   ** @return                    the abstract {@link File} path converted from
   **                            the specified string.
   */
  private File toFile(final String path) {
    File tmp = new File(path);
    if (!tmp.isAbsolute()) {
      tmp = new File(this.folder.folder(), path);
      try {
        tmp = tmp.getCanonicalFile();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    return tmp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readTemplate
  /**
   ** Fetchs the template from the local file system and converts it to a
   ** string.
   **
   ** @param  reader             the {@link Reader} to the file that will
   **                            be used as the template for parsing.
   **
   ** @return                    the parsed template.
   */
  private Model readTemplate(final Reader reader) {
    try {
      return new MavenXpp3Reader().read(reader);
    }
    catch (IOException e) {
      // display an error alert about that the template could not be loaded
      // from the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_READING_ERROR, e.getLocalizedMessage())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
      return null;
    }
    catch (XmlPullParserException e) {
      // display an error alert about that the template could not be loaded
      // from the resources.
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.TEMPALTE_READING_ERROR, e.getLocalizedMessage())
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
      return null;
    }
  }
}