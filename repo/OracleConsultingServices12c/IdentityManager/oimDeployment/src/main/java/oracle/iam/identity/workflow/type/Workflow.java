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

    File        :   Workflow.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Workflow.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.type;

import oracle.iam.identity.common.spi.WorkflowInstance;

////////////////////////////////////////////////////////////////////////////////
// class Workflow
// ~~~~~ ~~~~~~~~
/**
 ** <code>Workflow</code> represents a workflow definition in Identity Manager
 ** that might be registered, enabled or disable after or during a deployment
 ** operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Workflow extends Composite {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Workflow</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Workflow() {
    // ensure inheritance
    super(new WorkflowInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setService
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>service</code>.
   **
   ** @param  service            the service name of the workflow in Identity
   **                            Manager to handle.
   */
  public void setService(final String service) {
    checkAttributesAllowed();
    ((WorkflowInstance)this.delegate).service(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCategory
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>service</code>.
   **
   ** @param  category           the category of the workflow in Identity
   **                            Manager to handle.
   */
  public void setCategory(final Category category) {
    checkAttributesAllowed();
    ((WorkflowInstance)this.delegate).category(category.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProvider
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>provider</code>.
   **
   ** @param  provider           the provider of the workflow in Identity
   **                            Manager to handle.
   */
  public void setProvider(final Provider provider) {
    checkAttributesAllowed();
    ((WorkflowInstance)this.delegate).provider(provider.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperationID
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>operationID</code>.
   **
   ** @param  operationID        the id of the operation of the workflow in
   **                            Identity Manager to handle.
   */
  public void setOperationID(final String operationID) {
    checkAttributesAllowed();
    ((WorkflowInstance)this.delegate).operationID(operationID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPayloadID
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>payloadID</code>.
   **
   ** @param  payloadID          the id of the payload of the workflow in
   **                            Identity Manager to handle.
   */
  public void setPayloadID(final String payloadID) {
    checkAttributesAllowed();
    ((WorkflowInstance)this.delegate).payloadID(payloadID);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link WorkflowInstance} delegate of Identity Manager to
   ** handle.
   **
   ** @return                    the {@link WorkflowInstance} delegate of
   **                            Identity Manager.
   */
  public final WorkflowInstance instance() {
    if (isReference())
      return ((Workflow)getCheckedRef()).instance();

    return (WorkflowInstance)this.delegate;
  }
}