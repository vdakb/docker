package bka.iam.identity.event.org;

import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationError;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.platform.OIMInternalClient;
import oracle.iam.platform.context.ContextManager;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.AccountNotFoundException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.util.CommonUtil;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;

import javax.security.auth.login.LoginException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static bka.iam.identity.event.org.OrganizationAttributeValidationHandler.NSIS_CODE_ATTRIBUTE;
import static bka.iam.identity.event.org.OrganizationAttributeValidationHandler.NSIS_CODE_DISPLAY_NAME;

/**
 * Propagates the value of the <code>Code NSIS</code> attribute if it has changed.
 * The update involves users who are members of the organization
 * and have at lease one <code>NSIS</code> related account.
 */
public class OrganizationAttributePostProcessor extends AbstractPostProcessHandler {

  /**
   * Name of the NSIS Production object.
   */
  private static final String NSIS_PROD_RESOURCE_OBJECT_NAME = "NSISP Account";
  /**
   * Name of the NSIS Test object.
   */
  private static final String NSIS_TEST_RESOURCE_OBJECT_NAME = "NSIST Account";
  /**
   * Name of the NSIS object.
   */
  private static final String NSIS_RESOURCE_OBJECT_NAME      = "NSIS Account";

  /**
   * The implementation of this post process handler in one-off orchestration.
   * <p>
   * All Organization post process events are handled in this method.
   *
   * @param processId     the identifier of the orchestration process.
   * @param eventId       the identifier of the orchestration event
   * @param orchestration the object containing contextual information
   *                      such as orchestration parameters, operation.
   */
  @Override
  public EventResult execute(long processId, long eventId, Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    processEvent(orchestration.getTarget().getEntityId(), orchestration.getParameters());
    trace(method, SystemMessage.METHOD_EXIT);
    return new EventResult();
  }

  /**
   * The implementation of this post processor handler for bulk orchestration.
   * <p>
   * Organization post process events should not be handled in this method.
   *
   * @param processId         the identifier of the orchestration process.
   * @param eventId           the identifier of the orchestration event
   * @param bulkOrchestration the object containing contextual information
   *                          such as orchestration parameters, operation.
   */
  @Override
  public BulkEventResult execute(long processId, long eventId, BulkOrchestration bulkOrchestration) {
    final String method = "execute";

    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
    return new BulkEventResult();
  }

