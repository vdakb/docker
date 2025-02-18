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

    File        :   ApplicationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.iam.platform.authopss.vo.EntityPublication;
import oracle.iam.platform.authopss.vo.AppInstancePublication;

import oracle.iam.platform.authopss.api.PolicyConstants;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.usermgmt.api.UserManager;

import oracle.iam.platformservice.api.EntityPublicationService;

import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.FormInfo;
import oracle.iam.provisioning.vo.FormField;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ChildTableRecord;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

import oracle.iam.catalog.vo.Catalog;

import oracle.iam.catalog.api.CatalogService;

import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.ApprovalConstants;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;

import oracle.iam.request.api.RequestService;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>ApplicationHandler</code> creates,deletes and configures an
 ** application instance in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ApplicationHandler extends EntitlementHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the business logic layer to operate on <code>Application Instance</code>s
   */
  protected ApplicationInstanceService applicationService;

  /**
   ** the business logic layer to provision <code>Application Instance</code>s
   */
  protected ProvisioningService provisioningService;

  /**
   ** the business logic layer to operate on <code>Resource Object</code>s
   */
  protected tcObjectOperationsIntf resourceService;

  /**
   ** the business logic layer to operate on <code>IT Resource</code>s
   */
  protected tcITResourceInstanceOperationsIntf endpointService;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ApplicationHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ApplicationHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend, PolicyConstants.Resources.APPLICATION_INSTANCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Sets the <code>Resource Object</code> an <code>Application Instance</code>
   ** belongs to in Identity Manager.
   **
   ** @param  resource           the <code>Resource Object</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void resource(final String resource) {
    if (this.single == null)
      this.single = new ApplicationData();

    ((ApplicationData)this.single).resource(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Called to inject the <code>IT Resource</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @param  server             the <code>IT Resource</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void server(final String server) {
    if (this.single == null)
      this.single = new ApplicationData();

    ((ApplicationData)this.single).server(server);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSet
  /**
   ** Called to inject the <code>Request DataSet</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @param  dataSet            the <code>Request DataSet</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void dataSet(final String dataSet) {
    if (this.single == null)
      this.single = new ApplicationData();

    ((ApplicationData)this.single).dataSet(dataSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (overridden)
  /**
   ** Creates a new <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.endpointService     = task.service(tcITResourceInstanceOperationsIntf.class);
    this.resourceService     = task.service(tcObjectOperationsIntf.class);
    this.publisherService    = task.service(EntityPublicationService.class);
    this.applicationService  = task.service(ApplicationInstanceService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null) {
      final ApplicationData single = (ApplicationData)this.single;
      create(single);
      // at first we revoke the enablements ...
      disable(single);
      // ... and adding the new enablements afterwards
      enable(single);
    }

    for (CatalogElement cursor : this.multiple) {
      final ApplicationData data = (ApplicationData)cursor;
      create(data);
      // at first we revoke the enablements ...
      disable(data);
      // ... and adding the new enablements afterwards
      enable(data);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (overridden)
  /**
   ** Mofifies an existing <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.userService         = task.service(UserManager.class);
    this.catalogService      = task.service(CatalogService.class);
    this.requestService      = task.service(RequestService.class);
    this.endpointService     = task.service(tcITResourceInstanceOperationsIntf.class);
    this.resourceService     = task.service(tcObjectOperationsIntf.class);
    this.publisherService    = task.service(EntityPublicationService.class);
    this.applicationService  = task.service(ApplicationInstanceService.class);
    this.provisioningService = task.service(ProvisioningService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null) {
      final ApplicationData single = (ApplicationData)this.single;
      modify(single);
      // at first we revoke the entity publications ...
      unpublish(single);
      // ... and adding the new entity publications afterwards
      publish(single, single.entitlement());
    }

    for (CatalogElement cursor : this.multiple) {
      final ApplicationData data = (ApplicationData)cursor;
      modify(data);
      // at first we revoke the entity publications ...
      unpublish(data);
      // ... and adding the new entity publications afterwards
      publish(data, data.entitlement());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (overridden)
  /**
   ** Deletes an existing <code>Application Instance</code> from Identity
   ** Manager through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.publisherService    = task.service(EntityPublicationService.class);
    this.applicationService  = task.service(ApplicationInstanceService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null)
      delete((ApplicationData)this.single);

    for (CatalogElement cursor : this.multiple)
      delete((ApplicationData)cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an new <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  data               the {@link ApplicationData} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final ApplicationData data)
    throws ServiceException {

    if (exists(data)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ApprovalConstants.APP_INSTANCE_ENTITY, data.name()));
      modify(data);
    }
    else {
      // signature of the contructor of an ApplicationInstance requires a
      // parameter "uiFragmentName" that's never used in the value holder at
      // all hence we are passing a "dummy" to fullfill the requirements on
      // create an instance of ApplicationInstance
      ApplicationInstance instance = new ApplicationInstance();
      if (createInstance(instance, data))
        try {
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, ApprovalConstants.APP_INSTANCE_ENTITY, data.name()));
          instance = this.applicationService.addApplicationInstance(instance);
          // fetch through the ID if the ceated application instance to keep
          // further processes in sync
          data.entityID(String.valueOf(instance.getApplicationInstanceKey()));
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, ApprovalConstants.APP_INSTANCE_ENTITY, data.name()));
        }
        catch (Exception e) {
          throw new ServiceException(ServiceError.UNHANDLED, e);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Application Instance</code> in Identity
   ** Manager through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  instance           the {@link ApplicationData} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final ApplicationData instance)
    throws ServiceException {

    final ApplicationInstance entityID = entityID(instance);
    if (entityID != null) {
      try {
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, ApprovalConstants.APP_INSTANCE_ENTITY, instance.name()));
        this.applicationService.deleteApplicationInstance(entityID.getApplicationInstanceKey());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, ApprovalConstants.APP_INSTANCE_ENTITY, instance.name()));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
    else {
      final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify

  /**
   ** Modifies an existing <code>Application Instance</code> in Identity
   ** Manager through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  data           the {@link ApplicationData} to modify.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final ApplicationData data)
    throws ServiceException {

    ApplicationInstance instance = entityID(data);
    if (instance != null) {
      if (createInstance(instance, data)) {
        try {
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, ApprovalConstants.APP_INSTANCE_ENTITY, data.name()));
          instance = this.applicationService.updateApplicationInstance(instance);
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, ApprovalConstants.APP_INSTANCE_ENTITY, data.name()));
        }
        catch (Exception e) {
          throw new ServiceException(ServiceError.UNHANDLED, e);
        }
      }
      // perform catalog update
      if (!CollectionUtility.empty(data.parameter()))
        super.modify(data);
      // perform provisioning operations if required
      if (!CollectionUtility.empty(data.direct())) {
        provision(instance, data.direct());
      }
      // perform provisioning operations if required
      if (!CollectionUtility.empty(data.request())) {
        final Catalog catalog = lookup(data);
        if (catalog != null) {
          request(catalog, data.request());
        }
      }
    }
    else {
      final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, data.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link ApplicationData} exists in Identity
   ** Manager through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  instance           the {@link ApplicationData} to check
   **                            for existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link ApplicationData} exists in the backend
   **                            system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final ApplicationData instance)
    throws ServiceException {

    return entityID(instance) != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityID
  /**
   ** Returns an existing {@link ApplicationInstance} from Identity Manager
   ** through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  data               the {@link ApplicationData} providing the name
   **                            of the {@link ApplicationInstance} to return.
   **
   ** @return                    the {@link ApplicationInstance} for
   **                            <code>applicationName</code> or
   **                            <code>null</code> if the specified
   **                            <code>applicationName</code> does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public ApplicationInstance entityID(final ApplicationData data)
    throws ServiceException {

    final ApplicationInstance instance = lookupApplication(data.name());
    data.entityID(String.valueOf(instance.getApplicationInstanceKey()));
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPublication (overridden)
  /**
   ** Returns an {@link EntityPublication} to be passed to publication service
   ** method of Identity Manager through the discovered
   ** {@link EntityPublicationService}.
   **
   ** @param  scope              the details of an {@link Organization}.
   ** @param  instance           the {@link EntitlementData} specifying
   **                            the date.
   ** @param  hierarchy          <code>true</code> if the publication is
   **                            hierarchy aware; otherwise <code>false</code>.
   **
   ** @return                    the proper {@link EntityPublication}.
   */
  @Override
  protected EntityPublication createPublication(final Organization scope, final EntitlementData instance, final boolean hierarchy) {
    final AppInstancePublication publication = new AppInstancePublication();
    publication.setEntityId(instance.entityID());
    publication.setEntityType(this.type.getId());
    publication.setEntityIdAsLong(Long.valueOf(instance.entityID()));
    publication.setScopeId(scope.getEntityId());
    publication.setScopeIdAsLong(Long.valueOf(scope.getEntityId()));
    publication.setScopeName((String)scope.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()));
    publication.setHierarchicalScope(hierarchy);
    return publication;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildElement (overridden)

  /**
   ** Factory method to create a <code>Catalog Element</code>.
   **
   ** @return                    an {@link ApplicationData} instance.
   */
  @Override
  protected CatalogElement buildElement() {
    return new ApplicationData();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provision
  /**
   ** Provisions an <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ProvisioningService} to beneficiaries.
   **
   ** @param  instance           the {@link ApplicationInstance} to provision to
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  identity           the <code>Beneficiaries</code> as targets of
   **                            the provisioning operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void provision(final ApplicationInstance instance, final List<ProvisioningInstance> identity)
    throws ServiceException {

    for (ProvisioningInstance cursor : identity) {
      // perfom provisioning if the current instance is requesting the operation
      switch (cursor.operation()) {
        case create : accountCreate(instance, cursor);
                      break;
        case modify : accountModify(instance, cursor);
                      break;
        case revoke : accountRevoke(instance, cursor);
                      break;
        default     : error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, cursor.operation().id(), ApprovalConstants.APP_INSTANCE_ENTITY));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreate
  /**
   ** Provisions an <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ProvisioningService} to beneficiaries.
   **
   ** @param  instance           the {@link ApplicationInstance} to provision to
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  beneficiary        the <code>Beneficiary</code> as target of
   **                            the provisioning operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void accountCreate(final ApplicationInstance instance, final ProvisioningInstance beneficiary)
    throws ServiceException {

    // validate beneficiary
    String identity = null;
    try {
      identity = lookupIdentity(beneficiary.name());
    }
    catch (BuildException e) {
      final String[] error = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.getApplicationInstanceName(), ApprovalConstants.USER_ENTITY, beneficiary.name(), e.getLocalizedMessage() };
      error(FeatureResourceBundle.format(FeatureError.PROVISION_ASSIGN_FAILED, error));
      return;
    }
    accountCreate(instance, identity, beneficiary.name(), beneficiary.dataSet());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreate
  /**
   ** Provisions an <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ProvisioningService} to beneficiaries.
   **
   ** @param  instance           the {@link ApplicationInstance} to provision to
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  identity           the system identifier <code>EntityId</code> for
   **                            the specified <code>Beneficiary</code>.
   ** @param  beneficiary        the <code>Beneficiary</code> as target of
   **                            the provisioning operation.
   ** @param  request            the {@link RequestForm} providing the data to
   **                            transfer.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void accountCreate(final ApplicationInstance instance, final String identity, final String beneficiary, final RequestForm request)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.getApplicationInstanceName(), ApprovalConstants.USER_ENTITY, beneficiary };
    if (primaryAccountExists(instance, identity)) {
      error(FeatureResourceBundle.format(FeatureError.ACCOUNT_EXISTS, arguments));
      accountModify(instance, identity, beneficiary, request);
    }
    else {
      info(FeatureResourceBundle.format(FeatureMessage.PROVISION_ASSIGN_BEGIN, arguments));
      final Map<String, Object> accountData = new HashMap<String, Object>();
      final Account             account = new Account(instance, new AccountData(String.valueOf(instance.getAccountForm().getFormKey()), null, accountData));
      // set account type
      account.setAccountType(Account.ACCOUNT_TYPE.Primary);
      account.setAccountPasswordEncrypted(true);
      transferFormData(instance, account, request);
      /*
      // determine which of the account attributes is the ITResource used for
      // provisioning
      final FormInfo            form        = instance.getAccountForm();
      final List<FormField>     field       = form.getFormFields();
      for (FormField cursor : field) {
        if (cursor.getProperty("ITResource") == null)
          continue;
        accountData.put(cursor.getName(), instance.getItResourceKey());
        break;
      }

      // prepare additional account data
      if (accountForm != null) {
        if (!CollectionUtility.empty(accountForm.attribute())) {
          for (RequestForm.Attribute attribute : accountForm.attribute()) {
            accountData.put(attribute.name(), attribute.value());
          }
        }
        if (!CollectionUtility.empty(accountForm.dataSet())) {
          final Map<String, ArrayList<ChildTableRecord>> subordinatedData = new HashMap<String, ArrayList<ChildTableRecord>>();
          for (String subordinatedName : accountForm.dataSet().keySet()) {
          final ArrayList<ChildTableRecord> xxxxx = new ArrayList<ChildTableRecord>();
          for (List<RequestForm.Attribute> collection : accountForm.dataSet().get(subordinatedName)) {
            final Map<String, Object> tupel  = new HashMap<String, Object>();
            for (RequestForm.Attribute attribute : collection) {
              tupel.put(attribute.name(), attribute.value());
            }
            final ChildTableRecord record = new ChildTableRecord();
            record.setAction(ChildTableRecord.ACTION.Add);
            record.setChildData(tupel);
            xxxxx.add(record);
           }
           // put form name and associated data
           subordinatedData.put(subordinatedName, xxxxx);
          }
          account.getAccountData().setChildData(subordinatedData);
        }
      }
*/
      try {
        this.provisioningService.provision(identity, account);
        info(FeatureResourceBundle.format(FeatureMessage.PROVISION_ASSIGN_SUCCESS, arguments));
      }
      catch (Exception e) {
        final String[] error = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.getApplicationInstanceName(), ApprovalConstants.USER_ENTITY, beneficiary, e.getLocalizedMessage() };
        if (failonerror())
          throw new FeatureException(FeatureError.PROVISION_ASSIGN_FAILED, error);
        else
          error(FeatureResourceBundle.format(FeatureError.PROVISION_ASSIGN_FAILED, error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRevoke
  /**
   ** De-Provisions an <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ProvisioningService} from beneficiaries.
   **
   ** @param  instance           the {@link ApplicationInstance} to revoke from
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  beneficiary        the <code>Beneficiary</code> as target of
   **                            the revoke operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void accountRevoke(final ApplicationInstance instance, final ProvisioningInstance beneficiary)
    throws ServiceException {

    // validate beneficiary
    String identity = null;
    try {
      identity = lookupIdentity(beneficiary.name());
    }
    catch (BuildException e) {
      final String[] error = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.getApplicationInstanceName(), ApprovalConstants.USER_ENTITY, beneficiary.name(), e.getLocalizedMessage() };
      error(FeatureResourceBundle.format(FeatureError.PROVISION_REVOKE_FAILED, error));
      return;
    }
    accountRevoke(instance, identity, beneficiary.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountRevoke
  /**
   ** De-Provisions an <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link ProvisioningService} from beneficiaries.
   **
   ** @param  instance           the {@link ApplicationInstance} to revoke from
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  identity           the <code>Beneficiary</code> as target of
   **                            the revoke operation.
   ** @param  beneficiary        the <code>Beneficiary</code> as target of
   **                            the revoke operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void accountRevoke(final ApplicationInstance instance, final String identity, final String beneficiary)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.getApplicationInstanceName(), ApprovalConstants.USER_ENTITY, beneficiary };
    final Account  primary = primaryAccountLookup(instance, identity, true);
    if (primary != null) {
      warning(FeatureResourceBundle.format(FeatureMessage.PROVISION_REVOKE_BEGIN, arguments));
      warning(FeatureResourceBundle.format(FeatureMessage.PROVISION_REVOKE_SUCCESS, arguments));
    }
    else {
      if (failonerror())
        throw new FeatureException(FeatureError.ACCOUNT_NOTEXISTS, arguments);
      else
        error(FeatureResourceBundle.format(FeatureError.ACCOUNT_NOTEXISTS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   ** Modifies an <code>Application Instance</code> account in Identity Manager
   ** through the discovered {@link ProvisioningService}.
   **
   ** @param  instance           the {@link ApplicationInstance} to modify for
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  beneficiary        the <code>Beneficiary</code> as target of
   **                            the revoke operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void accountModify(final ApplicationInstance instance, final ProvisioningInstance beneficiary)
    throws ServiceException {

    // validate beneficiary
    String identity = null;
    try {
      identity = lookupIdentity(beneficiary.name());
    }
    catch (BuildException e) {
      final String[] error = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.getApplicationInstanceName(), ApprovalConstants.USER_ENTITY, beneficiary.name(), e.getLocalizedMessage() };
      error(FeatureResourceBundle.format(FeatureError.PROVISION_MODIFY_FAILED, error));
      return;
    }
    accountModify(instance, identity, beneficiary.name(), beneficiary.dataSet());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   ** Modifies an <code>Application Instance</code> account in Identity Manager
   ** through the discovered {@link ProvisioningService}.
   **
   **
   ** @param  instance           the {@link ApplicationInstance} to provision to
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  identity           the system identifier <code>EntityId</code> for
   **                            the specified <code>Beneficiary</code>.
   ** @param  beneficiary        the <code>Beneficiary</code> as target of
   **                            the provisioning operation.
   ** @param  request            the {@link RequestForm} providing the data to
   **                            transfer.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void accountModify(final ApplicationInstance instance, final String identity, final String beneficiary, final RequestForm request)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, instance.getApplicationInstanceName(), ApprovalConstants.USER_ENTITY, beneficiary };
    final Account  primary = primaryAccountLookup(instance, identity, true);
    if (primary != null) {
      // verify if is something to provision
      if (request == null) {
        warning(FeatureResourceBundle.format(FeatureMessage.PROVISION_MODIFY_SKIPPED, arguments));
      }
      else {
        info(FeatureResourceBundle.format(FeatureMessage.PROVISION_MODIFY_BEGIN, arguments));
        transferFormData(instance, primary, request);
        /*
        // prepare account form
        final Map<String, Object> accountData  = primary.getAccountData().getData();
        if (!CollectionUtility.empty(accountForm.attribute())) {
          for (RequestForm.Attribute attribute : accountForm.attribute()) {
            accountData.put(attribute.name(), attribute.value());
          }
        }

        if (!CollectionUtility.empty(accountForm.dataSet())) {
          final Map<String, ArrayList<ChildTableRecord>> subordinatedData = new HashMap<String, ArrayList<ChildTableRecord>>();
          for (String subordinatedName : accountForm.dataSet().keySet()) {
            final ArrayList<ChildTableRecord> xxxxx = new ArrayList<ChildTableRecord>();
            for (List<RequestForm.Attribute> collection : accountForm.dataSet().get(subordinatedName)) {
              final Map<String, Object> tupel  = new HashMap<String, Object>();
              for (RequestForm.Attribute attribute : collection) {
                tupel.put(attribute.name(), attribute.value());
              }
              final ChildTableRecord record = new ChildTableRecord();
              record.setAction(ChildTableRecord.ACTION.Add);
              record.setChildData(tupel);
              xxxxx.add(record);
            }
            // put form name and associated data
            subordinatedData.put(subordinatedName, xxxxx);
          }
          primary.getAccountData().setChildData(subordinatedData);
        }
*/
        try {
          this.provisioningService.modify(primary);
        }
        catch (Exception e) {
          throw new ServiceException(ServiceError.UNHANDLED, e);
        }
        info(FeatureResourceBundle.format(FeatureMessage.PROVISION_MODIFY_SUCCESS, arguments));
      }
    }
    else {
      if (failonerror())
        throw new FeatureException(FeatureError.ACCOUNT_NOTEXISTS, arguments);
      else
        error(FeatureResourceBundle.format(FeatureError.ACCOUNT_NOTEXISTS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryAccountExists
  /**
   ** Checks if the specified {@link ApplicationData} exists in Identity
   ** Manager through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  instance           the {@link ApplicationInstance} to
   **                            check for account existance.
   ** @param  identity           the system identifier <code>EntityId</code> for
   **                            the specified <code>Beneficiary</code> as
   **                            subject of the check operation.
   **
   ** @return                    <code>true</code> if the primary account
   **                            exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean primaryAccountExists(final ApplicationInstance instance, final String identity)
    throws ServiceException {

    final Account account = primaryAccountLookup(instance, identity, false);
    return account != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryAccountLookup
  /**
   ** Returns the primary {@link Account} for an {@link ApplicationInstance} and
   ** the identity (aka User) through the discovered
   ** {@link ApplicationInstanceService}.
   **
   ** @param  instance           the {@link ApplicationInstance} to
   **                            check for account existance.
   ** @param  identity           the system identifier <code>EntityId</code> for
   **                            the specified <code>Beneficiary</code> as
   **                            subject of the check operation.
   ** @param  populate           <code>true</code> to indicate if account data
   **                            should be populated in the returned
   **                            {@link Account} instance. If set to
   **                            <code>false</code>, account data will not be
   **                            populated.
   **
   ** @return                    the {@link Account}; may be <code>null</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public Account primaryAccountLookup(final ApplicationInstance instance, final String identity, final boolean populate)
    throws ServiceException {

    // prevent bogus state
    if (this.provisioningService == null)
      throw new ServiceException(ServiceError.ABORT, "provisioningService is null");

    // the identity's application accounts for a specific application instance
    final SearchCriteria criteria = new SearchCriteria(
      new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.APPINST_ID.getId(),   instance.getApplicationInstanceKey(), SearchCriteria.Operator.EQUAL)
    , new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_TYPE.getId(), Account.ACCOUNT_TYPE.Primary.getId(), SearchCriteria.Operator.EQUAL)
    , SearchCriteria.Operator.AND
    );

    Account account = null;
    try {
      final List<Account> result = this.provisioningService.getAccountsProvisionedToUser(identity, criteria, null, populate);
      if (result.size() > 1)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, "Primary Account", instance.getApplicationInstanceName()));
      else
        account = (result == null || result.size() == 0) ? null : result.get(0);
    }
    catch (Exception e) {
      error(e.getLocalizedMessage());
    }
    return account;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Requests an <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link RequestService} for beneficiaries.
   **
   ** @param  catalog            the {@link Catalog} item to request.
   ** @param  identity           the <code>Beneficiaries</code> as targets of
   **                            the request operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void request(final Catalog catalog, final List<ProvisioningInstance> identity)
    throws ServiceException {

    for (ProvisioningInstance cursor : identity) {
      // perfom provisioning if the current instance is requesting the operation
      switch (cursor.operation()) {
        case create: requestCreate(catalog, cursor);
          break;
        case revoke: requestRevoke(catalog, cursor);
          break;
        default: error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, cursor.operation().id(), ApprovalConstants.APP_INSTANCE_ENTITY));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestCreate
  /**
   ** Assigns an <code>Application Instance</code> in Identity Manager
   ** through the discovered {@link RequestService} to a
   ** <code>Beneficiary</code>.
   **
   ** @param  catalog            the {@link Catalog} item to request.
   ** @param  identity           the <code>Beneficiary</code> as target of
   **                            the request operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void requestCreate(final Catalog catalog, ProvisioningInstance identity)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, catalog.getEntityName(), ApprovalConstants.USER_ENTITY, identity.name() };
    warning(FeatureResourceBundle.format(FeatureMessage.REQUEST_ASSIGN_BEGIN, arguments));
    final List<RequestBeneficiaryEntityAttribute> f = new ArrayList<RequestBeneficiaryEntityAttribute>();
    final RequestForm                             form = identity.dataSet();
    for (RequestForm.Attribute ca : form.attribute()) {
      // here in each RequestBeneficiaryEntityAttribute object we will be
      // setting the request data FIELD_1,FIELD_2,FIELD_3 are the request data
      // set field label name and the "value" is the corresponding value.
      f.add(new RequestBeneficiaryEntityAttribute(ca.name(), ca.value(), RequestBeneficiaryEntityAttribute.TYPE.getEvaluatedDataType(ca.type())));
    }
    final List<RequestBeneficiaryEntity> entity = new ArrayList<RequestBeneficiaryEntity>();
    entity.add(requestEntity(catalog, RequestConstants.MODEL_PROVISION_APPLICATION_INSTANCE_OPERATION, f));
    submit(identity.name(), entity);
    warning(FeatureResourceBundle.format(FeatureMessage.REQUEST_ASSIGN_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestRevoke
  /**
   ** Revokes an <code>Application Instance</code> in Identity Server through
   ** the discovered {@link RequestService} from a <code>Beneficiary</code>.
   **
   ** @param  catalog            the {@link Catalog} item to request.
   ** @param  identity           the <code>Beneficiary</code> as target of
   **                            the request operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void requestRevoke(final Catalog catalog, ProvisioningInstance identity)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, catalog.getEntityName(), ApprovalConstants.USER_ENTITY, identity.name() };
    warning(FeatureResourceBundle.format(FeatureMessage.REQUEST_REVOKE_BEGIN, arguments));
    final List<RequestBeneficiaryEntity> entity = new ArrayList<RequestBeneficiaryEntity>();
    entity.add(requestEntity(catalog, RequestConstants.MODEL_DEPROVISION_OPERATION, null));
    submit(identity.name(), entity);
    warning(FeatureResourceBundle.format(FeatureMessage.REQUEST_REVOKE_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupResource
  /**
   ** Returns the internal sstem identifier of an existing
   ** <code>Resource Object</code> in Identity Manager through the discovered
   ** {@link tcObjectOperationsIntf}.
   **
   ** @param  objectName         the name of a <code>Resource Object</code> to
   **                            lookup.
   **
   ** @return                    the internal system identifier of the
   **                            <code>Resource Object</code> or
   **                            <code>-1L</code> if the specified
   **                            <code>objectName</code> does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected long lookupResource(final String objectName)
    throws ServiceException {

    // prevent bogus state
    if (this.resourceService == null)
      throw new ServiceException(ServiceError.ABORT, "resourceService is null");

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ResourceHandler.NAME, objectName);

    long identifier = -1L;
    try {
      tcResultSet resultSet = this.resourceService.findObjects(filter);
      if (resultSet.getRowCount() == 0)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, "Resource Object", objectName));
      else if (resultSet.getRowCount() > 1)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, "Resource Object", objectName));
      else
        identifier = resultSet.getLongValue(ResourceHandler.KEY);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupServer
  /**
   ** Returns the internal sstem identifier of an existing
   ** <code>IT Resource</code> in Identity Manager through the discovered
   ** {@link tcObjectOperationsIntf}.
   **
   ** @param  serverName         the name of a <code>IT Resource</code> to
   **                            lookup.
   **
   ** @return                    the internal system identifier of the
   **                            <code>IT Resource</code> or
   **                            <code>-1L</code> if the specified
   **                            <code>serverName</code> does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected long lookupServer(final String serverName)
    throws ServiceException {

    // prevent bogus state
    if (this.endpointService == null)
      throw new ServiceException(ServiceError.ABORT, "endpointService is null");

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ITResourceHandler.NAME, serverName);

    long identifier = -1L;
    try {
      tcResultSet resultSet = this.endpointService.findITResourceInstances(filter);
      if (resultSet.getRowCount() == 0)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, "IT Resource", serverName));
      else if (resultSet.getRowCount() > 1)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, "IT Resource", serverName));
      else
        identifier = resultSet.getLongValue(ITResourceHandler.KEY);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupApplication
  /**
   ** Returns an existing {@link ApplicationInstance} from Identity Manager
   ** through the discovered {@link ApplicationInstanceService}.
   **
   ** @param  applicationName    the name of an
   **                            <code>Application Instance</code> to lookup.
   **
   ** @return                    the {@link ApplicationInstance} for
   **                            <code>applicationName</code> or
   **                            <code>null</code> if the specified
   **                            <code>applicationName</code> does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected ApplicationInstance lookupApplication(final String applicationName)
    throws ServiceException {

    // prevent bogus state
    if (this.applicationService == null)
      throw new ServiceException(ServiceError.ABORT, "applicationService is null");

    ApplicationInstance instance = null;
    try {
      instance = this.applicationService.findApplicationInstanceByName(applicationName);
    }
    catch (ApplicationInstanceNotFoundException e) {
      final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, applicationName };
      debug(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createInstance
  /**
   ** Transforms the given {@link  ApplicationData} provider to an
   ** {@link ApplicationInstance} by looking up the required information in
   ** Identity Manager.
   **
   ** @param  instance           the {@link ApplicationInstance} that will
   **                            recieve the values from the given
   **                            {@link  ApplicationData}.
   ** @param  provider           the {@link ApplicationData} to obtain the
   **                            values from and used to lookup specific
   **                            identifier to put in the passed
   **                            {@link ApplicationInstance} as the reciever.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean createInstance(final ApplicationInstance instance, final ApplicationData provider)
    throws ServiceException {

    boolean changed = false;
    // verify if the display name of the application instance is requested to
    // change
    if (!StringUtility.isEmpty(provider.displayName())) {
      instance.setDisplayName(provider.displayName());
      changed = true;
    }
    // verify if the description of the application instance is requested to
    // change
    if (!StringUtility.isEmpty(provider.description())) {
      instance.setDescription(provider.description());
      changed = true;
    }
    // verify if the Request DataSet of the application instance is requested to
    // change
    if (!StringUtility.isEmpty(provider.dataSet())) {
      instance.setDataSetName(provider.dataSet());
      changed = true;
    }
    // verify if the parent of the application instance is requested to change
    if (!StringUtility.isEmpty(provider.parent())) {
      final ApplicationInstance parent = lookupApplication(provider.parent());
      if (parent != null) {
        instance.setParentAppInstance(parent);
        instance.setParentKey(parent.getApplicationInstanceKey());
        changed = true;
      }
      else {
        final String[] arguments = { ApprovalConstants.APP_INSTANCE_ENTITY, provider.parent() };
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      }
    }

    // verify if applicable to entitlements of the application instance is
    // requested to change
    if (provider.entitlement() != null) {
      instance.setApplicableToEntitlement(provider.entitlement());
      changed = true;
    }

    // verify if the association with a Resource Object for the application
    // instance is requested to change
    if (!StringUtility.isEmpty(provider.resource())) {
      final long resource = lookupResource(provider.resource());
      if (resource == -1L) {
        final String[] arguments = { "Resource Object", provider.resource() };
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      }
      instance.setObjectKey(resource);
      changed = true;
    }

    // verify if the association with a IT Resourcefor the application instance
    // is requested to change
    if (!StringUtility.isEmpty(provider.server())) {
      final long server = lookupServer(provider.server());
      if (server == -1L) {
        final String[] arguments = { "IT Resource", provider.server() };
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
        else
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      }
      instance.setItResourceKey(server);
      changed = true;
    }
    return changed;
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
   */
  private void transferFormData(final ApplicationInstance instance, final Account account, final RequestForm request) {
    final FormInfo            form = instance.getAccountForm();
    final List<FormField>     schema = form.getFormFields();
    final Map<String, Object> accountData = account.getAccountData().getData();
    // prepare additional account data
    if (request != null) {
      if (!CollectionUtility.empty(request.attribute())) {
        for (RequestForm.Attribute attribute : request.attribute()) {
          final FormField field = fieldByLabel(schema, attribute.name());
          if (field != null) {
            // relies on attribute validation
            if (attribute.prefix()) {
              accountData.put(field.getName(), String.format(attribute.pattern(), instance.getItResourceKey(), attribute.value()));
            }
            else {
              accountData.put(field.getName(), attribute.value());
            }
          }
          else {
            error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, "Account Attribute", attribute.name()));
          }
        }
      }
      if (!CollectionUtility.empty(request.dataSet())) {
        final Map<String, ArrayList<ChildTableRecord>> subordinatedData = new HashMap<String, ArrayList<ChildTableRecord>>();
        for (String subordinatedName : request.dataSet().keySet()) {
          final FormInfo subordinatedForm = formByName(instance.getChildForms(), subordinatedName);
          if (subordinatedForm != null) {
            final ArrayList<ChildTableRecord> xxxxx = new ArrayList<ChildTableRecord>();
            for (List<RequestForm.Attribute> collection : request.dataSet().get(subordinatedName)) {
              final Map<String, Object> tupel = new HashMap<String, Object>();
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
              }
              final ChildTableRecord record = new ChildTableRecord();
              record.setAction(ChildTableRecord.ACTION.Add);
              record.setChildData(tupel);
              xxxxx.add(record);
            }
            // put form name and associated data
            subordinatedData.put(subordinatedName, xxxxx);
          }
          account.getAccountData().setChildData(subordinatedData);
        }
      }
    }
    // always determine which of the account attributes is the ITResource used
    // for provisioning and set the proper data regardless if those data are
    // part of the given data
    for (FormField cursor : schema) {
      if (!cursor.getProperties().containsKey("ITResource"))
        continue;
      accountData.put(cursor.getName(), instance.getItResourceKey());
      break;
    }
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
      if (cursor.getName().equalsIgnoreCase(name)) {
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
}