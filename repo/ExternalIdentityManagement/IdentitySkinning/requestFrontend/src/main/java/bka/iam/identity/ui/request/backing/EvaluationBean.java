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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   MyAccessAccounts Page Customization

    File        :   EvaluationBean.java

    Compiler    :   JDK 1.8

    Author      :

    Purpose     :   This file implements the class
                    AccountView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.ui.request.backing;

import bka.iam.identity.ui.RequestError;
import bka.iam.identity.ui.RequestMessage;
import bka.iam.identity.ui.resource.Bundle;
import com.thortech.xl.systemverification.api.DDKernelService;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.faces.ADF;
import oracle.hst.foundation.faces.JSF;
import oracle.iam.identity.frontend.AbstractManagedBean;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.context.ContextAwareNumber;
import oracle.iam.platform.context.ContextManager;
import oracle.iam.platform.kernel.EventFailedException;
import oracle.iam.platform.kernel.OrchestrationEngine;
import oracle.iam.platform.kernel.vo.*;
import oracle.iam.ui.platform.model.common.OIMClientFactory;
import oracle.iam.ui.platform.utils.SystemUtils;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * EvaluationBean, a backing scoped managed bean to handle customized events on the my-access-accounts page.
 */
public class EvaluationBean extends AbstractManagedBean {

  /**
   * The official serial version ID which says cryptically which version we're compatible with.
   */
  private static final long serialVersionUID    = -4627980345471104553L;
  private static final String ORCHESTRATION_SQL = "select ORCHESTRATION from ORCHPROCESS where ID =?";
  private static final String CHILD_SQL         = "select STATUS, ORCHESTRATION from ORCHPROCESS where PARENTPROCESSID =?";

  /**
   * Triggers the access policy evaluation process immediately
   * only for the selected user. This method is called from the UI.
   */
  public void evaluate() {
    final String method = "evaluate";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      final String beneficiary          = getBeneficiaryKey();
      final DDKernelService facade      = getDDKernelService();
      final Orchestration orchestration = initializeOrchestration(beneficiary);
      context(false);

      final OrchestrationResult result = (OrchestrationResult) facade.invoke("orchestrate", new Object[]{orchestration}, new Class[]{AbstractGenericOrchestration.class});
      raiseOrchestrationResult(result, beneficiary);
    } catch (RuntimeException | SQLException | NamingException | IOException | ClassNotFoundException e) {
      raiseInternalError(e);
    } finally {
      context(true);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  /**
   * Ensure the JOBHISTORYID context value exists otherwise the InitiatePolicyEvaluationAndProvisioning handler fails.
   *
   * @param clear defines if the context has to cleared or initialized
   */
  private void context(final boolean clear) {
    if (clear) {
      Object value = ContextManager.getValue("JOBHISTORYID");
      if (value != null && (value instanceof ContextAwareNumber && (Long) ((ContextAwareNumber) value).getObjectValue() == -1L)) {
        ContextManager.setValue("JOBHISTORYID", null);
      }
    } else {
      if (ContextManager.getValue("JOBHISTORYID") == null) {
        ContextManager.setValue("JOBHISTORYID", new ContextAwareNumber("-1"));
      }
    }
  }

  /**
   * Initializes the orchestration which will be executed to achieve the evaluation.
   *
   * @param beneficiary The key of user who has to be evaluated.
   * @return A parameterized orchestration instance to be executed.
   */
  private Orchestration initializeOrchestration(final String beneficiary) {
    OrchestrationTarget target = new EntityOrchestration("User", beneficiary);
    Orchestration orchestration = new Orchestration("EVALUATE_POLICIES", true, target, false, true);
    orchestration.setTargetUserIds(new String[]{beneficiary});
    return orchestration;
  }

  /**
   * Handles the result of the orchestration, shows a message to the user
   * based on the success of the evaluation process.
   *
   * @param result      The result of the orchestration.
   * @param beneficiary The key of the beneficiary user.
   */
  private void raiseOrchestrationResult(final OrchestrationResult result, final String beneficiary) throws SQLException, NamingException, IOException, ClassNotFoundException {
    if (OrchestrationEngine.ProcessStatus.COMPLETED == result.getProcessStatus()) {
      boolean childrenFailed = lookupChildren(result.getProcessId());
      if (!childrenFailed) {
        info(String.format("Access policy evaluation process with id [%d] successfully finished for user: %s.", result.getPId().getId(), beneficiary));
        raiseTaskFlowFeedbackEvent(Bundle.string(RequestMessage.EVALUATION_SUCCEEDED));
      }
    } else {
      warning(String.format("Access policy evaluation process with id [%d] resulted in %s status for user: %s.", result.getPId().getId(), result.getProcessStatus().toString(), beneficiary));
      lookupResult(result.getProcessId());
    }
  }


  private void lookupResult(final long processId) throws NamingException, SQLException, IOException, ClassNotFoundException {
    InitialContext context = new InitialContext();
    DataSource datasource = (DataSource) context.lookup("jdbc/operationsDB");

    try (Connection connection = datasource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(ORCHESTRATION_SQL);
      preparedStatement.setLong(1, processId);

      logger().debug("SQL: " + ORCHESTRATION_SQL + "[" + processId + "]");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Blob blob = resultSet.getBlob("ORCHESTRATION");
        processResult(blob, processId);
      }
    }
  }

