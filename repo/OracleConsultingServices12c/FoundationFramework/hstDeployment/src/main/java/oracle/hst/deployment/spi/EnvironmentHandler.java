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

    File        :   EnvironmentHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EnvironmentHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Formatter;
import java.util.LinkedHashMap;

import javax.management.ObjectName;
import javax.management.MBeanServerConnection;
import javax.management.InstanceNotFoundException;
import javax.management.AttributeNotFoundException;
import javax.management.MalformedObjectNameException;

import javax.management.openmbean.CompositeData;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class EnvironmentHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>EnvironmentHandler</code> configures Oracle WebLogic Domain.
 ** <p>
 ** All WebLogic Server MBeans have a name, a type and a domain. These
 ** attributes are reflected in the MBean's JMX Object Name. The Object Name is
 ** the unique identifier for a given MBean across all domains, and has the
 ** following structure:
 ** <pre>
 **   domain name:Name=name,Type=type[,attr=value]...
 ** </pre>
 ** Name is a unique identifier for a given domain and type of MBean.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EnvironmentHandler extends AbstractServerHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Locale  LOCALE = Locale.getDefault();
  private static final String  FORMAT = ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_FORMAT);
  private static final String  ENTITY = ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_ENTITY);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the single <code>Bean</code> to configure or to investigate
   */
  protected Domain             single;

  /**
   ** the multiple <code>Bean</code>s to configure or to create
   */
  protected final List<Domain> multiple = new ArrayList<Domain>();

  private String               location;

  /**  the name of the application. e.g.: /domain/application */
  private String               application;

  /**  the type of a driver. e.g.: email */
  private String               driverType;

  /**  the value of the version. e.g.: /domain/application#version */
  private String               version;

  /**  the name of the server. e.g.: soa */
  private String               server;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Value
  // ~~~~~ ~~~~~
  public static class Value {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the actual value existing in the source system */
    boolean change;

    /** the actual value existing in the source system */
    Object actual;

    /** the target value existing in the source system */
    final Object target;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Value</code> with the specified target value.
     **
     ** @param  target           the target value of this <code>Value</code>.
     */
    public Value(final Object target) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.target = target;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parameter
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Parameter</code> wrappes the specification of ...
   */
  public static class Parameter {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the class name of the value object */
    Class clazz;

    /** the target value to pass */
    Object value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Parameter</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Parameter() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Parameter</code> with the specified target value.
     **
     ** @param  clazz            the class of the value object.
     ** @param  value            the target value to pass.
     */
    public Parameter(final Class clazz, final Object value) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.clazz = clazz;
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: clazz
    /**
     ** Called to inject the argument for parameter <code>clazz</code>.
     **
     ** @param  clazz            the class name of the value object.
     */
    public final void clazz(final Class clazz) {
      this.clazz = clazz;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Called to inject the argument for parameter <code>value</code>.
     **
     ** @param  value            the target value to pass.
     */
    public final void value(final Object value) {
      this.value = value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Operation
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Operation</code> wrappes the specification of ...
   */
  public static class Operation {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the name og the operation to invoke on a MBean */
    String                method    = null;

    /** the parameters to be passed to a MBean invoke */
    final List<Parameter> parameter = new ArrayList<Parameter>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Operation</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Operation() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Operation</code> with the specified method name and
     ** parameter.
     **
     ** @param  method           the name of the method to invoke at the MBean.
     ** @param  parameter        the {@link List} of {@link Parameter}s to be
     **                          passed to the invoked method.
     */
    public Operation(final String method, final List<Parameter> parameter) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.method = method;
      this.parameter.addAll(parameter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: method
    /**
     ** Called to inject the argument for parameter <code>method</code>.
     **
     ** @param  method           the name of the method this operation on an
     **                          MBean invokes.
     */
    public final void method(final String method) {
      this.method = method;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  object           the {@link Parameter} to add.
     */
    public void add(final Parameter object) {
      this.parameter.add(object);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class LinkedName
  // ~~~~~ ~~~~~~~~~~
  public static abstract class LinkedName extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    LinkedName root = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>LinkedName</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public LinkedName() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>LinkedName</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public LinkedName(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the type of the bean in Oracle WebLogic Domain to handle.
     **
     ** @return                  the type context to handle in Oracle WebLogic
     **                          Domain.
     */
    public final String type() {
      if (this.root == null)
        return null;

      StringBuilder builder = new StringBuilder(this.root.name());
      LinkedName    cursor  = this.root.root;
      while (cursor != null) {
        if (cursor.root != null) {
          builder.append(',');
          builder.append(cursor.root.name());
          builder.append("=");
          builder.append(cursor.name());
          cursor = cursor.root.root;
        }
      }
      return builder.toString();
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
     ** <code>null</code> and is a <code>OrganizationInstance</code> object that
     ** represents the same <code>name</code> as this instance.
     **
     ** @param other             the object to compare this <code>Domain</code>
     **                          with.
     **
     ** @return                  <code>true</code> if the <code>Domain</code>s
     **                          are equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Name))
        return false;

      final Name another = (Name)other;
      return another.name().equals(this.name());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: validate
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException     in case the instance does not meet the
     **                            requirements.
     */
    public void validate()
      throws BuildException {

      if (StringUtility.isEmpty(this.name()))
        handleAttributeMissing("name");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Domain
  // ~~~~~ ~~~~~~
  /**
   ** All WebLogic Server MBeans have a name, a type and a domain. These
   ** attributes are reflected in the MBean's JMX Object Name. The Object Name is
   ** the unique identifier for a given MBean across all domains, and has the
   ** following structure:
   ** <pre>
   **   domain name:Name=name,Type=type[,attr=value]...
   ** </pre>
   ** Name is a unique identifier for a given domain and type of MBean.
   ** <p>
   ** This class implements the domain part of the unique identifier of a MBean.
   */
  public static final class Domain extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final List<Bean> bean = new ArrayList<Bean>();
    final List<Type> type = new ArrayList<Type>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Domain</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Domain() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Domain</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public Domain(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  object           the {@link Bean} to add.
     **
     ** @throws BuildException   if the specified {@link Bean} is already
     **                          assigned to this instance.
     */
    public void add(final Bean object)
      throws BuildException {

      this.bean.add(object);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  object           the {@link Type} to add.
     **
     ** @throws BuildException   if the specified {@link Type} is already
     **                          assigned to this instance.
     */
    public void add(final Type object)
      throws BuildException {

      this.type.add(object);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: validate
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException     in case the instance does not meet the
     **                            requirements.
     */
    public void validate()
      throws BuildException {

      // ensure inheritance
      super.validate();

      // validate all associated types
      if (!this.type.isEmpty())
        for (Type type : this.type)
          type.validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  // ~~~~~ ~~~~
  /**
   ** All WebLogic Server MBeans have a name, a type and a domain. These
   ** attributes are reflected in the MBean's JMX Object Name. The Object Name is
   ** the unique identifier for a given MBean across all domains, and has the
   ** following structure:
   ** <pre>
   **   domain name:Name=name,Type=type[,attr=value]...
   ** </pre>
   ** Name is a unique identifier for a given domain and type of MBean.
   ** <p>
   ** This class implements the type part of the unique identifier of a MBean.
   ** <br>
   ** Examples of resource types include Server, WebComponent or
   ** JDBCConnectionPoolRuntime. Type is also used to distinguish between
   ** administration, configuration, and runtime MBeans.
   */
  public static final class Type extends LinkedName {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final List<Bean> path = new ArrayList<Bean>();
    final List<Type> type = new ArrayList<Type>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Type() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public Type(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified type instance to the path of types.
     **
     ** @param  object           the {@link Type} to add.
     **
     ** @throws BuildException   if the specified {@link Type} is already
     **                          assigned to this instance.
     */
    public void add(final Type object)
      throws BuildException {

      object.root = this;
      this.type.add(object);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified type instance to the path of types.
     **
     ** @param  object           the {@link Bean} to add.
     **
     ** @throws BuildException   if the specified {@link Bean} is already
     **                          assigned to this instance.
     */
    public void add(final Bean object)
      throws BuildException {

      object.root = this;
      this.path.add(object);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: validate
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException     in case the instance does not meet the
     **                            requirements.
     */
    public void validate()
      throws BuildException {

      // ensure inheritance
      super.validate();

      // validate all associated beans
      if (!this.path.isEmpty())
        for (Bean bean : this.path)
          bean.validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Bean
  // ~~~~~ ~~~~
  /**
   ** All WebLogic Server MBeans have a name, a type and a domain. These
   ** attributes are reflected in the MBean's JMX Object Name. The Object Name
   ** is the unique identifier for a given MBean across all domains, and has the
   ** following structure:
   ** <pre>
   **   domain name:Name=name,Type=type[,attr=value]...
   ** </pre>
   ** Name is a unique identifier for a given domain and type of MBean.
   ** <p>
   ** This class implements the name part of the unique identifier of a MBean.
   */
  public static final class Bean extends LinkedName {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** <code>true</code> if the name building has to care about how the
     ** Name and Type of the object name to build for this bean has to be
     ** spelled.
     ** <ol>
     **   <li><code>false</code> (the default) advice that the name and type
     **       prefix has to be spelled in lower case letters.
     **   <li><code>true</code> advice that the name and type prefix has to be
     **       spelled in camel case letters.
     ** </ol>
     */
    boolean                  camelCase = false;

    // the full qualified object name evaluated by the value fetch of this
    // MBean instance
    ObjectName               fqon      = null;

    final List<Type>         path      = new ArrayList<Type>();
    final Map<String, Value> attribute = new LinkedHashMap<String, Value>();
    final List<Operation>    operation = new ArrayList<Operation>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Bean</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Bean() {
      // ensure inheritance
      super();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: camelCase
    /**
     ** Set to <code>true</code> if the name building has to care about how the
     ** Name and Type of the object name to build for this bean has to be
     ** spelled.
     ** <ol>
     **   <li><code>false</code> (the default) advice that the name and type
     **       prefix has to be spelled in lower case letters.
     **   <li><code>true</code> advice that the name and type prefix has to be
     **       spelled in camel case letters.
     ** </ol>
     **
     ** @param  camelCase        <code>true</code> advice that the name and
     **                          type prefix has to be spelled in camel case
     **                          letters; otherwise <code>false</code>.
     */
    public final void camelCase(final boolean camelCase) {
      this.camelCase = camelCase;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add the specified value pairs to the parameters that has to be applied.
     **
     ** @param  object           the {@link Type} to add.
     **
     ** @throws BuildException   if the specified {@link Type} is already
     **                          assigned to this instance.
     */
    public void add(final Type object)
      throws BuildException {

      object.root = this;
      this.path.add(object);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addAttribute
    /**
     ** Add the specified value pair to the attributes that has to be applied
     ** after an import operation.
     **
     ** @param  name             the name of the parameter of the Oracle
     **                          WebLogic Domain entity instance.
     ** @param  value            the value for <code>name</code> to set on the
     **                          Oracle WebLogic Domain entity instance.
     ** @param  clazz            the type of the value for <code>name</code>
     **                          to set on the Oracle WebLogic Domain entity
     **                          instance.
     **
     ** @throws BuildException   if the specified name is already part of the
     **                          parameter mapping.
     */
    public void addAttribute(final String name, final String value, final Class<?> clazz)
      throws BuildException {

      if (this.attribute.containsKey(name))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_PARAMETER_ONLYONCE, name));

      // add the value pair to the attributes with the proper type
      if (Boolean.class.equals(clazz))
        this.attribute.put(name, new Value(Boolean.valueOf(value)));
      else if (Integer.class.equals(clazz))
        this.attribute.put(name, new Value(Integer.valueOf(value)));
      else if (Long.class.equals(clazz))
        this.attribute.put(name, new Value(Long.valueOf(value)));
      else if (Float.class.equals(clazz))
        this.attribute.put(name, new Value(Float.valueOf(value)));
      else if (Double.class.equals(clazz))
        this.attribute.put(name, new Value(Double.valueOf(value)));
      else
        // fall back to string if the type isn't mapped
        this.attribute.put(name, new Value(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   addProperty
    /**
     ** Add the specified value pairs to the properties akya parameters that has
     ** to be applied.
     **
     ** @param  name             the name of the parameter to create a mapping
     **                          for on this instance.
     ** @param  value            the value for <code>name</code> to set on this
     **                          instance.
     ** @param  clazz            the type of the value for <code>name</code>
     **                          to set on the Oracle WebLogic Domain entity
     **                          instance.
     **
     ** @throws BuildException   if the specified name is already part of the
     **                          parameter mapping.
     */
    public void addProperty(final String name, final String value, final Class<?> clazz)
      throws BuildException {

      // add the value pair to the parameters with the proper type
      if (Boolean.class.equals(clazz))
        super.addParameter(name, Boolean.valueOf(value));
      else if (Integer.class.equals(clazz))
        super.addParameter(name, Integer.valueOf(value));
      else if (Long.class.equals(clazz))
        super.addParameter(name, Long.valueOf(value));
      else if (Float.class.equals(clazz))
        super.addParameter(name, Float.valueOf(value));
      else if (Double.class.equals(clazz))
        super.addParameter(name, Double.valueOf(value));
      else
        super.addParameter(name, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addOperation
    /**
     ** Add the specified operation to the operations that has to be invoked
     ** after an import operation.
     **
     ** @param  operation        the operation mapping for <code>method</code>
     **                          to be passed to the Oracle WebLogic Domain
     **                          entity instance.
     */
    public void addOperation(final Operation operation) {

      // add the value pair to the parameters
      this.operation.add(operation);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EnvironmentHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public EnvironmentHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Sets the name of the location the runtime server has to connect to.
   **
   ** @param  location           the name of the location the runtime server
   **                            has to connect to.
   */
  public final void location(final String location) {
    this.location = location;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns the name of the location the runtime server has to connect to.
   **
   ** @return                    the name of the location the runtime server has
   **                            to connect to.
   */
  protected final String location() {
    return this.location;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   driverType
  /**
   ** Sets the name of the driverType the runtime server has to connect to.
   **
   ** @param  driverType         the name of the driverType the runtime server
   **                            has to connect to.
   */
  public final void driverType(final String driverType) {
    this.driverType = driverType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   driverType
  /**
   ** Returns the name of the driverType the runtime server has to connect to.
   **
   ** @return                    the name of the driverType the runtime server
   **                            has to connect to.
   */
  protected final String driverType() {
    return this.driverType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Sets the name of the application the runtime server has to connect to.
   **
   ** @param  application        the name of the application the runtime server
   **                            has to connect to.
   */
  public final void application(final String application) {
    this.application = application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the name of the application the runtime server has to connect to.
   **
   ** @return                    the name of the application the runtime server
   **                            has to connect to.
   */
  protected final String application() {
    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Sets the version of the application the runtime server has to connect to.
   **
   ** @param  version            the version of the application the runtime
   **                            server has to connect to.
   */
  public final void version(final String version) {
    this.version = version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the version of the application the runtime server has to connect
   ** to.
   **
   ** @return                    the version of the application the runtime
   **                            server has to connect to.
   */
  protected final String version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Sets the name of the server the runtime server has to connect to.
   **
   ** @param  server             the name of the server the runtime server
   **                            has to connect to.
   */
  public final void server(final String server) {
    this.server = server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Returns the name of the server the runtime server has to connect to.
   **
   ** @return                    the name of the server the runtime server
   **                            has to connect to.
   */
  protected final String server() {
    return this.server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domain
  /**
   ** Sets the domain of the application the runtime server has to connect to.
   **
   ** @param  name               the name of the domain the runtime server has
   **                            to connect to.
   */
  public final void domain(final String name) {
    if (this.single == null)
      this.single = new Domain();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type of the application the runtime server has to connect to.
   **
   ** @param  name               the name of the type the runtime server has to
   **                            connect to.
   */
  public final void type(final String name) {
    if (this.single == null)
      this.single = new Domain();

    if (this.single.type.isEmpty())
      this.single.type.add(new Type());

    this.single.type.get(0).name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bean
  /**
   ** Sets the bean of the application the runtime server has to connect to.
   **
   ** @param  name               the name of the bean the runtime server has to
   **                            connect to.
   */
  public final void bean(final String name) {
    if (this.single == null)
      this.single = new Domain();

    if (this.single.type.isEmpty())
      this.single.type.add(new Type());

    final Type type = this.single.type.get(0);
    if (type.path.isEmpty())
      type.path.add(new Bean());

    final Bean object = type.path.get(0);
    object.root = type;
    type.path.get(0).name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   camelCase
  /**
   ** Sets the bean of the application the runtime server has to connect to.
   **
   ** @param  onoff              the name of the bean the runtime server has to
   **                            connect to.
   */
  public final void camelCase(final boolean onoff) {
    if (this.single == null)
      this.single = new Domain();

    if (this.single.type.isEmpty())
      this.single.type.add(new Type());

    final Type type = this.single.type.get(0);
    if (type.path.isEmpty())
      type.path.add(new Bean());

    final Bean object = type.path.get(0);
    object.root = type;
    type.path.get(0).camelCase(onoff);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (ServiceProvider)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Oracle
   **                            WebLogic Domain entity instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Oracle WebLogic Domain entity instance.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      handleAttributeMissing("domain");

    if (this.single.type.isEmpty())
      handleAttributeMissing("type");

    final Type type = this.single.type.get(0);
    if (type.path.isEmpty())
      handleAttributeMissing("bean");

    type.path.get(0).addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (ServiceProvider)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Oracle WebLogic Domain entity instance.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    if (this.single == null)
      handleAttributeMissing("domain");

    if (this.single.type.isEmpty())
      handleAttributeMissing("type");

    final Type type = this.single.type.get(0);
    if (type.path.isEmpty())
      handleAttributeMissing("bean");

    type.path.get(0).addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the Entitlement in Oracle Identity
   **                            Manager to handle.
   */
  @Override
  public void name(final String name) {
    domain(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProperty
  /**
   ** Add the specified value pairs to the properties akya parameters that has
   ** to be applied.
   **
   ** @param  name               the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>name</code> to set on this
   **                            instance.
   ** @param  clazz              the type of the value for <code>name</code>
   **                            to set on the Oracle WebLogic Domain entity
   **                            instance.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  public void addProperty(final String name, final String value, final Class<?> clazz)
    throws BuildException {

    if (this.single == null)
      handleAttributeMissing("domain");

    if (this.single.type.isEmpty())
      handleAttributeMissing("type");

    final Type type = this.single.type.get(0);
    if (type.path.isEmpty())
      handleAttributeMissing("bean");

    type.path.get(0).addProperty(name, value, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Add the specified value pair to the attributes that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the attribute of the Oracle
   **                            WebLogic Domain entity instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Oracle WebLogic Domain entity instance.
   ** @param  clazz              the type of the value for <code>name</code>
   **                            to set on the Oracle WebLogic Domain entity
   **                            instance.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            attribute mapping.
   */
  public void addAttribute(final String name, final String value, final Class<?> clazz)
    throws BuildException {

    if (this.single == null)
      handleAttributeMissing("domain");

    if (this.single.type.isEmpty())
      handleAttributeMissing("type");

    final Type type = this.single.type.get(0);
    if (type.path.isEmpty())
      handleAttributeMissing("bean");

    type.path.get(0).addAttribute(name, value, clazz);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   addOperation
  /**
   ** Add the specified operation to the operations that has to be invoked
   ** after an import operation.
   **
   ** @param  operation            the operation mapping for <code>method</code>
   **                              to be passed to the Oracle WebLogic Domain
   **                              entity instance.
   */
  public void addOperation(final Operation operation)
    throws BuildException {

    if (this.single == null)
      handleAttributeMissing("domain");

    if (this.single.type.isEmpty())
      handleAttributeMissing("type");

    final Type type = this.single.type.get(0);
    if (type.path.isEmpty())
      handleAttributeMissing("bean");

    type.path.get(0).addOperation(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_MISSING, "domain"));

    if (this.single != null)
      this.single.validate();

    for (Domain i : this.multiple)
      i.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDomain
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link Domain} to add.
   **
   ** @throws BuildException     if the specified {@link Domain} is already
   **                            assigned to this task.
   */
  public void addDomain(final Domain object)
    throws BuildException {

    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   actual
  /**
   ** Writes the <code>JMX ObjectName</code> report containing the actual values
   ** for all beans fetched through the given {@link MBeanServerConnection} to
   ** the associated log.
   **
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void actual(final MBeanServerConnection connection)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.connection = connection;
    if (this.single != null)
      actual(this.single);

    for (Domain i : this.multiple)
      actual(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Writes the <code>JMX ObjectName</code> report containing the actual and
   ** target values for all beans fetched through the given
   ** {@link MBeanServerConnection} to the associated log.
   **
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void compare(final MBeanServerConnection connection)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.connection = connection;
    if (this.single != null)
      compare(this.single);

    for (Domain i : this.multiple)
      compare(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes the <code>JMX ObjectName</code> operations for all beans
   ** through the given {@link MBeanServerConnection}.
   **
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void invoke(final MBeanServerConnection connection)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.connection = connection;
    if (this.single != null)
      invoke(this.single);

    for (Domain i : this.multiple)
      invoke(i);
    
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Stores the <code>JMX ObjectName</code> for all beans through the given
   ** {@link MBeanServerConnection}.
   **
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final MBeanServerConnection connection)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.connection = connection;
    if (this.single != null) {
      modify(this.single);
    }

    for (Domain i : this.multiple)
      modify(i);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   actual
  /**
   ** Writes the <code>JMX ObjectName</code> report containing the actual values
   ** for all beans in the specifed {@link Domain} to the associated log.
   **
   ** @paramm  domain            the wrapper to report the MBeans belonging to
   **                            the domain.
   */
  private void actual(final Domain instance)
    throws ServiceException {

    for (Type type : instance.type)
      actual(instance, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   actual
  /**
   ** Writes the <code>JMX ObjectName</code> report containing the actual values
   ** for all beans which matches the specified {@link Type} in the specifed
   ** {@link Domain} to the associated log.
   **
   ** @paramm  domain            the wrapper to report the MBeans belonging to
   **                            the domain.
   ** @paramm  type              the wrapper to report the MBeans belonging to
   **                            the type in the specific domain.
   */
  private void actual(final Domain domain, final Type instance)
    throws ServiceException {

    final StringBuilder builder   = new StringBuilder();
    final Formatter     formatter = new Formatter(builder);
    for (Bean bean : instance.path) {
      objectName(domain, bean);

      if (bean.attribute.keySet().contains("*")) {
        bean.attribute.clear();
        final Set<String> attributeSet = describe(bean.fqon);
        for (String name : attributeSet)
          bean.attribute.put(name, new Value(null));
      }

      warning(ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_SEPARATOR));
      warning(bean.fqon.toString());
      warning(ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_SEPARATOR));
      final Set<Map.Entry<String, Value>> attribute = bean.attribute.entrySet();
      if (CollectionUtility.empty(attribute))
        warning(ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_EMPTY));
      else {
        fetch(bean);
        for (Map.Entry<String, Value> entry : attribute) {
          builder.delete(0, builder.length());
          formatter.format(LOCALE, FORMAT, entry.getKey(), entry.getValue().actual);
          warning(builder.toString());
        }
      }
      for (Type type : bean.path)
        actual(domain, type);
    }
    for (Type type : instance.type)
      actual(domain, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Writes the <code>JMX ObjectName</code> report containing the actual and
   ** target values in the specifed {@link Domain} to the associated log.
   **
   ** @paramm  domain            the wrapper to compare the MBeans belonging to
   **                            the domain.
   */
  private void compare(final Domain instance)
    throws ServiceException {

    for (Type type : instance.type)
      compare(instance, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Reports actual and target values for all beans in the specifed
   ** {@link Domain} and matchs the specified {@link Type}.
   ** Writes the <code>JMX ObjectName</code> report containing the actual and
   ** target values in the specifed {@link Domain} to the associated log.
   **
   ** @paramm  domain            the wrapper to compare the MBeans belonging to
   **                            the domain.
   ** @paramm  type              the wrapper to compare the MBeans belonging to
   **                            the type in the specific domain.
   */
  private void compare(final Domain domain, final Type instance)
    throws ServiceException {

    final StringBuilder builder   = new StringBuilder();
    final Formatter     formatter = new Formatter(builder);
    for (Bean bean : instance.path) {
      objectName(domain, bean);
      warning(ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_SEPARATOR));
      warning(bean.fqon.toString());
      final Set<Map.Entry<String, Value>> parameter = bean.attribute.entrySet();
      if (CollectionUtility.empty(parameter))
        warning(ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_EMPTY));
      else {
        fetch(bean);
        for (Map.Entry<String, Value> entry : parameter) {
          warning(ServiceResourceBundle.string(ServiceMessage.FUSION_INVENTORY_SEPARATOR));
          builder.delete(0, builder.length());
          formatter.format(LOCALE, FORMAT, entry.getKey(), entry.getValue().actual);
          warning(builder.toString());
          builder.delete(0, builder.length());
          formatter.format(LOCALE, FORMAT, ""            , entry.getValue().target);
          warning(builder.toString());
        }
      }
      for (Type type : bean.path)
        compare(domain, type);
    }
    for (Type type : instance.type)
      compare(domain, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes the <code>JMX ObjectName</code> operations for all beans in the
   ** specifed {@link Domain}.
   **
   ** @paramm  domain            the wrapper to invoking the MBean operations
   **                            belonging to the domain.
   */
  private void invoke(final Domain instance)
    throws ServiceException {

    for (Type type : instance.type)
      invoke(instance, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes the <code>JMX ObjectName</code> operations for all beans in the
   ** specifed {@link Domain} and matchs the specified {@link Type}.
   **
   ** @paramm  domain            the wrapper to invoke the MBeans operations
   **                            belonging to the domain.
   ** @paramm  type              the wrapper to configure the MBeans operations
   **                            belonging to the type in the specific domain.
   */
  private void invoke(final Domain domain, final Type instance)
    throws ServiceException {

    for (Bean bean : instance.path) {
      if (bean.operation.size() > 0) {
        objectName(domain, bean);
        for (Operation operation : bean.operation) {
          Object[] parameter = null;
          String[] signature = null;
          int      i         = operation.parameter.size();
          if (i > 0) {
            parameter = new Object[i];
            signature = new String[i];
            i         = 0;
            for (Parameter object : operation.parameter) {
              parameter[i] = object.value;
              signature[i] = object.clazz.getName();
              i++;
            }
          }
          invoke(bean.fqon, operation.method, parameter, signature);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies the <code>JMX ObjectName</code> values for all beans in the
   ** specifed {@link Domain}.
   **
   ** @paramm  domain            the wrapper to modify the MBeans belonging to
   **                            the domain.
   */
  private void modify(final Domain instance)
    throws ServiceException {

    for (Type type : instance.type)
      modify(instance, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies the <code>JMX ObjectName</code> values for all beans in the
   ** specifed {@link Domain} and matchs the specified {@link Type}.
   **
   ** @paramm  domain            the wrapper to modify the MBeans belonging to
   **                            the domain.
   ** @paramm  type              the wrapper to modify the MBeans belonging to
   **                            the type in the specific domain.
   */
  private void modify(final Domain domain, final Type instance)
    throws ServiceException {

    for (Bean bean : instance.path) {
      objectName(domain, bean);
      final String[] arguments = { ENTITY,  bean.fqon.toString() };
      warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));
      for (Map.Entry<String, Value> entry : bean.attribute.entrySet())
        attribute(bean.fqon, entry.getKey(), entry.getValue().target);

      for (Map.Entry<String, Object> entry : bean.parameter().entrySet())
        property(bean.fqon, entry.getKey(), entry.getValue());
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, arguments));
    }
    for (Type type : instance.type)
      modify(domain, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Returns the <code>JMX ObjectName</code> values for the specified bean.
   **
   ** @paramm  domain
   ** @paramm  bean
   */
  private final void fetch(final Bean bean)
    throws ServiceException {

    try {
      for (Map.Entry<String, Value> entry : bean.attribute.entrySet()) {
        final Value value = entry.getValue();
        try {
          final Object unknown = this.connection.getAttribute(bean.fqon, entry.getKey());
          if (unknown instanceof CompositeData[]) {
            final CompositeData[] data = (CompositeData[])unknown;
            for (CompositeData i : data)
              System.out.println(i.get("name"));
          }
          else if (unknown instanceof Object[])
            value.actual = StringUtility.listToString(Arrays.asList((Object[])unknown));
          else
            value.actual = String.valueOf(unknown);
        }
        catch (AttributeNotFoundException e) {
          debug(ServiceResourceBundle.format(ServiceError.MANAGEDBEAN_ATTRIBUTE_NOTFOUND, bean.fqon.toString(), entry.getKey(), e.getLocalizedMessage()));
          value.actual = "undefined";
        }
        catch (InstanceNotFoundException e) {
          error(ServiceResourceBundle.format(ServiceError.MANAGEDBEAN_INSTANCE_NOTFOUND, bean.fqon.toString(), e.getLocalizedMessage()));
          value.actual = "undefined";
        }
      }
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName
  /**
   ** Returns the <code>JMX ObjectName</code> name this task will execute.
   **
   ** @return                    the <code>JMX ObjectName</code> name this task
   **                            will execute.
   */
  private final void objectName(final Domain domain, final Bean bean) {
    final StringBuilder builder = new StringBuilder(String.format("%s:", domain.name()));
    // if a specific location has to be used put in in front of name and type
    if (!StringUtility.isEmpty(this.location))
      builder.append(String.format("Location=%s", this.location));

    if (builder.charAt(builder.length() - 1) != ':')
      builder.append(',');
    builder.append(String.format(bean.camelCase ? "Name=%s" : "name=%s", bean.name()));

    if (!StringUtility.isEmpty(this.driverType))
      builder.append(String.format(",driverType=%s", this.driverType));

    builder.append(String.format(bean.camelCase ? ",Type=%s" : ",type=%s", bean.type()));

    if (!StringUtility.isEmpty(this.application))
      builder.append(String.format(",Application=%s", this.application));

    if (!StringUtility.isEmpty(this.version))
      builder.append(String.format(",ApplicationVersion=%s", this.version));

    if (!StringUtility.isEmpty(this.server))
      builder.append(String.format(",server=%s", this.server));

    try {
      bean.fqon = new ObjectName(builder.toString());
    }
    catch (MalformedObjectNameException e) {
      throw new AssertionError(e.getMessage());
    }
  }
}