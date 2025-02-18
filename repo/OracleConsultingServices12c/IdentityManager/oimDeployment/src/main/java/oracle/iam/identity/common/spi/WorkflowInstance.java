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

    File        :   WorkflowInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    WorkflowInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.platform.workflowservice.vo.WorkflowDefinition;

////////////////////////////////////////////////////////////////////////////////
// class WorkflowInstance
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>WorkflowInstance</code> represents a workflow in Oracle
 ** Identity Manager that might be registered, enabled or disabled after or
 ** during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WorkflowInstance extends CompositeInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String category    = "Approval";
  private String provider    = "BPEL";
  private String payloadID   = "payload";
  private String operationID = "process";
  private String service     = "RequestApprovalService";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WorkflowInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public WorkflowInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Called to inject the <code>service</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  service            the <code>service</code> of the workflow
   **                            related to Identity Manager.
   */
  public void service(final String service) {
    this.service = service;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns the <code>service</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>service</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String service() {
    return this.service;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Called to inject the <code>category</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  category           the <code>category</code> of the workflow
   **                            related to Identity Manager.
   */
  public void category(final String category) {
    this.category = category;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the <code>category</code> of the workflow related to Identity
   ** Manager.
   **
   ** @return                    the <code>category</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String category() {
    return this.category;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Called to inject the <code>provider</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  provider           the <code>provider</code> of the workflow
   **                            related to Identity Manager.
   */
  public void provider(final String provider) {
    this.provider = provider;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the <code>provider</code> of the workflow related to Oracle
   ** Identity Manager.
   **
   ** @return                    the <code>provider</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String provider() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payloadID
  /**
   ** Called to inject the <code>payloadID</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  payloadID          the <code>payloadID</code> of the workflow
   **                            related to Identity Manager.
   */
  public void payloadID(final String payloadID) {
    this.payloadID = payloadID;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   payloadID
  /**
   ** Returns the <code>payloadID</code> of the workflow related to Oracle
   ** Identity Manager.
   **
   ** @return                    the <code>payloadID</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String payloadID() {
    return this.payloadID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationID
  /**
   ** Called to inject the <code>operationID</code> of the workflow related to
   ** Identity Manager.
   **
   ** @param  operationID        the <code>operationID</code> of the workflow
   **                            related to Identity Manager.
   */
  public void operationID(final String operationID) {
    this.operationID = operationID;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   operationID
  /**
   ** Returns the <code>operationID</code> of the workflow related to Oracle
   ** Identity Manager.
   **
   ** @return                    the <code>operationID</code> of the workflow
   **                            related to Identity Manager.
   */
  public final String operationID() {
    return this.operationID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>WorkflowInstance</code> object that
   ** represents the same <code>canonical</code> name as this instance.
   **
   ** @param other               the object to compare this
   **                            <code>WorkflowInstance</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>WorkflowInstance</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof WorkflowInstance))
      return false;

    final WorkflowInstance another = (WorkflowInstance)other;
    return this.toString().equals(another.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of an workflow like domain. If it's
   ** <code>false</code> only the domain,name and version of the workflow has
   ** to be present.
   **
   ** @param  strict             <code>true</code> if all attributes has to be
   **                            present; <code>false</code> if only
   **                            <ol>
   **                              <li>domain
   **                              <li>name
   **                              <li>version
   **                            </ol>
   **                            are needed for the operation to perform.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final boolean strict)
    throws BuildException {

    // ensure inhertitance
    super.validate();

    if (StringUtility.isEmpty(this.service))
      handleAttributeMissing("service");

    if (StringUtility.isEmpty(this.category))
      handleAttributeMissing("category");

    if (StringUtility.isEmpty(this.provider))
      handleAttributeMissing("provider");

    if (StringUtility.isEmpty(this.operationID))
      handleAttributeMissing("operationID");

    if (StringUtility.isEmpty(this.payloadID))
      handleAttributeMissing("payloadID");

    if (strict) {
      if (this.humanTask() == null || this.humanTask().isEmpty())
        handleElementMissing("task");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toDefinition
  /**
   ** Factory method to create an instance of the data transfer object the
   ** Identity Manager service is able to handle.
   **
   ** @return                    an instance of the data transfer object the
   **                            Identity Manager service is able to handle.
   */
  WorkflowDefinition toDefinition() {
    // may be its a little oversized waht we are doing here but it looks like
    // more safe to pass null to the workflow registration service instead of an
    // empty string
    StringBuilder buffer = null;
    if (this.humanTask() != null && !this.humanTask().isEmpty()) {
      buffer = new StringBuilder();
      int count = 0;
      for (CompositeInstance.Task task : this.humanTask()) {
        // add the delimiter only if there is more than one task in the
        // collection
        if (count > 0)
          buffer.append(":");
        buffer.append(task.name());
        count++;
      }
    }
    return new WorkflowDefinition(name(), this.category, this.provider, this.service, partition(), revision(), this.payloadID, this.operationID, buffer == null ? null : buffer.toString());
  }
}