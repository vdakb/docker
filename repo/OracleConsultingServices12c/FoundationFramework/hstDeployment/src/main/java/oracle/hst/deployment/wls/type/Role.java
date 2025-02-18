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

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Role.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wls.type;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.ServerSecurityHandler;

////////////////////////////////////////////////////////////////////////////////
// class Role
// ~~~~~ ~~~~
public class Role extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ServerSecurityHandler.Role delegate = new ServerSecurityHandler.Role();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Member
  // ~~~~~ ~~~~~~
  /**
   ** <code>Member</code> defines the container to accumulate users or roles to
   ** be granted to or revoked from a role.
   ** <p>
   ** <b>Note</b>:
   ** Class needs to be declared <code>public</code> to allow ANT introspection.
   */
  public static abstract class Member extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected List<User> users = null;
    protected List<Role> roles = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Member</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    protected Member() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setUsername
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>username</code>.
     **
     ** @param  name             the name of the user to manage in Oracle
     **                          WebLogic Server Domain.
     */
    public void setUsername(final String name) {
      checkAttributesAllowed();
      addConfiguredUser(new User(name));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setRolename
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>rolename</code>.
     **
     ** @param  name             the name of the role to manage in Oracle
     **                          WebLogic Server Domain.
     */
    public void setRolename(final String name) {
      checkAttributesAllowed();
      addConfiguredRole(new Role(name));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredUser
    /**
     ** Call by the ANT deployment to inject the argument for nested parameter
     ** <code>user</code>.
     **
     ** @param  user             the {@link User} to manage in any operation.
     **
     ** @throws BuildException   if a {@link User} has missing data.
     */
    public void addConfiguredUser(final User user)
      throws BuildException {

      if (this.users == null)
        this.users = new ArrayList<User>();

      if (this.users.contains(user))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_ONLYONCE, user.instance().name()));

      this.users.add(user);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredRole
    /**
     ** Call by the ANT deployment to inject the argument for nested parameter
     ** <code>role</code>.
     **
     ** @param  role             the {@link Role} to manage in any operation.
     **
     ** @throws BuildException   if a {@link Role} has missing data.
     */
    public void addConfiguredRole(final Role role)
      throws BuildException {

      if (this.roles == null)
        this.roles = new ArrayList<Role>();

      if (this.roles.contains(role))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_ONLYONCE, role.instance().name()));

      this.roles.add(role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Grant
  // ~~~~~ ~~~~~
  /**
   ** <code>Grant</code> defines the container to accumulate users or roles to
   ** be granted to a role.
   */
  public static class Grant extends Member {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Grant</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Grant() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Revoke
  // ~~~~~ ~~~~~~
  /**
   ** <code>Revoke</code> defines the container to accumulate users or roles to
   ** be revoked from a role.
   */
  public static class Revoke extends Member {

    /////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Grant</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Revoke() {
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
   ** Constructs a <code>Role</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Role() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Role</code> with the specified name.
   **
   ** @param  name               the name of the role.
   */
  protected Role(final String name) {
    // ensure inheritance
    super();

    // ensure inheritance
    this.delegate.name(name);
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
   ** @param  name               the name of the bean to manage in Oracle
   **                            WebLogic Server Domain.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description        the description of the bean to manage in Oracle
   **                            WebLogic Server Domain.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    this.delegate.description(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link ServerSecurityHandler.Role} delegate.
   **
   ** @return                    the {@link ServerSecurityHandler.Role}
   **                            delegate.
   */
  public final ServerSecurityHandler.Role instance() {
    return this.delegate;
  }

  /////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  /////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.delegate.name().hashCode();
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method: equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Role</code> object that represents
   ** the same <code>name</code>.
   **
   ** @param other             the object to compare this <code>Role</code>
   **                          with.
   **
   ** @return                  <code>true</code> if the <code>Role</code>s
   **                          are equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof User))
      return false;

    final Role another = (Role)other;
    return another.delegate.name().equals(this.delegate.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredGrant
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>grant</code>.
   **
   ** @param  member             the {@link Grant} to manage in a grant
   **                            to role operation.
   **
   ** @throws BuildException     if a {@link Grant} has missing data.
   */
  public void addConfiguredGrant(final Grant member)
    throws BuildException {

    if (member.users != null && !member.users.isEmpty())
      for (User user : member.users) {
        this.delegate.addGrant(user.instance());
      }

    if (member.roles != null && !member.roles.isEmpty())
      for (Role role : member.roles) {
        this.delegate.addGrant(role.instance());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRevoke
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>revoke</code>.
   **
   ** @param  member             the {@link Revoke} to manage in a
   **                            revoke from role operation.
   **
   ** @throws BuildException     if a {@link Revoke} has missing data.
   */
  public void addConfiguredRevoke(final Revoke member)
    throws BuildException {

    if (member.users != null && !member.users.isEmpty())
      for (User user : member.users) {
        this.delegate.addRevoke(user.instance());
      }

    if (member.roles != null && !member.roles.isEmpty())
      for (Role role : member.roles) {
        this.delegate.addRevoke(role.instance());
      }
  }
}