package bka.iam.identity.event.org;

import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationError;
import bka.iam.identity.event.OrchestrationHandler;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.iam.identity.exception.OrganizationManagerException;
import oracle.iam.identity.foundation.event.AbstractValidationHandler;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.kernel.ValidationException;
import oracle.iam.platform.kernel.ValidationFailedException;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.Orchestration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Offers common methods to validate organization objects in a
 * <code>ValidationHandler</code>.
 * <br>
 * The validator is implementing the following validation rules
 * on the attribute <code>Code NSIS</code>:
 * <br>
 * <ul>
 *   <li>If the parent organization is set to <b>NSIS</b> then:
 *      <ul>
 *          <li>Attribute <code>Code NSIS</code> must not be blank
 *          <li>Attribute <code>Code NSIS</code> must be unique
 *      </ul>
 *   <li>Otherwise:
 *       <ul>
 *         <li>Attribute <code>Code NSIS</code> must be unique, it is not blank
 *      </ul>
 * </ul>
 *
 */
public class OrganizationAttributeValidationHandler extends AbstractValidationHandler {

  /**
   * Custom attribute for organization entity to store the NSIS related code.
   */
  static final String NSIS_CODE_ATTRIBUTE = "code_nsis";

  /**
   * Display name of the attribute <code>code_nsis</code>.
   */
  static final String NSIS_CODE_DISPLAY_NAME = "Code NSIS";

  /**
   * Constant field, holds the name of the NSIS organization.
   */
  private static final String NSIS_ORGANIZATION_NAME = "NSIS";

