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

    File        :   AbstractInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Calendar;
import java.util.LinkedHashMap;

import java.text.ParseException;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AbstractInstance
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractInstance</code> represents a data instance wrapper a
 ** {@link AbstractHandler} use to apply deployment operations on any deployment
 ** objects.
 ** <p>
 ** Such oparations might be a create, delete or configure action on an
 ** object peformed before, after or during a deployment operation.
 ** <p>
 ** Subclasses of this classes providing the data model an implementation of
 ** {@link AbstractHandler} needs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private long                    identifier = -1L;
  private String                  name       = null;
  private HashMap<String, Object> parameter  = new LinkedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractInstance</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractInstance</code> with the specified identifier.
   **
   ** @param  identifier         the internal identifier.
   */
  protected AbstractInstance(final long identifier) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.identifier = identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractInstance</code> with the specified name.
   **
   ** @param  name               the value set for the name property.
   */
  protected AbstractInstance(final String name) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractInstance</code> with the specified identifier
   ** and name.
   **
   ** @param  identifier         the internal identifier.
   ** @param  name               the value set for the name property.
   */
  protected AbstractInstance(final long identifier, final String name) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.identifier = identifier;
    this.name       = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Called to inject the argument for parameter <code>identifier</code>.
   **
   ** @param  identifier         the internal identifier to handle in Oracle
   **                            Weblogic Domain server entity instance.
   **
   ** @return                    the <code>AbstractInstance</code> for method
   **                            chaining purpose.
   */
  public final AbstractInstance identifier(final long identifier) {
    this.identifier = identifier;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the internal identifier of the entity instance.
   **
   ** @return                    the internal identifier of the entity instance.
   */
  public final long identifier() {
    return this.identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name context to handle in Oracle Weblogic
   **                            Domain server entity instance.
   **
   ** @return                    the <code>AbstractInstance</code> for method
   **                            chaining purpose.
   */
  public AbstractInstance name(final String name) {
    this.name = name;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the entity instance in Oracle Weblogic Domain server
   ** entity instance to handle.
   **
   ** @return                    the name context to handle in Oracle Weblogic
   **                            Domain server entity instance.
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
   ** @return                    the <code>boolean</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
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
   ** @return                    the <code>boolean</code> for the given key or
   **                            the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
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
   ** @return                    the <code>int</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
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
   ** @return                    the <code>int</code> for the given key or the
   **                            default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public int integerParameter(final String key, final int defaultValue) {
    final String result = stringParameter(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else
      return Integer.parseInt(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longParameter
  /**
   ** Returns a <code>long</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the <code>long</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public long longParameter(final String key) {
    final String result = stringParameter(key);
    return Long.parseLong(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longParameter
  /**
   ** Returns a <code>long</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the <code>long</code> for the given key or the
   **                            default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public long longParameter(final String key, final long defaultValue) {
    final String result = stringParameter(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else
      return Long.parseLong(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateParameter
  /**
   ** Returns a {@link Date} from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired date attribute.
   **
   ** @return                    the {@link Date} for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>Date</code>.
   */
  public Date dateParameter(final String key) {
    try {
      final Calendar result = DateUtility.parseDate(stringParameter(key), "yyyy-MM-dd'T'HH:mm:ss'Z'");
      return result.getTime();
    }
    catch (ParseException e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateParameter
  /**
   **Returns a {@link Date} from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired date attribute.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the {@link Date} for the given key or the
   **                            default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>Date</code>.
   */
  public Date dateParameter(final String key, final Date defaultValue) {
    String result = (String)this.parameter.get(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else
      try {
        final Calendar temp = DateUtility.parseDate(result, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        return temp.getTime();
      }
      catch (ParseException e) {
        throw new BuildException(e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringParameter
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the <code>String</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public String stringParameter(final String key) {
    return stringParameter(key, SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringParameter
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the int for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public String stringParameter(final String key, final String defaultValue) {
    String result = (String)this.parameter.get(key);
    if (StringUtility.isEmpty(result))
      result = defaultValue;

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringListParameter
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired {@link List} of
   **                            <code>String</code>.
   **
   ** @return                    the <code>String</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public List<String> stringListParameter(final String key) {
    return stringListParameter(key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringListParameter
  /**
   ** Returns a {@link List} of <code>String</code> from the attribute mapping of
   ** this wrapper.
   **
   ** @param  key                the key for the desired {@link List} of
   **                            <code>String</code>.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the {@link List} of <code>String</code> for the
   **                            given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a {@link List}
   **                            of <code>String</code>.
   */
  public List<String> stringListParameter(final String key, final List<String> defaultValue) {
    @SuppressWarnings("unchecked")
    List<String> result = (List<String>)this.parameter.get(key);
    if (result == null)
      result = defaultValue;

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns the parameter mapping of the Object Instance to handle.
   **
   ** @return                    the parameter mapping of the Object Instance to
   **                            handle.
   */
  public final HashMap<String, Object> parameter() {
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAttributeError
  /**
   ** Creates and throws a {@link BuildException} about an attribute mixup.
   **
   ** @param  attributeName      the name of the attribute mixed.
   **
   ** @throws BuildException     always.
   */
  public static void handleAttributeError(final String attributeName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, attributeName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAttributeMandatory
  /**
   ** Creates and throws a {@link BuildException} about an attribute mandatory.
   **
   ** @param  attributeName      the name of the mandatory attribute.
   **
   ** @throws BuildException     always.
   */
  public static void handleAttributeMandatory(final String attributeName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, attributeName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAttributeMissing
  /**
   ** Creates and throws a {@link BuildException} about a missing attribute.
   **
   ** @param  attributeName      the name of the attribute missed.
   **
   ** @throws BuildException     always.
   */
  public static void handleAttributeMissing(final String attributeName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, attributeName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleParameterMissing
  /**
   ** Creates and throws a {@link BuildException} about a specific parameter
   ** element is missing.
   **
   ** @param  parameterName      the name of the parameter element missed.
   **
   ** @throws BuildException     always.
   */
  public static void handleParameterMissing(final String parameterName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, parameterName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleElementMissing
  /**
   ** Creates and throws a {@link BuildException} about a missing element.
   **
   ** @param  elementName        the name of the element missed.
   **
   ** @throws BuildException     always.
   */
  public static void handleElementMissing(final String elementName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_MISSING, elementName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleElementUnexpected
  /**
   ** Creates and throws a {@link BuildException} about an unexpected element.
   **
   ** @param  elementName        the name of the element unexpected.
   **
   ** @throws BuildException     always.
   */
  public static void handleElementUnexpected(final String elementName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_UNEXPECTED, elementName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleElementUnexpected
  /**
   ** Creates and throws a {@link BuildException} about an unexpected element.
   **
   ** @param  elementName        the name of the element unexpected.
   ** @param  context            the context information to explain.
   **
   ** @throws BuildException     always.
   */
  public static void handleElementUnexpected(final String elementName, final String context)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_UNEXPECTED_CONTEXT, elementName, context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Indicates whether both objects are "equal to" the other one.
   **
   ** @param  object1           the firs reference object with which to compare.
   ** @param  object2           the firs reference object with which to compare.
   **
   ** @return                   <code>true</code> if the objects are the same;
   **                           <code>false</code> otherwise.
   */
  public static final boolean equal(final Object object1, final Object object2) {
    if (object1 == object2)
      return true;

    if (object1 == null || object2 == null)
      return false;

    return object1.equals(object2);
  }

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
    long   hc = getClass().hashCode();
    String id = name();
    if (id != null) {
      hc += id.hashCode();
    }
    return (int)hc;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one.
   ** <p>
   ** The <code>equals</code> method implements an equivalence relation on
   ** non-<code>null</code> object references:
   ** <ul>
   **   <li>It is <i>reflexive</i>: for any non-<code>null</code> reference
   **       value <code>x</code>, <code>x.equals(x)</code> should return
   **       <code>true</code>.
   **   <li>It is <i>symmetric</i>: for any non-<code>null</code> reference
   **       values <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
   **       should return <code>true</code> if and only if
   **       <code>y.equals(x)</code> returns <code>true</code>.
   **   <li>It is <i>transitive</i>: for any non-<code>null</code> reference
   **       values <code>x</code>, <code>y</code>, and <code>z</code>, if
   **       <code>x.equals(y)</code> returns <code>true</code> and
   **       <code>y.equals(z)</code> returns <code>true</code>, then
   **       <code>x.equals(z)</code> should return <code>true</code>.
   **   <li>It is <i>consistent</i>: for any non-<code>null</code> reference
   **       values <code>x</code> and <code>y</code>, multiple invocations of
   **       <code>x.equals(y)</code> consistently return <code>true</code> or
   **       consistently return <code>false</code>, provided no information used
   **       in <code>equals</code> comparisons on the objects is modified.
   **   <li>For any non-<code>null</code> reference value <code>x</code>,
   **       <code>x.equals(null)</code> should return <code>false</code>.
   ** </ul>
   ** <p>
   ** Note that it is generally necessary to override the <code>hashCode</code>
   ** method whenever this method is overridden, so as to maintain the general
   ** contract for the <code>hashCode</code> method, which states that equal
   ** objects must have equal hash codes.
   **
   ** @param  other             the reference object with which to compare.
   **
   ** @return                   <code>true</code> if this object is the same as
   **                           the other argument; <code>false</code>
   **                           otherwise.
   **
   ** @see    #hashCode()
   */
  @Override
  public boolean equals(final Object other) {
    if (other == null || (!(other instanceof AbstractInstance)))
      return false;

    final AbstractInstance another = (AbstractInstance)other;
    return (equal(getClass(), another.getClass())) && (equal(name(), another.name()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
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
   ** @return                    the <code>AbstractInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  public AbstractInstance addParameter(final String name, final boolean value)
    throws BuildException {

    addParameter(name, Boolean.valueOf(value));
    return this;
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
   ** @return                    the <code>AbstractInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  public AbstractInstance addParameter(final String name, final int value)
    throws BuildException {

    addParameter(name, Integer.valueOf(value));
    return this;
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
   ** @return                    the <code>AbstractInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  public AbstractInstance addParameter(final String name, final Object value)
    throws BuildException {

    if (this.parameter.containsKey(name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_PARAMETER_ONLYONCE, name));

    // add the value pair to the parameters
    this.parameter.put(name, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Oracle Weblogic Domain server entity instance.
   **
   ** @return                    the <code>AbstractInstance</code> for method
   **                            chaining purpose.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that already part of the parameter
   **                            mapping.
   */
  public AbstractInstance addParameter(final Map<String, Object> parameter)
    throws BuildException {

    for (Map.Entry<String, Object> entry : parameter.entrySet())
      addParameter(entry.getKey(), entry.getValue());
    return this;
  }
}