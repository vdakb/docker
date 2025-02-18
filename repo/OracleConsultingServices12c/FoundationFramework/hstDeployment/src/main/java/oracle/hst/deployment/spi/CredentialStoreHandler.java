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

    File        :   CredentialStoreHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CredentialStoreHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.List;
import java.util.ArrayList;

import javax.management.ObjectName;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;

import org.apache.tools.ant.BuildException;

import oracle.security.jps.mas.mgmt.jmx.util.JpsJmxConstants;

import oracle.security.jps.mas.mgmt.jmx.credstore.PortableCredential;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class CredentialStoreHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage credential stores in Oracle WebLogic
 ** Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CredentialStoreHandler extends AbstractInvocationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Manages credential data, that is, the store service configured in the
   ** default context.
   ** <p>
   ** Update or write operations do not require server restart to effect
   ** changes. All changes are effected immediately. Access is restricted to
   ** administrators only.
   */
  private static ObjectName   credentialStore;

  private static final String ENTITY_ALIAS      = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_ALIAS);
  private static final String ENTITY_CREDENTIAL = ServiceResourceBundle.string(ServiceMessage.SECURITY_ENTITY_CREDENTIAL);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the single <code>Alias</code> to configure or to investigate */
  protected Alias               single;

  /** the multiple <code>Alias</code>s to configure or to create */
  protected List<Alias>         multiple = new ArrayList<Alias>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Credential
  // ~~~~~ ~~~~~~~~~~
  public static class Credential extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // we cannot make it final because it has to be writable for various
    // operations like display
    PortableCredential credential = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Credential</code> with the specified name.
     **
     ** @param  action           the {@link ServiceOperation} to apply on the
     **                          specified <code>key</code> and
     **                          {@link PortableCredential}.
     ** @param  key              the credential key.
     ** @param  credential       the {@link PortableCredential} to configure
     **                          may be empty but never <code>null</code>.
     */
    public Credential(final String action, final String key, PortableCredential credential) {
      // ensure inheritance
      this(action == null ? ServiceOperation.none : ServiceOperation.valueOf(action), key, credential);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Credential</code> with the specified name.
     **
     ** @param  action           the {@link ServiceOperation} to apply on the
     **                          specified <code>key</code> and
     **                          {@link PortableCredential}.
     ** @param  key              the credential key.
     ** @param  credential       the {@link PortableCredential} to configure
     **                          may be empty but never <code>null</code>.
     */
    public Credential(final ServiceOperation action, final String key, PortableCredential credential) {
      // ensure inheritance
      super(key);

      // initialize instance attributes
      this.action     = action;
      this.credential = credential;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate (overridden)
    /**
     ** The entry point to validate the type to use.
     ** <br>
     ** <code>action</code> and <code>credential</code> are mandatory for this
     ** type.
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

      if (this.credential == null)
        handleAttributeMissing("credential");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Alias
  // ~~~~~ ~~~~~
  /**
   ** A credential is uniquely identified by a map name and a key name.
   ** <p>
   ** Typically, the map name corresponds with the name of an application and
   ** all credentials with the same map name define a logical group of
   ** credentials, such as the credentials used by the application.
   ** <p>
   ** All map names in a credential store must be distinct.
   */
  public static class Alias extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the single <code>Credential</code> to configure or to investigate */
    Credential       single   = null;

    /** the multiple <code>Credential</code>s to configure or to create */
    List<Credential> multiple = new ArrayList<Credential>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Alias</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Alias() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   credential
    /**
     ** Add the specified credential mapping to the managed credentials.
     **
     ** @param  credential       the {@link Credential} to add.
     **
     ** @throws BuildException   if the specified {@link Credential} is already
     **                          assigned to this task.
     */
    public void credential(final Credential credential)
      throws BuildException {

      if (this.multiple.contains(credential))
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      // set the single instance to the object to handle
      this.single = credential;
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

      // ensure inheritance
      super.validate();

      if (this.action == null)
        handleAttributeMissing("action");

      if (this.action != ServiceOperation.delete && this.single == null && this.multiple.isEmpty())
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      if (this.single != null)
        this.single.validate();

      for (Credential i : this.multiple)
        i.validate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   add
    /**
     ** Add the specified credential mapping to the managed credentials.
     **
     ** @param  credential       the {@link Credential} to add.
     **
     ** @throws BuildException   if the specified {@link Credential} is already
     **                          assigned to this task.
     */
    public void add(final Credential credential)
      throws BuildException {

      // prevent bogus input
      if ((this.single != null && this.single.equals(credential)) || this.multiple.contains(credential))
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      switch (this.action) {
        case create :
        case delete : credential.action = this.action;
      }

      // add the instance to the object to handle
      this.multiple.add(credential);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>CredentialStoreHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public CredentialStoreHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
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
      this.single = new Alias();

    this.single.action(action);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (overridden)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the alias.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void name(final String name)
    throws BuildException {

    if (this.single == null)
      this.single = new Alias();

    this.single.name(name);
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
    return credentialStore();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified alias mapping to the managed aliases.
   **
   ** @param  object             the {@link Alias} to add.
   **
   ** @throws BuildException     if the specified {@link Alias} is already
   **                            assigned to this task.
   */
  public void add(final Alias object)
    throws BuildException {

    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified credential mapping to the managed credentials.
   **
   ** @param  credential       the {@link Credential} to add.
   **
   ** @throws BuildException   if the specified {@link Credential} is already
   **                          assigned to this task.
   */
  public void add(final Credential credential)
    throws BuildException {

    if (this.single == null)
      handleAttributeMissing("alias");

    this.single.add(credential);
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

    if (this.single == null && this.multiple.isEmpty())
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (this.single != null)
      this.single.validate();

    for (Alias i : this.multiple)
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

    for (Alias i : this.multiple)
      dispatch(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Performs all action on the {@link Alias} instance through the given
   ** {@link MBeanServerConnection}.
   **
   ** @param  alias              the {@link Alias} the configured actions has to
   **                            be performed for.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void dispatch(final Alias alias)
    throws ServiceException {

    // if the alias to configure provides any detail information about specific
    // credentails than performe those credential actions
    if (alias.single != null)
      dispatch(alias, alias.single);

    for (Credential credential : alias.multiple)
      dispatch(alias, credential);

    if (alias.action == ServiceOperation.delete)
      delete(alias);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Configures a specific credentials of an alias.
   **
   ** @param  alias              the {@link Alias} to configure accordingly.
   ** @param  credential         the {@link Credential} to configure belonging
   **                            to the specified {@link Alias}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void dispatch(final Alias alias, final Credential credential)
    throws ServiceException {

    switch (credential.action) {
      case create : create(alias, credential);
                    break;
      case modify : modify(alias, credential);
                    break;
      case delete : delete(alias, credential);
      case none   : break;
      default     : error(ServiceResourceBundle.format(ServiceError.OPERATION_UNSUPPORTED, alias.action.toString(), Credential.class.getName()));
                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Removes a credential mapping given the {@link Alias} instance in the
   ** credential store.
   ** <p>
   ** Invokes the operation <code>deleteCredentialMap</code> on an MBean.
   **
   ** @param  alias              the {@link Alias} instance providing the data
   **                            to delete the credential mapping.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final Alias alias)
    throws ServiceException {

    if (!exists(alias))
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_ALIAS, alias.name()));
    else {
      // delete entire map
      final String   operation = "deleteCredentialMap";
      final Object[] parameter = { alias.name() };
      final String[] signature = { STRING_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, ENTITY_ALIAS, alias.name()));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, ENTITY_ALIAS, alias.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Indicates whether the specified alias given the {@link Alias}
   ** instance exists in the credential store.
   ** <p>
   ** Invokes the operation <code>containsMap</code> on an MBean.
   **
   ** @param  alias              the {@link Alias} instance providing the
   **                            data to indicate the existance.
   **
   ** @return                    <code>Boolean.TRUE</code> if the alias exists
   **                            for the information provided; otherwise
   **                            <code>Boolean.FALSE</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Boolean exists(final Alias alias)
    throws ServiceException {

    final String   operation = "containsMap";
    final Object[] parameter = { alias.name() };
    final String[] signature = { STRING_CLASS };
    return (Boolean)invoke(operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Indicates whether the specified credentails given the {@link Credential}
   ** instance exists in the specified authenticator.
   ** <p>
   ** Invokes the operation <code>containsCredential</code> on an MBean.
   **
   ** @param  alias              the {@link Alias} representing the credential
   **                            map used to check the existance of the
   **                            specified {@link Credential}.
   ** @param  credential         the {@link Credential} instance providing the
   **                            data to indicate the existance.
   **
   ** @return                    <code>Boolean.TRUE</code> if the credential
   **                            exists for the information provided; otherwise
   **                            <code>Boolean.FALSE</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private Boolean exists(final Alias alias, final Credential credential)
    throws ServiceException {

    final String   operation = "containsCredential";
    final Object[] parameter = { alias.name(), credential.name() };
    final String[] signature = { STRING_CLASS, STRING_CLASS };
    return (Boolean)invoke(operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Associates the <code>char[]</code> to the specified <code>alias</code> and
   ** <code>key</code>.
   ** <p>
   ** Invokes the operation <code>setPortableCredential</code> or
   ** <code>resetPortableCredential</code> on an MBean.
   ** <br>
   ** Which operation really will be invoked depends on if a credential ecists
   ** for the specified <code>alias</code> and <code>key</code>.
   **
   ** @param  alias              the {@link Alias} of the credential mapping
   **                            used to associate the <code>char[]</code> in
   **                            {@link Credential} with its name.
   ** @param  credential         the {@link Credential} which represents the
   **                            credentials object containing usually a
   **                            username and password.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final Alias alias, final Credential credential)
    throws ServiceException {

    if (exists(alias, credential)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ENTITY_CREDENTIAL, credential.name()));
      modify(alias, credential);
    }
    else {
      final String   operation = "setPortableCredential";
      final Object[] parameter = { alias.name(), credential.name(), credential.credential.toCompositeData(null) };
      final String[] signature = { STRING_CLASS, STRING_CLASS,      COMPOSITEDATA_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, ENTITY_CREDENTIAL, credential.name()));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, ENTITY_CREDENTIAL, credential.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Associates the <code>char[]</code> to the specified <code>alias</code> and
   ** <code>key</code>.
   ** <p>
   ** Invokes the operation <code>resetPortableCredential</code> on an MBean.
   **
   ** @param  alias              the {@link Alias} of the credential mapping
   **                            used to associate the <code>char[]</code> in
   **                            {@link Credential} with its name.
   ** @param  credential         the {@link Credential} which represents the
   **                            credentials object containing usually a
   **                            username and password.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void modify(final Alias alias, final Credential credential)
    throws ServiceException {

    if (!exists(alias, credential))
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_CREDENTIAL, credential.name()));
    else {
      final String   operation = "resetPortableCredential";
      final Object[] parameter = { alias.name(), credential.name(), credential.credential.toCompositeData(null) };
      final String[] signature = { STRING_CLASS, STRING_CLASS, COMPOSITEDATA_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, ENTITY_CREDENTIAL, credential.name()));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, ENTITY_CREDENTIAL, credential.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Removes the Credential objects associated with the specified
   ** <code>alias</code> and <code>key</code>.
   ** <p>
   ** Invokes the operation <code>deleteCredential</code> on an MBean.
   **
   ** @param  alias              the {@link Alias} of the credential mapping
   **                            used to associate the <code>char[]</code> in
   **                            {@link Credential} with its name.
   ** @param  credential         the {@link Credential} which represents the
   **                            credentials object containing usually a
   **                            username and password.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final Alias alias, final Credential credential)
    throws ServiceException {

    if (!exists(alias, credential)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ENTITY_CREDENTIAL, credential.name()));
    }
    else {
      final String   operation = "deleteCredential";
      final Object[] parameter = { alias.name(), credential.name() };
      final String[] signature = { STRING_CLASS, STRING_CLASS };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, ENTITY_CREDENTIAL, credential.name()));
      invoke(operation, parameter, signature);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, ENTITY_CREDENTIAL, credential.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credentialStore
  /**
   ** Returns the JMX {@link ObjectName} to access the credential store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            credential store.
   */
  private static ObjectName credentialStore() {
    if (credentialStore == null) {
      try {
        // Manages credential data, that is, the store service configured in the
        // default context.
        // Update or write operations do not require server restart to effect
        // changes. All changes are effected immediately. Access is restricted
        // to administrators only.
        credentialStore = new ObjectName(JpsJmxConstants.MBEAN_JPS_CREDENTIAL_STORE);
      }
      catch (MalformedObjectNameException e) {
        throw new AssertionError(e.getMessage());
      }
    }
    return credentialStore;
  }
}