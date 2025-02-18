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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Framework

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Calendar;

import java.util.stream.Collectors;

import java.io.Serializable;

import org.xml.sax.InputSource;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;

import com.thortech.xl.util.adapters.tcUtilXellerateOperations;

import com.thortech.xl.dataaccess.tcDataProvider;

import java.text.ParseException;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MDSIOException;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ScriptContextBuilder;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.api.operations.CreateApiOp;
import org.identityconnectors.framework.api.operations.UpdateApiOp;
import org.identityconnectors.framework.api.operations.DeleteApiOp;
import org.identityconnectors.framework.api.operations.APIOperation;
import org.identityconnectors.framework.api.operations.ScriptOnConnectorApiOp;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractAdapterTask;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.FormDefinition;
import oracle.iam.identity.foundation.naming.ProcessInstance;

import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.connector.integration.TargetResource;
import oracle.iam.identity.connector.integration.ServerResource;
import oracle.iam.identity.connector.integration.FrameworkBundle;
import oracle.iam.identity.connector.integration.FrameworkMessage;

////////////////////////////////////////////////////////////////////////////////
// abstract class Provisioning
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Provisioning</code> implements the base functionality of a
 ** end point for the Identity Manager Adapter Factory which handles data
 ** delivered to a Identity Connector Server.
 ** <br>
 ** This is wrapper class has methods for account operations like create
 ** account, modify account, delete account etc.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public abstract class Provisioning extends AbstractAdapterTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the provisioning process and task */
  protected final Integer           processTask;
  protected final Long              processInstance;

  /** the abstraction layer to describe the connection to the connector server */
  protected ServerResource          endpoint;
  protected ConnectorFacade         connector;

  /**
   ** the mapping of attribute name and their transformation for provisioning
   ** belonging to multi-valued attributes
   */
  protected Pair<String, String>    element;

  /**
   ** the mapping of attribute name and their transformation for provisioning
   ** belonging to an account
   */
  protected Descriptor.Provisioning descriptor;

  protected TargetFeature           feature;
  protected TargetResource          resource;

  /**
   ** the value required ro rename an account where no system identifier is
   ** provided by the target system
   */
  protected String                  origin;

  /**
   ** the attribute value mapping how they are retrieved from th process form
   ** in Identity Manager
   */
  protected Map<String, Object>     data;

  //////////////////////////////////////////////////////////////////////////////
  // enum Action
  // ~~~~ ~~~~~~
  /**
   ** <code>Action</code> defines the actions which are applicable on
   ** multi-valued attributes.
   */
  public static enum Action {
      ASSIGN
    , REVOKE
    , UPDATE
    ,
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Provisioning</code> task adpater that allows use
   ** as a JavaBean.
   **
   ** @param  provider           the session provider connection
   **                            <br>
   **                            Allowed object is {@link tcDataProvider}.
   ** @param  processInstance    the Process Instance Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  processTask        the Process Task Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  loggerCategory     the category for the Logger.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected Provisioning(final tcDataProvider provider, final Long processInstance, final Integer processTask, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);

    // initialize instance attributes
    this.processInstance  = processInstance;
    this.processTask      = processTask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Creates the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   ** <p>
   ** This method is intendate to use in provisioning accounts and maps the
   ** account form to the target system attributes.
   **
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched here.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String createProcessData(final String path) {
    final String method = "createProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the task descriptor that provides the attribute mapping and
      // transformations to be applied on the mapped attributes
      unmarshal(path);
      this.data = fetchData(this.descriptor.attribute());
      // avoid any action if the data set is empty
      if (this.data == null || this.data.size() == 0)
        throw TaskException.abort(TaskBundle.string(TaskError.ATTRIBUTE_MAPPING_EMPTY));

      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel()) {
        debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(this.data)));
      }
      // apply the transformation rules if required
      if (this.descriptor.transformation()) {
        this.data = this.descriptor.transformer().transform(this.data);
        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel())
          debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, StringUtility.formatCollection(this.data)));
      }
      return SUCCESS;
    }
    catch (TaskException e) {
      return updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Creates the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   ** <p>
   ** This method is intendate to use in removing multi-valued attributes
   ** (entitlements) and maps the entitlement form to the target system
   ** attributes.
   **
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the target element section of the descriptor
   **                            specifying the attribute mapping definition of
   **                            fields and their transformation that are part
   **                            of a multi-value provisioning task.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the source element section of the descriptor
   **                            specifying the attribute mapping definition of
   **                            fields and their transformation that are part
   **                            of a multi-value provisioning task.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched here.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String createProcessData(final String path, final String type, final String name) {
    final String method = "createProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the task descriptor that provides the attribute mapping and
      // transformations to be applied on the mapped attributes
      unmarshal(path);
      this.element = Pair.of(type, name);
      final Descriptor reference = this.descriptor.reference.get(this.element);
      if (reference == null)
        throw TaskException.abort(TaskBundle.format(TaskError.METADATA_OBJECT_NOTFOUND, this.element));

      this.data = fetchDeleted(name, reference.attribute);
      // avoid any action if the data set is empty
      if (this.data == null || this.data.size() == 0)
        throw TaskException.abort(TaskBundle.string(TaskError.ATTRIBUTE_MAPPING_EMPTY));

      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel()) {
        debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(this.data)));
      }
      // apply the transformation rules if required
      if (reference.transformation()) {
        this.data = reference.transformer().transform(this.data);
        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel())
          debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, StringUtility.formatCollection(this.data)));
      }
      return SUCCESS;
    }
    catch (TaskException e) {
      return updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Creates the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   ** <p>
   ** This method is intendate to use in provisioning multi-valued attributes
   ** (entitlements) and maps the entitlement form to the target system
   ** attributes.
   **
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the target element section of the descriptor
   **                            specifying the attribute mapping definition of
   **                            fields and their transformation that are part
   **                            of a multi-value provisioning task.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the sourceelement section of the descriptor
   **                            specifying the attribute mapping definition of
   **                            fields and their transformation that are part
   **                            of a multi-value provisioning task.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the primary key of the process form to obtain
   **                            the data.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message.
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched here.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String createProcessData(final String path, final String type, final String name, final Long primary) {
    final String method = "createProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the task descriptor that provides the attribute mapping and
      // transformations to be applied on the mapped attributes
      unmarshal(path);
      this.element   = Pair.of(type, name);
      final Descriptor reference = this.descriptor.reference.get(this.element);
      if (reference == null)
        throw TaskException.abort(TaskBundle.format(TaskError.METADATA_OBJECT_NOTFOUND, this.element));

      this.data = fetchData(name, primary, reference.attribute);
      // avoid any action if the data set is empty
      if (this.data == null || this.data.size() == 0)
        throw TaskException.abort(TaskBundle.string(TaskError.ATTRIBUTE_MAPPING_EMPTY));

      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel()) {
        debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(this.data)));
      }
      // apply the transformation rules if required
      if (reference.transformation()) {
        this.data = reference.transformer.transform(this.data);
        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel())
          debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, StringUtility.formatCollection(this.data)));
      }
      return SUCCESS;
    }
    catch (TaskException e) {
      return updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntry
  /**
   ** Creates an entry of specified type in Service Provider, the values are
   ** taken from current Form.
   **
   ** @param  type               the entry type to create.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response code.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String createEntry(final ObjectClass type, final Long endpoint) {
    final String method = "createEntry";
    trace(method, SystemMessage.METHOD_ENTRY);
    // be optimistic that the task will succeeed
    String response = SUCCESS;
    try {
      configure(endpoint);

      final Set<Attribute>dataSet = DescriptorTransformer.build(this.descriptor, this.data);
      // avoid any action if the data set is empty
      if (dataSet == null || dataSet.size() == 0)
        throw TaskException.abort(TaskBundle.string(TaskError.ATTRIBUTE_MAPPING_EMPTY));

      final OperationOptions option  = operationBuilder(CreateApiOp.class).build();
      final OperationOptions script  = operationBuilder(ScriptOnConnectorApiOp.class).build();
      // execute before create action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.CREATE_BEFORE, dataSet, script);
      // execute create action
      final Uid uid = this.connector.create(type, dataSet, option);
      // writeback to the form if required
      updateField(this.descriptor.identifier(), uid.getUidValue());
      // execute after create action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.CREATE_AFTER, dataSet, script);
    }
    catch (ConnectorException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    catch (TaskException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAttribute
  /**
   ** Modifies one or more attributes of an entry of specified type in the
   ** <code>Service Provider</code>.
   **
   ** @param  type               the entry type to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  attribute          the name(s) of the attribute(s) to modify.
   **                            <br>
   **                            This is used as a filter to pick up only
   **                            special fields from the previously collected
   **                            attribute mapping.
   **                            <br>
   **                            <b>Note:</b>
   **                            The given String must separate the fields with
   **                            the <code>|</code> character.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched anywhere.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String modifyAttribute(final ObjectClass type, final Long endpoint, final String attribute) {
    // change all affected attributes specified by the attribute filter
    // Important: string split use regex hence escape correctly to get proper
    // result
    return modifyEntry(type, endpoint, DescriptorTransformer.build(this.descriptor, this.data), CollectionUtility.set(attribute.split("\\|")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAttribute
  /**
   ** Modifies one or more attributes of an entry of specified type in the
   ** <code>Service Provider</code>.
   **
   ** @param  type               the entry type to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  attribute          the name(s) and the values of the attribute(s)
   **                            to modify.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element consist as a {@link String} for the key
   **                            and an {@link Object} as its value.
   **
   ** @return                    an appropriate response message
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched anywhere.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String modifyAttribute(final ObjectClass type, final Long endpoint, final Map<String, Object> attribute) {
    // change all affected attributes specified by the attribute filter
    final String method = "modifyAttribute";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return modifyEntry(type, endpoint, DescriptorTransformer.build(this.descriptor, this.data), describeForm(attribute.keySet()));
    }
    catch (TaskException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      return updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activateEntry
  /**
   ** Activates an entry of specified type in the <code>Service Provider</code>.
   **
   ** @param  type               the type of the object to activate.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response code.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String activateEntry(final ObjectClass type, final Long endpoint) {
    return modifyEntry(type, endpoint, CollectionUtility.set(AttributeBuilder.buildEnabled(true)), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deactivateEntry
  /**
   ** Deactivates an entry of specified type in the
   ** <code>Service Provider</code>.
   **
   ** @param  type               the type of the object to deactivate.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response code.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String deactivateEntry(final ObjectClass type, final Long endpoint) {
    return modifyEntry(type, endpoint, CollectionUtility.set(AttributeBuilder.buildEnabled(false)), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unlockEntry
  /**
   ** Unlocks an entry of specified type in the <code>Service Provider</code>.
   **
   ** @param  type               the type of the object to lock.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response code.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String unlockEntry(final ObjectClass type, final Long endpoint) {
    return modifyEntry(type, endpoint, CollectionUtility.set(AttributeBuilder.buildLockOut(false)), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lockEntry
  /**
   ** Locks an entry of specified type in the <code>Service Provider</code>.
   **
   ** @param  type               the type of the object to lock.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response code.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String lockEntry(final ObjectClass type, final Long endpoint) {
    return modifyEntry(type, endpoint, CollectionUtility.set(AttributeBuilder.buildLockOut(true)), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyEntry
  /**
   ** Modifies an entry of specified type in the <code>Service Provider</code>.
   **
   ** @param  type               the entry type to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  dataSet            the attribute values for the entry type to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  filter             the name(s) of the attribute(s) to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an appropriate response message
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched anywhere.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String modifyEntry(final ObjectClass type, final Long endpoint, final Set<Attribute> dataSet, final Set<String> filter) {
    final String method = "modifyEntry";
    trace(method, SystemMessage.METHOD_ENTRY);
    // be optimistic that the task will succeeed
    String response = SUCCESS;
    try {
      configure(endpoint);

      // avoid any action if the dataSet is empty
      if (dataSet == null || dataSet.size() == 0)
        throw TaskException.abort(TaskBundle.string(TaskError.ATTRIBUTE_MAPPING_EMPTY));

      final Set<Attribute>   latch  = filter == null ? dataSet : dataSet.stream().filter(a -> filter.contains(a.getName())).collect(Collectors.toSet());
      final OperationOptions option = operationBuilder(UpdateApiOp.class).build();
      final OperationOptions script = operationBuilder(ScriptOnConnectorApiOp.class).build();

      // execute before modify action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.MODIFY_BEFORE, latch, script);
      // this method is intended to update account profiles hence the form is
      // populated and contains the identifiying attribute
      final String identifier = (String)this.data.get(Uid.NAME);
      final Uid    result     = this.connector.update(type, AttributeFactory.uid(fetchIdentifier()), latch, option);
      // execute after modify action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.MODIFY_AFTER, latch, script);
      // writeback the revieved uid only if its changed
      if (!StringUtility.isEqual(identifier, result.getUidValue()))
        updateField(this.descriptor.identifier(), result.getUidValue());
    }
    catch (ConnectorException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    catch (TaskException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteEntry
  /**
   ** Deletes object of specified type on target resource.
   **
   ** @param  type               the entry type to delete.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response code.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String deleteEntry(final ObjectClass type, final Long endpoint) {
    final String method = "deleteEntry";
    trace(method, SystemMessage.METHOD_ENTRY);
    // be optimistic that the task will succeeed
    String response = SUCCESS;
    try {
      configure(endpoint);

      final Uid              identifier = AttributeFactory.uid(fetchIdentifier());
      final Set<Attribute>   dataSet    = CollectionUtility.set(identifier);
      final OperationOptions option     = operationBuilder(DeleteApiOp.class).build();
      final OperationOptions script     = operationBuilder(ScriptOnConnectorApiOp.class).build();
      // execute before delete action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.DELETE_BEFORE, dataSet, script);
      // execute delete operation
      this.connector.delete(type, identifier, option);
      // execute after delete action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.DELETE_AFTER, dataSet, script);
    }
    catch (ConnectorException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    catch (TaskException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignPermission
  /**
   ** Modifies an entry of specified type in the <code>Service Provider</code>.
   **
   ** @param  type               the entry type to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched anywhere.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String assignPermission(final ObjectClass type, final Long endpoint) {
    final String method = "assignPermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return modifyEntry(Action.ASSIGN, type, endpoint);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokePermission
  /**
   ** Modifies an entry of specified type in the <code>Service Provider</code>.
   **
   ** @param  type               the entry type to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched anywhere.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String revokePermission(final ObjectClass type, final Long endpoint) {
    final String method = "revokePermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return modifyEntry(Action.REVOKE, type, endpoint);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyPermission
  /**
   ** Modifies an entry of specified type in the <code>Service Provider</code>.
   **
   ** @param  type               the entry type to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response message
   **                            <br>
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catched anywhere.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String modifyPermission(final ObjectClass type, final Long endpoint) {
    final String method = "updatePermission";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return modifyEntry(Action.UPDATE, type, endpoint);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyEntry
  /**
   ** Updates the object of specified type on target resource by adding the
   ** identifying attribute as a multi-valued attribute driven by a child table
   ** which was just added to that table.
   **
   ** @param  action             the action type for modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Action}.
   ** @param  type               the entry type to delete.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an appropriate response code.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String modifyEntry(final Action action, final ObjectClass type, final Long endpoint) {
    final String method = "modifyEntry";
    trace(method, SystemMessage.METHOD_ENTRY);
    // be optimistic that the task will succeeed
    String response = SUCCESS;
    try {
      configure(endpoint);

      final Set<Attribute> dataSet = DescriptorTransformer.build(DescriptorTransformer.objectClass(this.element.tag), this.descriptor.reference.get(this.element), this.data);
      // avoid any action if the dataSet is empty
      if (dataSet == null || dataSet.size() == 0)
        throw TaskException.abort(TaskBundle.string(TaskError.ATTRIBUTE_MAPPING_EMPTY));

      final OperationOptions option = operationBuilder(UpdateApiOp.class).build();
      final OperationOptions script = operationBuilder(ScriptOnConnectorApiOp.class).build();
      // execute before modify action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.MODIFY_BEFORE, dataSet, script);
      // execute modify operation
      final Uid identifier = AttributeFactory.uid(fetchIdentifier());
      switch(action) {
        case ASSIGN : this.connector.addAttributeValues(type,    identifier, dataSet, option);
                      break;
        case REVOKE : this.connector.removeAttributeValues(type, identifier, dataSet, option);
                      break;
        case UPDATE : this.connector.removeAttributeValues(type, identifier, dataSet, option);
                      this.connector.addAttributeValues(type,    identifier, dataSet, option);
                      break;
      }
      // execute after modify action
      // TODO: check if we can use the existing data mapping instead to convert
      //       it first to a set of attributes that will converted back to a map
      //       of attributes
      executeAction(Descriptor.Action.Phase.MODIFY_AFTER, dataSet, script);
    }
    catch (ConnectorException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    catch (TaskException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // update task to reflcet the erro ocurred in the UI
      response = updateTask(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationBuilder
  /**
   ** Callback method to create {@link OperationOptionsBuilder} for provided
   ** operation.
   ** <p>
   ** Form values are available in instance variable <code>data</code> that is
   ** populated before this method will be invoked.
   ** <p>
   ** The defautl implementation creates an empty
   ** {@link OperationOptionsBuilder} only. Subclasses should override the
   ** method if their is a need to customize the {@link OperationOptions} that
   ** will be passed to the connector.
   **
   ** @param  operation          the {@link APIOperation} for which the
   **                            operation options are provided, if
   **                            <code>null</code> all operation options
   **                            specified in Operation Options lookup will be
   **                            provided.
   **
   ** @return                    the appropriate {@link OperationOptionsBuilder}
   **                            instance.
   **                            Never be <code>null</code>.
   **                            <br>
   **                            Possible object
   **                            {@link OperationOptionsBuilder}.
   **
   ** @throws TaskException      if the {@link OperationOptionsBuilder} could
   **                            not be created properly; e.g. missing required
   **                            data.
   */
  protected OperationOptionsBuilder operationBuilder(final Class<? extends APIOperation> operation)
    throws TaskException {

    return new OperationOptionsBuilder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeAction
  /**
   ** Executes a certain action.
   **
   ** @param  phase              the phase in which the action to be executed.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link Descriptor.Action.Phase}.
   ** @param  parameter          the {@link Set} of {@link Attribute}s which are
   **                            passed to the connector as script parameters.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  option             the unique indentifier of the subject to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   */
  protected void executeAction(final Descriptor.Action.Phase phase, final Set<Attribute> parameter, final OperationOptions option) {
    final String method = "executeAction";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Descriptor.Action action = this.descriptor.action(phase);
    if (action == null) {
      warning(FrameworkBundle.format(FrameworkMessage.SCRIPT_ACTION_EMPTY, phase.id()));
      trace(method, SystemMessage.METHOD_EXIT);
      return;
    }
    // merge the script options
    final OperationOptionsBuilder builder = new OperationOptionsBuilder();
    if (option != null)
      builder.getOptions().putAll(option.getOptions());

    final OperationOptions actionOptions = operationOption(action.option());
    if (actionOptions != null) {
      builder.getOptions().putAll(actionOptions.getOptions());
    }

    info(FrameworkBundle.format(FrameworkMessage.SCRIPT_ACTION_START, phase.id(), action));
    final ScriptContextBuilder script = new ScriptContextBuilder(action.language().id(), action.command());
    script.addScriptArgument("attributes", AttributeUtil.toMap(parameter));
    script.addScriptArgument("timing", phase.id());
    try {
      switch (action.target()) {
        case CONNECTOR : this.connector.runScriptOnConnector(script.build(), builder.build());
                         break;
        case RESOURCE  : this.connector.runScriptOnResource(script.build(), builder.build());
                         break;
        default        : throw new IllegalStateException();
      }
    }
    catch (Exception e) {
      error(method, FrameworkBundle.format(FrameworkMessage.SCRIPT_ACTION_FAILED, phase.id(), action));
    }
    info(FrameworkBundle.format(FrameworkMessage.SCRIPT_ACTION_SUCCESS, phase.id(), action));
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from a path.
   **
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final String path)
    throws TaskException {

    final String method = "unmarshal";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create the task descriptor that provides the attribute mapping and
    // transformations to be applied on the mapped attributes
    this.descriptor = Descriptor.buildProvisioning(this);
    try {
      // prevent bogus input
      if (StringUtility.isEmpty(path))
        TaskException.argumentIsNull("path");

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      DescriptorFactory.configure(this.descriptor, document);
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Initalize the configuration capabilities.
   **
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void configure(final Long endpoint)
    throws TaskException {

    final String method = "configure";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      this.resource = populateResource(endpoint);
      if (!StringUtility.isEmpty(this.resource.connectorServer()))
        this.endpoint = ServerResource.build(this, this.resource.connectorServer());
      if (!StringUtility.isEmpty(this.resource.feature()))
        this.feature = populateFeature(this.resource.feature());

      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel()) {
        if (this.endpoint != null)
          debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.endpoint.toString()));
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.resource.toString()));
      }
      this.connector = BundleLocator.create(this.endpoint, this.resource, this.feature);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateResource
  /**
   ** Initalize the IT Resource capabilities.
   ** <p>
   ** The approach implementing this method should be:
   ** <ol>
   **   <li>If only  Application Name is configured as job parameter and
   **       IT Resource or Reconciliation Object is not configured, then
   **       IT Resource and Reconciliation Object will be fetched mapped with
   **       the given application Name
   **   <li>If IT Resource and Reconciliation Object are configured as job
   **       parameter then they gets priority even application name is also
   **       configured as job parameter
   **   <li>getParameter method logic handle the precedence logic of for cases
   **       mentioned in point #1 &amp; 2
   ** </ol>
   **
   ** @param  endpoint           the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    a {@link TargetResource} populated and
   **                            validated.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  protected abstract TargetResource populateResource(final Long endpoint)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateFeature
  /**
   ** Factory method to populate the <code>Metadata Descriptor</code> which
   ** is associated with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance may be populated manually.
   **
   ** @param  path               the path to the feature configration stored in
   **                            the Metadata Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>TargetFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is {@link TargetFeature}.
   **
   ** @throws TaskException      in case marshalling the
   **                            <code>Metadata Descriptor</code> fails.
   */
  protected final TargetFeature populateFeature(final String path)
    throws TaskException {

    final String method  = "populateFeature";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      if (document == null)
        throw new TaskException(TaskError.METADATA_OBJECT_NOTFOUND, path);

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      return populateFeature(document.read());
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
    catch (MDSIOException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateFeature
  /**
   ** Factory method to populate the <code>Metadata Descriptor</code> which
   ** is associated with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance may be populated manually.
   **
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    an instance of <code>TargetFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is {@link TargetFeature}.
   **
   ** @throws TaskException      in case marshalling the
   **                            <code>Metadata Descriptor</code> fails.
   */
  protected abstract TargetFeature populateFeature(final InputSource source)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTask
  /**
   ** Updates a the notes of a provisioning process instance
   ** <code>processInstance</code> with information provided by the given
   ** exception.
   **
   ** @param  e                  the exeception providing detailed information
   **                            about the error occured.
   **                            <br>
   **                            Allowed object is {@link TaskException};
   **
   ** @return                    if the exception has a well formatted message
   **                            the error code is obtained and returned;
   **                            otherwise {@link TaskError#UNHANDLED} is
   **                            returned.
   **                            <br>
   **                            Possible object is {@link String};
   */
  protected final String updateTask(final ConnectorException e) {
    // report the exception in the log spooled for further investigation
    // the exception thrown provides a special format in the message containing
    // a error code and a detailed text this message needs to be splitted by an
    // "::" character sequence
    final String[] message = e.getLocalizedMessage().split("::");
    if (message.length > 1) {
      // updating the task may also throw unrecoverable exceptions but cannot
      // be written to the task to avoid infinite loops on exception hence this
      // specific behavior can only be spooled to the log
      try {
        updateTask(message[0], message[1]);
      }
      catch (TaskException x) {
        fatal("updateTask", x);
      }
      return message[0];
    }
    else {
      // updating the task may also throw unrecoverable exceptions but cannot
      // be written to the task to avoid infinite loops on exception hence
      // this specific behavior can only be spooled to the log
      try {
        updateTask(TaskError.UNHANDLED, message[0]);
      }
      catch (TaskException x) {
        fatal("updateTask", x);
      }
      return TaskError.UNHANDLED;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTask
  /**
   ** Updates a the notes of a provisioning process instance
   ** <code>processInstance</code> with information provided by the given
   ** exception.
   **
   ** @param  e                  the exeception providing detailed information
   **                            about the error occured.
   **                            <br>
   **                            Allowed object is {@link TaskException};
   **
   ** @return                    the error code of the given exception.
   **                            <br>
   **                            Possible object is {@link String};
   */
  protected final String updateTask(final TaskException e) {
    // updating the task may also throw unrecoverable exceptions but cannot be
    // written to the task to avoid infinite loops on exception hence this
    // specific behavior can only be spooled to the log
    try {
      updateTask(e.code(), e.getLocalizedMessage());
    }
    catch (TaskException x) {
      fatal("updateTask", x);
    }
    return e.code();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchData
  /**
   ** Returns the source system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   **
   ** @param  mapping            the {@link Set} of attribute mappings of fields
   **                            that are part of a provisioning task.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element of type {@link Descriptor.Attribute}.
   **
   ** @return                    a {@link Map} containing the process data
   **                            mapped to the Itendity Manager attribute names.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element consist as a {@link String} for the key
   **                            and an {@link Object} as its value.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Map<String, Object> fetchData(final Set<Descriptor.Attribute> mapping)
    throws TaskException {

    final String method = "fetchData";
    trace(method, SystemMessage.METHOD_ENTRY);
    Map<String, Object> result = null;
    final tcFormInstanceOperationsIntf facade = formInstanceFacade();
    try {
      final tcResultSet formData = facade.getProcessFormData(this.processInstance);
      result = transfer(formData, mapping);
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchData
  /**
   ** Returns the source system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   **
   ** @param  name               the name of the process form the data are
   **                            comming from.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the primary key of the row the data are
   **                            comming from.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  reference          the {@link Descriptor} specifying the mapping
   **                            between source and target atributes that are
   **                            part of a provisioning task.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  mapping            the {@link Set} of attributes specifying the
   **                            UD_ column names of the process form that are
   **                            part of a provisioning task.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of {@link Descriptor.Attribute}.
   **
   ** @return                    a {@link Map} containing the process data
   **                            mapped to the Identity Manager attribute names.
   **                            Possible object is {@link Map} where each
   **                            element consist as a {@link String} for the key
   **                            and an {@link Object} as its value.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Map<String, Object> fetchData(final String name, final Long primary, final Set<Descriptor.Attribute> mapping)
    throws TaskException {

    final String method = "fetchData";
    trace(method, SystemMessage.METHOD_ENTRY);
    Map<String, Object>                result         = null;
    final tcFormInstanceOperationsIntf instanceFacade = formInstanceFacade();
    try {
      final long        definition = instanceFacade.getProcessFormDefinitionKey(this.processInstance);
      final tcResultSet multivalue = instanceFacade.getChildFormDefinition(definition, instanceFacade.getActiveVersion(definition));
      // for each form definition code check to see whether there is a
      // corresponding label in the lookup
      for (int i = 0; i < multivalue.getRowCount(); i++) {
        multivalue.goToRow(i);
        // take care the result set obtained provides only the raw column
        // names
        final String label = multivalue.getStringValue(FormDefinition.NAME);
        if (name.equals(label)) {
          // we have a hit so apply the column filter on the result set
          final tcResultSet formData = instanceFacade.getProcessFormChildData(multivalue.getLongValue(FormDefinition.CHILD_KEY), this.processInstance, primary);
          result = transfer(formData, mapping);
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      instanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchDeleted
  /**
   ** Returns the source system attributes and their corresponding values which
   ** was just deleted from the child form by the <code>processTask</code>
   ** passed to the constructor.
   ** <br>
   ** The deleted data exists in table OSI in Identity Manager hence we use
   ** <code>tcUtilXellerateOperations</code> and not form API as deleted is no
   ** more part of form.
   **
   ** @param  name               the name of the child form to fetch the deleted
   **                            data for.
   **
   ** @return                    a {@link Map} containing the deleted data
   **                            mapped to the Identity Manager attribute names.
   **                            Possible object is {@link Map} where each
   **                            element consist as a {@link String} for the key
   **                            and an {@link Object} as its value.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Map<String, Object> fetchDeleted(final String name, final Set<Descriptor.Attribute> mapping)
    throws TaskException {
    final String method = "fetchDeleted";
    trace(method, SystemMessage.METHOD_ENTRY);
    Map<String, Object>                result = null;
    final tcFormInstanceOperationsIntf facade = formInstanceFacade();
    try {
      final Long        definition = facade.getProcessFormDefinitionKey(this.processInstance);
      final tcResultSet resultSet  = facade.getChildFormDefinition(definition, facade.getActiveVersion(definition));
      for (int i = 0; i < resultSet.getRowCount(); i++) {
        // set position in result set
        resultSet.goToRow(i);
        // obtain the name of the form for comparison
        final String label = resultSet.getStringValue(FormDefinition.NAME);
        if (name.equals(label)) {
          // we have a hit
          final Long instance = resultSet.getLongValue(FormDefinition.CHILD_KEY);
          // TODO: looks realy ugly but the data are a String/Object mapping but
          //       the service invoked returns a String/String mapping
          result = transfer(tcUtilXellerateOperations.getDeletedProcessFormChildData(provider(), instance, this.processInstance, this.processTask), mapping);
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Returns the Identity Manager attributes and their corresponding values
   ** from a process form instance.
   ** <b>
   ** The returning result contains the column names of the process form mapped
   ** to the corresponding values. Only values will be returned that are mapped
   ** by the source attribute name of any {@link Descriptor.Attribute}.
   ** <br>
   ** If a {@link Descriptor.Attribute} is flagged as <code>entitlement</code>
   ** or <code>lookup</code> a potentialy prefixed <code>IT Resource</code> is
   ** removed from the value obtained from the process form.
   **
   ** @param  formData           the {@link tcResultSet} providing acces to a
   **                            certain record populated from a proecess from.
   **                            <br>
   **                            Allowed object is {@link tcResultSet}.
   ** @param  mapping            the {@link Set} of attributes specifying the
   **                            UD_ column names of the process form that are
   **                            part of a provisioning task.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of {@link Descriptor.Attribute}.
   **
   ** @return                    a {@link Map} containing the process data
   **                            mapped to the Identity Manager attribute names.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Map<String, Object> transfer(final tcResultSet formData, final Set<Descriptor.Attribute> mapping)
    throws TaskException {

    final String method = "transfer";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map<String, Object> targetData = new HashMap<String, Object>();
    try {
      // the result set can only have one row
      formData.goToRow(0);
      // gets the attributes that the reference descriptor is trying to
      // provision to the Target System by using the source of the attribute
      // name to obtain the value and put it in the mapping with the target
      // attribute id
      for (final Descriptor.Attribute cursor : mapping) {
        final String value = cursor.entitlement() ? Descriptor.unescapePrefix(formData.getStringValue(cursor.source)) : formData.getStringValue(cursor.source);
        switch(cursor.type) {
          case LONG     : targetData.put(cursor.source, Long.valueOf(value));
                          break;
          case DATE     : targetData.put(cursor.source, dateValue(value).getTime());
                          break;
          case CALENDAR : targetData.put(cursor.source, calendarValue(value).getTime());
                          break;
          case BOOLEAN  : targetData.put(cursor.source, booleanValue(value));
                          break;
          default       : targetData.put(cursor.source, value);
        }
      }
    }
    catch (Exception e) {
      throw TaskException.abort(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return targetData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describeForm
  /**
   ** Returns the source attributes that match the form fields.
   ** <br>
   ** Due to the provisioning descriptor describes also the child forms of an
   ** account form the match of the process attributes is based on the collumn
   ** names of the form; not on the labels.
   ** <br>
   ** Therefor each clned connector must provide its own metadata descriptor.
   **
   ** @param  source             the {@link Set} of attribute names of fields
   **                            that are part of a provisioning task.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element of type {@link String}.
   **
   ** @return                    a {@link Set} containing the attribute labels
   **                            of fields that are part of a provisioning task.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element of type {@link String}.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Set<String> describeForm(final Set<String> source)
    throws TaskException {

    final String method = "describeForm";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Set<String>                    processData      = new HashSet<String>();
    final tcFormDefinitionOperationsIntf definitionFacade = this.formDefinitionFacade();
    final tcFormInstanceOperationsIntf   instanceFacade   = this.formInstanceFacade();
    try {
      final long        definition = instanceFacade.getProcessFormDefinitionKey(this.processInstance);
      final tcResultSet schema     = definitionFacade.getFormFields(definition, instanceFacade.getActiveVersion(definition));
      // for each form field definition code check to see whether there is a
      // corresponding User Defined Field in the lookup
      for (int i = 0; i < schema.getRowCount(); i++) {
        // set the current position in the result set
        schema.goToRow(i);
        // if there is a match between Forms Definition Label and attribute
        // mapping id associate the Target System specific attribute with the
        // value of the Process Data
        final String name = schema.getStringValue(FormDefinition.COLUMN_NAME);
        if (source.contains(name)) {
          // match the source attribute using the descriptor
          for (Descriptor.Attribute cursor : this.descriptor.attribute())
            if (cursor.source.equals(name))
              processData.add(cursor.id);
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      definitionFacade.close();
      instanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return processData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Returns the Identity Manager attributes and their corresponding values
   ** from a process form instance.
   ** <b>
   ** The returning result contains the column names of the process form mapped
   ** to the corresponding values. Only values will be returned that are mapped
   ** by the source attribute name of any {@link Descriptor.Attribute}.
   ** <br>
   ** If a {@link Descriptor.Attribute} is flagged as <code>entitlement</code>
   ** or <code>lookup</code> a potentialy prefixed <code>IT Resource</code> is
   ** removed from the value obtained from the process form.
   **
   ** @param  formData           the {@link tcResultSet} providing acces to a
   **                            certain record populated from a proecess from.
   **                            <br>
   **                            Allowed object is {@link tcResultSet}.
   ** @param  mapping            the {@link Set} of attributes specifying the
   **                            UD_ column names of the process form that are
   **                            part of a provisioning task.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of {@link Descriptor.Attribute}.
   **
   ** @return                    a {@link Map} containing the process data
   **                            mapped to the Identity Manager attribute names.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Map<String, Object> transfer(final Map<String, String> formData, final Set<Descriptor.Attribute> mapping){
    final String method = "transfer";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Map<String, Object> targetData = new HashMap<String, Object>();
    // gets the attributes that the reference descriptor is trying to
    // provision to the Target System by using the source of the attribute
    // name to obtain the value and put it in the mapping with the target
    // attribute id
    for (final Descriptor.Attribute cursor : mapping) {
        final String value = cursor.entitlement() ? Descriptor.unescapePrefix(formData.get(cursor.source)) : formData.get(cursor.source);
        switch(cursor.type) {
          case LONG     : targetData.put(cursor.source, Long.valueOf(value));
                          break;
          case DATE     : targetData.put(cursor.source, dateValue(value));
                          break;
          case CALENDAR : targetData.put(cursor.source, calendarValue(value));
                          break;
          case BOOLEAN  : targetData.put(cursor.source, booleanValue(value));
                          break;
          default       : targetData.put(cursor.source, value);
        }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return targetData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchIdentifier
  /**
   ** Fetchs the identifier value of the account by its column name from the
   ** process form which belongs to the given process instance
   ** <code>processInstance</code>.
   **
   ** @return                     the value of the field to set.
   **                             <br>
   **                             Possible object is {@link String}.
   **
   ** @throws TaskException       if the operation fails.
   */
  private String fetchIdentifier()
    throws TaskException {

    // if a task sets a value for origin, it wants to indicate that the old
    // value is to be used for the identifier and that it is very likely to be
    // renamed
    if (this.origin != null)
      return this.origin;

    final String method = "fetchIdentifier";
    trace(method, SystemMessage.METHOD_ENTRY);
    final tcFormInstanceOperationsIntf facade = formInstanceFacade();
    try {
      final tcResultSet formData = facade.getProcessFormData(this.processInstance);
      formData.goToRow(0);
      return formData.getStringValue(this.descriptor.identifier());
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      facade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateField
  /**
   ** Updates a form field by its column name in the process form which belongs
   ** to the given process instance <code>processInstance</code>.
   **
   ** @param  name                the cloumn name of the field to update.
   **                             <br>
   **                             Allowed object is {@link String}.
   ** @param  value               the value of the field to set.
   **                             <br>
   **                             Allowed object is {@link String}.
   **
   ** @throws TaskException       if the operation fails.
   */
  private void updateField(final String name, final String value)
    throws TaskException {

    final tcFormInstanceOperationsIntf facade = formInstanceFacade();
    try {
      facade.setProcessFormData(this.processInstance, CollectionUtility.map(name, value));
    }
    catch (Exception e) {
      throw TaskException.unhandled(e);
    }
    finally {
      facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTask
  /**
   ** Updates a the notes of a provisioning process instance
   ** <code>processInstance</code>.
   **
   ** @param  reason              the reason (aka code) of the error occured.
   **                             <br>
   **                             Allowed object is {@link String}.
   ** @param  error               the detailed description of the error key
   **                             word.
   **                             <br>
   **                             Allowed object is {@link String}.
   **
   ** @throws TaskException      if the operation fails.
   */
  private void updateTask(final String reason, final String error)
    throws TaskException {

    final long                         task   = this.processTask.longValue();
    final tcProvisioningOperationsIntf facade = service(tcProvisioningOperationsIntf.class);
    // add note and error details to task
    try {
      final tcResultSet detail = facade.getProvisioningTaskDetails(task);
      facade.updateTask(task, detail.getByteArrayValue(ProcessInstance.VERSION), CollectionUtility.map(new String[]{ProcessInstance.REASON, ProcessInstance.ERROR}, new String[]{reason, error}));
    }
    catch (Exception e) {
      throw TaskException.unhandled(e);
    }
    finally {
      facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationOption
  /**
   ** Creates {@link OperationOptions} belonging to an {@link Action}.
   **
   ** @param  option             the {@link Action} options to set.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   */
  private OperationOptions operationOption(final List<Pair<String, Serializable>> option) {
    // prevent bogus input
    if (option == null) {
      return null;
    }
    final OperationOptionsBuilder builder = new OperationOptionsBuilder();
    for (Pair<String, Serializable> cursor : option) {
      builder.setOption(cursor.tag, cursor.value);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Transform a string to a boolean value.
   **
   ** @param  value              the string representation of a boolean value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the boolean value of the string representation.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  private Boolean booleanValue(final String value) {
    return "1".equals(value) ? Boolean.TRUE : "true".equalsIgnoreCase(value) ? Boolean.TRUE : Boolean.FALSE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Transform a string to a date value.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The string is excpacted in the internal format of Identity Governance.
   **
   ** @param  value              the string representation of a date value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the date value of the string representation.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  private Date dateValue(final String value) {
    return DateUtility.getDate(calendarValue(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   calendarValue
  /**
   ** Transform a string to a calendar value.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The string is excpacted in the internal format of Identity Governance.
   **
   ** @param  value              the string representation of a calendar value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the calendar value of the string
   **                            representation.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  private Calendar calendarValue(final String value) {
    try {
      return DateUtility.parseDate(value, DateUtility.ISO8601);
    }
    catch (ParseException e) {
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(DateUtility.now());
      return calendar;
    }
  }
}