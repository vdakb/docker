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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Register.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.task;

import oracle.hst.deployment.ServiceOperation;

import oracle.iam.identity.common.spi.WorkflowHandler;

import oracle.iam.identity.workflow.type.Category;
import oracle.iam.identity.workflow.type.Provider;

////////////////////////////////////////////////////////////////////////////////
// class Register
// ~~~~~ ~~~~~~~~
/**
 ** Registers a workflow within within the repository of Identity Manager.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 ** <p>
 ** If the Apache Ant task is used to register workflows they are specified by
 ** the nested element <code>workflow:workflow</code>. For example:
 ** <pre>
 **   &lt;workflow:composite partition="default" name="names1" revision="1.0.0.0" task="SAP Approval"/&gt;
 **   &lt;workflow:composite partition="default" name="names2" revision="1.0.0.0" task="GDS Approval"/&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Register extends Repository {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Register</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Register() {
    // ensure inheritance
    super();

    // set the operation programatically
    this.handler.operation(ServiceOperation.register);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCategory
  /**
   ** Sets the category of the workflow.
   **
   ** @param  category           the category of the workflow.
   */
  public final void setCategory(final Category category) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.category(category.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setService
  /**
   ** Sets the name of the service of the workflow.
   **
   ** @param  service            the name of the service of the workflow.
   */
  public final void setService(final String service) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.service(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProvider
  /**
   ** Sets the provider of the workflow.
   **
   ** @param  provider           the provider of the workflow.
   */
  public final void setProvider(final Provider provider) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.provider(provider.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPayloadID
  /**
   ** Sets the id of the payload of the workflow.
   **
   ** @param  payloadID          the id of the payload of the workflow.
   */
  public final void setPayloadID(final String payloadID) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.payloadID(payloadID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperationID
  /**
   ** Sets the id of the operation of the workflow.
   **
   ** @param  operationID        the id of the operation of the workflow.
   */
  public final void setOperationID(final String operationID) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.operationID(operationID);
  }
}