  /**
   * Checks if the update should be executed or not, and starts the update if it is required.
   *
   * @param entityId   The ID of the modified organization.
   * @param parameters The Orchestration parameter map.
   */
  private void processEvent(final String entityId, final Map<String, Serializable> parameters) {
    final String method = "processEvent";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      if (!parameters.containsKey(NSIS_CODE_ATTRIBUTE)) {
        debug(method, String.format("%s is not involved in the current modification.", NSIS_CODE_DISPLAY_NAME));
        return;
      }

      final List<String> organizationMemberKeys = getOrganizationMemberKeys(entityId);
      if (CollectionUtility.empty(organizationMemberKeys)) {
        debug(method, String.format("Organization %s has no members to be updated.", entityId));
        return;
      }

      final String loggedInUser = ContextManager.getOIMUser();
      OIMInternalClient oimClient = null;
      try {
        oimClient = CommonUtil.beginLegacyAuth();
        final List<ApplicationInstance> applicationInstances = getNsisApplicationInstances(oimClient);
        for (final ApplicationInstance application : applicationInstances) {
          updateAccounts((String) parameters.getOrDefault(NSIS_CODE_ATTRIBUTE, ""), application, organizationMemberKeys, oimClient);
        }
      } catch (LoginException e) {
        error(method, OrchestrationBundle.string(OrchestrationError.LOGIN_FAILED), e);
        throw eventFailed(e, method, OrchestrationError.LOGIN_FAILED, OrchestrationBundle.RESOURCE);
      } finally {
        if (oimClient != null) {
          CommonUtil.endLegacyAuth(oimClient, loggedInUser);
        }
      }
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Collects the accounts belonging to the application.
   *
   * @param code                   The new value of the attribute <code>CODE_NSIS</code>.
   * @param application            Application Instance object, the related accounts are searched.
   * @param organizationMemberKeys List of the organization member IDs.
   * @param oimClient              The OIMInternalClient object that provides access
   *                               to the service calls with administrator permission.
   */
  private void updateAccounts(final String code, final ApplicationInstance application, final List<String> organizationMemberKeys, final OIMInternalClient oimClient) {
    final String method = "updateAccounts";
    trace(method, SystemMessage.METHOD_ENTRY);

    final ProvisioningService provisioningService = oimClient.getService(ProvisioningService.class);
    try {
      List<Account> accounts = getNsisAccountsRelatedToOrganization(organizationMemberKeys, application, provisioningService);
      for (Account cursor : accounts) {
        Account account = provisioningService.getAccountDetails(Long.parseLong(cursor.getAccountID()));
        modifyAccount(code, account, provisioningService);
      }
    } catch (GenericProvisioningException | AccountNotFoundException e) {
      error(method, OrchestrationBundle.format(OrchestrationError.ACCOUNT_UPDATE, application.getApplicationInstanceName()), e);
      throw eventFailed(e, method, OrchestrationError.ACCOUNT_UPDATE, OrchestrationBundle.RESOURCE, application.getApplicationInstanceName());
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }


  /**
   * Sets the provided value on form field <code>OID</code> and performs a modification on the account.
   *
   * @param code                The new value of the attribute <code>CODE_NSIS</code>.
   * @param account             {@link Account} to be updated
   * @param provisioningService A {@link ProvisioningService} instance that has administrator permissions.
   */
  private void modifyAccount(final String code, final Account account, final ProvisioningService provisioningService)
    throws GenericProvisioningException, AccountNotFoundException {
    final String method = "modifyAccount";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final String form = account.getAppInstance().getAccountForm().getName();
      final String field = account.getAppInstance().getObjectName().equalsIgnoreCase(NSIS_RESOURCE_OBJECT_NAME) ? "_PTS" : "_OID";
      account.getAccountData().getData().replace(form + field, code);
      provisioningService.modify(account);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Providing the provisioned or enabled Accounts related to the given Application Instance
   * and filtered by given User ID list.
   *
   * @param organizationMemberKeys List of the organization member IDs.
   * @param application            Application Instance object, the related accounts are searched.
   * @param provisioningService    The ProvisioningService instance that has administrator permissions.
   * @return List of accounts returned by the query.
   */
  private List<Account> getNsisAccountsRelatedToOrganization(final List<String> organizationMemberKeys, final ApplicationInstance application, final ProvisioningService provisioningService) {
    final String method = "getNsisAccountsRelatedToOrganization";
    trace(method, SystemMessage.METHOD_ENTRY);

    SearchCriteria criteria = new SearchCriteria(
      new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), ProvisioningConstants.ObjectStatus.PROVISIONED.getId(), SearchCriteria.Operator.EQUAL),
      new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), ProvisioningConstants.ObjectStatus.ENABLED.getId(), SearchCriteria.Operator.EQUAL),
      SearchCriteria.Operator.OR);
    criteria = new SearchCriteria(criteria,
      new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), ProvisioningConstants.ObjectStatus.DISABLED.getId(), SearchCriteria.Operator.EQUAL),
      SearchCriteria.Operator.OR);

    try {
      return provisioningService.getProvisionedAccountsForAppInstance(application.getApplicationInstanceName(), criteria, null)
        .stream()
        .filter(account -> organizationMemberKeys.contains(account.getUserKey()))
        .collect(Collectors.toList());
    } catch (GenericProvisioningException e) {
      error(method, OrchestrationBundle.format(OrchestrationError.ACCOUNT_SEARCH, application.getApplicationInstanceName()), e);
      throw eventFailed(e, method, OrchestrationError.ACCOUNT_SEARCH, OrchestrationBundle.RESOURCE, application.getApplicationInstanceName());
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Searching for the <code>NSIS</code> related Application Instances.
   *
   * @param oimClient The OIMInternalClient object that provides access
   *                  to the service calls with administrator permission.
   * @return The list of the <code>NSIS</code> related Application Instances.
   */
  private List<ApplicationInstance> getNsisApplicationInstances(final OIMInternalClient oimClient) {
    final String method = "getNsisApplicationInstances";
    trace(method, SystemMessage.METHOD_ENTRY);

    final SearchCriteria criteria = new SearchCriteria(
        new SearchCriteria(new SearchCriteria(ApplicationInstance.OBJ_NAME, NSIS_PROD_RESOURCE_OBJECT_NAME, SearchCriteria.Operator.EQUAL),
                           new SearchCriteria(ApplicationInstance.OBJ_NAME, NSIS_TEST_RESOURCE_OBJECT_NAME, SearchCriteria.Operator.EQUAL),
                           SearchCriteria.Operator.OR),
        new SearchCriteria(ApplicationInstance.OBJ_NAME, NSIS_RESOURCE_OBJECT_NAME, SearchCriteria.Operator.EQUAL),
        SearchCriteria.Operator.OR);

    final ApplicationInstanceService applicationInstanceService = oimClient.getService(ApplicationInstanceService.class);
    try {
      return applicationInstanceService.findApplicationInstance(criteria, null);
    } catch (GenericAppInstanceServiceException e) {
      error(method, OrchestrationBundle.format(OrchestrationError.APPLICATION_SEARCH_BY_OBJ, NSIS_PROD_RESOURCE_OBJECT_NAME, NSIS_TEST_RESOURCE_OBJECT_NAME), e);
      throw eventFailed(e, method, OrchestrationError.APPLICATION_SEARCH_BY_OBJ, OrchestrationBundle.RESOURCE, NSIS_PROD_RESOURCE_OBJECT_NAME, NSIS_TEST_RESOURCE_OBJECT_NAME);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Returning the members of the provided organization ID.
   *
   * @param entityId The ID of the modified organization.
   * @return List of users who are members of the organization.
   */
  private List<String> getOrganizationMemberKeys(final String entityId) {
    final String method = "getOrganizationMemberKeys";
    trace(method, SystemMessage.METHOD_ENTRY);

    final OrganizationManager organizationManager = service(OrganizationManager.class);
    try {
      return organizationManager.getOrganizationMemberIds(entityId);
    } catch (OrganizationManagerException e) {
      error(method, OrchestrationBundle.format(OrchestrationError.ORGANIZATION_MEMBERS, entityId), e);
      throw eventFailed(e, method, OrchestrationError.ORGANIZATION_MEMBERS, OrchestrationBundle.RESOURCE, entityId);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}
