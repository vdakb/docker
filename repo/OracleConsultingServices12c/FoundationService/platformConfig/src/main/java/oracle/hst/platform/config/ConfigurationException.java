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

    System      :   Foundation Configuration Extension
    Subsystem   :   Common Shared Utility

    File        :   ConfigurationException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigurationException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.config;

import java.util.Collection;

////////////////////////////////////////////////////////////////////////////////
// class ConfigurationException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Base class for all used exception across configuration tiers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConfigurationException extends Exception {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2102996942975610106")
  private static final long serialVersionUID = 6503945523676435662L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ConfigurationException</code> with the specified detail
   ** message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message           the detail message.
   **                            <br>
   **                            he detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected ConfigurationException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ConfigurationException</code> and passes it the parent
   ** exception.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The detail message associated with <code>causing</code> is <b>not</b>
   ** automatically incorporated in this exception's detail message.
   **
   ** @param  message            the detail message.
   **                            <br>
   **                            he detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected ConfigurationException(final String message, final Throwable cause) {
    // ensure inheritance
    super(message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>ConfigurationException</code> with
   ** the {@link ConfigurationError#UNHANDLED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>ConfigurationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ConfigurationException</code>.
   */
  public static ConfigurationException unhandled(final Throwable causing) {
    return unhandled(causing.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>ConfigurationException</code> with
   ** the {@link ConfigurationError#UNHANDLED} error keyword.
   **
   ** @param  message            the error message for this exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ConfigurationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ConfigurationException</code>.
   */
  public static ConfigurationException unhandled(final String message) {
    return new ConfigurationException(ConfigurationBundle.string(ConfigurationError.UNHANDLED, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>ConfigurationException</code> with
   ** the {@link ConfigurationError#GENERAL} error keyword.
   **
   ** @param  message            the error message for this exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ConfigurationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ConfigurationException</code>.
   */
  public static ConfigurationException general(final String message) {
    return new ConfigurationException(ConfigurationBundle.string(ConfigurationError.GENERAL, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>ConfigurationException</code> with
   ** the {@link ConfigurationError#ABORT} error keyword.
   **
   ** @param  message            the error message for this exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ConfigurationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ConfigurationException</code>.
   */
  public static ConfigurationException abort(final String message) {
    return new ConfigurationException(ConfigurationBundle.string(ConfigurationError.ABORT, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>ConfigurationException</code> with
   ** the specified code keyword.
   **
   ** @param  path               the path to the configuration the
   **                            <code>error</code>(s) detected within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  error              the collection of errors detected.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>ConfigurationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ConfigurationException</code>.
   */

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ConfigurationException</code> from a detailed message
   ** and a causing exception.
   **
   ** @param  message            the error message for this exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>ConfigurationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ConfigurationException</code>.
   */
  public static ConfigurationException build(final String message, final Throwable cause) {
    // ensure inheritance
    return new ConfigurationException(message, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>ConfigurationException</code> with
   ** the specified code keyword.
   **
   ** @param  path               the path to the configuration the
   **                            <code>error</code>(s) detected within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  error              the collection of errors detected.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>ConfigurationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ConfigurationException</code>.
   */
  public static ConfigurationException build(final String path, final Collection<String> error) {
    return new ConfigurationException(format(path, error));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the detailed message of a configuration exception
   **
   ** @param  path               the path to the configuration the
   **                            <code>error</code>(s) detected within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  error              the collection of errors detected.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the fromatted detailed message.
   **                            <br>
   **                            Possible object is <code>String</code>.
   */
  protected static String format(final String path, final Collection<String> errors) {
    final StringBuilder msg = new StringBuilder(ConfigurationBundle.string(errors.size() == 1 ? ConfigurationError.ERROR_SINGLE : ConfigurationError.ERROR_MULTIPLE, path));
    for (String error : errors) {
      msg.append("  * ").append(error).append('\n');
    }
    return msg.toString();
  }
}