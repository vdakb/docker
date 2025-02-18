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
    Subsystem   :   Common Shared Utility Facility

    File        :   ClassUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ClassUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.List;
import java.util.ArrayList;

import java.util.Iterator;

import oracle.hst.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// class ExceptionUtility
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** A class that provides static utilities for exception handling.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExceptionUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The text separator for the stack trace. */
  private static final String STACK_TRACE_SEPARATOR = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";

  /** The "caused by" element. */
  private static final String STACK_TRACE_CAUSED_BY = "caused by: ";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExceptionUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ExceptionUtility()".
   */
  private ExceptionUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStackTraceStrings
  /**
   ** Returns a list of strings that correspond to the stack trace of a
   ** {@link Exception}.
   **
   ** @param  e                  the {@link Exception}
   **
   ** @return                    a list of strings that correspond to the stack
   **                            trace of an {@link Exception}.
   */
  public static List<String> getStackTraceStrings(final Exception e) {
    return internalStackTrace(e);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printableStackTrace
  /**
   ** Returns a printable form of the stack trace of an {@link Exception}.
   **
   ** @param  e                  the {@link Exception}
   **
   ** @return                    a printable form of the stack trace of an
   **                            {@link Exception}.
   */
  public static String printableStackTrace(final Exception e) {
    final StringBuilder    buffer    = new StringBuilder();
    final Iterator<String> strings   = internalStackTrace(e).iterator();
    String                 separator = SystemConstant.EMPTY;
    while (strings.hasNext()) {
      buffer.append(separator).append(strings.next());
      separator = "\n";
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internalStackTrace
  /**
   ** Returns a list of strings that correspond to the stack trace of a
   ** {@link Throwable}.
   **
   ** @param  t                  the {@link Throwable}
   **
   ** @return                    a list of strings that correspond to the stack
   **                            trace of a {@link Throwable}.
   */
  private static List<String> internalStackTrace(final Throwable t) {
    final List<String> result = new ArrayList<String>();
    result.add(t.toString());
    StackTraceElement[] element = t.getStackTrace();
    for (int i = 0; i < element.length; i++)
      result.add(element[i].toString());

    final Throwable cause = t.getCause();
    if (cause != null) {
      result.add(STACK_TRACE_SEPARATOR + STACK_TRACE_CAUSED_BY);
      result.addAll(internalStackTrace(cause));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internalShortStackTrace
  /**
   ** Returns a list of strings that correspond to the short stack trace of a
   ** {@link Throwable}.
   **
   ** @param  t                  the {@link Throwable}
   ** @param  addPrefix          <code>true</code> to add the "caused by" prefix
   **
   ** @return                    a list of strings that correspond to the stack
   **                            trace of a {@link Throwable}.
   */
  private static List<String> internalShortStackTrace(final Throwable t, final boolean addPrefix) {
    List<String> result = new ArrayList<String>();
    if (addPrefix)
      result.add(STACK_TRACE_CAUSED_BY + t.toString());
    else
      result.add(t.toString());

    final Throwable cause = t.getCause();
    if (cause != null)
      result.addAll(internalShortStackTrace(cause, true));

    return result;
  }
}