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

    File        :   ResourceHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import Thor.API.Operations.tcObjectOperationsIntf;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeaturePlatformTask;

////////////////////////////////////////////////////////////////////////////////
// class ResourceHandler
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>ResourceHandler</code> creates, deletes and configures a
 ** <code>Resource Object</code> in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for Resource Objects. */
  public static final String PREFIX               = "Objects.";

  /** Standard prefix name for provisioning status. */
  public static final String STATUS_PREFIX         = "Object Status.";

  /** Standard prefix name for Object Instance. */
  public static final String OBJECT_INSTANCE_PREFIX = "Object Instance.";

  /** Standard prefix name for User Object Instance (OIU). */
  public static final String USER_INSTANCE_PREFIX   = "Users-Object Instance For User.";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition system key should be resolved
   */
  public static final String KEY                    = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition name should be resolved
   */
  public static final String NAME                   = PREFIX + FIELD_NAME;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the name of the <code>Resource Object</code> instance to configure or to
   ** create
   */
  private ResourceInstance single;

  /**
   ** the list of the <code>Resource Object</code> instance to configure or to
   ** create
   */
  private List<ResourceInstance> multiple = new ArrayList<ResourceInstance>();

  /**
   ** the business logic layer to operate on <code>Resource Object</code> types
   */
  private tcObjectOperationsIntf facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ResourceHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ResourceHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Resource Object</code> instance of Identity
   ** Manager to handle.
   **
   ** @return                    the name of the <code>Resource Object</code>
   **                            instance in Identity Manager.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the <code>Resource Object</code>
   **                            instance in Identity Manager.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new ResourceInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager <code>IT Resource</code> instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager <code>IT Resource</code>
   **                            instance.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = new ResourceInstance();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager <code>IT Resource</code>
   **                            instance.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    if (this.single == null)
      this.single = new ResourceInstance();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException   in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (!(this.operation() == ServiceOperation.delete))
      try {
        if (this.single != null)
          this.single.validate();

        for (ResourceInstance i : this.multiple)
          i.validate();
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link ResourceInstance} to add.
   **
   ** @throws BuildException     if the specified {@link ResourceInstance} is
   **                            already assigned to this task.
   */
  public void addInstance(final ResourceInstance object)
    throws BuildException {

    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new <code>Resource Object</code> in Identity Manager through the
   ** discovered {@link tcObjectOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcObjectOperationsIntf.class);
    try {
      ;
    }
    finally {
      if (this.facade != null)
        this.facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing <code>Resource Object</code> from Identity Manager
   ** through the given {@link tcObjectOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcObjectOperationsIntf.class);
    try {
      ;
    }
    finally {
      if (this.facade != null)
        this.facade.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Mofifies an existing <code>Resource Object</code> in Identity Manager
   ** through the discovered {@link tcObjectOperationsIntf}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void modify(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(tcObjectOperationsIntf.class);
    try {
      ;
    }
    finally {
      if (this.facade != null)
        this.facade.close();
    }
  }
}