  private boolean lookupChildren(final long processId) throws NamingException, SQLException, IOException, ClassNotFoundException {
    InitialContext context = new InitialContext();
    DataSource datasource = (DataSource) context.lookup("jdbc/operationsDB");
    boolean failed = false;

    try (Connection connection = datasource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(CHILD_SQL);
      preparedStatement.setLong(1, processId);

      logger().debug("SQL: " + CHILD_SQL + "[" + processId + "]");
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        String status = resultSet.getString("STATUS");
        if (OrchestrationEngine.ProcessStatus.getStatus(status) != OrchestrationEngine.ProcessStatus.COMPLETED) {
          Blob blob = resultSet.getBlob("ORCHESTRATION");
          processResult(blob, processId);
          failed = true;
        }
      }
    }
    return failed;
  }

  private void processResult(final Blob blob, final long processId) throws IOException, SQLException, ClassNotFoundException {
    try (ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(blob.getBinaryStream()))) {
      Object object = ois.readObject();
      if (object instanceof oracle.iam.platform.kernel.Process) {
        Serializable result = ((oracle.iam.platform.kernel.Process) object).getResult();
        if (result instanceof EventFailedException) {
          raiseException((EventFailedException) result);
        } else {
          raiseUnknownResult(processId);
        }
      } else {
        raiseUnknownResult(processId);
      }
    }
  }

  /**
   * Provides the key of the user who has to be evaluated.
   * If the user was selected from the Manage Users page, the <code>userLogin</code>
   * page parameter must be presented and in spite of the name it contains the key, not the login.
   * <br/>
   * In case of the <code>userLogin</code> is null, the event is originated from the My Access page,
   * what is definitely a self-operation, and a <code>OIMClientFactory.getLoginUserKey()</code> can be
   * used to provide the key of the beneficiary user.
   *
   * @return The key of the beneficiary user.
   */
  private String getBeneficiaryKey() {
    final Map<String, Object> scope = ADF.pageFlowScope();
    Object userKey = scope.get("userLogin");
    return userKey != null ? (String) userKey : OIMClientFactory.getLoginUserKey();
  }

  /**
   * In case of exception, this method raise a popup message which indicate the
   * raising error message.
   *
   * @param cause the causing exception that should be displayed
   *              on UI.
   */
  private void raiseInternalError(final Exception cause) {
    fatal(cause);
    ADF.rootPort().clearException();
    JSF.showErrorMessage(Bundle.string(RequestError.EVALUATION_INTERNAL_ERROR));
  }

  /**
   * In case of exception, this method raise a popup message which indicate the
   * raising error message.
   *
   * @param cause the causing exception that should be displayed
   *              on UI.
   */
  private void raiseException(final EventFailedException cause) {
    fatal(cause);
    JSF.showErrorMessage(cause.getLocalizedMessage());
  }

  /**
   * In case of an unexpected error, this method raise a popup message which indicate the
   * unknown error.
   *
   * @param processId processId what resulted in an unknown error.
   */
  private void raiseUnknownResult(final long processId) {
    warning(String.format("Process %d resulted in an unexpected error.", processId));
    JSF.showErrorMessage(Bundle.format(RequestError.EVALUATION_UNKNOWN_ERROR, processId));
  }

  /**
   * Returns a DDKernelService. Login is not necessary as long as the functionality
   * available only for the System Administrators.
   *
   * @return A DDKernelService instance.
   */
  private DDKernelService getDDKernelService() {
    final Hashtable<String, Object> env = new Hashtable<>();
    final String url = SystemUtils.getProperty("providerURL");
    if (url != null) {
      env.put("java.naming.provider.url", url);
    }

    OIMClient client = new OIMClient(env);
    return client.getService(DDKernelService.class);
  }
}
