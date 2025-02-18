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

    File        :   ProvisioningInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProvisioningInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class ProvisioningInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>ProvisioningInstance</code> represents an identity in Identity Manager
 ** that might be target of a provisioning operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProvisioningInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the operation executed on this instance */
  private ServiceOperation operation;

  /** the attributes mapping specific for a request */
  private RequestForm      dataSet;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ProvisioningInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProvisioningInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ProvisioningInstance</code> with the specified name.
   **
   ** @param  name               the name of the <code>User</code> to
   **                            provision.
   */
  public ProvisioningInstance(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Called to inject the argument for parameter <code>operation</code>.
   **
   ** @param  operation          the operation to execute against Identity
   **                            Manager entity instance.
   */
  public void operation(final ServiceOperation operation) {
    if (operation == null)
      handleAttributeMissing("operation");

    this.operation = operation;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns the operation to execute against Identity Manager entity
   ** instance.
   **
   ** @return                    the operation to execute against Identity
   **                            Manager entity instance.
   */
  public final ServiceOperation operation() {
    return this.operation;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   dataSet
  /**
   ** Returns the dataSet's data to be applied during a request.
   **
   ** @return                    the data mapping to be applied during a
   **                            provisioning or request opertation.
   */
  public final RequestForm dataSet() {
    return this.dataSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overriden)
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate()
    throws BuildException {

    // ensure inheritance
    super.validate();

    if (this.dataSet != null)
      dataSet.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDataSet
  /**
   ** Called to inject the argument for element <code>dataSet</code>.
   **
   ** @param  dataSet            the data mapping to be applied during a
   **                            provisioning or request opertation.
   */
  public void dataSet(final RequestForm dataSet) {
    // prevent bogus input
    if (dataSet == null)
      handleAttributeMissing("dataSet");

    // prevent bogus state
    if (this.dataSet != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "dataSet"));

    this.dataSet = dataSet;
  }
}