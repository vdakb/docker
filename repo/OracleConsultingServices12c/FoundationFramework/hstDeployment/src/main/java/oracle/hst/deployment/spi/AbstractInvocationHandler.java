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

    File        :   AbstractInvocationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractInvocationHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;

import java.io.IOException;

import javax.management.MBeanInfo;
import javax.management.Attribute;
import javax.management.ObjectName;
import javax.management.ObjectInstance;
import javax.management.MBeanException;
import javax.management.MBeanAttributeInfo;
import javax.management.ReflectionException;
import javax.management.MBeanServerConnection;
import javax.management.InstanceNotFoundException;
import javax.management.AttributeNotFoundException;
import javax.management.MalformedObjectNameException;

import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.CompositeDataSupport;

import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularType;
import javax.management.openmbean.TabularDataSupport;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AbstractInvocationHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage java platform security in Oracle
 ** WebLogic Server domains.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow referencing
 ** the enum Action in subclasses of
 ** <code>org.apache.tools.ant.types.EnumeratedAttribute</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractInvocationHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Provides a common access point for navigating to all runtime and
   ** configuration MBeans in the domain as well as to MBeans that provide
   ** domain-wide services (such as controlling and monitoring the life cycles
   ** of servers and message-driven EJBs and coordinating the migration of
   ** migratable services).
   ** <p>
   ** This MBean is available only on the Administration Server.
   */
  public static final String    DOMAIN_SERVER          = "/jndi/weblogic.management.mbeanservers.domainruntime";

  /**
   ** To monitor only active configuration MBeans (and not runtime MBeans), use
   ** a Runtime MBean Server.
   ** <p>
   ** Monitoring through a Runtime MBean Server requires less memory and network
   ** traffic than monitoring through the Domain Runtime MBean Server.
   ** (WebLogic Server does not initialize the Domain Runtime MBean Server until
   ** a client requests a connection to it.)
   */
  public static final String    RUNTIME_SERVER         = "/jndi/weblogic.management.mbeanservers.runtime";

  /**
   ** The Domain Runtime MBean Server.
   ** <p>
   ** Provides access to MBeans for domain-wide services such as application
   ** deployment, JMS servers, and JDBC data sources. It also is a single point
   ** for accessing the hierarchies of all runtime MBeans and all active
   ** configuration MBeans for all servers in the domain.
   ** <p>
   ** This MBean is available only on the Administration Server.
   */
  public static final String    DOMAIN_SERVICE         = "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean";

  /**
   ** Attribute of the <code>DomainRuntime</code> runtime service containing
   ** DomainRuntimeMBean for the current WebLogic Server domain.
   */
  public static final String    DOMAIN_RUNTIME         = "DomainRuntime";

  /**
   ** Attribute of the <code>DomainRuntime</code> runtime service containing the
   ** array of server runtime Lifecycle services all configured servers in the
   ** WebLogic Server domain.
   */
  public static final String    LIFECYCLE_RUNTIME      = "ServerLifeCycleRuntimes";

  /**
   ** Attribute of the <code>DomainRuntime</code> runtime service containing the
   ** array of all configured servers in the WebLogic Server domain.
   */
  public static final String    SERVER_RUNTIME         = "ServerRuntimes";

  /**
   ** Attribute of the <code>ServerRuntime</code> runtime service containing the
   ** array of application services deployed on a WebLogic Server.
   */
  public static final String    APPLICATION_RUNTIME    = "ApplicationRuntime";

  /**
   ** Attribute of the <code>ApplicationrRuntime</code> runtime service
   ** containing the array of component services provisioned by an application
   ** on a WebLogic Server.
   */
  public static final String    COMPONENT_RUNTIME      = "ComponentRuntimes";

  public static final String[]  MAP_ITEM_NAMES         = { "key", "value" };

  protected static final String STRING_CLASS           = String.class.getName();
  protected static final String BOOLEAN_CLASS          = Boolean.class.getName();
  protected static final String INTEGER_CLASS          = Integer.class.getName();
  protected static final String COMPOSITEDATA_CLASS    = CompositeData.class.getName();
  protected static final String COMPOSITEARRAY_CLASS   = CompositeData[].class.getName();

  protected static final String DOMAIN_RUNTIME_DOMAIN  = "com.bea";
  protected static final String DOMAIN_RUNTIME_NAME    = "DomainRuntimeService";
  protected static final String DOMAIN_RUNTIME_TYPE    = "weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean";

  protected static final String COMPONENT_TYPE_EAR     = "??????";
  protected static final String COMPONENT_TYPE_EJB     = "EJBComponentRuntime";
  protected static final String COMPONENT_TYPE_WEB     = "WebAppComponentRuntime";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  public static TabularType     typeStringMap           = null;
  public static CompositeType   typeStringMapEntry      = null;
  public static ArrayType       typeStringArray         = null;
  public static CompositeType   typeStringMapArrayEntry = null;
  public static TabularType     typeStringMapArray      = null;

  static ObjectName             domainRuntimeService;


  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      typeStringMapEntry = new CompositeType("StringMapEntry", "A Map entry whose value type is String", MAP_ITEM_NAMES, MAP_ITEM_NAMES, new OpenType[] { SimpleType.STRING, SimpleType.STRING });
    }
    catch (OpenDataException ignored) {
      // intentionally left blank
      ;
    }
    try {
      typeStringMap = new TabularType("Map", "A Map of String keys to String values", typeStringMapEntry, new String[] { MAP_ITEM_NAMES[0] });
    }
    catch (OpenDataException ignored) {
      // intentionally left blank
      ;
    }
    try {
      typeStringArray = new ArrayType(SimpleType.STRING, false);
    }
    catch (OpenDataException ignored) {
      // intentionally left blank
      ;
    }
    try {
      typeStringMapArrayEntry = new CompositeType("StringMapEntry", "A Map entry whose value type is String", MAP_ITEM_NAMES, MAP_ITEM_NAMES, new OpenType[] { SimpleType.STRING, typeStringArray });
    }
    catch (OpenDataException ignored) {
      // intentionally left blank
      ;
    }
    try {
      typeStringMapArray = new TabularType("Map", "A Map of String keys to String values", typeStringMapArrayEntry, new String[] { MAP_ITEM_NAMES[0] });
    }
    catch (OpenDataException ignored) {
      // intentionally left blank
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected MBeanServerConnection connection;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class JNDI
  // ~~~~~ ~~~~
  /**
   ** Declaration of naming conventions which take case sensitivity in account
   */
  public enum JNDI {
      Root           ("com.bea",           "oracle.j2ee.config")
    , Type           ("Type",              "type")
    , Name           ("Name",              "name")
    , Location       ("Location",          "Location")
    , Application    (APPLICATION_RUNTIME, "Application")
    , WebService     ("WseeV2Runtime",     "WebServicesConfig.WebServiceConfig")
    , WebServicePort ("Ports",             "WebServicePortConfigs")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** how a name is spelled by standard WebLogic MBean Servers universum */
    public final String WLS;

    /** how a name is spelled by Oracle Fusion Middleware extensions */
    public final String JRF;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>JNDI</code> naming convention dictionary.
     **
     ** @param  wls              how a name is spelled by standard WebLogic
     **                          MBean Servers universum.
     ** @param  jrf              how a name is spelled by Oracle Fusion
     **                          Middleware extensions.
     */
    JNDI(final String wls, final String jrf) {
      // initialize instance attributes
      this.WLS = wls;
      this.JRF = jrf;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ComponentType
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Declaration of naming conventions which translates types of deployed
   ** components
   */
  public enum ComponentType {
      EAR(COMPONENT_TYPE_EAR)
    , WEB(COMPONENT_TYPE_WEB)
    , EJB(COMPONENT_TYPE_EJB)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** how a component type is spelled by standard Weblogics MBean Servers
     ** universum
     */
    public final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ComponentType</code> naming convention dictionary.
     **
     ** @param  value            how a component type is spelled by standard
     **                          Weblogics MBean Serversuniversum.
     */
    ComponentType(final String value) {
      // initialize instance attributes
      this.value = value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Name
  // ~~~~~ ~~~~
  public static abstract class Name extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the action to apply  */
    ServiceOperation action = ServiceOperation.none;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Name</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Name() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Name</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public Name(final String name) {
      // ensure inheritance
      super();

      // initialize instance attributes
      name(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: action
    /**
     ** Called to inject the argument for parameter <code>action</code>.
     **
     ** @param  action           the action to apply in Oracle WebLogic Domain
     **                          MBean Server.
     */
    public final void action(final ServiceOperation action) {
      this.action = action;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Name</code> object that represents
     ** the same <code>name</code> and <code>action</code> as this instance.
     **
     ** @param other             the object to compare this <code>Name</code>
     **                          with.
     **
     ** @return                  <code>true</code> if the <code>Name</code>s
     **                          are equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Name))
        return false;

      final Name another = (Name)other;
      return another.name().equals(this.name()) && another.action == this.action;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AbstractInvocationHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  protected AbstractInvocationHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor mehods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainRuntimeService
  /**
   ** Returns the name of the domain runtime service to connect to.
   **
   ** @return                    the name of the domain runtime service to
   **                            connect to.
   **
   ** @throws ServiceException   the string constructed based on the implemented
   **                            properties does not have the right format.
   */
  public static final ObjectName domainRuntimeService()
    throws ServiceException {

    if (domainRuntimeService == null) {
      final Map<String, String> properties = new HashMap<>();
      properties.put(JNDI.Name.WLS, DOMAIN_RUNTIME_NAME);
      properties.put(JNDI.Type.WLS, DOMAIN_RUNTIME_TYPE);
      domainRuntimeService = createObjectName(JNDI.Root.WLS, properties, false);
    }

    return domainRuntimeService;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName
  /**
   ** Returns the JMX {@link ObjectName} to access the MBean.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            MBean.
   **
   ** @throws ServiceException   if the JMX {@link ObjectName} to build is
   **                            malformed.
   */
  protected abstract ObjectName objectName()
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
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

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Oracle
   **                            WebLogic Domain entity instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Oracle WebLogic Domain entity instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Oracle WebLogic Domain entity instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryDomainRuntimeService
  /**
   ** Returns the name of the Oracle WebLogic Server Domain Runtime Service.
   **
   ** @param  connection         an {@link MBeanServerConnection} used to
   **                            fetch the desired {@link ObjectName}.
   **
   ** @return                    an {@link ObjectName}s for the MBeans selected.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public static ObjectName queryDomainRuntimeService(final MBeanServerConnection connection)
    throws ServiceException {

    final ObjectName[] domain = queryNames(connection, domainRuntimeService());
    return domain[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryServers
  /**
   ** Returns the names of Server MBeans controlled by the MBean server.
   **
   ** @param  connection         an {@link MBeanServerConnection} used to
   **                            fetch the desired array of {@link ObjectName}s.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty empty is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public static ObjectName[] queryServers(final MBeanServerConnection connection)
    throws ServiceException {

    final Map<String, String> properties = new HashMap<>();
    properties.put(JNDI.Name.WLS, DOMAIN_RUNTIME_NAME);
    properties.put(JNDI.Type.WLS, DOMAIN_RUNTIME_TYPE);
    try {
      return (ObjectName[])connection.getAttribute(createObjectName(DOMAIN_RUNTIME_DOMAIN, properties, false), SERVER_RUNTIME);
    }
    catch (AttributeNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_ATTRIBUTENAME_NOTFOUND, e.getLocalizedMessage());
    }
    catch (InstanceNotFoundException e) {
      throw new ServiceException(ServiceError.CONTROL_BEANINSTANCE_NOTFOUND, e.getLocalizedMessage());
    }
    catch (MBeanException | ReflectionException | IOException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryNames
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  connection         an {@link MBeanServerConnection} used to
   **                            fetch the desired array of {@link ObjectName}s.
   ** @param  domain             a string of characters not including the
   **                            character colon <code>:</code>.
   **                            <br>
   **                            It is recommended that the domain should not
   **                            contain the string "//", which is reserved for
   **                            future use.
   **                            <br>
   **                            if the provided value is empty, it will be
   **                            replaced the pattern <code>*</code>.
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty empty is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public static ObjectName[] queryNames(final MBeanServerConnection connection, final String domain, final Map<String, String> properties)
    throws ServiceException {

    return queryNames(connection, createObjectName(domain, properties));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryNames
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  connection         an {@link MBeanServerConnection} used to
   **                            fetch the desired array of {@link ObjectName}s.
   ** @param  query              an {@link ObjectName} which can be used by
   **                            pattern  matching and/or a Query expression.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty empty is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public static ObjectName[] queryNames(final MBeanServerConnection connection, final ObjectName query)
    throws ServiceException {

    // prevent bogus state
    if (connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    try {
      return connection.queryNames(query, null).toArray(new ObjectName[0]);
    }
    catch (IOException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryBeans
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  connection         an {@link MBeanServerConnection} used to
   **                            fetch the desired array of {@link ObjectName}s.
   ** @param  domain             a string of characters not including the
   **                            character colon <code>:</code>.
   **                            <br>
   **                            It is recommended that the domain should not
   **                            contain the string "//", which is reserved for
   **                            future use.
   **                            <br>
   **                            if the provided value is empty, it will be
   **                            replaced the pattern <code>*</code>.
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   **
   ** @return                    an array containing the {@link ObjectInstance}s
   **                            for the MBeans selected. If no MBean satisfies
   **                            the query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public static ObjectInstance[] queryBeans(final MBeanServerConnection connection, final String domain, final Map<String, String> properties)
    throws ServiceException {

    return queryBeans(connection, createObjectName(domain, properties));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryBeans
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  connection         an {@link MBeanServerConnection} used to
   **                            fetch the desired array of {@link ObjectName}s.
   ** @param  query              the {@link ObjectName} pattern identifying the
   **                            MBeans to be retrieved. If <code>null</code> or
   **                            no domain and key properties are specified, all
   **                            the MBeans registered will be retrieved.
   **
   ** @return                    an array containing the {@link ObjectInstance}s
   **                            for the MBeans selected. If no MBean satisfies
   **                            the query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public static ObjectInstance[] queryBeans(final MBeanServerConnection connection, final ObjectName query)
    throws ServiceException {

    // prevent bogus state
    if (connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    try {
      return connection.queryMBeans(query, null).toArray(new ObjectInstance[0]);
    }
    catch (IOException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes an operation on an MBean.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
   ** @param  connection         an {@link MBeanServerConnection} used to
   **                            invoke the the operation .
   ** @param  instance           the object name of the MBean {@link ObjectName}
   **                            on which the method is to be invoked.
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public static Object invoke(final MBeanServerConnection connection, final ObjectName instance, final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    // prevent bogus state
    if (connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    Object result = null;
    try {
      result = connection.invoke(instance, operation, parameter, signature);
    }
    catch (InstanceNotFoundException e) {
      final String[] arguments = {operation, e.getLocalizedMessage()};
      throw new ServiceException(ServiceError.MANAGEDBEAN_INSTANCE_NOTFOUND, arguments);
    }
    catch (ReflectionException e) {
      final String[] arguments = {operation, e.getLocalizedMessage()};
      throw new ServiceException(ServiceError.MANAGEDBEAN_SIGNATURE_NOTFOUND, arguments);
    }
    catch (MBeanException e) {
      throw new ServiceException(ServiceMessage.OPERATION_INVOKE_ERROR, operation);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createObjectName
  /**
   ** Factory method to create an {@link ObjectName} which can be used by
   ** pattern matching on the created {@link ObjectName} and/or a Query
   ** expression.
   **
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   **
   ** @return                    an {@link ObjectName} which can be used by
   **                            pattern matching on the created
   **                            {@link ObjectName} and/or a Query expression.
   **
   ** @throws ServiceException   the string constructed based on the provided
   **                            <code>properties</code> does not have the right
   **                            format.
   */
  public static ObjectName createObjectName(final Map<String, String> properties)
    throws ServiceException {

    return createObjectName(null, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createObjectName
  /**
   ** Factory method to create an {@link ObjectName} which can be used by
   ** pattern matching on the created {@link ObjectName} and/or a Query
   ** expression.
   **
   ** @param  domain             a string of characters not including the
   **                            character colon <code>:</code>.
   **                            <br>
   **                            It is recommended that the domain should not
   **                            contain the string "//", which is reserved for
   **                            future use.
   **                            <br>
   **                            if the provided value is empty, it will be
   **                            replaced the pattern <code>*</code>.
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   **
   ** @return                    an {@link ObjectName} which can be used by
   **                            pattern matching on the created
   **                            {@link ObjectName} and/or a Query expression.
   **
   ** @throws ServiceException   the string constructed based on the provided
   **                            <code>properties</code> does not have the right
   **                            format.
   */
  public static ObjectName createObjectName(final String domain, final Map<String, String> properties)
    throws ServiceException {

    return createObjectName(domain, properties, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createObjectName
  /**
   ** Factory method to create an {@link ObjectName} which can be used by
   ** pattern matching on the created {@link ObjectName} and/or a Query
   ** expression.
   **
   ** @param  domain             a string of characters not including the
   **                            character colon <code>:</code>.
   **                            <br>
   **                            It is recommended that the domain should not
   **                            contain the string "//", which is reserved for
   **                            future use.
   **                            <br>
   **                            if the provided value is empty, it will be
   **                            replaced the pattern <code>*</code>.
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   ** @param query               <code>true</code> if the {@link ObjectName} to
   **                            create have to be a Query expression.
   **
   ** @return                    an {@link ObjectName} which can be used by
   **                            pattern matching on the created
   **                            {@link ObjectName} and/or a Query expression.
   **
   ** @throws ServiceException   the string constructed based on the provided
   **                            <code>properties</code> does not have the right
   **                            format.
   */
  public static ObjectName createObjectName(final String domain, final Map<String, String> properties, final boolean query)
    throws ServiceException {

    final StringBuilder buffer = new StringBuilder();
    if (!StringUtility.isEmpty(domain))
      buffer.append(domain).append(":");
    else
      buffer.append("*:");

    for (Map.Entry<String, String> cursor : properties.entrySet())
      buffer.append(cursor.getKey()).append("=").append(cursor.getValue()).append(",");

    if (!properties.isEmpty())
      buffer.deleteCharAt(buffer.length() - 1);

    return createObjectName(buffer.toString(), query);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createObjectName
  /**
   ** Factory method to create an {@link ObjectName} which can be used by
   ** pattern matching on the created {@link ObjectName} and/or a Query
   ** expression.
   **
   ** @param  objectName         a string repesentaion of the {@link ObjectName}
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   ** @param query               <code>true</code> if the {@link ObjectName} to
   **                            create have to be a Query expression.
   **
   ** @return                    an {@link ObjectName} which can be used by
   **                            pattern matching on the created
   **                            {@link ObjectName} and/or a Query expression.
   **
   ** @throws ServiceException   the string constructed based on the provided
   **                            <code>properties</code> does not have the right
   **                            format.
   */
  public static ObjectName createObjectName(final String objectName, final boolean query)
    throws ServiceException {

    final StringBuilder name = new StringBuilder(objectName);
    if (query)
      name.append(",*");
    try {
      return new ObjectName(name.toString());
    }
    catch (MalformedObjectNameException e) {
      throw new ServiceException(ServiceError.MANAGEDBEAN_OBJECTNAME_MALFORMED, objectName);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapToTabularData
  public static TabularData mapToTabularData(final Map<String, Object> map)
    throws ServiceException {

    final TabularData data = new TabularDataSupport(typeStringMap);
    try {
      for (Map.Entry<String, Object> cursor : map.entrySet())
        data.put(new CompositeDataSupport(typeStringMapEntry, MAP_ITEM_NAMES, new String[] { cursor.getKey(), cursor.getValue().toString() }));
    }
    catch (OpenDataException e) {
      throw new ServiceException(ServiceError.ABORT, e);
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapFromTabularData
  public static Map<String, String> mapFromTabularData(final TabularData data)
    throws ServiceException {

    final Map<String, String> result = new HashMap<>();
    if (data != null) {
      final Iterator cursor = data.values().iterator();
      while (cursor.hasNext()) {
        final CompositeData cd = (CompositeData)cursor.next();
        result.put((String)cd.get(MAP_ITEM_NAMES[0]), (String)cd.get(MAP_ITEM_NAMES[1]));
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapListToTabularData
  public static TabularData mapListToTabularData(final Map<String, List<String>> map)
    throws ServiceException {

    final TabularData data = new TabularDataSupport(typeStringMapArray);
    try {
      for (Map.Entry<String, List<String>> cursor : map.entrySet()) {
        data.put(new CompositeDataSupport(typeStringMapEntry, MAP_ITEM_NAMES, new Object[] { cursor.getKey(), cursor.getValue().toArray(new String[0]) }));
      }
    }
    catch (OpenDataException e) {
      throw new ServiceException(ServiceError.ABORT, e);
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapListFromTabularData
  public static Map<String, List<String>> mapListFromTabularData(final TabularData data)
    throws ServiceException {

    final Map<String, List<String>> result = new HashMap<>();
    if (data != null) {
      final Iterator cursor = data.values().iterator();
      while (cursor.hasNext()) {
        final CompositeData cd = (CompositeData)cursor.next();
        result.put((String)cd.get(MAP_ITEM_NAMES[0]), CollectionUtility.list((String[])cd.get(MAP_ITEM_NAMES[1])));
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleThrowable
  /**
   ** Creates and throws a {@link ServiceException} about an unhandled
   ** exception.
   **
   ** @param  throwable          the {@link Throwable} as the reason to notify.
   **
   ** @throws ServiceException   always.
   */
  protected void handleThrowable(final Throwable throwable)
    throws ServiceException {

    error(throwable.getLocalizedMessage());
    if (failonerror()) {
      if (throwable instanceof ServiceException) {
        throw (ServiceException)throwable;
      }
      throw new ServiceException(ServiceError.UNHANDLED, throwable);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describe
  /**
   ** Returns all attribute name <code>JMX ObjectName</code> of an MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean
   **                            the attribute metadata are requested for.
   **
   ** @return                    the <code>JMX ObjectName</code> name this task
   **                            will execute.
   */
  protected final Set<String> describe(final ObjectName objectName) {
    final Set<String> descriptor = new HashSet<String>();
    try {
      final MBeanInfo metadata = this.connection.getMBeanInfo(objectName);
      if (metadata != null) {
        final MBeanAttributeInfo[] attribute = metadata.getAttributes();
        for (MBeanAttributeInfo i : attribute)
          descriptor.add(i.getName());
      }
    }
    catch (Exception e) {
      throw new AssertionError(e.getMessage());
    }
    return descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets an attribute on an MBean.
   **
   ** @param  name               the name of the attribute to set.
   ** @param  value              the value to set for name.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected void attribute(final String name, final Object value)
    throws ServiceException {

    attribute(objectName(), new Attribute(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets an attribute on an MBean.
   **
   ** @param  attribute          the {@link Attribute} to set.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected void attribute(final Attribute attribute)
    throws ServiceException {

    attribute(objectName(), attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets an attribute on an MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean
   **                            the attribute has to set on.
   ** @param  name               the name of the attribute to set.
   ** @param  value              the value to set for name.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected void attribute(final ObjectName objectName, final String name, final Object value)
    throws ServiceException {

    attribute(objectName, new Attribute(name, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets an attribute on an MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean
   **                            the attribute has to set on.
   ** @param  attribute          the {@link Attribute} to set.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected void attribute(final ObjectName objectName, final Attribute attribute)
    throws ServiceException {

    try {
      this.connection.setAttribute(objectName, attribute);
    }
    catch (Exception e) {
      fatal(e);
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringAttribute
  /**
   ** Returns an attribute value of a MBean.
   **
   ** @param  name               the name of the attribute to return.
   **
   ** @return                    the value of the retrieved attribute.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected String stringAttribute(final String name)
    throws ServiceException {

    return (String)attribute(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns an attribute value of a MBean.
   **
   ** @param  name               the name of the attribute to return.
   **
   ** @return                    the value of the retrieved attribute.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object attribute(final String name)
    throws ServiceException {

    return attribute(objectName(), name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringAttribute
  /**
   ** Returns an attribute value of a MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean of
   **                            the attribute has to be returned from.
   ** @param  name               the name of the attribute to return.
   **
   ** @return                    the value of the retrieved attribute.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected String stringAttribute(final ObjectName objectName, final String name)
    throws ServiceException {

    return (String)attribute(objectName, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns an attribute value of a MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean of
   **                            the attribute has to be returned from.
   ** @param  name               the name of the attribute to return.
   **
   ** @return                    the value of the retrieved attribute.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object attribute(final ObjectName objectName, final String name)
    throws ServiceException {

    Object value = null;
    try {
      value = this.connection.getAttribute(objectName, name);
    }
    catch (Exception e) {
      fatal(e);
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Sets a property on an MBean.
   **
   ** @param  name               the name of the property to set.
   ** @param  value              the value to set for name.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected void property(final String name, final Object value)
    throws ServiceException {

    property(objectName(), name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Sets a property on an MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean
   **                            the property has to set on.
   ** @param  name               the name of the property to set.
   ** @param  value              the value to set for name.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected void property(final ObjectName objectName, final String name, final Object value)
    throws ServiceException {

    final String   operation = "setProperty";
    final Object[] parameter = { name,         value };
    final String[] signature = { STRING_CLASS, value.getClass().getName() };
    invoke(objectName, operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Sets a property on an MBean.
   **
   ** @param  name               the name of the property to return.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object property(final String name)
    throws ServiceException {

    return property(objectName(), name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns a property value of a MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean of
   **                            the property has to be returned from.
   ** @param  name               the name of the property to return.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object property(final ObjectName objectName, final String name)
    throws ServiceException {

    Object value = null;
    try {
      final String   operation = "setProperty";
      final Object[] parameter = { name };
      final String[] signature = { STRING_CLASS };
      value = invoke(objectName, operation, parameter, signature);
    }
    catch (Exception e) {
      fatal(e);
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryDomainRuntimeService
  /**
   ** Returns the name of the Oracle WebLogic Server Domain Runtime Service.
   **
   ** @return                    an {@link ObjectName}s for the MBeans selected.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  public final ObjectName queryDomainRuntimeService()
    throws ServiceException {

    return queryDomainRuntimeService(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryServers
  /**
   ** Returns the names of Server MBeans controlled by the MBean server.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectName[] queryServers()
    throws ServiceException {

    return queryServers(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryNames
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  domain             a string of characters not including the
   **                            character colon <code>:</code>.
   **                            <br>
   **                            It is recommended that the domain should not
   **                            contain the string "//", which is reserved for
   **                            future use.
   **                            <br>
   **                            if the provided value is empty, it will be
   **                            replaced the pattern <code>*</code>.
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectName[] queryNames(final String domain, final Map<String, String> properties)
    throws ServiceException {

    return queryNames(this.connection, domain, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryNames
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  query              an {@link ObjectName} which can be used by
   **                            pattern  matching and/or a Query expression.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectName[] queryNames(final ObjectName query)
    throws ServiceException {

    return queryNames(this.connection, query);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryBeans
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  domain             a string of characters not including the
   **                            character colon <code>:</code>.
   **                            <br>
   **                            It is recommended that the domain should not
   **                            contain the string "//", which is reserved for
   **                            future use.
   **                            <br>
   **                            if the provided value is empty, it will be
   **                            replaced the pattern <code>*</code>.
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   **
   ** @return                    an array containing the {@link ObjectInstance}s
   **                            for the MBeans selected. If no MBean satisfies
   **                            the query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectInstance[] queryBeans(final String domain, final Map<String, String> properties)
    throws ServiceException {

    return queryBeans(this.connection, domain, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryBeans
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the {@link Set} of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  query              the {@link ObjectName} pattern identifying the
   **                            MBeans to be retrieved. If <code>null</code> or
   **                            no domain and key properties are specified, all
   **                            the MBeans registered will be retrieved.
   **
   ** @return                    an array containing the {@link ObjectInstance}s
   **                            for the MBeans selected. If no MBean satisfies
   **                            the query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectInstance[] queryBeans(final ObjectName query)
    throws ServiceException {

    return queryBeans(this.connection, query);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes an operation on the MBean returned by (@link #objectName()}.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object invoke(final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    return invoke(objectName(), operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes an operation on an MBean.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean of
   **                            the operation to invoke.
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object invoke(final ObjectName objectName, final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE, operation));
    Object result = null;
    try {
      result = invoke(this.connection, objectName, operation, parameter, signature);
      debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_SUCCESS, operation));
    }
    catch (ServiceException e) {
      error(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, operation));
      if (failonerror())
        throw e;
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   silent (overridden)
  /**
   ** Invokes an operation on an MBean by supressing any message.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object silent(final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE, operation));
    Object result = null;
    try {
      result = invoke(objectName(), operation, parameter, signature);
      debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_SUCCESS, operation));
    }
    catch (Exception e) {
      debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, operation));
    }
    return result;
  }
}