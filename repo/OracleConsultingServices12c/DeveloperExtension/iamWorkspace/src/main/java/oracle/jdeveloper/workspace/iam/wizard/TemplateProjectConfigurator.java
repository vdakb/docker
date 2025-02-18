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

    File        :   TemplateProjectConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TemplateProjectConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descroptor files
                                               itself in the previewer.
    11.1.1.3.37.60.61  2014-10-28  DSteding    ADF Modeler ContentSet
                                               configutation moved to
                                               WEB-INF/classes.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.ide.Ide;

import oracle.ide.net.URLPath;
import oracle.ide.net.URLFactory;

import oracle.ide.model.TechId;
import oracle.ide.model.Project;
import oracle.ide.model.ContentSet;
import oracle.ide.model.ResourcePaths;
import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyScopeConfiguration;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.JavaProject;
import oracle.jdeveloper.model.J2eeSettings;
import oracle.jdeveloper.model.PathsConfiguration;

import oracle.adfdtinternal.model.ide.settings.ADFMSettings;

import oracle.jdeveloper.compiler.BuildSystemConfiguration;

import oracle.jdeveloper.compiler.ant.AntConfiguration;

import oracle.jdeveloper.maven.compiler.MavenConfiguration;

import oracle.jdevimpl.compiler.CompilerOptionManager;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.project.content.build.Settings;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;

