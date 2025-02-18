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

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   AbstractPreProcessHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractPreProcessHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import java.util.HashMap;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.platform.kernel.spi.PreProcessHandler;

import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractPreProcessHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractPreProcessHandler</code> provide the basic implementation
 ** of common tasks a pre-process handler needs.
 ** <p>
 ** This class implements the interface {@link PreProcessHandler}
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractPreProcessHandler extends    AbstractProcessHandler
                                                implements PreProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractPreProcessHandler</code> which use the default
   ** category for logging purpose.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractPreProcessHandler() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractPreProcessHandler</code> which use the
   ** specified category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractPreProcessHandler(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (EventHandler)
  /**
   ** Called during creation of the orchestration engine at server startup.
   **
   ** @param  parameter          the parameter mapping passed to the
   **                            {@link AbstractProcessHandler} obtained from
   **                            the descriptor and send by the Orchestration.
   */
  @Override
  public void initialize(final HashMap<String, String> parameter) {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancel (Cancellable)
  /**
   ** Method containing the logic that need to be executed if the orchestration
   ** is cancelled.
   ** <p>
   ** This would be called only for asynchronous actions.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   **
   ** @return                    the outcome of the event handler.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public boolean cancel(final long processId, final long eventId, final AbstractGenericOrchestration orchestration) {
    // intentionally returns false
		return false;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compensate (Compensable)
  /**
   ** Method containing the logic that need to be executed to compensate the
   ** changes made by current event handler if the orchestration fails.
   ** <p>
   ** This method is allways called by the plugin framework if something goes
   ** wrong.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   */
  @Override
  public void compensate(final long processId, final long eventId, final AbstractGenericOrchestration orchestration) {
    // intentionally left blank
  }
}