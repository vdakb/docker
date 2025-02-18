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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Loggable.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Loggable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.logging;

////////////////////////////////////////////////////////////////////////////////
// interface Loggable
// ~~~~~~~~~ ~~~~~~~~
/**
 ** The <code>Loggable</code> desclares the base functionality of a loggable
 ** service end point.
 **
 ** @param  <T>                  the type of the loggable implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              implementing this interface (loggables can
 **                              return their own specific type instead of type
 **                              defined by this interface only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Loggable<T extends Loggable> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Constant used to demarcate method entry/exit etc. */
  static final String METHOD_ENTRY = "entry";
  static final String METHOD_EXIT  = "exit";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap
  /**
   ** Returns the core {@link java.util.logging.Logger} wrapped by this
   ** <code>Logger</code>.
   **
   ** @return                    the core {@link java.util.logging.Logger}
   **                            wrapped by this <code>Logger</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link java.util.logging.Logger}.
   */
  java.util.logging.Logger unwrap();

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
   **                            <br>
   **                            Possible object is {@link Logger}.
   */
  Logger logger();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to an associated {@link Logger}.
   **
   ** @param  location           the location where the logging event occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  what               the exception as the reason to log.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>Loggable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Loggable</code> of
   **                            type <code>T</code>.
   */
  Loggable<T> fatal(final String location, final Throwable what);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error to an associated {@link Logger}.
   **
   ** @param  location           the location where the logging event occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Loggable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Loggable</code> of
   **                            type <code>T</code>.
   */
  Loggable<T> error(final String location, final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Logs an warning error to the associated {@link Logger}.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Loggable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Loggable</code> of
   **                            type <code>T</code>.
   */
  Loggable<T> warning(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Logs an warning error to the associated {@link Logger}.
   **
   ** @param  location           the location where the logging event occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            what is the reason to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Loggable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Loggable</code> of
   **                            type <code>T</code>.
   */
  Loggable<T> warning(final String location, final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to an associated {@link Logger}.
   **
   ** @param  message            what is the reason to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Loggable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Loggable</code> of
   **                            type <code>T</code>.
   */
  Loggable<T> info(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to an associated {@link Logger}.
   **
   ** @param  location           the location where the logging event occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            what is the reason to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Loggable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Loggable</code> of
   **                            type <code>T</code>.
   */
  Loggable<T> debug(final String location, final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a trace message to an associated {@link Logger}.
   **
   ** @param  location           the location where the logging event occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            what is the reason to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Loggable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Loggable</code> of
   **                            type <code>T</code>.
   */
  Loggable<T> trace(final String location, final String message);
}