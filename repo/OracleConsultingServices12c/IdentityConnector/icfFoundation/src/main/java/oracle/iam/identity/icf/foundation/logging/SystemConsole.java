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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   SystemConsole.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemConsole.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

////////////////////////////////////////////////////////////////////////////////
// class SystemConsole
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A convenient implementation for testing and debugging purpose.
 ** <p>
 ** This implementation publishes log records to <code>System.err</code>. By
 ** default a <code>SimpleFormatter</code> is used to generate brief summaries.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class SystemConsole extends AbstractLogger {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Console
  // ~~~~~ ~~~~~~~
  /**
   ** A logger that writes all to the console.
   */
  private static class Console extends Logger {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Logger</code> for the specified logging category.
     **
     ** @param  category         the category for the Logger.
     */
    private Console(final String category) {
      // ensure inheritance
      super(category);

      // create an appropriate handler with a nice formatter
      ConsoleHandler handler = new ConsoleHandler();
      handler.setFormatter(new SimpleFormatter());
      handler.setLevel(Logger.TRACE);

      // configure the logger
      this.delegate.addHandler(handler);
      this.delegate.setLevel(Logger.TRACE);
      this.delegate.setUseParentHandlers(false);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SystemConsole</code> which use the specified category
   ** for logging purpose.
   **
   ** @param  category           the category for the Logger.
   */
  public SystemConsole(final String category) {
    // ensure inheritance
    super(new Console(category));
  }
}