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

    System      :   Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   MDSServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MDSServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
    1.1.0.0     2014-11-29  DSteding    Configuration for namespaces and
                                        customization classes added.
*/

package oracle.hst.deployment.type;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.mds.core.MDSInstance;

import oracle.mds.config.PConfig;
import oracle.mds.config.MDSConfig;
import oracle.mds.config.TypeConfig;
import oracle.mds.config.CustConfig;
import oracle.mds.config.NamespaceConfig;
import oracle.mds.config.MDSConfigurationException;

import oracle.mds.naming.Namespace;

import oracle.mds.sandbox.Sandbox;

import oracle.mds.exception.MDSException;
import oracle.mds.exception.MDSRuntimeException;

import oracle.mds.persistence.MetadataStore;

import oracle.mds.persistence.stores.db.DBMetadataStore;

import oracle.mds.internal.config.DefaultNamespaceConfig;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.mds.type.Metadata;
import oracle.hst.deployment.mds.type.Customization;

////////////////////////////////////////////////////////////////////////////////
// class MDSServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The client environment wrapper of a connection to a Oracle Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.0.0.0
 */
public class MDSServerContext extends AbstractContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE      = "mds";
  static final String        CONTEXT_REFERENCE = "contextRef";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             name              = null;
  private String             partition         = null;

  private DBSServerContext   database          = null;
  private MDSConfig          config            = null;
  private MDSInstance        instance          = null;

  private Sandbox            sandbox           = null;
  private Metadata           metadata          = null;
  private Customization      customization     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MDSServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MDSServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MDSServerContext</code> which use the specified
   ** {@link DBSServerContext} to establish the connection to the persistance
   ** layer.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  database           the {@link DBSServerContext} used to establish
   **                            the connection to the persistance layer.
   **                            <br>
   **                            Allowed object is {@link DBSServerContext}.
   ** @param  name               the name of the instance in a Oracle Metadata
   **                            Store.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  partition          the value for the attribute
   **                            <code>partition</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public MDSServerContext(final DBSServerContext database, final String name, final String partition) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.database  = database;
    this.name      = name;
    this.partition = partition;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid (overridden)
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>refid</code>.
   **
   ** @param  reference          the id of this instance.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference) {
    Object other = reference.getReferencedObject(getProject());
    if(other instanceof MDSServerContext) {
      MDSServerContext that = (MDSServerContext)other;
      this.database = that.database;
      this.metadata = that.metadata;
      this.instance = that.instance;
      // ensure inheritance
      super.setRefid(reference);
    }
    else
      handleReferenceError(reference, CONTEXT_TYPE, other.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link ServerType} in the build script hierarchy.
   **
   ** @param  reference          the attribute value convertable to a
   **                            {@link MDSServerContext}.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   */
  public void setContextRef(final Reference reference) {
    // prevent bogus input
    final Object other = reference.getReferencedObject(this.getProject());
    if (!(other instanceof DBSServerContext))
      handleReferenceError(reference, DBSServerContext.CONTEXT_TYPE, other.getClass());

    database((DBSServerContext)other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalRef (overrridden)
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>principal</code> as a {@link Reference} of a declared
   ** {@link SecurityPrincipal} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link SecurityPrincipal}.
   */
  @Override
  public void setPrincipalRef(final Reference reference) {
    // prevent bogus input
    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof SecurityPrincipal))
      handleReferenceError(reference, SecurityPrincipal.TYPE, object.getClass());

    principal((SecurityPrincipal)object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the instance in a Oracle Metadata
   **                            Store.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setName(final String name) {
    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("name");

    this.name = name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the entity instance in a Oracle Metadata Store.
   **
   ** @return                    the name of the entity instance in a Oracle
   **                            Metadata Store.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   database
  /**
   ** Call by the ANT kernel to inject the argument for parameter database.
   **
   ** @param  database           the {@link DBSServerContext} used to establish
   **                            the connection to the persistance layer.
   **                            <br>
   **                            Allowed object is {@link DBSServerContext}.
   **
   ** @throws BuildException     if the <code>database</code> context is
   **                            already specified by a reference.
   */
  public final void database(final DBSServerContext database) {
    // prevent bogus input
    if (this.database != null)
      handleAttributeError("database");

    this.database = database;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   principal (overrridden)
  /**
   ** Call by the ANT kernel to inject the argument for parameter serverType.
   **
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Allowed object is {@link SecurityPrincipal}.
   **
   ** @throws BuildException     if the <code>principal</code> attribute is
   **                            already specified by a reference.
   */
  @Override
  public final void principal(final SecurityPrincipal principal) {
    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("principal");

    this.database.principal(principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principal (overrridden)
  /**
   ** Returns the security principal used to establish a connection to the
   ** server.
   **
   ** @return                    the security principal used to establish a
   **                            connection to the server.
   **                            <br>
   **                            Possible object is {@link SecurityPrincipal}.
   */
  @Override
  public final SecurityPrincipal principal() {
    return this.database.principal();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername (overrridden)
  /**
   ** Call by the ANT kernel to inject the argument for parameter principalName.
   **
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  @Override
  public final void setUsername(final String username) {
    this.database.setUsername(username);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username (overrridden)
  /**
   ** Returns the name of the security principal of the Database Server used to
   ** connect to.
   **
   ** @return                    the name of the security principal Database
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String username() {
    return this.database.username();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword (overrridden)
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** principalPassword.
   **
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  @Override
  public final void setPassword(final String password) {
    this.database.setPassword(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (overrridden)
  /**
   ** Returns the password of the security principal of the Database Server
   ** used to connect to.
   **
   ** @return                    the password of the security principal Database
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String password() {
    return this.database.password();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPartition
  /**
   ** Call by the ANT kernel to inject the <code>partition</code> attribute.
   **
   ** @param  partition          the value for the attribute
   **                            <code>partition</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>partition</code> attribute is
   **                            already specified by a reference.
   */
  public final void setPartition(final String partition){
    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("partition");

    this.partition = partition;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the <code>partition</code> attribute of this context.
   **
   ** @return                    the <code>partition</code> attribute of this
   **                            context.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String partition() {
    return this.partition;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata
  /**
   ** Returns the <code>metadata</code> attribute of this context.
   **
   ** @return                    the <code>metadata</code> attribute of this
   **                            context.
   **                            <br>
   **                            Possible object is {@link Metadata}.
   */
  public final Metadata metadata() {
    return this.metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   customization
  /**
   ** Returns the <code>customization</code> attribute of this context.
   **
   ** @return                    the <code>customization</code> attribute of
   **                            this context.
   **                            <br>
   **                            Possible object is {@link Customization}.
   */
  public final Customization customization() {
    return this.customization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Metadata Store.
   **
   ** @return                    the fullqualified URL to the Metadata Store.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextURL() {
    return this.database.contextURL();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link MDSInstance}.
   **
   ** @return                    the {@link MDSInstance}.
   **                            <br>
   **                            Possible object is {@link MDSInstance}.
   */
  public final MDSInstance instance() {
    return this.instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activeSandbox
  /**
   ** Sets the {@link Sandbox} activated in a Oracle Metadata Store.
   **
   ** @param  sandbox            the {@link Sandbox} activated in a Oracle
   **                            Metadata Store.
   **                            <br>
   **                            Allowed object is {@link Sandbox}.
   */
  public void activeSandbox(final Sandbox sandbox) {
    this.sandbox = sandbox;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   activeSandbox
  /**
   ** Returns the {@link Sandbox} activated in a Oracle Metadata Store.
   **
   ** @return                    the {@link Sandbox} activated in a Oracle
   **                            Metadata Store.
   **                            <br>
   **                            Possible object is {@link Sandbox}.
   */
  public final Sandbox activeSandbox() {
    return this.sandbox;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredMetadata
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Metadata}.
   **
   ** @param  metadata           the subject of maintenance.
   **                            <br>
   **                            Allowed object is {@link Metadata}.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Metadata</code>
   */
  public void addConfiguredMetadata(final Metadata metadata)
    throws BuildException {

    checkChildrenAllowed();

    // check if we have this metadata path already
    if (this.metadata != null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.METADATA_CONFIG_ONLYONCE));

    // ensure inheritance
    this.metadata = metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCustomization
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Customization}.
   **
   ** @param  customization      the subject of maintenance.
   **                            <br>
   **                            Allowed object is {@link Customization}.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Customization</code>
   */
  public void addConfiguredCustomization(final Customization customization)
    throws BuildException {

    checkChildrenAllowed();

    // check if we have this metadata path already
    if (this.customization != null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.CUSTCLASS_CONFIG_ONLYONCE));

    // ensure inheritance
    this.customization = customization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    if (StringUtility.isEmpty(name))
      handleAttributeError("name");

    if (StringUtility.isEmpty(partition))
      handleAttributeError("partition");

    if (this.database == null)
      handleAttributeError(CONTEXT_REFERENCE);

    this.database.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of this task that
   ** afterwards can be passed to establish a connection to the target system.
   **
   ** @return                    the context this task use to communicate with
   **                            the MDS database server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   **
   ** @throws ServiceException   if the method is not able to retun a valid
   **                            URL.
   */
  public final Properties environment()
    throws ServiceException {

    final Properties environment = new Properties();
    environment.put(DBMetadataStore.JDBC_URL,       this.database.contextURL());
    environment.put(DBMetadataStore.JDBC_USER_NAME, this.username());
    environment.put(DBMetadataStore.JDBC_PASSWORD,  this.password());
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType (overridden)
  /**
   ** Returns the specific type of the implemented context.
   **
   ** @return                    the specific type of the implemented context.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String contextType() {
    return CONTEXT_TYPE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (overridden)
  /**
   ** Establish a connection to the Metadata Store and creates the connection
   ** used during task execution.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  public void connect()
    throws ServiceException {

    if (this.established())
      throw new ServiceException(ServiceError.UNHANDLED);

    // be optimistic
    this.established(true);
    MDSConfig config = null;
    try {
      // create a metadata store that leverage a database
      final MetadataStore store = new DBMetadataStore(this.database.username(), this.database.password(), this.database.contextURL(), this.partition);
      config = new MDSConfig(null, new PConfig(store), null);
    }
    catch (Exception e) {
      this.established(false);
      throw new ServiceException(ServiceError.UNHANDLED, e.getLocalizedMessage());
    }

    try {
      this.instance = aquireInstance(this.name, config);
    }
    catch (ServiceException e) {
      this.established(false);
      throw e;
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (overridden)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disconnect()
    throws ServiceException {

    releaseInstance(this.name);
    this.established(false);
    this.instance    = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Factory method to create the an instance of {@link MDSConfig}.
   ** <p>
   **  If it is not already created, creates one based on a pre-defined
   **  provider.xsd and two CustomizationClass types.
   ** <p>
   ** The defaul implementation is specifying the persistance configuration only
   ** for the created configuration.
   ** <br>
   ** Type configuration and Customization class configuration are not assigned
   ** to the created metadata store configuration.
   **
   **
   ** @return                    the configuration to connect to the metadata
   **                            store containing persitance configuration only.
   **                            <br>
   **                            Possible object is {@link MDSConfig}.
   **
   ** @throws ServiceException   if the connection instance cannot be created.
   */
  public MDSConfig config()
    throws ServiceException {

    if (this.config == null)
      this.config = new MDSConfig(typeConfig(), persistenceConfig(), customizationConfig());

    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeConfig
  /**
   ** Factory method to create the type configuration {@link TypeConfig}.
   **
   ** @return                    the type configuration {@link TypeConfig}.
   **                            <br>
   **                            Possible object is {@link TypeConfig}.
   **
   ** @throws ServiceException   if the type instance cannot be created.
   */
  public TypeConfig typeConfig()
    throws ServiceException {

    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   persistenceConfig
  /**
   ** Factory method to create the persistence configuration {@link PConfig}.
   **
   ** @return                    the persistence configuration {@link PConfig}.
   **                            <br>
   **                            Possible object is {@link PConfig}.
   **
   ** @throws ServiceException   if the persistence instance cannot be created.
   */
  public PConfig persistenceConfig()
    throws ServiceException {

    try {
      // create a metadata store that leverage a database
      final MetadataStore          store = new DBMetadataStore(this.database.username(), this.database.password(), this.database.contextURL(), this.partition);
      // create the default namespace configuration
      final List<NamespaceConfig>  nscfg = new ArrayList<NamespaceConfig>(1);
      // add a DefaultNamespaceConfig is nothing else as a Namespace but the
      // class type is used internally by PConfig to determine to which
      // namespace list the namespace will be added
      nscfg.add(new DefaultNamespaceConfig(Namespace.createDefaultNamespace(null), store));
      // add all configured namespaces to the list of namespace if there are any
      if (this.metadata != null) {
        final Iterator<String> i = this.metadata.iterator();
        while (i.hasNext())
          nscfg.add(new NamespaceConfig(Namespace.create(i.next(), null), store));
      }

      return new PConfig(nscfg.toArray(new NamespaceConfig[0]));
    }
    catch (MDSException e) {
      this.established(false);
      throw new ServiceException(ServiceError.UNHANDLED, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   customizationConfig
  /**
   ** Factory method to create the customization configuration
   ** {@link CustConfig}.
   **
   ** @return                    the customization configuration
   **                            {@link CustConfig}.
   **                            <br>
   **                            Possible object is {@link CustConfig}.
   **
   ** @throws ServiceException   if the customization instance cannot be
   **                            created.
   */
  public CustConfig customizationConfig()
    throws ServiceException {

    try {
      return (this.customization == null) ? null : this.customization.config();
    }
    catch (MDSConfigurationException e) {
      this.established(false);
      throw new ServiceException(ServiceError.UNHANDLED, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquireInstance
  /**
   ** Creates the instance to the metadata store.
   **
   ** @param  name               the name of the connection registered in
   **                            the connection cache.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  config             the {@link MDSConfig} used to perform the
   **                            operation.
   **                            <br>
   **                            Possible object is {@link MDSConfig}.
   **
   ** @return                    the instance to the metadata store.
   **                            <br>
   **                            Possible object is {@link MDSInstance}.
   **
   ** @throws ServiceException   if the connection instance cannot be created.
   */
  protected MDSInstance aquireInstance(final String name, final MDSConfig config)
    throws ServiceException {

    // create an instance to the metadata store leveraging the provided
    // configuration and will be accesssible by the provided name
    // if an instance already existe for the provided name it will be reused to
    // create a session
    MDSInstance instance = null;
    try {
      trace(ServiceResourceBundle.string(ServiceMessage.METADATA_INSTANCE_CREATE));
      instance = MDSInstance.getOrCreateInstance(name, config);
      trace(ServiceResourceBundle.string(ServiceMessage.METADATA_INSTANCE_CREATED));
    }
    catch (MDSConfigurationException e) {
      final ServiceException ex =  new ServiceException(ServiceError.METADATA_INSTANCE_CREATE, e);
      error(ex.getLocalizedMessage());
      throw ex;
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   releaseInstance
  /**
   ** Release all resources aquired for the metadata store.
   **
   ** @param  name               the name of the connection registered in
   **                            the connection cache.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ServiceException   if the connection instance cannot be closed.
   */
  protected void releaseInstance(final String name)
    throws ServiceException {

    // release an instance from the metadata store
    if (MDSInstance.getInstance(name) != null)
      try {
        trace(ServiceResourceBundle.string(ServiceMessage.METADATA_INSTANCE_CLOSE));
        MDSInstance.releaseInstance(name);
        trace(ServiceResourceBundle.string(ServiceMessage.METADATA_INSTANCE_CLOSED));
      }
      catch (MDSRuntimeException e) {
        ServiceException ex =  new ServiceException(ServiceError.METADATA_INSTANCE_CLOSE, e);
        error(ex.getLocalizedMessage());
        throw ex;
      }
  }
}