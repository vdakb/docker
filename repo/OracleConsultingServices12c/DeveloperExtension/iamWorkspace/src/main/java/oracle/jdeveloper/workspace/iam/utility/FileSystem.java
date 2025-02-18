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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   FileSystem.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FileSystem.

    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import java.net.URL;

////////////////////////////////////////////////////////////////////////////////
// abstract class FileSystem
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The <code>FileSystem</code> class supplies utility methods for copying,
 ** moving and securely deleting files.
 ** The <code>FileSystem</code> has the ability to search for files on the
 ** classpath and optionally in a list of <code>URL</code>s by appending the
 ** file name.
 ** <br>
 ** The files are sought using the following algorithm:
 ** <ol>
 **   <li>Try and get the file as an <code>URL</code></li>
 **   <li>Replace any "." with "/" in a dotted name</li>
 **   <li>Try and get the file as a resource on the classpath</li>
 **   <li>Try and get the file from a list <code>URL</code>s by appending the
 **       file name to them
 ** </ol>
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class FileSystem {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PROTOCOL = "file";

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
    throw new UnsupportedOperationException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Equivalent to {@link #normalize(String, char)
   ** normalize(path, File.separatorChar)}.
   **
   ** @param  path               the non-<code>null</code> path name to
   **                            normalize.
   **
   ** @return                    <code>path</code> if it was already in
   **                            normalized form. Otherwise, a new String with
   **                            the normalized form of the given path.
   */
  public static String normalize(final String path) {
    return normalize(path, File.separatorChar);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Removes all redundant separators, dot directories (<code>"."</code>) and
   ** dot-dot directories (<code>".."</code>) from the path name and returns the
   ** result.
   ** <p>
   ** If present, a single trailing separator character is retained. An empty
   ** path results in {<code>"."</code>.
   ** <p>
   ** On Windows, a path may be prefixed by a drive letter followed by a colon.
   ** On all platforms, a path may be prefixed by two leading separators to
   ** indicate a UNC, although this is currently supported on Windows only.
   **
   ** @param  path               the non-<code>null</code> path name to
   **                            normalize.
   ** @param  separatorChar      the separator character.
   **
   ** @return                    <code>path</code> if it was already in
   **                            normalized form. Otherwise, a new String with
   **                            the normalized form of the given path.
   **
   ** @throws NullPointerException if path is {@code null}.
   */
  public static String normalize(final String path, final char separatorChar) {
    final int          prefixLen = prefixLength(path, separatorChar);
    final int          pathLen   = path.length();
    final StringBuilder buffer   = new StringBuilder(pathLen);
    normalize(path.substring(prefixLen, pathLen), separatorChar, 0, pathLen - prefixLen, buffer);

    buffer.insert(0, path.substring(0, prefixLen));
    if (buffer.length() == prefixLen && (prefixLen <= 0 || buffer.charAt(prefixLen - 1) != separatorChar))
      buffer.append('.');
    if (pathLen > 0 && path.charAt(pathLen - 1) == separatorChar)
      if (buffer.charAt(buffer.length() - 1) != separatorChar)
        // retain trailing separator
        buffer.append(separatorChar);
    final int bufferLen = buffer.length();
    String result;
    if (bufferLen == path.length()) {
      assert path.equals(buffer.toString());
      result = path;
    }
    else {
      result = buffer.toString();
      if (path.startsWith(result))
        result = path.substring(0, bufferLen);
    }
    // postcondition
    assert !result.equals(path) || result == path;
    return result;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefixLength
  /**
   ** Returns the length of the file system prefix in <code>path</code>.
   ** <p>
   ** File system prefixes are:
   ** <ol>
   **   <li>A letter followed by a colon and an optional separator.
   **       On Windows, this is the notation for a drive.
   **   <li>Two leading separators.
   **       On Windows, this is the notation for a UNC.
   **   <li>A single leading separator.
   **       On Windows and POSIX, this is the notation for an absolute path.
   ** </ol>
   ** This method works identical on all platforms, so even if the separator is
   ** <code>'/'<code>, two leading separators would be considered to be a UNC
   ** and hence the return value would be <code>2</code>.
   **
   ** @param  path               the file system path.
   ** @param  separatorChar      the file name separator character in
   **                            <code>path</code>.
   **
   ** @return                    the number of characters in the prefix.
   **
   ** @throws NullPointerException if <code>path</code> is <code>null</code>.
   */
  private static int prefixLength(final String path, final char separatorChar) {
    // default prefix length
    int len = 0;
    if (path.length() > 0 && path.charAt(0) == separatorChar) {
      len++; // leading separator or first character of a UNC.
    }
    else if (path.length() > 1 && path.charAt(1) == ':') {
      final char drive = path.charAt(0);
      if ('A' <= drive && drive <= 'Z' || 'a' <= drive && drive <= 'z') { // US-ASCII letters only
        // Path is prefixed with drive, e.g. "C:\\Programs".
        len = 2;
      }
    }

    if (path.length() > len && path.charAt(len) == separatorChar)
      // next separator is considered part of prefix
      len++;

    return len;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativePath
  /**
   ** Returns the relative path of {@link File} <code>file</code> with respect
   ** to <code>base</code> directory.
   ** <p>
   ** Example:
   ** <pre>
   **   home = /a/b/c
   **   file = /a/d/e/x.txt
   **   s = getRelativePath(home,f) = ../../d/e/x.txt
   ** </pre>
   **
   ** @param  file               the {@link File} path to relativize the path to
   **                            <code>base</code> for.
   ** @param  base               the base path, should be a directory, not a
   **                            file, or it doesn't make sense.
   **
   ** @return                    path from <code>file</code> to
   **                            <code>home</code> as a string.
   **
   ** @throws IOException        if <code>file</code> isn't relative to home.
   **                            This exception you will recieve especially on
   **                            Windows OS if <code>file</code> and
   **                            <code>home</code> are on different drives.
   */
  public static String relativePath(final File file, final File base)
    throws IOException {

    return matchPath(pathList(file), pathList(base));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   matchPath
  /**
   ** Rigure out a string representing the relative path of <code>file</code>
   ** with respect to <code>base</code>
   **
   ** @param  filePath           the path component to relativize to the path
   **                            component of <code>homePath</code>.
   ** @param  basePath           the path component, should be a directory, not
   **                            a file, or it doesn't make sense.
   **
   ** @return                    path from <code>filePath</code> to
   **                            <code>homePath</code> as a string.
   **
   ** @throws IOException        if <code>file</code> isn't relative to home.
   **                            This exception you will recieve especially on
   **                            Windows OS if <code>file</code> and
   **                            <code>home</code> are on different drives.
   */
  public static String matchPath(final String[] filePath, final String[] basePath)
    throws IOException {

    // start at the beginning of the arrays and iterate while both lists are
    // equal this eliminates common root by comparing as long it goes
    int   count = 0;
    String file = filePath[count];
    String base = basePath[count];
    while ((count < filePath.length - 1) && (count < basePath.length - 1) && file.equals(base)) {
      count++;
      file = filePath[count];
      base = basePath[count];
    }
    if (file.equals(base))
      count++;

    // for each remaining level in the file path, add a ..
    StringBuilder path = new StringBuilder();
    for (int i = count; i < basePath.length; i++)
      path.append("..").append(File.separatorChar);

    // if we are in the same directory
    if (path.length() == 0)
      path.append(ClassUtility.PACKAGE_SEPARATOR_CHAR).append(File.separatorChar);

    // for each level in the file path, add the path
    for (int i = count; i < filePath.length - 1; i++)
      path.append(filePath[i]).append(File.separatorChar);

    // file name
    if (count < filePath.length)
      path.append(filePath[filePath.length - 1]);

    // just to test convert the string to a file and resolve it to a canonical
    // path. This conversation will throw an IOException
    new File(path.toString()).getCanonicalFile();

    return path.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pathList
  /**
   ** Break a path down into individual elements and add to a list.
   ** <p>
   ** Example: If a path is /a/b/c/d.txt, the breakdown will be [d.txt,c,b,a].
   **
   ** @param  file               the input file.
   **
   ** @return                    an string array with the individual elements of
   **                            the path in reverse order
   */
  public static String[] pathList(final File file) {
    // Windows OS seems in some cases not to stop getParent() at 'c:\', which
    // we considered to be root. For that reason we had to tweak in the
    // following to 'ugly' line:
    // file.getCanonicalFile().toURI();
    // build the path stack info to compare it afterwards
    return file.toURI().getPath().split("/");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInputStream
  /**
   ** Return an <code>InputStream</code> for reading the passed filename.
   ** <br>
   ** At first the method tries to open a file with the passed name and if this
   ** fails tries to convert to an <code>URL</code> using <code>getURL</code>
   ** and then calls <code>openStream</code> on the resulting <code>URL</code>
   ** if any.
   **
   ** @param  path               the name of the file to find.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
   */
  public static InputStream getInputStream(final String path) {
    return getInputStream(path, (Class)null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInputStream
  /**
   ** Return an <code>InputStream</code> for reading the passed filename.
   ** <br>
   ** At first the method tries to open a file with the passed name and if this
   ** fails tries to convert to an <code>URL</code> using <code>getURL</code>
   ** and then calls <code>openStream</code> on the resulting <code>URL</code>
   ** if any.
   **
   ** @param  path               the name of the file to find.
   ** @param  object             the object whose classloader should be used
   **                            when converting to url.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
   */
  public static InputStream getInputStream(final String path, final Object object) {
    return getInputStream(path, object == null ? null : object.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInputStream
  /**
   ** Return an <code>InputStream</code> for reading the passed filename.
   ** <br>
   ** At first the method tries to open a file with the passed name and if this
   ** fails tries to convert to an <code>URL</code> using <code>getURL</code>
   ** and then calls <code>openStream</code> on the resulting <code>URL</code>
   ** if any.
   **
   ** @param  path               the name of the file to find.
   ** @param  clazz              the class whose classloader should be used
   **                            when converting to url.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
   */
  public static InputStream getInputStream(final String path, final Class<?> clazz) {
    try {
      return new FileInputStream(path);
    }
    catch (Exception e) {
      // ignore it
      ;
    }

    URL url = getURL(path, clazz);
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
  // Method:   getInputStream
  /**
   ** Return an <code>InputStream</code> for reading the passed filename.
   ** <br>
   ** At first the method tries to open a file with the passed name and if this
   ** fails tries to convert to an <code>URL</code> using <code>getURL</code>
   ** and then calls <code>openStream</code> on the resulting <code>URL</code>
   ** if any.
   **
   ** @param  path               the name of the file to find.
   ** @param  loader             the classloader which should be used when
   **                            converting to url.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
   */
  public static InputStream getInputStream(final String path, final ClassLoader loader) {
    URL url = getURL(path, loader);
    try {
      return url == null ? null : url.openStream();
    }
    catch (IOException e) {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getURL
  /**
   ** Convert a <code>fileName</code> to an URL.
   ** <br>
   ** The file is searched by using the following approach:
   ** <ol>
   **   <li>Try and get the file as an <code>URL</code></li>
   **   <li>Replace any "." with "/" in a dotted name</li>
   **   <li>Try and get the file as a resource on the classpath</li>
   **   <li>Try and get the file from a list <code>URL</code>s by appending the file name to them
   ** </ol>
   ** The classloader for the <code>FileSystem</code> object is used when
   ** attempting to convert the URL.
   **
   ** @param  path               the name of the file to find.
   **
   ** @return                    the <code>URL</code> object for the file or
   **                            <code>null</code> if not found.
   */
  public static URL getURL(final String path) {
    return getURL(path, (Class)null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getURL
  /**
   ** Convert a <code>fileName</code> to an URL.
   ** <br>
   ** The file is searched by using the following approach:
   ** <ol>
   **   <li>Try and get the file as an <code>URL</code></li>
   **   <li>Replace any "." with "/" in a dotted name</li>
   **   <li>Try and get the file as a resource on the classpath</li>
   **   <li>Try and get the file from a list <code>URL</code>s by appending the file name to them
   ** </ol>
   ** The classloader for the <code>FileSystem</code> object is used when
   ** attempting to convert the URL.
   **
   ** @param  path               the name of the file to find.
   ** @param  object             the object whose classloader should be used
   **                            when converting to url.
   **
   ** @return                    the <code>URL</code> object for the file or
   **                            <code>null</code> if not found.
   */
  public static URL getURL(final String path, final Object object) {
    return getURL(path, object == null ? null : object.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getURL
  /**
   ** Convert a <code>path</code> to an URL.
   ** <br>
   ** The file is searched by using the following approach:
   ** <ol>
   **   <li>Try and get the file as an <code>URL</code></li>
   **   <li>Replace any "." with "/" in a dotted name</li>
   **   <li>Try and get the file as a resource on the classpath</li>
   **   <li>Try and get the file from a list <code>URL</code>s by appending the file name to them
   ** </ol>
   ** The classloader for the <code>FileSystem</code> object is used when
   ** attempting to convert the URL.
   **
   ** @param  path               the name of the file to find.
   ** @param  clazz              the class whose classloader should be used
   **                            when converting to url.
   **
   ** @return                    the <code>URL</code> object for the file or
   **                            <code>null</code> if not found.
   */
  public static URL getURL(final String path, final Class<?> clazz) {
    URL url = null;
    // if the path isn't malformed lines below will be successful convert to
    // an url
    try {
      url = new URL(path);
      return url;
    }
    catch (Exception e) {
      // maybe it was malformed
      url = null;
    }
    // if the path was malformed lines below change the protocol to file and
    // try to convert the filename to an url.
    // the expansion to a file url produce a wellformed url which is not enough
    // to have the correct resource.
    // the check is only complete if we can obtain a handle to the specified
    // resource which will be done by open an inputstrem on the url.
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
    url = getURL(path, FileSystem.class.getClassLoader());
    return url;
  }

   //////////////////////////////////////////////////////////////////////////////
  // Method:   getURL
  /**
   ** Convert a <code>fileName</code> to an URL.
   ** <br>
   ** The file is searched as a resource on the classpath. If the passed
   ** classloader is null the context or system classloader is used.
   **
   ** @param  path               the name of the file to find.
   ** @param  loader             the classloader which should be used when
   **                            converting to url.
   **
   ** @return                    the <code>URL</code> object for the file or
   **                            <code>null</code> if not found.
   */
  public static URL getURL(final String path, ClassLoader loader) {
    if (loader == null) {
      loader = Thread.currentThread().getContextClassLoader();
      if (loader == null)
        loader = ClassLoader.getSystemClassLoader();
    }

    // try to load the file from classloader
    return loader.getResource(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** This is a recursive call: The top level call should provide <code>0</code>
   ** as the <code>skip<code> parameter, the length of the path as the
   ** <code>end</code> parameter and an empty string buffer as the
   ** <code>result</code> parameter.
   **
   ** @param  collapse           the number of adjacent <i>dir/..</i> segments
   **                            in the path to collapse.
   **                            This value must not be negative.
   ** @param  end                the current position in <code>path<code>.
   **                            Only the string to the left of this index is
   **                            considered. If not positive, nothing happens.
   ** @param  buffer             the non-<code>null</code> string buffer for the
   **                            result.
   **
   ** @return                    the number of adjacent segments in the path
   **                            which have <em>not</em> been collapsed at this
   **                            position.
   */
  private static int normalize(final String path, final char separatorChar, final int collapse, final int end, final StringBuilder buffer) {
    assert collapse >= 0;
    if (0 >= end)
      return collapse;

    final int next = path.lastIndexOf(separatorChar, end - 1);
    final String base = path.substring(next + 1, end);
    int notCollapsed;
    if (0 >= base.length() || ".".equals(base)) {
      return normalize(path, separatorChar, collapse, next, buffer);
    }
    else if ("..".equals(base)) {
      notCollapsed = normalize(path, separatorChar, collapse + 1, next, buffer) - 1;
      if (0 > notCollapsed)
        return 0;
    }
    else if (0 < collapse) {
      return normalize(path, separatorChar, collapse - 1, next, buffer);
    }
    else {
      assert 0 == collapse;
      notCollapsed = normalize(path, separatorChar, 0, next, buffer);
      assert 0 == notCollapsed;
    }

    final int bufferLen = buffer.length();
    if (bufferLen > 0)
      buffer.append(separatorChar);
    buffer.append(base);
    return notCollapsed;
  }
}