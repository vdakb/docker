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

    File        :   FeaturePanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    FeaturePanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.63  2015-02-14  DSteding    Improved reusability for
                                               different content sets
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Content Set's shipped to standard
                                               PathConfiguration container
*/

package oracle.jdeveloper.workspace.iam.project.content;

import java.net.URL;

import java.awt.Insets;
import java.awt.GridBagLayout;

import javax.swing.Box;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLTextField;
import oracle.ide.net.URLFileSystem;

import oracle.ide.model.Project;
import oracle.ide.model.ContentSet;
import oracle.ide.model.ProjectContent;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;

import oracle.ide.model.panels.ProjectSettingsTraversablePanel;

import oracle.ide.controls.JWrappedLabel;

import oracle.jdeveloper.model.ContentSetPanel;

import oracle.jdeveloper.workspace.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class FeaturePanel
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Panel for editing the feature specific content set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
public abstract class FeaturePanel extends ProjectSettingsTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8730420578041996979")
  private static final long serialVersionUID = 8271836957723259341L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String[]        feature;

  /**
   ** label to be displayed an providing the usage text of the property if the
   ** default project are selected for modifying
   **/
  private final JWrappedLabel   note         = new JWrappedLabel();

  /**
   ** single value field to be displayed an providing the value of the property
   ** if the default project are selected for modifying
   **/
  private final URLTextField    field        = new URLTextField();

  private final ContentSetPanel contentPanel = new ContentSetPanel();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeaturePanel</code> with the specified content
   ** key of the {@link ContentSet} to maintain.
   **
   ** @param  featureKey         the key that specifies the {@link ContentSet}
   **                            belonging to a specific feature.
   */
  protected FeaturePanel(final String featureKey) {
    // ensure inheritance
    super();

    // initialize instance
    this.feature = new String[] {featureKey};

    // initialize UI components
    initializeLayout();
    localizeComponent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   noteComponent
  /**
   ** Returns the UI component which displays the meaning of the property.
   **
   ** @return                    the UI component which displays the meaning of
   **                            the property.
   */
  protected JWrappedLabel noteComponent() {
    return this.note;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueComponent
  /**
   ** Returns the UI component which displays the value of the property.
   **
   ** @return                    the UI component which displays the value of
   **                            the property.
   */
  protected URLTextField valueComponent() {
    return this.field;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentComponent
  /**
   ** Returns the UI component which displays the content set list.
   **
   ** @return                    the UI component which displays the value of
   **                            the property.
   */
  protected ContentSetPanel contentComponent() {
    return this.contentPanel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultDirectory
  /**
   ** Returns the name of the default content set directory.
   ** <p>
   ** The name isn't an absolute path; it's specifies the relative location
   ** regarding the project base directory.
   **
   ** @return                    the name of the default content set directory.
   */
  protected abstract String defaultDirectory();

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataKey (ProjectSettingsTraversablePanel)
  /**
   ** Returns the single key used to identify the project properties.
   **
   ** @return                    the unique key used to store and retrieve
   **                            project properties.
   */
  @Override
  public String getDataKey() {
    return this.feature[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPropertyKeys (ProjectSettingsTraversablePanel)
  /**
   ** Returns the array of property keys for this properties panel.
   ** <p>
   ** The panel contains UI for properties that span only one HashStructure.
   **
   ** @return                    a array of property keys for this panel,
   **
   ** @see    #getDataKey
   */
  @Override
  public String[] getPropertyKeys() {
    return this.feature.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onProjectPanelEntry (ProjectSettingsTraversablePanel)
  /**
   ** Invoked when the panel is about to be displayed.
   **
   **  @param  context            the shared data context
   */
  @Override
  public void onProjectPanelEntry(final TraversableContext context) {
    final boolean    isDefault  = isDefaultProject(context);
    final ContentSet contentSet = contentSet(context);
    if (isDefault) {
      final URL defaultProject = URLFileSystem.getParent(Ide.getDefaultProject().getURL());
      this.field.setBaseURL(defaultProject);

      URL contentURL = contentSet.getURLPath().getFirstEntry();
      if (contentURL == null) {
        contentURL = URLFactory.newDirURL(defaultProject, defaultDirectory());
        contentSet.getURLPath().add(contentURL);
      }
      this.field.setURL(contentURL);
      this.field.setVisible(true);
      this.contentPanel.setVisible(false);
    }
    else {
      final Project project = (Project)context.find(Project.DATA_KEY);
      this.contentPanel.loadFrom(project, contentSet);
      this.field.setVisible(false);
      this.contentPanel.setVisible(true);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExit (overridden)
  /**
   ** This method is called when the <code>Traversable</code> is being exited.
   ** <p>
   ** At this point, the <code>Traversable</code> should copy the data from its
   ** associated UI back into the data structures in the
   ** {@link TraversableContext}.
   ** <p>
   ** If the <code>Traversable</code> should not be exited because the user has
   ** entered either incomplete, invalid, or inconsistent data, then this method
   ** can throw a {@link TraversalException} to indicate to the property dialog
   ** or wizard that validation failed and that the user should not be allowed
   ** to exit the current <code>Traversable</code>. Refer to the
   ** {@link TraversalException} javadoc for details on passing the error
   ** message that should be shown to the user.
   **
   ** @param  context            the data object where changes made in the UI
   **                            should be copied so that the changes can be
   **                            accessed by other <code>Traversable</code>s.
   **
   ** @throws TraversalException if the user has entered either incomplete,
   **                            invalid, or inconsistent data.
   **                            <p>
   **                            This exception prevents the property dialog or
   **                            wizard from continuing and forces the user to
   **                            stay on the current <code>Traversable</code>
   **                            until the data entered is valid or the user
   **                            cancels. The exception class itself is capable
   **                            of carrying an error message that will be shown
   **                            to the user. Refer to its javadoc for details.
   */
  @Override
  public void onExit(final TraversableContext context)
    throws TraversalException {

    if (context.getDirection() != TraversableContext.BACKWARD_TRAVERSAL) {
      final boolean  isDefault = isDefaultProject(context);
      if (isDefault) {
        final Project    project    = (Project)context.find(Project.DATA_KEY);
        final ContentSet contentSet = contentSet(project);
        contentSet.getURLPath().setEntries(new URL[] { this.field.getURL() });
      }
      else {
        try {
          this.contentPanel.saveContentSet();
        }
        catch (ContentSetPanel.EmptyContentSetException e) {
          throw new TraversalException(Bundle.string(Bundle.CONTENT_UNITTEST_PATH_EMPTY), Bundle.string(Bundle.CONTENT_UNITTEST_PATH_ERROR));
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   initializeLayout
  /**
   ** Layout the panel.
   */
  protected void initializeLayout() {
    this.setLayout(new GridBagLayout());

    this.add(this.note,                gbc(0,  0, 1, 1, 0.0D,  0.0D, ANCHOR_W, FILL_X,  new Insets( 0, 0, 5, 5), 0, 0));
    this.add(this.field,               gbc(0,  2, 1, 1, 1.0D,  0.0D, ANCHOR_C, FILL_X,  new Insets(10, 0, 5, 5), 0, 0));
    this.add(this.contentPanel,        gbc(0,  3, 1, 1, 1.0D, 10.0D, ANCHOR_C, FILL_XY, new Insets(10, 0, 5, 5), 0, 0));
    this.add(Box.createVerticalGlue(), gbc(0, 10, 1, 1, 0.0D,  0.1D, ANCHOR_C, FILL_XY, new Insets(10, 0, 0, 0), 0, 0));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  protected abstract void localizeComponent();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentSet
  /**
   ** This gets a defensive copy of the project settings being used in the
   ** <b>Application -&gt; Project Properties -&gt; Project Source Path</b> or
   ** <b>Application -&gt; Default Project Properties -&gt; Project Source Path</b>
   ** dialog.
   ** <p>
   ** The IDE framework takes care of making this copy, and applying it back to
   ** the real preferences store, or abandoning it if the user clicks
   ** <b>Cancel</b>.
   **
   ** @param  context            the data object where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI or store the
   **                            changes made in the UI so that the changes can
   **                            be accessed by other <code>Traversable</code>s.
   **
   ** @return                    the copy of the {@link ContentSet} being used
   **                            by this <code>Traversable</code>.
   */
  protected ContentSet contentSet(final TraversableContext context) {
    return contentSet((Project)context.find(Project.DATA_KEY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentSet
  /**
   ** This gets a defensive copy of the project settings being used in the
   ** <b>Application -&gt; Project Properties -&gt; Project Source Path</b> or
   ** <b>Application -&gt; Default Project Properties -&gt; Project Source Path</b>
   ** dialog.
   ** <p>
   ** The IDE framework takes care of making this copy, and applying it back to
   ** the real preferences store, or abandoning it if the user clicks
   ** <b>Cancel</b>.
   **
   ** @param  project            the data object where the
   **                            <code>ContentSet</code> locates the data that
   **                            it needs to populate the UI or store the
   **                            changes made in the UI so that the changes can
   **                            be accessed by other <code>Traversable</code>s.
   **
   ** @return                    the copy of the {@link ContentSet} being used
   **                            by this <code>Traversable</code>.
   */
  protected ContentSet contentSet(final Project project) {
    return ProjectContent.getInstance(project).getContentSet(getDataKey());
  }
}