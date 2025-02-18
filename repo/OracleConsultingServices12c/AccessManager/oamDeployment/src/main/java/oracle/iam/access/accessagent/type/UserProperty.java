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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   UserProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.type.DelegatingDataType;

import oracle.iam.access.common.spi.schema.UserDefinedParam;
import oracle.iam.access.common.spi.schema.UserDefinedParameters;

////////////////////////////////////////////////////////////////////////////////
// class UserProperty
// ~~~~~ ~~~~~~~~~~~~
public class UserProperty extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final UserDefinedParameters delegate = factory.createUserDefinedParameters();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Property
  // ~~~~~ ~~~~~~~~
  public static class Property extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final UserDefinedParam delegate = factory.createUserDefinedParam();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Value
    // ~~~~~ ~~~~~
    public static class Value extends ServiceDataType {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      String value;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Value</code> type that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public Value() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Mutator methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: setValue
      /**
       ** Sets the value of the <code>value</code> property.
       **
       ** @param  value            the value of the <code>value</code> property.
       **                          Allowed object is {@link String}.
       */
      public void setValue(final String value) {
        this.value = value;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Property</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Property() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
    /**
     ** Sets the value of the <code>name</code> property.
     **
     ** @param  value              the value of the <code>name</code> property.
     **                            Allowed object is {@link String}.
     */
    public void setName(final String value) {
      this.delegate.setName(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredUserValue
    /**
     ** Call by the ANT deployment to inject the argument for adding a
     ** <code>Value</code>.
     **
     ** @param  value            the {@link Value} instance to add.
     **                          Allowed object is {@link Property}.
     **
     ** @throws BuildException   if the rule instance is already added.
     */
    public void addConfiguredUserValue(final Value value)
      throws BuildException {

      // verify if the reference id is set on the element to prevent inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // prevent bogus input
      if (value == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "value"));

      // prevent bogus input
      if (this.delegate.getValue().contains(value.value))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "value"));

      this.delegate.getValue().add(value.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserProperty</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserProperty() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredUserProperty
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Property</code>.
   **
   ** @param  value              the {@link Property} instance to add.
   **                            Allowed object is {@link Property}.
   **
   ** @throws BuildException     if the rule instance is already added.
   */
  public void addConfiguredUserProperty(final Property value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "property"));

    // prevent bogus input
    if (this.delegate.getUserDefinedParam().contains(value.delegate))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "property"));

    this.delegate.getUserDefinedParam().add(value.delegate);
  }
}