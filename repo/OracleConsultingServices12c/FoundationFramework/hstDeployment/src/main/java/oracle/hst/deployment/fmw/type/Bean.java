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

    File        :   Bean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Bean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.fmw.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;

import oracle.hst.deployment.spi.EnvironmentHandler;

////////////////////////////////////////////////////////////////////////////////
// class Bean
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
 ** This class implements the name part of the unique identifier of a MBean.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bean extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final EnvironmentHandler.Bean delegate = new EnvironmentHandler.Bean();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Value
  // ~~~~~ ~~~~~
  /**
   ** <code>Value</code> defines the attribute and property definition of a
   ** JXM Bean.
   */
  public static class Value extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String   name;
    private String   value;
    private Class<?> type  = String.class;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Value</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    protected Value() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>name</code>.
     **
     ** @param  name               the name of the parameter in Oracle WebLogic
     **                            Domain to handle.
     */
    public void setName(final String name) {
      checkAttributesAllowed();
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the parameter in Oracle WebLogic Domain to handle.
     **
     ** @return                  the name of the parameter in Oracle WebLogic
     **                          Domain to handle.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setValue
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>value</code>.
     **
     ** @param  value            the value of the parameter in Oracle WebLogic
     **                          Domain to handle.
     */
    public void setValue(final String value) {
      checkAttributesAllowed();
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the parameter in Oracle WebLogic Domain to handle.
     **
     ** @return                  the value of the parameter in Oracle WebLogic
     **                          Domain to handle.
     */
    public final String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setType
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>type</code>.
     **
     ** @param  clazz              the clazz of the value.
     */
    public void setType(final Class<?> clazz) {
      checkAttributesAllowed();
      this.type = clazz;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the type of the value in Oracle WebLogic Domain to handle.
     **
     ** @return                  the type of the value in Oracle WebLogic
     **                          Domain to handle.
     */
    public final Class<?> type() {
      return this.type;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Attribute</code> defines the attribute definition of an JXM Bean.
   */
  public static class Attribute extends Value {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Attribute</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Attribute() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Property
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>Property</code> defines the property definition of an JXM Bean.
   */
  public static class Property extends Value {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Property</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Property() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parameter
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Parameter</code> defines the parameter definition of an operation
   ** invoked at a JXM Bean.
   */
  public static class Parameter extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final EnvironmentHandler.Parameter delegate = new EnvironmentHandler.Parameter();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
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
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setClass
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>class</code>.
     **
     ** @param  clazz              the clazz of the parameter.
     */
    public void setClass(final Class clazz) {
      checkAttributesAllowed();
      this.delegate.clazz(clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setValue
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>value</code>.
     **
     ** @param  object           the value object of the parameter.
     */
    public void setValue(final String object) {
      checkAttributesAllowed();
      this.delegate.value(object);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Operation
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Operation</code> defines the operational definition of an JXM Bean.
   **
   ** @author  dieter.steding@oracle.com
   ** @version 1.0.0.0
   ** @since   1.0.0.0
   */
  public static class Operation extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final EnvironmentHandler.Operation delegate = new EnvironmentHandler.Operation();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Operation</code> type that allows use as a
     ** JavaBean.
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
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setMethod
    /**
     ** Called to inject the argument for parameter <code>method</code>.
     **
     ** @param  method           the name of the method this operation on an
     **                          MBean invokes.
     */
    public final void setMethod(final String method) {
      checkAttributesAllowed();
      this.delegate.method(method);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   instance
    /**
     ** Returns the {@link EnvironmentHandler.Operation} delegate of Oracle
     ** Weblogic Domain entity instance to handle.
     **
     ** @return                  the {@link EnvironmentHandler.Bean} delegate
     **                          of Oracle  Weblogic Domain entity instance.
     */
    public final EnvironmentHandler.Operation instance() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredParameter
    /**
     ** Call by the ANT deployment to inject the argument for nested parameter
     ** <code>parameter</code>.
     **
     ** @param  parameter        the named value pair to add.
     */
    public void addConfiguredParameter(final Parameter parameter)
      throws BuildException {

      checkChildrenAllowed();
      this.delegate.add(parameter.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
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
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the bean in Oracle Weblogic Domain
   **                            to handle.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCamelCase
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>camelCase</code>.
   ** <p>
   ** Set to <code>true</code> if the name building has to care about how the
   ** Name and Type of the object name to build for this bean has to be spelled.
   ** <ol>
   **   <li><code>false</code> (the default) advice that the name and type
   **       prefix has to be spelled in lower case letters.
   **   <li><code>true</code> advice that the name and type prefix has to be
   **       spelled in camel case letters.
   ** </ol>
   **
   ** @param  camelCase            <code>true</code> advice that the name and
   **                              type prefix has to be spelled in camel case
   **                              letters; otherwise <code>false</code>.
   */
  public void setCamelCase(final boolean camelCase) {
    checkAttributesAllowed();
    this.delegate.camelCase(camelCase);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link EnvironmentHandler.Bean} delegate of Oracle Weblogic
   ** Domain entity instance to handle.
   **
   ** @return                    the {@link EnvironmentHandler.Bean} delegate
   **                            of Oracle Weblogic Domain entity instance.
   */
  public final EnvironmentHandler.Bean instance() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredType
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>xmlconfig</code>.
   **
   ** @param  type               a fully qualified metadata document to export,
   **                            import or delete.
   **                            <p>
   **                            This can be specified by a comma-separated list
   **                            of fully qualified metadata documents or a
   **                            document name pattern that is used to identify
   **                            which documents to handled.
   **
   ** @throws BuildException     if the specified {@link Type} is already
   **                            assigned to this instance.
   */
  public void addConfiguredType(final Type type)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.add(type.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAttribute
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>attribute</code>.
   **
   ** @param  attribute          the named value pait to add.
   **
   ** @throws BuildException     if the specified attribute is already part of
   **                            the attribute mapping.
   */
  public void addConfiguredAttribute(final Attribute attribute)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.addAttribute(attribute.name(), attribute.value(), attribute.type());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProperty
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>property</code>.
   **
   ** @param  property           the named value pait to add.
   **
   ** @throws BuildException     if the specified property is already part of
   **                            the parameter mapping.
   */
  public void addConfiguredProperty(final Property property)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.addProperty(property.name(), property.value(), property.type());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredOperation
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>operation</code>.
   **
   ** @param  operation          the operation add.
   **
   ** @throws BuildException     if the specified operation is already part of
   **                            the mapping.
   */
  public void addConfiguredOperation(final Operation operation)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.addOperation(operation.delegate);
  }
}