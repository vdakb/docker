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

    File        :   LookupHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcLookupOperationsIntf;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.FeaturePlatformTask;

////////////////////////////////////////////////////////////////////////////////
// class LookupHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>LookupHandler</code> creates,deletes and configures a Lookup
 ** Definitions in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LookupHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for <code>Lookup Definition</code>s. */
  public static final String PREFIX = "Lookup Definition.";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup code key should be mapped
   */
  public static final String FIELD_CODE = "Code";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup type should be mapped
   */
  public static final String FIELD_TYPE = "Type";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup field should be mapped
   */
  public static final String FIELD_FIELD = "Field";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup required property should be mapped
   */
  public static final String FIELD_REQUIRED = "Required";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup group key should be mapped
   */
  public static final String FIELD_GROUP = "Group";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** code key should be mapped
   */
  public static final String FIELD_ENCODED = "Lookup Code Information.Code Key";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** code key should be mapped
   */
  public static final String FIELD_DECODED = "Lookup Code Information.Decode";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** language should be mapped
   */
  public static final String FIELD_LANGUAGE = "Lookup Code Information.Language";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** language county should be mapped
   */
  public static final String FIELD_COUNTRY = "Lookup Code Information.Country";

  /**
   ** the mapping key contained in a collection to specify that the lookup code
   ** key value of the lookup definition should be resolved
   */
  public static final String CODE = PREFIX + FIELD_CODE;

  /**
   ** the mapping key contained in a collection to specify that the lookup type
   ** value of the lookup definition should be resolved
   */
  public static final String TYPE = PREFIX + FIELD_TYPE;

  /**
   ** the mapping key contained in a collection to specify that the lookup field
   ** value of the lookup definition should be resolved
   */
  public static final String FIELD = PREFIX + FIELD_FIELD;

  /**
   ** the mapping key contained in a collection to specify that the lookup
   ** required property of the lookup definition should be resolved
   */
  public static final String REQUIRED = PREFIX + FIELD_REQUIRED;

  /**
   ** the mapping key contained in a collection to specify that the lookup group
   ** key value of the lookup definition should be resolved
   */
  public static final String GROUP = PREFIX + FIELD_GROUP;

  /**
   ** the mapping key contained in a collection to specify that the encoded
   ** value of the lookup definition entry should be resolved
   */
  public static final String ENCODED = PREFIX + FIELD_ENCODED;

  /**
   ** the mapping key contained in a collection to specify that the decoded
   ** value of the lookup definition entry should be resolved
   */
  public static final String DECODED = PREFIX + FIELD_DECODED;

  /**
   ** the mapping key contained in a collection to specify that the language
   ** value of the lookup definition entry should be resolved
   */
  public static final String LANGUAGE = PREFIX + FIELD_LANGUAGE;

  /**
   ** the mapping key contained in a collection to specify that the language
   ** country value of the lookup definition entry should be resolved
   */
  public static final String COUNTRY = PREFIX + FIELD_COUNTRY;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the <code>Lookup Definition</code> to configure or to create */
  private LookupInstance single;

  /** the name of the <code>Lookup Definition</code> to configure or to create */
  private List<LookupInstance> multiple = new ArrayList<LookupInstance>();

  /** the business logic layer to operate on <code>Lookup Definition</code>s */
  private tcLookupOperationsIntf facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>LookupHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public LookupHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Lookup Definition</code> of Identity Manager
   ** to handle.
   **
   ** @return                    the name of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Called to inject the argument for parameter <code>group</code>.
   **
   ** @param  group              the group of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public void group(final String group) {
    if (this.single == null)
      this.single = new LookupInstance();

    this.single.group(group);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Returns the group of the <code>Lookup Definition</code> of Identity
   ** Manager to handle.
   **
   ** @return                    the group of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public final String group() {
    return this.single == null ? null : this.single.group();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   field
  /**
   ** Called to inject the argument for parameter <code>field</code>.
   **
   ** @param  field              the field of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public void field(final String field) {
    if (this.single == null)
      this.single = new LookupInstance();

    this.single.field(field);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   field
  /**
   ** Returns the field of the <code>Lookup Definition</code> of Identity
   ** Manager to handle.
   **
   ** @return                    the field of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public final String field() {
    return this.single == null ? null : this.single.field();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager <code>Lookup Definition</code>.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager
   **                            <code>Lookup Definition</code>.
   **
   ** @throws BuildException    if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = new LookupInstance();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager
   **                            <code>Lookup Definition</code>.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    if (this.single == null)
      this.single = new LookupInstance();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractHandler)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (!(this.operation() == ServiceOperation.delete))
      try {
        if (this.single != null)
          this.single.validate();

        for (LookupInstance i : this.multiple)
          i.validate();
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new LookupInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link LookupInstance} to add.
   **
   ** @throws BuildException     if the specified {@link LookupInstance} is
   **                            already assigned to this task.
   */
  public void addInstance(final LookupInstance object) {
    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>Lookup Definition</code> in Identity Manager through
   ** the discovered {@link tcLookupOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcLookupOperationsIntf.class);
    try {
      if (this.single != null)
        create(this.single);

      for (LookupInstance i : this.multiple)
        create(i);
    }
    finally {
      if (this.facade != null)
        this.facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Lookup Definition</code> in Identity Manager
   ** Server through the discovered {@link tcLookupOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcLookupOperationsIntf.class);
    try {
      if (this.single != null)
        delete(this.single);

      for (LookupInstance i : this.multiple)
        delete(i);
    }
    finally {
      if (this.facade != null)
        this.facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>Lookup Definition</code> in Identity Manager
   ** through the discovered {@link tcLookupOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcLookupOperationsIntf.class);
    try {
      if (this.single != null)
        modify(this.single);

      for (LookupInstance i : this.multiple)
        modify(i);
    }
    finally {
      if (this.facade != null)
        this.facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>Lookup Definition</code> in Identity Manager through
   ** the discovered {@link tcLookupOperationsIntf}.
   **
   ** @param  instance           the {@link LookupInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final LookupInstance instance)
    throws ServiceException {

    final String[] arguments = { CODE, instance.name() };
    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
    if (!exists(instance)) {
      // extends the parameter mapping with the type and name
      try {
        // first create a lookup like it has to be per default this will create
        // a "normal" lookup definition
        this.facade.addLookupCode(instance.name());
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
    else {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
    }
    // extends the parameter mapping with the type and name
    final Map<String, String> attribute = new HashMap<String, String>();
    // unfortunately we facing a bug if we try in the standard way to map
    // the pseudo column code specified Lookup.GROUP
    // A wrong value is mapped in the lookup definition
    // Lookup Definition.Group for the field information. Instead of
    // LKU_GROUP the mispelled value LKU_TYPE_GROUP is specified.
    // Therefore we are mapping the native column name
    attribute.put("LKU_GROUP", instance.group());
    // unfortunately a field lookup which is required for custom lookup
    // queries cannot be created by the API used
    // if a filed lookup has to be to create update the lookup properties
    // accordingly in a secnd step
    if (!StringUtility.isEmpty(instance.field())) {
      attribute.put(TYPE, "f");
      attribute.put(FIELD, instance.field());
      attribute.put(REQUIRED, instance.required() ? "1" : "0");
    }
    try {
      this.facade.updateLookupCode(instance.name(), attribute);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, CODE, instance.name()));
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Lookup Definition</code> of the Identity Manager
   ** through the discovered {@link tcLookupOperationsIntf}.
   **
   ** @param  instance           the {@link LookupInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final LookupInstance instance)
    throws ServiceException {

    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, CODE, instance.name()));
    if (!exists(instance))
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, CODE, instance.name()));
    else
      try {
        this.facade.removeLookupCode(instance.name());
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, CODE, instance.name()));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an existing <code>Lookup Definition</code> of the Identity
   ** Manager through the discovered {@link LookupInstance}.
   **
   ** @param  instance           the {@link LookupInstance} to configure.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final LookupInstance instance)
    throws ServiceException {

    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, CODE, instance.name()));
    if (!exists(instance))
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, CODE, instance.name()));
    else {
      // extends the parameter mapping with the type and name
      final Map<String, String> attribute = new HashMap<String, String>();
      // unfortunately we facing a bug if we try in the standard way to map
      // the pseudo column code specified Lookup.GROUP
      // A wrong value is mapped in the lookup definition
      // Lookup Definition.Group for the field information. Instead of
      // LKU_GROUP the mispelled value LKU_TYPE_GROUP is specified.
      // Therefore we are mapping the native column name
      attribute.put("LKU_GROUP", instance.group());
      // unfortunately a field lookup which is required for custom lookup
      // queries cannot be created by the API used
      // if a filed lookup has to be to create update the lookup properties
      // accordingly in a secnd step
      if (!StringUtility.isEmpty(instance.field())) {
        attribute.put(TYPE, "f");
        attribute.put(FIELD, instance.field());
        attribute.put(REQUIRED, instance.required() ? "1" : "0");
      }
      try {
        this.facade.updateLookupCode(instance.name(), attribute);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, CODE, instance.name()));
      }
      catch (Exception e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns an existing <code>Lookup Definition</code> from Identity Manager
   ** through the discovered {@link tcLookupOperationsIntf}.
   **
   ** @param  instance           the {@link LookupInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the
   **                            {@link LookupInstance} exists in the backend
   **                            system; otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final LookupInstance instance)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(CODE, instance.name());
    try {
      final tcResultSet resultSet = this.facade.findLookupsDetails(filter);
      return (resultSet.getRowCount() == 1);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }
}