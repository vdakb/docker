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

    File        :   Principal.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Principal.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.AbstractPolicyHandler;
import oracle.hst.deployment.spi.ApplicationPolicyHandler;

//////////////////////////////////////////////////////////////////////////////
// abstract class Principal
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
public abstract class Principal extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  ////////////////////////////////////////////////////////////////////////////

  final AbstractPolicyHandler.Principal delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum  Type
  // ~~~~~ ~~~~
  public static enum Type {
    CODEBASE, PRINCIPAL, ENTERPRISE_ROLE, ENTERPRISE_USER, APPLICATION_ROLE, CUSTOM;

    @SuppressWarnings("compatibility:-1204726436022056060")
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Codebase
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>Codebase</code> defines the container to accumulate
   ** permissions to be granted to or revoked from a policy.
   */
  public static class Codebase extends Principal {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Codebase</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Codebase() {
      // ensure inheritance
      super(new AbstractPolicyHandler.Principal(AbstractPolicyHandler.Type.CODEBASE, null));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   setFile
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>file</code>.
     **
     ** @param  file               the file of the bean to handle in Oracle
     **                            WebLogic Server Domain.
     */
    public void setFile(final String file) {
      checkAttributesAllowed();
      this.delegate.file(file);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class EnterpriseRole
  // ~~~~~ ~~~~~~~~~~~~~~
  /**
   ** <code>EnterpriseRole</code> defines the container to accumulate
   ** permissions to be granted to or revoked from a policy.
   */
  public static class EnterpriseRole extends Principal {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>EnterpriseRole</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public EnterpriseRole() {
      // ensure inheritance
      super(new AbstractPolicyHandler.Principal(AbstractPolicyHandler.Type.ENTERPRISE_ROLE, "weblogic.security.principal.WLSGroupImpl.class"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class EnterpriseUser
  // ~~~~~ ~~~~~~~~~~~~~~
  /**
   ** <code>EnterpriseUser</code> defines the container to accumulate
   ** permissions to be granted to or revoked from a policy.
   */
  public static class EnterpriseUser extends Principal {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>EnterpriseUser</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public EnterpriseUser() {
      // ensure inheritance
      super(new AbstractPolicyHandler.Principal(AbstractPolicyHandler.Type.ENTERPRISE_USER, "weblogic.security.principal.WLSUserImpl.class"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ApplicationRole
  // ~~~~~ ~~~~~~~~~~~~~~~
  /**
   ** <code>ApplicationRole</code> defines the container to accumulate
   ** permissions to be granted to or revoked from an application role.
   */
  public static class ApplicationRole extends Principal {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>ApplicationRole</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public ApplicationRole() {
      // ensure inheritance
      super(new AbstractPolicyHandler.Role(AbstractPolicyHandler.Type.APPLICATION_ROLE, null));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setDisplayName
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>displayname</code>.
     **
     ** @param  displayname       the display name of the role in Oracle WebLogic
     **                           Server Domain.
     **
     ** @throws BuildException   indicates that refid has to be the only
     **                          attribute if it is set.
     */
    public void setDisplayName(final String displayname)
      throws BuildException {

      checkAttributesAllowed();
      ((ApplicationPolicyHandler.Role)this.delegate).displayname(displayname);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setDescription
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>description</code>.
     **
     ** @param  description      the description of the credential mapping in
     **                          the Oracle WebLogic Server Domain.
     **
     ** @throws BuildException   indicates that refid has to be the only
     **                          attribute if it is set.
     */
    public void setDescription(final String description)
      throws BuildException {

      checkAttributesAllowed();
      ((ApplicationPolicyHandler.Role)this.delegate).description(description);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredAssign
    /**
     ** Call by the ANT deployment to inject the argument for nested parameter
     ** <code>assign</code>.
     **
     ** @param  grantee          the {@link Member.Assign} to be assigned to
     **                          this role.
     **
     ** @throws BuildException   if a {@link Member.Assign} has missing data.
     */
    public void addConfiguredAssign(final Member.Assign grantee)
      throws BuildException {

      checkChildrenAllowed();
      ((ApplicationPolicyHandler.Role)this.delegate).addMemberAssign(grantee.principal());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredRemove
    /**
     ** Call by the ANT deployment to inject the argument for nested parameter
     ** <code>remove</code>.
     **
     ** @param  grantee            the {@link Member.Remove} to be revoked
     **                            from this role.
     **
     ** @throws BuildException     if a {@link Member.Remove} has missing data.
     */
    public void addConfiguredRevoke(final Member.Remove grantee)
      throws BuildException {

      checkChildrenAllowed();
      ((ApplicationPolicyHandler.Role)this.delegate).addMemberRemove(grantee.principal());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Principal</code> which wrappes the specified principal.
   **
   ** @param  delegate           the principal delegate to wrap.
   */
  public Principal(final AbstractPolicyHandler.Principal delegate) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
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
   ** @param  name               the name of the role in Oracle WebLogic Server
   **                            Domain.
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
   ** Returns the {@link AbstractPolicyHandler.Principal} delegate.
   **
   ** @return                    the {@link AbstractPolicyHandler.Principal}
   **                            delegate.
   */
  public final AbstractPolicyHandler.Principal instance() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredGrant
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>assign</code>.
   **
   ** @param  permission         the {@link Permission.Assign} to grant.
   **
   ** @throws BuildException     if a {@link Permission.Assign} has missing data.
   */
  public void addConfiguredGrant(final Permission.Assign permission)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.addAssign(permission.permission());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRevoke
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>assign</code>.
   **
   ** @param  permission         the {@link Permission} to grant.
   **
   ** @throws BuildException     if a {@link Permission} has missing data.
   */
  public void addConfiguredRevoke(final Permission.Remove permission)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.addRemove(permission.permission());
  }
}