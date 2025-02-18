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

    File        :   AbstractPolicyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractPolicyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Set;
import java.util.HashSet;

import org.apache.tools.ant.BuildException;

import oracle.security.jps.mas.mgmt.jmx.policy.PortablePrincipal;
import oracle.security.jps.mas.mgmt.jmx.policy.PortablePermission;

import oracle.hst.deployment.ServiceError;

import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.security.jps.mas.mgmt.jmx.policy.PortableApplicationRole;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractPolicyHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage policy stores in Oracle WebLogic
 ** Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractPolicyHandler extends AbstractInvocationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String ENTITY_ROLE       = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_ROLE);
  protected static final String ENTITY_POLICY     = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_POLICY);
  protected static final String ENTITY_CODEBASE   = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_CODEBASE);
  protected static final String ENTITY_PRINCIPAL  = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_PRINCIPAL);
  protected static final String ENTITY_PERMISSION = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_PERMISSION);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum  Type
  // ~~~~~ ~~~~
  public static enum Type {
    CODEBASE, PRINCIPAL, ENTERPRISE_ROLE, ENTERPRISE_USER, APPLICATION_ROLE, CUSTOM;

    @SuppressWarnings("compatibility:7019256613238395612")
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Principal
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Principal</code> are the system-wide policies applied to all
   ** applications deployed to current management domain or only to one
   ** application.
   ** <p>
   ** <code>Principal</code> can used to grant or revoke special permissions and
   ** privileges to principals.
   */
  public static class Principal extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Type                     type;

    final String                   clazz;

    /** the <code>PortablePermission</code>s to grant to this policy */
    final Set<PortablePermission> permissionAssign = new HashSet<PortablePermission>();

    /** the <code>PortablePermission</code>s to revoke from this policy */
    final Set<PortablePermission> permissionRemove = new HashSet<PortablePermission>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Principal</code> of the specified type.
     **
     ** @param  type             the type of the policy.
     ** @param  clazz            the implementing class in the JEE container.
     */
    public Principal(final Type type, final String clazz) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.type  = type;
      this.clazz = clazz;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   file
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>file</code>.
     **
     ** @param  file             the file of the bean to handle in Oracle
     **                          WebLogic Server Domain.
     */
    public void file(final String file) {
      name(String.format("file:%s", file));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   type
    /**
     ** Transforms the exposed type to the internal representation used in
     ** Oracle WebLogic Server Domain.
     **
     ** @return                  the {@link PortablePrincipal.PrincipalType}
     **                          in Oracle WebLogic Server Domain.
     */
    public PortablePrincipal.PrincipalType type() {
      switch (this.type) {
        case ENTERPRISE_ROLE  : return PortablePrincipal.PrincipalType.ENT_ROLE;
        case ENTERPRISE_USER  : return PortablePrincipal.PrincipalType.ENT_USER;
        case APPLICATION_ROLE : return PortablePrincipal.PrincipalType.APP_ROLE;
        case CUSTOM           : return PortablePrincipal.PrincipalType.CUSTOM;
        default               : return null;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groupded by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addAssign
    /**
     ** Add the permission to be granted to the managed permissions.
     **
     ** @param  permission       the {@link Set} of {@link PortablePermission}
     **                          to add.
     **
     ** @throws BuildException   if the specified {@link PortablePermission} is
     **                          already assigned to this task.
     */
    public void addAssign(final Set<PortablePermission> permission)
      throws BuildException {

      for (PortablePermission item : permission)
        addAssign(item);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addAssign
    /**
     ** Add the permission to be granted to the managed permissions.
     **
     ** @param  permission       the {@link PortablePermission} to add.
     **
     ** @throws BuildException   if the specified {@link PortablePermission} is
     **                          already assigned to this task.
     */
    public void addAssign(final PortablePermission permission)
      throws BuildException {

      if (this.permissionAssign.contains(permission))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, permission.getPermissionClassName()));

      // add the instance to the object to handle
      this.permissionAssign.add(permission);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addRemove
    /**
     ** Add the permission to be removed to the managed permissions.
     **
     ** @param  permission       the {@link Set} of {@link PortablePermission}
     **                          to add.
     **
     ** @throws BuildException   if the specified {@link PortablePermission} is
     **                          already assigned to this task.
     */
    public void addRemove(final Set<PortablePermission> permission)
      throws BuildException {

      for (PortablePermission item : permission)
        addRemove(item);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addRemove
    /**
     ** Add the permission to be removed to the managed permissions.
     **
     ** @param  permission       the {@link PortablePermission} to add.
     **
     ** @throws BuildException   if the specified {@link PortablePermission} is
     **                          already assigned to this task.
     */
    public void addRemove(final PortablePermission permission)
      throws BuildException {

      if (this.permissionRemove.contains(permission))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, permission.getPermissionClassName()));

      // add the instance to the object to handle
      this.permissionRemove.add(permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Role
  // ~~~~~ ~~~~
  /**
   ** <code>Role</code> are only to one applicationapplied in the current
   ** management domain.
   ** <p>
   ** <code>Role</code> can used to grant or revoke special permissions and
   ** privileges to principals.
   */
  public static class Role extends Principal {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String  displayname;
    private String  description;

    /** the collection of <code>Principal</code>s to grant */
    Set<Principal> memberAssign = new HashSet<Principal>();

    /** the collection of <code>Principal</code>s to revoke */
    Set<Principal> memberRemove = new HashSet<Principal>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Role</code> of the specified type.
     **
     ** @param  type             the type of the policy.
     ** @param  clazz            the implementing class in the JEE container.
     */
    public Role(final Type type, final String clazz) {
      // ensure inheritance
      super(type, clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: displayname
    /**
     ** Called to inject the argument for parameter
     ** <code>displayname</code>.
     **
     ** @param  displayname     the display name of the role in Oracle WebLogic
     **                         Server Domain.
     */
    public final void displayname(final String displayname) {
      this.displayname = displayname;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: displayname
    /**
     ** Returns the display name of the role in Oracle Weblogic Domain server
     ** entity instance to handle.
     **
     ** @return                 the display name of the role in Oracle WebLogic
     **                         Server Domain entity instance.
     */
    public final String displayname() {
      return this.displayname;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Called to inject the argument for parameter
     ** <code>description</code>.
     **
   ** @param  description        the description of the credential mapping in
   **                            the Oracle WebLogic Server Domain.
     */
    public final void description(final String description) {
      this.description = description;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Returns the description of the role in Oracle Weblogic Domain server
     ** entity instance to handle.
     **
     ** @return                 the description of the role in Oracle WebLogic
     **                         Server Domain entity instance.
     */
    public final String description() {
      return this.description;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: validate (overridden)
    /**
     ** The entry point to validate the type to use.
     ** <br>
     ** <code>action</code> and <code>role</code> are mandatory for this type.
     **
     ** @throws BuildException     in case the instance does not meet the
     **                            requirements.
     */
    @Override
    public void validate()
      throws BuildException {

      // ensure inheritance
      super.validate();

      if (this.action == null)
        handleAttributeMissing("action");

      for (Principal principal : this.memberAssign)
        principal.validate();

      for (Principal principal : this.memberRemove)
        principal.validate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals
    /**
     ** Compares the specified {@link PortableApplicationRole} with the
     ** properties of this instance.
     **
     ** @param  other            the {@link PortableApplicationRole} providing
     **                          the properties to compare with the properties
     **                          of this instance.
     **
     ** @return                  <code>true</code> if the
     **                          {@link PortableApplicationRole} has is
     **                          identicaly in name, displayname and description
     **                          to the properties of this instance.
     */
    public boolean equals(final PortableApplicationRole other) {
      return this.name().equals(other.getPrincipalName()) && this.displayname.equals(other.getDisplayName()) && this.description.equals(other.getDescription());
    }
    ////////////////////////////////////////////////////////////////////////////
    // Method: addMemberAssign
    /**
     ** Add a collection of {@link Principal}s to the managed members to be
     ** granted.
     **
     ** @param  principal        the collection of {@link Principal}s to add.
     **
     ** @throws BuildException   if one the specified {@link Principal}s is
     **                          already assigned to this task.
     */
    public void addMemberAssign(final Set<Principal> principal)
      throws BuildException {

      // prevent bogus input
      if (principal == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      for (Principal whom : principal)
        addMemberAssign(whom);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addMemberAssign
    /**
     ** Add the specified {@link Principal} to the managed members.
     **
     ** @param  principal        the {@link Principal} to add.
     **
     ** @throws BuildException   if the specified {@link Principal} is already
     **                          assigned to this task.
     */
    public void addMemberAssign(final Principal principal)
      throws BuildException {

      // prevent bogus input
      if (principal == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      // prevent bogus input
      if (this.memberAssign.contains(principal))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, principal.name()));

      // add the instance to the object to handle
      this.memberAssign.add(principal);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addMemberRemove
    /**
     ** Add a collection of {@link Principal}s to the managed members to be
     ** revoked.
     **
     ** @param  principal        the collection of {@link Principal}s to add.
     **
     ** @throws BuildException   if one the specified {@link Principal}s is
     **                          already assigned to this task.
     */
    public void addMemberRemove(final Set<Principal> principal)
      throws BuildException {

      // prevent bogus input
      if (principal == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      for (Principal whom : principal)
        addMemberRemove(whom);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addMemberRemove
    /**
     ** Add the specified {@link Principal} to the managed members.
     **
     ** @param  principal        the {@link Principal} to add.
     **
     ** @throws BuildException   if the specified {@link Principal} is already
     **                          assigned to this task.
     */
    public void addMemberRemove(final Principal principal)
      throws BuildException {

      // prevent bogus input
      if (principal == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      // prevent bogus input
      if (this.memberRemove.contains(principal))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, principal.name()));

      // add the instance to the object to handle
      this.memberRemove.add(principal);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Policy
  // ~~~~~ ~~~~~~
  public static class Policy extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the single <code>Role</code> to configure or to investigate */
    Role       single   = null;

    /** the multiple <code>Role</code>s to configure or to create */
    Set<Role> multiple = new HashSet<Role>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Policy</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Policy() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   role
    /**
     ** Add the specified role to the managed roles.
     **
     ** @param  role             the {@link Role} to add.
     **
     ** @throws BuildException   if the specified {@link Role} is already
     **                          assigned to this task.
     */
    public void role(final Role role)
      throws BuildException {

      // prevent bogus input
      if (role == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      if (this.multiple.contains(role))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, role.name()));

      // set the single instance to the object to handle
      this.single = role;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groupded by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate (overridden)
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException     in case the instance does not meet the
     **                            requirements.
     */
    @Override
    public void validate()
      throws BuildException {

      if (this.single != null)
        this.single.validate();

      for (Role i : this.multiple)
        i.validate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   add
    /**
     ** Add the specified {@link Role} to the managed roles.
     **
     ** @param  role             the {@link Role} to add.
     **
     ** @throws BuildException   if the specified {@link Role} is already
     **                          assigned to this task.
     */
    public void add(final Role role)
      throws BuildException {

      // prevent bogus input
      if (role == null)
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      // prevent bogus input
      if ((this.single != null && this.single.equals(role)) || this.multiple.contains(role))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, role.name()));

      // add the instance to the object to handle
      this.multiple.add(role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AbstractPolicyHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  AbstractPolicyHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }
}