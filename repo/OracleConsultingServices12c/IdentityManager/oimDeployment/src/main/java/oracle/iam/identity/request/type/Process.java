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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Process.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Process.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Process
// ~~~~~ ~~~~~~~
/**
 ** <code>Process</code> encapsulate a Identity Manager
 ** <code>Process Definition</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Process extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for Form Definitions. */
  public static final String PREFIX = "Process Definition.";

  /**
   ** the mapping key contained in a collection to specify that the process
   ** system key should be resolved
   */
  public static final String KEY = PREFIX + AbstractHandler.FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the process name
   ** should be resolved
   */
  public static final String NAME = PREFIX + AbstractHandler.FIELD_NAME;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                         name;
  private ProcessForm                    form = null;
  private DataSetValidator               validator = null;
  private final List<Operation>          operation = new ArrayList<Operation>();
  private final List<Attribute>          attribute = new ArrayList<Attribute>();
  private final List<AttributeReference> reference = new ArrayList<AttributeReference>();

  private final List<String> flattenA = new ArrayList<String>();
  private final List<String> flattenR = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Process Definition</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Process() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Process Definition</code> for the specified name.
   **
   ** @param name                the name of the
   **                            <code>Process Definition</code>.
   */
  public Process(final String name) {
    // ensure inheritance
    super();

    this.name = name;
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
   ** @param  name               the name of the <code>Process Definition</code>
   **                            in Identity Manager.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Process Definition</code> instance of
   ** Identity
   ** Manager.
   **
   ** @return                    the name of the Process Definition instance of
   **                            Identity Manager.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   form
  /**
   ** Sets the main process form of the <code>Process Definition</code> in
   ** Identity Manager.
   **
   ** @param  form               the main process form of the
   **                            <code>Process Definition</code> in Identity
   **                            Manager.
   */
  public void form(final ProcessForm form) {
    this.form = form;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   form
  /**
   ** Returns the main process form of the <code>Process Definition</code> in
   ** Identity Manager.
   **
   ** @return                    the main process form of the
   **                            <code>Process Definition</code> in Identity
   **                            Manager.
   */
  public ProcessForm form() {
    return this.form;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns the operations assigned to this <code>Process Definition</code>
   ** in Identity Manager.
   **
   ** @return                    the operations assigned to this
   **                            <code>Process Definition</code> in Identity
   **                            Manager.
   */
  public List<Operation> operation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Returns <code>true</code> if the specified operations assigned to this
   ** <code>Process Definition</code> in Identity Manager.
   **
   ** @param  operation          the operations to check for assignment.
   **
   ** @return                    <code>true</code> if the specified operations
   **                            is assigned to this
   **                            <code>Process Definition</code> in Identity
   **                            Manager; otherwise <code>false</code>.
   */
  public boolean contains(final String operation) {
    boolean result = false;
    for (Operation o : this.operation) {
      if (operation.equals(o.getValue())) {
        result = true;
        break;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Lookup of the operations for the specified name assigned to this
   ** <code>Process Definition</code> instance of Identity Manager.
   **
   ** @param  name               the tag name of the desired {@link Operation}.
   **
   ** @return                    the operations for the specified name assigned
   **                            to this <code>Process Definition</code> in
   **                            Identity Manager.
   */
  public Operation operation(final String name) {
    Operation result = null;
    for (Operation cursor : this.operation) {
      if (name.equals(cursor.getValue())) {
        result = cursor;
        break;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationIterator
  /**
   ** Returns the operations assigned to this <code>Process Definition</code> in
   ** Identity Manager as an {@link Iterator}.
   **
   ** @return                    the operations assigned to this
   **                            <code>Process Definition</code> in Identity
   **                            Manager as an {@link Iterator}.
   */
  public Iterator<Operation> operationIterator() {
    return this.operation.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Attribute}s assigned to the
   ** <code>Process Definition</code>.
   **
   **
   ** @return                    the {@link Attribute}s assigned to the
   **                            <code>Process Definition</code>.
   */
  public List<Attribute> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the {@link AttributeReference}s assigned to the Process
   ** Definition.
   **
   **
   ** @return                    the {@link AttributeReference}s assigned to the
   **                            <code>Process Definition</code>.
   */
  public List<AttributeReference> reference() {
    return this.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validator
  /**
   ** Returns the {@link DataSetValidator}s assigned to the Process
   ** Definition.
   **
   **
   ** @return                    the {@link DataSetValidator}s assigned to the
   **                            <code>Process Definition</code>.
   */
  public DataSetValidator validator() {
    return this.validator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    return StringUtility.caseInsensitiveHash(this.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Resource</code> object that
   ** represents the same<code>name</code> as this instance.
   **
   ** @param other               the object to compare this
   **                            <code>Process</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>Process</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Resource))
      return false;

    final Process another = (Process)other;
    return this.name.equals(another.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** <b>Note:</b>
   ** We are not calling the validation method on the super class to prevent
   ** the validation of the <code>parameter</code> mapping.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, "name"));

    if (this.operation.isEmpty())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.WORKFLOW_OPERATION_MANDATORY));
    else {
      for (Operation operation : this.operation)
        operation.validate();
    }
    if (this.validator != null)
      this.validator.validate();

    if (!this.reference.isEmpty()) {
      for (AttributeReference reference : this.reference)
        reference.validate();
    }
    if (!this.attribute.isEmpty()) {
      for (Attribute attribute : this.attribute)
        attribute.validate();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRequestAttribute
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Attribute}.
   **
   ** @param  attribute          the subject of substitution.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            {@link Attribute}.
   */
  public void addConfiguredRequestAttribute(final Attribute attribute)
    throws BuildException {

    if (this.flattenA.contains(attribute.getName()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_ATTRIBUTE_ONLYONCE, attribute.getName()));

    this.flattenA.add(attribute.getName());
    this.attribute.add(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredEntityAttribute
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link AttributeReference}.
   **
   ** @param  reference          the subject of substitution.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            {@link AttributeReference}.
   */
  public void addConfiguredEntityAttribute(final AttributeReference reference)
    throws BuildException {

    if (this.flattenR.contains(reference.getName()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_REFERENCE_ONLYONCE, reference.getName()));

    this.flattenR.add(reference.getName());
    this.reference.add(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredOperation
  /**
   ** Called to add the nested element for parameter <code>operation</code>.
   **
   ** @param  operation          the operation to add.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            {@link Operation}.
   */
  public void addConfiguredOperation(final Operation operation)
    throws BuildException {

    if (this.operation.contains(operation))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_OPERATION_ONLYONCE, operation.getValue()));

    for (Attribute attribute : operation.attribute()) {
      if (this.flattenA.contains(attribute.getName()))
        throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_ATTRIBUTE_ONLYONCE, attribute.getName(), "process"));

      this.flattenA.add(attribute.getName());
    }

    for (AttributeReference reference : operation.reference()) {
      if (this.flattenR.contains(reference.getName()))
        throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_ATTRIBUTE_ONLYONCE, reference.getName(), "process"));

      this.flattenR.add(reference.getName());
    }

    this.operation.add(operation);
  }
}