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

    File        :   ListResourceBundle.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResourceBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

import java.lang.ref.SoftReference;

import java.util.Locale;
import java.util.ResourceBundle;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

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
 */
public abstract class ListResourceBundle extends java.util.ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String LOCATION = "%s-%s";
  protected static final String MESSAGE  = "%s : %s";

  protected static final Locale DEFAULT  = Locale.getDefault();

  /**
   ** The cache is a map from hash keys (with bundle base name and, locale to
   ** either a resource bundle or NONEXISTENT_BUNDLE wrapped by a Reference.
   ** <p>
   ** The cache is a ConcurrentMap, allowing the cache to be searched
   ** concurrently by multiple threads. This will also allow the hash keys to be
   ** reclaimed along with the ClassLoaders they reference.
   */
  private static final ConcurrentMap<Hash, Reference> CACHE = new ConcurrentHashMap<Hash, Reference>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface HashReference
  // ~~~~~~~~~ ~~~~~~~~~~~~~
  /**
   ** The common interface to get a {@link Hash} in  BundleReference.
   */
  private static interface HashReference {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hash
    Hash hash();
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Hash
  // ~~~~~ ~~~~
  /**
   ** Key used for cached resource bundles.
   ** <br>
   ** The key checks the base name and the locale to determine if the resource
   ** is a match to the requested one.
   */
  private static class Hash {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Class<?> clazz;
    private final Locale   locale;
    // hash code value cache to avoid recalculating the hash code of this
    // instance.
    private int            hash;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Hash</code> key for the given name and
     ** {@link Locale}.
     */
    Hash(final Class<?> clazz, final Locale locale) {
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
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public final int hashCode() {
      return this.hash;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Reference
  // ~~~~~ ~~~~~~~~~
  /**
   * References to bundles are soft references so that they can be garbage
   * collected when they have no hard references.
   */
  private static class Reference extends    SoftReference<ListResourceBundle>
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
     ** Constructs a <code>Reference</code> to a ResourceBundle.
     */
    Reference(final ListResourceBundle referent, final Hash hash) {
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
     **                          <br>
     **                          Possible object is {@link Hash}.
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
  // Method:   bundle
  /**
   ** Returns a {@link ListResourceBundle} for the specified {@link Locale}.
   **
   ** @param  base               the base class of the resource bundle
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    a resource bundle for the default
   **                            {@link Locale}.
   **                            <br>
   **                            Possible object is {@link ListResourceBundle}.
   */
  public static ListResourceBundle bundle(final Class<?> base) {
    return bundle(base, DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bundle
  /**
   ** Returns a {@link ListResourceBundle} for the specified {@link Locale}.
   **
   ** @param  base               the base class of the resource bundle
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  locale             the {@link Locale} for which a resource bundle
   **                            is desired.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    a resource bundle for the given {@link Locale}.
   **                            <br>
   **                            Possible object is {@link ListResourceBundle}.
   */
  public static ListResourceBundle bundle(final Class<?> base, final Locale locale) {
    Hash               hash   = new Hash(base, locale == null ? DEFAULT : locale);
    // quick lookup of the cache.
    Reference          ref    = CACHE.get(hash);
    ListResourceBundle bundle = null;
    if (ref != null) {
      bundle = ref.get();
      ref = null;
    }
    // no valid bundle was found in the cache, so we need to load the resource
    // bundle and its parents.
    if (bundle == null) {
      bundle = (ListResourceBundle)ResourceBundle.getBundle(hash.clazz.getName(), hash.locale, hash.clazz.getClassLoader());
      // put the bundle in the cache if it's not been in the cache.
      ref = CACHE.putIfAbsent(hash, new Reference(bundle, hash));
    }
    return bundle;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing the class and method name.
   **
   ** @param  thread             the current thread {@link Thread} requiring the
   **                            trace.
   **                            <br>
   **                            Allowed object is {@link Thread}.
   ** @param  clazz              the {@link Class} of the class requiring the
   **                            trace.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  methodName         the methode where the request occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a formatted string containing
   **                            <code>thread</code>, <code>clazz</code> and
   **                            <code>methodName</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String location(final Thread thread, final Class<?> clazz, final String methodName) {
    return location(thread, clazz.getName(), methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing the class and method name.
   **
   ** @param  thread             the current thread {@link Thread} requiring the
   **                            trace.
   **                            <br>
   **                            Allowed object is {@link Thread}.
   ** @param  location           the location where the request occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  methodName         the methode where the request occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  methodName         the methode where the request occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String location(final Class<?> clazz, final String methodName) {
    return location(clazz.getName(), methodName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Builds the location string containing the class and method name.
   **
   ** @param  className          the name of the class requiring the trace
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  methodName         the methode where the request occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the location string containing the class and
   **                            method name.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a formatted string containing
   **                            <code>location</code> and <code>message</code>.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the methode where the request occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a formatted string containing
   **                            <code>clazz</code>, <code>method</code> and
   **                            <code>message</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String message(final String clazz, final String method, final String message) {
    return message(location(clazz, method), message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Returns a formatted string using the specified format string and
   ** arguments.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  format             a <a href="../util/Formatter.html#syntax">format string</a>
   **                            <br>
   **                            Allowed object is {@link String}.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String format(final String format, final Object... arguments) {
    final int count = arguments == null ? 0 : arguments.length;
    for (int i = 0; i < count; ++i) {
      if (arguments[i] == null)
        arguments[i] = "(null)";
    }
    return String.format(format, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                the key for the desired string format
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution arguments.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the formatted String resource
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String formatted(final String key, final Object... arguments) {
    return (arguments == null) ? internal(key) : format(internal(key), arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                the key for the desired string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the object for the given key.
   */
  protected final String internal(final String key) {
    return internal(key, DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                the key for the desired string.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    the object for the given key.
   */
  protected final String internal(final String key, final Locale locale) {
    return bundle(this.getClass(), locale).getObject(key).toString();
  }
}