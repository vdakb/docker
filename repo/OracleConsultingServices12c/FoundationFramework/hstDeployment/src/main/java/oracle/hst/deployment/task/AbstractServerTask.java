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

    File        :   AbstractServerTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractServerTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractServerTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant JMX mbean tasks.
 ** Implementations of <code>Server</code> inherit its attributes (see below)
 ** for connecting to the JMX MBean server.
 ** <p>
 ** Refer to the user documentation for more information and examples on how to
 ** use this task.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractServerTask extends AbstractServiceTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractServiceTask</code> Ant task that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractServerTask() {
    // ensure inheritance
    super();
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
    return AbstractInvocationHandler.DOMAIN_SERVER;
  }
}