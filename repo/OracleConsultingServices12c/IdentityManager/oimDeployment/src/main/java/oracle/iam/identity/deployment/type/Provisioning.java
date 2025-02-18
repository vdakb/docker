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

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Provisioning.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.common.spi.ProvisioningInstance;

////////////////////////////////////////////////////////////////////////////////
// class Provisioning
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>Provisioning</code> represents a user in Identity Manager that might
 ** be target of a provisioning operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Provisioning extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Provisioning() {
    // ensure inheritance
    super(new ProvisioningInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> type use the specified
   ** {@link ProvisioningInstance} to store the ANT attributes.
   ** <p>
   ** Commonly used by sub classes to pass in their own class of an
   ** {@link ProvisioningInstance}.
   **
   ** @param  instance           the {@link ProvisioningInstance} to store the
   **                            ANT attributes.
   */
  protected Provisioning(final ProvisioningInstance instance) {
    // ensure inheritance
    super(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperation
  /**
   ** Call by the ANT deployment to inject the argument for attribute
   ** <code>operation</code>.
   **
   ** @param  operation          the {@link Operation} to execute in Identity
   **                            Manager.
   **
   ** @throws BuildException     if the specified {@link Operation} is not
   **                            valid.
   */
  public void setOperation(final Operation operation)
    throws BuildException {

    ((ProvisioningInstance)this.delegate).operation(operation.value());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link ProvisioningInstance} delegate of Identity Manager to
   ** handle.
   **
   ** @return                    the {@link ProvisioningInstance} delegate of
   **                            Identity Manager to handle.
   */
  public final ProvisioningInstance instance() {
    if (isReference())
      return ((Provisioning)getCheckedRef()).instance();

    return (ProvisioningInstance)this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredDataSet
  /**
   ** Add the specified {@link RequestDataSet}.
   **
   ** @param dataSet             the {@link RequestDataSet} to add.
   */
  public void addConfiguredDataSet(final RequestDataSet dataSet) {
    checkAttributesAllowed();
    ((ProvisioningInstance)this.delegate).dataSet(dataSet.instance());
  }
}