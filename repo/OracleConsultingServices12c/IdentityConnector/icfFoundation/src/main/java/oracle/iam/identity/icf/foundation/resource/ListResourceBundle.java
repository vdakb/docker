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

    File        :   ListResourceBundle.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResourceBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.resource;

////////////////////////////////////////////////////////////////////////////////
// abstract class ListResourceBundle
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>ListResourceBundle</code> is an abstract subclass of
 ** {@link java.util.ListResourceBundle} that manages locale-dependent
 ** resources.
 ** <p>
 ** Subclasses must override getContents and provide an array, where each item
 ** in the array is a pair of objects. The first element of each pair is the
 ** key, which must be a String, and the second element is the value associated
 ** with that key.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ListResourceBundle extends    java.util.ListResourceBundle
                                         implements org.identityconnectors.framework.common.objects.ConnectorMessages {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String LOCATION = "%s::%s";
  private static final String MESSAGE  = "%s: %s";

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format (ConnectorMessages)
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle} .
   ** <p>
   ** This will substitute "%n$s" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param                     key the key for the desired string pattern.
   ** @param  arguments          the array of substitution arguments.
   **
   ** @return                    the formatted String resource.
   */
  public String format(final String key, final String defaultMessage, final Object... arguments) {
    return stringFormat(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing the class and method name.
   **
   ** @param  thread             the current thread {@link Thread} requiring the
   **                            trace.
   ** @param  clazz              the {@link Class} of the class requiring the
   **                            trace.
   ** @param  methodName         the methode where the request occurred.
   **
   ** @return                    a formatted string containing
   **                            <code>thread</code>, <code>clazz</code> and
   **                            <code>methodName</code>.
   */
  public static String location(final Thread thread, final Class<?> clazz, final String methodName) {
    return location(thread, clazz.getSimpleName(), methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing the class and method name.
   **
   ** @param  thread             the current thread {@link Thread} requiring the
   **                            trace.
   ** @param  location           the location where the request occurred.
   ** @param  methodName         the methode where the request occurred.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   */
  public static String location(final Thread thread, final String location, final String methodName) {
    return String.format("%s::%s-%s", thread.getName(), location, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing the class and method name.
   **
   ** @param  clazz              the {@link Class} of the class requiring the
   **                            trace.
   ** @param  methodName         the methode where the request occurred.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   */
  public static String location(final Class<?> clazz, final String methodName) {
    return location(clazz.getSimpleName(), methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing the class and method name.
   **
   ** @param  className          the name of the class requiring the trace
   ** @param  methodName         the methode where the request occurred.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   */
  public static String location(final String className, final String methodName) {
    return String.format(LOCATION, className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Builds the message from a provided location and the specified
   ** {@link Throwable}.
   **
   ** @param  location           the location where the request occurred.
   ** @param  message            the message to log.
   **
   ** @return                    a formatted string containing
   **                            <code>location</code> and <code>message</code>.
   */
  public static String message(final String location, final String message) {
    return String.format(MESSAGE, location, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Builds an application message.
   **
   ** @param  clazz              the class requiring the trace
   ** @param  method             the methode where the request occurred.
   ** @param  message            the message to log.
   **
   ** @return                    a formatted string containing
   **                            <code>clazz</code>, <code>method</code> and
   **                            <code>message</code>.
   */
  public static String message(final String clazz, final String method, final String message) {
    return message(location(clazz, method), message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   character
  /**
   ** Returns the first character of the String associated with
   ** <code>key</code>.
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the first character of the String associated
   **                            with <code>key</code>.
   */
  public char character(final String key) {
    final String tmp = getString(key);
    return (tmp == null || tmp.trim().length() == 0) ? '\0' : tmp.charAt(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle} .
   ** <p>
   ** This will substitute "%n$s" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param                     key the key for the desired string pattern.
   ** @param  arguments          the array of substitution arguments.
   **
   ** @return                    the formatted String resource.
   */
  public String stringFormat(final String key, final Object... arguments) {
    int count = arguments == null ? 0 : arguments.length;
    if (count == 0)
      return this.getString(key);

    for (int i = 0; i < count; ++i) {
      if (arguments[i] == null)
        arguments[i] = "(null)";
    }
    final String result = String.format(this.internalString(key), arguments);
    for (int i = 0; i < count; ++i) {
      if (arguments[i] == "(null)")
        arguments[i] = null;
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internalString
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                 the key for the desired string.
   **
   ** @return                     the object for the given key.
   */
  protected final String internalString(final String key) {
    return this.getObject(key).toString();
  }
}