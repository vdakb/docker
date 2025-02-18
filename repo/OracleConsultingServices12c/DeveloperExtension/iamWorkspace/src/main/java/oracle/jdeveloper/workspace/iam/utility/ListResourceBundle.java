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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ListResourceBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ListResourceBundle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import java.util.Locale;
import java.util.TimeZone;
import java.util.MissingResourceException;

import java.text.Format;
import java.text.DateFormat;
import java.text.MessageFormat;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class ListResourceBundle extends java.util.ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONFIG_FILE_TYPE = "xml";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Object       lock            = new Object();

  private final String       baseName;
  private final Locale       locale;
  private final TimeZone     timeZone;

  private MessageFormat      messageFormat    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListResourceBundle</code> that allows use as a JavaBean.
   ** <p>
   ** The default locale will be used.
   ** <p>
   ** The default time zone will be used.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ListResourceBundle() {
    // ensure inheritance
    this(null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListResourceBundle</code> by loading the base name
   ** resource bundle and setting it as its parent.
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
  public ListResourceBundle(final String baseName) {
    // ensure inheritance
    this(baseName, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListResourceBundle</code> ready to be populated with
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
  public ListResourceBundle(final Locale locale, final TimeZone timeZone) {
    // ensure inheritance
    this(null, locale, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListResourceBundle</code> by loading the base name
   ** resource bundle and setting it as its parent.
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
  public ListResourceBundle(final String baseName, Locale locale, TimeZone timeZone) {
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
  // Methods groupe by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    return (tmp == null || tmp.trim().length() == 0) ? 0x00 : tmp.charAt(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{1}" occurrences in the string resource with the
   ** appropriate parameter "n".
   **
   ** @param  key                 the key for the desired string pattern
   ** @param  argument1           the subsitution value for {0}
   **
   ** @return                     the formatted String resource
   */
  public String formatted(final String key, final Object argument1) {
    final Object[] arguments = { argument1 };
    return formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{2}" occurrences in the string resource with the
   ** appropriate parameter "n".
   **
   ** @param  key                 the key for the desired string pattern
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   **
   ** @return                     the formatted String resource
   */
  public String formatted(final String key, final Object argument1, final Object argument2) {
    final Object[] arguments = { argument1, argument2 };
    return formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{3}" occurrences in the string resource with the
   ** appropriate parameter "n".
   **
   ** @param  key                 the key for the desired string pattern
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   ** @param  argument3           the subsitution value for {2}
   **
   ** @return                     the formatted String resource
   */
  public String formatted(final String key, final Object argument1, final Object argument2, final Object argument3) {
    final Object[] arguments = { argument1, argument2, argument3 };
    return formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                 the key for the desired string pattern
   ** @param  arguments           the array of substitution arguments.
   **
   ** @return                     the formatted String resource
   */
  public String formatted(final String key, final Object[] arguments) {
    int count = arguments == null ? 0 : arguments.length;
    if (count == 0)
      return this.getString(key);

    for (int i = 0; i < count; ++i) {
      if (arguments[i] == null)
        arguments[i] = "(null)";
    }
    final String result = MessageFormat.format(this.internalString(key), arguments);
    for (int i = 0; i < count; ++i) {
      if (arguments[i] == "(null)")
        arguments[i] = null;
    }
    return result;
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
   ** @param  key                 the key for the desired string pattern.
   ** @param  arguments           the objects to be formatted and substituted.
   **
   ** @return                     the formatted String resource.
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
  // Method:   format
  /**
   ** Formats the objects and substitutes them into the message string pattern.
   **
   ** @param  pattern             the pattern for this message string.
   ** @param  arguments           the objects to be formatted and substituted.
   **
   ** @return                     the formatted message string.
   **
   ** @throws NullPointerException if pattern is <code>null</code>
   */
  public String format(final String pattern, final Object... arguments) {
    // prevent bogus inpiut
    if (pattern == null)
      throw new NullPointerException();

    synchronized (this.lock) {
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
  // Method:   fetchIcon
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                 the key for the desired string
   **
   ** @return                     the {@link Icon} resource the specified
   **                             <code>key</code> belongs to.
   */
  public Icon fetchIcon(final String key) {
    return IconFactory.create(this.getClass(), getString(key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchGalleryIcon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will always have the dimension 16x16 pixel to
   ** for the layout of the Oracle JDeveloper navigator item list
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the {@link Icon} resource scaled up or doen to
   **                            a dimension of 16x16 pixel.
   */
  public Icon fetchGalleryIcon(final String key) {
    return fetchGalleryIcon(key, 16, 16);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchGalleryIcon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will scaled to the dimension <code>width</code>
   ** <code>height</code> in pixel to fit the layout of the Oracle JDeveloper
   ** Gallery item list
   **
   ** @param  key                index into the resource array.
   ** @param  width              the indented width of the generated image.
   ** @param  height             the indented height of the generated image.
   **
   ** @return                    the {@link Icon} resource scaled up or doen to
   **                            a dimension of 16x16 pixel.
   */
  public Icon fetchGalleryIcon(final String key, final int width, int height) {
    return fetchScaledIcon(key, width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchScaledIcon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will scaled to the dimension <code>width</code>
   ** <code>height</code> in pixel to fit the layout of the Oracle JDeveloper
   ** Gallery item list
   **
   ** @param  key                index into the resource array.
   ** @param  width              the indented width of the generated image.
   ** @param  height             the indented height of the generated image.
   **
   ** @return                    the {@link Icon} resource scaled up or doen to
   **                            a dimension of 16x16 pixel.
   */
  public Icon fetchScaledIcon(final String key, final int width, int height) {
    return IconFactory.scaled(null, IconFactory.create(this.getClass(), getString(key)), width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchImage
  /**
   ** Fetchs an {@link Image} for the given key from this resource bundle or one
   ** of its parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                 the key for the desired {@link Image}.
   **
   ** @return                     an {@link Image} for the given key.
   **
   ** @throws MissingResourceException if no object for the given key can be
   **                                  found
   */
  public Image fetchImage(final String key) {
    final ImageIcon imageIcon = IconFactory.create(this.getClass(), getString(key));
    return imageIcon != null ? imageIcon.getImage() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internalString
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                the key for the desired string.
   **
   ** @return                    the object for the given key as a string.
   **
   ** @throws MissingResourceException if no object for the given key can be
   **                                  found
   */
  protected final String internalString(final String key) {
    return this.getObject(key).toString();
  }

  private void setParent() {
    if (this.baseName == null)
      setParent(null);
    else {
      try {
        // Set the base name resource bundle as the parent.
        setParent(java.util.ListResourceBundle.getBundle(this.baseName, this.locale, ListResourceBundle.class.getClassLoader()));
      }
      catch (MissingResourceException e) {
        try { // Set the base name resource bundle as the parent.
          setParent(java.util.ListResourceBundle.getBundle(this.baseName, this.locale, getClass().getClassLoader()));
        }
        catch (MissingResourceException mre1) {
          throw mre1;
        }
      }
    }
  }
}