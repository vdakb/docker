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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Administration Management

    File        :   OrchestrationEventAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrchestrationEventAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import javax.management.openmbean.CompositeData;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class OrchestrationEventAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the event within the context of a particular Orchestration Process.
 ** This is an Orchestration Event (where the type of object is an
 ** OrchestrationEvent) in OIM.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class OrchestrationEventAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PROCESSID        = "processId";
  public static final String FK               = "Bind_processId";
  public static final String PK               = "eventId";
  public static final String NAME             = "eventName";
  public static final String OPERATION        = "operation";
  public static final String STAGE            = "stage";
  public static final String ORDER            = "order";
  public static final String STATUS           = "status";
  public static final String SYNCHRONOUS      = "sync";
  public static final String EXECUTION        = "execTimeMillis";
  public static final String HANDLER          = "handlerClass";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6159599953813446774")
  private static final long  serialVersionUID = -2530973898021953135L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               eventId;
  @ModelAttr
  private String             eventName;
  @ModelAttr
  private Long               processId;
  @ModelAttr
  private String             operation;
  @ModelAttr
  private String             stage;
  @ModelAttr
  private String             status;
  @ModelAttr
  private String             order;
  @ModelAttr
  private Boolean            synchronous;
  @ModelAttr
  private String             execution;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrchestrationEventAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrchestrationEventAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrchestrationEventAdapter</code> values object which
   ** belongs to the specified orchestration process and use the specified
   ** {@link CompositeData} to populate its value.
   **
   ** @param  processId          the identifier of the orchestration process
   **                            this event belongs to.
   ** @param  data               the {@link CompositeData} providing the values
   **                            to populate
   */
  private OrchestrationEventAdapter(final Long processId, final CompositeData data) {
    // ensure inheritance
    this(processId);

    // initialize instance attributes
    final String   id = (String)data.get(PK);
    final String[] fc = id.split(" ");
    this.eventId      = Long.valueOf(fc[1]);
    this.eventName    = fc[3];
    this.operation    = (String)data.get(OPERATION);
    this.stage        = (String)data.get(STAGE);
    this.order        = (String)data.get(ORDER);
    this.status       = (String)data.get(STATUS);
    this.synchronous  = Boolean.valueOf((String)data.get(SYNCHRONOUS));
    this.execution    = (String)data.get(EXECUTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrchestrationEventAdapter</code> values object which
   ** use the specified orchestration process.
   **
   ** @param  processId          the identifier of the orchestration process
   **                            this event belongs to.
   */
  private OrchestrationEventAdapter(final Long processId) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.processId = processId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrchestrationEventAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public OrchestrationEventAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrchestrationEventAdapter</code> values object which use
   ** the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public OrchestrationEventAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessId
  /**
   ** Sets the value of the processId property.
   **
   ** @param  value              the value of the processId property.
   **                            Allowed object is {@link Long}.
   */
  public void setProcessId(final Long value) {
    this.processId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessId
  /**
   ** Returns the value of the processId property.
   **
   ** @return                    the value of the processId property.
   **                            Possible object is {@link Long}.
   */
  public Long getProcessId() {
    return this.processId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEventId
  /**
   ** Sets the value of the eventId property.
   **
   ** @param  value              the value of the eventId property.
   **                            Allowed object is {@link Long}.
   */
  public void setEventId(final Long value) {
    this.eventId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEventId
  /**
   ** Returns the value of the eventId property.
   **
   ** @return                    the value of the eventId property.
   **                            Possible object is {@link Long}.
   */
  public Long getEventId() {
    return this.eventId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEventName
  /**
   ** Sets the value of the eventName property.
   **
   ** @param  value              the value of the eventName property.
   **                            Allowed object is {@link String}.
   */
  public void setEventName(final String value) {
    this.eventName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEventName
  /**
   ** Returns the value of the eventName property.
   **
   ** @return                    the value of the eventName property.
   **                            Possible object is {@link String}.
   */
  public String getEventName() {
    return this.eventName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOperation
  /**
   ** Sets the value of the operation property.
   **
   ** @param  value              the value of the operation property.
   **                            Allowed object is {@link String}.
   */
  public void setOperation(final String value) {
    this.operation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOperation
  /**
   ** Returns the value of the operation property.
   **
   ** @return                    the value of the operation property.
   **                            Possible object is {@link String}.
   */
  public String getOperation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStage
  /**
   ** Sets the value of the stage property.
   **
   ** @param  value              the value of the stage property.
   **                            Allowed object is {@link String}.
   */
  public void setStage(final String value) {
    this.stage = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStage
  /**
   ** Returns the value of the stage property.
   **
   ** @return                    the value of the stage property.
   **                            Possible object is {@link String}.
   */
  public String getStage() {
    return this.stage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOrder
  /**
   ** Sets the value of the order property.
   **
   ** @param  value              the value of the order property.
   **                            Allowed object is {@link String}.
   */
  public void setOrder(final String value) {
    this.order = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOrder
  /**
   ** Returns the value of the order property.
   **
   ** @return                    the value of the order property.
   **                            Possible object is {@link String}.
   */
  public String getOrder() {
    return this.order;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              the value of the status property.
   **                            Allowed object is {@link String}.
   */
  public void setStatus(final String value) {
    this.status = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStatus
  /**
   ** Returns the value of the status property.
   **
   ** @return                    the value of the status
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSynchronous
  /**
   ** Sets the value of the synchronous property.
   **
   ** @param  value              the value of the synchronous property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setSynchronous(final Boolean value) {
    this.synchronous = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSynchronous
  /**
   ** Returns the value of the synchronous property.
   **
   ** @return                    the value of the synchronous property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getSynchronous() {
    return this.synchronous;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setExecution
  /**
   ** Sets the value of the execution property.
   **
   ** @param  value              the value of the execution property.
   **                            Allowed object is {@link String}.
   */
  public void setExecution(final String value) {
    this.execution = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getExecution
  /**
   ** Returns the value of the execution property.
   **
   ** @return                    the value of the execution property.
   **                            Possible object is {@link String}.
   */
  public String getExecution() {
    return this.execution;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>OrchestrationEventAdapter</code> values
   ** object which belongs to the specified orchestration process and use the
   ** specified {@link CompositeData} to populate its value.
   **
   ** @param  processId          the identifier of the orchestration process
   **                            this event belongs to.
   ** @param  data               the {@link CompositeData} providing the values
   **                            to populate.
   **
   ** @return                    an instance of <code>DatabaseUpdate</code>
   **                            with the properties provided.
   */
  public static OrchestrationEventAdapter build(final Long processId, final CompositeData data) {
    return new OrchestrationEventAdapter(processId, data);
  }
}