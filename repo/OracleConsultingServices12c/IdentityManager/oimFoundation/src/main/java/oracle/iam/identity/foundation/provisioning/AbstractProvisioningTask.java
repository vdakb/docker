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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Provisioning Facilities

    File        :   AbstractEventHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractEventHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.provisioning;

import java.lang.reflect.Array;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import oracle.mds.naming.ReferenceException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AbstractAdapterTask;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.LookupValue;
import oracle.iam.identity.foundation.naming.FormDefinition;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractProvisioningTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractProvisioningTask</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Provisioning Event
 ** which handles data delivered to a Target System.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractProvisioningTask extends AbstractAdapterTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the mapping of attribute name and their transformation */
  protected Descriptor        descriptor;

  /**
   ** the process data mapped between Oracle Identity Manager process form and
   ** target system attributes
   */
  private Map<String, Object> data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractSchedulerBaseTask</code> which use the
   ** specified category for logging purpose.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractProvisioningTask(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractProvisioningTask</code> which use the
   ** specified category for logging purpose.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   ** @param  processName        the name of the process used for debugging
   **                            purpose in the scope of gathering performance
   **                            metrics.
   */
  protected AbstractProvisioningTask(final tcDataProvider provider, final String loggerCategory, final String processName) {
    // ensure inheritance
    super(provider, loggerCategory, processName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data
  /**
   ** Returns the process data mapped between Oracle Identity Manager process
   ** form and target system attributes.
   **
   ** @return                    the mapped process data.
   */
  protected final Map<String, Object> data() {
    return this.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
   **
   ** @param  value              the value to convert to native boolean.
   **
   ** @return                    the boolean for the given key.
   */
  public static boolean booleanValue(String value) {
    // convert the yes/no semantic to the correct meaning for class Boolean
    if (SystemConstant.YES.equalsIgnoreCase(value))
      value = SystemConstant.TRUE;

    return Boolean.valueOf(value).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Parses the string argument as a signed decimal integer.
   **
   ** @param  value              a <code>String</code> containing the
   **                            <code>int</code> representation to be parsed
   **
   ** @return                    the integer value represented by the argument
   **                            in decimal.
   **
   ** @throws NumberFormatException if the string does not contain a
   **                               parsable integer.
   */
  public static int integerValue(final String value) {
    return Integer.parseInt(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Parses the string argument as a signed decimal integer.
   **
   ** @param  value              a <code>String</code> containing the
   **                            <code>int</code> representation to be parsed
   ** @param  defaultValue       the value that should be returned if
   **                            <code>null</code> or an empty
   **                            <code>String</code> is passed in.
   **
   ** @return                    the integer value represented by the argument
   **                            in decimal.
   **
   ** @throws NumberFormatException if the string does not contain a
   **                               parsable integer.
   */
  public static int integerValue(final String value, final int defaultValue) {
    return (StringUtility.isEmpty(value)) ? defaultValue : Integer.parseInt(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Creates the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
    **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  path               the path to the configration strored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   **
   ** @return                    an appropriate response message.
   **                            This is either <code>SUCCESS</code> if this
   **                            method completes successfully or the error code
   **                            of the {@link TaskException} catch here.
   */
  public String createProcessData(final Long processInstance, final String path) {
    final String method = "createProcessData";
    try {
      // create the task descriptor that provides the attribute mapping and
      // transformations to be applied on the mapped attributes
      unmarshal(path);
      // create the attribute mapping as described
      this.data = createProcessData(processInstance, this.descriptor.attributeMapping(), this.descriptor.natively());
      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel()) {
        debug(method, TaskBundle.format(TaskMessage.ENTITY_PROVISION, this.descriptor.identifier(), this.data.get(this.descriptor.identifier())));
        debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(this.data)));
      }
      // apply the transformation rules if required
      if (this.descriptor.transformationEnabled()) {
        this.data = this.descriptor.transformationMapping().transform(this.data);
        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel())
          this.debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, StringUtility.formatCollection(this.data)));
      }
      return SUCCESS;
    }
    catch (TaskException e) {
      return e.code();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Creates the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
    **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  attributeMapping   the name of the <code>Lookup Definition</code>
   **                            specifying the attribute mapping definition of
   **                            fields that are part of a provisioning task.
   **
   ** @throws TaskException      if the operation fails.
   */
  @Deprecated
  public void createProcessData(final String processInstance, final String attributeMapping)
    throws TaskException {

    // map the attributes by column label
    this.data = createProcessData(processInstance, attributeMapping, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessDataBulk
  /**
   ** Creates the target system attributes and their corresponding values for
   ** bulk operations.
   ** <p>
   ** The assumption is that Identity Manager already populated the changes
   ** which are applied on the process form hence we don't need to populate the
   ** attribute values from the process form.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  path               the path to the configration strored in the
   **                            Metadata Service specifying the attribute
   **                            mapping definition of fields and their
   **                            transformation that are part of a provisioning
   **                            task.
   ** @param  processData        a {@link Map} containing the raw process data.
   **
   ** @return                    a {@link Map} containing the process data
   **                            mapped to the target system attribute names.
   */
  public final String createProcessDataBulk(final Long processInstance, final String path, final Map<String, Object> processData) {
    final String method = "createProcessDataBulk";
    try {
      // create the task descriptor that provides the attribute mapping and
      // transformations to be applied on the mapped attributes
      unmarshal(path);
      // create the attribute mapping as described
      this.data = this.descriptor.attributeMapping().filterByEncoded(processData);
      // produce the logging output only if the logging level is enabled for
      if (this.logger != null && this.logger.debugLevel()) {
        debug(method, TaskBundle.format(TaskMessage.ENTITY_PROVISION, this.descriptor.identifier(), this.data.get(this.descriptor.identifier())));
        debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(this.data)));
      }
      // apply the transformation rules if required
      if (this.descriptor.transformationEnabled()) {
        this.data = this.descriptor.transformationMapping().transform(this.data);
        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel())
          this.debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, StringUtility.formatCollection(this.data)));
      }
      return SUCCESS;
    }
    catch (TaskException e) {
      return e.code();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformProcessData
  /**
   ** Transforms the target system attributes by applying the transformers
   ** specified by the <code>Lookup Definition</code>
   ** <code>transformation</code>.
   ** <p>
   ** The implementation asumes that {@link #createProcessData(String, String, boolean)}
   ** was executed before this method is invoked
  */
  public void transformProcessData() {
    this.data = this.descriptor.transformationMapping().transform(this.data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformProcessData
  /**
   ** Transforms the target system attributes by applying the transformers
   ** specified by the <code>Lookup Definition</code>
   ** <code>transformation</code>.
   ** <p>
   ** The implementation asumes that {@link #createProcessData(String, String, boolean)}
   ** was executed before this method is invoked
   **
   ** @param  transformation     the name of the <code>Lookup Definition</code>
   **                            specifying the attribute transformation
   **                            definition of fields that are part of a
   **                            provisioning task.
   **
   ** @throws TaskException      if the operation fails.
   */
  @Deprecated
  public void transformProcessData(final String transformation)
    throws TaskException {

    // prevent bogus input
    if (StringUtility.isEmpty(transformation) || this.data == null || this.data.size() == 0)
      return;

    this.data = this.descriptor.transformationMapping().transform(this.data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataContains
  /**
   ** Returns <code>true</code> if the process data contains a mapping for the
   ** specified <code>key</code>.
   ** <p>
   ** More formally, returns <code>true</code> if and only if the process data
   ** contains a mapping for <code>key</code> <i>k</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   **
   ** @param  key                the key whose presence in the process data is
   **                            to be tested.
   **
   ** @return                    <code>true</code> if the process data contains
   **                            a mapping for the specified <code>key</code>.
   */
  public boolean processDataContains(final String key) {
    return processDataContains(this.data, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataContains
  /**
   ** Returns <code>true</code> if the specified collection <code>data</code>
   ** contains a mapping for the specified <code>key</code>.
   ** <p>
   ** More formally, returns <code>true</code> if and only if <code>data</code>
   ** contains a mapping for <code>key</code> <i>k</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   **
   ** @param  data               the {@link Map} providing the mapping to be
   **                            tested.
   ** @param  key                the key whose presence in the the {@link Map}
   **                            is to be tested.
   **
   ** @return                    <code>true</code> if the process data contains
   **                            a mapping for the specified <code>key</code>.
   */
  public boolean processDataContains(final Map<String, Object> data, final String key) {
    return data == null ? false : data.containsKey(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataBoolean
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the process data contains no mapping for the
   ** <code>key</code>.
   ** <p>
   ** More formally, if the process data contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If the process data permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that the process
   ** data contains no mapping for the <code>key</code>; it's also possible that
   ** the map explicitly maps the <code>key</code> to <code>null</code>. The
   ** {@link #processDataContains(String)} operation may be used to distinguish
   ** these two cases.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Boolean processDataBoolean(final String key) {
    return processDataBoolean(this.data, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataBoolean
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the specified collection <code>data</code> contains
   ** no mapping for the <code>key</code>.
   ** <p>
   ** More formally, if <code>data</code> contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If <code>data</code> permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that
   ** <code>data</code> contains no mapping for the <code>key</code>; it's also
   ** possible that <code>data</code> explicitly maps the <code>key</code> to
   ** <code>null</code>. The {@link #processDataContains(Map, String)} operation
   ** may be used to distinguish these two cases.
   **
   ** @param  data               the data mapping to take the value from for
   **                            <code>key</code>.
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Boolean processDataBoolean(final Map<String, Object> data, final String key) {
    return data == null ? null : (Boolean)data.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataInteger
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the process data contains no mapping for the
   ** <code>key</code>.
   ** <p>
   ** More formally, if the process data contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If the process data permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that the process
   ** data contains no mapping for the <code>key</code>; it's also possible that
   ** the map explicitly maps the <code>key</code> to <code>null</code>. The
   ** {@link #processDataContains(String)} operation may be used to distinguish
   ** these two cases.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Integer processDataInteger(final String key) {
    return processDataInteger(this.data, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataInteger
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the specified collection <code>data</code> contains
   ** no mapping for the <code>key</code>.
   ** <p>
   ** More formally, if <code>data</code> contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If <code>data</code> permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that
   ** <code>data</code> contains no mapping for the <code>key</code>; it's also
   ** possible that <code>data</code> explicitly maps the <code>key</code> to
   ** <code>null</code>. The {@link #processDataContains(Map, String)} operation
   ** may be used to distinguish these two cases.
   **
   ** @param  data               the data mapping to take the value from for
   **                            <code>key</code>.
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Integer processDataInteger(final Map<String, Object> data, final String key) {
    return data == null ? null : (Integer)data.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataLong
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the process data contains no mapping for the
   ** <code>key</code>.
   ** <p>
   ** More formally, if the process data contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If the process data permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that the process
   ** data contains no mapping for the <code>key</code>; it's also possible that
   ** the map explicitly maps the <code>key</code> to <code>null</code>. The
   ** {@link #processDataContains(String)} operation may be used to distinguish
   ** these two cases.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Long processDataLong(final String key) {
    return processDataLong(this.data, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataLong
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the specified collection <code>data</code> contains
   ** no mapping for the <code>key</code>.
   ** <p>
   ** More formally, if <code>data</code> contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If <code>data</code> permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that
   ** <code>data</code> contains no mapping for the <code>key</code>; it's also
   ** possible that <code>data</code> explicitly maps the <code>key</code> to
   ** <code>null</code>. The {@link #processDataContains(Map, String)} operation
   ** may be used to distinguish these two cases.
   **
   ** @param  data               the data mapping to take the value from for
   **                            <code>key</code>.
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Long processDataLong(final Map<String, Object> data, final String key) {
    return data == null ? null : (Long)data.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataDate
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the process data contains no mapping for the
   ** <code>key</code>.
   ** <p>
   ** More formally, if the process data contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If the process data permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that the process
   ** data contains no mapping for the <code>key</code>; it's also possible that
   ** the map explicitly maps the <code>key</code> to <code>null</code>. The
   ** {@link #processDataContains(String)} operation may be used to distinguish
   ** these two cases.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Date processDataDate(final String key) {
    return processDataDate(this.data, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataDate
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the specified collection <code>data</code> contains
   ** no mapping for the <code>key</code>.
   ** <p>
   ** More formally, if <code>data</code> contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If <code>data</code> permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that
   ** <code>data</code> contains no mapping for the <code>key</code>; it's also
   ** possible that <code>data</code> explicitly maps the <code>key</code> to
   ** <code>null</code>. The {@link #processDataContains(Map, String)} operation
   ** may be used to distinguish these two cases.
   **
   ** @param  data               the data mapping to take the value from for
   **                            <code>key</code>.
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public Date processDataDate(final Map<String, Object> data, final String key) {
    return data == null ? null : (Date)data.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataString
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the process data contains no mapping for the
   ** <code>key</code>.
   ** <p>
   ** More formally, if the process data contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If the process data permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that the process
   ** data contains no mapping for the <code>key</code>; it's also possible that
   ** the map explicitly maps the <code>key</code> to <code>null</code>. The
   ** {@link #processDataContains(String)} operation may be used to distinguish
   ** these two cases.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public String processDataString(final String key) {
    return processDataString(this.data, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDataString
  /**
   ** Returns the value to which the specified <code>key</code> is mapped, or
   ** <code>null</code> if the specified collection <code>data</code> contains
   ** no mapping for the <code>key</code>.
   ** <p>
   ** More formally, if <code>data</code> contains a mapping from a
   ** <code>key</code> <i>k</i> to a value <i>v</i> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <i>v</i>; otherwise it returns <code>null</code>.
   ** <br>
   ** (There can be at most one such mapping.)
   ** <p>
   ** If <code>data</code> permits <code>null</code> values, then a return value
   ** of <code>null</code> does not <b>necessarily</b> indicate that
   ** <code>data</code> contains no mapping for the <code>key</code>; it's also
   ** possible that <code>data</code> explicitly maps the <code>key</code> to
   ** <code>null</code>. The {@link #processDataContains(Map, String)} operation
   ** may be used to distinguish these two cases.
   **
   ** @param  data               the data mapping to take the value from for
   **                            <code>key</code>.
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>key</code> is mapped, or
   **                            <code>null</code> if the process data contains
   **                            no mapping for the <code>key</code>.
   */
  public String processDataString(final Map<String, Object> data, final String key) {
    return data == null ? null : (String)data.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findEncodedValue
  /**
   ** Resolve the encoded value for the corresponding decoded value from the
   ** specified <code>Lookup Definition</code>.
   **
   ** @param lookupDefinition    the lookup name where all the mapping are
   **                            reconciled
   ** @param decodeValue         the GUI value
   **
   ** @return                    the corresponding encoded value.
   **
   ** @throws TaskException      if the <code>Lookup Definition</code> couldn't
   **                            be found.
   */
  public String findEncodedValue(final String lookupDefinition, final String decodeValue)
    throws TaskException {

    try {
      tcResultSet resultSet = lookupFacade().getLookupValues(lookupDefinition);
      int         rowCount  = resultSet.getRowCount();

      String encoded = SystemConstant.EMPTY;
      for (int i = 0; i < rowCount; i++) {
        resultSet.goToRow(i);
        if (decodeValue.equalsIgnoreCase(resultSet.getStringValue(LookupValue.DECODED))) {
          encoded = resultSet.getStringValue(LookupValue.ENCODED);
          break;
        }
      }
      return encoded;
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findDecodedValue
  /**
   ** Resolve the encoded value for the corresponding decoded value from the
   ** specified <code>Lookup Definition</code>.
   **
   ** @param  lookupDefinition   the lookup name where all the mapping are
   **                            reconciled
   ** @param  encodeValue        the system value.
   **
   ** @return                    the corresponding decoded value.
   **
   ** @throws TaskException      if the <code>Lookup Definition</code> couldn't
   **                            be found.
   */
  public String findDecodedValue(String lookupDefinition, String encodeValue)
    throws TaskException {

    try {
      tcResultSet resultSet = lookupFacade().getLookupValues(lookupDefinition);
      int         rowCount  = resultSet.getRowCount();

      String encoded = SystemConstant.EMPTY;
      for (int i = 0; i < rowCount; i++) {
        resultSet.goToRow(i);
        if (encodeValue.equalsIgnoreCase(resultSet.getStringValue(LookupValue.ENCODED))) {
          encoded = resultSet.getStringValue(LookupValue.DECODED);
          break;
        }
      }
      return encoded;
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from a path.
   **
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final String path)
    throws TaskException {

    final String method = "unmarshal";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create the task descriptor that provides the attribute mapping and
    // transformations to be applied on the mapped attributes
    this.descriptor = new Descriptor(this);

    try {
      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      DescriptorFactory.configure(this.descriptor, document);
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Returns the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  attributeMapping   the name of the <code>Lookup Definition</code>
   **                            specifying the attribute mapping definition of
   **                            fields that are part of a provisioning task.
   ** @param  useColumnName      whether the column names (UD_*) will be used to
   **                            map the values or the logical labels instead.
   **
   ** @return                    a {@link Map} containing the process data
   **                            mapped to the target system attribute names.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Map<String, Object> createProcessData(final Long processInstance, final AttributeMapping attributeMapping, final boolean useColumnName)
    throws TaskException {

    final String method = "createProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Map<String, Object>            processData          = new LinkedHashMap<String, Object>();
    final long                           processKey           = processInstance.longValue();
    final tcFormDefinitionOperationsIntf formDefinitionFacade = this.formDefinitionFacade();
    final tcFormInstanceOperationsIntf   formInstanceFacade   = this.formInstanceFacade();
    try {
      long formDefinition   = formInstanceFacade.getProcessFormDefinitionKey(processKey);
      tcResultSet formField = formDefinitionFacade.getFormFields(formDefinition, formInstanceFacade.getProcessFormVersion(processKey));
      tcResultSet formData  = formInstanceFacade.getProcessFormData(processKey);
      // for each form field definition code check to see whether there is a
      // corresponding User Defined Field in the lookup
      for (int j = 0; j < formField.getRowCount(); j++) {
        formField.goToRow(j);
        final String columnName = formField.getStringValue(FormDefinition.COLUMN_NAME);
        // use the process form UD_ column name or the field label for the
        // compare against the mapping definition
        final String mappingName = useColumnName ? columnName : formField.getStringValue(FormDefinition.COLUMN_LABEL);
        // if there is a match between Forms Definition Column/Label and lookup
        // code value associate the Target System specific attribute with the
        // value of the Process Data
        if (attributeMapping.containsKey(mappingName)) {
          final String attributeValue = formData.getStringValue(columnName);
          // gets the attributes that the request is trying to provision to
          // the Target System. One field can drive more than one attributes
          // but necessarily not all the attributes mapped may not be
          // provisioned.
          StringTokenizer tokenizer = new StringTokenizer(attributeMapping.stringValue(mappingName).trim(), "|");
          while (tokenizer.hasMoreTokens()) {
            // put all attributes in the mapping regardless if they are empty
            // or not.
            processData.put(tokenizer.nextToken().trim(), attributeValue);
          }
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      formDefinitionFacade.close();
      formInstanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return processData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessData
  /**
   ** Returns the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   ** @param  attributeMapping   the name of the <code>Lookup Definition</code>
   **                            specifying the attribute mapping definition of
   **                            fields that are part of a provisioning task.
   ** @param  useColumnName      whether the column names (UD_*) will be used to
   **                            map the values of the logical lables instead.
   **
   ** @return                    a {@link Map} containing the process data mapped
   **                            to the target system attribute names.
   **
   ** @throws TaskException      if the operation fails.
   */
  @Deprecated
  private Map<String, Object> createProcessData(final String processInstance, final String attributeMapping, final boolean useColumnName)
    throws TaskException {

    final String method = "createProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Map<String, Object>            processData          = new LinkedHashMap<String, Object>();
    final long                           processKey           = Long.parseLong(processInstance);
    final tcFormDefinitionOperationsIntf formDefinitionFacade = this.formDefinitionFacade();
    final tcFormInstanceOperationsIntf   formInstanceFacade   = this.formInstanceFacade();
    final tcLookupOperationsIntf         lookupFacade         = this.lookupFacade();
    try {
      long formDefinition   = formInstanceFacade.getProcessFormDefinitionKey(processKey);
      tcResultSet formField = formDefinitionFacade.getFormFields(formDefinition, formInstanceFacade.getProcessFormVersion(processKey));
      tcResultSet needField = lookupFacade.getLookupValues(attributeMapping);

      tcResultSet formData  = formInstanceFacade.getProcessFormData(processKey);
      for (int k = 0; k < needField.getRowCount(); k++) {
        needField.goToRow(k);
        final String needName = needField.getStringValue(LookupValue.ENCODED).trim();
        if (StringUtility.isEmpty(needName))
          continue;

        // for each form field definition code check to see whether there is a
        // corresponding User Defined Field in the lookup
        for (int j = 0; j < formField.getRowCount(); j++) {
          formField.goToRow(j);
          final String columnName = formField.getStringValue(FormDefinition.COLUMN_NAME);
          // use the process form UD_ column name or the field label
          // for the compare against the mapping definition
          final String mappingName = useColumnName ? columnName : formField.getStringValue(FormDefinition.COLUMN_LABEL);
          // if there is a match between Forms Definition Column/Label and
          // lookup code value associate the Target System specific attribute
          // with the value of the Process Data
          if (needName.equalsIgnoreCase(mappingName)) {
            final String attributeValue = formData.getStringValue(columnName);
            // gets the attributes that the request is trying to provision to
            // the Target System. One field can drive more than one attributes
            // but necessarily not all the attributes mapped may not be
            // provisioned.
            StringTokenizer tokenizer = new StringTokenizer(needField.getStringValue(LookupValue.DECODED).trim(), "|");
            while (tokenizer.hasMoreTokens()) {
              // put all attributes in the mapping regardless if they are empty
              // or not.
              processData.put(tokenizer.nextToken().trim(), attributeValue);
            }
          }
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      formDefinitionFacade.close();
      formInstanceFacade.close();
      lookupFacade.close();

      trace(method, SystemMessage.METHOD_EXIT);
    }
    return processData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProcessChildData
  /**
   ** Returns the target system attributes and their corresponding values by
   ** comparing user defined form definitions, lookup values and user defined
   ** form values.
   **
   ** @param  instanceKey        the process instance key
   ** @param  attributeMapping   the Lookup Code containing the target attribute
   **                            mapping.
   ** @param  useColumnName      whether the column names (UD_*) will be used to
   **                            map the values of the logical lables instead.
   **
   ** @return                    a {@link Map} containing the process data mapped
   **                            to the target system attribute names.
   **
   ** @throws TaskException      if the operation fails.
   */
  @SuppressWarnings("unchecked")
  private Map[] createProcessChildData(String instanceKey, String attributeMapping, String childForm, boolean useColumnName)
    throws TaskException {

    final String method = "createProcessChildData";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map<String, String>[]                processData          = null;
    final long                           processKey           = Long.parseLong(instanceKey);
    final tcLookupOperationsIntf         lookupFacade         = this.lookupFacade();
    final tcFormDefinitionOperationsIntf formDefinitionFacade = this.formDefinitionFacade();
    final tcFormInstanceOperationsIntf   formInstanceFacade   = this.formInstanceFacade();
    final long                           childFormKey         = findChildFormKey(processKey, childForm);
    try {
      tcResultSet formField = formDefinitionFacade.getFormFields(childFormKey, findChildFormVersion(processKey, childForm));

      tcResultSet attrField = lookupFacade.getLookupValues(attributeMapping);

      int fieldCount = formField.getRowCount();
      int attrCount  = attrField.getRowCount();

      tcResultSet formData  = formInstanceFacade.getProcessFormChildData(childFormKey, processKey);
      String[]    formName  = formData.getColumnNames();
      processData           = (Map<String, String>[])Array.newInstance(HashMap.class, fieldCount);
      // iterate the process form child table rows
      for (int i = 0; i < fieldCount; i++) {
        formField.goToRow(i);
        processData[i] = new LinkedHashMap<String, String>();
        // for each child column check to see whether there is a corresponding
        // UDF in the mapping
        for (int j = 0; j < attrCount; j++) {
          attrField.goToRow(j);
          // iterate the columns on the form child table row
          for (int k = 0; k < formName.length; k++) {
            final String field = formName[k];
            // if there is a match between child field column and lookup code
            // value
            if (field.equalsIgnoreCase(attrField.getStringValue(LookupValue.ENCODED))) {
              for (int l = 0; l < formData.getRowCount(); l++) {
                formData.goToRow(l);
                final String columnName = formField.getStringValue(FormDefinition.COLUMN_NAME);
                // use the process form UD_ column name or the field label
                // for the compare against the mapping definition
                final String mappingName = useColumnName ? columnName : formField.getStringValue(FormDefinition.COLUMN_LABEL);
                // if there is a match between child field column and child
                // defination column name
                if (field.equalsIgnoreCase(formData.getStringValue(mappingName))) {
                  final String value = formData.getStringValue(field);
                  if (!StringUtility.isEmpty(value)) {
                    processData[i].put(attrField.getStringValue(LookupValue.DECODED), value);
                  }
                }
              }
              break;
            }
          }
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
   finally {
      formDefinitionFacade.close();
      formInstanceFacade.close();
      lookupFacade.close();

      trace(method, SystemMessage.METHOD_EXIT);
    }
    return processData;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findChildFormKey
  /**
   ** Returns the child form definition key for the specified child table.
   **
   ** @param  processKey         the Process Instance Key
   ** @param  childForm          the name of the child form to find
   **
   ** @return                    the Form Definition Key for the specified
   **                            parameter or <code>0</code> if no child form
   **                            exists.
   **
   ** @throws TaskException      if the operation fails.
   */
  private long findChildFormKey(long processKey, String childForm)
    throws TaskException {

    final String method = "findChildFormKey";
    trace(method, SystemMessage.METHOD_ENTRY);

    final tcFormInstanceOperationsIntf   formInstanceFacade   = this.formInstanceFacade();
    try {
      long        formKey    = formInstanceFacade.getProcessFormDefinitionKey(processKey);
      tcResultSet child      = formInstanceFacade.getChildFormDefinition(formKey, formInstanceFacade.getProcessFormVersion(processKey));
      int         childCount = child.getRowCount();
      for (int i = 0; i < childCount; i++) {
        child.goToRow(i);
        if (childForm.equalsIgnoreCase(child.getStringValue(FormDefinition.NAME)))
          return child.getLongValue(FormDefinition.CHILD_KEY);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      formInstanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findChildFormVersion
  /**
   ** Returns the child form version for the specified child table.
   **
   ** @param  processKey         the Process Instance Key
   ** @param  childForm          the name of the child form to find
   **
   ** @return                    the Form Definition Version for the specified
   **                            parameter or <code>0</code> if no child form
   **                            exists.
   **
   ** @throws TaskException      if the operation fails.
   */
  private int findChildFormVersion(long processKey, String childForm)
    throws TaskException {

    final String method = "findChildFormVersion";
    trace(method, SystemMessage.METHOD_ENTRY);

    final tcFormInstanceOperationsIntf   formInstanceFacade   = this.formInstanceFacade();
    try {
      long        formKey    = formInstanceFacade.getProcessFormDefinitionKey(processKey);
      tcResultSet child      = formInstanceFacade.getChildFormDefinition(formKey, formInstanceFacade.getProcessFormVersion(processKey));
      int         childCount = child.getRowCount();
      for (int i = 0; i < childCount; i++) {
        child.goToRow(i);
        if (childForm.equalsIgnoreCase(child.getStringValue(FormDefinition.NAME)))
          return child.getIntValue(FormDefinition.CHILD_VERSION);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      formInstanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return 0;
  }
}