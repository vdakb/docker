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
    Subsystem   :   Deployment Utilities

    File        :   ServerControlOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerControlOperation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// class ServerControlOperation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>ServerControlOperation</code> defines the attribute values that can be
 ** passed to a server control operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerControlOperation extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Uses the Node Manager to start a Managed Server.
   */
  private static final String START          = "start";

  /**
   ** Gracefully transitions a server to the SHUTDOWN state.
   ** <p>
   ** The server completes all current work before it shuts down.
   */
  private static final String SHUTDOWN       = "shutdown";

  /**
   ** Immediately transitions a server to the SHUTDOWN  state.
   ** <p>
   ** The server immediately terminates all current work, moves through the
   ** SHUTTING_DOWN state, and ends in the SHUTDOWN  state.
   */
  private static final String SHUTDOWN_FORCE = "forceShutdown";

  /**
   ** Gracefully suspends server to ADMIN state.
   ** <p>
   ** New requests are rejected and inflight work is allowed to complete.
   */
  private static final String SUSPEND        = "suspend";

  /**
   ** Transitions the server from RUNNING to ADMIN state forcefully cancelling
   ** inflight work.
   ** <p>
   ** Work that cannot be cancelled is dropped. Applications are brought into
   ** the admin mode forcefully.
   */
  private static final String SUSPEND_FORCE  = "forceSuspend";

  /**
   **   Uses the Node Manager to start a Managed Server.
   */
  private static final String RESUME         = "resume";

  // the names of the allowed server types in alphabetical order
  private static final String[] registry = {
    START
  , SHUTDOWN
  , SHUTDOWN_FORCE
  , SUSPEND
  , SUSPEND_FORCE
  , RESUME
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerControlOperation</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerControlOperation(){
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the server where a task will connecting to.
   **
   ** @return                    the type of the server where a task will
   **                            connecting to.
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return super.getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   **                            Allowed object is array of {@link String}s.
   */
  @Override
  public String[] getValues() {
    return registry;
  }
}