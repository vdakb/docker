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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   AccountRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountRequest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.request;

import java.util.List;

import com.thortech.xl.dataaccess.tcDataProvider;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import oracle.iam.platform.Platform;

import oracle.iam.platform.config.DiscoveryConfig;

import oracle.iam.platform.workflowservice.api.WorkflowProvider;
import oracle.iam.platform.workflowservice.vo.WorkflowDefinition;

import oracle.iam.platform.workflowservice.exception.IAMWorkflowException;

import oracle.iam.platform.workflowservice.impl.BPELProvider;

import oracle.iam.request.vo.Request;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestSearchCriteria;

import oracle.iam.request.api.RequestService;

import oracle.iam.request.exception.RequestServiceException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.ots.resource.RequestBundle;

////////////////////////////////////////////////////////////////////////////////
// class AccountRequest
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>AccountRequest</code> acts as the service end point for the
 ** Oracle Identity Manager to provision account properties to an Offline
 ** Service.
 ** <br>
 ** This is wrapper class has methods for account operations like create
 ** account, delete account, enable account, disable account, modify account
 ** etc.
 ** <br>
 ** This class internally calls {@link RequestService} to instancetiate the
 ** SOA composite a full fillment user has to approve after (s)he proceed all
 ** actions required and returns appropriate message code.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class AccountRequest extends EntityRequest {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final WorkflowDefinition definition;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  enum Type {
    APPLICATION("Application")
  , ENTITLEMENT("Entitlement");

    private final String name;

    private Type(final String name) {
      this.name = name;
    }
  }

  enum Operation {
    CREATE, DELETE, ENABLE, DISABLE, MODIFY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountRequest</code> task adapter.
   **
   ** @param  provider           the session provider connection
   ** @param  partition          the name of the partition in which the SOA
   **                            composite application is deployed. The default
   **                            value is <code>default</code>.
   ** @param  name               the name of the SOA composite application.
   ** @param  revision           the revision ID of the composite related to
   **                            Oracle Identity Manager and/or Oracle SOA
   **                            Suite.
   ** @param  category           the <code>category</code> of the workflow
   **                            related to Oracle Identity Manager. Normally
   **                            <code>BPEL</code>.
   ** @param  service            the entry point to the composite workflow to
   **                            start a process.
   ** @param  operationID        the <code>operationID</code> of the workflow
   **                            related to Oracle Identity Manager.
   ** @param  payloadID          the id of the payload of the workflow in
   **                            Oracle Identity Manager to handle.
   */
  public AccountRequest(final tcDataProvider provider, final String partition, final String name, final String revision, final String category, final String service, final String operationID, final String payloadID) {
    // ensure inheritance
    super(provider);

    // initailize instance
    this.definition = new WorkflowDefinition(name, category, "BPEL", service, partition, revision, payloadID, operationID, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gruped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the account create
   ** request.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  processTask        the process task key providing the data.
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **
   ** @return                    an appropriate response message.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   */
  public final String create(final Long processInstance, final Long processTask, final String path) {
    return invoke(processInstance, processTask, path, Operation.CREATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the account delete
   ** request.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  processTask        the process task key providing the data.
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **
   ** @return                    an appropriate response message.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   */
  public final String delete(final Long processInstance, final Long processTask, final String path) {
    return invoke(processInstance, processTask, path, Operation.DELETE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the account enable
   ** request.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  processTask        the process task key providing the data.
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **
   ** @return                    an appropriate response message.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   */
  public final String enable(final Long processInstance, final Long processTask, final String path) {
    return invoke(processInstance, processTask, path, Operation.ENABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the account
   ** disable request.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  processTask        the process task key providing the data.
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **
   ** @return                    an appropriate response message.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   */
  public final String disable(final Long processInstance, final Long processTask, final String path) {
    return invoke(processInstance, processTask, path, Operation.DISABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the account modify
   ** request.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  processTask        the process task key providing the data.
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **
   ** @return                    an appropriate response message.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   */
  public final String modify(final Long processInstance, final Long processTask, final String path) {
    return invoke(processInstance, processTask, path, Operation.MODIFY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the account
   ** disable request.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  processTask        the process task key providing the data.
   ** @param  path               the path to the configration stored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   ** @param  operation          the operation to excecute.
   **
   ** @return                    an appropriate response message.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   */
  protected final String invoke(final Long processInstance, final Long processTask, final String path, final Operation operation) {
    final String method = "invoke";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // create the task descriptor that provides the attribute mapping and
      // transformations to be applied on the mapped attributes
      unmarshal(path);

      final WorkflowProvider provider = new BPELProvider();
      final AccountPayload   payload  = createPayload(processInstance, processTask, operation, Type.APPLICATION);
      provider.initiate(payload.marshal(), this.definition);
      return SUCCESS;
    }
    catch (IAMWorkflowException e) {
      if ("IAM-2010043".equals(e.getErrorCode())) {
        // can return "IAM-2010043" --> The composite %1$s does not exist on SOA server
        error(method, RequestBundle.format(RequestError.COMPOSITE_NOT_FOUND, this.definition.getName()));
        return RequestError.COMPOSITE_NOT_FOUND;
      }
      else
        return TaskError.UNHANDLED;
    }
    catch (TaskException e) {
      error(method, e.getLocalizedMessage());
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPayload
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the request.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  operation          the operation to excecute.
   ** @param  entityType         the type of the entity the operation belngs to.
   **
   ** @return                    the {@link AccountPayload} providing the
   **                            appropriate process data.
   **
   ** @throws TaskException      if not all data required to create the payload
   **                            wrapper could be obtained from the several
   **                            sources.
   */
  private AccountPayload createPayload(final Long processInstance, final Long processTask, final Operation operation, final Type entityType)
    throws TaskException {

    final String method = "createPayload";
    trace(method, SystemMessage.METHOD_ENTRY);

    final AccountPayload        payload   = new AccountPayload();
    final PreparedStatementUtil statement = new PreparedStatementUtil();
    final String                query     = "select orc.request_key,orc.orc_tos_instance_key,oiu.oiu_key,app_instance.app_instance_key,app_instance.app_instance_display_name,obj.obj_name,usr.usr_first_name, usr.usr_last_name,usr.usr_login,svr.svr_name from orc orc, obi obi,obj obj,usr usr,oiu oiu left outer join app_instance app_instance on oiu.app_instance_key=app_instance.app_instance_key left outer join svr svr on svr.svr_key = app_instance.itresource_key where oiu.usr_key=usr.usr_key and oiu.orc_key=orc.orc_key and oiu.obi_key=obi.obi_key and obi.obj_key=obj.obj_key and orc.orc_key= ?";
    statement.setStatement(this.provider, query);
    statement.setLong(1, processInstance);
    try {
      statement.execute(1);
      final tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        String requestKey  = dataSet.getString("request_key") != null ? dataSet.getString("request_key") : "";
        String instanceKey = dataSet.getString("app_instance_key") != null ? dataSet.getString("app_instance_key") : "";
        payload.requestKey(requestKey);
        payload.requesterLogin(requesterLogin(requestKey));
        payload.beneficiaryLogin(dataSet.getString("usr_login"));
        payload.beneficiaryFirstName(dataSet.getString("usr_first_name"));
        payload.beneficiaryLastName(dataSet.getString("usr_last_name"));
        payload.instanceName(dataSet.getString("app_instance_display_name") != null ? dataSet.getString("app_instance_display_name") : "");
        payload.objectName(dataSet.getString("obj_name") != null ? dataSet.getString("obj_name") : "");
        payload.resourceName(dataSet.getString("svr_name") != null ? dataSet.getString("svr_name") : "");
        payload.processInstance(dataSet.getString("oiu_key"));
        payload.processTask(String.valueOf(processTask));
        payload.descriptiveField(dataSet.getString("orc_tos_instance_key"));
        payload.operation(operation.name());
        payload.callbackURL(callbackURL());
        if (entityType == Type.APPLICATION) {
           payload.entityKey(instanceKey);
        }
      }
    }
    catch (Exception e) {
      error(method, RequestBundle.format(RequestError.PAYLOAD, processInstance, processTask));
      throw new RequestException(RequestError.PAYLOAD, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return payload;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requesterLogin
  /**
   ** Invokes SOA to instantiate a new BPEL process to handle the request.
   **
   ** @param  requestKey         the internal identifier of the request the
   **                            login name of the requester has to be resolved
   **                            from.
   **
   ** @return                    the login name of the identity that created the
   **                            request.
   **
   ** @throws TaskException      if not all data required to create the payload
   **                            wrapper could be obtained from the several
   **                            sources.
   */
  private String requesterLogin(final String requestKey)
    throws TaskException {

    final String method = "requesterLogin";
    trace(method, SystemMessage.METHOD_ENTRY);

    String requesterLogin = "";
    // prevent bogus input
    if (StringUtility.isEmpty(requestKey))
      return requesterLogin;

    final RequestService       service = service(RequestService.class);
    final RequestSearchCriteria filter = new RequestSearchCriteria();

    String requester = "";
    try {
      filter.addExpression(RequestConstants.REQUESTER_KEY, requestKey, RequestSearchCriteria.Operator.EQUAL);
      final List<Request> result = service.search(filter, null, null);
      if (result != null) {
        for (Request request : result)
          requester = request.getRequesterKey();
      }
    }
    catch (RequestServiceException e) {
      if ("IAM-2050081".equals(e.getErrorCode()))
        throw TaskException.unhandled(e);
      else if ("IAM-2050074".equals(e.getErrorCode()))
        throw TaskException.general(e);
    }

    if (!StringUtility.isEmpty(requester)) {
      try {
        final PreparedStatementUtil statement = new PreparedStatementUtil();
        statement.setStatement(this.provider, "select usr_login from usr where usr_key=?");
        statement.setString(1, requester);
        statement.execute(1);
        final tcDataSet dataSet = statement.getDataSet();
        if (dataSet.getRowCount() > 0)
          requesterLogin = dataSet.getString("usr_login");
      }
      catch (Exception e) {
        final String[] arguments = {requestKey, requester};
        throw new RequestException(RequestError.REQUESTER, arguments, e);
      }
      finally {
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
    return requesterLogin;
  }

  private String noteDate(final Long processTask) {
    final String method = "noteDate";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String                query     = "select osi_note from osi where sch_key=? ";
    final PreparedStatementUtil statement = new PreparedStatementUtil();

    String note = SystemConstant.EMPTY;
    try {
      statement.setStatement(this.provider, query);
      statement.setLong(1, processTask.longValue());
      statement.execute(1);
      final tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        dataSet.goToRow(0);
        note = dataSet.getString("osi_note");
      }
    }
    catch (Exception e) {
      error(method, RequestBundle.format(RequestError.NOTE, processTask));
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return note;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   callbackURL
  /**
   ** Returns the URL that has to be passed to the SOA composite to ...
   **
   ** @return                    the URL that has to be passed to the SOA
   **                            composite to ...
   */
  private String callbackURL() {
    final DiscoveryConfig setting  = Platform.getConfiguration().getDiscoveryConfig();
    return setting.getOimFrontEndURL() + "/provisioning-callback/ProvisioningCallbackService";
  }
}