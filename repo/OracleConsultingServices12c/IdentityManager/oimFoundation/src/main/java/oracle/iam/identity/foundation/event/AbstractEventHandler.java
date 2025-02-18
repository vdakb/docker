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
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import java.util.Date;

import java.text.DateFormat;

import com.thortech.xl.dataobj.tcDataBase;

import com.thortech.xl.dataobj.util.XLDatabase;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.platform.Platform;

import oracle.iam.platform.kernel.EventFailedException;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.resource.EventBundle;

////////////////////////////////////////////////////////////////////////////////
// class AbstractEventHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractEventHandler</code> provide the basic implementation of
 ** common tasks a process event handler needs.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractEventHandler implements Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String  LOGGER_CATEGORY = "OCS.USR.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String shortName;
  private final Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractEventHandler</code> which use the default
   ** category for logging purpose.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractEventHandler() {
    this(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractEventHandler</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractEventHandler(final String loggerCategory) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.shortName = ClassUtility.shortName(this);
    this.logger    = Logger.create(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   database
  /**
   ** Returns the session provider connection associated with this event
   ** handler.
   ** <p>
   ** This return an instance of {@link tcDataBase} to operate on the API in the
   ** old fashion.
   **
   ** @return                    the session provider connection associated with
   **                            this task.
   */
  public tcDataBase database() {
    return XLDatabase.getInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the session provider connection associated with this task.
   ** <p>
   ** This return an instance of {@link tcDataProvider} to operate on the API in
   ** the old fashion.
   **
   ** @return                    the session provider connection associated with
   **                            this task.
   */
  public tcDataProvider provider() {
    return XLDatabase.getInstance().getDataBase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with this task.
   **
   ** @return                    the logger associated with this task.
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String method, final Throwable what) {
    this.logger.fatal(this.shortName, method, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void error(final String method, final String message) {
    this.logger.error(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String message) {
    this.logger.warn(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String method, final String message) {
    this.logger.warn(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void info(final String message) {
    this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void debug(final String method, final String message) {
    this.logger.debug(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a trace message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void trace(final String method, final String message) {
    this.logger.trace(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   ** @param  what               the exception as the reason to log.
   */
  public final void error(final String method, final String message, final Throwable what) {
    this.logger.error(this.shortName, method, String.format("%s %s", message, what.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   ** @param  what               the exception as the reason to log.
   */
  public final void warning(final String method, final String message, final Throwable what) {
    this.logger.warn(this.shortName, method, String.format("%s %s", message, what.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an error
   ** <code>code</code> which is resolved to a human readable message
   ** leveraging the specified default {@link ListResourceBundle}
   ** {@link EventBundle}.
   **
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the {@link EventFailedException}
   **                            to create belongs to.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final String event, final String code) {
    return eventFailed(null, event, code, EventBundle.RESOURCE, null);  
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an error
   ** <code>code</code> which is resolved to a human readable message
   ** leveraging the specified default {@link ListResourceBundle}
   ** {@link EventBundle}.
   **
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the {@link EventFailedException}
   **                            to create belongs to.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final String event, final String code, final Object... arguments) {
    return eventFailed(null, event, code, EventBundle.RESOURCE, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final String event, final String code, final ListResourceBundle bundle) {
    return eventFailed(null, event, code, bundle, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final String event, final String code, final ListResourceBundle bundle, final Object... arguments) {
    return eventFailed(null, event, code, bundle, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an error
   ** <code>code</code> which is resolved to a human readable message
   ** leveraging the specified default {@link ListResourceBundle}
   ** {@link EventBundle}.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the {@link EventFailedException}
   **                            to create belongs to.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final Throwable cause, final String event, final String code) {
    return eventFailed(cause, event, code, EventBundle.RESOURCE, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final Throwable cause, final String event, final String code, final ListResourceBundle bundle) {
    return eventFailed(cause, event, code, bundle, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an error
   ** <code>code</code> which is resolved to a human readable message
   ** leveraging the specified default {@link ListResourceBundle}
   ** {@link EventBundle}.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the {@link EventFailedException}
   **                            to create belongs to.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final Throwable cause, final String event, final String code, final Object... arguments) {
    return eventFailed(cause, event, code, EventBundle.RESOURCE, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventFailed
  /**
   ** Factory method to create a {@link EventFailedException} from an
   ** error <code>code</code> which is resolved to a human readable message
   ** leveraging the specified {@link ListResourceBundle} <code>bundle</code>.
   **
   ** @param  cause              the  {@link Throwable} the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  event              the name of the evenet causing the
   **                            {@link EventFailedException} to create belongs.
   ** @param  code               the error code the
   **                            {@link EventFailedException} to create
   **                            belongs to.
   ** @param  bundle             the {@link ListResourceBundle} to translate the
   **                            error <code>code</code> to a human readable
   **                            message.
   ** @param  arguments          the substitution arguments for the error
   **                            <code>code</code>.
   **
   ** @return                    a {@link EventFailedException}.
   */
  public static EventFailedException eventFailed(final Throwable cause, final String event, final String code, final ListResourceBundle bundle, final Object... arguments) {
    String message = (arguments == null) ? bundle.getString(code) : bundle.stringFormatted(code, arguments);
    message = errorMessage(message, code, arguments);
    String addition = null;
    if (cause != null) {
      addition = cause.getMessage();
    }

    final EventFailedException throwable = new EventFailedException(code, message, addition, event, cause);

    if (arguments != null) {
      throwable.setErrorData(arguments);
    }
    return throwable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorMessage
  /**
   ** Formatting a error message in the same way as the product itself
   **
   ** @param  message            a localized string forming the human readable
   **                            message.
   ** @param  code               the identifier of the error message.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                    the formatted error message.
   */
  public static String errorMessage(final String message, final String code, final Object... arguments) {
    // Request group wants this format
    // EVT-3050050:Mandatory attributes First Name, Last Name are missing:First Name, Last Name

    final StringBuilder builder = new StringBuilder();
    builder.append(code).append(":").append(message).append(":");

    if (arguments != null) {
      int count = 0;
      for (Object cursor : arguments) {
        if (cursor instanceof Object[]) {
          Object[] errors = (Object[])cursor;
          int      count1 = 0;
          for (Object error : errors) {
            builder.append((error instanceof Date ? DateFormat.getInstance().format(error) : error));
            count1++;
            if (count1 < errors.length) {
              builder.append(",");
            }
          }
        }
        else
          builder.append((cursor instanceof Date ? DateFormat.getInstance().format(cursor) : cursor));
        count++;
        if (count < arguments.length) {
          builder.append(":");
        }
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns an instance of a Business Facade by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  <T>                the expected class type.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   */
  protected static final <T> T service(final Class<T> serviceClass) {
    return Platform.getService(serviceClass);
  }
}