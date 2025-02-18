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

    File        :   FeatureProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    FeatureProvider.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.63  2015-02-14  DSteding    content root path added
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Content Set's shipped to standard
                                               PathConfiguration container
*/

package oracle.jdeveloper.workspace.iam.project.content;

import java.net.URL;

import oracle.ide.model.Project;
import oracle.ide.model.ContentSet;
import oracle.ide.model.ProjectContent;
import oracle.ide.model.ContentSetProvider;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Callback class used to configure a {@link Project} with a
 ** {@link ContentSet}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class FeatureProvider extends ContentSetProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeatureProvider</code> with the specified
   ** content key of the {@link ContentSet} to maintain.
   ** <br>
   ** Initialize this {@link ContentSetProvider} with the key of the content set
   ** within the project file and short label used to identify the content set
   ** to the user.
   **
   ** @param  featureKey         the key that specifies the {@link ContentSet}
   **                            belonging to a specific feature.
   ** @param  categoryLabel      the short label used to identify the content
   **                            set to the user. This is a translatable string.
   */
  protected FeatureProvider(final String featureKey, final String categoryLabel) {
    // ensure inheritance
    super(featureKey, categoryLabel);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canExtendURLPath (overridden)
  /**
   ** Returns <code>true</code> if the {@link ContentSetProvider} allows a
   ** {@link URL} to be added to the ContentSet's <code>URLPath</code> in the
   ** given {@link Project} context. Returns <code>false</code> otherwise.
   **
   ** @param  project            the {@link Project} to query.
   **
   ** @return                    <code>true</code> if the
   **                            {@link ContentSetProvider} allows a {@link URL}
   **                            to be added; <code>false</code> otherwise.
   */
  @Override
  public boolean canExtendURLPath(Project project) {
    return !project.isDefault();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canContainJavaSources (overridden)
  /**
   ** Returns <code>true</code> if the {@link ContentSetProvider} may point to
   ** Java sources. Returns <code>false</code> otherwise.
   ** <p>
   ** The default return value is <code>false</code>. When the provider returns
   ** <code>true</code>, this affects how the Java root directories for a
   ** {@link Project} are determined by the ProjectContent.getJavaRootDirs()
   ** method. Typically, when a provider returns <code>true</code> from this
   ** method, it will also return <code>true</code> from
   ** {@link #displayFoldersAsPackages()}.
   **
   **
   ** @return                    <code>true</code> if the
   **                            {@link ContentSetProvider} may point to Java
   **                            sources.
   */
  @Override
  public boolean canContainJavaSources() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayFoldersAsPackages (overridden)
  /**
   ** Returns <code>true</code> if the {@link ContentSetProvider} wants its
   ** folders to be rendered in the UI as if they are Java packages. This method
   ** only affects how folders are displayed in the UI and does not affect the
   ** Java root directories of a project in the same way that
   ** {@link #canContainJavaSources()} does.
   **
   ** @return                    <code>true</code> if the
   **                            {@link ContentSetProvider} wants its folders to
   **                            be rendered in the UI as if they are Java
   **                            packages.
   */
  @Override
  public boolean displayFoldersAsPackages() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentSet
  /**
   ** Returns the {@link ContentSet} from the specified {@link Project} this
   ** ContentSetProvider for Oracle Identity and Access Management projects
   ** belongs too.
   **
   ** @param  project            the {@link Project} providing the registered
   **                            {@link ContentSet}s.
   **
   ** @return                    the {@link ContentSet} contained in the given
   **                            {@link Project} bound at the key.
   */
  public ContentSet contentSet(final Project project) {
    return ProjectContent.getInstance(project).getContentSet(getKey());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the {@link ContentSet} this {@link ContentSetProvider} managed
   ** from the specified {@link Project}.
   **
   ** @param  project            the {@link Project} the {@link ContentSet} have
   **                            to be removed from.
   */
  public void remove(final Project project) {
    cleanup(project);
    ProjectContent.getInstance(project).getContentSetList().remove(getKey());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanup
  /**
   ** Cleanup the {@link ContentSet} this {@link ContentSetProvider} managed
   ** within the specified {@link Project project}.
   ** <b>Note:</b>
   ** Only the child {@link ContentSet}s and PattermFilter are removed. The
   ** {@link ContentSet} managed by this {@link ContentSetProvider} itself
   ** remains part of the specified {@link Project project}. If you want to
   ** drop the entire {@link ContentSet} use {@link #remove(Project)} instead.
   **
   ** @param  project            the {@link Project} the {@link ContentSet} have
   **                            to be cleaned up.
   */
  public void cleanup(final Project project) {
    final ContentSet contentSet = ProjectContent.getInstance(project).getContentSet(getKey());
    if (contentSet != null) {
      contentSet.getURLPath().setEntries(null);
      contentSet.getPatternFilters().removeFilters();
      contentSet.removeAllContentSets();
    }
  }
}