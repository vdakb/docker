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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ApplicationPolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationPolicy.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.ApplicationPolicyHandler;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationPolicy
// ~~~~~ ~~~~~~~~~~~~~~~~~
public class ApplicationPolicy extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ApplicationPolicyHandler.Policy delegate = new ApplicationPolicyHandler.Policy();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationPolicy</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationPolicy() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAction
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>action</code>.
   **
   ** @param  action             the action to apply on the credential mapping
   **                            in the Oracle WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setAction(final Action action) {
    checkAttributesAllowed();
    this.delegate.action(ServiceOperation.from(action.getValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the bean to handle in Oracle
   **                            WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link ApplicationPolicyHandler.Policy} delegate.
   **
   ** @return                    the {@link ApplicationPolicyHandler.Policy}
   **                            delegate.
   */
  public final ApplicationPolicyHandler.Policy instance() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRole
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>role</code>.
   **
   ** @param  role               the {@link Principal.ApplicationRole} to manage
   **                            in any operation.
   **
   ** @throws BuildException     if a {@link Principal.ApplicationRole} has
   **                            missing data or if the specified
   **                            {@link Principal.ApplicationRole} is already
   **                            assigned to this task or indicates that this
   **                            type element must not have child elements if
   **                            the refid attribute is set.
   */
  public void addConfiguredApplicationRole(final Principal.ApplicationRole role)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.add((ApplicationPolicyHandler.Role)role.delegate);
  }
}