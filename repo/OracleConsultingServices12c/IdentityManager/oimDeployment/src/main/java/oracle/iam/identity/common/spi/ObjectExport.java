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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ObjectExport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ObjectExport.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
    0.0.0.2     2013-18-02  DSteding    Fixed DE-000066
                                        Exporting an Application Instance has to
                                        export the associated Entity
                                        Publication.
*/

package oracle.iam.identity.common.spi;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;

import javax.swing.JOptionPane;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import org.apache.tools.ant.BuildException;

import Thor.API.Operations.tcExportOperationsIntf;

import com.thortech.xl.vo.ddm.RootObject;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.deployment.type.ExportSet;

////////////////////////////////////////////////////////////////////////////////
// class ObjectExport
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Exports Identity Manager objects to file.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 ** <p>
 ** The objects to be exported can be listed in an property file if the command
 ** line interface is used in the format:
 ** <pre>
 **  [category1]=[object-names1]
 **  [category2]=[object-names2]
 ** </pre>
 ** If the Apache Ant task is used the object to be exported are specified by
 ** the nested element <code>object:category</code>. For example:
 ** <pre>
 **   &lt;object:category value="category1" name="names1"/&gt;
 **   &lt;object:category value="category2" name="names2"/&gt;
 ** </pre>
 ** <p>
 ** If the Apache Ant task is used the object to be exported are specified by
 ** the nested element <code>object:category</code>. For example:
 ** <pre>
 **   &lt;object:category value="category1" name="names1"/&gt;
 **   &lt;object:category value="category2" name="names2"/&gt;
 ** </pre>
 ** Categories for Identity Manager 11g/12c are in alphabetical order:
 ** <ol>
 **   <li>AccessPolicy
 **   <li>DataObjectDef
 **   <li>CatalogDefinition <b>new</b> since <i>11.1.2.0</i>
 **   <li>CertificationDefinition <b>new</b> since <i>11.1.2.2</i>
 **   <li>CertificationConfiguration <b>new</b> since <i>11.1.2.2</i>
 **   <li>CustomResourceBundle <b>new</b> since <i>11.1.2.0</i>
 **   <li>EmailDef
 **   <li>EntityAdapter
 **   <li>EntityPublication <b>new</b> since <i>11.1.2.0</i>
 **   <li>ErrorCode
 **   <li>eventhandlers
 **   <li>GenericConnector
 **   <li>ITResource
 **   <li>ITResourceDef
 **   <li>Jar <b>new</b> since <i>11.1.2.0</i>
 **   <li>Job
 **   <li>Lookup
 **   <li>LOCALTEMPLATE
 **   <li>NOTIFICATIONTEMPLATE
 **   <li>OESPolicy
 **   <li>Organization
 **   <li>Org Metadata <b>new</b> since <i>11.1.2.0</i>
 **   <li>PasswordPolicy
 **   <li>Plugin <b>new</b> since <i>11.1.2.0</i>
 **   <li>PrepopAdapter
 **   <li>Process
 **   <li>Process Form
 **   <li>RequestDataset
 **   <li>RequestTemplate
 **   <li>Resource
 **   <li>Resource Form
 **   <li>RiskConfiguration <b>new</b> since <i>11.1.2.2</i>
 **   <li>Role and Orgs UDF
 **   <li>Role Metadata <b>new</b> since <i>11.1.2.0</i>
 **   <li>Rule
 **   <li>scheduleTasks
 **   <li>SystemProperties
 **   <li>TaskAdapter
 **   <li>Trigger
 **   <li>User Metadata <b>new</b> since <i>11.1.2.0</i>
 **   <li>User UDF <b>deprecated</b> since <i>11.1.2.0</i>
 **   <li>UserGroup
 **   <li>WorkFlowDefinition
 ** </ol>
 ** <b>Note</b>: The names of the categories are case-sensitive.
 ** <p>
 ** Example input property file:
 ** # Exports the Generic Directory Service task adapters
 ** TaskAdapter = adpGDS*
 ** #Exports the GDS prepopulate adapters
 ** PrepopAdapter = adpGDS*
 ** <p>
 ** Wild cards ('*') in object names are accepted.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ObjectExport extends ObjectRegistry {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Set<String>              UPDATETAGS = new HashSet<String>();
  private static final Map<String, Set<String>> DEPENDENCIY = new HashMap<String, Set<String>>();

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static String exportDate;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean forceOverride = false;

  private ExportSet                    exportFile = null;
  private final Collection<File>       flattenSet = new HashSet<File>();
  private final Collection<RootObject> flattenName = new HashSet<RootObject>();
  private final Collection<ExportSet>  exportSet = new ArrayList<ExportSet>();

  private Collection<RootObject> compilation = new ArrayList<RootObject>();

  /** the business logic layer to operate on */
  private tcExportOperationsIntf facade;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    Calendar calendar = Calendar.getInstance();
    exportDate = Long.toString(calendar.getTimeInMillis());

    UPDATETAGS.add("exported-date");
    UPDATETAGS.add("AAD_UPDATE");
    UPDATETAGS.add("ACP_UPDATE");
    UPDATETAGS.add("ACT_UPDATE");
    UPDATETAGS.add("ADL_UPDATE");
    UPDATETAGS.add("ADP_UPDATE");
    UPDATETAGS.add("ADJ_UPDATE");
    UPDATETAGS.add("ADM_UPDATE");
    UPDATETAGS.add("ADT_UPDATE");
    UPDATETAGS.add("ADV_UPDATE");
    UPDATETAGS.add("ARS_UPDATE");
    UPDATETAGS.add("ATP_UPDATE");
    UPDATETAGS.add("DAV_UPDATE");
    UPDATETAGS.add("DEP_UPDATE");
    UPDATETAGS.add("DOB_UPDATE");
    UPDATETAGS.add("DVT_UPDATE");
    UPDATETAGS.add("EMD_UPDATE");
    UPDATETAGS.add("ERR_UPDATE");
    UPDATETAGS.add("EVT_UPDATE");
    UPDATETAGS.add("FUG_UPDATE");
    UPDATETAGS.add("GPP_UPDATE");
    UPDATETAGS.add("LKU_UPDATE");
    UPDATETAGS.add("LKV_UPDATE");
    UPDATETAGS.add("MAV_UPDATE");
    UPDATETAGS.add("MIL_UPDATE");
    UPDATETAGS.add("MSG_UPDATE");
    UPDATETAGS.add("MST_UPDATE");
    UPDATETAGS.add("OBA_UPDATE");
    UPDATETAGS.add("OBJ_UPDATE");
    UPDATETAGS.add("ODF_UPDATE");
    UPDATETAGS.add("ORF_UPDATE");
    UPDATETAGS.add("ORR_UPDATE");
    UPDATETAGS.add("OST_UPDATE");
    UPDATETAGS.add("OUG_UPDATE");
    UPDATETAGS.add("RAV_UPDATE");
    UPDATETAGS.add("POF_UPDATE");
    UPDATETAGS.add("POG_UPDATE");
    UPDATETAGS.add("POL_UPDATE");
    UPDATETAGS.add("POP_UPDATE");
    UPDATETAGS.add("PKG_UPDATE");
    UPDATETAGS.add("PRF_UPDATE");
    UPDATETAGS.add("PUG_UPDATE");
    UPDATETAGS.add("PTY_UPDATE");
    UPDATETAGS.add("RML_UPDATE");
    UPDATETAGS.add("RPG_UPDATE");
    UPDATETAGS.add("RRE_UPDATE");
    UPDATETAGS.add("RRL_UPDATE");
    UPDATETAGS.add("RRT_UPDATE");
    UPDATETAGS.add("RUE_UPDATE");
    UPDATETAGS.add("RUL_UPDATE");
    UPDATETAGS.add("RSC_UPDATE");
    UPDATETAGS.add("RVM_UPDATE");
    UPDATETAGS.add("SDC_UPDATE");
    UPDATETAGS.add("SDK_UPDATE");
    UPDATETAGS.add("SDL_UPDATE");
    UPDATETAGS.add("SDP_UPDATE");
    UPDATETAGS.add("SEL_UPDATE");
    UPDATETAGS.add("SPD_UPDATE");
    UPDATETAGS.add("SRE_UPDATE");
    UPDATETAGS.add("SUG_UPDATE");
    UPDATETAGS.add("SVD_UPDATE");
    UPDATETAGS.add("SVR_UPDATE");
    UPDATETAGS.add("TOS_UPDATE");
    UPDATETAGS.add("TSA_UPDATE");
    UPDATETAGS.add("TSK_UPDATE");
    UPDATETAGS.add("UGP_UPDATE");
    UPDATETAGS.add("UNM_UPDATE");
    UPDATETAGS.add("UWP_UPDATE");

    UPDATETAGS.add("TSK_START_TIME");
    UPDATETAGS.add("TSK_NEXT_START_TIME");
    UPDATETAGS.add("TSK_LAST_START_TIME");
    UPDATETAGS.add("TSK_LAST_STOP_TIME");

    final Set<String> filter = new HashSet<String>();
    filter.add("EntityPublication");
    DEPENDENCIY.put("UserGroup", filter);
    DEPENDENCIY.put("ApplicationInstance", filter);
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectExport</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ObjectExport(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend, INDENT, INDENTNUMBER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportFile
  /**
   ** Called to inject the argument for instance attribute <code>file</code>.
   **
   ** @param  exportFile         the path of the file where the export has to be
   **                            written to.
   **
   */
  public void exportFile(final File exportFile) {
    if (this.exportFile == null)
      this.exportFile = new ExportSet();

    this.exportFile.setExportFile(exportFile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Called to inject the argument for instance attribute
   ** <code>description</code>.
   **
   ** @param  description        the description in the created deployment.
   */
  public void description(final String description) {
    if (this.exportFile == null)
      this.exportFile = new ExportSet();

    this.exportFile.setDescription(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCategory
  /**
   ** Calledto inject the argument for adding a category.
   **
   ** @param  physicalType       the name of the physical type
   **                            {@link RootObject} to add.
   ** @param  pattern            the value of the  {@link RootObject} to add.
   **                            The argument can contain wildcards
   **
   ** @throws BuildException     if the specified {@link RootObject} is
   **                            already part of this export operation.
   */
  public void addCategory(final String physicalType, final String pattern) {
    addCategory(new RootObject(physicalType, pattern));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCategory
  /**
   ** Called to inject the argument for adding a single {@link RootObject}.
   **
   ** @param  object             the deployment object to add.
   **
   ** @throws BuildException     if the specified {@link RootObject} is already
   **                            part of this export operation.
   */
  public void addCategory(final RootObject object) {

    if (this.flattenName.contains(object)) {
      final String message = FeatureResourceBundle.format(FeatureError.EXPORT_CATEGORY_ONLYONCE, object.getPhysicalType(), object.getName());
      throw new BuildException(message);
    }

    if (this.exportFile == null)
      this.exportFile = new ExportSet();

    this.exportFile.addObject(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addExportSet
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link ExportSet}.
   **
   ** @param  exportSet          the set of categories to export.
   **
   ** @throws BuildException     if the file the {@link ExportSet} or a
   **                            {@link RootObject} contained in the set is
   **                            already part of this export operation.
   */
  public void addExportSet(final ExportSet exportSet) {
    // check if we have this file already
    if (this.flattenSet.contains(exportSet.exportFile())) {
      final Object[] parameter = { exportSet.exportFile().getAbsolutePath() };
      throw new BuildException(FeatureResourceBundle.format(FeatureError.EXPORT_FILE_ONLYONCE, parameter));
    }

    for (RootObject category : exportSet.category()) {
      if (this.flattenName.contains(category)) {
        final Object[] parameter = { category.getPhysicalType(), category.getName() };
        final String   message = FeatureResourceBundle.format(FeatureError.EXPORT_CATEGORY_ONLYONCE, parameter);
        throw new BuildException(message);
      }
      this.flattenName.add(category);
    }

    this.flattenSet.add(exportSet.exportFile());
    this.exportSet.add(exportSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportSet
  /**
   ** Returns the {@link Collection} with {@link ExportSet}s used.
   **
   ** @return                    the {@link Collection} with {@link ExportSet}s
   **                            used.
   */
  public final Collection<ExportSet> exportSet() {
    return this.exportSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if ((this.exportFile == null) && (this.exportSet.size() == 0))
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_MANDATORY));

    try {
      if (this.exportFile != null)
        this.exportFile.validate();

      for (ExportSet cursor : this.exportSet)
        cursor.validate();
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Exports all object definition from Identity Manager through the discovered
   ** {@link tcExportOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void execute(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcExportOperationsIntf.class);

    try {
      // create the DocumentBuilder and Transformer instances
      createBuilder(null);
      createTransformer();

      // If we've got to this point, we know the usage must be correct.
      // Let's check whether a single target attribute has been used.
      if (this.exportFile != null)
        execute(this.exportFile);

      // whether a exportFile attribute was specified, one nested export set
      // may be specified.
      for (ExportSet cursor : this.exportSet)
        execute(cursor);
    }
    finally {
      if (this.facade != null)
        this.facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Export the object definition for the specfied {@link ExportSet}.
   **
   ** @param  export             the {@link ExportSet} to export.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void execute(final ExportSet export)
    throws ServiceException {

    createCompilation(export);
    if (this.compilation.isEmpty()) {
      error(FeatureResourceBundle.format(FeatureMessage.EXPORT_ASSEMBLY_EMPTY, export.description()));
    }
    else {
      // create the XML DOM in memory
      createDocument(export);
      // update all well known update timestamps with the current date and time
      updateTimestamps(this.document());
      // write the XML to disk
      exportDocument(export);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCompilation
  /**
   ** Export the object definition for the specfied {@link ExportSet}.
   **
   ** @param  export             the {@link ExportSet} to export.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void createCompilation(final ExportSet export)
    throws ServiceException {

    debug(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_CREATE, export.description()));
    // reset any artifacts
    this.compilation.clear();
    try {
      for (RootObject object : export.category()) {
        final String[]         parameter = { object.getName(), object.getPhysicalType(), null };
        Collection<RootObject> result = this.facade.findObjects(parameter[1], parameter[0]);
        parameter[2] = result.toString();
        if (result == null || result.isEmpty())
          error(FeatureResourceBundle.format(FeatureMessage.EXPORT_CATEGORY_SEARCH, parameter));
        else {
          warning(FeatureResourceBundle.format(FeatureMessage.EXPORT_CATEGORY_SEARCH, parameter));
          for (RootObject root : result) {
            // always add the dataobject definition to the export if a form is
            // requested to export to have also the permissions in the axport
            // as required by the style guide the data objec definition has to
            // be occur in the export file before the form itself occurs
            if (root.getPhysicalType() != null) {
              if ("Form".equals(root.getPhysicalType())) {
                Collection<RootObject> dataobject = this.facade.findObjects("DataObjectDef", "com.thortech.xl.dataobj.tc" + root.getName());
                for (Iterator<RootObject> k = dataobject.iterator(); k.hasNext();) {
                  final RootObject sibling = k.next();
                  sibling.setExportable(true);
                  // add the access definition object add frist an than the
                  // requested form it self
                  this.compilation.add(sibling);
                  this.compilation.add(root);
                }
              }
              else if (DEPENDENCIY.keySet().contains(root.getPhysicalType())) {
                // add the object definition to the compilation to make it
                // requested form it self
                this.compilation.add(root);
                createDependencies(root);
                // this leads to export of the registered dependencies like
                // Entity Publication assicoated with the root object to export
                createDependencyTree(root, DEPENDENCIY.get(root.getPhysicalType()));
              }
              else {
                // any other root object can be added as it is
                this.compilation.add(root);
                createDependencies(root);
              }
            }
          }
        }
      }
      // initiate the visitors by traversing the dependencies at first
      trace(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_SUCCESS, export.description()));
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.ASSEMBLY_ERROR, export.description()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDependencies
  /**
   ** Export the object dependencies for the specfied {@link ExportSet}.
   **
   ** @param  root               the {@link RootObject} to create the
   **                            dependencies for.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void createDependencies(final RootObject root)
    throws ServiceException {

    debug(FeatureResourceBundle.format(FeatureMessage.DEPENDENCY_CREATE, root.getName()));

    final Collection<RootObject> filter = new HashSet<RootObject>();
    filter.add(root);
    try {
      root.setParents(this.facade.retrieveDependencyTree(filter));
      final Collection<RootObject> result = this.facade.retrieveChildren(filter);
      final Iterator<RootObject>   i = result.iterator();
      RootObject                   self = null;
      do {
        self = i.next();
        if (self.equals(root)) {
          root.setChilds(self.getChilds());
          self = null;
        }
      } while (self != null);
      // initiate the visitors by traversing the dependencies at first
      trace(FeatureResourceBundle.format(FeatureMessage.DEPENDENCY_SUCCESS, root.getName()));
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.DEPENDENCY_ERROR, root.getName()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDependencyTree
  /**
   ** Export the object dependenctry for the specfied {@link ExportSet}.
   **
   ** @param  root               the {@link RootObject} to create the
   **                            dependencies for.
   ** @param  filter             the collection with names of root objects that
   **                            has to be evaluated in the dependencies.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void createDependencyTree(final RootObject root, final Set<String> filter)
    throws ServiceException {

    debug(FeatureResourceBundle.format(FeatureMessage.DEPENDENCY_CREATE, root.getName()));
    try {
      // initiate the visitors by traversing the dependencies at first
      trace(FeatureResourceBundle.format(FeatureMessage.DEPENDENCY_SUCCESS, root.getName()));
      for (RootObject child : root.getChilds()) {
        if (filter.contains(child.getPhysicalType())) {
          if (!this.compilation.contains(child)) {
            child.setExportable(true);
            // add the child object definition to the compilation
            this.compilation.add(child);
            createDependencies(child);
            createDependencyTree(child, filter);
          }
        }
      }
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.DEPENDENCY_ERROR, root.getName()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDocument
  /**
   ** Export the object definition for the specfied {@link ExportSet}.
   **
   ** @param  export             the {@link ExportSet} to export.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void createDocument(final ExportSet export)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CREATE, export.description()));
    // retrieve the complete XML
    String fragment = null;
    try {
      fragment = this.facade.getExportXML(this.compilation, export.description());
      warning(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_SUCCESS, export.description()));
    }
    catch (Exception e) {
      error(ServiceResourceBundle.format(ServiceMessage.DOCUMENT_ERROR, export.description()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }

    // strip any character from the outcome that will break the further
    // formatting
    fragment = fragment.replaceAll(">[ ]*<", "><");
    //    fragment = fragment.replaceAll("\\n","").replaceAll("\\t","");
    fragment = fragment.replaceAll("\\t", "");
    //    fragment = fragment.replaceAll(">[ ]*<", "><");

    createDocument(fragment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTimestamps
  /**
   ** Changes all occurences of the well known timstamps in the specified
   ** {@link Node} to the timestamp the generated XML file is writen to the
   ** file system.
   **
   ** @param  parent             the {@link Node} the update timestamps should
   **                            be changed to the current date and time.
   */
  private void updateTimestamps(final Node parent) {
    NamedNodeMap map = parent.getAttributes();
    if (map != null && map.getLength() > 0) {
      for (int k = 0; k < map.getLength(); k++) {
        final Node node = map.item(k);
        if (UPDATETAGS.contains(node.getNodeName()))
          node.setNodeValue(exportDate);
      }
    }

    NodeList childs = parent.getChildNodes();
    if (childs == null)
      return;

    for (int k = 0; k < childs.getLength(); k++) {
      final Node node = childs.item(k);
      if (UPDATETAGS.contains(node.getNodeName()))
        node.getFirstChild().setNodeValue(exportDate);

      updateTimestamps(node);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportDocument
  /**
   ** Writes the XML document to the local file system
   **
   ** @param  export             the {@link ExportSet} to write as an XML
   **                            document to the local file system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void exportDocument(final ExportSet export)
    throws ServiceException {

    int response = 0;
    if (!this.forceOverride && export.exportFile().exists()) {
      response = JOptionPane.showConfirmDialog
        (null
      , ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CONFIRMATION_MESSAGE, export.exportFile().getName(), export.exportFile().getParent())
      , ServiceResourceBundle.format(ServiceMessage.DOCUMENT_CONFIRMATION_TITLE,   export.description())
      , JOptionPane.YES_NO_OPTION
      , JOptionPane.QUESTION_MESSAGE
      );
    }

    if (response == 0)
      writeDocument(export.exportFile());
  }
}