import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateObjectConfigurator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The {@link Project} configurator interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TemplateProjectConfigurator extends TemplateObjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The bin/ directory contains the result of the build (typically a JAR
   ** file), as well as all all of the intermediate files generated during the
   ** build. For example, after performing a build, the bin/ directory will
   ** contain a copy of the resource files and the compiled Java classes.
   */
  public static final String OUTPUT_DIRECTORY    = "bin";

  /**
   ** As the name indicates, src/main is the most important directory of a
   ** project. Anything that is supposed to be part of an artifact, be it a jar
   ** or war, should be present here.
   */
  public static final String MAIN_DIRECTORY      = "src/main/";
  
  /**
   ** As the name indicates, src/site site documentation created using the Maven
   ** Site Plugin
   */
  public static final String SITE_DIRECTORY      = "src/site/";

  /**
   ** The directory src/test is the place where tests of each component in the
   ** application reside.
   ** <br>
   ** Note that none of these directories or files will become part of the
   ** artifact.
   */
  public static final String TEST_DIRECTORY      = "src/test/";
  
  /**
   ** default path relative to the project base URL where build files managed
   */
  public static final String BUILD_DIRECTORY     = "ant";

  /**
   ** Each java/ sub-directory contains Java source code (*.java files) with the
   ** standard Java directory layout (that is, where the directory pathnames
   ** mirror the Java package names, with / in place of the . character). The
   ** src/main/java/ directory contains the bundle source code and the
   ** src/test/java/ directory contains the unit test source code.
   */
  public static final String SOURCE_DIRECTORY    = "java";
  
  /**
   ** The directory containing all the files associated with the binding layer,
   ** specifically adfm.xml, DataBindings.cpx and the relating PageDef.xml
   ** files.
   */
  public static final String ADFSRC_DIRECTORY    = "adf";
  
  /**
   ** configuration files and others such as i18n files, per-environment
   ** configuration files, and XML configurations
   */
  public static final String RESOURCES_DIRECTORY = "resources";

  /**
   ** the directory for source code that relates to rendering UI content
   ** including pages, page fragments, page templates, declarative components,
   ** skins, task flows, task flow templates, as well as JEE required files
   ** including faces-config.xml, trinidad-config.xml and web.xml.
   */
  public static final String CONTENT_DIRECTORY   = "static";
  public static final String WEBINF_DIRECTORY    = "WEB-INF";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the shortcut to the folder of the project to maintain */
  private final File         folder;

  private final Project      project;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TemplateProjectConfigurator</code> for the specified
   ** {@link Project}.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateObjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  public TemplateProjectConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(context);

    // initialize instance attributes
    this.project = project;
    this.folder  = new File(this.project.getBaseDirectory());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectFolder
  /**
   ** Returns the {@link File} the {@link Project} the instance is configuring
   ** is located.
   **
   ** @return                    the {@link File} the {@link Project} the
   **                            instance is configuring is located.
   */
  public final File projectFolder() {
    return this.folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   project
  /**
   ** Returns the {@link Project} the configurator handles.
   **
   ** @return                    the {@link Project} the configurator
   **                            handles.
   */
  public final Project project() {
    return this.project;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base clases
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRelated (TemplateObjectConfigurator)
  /**
   ** Returns <code>true</code> if the project node is related to Oracle Access
   ** Manager.
   **
   ** @param  technology         the {@link TechId} that must march the defined
   **                            technologies in the {@link Project}.
   **
   ** @return                    <code>true</code> if the project node
   **                            represented by this configurator is related to;
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean isRelated(final TechId technology) {
    TechnologyScope techScope = TechnologyScopeConfiguration.getInstance(this.project).getTechnologyScope();
    return (!techScope.isEmpty() && techScope.contains(technology));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateRootFolder (TemplateObjectConfigurator)
  /**
   ** Creates the starting root node of the buildfile hierarchy with all
   ** the options of each particular node. This gives the end user the ability
   ** to adopt those option especially the substitution parameter.
   **
   ** @return                    the created {@link TemplateFolder} ready for
   **                            preview.
   */
  @Override
  public TemplateFolder templateRootFolder() {
    // build the workspace root folder the file to generate will be located
    // within (remember this does not need to be the folder of the Oracle
    // JDeveloper Workspace the given project is associated with)
    final TemplateFolder   iamRoot   = new TemplateFolder(featureFolder(this.projectFolder(), 3));
    final TemplateStream   iam       = iamRoot.add(templateRootPreview(iamRoot));
    final TemplateFolder   wksRoot   = iamRoot.add(featureFolder(this.projectFolder(), 2));
    final TemplateStream   workspace = wksRoot.add(templateWorkspacePreview(wksRoot));
    // we assume the workspace template specifies the pattern #{iam.preferences}
    // to import the root preference file
    workspace.include(Manifest.IAM_PREFERENCE, iam);
    templateFeaturePreview(workspace.folder());
    return iamRoot;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenRootFolder (TemplateObjectConfigurator)
  /**
   ** Creates the starting root node of the buildfile hierarchy with all
   ** the options of each particular node. This gives the end user the ability
   ** to adopt those option especially the substitution parameter.
   **
   ** @return                    the created {@link ModelFolder} ready for
   **                            preview.
   */
  @Override
  public ModelFolder mavenRootFolder() {
    // build the workspace root folder the file to generate will be located
    // within (remember this does not need to be the folder of the Oracle
    // JDeveloper Workspace the given project is associated with)
    final ModelFolder iamRoot   = new ModelFolder(featureFolder(this.projectFolder(), 3));
    final ModelStream iam       = iamRoot.add(mavenRootPreview(iamRoot));
    final ModelFolder wksRoot   = iamRoot.add(featureFolder(this.projectFolder(), 2));
    final ModelStream workspace = wksRoot.add(mavenWorkspacePreview(wksRoot));
    mavenFeaturePreview(workspace.folder());
    return iamRoot;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pathEntry
  /**
   ** Return the relative classpath entry to the specified file.
   **
   ** @param  folder             the {@link File} pathname the entry is within.
   ** @param  name               the name of the entry.
   **
   ** @return                    the {@link URL} of the entry to add to a path.
   */
  public static URL pathEntry(final File folder, final String name) {
    final File library = new File(folder, name);
    return URLFactory.newFileURL(library);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saveProject
  /**
   ** Saves the specified {@link Project}.
   */
  public void saveProject() {
    try {
      this.project.save();
    }
    catch (IOException e) {
      ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outputDirectory
  /**
   ** Sets the directory of the wrapped {@link Project} to which generated
   ** output such as class files will be sent.
   **
   ** @param  outputDirectory    the directory to which generated output will be
   **                            sent to set.
   */
  public void outputDirectory(final URL outputDirectory) {
    JavaProject.getInstance(this.project).setOutputDirectory(outputDirectory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outputDirectory
  /**
   ** Returns the directory of the wrapped {@link Project} to which generated
   ** output such as class files will be sent.
   **
   ** @return                    the directory to which generated output will be
   **                            sent.
   */
  public URL outputDirectory() {
    return JavaProject.getInstance(this.project).getOutputDirectory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildDirectory
  /**
   ** Sets the directory of the wrapped {@link Project} where the build content
   ** is located.
   ** <p>
   ** This will create the {@link PathsConfiguration}
   ** <pre>
   **  &lt;hash n="oracle.jdeveloper.workspace.iam.project.content.build.Settings"&gt;
   **    &lt;hash n="content"&gt;
   **      &lt;list n="url-path"&gt;
   **        &lt;url path="ant/"/&gt;
   **      &lt;/list&gt;
   **      &lt;list n="pattern-filters"&gt;
   **        &lt;string v="+**"/&gt;
   **      &lt;/list&gt;
   **    &lt;/hash&gt;
   **  &lt;/hash&gt;
   ** </pre>
   **
   ** @param  directory          the directory to which generated build files
   **                            will be sent to set.
   */
  public void buildDirectory(final URLPath directory) {
    final ContentSet contentSet = oracle.jdeveloper.workspace.iam.project.content.build.Settings.instance(this.project).contentSet();
    final URLPath    path       = contentSet.getURLPath();
    path.setEntries(null);
    path.add(directory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   siteDirectory
  /**
   ** Sets the directory of the wrapped {@link Project} where the DocBook site
   ** is located.
   ** <p>
   ** This will create the site {@link Settings}
   ** <pre>
   **  &lt;hash n="oracle.jdeveloper.workspace.iam.project.content.site.Settings"&gt;
   **    &lt;hash n="content"&gt;
   **      &lt;list n="pattern-filters"&gt;
   **        &lt;string v="+**"/&gt;
   **      &lt;/list&gt;
   **      &lt;list n="url-path"&gt;
   **        &lt;url path="src/main/site/"/&gt;
   **      &lt;/list&gt;
   **    &lt;/hash&gt;
   **  &lt;/hash&gt;
   ** </pre>
   **
   ** @param  directory          the directory to which generated DocBook files
   **                            will be sent to set.
   */
  public void siteDirectory(final URLPath directory) {
    final ContentSet contentSet = oracle.jdeveloper.workspace.iam.project.content.site.Settings.instance(this.project).contentSet();
    final URLPath    path       = contentSet.getURLPath();
    path.setEntries(null);
    path.add(directory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDirectory
  /**
   ** Sets the directory of the wrapped {@link Project} where the junit content
   ** is located.
   ** <p>
   ** This will create the site {@link Settings}
   ** <pre>
   **  &lt;hash n="oracle.jdeveloper.workspace.iam.project.content.test.Settings"&gt;
   **    &lt;hash n="content"&gt;
   **      &lt;list n="pattern-filters"&gt;
   **        &lt;string v="+**"/&gt;
   **      &lt;/list&gt;
   **      &lt;list n="url-path"&gt;
   **        &lt;url path="src/test/java/"/&gt;
   **      &lt;/list&gt;
   **    &lt;/hash&gt;
   **  &lt;/hash&gt;
   ** </pre>
   **
   ** @param  directory          the directory to which generated junit files
   **                            will be sent to set.
   */
  public void testDirectory(final URLPath directory) {
    final ContentSet contentSet = oracle.jdeveloper.workspace.iam.project.content.junit.Settings.instance(this.project).contentSet();
    final URLPath    path       = contentSet.getURLPath();
    path.setEntries(null);
    path.add(directory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDirectory
  /**
   ** Returns the directory of the wrapped {@link Project} where the junit
   ** sources are located.
   **
   ** @return                    the directory where the junit sources the
   **                            wrapped {@link Project} are located.
   */
  public URLPath testDirectory() {
    return PathsConfiguration.getInstance(this.project).getProjectSourcePath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceDirectory
  /**
   ** Sets the directory of the wrapped {@link Project} where the sources are
   ** located.
   ** <p>
   ** This will create the {@link PathsConfiguration}
   ** <pre>
   **  &lt;hash n="oracle.jdeveloper.model.PathsConfiguration"&gt;
   **    &lt;hash n="javaContentSet"&gt;
   **      &lt;list n="pattern-filters"&gt;
   **        &lt;string v="+**"/&gt;
   **      &lt;/list&gt;
   **      &lt;list n="url-path"&gt;
   **        &lt;url path="src/main/java/"/&gt;
   **        &lt;url path="SCA-INF/src/main/java/"/&gt;
   **      &lt;/list&gt;
   **    &lt;/hash&gt;
   **  &lt;/hash&gt;
   ** </pre>
   **
   ** @param  directory          the directory to which generated output will be
   **                            sent to set.
   */
  public void sourceDirectory(final URLPath directory) {
    final ContentSet contentSet = PathsConfiguration.getInstance(this.project).getJavaContentSet();
    final URLPath    path       = contentSet.getURLPath();
    path.setEntries(null);
    path.add(directory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sourceDirectory
  /**
   ** Returns the directory of the wrapped {@link Project} where the sources are
   ** located.
   **
   ** @return                    the directory where the sources the wrapped
   **                            {@link Project} are located.
   */
  public URLPath sourceDirectory() {
    return PathsConfiguration.getInstance(this.project).getProjectSourcePath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceDirectory
  /**
   ** Sets the directory of the wrapped {@link Project} where the resources are
   ** located.
   ** <p>
   ** This will create the {@link ResourcePaths}
   ** <pre>
   **  &lt;hash n="oracle.ide.model.ResourcePaths"&gt;
   **    &lt;hash n="resourcesContentSet"&gt;
   **      &lt;list n="pattern-filters"&gt;
   **        &lt;string v="+**"/&gt;
   **      &lt;/list&gt;
   **      &lt;list n="url-path"&gt;
   **        &lt;url path="src/main/resources/"/&gt;
   **      &lt;/list&gt;
   **    &lt;/hash&gt;
   **  &lt;/hash&gt;
   ** </pre>
   **
   ** @param  directory          the directory to which generated output will be
   **                            sent to set.
   */
  public void resourceDirectory(final URLPath directory) {
    final ContentSet contentSet = ResourcePaths.getInstance(this.project).getResourcesContentSet();
    final URLPath    path       = contentSet.getURLPath();
    path.setEntries(null);
    path.add(directory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceDirectory
  /**
   ** Returns the directory of the wrapped {@link Project} where the sources are
   ** located.
   **
   ** @return                    the directory where the sources the wrapped
   **                            {@link Project} are located.
   */
  public URLPath resourceDirectory() {
    return ResourcePaths.getInstance(this.project).getProjectResourcesPath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureClassPath
  /**
   ** Configures the libraries the {@link Project} needs.
   */
  protected abstract void configureClassPath();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureOutputDirectory
  /**
   ** Configures the <code>Output Directory</code> accordingly to the Oracle
   ** Consulting <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureOutputDirectory() {
    final File output = new File(this.project.getBaseDirectory(), OUTPUT_DIRECTORY);
    outputDirectory(URLFactory.newDirURL(output));
    // create the defined structure in the filesystem
    output.mkdirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureResourceDirectory
  /**
   ** Configures the <code>Resource Directory</code> accordingly to the Oracle
   ** Consulting <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureResourceDirectory() {
    final File    source = new File(configureBranch(MAIN_DIRECTORY), RESOURCES_DIRECTORY);
    final URLPath path   = new URLPath();
    path.add(URLFactory.newDirURL(source));
    resourceDirectory(path);
    // create the defined structure in the filesystem
    source.mkdirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureSourceDirectory
  /**
   ** Configures the <code>Source Directory</code> accordingly to the Oracle
   ** Consulting <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureSourceDirectory() {
    final File    source = new File(configureBranch(MAIN_DIRECTORY), SOURCE_DIRECTORY);
    final URLPath path   = new URLPath();
    path.add(URLFactory.newDirURL(source));
    sourceDirectory(path);
    // create the defined structure in the filesystem
    source.mkdirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureSiteDirectory
  /**
   ** Configures the <code>Site Directory</code> accordingly to the Oracle
   ** Consulting <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureSiteDirectory() {
    final File    site = configureBranch(SITE_DIRECTORY);
    final URLPath path  = new URLPath();
    path.add(URLFactory.newDirURL(site));
    siteDirectory(path);
    // create the defined structure in the filesystem
    site.mkdirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureBuildDirectory
  /**
   ** Configures the <code>Build Directory</code> accordingly to the Oracle
   ** Consulting <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureBuildDirectory() {
    final File    build = new File(this.project.getBaseDirectory(), BUILD_DIRECTORY);
    final URLPath path  = new URLPath();
    path.add(URLFactory.newDirURL(build));
    buildDirectory(path);
    // create the defined structure in the filesystem
    build.mkdirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureTestDirectory
  /**
   ** Configures the <code>Test Directory</code> accordingly to the Oracle
   ** Consulting <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureTestDirectory() {
    final File    test = new File(configureBranch(TEST_DIRECTORY), SOURCE_DIRECTORY);
    final URLPath path = new URLPath();
    path.add(URLFactory.newDirURL(test));
    testDirectory(path);
    // create the defined structure in the filesystem
    test.mkdirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureContentDirectory
  /**
   ** Configures the <code>Web Content Directory</code> accordingly to the
   ** Oracle Consulting
   ** <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureContentDirectory() {
    final File content = new File(configureBranch(MAIN_DIRECTORY), CONTENT_DIRECTORY);
    final J2eeSettings settings = J2eeSettings.getInstance(this.project);
    settings.setHtmlRootDirectory(URLFactory.newDirURL(content));
    // create the defined structure in the filesystem
    final File webinf = new File(content, WEBINF_DIRECTORY);
    webinf.mkdirs();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureModelDirectory
  /**
   ** Configures the <code>ADF Content Directory</code> accordingly to the
   ** Oracle Consulting
   ** <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureModelDirectory() {
    final File file   = new File(configureBranch(MAIN_DIRECTORY), ADFSRC_DIRECTORY);
    final ADFMSettings adfm = ADFMSettings.getInstance(this.project);
    adfm.getContentSet().setContentFolderURL(URLFactory.newDirURL(file));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureCompiler
  /**
   ** Configures the compiler options accordingly to the Oracle Consulting
   ** <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  protected void configureCompiler() {
    // configure the compiler options inside of the IDE
    final CompilerOptionManager compiler = CompilerOptionManager.getInstance(this.project);
    // FIXME:: looks like no longe supported
//    compiler.setShowUnusedImportWarnings(true);
//    compiler.setShowPartialImportWarnings(true);
    compiler.setShowDeprecations(true);
    // FIXME:: looks like no longe supported
//    compiler.setShowSelfDeprecations(true);

    // configure the build options inside of the IDE
    final BuildSystemConfiguration buildsystem = BuildSystemConfiguration.getInstance(this.project);
    buildsystem.setCurrentWebIanaEncoding("UTF-8");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureBuildfile
  /**
   ** Configures the build file for a development project.
   */
  protected void configureBuildfile() {
    final String buildfile = buildfile();
    if (StringUtility.empty(buildfile))
      this.project.getProperties().remove(AntConfiguration.DATA_KEY);
    else {
      final File buildFile = new File(this.project.getBaseDirectory(), buildfile);
      final AntConfiguration configuration = AntConfiguration.getAntConfiguration(this.project);
      configuration.setProjectBuildfile(URLFactory.newFileURL(buildFile));
      configuration.setTargetList(null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureMavenfile
  /**
   ** Configures the build file for a development project.
   */
  protected void configureMavenfile() {
    final String mavenfile = mavenfile();
    if (StringUtility.empty(mavenfile))
      this.project.getProperties().remove(MavenConfiguration.DATA_KEY);
    else {
      // configure the Maven build path needed for development
      final URLPath  path      = Settings.instance(project).path();
      final MavenConfiguration configuration = MavenConfiguration.getInstance(project);
      configuration.setPomfile(URLFactory.newURL(path.getFirstEntry(), mavenfile));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildfile
  /**
   ** Returns the name of the build file for a development project.
   **
   ** @return                    the name of the build file for a development
   **                            project.
   */
  protected abstract String buildfile();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenfile
  /**
   ** Returns the name of the build file for a development project.
   **
   ** @return                    the name of the build file for a development
   **                            project.
   */
  protected abstract String mavenfile();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureBranch
  /**
   ** Configures the <code>Branch</code> accordingly to the Oracle Consulting
   ** <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   **
   ** @param  branch             the relative name of the directory regarding
   **                            the project base directory.
   **
   ** @return                    the abstract {@link File} path.
   */
  protected File configureBranch(final String branch) {
    return new File(this.project.getBaseDirectory(), branch);
  }
}