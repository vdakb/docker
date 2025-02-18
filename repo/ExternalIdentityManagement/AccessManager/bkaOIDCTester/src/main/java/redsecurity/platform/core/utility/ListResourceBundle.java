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

    File        :   ListResourceBundle.java

    Compiler    :   Java Developer Kit 8

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    ListResourceBundle.


    Revisions   Date        Editor                    Comment
    -----------+-----------+-------------------------+--------------------------
    1.0.0.0     2023-28-06  dieter.steding@icloud.com First release version
*/

package redsecurity.platform.core.utility;

import java.util.Locale;
import java.util.ResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ListResourceBundle
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ListResourceBundle implements ResourceBundleProvider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ResourceBundle delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ListResourceBundle</code>.
   **
   ** @param  bundle             a {@link ResourceBundle} that represents a
   **                            property file to read from.
   */
  protected ListResourceBundle(final ResourceBundle bundle) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = bundle;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string (ResourceBundleProvider)
  /**
   ** Fetch a {@link String} from this <code>ListResourceBundle</code>.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                the key into the resource array.
   **
   ** @return                    the string resource.
   */
  @Override
  public final String string(final String key) {
    return string(key, DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string (ResourceBundleProvider)
  /**
   ** Fetch a {@link String} from this <code>ListResourceBundle</code>.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                the key into the resource array.
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   **
   ** @return                    the string resource.
   */
  @Override
  public final String string(final String key, final Locale locale) {
    return internal(key, locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string (ResourceBundleProvider)
  /**
   ** Fetch a formatted {@link String} resource from this
   ** <code>ListResourceBundle</code>.
   ** <p>
   ** This will substitute <code>%n$s</code> occurrences in the string resource
   ** with the appropriate parameter <code>n</code> from the array.
   **
   ** @param  key                the key for the desired string format
   ** @param  arguments          the array of substitution arguments.
   **
   ** @return                    the formatted String resource
   */
  @Override
  public final String string(final String key, final Object... arguments) {
    return (arguments == null) ? internal(key) : ResourceBundleProvider.format(internal(key), arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a formatted {@link String} resource from this
   ** <code>ListResourceBundle</code>.
   ** <p>
   ** This will substitute <code>%n$s</code> occurrences in the string resource
   ** with the appropriate parameter <code>n</code> from the array.
   **
   ** @param  key                the key for the desired string format
   ** @param  arguments          the array of substitution arguments.
   **
   ** @return                    the formatted String resource
   */
  @Override
  public final String string(final String key, final Locale locale, final Object... arguments) {
    return (arguments == null) ? internal(key, locale) : ResourceBundleProvider.format(internal(key, locale), arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Returns a <code>ListResourceBundle</code> for the default {@link Locale}.
   **
   ** @param  base               the base class of the resource bundle
   **
   ** @return                    a resource bundle for the default
   **                            {@link Locale}.
   */
  public static ResourceBundleProvider load(final Class<? extends ResourceBundleProvider> base) {
    return load(base, DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Returns a {@link ResourceBundle} for the specified {@link Locale}.
   **
   ** @param  base               the base class of the resource bundle
   ** @param  locale             the {@link Locale} for which a resource bundle
   **                            is desired.
   **
   ** @return                    a resource bundle for the given {@link Locale}.
   */
  public static ResourceBundleProvider load(final Class<? extends ResourceBundleProvider> base, final Locale locale) {
    return bundle(base.getName(), locale, base.getClassLoader());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                the key for the desired string.
   **
   ** @return                    the object for the given key.
   */
  private String internal(final String key) {
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
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   **
   ** @return                    the object for the given key.
   */
  private String internal(final String key, final Locale locale) {
    // either the local match or the resource bundle loaded is a common bundle
    // without specific properties for the language
    return ((this.delegate.getLocale().getLanguage().length() == 0) || (this.delegate.getLocale().equals(locale)))
         ? this.delegate.getString(key)
         : bundle(this.delegate.getBaseBundleName(), locale, this.delegate.getClass().getClassLoader()).string(key, locale)
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bundle
  /**
   ** Returns a {@link ResourceBundle} for the specified {@link Locale}.
   **
   ** @param  base               the base name of the resource bundle.
   ** @param  locale             the {@link Locale} for which a resource bundle
   **                            is desired.
   ** @param  loader             the class loader from which to load the
   **                            resource bundle.
   **
   ** @return                    a resource bundle for the given {@link Locale}.
   */
  private static ResourceBundleProvider bundle(final String base, final Locale locale, final ClassLoader loader) {
    final Hash hash = new Hash(base, locale == null ? DEFAULT : locale);
    // quick lookup of the cache.
    ResourceBundleProvider bundle = ResourceBundleProvider.lookup(hash);
    // no valid bundle was found in the cache, so we need to load the resource
    // bundle and its parents.
    if (bundle == null) {
      bundle = new ListResourceBundle(ResourceBundle.getBundle(hash.clazz, hash.locale, loader, ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_CLASS)));
      // put the bundle in the cache if it's not been in the cache.
      CACHE.putIfAbsent(hash, new Reference(bundle, hash));
    }
    return bundle;
  }
}