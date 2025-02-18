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

    File        :   RequestRegistry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RequestRegistry.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.math.BigInteger;

import javax.swing.JOptionPane;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.tools.ant.BuildException;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcPropertyNotFoundException;

import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.ServiceProvider;

import oracle.iam.identity.foundation.naming.FormDefinition;
import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.request.type.Widget;
import oracle.iam.identity.request.type.Variant;
import oracle.iam.identity.request.type.Process;
import oracle.iam.identity.request.type.Resource;
import oracle.iam.identity.request.type.Operation;
import oracle.iam.identity.request.type.Attribute;
import oracle.iam.identity.request.type.LookupQuery;
import oracle.iam.identity.request.type.ProcessForm;
import oracle.iam.identity.request.type.ObjectFactory;
import oracle.iam.identity.request.type.RequestDataSet;
import oracle.iam.identity.request.type.DataSetValidator;
import oracle.iam.identity.request.type.AttributeReference;

////////////////////////////////////////////////////////////////////////////////
// class RequestRegistry
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle request data.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RequestRegistry extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Map<String, String> VARIANT = new HashMap<String, String>();
  static final Map<String, String> WIDGET = new HashMap<String, String>();
  static final Map<String, String> MODEL = new HashMap<String, String>();

  static final int DEFAULT_LENGTH = 20;
  static final int SERVER_LENGTH = 10;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static ObjectFactory factory = new ObjectFactory();
  private static Marshaller    marshaller = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      // The JAXBContext instance is initialized from a list of colon separated
      // Java package names. Each java package contains JAXB mapped classes,
      // schema-derived classes and/or user annotated classes.
      // Additionally, the java package may contain JAXB package annotations
      // that must be processed. (see JLS 3rd Edition, Section 7.4.1. Package
      // Annotations).
      JAXBContext context = JAXBContext.newInstance(ClassUtility.packageName(ObjectFactory.class));

      marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.valueOf(true));
      marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.oracle.com/schema/oim/request");
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    VARIANT.put(Variant.BYTE,          "Byte");
    VARIANT.put(Variant.DOUBLE,        "Double");
    VARIANT.put(Variant.INTEGER,       "Integer");
    VARIANT.put(Variant.STRING,        "String");
    VARIANT.put(Variant.SHORT,         "Short");
    VARIANT.put(Variant.LONG,          "Long");
    VARIANT.put(Variant.DATE,          "Date");
    VARIANT.put(Variant.BOOLEAN,       "Boolean");
    VARIANT.put(Variant.ARRAY,         "ByteArray");

    WIDGET.put("TextField",             Widget.TEXT);
    WIDGET.put("DOField",               Widget.TEXT);
    WIDGET.put("PasswordField",         Widget.TEXT);
    WIDGET.put("TextArea",              Widget.TEXTAREA);
    WIDGET.put("CheckBox",              Widget.CHECKBOX);
    WIDGET.put("RadioButton",           Widget.RADIO);
    WIDGET.put("DateFieldDlg",          Widget.DATE);
    WIDGET.put("ComboBox",              Widget.DROPDOWN);
    WIDGET.put("LookupField",           Widget.LOOKUP);
    WIDGET.put("ITResourceLookupField", Widget.ITRESOURCE);

    //    MODEL.put(Operation.SELFPROVISION,  "SelfProvisionResource");
    MODEL.put(ServiceOperation.provision.id(),   "ProvisionResource");
    MODEL.put(ServiceOperation.deprovision.id(), "DeprovisionResource");
    MODEL.put(ServiceOperation.modify.id(),      "ModifyResource");
    MODEL.put(ServiceOperation.enable.id(),      "EnableProvisionedResource");
    MODEL.put(ServiceOperation.disable.id(),     "DisableProvisionedResource");
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean forceOverride = false;

  private Resource             resource    = null;
  private final List<File>     fileSet     = new ArrayList<File>();
  private final List<Resource> resourceSet = new ArrayList<Resource>();

  /** the business logic layer to operate on */
  private tcObjectOperationsIntf         objectFacade;
  private tcFormDefinitionOperationsIntf modelFacade;
  private tcFormInstanceOperationsIntf   instanceFacade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestRegistry</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public RequestRegistry(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride      <code>true</code> to override the existing file
   **                            without to aks for user confirmation.
   */
  public void forceOverride(final boolean forceOverride) {
    this.forceOverride = forceOverride;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Returns the how to handle existing files
   ** to.
   **
   ** @return                    <code>true</code> if the existing file will be
   **                            overridden without any further confirmation.
   */
  public final boolean forceOverride() {
    return this.forceOverride;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException   in case an error does occur.
   */
  @Override
  public void validate() {
    if ((this.resource == null) && (this.resourceSet.size() == 0))
      throw new BuildException(FeatureResourceBundle.string(FeatureError.EXPORT_FILE_MANDATORY));

    try {
      if ((this.resource != null))
        this.resource.validate();

      Iterator<Resource> i = this.resourceSet.iterator();
      while (i.hasNext())
        i.next().validate();
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addResource
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Resource}.
   **
   ** @param  resource           the resources to add for generation.
   **
   ** @throws BuildException     if the file the {@link Resource} in the set is
   **                            already part of this export operation.
   */
  public void addResource(final Resource resource) {
    // check if we have the file already added
    if (this.fileSet.contains(resource.folder()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_DATASETFILE_ONLYONCE, resource.folder().getAbsolutePath()));

    // check if we have the resource already added
    if (this.resourceSet.contains(resource.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_RESOURCE_ONLYONCE, resource.name()));

    this.fileSet.add(resource.folder());
    this.resourceSet.add(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Creates a new role in  Identity Manager through the discovered
   ** {@link tcObjectOperationsIntf}.
   **
   ** @param  objectFacade       the {@link tcObjectOperationsIntf}
   **                            used to perform the operation.
   ** @param  modelFacade        the {@link tcFormDefinitionOperationsIntf}
   **                            used to perform the operation.
   ** @param  instanceFacade     the {@link tcFormInstanceOperationsIntf}
   **                            used to perform the operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void generate(final tcObjectOperationsIntf objectFacade, final tcFormDefinitionOperationsIntf modelFacade, final tcFormInstanceOperationsIntf instanceFacade)
    throws ServiceException {

    this.objectFacade = objectFacade;
    this.modelFacade = modelFacade;
    this.instanceFacade = instanceFacade;

    if (this.resource != null)
      generate(this.resource);

    Iterator<Resource> i = this.resourceSet.iterator();
    while (i.hasNext())
      generate(i.next());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates a new request data set in Identity Manager through the
   ** discovered {@link tcObjectOperationsIntf}.
   **
   ** @param  instance           the {@link Resource} the request data ser has
   **                            to be generated for.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void generate(final Resource instance)
    throws ServiceException {

    // extend the attribute mapping with the name of the role to create
    try {
      if (!exists(instance)) {
        final String[] arguments = { ResourceObject.NAME, instance.name() };
        warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
        return;
      }
      populate(instance);
      if (instance.process().size() == 0) {
        final String[] arguments = { ResourceObject.NAME, instance.name() };
        warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
        return;
      }
      createDataSet(instance);
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataSet
  /**
   ** Creates the Request DataSet for the specified instance of {@link Resource}.
   **
   ** @param  resource           the {@link Resource} the metadata has to be
   **                            created for.
   **
   ** @throws ServiceException   if the create process fails
   */
  private void createDataSet(final Resource resource)
    throws ServiceException {

    final List<AttributeReference> reference = new ArrayList<AttributeReference>();
    for (Process process : resource.process()) {
      transform(process.form(), reference);

      createDataSet(resource, process, reference);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataSet
  /**
   ** Creates the Request DataSet for the specified instance of {@link Resource}
   ** and the particular {@link Process} with the specified list of
   ** {@link AttributeReference}s.
   **
   ** @param  resource           the {@link Resource} the Request DataSet has to
   **                            be created for.
   ** @param  process            the {@link Process} belonging to the
   **                            <code>resource</code> the Request DataSet has
   **                            to be created for.
   ** @patam  reference          the list of {@link AttributeReference}s to be
   **                            contained in the created Request DataSet.
   **
   ** @throws ServiceException   if the create process fails
   */
  private void createDataSet(final Resource resource, final Process process, final List<AttributeReference> reference)
    throws ServiceException {

    final List<AttributeReference> summary = new ArrayList<AttributeReference>();
    final List<Attribute>          attribute = new ArrayList<Attribute>();

    if (process.contains(ServiceOperation.provision.id())) {
      final Operation o = process.operation(ServiceOperation.provision.id());
      summary.clear();
      summary.addAll(reference);
      summary.addAll(process.reference());
      attribute.clear();
      attribute.addAll(process.attribute());
      if (o != null) {
        reference.addAll(o.reference());
        attribute.addAll(o.attribute());
      }
      createDataSet(resource, ServiceOperation.provision, summary, attribute, process.validator());
    }

    if (process.contains(ServiceOperation.modify.id())) {
      final Operation o = process.operation(ServiceOperation.provision.id());
      summary.clear();
      summary.addAll(reference);
      summary.addAll(process.reference());
      attribute.clear();
      attribute.addAll(process.attribute());
      if (o != null) {
        reference.addAll(o.reference());
        attribute.addAll(o.attribute());
      }
      createDataSet(resource, ServiceOperation.modify, summary, attribute, process.validator());
    }
    /*
    if (process.contains(Process.SELFPROVISION)) {
      final Operation o = process.operation(Process.PROVISION);
      summary.clear();
      summary.addAll(reference);
      summary.addAll(process.reference());
      attribute.clear();
      attribute.addAll(process.attribute());
      if (o != null) {
        reference.addAll(o.reference());
        attribute.addAll(o.attribute());
      }
      createDataSet(resource, Process.SELFPROVISION, summary, attribute, process.validator());
    }
*/
    if (process.contains(ServiceOperation.enable.id()))
      createDataSet(resource, ServiceOperation.enable, null, null, null);

    if (process.contains(ServiceOperation.disable.id()))
      createDataSet(resource, ServiceOperation.disable, null, null, null);

    if (process.contains(ServiceOperation.deprovision.id()))
      createDataSet(resource, ServiceOperation.deprovision, null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataSet
  private void createDataSet(final Resource instance, final ServiceOperation operation, final List<AttributeReference> reference, final List<Attribute> attribute, final DataSetValidator validator)
    throws ServiceException {

    final String[] arguments = { MODEL.get(operation.id()), instance.name(), operation.id().toUpperCase() };
    info(FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_CREATE, arguments));
    RequestDataSet dataSet = factory.createRequestDataSet();

    if (reference != null)
      dataSet.getAttributeReference().addAll(reference);
    if (attribute != null)
      dataSet.getAttribute().addAll(attribute);
    if (validator != null)
      dataSet.setDataSetValidator(validator);

    dataSet.setName(MODEL.get(operation) + instance.name());
    dataSet.setOperation(operation.id().toUpperCase());
    dataSet.setEntity(instance.name());

    try {
      if (!instance.folder().exists())
        instance.folder().mkdirs();

      final String[] path = { dataSet.getName() + ".xml", instance.folder().getAbsolutePath() };
      int            response = 0;
      File           output = new File(instance.folder(), path[0]);
      if (!this.forceOverride && output.exists()) {
        response = JOptionPane.showConfirmDialog
          (null
        , FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_CONFIRMATION_MESSAGE, path, instance.folder())
        , FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_CONFIRMATION_TITLE,   dataSet.getName())
        , JOptionPane.YES_NO_OPTION
        , JOptionPane.QUESTION_MESSAGE
        );
      }

      if (response == JOptionPane.YES_OPTION) {
        marshaller.marshal(dataSet, new FileOutputStream(output));
        info(FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_SUCCESS, arguments));
      }
      else
        warning(FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_SKIPPED, arguments));
    }
    catch (FileNotFoundException e) {
      error(FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_ERROR, arguments));
      if (failonerror())
        throw new ServiceException(ServiceError.GENERAL, e);
      else
        error(ServiceResourceBundle.format(ServiceError.GENERAL, e.getLocalizedMessage()));
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.REQUEST_DATASET_ERROR, arguments));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Find the resource entity definition in Identity Manager database
   ** repository.
   **
   ** @param  resource           the {@link Resource} to populate from the
   **                            metadata repository of Identity Manager.
   **
   ** @throws ServiceException   if the assembler process fails
   */
  private void populate(final Resource resource)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ResourceObject.NAME, resource.name());
    try {
      tcResultSet cursor = this.objectFacade.findObjects(filter);
      if (cursor.getRowCount() == 0) {
        final String[] arguments = { ResourceObject.NAME, resource.name() };
        error(FeatureResourceBundle.format(FeatureError.RESOURCE_OBJECT_NOTEXISTS, arguments));
        return;
      }
      if (cursor.getRowCount() > 1) {
        final String[] arguments = { ResourceObject.NAME, resource.name() };
        error(FeatureResourceBundle.format(FeatureError.RESOURCE_OBJECT_AMBIGUOS, arguments));
        return;
      }

      cursor.goToRow(0);
      long objectKey = cursor.getLongValue(ResourceObject.KEY);

      cursor = this.objectFacade.getProcessesForObject(objectKey);
      if (cursor.getRowCount() == 0) {
        final String[] arguments = { ResourceObject.NAME, resource.name() };
        error(FeatureResourceBundle.format(FeatureError.PROCESS_DEFINITION_NOTEXISTS, arguments));
        return;
      }

      final boolean all = (resource.process().size() == 0);
      for (int i = 0; i < cursor.getRowCount(); i++) {
        cursor.goToRow(i);
        final String processName = cursor.getStringValue(Process.NAME);
        Process      process = null;
        for (int j = 0; i < resource.process().size(); i++) {
          process = resource.process().get(j);
          if (process.name().equals(processName))
            break;
        }
        // if the process definition is not specified add all process forms or
        // add only processes that match the requested process definitionen
        if (all) {
          process = new Process(processName);
          resource.addConfiguredProcess(process);
        }
        long formKey = cursor.getLongValue(FormDefinition.KEY);
        int  version = this.instanceFacade.getActiveVersion(formKey);
        filter.clear();
        filter.put(FormDefinition.KEY, String.valueOf(formKey));
        filter.put(FormDefinition.VERSION_ACTIVE, String.valueOf(version));
        tcResultSet form = this.modelFacade.findForms(filter);
        if (form.getRowCount() == 0) {

        }
        if (form.getRowCount() > 1) {

        }
        form.goToRow(0);
        ProcessForm processForm = new ProcessForm(formKey, version, form.getStringValue(FormDefinition.NAME), form.getStringValue(FormDefinition.DESCRIPTION));
        process.form(processForm);
        populate(process, formKey, version);
      }
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link Resource} exists in Identity Manager
   ** through the discovered {@link tcObjectOperationsIntf}.
   **
   ** @param  resource           the {@link Resource} to check for existance.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean exists(final Resource resource)
    throws ServiceException {

    boolean             exactMatch = false;
    Map<String, String> filter = new HashMap<String, String>();
    filter.put(ResourceObject.NAME, resource.name());
    try {
      tcResultSet resultSet = this.objectFacade.findObjects(filter);
      exactMatch = (resultSet.getRowCount() == 1);
    }
    catch (Exception e) {
      exactMatch = false;
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
    return exactMatch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** ....
   **
   ** @param  form               the {@link ProcessForm} to transform in a
   **                            {@link List} of {@link AttributeReference}s and
   **                            {@link Attribute}s.
   ** @param  reference          the container for {@link AttributeReference}s
   */
  private void transform(ProcessForm form, List<AttributeReference> reference) {
    reference.clear();

    for (ProcessForm.Field main : form.field())
      reference.add(toReference(main));

    for (ProcessForm child : form.child())
      reference.add(toReference(child));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toAttribute
  /**
   ** This creates the {@link Attribute} element of the request dataset for
   ** various properties defined for a {@link ProcessForm.Field} through Design
   ** Console.
   **
   ** @param  field              the {@link ProcessForm.Field} to transform in a
   **                            {@link Attribute}.
   **
   ** @return                    the created {@link Attribute} derived from the
   **                            specified {@link ProcessForm.Field}.
   */
  private Attribute toAttribute(final ProcessForm.Field field) {
    final String    variant = field.variant();
    final Attribute attribute = new Attribute();
    attribute.setName(field.label());
    attribute.setType(VARIANT.get(variant));
    attribute.setWidget(WIDGET.get(field.type()));

    if (Variant.PASSWORD.equalsIgnoreCase(field.variant()))
      attribute.setMasked(true);

    // if the variant type is date, setting the length of the field to 20 we
    // are not allowed to set the length thru design console for a field of
    // type Date but we need to have it in request dataset as "length" is a
    // mandatory attribute for all the attribute/attribute reference elements
    if (Variant.DATE.equalsIgnoreCase(variant)
     || Variant.INTEGER.equalsIgnoreCase(variant)
     || Variant.SHORT.equalsIgnoreCase(variant)
     || Variant.LONG.equalsIgnoreCase(variant)
     || Variant.BOOLEAN.equalsIgnoreCase(variant)
     || Variant.DOUBLE.equalsIgnoreCase(variant)
     || Variant.BYTE.equalsIgnoreCase(variant))
      attribute.setLength(BigInteger.valueOf(DEFAULT_LENGTH));
    else
      attribute.setLength(BigInteger.valueOf(field.length()));

    if (field.propertySize() > 0) {
      Iterator<String> i = field.propertyKeyIterator();
      while (i.hasNext()) {
        final String key = i.next();
        final String value = field.propertyValue(key);

        if (ProcessForm.PROPERTY_REQUIRED.equalsIgnoreCase(key))
          attribute.setRequired(Boolean.valueOf(SystemConstant.TRUE.equalsIgnoreCase(value)));

        // Current implementation of hidden attribute in request dataset works
        // as follows hidden=true. Visible to requester but not visible to
        // approver. hidden=false visible to both"
        if (ProcessForm.PROPERTY_VISIBLE.equalsIgnoreCase(key))
          attribute.setHidden(Boolean.valueOf(SystemConstant.TRUE.equalsIgnoreCase(value)));

        if (ProcessForm.PROPERTY_LOOKUP_CODE.equalsIgnoreCase(key))
          attribute.setLookupCode(value);
      }
    }
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toReference
  /**
   ** This creates the {@link AttributeReference} element of the request dataset
   ** for various properties defined for a {@link ProcessForm.Field} through
   ** Design Console.
   **
   ** @param  form               the {@link ProcessForm} to transform in a
   **                            {@link AttributeReference}.
   **
   ** @return                    the created {@link AttributeReference} derived
   **                            from the specified {@link ProcessForm.Field}.
   */
  private AttributeReference toReference(final ProcessForm form) {
    final AttributeReference reference = new AttributeReference();
    reference.setName(form.logicalName());
    reference.setAttrRef(form.physicalName());

    reference.setType(VARIANT.get(Variant.STRING));
    reference.setWidget(WIDGET.get("TextField"));
    reference.setLength(BigInteger.valueOf(form.field().get(0).length()));

    for (ProcessForm.Field field : form.field())
      reference.getAttributeReference().add(toReference(field));

    return reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toReference
  /**
   ** This creates the {@link AttributeReference} element of the request dataset
   ** for various properties defined for a {@link ProcessForm.Field} through
   ** Design Console.
   **
   ** @param  field              the {@link ProcessForm} to transform in a
   **                            {@link AttributeReference}.
   **
   ** @return                    the created {@link AttributeReference} derived
   **                            from the specified {@link ProcessForm.Field}.
   */
  private AttributeReference toReference(final ProcessForm.Field field) {
    final String             variant = field.variant();
    final AttributeReference reference = new AttributeReference();
    reference.setName(field.label());
    reference.setAttrRef(field.label());
    reference.setType(VARIANT.get(variant));
    reference.setWidget(WIDGET.get(field.type()));

    if (Variant.PASSWORD.equalsIgnoreCase(variant))
      reference.setMasked(true);

    // if the variant type is date, setting the length of the field to 20 we
    // are not allowed to set the length thru design console for a field of
    // type Date but we need to have it in request dataset as "length" is a
    // mandatory attribute for all the attribute/attribute reference elements
    if (Variant.DATE.equalsIgnoreCase(variant)
     || Variant.INTEGER.equalsIgnoreCase(variant)
     || Variant.SHORT.equalsIgnoreCase(variant)
     || Variant.LONG.equalsIgnoreCase(variant)
     || Variant.BOOLEAN.equalsIgnoreCase(variant)
     || Variant.DOUBLE.equalsIgnoreCase(variant)
     || Variant.BYTE.equalsIgnoreCase(variant))
      reference.setLength(BigInteger.valueOf(DEFAULT_LENGTH));
    else
      reference.setLength(BigInteger.valueOf(field.length()));

    if (field.propertySize() > 0) {
      Iterator<String> i = field.propertyKeyIterator();
      while (i.hasNext()) {
        final String key = i.next();
        final String value = field.propertyValue(key);

        if (ProcessForm.PROPERTY_REQUIRED.equalsIgnoreCase(key))
          reference.setRequired(Boolean.valueOf(SystemConstant.TRUE.equalsIgnoreCase(value)));

        // current implementation of hidden attribute in request dataset works
        // as follows:
        // hidden=true. Visible to requester but not visible to approver.
        // hidden=false visible to both
        if (ProcessForm.PROPERTY_VISIBLE.equalsIgnoreCase(key))
          reference.setHidden(Boolean.valueOf(SystemConstant.FALSE.equalsIgnoreCase(value)));

        if (ProcessForm.PROPERTY_LOOKUP_CODE.equalsIgnoreCase(key))
          reference.setLookupCode(value);

        // the value in case of it resource is the key to svd_svr_type fetching
        // the svd_svr_type for this value
        if (ProcessForm.PROPERTY_LOOKUP_TYPE.equalsIgnoreCase(key)) {
          try {
            String serverType = this.modelFacade.getITResourceServerType(field.key());
            reference.setLength(BigInteger.valueOf(SERVER_LENGTH));
            reference.setItresourceType(serverType);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }

        if (ProcessForm.PROPERTY_LOOKUP_QUERY.equalsIgnoreCase(key)) {
          LookupQuery lookupQuery = reference.getLookupQuery();
          if (lookupQuery == null) {
            lookupQuery = new LookupQuery();
            reference.setLookupQuery(lookupQuery);
          }
          lookupQuery.setLookupQuery(value);
          lookupQuery.setDisplayField(field.propertyValue(ProcessForm.PROPERTY_LOOKUP_DISPLAY));
          lookupQuery.setSaveField(field.propertyValue(ProcessForm.PROPERTY_LOOKUP_COLUMN));
        }
      }
    }
    return reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Find the entity definition in the Identity Manager database repository.
   **
   ** @param  process            the {@link Process} the {@link ProcessForm} has
   **                            to be populate from the metadata repository of
   **                            Identity Manager.
   ** @param  key                the system identifier of the
   **                            {@link ProcessForm} to populate.
   ** @param  version            the version identifier of the
   **                            {@link ProcessForm} to populate.
   **
   ** @throws ServiceException   if the assembler process fails
   */
  private void populate(final Process process, long key, int version)
    throws ServiceException {

    Map<String, String> filter = new HashMap<String, String>();
    filter.put(FormDefinition.KEY, String.valueOf(key));
    filter.put(FormDefinition.VERSION_ACTIVE, String.valueOf(version));
    try {
      tcResultSet form = this.modelFacade.findForms(filter);
      if (form.getRowCount() == 0) {
      }
      if (form.getRowCount() > 1) {
      }

      form.goToRow(0);
      ProcessForm processForm = new ProcessForm(key, version, form.getStringValue(FormDefinition.NAME), form.getStringValue(FormDefinition.DESCRIPTION));
      process.form(processForm);
      populate(processForm);
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Find the entity definition in Identity Manager database repository.
   **
   ** @param  form               the {@link ProcessForm} to populate from the
   **                            metadata repository of Identity Manager.
   **
   ** @throws ServiceException   if the assembler process fails
   */
  private void populate(final ProcessForm form)
    throws ServiceException {

    try {
      tcResultSet fields = this.modelFacade.getFormFields(form.key(), form.version());
      for (int j = 0; j < fields.getRowCount(); j++) {
        fields.goToRow(j);
        ProcessForm.Field field = form.createProcessField(
          fields.getLongValue(FormDefinition.COLUMN_KEY)
        , fields.getStringValue(FormDefinition.COLUMN_NAME)
        , fields.getStringValue(FormDefinition.COLUMN_LABEL)
        , fields.getStringValue(FormDefinition.COLUMN_TYPE)
        , fields.getStringValue(FormDefinition.COLUMN_VARIANT)
        , fields.getIntValue(FormDefinition.COLUMN_LENGTH)
        );
        form.addProcessField(field);
        populate(field);
      }
      tcResultSet forms = RequestRegistry.this.instanceFacade.getChildFormDefinition(form.key(), form.version());
      for (int j = 0; j < forms.getRowCount(); j++) {
        forms.goToRow(j);
        ProcessForm child = new ProcessForm(forms.getLongValue(FormDefinition.CHILD_KEY), forms.getIntValue(FormDefinition.CHILD_VERSION), forms.getStringValue(FormDefinition.NAME), forms.getStringValue(FormDefinition.DESCRIPTION));
        form.addProcessForm(child);
        populate(child);
      }
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Find the entity definition in Identity Manager database repository.
   **
   ** @param  field              the {@link ProcessForm.Field} to populate from
   **                            the metadata repository of Identity Manager.
   **
   ** @throws ServiceException   if the assembler process fails
   */
  private void populate(final ProcessForm.Field field)
    throws ServiceException {

    try {
      field.addProperty(ProcessForm.PROPERTY_REQUIRED, this.modelFacade.getFormFieldPropertyValue(field.key(), ProcessForm.PROPERTY_REQUIRED));
      field.addProperty(ProcessForm.PROPERTY_VISIBLE, this.modelFacade.getFormFieldPropertyValue(field.key(), ProcessForm.PROPERTY_VISIBLE));
      try {
        field.addProperty(ProcessForm.PROPERTY_LOOKUP_CODE, this.modelFacade.getFormFieldPropertyValue(field.key(), ProcessForm.PROPERTY_LOOKUP_CODE));
      }
      catch (tcPropertyNotFoundException e) {
        // intentionally left blank
        ;
      }
      try {
        field.addProperty(ProcessForm.PROPERTY_LOOKUP_TYPE, this.modelFacade.getFormFieldPropertyValue(field.key(), ProcessForm.PROPERTY_LOOKUP_TYPE));
      }
      catch (tcPropertyNotFoundException e) {
        // intentionally left blank
        ;
      }
      try {
        field.addProperty(ProcessForm.PROPERTY_LOOKUP_QUERY, this.modelFacade.getFormFieldPropertyValue(field.key(), ProcessForm.PROPERTY_LOOKUP_QUERY));
        field.addProperty(ProcessForm.PROPERTY_LOOKUP_DISPLAY, this.modelFacade.getFormFieldPropertyValue(field.key(), ProcessForm.PROPERTY_LOOKUP_DISPLAY));
        field.addProperty(ProcessForm.PROPERTY_LOOKUP_COLUMN, this.modelFacade.getFormFieldPropertyValue(field.key(), ProcessForm.PROPERTY_LOOKUP_COLUMN));
      }
      catch (tcPropertyNotFoundException e) {
        // intentionally left blank
        ;
      }
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }
}