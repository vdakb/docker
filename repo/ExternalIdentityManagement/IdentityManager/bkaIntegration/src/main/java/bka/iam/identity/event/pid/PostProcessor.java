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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Governance Services Integration

    File        :   IdentityPreProcessor.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityPreProcessor.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.event.pid;

import java.util.Map;
import java.util.HashMap;

import java.io.Serializable;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.pluginframework.Plugin;
import oracle.iam.platform.pluginframework.PluginFramework;
import oracle.iam.platform.pluginframework.PluginStoreException;

import oracle.iam.platform.entitymgr.EntityManager;
import oracle.iam.platform.entitymgr.ProviderException;
import oracle.iam.platform.entitymgr.StaleEntityException;
import oracle.iam.platform.entitymgr.NoSuchEntityException;
import oracle.iam.platform.entitymgr.InvalidDataTypeException;
import oracle.iam.platform.entitymgr.UnknownAttributeException;
import oracle.iam.platform.entitymgr.InvalidDataFormatException;
import oracle.iam.platform.entitymgr.UnsupportedOperationException;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

import bka.iam.identity.pid.Context;

import bka.iam.identity.rest.ServiceResource;

////////////////////////////////////////////////////////////////////////////////
// class PostProcessor
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This custom post process event handler assigning
 ** <code>Anonymous Identifier</code> at the time a new identity is on-boarded.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PostProcessor extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PREFIX_PARAMETER   = "igs.pid.prefix";
  private static final String PROFILE_PARAMETER  = "igs.pid.profile";
  private static final String ENDPOINT_PARAMETER = "igs.pid.endpoint";

  private static final String PREFIX_DEFAULT     = "p";
  private static final String PROFILE_DEFAULT    = "anonymized";
  private static final String ENDPOINT_DEFAULT   = "IM.PID.Endpoint";

  /** the category of the logging facility to use */
  static final String         LOGGER_CATEGORY    = "BKA.IDENTITY.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // instace attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  prefix                         = PREFIX_DEFAULT;
  private String  profile                        = PROFILE_DEFAULT;
  private String  endpoint                       = ENDPOINT_DEFAULT;
  /** the abstraction layer to describe the connection to the target system */
  private Context context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>PostProcessor</code> notification handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PostProcessor() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
  /**
   ** The implementation of this post process event handler in one-off
   ** orchestration.
   ** <p>
   ** All User prepopulate events are handled in this method
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   **
   ** @return                    a {@link EventResult} indicating the
   **                            operation has proceed.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public EventResult execute(long processId, long eventId, Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    // Event Result is a way for the event handler to notify the kernel of any
    // failures or errors and also if any subsequent actions need to be taken
    // (immediately or in a deferred fashion). It can also be used to indicate
    // if the kernel should restart this orchestration or veto it if the event
    // handler doesn't want to notify the kernel of anything it shouldn't return
    // a null value  instead an empty EventResult object
    EventResult result = null;
    try {
      // configure the REST service to obtain a anonymous identifier per
      // identity
      // cannot be done in the initialization phase of the handler due to the
      // IT Resource might not configured at the time the handler is initialized
      this.context = Context.build(this, ServiceResource.build(this, this.endpoint));
      // use the EntityManager to avoid triggering potential EventHandlers that
      // are doing the same stuff on regular bases
      final EntityManager manager = service(EntityManager.class);
      // process this single event
      if (CREATE.equals(orchestration.getOperation())) {
        // contains values of user
        result = modifyIdentity(manager, orchestration.getParameters());
      }
      else if (DELETE.equals(orchestration.getOperation())) {
        // contains old and new values of user
        result = deleteIdentifier(orchestration.getInterEventData());
      }
    }
    catch (TaskException e) {
      fatal(method, e);
      result = new EventResult();
      result.setFailureReason(e);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
  /**
   ** The implementation of this post process event handler for bulk
   ** orchestration.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestration parameters, operation.
   **
   ** @return                    a {@link BulkEventResult} indicating the
   **                            operation has proceed.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public BulkEventResult execute(final long processId, final long eventId, final BulkOrchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    // even if you don't implement a bulk handler you generally want to return
    // the BulkEventResult class otherwise bulk orchestrations will error out
    // and orphan
    final BulkEventResult result = new BulkEventResult();
    try {
      // configure the REST service to obtain a anonymous identifier per
      // identity
      // cannot be done in the initialization phase of the handler due to the
      // IT Resource might not configured at the time the handler is initialized
      this.context = Context.build(this, ServiceResource.build(this, this.endpoint));
      // use the EntityManager to avoid triggering potential EventHandlers that
      // are doing the same stuff on regular bases
      final EntityManager manager = service(EntityManager.class);

      // obtain every changes from all users
      final HashMap<String, Serializable>[] parameter = orchestration.getBulkParameters();
      // iterate each affected user
      for (int i = 0; i < parameter.length; i++)  {
        // process this single event
        if (CREATE.equals(orchestration.getOperation())) {
          // contains values of user
          modifyIdentity(manager, parameter[i]);
        }
        else if (DELETE.equals(orchestration.getOperation())) {
          // contains old and new values of user
          deleteIdentifier(orchestration.getInterEventData());
        }
      }
    }
    catch (TaskException e) {
      fatal(method, e);
      result.setFailureReason(e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** Called during creation of the orchestration engine at server startup.
   **
   ** @param  parameter          the parameter mapping passed to the
   **                            {@link AbstractPostProcessHandler} obtained
   **                            from the descriptor and send by the
   **                            <code>Orchestration</code>.
   */
  @Override
  public void initialize(final HashMap<String, String> parameter) {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      final Plugin plugin = PluginFramework.getPluginRegistry().getPlugin(EVENT, this.getClass().getName());
      final Map    data   = plugin.getMetadata();
      if (data.containsKey(PREFIX_PARAMETER))
        this.prefix = (String)data.get(PREFIX_PARAMETER);
      if (data.containsKey(PROFILE_PARAMETER))
        this.profile = (String)data.get(PROFILE_PARAMETER);
      if (data.containsKey(ENDPOINT_PARAMETER))
        this.endpoint = (String)data.get(ENDPOINT_PARAMETER);
    }
    catch (PluginStoreException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyIdentity
  /**
   ** Modifies the effected identities by requesting a new anonymous identifier
   ** from the Service Provider and store it in the configured attribute.
   **
   ** @param  manager            the {@link EntityManager} performing the
   **                            transaction.
   **                            <br>
   **                            Allowed object is {@link EntityManager}.
   ** @param  data               the event data needed to generate a
   **                            <code>Anonymous Identifier</code> and assigned
   **                            to the belonging identity.
   **                            <br>
   **                            Allowed object is {@link HashMap} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Serializable} for the value.
   **
   ** @return                    the result of the event.
   **                            <br>
   **                            Possible object is {@link EventResult}.
   */
  protected EventResult modifyIdentity(final EntityManager manager, final HashMap<String, Serializable> data)  {
    final String method  = "modifyIdentity";
    trace(method, SystemMessage.METHOD_ENTRY);

    // Event Result is a way for the event handler to notify the kernel of any
    // failures or errors and also if any subsequent actions need to be taken
    // (immediately or in a deferred fashion). It can also be used to indicate
    // if the kernel should restart this orchestration or veto it if the event
    // handler doesn't want to notify the kernel of anything it shouldn't return
    // a null value  instead an empty EventResult object
    final EventResult result = new EventResult();
    // use the EntityManager to avoid triggering potential EventHandlers that
    // are doing the same stuff on regular bases
    final String sid = fetchOrchestrationData(data, UserManagerConstants.AttributeName.USER_KEY.getId());
    final String uid = fetchOrchestrationData(data, UserManagerConstants.AttributeName.USER_LOGIN.getId());
    try {
      manager.modifyEntity(UserManagerConstants.USER_ENTITY, sid, createIdentifier(uid));
    }
    catch (ProviderException | NoSuchEntityException | UnknownAttributeException | UnsupportedOperationException e) {
      fatal(method, e);
      result.setFailureReason(e);
    }
    catch (StaleEntityException | InvalidDataTypeException | InvalidDataFormatException e) {
      fatal(method, e);
      result.setFailureReason(e);
    }
    catch (TaskException e) {
      fatal(method, e);
      result.setFailureReason(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  /**
   ** Build and executes the request to a new anonymous identifier at the
   ** Service Provider for the specified user resource <code>usedby</code>.
   **
   ** @param  usedby             the user resource to create the anonymous
   **                            identifier for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully created anonymous
   **                            identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  private HashMap<String, Object> createIdentifier(final String usedby)
    throws TaskException {

    final String method  = "createIdentifier";
    trace(method, SystemMessage.METHOD_ENTRY);
    final HashMap<String, Object> attribute = new HashMap<String, Object>();
    try {
      attribute.put(this.profile, this.context.createIdentifier(this.prefix, usedby));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  /**
   ** Build and executes the request to delete the provided new anonymous
   ** identifier at the Service Provider.
   **
   ** @param  usedby             the user resource to create the anonymous
   **                            identifier for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully created anonymous
   **                            identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  private EventResult deleteIdentifier(final HashMap<String, Serializable> data) {
    final String method = "deleteIdentifier";
    trace(method, SystemMessage.METHOD_ENTRY);
    debug(method, "Deleting anonymous identifier");

    // Event Result is a way for the event handler to notify the kernel of any
    // failures or errors and also if any subsequent actions need to be taken
    // (immediately or in a deferred fashion). It can also be used to indicate
    // if the kernel should restart this orchestration or veto it if the event
    // handler doesn't want to notify the kernel of anything it shouldn't return
    // a null value  instead an empty EventResult object
    final EventResult result = new EventResult();
    try {
      final String pid = fetchOrchestrationData(data, this.profile);
      this.context.deleteIdentifier(pid);
    }
    catch (Exception e) {
      result.setFailureReason(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }
}