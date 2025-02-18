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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   SandboxGenerator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SandboxGenerator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import Thor.API.Exceptions.*;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.tcResultSet;
import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.deployment.task.ServiceProvider;
import oracle.hst.deployment.type.MDSServerContext;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLOutputNode;
import oracle.iam.identity.common.*;
import oracle.iam.identity.common.adf.marshal.*;
import oracle.iam.identity.foundation.naming.FormDefinition;
import oracle.iam.identity.foundation.naming.ResourceObject;
import oracle.iam.platform.utils.vo.OIMType;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.request.api.RequestDataSetService;
import oracle.iam.request.dataset.vo.AttributeReference;
import oracle.iam.request.dataset.vo.RequestDataSet;
import oracle.iam.request.exception.RequestServiceException;
import oracle.mds.exception.MDSException;
import oracle.mds.naming.DocumentName;
import oracle.mds.persistence.PContext;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.PManager;
import org.apache.tools.ant.BuildException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////
// class SandboxGenerator
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to generate sandbox artifacts that are
 ** published in the Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SandboxGenerator extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The attribute properties which needs to be populated from the form fields
   */
  private static final String[] PROPERTY = {
    "Required"
  , "Type"
  , "Visible Field"
  , "Auto Complete"
  , "Column Captions"
  , "Column Names"
  , "Column Widths"
  , "Lookup Column Name"
  , "Lookup Code"
  , "Lookup Query"
  , "AccountName"
  , "AccountId"
  , "AccountPassword"
  , "Account Discriminator"
  , "ITResource"
  , "Lookup Query"
  , "Entitlement"
  , "OIAParentAttribute"
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<SandboxInstance>     multiple = new ArrayList<SandboxInstance>();

  /**
   ** The business logic layer to operate on.
   */
  private tcObjectOperationsIntf         objectFacade;
  private tcFormDefinitionOperationsIntf modelFacade;
  private tcFormInstanceOperationsIntf   instanceFacade;
  private RequestDataSetService          dataSetFacade;

  /**
   ** The catalog application module.
   */
  private File                           path;

  /**
   ** The data provider.
   */
  private Catalog                        catalog;

  /**
   ** Whether the catalog data stay as fetch from the data provider or cleared
   ** afterwards.
   */
  private boolean                        reset   = false;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Visitor
  // ~~~~~ ~~~~~~~
  /**
   ** A simple visitor of files with default behavior to visit all files and to
   ** re-throw I/O errors.
   */

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SandboxGenerator</code> to initialize the instance.
   **
   ** @param  frontend           the {@link ServiceFrontend} that instantiated
   **                            this service.
   */
  public SandboxGenerator(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Sets the path to the catalog application module.
   **
   ** @param  path              the path to the catalog application module.
   */
  public final void path(final File path) {
    this.path = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Sets the flag to reset the catalog application module.
   ** <p>
   ** An initalized catalog application module looks like
   ** <pre>
   **   &lt;?xml version='1.0' encoding='ISO-8859-1'?&gt;
   **   &lt;PDefApplicationModule xmlns="http://xmlns.oracle.com/bc4j" Name="CatalogAM"/&gt;
   ** </pre>
   ** There is no view usage registered at this module.
   **
   ** @param  value            the value to set for resetting the catalog
   **                          application module.
   **                          <br>
   **                          Default: <code>false</code>.
   */
  public final void reset(final boolean value) {
    this.reset = value;
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
    if (this.multiple == null || this.multiple.size() == 0)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_MANDATORY));

    for (SandboxInstance cursor : this.multiple)
      cursor.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Marshals the <code>Sandbox</code>s to the files evaluated from the
   ** properties of the associated {@link SandboxInstance}s.
   **
   ** @param  task               the {@link FeaturePlatformTask} used to invoke
   **                            operations on the Identity Manager.
   ** @param  context            the {@link MDSServerContext} providing the
   **                            connection details to access the metadata
   **                            service.
   */
  public void execute(final FeaturePlatformTask task, final MDSServerContext context) {
    // validate the configuration before any other stuff
    if (this.path != null || context != null) {
      this.catalog = new Catalog();
    }

    if (context != null) {
      try {
        context.connect();
        final PManager  pmanager  = context.instance().getPersistenceManager();
        final PContext  pcontext  = pmanager.createPContext();
        final PDocument pdocument = pmanager.getDocument(pcontext, DocumentName.create(Catalog.PATH + Catalog.CUST));
        CatalogFactory.configure(this.catalog, pdocument.read());
      }
      catch (MDSException e) {
        throw new BuildException(e);
      }
      catch (ServiceException e) {
        throw new BuildException(e);
      }
      finally {
        try {
          context.disconnect();
        }
        catch (ServiceException e) {
          throw new BuildException(e);
        }
      }
      // persists the changes in the file system based catalog application module
      marshalModule();
    }
    // if a path is set on the task to use an offline catalog module populate
    // the state of the application module from the file system
    if (this.path != null) {
      CatalogFactory.configure(this.catalog, new File(this.path, Catalog.CUST));
      // if the flow specifies to run from scratch delete any registered view
      // usage and reset the flag itself
      if (this.reset) {
        this.catalog.reset();
        this.reset = false;
      }
    }

    this.objectFacade   = task.service(tcObjectOperationsIntf.class);
    this.modelFacade    = task.service(tcFormDefinitionOperationsIntf.class);
    this.instanceFacade = task.service(tcFormInstanceOperationsIntf.class);
    this.dataSetFacade  = task.service(RequestDataSetService.class);

    for (SandboxInstance cursor : this.multiple) {
      if (this.path == null && context == null) {
        this.catalog = new Catalog("oracle.iam.ui.catalog.model.am.CatalogAM", cursor.version().persistence, cursor.minor(), false);
      }

      marshal(cursor);
      compress(cursor);
    }
    // persists the changes in the file system based catalog application module
    if (path != null) {
      marshalModule();
    }
  }

  /**
   ** Marshals the view object usages of an application module customization.
   **
   ** @throws BuildException       if an error occurs.
   */
  private void marshalModule()
    throws BuildException {

    try {
      // verify if the path where we want to operate exists
      final Metadata.Path path = Metadata.path(this.path.getAbsolutePath());
      Catalog.marshalModule(this, path, this.catalog.version(), this.catalog);
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureError.SANDBOX_MARSHALL_FAILED, Catalog.CUST, this.path));
      if (failonerror())
        throw new BuildException(e);
      else
        fatal(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSandboxInstance
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link SandboxInstance}.
   **
   ** @param  instance         the file a sandbox will be imported from.
   **
   ** @throws BuildException     if the {@link SandboxInstance} already
   **                            contained in the collection of this import
   **                            operation.
   */
  public void addSandboxInstance(final SandboxInstance instance)
    throws BuildException {

    // check if an instance set can be added to this task or if it has to stick
    // on a single file
    if (this.multiple.contains(instance))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "instance"));

    // check if we have this file already
    this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Marshals a <code>Sandbox</code> to the file evaluated from the properties
   ** of the given {@link SandboxInstance}.
   **
   ** @param  instance           the {@link SandboxInstance} the content
   **                            descriptors has to be generated.
   */
  public void marshal(final SandboxInstance instance) {
    warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALL, instance.name(), instance.path()));
    if (!validatePath(instance))
      return;

    try {
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALL_POPULATE, instance.name()));
      populateMetadata(instance);
      populateProcessForm(instance);
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALL_POPULATED, instance.name()));
      if (!validateDataSet(instance))
        return;

      marshalSandbox(instance);
      // may be we have a sandbox skeleton mean only the sandbox metadata
      // itself needs to be marshalled
      if (instance.account != null) {
        debug(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALL_BACKEND, instance.path(), instance.name()));
        marshalDynamicModel(instance);
        marshalPersistenceModel(instance);

        debug(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALL_MODULE, instance.path(), instance.name()));
        marshalCatalog(instance);

        debug(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALL_FRONTEND, instance.path(), instance.name()));
        marshalAccountPageDef(instance);
        marshalAccountPage(instance);
        marshalEntitlementPageDef(instance);
        marshalEntitlementPage(instance);

        debug(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALL_TASKFLOW, instance.path(), instance.name()));
        marshalAccountTaskFlow(instance);
        marshalEntitlementTaskFlow(instance);
        warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_MARSHALLED, instance.name(), instance.path()));
      }
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureError.SANDBOX_MARSHALL_FAILED, instance.name(), instance.path()));
      if (failonerror())
        throw new BuildException(e);
      else
        fatal(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compress
  /**
   ** Compress a <code>Sandbox</code> to the zip file evaluated from the
   ** properties of the given {@link SandboxInstance}.
   **
   ** @param  instance           the {@link SandboxInstance} the content
   **                            descriptors has to be compressed.
   */
  public void compress(final SandboxInstance instance) {
    final Path source = Paths.get(instance.path().toURI());
    final Path target = Paths.get(source.toString() + ".zip");
    try {
      SandboxVisitor.compressPath(source, target);

      if (instance.cleanup()) {
        deletePath(source.toFile());
      }
    }
    catch (IOException e) {
      if (failonerror())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_COMPRESS, instance.name(), e.getMessage()));
      else
        error(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_COMPRESS, instance.name(), e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalSandbox
  /**
   ** Marshals the metadata descriptor of a <code>Sandbox</code> to the file
   ** evaluated from the properties of this <code>Sandbox</code> instance.
   **
   ** @param  sandbox            the {@link SandboxInstance} the
   **                            <code>Sandbox</code> metadata descriptor has to
   **                            marshalled to the approriate file.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalSandbox(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    // verify if the path where we want to operate exists
    final Metadata.Path path = Metadata.path(sandbox.path(), Sandbox.PATH);
    path.ensureExists();
    Sandbox.marshal(this, path, sandbox);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalDynamicModel
  /**
   ** Marshals the ADF entity objects and view objects of a
   ** <code>Sandbox</code> to the files evaluated from the properties of this
   ** <code>Sandbox</code> instance.
   **
   ** @param  sandbox            the {@link SandboxInstance} the
   **                            <code>Sandbox</code> entity objects and view
   **                            objects has to marshalled to the approriate
   **                            files.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalDynamicModel(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    // verify if the path where we want to operate exists
    final Metadata.Path path = Metadata.path(Metadata.path(sandbox.path(), DynamicModel.PATH), sandbox.dataSet);
    path.ensureExists();
    DynamicModel.marshal(this, path, sandbox, sandbox.metadata.others.values());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPersistenceModel
  /**
   ** Marshals the persistence definition for ADF entity objects and view
   ** objects of a <code>Sandbox</code> to the files evaluated from the
   ** properties of this <code>Sandbox</code> instance.
   **
   ** @param  sandbox            the {@link SandboxInstance} the
   **                            <code>Sandbox</code> persistence definition has
   **                            to marshalled to the approriate files.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalPersistenceModel(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    // verify if the path where we want to operate exists
    final Metadata.Path path = Metadata.path(sandbox.path(), PersistenceModel.PATH);
    path.ensureExists();
    PersistenceModel.marshal(this, path, sandbox.version, sandbox.dataSet, sandbox.metadata.account, sandbox.metadata.others.values());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   **
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountPageDef(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    // verify if the path where we want to operate exists
    final Metadata.Cust cust = Metadata.cust(sandbox.path(), PageDefinition.PATH);
    cust.ensureExists();
    marshalAccountBulkPageDef(cust,   sandbox);
    marshalAccountCreatePageDef(cust, sandbox);
    marshalAccountModifyPageDef(cust, sandbox);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountPage
  /**
   ** Marshals the page of a <code>Sandbox</code> to the file evaluated from the
   ** properties of the given {@link SandboxInstance}.
   **
   ** @param  sandbox            the {@link SandboxInstance} the pages has to be
   **                            generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountPage(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    // verify if the path where we want to operate exists
    final Metadata.Cust cust = Metadata.cust(sandbox.path(), PageView.PATH);
    cust.ensureExists();
    marshalAccountBulkPage(cust,   sandbox);
    marshalAccountCreatePage(cust, sandbox);
    marshalAccountModifyPage(cust, sandbox);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   **
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementPageDef(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    FormInstance.Account account = sandbox.account();
    for (FormInstance other : account.other()) {
      if (other.entitlement()) {
        // verify if the path where we want to operate exists
        final Metadata.Cust cust = Metadata.cust(sandbox.path(), PageDefinition.PATH);
        cust.ensureExists();
        marshalEntitlementBulkPageDef(cust,   sandbox, other);
        marshalEntitlementCreatePageDef(cust, sandbox, other);
        marshalEntitlementModifyPageDef(cust, sandbox, other);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementPage
  /**
   ** Marshals the page of a <code>Sandbox</code> to the file evaluated from the
   ** properties of the given {@link SandboxInstance}.
   **
   ** @param  sandbox            the {@link SandboxInstance} the pages has to be
   **                            generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementPage(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    FormInstance.Account account = sandbox.account();
    for (FormInstance other : account.other()) {
      if (other.entitlement()) {
        // verify if the path where we want to operate exists
        final Metadata.Cust cust = Metadata.cust(sandbox.path(), PageView.PATH);
        cust.ensureExists();
        
        marshalEntitlementBulkPage(cust,   sandbox, other);
        marshalEntitlementCreatePage(cust, sandbox, other);
        marshalEntitlementModifyPage(cust, sandbox, other);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountTaskFlow
  /**
   ** Marshals the task flow of a <code>Sandbox</code> to the file evaluated
   ** from the properties of the given {@link SandboxInstance}.
   **
   ** @param  sandbox            the {@link SandboxInstance} the task flow
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  public void marshalAccountTaskFlow(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    // verify if the path where we want to operate exists
    final Metadata.Path path = Metadata.path(sandbox.path(), TaskFlow.PATH);
    path.ensureExists();
    TaskFlow.marshalAccount(this, path, sandbox.dataSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementTaskFlow
  /**
   ** Marshals the task flow of a <code>Sandbox</code> to the file evaluated
   ** from the properties of the given {@link SandboxInstance}.
   **
   ** @param  sandbox            the {@link SandboxInstance} the task flow
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  public void marshalEntitlementTaskFlow(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    // verify if the path where we want to operate exists
    final Metadata.Path path = Metadata.path(sandbox.path(), TaskFlow.PATH);
    path.ensureExists();
    FormInstance.Account account = sandbox.account();
    for (FormInstance other : account.other()) {
      if (other.entitlement()) {
        // build the entitlement specific name by concatenating dataset and form
        final String dataSet = sandbox.dataSet + other.name();
        TaskFlow.marshalEntitlement(this, path, dataSet);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountBulkPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to bulk operations
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountBulkPageDef(final Metadata.Cust cust, final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    generatePageDef(PageDefinition.BULK,  sandbox.dataSet, cust);
    accountPageDef(PageDefinition.BULK, sandbox, cust.path());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountCreatePageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to create operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountCreatePageDef(final Metadata.Cust cust, final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    generatePageDef(PageDefinition.CREATE,  sandbox.dataSet, cust);
    accountPageDef(PageDefinition.CREATE, sandbox, cust.path());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountModifyPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to modify operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountModifyPageDef(final Metadata.Cust cust, final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    generatePageDef(PageDefinition.MODIFY,  sandbox.dataSet, cust);
    accountPageDef(PageDefinition.MODIFY, sandbox, cust.path());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountBulkPage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page view belongs to bulk operations
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountBulkPage(final Metadata.Cust cust, final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    final String  row   = sandbox.account.stringParameter(FormInstance.Panel.Hint.ROW.id(),    "5");
    final String  col   = sandbox.account.stringParameter(FormInstance.Panel.Hint.COLUMN.id(), "2");
    generatePage(PageView.BULK, sandbox.dataSet, sandbox.bundle, cust);
    final XMLOutputNode root = PageView.customizePage(this, cust.path(), sandbox.version.customization, String.format(PageView.BULK, sandbox.dataSet));
    PageView.marshalCustomization(root, row, col);
//    PageView.marshalAccountBulk(root, sandbox.dataSet, sandbox.account, sandbox.metadata.account, sandbox.metadata.others);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountCreatePage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page view belongs to create operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountCreatePage(final Metadata.Cust cust, final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    generatePage(PageView.CREATE, sandbox.dataSet, sandbox.bundle, cust);
    final XMLOutputNode root = PageView.customizePage(this, cust.path(), sandbox.version.customization, String.format(PageView.CREATE, sandbox.dataSet));
    PageView.marshalAccountCreate(root, sandbox);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccountModifyPage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page view belongs to create operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalAccountModifyPage(final Metadata.Cust cust, final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    generatePage(PageView.MODIFY, sandbox.dataSet, sandbox.bundle, cust);
    final XMLOutputNode root = PageView.customizePage(this, cust.path(), sandbox.version.customization, String.format(PageView.MODIFY, sandbox.dataSet));
    PageView.marshalAccountModify(root, sandbox);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementBulkPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to bulk operations
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementBulkPageDef(final Metadata.Cust cust, final SandboxInstance sandbox, final FormInstance form)
    throws IOException
    ,      XMLException {

    // build the entitlement specific name by concatenating dataset and form
    final String dataSet = sandbox.dataSet + form.name();
    generatePageDef(PageDefinition.BULK, dataSet, cust);
    entitlementPageDef(PageDefinition.BULK, sandbox, form, cust.path());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementCreatePageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to create operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementCreatePageDef(final Metadata.Cust cust, final SandboxInstance sandbox, final FormInstance form)
    throws IOException
    ,      XMLException {

    // build the entitlement specific name by concatenating dataset and form
    final String dataSet = sandbox.dataSet + form.name();
    generatePageDef(PageDefinition.CREATE, dataSet, cust);
    entitlementPageDef(PageDefinition.CREATE, sandbox, form, cust.path());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementModifyPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to modify operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementModifyPageDef(final Metadata.Cust cust, final SandboxInstance sandbox, final FormInstance form)
    throws IOException
    ,      XMLException {

    // build the entitlement specific name by concatenating dataset and form
    final String dataSet = sandbox.dataSet + form.name();
    generatePageDef(PageDefinition.MODIFY, dataSet, cust);
    entitlementPageDef(PageDefinition.MODIFY, sandbox, form, cust.path());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementBulkPage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page view belongs to bulk operations
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   ** @param  form               the {@link FormInstance} of an entitlement the
   **                            page view descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementBulkPage(final Metadata.Cust cust, final SandboxInstance sandbox, final FormInstance form)
    throws IOException
    ,      XMLException {

    final String  row   = form.stringParameter(FormInstance.Panel.Hint.ROW.id(),    "1");
    final String  col   = form.stringParameter(FormInstance.Panel.Hint.COLUMN.id(), "2");
    // build the entitlement specific name by concatanating dataset and form
    final String dataSet = sandbox.dataSet + form.name();
    generatePage(PageView.BULK, dataSet, sandbox.bundle, cust);
    final XMLOutputNode root = PageView.customizePage(this, cust.path(), sandbox.version.customization, String.format(PageView.BULK, dataSet));
    PageView.marshalCustomization(root, row, col);
//    PageView.marshalEntitlementBulk(root, sandbox.dataSet, sandbox.account, sandbox.metadata.account, sandbox.metadata.others);
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementCreatePage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page view belongs to create operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   ** @param  form               the {@link FormInstance} of an entitlement the
   **                            page view descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementCreatePage(final Metadata.Cust cust, final SandboxInstance sandbox, final FormInstance form)
    throws IOException
    ,      XMLException {

    // build the entitlement specific name by concatanating dataset and form
    final String dataSet = sandbox.dataSet + form.name();
    generatePage(PageView.CREATE, dataSet, sandbox.bundle, cust);
    final XMLOutputNode root = PageView.customizePage(this, cust.path(), sandbox.version.customization, String.format(PageView.CREATE, dataSet));
    PageView.marshalEntitlementCreate(root, form, sandbox.metadata.others().get(form.name()));
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlementModifyPage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page view belongs to create operations.
   **
   ** @param  cust               the {@link Metadata.Cust} abstract pathname to
   **                            marshaling the artifacts to.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   ** @param  form               the {@link FormInstance} of an entitlement the
   **                            page view descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalEntitlementModifyPage(final Metadata.Cust cust, final SandboxInstance sandbox, final FormInstance form)
    throws IOException
    ,      XMLException {

    // build the entitlement specific name by concatanating dataset and form
    final String dataSet = sandbox.dataSet + form.name();
    generatePage(PageView.MODIFY, dataSet, sandbox.bundle, cust);
    final XMLOutputNode root = PageView.customizePage(this, cust.path(), sandbox.version.customization, String.format(PageView.MODIFY, dataSet));
    PageView.marshalEntitlementModify(root, form, sandbox.metadata.others().get(form.name()));
    root.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generatePageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to modify operations.
   **
   ** @param  operation          the operations type of the page definition to
   **                            generated.
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   ** @param  path               the {@link Metadata.Path} abstract pathname to
   **                            marshaling the artifacts to.
   **
   ** @throws  XMLException      if the XML operations failed.
   */
  private void generatePageDef(final String operation, final String dataSet, final Metadata.Path path)
    throws XMLException {

    PageDefinition.marshalDefinition(this, path, String.format(operation, dataSet));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to modify operations.
   **
   ** @param  operation          the operations type of the page definition to
   **                            generated; usually one of:
   **                            <ul>
   **                              <li>{@link PageDefinition#BULK}
   **                              <li>{@link PageDefinition#CREATE}
   **                              <li>{@link PageDefinition#MODIFY}
   **                            </ul>
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   ** @param  path               the {@link Metadata.Path} abstract pathname to
   **                            marshaling the artifacts to.
   **
   ** @throws  XMLException      if the XML operations failed.
   */
  private void accountPageDef(final String operation, final SandboxInstance sandbox, final Metadata.Path path)
    throws XMLException {

    PageDefinition.marshalCustomization(this, path, sandbox.version.customization, String.format(operation, sandbox.dataSet), sandbox.dataSet, sandbox.metadata.account, sandbox.metadata.action, sandbox.metadata.others.values(), operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementPageDef
  /**
   ** Marshals the page definition descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   ** <p>
   ** The generated page definition belongs to modify operations.
   **
   ** @param  operation          the operations type of the page definition to
   **                            generated; usually one of:
   **                            <ul>
   **                              <li>{@link PageDefinition#BULK}
   **                              <li>{@link PageDefinition#CREATE}
   **                              <li>{@link PageDefinition#MODIFY}
   **                            </ul>
   ** @param  sandbox            the {@link SandboxInstance} the page definition
   **                            descriptor has to be generated.
   ** @param  path               the {@link Metadata.Path} abstract pathname to
   **                            marshaling the artifacts to.
   **
   ** @throws  XMLException      if the XML operations failed.
   */
  private void entitlementPageDef(final String operation, final SandboxInstance sandbox, final FormInstance form, final Metadata.Path path)
    throws XMLException {

    // build the entitlement specific name by concatenating dataset and form
    final String dataSet = sandbox.dataSet + form.name();
    PageDefinition.marshalCustomization(this, path, sandbox.version.customization, String.format(operation, dataSet), dataSet, sandbox.metadata.others.get(form.name()), operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateAccountPage
  /**
   ** Marshals the page view descriptor of a <code>Sandbox</code> to the
   ** file evaluated from the properties of the given {@link SandboxInstance}.
   **
   ** @param  operation          the operations type of the page view to
   **                            generated.
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   ** @param  path               the {@link Metadata.Path} abstract pathname to
   **                            marshaling the artifacts to.
   **
   ** @throws  XMLException      if the XML operations failed.
   */
  private void generatePage(final String operation, final String dataSet, final Collection<SandboxInstance.Bundle> bundle, final Metadata.Path path)
    throws XMLException {

    PageView.marshalDefinition(this, path, String.format(operation, dataSet), bundle);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalCatalog
  /**
   ** Marshals the view object usages of an application module customization in
   ** a <code>Sandbox</code> to the file evaluated from the properties of the
   ** given {@link SandboxInstance}.
   **
   ** @param  sandbox            the {@link SandboxInstance} the page view
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalCatalog(final SandboxInstance sandbox)
    throws IOException
    ,      XMLException {

    this.catalog.usage(sandbox.dataSet);
    for (FormInfo cursor : sandbox.metadata.others.values()) {
      this.catalog.usage(sandbox.dataSet, cursor.getName()).link(sandbox.dataSet,  cursor.getName());
    }

    Metadata.Cust cust = Metadata.cust(sandbox.path(), "persdef/oracle/iam/ui/catalog/model/am");
    // verify if the path where we want to operate exists
    cust.ensureExists();
    Catalog.marshalModule(this, cust.path(), sandbox.version.customization, this.catalog);
    cust = Metadata.cust(sandbox.path(), "oracle/iam/ui/common/model");
    // verify if the path where we want to operate exists
    cust.ensureExists();
    Catalog.marshalDefinitions(this, cust.path(), sandbox.version.customization, this.catalog);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateMetadata
  /**
   ** Populates the internal identifier of a <code>Resource Object</code> and
   ** the associated internal identifier <code>Process Form</code> from Identity
   ** Manager through the discovered {@link tcObjectOperationsIntf}.
   **
   ** @param  instance           the {@link SandboxInstance} to lookup the
   **                            <code>Resource Object</code> and
   **                            <code>Process Form</code> for.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void populateMetadata(final SandboxInstance instance)
    throws FeatureException {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ResourceObject.NAME, instance.resource());

    instance.metadata = null;
    try {
      tcResultSet cursor = this.objectFacade.findObjects(filter);
      if (cursor.getRowCount() == 0) {
        throw new FeatureException(FeatureError.RESOURCE_OBJECT_NOTEXISTS, instance.resource());
      }
      else if (cursor.getRowCount() > 1) {
        throw new FeatureException(FeatureError.RESOURCE_OBJECT_AMBIGUOS, instance.resource());
      }

      instance.metadata = new SandboxInstance.Metadata(cursor.getLongValue(ResourceObject.KEY), instance.resource());
      cursor = this.objectFacade.getProcessesForObject(instance.metadata.objectKey);
      if (cursor.getRowCount() == 0) {
        throw new FeatureException(FeatureError.PROCESS_DEFINITION_NOTEXISTS, instance.resource());
      }
      else if (cursor.getRowCount() > 1) {
        throw new FeatureException(FeatureError.PROCESS_DEFINITION_AMBIGUOS, instance.resource());
      }
      instance.metadata.processFormKey = cursor.getLongValue(FormDefinition.KEY);
    }
    catch (tcObjectNotFoundException e) {
      throw new FeatureException(FeatureError.RESOURCE_OBJECT_NOTEXISTS, instance.resource());
    }
    catch (tcColumnNotFoundException e) {
      throw new FeatureException(e);
    }
    catch (tcAPIException e) {
      throw new FeatureException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateProcessForm
  /**
   ** Populates the <code>Process Form</code> for the
   ** <code>Resource Object</code> from the Oracle Identity Manager through the
   ** discovered {@link tcObjectOperationsIntf}.
   ** <p>
   ** The system identifier of the <code>Process Form</code> to populate was
   ** previously verified by {@link lookupObject(SandboxInstance) and set in the
   ** metadata attribute of the specified {@link SandBoxInstance}
   ** code>instance</code>.
   **
   ** @param  instance           the {@link SandboxInstance} to populate the
   **                            <code>Process Form</code> for.
   **
   ** @throws Exception          in case an error does occur.
   */
  private void populateProcessForm(final SandboxInstance instance)
    throws Exception {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(FormDefinition.KEY, String.valueOf(instance.metadata.processFormKey));
    tcResultSet cursor = this.modelFacade.findForms(filter);
    cursor.goToRow(0);
    instance.metadata.account = new FormInfo(cursor.getStringValue(FormDefinition.NAME), cursor.getStringValue(FormDefinition.DESCRIPTION), cursor.getStringValue(FormDefinition.VERSION_LATEST), cursor.getStringValue(FormDefinition.VERSION_ACTIVE));
    instance.metadata.account.setFormKey(instance.metadata.processFormKey);
    instance.metadata.account.setChild(false);

    cursor = this.modelFacade.getFormFields(instance.metadata.account.getFormKey(), Integer.valueOf(instance.metadata.account.getActiveVersion()).intValue());
    for (int i = 0; i < cursor.getRowCount(); i++) {
      cursor.goToRow(i);
      String type = cursor.getStringValue(FormDefinition.COLUMN_TYPE);
      String defaultValue = cursor.getStringValue(FormDefinition.COLUMN_VALUE);
      // 14282676 - Since ITResourceLookupField variant type is long and the
      // default value can not be converted into long we are setting it to "0"
      // TODO: do we really need to remove the default value for an
      // ITResourceLookupField?
      if ((type != null) && (type.equalsIgnoreCase("ITResourceLookupField"))) {
        defaultValue = "0";
      }
      String  encrypted = cursor.getStringValue(FormDefinition.COLUMN_ENCRYPTED);
      boolean encryptFlag = false;
      // Fix for bug #17869410.
      // SDC_ENCRYPTED has only values of 0 and 1.
      // Setting encrypted flag if the value is 1.
      if (encrypted != null && encrypted.trim().equals("1"))
        encryptFlag = true;
      final FormField fld = new FormField(
        cursor.getStringValue(FormDefinition.COLUMN_NAME)
      , cursor.getLongValue(FormDefinition.COLUMN_KEY)
      , cursor.getStringValue(FormDefinition.COLUMN_LABEL)
      , type
      , defaultValue
      , encryptFlag
      );
      fld.setVariantType(cursor.getStringValue(FormDefinition.COLUMN_VARIANT));
      fld.setLength(cursor.getLongValue(FormDefinition.COLUMN_LENGTH));
      fld.setProperties(properties(this.modelFacade, fld.getFldKey()));
      instance.metadata.account.addFormField(fld);
    }
    // add a virtual attribute for the service account feature to the form
    instance.metadata.account.addFormField(new FormField("serviceaccount", -1L, "Service Account", "CheckBox", "false", false));

    cursor = this.instanceFacade.getChildFormDefinition(instance.metadata.account.getFormKey(), Integer.valueOf(instance.metadata.account.getActiveVersion()).intValue());
    for (int j = 0; j < cursor.getRowCount(); j++) {
      cursor.goToRow(j);
      final long     formKey = cursor.getLongValue(FormDefinition.CHILD_KEY);
      final FormInfo detail  = new FormInfo(
        cursor.getStringValue(FormDefinition.NAME)
      , cursor.getStringValue(FormDefinition.DESCRIPTION)
      , cursor.getStringValue(FormDefinition.CHILD_VERSION)
      , cursor.getStringValue(FormDefinition.CHILD_VERSION)
      );
      detail.setFormKey(formKey);
      detail.setChild(true);
      final tcResultSet field = this.modelFacade.getFormFields(formKey, Integer.valueOf(detail.getActiveVersion()).intValue());
      for (int k = 0; k < field.getRowCount(); k++) {
        field.goToRow(k);
        String  encrypted = field.getStringValue(FormDefinition.COLUMN_ENCRYPTED);
        boolean encryptFlag = false;
        // Fix for bug #17869410.
        // SDC_ENCRYPTED has only values of 0 and
        // 1. Setting encrypted flag if the value is 1.
        if (encrypted != null && encrypted.trim().equals("1"))
          encryptFlag = true;
        final FormField fld = new FormField(
          field.getStringValue(FormDefinition.COLUMN_NAME)
        , field.getLongValue(FormDefinition.COLUMN_KEY)
        , field.getStringValue(FormDefinition.COLUMN_LABEL)
        , field.getStringValue(FormDefinition.COLUMN_TYPE)
        , field.getStringValue(FormDefinition.COLUMN_VALUE)
        , encryptFlag
        );
        fld.setVariantType(field.getStringValue(FormDefinition.COLUMN_VARIANT));
        fld.setLength(field.getLongValue(FormDefinition.COLUMN_LENGTH));
        fld.setProperties(properties(this.modelFacade, fld.getFldKey()));
        detail.addFormField(fld);
      }
      instance.metadata.action.put(detail.getName(), j);
      instance.metadata.others.put(detail.getName(), detail);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toAttribute
  /**
   ** This creates the {@link DataSetInstance.Attribute} elements of the request
   ** dataset for various properties defined for a {@link FormInfo} through
   ** Design Console.
   **
   ** @param  field              the {@link FormInfo} to transform in a
   **                            collection of
   **                            {@link DataSetInstance.Attribute}.
   **
   ** @return                    the created collection of
   **                            {@link DataSetInstance.Attribute} derived from
   **                            the specified {@link FormInfo}.
   */
  private List<DataSetInstance.Attribute> toAttribute(final FormInfo form) {
    final List<DataSetInstance.Attribute> list = new ArrayList<DataSetInstance.Attribute>();
    for (FormField cursor : form.getFormFields()) {
      final DataSetInstance.Attribute attr = new DataSetInstance.Attribute(cursor.getName(), DataSetInstance.Attribute.Type.from(cursor.getVariantType()));
      attr.description = cursor.getLabel();
      attr.displayName = cursor.getLabel();
      attr.length = (int)cursor.getLength();
      attr.defaultValue = cursor.getDefaultValue();
      attr.refAttrName = cursor.getLabel();
      attr.lookupCode = (String)cursor.getProperties().get("Lookup Code");
      Object entitlement = cursor.getProperties().get("Entitlement");
      if (entitlement instanceof String) {
        attr.entitlement = Boolean.valueOf((String)entitlement);
      }
      attr.searchable = false;
      attr.readOnly = false;
      /*
      if (cursor.getType().equalsIgnoreCase("RadioButton") || cursor.getType().equalsIgnoreCase("ComboBox")) {
        attr.displayHint = DataSetInstance.Attribute.Hint.fromValue("CHOICE");
        attr.searchable = true;
      }
      else if (cursor.getType().equalsIgnoreCase("LookupField")) {
        attr.displayHint = DataSetInstance.Attribute.Hint.fromValue("INPUT_TEXT_LOV");
        attr.searchable  = true;
      }
      else if (cursor.getType().equalsIgnoreCase("PasswordField")) {
        attr.displayHint = DataSetInstance.Attribute.Hint.fromValue("PASSWORD");
        attr.searchable = true;
      }
      else if (cursor.getType().equalsIgnoreCase("DOField")) {
        attr.readOnly     = true;
        attr.defaultValue = null;
      }
      else if (cursor.getType().equalsIgnoreCase("DateFieldDlg")) {
        attr.searchable = true;
      }
      */
      attr.bulkUpdatable = false;
      attr.mandatory     = false;
      attr.encrypted     = cursor.isEncrypted();
      attr.keyField      = false;
      attr.createOnly    = false;
      attr.itresource    = cursor.getType().equals("ITResourceLookupField");
      list.add(attr);
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toReference
  /**
   ** This creates the {@link AttributeReference} element of the request dataset
   ** for various properties defined for a {@link DataSetInstance.Attribute}
   ** through Design Console.
   **
   ** @param  entity             the name of the <code>Resource Object</code> to
   **                            the given collection attributes belongs to.
   ** @param  attribute          the {@link DataSetInstance.Attribute} to
   **                            transform in a {@link AttributeReference}.
   **
   ** @return                    the created {@link AttributeReference} derived
   **                            from the specified
   **                            {@link DataSetInstance.Attribute}.
   */
  private AttributeReference toReference(final String entity, final DataSetInstance.Attribute attribute) {
    final AttributeReference reference = new AttributeReference();
    String                   attrName = attribute.displayName;

    reference.setName(attrName);
    reference.setAttrRef(attrName);
    reference.setType(attribute.type.dataSetType());
    reference.setAvailableInBulk(attribute.bulkUpdatable);
    reference.setHidden(attribute.encrypted);
    reference.setWidget("textarea");
    reference.setEntityType(entity);
    reference.setMasked(attribute.encrypted);
    // musst be a bug but that's realy what was found in the code
    //    attribute.lookupCode = attribute.lookupCode;
    // setting the missing attribute, required in the data set.
    // This is must to validate the mandatory attributes in the request engine.
    reference.setRequired(attribute.mandatory && attribute.bulkUpdatable);
    if (attribute.length > 0)
      reference.setLength(BigInteger.valueOf(attribute.length));
    else
      reference.setLength(BigInteger.valueOf(256));
    // if it's ITResource field then don't set the mandatory as true
    if (attribute.itresource) {
      reference.setRequired(false);
    }
    return reference;
  }

  static final String SUFFIX = "__c";
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fixAttrNames
  private void fixAttrNames(final List<DataSetInstance.Attribute> attrs) {
    for (DataSetInstance.Attribute a : attrs) {
      if (a.name.toLowerCase().endsWith(SUFFIX)) {
        a.name = a.name.substring(0, a.name.length() - 1) + a.name.substring(a.name.length() - 1).toLowerCase();
      }
      else {
        a.name += SUFFIX;

      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Discovers and returns the properties  of <code>Process Form</code> field
   ** from Identity Manager through the discovered
   ** {@link tcFormDefinitionOperationsIntf}.
   ** <p>
   ** The implementation looks really ugly but there isnt't a method existing
   ** that fetches all properties defined for a field. In the kernel
   ** implementation direct access to the database is used to populate the
   ** properties which is impossible to do it from remote
   **
   ** @param  attribute          the internal identifier of the
   **                            <code>Process Form</code> attribute the
   **                            properties needs to be populated for.
   **
   ** @return                    the tagged-value-pair mapping of properties
   **                            defined for the attribute.
   **
   ** @throws Exception          in case an error does occur.
   */
  private HashMap<String, Object> properties(final tcFormDefinitionOperationsIntf facade, final long attribute)
    throws Exception {

    final HashMap<String, Object> values = new HashMap<String, Object>();
    try {
      for (String name : PROPERTY) {
        try {
          values.put(name, facade.getFormFieldPropertyValue(attribute, name));
        }
        catch (tcPropertyNotFoundException e) {
          debug(String.format("Property '%s' has not been assigned to form field with key '%d'", name, attribute));
          continue;
        }
      }
    }
    catch (tcFormFieldNotFoundException e) {
      throw e;
    }
    catch (tcAPIException e) {
      throw e;
    }
    return values;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateDataSet
  /**
   ** Validates if the <code>Request DataSet</code> needed is valid.
   ** <br>
   ** If the <code>Request DataSet</code> is valid but not existing a new one
   ** will be created.
   **
   ** @param  instance           the {@link SandboxInstance} to validate.
   **
   ** @return                    <code>true</code> if further processing can
   **                            happen.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private boolean validateDataSet(final SandboxInstance instance)
    throws FeatureException {

    RequestDataSet dataSet = null;
    try {
      final List<RequestDataSet> result = this.dataSetFacade.getRequestDataSets(OIMType.Resource, instance.resource);
      if (result.size() > 1) {
        for (RequestDataSet cursor : result) {
          if (cursor.getName().equals(instance.dataSet())) {
            dataSet = cursor;
            break;
          }
        }
      }
      if (result.size() == 1) {
        dataSet = result.get(0);
      }
    }
    catch (RequestServiceException e) {
      throw new FeatureException(e);
    }

    if (dataSet == null) {
      dataSetCreate(populateRequestDataSet(instance));
    } else if (instance.forceOverride() == ForceOverride.NEVER) {
      //do nothing
    } else if (instance.forceOverride() == ForceOverride.FALSE) {
      // 0=yes, 1=no, 2=cancel
      int response = JOptionPane.showConfirmDialog(
        null
      , FeatureResourceBundle.format(FeatureMessage.SANDBOX_DATASET_MESSAGE, instance.dataSet)
      , FeatureResourceBundle.format(FeatureMessage.SANDBOX_DATASET_TITLE,   instance.dataSet)
      , JOptionPane.YES_NO_CANCEL_OPTION
      , JOptionPane.QUESTION_MESSAGE
      );
      switch (response) {
        case JOptionPane.YES_OPTION    : dataSetModify(dataSet);
                                         break;
        case JOptionPane.NO_OPTION     : error(FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_SKIPPED, dataSet.getName(), dataSet.getEntity()));
                                         break;
        case JOptionPane.CANCEL_OPTION : error(FeatureResourceBundle.format(FeatureError.SANDBOX_MARSHALL_STOPPED, instance.name(), instance.path()));
                                         return false;
        default                        : return false;
      }
    }
    else if (instance.forceOverride() == ForceOverride.TRUE) {
      dataSetModify(dataSet);
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSetCreate
  /**
   ** Creates the <code>Request DataSet</code>.
   **
   ** @param  dataSet            the {@link RequestDataSet} to create.
   **
   ** @throws BuildException     in an error does occurs creating the
   **                            <code>Request DataSet</code>.
   */
  private void dataSetCreate(final RequestDataSet dataSet) {
    warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_DATASET_CREATE, dataSet.getName()));
    try {
      this.dataSetFacade.createRequestDataSet(dataSet);
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_DATASET_CREATED, dataSet.getName()));
    }
    catch (RequestServiceException e) {
      if (failonerror())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.REQUEST_DATASET_CREATE, dataSet.getEntity(), e.getMessage()));
      else
        error(FeatureResourceBundle.format(FeatureError.REQUEST_DATASET_CREATE, dataSet.getEntity(), e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSetModify
  /**
   ** Modifies an existing <code>Request DataSet</code>.
   **
   ** @param  dataSet            the {@link RequestDataSet} to modify.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Request DataSet</code> was modified
   **                            successfully.
   */
  private void dataSetModify(final RequestDataSet dataSet) {
    warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_DATASET_MODIFY, dataSet.getName()));
    try {
      this.dataSetFacade.updateRequestDataSet(dataSet);
      warning(FeatureResourceBundle.format(FeatureMessage.SANDBOX_DATASET_MODIFIED, dataSet.getName()));
    }
    catch (RequestServiceException e) {
      if (failonerror())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.REQUEST_DATASET_MODIFY, dataSet.getEntity(), e.getMessage()));
      else
        error(FeatureResourceBundle.format(FeatureError.REQUEST_DATASET_MODIFY, dataSet.getEntity(), e.getMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateRequestDataSet
  /**
   ** Populates the {@link RequestDataSet} of a <code>Resource Object</code>
   ** from Identity Manager through the discovered
   ** {@link RequestDataSetService}.
   **
   ** @param  instance           the {@link SandboxInstance} to lookup the
   **                            {@link RequestDataSet} for.
   */
  private RequestDataSet populateRequestDataSet(final SandboxInstance instance) {
    final RequestDataSet dataSet = new RequestDataSet();
    dataSet.setName(instance.dataSet);
    dataSet.setEntity(instance.resource);

    final List<DataSetInstance.Attribute> account = toAttribute(instance.metadata.account);
    fixAttrNames(account);

    final List<AttributeReference> attributes = dataSet.getAttributeReference();
    for (DataSetInstance.Attribute cursor : account) {
      if (!DataSetInstance.INGORE.contains(cursor.refAttrName))
        attributes.add(toReference(instance.resource, cursor));
    }

    for (FormInfo cursor : instance.metadata.others.values()) {
      final AttributeReference ref = new AttributeReference();
      ref.setName(cursor.getName());
      ref.setAttrRef(cursor.getName());
      ref.setType(DataSetInstance.Attribute.Type.TEXT.toString());
      ref.setWidget("textarea");
      ref.setEntityType(instance.resource);
      ref.setLength(BigInteger.valueOf(256));
      // make child form container attribute reference as always bulk updatable
      ref.setAvailableInBulk(true);
      List<AttributeReference> childAttrs = ref.getAttributeReference();
      for (DataSetInstance.Attribute attr : toAttribute(cursor)) {
        childAttrs.add(toReference(instance.resource, attr));
      }
      attributes.add(ref);
    }
    return dataSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validatePath
  /**
   ** Validates if the path needed to the marshal a sandbox is valid and empty.
   ** <br>
   ** If the path is valid but not empty the user needs to confirm that the
   ** directory used to mashall the sandbox files are cleanup (any file or
   ** directory will be deleted.
   **
   **
   ** @param  instance           the {@link SandboxInstance} to validate.
   **
   ** @return                    <code>true</code> if further processing can
   **                            happen.
   */
  private boolean validatePath(final SandboxInstance instance) {
    final File path = instance.path();
    if (instance.forceOverride() == ForceOverride.FALSE && path.exists()) {
      int response = JOptionPane.showConfirmDialog(
        null
      , FeatureResourceBundle.format(FeatureMessage.SANDBOX_DIRECTORY_MESSAGE, path.getName(), path.getParent())
      , FeatureResourceBundle.format(FeatureMessage.SANDBOX_DIRECTORY_TITLE,   instance.name())
      , JOptionPane.YES_NO_CANCEL_OPTION
      , JOptionPane.QUESTION_MESSAGE
      );
      switch (response) {
        case JOptionPane.YES_OPTION    : return deletePath(path);
        case JOptionPane.NO_OPTION     : return true;
        case JOptionPane.CANCEL_OPTION : return false;
        default                        : return false;
      }
    }
    else if (instance.forceOverride() == ForceOverride.TRUE && path.exists()) {
      return deletePath(path);
    }
    else
      return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deletePath
  /**
   ** Deletes the entire structure of a sandbox provioulsy marshalled.
   **
   ** @param  path               the abstract pathname to delete.
   **
   ** @return                    <code>true</code> if further processing can
   **                            happen.
   **
   ** @throws BuildException     if the path could not be deleted entirely.
   */
  private boolean deletePath(final File path) {
    // delete the entire directory of the previously marshalled expolded
    // sandbox
    try {
      delete(path);
    }
    // if an exception is trown by the delete operation abort any further
    // action
    catch (IOException e) {
      if (failonerror())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_DELETE, e.getMessage()));
      else
        error(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_DELETE, e.getMessage()));
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes the entire structur of a sandbox provioulsy marshalled.
   ** <br>
   ** This step is required to have only those file in the archive produced
   ** later that are realy wanted.
   ** <p>
   ** Deletes the file or directory denoted by <code>path</code>. This pathname
   ** should denote to the directory of the exploded sandbox.
   ** <p>
   ** Note that the {@link java.nio.file.Files} class defines the
   ** {@link java.nio.file.Files#delete(Path) delete} method to throw an
   ** {@link IOException} when a file cannot be deleted. This is useful for
   ** error reporting and to diagnose why a file cannot be deleted.
   **
   ** @param  path               the abstract pathname to an exploded sandbox
   **                            directory.
   **
   ** @return                    <code>true</code> if and only if the file or
   **                            directory is successfully deleted;
   **                            <code>false</code> otherwise.
   **
   ** @throws  IOException       if the operations at the file system fails.
   */
  private void delete(final File path)
    throws IOException {

    if (path.isDirectory()) {
      for (File cursor : path.listFiles())
        delete(cursor);
    }
    if (!path.delete())
      throw new IOException(path.getAbsolutePath());
  }

}