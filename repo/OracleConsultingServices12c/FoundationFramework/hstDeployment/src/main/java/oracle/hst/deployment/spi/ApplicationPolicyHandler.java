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

    File        :   ApplicationPolicyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationPolicyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Set;
import java.util.HashSet;

import javax.management.ObjectName;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;

import javax.management.openmbean.CompositeData;

import org.apache.tools.ant.BuildException;

import oracle.security.jps.mas.mgmt.jmx.util.JpsJmxConstants;

import oracle.security.jps.mas.mgmt.jmx.policy.PortableGrant;
import oracle.security.jps.mas.mgmt.jmx.policy.PortableGrantee;
import oracle.security.jps.mas.mgmt.jmx.policy.PortablePrincipal;
import oracle.security.jps.mas.mgmt.jmx.policy.PortablePermission;
import oracle.security.jps.mas.mgmt.jmx.policy.PortableJavaPolicy;
import oracle.security.jps.mas.mgmt.jmx.policy.PortableRoleMember;
import oracle.security.jps.mas.mgmt.jmx.policy.PortableApplicationRole;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationPolicyHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage application policies in Oracle
 ** WebLogic Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ApplicationPolicyHandler extends AbstractPolicyHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Manages application policies in the policy store configured in the default
   ** context.
   ** <p>
   ** Update or write operations do not require server restart to effect
   ** changes. All changes are effected immediately.
   */
  private static ObjectName applicationPolicyStore;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the single <code>Policy</code> to configure or to investigate */
  protected Policy          single;

  /** the multiple <code>Policy</code>s to configure or to create */
  protected Set<Policy>     multiple = new HashSet<Policy>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ApplicationPolicyHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ApplicationPolicyHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

 //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvocationHandler)
  /**
   ** Returns the JMX {@link ObjectName} to access the credential store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            credential store.
   */
  @Override
  protected final ObjectName objectName() {
    return applicationPolicyStore();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Called to inject the argument for parameter <code>action</code>.
   **
   ** @param  action           the action to apply in Oracle WebLogic Domain
   **                          MBean Server.
   */
  public final void action(final ServiceOperation action) {
    if (this.single == null)
      this.single = new Policy();

    this.single.action(action);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Called to inject the argument for parameter <code>action</code>.
   **
   ** @param  name             the name of the policy to maintain in Oracle
   **                          WebLogic Domain MBean Server.
   */
  public final void name(final String name) {
    if (this.single == null)
      this.single = new Policy();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified policy to the managed policies.
   **
   ** @param  policy             the {@link Policy} to add.
   **
   ** @throws BuildException     if the specified {@link Policy} is already
   **                            assigned to this task.
   */
  public void add(final Policy policy)
    throws BuildException {

    // prevent bogus input
    if (policy == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    // prevent bogus input
    if ((this.single != null && this.single.equals(policy)) || this.multiple.contains(policy))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, policy.name()));

    // add the instance to the object to handle
    this.multiple.add(policy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    if (this.single != null)
      this.single.validate();

    for (Policy i : this.multiple)
      i.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Performs all action on the instances assigned to this task through the
   ** given {@link MBeanServerConnection}.
   **
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void execute(final MBeanServerConnection connection)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.connection = connection;

    if (this.single != null)
      dispatch(this.single);

    for (Policy i : this.multiple)
      dispatch(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Performs all action on the {@link Policy} instance through the given
   ** {@link MBeanServerConnection}.
   **
   ** @param  policy             the {@link Policy} the configured actions has
   **                            to be performed for.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void dispatch(final Policy policy)
    throws ServiceException {

    switch (policy.action) {
      case create  : create(policy);
      case none   :  modify(policy);
                     break;
      case delete :  delete(policy);
                     break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an application policy given {@link Policy} instance.
   ** <p>
   ** Invokes the operation <code>createApplicationPolicy</code> on an MBean.
   ** <p>
   ** If neccessary, create an application policy and return to caller.
   ** Based on the algorithm in <code>JpsAuth</code> this checks for
   ** <pre>
   **   PolicyStoreAccessPermission("context=APPLICATION,name=Applicaiton's stripe Id" , "createApplicationPolicy")
   ** </pre>
   ** This may result in an AccessControlException or SecurityException.
   **
   ** @param  policy             the {@link Policy} the to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final Policy policy)
    throws ServiceException {

    if (exists(policy)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ENTITY_POLICY, policy.name()));
    }
    else {
      final String   operation = "createApplicationPolicy";
      final Object[] parameter = { policy.name() };
      final String[] signature = { STRING_CLASS };
      final String[] arguments  = { ENTITY_POLICY, policy.name() };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an application policy given {@link Policy} instance.
   **
   ** @param  policy             the {@link Policy} the to modify.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void modify(final Policy policy)
    throws ServiceException {

    if (policy.single != null)
      dispatch(policy, policy.single);

    for (Role role : policy.multiple)
      dispatch(policy, role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an application policy given the {@link Policy} instance.
   ** <p>
   ** Invokes the operation <code>deleteApplicationPolicy</code> on an MBean.
   ** <p>
   ** Based on the algorithm in <code>JpsAuth</code> this checks for
   ** <pre>
   **   PolicyStoreAccessPermission("context=APPLICATION,name=Applicaiton's stripe Id" , "deleteApplicationPolicy").
   ** </pre>
   ** This may result in an AccessControlException or SecurityException
   **
   ** @param  policy             the {@link Policy} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final Policy policy)
    throws ServiceException {

    if (!exists(policy))
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_POLICY, policy.name()));
    else {
      final String   operation = "deleteApplicationPolicy";
      final Object[] parameter = { policy.name() };
      final String[] signature = { STRING_CLASS };
      final String[] arguments  = { ENTITY_POLICY, policy.name() };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Indicates whether the specified application policy given the
   ** {@link Policy} instance exists in the policy store.
   ** <p>
   ** Invokes the operation <code>getApplicationPolicy</code> on an MBean.
   **
   ** @param  policy             the {@link Policy} instance providing the
   **                            data to indicate the existance.
   **
   ** @return                    <code>true</code> if the policy exists for the
   **                            information provided; otherwise
   **                            <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean exists(final Policy policy)
    throws ServiceException {

    return (find(policy) != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookup the {@link Policy} for the application.
   ** <p>
   ** Invokes the operation <code>getApplicationPolicy</code> on an MBean.
   **
   ** @param  policy             the {@link Policy} instance providing the
   **                            data to lookup.
   **
   ** @return                    the {@link PortableJavaPolicy} found in the
   **                            system or <code>null</code> if no
   **                            application policy exists for the given
   **                            {@link Policy}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private PortableJavaPolicy find(final Policy policy)
    throws ServiceException {

    final String   operation = "getApplicationPolicy";
    final Object[] parameter = { policy.name() };
    final String[] signature = { STRING_CLASS };
    // we need to catch the ServiceException to figure out the reason of the
    // failure
    // if the causing exception is a MBeanException with code JPS-04028 than
    // the policy doesn't exists hence we will retunr null instaed of rethrow
    // the exception
    CompositeData data = null;
    try {
      data = (CompositeData)invoke(operation, parameter, signature);
    }
    catch (ServiceException e) {
      final Throwable t = e.getCause();
      if (!t.getMessage().startsWith("JPS-04028"))
        throw e;
    }
    return data == null ? null : PortableJavaPolicy.from(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Performs all action on the {@link Policy} instance through the given
   ** {@link MBeanServerConnection}.
   **
   ** @param  policy             the {@link Policy} the configured actions has
   **                            to be performed for.
   ** @param  role               the {@link Role} to be configured in the
   **                            <code>policy</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void dispatch(final Policy policy, final Role role)
    throws ServiceException {

    switch (role.action) {
      case create : create(policy, role);
      case none   : modify(policy, role);
                    break;
      case delete : delete(policy, role);
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an application role given the {@link Role} instance in the
   ** application policy specified by {@link Policy} <code>policy</code>.
   ** <p>
   ** Invokes the operation <code>createApplicationRole</code> on an MBean.
   **
   ** @param  policy             the {@link Policy} instance providing the
   **                            data  where the role has been created.
   ** @param  role               the name of the Application Role to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final Policy policy, final Role role)
    throws ServiceException {

    final PortableApplicationRole portable = find(policy, role);
    if (portable != null) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ENTITY_ROLE, role.name()));
    }
    else {
      final String   operation = "createApplicationRole";
      final Object[] parameter = { policy.name(), role.name(),  role.displayname(), role.description(), null};
      final String[] signature = { STRING_CLASS,  STRING_CLASS, STRING_CLASS,     STRING_CLASS,     STRING_CLASS};
      final String[] arguments = { ENTITY_ROLE,  role.name() };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an application role given the {@link Role} instance in the
   ** application policy specified by {@link Policy} <code>policy</code>.
   ** <p>
   ** Invokes the operation <code>updateApplicationRole(</code> on an MBean.
   **
   ** @param  policy             the {@link Policy} instance providing the
   **                            data  where the role has been modified.
   ** @param  role               the name of the Application Role to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void modify(final Policy policy, final Role role)
    throws ServiceException {

    final PortableApplicationRole portable = find(policy, role);
    if (portable == null)
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_ROLE, role.name()));
    else {
      // revoke and grant the configured permissions and memberships
      final String application = policy.name();
      if (!role.equals(portable)) {
        // apply all changes on the role itself
        final String   operation = "updateApplicationRole";
        // important in contrast to createApplicationRole the arguments of display
        // name and description are swapped
        final Object[] parameter = { application,  role.name(),  role.description(), role.displayname() };
        final String[] signature = { STRING_CLASS, STRING_CLASS, STRING_CLASS,       STRING_CLASS };
        final String[] arguments = { ENTITY_ROLE,  role.name() };
        warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));
        invoke(operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, arguments));
      }
      removePermission(application, portable, role.permissionRemove);
      assignPermission(application, portable, role.permissionAssign);
      removePrincipal (application, portable, role.memberRemove);
      assignPrincipal (application, portable, role.memberAssign);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete an application role given the {@link Role} instance in the
   ** application policy specified by {@link Policy} <code>policy</code>.
   ** <p>
   ** Invokes the operation <code>deleteApplicationRole</code> on an MBean.
   **
   ** @param  policy             the {@link Policy} instance providing the
   **                            data where the role has been deleted.
   ** @param  role               the name of the Application Role to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final Policy policy, final Role role)
    throws ServiceException {

    final PortableApplicationRole portable = find(policy, role);
    if (portable == null)
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_ROLE, role.name()));
    else {
      final String   operation = "deleteApplicationRole";
      final Object[] parameter = { policy.name(), role.name() };
      final String[] signature = { STRING_CLASS,  STRING_CLASS };
      final String[] arguments = { ENTITY_ROLE,  role.name() };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Lookup the {@link PortableApplicationRole} for the application specified
   ** by <code>policy</code> that matches the name <code>role</code>.
   ** <p>
   ** Invokes the operation <code>getApplicationRole</code> on an MBean.
   **
   ** @param  policy             the {@link Policy} instance providing the
   **                            data to indicate the existance of the specified
   **                            {@link Role} <code>role</code>.
   ** @param  role               the name of the Application Role to lookup.
   **
   ** @return                    the {@link PortableApplicationRole} which
   **                            represents the Application Role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private PortableApplicationRole find(final Policy policy, final Role role)
    throws ServiceException {

    final String        operation = "getApplicationRole";
    final Object[]      parameter = { policy.name(), role.name() };
    final String[]      signature = { STRING_CLASS,  STRING_CLASS };
    // since FMW 11.1.1.9 an exception is thrown if the requested object does
    // not exists hence we need to catch the exception to keep the existing
    // implementation in place
    try {
      final CompositeData data = (CompositeData)invoke(operation, parameter, signature);
      return PortableApplicationRole.from(data);
    }
    catch (ServiceException e) {
      if (e.getCause() instanceof MBeanException) {
        return null;
      }
      else
        throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignPrincipal
  /**
   ** Add members to a specfic Application Role.
   ** <p>
   ** Invokes the operation <code>addMembersToApplicationRole</code> on a MBean.
   **
   ** @param  policy             the application instance providing the data to
   **                            indicate the context of the specified
   **                            {@link PortableApplicationRole}.
   ** @param  portable           the {@link PortableApplicationRole} to which
   **                            members has to be added.
   ** @param  principal          the {@link Set} of {@link Principal} which
   **                            becomes membership on the role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assignPrincipal(final String policy, final PortableApplicationRole portable, Set<Principal> principal)
    throws ServiceException {

    for (Principal item : principal) {
      final PortableRoleMember member = new PortableRoleMember(item.clazz, item.name(), item.type());
      if (member(policy, portable, member))
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, ENTITY_ROLE, item.name()));
      else
        assign(policy, portable, member);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Add members to a specfic Application Role.
   ** <p>
   ** Invokes the operation <code>addMembersToApplicationRole</code> on a MBean.
   **
   ** @param  policy             the application instance providing the data to
   **                            indicate the context of the specified
   **                            {@link PortableApplicationRole}.
   ** @param  role               the {@link PortableApplicationRole} to which
   **                            members has to be added.
   ** @param  member             the {@link PortableRoleMember} which contains
   **                            the member that should be granted to the role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assign(final String policy, final PortableApplicationRole role, final PortableRoleMember member)
    throws ServiceException {

    final String          operation = "addMembersToApplicationRole";
    final CompositeData[] composite = { member.toCompositeData(null) };
    final Object[]        parameter = { policy,       role.toCompositeData(null), composite };
    final String[]        signature = { STRING_CLASS, COMPOSITEDATA_CLASS,        COMPOSITEARRAY_CLASS };
    final String[]        arguments = { ENTITY_PRINCIPAL, member.getPrincipalName(), ENTITY_ROLE, role.getPrincipalName() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
    invoke(operation, parameter, signature);
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignPermission
  /**
   ** Add permission to a specfic Application Role.
   ** <p>
   ** Invokes the operation <code>grantToApplicationPolicy</code> on a MBean.
   **
   ** @param  policy             the application instance providing the data to
   **                            indicate the context of the specified
   **                            {@link PortableApplicationRole}.
   ** @param  portable           the {@link PortableApplicationRole} to which
   **                            permissions has to be added.
   ** @param  permission         the array of {@link PortablePermission}s which
   **                            contains information about the grants.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assignPermission(final String policy, final PortableApplicationRole portable, final Set<PortablePermission> permission)
    throws ServiceException {

    final Set<PortablePermission> source = permission(policy, portable);
    // unfortunately don't know why but we cannot not use removeAll on the
    // collection permission; it removes nothing if a match can be identified
    // hence we have to doit on our own, aaaargh
    for (PortablePermission target : permission)
      for (PortablePermission cursor : source) {
        if (equal(cursor, target))
          permission.remove(target);
        // may be there's no longer anything to compare
        if (permission.isEmpty())
          break;
      }

    if (!permission.isEmpty()) {
      final PortableGrantee grantee    = new PortableGrantee( new PortablePrincipal[] { portable }, null);
      final PortableGrant   grant      = new PortableGrant(grantee, permission.toArray(new PortablePermission[0]));
      final CompositeData[] composite  = { grant.toCompositeData(null) };

      final String          operation  = "grantToApplicationPolicy";
      final Object[]        parameter  = { policy,       composite };
      final String[]        signature  = { STRING_CLASS, COMPOSITEARRAY_CLASS };
      final String[]        arguments  = { ENTITY_POLICY, ENTITY_PERMISSION, ENTITY_ROLE, portable.getPrincipalName() };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_SUCCESS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePrincipal
  /**
   ** Revokes members from a specfic Application Role.
   ** <p>
   ** Invokes the operation <code>removeMembersFromApplicationRole</code> on a
   ** MBean.
   **
   ** @param  policy             the application instance providing the data to
   **                            indicate the context of the specified
   **                            {@link PortableApplicationRole}.
   ** @param  portable           the {@link PortableApplicationRole} from which
   **                            members has to be revoked.
   ** @param  principal          the {@link Set} of {@link Principal} which
   **                            loose membership on the role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void removePrincipal(final String policy, final PortableApplicationRole portable, final Set<Principal> principal)
    throws ServiceException {

    for (Principal item : principal) {
      final PortableRoleMember grantee = new PortableRoleMember(item.clazz, item.name(), item.type());
      if (member(policy, portable, grantee))
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTASSINGED, ENTITY_ROLE, item.name()));
      else
        remove(policy, portable, grantee);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Revokes a specific member from a specfic Application Role.
   ** <p>
   ** Invokes the operation <code>removeMembersFromApplicationRole</code> on a
   ** MBean.
   **
   ** @param  policy             the application instance providing the data to
   **                            indicate the context of the specified
   **                            {@link PortableApplicationRole}.
   ** @param  role               the {@link PortableApplicationRole} form which
   **                            the member has to be revoked.
   ** @param  member             the {@link PortableRoleMember} which contains
   **                            the member that should be revoked from the
   **                            role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void remove(final String policy, final PortableApplicationRole role, final PortableRoleMember member)
    throws ServiceException {

    final String          operation = "removeMembersFromApplicationRole";
    final CompositeData[] composite = { member.toCompositeData(null) };
    final Object[]        parameter = { policy,       role.toCompositeData(null), composite };
    final String[]        signature = { STRING_CLASS, COMPOSITEDATA_CLASS,        COMPOSITEARRAY_CLASS };
    final String[]        arguments = { ENTITY_PRINCIPAL, member.getPrincipalName(), ENTITY_ROLE, role.getPrincipalName() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
    invoke(operation, parameter, signature);
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePermission
  /**
   ** Remove permission from a specfic Application Role.
   ** <p>
   ** Invokes the operation <code>revokeFromApplicationPolicy</code> on a MBean.
   **
   ** @param  policy             the application instance providing the data to
   **                            indicate the context of the specified
   **                            {@link PortableApplicationRole}.
   ** @param  portable           the {@link PortableApplicationRole} from which
   **                            permissions has to be revoked.
   ** @param  permission         the {@link Set} of {@link PortablePermission}s
   **                            which contains information about the grants.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void removePermission(final String policy, final PortableApplicationRole portable, final Set<PortablePermission> permission)
    throws ServiceException {

    final Set<PortablePermission> source = permission(policy, portable);
    // unfortunately don't know why but we cannot not use retainAll on the
    // collection permission; it removes nothing if a match can be identified
    // hence we have to doit on our own, aaaargh
    for (PortablePermission target : permission) {
      boolean contained = false;
      for (PortablePermission cursor : source) {
        if (equal(cursor, target)) {
          contained = true;
          break;
        }
      }
      if (!contained)
        permission.remove(target);
    }

    if (!permission.isEmpty()) {
      final PortableGrantee grantee    = new PortableGrantee( new PortablePrincipal[] { portable }, null);
      final PortableGrant   grant      = new PortableGrant(grantee, permission.toArray(new PortablePermission[0]));
      final CompositeData[] composite  = { grant.toCompositeData(null) };

      final String          operation  = "revokeFromApplicationPolicy";
      final Object[]        parameter  = { policy,       composite };
      final String[]        signature  = { STRING_CLASS, COMPOSITEARRAY_CLASS };
      final String[]        arguments  = { ENTITY_POLICY, ENTITY_PERMISSION, ENTITY_ROLE, portable.getPrincipalName() };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   member
  /**
   ** Validates the membership of the specified {@link PortableRoleMember}
   ** for a specific application role.
   ** <p>
   ** Invokes the operation <code>getMembersForApplicationRole</code> on an MBean.
   **
   ** @param  policy             Application Id where the members has to be
   **                            returned.
   ** @param  role               the {@link PortableApplicationRole} for which
   **                            members has to be returned.
   **
   ** @return                    the {@link PortableRoleMember}s which
   **                            represents the members of the Application Role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean member(final String policy, final PortableApplicationRole role, final PortableRoleMember member)
    throws ServiceException {

    final Set<PortableRoleMember> all = member(policy, role);
    boolean exists = false;
    for (PortableRoleMember cursor : all) {
      if (cursor.getPrincipalName().equals(member.getPrincipalName())) {
        exists = true;
        break;
      }
    }
    return exists;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   member
  /**
   ** Returns the membership for the specified {@link PortableApplicationRole}.
   ** <p>
   ** Invokes the operation <code>getMembersForApplicationRole</code> on an MBean.
   **
   ** @param  policy             Application Id where the members of the
   **                            specified role has to be returned for.
   ** @param  role               the {@link PortableApplicationRole} for which
   **                            members has to be returned.
   **
   ** @return                    the {@link PortableRoleMember}s which
   **                            represents the members of the Application Role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Set<PortableRoleMember> member(final String policy, final PortableApplicationRole role)
    throws ServiceException {

    final String   operation = "getMembersForApplicationRole";
    final Object[] parameter = { policy, role.toCompositeData(null) };
    final String[] signature = { STRING_CLASS,  COMPOSITEDATA_CLASS };
    return member((CompositeData[])invoke(operation, parameter, signature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Returns the permissions granted to the specified
   ** {@link PortablePrincipal}.
   ** <p>
   ** Invokes the operation <code>getPermissions</code> on an MBean.
   **
   ** @param  policy             Application Id where the permissions of the
   **                            specified role has to be returned for.
   ** @param  principal          the {@link PortablePrincipal} for which
   **                            permissions has to be returned.
   **
   ** @return                    the {@link PortablePermission}s which
   **                            represents the permission granted to the
   **                            Application Role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Set<PortablePermission> permission(final String policy, final PortablePrincipal principal)
    throws ServiceException {

    final String   operation = "getPermissions";
    final Object[] parameter = { policy,        principal.toCompositeData(null) };
    final String[] signature = { STRING_CLASS,  COMPOSITEDATA_CLASS };
    return permission((CompositeData[])invoke(operation, parameter, signature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationPolicyStore
  /**
   ** Returns the JMX {@link ObjectName} to access the application policy store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            application policy store.
   */
  private static ObjectName applicationPolicyStore() {
    if (applicationPolicyStore == null) {
      try {
        // Manages application policies in the policy store configured in the
        // default context.
        // Update or write operations do not require server restart to effect
        // changes. All changes are effected immediately.
        applicationPolicyStore = new ObjectName(JpsJmxConstants.MBEAN_JPS_APPLICATION_POLICY_STORE);
      }
      catch (MalformedObjectNameException e) {
        throw new AssertionError(e.getMessage());
      }
    }
    return applicationPolicyStore;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   member
  /**
   ** Transforms an array of {@link CompositeData} to an array of
   ** {@link PortableRoleMember).
   ** <p>
   ** <b>Note</b>: This is not type safe,
   **
   ** @param  data               an array of {@link CompositeData} to transform
   **                            to an array of {@link PortableRoleMember).
   **
   ** @return                    the transformed {@link PortableRoleMember)s.
   */
  private static Set<PortableRoleMember> member(final CompositeData[] data) {
    final Set<PortableRoleMember> member = new HashSet<PortableRoleMember>(data.length);
    for (CompositeData composite : data)
      member.add(PortableRoleMember.from(composite));
    return member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Transforms an array of {@link CompositeData} to an array of
   ** {@link PortablePermission).
   ** <p>
   ** <b>Note</b>: This is not type safe,
   **
   ** @param  data               an array of {@link CompositeData} to transform
   **                            to an array of {@link PortablePermission).
   **
   ** @return                    the transformed {@link PortablePermission)s.
   */
  private static Set<PortablePermission> permission(final CompositeData[] data) {
    final Set<PortablePermission> permission = new HashSet<PortablePermission>(data.length);
    for (CompositeData composite : data)
      permission.add(PortablePermission.from(composite));
    return permission;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two {@link PortablePermission) if theey are equal.
   **
   ** @param  source             the {@link PortablePermission} to compare
   **                            with the {@link PortablePermission)
   **                            <code>target</code>.
   ** @param  target             the {@link PortablePermission} to compare
   **                            with the {@link PortablePermission)
   **                            <code>source</code>.
   **
   ** @return                    <code>true</code> if both
   **                            {@link PortablePermission} are identically in
   **                            their logical definition.
   */
  private static boolean equal(final PortablePermission source, final PortablePermission target) {
    return (target.getPermissionClassName().equals(source.getPermissionClassName()) && target.getActions().equals(source.getActions()) && target.getTarget().equals(source.getTarget()));
  }
}