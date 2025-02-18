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

    File        :   AbstractValidationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractValidationHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import java.util.HashMap;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.platform.kernel.ValidationFailedException;

import oracle.iam.platform.kernel.spi.ValidationHandler;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.resource.EventBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractValidationHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractValidationHandler</code> provide the basic implementation
 ** of common tasks a validation handler needs.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractValidationHandler extends    AbstractProcessHandler
                                                implements ValidationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String  LOGGER_CATEGORY = "OCS.OIM.VALIDATION";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractValidationHandler</code> which use the default
   ** category for logging purpose.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractValidationHandler() {
    // ensure inheritance
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractValidationHandler</code> which use the
   ** specified category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  public AbstractValidationHandler(final String loggerCategory) {
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
   **                            {@link AbstractEventHandler} obtained from
   **                            the descriptor and send by the Orchestration.
   */
  @Override
  public void initialize(final HashMap<String, String> parameter) {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified default {@link ListResourceBundle}
   ** {@link EventBundle}.
   **
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final String code) {
    return validationFailed(code, EventBundle.RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified default {@link ListResourceBundle}
   ** {@link EventBundle}.
   **
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final String code, final Object... arguments) {
    return validationFailed(null, code, EventBundle.RESOURCE, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final String code, final ListResourceBundle bundle) {
    return validationFailed(code, bundle, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final String code, final ListResourceBundle bundle, final Object... arguments) {
    return validationFailed(null, code, bundle, arguments);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the default {@link ListResourceBundle} {@link EventBundle}.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final Throwable cause, final String code) {
    return validationFailed(cause, code, EventBundle.RESOURCE, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the default {@link ListResourceBundle} {@link EventBundle}.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final Throwable cause, final String code, final Object... arguments) {
    return validationFailed(cause, code, EventBundle.RESOURCE, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final Throwable cause, final String code, final ListResourceBundle bundle) {
    return validationFailed(cause, code, bundle, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validationFailed
  /**
   ** Factory method to create a {@link ValidationFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  code               the error code the
   **                            {@link ValidationFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link ValidationFailedException}.
   */
  public static ValidationFailedException validationFailed(final Throwable cause, final String code, ListResourceBundle bundle, final Object... arguments) {
    String message = (arguments == null) ? bundle.getString(code) : bundle.stringFormatted(code, arguments);
    message = errorMessage(message, code, arguments);
    ValidationFailedException throwable = (cause == null) ? new ValidationFailedException(message) : new ValidationFailedException(message, cause);
    throwable.setErrorCode(code);
    if (arguments != null) {
      throwable.setErrorData(arguments);
    }
    return throwable;
  }
}