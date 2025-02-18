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

    File        :   CatalogHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.catalog.vo.Catalog;
import oracle.iam.catalog.vo.CatalogConstants;
import oracle.iam.catalog.vo.CatalogSearchCriteria;

import oracle.iam.catalog.api.CatalogService;

import oracle.iam.catalog.exception.CatalogException;

import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.RequestData;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;

import oracle.iam.request.api.RequestService;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.exception.NoSuchRoleException;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.exception.NoSuchUserException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class CatalogHandler
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>CatalogHandler</code> creates, deletes and configures a
 ** <code>Catalog</code> element in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CatalogHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String ENTITY_TYPE = "Catalog Item";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the single {@link CatalogElement} to configure or to create */
  protected CatalogElement single;

  /** the collection of {@link CatalogElement}s configure or to create */
  protected List<CatalogElement> multiple = new ArrayList<CatalogElement>();

  /** the business logic layer to operate on user profiles */
  protected UserManager userService;

  /** the business logic layer to operate on roles */
  protected RoleManager roleService;

  /**
   ** the business logic layer to operate on <code>Request</code>s
   */
  protected RequestService requestService;

  /** the business logic layer to operate on catalog */
  protected CatalogService catalogService;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Name
  // ~~~~~ ~~~~
  enum Name {
      CATEGORY        ("category")
    , CERTIFIABLE     ("certifiable")
    , AUDITABLE       ("auditable")
    , AUDIT_OBJECTIVE ("auditObjective")
    , RISK            ("risk")
    , APPROVER_USER   ("approverUser")
    , APPROVER_ROLE   ("approverRole")
    , CERTIFIER_USER  ("certifierUser")
    , CERTIFIER_ROLE  ("certifierRole")
    , FULFILLMENT_USER("fulfillmentUser")
    , FULFILLMENT_ROLE("fulfillmentRole")
    , USER_TAGS       ("userTags")
    , PARENT_KEY      ("parentKey")
    , PARENT_TYPE     ("parentType")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Name</code> that allows use as a JavaBean.
     **
     ** @param  id               the identifier of the parameter.
     */
    Name(final String id) {
      this.id = id;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>CatalogHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public CatalogHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);

    // the default operational mode is modify
    operation(ServiceOperation.modify);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager Role.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager Role.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = buildElement();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager Role.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    if (this.single == null)
      this.single = buildElement();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the Catalog Element in Oracle
   **                            Identity Manager to handle.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = buildElement();

    this.single.name(name);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the catalog element in Identity Manager to handle.
   **
   ** @return                    the name of the catalog element in Oracle
   **                            Identity Manager to handle.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link CatalogElement} to add.
   **
   ** @throws BuildException     if the specified {@link EntitlementData} is
   **                            already assigned to this task.
   */
  public void addInstance(final CatalogElement object)
    throws BuildException {

    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
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

    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (this.single != null)
      this.single.validate(operation());

    for (CatalogElement instance : this.multiple)
      instance.validate(operation());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing catalog element in Identity Manager through the
   ** discovered {@link CatalogService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.roleService = task.service(RoleManager.class);
    this.userService = task.service(UserManager.class);
    this.catalogService = task.service(CatalogService.class);

    if (this.single != null)
      modify(this.single);

    for (CatalogElement i : this.multiple)
      modify(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing catalog element from Identity Manager through
   ** the discovered {@link CatalogService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.catalogService = task.service(CatalogService.class);

    if (this.single != null)
      enable(this.single);

    for (CatalogElement i : this.multiple)
      enable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing catalog element from Identity Manager through the
   ** discovered {@link CatalogService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.catalogService = task.service(CatalogService.class);

    if (this.single != null)
      disable(this.single);

    for (CatalogElement i : this.multiple)
      disable(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing catalog element in Identity Manager through the
   ** discovered {@link CatalogService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.catalogService = task.service(CatalogService.class);

    if (this.single != null)
      delete(this.single);

    for (CatalogElement i : this.multiple)
      delete(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies the catalog item, this can also be used to update the tags and
   ** metadata value. Ideally this API should only be used to update the catalog
   ** specific data. For updating metadata and tags, Metadata and Tags API
   ** should be used.
   ** <br>
   ** Name , Display Name and Description of catalog item will not be updated
   ** using this API. No bulk update API has been provided for this release.
   **
   ** @param  instance           the {@link CatalogElement} to modify.
   **
   ** @throws ServiceException   if the operation fails
   */
  public void modify(final CatalogElement instance)
    throws ServiceException {

    final Catalog catalog = lookup(instance);
    if (catalog != null) {
      final String[] arguments = { ENTITY_TYPE, instance.name() };
      if (changed(catalog, instance)) {
        try {
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));
          this.catalogService.updateCatalogItems(catalog);
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, arguments));
        }
        catch (Exception e) {
          throw new ServiceException(ServiceError.UNHANDLED, e);
        }
      }
      else {
        warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SKIPPED, arguments));
      }
    }
    else {
      if (failonerror())
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an catalog element in Identity Manager through the discovered
   ** {@link CatalogService}.
   ** <br>
   ** Modify API can also be used to achieve the same result.
   **
   ** @param  instance           the {@link CatalogElement} to enable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable(final CatalogElement instance)
    throws ServiceException {

    final Catalog catalog = lookup(instance);
    if (catalog != null)
      try {
        final String[] arguments = { ENTITY_TYPE, instance.name() };
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_BEGIN, arguments));
        catalog.setRequestable(Boolean.TRUE);
        this.catalogService.updateCatalogItems(catalog);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_SUCCESS, arguments));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      if (failonerror())
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OPERATION_ENABLE_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_ENABLE_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an catalog element in Identity Manager through the discovered
   ** {@link CatalogService}.
   ** <br>
   ** Modify API can also be used to achieve the same result.
   **
   ** @param  instance           the {@link CatalogElement} to disable.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable(final CatalogElement instance)
    throws ServiceException {

    final Catalog catalog = lookup(instance);
    if (catalog != null)
      try {
        final String[] arguments = { ENTITY_TYPE, instance.name() };
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_BEGIN, arguments));
        catalog.setRequestable(Boolean.FALSE);
        this.catalogService.updateCatalogItems(catalog);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_SUCCESS, arguments));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      if (failonerror())
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OPERATION_DISABLE_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_DISABLE_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete the catalog item from catalog table by performing a hard delete.
   ** <br>
   ** For soft delete modify API can be used.
   **
   ** @param  instance           the {@link CatalogElement} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final CatalogElement instance)
    throws ServiceException {

    final Catalog catalog = lookup(instance);
    if (catalog != null)
      try {
        final String[] arguments = { ENTITY_TYPE, instance.name() };
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
        final List<Catalog> items = new ArrayList<Catalog>();
        items.add(catalog);
        this.catalogService.deleteCatalogItems(items, false);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    else {
      if (failonerror())
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, ENTITY_TYPE, instance.name(), ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_TYPE, instance.name())));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Submit a request.
   ** <p>
   ** The request is splitted per request entity, hence only single requestes
   ** per request entity are submitted. For example if there are a beneficiary
   ** that 4 request entity as the result 4 requests are submitted.
   **
   ** @param  identity           the public name of the {@link Beneficiary} the
   **                            request is created for.
   **                            Used for logging purpose only,
   ** @param  entity             the {@link List} of
   **                            {@link RequestBeneficiaryEntity} the request
   **                            consists off.
   **
   ** @throws ServiceException   if the request could not be submitted.
   */
  protected void submit(final String identity, final List<RequestBeneficiaryEntity> entity)
    throws ServiceException {

    for (RequestBeneficiaryEntity cursor : entity) {
      submit(identity, cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Submit a single request.
   **
   ** @param  identity           the public name of the {@link Beneficiary} the
   **                            request is created for.
   **                            Used for logging purpose only,
   ** @param  entity             the {@link RequestBeneficiaryEntity} the
   **                            request consists off.
   **
   ** @throws ServiceException   if the request could not be submitted.
   */
  protected void submit(final String identity, final RequestBeneficiaryEntity entity)
    throws ServiceException {

    // prevent bogus state
    if (this.requestService == null)
      throw new ServiceException(ServiceError.ABORT, "requestService is null");

    final Beneficiary beneficiary = lookupBeneficiary(identity);
    beneficiary.setTargetEntities(CollectionUtility.list(entity));

    final RequestData req = new RequestData();
    req.setBeneficiaries(CollectionUtility.list(beneficiary));
    try {
      final String[] arguments = { entity.getRequestEntityType().getValue(), entity.getEntitySubType(), beneficiary.getBeneficiaryType(), identity };
      debug(FeatureResourceBundle.format(FeatureMessage.REQUEST_SUBMIT_BEGIN, arguments));
      final String requestID = this.requestService.submitRequest(req);
      debug(FeatureResourceBundle.format(FeatureMessage.REQUEST_SUBMIT_SUCCESS, requestID));
    }
    catch (Exception e) {
      final String[] arguments = { entity.getRequestEntityType().getValue(), entity.getEntitySubType(), beneficiary.getBeneficiaryType(), identity, e.getLocalizedMessage() };
      if (failonerror()) {
        throw new FeatureException(FeatureError.REQUEST_SUBMIT_FAILED, arguments);
      }
      else {
        error(FeatureResourceBundle.format(FeatureError.REQUEST_SUBMIT_FAILED, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestEntity
  /**
   ** Factory method to create a {@link RequestBeneficiaryEntity} instance that
   ** belongs to the specified {@link Catalog} and the given operations model.
   **
   ** @param  catalog            the {@link Catalog} as purpose of a request.
   ** @param  model              the operations model for the specified
   **                            {@link CatalogElement}.
   ** @param  data               the attribute mapping to apply on the
   **                            {@link RequestBeneficiaryEntity}.
   **
   ** @return                    an {@link RequestBeneficiaryEntity} prepared
   **                            for further usage.
   */
  protected RequestBeneficiaryEntity requestEntity(final Catalog catalog, final String model, final List<RequestBeneficiaryEntityAttribute> data) {

    final RequestBeneficiaryEntity rbe = new RequestBeneficiaryEntity();
    // the name of the catalog element looked up above
    rbe.setEntityKey(catalog.getEntityKey());
    // the name of the catalog element looked up above
    rbe.setEntitySubType(catalog.getEntityName());
    // set the type of the request
    rbe.setRequestEntityType(catalog.getEntityType());
    // the request operation type
    rbe.setOperation(model);
    // the optional request data
    if (!CollectionUtility.empty(data))
      rbe.setEntityData(data);
    return rbe;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns an existing catalog element of the Identity Manager through the
   ** discovered {@link CatalogService}.
   **
   ** @param  instance           the {@link CatalogElement} providing the data
   **                            for lookup.
   **
   ** @return                    the {@link Catalog} item.
   **                            May be <code>null</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected Catalog lookup(final CatalogElement instance)
    throws ServiceException {

    // prevent bogus state
    if (this.catalogService == null)
      throw new ServiceException(ServiceError.ABORT, "catalogService is null");

    Catalog item = null;
    try {
      final SearchCriteria criteria = new SearchCriteria(
        new SearchCriteria(CatalogConstants.CATALOG_ENTITY_KEY,  instance.entityID(),         SearchCriteria.Operator.EQUAL)
      , new SearchCriteria(CatalogConstants.CATALOG_ENTITY_TYPE, instance.type().getValue(), SearchCriteria.Operator.EQUAL)
      , SearchCriteria.Operator.AND
      );
      final List<Catalog>  result = this.catalogService.findCatalog(criteria, 1, 2, CatalogConstants.CATALOG_ENTITY_NAME, CatalogSearchCriteria.SortCriteria.ASCENDING);
      if (result == null || result.size() == 0) {
        final String[] arguments = { ENTITY_TYPE, instance.name() };
        debug(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      }
      else if (result.size() > 1) {
        final String[] arguments = { ENTITY_TYPE, instance.name() };
        debug(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_AMBIGUOS, arguments));
      }
      else {
        item = result.get(0);
      }
      return item;
    }
    catch (CatalogException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupBeneficiary
  /**
   ** Returns an existing entity of Identity Manager through the discovered
   ** {@link UserManager}.
   **
   ** @param  name               the public name of the {@link Beneficiary} to
   **                            lookup.
   **
   ** @return                    the internal identifier for type and name
   **                            for <code>name</code> or
   **                            <code>null</code> if an identity does not
   **                            exists with the specified <code>name</code>.
   **
   ** @throws ServiceException   if an unhandled exception occured.
   */
  protected Beneficiary lookupBeneficiary(final String name)
    throws ServiceException {

    final Beneficiary beneficiary = new Beneficiary();
    // the key of the bebeficiary is the users key
    beneficiary.setBeneficiaryKey(lookupIdentity(name));
    // set the type as user beneficiary
    beneficiary.setBeneficiaryType(Beneficiary.USER_BENEFICIARY);
    return beneficiary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupIdentity
  /**
   ** Returns an existing user of the Identity Manager through the given
   ** {@link UserManager}.
   **
   ** @param  name               the login name of the user to lookup the
   **                            internal identifier.
   **
   ** @return                    the system identifier <code>EntityId</code> for
   **                            the specified name or <code>null</code> if the
   **                            user doesn't exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected String lookupIdentity(final String name)
    throws ServiceException {

    // prevent bogus state
    if (this.userService == null)
      throw new ServiceException(ServiceError.ABORT, "userService is null");

    User identity = null;
    try {
      identity = this.userService.getDetails(name, null, true);
    }
    catch (NoSuchUserException e) {
      final String[] arguments = { UserManagerConstants.USER_ENTITY, name };
      if (failonerror())
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return (identity == null) ? null : identity.getEntityId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Returns an existing role of the Identity Manager through the discovered
   ** {@link RoleManager}.
   **
   ** @param  name               the name of the role to lookup the internal
   **                            identifier.
   **
   ** @return                    the system identifier <code>EntityId</code> for
   **                            the specified name or <code>null</code> if the
   **                            role doesn't exists.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public String lookupRole(final String name)
    throws ServiceException {

    // prevent bogus state
    if (this.roleService == null)
      throw new ServiceException(ServiceError.ABORT, "roleService is null");

    Role identity = null;
    try {
      identity = this.roleService.getDetails(RoleManagerConstants.ROLE_NAME, name, null);
    }
    catch (NoSuchRoleException e) {
      final String[] arguments = { RoleManagerConstants.ROLE_ENTITY_NAME, name };
      if (failonerror())
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
      else
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return (identity == null) ? null : identity.getEntityId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildElement
  /**
   ** Factory method to create a <code>Catalog Element</code>.
   **
   ** @return                    a {@link CatalogElement} instance.
   */
  protected CatalogElement buildElement() {
    return new CatalogElement();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Modifies the provided {@link Catalog} item with the parameter values that
   ** are set.
   **
   ** @param  catalog            the {@link Catalog} item to modify.
   ** @param  provider           the {@link CatalogElement} as the data
   **                             provider.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link Catalog} item is modified.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final boolean changed(final Catalog catalog, final CatalogElement provider)
    throws ServiceException {

    boolean                   changed = false;
    final Map<String, Object> parameter = provider.parameter();

    if (parameter.containsKey(Name.CERTIFIABLE.id)) {
      catalog.setCertifiable(provider.booleanParameter(Name.CERTIFIABLE.id));
      changed = true;
    }
    if (parameter.containsKey(Name.AUDITABLE.id)) {
      catalog.setAuditable(provider.booleanParameter(Name.AUDITABLE.id));
      changed = true;
    }
    if (parameter.containsKey(Name.APPROVER_USER.id)) {
      final String user = provider.stringParameter(Name.APPROVER_USER.id);
      catalog.setApproverUser(StringUtility.isEmpty(user) ? null : lookupIdentity(user));
      changed = true;
    }
    if (parameter.containsKey(Name.APPROVER_ROLE.id)) {
      final String role = provider.stringParameter(Name.APPROVER_ROLE.id);
      catalog.setApproverRole(StringUtility.isEmpty(role) ? null : lookupRole(role));
      changed = true;
    }
    if (parameter.containsKey(Name.CERTIFIER_USER.id)) {
      final String user = provider.stringParameter(Name.CERTIFIER_USER.id);
      catalog.setCertifierUser(StringUtility.isEmpty(user) ? null : lookupIdentity(user));
      changed = true;
    }
    if (parameter.containsKey(Name.CERTIFIER_ROLE.id)) {
      final String role = provider.stringParameter(Name.CERTIFIER_ROLE.id);
      catalog.setCertifierRole(StringUtility.isEmpty(role) ? null : lookupRole(role));
      changed = true;
    }
    if (parameter.containsKey(Name.FULFILLMENT_USER.id)) {
      final String user = provider.stringParameter(Name.FULFILLMENT_USER.id);
      catalog.setFulFillMentUser(StringUtility.isEmpty(user) ? null : lookupIdentity(user));
      changed = true;
    }
    if (parameter.containsKey(Name.FULFILLMENT_ROLE.id)) {
      final String role = provider.stringParameter(Name.FULFILLMENT_ROLE.id);
      catalog.setFulFillMentRole(StringUtility.isEmpty(role) ? null : lookupRole(role));
      changed = true;
    }
    if (parameter.containsKey(Name.CATEGORY.id)) {
      catalog.setCategoryName(provider.stringParameter(Name.CATEGORY.id));
      changed = true;
    }
    if (parameter.containsKey(Name.RISK.id)) {
      catalog.setItemRisk(provider.integerParameter(Name.RISK.id));
      catalog.setRiskUpdateTime(DateUtility.now());
      changed = true;
    }
    if (parameter.containsKey(Name.AUDIT_OBJECTIVE.id)) {
      catalog.setAuditObjectives(provider.stringParameter(Name.AUDIT_OBJECTIVE.id));
      changed = true;
    }
    if (parameter.containsKey(Name.USER_TAGS.id)) {
      catalog.setUserDefinedTags(provider.stringParameter(Name.USER_TAGS.id));
      changed = true;
    }
    return changed;
  }
}