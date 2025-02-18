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

    File        :   Parameter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Parameter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.scheduler.type;

import java.util.Map;
import java.util.LinkedHashMap;

import java.io.Serializable;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.iam.scheduler.vo.ITResource;
import oracle.iam.scheduler.vo.JobParameter;

import oracle.hst.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// class Parameter
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Parameter</code> defines the restriction on value parameter that can
 ** be passed to scheduled job deployments.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Parameter extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PARAMETER_TYPE_REQUIRED = "required";
  public static final String PARAMETER_TYPE_ENCRYPTED = "encrypted";
  public static final String PARAMETER_TYPE_HELPTEXT = "helpText";

  // the supported data types
  static String[] DATA_TYPE = {
    JobParameter.DATA_TYPE_BOOLEAN
  , JobParameter.DATA_TYPE_NUMBER
  , JobParameter.DATA_TYPE_STRING
  , JobParameter.DATA_TYPE_ITRESOURCE
  };

  // the supported parameter types
  static String[] PARAMETER_TYPE = {
    PARAMETER_TYPE_REQUIRED
  , PARAMETER_TYPE_ENCRYPTED
  , PARAMETER_TYPE_HELPTEXT
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              name = null;
  private String              value = null;
  private Type                type = null;
  private Map<String, String> property = new LinkedHashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  // ~~~~~ ~~~~
  /**
   ** Helper class, holds the attribute <code>type</code> values.
   ** <p>
   ** Class must be declared static to give ANT the chance to instanciate the
   ** inner class.
   */
  public static class Type extends EnumeratedAttribute {

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the name of the category to be exported or imported by this
     ** category.
     **
     ** @return                  the name of the category to be exported or
     **                          imported by this category.
     */
    public final String value() {
      return super.getValue();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  getValues (EnumeratedAttribute)
    /**
     ** The only method a subclass needs to implement.
     **
     ** @return                  an array holding all possible values of the
     **                          enumeration. The order of elements must be
     **                          fixed so that indexOfValue(String) always
     **                          return the same index for the same value.
     */
    @Override
    public String[] getValues() {
      return DATA_TYPE;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Property
  // ~~~~~ ~~~~~~~~
  /**
   ** Helper class, holds the nested <code>property</code> values.
   ** <p>
   ** Class must be declared static to give ANT the chance to instanciate the
   ** inner class.
   */
  public static class Property extends EnumeratedAttribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String option = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
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
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setOption
    /**
     ** Call by the ANT kernel to inject the argument for parameter option.
     **
     ** @param  option         the value option to set.
     */
    public void setOption(final String option) {
      this.option = option;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: option
    /**
     ** Returns the value option.
     **
     ** @return                  the value option.
     */
    public final String option() {
      return this.option;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  getValues (EnumeratedAttribute)
    /**
     ** The only method a subclass needs to implement.
     **
     ** @return                  an array holding all possible values of the
     **                          enumeration. The order of elements must be
     **                          fixed so that indexOfValue(String) always
     **                          return the same index for the same value.
     */
    @Override
    public String[] getValues() {
      return PARAMETER_TYPE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public int hashCode() {
      long   hc = getClass().hashCode();
      String id = this.value;
      if (id != null) {
        hc += id.hashCode();
      }
      return (int)hc;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Property</code> object that
     ** represents the same <code>value</code> as this object.
     **
     ** @param other               the object to compare this
     **                            <code>Property</code> against.
     **
     ** @return                   <code>true</code> if the
     **                           <code>Property</code>s are
     **                           equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Property))
        return false;

      final Property property = (Property)other;
      return this.value.equals(property.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>Parameter</code> to initialize the instance.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager job instance.
   ** @param  type               the type for <code>name</code> to set on the
   **                            Identity Manager job instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager job instance.
   */
  public Parameter(final String name, final Type type, final String value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name = name;
    this.type = type;
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Retunr the properties of this job parameter.
   **
   ** @return                  the properties of this job parameter.
   */
  public Map<String, String> property() {
    return this.property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT kernel to inject the argument for parameter name.
   **
   ** @param  name             the name of the Identity Manager object
   **                          this job parameter wraps.
   */
  public void setName(final String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of this job parameter.
   **
   ** @return                    the name of this job parameter.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   ** Call by the ANT kernel to inject the argument for parameter value.
   **
   ** @param  value            the value of this job parameter.
   */
  public void setValue(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of this job parameter.
   **
   ** @return                    the value of this job parameter.
   */
  public final Serializable value() {
    Serializable value = null;
    if (JobParameter.DATA_TYPE_BOOLEAN.equals(this.type.value()))
      value = new Boolean(this.value);
    else if (JobParameter.DATA_TYPE_STRING.equals(this.type.value()))
      value = this.value;
    else if (JobParameter.DATA_TYPE_NUMBER.equals(this.type.value()))
      return new Long(this.value);
    else if (JobParameter.DATA_TYPE_ITRESOURCE.equals(this.type.value())) {
      final ITResource tmp = new ITResource();
      tmp.setName(this.value);
      value = tmp;
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDataType
  /**
   ** Call by the ANT kernel to inject the argument for parameter type.
   **
   ** @param  type              the type of this job parameter.
   */
  public void setDatatype(final Type type) {
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of this job parameter.
   **
   ** @return                    the type of this job parameter.
   */
  public final Type type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: setRequired
  /**
   ** Call by the ANT kernel to inject the argument for parameter required.
   **
   ** @param  required           <code>true</code> if the parameter is required
   **                            for execution.
   */
  public void setRequired(final boolean required) {
    this.property.put(PARAMETER_TYPE_REQUIRED, required ? SystemConstant.TRUE : SystemConstant.FALSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Returns <code>true</code> if the parameter is required for execution.
   **
   ** @return                    <code>true</code> if the parameter is required
   **                            for execution.
   */
  public boolean required() {
    return Boolean.parseBoolean(this.property.get(PARAMETER_TYPE_REQUIRED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEncrypted
  /**
   ** Call by the ANT kernel to inject the argument for parameter encrypted.
   **
   ** @param  encrypted          <code>true</code> if the parameter is encrypted
   **                            for execution.
   */
  public void setEncrypted(final boolean encrypted) {
    this.property.put(PARAMETER_TYPE_ENCRYPTED, encrypted ? SystemConstant.TRUE : SystemConstant.FALSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypted
  /**
   ** Returns <code>true</code> if the parameter is encrypted for execution.
   **
   ** @return                    <code>true</code> if the parameter is encrypted
   **                            for execution.
   */
  public boolean encrypted() {
    return Boolean.parseBoolean(this.property.get(PARAMETER_TYPE_ENCRYPTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHelpText
  /**
   ** Call by the ANT kernel to inject the argument for parameter help.
   **
   ** @param  helpText           the text to display to an end user if he or she
   **                            hovers over the field.
   */
  public void setHelpText(final String helpText) {
    this.property.put(PARAMETER_TYPE_HELPTEXT, helpText);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   helpText
  /**
   ** Returns the text to display to an end user if he or she hovers over the
   ** field.
   **
   ** @return                    the text to display to an end user if he or she
   **                            hovers over the field.
   */
  public final String helpText() {
    return this.property.get(PARAMETER_TYPE_HELPTEXT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProperty
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Property}.
   **
   ** @param  property          the {@link Property} to add.
   */
  public void addProperty(final Property property) {
    checkAttributesAllowed();
    this.property.put(property.getValue(), property.option());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProperty
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Property}.
   **
   ** @param  property          the {@link Property} to add.
   */
  public void addConfiguredProperty(final Property property) {
    addProperty(property);
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
    return this.name.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Parameter</code> object that
   ** represents the same <code>name</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>Parameter</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>Parameter</code>s are equal;
   **                           <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Parameter))
      return false;

    final Parameter parameter = (Parameter)other;
    return this.name.equals(parameter.name);
  }
}