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

    File        :   ServerSecurityHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerSecurityHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import javax.management.ObjectName;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.MBeanServerConnection;
import javax.management.InstanceNotFoundException;
import javax.management.AttributeNotFoundException;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServerSecurityHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage the security context in Oracle
 ** WebLogic Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerSecurityHandler extends AbstractServerHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String AUTHENTICATION_PROVIDER = "AuthenticationProviders";
  private static final String DEFAULT_AUTHENTICATOR   = "DefaultAuthenticator";
  private static final String DEFAULT_REALM           = "DefaultRealm";

  private static final String ENTITY_ROLE             = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_ROLE);
  private static final String ENTITY_USER             = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_USER);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the single <code>User</code> to configure or to investigate */
  protected String            realm         = DEFAULT_REALM;

  /** the single <code>User</code> to configure or to investigate */
  protected String            authenticator = DEFAULT_AUTHENTICATOR;

  /** the single <code>User</code> to configure or to investigate */
  protected User              singleUser;

  /** the multiple <code>User</code>s to configure or to create */
  protected List<User>        multipleUser = new ArrayList<User>();

  /** the single <code>Role</code> to configure or to investigate */
  protected Role              singleRole;

  /** the multiple <code>Role</code>s to configure or to create */
  protected List<Role>        multipleRole = new ArrayList<Role>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class DescriptiveName
  // ~~~~~ ~~~~~~~~~~~~~~~
  public static class DescriptiveName extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    String description = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>DescriptiveName</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public DescriptiveName() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>DescriptiveName</code> with the specified name.
     **
     ** @param  name             the name of the new name instance.
     */
    protected DescriptiveName(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Called to inject the argument for parameter <code>description</code>.
     **
     ** @param  description      the description to handle in Oracle WebLogic
     **                          Domain MBean Server.
     */
    public final void description(final String description) {
      this.description = description;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class User
  // ~~~~~ ~~~~
  public static class User extends DescriptiveName {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    String password = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>User</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public User() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>User</code> with the specified name.
     **
     ** @param  name             the name of the new user.
     */
    protected User(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: password
    /**
     ** Called to inject the argument for parameter <code>password</code>.
     **
     ** @param  password         the password to handle in Oracle WebLogic
     **                          Domain MBean Server.
     */
    public final void password(final String password) {
      this.password = password;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate (overridden)
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

      if (this.action == ServiceOperation.create && StringUtility.isEmpty(this.password))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "password"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Role
  // ~~~~~ ~~~~
  public static class Role extends DescriptiveName {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    List<User> userAssign = new ArrayList<User>();
    List<User> userRevoke = new ArrayList<User>();
    List<Role> roleAssign = new ArrayList<Role>();
    List<Role> roleRevoke = new ArrayList<Role>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Role</code> with the specified name.
     **
     ** @param  name             the name of the new role.
     */
    protected Role(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate (overridden)
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

      for (User i : this.userAssign)
        i.validate();

      for (User i : this.userRevoke)
        i.validate();

      for (Role i : this.roleAssign)
        i.validate();

      for (Role i : this.roleRevoke)
        i.validate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addGrant
    /**
     ** Add the specified { @link User} to the managed members that has to be
     ** granted.
     **
     ** @param  member           the {@link User} to add as to be granted.
     **
     ** @throws BuildException   if the specified {@link User} is already
     **                          assigned to this task.
     */
    public void addGrant(final User member)
      throws BuildException {

      // prevent bogus input
      if (member.action != null && member.action == ServiceOperation.delete)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, "action"));

      // prevent bogus input
      if (this.userAssign.contains(member) || this.userRevoke.contains(member))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, member.name()));

      // add the instance to the object to handle
      this.userAssign.add(member);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addGrant
    /**
     ** Add the specified { @link User} to the managed members that has to be
     ** granted.
     **
     ** @param  member           the {@link User} to add as to be granted.
     **
     ** @throws BuildException   if the specified {@link User} is already
     **                          assigned to this task.
     */
    public void addGrant(final Role member)
      throws BuildException {

      // prevent bogus input
      if (member.action != null && member.action == ServiceOperation.delete)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, "action"));

      // prevent bogus input
      if (this.roleAssign.contains(member) || this.roleRevoke.contains(member))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, member.name()));

      // add the instance to the object to handle
      this.roleAssign.add(member);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addRevoke
    /**
     ** Add the specified { @link User} to the managed members that has to be
     ** revoked.
     **
     ** @param  member           the {@link User} to add as to be revoked.
     **
     ** @throws BuildException   if the specified {@link User} is already
     **                          assigned to this task.
     */
    public void addRevoke(final User member)
      throws BuildException {

      // prevent bogus input
      if (member.action != null && member.action == ServiceOperation.create)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, "action"));

      // prevent bogus input
      if (this.userAssign.contains(member) || this.userRevoke.contains(member))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, member.name()));

      // add the instance to the object to handle
      this.userRevoke.add(member);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addRevoke
    /**
     ** Add the specified { @link Role} to the managed members that has to be
     ** revoked.
     **
     ** @param  member           the {@link Role} to add as to be revoked.
     **
     ** @throws BuildException   if the specified {@link Role} is already
     **                          assigned to this task.
     */
    public void addRevoke(final Role member)
      throws BuildException {

      // prevent bogus input
      if (member.action != null && member.action == ServiceOperation.create)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, "action"));

      // prevent bogus input
      if (this.roleAssign.contains(member) || this.roleRevoke.contains(member))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, member.name()));

      // add the instance to the object to handle
      this.roleRevoke.add(member);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServerSecurityHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ServerSecurityHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   realm
  /**
   ** Sets the name of the security realm to use for managing users and groups.
   **
   ** @param  realm               the name of the security realm to use for
   **                             managing users and groups.
   */
  public final void realm(final String realm) {
    this.realm = realm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticator
  /**
   ** Stes the name of the authenticator to use for managing users and groups.
   **
   ** @param  authenticator       the name of the authenticator to use for
   **                             managing users and groups.
   */
  public final void authenticator(final String authenticator) {
    this.authenticator = authenticator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (overridden)
  /**
   ** Returns the JMX {@link ObjectName} to access the credential store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            credential store.
   */
  @Override
  protected final ObjectName objectName() {
    try {
      return securityConfiguration();
    }
    catch (Exception e) {
      throw new AssertionError(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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

    if (StringUtility.isEmpty(this.realm))
      handleAttributeMissing("realm");

    if (StringUtility.isEmpty(this.authenticator))
      handleAttributeMissing("authenticator");

    if (this.singleUser == null && this.multipleUser.isEmpty() && this.singleRole == null && this.multipleRole.isEmpty())
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (this.singleUser != null)
      this.singleUser.validate();

    if (this.singleRole != null)
      this.singleRole.validate();

    for (User i : this.multipleUser)
      i.validate();

    for (Role i : this.multipleRole)
      i.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified role to the managed roles.
   **
   ** @param  object             the {@link Role} to add.
   **
   ** @throws BuildException     if the specified {@link Role} is already
   **                            assigned to this task.
   */
  public void add(final Role object)
    throws BuildException {

    // prevent bogus input
    if ((this.singleRole != null && this.singleRole.equals(object)) || this.multipleRole.contains(object))
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_ONLYONCE));

    // add the instance to the object to handle
    this.multipleRole.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified role to the managed credentials.
   ** <p>
   ** A {@link User} instance can only be added if a role attribute is defined
   ** on the task.
   **
   ** @param  object           the {@link User} to add.
   **
   ** @throws BuildException   if the specified {@link User} is already
   **                          assigned to this task.
   */
  public void add(final User object)
    throws BuildException {

    // prevent bogus input
    if ((this.singleUser != null && this.singleUser.equals(object)) || this.multipleUser.contains(object))
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_ONLYONCE));

    this.multipleUser.add(object);
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

    this.connection = connection;

    // create the required ObjectNames
    ObjectName authenticator = authenticator(this.realm, this.authenticator);
    if (authenticator == null) {
      if (failonerror()) {
        final String[] parameter = {this.realm, this.authenticator };
        throw new ServiceException(ServiceError.SECURITY_AUTHENTICATOR_NOTEXISTS, parameter);
      }
      else {
        error(ServiceResourceBundle.format(ServiceError.SECURITY_AUTHENTICATOR_NOTEXISTS, realm, authenticator));
        return;
      }
    }

    if (this.singleRole != null)
      dispatch(authenticator, this.singleRole);

    if (this.singleUser != null)
      dispatch(authenticator, this.singleUser);

    for (Role role : this.multipleRole)
      dispatch(authenticator, role);

    for (User user : this.multipleUser)
      dispatch(authenticator, user);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Performs all action on the {@link User} instance through the given
   ** {@link MBeanServerConnection}.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to maintain the specified {@link User}.
   ** @param  user               the {@link User} the configured actions has to
   **                            be performed for.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void dispatch(final ObjectName authenticator, final User user)
    throws ServiceException {

    switch (user.action) {
      case create : create(authenticator, user);
                    break;
      case modify : modify(authenticator, user);
                    break;
      case delete : delete(authenticator, user);
      case none   : break;
      default     : error(ServiceResourceBundle.format(ServiceError.OPERATION_UNSUPPORTED, user.action.toString(), User.class.getName()));
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Performs all action on the {@link Role} instance through the given
   ** {@link MBeanServerConnection}.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to maintain the specified {@link Role}.
   ** @param  role               the {@link Role} the configured actions has to
   **                            be performed for.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void dispatch(final ObjectName authenticator, final Role role)
    throws ServiceException {

    switch (role.action) {
      case create : create(authenticator, role);
                    break;
      case modify : modify(authenticator, role);
                    break;
      case delete : delete(authenticator, role);
                    break;
      case none   : member(authenticator, role);
                    break;
      default     : error(ServiceResourceBundle.format(ServiceError.OPERATION_UNSUPPORTED, role.action.toString(), Role.class.getName()));
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Performs all action on the {@link Role} instance to manage membership
   ** actions through the given {@link MBeanServerConnection}.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to maintain the specified {@link Role}.
   ** @param  role               the {@link Role} the configured membershiop
   **                            actions has to be performed for.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void member(final ObjectName authenticator, final Role role)
    throws ServiceException {

    for (Role member : role.roleRevoke)
      revoke(authenticator, role, member);

    for (Role member : role.roleAssign)
      assign(authenticator, role, member);

    for (User member : role.userRevoke)
      revoke(authenticator, role, member);

    for (User member : role.userAssign)
      assign(authenticator, role, member);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an enterprise user given the {@link User} instance in the
   ** authenticator specifed by {@link ObjectName} <code>authenticator</code>.
   ** <p>
   ** Invokes the operation <code>createUser</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to create the specified {@link User}.
   ** @param  user               the {@link User} instance providing the data
   **                            to create the enterprise user.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final ObjectName authenticator, final User user)
    throws ServiceException {

    if (exists(authenticator, user)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ENTITY_USER, user.name()));
      modify(authenticator, user);
    }
    else {
      final String   operation = "createUser";
      final Object[] parameter = { user.name(),  user.password, user.description };
      final String[] signature = { STRING_CLASS, STRING_CLASS,  STRING_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, ENTITY_USER, user.name()));
      invoke(authenticator, operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, ENTITY_USER, user.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an enterprise role given the {@link User} instance in the
   ** authenticator specifed by {@link ObjectName} <code>authenticator</code>.
   ** <p>
   ** Invokes the operation <code>setUserDescription</code> if the attribute
   ** <code>description</code> is not <code>null</code> and/or
   ** <code>resetUserPassword</code> if the attribute <code>password</code> is
   ** not <code>null</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to modify the specified {@link User}.
   ** @param  user               the {@link User} instance providing the data
   **                            to modify the enterprise role description.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void modify(final ObjectName authenticator, final User user)
    throws ServiceException {

    if (!exists(authenticator, user)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_USER, user.name()));
    }
    else {
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, ENTITY_USER, user.name()));
      if (user.description != null) {
        final String   operation = "setUserDescription";
        final Object[] parameter = { user.name(),  user.description };
        final String[] signature = { STRING_CLASS, STRING_CLASS };
        invoke(authenticator, operation, parameter, signature);
      }
      if (user.password != null) {
        final String   operation = "resetUserPassword";
        final Object[] parameter = { user.name(),  user.password };
        final String[] signature = { STRING_CLASS, STRING_CLASS};
        invoke(authenticator, operation, parameter, signature);
      }
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, ENTITY_USER, user.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an enterprise user given the {@link User} instance in the
   ** authenticator specifed by {@link ObjectName} <code>authenticator</code>.
   ** <p>
   ** Invokes the operation <code>removeUser</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to delete the specified {@link User}.
   ** @param  user               the {@link User} instance providing the data
   **                            to create the enterprise user.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final ObjectName authenticator, final User user)
    throws ServiceException {

    if (!exists(authenticator, user)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_USER, user.name()));
    }
    else {
      final String   operation = "removeUser";
      final Object[] parameter = { user.name() };
      final String[] signature = { STRING_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, ENTITY_USER, user.name()));
      invoke(authenticator, operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, ENTITY_USER, user.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an enterprise role given the {@link Role} instance in the
   ** authenticator specifed by {@link ObjectName} <code>authenticator</code>.
   ** <p>
   ** Invokes the operation <code>createGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to create the specified {@link Role}.
   ** @param  role               the {@link Role} instance providing the data
   **                            to create the enterprise role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final ObjectName authenticator, final Role role)
    throws ServiceException {

    if (exists(authenticator, role)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ENTITY_ROLE, role.name()));
      modify(authenticator, role);
    }
    else {
      final String   operation = "createGroup";
      final Object[] parameter = { role.name(),  role.description };
      final String[] signature = { STRING_CLASS, STRING_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, ENTITY_ROLE, role.name()));
      invoke(authenticator, operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, ENTITY_ROLE, role.name()));
      // check if we have to do something on memebership relations
      member(authenticator, role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an enterprise role given the {@link Role} instance in the
   ** authenticator specifed by {@link ObjectName} <code>authenticator</code>.
   ** <p>
   ** Invokes the operation <code>setGroupDescription</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to modify the specified {@link Role}.
   ** @param  role               the {@link Role} instance providing the data
   **                            to modify the enterprise role description.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void modify(final ObjectName authenticator, final Role role)
    throws ServiceException {

    if (!exists(authenticator, role)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_ROLE, role.name()));
    }
    else {
      // modify the description only if the attribute is set
      if (role.description != null) {
        final String   operation = "setGroupDescription";
        final Object[] parameter = { role.name(),  role.description };
        final String[] signature = { STRING_CLASS, STRING_CLASS };
        warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, ENTITY_ROLE, role.name()));
        invoke(authenticator, operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, ENTITY_ROLE, role.name()));
      }
      // check if we have to do something on memebership relations
      member(authenticator, role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an enterprise role given the {@link Role} instance in the
   ** authenticator specifed by {@link ObjectName} <code>authenticator</code>.
   ** <p>
   ** Invokes the operation <code>removeGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to delete the specified {@link Role}.
   ** @param  role               the {@link Role} instance providing the data
   **                            to delete the enterprise role.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final ObjectName authenticator, final Role role)
    throws ServiceException {

    if (!exists(authenticator, role)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_ROLE, role.name()));
    }
    else {
      // maintain the membership on roles first before the delete operation
      // itself happens
      member(authenticator, role);
      // remove the role from the system
      final String   operation = "removeGroup";
      final Object[] parameter = { role.name() };
      final String[] signature = { STRING_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, ENTITY_ROLE, role.name()));
      invoke(authenticator, operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, ENTITY_ROLE, role.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Grants membership on enterprise roles given the {@link Role} instance
   ** <code>role</code>.
   ** <p>
   ** Invokes the operation <code>addMemberToGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to grant the specified {@link Role}
   **                            <code>role</code> to the specified {@link Role}
   **                            <code>grantee</code>.
   ** @param  role               the existing {@link Role} instance providing
   **                            the data to which the <code>grantee</code> has
   **                            to be assinged to.
   ** @param  grantee            the {@link Role} instance providing the data
   **                            to be assigned to the enterprise role
   **                            <code>role</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assign(final ObjectName authenticator, final Role role, final Role grantee)
    throws ServiceException {

    // at first dispatch all actions that belongs to the grantee role
    // this includes create and/or delete regarding the action configured on
    // that type
    dispatch(authenticator, grantee);

    if (!exists(authenticator, grantee)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_ROLE, grantee.name()));
    }
    else {
      if (!member(authenticator, role.name(), grantee.name(), Boolean.TRUE))
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, ENTITY_ROLE, grantee.name()));
      else {
        final String[] arguments = { ENTITY_ROLE, role.name(), ENTITY_ROLE, grantee.name() };
        warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
        assign(authenticator, role.name(), grantee.name());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Grants membership on enterprise roles given the {@link Role} instance
   ** <code>role</code>.
   ** <p>
   ** Invokes the operation <code>addMemberToGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to grant the specified {@link Role}
   **                            <code>role</code> to the specified {@link User}
   **                            <code>grantee</code>.
   ** @param  role               the existing {@link Role} instance providing
   **                            the data to which the <code>grantee</code> has
   **                            to be assinged to.
   ** @param  grantee            the {@link User} instance providing the data
   **                            to be assigned to the enterprise role
   **                            <code>role</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assign(final ObjectName authenticator, final Role role, final User grantee)
    throws ServiceException {

    // at first dispatch all actions that belongs to the grantee user
    // this includes create and/or delete regarding the action configured on
    // that type
    dispatch(authenticator, grantee);

    if (!exists(authenticator, grantee)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_USER, grantee.name()));
    }
    else {
      if (member(authenticator, role.name(), grantee.name(), Boolean.TRUE))
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ALREADYASSINGED, ENTITY_USER, grantee.name()));
      else {
        final String[] arguments = { ENTITY_ROLE, role.name(), ENTITY_USER, grantee.name() };
        warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_BEGIN, arguments));
        assign(authenticator, role.name(), grantee.name());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ASSIGN_SUCCESS, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Grants membership on enterprise roles given the {@link Role} instance
   ** <code>role</code>.
   ** <p>
   ** Invokes the operation <code>addMemberToGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to grant the specified <code>role</code>
   **                            to the specified <code>grantee</code>.
   ** @param  role               the name of an existing to which the
   **                            <code>grantee</code> has to be assigned to.
   ** @param  grantee            the name of a user or role to be assigned to
   **                            the enterprise role <code>role</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void assign(final ObjectName authenticator, final String role, final String grantee)
    throws ServiceException {

    final String   operation = "addMemberToGroup";
    final Object[] parameter = { role,         grantee };
    final String[] signature = { STRING_CLASS, STRING_CLASS };
    invoke(authenticator, operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revokes membership from enterprise roles given the {@link Role} instance
   ** <code>role</code>.
   ** <p>
   ** Invokes the operation <code>removeMemberFromGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to revoke the specified {@link Role}
   **                            <code>grantee</code> from the specified
   **                            {@link Role} <code>role</code>.
   ** @param  role               the existing {@link Role} instance providing
   **                            the data from which the {@link Role}
   **                            <code>grantee</code> has to be revoked from.
   ** @param  grantee            the {@link Role} instance providing the data
   **                            to be revoked from the enterprise role
   **                            <code>role</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void revoke(final ObjectName authenticator, final Role role, final Role grantee)
    throws ServiceException {

    if (!exists(authenticator, grantee)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_ROLE, grantee.name()));
    }
    else {
      if (!member(authenticator, role.name(), grantee.name(), Boolean.FALSE))
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTASSINGED, ENTITY_ROLE, grantee.name()));
      else {
        final String[] arguments = { ENTITY_ROLE, role.name(), ENTITY_ROLE, grantee.name() };
        warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
        revoke(authenticator, role.name(), grantee.name());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
      }
    }

    // at last dispatch all actions that belongs to the grantee role
    // this includes create and/or delete regarding the action configured on
    // that type
    dispatch(authenticator, grantee);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revokes membership from enterprise roles given the {@link Role} instance
   ** <code>role</code>.
   ** <p>
   ** Invokes the operation <code>removeMemberFromGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to revoke the specified {@link User}
   **                            <code>grantee</code> from the specified
   **                            {@link Role} <code>role</code>.
   ** @param  role               the existing {@link Role} instance providing
   **                            the data from which the {@link User}
   **                            <code>grantee</code> has to be revoked from.
   ** @param  grantee            the {@link User} instance providing the data
   **                            to be revoked from the enterprise role
   **                            <code>role</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void revoke(final ObjectName authenticator, final Role role, final User grantee)
    throws ServiceException {

    if (!exists(authenticator, grantee)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_USER, grantee.name()));
    }
    else {
      if (!member(authenticator, role.name(), grantee.name(), Boolean.FALSE))
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTASSINGED, ENTITY_USER, grantee.name()));
      else {
        final String[] arguments = { ENTITY_ROLE, role.name(), ENTITY_USER, grantee.name() };
        warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_BEGIN, arguments));
        revoke(authenticator, role.name(), grantee.name());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_REVOKE_SUCCESS, arguments));
      }
    }

    // at last dispatch all actions that belongs to the grantee user
    // this includes create and/or delete regarding the action configured on
    // that type
    dispatch(authenticator, grantee);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revokes membership from enterprise roles given the {@link Role} instance
   ** <code>role</code>.
   ** <p>
   ** Invokes the operation <code>removeMemberFromGroup</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to revoke the specified {@link User}
   **                            <code>grantee</code> from the specified
   **                            {@link Role} <code>role</code>.
   ** @param  role               the name of an existing from which the
   **                            <code>grantee</code> has to be revoked from.
   ** @param  grantee            the name of a user or role to be revoked from
   **                            the enterprise role <code>role</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void revoke(final ObjectName authenticator, final String role, final String grantee)
    throws ServiceException {

    final String   operation = "removeMemberFromGroup";
    final Object[] parameter = { role,         grantee };
    final String[] signature = { STRING_CLASS, STRING_CLASS };
    invoke(authenticator, operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Indicates whether the specified user exists given the {@link Role}
   ** instance exists in the specified authenticator.
   ** <p>
   ** Invokes the operation <code>groupExists</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to test the existance of the specified
   **                            {@link Role}.
   ** @param  role               the {@link Role} instance providing the data
   **                            to indicate the existance.
   **
   ** @return                    <code>Boolean.TRUE</code> if the user exists
   **                            for the information provided; otherwise
   **                            <code>Boolean.FALSE</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Boolean exists(final ObjectName authenticator, final Role role)
    throws ServiceException {

    final String   operation = "groupExists";
    final Object[] parameter = { role.name() };
    final String[] signature = { STRING_CLASS };
    return (Boolean)invoke(authenticator, operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Indicates whether the specified user exists given the {@link User}
   ** instance exists in the specified authenticator.
   ** <p>
   ** Invokes the operation <code>userExists</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to test the existance of the specified
   **                            {@link User}.
   ** @param  user               the {@link User} instance providing the data
   **                            to indicate the existance.
   **
   ** @return                    <code>Boolean.TRUE</code> if the user exists
   **                            for the information provided; otherwise
   **                            <code>Boolean.FALSE</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Boolean exists(final ObjectName authenticator, final User user)
    throws ServiceException {

    final String   operation = "userExists";
    final Object[] parameter = { user.name() };
    final String[] signature = { STRING_CLASS };
    return (Boolean)invoke(authenticator, operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   member
  /**
   ** Indicates whether the specified {@link User} <code>grantee</code> is a
   ** member of the given {@link Role} <code>role</code> in the specified
   ** authenticator.
   ** <p>
   ** Invokes the operation <code>isMember</code> on an MBean.
   **
   ** @param  authenticator      the {@link ObjectName} of the authenticator
   **                            used to retrieve the membership of the
   **                            specified {@link User}.
   ** @param  role               the {@link Role} instance providing the data
   **                            to indicate the membership.
   ** @param  grantee            the name of a user or role to be checked for
   **                            membership in the enterprise role
   **                            <code>role</code>.
   ** @param  recursive          if set to <code>true</code>, the criteria for
   **                            membership extends to any role within the
   **                            role that is specified by <code>role</code>. If
   **                            this argument is set to <code>false</code>,
   **                            then this method checks only for direct
   **                            membership within the <code>role</code>.
   **
   ** @return                    <code>Boolean.TRUE</code> if the specified
   **                            <code>grantee</code> is member of the
   **                            enterprise role <code>role</code>; otherwise
   **                            <code>Boolen.FALSE</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Boolean member(final ObjectName authenticator, final String role, final String grantee, final Boolean recursive)
    throws ServiceException {

    final String   operation = "isMember";
    final Object[] parameter = { role,         grantee,      recursive };
    final String[] signature = { STRING_CLASS, STRING_CLASS, BOOLEAN_CLASS };
    return (Boolean)invoke(authenticator, operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticator
  /**
   ** Returns the managed bean to maintain security configurations of a
   ** specific authenticator in a Oracle WebLogic Server Domain.
   **
   ** @param  realm              the name of the security realm to obtain the
   **                            authenticator from a Oracle WebLogic Server
   **                            Domain.
   ** @param  authenticator      the name of the requested authenticator in
   **                            <code>realm</code> of a Oracle WebLogic Server
   **                            Domain.
   **
   ** @return                    the managed bean to maintain a specific
   **                            security authenticator configurations in a
   **                            Oracle WebLogic Server Domain.
   **                            Is <code>null</code> if the specified
   **                            authenticator was not found in the specified
   **                            security realm;
   **
   ** @throws ServiceException   if the request fails overall with detailed
   **                            information about the reason.
   */
  private ObjectName authenticator(final String realm, final String authenticator)
    throws ServiceException {

    // create the required ObjectNames
    ObjectName result = null;
    try {
      final ObjectName temp = (ObjectName)this.connection.getAttribute(securityConfiguration(), realm);
      if (temp == null) {
        if (failonerror())
          throw new ServiceException(ServiceError.SECURITY_REALM_NOTEXISTS, realm);
        else
          error(ServiceResourceBundle.format(ServiceError.SECURITY_REALM_NOTEXISTS, realm));
      }
      final ObjectName[] provider = (ObjectName[])connection.getAttribute(temp, AUTHENTICATION_PROVIDER);
      for (ObjectName bean : provider) {
        final String name = (String)this.connection.getAttribute(bean, FIELD_NAME);
        if (name.equals(authenticator)) {
          result = bean;
          break;
        }
      }
    }
    catch (ReflectionException e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    catch (InstanceNotFoundException e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    catch (AttributeNotFoundException e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    catch (MBeanException e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    catch (IOException e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   securityConfiguration
  /**
   ** Returns the managed bean to maintain security configurations in a WebLogic
   ** Server Domain.
   **
   ** @return                    the managed bean to maintain security
   **                            configurations in a WebLogic Server Domain.
   **
   ** @throws ServiceException   if the request fails overall with detailed
   **                            information about the reason.
   */
  private ObjectName securityConfiguration()
    throws ServiceException {

    ObjectName result = null;
    try {
      result = (ObjectName)this.connection.getAttribute(domainConfiguration(), SECURITY_CONFIGURATION);
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(e);
      else
        fatal(e);
    }
    return result;
  }
}