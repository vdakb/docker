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

    File        :   AbstractHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Map;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.ServiceProvider;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractHandler</code> defines basic functionality an object handler
 ** may perform any deployment objects.
 ** <p>
 ** Such functionality might be a create, delete or configure action on an
 ** object peformed before, after or during a deployment operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractHandler extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the key contained in a collection to specify that the system key should be
   ** mapped.
   */
  public static final String   FIELD_KEY  = "Key";

  /**
   ** the reconciliation key contained in a collection to specify that the role
   ** name should be mapped
   */
  public static final String   FIELD_NAME = "Name";

  /**
   ** the reconciliation key contained in a collection to specify that the role
   ** name should be mapped
   */
  public static final String   FIELD_TYPE = "Type";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the operation executed by this task */
  private ServiceOperation    operation;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AbstractHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public AbstractHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name context to handle in Oracle Identity
   **                            Manager.
   */
  public abstract void name(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Called to inject the argument for parameter <code>operation</code>.
   **
   ** @param  operation          the operation to execute against Oracle
   **                            WebLogic Domain entity instance.
   */
  public void operation(final ServiceOperation operation) {
    if (operation == null)
      handleAttributeMissing("operation");

    this.operation = operation;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns the operation to execute against Oracle WebLogic Domain entity
   ** instance.
   **
   ** @return                    the operation to execute against Oracle
   **                            WebLogic Domain entity instance.
   */
  public final ServiceOperation operation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    if (this.operation == null)
      handleAttributeMissing("operation");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Oracle
   **                            WebLogic Domain entity instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Oracle WebLogic Domain entity instance.
   **
   ** @throws BuildException     if the specified value pair is already part of
   **                            the parameter mapping.
   */
  public abstract void addParameter(final String name, final Object value)
    throws BuildException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Oracle WebLogic Domain entity instance.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that already part of the parameter
   **                            mapping.
   */
  public abstract void addParameter(final Map<String, Object> parameter)
    throws BuildException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwTaskAttributeMissing
  /**
   ** Creates and throws a {@link BuildException} about an attribute is missing.
   **
   ** @param  attributeName      the name of the missing attribute.
   **
   ** @throws BuildException     always.
   */
  protected void throwTaskAttributeMissing(final String attributeName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ATTRIBUTE_MISSING, attributeName));
  }
}