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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   FeaturePlatformTask.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeaturePlatformTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.task;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.AbstractTask;

import oracle.hst.deployment.type.ODSServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeaturePlatformTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant Unified Directory tasks that invokes
 ** operations on a Directory Server.
 ** <p>
 ** Implementations of  <code>FeaturePlatformTask</code> inherit its attributes
 ** (see below) for connecting to the Directory Server.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FeaturePlatformTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected ODSServerContext context     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>FeaturePlatformTask</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected FeaturePlatformTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>FeaturePlatformTask</code> that use the specified
   ** {@link ODSServerContext} <code>context</code> as the runtime environment.
   **
   ** @param  context            the {@link ODSServerContext} used as the
   **                            runtime environment.
   */
  protected FeaturePlatformTask(final ODSServerContext context) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.context = context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link ODSServerContext} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to a boolean
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link ODSServerContext}.
   */
  public void setContextRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof ODSServerContext)) {
      final String[] parameter = {reference.getRefId(), ODSServerContext.CONTEXT_TYPE, reference.getRefId(), object.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter));
    }
    this.context = (ODSServerContext)object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Return the connection context.
   **
   ** @return                    the connection context.
   */
  public final ODSServerContext context() {
    return this.context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractTask)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException    in case an error does occur.
   */
  @Override
  protected void validate() {
    if (this.context == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.CONTEXT_MANDATORY));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected (AbstractTask)
  /**
   ** Returns the state of connection.
   ** <br>
   ** <b>Note</b>: The connection used in this context isn't the LDAP connection
   ** normaly used. Subclasses must override this method to honor the actual
   ** state of a connection.
   **
   ** @return                    the state of connection.
   **                            Always <code>true</code> in this context.
   */
  @Override
  protected boolean connected() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractTask)
  /**
   ** Establish a connection to the Database server and creates the connection
   ** to the use during task execution.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  protected void connect()
    throws ServiceException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractTask)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  protected void disconnect()
    throws ServiceException {
    
    // intentionally left blank
  }
}