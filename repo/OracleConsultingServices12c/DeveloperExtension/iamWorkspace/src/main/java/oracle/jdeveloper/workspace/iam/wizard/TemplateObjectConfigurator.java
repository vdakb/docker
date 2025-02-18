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
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateObjectConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TemplateObjectConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descroptor files
                                               itself in the previewer.
    11.1.1.3.37.60.69  2017-31-01  DSteding    Removed spaces in directory names
                                               to avoid code generation issues.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.io.File;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;

import org.apache.maven.model.Model;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.ide.model.TechId;
import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversableContext;

import oracle.ide.config.Preferences;

import oracle.ide.util.VersionNumber;

import oracle.jdeveloper.library.JLibraryManager;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.model.ApplicationContent;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.model.IAMPreference;
import oracle.jdeveloper.workspace.iam.model.WKSPreference;

import oracle.jdeveloper.workspace.iam.parser.IAMPreferenceHandler;
import oracle.jdeveloper.workspace.iam.parser.WKSPreferenceHandler;
import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.preference.Provider;
import oracle.jdeveloper.workspace.iam.preference.FeaturePreference;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;

import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateObjectConfigurator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity and Access Management" application
 ** and project configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TemplateObjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final TraversableContext context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TemplateObjectConfigurator</code>.
   **
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  protected TemplateObjectConfigurator(final TraversableContext context) {
    // ensure inhertitance
    super();

    // initialize instance attributes
    this.context = context;

    // initialize context
    final Workspace workspace = Ide.getActiveWorkspace();
    TemplateWizardUtil.setApplicationURL(workspace.getURL(), this.context);
    TemplateWizardUtil.setApplicationPackage(workspace.getProperty(ApplicationContent.APPLICATION_PACKAGE_PREFIX_KEY), this.context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the {@link TraversableContext} the configurator handles.
   **
   ** @return                    the {@link TraversableContext} the configurator
   **                            handles.
   */
  public final TraversableContext context() {
    return this.context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toFolder
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
  public static File toFolder(final URL url) {
    if (url == null)
      return new File(StringUtility.EMPTY);

    URI uri = null;
    try {
      // this is the step that can fail, and so it should be this step that
      // should be fixed
      uri = URLFactory.encodeURL(url).toURI();
    }
    catch (URISyntaxException e) {
      // OK if we are here, then obviously the URL did not comply with RFC 2396.
      // This can only happen if we have illegal unescaped characters. If we
      // have one unescaped character, then the only automated fix we can apply,
      // is to assume all characters are unescaped. If we want to construct a
      // URI from unescaped characters, then we have to use the component
      // constructors:
      try {
        uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
      }
      catch (URISyntaxException e1) {
        // the URL is broken beyond automatic repair
        throw new IllegalArgumentException("Broken URL: " + url);
      }
    }
    final File path = new File(uri);
    return path.isDirectory() ? path : path.getParentFile();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   featureFolder
  /**
   ** Returns the folder from the local file system where the {@link File} is
   ** located within.
   ** <p>
   ** The method iterates form the spefified {@link File} upwards just to the
   ** folder that is one level beneath root. On a UNIX system the stop condition
   ** is the root file system. On a Microsoft system the stop condition is the
   ** drive.
   ** <p>
   ** For example:
   ** <br>
   ** As <code>project</code> is specified
   ** <pre>
   **   file:/D:\Project\OracleConsultingServices12c\IdentityManager\oimFoundation
   ** </pre>
   ** The returning absolute {@link File} will be
   ** <pre>
   **   file:/D:\Project\OracleConsultingServices12c\IdentityManager
   ** </pre>
   ** The desired {@link File} will only be returned if all folders are exists.
   **
   ** @param  base               the absolute {@link File} path for the
   **                            iteration.
   **                            Must not be <code>null</code>.
   ** @param  level              the number of superordinated folders can be
   **                            traversed before the resulting folder will be
   **                            return.
   **
   ** @return                    the last folder the specified {@link File} is
   **                            located within on level beneath the root file
   **                            system or <code>null</code> if the path is not
   **                            existing.
   */
  public static File featureFolder(final File base, int level) {
    File cursor = base;
    if (cursor == null || !cursor.exists())
      return null;

    File parent = cursor.getParentFile();
    if (level > 0 && parent.getParentFile() != null)
      cursor = featureFolder(parent, --level);
    return cursor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRelated
  /**
   ** Returns <code>true</code> if the project node is related to Oracle Access
   ** Manager.
   **
   ** @param  technology         the {@link TechId} that must match the defined
   **                            technologies.
   **
   ** @return                    <code>true</code> if the project node
   **                            represented by this configurator is related to;
   **                            otherwise <code>false</code>.
   */
  public abstract boolean isRelated(final TechId technology);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures a Oracle JDeveloper <code>Workspace</code> or
   ** <code>Project</code> completely.
   */
  public abstract void configure();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateRootFolder
  /**
   ** Creates the starting root node of the buildfile hierarchy with all
   ** the options of each particular node. This gives the end user the ability
   ** to adopt those option especially the substitution parameter.
   **
   ** @return                    the created {@link TemplateFolder} ready for
   **                            preview.
   */
  public abstract TemplateFolder templateRootFolder();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenRootFolder
  /**
   ** Creates the starting root node of the buildfile hierarchy with all
   ** the options of each particular node. This gives the end user the ability
   ** to adopt those option especially the substitution parameter.
   **
   ** @return                    the created {@link ModelFolder} ready for
   **                            preview.
   */
  public abstract ModelFolder mavenRootFolder();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureFeature
  /**
   ** Configures the product related feature preferences ANT build file for the
   ** project.
   **
   ** @param  context            the {@link TraversableContext} where the data
   **                            are stored as design time objects.
   */
  public abstract void configureFeature(final TraversableContext context);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateFeaturePreview
  /**
   ** Creates the product related feature target ANT build files for current
   ** project.
   **
   ** @param  workspaceFolder    the {@link TemplateFolder} where the workspace
   **                            configuration will be created and act as the
   **                            parent in the logical build file hierarchy for
   **                            all other build files created by this method
   **                            implementation.
   */
  public abstract void templateFeaturePreview(final TemplateFolder workspaceFolder);


  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenFeaturePreview
  /**
   ** Creates the product related feature target Maven Project Object Model
   ** build files for current project.
   **
   ** @param  workspaceFolder    the {@link ModelFolder} where the workspace
   **                            configuration will be created and act as the
   **                            parent in the logical build file hierarchy for
   **                            all other build files created by this method
   **                            implementation.
   */
  public abstract void mavenFeaturePreview(final ModelFolder workspaceFolder);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildJavaHome
  /**
   ** Determines the java home.
   **
   ** @return                    the {@link File} path to the java home.
   */
  protected static URL buildJavaHome() {
    return URLFileSystem.getParent(JLibraryManager.getDefaultJDK().getSDKBinDir());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateRootPreview
  /**
   ** Creates the global workspace ANT preferences build file for the
   ** project.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  protected TemplateStream templateRootPreview(final TemplateFolder folder) {
    final Provider       preference = Provider.instance(Preferences.getPreferences());
    final IAMPreference  context    = IAMPreference.instance(this.context);
    final TemplateStream item       = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));

    final VersionNumber javaVersion = JLibraryManager.getDefaultJDK().getJavaVersion();
    final File          ideHome     = new File(Ide.getOracleHomeDirectory());
    // configure the substitution parameter
    item.add(IAMPreferenceHandler.ANT_PROJECT,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),    context.project());
    item.add(IAMPreferenceHandler.JDK_HOME,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON),   item.absoluteFile(buildJavaHome()));
    item.add(IAMPreferenceHandler.JDK_VERSION,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON),   javaVersion.getPart(0) + "." + javaVersion.getPart(1));
    item.add(IAMPreferenceHandler.JCE_PASSPHRASE, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_FOLDER_ICON), context.passphrase());
    item.add(IAMPreferenceHandler.FMW_BASE,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_FOLDER_ICON), ideHome.getParentFile());
    item.add(IAMPreferenceHandler.WKS_BASE,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_FOLDER_ICON), folder.folder());
    item.add(IAMPreferenceHandler.OCS_BASE,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_FOLDER_ICON), item.absoluteFile(preference.foundation()).getParentFile());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      IAMPreferenceHandler handler = new IAMPreferenceHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(IAMPreferenceHandler.ANT_PROJECT,    handler.name());
        item.property(IAMPreferenceHandler.JDK_HOME,       item.absoluteFile(handler.propertyValue(IAMPreferenceHandler.JDK_HOME)));
        item.property(IAMPreferenceHandler.JDK_VERSION,    handler.propertyValue(IAMPreferenceHandler.JDK_VERSION));
        item.property(IAMPreferenceHandler.JCE_PASSPHRASE, handler.propertyValue(IAMPreferenceHandler.JCE_PASSPHRASE));
        item.property(IAMPreferenceHandler.FMW_BASE,       item.absoluteFile(handler.propertyValue(IAMPreferenceHandler.FMW_BASE)));
        item.property(IAMPreferenceHandler.WKS_BASE,       item.absoluteFile(handler.propertyValue(IAMPreferenceHandler.WKS_BASE)));
        item.property(IAMPreferenceHandler.OCS_BASE,       item.absoluteFile(handler.propertyValue(IAMPreferenceHandler.OCS_BASE)));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateWorkspacePreview
  /**
   ** Configures the global workspace ANT prefrence build file for the project.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  protected TemplateStream templateWorkspacePreview(final TemplateFolder folder) {
    final WKSPreference  context = WKSPreference.instance(this.context);
    final Provider       store   = Provider.instance(Preferences.getPreferences());
    final TemplateStream item    = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(WKSPreferenceHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),   context.project());
    item.add(FeaturePreference.FOUNDATION,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_FOLDER_ICON), Provider.last(store.foundation().getPath()));
    item.add(FeaturePreference.WORKSPACE,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_FOLDER_ICON), folder.folder().getName());
    item.add(Provider.HEADSTART,               TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_FOLDER_ICON), Provider.last(store.headstart().getPath()));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      WKSPreferenceHandler handler = new WKSPreferenceHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(WKSPreferenceHandler.ANT_PROJECT, handler.name());
        item.property(FeaturePreference.FOUNDATION,    item.absoluteFile(handler.propertyValue(FeaturePreference.FOUNDATION)));
        item.property(FeaturePreference.WORKSPACE,     item.absoluteFile(handler.propertyValue(FeaturePreference.WORKSPACE)));
        item.property(Provider.HEADSTART,              item.absoluteFile(handler.propertyValue(Provider.HEADSTART)));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenRootPreview
  /**
   ** Creates and writes the global workspace Maven Project Object Model build
   ** file for the project.
   **
   ** @param  folder             the {@link ModelFolder} where the file will be
   **                            created within.
   **
   ** @return                    the created {@link ModelStream} ready for
   **                            preview.
   */
  protected ModelStream mavenRootPreview(final ModelFolder folder) {
    final Provider      preference  = Provider.instance(Preferences.getPreferences());
    final VersionNumber javaVersion = JLibraryManager.getDefaultJDK().getJavaVersion();
    final File          ideHome     = new File(Ide.getOracleHomeDirectory());
    final ModelStream   item        = new ModelStream(folder, "iam-" + preference.release()  + "." + Manifest.CONFIG_FILE_TYPE);
    // configure the template to use
    item.template(ClassUtility.classNameToFile("iam-preferences", "pom"));
    item.read();

    final Model project = item.model();
    project.addProperty("maven.compiler.source",                javaVersion.getPart(0) + "." + javaVersion.getPart(1));
    project.addProperty("maven.compiler.target",                javaVersion.getPart(0) + "." + javaVersion.getPart(1));
    project.addProperty(IAMPreferenceHandler.FMW_BASE,          "/" + ideHome.getParentFile());
    project.addProperty(IAMPreferenceHandler.FMW_BASE + ".ide", "${fmw.base}/jdeveloper");
    project.addProperty(IAMPreferenceHandler.FMW_BASE + ".wls", "${fmw.base}/wlserver");
    project.addProperty(IAMPreferenceHandler.FMW_BASE + ".soa", "${fmw.base}/soa/soa");

    project.addProperty(IAMPreferenceHandler.WKS_BASE,          "/" + folder.folder());
    project.addProperty(IAMPreferenceHandler.OCS_BASE,          "/" + preference.foundation().toString());
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenWorkspacePreview
  /**
   ** Creates the workspace specific Maven Project Object Model build file for
   ** the project.
   **
   ** @param  folder             the {@link ModelFolder} where the
   **                            {@link ModelStream} file will be created
   **                            within.
   **
   ** @return                    the pre-populated Maven {@link ModelStream}.
   */
  protected ModelStream mavenWorkspacePreview(final ModelFolder folder) {
    final Provider    store = Provider.instance(Preferences.getPreferences());
    final ModelStream item  = new ModelStream(folder, "wks-" + store.release()  + "." + Manifest.CONFIG_FILE_TYPE);
    // configure the template to use
    item.template(ClassUtility.classNameToFile("wks-preferences", "pom"));
    item.read();

    final Model project = item.model();
    return item;
  }
}