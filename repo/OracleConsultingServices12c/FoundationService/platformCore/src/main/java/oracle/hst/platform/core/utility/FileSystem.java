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

    File        :   FileSystem.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FileSystem.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.LinkedList;

////////////////////////////////////////////////////////////////////////////////
// abstract class FileSystem
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The <code>FileSystem</code> class supplies utility methods for copying,
 ** moving and securely deleting files.
 ** The <code>FileSystem</code> has the ability to search for files on the
 ** classpath and optionally in a list of {@link URL}s by appending the file
 ** name.
 ** <br>
 ** The files are sought using the following algorithm:
 ** <ol>
 **   <li>Try and get the file as an {@link URL}
 **   <li>Replace any "." with "/" in a dotted name
 **   <li>Try and get the file as a resource on the classpath
 **   <li>Try and get the file from a list {@link URL}s by appending the file
 **       name to them
 ** </ol>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FileSystem {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String                 PROTOCOL  = "file";

  /** cached system property for the class path */
  public static final SystemProperty<String> CLASSPATH = SystemProperty.Builder.of(String.class)
    .name("classPath")
    .defaultValue(System.getProperty("java.class.path"))
    .build()
  ;

  /** cached system property for the class path */
  public static final SystemProperty<String> USERHOME  = SystemProperty.Builder.of(String.class)
    .name("userHome")
    .defaultValue(System.getProperty("user.home"))
    .build()
  ;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FileSystem</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new FileSystem()".
   */
  private FileSystem() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inputStream
  /**
   ** Return an <code>InputStream</code> for reading the passed filename.
   ** <br>
   ** At first the method tries to open a file with the passed name and if this
   ** fails tries to convert to an {@link URL} using
   ** {@link #url(String, ClassLoader)} and then calls {@link URL#openStream()}
   ** on the resulting {@link URL} if any.
   **
   ** @param  path               the name of the file to find.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
   **                            <br>
   **                            Possible object is {@link InputStream}.
   */
  public static InputStream inputStream(final String path) {
    return inputStream(path, (Class)null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inputStream
  /**
   ** Return an <code>InputStream</code> for reading the passed filename.
   ** <br>
   ** At first the method tries to open a file with the passed name and if this
   ** fails tries to convert to an {@link URL} using
   ** {@link #url(String, ClassLoader)} and then calls {@link URL#openStream()}
   ** on the resulting {@link URL} if any.
   **
   ** @param  path               the name of the file to find.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clazz              the class whose classloader should be used
   **                            when converting to {@link URL}.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
   **                            <br>
   **                            Possible object is {@link InputStream}.
   */
  public static InputStream inputStream(final String path, final Class<?> clazz) {
    try {
      return new FileInputStream(path);
    }
    catch (Exception e) {
      // intentionall left blank
      ;
    }

    URL url = url(path, clazz);
    if (url == null)
      return null;

    try {
      return url.openStream();
    }
    catch (IOException e) {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Convert a <code>fileName</code> to an URL.
   ** <br>
   ** The file is searched by using the following approach:
   ** <ol>
   **   <li>Try and get the file as an {@link URL}
   **   <li>Replace any "." with "/" in a dotted name
   **   <li>Try and get the file as a resource on the classpath
   **   <li>Try and get the file from a list {@link URL}s by appending the file
   **       name to them
   ** </ol>
   ** The classloader for the <code>FileSystem</code> object is used when
   ** attempting to convert the {@link URL}.
   **
   ** @param  path               the name of the file to find.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link URL} object for the file or
   **                            <code>null</code> if not found.
   **                            <br>
   **                            Possible object is {@link URL}.
   */
  public static URL url(final String path) {
    return url(path, (Class)null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Convert a <code>path</code> to an URL.
   ** <br>
   ** The file is searched by using the following approach:
   ** <ol>
   **   <li>Try and get the file as an {@link URL}
   **   <li>Replace any "." with "/" in a dotted name
   **   <li>Try and get the file as a resource on the classpath
   **   <li>Try and get the file from a list {@link URL}s by appending the file
   **       name to them
   ** </ol>
   ** The classloader for the <code>FileSystem</code> object is used when
   ** attempting to convert the URL.
   **
   ** @param  path               the name of the file to find.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clazz              the class whose classloader should be used
   **                            when converting to {@link URL}.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    the {@link URL} object for the file or
   **                            <code>null</code> if not found.
   **                            <br>
   **                            Possible object is {@link URL}.
   */
  public static URL url(final String path, final Class<?> clazz) {
    URL url = null;
    // if the path isn't malformed lines below will be successful convert to
    // an URL
    try {
      url = new URL(path);
      return url;
    }
    catch (Exception e) {
      // maybe it was malformed
      url = null;
    }
    // if the path was malformed lines below change the protocol to file and
    // try to convert the filename to an URL.
    // the expansion to a file url produce a wellformed url which is not enough
    // to have the correct resource.
    // the check is only complete if we can obtain a handle to the specified
    // resource which will be done by open an inputstrem on the URL.
    InputStream stream = null;
    try {
      url = new URL(PROTOCOL, null, path);
      stream = url.openStream();
      return url;
    }
    catch (Exception e) {
      url = null;
    }
    finally {
      try {
        stream.close();
      }
      catch (Exception e) {
        // itentionally left blank
        ;
      }
    }

    // try to load the file form the classloader derivated from passed class
    if (clazz != null) {
      url = clazz.getResource(path);
      if (url != null)
        return url;
    }

    // try to load the file form the context or system classloader
    url = url(path, FileSystem.class.getClassLoader());
    return url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Convert a <code>fileName</code> to an URL.
   ** <br>
   ** The file is searched as a resource on the classpath. If the passed
   ** classloader is null the context or system classloader is used.
   **
   ** @param  path               the name of the file to find.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  loader             the classloader which should be used when
   **                            converting to {@link URL}.
   **                            <br>
   **                            Allowed object is {@link ClassLoader}.
   **
   ** @return                    the {@link URL} object for the file or
   **                            <code>null</code> if not found.
   **                            <br>
   **                            Possible object is {@link URL}.
   */
  public static URL url(final String path, ClassLoader loader) {
    if (loader == null) {
      loader = Thread.currentThread().getContextClassLoader();
      if (loader == null)
        loader = ClassLoader.getSystemClassLoader();
    }

    // try to load the file from classloader
    return loader.getResource(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativePath
  /**
   ** Compare two paths to get a relative path.
   **
   ** @param  base               the base file path, ending with '/', indicating
   **                            a directory.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  path               a relative file path, ending with '/',
   **                            indicating a directory.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the relative path relative to the base path
   **                            object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String relativePath(final String base, final String path) {
    return relativePath(base, path, "./");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativePath
  /**
   ** Compare two paths to get a relative path.
   **
   ** @param  base               the base file path, ending with '/', indicating
   **                            a directory.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  path               a relative file path, ending with '/',
   **                            indicating a directory.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  equal              what to return if the two paths are equal,
   **                            usually "./".
   **                            <br>
   **                            You can also use "" or "." or whatever string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the relative path relative to the base path
   **                            object.
   */
  public static String relativePath(final String base, final String path, final String equal) {
    // if the two paths are equal
    if (base.equals(path) || "./".equals(path) || ".".equals(path))
      return equal;

    // start judging
    final String[] b = StringUtility.split(canonicalPath(base), "[\\\\/]");
    final String[] f = StringUtility.split(canonicalPath(path), "[\\\\/]");
    final int      l = Math.min(b.length, f.length);
    int pos = 0;
    for (; pos < l; pos++) {
      if (!b[pos].equals(f[pos]))
        break;
      // prove that the paths are equal
      if (l == pos && b.length == f.length)
        return equal;
    }
    // start finding the difference
    int dir = 1;
    if (base.endsWith("/"))
      dir = 0;
    final StringBuilder builder = new StringBuilder(StringUtility.dup("../", b.length - pos - dir));
    builder.append(StringUtility.concat(pos, f.length - pos, '/', f));
    if (path.endsWith("/"))
      builder.append('/');
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canonicalPath
  /**
   ** Returns the (unique) canonical path of a file or directory path.
   ** <br>
   ** There is only one canonical path to a file or directory while there can be
   ** many absolute paths to a file or directory (depending on the system).
   ** <br>
   ** For instance, on a Unix system, <code>/usr/local/../bin</code> is the same
   ** as <code>/usr/bin</code>. <code>canonicalPath()</code> resolves those
   ** ambiguities and returns the (unique) canonical path
   ** <br>
   ** Will merge ".." in the path.
   **
   * @param  path                the path
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the (unique) canonical path of a file or
   **                            directory path.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String canonicalPath(final String path) {
    if (StringUtility.blank(path))
      return path;

    final String[]           parts = StringUtility.split(path, "[\\\\/]");
    final LinkedList<String> paths = new LinkedList<String>();
    for (String s : parts) {
      if ("..".equals(s)) {
        if (paths.size() > 0)
          paths.removeLast();
        continue;
      }
      if (".".equals(s)) {
        // intentionally left blank
      }
      else {
        paths.add(s);
      }
    }

    final StringBuilder builder = StringUtility.join("/", paths);
    if (path.startsWith("/"))
      builder.insert(0, '/');
    if (path.endsWith("/"))
      builder.append('/');
    return builder.toString();
  }
}