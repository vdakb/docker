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

    File        :   GlobalPolicyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GlobalPolicyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Set;
import java.util.HashSet;

import org.apache.tools.ant.BuildException;

import javax.management.ObjectName;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;

import javax.management.openmbean.CompositeData;

import oracle.security.jps.mas.mgmt.jmx.util.JpsJmxConstants;

import oracle.security.jps.mas.mgmt.jmx.policy.PortableGrant;
import oracle.security.jps.mas.mgmt.jmx.policy.PortableGrantee;
import oracle.security.jps.mas.mgmt.jmx.policy.PortablePrincipal;
import oracle.security.jps.mas.mgmt.jmx.policy.PortablePermission;
import oracle.security.jps.mas.mgmt.jmx.policy.PortableCodeSource;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class GlobalPolicyHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage global policy stores in Oracle
 ** WebLogic Server domains.
 ** <p>
 ** <code>SystemPolicy</code> are the system-wide policies applied to all
 ** applications deployed to current management domain.
 ** <p>
 ** <code>Policy</code> can used to grant or revoke special permissions and
 ** privileges to principal or codebase.
 ** <p>
 ** There are two different types of system policies supported by application
 ** server:
 ** <ul>
 **   <li>principal - grants permissions and privileges to a list of users or
 **                   roles.
 **   <li>codebase  - grants permissions and privileges to a codebase,
 **                   which is mostly URL or location of jar file in
 **                   file system defined either by absolute or relative
 **                   path.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class GlobalPolicyHandler extends AbstractPolicyHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Manages global policies in the policy store configured in the default
   ** context.
   ** <p>
   ** Update or write operations do not require server restart to effect
   ** changes. All changes are effected immediately.
   */
  private static ObjectName globalPolicyStore;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the collection of <code>Principal</code>s to grant */
  protected Set<Principal>  assign = new HashSet<Principal>();

  /** the collection of <code>Principal</code>s to revoke */
  protected Set<Principal>  remove = new HashSet<Principal>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>GlobalPolicyHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public GlobalPolicyHandler(final ServiceFrontend frontend) {
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
    return globalPolicyStore();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: addAssign
  /**
   ** Add a collection of {@link Principal}s to the managed members to be
   ** granted.
   **
   ** @param  grantee          the collection of {@link Principal}s to add.
   **
   ** @throws BuildException   if one the specified {@link Principal}s is
   **                          already assigned to this task.
   */
  public void addAssign(final Set<Principal> grantee)
    throws BuildException {

    // prevent bogus input
    if (grantee == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    for (Principal principal : grantee)
      addAssign(principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAssign
  /**
   ** Add the specified {@link Principal} to the policies that have to be
   ** granted.
   **
   ** @param  object             the {@link Principal} that have to be granted.
   **
   ** @throws BuildException     if the specified {@link Principal} is already
   **                            assigned to this task.
   */
  public void addAssign(final Principal object)
    throws BuildException {

    // prevent bogus input
    if (this.assign.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.assign.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRemove
  /**
   ** Add a collection of {@link Principal}s to the managed members to be
   ** revoked.
   **
   ** @param  grantee          the collection of {@link Principal}s to add.
   **
   ** @throws BuildException   if one the specified {@link Principal}s is
   **                          already assigned to this task.
   */
  public void addRemove(final Set<Principal> grantee)
    throws BuildException {

    // prevent bogus input
    if (grantee == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    for (Principal principal : grantee)
      addRemove(principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRemove
  /**
   ** Add the specified {@link Principal} to the policies that have to be
   ** revoked.
   **
   ** @param  object             the {@link Principal} that have to be granted.
   **
   ** @throws BuildException     if the specified {@link Principal} is already
   **                            assigned to this task.
   */
  public void addRemove(final Principal object)
    throws BuildException {

    // prevent bogus input
    if (this.remove.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.remove.add(object);
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

    if (this.assign.isEmpty() && this.remove.isEmpty())
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    for (Principal i : this.assign)
      i.validate();

    for (Principal i : this.remove)
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

    // if the policy to configure provides any detail information about specific
    // permissions to revoke than performe those actions first
    for (Principal policy : this.remove) {
      switch (policy.type) {
        case CODEBASE  : removeCodebase(policy);
                         break;
        case PRINCIPAL : removePrincipal(policy);
                         break;
      }
    }

    for (Principal policy : this.assign) {
      switch (policy.type) {
        case CODEBASE  : removeCodebase(policy);
                         assignCodebase(policy);
                         break;
        case PRINCIPAL : removePrincipal(policy);
                         assignPrincipal(policy);
                         break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignCodebase
  /**
   ** Add permission grants to the System (Global) Policy
   ** <p>
   ** Invokes the operation <code>grantToSystemPolicy</code> on an MBean.
   **
   ** @param  principal          the {@link Principal} policy to grant.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assignCodebase(final Principal principal)
    throws ServiceException {

    // if no grant is configured we have nothing to do
    if (principal.permissionAssign.isEmpty())
      return;

    final PortableCodeSource portable = new PortableCodeSource(principal.name());
    if (exists(portable)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ENTITY_CODEBASE, principal.name()));
    }
    else {
      final PortableGrantee      grantee    = new PortableGrantee(null, portable);
      final PortablePermission[] permission = principal.permissionAssign.toArray(new PortablePermission[0]);
      assignPermission(principal.name(), new PortableGrant(grantee, permission));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeCodebase
  /**
   ** Remove premission grants from the System (Global) Policy
   ** <p>
   ** Invokes the operation <code>removeFromSystemPolicy</code> on an MBean.
   **
   ** @param  principal          the {@link Principal} policy to revoke.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void removeCodebase(final Principal principal)
    throws ServiceException {

    // if no revoke is configured we have nothing to do
    if (principal.permissionRemove.isEmpty())
      return;

    final PortableCodeSource portable = new PortableCodeSource(principal.name());
    if (!exists(portable)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_CODEBASE, principal.name()));
    }
    else {
      final PortableGrantee      grantee    = new PortableGrantee(null, portable);
      final PortablePermission[] permission = principal.permissionRemove.toArray(new PortablePermission[0]);
      removePermission(principal.name(), new PortableGrant(grantee, permission));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignPrincipal
  /**
   ** Add grants to the System (Global) Policy
   ** <p>
   ** Invokes the operation <code>grantToSystemPolicy</code> on an MBean.
   **
   ** @param  principal          the {@link Principal} policy to grant.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assignPrincipal(final Principal principal)
    throws ServiceException {

    // if no grant is configured we have nothing to do
    if (principal.permissionAssign.isEmpty())
      return;

    final PortablePrincipal portable = new PortablePrincipal(principal.clazz, principal.name(), principal.type());
    if (exists(portable)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ENTITY_PRINCIPAL, principal.name()));
    }
    else {
      final PortableGrantee      grantee    = new PortableGrantee( new PortablePrincipal[] { portable }, null);
      final PortablePermission[] permission = principal.permissionAssign.toArray(new PortablePermission[0]);
      assignPermission(principal.name(), new PortableGrant(grantee, permission));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePrincipal
  /**
   ** Remove permission grants from the System (Global) Policy.
   ** <p>
   ** Invokes the operation <code>revokeFromSystemPolicy</code> on an MBean.
   **
   ** @param  principal          the {@link Principal} policy to revoke.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void removePrincipal(final Principal principal)
    throws ServiceException {

    // if no revoke is configured we have nothing to do
    if (principal.permissionRemove.isEmpty())
      return;

    final PortablePrincipal portable = new PortablePrincipal(principal.clazz, principal.name(), PortablePrincipal.PrincipalType.ENT_ROLE);
    if (!exists(portable)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_PRINCIPAL, principal.name()));
    }
    else {
      final PortableGrantee      grantee    = new PortableGrantee(new PortablePrincipal[] { portable }, null);
      final PortablePermission[] permission = principal.permissionRemove.toArray(new PortablePermission[0]);
      removePermission(principal.name(), new PortableGrant(grantee, permission));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignPermission
  /**
   ** Adds permission grants to the System (Global) Policy.
   ** <p>
   ** Invokes the operation <code>grantToSystemPolicy</code> on an MBean.
   **
   ** @param  principal          the {@link Principal} policy to revoke.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assignPermission(final String principal, final PortableGrant grant)
    throws ServiceException {

    final String          operation = "grantToSystemPolicy";
    final CompositeData[] composite = { grant.toCompositeData(null) };
    final Object[]        parameter = { composite };
    final String[]        signature = { COMPOSITEARRAY_CLASS };
    final String[]        arguments = { ENTITY_POLICY, ENTITY_PERMISSION, ENTITY_CODEBASE, principal };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
    invoke(operation, parameter, signature);
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePermission
  /**
   ** Remove grants from the System (Global) Policy.
   ** <p>
   ** Invokes the operation <code>revokeFromSystemPolicy</code> on an MBean.
   **
   ** @param  principal          the {@link Principal} policy to revoke.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void removePermission(final String principal, final PortableGrant grant)
    throws ServiceException {

    final String          operation = "revokeFromSystemPolicy";
    final CompositeData[] composite = { grant.toCompositeData(null) };
    final Object[]        parameter = { composite };
    final String[]        signature = { COMPOSITEARRAY_CLASS };
    final String[]        arguments = { ENTITY_POLICY, ENTITY_PERMISSION, ENTITY_CODEBASE, principal };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
    invoke(operation, parameter, signature);
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Indicates whether the specified codebase given the
   ** {@link PortableCodeSource} instance exists in the credential store.
   ** <p>
   ** Invokes the operation <code>getCodeSourcePermissions</code> on an MBean.
   **
   ** @param  codebase           the {@link PortableCodeSource} to check for
   **                            existance.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean exists(final PortableCodeSource codebase)
    throws ServiceException {

    final PortablePermission[] permission = permission(codebase);
    return (permission.length > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Indicates whether the specified principal given the
   ** {@link PortablePrincipal} instance exists in the credential store.
   ** <p>
   ** Invokes the operation <code>getPermissions</code> on an MBean.
   **
   ** @param  principal          the {@link PortablePrincipal} to check for
   **                            existance.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private boolean exists(final PortablePrincipal principal)
    throws ServiceException {

    final PortablePermission[] permission = permission(principal);
    return (permission.length > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Returns the permissions granted to the passed in
   ** {@link PortableCodeSource}.
   ** <p>
   ** Invokes the operation <code>getCodeSourcePermissions</code> on an MBean.
   **
   ** @param  codebase           the {@link PortableCodeSource} the
   **                            {@link PortablePermission}s are requested for.
   **
   ** @return                    the array of {@link PortablePermission}s
   **                            granted to the {@link PortableCodeSource} in
   **                            the policy store.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private PortablePermission[] permission(final PortableCodeSource codebase)
    throws ServiceException {

    final String               operation  = "getCodeSourcePermissions";
    final Object[]             parameter  = { codebase.toCompositeData(null) };
    final String[]             signature  = { COMPOSITEDATA_CLASS };
    return permission((CompositeData[])invoke(operation, parameter, signature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Returns the permissions granted to the passed in
   ** {@link PortablePrincipal}.
   ** <p>
   ** Invokes the operation <code>getPermissions</code> on an MBean.
   **
   ** @param  principal          the {@link PortablePrincipal} the
   **                            {@link PortablePermission}s are requested for.
   **
   ** @return                    the array of {@link PortablePermission}s
   **                            granted to the {@link PortablePrincipal} in
   **                            the policy store.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private PortablePermission[] permission(final PortablePrincipal principal)
    throws ServiceException {

    final String               operation  = "getPermissions";
    final Object[]             parameter  = { principal.toCompositeData(null) };
    final String[]             signature  = { COMPOSITEDATA_CLASS };
    return permission((CompositeData[])invoke(operation, parameter, signature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** transform the specified array of {@link CompositeData} to an array of
   ** {@link PortablePermission}.
   **
   ** @param  composite          the array of {@link CompositeData} to transform
   **                            to {@link PortablePermission}s.
   **
   ** @return                    the array of {@link PortablePermission}s.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private PortablePermission[] permission(final CompositeData[] composite) {
    final PortablePermission[] permission = new PortablePermission[composite == null ? 0 : composite.length];
    if (composite != null)
      for (int i = 0; i < composite.length; i++)
        permission[i] = PortablePermission.from(composite[i]);
    return permission;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   globalPolicyStore
  /**
   ** Returns the JMX {@link ObjectName} to access the global policy store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            global policy store.
   */
  private static ObjectName globalPolicyStore() {
    if (globalPolicyStore == null) {
      try {
        // Manages global policies in the policy store configured in the default
        // context.
        // Update or write operations do not require server restart to effect
        // changes. All changes are effected immediately.
        globalPolicyStore = new ObjectName(JpsJmxConstants.MBEAN_JPS_GLOBAL_POLICY_STORE);
      }
      catch (MalformedObjectNameException e) {
        throw new AssertionError(e.getMessage());
      }
    }
    return globalPolicyStore;
  }
}