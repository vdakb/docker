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

    File        :   AbstractAddin.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AbstractAddin.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam;

import java.util.Map;
import java.util.HashMap;

import java.net.URL;

import javax.ide.util.MetaResource;

import oracle.ide.Ide;
import oracle.ide.Addin;
import oracle.ide.Context;
import oracle.ide.IdeConstants;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.ide.cmd.ShutdownHook;

import oracle.ide.resource.ModelArb;

import oracle.ide.model.TechId;
import oracle.ide.model.Element;
import oracle.ide.model.Workspace;
import oracle.ide.model.Workspaces;
import oracle.ide.model.TechnologyRegistry;

import oracle.ide.controller.Command;
import oracle.ide.controller.IdeAction;
import oracle.ide.controller.Controller;

import oracle.ide.gallery.ElementInfo;
import oracle.ide.gallery.GalleryManager;

import oracle.jdeveloper.template.ProjectTemplate;
import oracle.jdeveloper.template.AbstractTemplate;
import oracle.jdeveloper.template.ApplicationTemplate;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractAddin
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Addin for application and project functionality within Oracle
 ** JDeveloper to handle Identity and Access Management artifacts.
 ** <p>
 ** The <code>AbstractAddin</code> provides the mechanism for a JDeveloper
 ** extensions to carry out programmatic initialization during the startup
 ** sequence of the JDeveloper IDE.
 ** <br>
 ** The initialize() method of this Addin is called prior to the display of the
 ** main window.
 ** <br>
 ** The registration is done in the <code>addins</code> section of the extension
 ** manifest. For more information on the extension manifest, see the
 ** documentation in jdev\doc\extension.
 ** <br>
 ** Care should be taken when implementing the initialize() method to minimize
 ** the work carried out and the classes loaded. If it is possible to defer some
 ** initialization until a later point, it will be deferred.
 ** <p>
 ** <b>Note</b>:<br>
 ** To build this library you have to make a change in the project configuraion.
 ** You have to add
 ** <pre>
 **  &lt;url path="../../../../Oracle/product/fmw/11.1.1/jdeveloper/jdev/extensions/oracle.javacore.jar" jar-entry=""/&gt;
 ** </pre>
 ** to the project library "JDeveloper Extension SDK"
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class AbstractAddin implements Addin
                                    ,          Controller
                                    ,          ShutdownHook  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The key KEY should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   ** <br>
   ** Specifically, do NOT use CoolFeaturePrefs.class.getName(). The reason is
   ** that if CoolFeaturePrefs is ever renamed or moved,
   ** CoolFeaturePrefs.class.getName() will cause the KEY String to change,
   ** which introduces a preferences migration issue (since this key is used in
   ** the persisted XML) that will require more code and testing to accomodate
   ** and open up your code to annoying little bugs.
   ** <br>
   ** Always eliminate this cause of bugs by using a hard-coded String for key
   ** KEY.
   ** <p>
   ** By convention, KEY should be the fully qualified class name of the
   ** <code>HashStructureAdapter</code>. This helps ensure against name
   ** collisions. This also makes it easier to identify what piece of code is
   ** responsible for a preference when you're looking at the XML in the
   ** product-preferences.xml file. Of course, that only works as long as the
   ** adapter class itself is never renamed or moved, so avoid renaming or
   ** moving this class once it's been released in production.
   */
  public static final String DATA_KEY = "oracle.iam";

  private static final Map<String, AbstractTemplate> template = new HashMap<String, AbstractTemplate>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractAddin</code> that use the provided string as
   ** the label string displayed in the extension preferences
   **
   ** @param  label              the human readable name of this {@link Addin}.
   */
  protected AbstractAddin(final String label) {
    // ensure inheritance
    super();

    // Register the addin in the ComponentManager
    Ide.getVersionInfo().addComponent(label, Manifest.VERSION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupApplicationTemplate
  /**
   ** Returns the {@link ApplicationTemplate} from the registry mapped for the
   ** specified id.
   **
   ** @param  id                 the id of the template to lookup
   **
   ** @return                    the {@link ApplicationTemplate} from the
   **                            registry mapped for the specified id.
   */
  public static ApplicationTemplate lookupApplicationTemplate(final String id) {
    return (ApplicationTemplate)AbstractAddin.template.get(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupProjectTemplate
  /**
   ** Returns the {@link ProjectTemplate} from the registry mapped for the
   ** specified id.
   **
   ** @param  id                 the id of the template to lookup
   **
   ** @return                    the {@link ProjectTemplate} from the
   **                            registry mapped for the specified id.
   */
  public static ProjectTemplate lookupProjectTemplate(final String id) {
    return (ProjectTemplate)AbstractAddin.template.get(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleEvent (Controller)
  /**
   ** Handle the events that this controller is asked to handle
   **
   ** @param  action             the action that is to be executed
   ** @param  context            where was the user when the action was signaled
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise
   */
  @Override
  public boolean handleEvent(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null)
      return true;

    // all commands are handled by the specific command itself
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (Controller)
  /**
   ** This method updates the enabled status of the specified action within the
   ** specified context.
   **
   ** @param  action             the action that is to be executed
   ** @param  context            where was the user when the action was signaled
   **
   ** @return                    <code>true</code> if we handled the action,
   **                            <code>false</code> otherwise
   */
  @Override
  public boolean update(final IdeAction action, final Context context) {
    // first, bounce bogus input
    if (action == null || context == null)
      return true;

    // Now check to see what the command is
    final int     command   = action.getCommandId();
    final Element selection = element(context.getSelection());
    boolean       handled   = false;
    if (command == IdeConstants.REFRESH_CMD_ID)
      if (selection instanceof Command) {
        action.setEnabled(true);
        handled = true;
      }
    return handled;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canShutdown (ShutdownHook)
  /**
   ** Called on shutdown hooks in the order they were registered prior to
   ** shutting down the IDE. If any hook returns <code>false</code>, the
   ** shutdown sequence will be aborted and no further hooks will be queried.
   **
   ** @return                    <code>true</code> if shutdown can proceed,
   **                            <code>false</code> to abort shutdown.
   */
  @Override
  public boolean canShutdown() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdown (ShutdownHook)
  /**
   ** Called on all shutdown hooks in the order they were registered when the
   ** IDE is terminating. The IDE <b>will</b> quit when this method is called,
   ** therefore it is unnecessary to perform deregistration tasks that do not
   ** have an external effect.
   ** <br>
   ** For example, it is unnecessary to deregister menu items, but it might be
   ** necessary to close a network connection or save a file.
   */
  @Override
  public void shutdown() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultWorkspaceURL
  /**
   ** Returns a {@link URL} that represents the location of the default
   ** {@link Workspace}.
   **
   ** @return                    a {@link URL} that represents the location of
   **                            the default
   */
  public static URL defaultWorkspaceURL() {
    Workspaces workspaces = Ide.getWorkspaces();

    URL workDirectory = workspaces != null ? workspaces.getWorkDirectory() : null;
    if (workDirectory == null)
      workDirectory = URLFactory.newDirURL(Ide.getWorkDirectory());

    String workspaceName = ModelArb.getString(ModelArb.DEFAULT_WORKSPACE_NAME);
    int ctr = 1;
    URL workspaceURL;
    // iteration to generate a unique file name for the application
    do {
      // generate a synthetic name that is unique in the evaluated directory
      workspaceURL = URLFactory.newURL(workDirectory, workspaceName + String.valueOf(ctr++));
    } while (URLFileSystem.exists(workspaceURL));

    return workspaceURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** Returns the first element form the specified array of
   ** <code>Element</code>s.
   **
   ** @param  selection          the array of <code>Element</code>s where the
   **                            first element should obtained from.
   **
   ** @return                    the first element obtained from the specified
   **                            array of <code>Element</code>s.
   */
  public static Element element(final Element[] selection) {
    return selection != null && selection.length == 1 ? selection[0] : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scpServerTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to a SCP Server.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to a SCP
   **                            Server.
   */
  public static TechId scpServerTechnology() {
    TechnologyRegistry registry = TechnologyRegistry.getInstance();
    return registry.getTechId(Manifest.SCP_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jeeServerTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to a JEE Server.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to a JEE
   **                            Server.
   */
  public static TechId jeeServerTechnology() {
    TechnologyRegistry registry = TechnologyRegistry.getInstance();
    return registry.getTechId(Manifest.JEE_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mdsServerTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to a MDS Server.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to a MDS
   **                            Server.
   */
  public static TechId mdsServerTechnology() {
    TechnologyRegistry registry = TechnologyRegistry.getInstance();
    return registry.getTechId(Manifest.MDS_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   odsServerTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to a ODS Server.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to a ODS
   **                            Server.
   */
  public static TechId odsServerTechnology() {
    TechnologyRegistry registry = TechnologyRegistry.getInstance();
    return registry.getTechId(Manifest.ODS_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerTemplate
  /**
   ** Register the template and creates a gallery element specific for Oracle
   ** Identity and Access Management development into the {@link GalleryManager}.
   **
   ** @param  template           the {@link AbstractTemplate} registered and the
   **                            gallery item has to be created for.
   **                            Each element of the array represents a folder.
   ** @param  rule               the optional rule id that evaluates the state
   **                            (enabled/disabled) of the galleray item to
   **                            register.
   ** @param  wizard             the {@link AbstractWizard} class to guide the
   **                            user through the creation process the gallery
   **                            item covers.
   */
  protected void registerTemplate(final AbstractTemplate template, final String rule, final Class<? extends AbstractWizard> wizard) {
    registerTemplate(template, new String[] { Manifest.FEATURE }, rule, wizard);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerTemplate
  /**
   ** Register the template and creates a gallery element specific for Oracle
   ** Identity and Access Management development into the {@link GalleryManager}.
   **
   ** @param  template           the {@link AbstractTemplate} registered and the
   **                            gallery item has to be created for.
   ** @param  folderPath         the path to the gallery folder containing the
   **                            gallery element, which may not be
   **                            <code>null</code>.
   **                            Each element of the array represents a folder.
   ** @param  rule               the optional rule id that evaluates the state
   **                            (enabled/disabled) of the galleray item to
   **                            register.
   ** @param  wizard             the {@link AbstractWizard} class to guide the
   **                            user through the creation process the gallery
   **                            item covers.
   */
  protected void registerTemplate(final AbstractTemplate template, final String[] folderPath, final String rule, final Class<? extends AbstractWizard> wizard) {
    // unfortunately the TemplateManager isn't able to return a template by id
    // hence we register the template on ower own to shorten the access to the
    // template picked up by the wizard
    synchronized (AbstractAddin.template) {
      AbstractAddin.template.put(template.getTemplateId(), template);
    }
    // create a metaresource to fetch the icon resource displayed in the gallery
    // next to the descriptive text
    final MetaResource icon = new MetaResource(wizard.getClassLoader(), template.getIconFile());
    // create a generic gallery elements
    // the elment will be displayed in the folders represented by folderPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific ApplicationWizard class will guide the user through the
    // creation of the artifacts.
    final ElementInfo  info = new ElementInfo(folderPath, wizard.getName(), wizard.getClassLoader(), template.getName(), template.getDescription(), icon, template.isUnsorted());
    if (rule != null)
      info.setRule(rule);
    // register the gallery item from the template within the IDE
    GalleryManager.getGalleryManager().registerGalleryElement(info);
  }
}