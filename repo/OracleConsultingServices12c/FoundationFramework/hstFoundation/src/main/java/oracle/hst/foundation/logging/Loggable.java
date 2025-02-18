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

    System      :   Foundation Shared Library
    Subsystem   :   Common shared logging facilities

    File        :   Loggable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Loggable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.logging;

////////////////////////////////////////////////////////////////////////////////
// interface Loggable
// ~~~~~~~~~ ~~~~~~~~
/**
 ** The <code>Loggable</code> desclares the base functionality of a loggable
 ** service end point.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Returns the logger associated with this instance.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   **
   ** @return                    the logger associated with this instance.
   */
  Logger logger();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to an associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   ** @param  what               the exception as the reason to log.
   */
  void fatal(final String location, final Throwable what);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error to an associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   ** @param  message            the message to log.
   */
  void error(final String location, final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
  ** @param  message             the message to log.
   */
  void warning(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   ** @param  message            what is the reason to log.
   */
  void warning(final String location, final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to an associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  void info(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to an associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   ** @param  message            the message to log.
   */
  void debug(final String location, final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a trace message to an associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   ** @param  message            the message to log.
   */
  void trace(final String location, final String message);
}