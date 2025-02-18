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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   ResourceBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Hashtable;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.MissingResourceException;

import java.text.Format;
import java.text.DateFormat;
import java.text.MessageFormat;

import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

////////////////////////////////////////////////////////////////////////////////
// class ResourceBundle
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Bundle implements a number of features that ease development.
 ** <ul>
 **   <li>Message Formatting
 **   <li>Time Zone Awareness
 **   <li>Runtime Overloading
 **   <li>Map Interface
 **   <li>Serializabe Interface
 **   <li>Thread Safe Operation
 ** </ul>
 ** <b>Message Formatting</b>
 ** <br>
 ** Bundles incorporate the message formatting capabilities.
 ** <br>
 ** To format a message, simply invoke the
 ** <code>format(String pattern, Object... arguments)</code> method. The
 ** standard MessageFormat patterns apply. The pattern can also be loaded from a
 ** resource bundle. For this use the
 ** <code>getString(String key, Object... arguments)</code> method, where the
 ** <code>key</code> parameters is the resource bundle key under which the
 ** desired pattern can be found.
 ** <p>
 ** <b>Time Zone Awareness</b>
 ** <br>
 ** Bundles are aware of the time zone in addition to the locale.
 ** <br>
 ** The time zone specified will be used while formatting time zone related
 ** information, such as date and time.  You will also find the
 ** <code>annulTimeZone(...)</code> methods that cancel the time zone offset for
 ** dates that are not time zone specific, such as birthdays for example.
 ** <p>
 ** <b>Runtime Overloading</b>
 ** <br>
 ** Bundles allow runtime overloading. You can simply put and remove new
 ** kev=value pairs into the super resource bundle that overload the ones found
 ** in the base name resource bundle. To achieve this, use the
 ** <code>put(String key, Object value)</code> and
 ** <code>remove(Object key)</code> methods.
 ** <p>
 ** <b>Map Interface</b>
 ** <br>
 ** Bundles implement the Map interface and can be used as regular maps in most
 ** scenarios. Most of the map methods affect only overloaded values, with the
 ** exception of the get method, which works with both overloaded and loaded
 ** values.
 ** <p>
 ** <b>Serializable Interface</b>
 ** <br>
 ** Bundles can be serialized.
 ** <br>
 ** During serialization, only the overloaded values are marshaled. The parent
 ** resource bundle will be reloaded after serialization. Thus the base name
 ** resource bundle has to be available at the serialization source as well as
 ** the destination.
 ** <p>
 ** <b>Thread Safe Operation</b>
 ** <br>
 ** Bundles are thread safe.
 ** <br>
 ** The overloaded values are backed by a Hashtable, which is synchronized, and
 ** the formatting is synchronized as well. Thus you should have no problems
 ** using them in multi-threaded applications.
 ** <p>
 ** <b>Examples</b>
 ** <br>
 ** To use the formatting feature simply invoke the
 ** <code>format(String pattern, Object... arguments)</code> method:
 ** <pre>
 **   Bundle bundle = new Bundle(...);
 **   System.out.println(bundle.format("{0, date}", System.currentTimeMillis()));
 ** </pre>
 ** will generate the following output:
 ** <pre>
 **   Apr 15, 2006
 ** </pre>
 ** <p>
 ** The standard MessageFormat patterns apply.
 ** <br>
 ** To use the format stored in the base name resource bundle simply invoke the
 ** <code>getString(String key, Object... arguments)</code> method:
 ** <pre>
 **   System.out.println(bundle.getString("date", System.currentTimeMillis()));
 ** </pre>
 ** assuming the following key=value pair is present in the base name resource
 ** bundle:
 ** <pre>
 **   date = {0, date}
 ** </pre>
 ** will generate the following output:
 ** <pre>
 **   Apr 15, 2006
 ** </pre>
 ** <p>
 ** To overload the date key from the base resource bundle, simply put the new
 ** value using the desired key:
 ** <pre>
 **   bundle.put("date", "Today is {0, date}");
 **   System.out.println(bundle.getString("date", System.currentTimeMillis()));
 ** </pre>
 ** will generate the following output:
 ** <pre>
 **   Today is Apr 15, 2006
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceBundle extends    java.util.ResourceBundle
                            implements Map<String, Object>
                            ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2012468879831793326")
  private static final long               serialVersionUID = 7175733852309793169L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                    baseName;
  private final Locale                    locale;
  private final TimeZone                  timeZone;

  private final Hashtable<String, Object> overload         = new Hashtable<String, Object>();

  private MessageFormat                   messageFormat    = null;

  private boolean                         modifiable       = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceBundle</code> that allows use as a JavaBean.
   ** <p>
   ** The default locale will be used.
   ** <p>
   ** The default time zone will be used.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceBundle() {
    // ensure inheritance
    this(null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceBundle</code> by loading the base name resource
   ** bundle and setting it as its parent.
   ** <p>
   ** If the base name is specified as <code>null</code>, no parent resource
   ** bundle will be loaded.
   ** <p>
   ** The default locale will be used.
   ** <p>
   ** The default time zone will be used.
   **
   ** @param  baseName           the base name of the parent resource bundle.
   */
  public ResourceBundle(final String baseName) {
    // ensure inheritance
    this(baseName, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceBundle</code> ready to be populated with
   ** entries.
   ** <p>
   ** If the locale is specified as <code>null</code>, the default locale will
   ** be used.
   ** <p>
   ** If the time zone was specified as <code>null</code>, the default time zone
   ** will be used.
   **
   ** @param  locale             the locale used in formatting.
   ** @param  timeZone           the time zone used in formatting.
   */
  public ResourceBundle(final Locale locale, final TimeZone timeZone) {
    // ensure inheritance
    this(null, locale, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceBundle</code> by loading the base name resource
   ** bundle and setting it as its parent.
   ** <p>
   ** If the base name is specified as <code>null</code>, no parent resource
   ** bundle will be loaded.
   ** <p>
   ** If the locale is specified as <code>null</code>, the default locale will
   ** be used.
   ** <p>
   ** If the time zone was specified as <code>null</code>, the default time zone
   ** will be used.
   **
   ** @param  baseName           the base name of the parent resource bundle.
   ** @param  locale             the locale used in formatting.
   ** @param  timeZone           the time zone used in formatting.
   */
  public ResourceBundle(final String baseName, Locale locale, TimeZone timeZone) {
    // ensure inheritance
    super();

    if (locale == null)
      locale = Locale.getDefault();
    if (timeZone == null)
      timeZone = TimeZone.getDefault();

    this.baseName = baseName;
    this.locale   = locale;
    this.timeZone = timeZone;

    setParent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiable
  /**
   ** Makes the resource bundle unmodifiable.
   */
  public void unmodifiable() {
    this.modifiable = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiable
  /**
   ** Checks if the resource bundle is modifiable.
   **
   ** @return                    <code>true</code> if the resource bundle is
   **                            modifiable.
   */
  public boolean modifiable() {
    return this.modifiable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   baseName
  /**
   ** Return the parent resource bundle base name.
   **
   ** @return                    the parent resource bundle base name
   */
  public String baseName() {
    return this.baseName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Return the locale used in formatting.
   **
   ** @return                    the locale used in formatting.
   */
  public Locale locale() {
    return this.locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Return the time zone used in formatting.
   **
   ** @return                    the time zone used in formatting.
   */
  public TimeZone timeZone() {
    return this.timeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annulTimeZone
  /**
   ** Annuls the time zone offset for dates that are not time zone specific.
   ** <p>
   ** To format a birthday, which is not time zone specific, try:
   ** <pre>
   **   bundle.format("{0, date}", bundle.annulTimeZone(date));
   ** </pre>
   **
   ** @param  date               the date to be offset.
   **
   ** @return                    long representing the offset date.
   */
  public long annulTimeZone(final long date) {
    return date - this.timeZone.getRawOffset();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annulTimeZone
  /**
   ** Annuls the time zone offset for dates that are not time zone specific.
   ** <p>
   ** To format a birthday, which is not time zone specific, try:
   ** <pre>
   **   bundle.format("{0, date}", bundle.annulTimeZone(date));
   ** </pre>
   **
   ** @param  date               the {@link Date} to be offset.
   **
   ** @return                    the {@link Date} representing the offset date
   */
  public Date annulTimeZone(Date date) {
    return new Date(annulTimeZone(date.getTime()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setParent
  /**
   ** Sets the parent resource bundle.
   */
  private void setParent() {
    if (this.baseName == null)
      setParent(null);
    else {
      try {
        // set the base name resource bundle as the parent
        setParent(java.util.ResourceBundle.getBundle(this.baseName, this.locale, ResourceBundle.class.getClassLoader()));
      }
      catch (MissingResourceException e) {
        try {
          // set the base name resource bundle as the parent
          setParent(java.util.ResourceBundle.getBundle(this.baseName, this.locale, getClass().getClassLoader()));
        }
        catch (MissingResourceException mre1) {
          throw mre1;
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getParent
  /**
   ** Returns the parent resource bundle.
   **
   ** @return                    the parent resource bundle.
   */
  public java.util.ResourceBundle getParent() {
    return this.parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringFormatted
  /**
   ** Returns a formatted string pattern for the given key from this resource
   ** bundle or one of its parents. Calling this method is equivalent to
   ** calling:
   ** <pre>
   **   bundle.format(bundle.getString(key), arguments)
   ** </pre>
   **
   ** @param  key                the key for the desired string pattern.
   ** @param  arguments          the objects to be formatted and substituted.
   **
   ** @return                    the formatted String resource.
   **
   ** @throws NullPointerException     if key is <code>null</code>.
   ** @throws MissingResourceException if no object for the given key can be
   **                                  found
   ** @throws ClassCastException       if the object found for the given key is
   **                                  not a string.
   */
  public final String stringFormatted(final String key, final Object... arguments) {
    return format(getString(key), arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Formats the objects and substitutes them into the message string pattern.
   **
   ** @param  pattern            the pattern for this message string.
   ** @param  arguments          the objects to be formatted and substituted.
   **
   ** @return                    the formatted message string.
   **
   ** @throws NullPointerException if pattern is <code>null</code>
   */
  public String format(final String pattern, final Object... arguments) {
    // prevent bogus inpiut
    if (pattern == null)
      throw new NullPointerException();

    synchronized (this.overload) {
      if (this.messageFormat == null)
        this.messageFormat = new MessageFormat(pattern, this.locale);
      else
        this.messageFormat.applyPattern(pattern);
      Format[] formats = this.messageFormat.getFormats();
      if (formats != null) { // Adjust formats if any...
        for (Format format : formats) {
          if (format instanceof DateFormat)
            ((DateFormat)format).setTimeZone(this.timeZone);
        }
      }
      return this.messageFormat.format(arguments, new StringBuffer(), null).toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear (Map)
  /**
   ** Clear all overloaded key-value pairs from the bundle.
   */
  public void clear() {
    checkModifiable();
    this.overload.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Map)
  /**
   ** Returns the number of overloaded key-value mappings.
   **
   ** @return                    the number of overloaded key-value mappings.
   */
  @Override
  public int size() {
    return this.overload.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty (Map)
  /**
   ** Returns <code>true</code> if there are no overloaded key-value mappings.
   **
   ** @return                    <code>true</code> if there are no overloaded
   **                            key-value mappings.
   */
  @Override
  public boolean isEmpty() {
    return this.overload.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty (Map)
  /**
   ** Returns <code>true</code> if the is an overloaded mapping for the given
   ** key.
   **
   ** @param  key                the given key
   **
   ** @return                    <code>true</code> if the is an overloaded
   **                            mapping for the given key.
   **
   ** @throws NullPointerException if key is <code>null</code>.
   */
  @Override
  public boolean containsKey(final Object key) {
    return this.overload.containsKey(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsValue (Map)
  /**
   ** Returns <code>true</code> if the is an overloaded mapping for the given
   ** value.
   **
   ** @param  value              the given value.
   **
   ** @return                    <code>true</code> if the is an overloaded
   **                            mapping for the given value
   **
   ** @throws NullPointerException if value is <code>null</code>.
   */
  @Override
  public boolean containsValue(final Object value) {
    return this.overload.containsValue(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get (Map)
  /**
   ** Returns the overloaded or loaded value for the given key.
   ** <p>
   ** This method will return <code>null</code> if the key cannot be found in
   ** the overloaded or loaded values.
   ** <br>
   ** It will <b>not</b> throw a <i>MissingResourceException</i>.
   **
   ** @param  key                the given key.
   **
   ** @return                    the overloaded or loaded value for the given
   **                            key, or null
   **
   ** @throws NullPointerException if key is <code>null</code>.
   */
  @Override
  public Object get(final Object key) {
    // prevent bogus inpiut
    if (key == null)
      throw new NullPointerException();

    try {
      return getObject(key.toString());
    }
    catch (MissingResourceException mre) {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put (Map)
  /**
   ** Puts the overloaded key-value pair into the bundle.
   **
   ** @param  key                the given key.
   ** @param  value              the given value.
   **
   ** @return                    the replaced value if any, or
   **                            <code>null</code>.
   **
   ** @throws NullPointerException if key or value is <code>null</code>.
   */
  @Override
  public Object put(final String key, final Object value) {
    checkModifiable();
    return this.overload.put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put (Map)
  /**
   ** Puts all overloaded key-value pair into the bundle.
   **
   ** @param  map                the map of overloaded hey-value pairs.
   **
   **
   ** @throws NullPointerException if specified map is <code>null</code>.
   */
  @Override
  public void putAll(final Map<? extends String, ? extends Object> map) {
    checkModifiable();
    this.overload.putAll(map);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Map)
  /**
   ** Removes the overloaded key from the bundle.
   **
   ** @param  key                the given key.
   **
   ** @return                    the removed value if any, or <code>null</code>.
   **
   ** @throws NullPointerException if key is <code>null</code>.
   */
  @Override
  public Object remove(Object key) {
    checkModifiable();
    return this.overload.remove(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keySet (Map)
  /**
   ** Returns the set of overloaded keys.
   ** <p>
   ** The returned set is always unmodifiable.
   **
   ** @return                    the set of overloaded keys.
   */
  @Override
  public Set<String> keySet() {
    return Collections.unmodifiableSet(this.overload.keySet());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values (Map)
  /**
   ** Returns the set of overloaded values.
   ** <p>
   ** The returned set is always unmodifiable.
   **
   ** @return                    the set of overloaded values
   */
  @Override
  public Collection<Object> values() {
    return Collections.unmodifiableCollection(this.overload.values());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entrySet (Map)
  /**
   ** Returns the overloaded entries.
   ** <p>
   ** The returned set is always unmodifiable.
   **
   ** @return                    the overloaded entries.
   */
  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return Collections.unmodifiableSet(this.overload.entrySet());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKeys (ResourceBundle)
  /**
   ** Returns an enumeration of the overloaded keys.
   **
   ** @return                    the overloaded keys
   */
  @Override
  public Enumeration<String> getKeys() {
    return this.overload.keys();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleGetObject (ResourceBundle)
  /**
   ** Returns an object for the given key from this resource bundle.
   ** <br>
   ** Returns <code>null</code> if this resource bundle does not contain an
   ** object for the given key.
   **
   ** @param  key                the key for the desired object.
   **
   ** @return                     the object for the given key, or
   **                             <code>null</code>.
   */
  @Override
  protected Object handleGetObject(String key) {
    return this.overload.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Method for fetching a <code>ResourceBundle</code>.
   **
   ** @param  baseName           the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Bundle" or {PACKAGE_NAME} + "Bundle".
   ** @param locale              the locale to look for.
   ** @param  timeZone           the time zone used in formatting.
   **
   ** @return                    the {@link ResourceBundle}.
   */
  public static ResourceBundle instance(final String baseName, final Locale locale, final TimeZone timeZone) {
    return new ResourceBundle(baseName, locale, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkModifiable
  protected void checkModifiable() {
    if (!modifiable())
      throw new UnsupportedOperationException("unmodifiable");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeObject
  /**
   ** Writes the non-static and non-transient fields of the bundle state to
   ** the specified {@link ObjectOutputStream} <code>out</code>.
   **
   ** @param  out                the {@link ObjectOutputStream} to write the
   **                            non-static and non-transient fields of the
   **                            bundle to.
   **
   ** @throws IOException        if I/O errors occur while writing to
   **                            {@link ObjectOutputStream} <code>out</code>.
   */
  private void writeObject(final ObjectOutputStream out)
    throws IOException {

    out.defaultWriteObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readObject
  /**
   ** Reads the non-static and non-transient fields of the bundle state from the
   ** the specified {@link ObjectInputStream} <code>in</code>, and reloads the
   ** parent resource bundle.
   **
   ** @param  in                 the {@link ObjectInputStream} to read the
   **                            non-static and non-transient fields of the
   **                            bundle from.
   **
   ** @throws IOException            if I/O errors occur while reading from
   **                                {@link ObjectInputStream} <code>in</code>.
   ** @throws ClassNotFoundException if the class of a serialized object could
   **                                not be found.
   */
  private void readObject(final ObjectInputStream in)
    throws IOException
    ,      ClassNotFoundException {

    in.defaultReadObject();
    setParent();
  }
}