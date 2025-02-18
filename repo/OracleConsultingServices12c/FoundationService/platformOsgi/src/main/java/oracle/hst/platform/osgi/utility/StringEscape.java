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
    Subsystem   :   OSGI Container Interface

    File        :   StringEscape.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StringEscape.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.osgi.utility;

import java.util.List;

import java.io.IOException;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.osgi.function.ExceptionHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class StringEscape
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Miscellaneous collection of string utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class StringEscape {

  //////////////////////////////////////////////////////////////////////////////
  // class Java
  // ~~~~~ ~~~~~~~~~~~~~~~~~
  /**
   ** Miscellaneous collection of string utility methods. Mainly for internal
   ** use.
   */
  public static class Java {

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Java</code>.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Java()" and enforces use of the public method below.
     */
    private Java() {
      // should never be instantiated
      throw new AssertionError();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: wrap
    /**
     ** Converts normal strings to java escaped for double-quotes and wrapped in
     ** those double quotes.
     **
     ** @param  value            the collection of string values to escape.
     **                          <br>
     **                          Allowed object is {@link Iterable} where each
     **                          element is of type {@link String}.
     **
     ** @return                  the escaped string value.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public static List<String> wrap(final Iterable<String> value) {
      final List<String> result = CollectionUtility.list();
      // prevent bogus input
      if (value == null)
        return result;

      for (String cursor : value)
        result.add(wrap(cursor));
      return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: wrap
    /**
     ** Converts normal string to java escaped for double-quotes and wrapped in
     ** those double quotes.
     **
     ** @param  value            the string value to escape.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the escaped string value.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public static String wrap(final String value) {
      final StringBuilder out = new StringBuilder();
      try {
        wrap(value, out);
      }
      catch (IOException e) {
        // shouldn't happen for string builder
        throw ExceptionHandler.propagate(e);
      }
      return out.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: wrap
    /**
     ** Converts normal string to java escaped for double-quotes and wrapped in
     ** those double quotes.
     **
     ** @param  value            the string value to escape.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  output           the {@link Appendable} to collect the output.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public static void wrap(final String value, final Appendable output)
      throws IOException {

      if (value == null) {
        output.append("null");
      }
      else {
        output.append('"');
        escape(value, output);
        output.append('"');
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: escape
    /**
     ** Converts normal string to java escaped for double-quotes (but not
     ** wrapped in double quotes).
     **
     ** @param  value            the string value to escape.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  output           the {@link Appendable} to collect the output.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    public static void escape(final String value, final Appendable output)
      throws IOException {

      for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (c == '\\' || c == '"') {
          // NB do NOT escape single quotes; while valid for java, it is not in JSON (breaks jQuery.parseJSON)
          escape(output, c);
        }
        else if (c == '\n') {
          escape(output, 'n');
        }
        else if (c == '\t') {
          escape(output, 't');
        }
        else if (c == '\r') {
          escape(output, 'r');
        }
        else {
          output.append(c);
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: escape
    /**
     ** Escpapes the character specified.
     **
     ** @param  output           the {@link Appendable} to collect the output.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  c                the character value to escape.
     **                          <br>
     **                          Allowed object is <code>char</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    private static void escape(final Appendable out, final char c)
      throws IOException {

      out.append('\\').append(c);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>StringEscape</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new StringEscape()" and enforces use of the public method below.
   */
  private StringEscape() {
    // should never be instantiated
    throw new AssertionError();
  }
}