  /**
   * The implementation of this validation handler in one-off orchestration.
   * <p>
   * All Organization validation events are handled in this method.
   *
   * @param  processId          the identifier of the orchestration process.
   * @param  eventId            the identifier of the orchestration event
   * @param  orchestration      the object containing contextual information
   *                            such as orchestration parameters, operation.
   *
   * @throws ValidationException       if the entry to create or modify
   *                                   violates the rules for the operation.
   * @throws ValidationFailedException if the entry to create or modify
   *                                   violates the rules for the operation.
   */
  @Override
  public void validate(long processId, long eventId, Orchestration orchestration)
    throws ValidationException
    , ValidationFailedException {

    final String method = "validate";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      switch (OrganizationManagerConstants.Operations.valueOf(orchestration.getOperation())) {
        case CREATE: onCreate(orchestration.getParameters());
                     break;
        case MODIFY: onModify(orchestration.getTarget().getEntityId(), orchestration.getParameters());
                     break;
      }
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * The implementation of this validation handler for bulk orchestration.
   * <p>
   * Organization validation events should not be handled in this method.
   *
   * @param  processId          the identifier of the orchestration process.
   * @param  eventId            the identifier of the orchestration event
   * @param  bulkOrchestration  the object containing contextual information
   *                            such as orchestration parameters, operation.
   *
   * @throws ValidationException       if the entry to create or modify
   *                                   violates the rules for the operation.
   * @throws ValidationFailedException if the entry to create or modify
   *                                   violates the rules for the operation.
   */
  @Override
  public void validate(long processId, long eventId, BulkOrchestration bulkOrchestration)
    throws ValidationException
    , ValidationFailedException {

    final String method = "validate";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  /**
   * Initializing the necessary data
   * in case of the create operation, for the upcoming calls,
   * from the orchestration parameters.
   *
   * @param parameters The <code>Orchestration</code> parameter map.
   */
  private void onCreate(final Map<String, Serializable> parameters) {
    final String method = "onCreate";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final String parentOrganizationName = OrchestrationHandler.organization(fetchOrchestrationData(parameters, OrganizationManagerConstants.PARENT_ORG_KEY), false);
      final String nsisCode = fetchOrchestrationData(parameters, NSIS_CODE_ATTRIBUTE);
      final SearchCriteria query = new SearchCriteria(NSIS_CODE_ATTRIBUTE, nsisCode, SearchCriteria.Operator.EQUAL);

      validateNsisCode(parentOrganizationName, nsisCode, query);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Initializing the necessary data
   * in case of the modify operation, for the upcoming calls,
   * from the orchestration parameters.
   *
   * @param entityId   The ID of the modified organization.
   * @param parameters The <code>Orchestration</code> parameter map.
   */
  private void onModify(final String entityId, final Map<String, Serializable> parameters) {
    final String method = "onModify";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final String parentOrganizationName = OrchestrationHandler.organization(fetchOrchestrationData(parameters, OrganizationManagerConstants.PARENT_ORG_KEY), false);
      final String nsisCode = fetchOrchestrationData(parameters, NSIS_CODE_ATTRIBUTE);
      final SearchCriteria query = new SearchCriteria(
        new SearchCriteria(NSIS_CODE_ATTRIBUTE, nsisCode, SearchCriteria.Operator.EQUAL),
        new SearchCriteria(OrganizationManagerConstants.AttributeName.ID_FIELD.getId(), entityId, SearchCriteria.Operator.NOT_EQUAL),
        SearchCriteria.Operator.AND);

      validateNsisCode(parentOrganizationName, nsisCode, query);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Decides what validation should be applied
   * based on the provided parameters.
   *
   * @param parentOrganizationName Defined by the orchestration
   *                               parameter <code>parent_key</code>.
   * @param nsisCode               Defined by the orchestration
   *                               parameter <code>code_nsis</code>.
   * @param query                  The <code>SearchCriteria</code> query.
   */
  private void validateNsisCode(final String parentOrganizationName, final String nsisCode, final SearchCriteria query) {
    final String method = "validateNsisCodeUniqueness";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      if (StringUtility.isEqual(parentOrganizationName, NSIS_ORGANIZATION_NAME)) {
        if (StringUtility.isBlank(nsisCode)) {
          throw validationFailed(OrchestrationError.ATTRIBUTE_MUST_NOT_BE_BLANK, OrchestrationBundle.RESOURCE, NSIS_CODE_DISPLAY_NAME);
        } else {
          validateNsisCodeUniqueness(query);
        }
      } else {
        if (!StringUtility.isBlank(nsisCode)) {
          validateNsisCodeUniqueness(query);
        }
      }
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Calling the appropriate method with the provided query,
   * then validate its result: if a returning collection is not empty,
   * a <code>ValidationFailedException</code> will be thrown.
   *
   * @param query The <code>SearchCriteria</code> query object.
   */
  private void validateNsisCodeUniqueness(final SearchCriteria query) {
    final String method = "validateNsisCodeUniqueness";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      List<Organization> organizationsByNsisCode = findOrganizationsByNsisCode(query);
      if (!CollectionUtility.empty(organizationsByNsisCode)) {
        throw validationFailed(OrchestrationError.ATTRIBUTE_MUST_BE_UNIQUE, OrchestrationBundle.RESOURCE, NSIS_CODE_DISPLAY_NAME);
      }
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Querying organizations by the <code>SearchCriteria</code>.
   *
   * @param query The <code>SearchCriteria</code> query
   *              for searching all the organizations
   *              with the same <code>NSIS Code</code> attribute value.
   *              In case of modification operation, the query includes
   *              an exclusion sub-query for the target organization.
   * @return      List of the organizations. All of these organizations
   *              have the same <code>NSIS Code</code> attribute value.
   */
  private List<Organization> findOrganizationsByNsisCode(final SearchCriteria query) {
    final String method = "findOrganizationsByNsisCode";
    trace(method, SystemMessage.METHOD_ENTRY);

    Set<String> returnArgs = new HashSet<>();
    returnArgs.add(OrganizationManagerConstants.AttributeName.ID_FIELD.getId());

    final OrganizationManager organizationManager = Platform.getService(OrganizationManager.class);
    try {
      return organizationManager.search(query, returnArgs, null);
    } catch (OrganizationManagerException e) {
      error(method, OrchestrationBundle.format(OrchestrationError.ORGANIZATION_SEARCH_ERROR, query), e);
      throw validationFailed(e, OrchestrationError.ORGANIZATION_SEARCH_ERROR, OrchestrationBundle.RESOURCE, query);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}
