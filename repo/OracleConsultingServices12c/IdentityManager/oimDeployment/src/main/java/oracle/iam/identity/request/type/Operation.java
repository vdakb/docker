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

    File        :   Operation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Operation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.request.type;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.deployment.ServiceOperation;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Operation
// ~~~~~ ~~~~~~~~~
/**
 ** Helper class, holds the nested <code>operation</code> values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Operation extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the operations in alphabetical order
  private static final String[] registry = {
    ServiceOperation.enable.id()
  , ServiceOperation.disable.id()
  , ServiceOperation.modify.id()
  , ServiceOperation.provision.id()
  , ServiceOperation.deprovision.id()
//  , SELFPROVISION
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<Attribute>          attribute = new ArrayList<Attribute>();
  private List<AttributeReference> reference = new ArrayList<AttributeReference>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
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

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Attribute}s assigned to the Provisioning Process.
   **
   **
   ** @return                    the {@link Attribute}s assigned to the
   **                            Provisioning Process.
   */
  public List<Attribute> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the {@link AttributeReference}s assigned to the Provisioning
   ** Process.
   **
   **
   ** @return                    the {@link AttributeReference}s assigned to the
   **                            Provisioning Process.
   */
  public List<AttributeReference> reference() {
    return this.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return registry;
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
    return this.value.hashCode();
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
    if (!(other instanceof Operation))
      return false;

    final Operation another = (Operation)other;
    return this.value.equals(another.value);
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

    if (this.attribute.contains(attribute)) {
      final String[] arguments = { attribute.getName(), "operation" };
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_ATTRIBUTE_ONLYONCE, arguments));
    }

    this.attribute.add(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredEntityAttribute
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Attribute}.
   **
   ** @param  reference          the subject of substitution.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            {@link Attribute}.
   */
  public void addConfiguredEntityAttribute(final AttributeReference reference)
    throws BuildException {

    if (this.reference.contains(reference)) {
      final String[] arguments = { reference.getName(), "operation" };
      throw new BuildException(FeatureResourceBundle.format(FeatureError.WORKFLOW_REFERENCE_ONLYONCE, arguments));
    }

    this.reference.add(reference);
  }
}