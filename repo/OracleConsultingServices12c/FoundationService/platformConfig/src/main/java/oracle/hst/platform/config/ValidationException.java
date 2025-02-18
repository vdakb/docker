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

    File        :   ValidationException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ValidationException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.config;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.SortedSet;

import javax.validation.ConstraintViolation;

////////////////////////////////////////////////////////////////////////////////
// class ValidationException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** An exception thrown where there is an error validating a configuration
 ** object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ValidationException extends ConfigurationException {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ValidationException</code> with the specified detail
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
  private ValidationException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>ValidationException</code> with
   ** the specified code keyword.
   **
   ** @param  <T>                the type of constraint violation.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the configuration the
   **                            <code>error</code>(s) detected within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  violation          the collection of constraint violations
   **                            detected.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link ConstraintViolation}.
   **
   ** @return                    the <code>ValidationException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ValidationException</code>.
   */
  public static <T> ValidationException build(final String path, final Set<ConstraintViolation<T>> violation) {
    return new ValidationException(format(path, violation));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the detailed message of a configuration exception
   **
   ** @param  <T>                the type of constraint violation.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the configuration the
   **                            <code>error</code>(s) detected within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  violation          the collection of constraint violations
   **                            detected.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link ConstraintViolation}.
   **
   ** @return                    the fromatted detailed message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected static <T> String format(final String path, final Set<ConstraintViolation<T>> violation) {
    final StringBuilder msg = new StringBuilder(ConfigurationBundle.string(violation.size() == 1 ? ConfigurationError.SINGLE : ConfigurationError.MULTIPLE, path));
    final SortedSet<String> errors = new TreeSet<>();
    for (ConstraintViolation<?> cursor : violation) {
      errors.add(format(cursor));
    }
    return msg.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Converts a particular {@link ConstraintViolation} to a proper string.
   **
   ** @param  <T>                the type of constraint violation.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  violation          the {@link ConstraintViolation} to transform.
   **                            <br>
   **                            Allowed object is {@link ConstraintViolation}.
   **
   ** @return                    the string representation of the specified.
   **                            {@link ConstraintViolation}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static <T> String format(final ConstraintViolation<T> violation) {
    return (violation.getConstraintDescriptor().getAnnotation() instanceof ValidationMethod) ? violation.getMessage() : String.format("%s %s", violation.getPropertyPath(), violation.getMessage());
  }
}
