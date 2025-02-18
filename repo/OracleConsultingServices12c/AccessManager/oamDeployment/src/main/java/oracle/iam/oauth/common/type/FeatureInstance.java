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

    File        :   FeatureInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.type;

import java.util.Map;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureInstance
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>FeatureInstance</code> represents an <code>Entity</code>
 ** instance in Oracle Access Manager that might be created, deleted or
 ** configured during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FeatureInstance extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String              name      = null;
  protected Map<String, String> parameter = new HashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Instance</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FeatureInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Instance</code>.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(this.name) || this.parameter.size() > 0)
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for attribute <code>name</code>.
   **
   ** @param  name               the name of the instance in Oracle Access
   **                            Manager to handle.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the name of the instance in Oracle Access Manager
   ** to handle.
   **
   ** @return                    the name of the instance in Oracle Access
   **                            Manager to handle.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanParameter
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the boolean for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>boolean</code>.
   */
  public boolean booleanParameter(final String key) {
    String result = stringParameter(key);
    // convert the yes/no semantic to the correct meaning for class Boolean
    if (SystemConstant.YES.equalsIgnoreCase(result))
      result = SystemConstant.TRUE;

    return Boolean.valueOf(result).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanParameter
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the boolean for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>boolean</code>.
   */
  public boolean booleanParameter(final String key, final boolean defaultValue) {
    String result = stringParameter(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else {
      // convert the yes/no semantic to the correct meaning for class Boolean
      if (SystemConstant.YES.equalsIgnoreCase(result))
        result = SystemConstant.TRUE;

      return Boolean.valueOf(result).booleanValue();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerParameter
  /**
   ** Returns a <code>int</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the int for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>int</code>.
   */
  public int integerParameter(final String key) {
    final String result = stringParameter(key);
    return Integer.parseInt(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerParameter
  /**
   ** Returns a <code>int</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the int for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>int</code>.
   */
  public int integerParameter(final String key, final int defaultValue) {
    final String result = stringParameter(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else
      return Integer.parseInt(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringParameter
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the <code>String</code> for the given key.
   */
  public String stringParameter(final String key) {
    return stringParameter(key, SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringParameter
  /**
   **Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the string for the given key or the default.
   */
  public String stringParameter(final String key, final String defaultValue) {
    String result = this.parameter.get(key);
    if (StringUtility.isEmpty(result))
      result = defaultValue;

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the data type to use.
   ** <p>
   ** The defualt implementation does nothing.
   ** Subclasses are enforced to override this method to achive validation.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    if (StringUtility.isEmpty(this.name))
      handleAttributeMissing("name");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  name               the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>name</code> to set on this
   **                            instance.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  protected void addParameter(final String name, final String value)
    throws BuildException {

    if (this.parameter.containsKey(name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_PARAMETER_ONLYONCE, name));

    // add the value pair to the parameters
    this.parameter.put(name, value);
  }
}