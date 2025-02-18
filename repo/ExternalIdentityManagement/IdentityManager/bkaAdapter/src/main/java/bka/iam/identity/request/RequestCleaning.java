package bka.iam.identity.request;

import bka.iam.identity.ProcessError;
import bka.iam.identity.ProcessException;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.StringUtility;
import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.request.api.RequestService;
import oracle.iam.request.exception.NoRequestPermissionException;
import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.vo.Request;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestSearchCriteria;
import oracle.iam.request.vo.RequestStage;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The <code>RequestCleaning</code> closes all the requests
 * that are older than two months and still in a pending stage.
 */
public class RequestCleaning extends AbstractSchedulerTask {


  /**
   * the category of the logging facility to use
   */
  private static final String LOGGER_CATEGORY = "BKA.REQUEST.CLEANUP";

  /**
   * A RequestService instance to perform the API operations
   */
  private RequestService requestService;

  /**
   * A collection of a possible RequestStage values
   */
  private List<RequestStage> requestStages;

  /**
   * Constructs an empty <code>RequestCleaning</code> scheduler instance that allows use as a JavaBean.
   * <br>
   * Zero argument constructor required by the framework.
   * <br>
   * Default Constructor
   */
  public RequestCleaning() {
    super(LOGGER_CATEGORY);
  }

  /**
   * Returns the array with names which should be populated from the scheduled task definition of Oracle Identity Manager.
   * @return the array with names which should be populated. Possible object is array of TaskAttribute.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return new TaskAttribute[0];
  }

  /**
   * The initialization task.
   * @throws TaskException in case an error does occur.
   */
  @Override
  public void initialize() throws TaskException {
    super.initialize();

    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      this.requestService = service(RequestService.class);
      debug(method, "Fetching request stages");
      this.requestStages = requestService.getRequestStages();
      debug(method, String.format("Request stages %s are fetched", requestStages));
    } catch (RequestServiceException e) {
      fatal(method, e);
      throw new ProcessException(ProcessError.REQUEST_STAGES_FAILED, e);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * The entry point of the access policy cleaning task to perform.
   * @throws TaskException in case an error does occur.
   */
  @Override
  protected void onExecution() throws TaskException {
    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      List<Request> requests = findRequests();
      debug(method, String.format("%d requests were found to close", requests.size()));
      for (Request request : requests) {
        try {
          closeRequest(request);
        } catch (RequestServiceException | NoRequestPermissionException e) {
          fatal(method, e);
        }
      }
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * The implementation of a query that should be returning all the closable requests.
   *
   * @return a {@link List} of {@link Request} returned by the API.
   * @throws TaskException in case an error does occur.
   */
  private List<Request> findRequests() throws TaskException {
    final String method = "findRequests";
    trace(method, SystemMessage.METHOD_ENTRY);

    Set<String> returnArgs = new HashSet<>();
    returnArgs.add(RequestConstants.REQUEST_ID);
    LocalDateTime deadline = LocalDateTime.now().minus(2, ChronoUnit.MONTHS);
    RequestSearchCriteria searchCriteria = new RequestSearchCriteria();
    try {
      searchCriteria.setConjunctionOp(RequestSearchCriteria.Operator.AND);
      searchCriteria.addExpression(RequestConstants.REQUEST_STATE, RequestConstants.REQUEST_STATE_PENDING, RequestSearchCriteria.Operator.EQUAL);
      searchCriteria.addExpression(RequestConstants.REQUEST_CREATION_DATE, Timestamp.valueOf(deadline), RequestSearchCriteria.Operator.BEFORE);
      debug(method, String.format("Searching requests that are older than two months, and still in pending stage. Query: [%s]", searchCriteria));
      return requestService.search(searchCriteria, returnArgs, null);
    } catch (RequestServiceException e) {
      throw new ProcessException(ProcessError.REQUEST_SEARCH_FAILED, e);
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Checks if the request can be closed and closes if it can be, otherwise extends the close operation to all its children.
   *
   * @param request A {@link Request} instance to close.
   * @throws RequestServiceException generic request API exception, unexpected error happened.
   * @throws NoRequestPermissionException user has no permission to perform the operation.
   */
  private void closeRequest(final Request request) throws RequestServiceException, NoRequestPermissionException {
    final String method = "closeRequest";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      if (request.isRequestWithdrawAllowed(requestStages)) {
        debug(method, String.format("Closing request: [%s]", request.getRequestID()));
        requestService.closeRequest(request.getRequestID());
        debug(method, String.format("Request [%s] was closed successfully", request.getRequestID()));
      } else {
        debug(method, String.format("Request [%S] is not in a closable state", request.getRequestID()));
        closeChildRequests(request);
      }
    } catch (RequestServiceException e) {
      fatal(method, e);
      //if any of the child requests have been already closed or failed, it results in IAM-2050084
      if (StringUtility.isEqual(e.getErrorCode(), "IAM-2050084")) {
        //close all other child requests (those are still open) manually and the parent request's status will be resolved automatically
        debug(method, String.format("Some of the child requests of request [%s] have been already closed or failed", request.getRequestID()));
        closeChildRequests(request);
      } else {
        throw e;
      }
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Searches all the child requests of the given request
   * and invokes <code>closeRequest</code> to each of them.
   *
   * @param request {@link Request} instance that supposed to be the parent in the query.
   * @throws RequestServiceException generic request API exception, unexpected error happened.
   * @throws NoRequestPermissionException user has no permission to perform the operation.
   */
  private void closeChildRequests(final Request request) throws RequestServiceException, NoRequestPermissionException {
    final String method = "closeChildRequests";
    trace(method, SystemMessage.METHOD_ENTRY);
    debug(method, String.format("Searching child request of [%s]", request.getRequestID()));
    try {
      List<Request> childRequests = requestService.getChildRequests(request.getRequestID());
      debug(method, String.format("%d child requests were found to request [%s]", childRequests.size(), request.getRequestID()));
      debug(method, String.format("Closing child requests of [%s]", request.getRequestID()));
      for (Request childRequest : childRequests) {
        try {
          closeRequest(childRequest);
        } catch (RequestServiceException | NoRequestPermissionException e) {
          fatal(method, e);
        }
      }
    } finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}