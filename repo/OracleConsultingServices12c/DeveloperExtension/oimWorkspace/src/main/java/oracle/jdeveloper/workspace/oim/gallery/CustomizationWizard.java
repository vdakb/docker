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
    Subsystem   :   Identity Manager Facility

    File        :   CustomizationWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.37  2013-06-24  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.gallery;

import java.beans.PropertyChangeEvent;

import java.net.URL;

import oracle.ide.Context;

import oracle.ide.net.URLFactory;

import oracle.ide.model.TechId;
import oracle.ide.model.Project;
import oracle.ide.model.UpdateMessage;
import oracle.ide.model.TechnologyRegistry;
import oracle.ide.model.TechnologyScopeConfiguration;

import oracle.ide.config.Preferences;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.ApplicationTemplate;

import oracle.jdeveloper.workspace.iam.AbstractWizard;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.wizard.TemplateObjectSequence;
import oracle.jdeveloper.workspace.iam.wizard.TemplateApplicationWizard;
import oracle.jdeveloper.workspace.iam.wizard.TemplateApplicationSequence;

import oracle.jdeveloper.workspace.oim.Feature;
import oracle.jdeveloper.workspace.oim.Library;
import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.workspace.oim.preference.Store;

import oracle.jdeveloper.workspace.oim.project.Configurator;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationWizard
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity Manager Customization" gallery item.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.37
 */
public class CustomizationWizard extends AbstractWizard {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Flow
  // ~~~~~ ~~~~
  /**
   ** Implementation of the "Oracle Identity Manager Customization" dialog page
   ** flow.
   */
  private class Flow extends TemplateApplicationWizard {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: update (Observer)
    /**
     ** Notification message.
     ** <p>
     ** Subjects call this method when they notify their observers that the
     ** subjects state has changed.
     **
     ** @param observed          the subject whose state has changed.
     ** @param change            what has changed.
     */
    @Override
    public final void update(final Object observed, final UpdateMessage change) {
      // if the update message originates to the technology scope ...
      PropertyChangeEvent event = (PropertyChangeEvent)change.getProperty(TechnologyScopeConfiguration.TECHNOLOGY_SCOPE_PROPERTY);
      if (event != null)
        // ... configure the project accordingly
        Configurator.updateTechnology((Project)observed, wizardContext());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abtract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: applicationURL (TemplateApplicationWizard)
    /**
     ** Retrieves the {@link URL} of the application folder.
     **
     ** @return                    the {@link URL} of the application folder.
     */
    @Override
    protected final URL applicationURL() {
      final String defaultFolder = Store.instance(Preferences.getPreferences()).workspace();
      final URL    applicationURL = URLFactory.newUniqueURL(workingURL(), this.panel.nameGenerator());
      return URLFactory.newDirURL(applicationURL, StringUtility.empty(defaultFolder) ? Library.DEFAULT_FOLDER : defaultFolder);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createTechnologySequence (TemplateApplicationWizard)
    /**
     ** Create the application property configuration page flow.
     ** <p>
     ** This heck is necessary due to the unavailability of the application
     ** template to be extended with additional steps based on the technology
     ** scope.
     ** <p>
     ** This provides the opportunity to inject customizations in the page flow
     ** that have to proceed before the project configuration happens.
     **
     ** @param  context          the {@link TraversableContext} the page flow
     **                          will be based on.
     **
     ** @return                  the {@link TemplateObjectSequence} comprising
     **                          the wizard pages to configure the application.
     */
    @Override
    protected TemplateObjectSequence createTechnologySequence(final TraversableContext context) {
      final TechnologyRegistry registry   = TechnologyRegistry.getInstance();
      // create a proper technology scope for the application to create
      final TechId[]           technology = {
        registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.SCP_SERVER)
      , registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.JEE_SERVER)
      , registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.OIM_SERVER)
      , registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.MDS_SERVER)
      };

      return new TemplateApplicationSequence(context, technology);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functinality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   postProcessing (overridden)
    /**
     ** Called when the wizard is finished and all other processing has
     ** happened after the workspace and/or projects have been created, and the
     ** wizard pages allowed to make modifications to the generated application.
     */
    @Override
    protected void postProcessing() {
      // find the workspace by creating it or take it from the wizard context
      // workspaceContext();
      Configurator.updateTechnology(workspaceContext(), wizardContext());

      // ensure inheritance
      super.postProcessing();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CustomizationWizard</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CustomizationWizard() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAvailable (Wizard)
  /**
   ** Called when the sensitivity of the menu item that opens this wizard must
   ** be determined.
   ** <p>
   ** This method is overidden because a new Oracle JDeveloper Workspace can be
   ** created any time.
   ** <p>
   ** Since OSGi Bundle support introduced in JDeveloper the method has no
   ** importance anymore to conttrol the behavior of the gallery items this
   ** wizard belongs to. Therefor the method has to be implemented due to the
   ** class constraint where its declared abstract, but we retuning always
   ** <code>true</code>.
   **
   ** @param  context            the {@link Context} to use when invoking this
   **                            {@link AbstractWizard}.
   **
   ** @return                    always <code>true</code> due to the
   **                            modifications to control the state of the
   **                            gallery item.
   */
  @Override
  public final boolean isAvailable(final Context context) {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (Wizard)
  /**
   ** Provides the label that represents the Wizard in the Object Gallery.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Wizard and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   */
  @Override
  public String getShortLabel() {
    return Manifest.string(Manifest.CUSTOMIZATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke (Wizard)
  /**
   ** Invokes the wizard.
   **
   ** @param  context            the {@link Context} of the wizard.
   **                            All parameters required by the wizard must be
   **                            set on the context. The wizard may also return
   **                            values to the caller by setting them into the
   **                            {@link Context} for the caller to retrieve.
   **
   ** @return                    <code>true</code> if the invocation was
   **                            successful, <code>false</code> if it failed or
   **                            was canceled.
   */
  @Override
  public boolean invoke(final Context context) {
    // basic test if we are able to work
    if (!isAvailable(context))
      return true;

    final Flow dialog = new Flow();
    final ApplicationTemplate template = Feature.lookupApplicationTemplate(Manifest.CUSTOMIZATION);
    return dialog.runWizard(context, template);
  }
}