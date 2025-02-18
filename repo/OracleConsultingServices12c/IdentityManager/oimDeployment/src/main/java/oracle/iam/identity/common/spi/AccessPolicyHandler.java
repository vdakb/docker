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

    File        :   AccessPolicyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.entitymgr.vo.EntityConstants;

import oracle.iam.identity.rolemgmt.api.RoleManager;

import oracle.iam.identity.usermgmt.api.UserManager;

import oracle.iam.request.vo.ApprovalConstants;

import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.accesspolicy.vo.Record;
import oracle.iam.accesspolicy.vo.ChildRecord;
import oracle.iam.accesspolicy.vo.DefaultData;
import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.accesspolicy.vo.AccessPolicyElement;

import oracle.iam.accesspolicy.api.AccessPolicyService;

import oracle.iam.accesspolicy.exception.AccessPolicyServiceException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.accesspolicy.vo.ChildAttribute;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessPolicyHandler</code> creates,deletes and configures an
 ** Access Policy instance in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicyHandler extends ApplicationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the policy instances create */
  private List<AccessPolicyInstance> multiple = new ArrayList<AccessPolicyInstance>();

  /**
   ** the business logic layer to operate on <code>Access Policy</code>
   ** instances
   */
  private AccessPolicyService policyService;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AccessPolicyHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public AccessPolicyHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the role in Identity Manager to
   **                            handle.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void name(final String name)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the
   **                            <code>Access Policy</code> instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            <code>Access Policy</code> instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            <code>Access Policy</code> instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link AccessPolicyInstance} to add.
   **
   ** @throws BuildException     if the specified {@link AccessPolicyInstance}
   **                            is already assigned to this task.
   */
  public void addInstance(final AccessPolicyInstance object)
    throws BuildException {

    // prevent bogus input
    if (this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {

    if (this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    for (AccessPolicyInstance instance : this.multiple)
      instance.validate(operation());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>Access Policy</code> instance of the Identity Manager
   ** through the discovered {@link AccessPolicyService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userService        = task.service(UserManager.class);
    this.roleService        = task.service(RoleManager.class);
    this.policyService      = task.service(AccessPolicyService.class);
    this.applicationService = task.service(ApplicationInstanceService.class);
    for (AccessPolicyInstance i : this.multiple)
      create(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>Access Policy</code> instance in Identity
   ** Manager through the discovered {@link AccessPolicyService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userService        = task.service(UserManager.class);
    this.roleService        = task.service(RoleManager.class);
    this.policyService      = task.service(AccessPolicyService.class);
    this.applicationService = task.service(ApplicationInstanceService.class);
    for (AccessPolicyInstance i : this.multiple)
      modify(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Access Policy</code> instance in Identity
   ** Manager through the discovered {@link AccessPolicyService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.policyService = task.service(AccessPolicyService.class);
    for (AccessPolicyInstance i : this.multiple)
      delete(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>IT Resource</code> instance in Identity Manager
   ** through the discovered {@link AccessPolicyService}.
   **
   ** @param  instance           the {@link ITResourceInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final AccessPolicyInstance instance)
    throws ServiceException {

    final String[] arguments = { AccessPolicy.ENTITY_TYPE, instance.name() };
    AccessPolicy   policy = lookupPolicy(instance);
    if (policy != null) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
      modify(policy, instance);
    }
    else {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
      // assumes that we hav all the time a owner assigned to this policy
      // (ensured by validation of the instance)
      // this is either a choosen identity by user or the default xelsysadm
      String owner = null;
      if (!StringUtility.isEmpty(instance.ownerName())) {
        switch (instance.ownerType().value()) {
          case USER: owner = lookupIdentity(instance.ownerName());
            break;
          case ROLE: owner = lookupRole(instance.ownerName());
            break;
        }
      }
      policy = new AccessPolicy(instance.name(), instance.description(), instance.retrofit(), instance.priority(), instance.ownerType().value(), owner);
      // extends the parameter mapping with the control flags and description
      policy.setAttribute(AccessPolicy.ATTRIBUTE.REQUEST.getID(), instance.request() ? "1" : "0");
      // first step assign all resources which are denied by the access policy
      // to create
      for (ProvisioningInstance resource : instance.denied()) {
        final ApplicationInstance application = lookupApplication(resource.name());
        if (resource.operation() == ServiceOperation.assign)
          policy.getPolicyElements().add(new AccessPolicyElement(application.getApplicationInstanceKey(), true, AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.REVOKE));
      }
      // second step assign all resources which are provisioned by the access
      // policy to create
      for (AccessPolicyInstance.Provision resource : instance.allow()) {
        final ApplicationInstance application = lookupApplication(resource.name());
        if (resource.operation() == ServiceOperation.assign) {
          final AccessPolicyElement element = new AccessPolicyElement(application.getApplicationInstanceKey(), false, resource.revoke() ? AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.REVOKE : AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.DISABLE);
          policy.getPolicyElements().add(element);
          // in a create operation no default data ca exists on the policy hence
          // the resetting previously defined defaullt data can safely be
          // ignored
          transferFormData(application, element.getDefaultData(), resource.dataSet(), false);
        }
      }

      try {
        this.policyService.createAccessPolicy(policy);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, arguments));
      }
      catch (AccessPolicyServiceException e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>Access Policy</code> instance in Identity
   ** Manager through the discovered {@link AccessPolicyService}.
   **
   ** @param  instance           the {@link AccessPolicyInstance} to configure.
   **
   ** @throws BuildException     in either the <code>Access Policy</code> does
   **                            not exists or an attribute is not defined on
   **                            the <code>Access Policy</code> definition.
   ** @throws ServiceException   in case an unhandled rror does occur.
   */
  public void modify(final AccessPolicyInstance instance)
    throws ServiceException {

    final String[]     arguments = { AccessPolicy.ENTITY_TYPE, instance.name() };
    final AccessPolicy policy = lookupPolicy(instance);
    if (policy == null) {
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    else {
      modify(policy, instance);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Access Policy</code> instance in Identity
   ** Manager through the discovered {@link AccessPolicyService}.
   **
   ** @param  instance           the {@link AccessPolicyInstance} to delete.
   **
   ** @throws BuildException     in either the <code>Access Policy</code> does
   **                            not exists or an attribute is not defined on
   **                            the <code>Access Policy</code> definition.
   ** @throws ServiceException   in case an unhandled rror does occur.
   */
  public void delete(final AccessPolicyInstance instance)
    throws ServiceException {

    final String[] arguments = { AccessPolicy.ENTITY_TYPE, instance.name() };
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));

    final AccessPolicy policy = lookupPolicy(instance);
    if (policy == null) {
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    else {
      try {
        this.policyService.deleteAccessPolicy(policy.getEntityId(), true);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>Access Policy</code> instance in Identity
   ** Manager through the discovered {@link AccessPolicyService}.
   **
   ** @param  policy             the {@link AccessPolicy} to modify.
   ** @param  instance           the {@link AccessPolicyInstance} providing the
   **                            data.
   **
   ** @throws BuildException     in either the <code>Access Policy</code> does
   **                            not exists or an attribute is not defined on
   **                            the <code>Access Policy</code> definition.
   ** @throws ServiceException   in case an unhandled rror does occur.
   */
  public void modify(final AccessPolicy policy, AccessPolicyInstance instance)
    throws ServiceException {

    final String[] arguments = { AccessPolicy.ENTITY_TYPE, instance.name() };
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));

    boolean changed = false;
    if (instance.priority() != -1L) {
      policy.setPriority(instance.priority());
      changed = true;
    }

    if (!StringUtility.isEmpty(instance.description()) && !StringUtility.isEqual(policy.getDescription(), instance.description())) {
      policy.setDescription(instance.description());
      changed = true;
    }

    if (!StringUtility.isEmpty(instance.ownerName())) {
      String owner = null;
      switch (instance.ownerType().value()) {
        case USER: owner = lookupIdentity(instance.ownerName());
          break;
        case ROLE: owner = lookupRole(instance.ownerName());
          break;
      }
      if (!StringUtility.isEmpty(owner) && policy.getOwnerType() != instance.ownerType().value() && !StringUtility.isEqual(policy.getOwnerId(), owner)) {
        policy.setOwnerType(instance.ownerType().value());
        policy.setOwnerId(owner);
        changed = true;
      }
    }

    // first step remove all resources which are denied and
    Iterator<ProvisioningInstance> denied = instance.denied().iterator();
    while (denied.hasNext()) {
      final ProvisioningInstance deny = denied.next();
      final ApplicationInstance  application = lookupApplication(deny.name());
      for (AccessPolicyElement element : policy.getPolicyElements()) {
        final String[] message = { ApprovalConstants.APP_INSTANCE_ENTITY, application.getApplicationInstanceName(), policy.getEntityType(), policy.getName() };
        if (element.isDenial() && element.getApplicationInstanceID() == application.getApplicationInstanceKey()) {
          if (deny.operation() != ServiceOperation.assign) {
            warning(FeatureResourceBundle.format(FeatureMessage.ACCESSPOLICY_DENY_REVOKE, message));
            element.markForDelete();
            changed = true;
          }
          else {
            warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, message));
          }
          denied.remove();
        }
      }
    }
    // second step assign all remaining resources as denied resources
    denied = instance.denied().iterator();
    while (denied.hasNext()) {
      final ProvisioningInstance deny = denied.next();
      final ApplicationInstance  application = lookupApplication(deny.name());
      final String[]             message = { ApprovalConstants.APP_INSTANCE_ENTITY, application.getApplicationInstanceName(), policy.getEntityType(), policy.getName() };
      warning(FeatureResourceBundle.format(FeatureMessage.ACCESSPOLICY_DENY_ASSIGN, message));
      policy.getPolicyElements().add(new AccessPolicyElement(application.getApplicationInstanceKey(), true, AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.REVOKE));
      changed = true;
    }

    // third step remove all resources which are no longer be provisioned
    Iterator<AccessPolicyInstance.Provision> allowed = instance.allow().iterator();
    while (allowed.hasNext()) {
      final AccessPolicyInstance.Provision allow = allowed.next();
      final ApplicationInstance            application = lookupApplication(allow.name());
      for (AccessPolicyElement element : policy.getPolicyElements()) {
        final String[] message = { ApprovalConstants.APP_INSTANCE_ENTITY, application.getApplicationInstanceName(), policy.getEntityType(), policy.getName() };
        if (!element.isDenial() && element.getApplicationInstanceID() == application.getApplicationInstanceKey()) {
          if (allow.operation() != ServiceOperation.assign) {
            warning(FeatureResourceBundle.format(FeatureMessage.ACCESSPOLICY_DENY_REVOKE, message));
            element.markForDelete();
            changed = true;
          }
          else {
            warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, message));
            changed = transferFormData(application, element.getDefaultData(), allow.dataSet(), allow.reset());
          }
          allowed.remove();
        }
      }
    }
    // forth step assign all remaining resources as allowed resources
    allowed = instance.allow().iterator();
    while (allowed.hasNext()) {
      final AccessPolicyInstance.Provision allow = allowed.next();
      final ApplicationInstance            application = lookupApplication(allow.name());
      final String[]                       message = { ApprovalConstants.APP_INSTANCE_ENTITY, application.getApplicationInstanceName(), policy.getEntityType(), policy.getName() };
      warning(FeatureResourceBundle.format(FeatureMessage.ACCESSPOLICY_ALLOW_ASSIGN, message));
      final AccessPolicyElement element = new AccessPolicyElement(application.getApplicationInstanceKey(), false, allow.revoke() ? AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.REVOKE : AccessPolicyElement.ACTION_IF_NOT_APPLICABLE.DISABLE);
      policy.getPolicyElements().add(element);
      // we can ignor the change indicator returned by the data transfer due to
      // the add operation superseeds any change detected in this method
      transferFormData(application, element.getDefaultData(), allow.dataSet(), allow.reset());
      changed = true;
    }

    if (changed) {
      try {
        this.policyService.updateAccessPolicy(policy);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, arguments));
      }
      catch (AccessPolicyServiceException e) {
        final String[] error = { AccessPolicy.ENTITY_TYPE, instance.name(), e.getLocalizedMessage() };
        if (failonerror())
          throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, error);
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, error));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
    else
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SKIPPED, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupPolicy
  /**
   ** Returns an existing <code>Access Policy</code> instance form Identity
   ** Manager through the discovered {@link AccessPolicyService}.
   **
   ** @param  instance           the {@link AccessPolicyInstance} to lookup.
   **
   ** @return                    the system identifier <code>EntityId</code> for
   **                            the specified {@link AccessPolicyInstance}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected AccessPolicy lookupPolicy(final AccessPolicyInstance instance)
    throws ServiceException {

    AccessPolicy policy = null;
    try {
      final SearchCriteria     criteria = new SearchCriteria(EntityConstants.NAME, instance.name(), SearchCriteria.Operator.EQUAL);
      final List<AccessPolicy> result = this.policyService.findAccessPolicies(criteria, null);
      if (result.size() > 1) {
        final String[] arguments = { AccessPolicy.ENTITY_TYPE, instance.name() };
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_AMBIGUOS, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, arguments));
      }
      if (result.size() > 0) {
        policy = result.get(0);
        policy = this.policyService.getAccessPolicy(policy.getEntityId(), true);
      }
    }
    catch (AccessPolicyServiceException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transferFormData
  /**
   ** Transfer the given data to the appropriate process form schema
   **
   ** @param  instance           the {@link ApplicationInstance} to provision.
   ** @param  account            the {@link Account} as target of the
   **                            provisioning operation.
   ** @param  request            the {@link RequestForm} providing the data to
   **                            transfer.
   ** @param  reset              <code>true</code> if in a modify operation all
   **                            previously defined default data on the effected
   **                            <code>Access Policy</code> are deleted and only
   **                            the default data contained in the data set are
   **                            applied as default data.
   **
   ** @return                    <code>true</code> if any change applied on the
   **                            default data set of the specified
   **                            {@link DefaultData}.
   */
  private boolean transferFormData(final ApplicationInstance instance, final DefaultData account, final RequestForm request, final boolean reset) {
    boolean changed = false;
    // prepare previously defined child data
    if (reset) {
      for (ChildRecord record : account.getChildData()) {
        for (ChildAttribute cursor : record.getAttributes()) {
          cursor.markForDelete();
          changed = true;
        }
      }
    }
    // prepare additional account data
    final FormInfo        form = instance.getAccountForm();
    final List<FormField> schema = form.getFormFields();
    if (request != null) {
      if (!CollectionUtility.empty(request.attribute())) {
        final List<Record> accountData = account.getData();
        for (RequestForm.Attribute attribute : request.attribute()) {
          final FormField field = fieldByLabel(schema, attribute.name());
          if (field != null) {
            final Record record = recordByName(accountData, field.getName());
            if (record != null) {
              switch (attribute.operation()) {
                case delete :
                case revoke : record.markForDelete();
                              break;
                default     : // relies on attribute validation
                              record.setAttributeValue(attribute.prefix() ? String.format(attribute.pattern(), instance.getItResourceKey(), attribute.value()) : attribute.value());
              }
            }
            else {
              // relies on attribute validation
              if (attribute.prefix()) {
                accountData.add(new Record(form.getFormKey(), field.getName(), String.format(attribute.pattern(), instance.getItResourceKey(), attribute.value())));
              }
              else {
                accountData.add(new Record(form.getFormKey(), field.getName(), attribute.value()));
              }
            }
            changed = true;
          }
          else {
            error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, "Account Attribute", attribute.name()));
          }
        }
      }
      if (!CollectionUtility.empty(request.dataSet())) {
        for (String subordinatedName : request.dataSet().keySet()) {
          final FormInfo subordinatedForm = formByName(instance.getChildForms(), subordinatedName);
          for (List<RequestForm.Attribute> collection : request.dataSet().get(subordinatedName)) {
            final Map<String, String> tupel = new HashMap<String, String>();
            for (RequestForm.Attribute attribute : collection) {
              final FormField field = fieldByLabel(subordinatedForm.getFormFields(), attribute.name());
              if (field != null) {
                // relies on attribute validation
                if (attribute.prefix()) {
                  tupel.put(field.getName(), String.format(attribute.pattern(), instance.getItResourceKey(), attribute.value()));
                }
                else {
                  tupel.put(field.getName(), attribute.value());
                }
              }
              else {
                error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, "Entitlement Attribute", attribute.name()));
              }
              account.addChildData(subordinatedForm.getName(), tupel);
              changed = true;
            }
          }
        }
      }
    }
    return changed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formByName
  /**
   ** This method gets the specific form details provided a form fieldname.
   **
   ** @param  schema             the value object class representing the process
   **                            form.
   ** @param  name               the process form name.
   **
   ** @return                    a {@link FormInfo} value object for a given
   **                            form name.
   */
  private FormInfo formByName(final List<FormInfo> schema, final String name) {
    FormInfo found = null;
    for (FormInfo cursor : schema) {
      if (cursor.getDescription().equalsIgnoreCase(name)) {
        found = cursor;
        break;
      }
    }
    return found;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldByLabel
  /**
   ** This method gets the specific form field details provided a form field
   ** label.
   **
   ** @param  schema             the value object class representing the process
   **                            form.
   ** @param  label              the process form field label.
   **
   ** @return                    a {@link FormField} value object for a given
   **                            form field label.
   */
  private FormField fieldByLabel(final List<FormField> schema, final String label) {
    FormField found = null;
    for (FormField cursor : schema) {
      if (cursor.getLabel().equals(label)) {
        found = cursor;
        break;
      }
    }
    return found;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   recordByName
  /**
   ** This method gets the specific data record details provided a form field
   ** name.
   **
   ** @param  schema             the value object class representing the process
   **                            form.
   ** @param  label              the process form field name.
   **
   ** @return                    a {@link Record} value object for a given
   **                            form field label.
   */
  private Record recordByName(final List<Record> schema, final String name) {
    Record found = null;
    for (Record cursor : schema) {
      if (cursor.getAttributeName().equals(name)) {
        found = cursor;
        break;
      }
    }
    return found;
  }
}