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

    File        :   ApplicationInvokerTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationInvokerTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.task;

import java.util.Set;
import java.util.HashSet;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.deployment.task.AbstractInvokerTask;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class ApplicationInvokerTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Application Runtime JMX Bean to maintain configuration artifacts
 ** in Oracle Access Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ApplicationInvokerTask extends AbstractInvokerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  APPLICATION = "oam_admin";
  public static final String  VERSION     = "11.1.2.0.0";
  private static final String BEAN        = "com.oracle.oam:Application=%s,ApplicationVersion=%s,name=%s,type=%s";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String> flatten     = new HashSet<String>();
  private ObjectName          name        = null;
  private final Object[]      property    = {
    APPLICATION
  , VERSION
  , SystemConstant.EMPTY
  , SystemConstant.EMPTY
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationInvokerTask</code> Ant task which will gain
   ** access to a certain JMX bean.
   **
   ** @param  name               the name of the JMX bean.
   ** @param  type               the type of the JMX bean.
   */
  protected ApplicationInvokerTask(final String name, final String type) {
    // ensure inheritance
    super();

    // initiallize instance attributes
    this.property[2] = name;
    this.property[3] = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setApplication
  /**
   ** Sets the name of the application the runtime server has to connect to.
   **
   ** @param  application        the name of the application the runtime server
   **                            has to connect to.
   */
  public final void setApplication(final String application) {
    this.property[0] = application;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name        = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the name of the application the runtime server has to connect to.
   **
   ** @return                    the name of the application the runtime server
   **                            has to connect to.
   */
  protected final String application() {
    return this.property[0].toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Sets the version of the application the runtime server has to connect to.
   **
   ** @param  version            the version of the application the runtime
   **                            server has to connect to.
   */
  public final void setVersion(final String version) {
    this.property[1] = version;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name        = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the version of the application the runtime server has to connect
   ** to.
   **
   ** @return                    the version of the application the runtime
   **                            server has to connect to.
   */
  protected final String version() {
    return this.property[1].toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runtimeService (AbstactServiceTask)
  /**
   ** Returns the name of the runtime service to connect to.
   **
   ** @return                    the name of the runtime service to connect to.
   */
  @Override
  protected final String runtimeService() {
    return AbstractInvocationHandler.RUNTIME_SERVER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvokerTask)
  /**
   ** Returns the JMX {@link ObjectName} this task will execute.
   **
   ** @return                    the JMX {@link ObjectName} this task will
   **                            execute.
   */
  @Override
  protected final ObjectName objectName() {
    if (this.name == null) {
      synchronized(BEAN) {
        try {
          this.name = new ObjectName(String.format(BEAN, this.property));
        }
        catch (MalformedObjectNameException e) {
          throw new AssertionError(e.getMessage());
        }
      }
    }
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter (AbstractInvokerTask)
  /**
   ** Returns operation's parameter string.and signature arrays.
   **
   ** @return                    the operation's parameter string.
   */
  @Override
  protected final Object[] parameter() {
    // intentionally left blank
    throw new BuildException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation (AbstractInvokerTask)
  /**
   ** Returns the <code>operation</code> name this task will execute.
   **
   ** @return                    the <code>operation</code> name this task will
   **                            execute.
   */
  @Override
  protected final String operation() {
    // intentionally left blank
    throw new BuildException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signature (AbstractInvokerTask)
  /**
   ** Returns operation's signature arrays.
   **
   ** @return                    the operation's signature arrays.
   */
    //
  @Override
  protected  String[] signature() {
    // intentionally left blank
    throw new BuildException();
  }
}