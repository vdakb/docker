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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   Provider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Provider.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.83  2019-02-14  DSteding    First release version
    12.2.1.3.42.60.94  2021-03-09  DSteding    Content Set's shipped to standard
                                               PathConfiguration container
*/

package oracle.jdeveloper.workspace.iam.project.content.site;

import java.net.URL;

import oracle.javatools.data.HashStructure;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.model.Project;
import oracle.ide.model.Locatable;
import oracle.ide.model.ContentSet;

import oracle.ide.net.URLPath;
import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.ide.panels.Navigable;

import oracle.ide.util.PatternFilters;

import oracle.jdeveloper.workspace.iam.Bundle;

import oracle.jdeveloper.workspace.iam.project.content.FeatureProvider;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~
/**
 ** Callback class used to configure a {@link Project} with a {@link ContentSet}
 ** belonging to DocBook build processes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.83
 */
public class Provider extends FeatureProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> with the specified content key of the
   ** {@link ContentSet} to maintain.
   ** <br>
   ** Initialize this {@link FeatureProvider} with the key of the content set
   ** within the project file and short label used to identify the content set
   ** to the user.
   **
   ** @param  featureKey         the key that specifies the {@link ContentSet}
   **                            belonging to a specific feature.
   */
  public Provider(final String featureKey) {
    // ensure inheritance
    super(featureKey, Bundle.string(Bundle.CONTENT_SITEDOC_CATEGORY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initContentSet (ContentSetProvider)
  /**
   ** This method is called when a project is created or when a project is
   ** opened and is missing this provider's {@link ContentSet}. This method
   ** gives the {@link FeatureProvider} an opportunity to make sure that
   ** its {@link ContentSet} and other project properties are properly
   ** initialized.
   **
   ** @param  contentSet         the newly created {@link ContentSet} which
   **                            needs to be initialized.
   ** @param  directory          the directory containing the project being
   **                            modified. This is provided as a convenience,
   **                            since most {@link FeatureProvider}s derive
   **                            default paths based on the project directory.
   ** @param  context            the context in which the project creation is
   **                            occurring. Calling context.getProject() will
   **                            retrieve the Project instance being initialized
   **                            with the {@link ContentSet}.
   */
  @Override
  public final void initContentSet(final ContentSet contentSet, final URL directory, final Context context) {
    if (contentSet.getAllRootDirs().size() == 0)
      contentSet.getURLPath().add(defaultPath(context.getProject()));

    final PatternFilters filters = contentSet.getPatternFilters();
    if (!filters.containsFilters())
      filters.addInclude("**");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNavigable (ContentSetProvider)
  /**
   ** Returns a {@link Navigable} that represents the UI for this provider's
   ** {@link ContentSet} in the Project Properties dialog.
   **
   ** @return                    a {@link Navigable} to plug-in in the Project
   **                            Properties dialog.
   */
  @Override
  public final Navigable getNavigable() {
    return new Navigable(Bundle.string(Bundle.CONTENT_SITEDOC_CATEGORY), Panel.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultPath
  /**
   ** Given a project this creates a default  path relative the project's
   ** base URL and returns the URL of the new directory.
   **
   ** @param  project            the <code>Locatable</code> project were the
   **                            default path will be obtained from.
   **
   ** @return                    the default  path for the given
   **                            project.
   */
  public static URL defaultPath(final Locatable project) {
    final URL baseURL = project.getURL();
    return (baseURL == null) ? null : URLFactory.newDirURL(URLFileSystem.getParent(baseURL), defaultPath());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultPath
  /**
   ** Returns the default  path settings for the IDE.
   ** <br>
   ** "" by default.
   **
   ** @return                    the default  path settings for the IDE.
   */
  public static String defaultPath() {
    final Project  project  = Ide.getDefaultProject();
    final URL      baseURL  = project.getURL();
    final Settings settings = Provider.settings(project);
    if (settings != null) {
      URLPath path = settings.contentSet().getURLPath();
      if (path != null) {
        URL defaultPath = path.getFirstEntry();
        if (defaultPath != null)
          return URLFileSystem.toRelativeSpec(defaultPath, baseURL);
      }
    }
    return TemplateProjectConfigurator.SITE_DIRECTORY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   settings
  /**
   ** Given a project, returns the  content set settings stored for that
   ** project.
   ** <br>
   ** Returns <code>null</code> if the project doesn't already have any
   ** content set settings.
   ** <br>
   ** Will return the user specific settings if they exist and are valid,
   ** otherwise returns the shared settings.
   **
   ** @param  project            the <code>Project</code> were the settings
   **                            should obtained from.
   **
   ** @return                    the settings for the given project or
   **                            <code>null</code> if the given project don't
   **                            have any  content set settings.
   */
  protected static Settings settings(final Project project) {
    return settings(project.getProperties());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   settings
  /**
   ** Given a <code>HashStructure</code> object, returns the content set
   ** settings.
   ** <br>
   ** Returns <code>null</code> if the <code>HashStructure</code> contains no
   ** content set settings.
   **
   ** @param  properties         the <code>HashStructure</code> were the
   **                            settings should obtained from.
   **
   ** @return                    the settings for the given
   **                            <code>HashStructure</code> or <code>null</code>
   **                            if the given <code>HashStructure</code>
   **                            contains no  content set settings.
   */
  protected static Settings settings(final HashStructure properties) {
    HashStructure data = null;
    if (properties.containsKey(Settings.DATA))
      data = properties.getHashStructure(Settings.DATA);

    return data == null ? null : new Settings(data);
  }
}