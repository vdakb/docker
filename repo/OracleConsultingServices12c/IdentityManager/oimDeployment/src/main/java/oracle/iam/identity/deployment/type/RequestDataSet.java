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

    File        :   RequestDataSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    RequestDataSet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.RequestForm;

////////////////////////////////////////////////////////////////////////////////
// class RequestDataSet
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>RequestDataSet</code> represents the data mapping in Identity Manager
 ** that might be applied during a request operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RequestDataSet extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Attribute</code> represents an <code>Attribute</code> value of
   ** a <code>RequestDataSet</code>.
   */
  public static class Attribute extends DataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final RequestForm.Attribute delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Attribute() {
      // ensure inheritance
      super();

      // initialize instance
      this.delegate = new RequestForm.Attribute();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setRefid
    /**
     ** Call by the ANT kernel to inject the argument for parameter refid.
     ** <p>
     ** Makes this instance in effect a reference to another <code>Role</code>
     ** instance.
     ** <p>
     ** You must not set another attribute or nest elements inside this element
     ** if you make it a reference.
     **
     ** @param  reference        the id of this instance.
     **
     ** @throws BuildException   if any other instance attribute is already set.
     */
    public void setRefid(final Reference reference)
      throws BuildException {

      if (!StringUtility.isEmpty(this.delegate.name()))
        throw tooManyAttributes();

      // ensure inheritance
      super.setRefid(reference);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setOperation
    /**
     ** Call by the ANT deployment to inject the argument for attribute
     ** <code>operation</code>.
     **
     ** @param  operation        the {@link Operation} to execute in Identity
     **                          Manager.
     **
     ** @throws BuildException   if the specified {@link Operation} is not
     **                          valid.
     */
    public void setOperation(final Operation operation)
      throws BuildException {

      this.delegate.operation(operation.value());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>name</code>.
     **
     ** @param  name             the name of the attribute in Identity Manager
     **                          to handle.
     */
    public final void setName(final String name) {
      checkAttributesAllowed();
      this.delegate.name(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setType
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>type</code>.
     **
     ** @param  type             the type of the attribute in Identity Manager
     **                          to handle.
     */
    public final void setType(final String type) {
      checkAttributesAllowed();
      this.delegate.type(type);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setValue
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>value</code>.
     **
     ** @param  value            the value of the attribute in Identity Manager
     **                          to handle.
     */
    public final void setValue(final String value) {
      checkAttributesAllowed();
      this.delegate.value(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setPrefix
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>prefix</code>.
     **
     ** @param  value            <code>true</code> if the value of the attribute
     **                          in Identity Manager needs to be prefixed with
     **                          the system identifier of the associated
     **                          <code>IT Resource</code>.
     */
    public final void setPrefix(final boolean value) {
      checkAttributesAllowed();
      this.delegate.prefix(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setPattern
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>pattern</code>.
     **
     ** @param  pattern          the pattern of the attribute in Identity
     **                          Manager to handle.
     */
    public final void setPattern(final String pattern) {
      checkAttributesAllowed();
      this.delegate.pattern(pattern);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: instance
    /**
     ** Returns the {@link RequestForm} delegate of Identity Manager to handle.
     **
     ** @return                  the {@link RequestForm} delegate of Identity
     **                          Manager to handle.
     */
    public final RequestForm.Attribute instance() {
      if (isReference())
        return ((Attribute)getCheckedRef()).instance();

      return this.delegate;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RequestDataSet</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RequestDataSet() {
    // ensure inheritance
    super(new RequestForm());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link RequestForm} delegate of Identity Manager to handle.
   **
   ** @return                    the {@link RequestForm} delegate of Identity
   **                            Manager to handle.
   */
  public final RequestForm instance() {
    if (isReference())
      return ((RequestDataSet)getCheckedRef()).instance();

    return (RequestForm)this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAttribute
  /**
   ** Add the specified {@link Attribute}.
   **
   ** @param  attribute            the {@link RequestDataSet} to add.
   */
  public void addConfiguredAttribute(final Attribute attribute) {
    checkAttributesAllowed();
    ((RequestForm)this.delegate).addAttribute(attribute.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredDataSet
  /**
   ** Add the specified {@link RequestDataSet}.
   **
   ** @param  dataSet             the {@link RequestDataSet} to add.
   */
  public void addConfiguredDataSet(final RequestDataSet dataSet) {
    checkAttributesAllowed();
    ((RequestForm)this.delegate).addDataSet(dataSet.instance());
  }
}