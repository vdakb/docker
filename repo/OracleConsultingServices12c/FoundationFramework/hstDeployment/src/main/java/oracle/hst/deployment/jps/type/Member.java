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

    File        :   Member.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Member.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.type;

import java.util.Set;
import java.util.HashSet;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;

import oracle.hst.deployment.spi.AbstractPolicyHandler;

//////////////////////////////////////////////////////////////////////////////
// abstract class Member
// ~~~~~~~~ ~~~~~ ~~~~~~
public abstract class Member extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Set<AbstractPolicyHandler.Principal> principal = new HashSet<AbstractPolicyHandler.Principal>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Assign
  // ~~~~~ ~~~~~~
  /**
   ** <code>Assign</code> defines the container to accumulate permissions to
   ** be granted to a policy.
   */
  public static class Assign extends Member {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Assign</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Assign() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Remove
  // ~~~~~ ~~~~~~
  /**
   ** <code>Remove</code> defines the container to accumulate permissions to
   ** be revoked from a policy.
   */
  public static class Remove extends Member {

    /////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Remove</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Remove() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Member</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Member() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   principal
  /**
   ** Returns the {@link AbstractPolicyHandler.Principal} delegates.
   **
   ** @return                    the {@link AbstractPolicyHandler.Principal}
   **                            delegates.
   */
  public final Set<AbstractPolicyHandler.Principal> principal() {
    return this.principal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  addConfiguredCodebase
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>codebase</code>.
   **
   ** @param  principal        the {@link Principal.Codebase} to manage in any
   **                          operation.
   **
   ** @throws BuildException   if a {@link Principal.Codebase} has missing data.
   */
  public void addConfiguredCodebase(final Principal.Codebase principal)
    throws BuildException {

    add(principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  addConfiguredEnterpriseRole
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>role</code>.
   **
   ** @param  principal        the {@link Principal.EnterpriseRole} to manage
   **                          in any operation.
   **
   ** @throws BuildException   if a {@link Principal.EnterpriseRole} has missing
   **                          data.
   */
  public void addConfiguredEnterpriseRole(final Principal.EnterpriseRole principal)
    throws BuildException {

    add(principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: addConfiguredEnterpriseUser
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>user</code>.
   **
   ** @param  principal        the {@link Principal.EnterpriseUser} to manage in
   **                          any operation.
   **
   ** @throws BuildException   if a {@link Principal.EnterpriseUser} has missing
   **                          data.
   */
  public void addConfiguredEnterpriseUser(final Principal.EnterpriseUser principal)
    throws BuildException {

    add(principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  add
  /**
   ** Add a generic {@link Principal} to the policy.
   **
   ** @param  principal        the {@link Principal} to manage in any operation.
   **
   ** @throws BuildException   if a {@link Principal} has missing data.
   */
  private void add(final Principal principal)
    throws BuildException {

    this.principal.add(principal.instance());
  }
}