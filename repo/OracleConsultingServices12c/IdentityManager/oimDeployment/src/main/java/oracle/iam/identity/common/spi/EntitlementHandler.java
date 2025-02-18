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

    File        :   EntitlementHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.catalog.api.CatalogService;
import oracle.iam.catalog.vo.Catalog;
import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;
import oracle.iam.identity.deployment.type.Publication;
import oracle.iam.identity.exception.NoSuchOrganizationException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.authopss.api.PolicyConstants;
import oracle.iam.platform.authopss.vo.EntityPublication;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platformservice.api.EntityPublicationService;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.exception.GenericEntitlementServiceException;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.request.api.RequestService;
import oracle.iam.request.vo.ApprovalConstants;
////////////////////////////////////////////////////////////////////////////////
// class EntitlementHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>EntitlementHandler</code> creates, deletes and configures
 ** entitlements in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EntitlementHandler extends CatalogHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the type identifier of an <code>Entity Publication</code>s
   */
  protected final PolicyConstants.Resources type;

  /**
   ** the business logic layer to operate on <code>Entity Publication</code>s
   */
  protected EntityPublicationService publisherService;

  /**
   ** the business logic layer to operate on <code>Organization</code>s
   */
  protected OrganizationManager organizationService;

  /**
   ** the business logic layer to operate on <code>Entitlement</code>s
   */
  private EntitlementService entitlementService;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EntitlementHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public EntitlementHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    this(frontend, PolicyConstants.Resources.IT_RESOURCE_ENTITLEMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EntitlementHandler</code> to initialize the instance
   ** that use the specified entitlemant type.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  resourceType       the type
   */
  protected EntitlementHandler(final ServiceFrontend frontend, final PolicyConstants.Resources resourceType) {
    // ensure inheritance
    super(frontend);

    // initialize instance
    this.type = resourceType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPublication
  /**
   ** Add the specified userid to be handled.
   **
   ** @param  action             the action to perform for <code>userid</code>
   ** @param  recipient          the {@link List} of recipients either user
   **                            profiles or roles in Identity Manager to become
   **                            membership of the role.
   */
  public void addPublication(final ServiceOperation action, final List<Publication.Recipient> recipient) {
    if (this.single == null)
      this.single = buildElement();

    // add the value pair to the parameters
    ((EntitlementData)this.single).addPublication(action, recipient);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new role in Identity Manager through the discovered
   ** {@link EntitlementService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.publisherService    = task.service(EntityPublicationService.class);
    this.entitlementService  = task.service(EntitlementService.class);
    this.organizationService = task.service(OrganizationManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (overridden)
  /**
   ** Deletes an existing entitlement from Identity Manager through the given
   ** {@link EntitlementService}.
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
    this.entitlementService  = task.service(EntitlementService.class);
    this.organizationService = task.service(OrganizationManager.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (overridden)
  /**
   ** Mofifies an existing entitlement in Identity Manager through the
   ** discovered {@link EntitlementService}.
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
    this.publisherService    = task.service(EntityPublicationService.class);
    this.entitlementService  = task.service(EntitlementService.class);
    this.organizationService = task.service(OrganizationManager.class);

    if (this.single != null) {
      final EntitlementData single = (EntitlementData)this.single;
      modify(single);
      // at first we revoke the entity publications ...
      unpublish(single);
      // ... and adding the new entity publications afterwards
      publish(single, null);
    }

    for (CatalogElement cursor : this.multiple) {
      final EntitlementData data = (EntitlementData)cursor;
      modify(data);
      // at first we revoke the entity publications ...
      unpublish(data);
      // ... and adding the new entity publications afterwards
      publish(data, null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>Entitlement</code> in Identity Manager through
   ** the discovered {@link EntitlementService}.
   **
   ** @param  data               the {@link EntitlementData} to modify.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final EntitlementData data)
    throws ServiceException {

    Entitlement instance = entityID(data);
    if (instance != null) {
      if (createInstance(instance, data)) {
        try {
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, ApprovalConstants.ENTITLEMENT_ENTITY, data.name()));
          instance = this.entitlementService.updateEntitlement(instance);
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, ApprovalConstants.ENTITLEMENT_ENTITY, data.name()));
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
    }
    else {
      final String[] arguments = { ApprovalConstants.ENTITLEMENT_ENTITY, data.name() };
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publish
  /**
   ** Enables entity publications to organizations in Identity Manager through
   ** the discovered {@link EntityPublicationService}.
   **
   ** @param  instance           the {@link EntitlementData} to enable.
   ** @param  applyEntitlement   <code>true</code> if the oparetions have to
   **                            applied on the existing entitlements belonging
   **                            to the instance data.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void publish(final EntitlementData instance, final Boolean applyEntitlement)
    throws ServiceException {

    // prevent bogus state
    if (this.publisherService == null)
      throw new ServiceException(ServiceError.ABORT, "publisherService is null");

    final List<Publication.Recipient> recipient = instance.enabled();
    if (!recipient.isEmpty()) {
      final String[] arguments = { this.type.getId(), instance.name(), SystemConstant.EMPTY, SystemConstant.EMPTY };
      for (Publication.Recipient identity : recipient) {
        arguments[2] = identity.getValue();
        arguments[3] = identity.name();
        warning(FeatureResourceBundle.format(FeatureMessage.PUBLICATION_ASSIGN_BEGIN, arguments));

        final Organization scope = lookupRecipient(identity);
        if (scope == null) {
          if (failonerror()) {
            final String[] error = { identity.getValue(), identity.name() };
            throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, error);
          }
          else {
            // "Entity Publication assigment of %1$s "%2$s" to %3$s "%4$s" failed. Reason: %5$s"
            final String[] parameter = { this.type.getId(), instance.name(), identity.getValue(), identity.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, identity.getValue(), identity.name()) };
            error(FeatureResourceBundle.format(FeatureError.PUBLICATION_ASSIGN_FAILED, parameter));
          }
        }
        else {
          List<EntityPublication> request = CollectionUtility.list(createPublication(scope, instance, identity.hierarchy()));
          if (applyEntitlement == null)
            request = this.publisherService.addEntityPublications(request);
          else
            request = this.publisherService.addEntityPublications(request, applyEntitlement);
          if (request.size() > 0)
            info(FeatureResourceBundle.format(FeatureMessage.PUBLICATION_ASSIGN_SUCCESS, arguments));
          else {
            // "Entity Publication assigment of %1$s "%2$s" to %3$s "%4$s" failed. Reason: %5$s"
            final String[] parameter = { this.type.getId(), instance.name(), identity.getValue(), identity.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, identity.getValue(), identity.name()) };
            error(FeatureResourceBundle.format(FeatureError.PUBLICATION_ASSIGN_FAILED, parameter));
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unpublish
  /**
   ** Disables entity publications from organizations in Identity Manager
   ** through the discovered {@link EntityPublicationService}.
   **
   ** @param  instance           the {@link EntitlementData} to publish.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void unpublish(final EntitlementData instance)
    throws ServiceException {

    // prevent bogus state
    if (this.publisherService == null)
      throw new ServiceException(ServiceError.ABORT, "publisherService is null");

    final List<Publication.Recipient> recipient = instance.disabled();
    if (!recipient.isEmpty()) {
      final String[] arguments = { this.type.getId(), instance.name(), SystemConstant.EMPTY, SystemConstant.EMPTY };
      for (Publication.Recipient identity : recipient) {
        arguments[2] = identity.getValue();
        arguments[3] = identity.name();
        warning(FeatureResourceBundle.format(FeatureMessage.PUBLICATION_REVOKE_BEGIN, arguments));

        final Organization scope = lookupRecipient(identity);
        if (scope == null) {
          if (failonerror()) {
            final String[] error = { identity.getValue(), identity.name() };
            throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, error);
          }
          else {
            // "Entity Publication revocation of %1$s "%2$s" from %3$s "%4$s" failed. Reason: %5$s"
            final String[] parameter = { this.type.getId(), instance.name(), identity.getValue(), identity.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, identity.getValue(), identity.name()) };
            error(FeatureResourceBundle.format(FeatureError.PUBLICATION_REVOKE_FAILED, parameter));
          }
        }
        else {
          // get all entity plublications that belongs to the instance);
          final List<EntityPublication> remain = this.publisherService.listEntityPublications(this.type, instance.entityID(), null);
          // remove all that not match
          // matching is done by the instance id, type and scope id of the
          // pubcliation
          remain.retainAll(CollectionUtility.list(createPublication(scope, instance, identity.hierarchy())));
          // remove all retaining publications
          if (this.publisherService.removeEntityPublications(remain)) {
            info(FeatureResourceBundle.format(FeatureMessage.PUBLICATION_REVOKE_SUCCESS, arguments));
          }
          else {
            // "Entity Publication revocation of %1$s "%2$s" from %3$s "%4$s" failed. Reason: %5$s"
            final String[] parameter = { this.type.getId(), instance.name(), identity.getValue(), identity.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTASSINGED, identity.getValue(), identity.name()) };
            error(FeatureResourceBundle.format(FeatureError.PUBLICATION_REVOKE_FAILED, parameter));
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Assigns a <code>Entitlement</code> in Identity Manager through the
   ** discovered {@link RequestService} to requests.
   **
   ** @param  data               the {@link EntitlementData} to assign.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void assign(final EntitlementData data)
    throws ServiceException {

    final Catalog catalog = lookup(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revokes an <code>Entitlement</code> in Identity Manager through the
   ** discovered {@link RequestService} from requests.
   **
   ** @param  data               the {@link EntitlementData} to assign.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void revoke(final EntitlementData data)
    throws ServiceException {

    final Catalog catalog = lookup(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityID
  /**
   ** Returns an existing {@link Entitlement} from Identity Manager through the
   ** discovered {@link EntitlementService}.
   **
   ** @param  data               the {@link EntitlementData} providing the name
   **                            of the {@link Entitlement} to return.
   **
   ** @return                    the {@link Entitlement} for
   **                            <code>entitlementName</code> or
   **                            <code>null</code> if the specified
   **                            <code>entitlementName</code> does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public Entitlement entityID(final EntitlementData data)
    throws ServiceException {

    final Entitlement instance = lookupEntitlement(data.name());
    data.entityID(String.valueOf(instance.getEntitlementKey()));
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRecipient
  /**
   ** Returns an existing entity of the Identity Manager through the discovered
   ** {@link OrganizationManager}.
   **
   ** @param  recipient          the {@link Publication.Recipient} to lookup.
   **
   ** @return                    the internal identifier for type and name
   **                            contained in {@link Publication.Recipient} or
   **                            <code>null</code> if the specified
   **                            {@link Publication.Recipient} does not exists.
   **
   ** @throws ServiceException   if an unhandled exception occured.
   */
  protected Organization lookupRecipient(final Publication.Recipient recipient)
    throws ServiceException {

    return lookupOrganization(recipient.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrganization
  /**
   ** Returns an existing organization of the Identity Manager hrough the
   ** discovered {@link OrganizationManager}.
   **
   ** @param  name               the public name of the {@link Organization} to
   **                            lookup.
   **
   ** @return                    the internal identifier for type and name
   **                            for <code>name</code> or
   **                            <code>null</code> if an organization does not
   **                            exists with the specified <code>name</code>.
   **
   ** @throws ServiceException   if an unhandled exception occured.
   */
  protected Organization lookupOrganization(final String name)
    throws ServiceException {

    // prevent bogus state
    if (this.organizationService == null)
      throw new ServiceException(ServiceError.ABORT, "organizationService is null");

    Organization identity = null;
    try {
      identity = this.organizationService.getDetails(name, null, true);
    }
    catch (NoSuchOrganizationException e) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, OrganizationManagerConstants.ORGANIZATION_ENTITY, name));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPublication
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
  protected EntityPublication createPublication(final Organization scope, final EntitlementData instance, final boolean hierarchy) {
    final EntityPublication publication = new EntityPublication();
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
   ** @return                    a {@link CatalogElement} instance.
   */
  @Override
  protected CatalogElement buildElement() {
    return new EntitlementData();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provision
  /**
   ** Provisions an <code>Entitlement</code> in Identity Manager through the
   ** discovered <code>ProvisioningService</code> to beneficiaries.
   **
   ** @param  instance           the {@link Entitlement} to provision to
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  identity           the <code>Beneficiaries</code> as targets of
   **                            the provisioning operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void provision(final Entitlement instance, final List<ProvisioningInstance> identity)
    throws ServiceException {

    for (ProvisioningInstance cursor : identity) {
      // perfom provisioning if the current instance is requesting the operation
      switch (cursor.operation()) {
        case create: entitlementCreate(instance, cursor);
          break;
        case modify: entitlementModify(instance, cursor);
          break;
        case revoke: entitlementRevoke(instance, cursor);
          break;
        default: error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, cursor.operation().id(), ApprovalConstants.APP_INSTANCE_ENTITY));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementCreate
  /**
   ** Provisions an <code>Entitlement</code> in Identity Manager through the
   ** discovered <code>ProvisioningService</code> to beneficiaries.
   **
   ** @param  instance           the {@link Entitlement} to provision to the
   **                            collection of <code>Beneficiaries</code>.
   ** @param  identity           the <code>Beneficiary</code> as target of
   **                            the provisioning operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void entitlementCreate(final Entitlement instance, final ProvisioningInstance identity)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.ENTITLEMENT_ENTITY, instance.getDisplayName(), ApprovalConstants.USER_ENTITY, identity.name() };
    info(FeatureResourceBundle.format(FeatureMessage.PROVISION_ASSIGN_BEGIN, arguments));
    info(FeatureResourceBundle.format(FeatureMessage.PROVISION_ASSIGN_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementRevoke
  /**
   ** De-Provisions an <code>Entitlement</code> in Identity Manager through the
   ** discovered <code>ProvisioningService</code> from beneficiaries.
   **
   ** @param  instance           the {@link Entitlement} to revoke from the
   **                            collection of <code>Beneficiaries</code>.
   ** @param  identity           the <code>Beneficiary</code> as target of
   **                            the revoke operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void entitlementRevoke(final Entitlement instance, final ProvisioningInstance identity)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.ENTITLEMENT_ENTITY, instance.getDisplayName(), ApprovalConstants.USER_ENTITY, identity.name() };
    info(FeatureResourceBundle.format(FeatureMessage.PROVISION_REVOKE_BEGIN, arguments));
    info(FeatureResourceBundle.format(FeatureMessage.PROVISION_REVOKE_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementModify
  /**
   ** Modifies an <code>Entitlement</code> account in Identity Manager through
   ** the discovered <code>ProvisioningService</code>.
   **
   ** @param  instance           the {@link Entitlement} to modify for
   **                            the collection of <code>Beneficiaries</code>.
   ** @param  identity           the <code>Beneficiary</code> as target of
   **                            the revoke operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void entitlementModify(final Entitlement instance, final ProvisioningInstance identity)
    throws ServiceException {

    final String[] arguments = { ApprovalConstants.ENTITLEMENT_ENTITY, instance.getDisplayName(), ApprovalConstants.USER_ENTITY, identity.name() };
    info(FeatureResourceBundle.format(FeatureMessage.PROVISION_MODIFY_BEGIN, arguments));
    info(FeatureResourceBundle.format(FeatureMessage.PROVISION_MODIFY_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createInstance
  /**
   ** Transforms the given {@link  ApplicationData} provider to an
   ** {@link ApplicationInstance} by looking up the required information in
   ** Identity Manager.
   **
   ** @param  instance           the {@link Entitlement} that will recieve the
   **                            values from the given {@link  EntitlementData}.
   ** @param  provider           the {@link EntitlementData} to obtain the
   **                            values from and used to lookup specific
   **                            identifier to put in the passed
   **                            {@link Entitlement} as the reciever.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean createInstance(final Entitlement instance, final EntitlementData provider)
    throws ServiceException {

    boolean changed = false;
    // verify if the display name of the entitlement is requested to change
    if (!StringUtility.isEmpty(provider.displayName())) {
      instance.setDisplayName(provider.displayName());
      changed = true;
    }
    // verify if the description of the entitlement is requested to change
    if (!StringUtility.isEmpty(provider.description())) {
      instance.setDescription(provider.description());
      changed = true;
    }
    return changed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupEntitlement
  /**
   ** Returns an existing {@link Entitlement} from Identity Manager through the
   ** discovered {@link EntitlementService}.
   **
   ** @param  entitlementName    the name of an {@link Entitlement} to lookup.
   **
   ** @return                    the {@link Entitlement} for
   **                            <code>entitlementName</code> or
   **                            <code>null</code> if the specified
   **                            <code>entitlementName</code> does not exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Entitlement lookupEntitlement(final String entitlementName)
    throws ServiceException {

    // prevent bogus state
    if (this.entitlementService == null)
      throw new ServiceException(ServiceError.ABORT, "entitlementService is null");

    // the identity's application accounts for a specific application instance
    final SearchCriteria criteria = new SearchCriteria(ProvisioningConstants.EntitlementInstanceSearchAttribute.ENTITLEMENT_VALUE.getId(), entitlementName, SearchCriteria.Operator.EQUAL);

    Entitlement instance = null;
    try {
      final List<Entitlement> result = this.entitlementService.findEntitlements(criteria, null);
      if (result.size() > 1)
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, ApprovalConstants.ENTITLEMENT_ENTITY, entitlementName));
      else
        instance = (result == null || result.size() == 0) ? null : result.get(0);
    }
    catch (GenericEntitlementServiceException e) {
      final String[] arguments = { ApprovalConstants.ENTITLEMENT_ENTITY, entitlementName };
      debug(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return instance;
  }
}