/*
                         Copyright © 2023 Red.Security

    Licensed under the MIT License (the "License"); you may not use this file
    except in compliance with the License. You may obtain a copy of the License
    at

                      https://opensource.org/licenses/MIT

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to
    deal in the Software without restriction, including without limitation the
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
    sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
    IN THE SOFTWARE.

    ----------------------------------------------------------------------------

    System      :   Platform Service Extension
    Subsystem   :   Common Shared Utility Library

    File        :   ResourceBundleProvider.java

    Compiler    :   Java Developer Kit 8

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    ResourceBundleProvider.


    Revisions   Date        Editor                    Comment
    -----------+-----------+-------------------------+--------------------------
    1.0.0.0     2023-28-06  dieter.steding@icloud.com First release version
*/

package redsecurity.platform.core.utility;

import java.lang.ref.SoftReference;

import java.util.Locale;
import java.util.ResourceBundle;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

////////////////////////////////////////////////////////////////////////////////
// interface ResourceBundleProvider
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>ResourceBundleProvider</code> is a service provider interface for
 ** resource bundles.
 ** <br>
 ** It is used by {@link ResourceBundle#getBundle(String) ResourceBundle.getBundle}
 ** factory methods to locate and load the service providers that are deployed
 ** as modules via {@link java.util.ServiceLoader ServiceLoader}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ResourceBundleProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Locale   DEFAULT  = Locale.ENGLISH;

  static final String   LOCATION = "%s::%s";
  static final String   MESSAGE  = "%s: %s";

  /**
   ** The cache is a map from hash keys (with bundle base name and, locale to
   ** either a resource bundle or NONEXISTENT_BUNDLE wrapped by a Reference.
   ** <p>
   ** The cache is a ConcurrentMap, allowing the cache to be searched
   ** concurrently by multiple threads. This will also allow the hash keys to be
   ** reclaimed along with the ClassLoaders they reference.
   */
  static final ConcurrentMap<Hash, Reference> CACHE = new ConcurrentHashMap<Hash, Reference>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Hash
  // ~~~~~ ~~~~
  /**
   ** Key used for cached resource bundles.
   ** <br>
   ** The key checks the base name and the locale to determine if the resource
   ** is a match to the requested one.
   */
  static class Hash {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final String clazz;
    final Locale locale;
    // hash code value cache to avoid recalculating the hash code of this
    // instance.
    private int  hash;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Hash</code> key for the given name and
     ** {@link Locale}.
     */
    Hash(final String clazz, final Locale locale) {
      this.clazz  = clazz;
      this.locale = locale;
      this.hash   = this.clazz.hashCode() << 3;
      this.hash  ^= this.locale.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public final int hashCode() {
      return this.hash;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Returns whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Hash</code>es are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Hash</code>es may be different even though they contain the same
     ** set of names with the same values, but in a different order.
     **
     ** @param  other            the reference object with which to compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Hash that = (Hash)other;
      return (this.hash  == that.hash);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface HashReference
  // ~~~~~~~~~ ~~~~~~~~~~~~~
  /**
   ** The common interface to get a {@link Hash} in {@link Reference}.
   */
  static interface HashReference {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hash
    Hash hash();
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Reference
  // ~~~~~ ~~~~~~~~~
  /**
   ** References to bundles are soft references so that they can be garbage
   ** collected when they have no hard references.
   */
  static class Reference extends    SoftReference<ResourceBundleProvider>
                         implements HashReference {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Hash hash;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Reference</code> to a {@link ResourceBundleProvider}.
     */
    Reference(final ResourceBundleProvider referent, final Hash hash) {
      // ensure inheritance
      super(referent);

      // initialize instance attributes
      this.hash = hash;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implementd interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Methode: hash (HashReference)
    /**
     ** Returns the hash code to reference.
     **
     ** @return                  the hash code to reference.
     */
    @Override
    public final Hash hash() {
      return this.hash;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a <code>ResourceBundleProvider</code> for the specified base name
   ** and {@link Locale} from the cache.
   **
   ** @param  base               the base name of the resource bundle.
   ** @param  locale             the {@link Locale} for which a resource bundle
   **                            is desired.
   **
   ** @return                    a resource bundle provider for base name and
   **                            {@link Locale}
   */
  static ResourceBundleProvider lookup(final String base, final Locale locale) {
    return lookup(new Hash(base, locale == null ? DEFAULT : locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a <code>ResourceBundleProvider</code> for the {@link Hash} value.
   **
   ** @param  value              the {@link Hash} for which a resource bundle
   **                            is desired.
   **
   ** @return                    a resource bundle provider the {@link Hash}
   **                            value.
   */
  static ResourceBundleProvider lookup(final Hash value) {
    // quick lookup of the cache.
    final Reference r  = CACHE.get(value);
    return (r != null)  ? r.get() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing name of <code>clazz</code> and
   ** <code>methodName</code>.
   **
   ** @param  clazz              the {@link Class} of the class requiring the
   **                            trace.
   ** @param  methodName         the methode where the request occurred.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   */
  static String location(final Class<?> clazz, final String methodName) {
    return location(clazz.getName(), methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing <code>className</code> and
   ** <code>methodName</code>.
   **
   ** @param  className          the name of the class requiring the trace.
   ** @param  methodName         the methode where the request occurred.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   */
  static String location(final String className, final String methodName) {
    return format(LOCATION, className, methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Builds the message from a provided <code>location</code> and the specified
   ** <code>message</code> for the specified <code>clazz</code>.
   **
   ** @param  clazz              the class requiring the trace.
   ** @param  method             the methode where the request occurred.
   ** @param  message            the message to log.
   **
   ** @return                    a formatted string containing
   **                            <code>clazz</code>, <code>method</code> and
   **                            <code>message</code>.
   */
  static String message(final Class<?> clazz, final String method, final String message) {
    return message(location(clazz, method), message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Builds the message from a provided <code>location</code> and the specified
   ** <code>message</code> for the specified <code>clazz</code>.
   **
   ** @param  clazz              the class requiring the trace.
   ** @param  method             the methode where the request occurred.
   ** @param  message            the message to log.
   **
   ** @return                    a formatted string containing
   **                            <code>clazz</code>, <code>method</code> and
   **                            <code>message</code>.
   */
  static String message(final String clazz, final String method, final String message) {
    return message(location(clazz, method), message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Builds the message from a provided <code>location</code> and the specified
   ** <code>message</code>.
   **
   ** @param  location           the location where the request occurred.
   ** @param  message            the message to log.
   **
   ** @return                    a formatted string containing
   **                            <code>location</code> and <code>message</code>.
   */
  static String message(final String location, final String message) {
    return format(MESSAGE, location, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Returns a formatted string using the specified format string and
   ** arguments.
   ** <p>
   ** This will substitute <code>%n$s</code> occurrences in the string resource
   ** with the appropriate parameter <code>n</code> from the array.
   **
   ** @param  format             a <a href="../util/Formatter.html#syntax">format string</a>
   ** @param  arguments          the arguments referenced by the format
   **                            specifiers in the format string. If there are
   **                            more arguments than format specifiers, the
   **                            extra arguments are ignored. The number of
   **                            arguments is variable and may be zero.
   **                            <br>
   **                            The maximum number of arguments is limited by
   **                            the maximum dimension of a Java array as
   **                            defined by <cite>The Java&trade; Virtual Machine Specification</cite>.
   **                            <br>
   **                            The behaviour on a <code>null</code> argument
   **                            depends on the <a href="../util/Formatter.html#syntax">conversion</a>.
   **
   ** @return                    the formatted String resource
   */
  static String format(final String format, final Object... arguments) {
    final int count = arguments == null ? 0 : arguments.length;
    for (int i = 0; i < count; ++i) {
      if (arguments[i] == null)
        arguments[i] = "(null)";
    }
    return String.format(format, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a {@link String} from this <code>ResourceBundleProvider</code>.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                the key into the resource array.
   **
   ** @return                    the string resource.
   */
  String string(final String key);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a {@link String} from this <code>ResourceBundleProvider</code>.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                the key into the resource array.
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   **
   ** @return                    the string resource.
   */
  String string(final String key, final Locale locale);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a formatted {@link String} resource from this
   ** <code>ResourceBundleProvider</code>.
   ** <p>
   ** This will substitute <code>%n$s</code> occurrences in the string resource
   ** with the appropriate parameter <code>n</code> from the array.
   **
   ** @param  key                the key for the desired string format
   ** @param  arguments          the array of substitution arguments.
   **
   ** @return                    the formatted String resource
   */
  String string(final String key, final Object... arguments);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a formatted {@link String} resource from this
   ** <code>ResourceBundleProvider</code>.
   ** <p>
   ** This will substitute <code>%n$s</code> occurrences in the string resource
   ** with the appropriate parameter <code>n</code> from the array.
   **
   ** @param  key                the key for the desired string format
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   ** @param  arguments          the array of substitution arguments.
   **
   ** @return                    the formatted String resource
   */
  String string(final String key, final Locale locale, final Object... arguments);
}