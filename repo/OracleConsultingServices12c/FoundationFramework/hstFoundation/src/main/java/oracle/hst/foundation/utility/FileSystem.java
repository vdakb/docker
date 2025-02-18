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

    System      :   Foundation Shared Library
    Subsystem   :   Common shared naming facilities

    File        :   FileSystem.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FileSystem.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import java.text.SimpleDateFormat;

import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import java.nio.charset.StandardCharsets;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

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
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PROTOCOL       = "file";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

   /** cached system property for the class path */
   private static String   classPath        = null;

  /**
   ** the list of application wide visible search pathes.
   ** <b>NOTE:</b>
   ** Any entry should be end with "/"
   */
  private static List<URL> globalSearchPath = Collections.synchronizedList(new ArrayList<URL>());

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
  // Method:  classPath
  /**
   ** Queries the system properties for the class path.
   **
   ** @return                    the class path.
   */
  public static String classPath() {
    if (classPath == null)
      classPath = System.getProperty("java.class.path");

    return System.getProperty("java.class.path");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userHome
  /**
   ** Queries the system properties for the user's home directory.
   **
   ** @return                    the user's home directory.
   */
  public static String userHome() {
    return System.getProperty("user.home");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingFolder
  /**
   ** Queries the system properties for the user's current working directory.
   **
   ** @return                    the user's current working directory.
   */
  public static String workingFolder() {
    return System.getProperty("user.dir");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   temporaryFolder
  /**
   ** Queries the system properties for the default temp file path.
   **
   ** @return                    the default temp file path.
   */
  public static String temporaryFolder() {
    return System.getProperty("java.io.tmpdir");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addGlobalSearchPath
  /**
   ** Add a URL to the global list that is setup when a <code>FileSystem</code>
   ** is created.
   **
   ** @param  path               a URL path to search.
   **
   ** @throws MalformedURLException if the URL string is invalid or doesn't end
   **                               with a "/".
   */
  public static void addGlobalSearchPath(final String path)
    throws MalformedURLException {

    addPathToList(globalSearchPath, path);
  }

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
   ** @throws NullPointerException if path is <code>null</code>.
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
  // Method:   pathList
  /**
   ** Break a path down into individual elements and add to a list.
   ** <p>
   ** Example: If a path is /a/b/c/d.txt, the breakdown will be [d.txt,c,b,a].
   **
   ** @param  file               the input file.
   **
   ** @return                    a {@link List} collection with the individual
   **                            elements of the path in reverse order
   */
  public static String[] pathList(final File file) {
    // Windows OS seems in some cases not to stop getParent() at 'c:\', which
    // we considered to be root. For that reason we had to tweak in the
    // following 'ugly' line:
    // file.getCanonicalFile().toURI();
    // build the path stack info to compare it afterwards
    return file.toURI().getPath().split("/");
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
      path.append(SystemConstant.PERIOD).append(File.separatorChar);

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
  // Method:   findFileOnClassPath
  /**
   ** Returns a reference to a file with the specified name that is located
   ** somewhere on the classpath.
   **
   ** @param  name               the filename to search for.
   **
   ** @return                    a reference to a file or <code>null</code> if
   **                            no file could be found.
   */
  public static File findFileOnClassPath(final String name) {
    final StringTokenizer tokenizer = new StringTokenizer(classPath(), SystemConstant.PATHBREAK);
    while (tokenizer.hasMoreTokens()) {
      final String pathElement            = tokenizer.nextToken();
      final File   directoryOrJar         = new File(pathElement);
      final File   absoluteDirectoryOrJar = directoryOrJar.getAbsoluteFile();

      if (absoluteDirectoryOrJar.isFile()) {
        final File target = new File(absoluteDirectoryOrJar.getParent(), name);
        if (target.exists())
          return target;
      }
      else {
        final File target = new File(directoryOrJar, name);
        if (target.exists()) {
          return target;
        }
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dropFileExtension
  /**
   ** Removes the file extension from the given file name.
   **
   ** @param  file               the file name.
   ** @return                    the file name without the file extension.
   */
  public String dropFileExtension(final String file) {
    final int idx = file.lastIndexOf(SystemConstant.PERIOD);
    // handles unix hidden files and files without an extension.
    if (idx < 1)
      return file;

    return file.substring(0, idx);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fileExtension
  /**
   ** Returns the file extension of the given file name.
   ** <br>
   ** The returned value will contain the dot.
   **
   ** @param  file               the file name.
   **
   ** @return                    the file extension.
   */
  public String fileExtension(final String file) {
    final int idx = file.lastIndexOf(SystemConstant.PERIOD);
    // handles unix hidden files and files without an extension.
    if (idx < 1)
      return SystemConstant.EMPTY;

    return file.substring(idx);
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
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
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
   ** @param  object             the object whose classloader should be used
   **                            when converting to {@link URL}.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
   */
  public static InputStream inputStream(final String path, final Object object) {
    return inputStream(path, object == null ? (Class<?>)null : object.getClass());
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
   ** @param  clazz              the class whose classloader should be used
   **                            when converting to {@link URL}.
   **
   ** @return                    a <code>InputStream</code> or <code>null</code>
   **                            if the file cannot be opened.
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
  // Method:   inputStream
  /**
   ** Return an {@link InputStream} for reading the passed filename.
   ** <br>
   ** At first the method tries to open a file with the passed name and if this
   ** fails tries to convert to an {@link URL} using
   ** {@link #url(String, ClassLoader)} and then calls {@link URL#openStream()}
   ** on the resulting {@link URL} if any.
   **
   ** @param  path               the name of the file to find.
   ** @param  loader             the classloader which should be used when
   **                            converting to {@link URL}.
   **
   ** @return                    an {@link InputStream} or <code>null</code> if
   **                            the file cannot be opened.
   */
  public static InputStream inputStream(final String path, final ClassLoader loader) {
    URL url = url(path, loader);
    try {
      return url == null ? null : url.openStream();
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
   **
   ** @return                    the {@link URL} object for the file or
   **                            <code>null</code> if not found.
   */
  public static URL url(final String path) {
    return url(path, (Class)null);
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
   ** attempting to convert the URL.
   **
   ** @param  path               the name of the file to find.
   ** @param  object             the object whose classloader should be used
   **                            when converting to {@link URL}.
   **
   ** @return                    the {@link URL} object for the file or
   **                            <code>null</code> if not found.
   */
  public static URL url(final String path, final Object object) {
    return url(path, object == null ? (Class)null : object.getClass());
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
   ** @param  clazz              the class whose classloader should be used
   **                            when converting to {@link URL}.
   **
   ** @return                    the {@link URL} object for the file or
   **                            <code>null</code> if not found.
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
    // try to load the file global search path
    if (url == null) {
      for (Iterator<URL> i = globalSearchPath.iterator(); i.hasNext(); ) {
        try {
          url = new URL(i.next().getPath() + path);
        }
        catch (Exception e) {
          ;
        }
      }
    }
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
   ** @param  loader             the classloader which should be used when
   **                            converting to {@link URL}.
   **
   ** @return                    the {@link URL} object for the file or
   **                            <code>null</code> if not found.
   */
  public static URL url(final String path, ClassLoader loader) {
    if (loader == null) {
      loader = Thread.currentThread().getContextClassLoader();
      if (loader == null)
        loader = ClassLoader.getSystemClassLoader();
    }

    // try to load the file from classloader
    URL url = loader.getResource(path);
    if (url == null) {
      // try to load the file from class loader by prepending the global search
      // path names
      for (Iterator<URL> i = globalSearchPath.iterator(); i.hasNext(); ) {
        url =loader.getResource(i.next().getPath() + path);
        if (url != null)
          break;
      }
    }
    return url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   directoryValid
  /**
   ** Is the passed String a valid directory and is a file and is readable.
   **
   ** @param  directory          the name of the directory to check
   **
   ** @return                    whether the passed <code>directory</code> is a
   **                            directory.
   */
  public static boolean directoryValid(final String directory) {
   // we are a little bit pessimistic
    boolean result = false;
    if (!StringUtility.isEmpty(directory)) {
      // create an abstact file
      File dir = new File(directory);
      // check if file exists and is a directory
      result = (dir.exists() && dir.isDirectory());
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fileValid
  /**
   ** Is the passed String a valid filename and is a file and is readable.
   **
   ** @param  filename           the name of the file to check.
   **
   ** @return                    whether the passed <code>filename</code> is a
   **                            file.
   */
  public static boolean fileValid(final String filename) {
   // we are a little bit pessimistic
    boolean result = false;
    if (!StringUtility.isEmpty(filename)) {
      // check if file exists and is a file and readable
      result = fileValid(new File(filename));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fileValid
  /**
   ** Is the passed String a valid filename and is a file and is readable.
   **
   ** @param  file               the {@link File} path to check.
   **
   ** @return                    whether the passed <code>filename</code> is a
   **                            file.
   */
  public static boolean fileValid(final File file) {
    return (file.exists() && file.isFile() && file.canRead());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toFile
  /**
   ** Convert a {@link URL} to a {@link File} path.
   **
   ** @param  url                the {@link URL} to convert to {@link File}.
   **
   ** @return                    the {@link File} path for the given
   **                            {@link URL}.
   */
  public static File toFile(final URL url) {
    URI uri;
    try {
      // this is the step that can fail, and so
      // it should be this step that should be fixed
      uri = url.toURI();
    }
    catch (URISyntaxException e) {
      // OK if we are here, then obviously the URL did not comply with RFC 2396.
      // This can only happen if we have illegal unescaped characters. If we
      // have one unescaped character, then the only automated fix we can apply,
      // is to assume all characters are unescaped. If we want to construct a
      // URI from unescaped characters, then we have to use the component
      // constructors:
      try {
        uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
      }
      catch (URISyntaxException e1) {
        // the URL is broken beyond automatic repair
        throw new IllegalArgumentException("Broken URL: " + url);
      }
    }
    return new File(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a path into its parent path and its base name, recognizing platform
   ** specific file system roots.
   ** <p>
   ** Equivalent to {@link #split(String, char)
   ** split(path, File.separatorChar)}.
   **
   ** @param  path               the name of the path which's parent path and
   **                            base name are to be returned.
   **
   ** @return                    An array holding at least two strings:
   **                            <ol>
   **                              <li>Index zero holds the parent path or
   **                                  <code>null</code> if the path does not
   **                                  specify a parent. This name compares
   **                                  equal with
   **                                  {@link java.io.File#getParent()}.
   **                              <li>Index one holds the base name. This name
   **                                  compares equal with
   **                                  {@link java.io.File#getName()}.
   **                            </ol>
   **
   ** @throws NullPointerException if path is <code>null</code>.
   */
  public static String[] split(final String path) {
    return split(path, File.separatorChar);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a path into its parent path and its base name, recognizing platform
   ** specific file system roots.
   ** <p>
   ** Equivalent to {@link #split(String, char, String[])
   ** split(path, separatorChar, new String[2])}.
   **
   ** @param  path               the name of the path which's parent path and
   **                            base name are to be returned.
   ** @param  separatorChar      the path separator character to use for this
   **                            operation.
   **
   ** @return                    An array holding at least two strings:
   **                            <ol>
   **                              <li>Index zero holds the parent path or
   **                                  <code>null</code> if the path does not
   **                                  specify a parent. This name compares
   **                                  equal with
   **                                  {@link java.io.File#getParent()}.
   **                              <li>Index one holds the base name. This name
   **                                  compares equal with
   **                                  {@link java.io.File#getName()}.
   **                            </ol>
   **
   ** @throws NullPointerException if path is <code>null</code>.
   */
  public static String[] split(final String path, final char separatorChar) {
    return split(path, separatorChar, new String[2]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a path into its parent path and its base name, recognizing platform
   ** specific file system roots.
   **
   ** @param  path               the name of the path which's parent path and
   **                            base name are to be returned.
   ** @param  separatorChar      the path separator character to use for this
   **                            operation.
   ** @param result              an array of at least two {@link String}
   **                            elements to hold the result upon return.
   **
   ** @return                    An array holding at least two strings:
   **                            <ol>
   **                              <li>Index zero holds the parent path or
   **                                  <code>null</code> if the path does not
   **                                  specify a parent. This name compares
   **                                  equal with
   **                                  {@link java.io.File#getParent()}.
   **                              <li>Index one holds the base name. This name
   **                                  compares equal with
   **                                  {@link java.io.File#getName()}.
   **                            </ol>
   **
   ** @throws NullPointerException if path is <code>null</code>.
   */
  public static String[] split(final String path, final char separatorChar, final String[] result) {
    final int prefixLen = prefixLength(path, separatorChar);

    // skip any trailing separators and look for the previous separator.
    int baseBegin = -1;
    int baseEnd   = path.length() - 1;
    if (prefixLen <= baseEnd) {
      baseEnd = lastIndexNot(path, separatorChar, baseEnd);
      baseBegin = path.lastIndexOf(separatorChar, baseEnd);
    }
    baseEnd++; // convert end index to interval boundary

    // Finally split according to our findings.
    // found separator after the prefix?
    if (baseBegin >= prefixLen) {
      final int parentEnd = lastIndexNot(path, separatorChar, baseBegin) + 1;
      // include separator, may produce separator only!
      result[0] = path.substring(0, parentEnd > prefixLen ? parentEnd : prefixLen);
      // between separator and trailing separator
      result[1] = path.substring(baseBegin + 1, baseEnd);
    }
    // no separator after prefix
    else {
      // prefix exists and we have more?
      if (0 < prefixLen && prefixLen < baseEnd)
        // prefix is parent
        result[0] = path.substring(0, prefixLen);
      else
        // no parent
        result[0] = null;
      result[1] = path.substring(prefixLen, baseEnd);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copyDirectory
  /**
   ** Copy directory contents
   **
   ** @param  fromDir            the abstract file where to copy from.
   ** @param  toDir              the abstract the file where to copy to.
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
  public static void copyDirectory(final File fromDir, final File toDir)
    throws SystemException {

    String[] items = fromDir.list();
    if (items != null) {
      int count = items.length;
      for (int i = 0; i < count; i++) {
        String entryName = items[i];
        File entry = new File(fromDir, entryName);
        if (entry.isDirectory()) {
          File targetDir = new File(toDir, entryName);
          targetDir.mkdirs();
          // call recursivly
          copyDirectory(entry, targetDir);
        }
        else if (entry.isFile()) {
          String srcPath = entry.getAbsolutePath();
          String destPath = toDir.getAbsolutePath() + File.separatorChar + entryName;
          copy(srcPath, destPath);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backup
  //
  public static void backup(final String fileName, final String suffix)
    throws SystemException {

    // Obtain the current time
    Date             currentDate = new Date();
    SimpleDateFormat formatter   = new SimpleDateFormat("yyyyMMdd-HHmmss");

    // Create a name for the backup file
    String backupName = fileName + suffix + "-" + formatter.format(currentDate);
    // copy the existing file to the backuo
    copy(fileName, backupName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copy
  /**
   ** Performs the file copy.
   ** <br>
   ** Before copying the file, however, it performs a lot of tests to make sure
   ** everything is as it should be.
   **
   ** @param  fromFileName       the name of the file where to copy from.
   ** @param  toFileNname        the name of the file where to copy to.
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
 public static void copy(final String fromFileName, final String toFileNname)
   throws SystemException {

   // at first bogus invalid input
   if (StringUtility.isEmpty(fromFileName) || StringUtility.isEmpty(toFileNname))
     return;

   // Get File objects from Strings
   File  fromFile = new File(fromFileName);
   File  toFile   = new File(toFileNname);

    copy(fromFile, toFile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copy
  /**
   ** Performs the file copy.
   ** <br>
   ** Before copying the file, however, it performs a lot of tests to make sure
   ** everything is as it should be.
   **
   ** @param  fromFile           the abstract file where to copy from.
   ** @param  toFile             the abstract the file where to copy to.
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
  public static void copy(final File fromFile, File toFile)
    throws SystemException {

    copy(fromFile, toFile, "UTF-8");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copy
  /**
   ** Performs the file copy.
   ** <br>
   ** Before copying the file, however, it performs a lot of tests to make sure
   ** everything is as it should be.
   **
   ** @param  fromFile           the abstract file where to copy from.
   ** @param  toFile             the abstract the file where to copy to.
   ** @param  encoding           the name of a supported
   **                            {@link java.nio.charset.Charset charset}
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
  public static void copy(final File fromFile, File toFile, final String encoding)
    throws SystemException {

    // prevent bogus input
    if (fromFile == null || toFile == null)
      throw new SystemException(SystemError.ARGUMENT_IS_NULL, (fromFile == null) ? "fromFile" : "toFile");

    final String fromFileName = fromFile.getName();

    // First make sure the source file exists, is a file, and is readable.
    if (!fromFile.exists())
      throw new SystemException(SystemError.FILE_MISSING, fromFileName);
    if (!fromFile.isFile())
      throw new SystemException(SystemError.FILE_NOT_FILE, fromFileName);
    if (!fromFile.canRead())
      throw new SystemException(SystemError.FILE_READ, fromFileName);

    // If the destination is a directory, use the source file name
    // as the destination file name
    if (toFile.isDirectory())
      toFile = new File(toFile, fromFileName);

    // If the destination exists, make sure it is a writeable file
    // and ask before overwriting it.  If the destination doesn't
    // exist, make sure the directory exists and is writeable.
    if (toFile.exists()) {
      if (!toFile.canWrite())
        throw new SystemException(SystemError.FILE_WRITE, fromFileName);
   }

    // If we've gotten this far, then everything is okay.
    // So we copy the file, a buffer of bytes at a time.
    try {
      copy(new FileInputStream(fromFile), new FileOutputStream(toFile), encoding);
    }
    catch (IOException e) {
      throw new SystemException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copy
  /**
   ** Performs the file copy.
   ** <br>
   ** Before copying the file, however, it performs a lot of tests to make sure
   ** everything is as it should be.
   **
   ** @param  fromStream         the file stream where to copy from.
   ** @param  toStream           the file stream where to copy to.
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
  public static void copy(final FileInputStream fromStream, final FileOutputStream toStream)
    throws SystemException {

    // prevent bogus input
    if (fromStream == null || toStream == null)
      throw new SystemException(SystemError.ARGUMENT_IS_NULL, (fromStream == null) ? "fromStream" : "toStream");

    copy(new InputStreamReader(fromStream), new OutputStreamWriter(toStream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copy
  /**
   ** Performs the file copy.
   ** <br>
   ** Before copying the file, however, it performs a lot of tests to make sure
   ** everything is as it should be.
   **
   ** @param  fromStream         the file stream where to copy from.
   ** @param  toStream           the file stream where to copy to.
   ** @param  encoding           the name of a supported
   **                            {@link java.nio.charset.Charset charset}
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
  public static void copy(final FileInputStream fromStream, final FileOutputStream toStream, final String encoding)
    throws SystemException {

    // prevent bogus input
    if (fromStream == null || toStream == null)
      throw new SystemException(SystemError.ARGUMENT_IS_NULL, (fromStream == null) ? "fromStream" : "toStream");

    try {
      copy(new InputStreamReader(fromStream, encoding), new BufferedWriter(new OutputStreamWriter(toStream, encoding)));
    }
    catch (UnsupportedEncodingException e) {
      throw new SystemException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copy
  /**
   ** Performs the file copy.
   ** <br>
   ** Before copying the file, however, it performs a lot of tests to make sure
   ** everything is as it should be.
   **
   ** @param  reader             the {@link Reader} where to copy from.
   ** @param  writer             the {@link Writer} where to copy to.
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
  public static void copy(final Reader reader, final Writer writer)
    throws SystemException {

    // prevent bogus input
    if (reader == null || writer == null)
      throw new SystemException(SystemError.ARGUMENT_IS_NULL, (reader == null) ? "reader" : "writer");

    final char[] buffer = new char[10240];
    try {
      // how many bytes in buffer
      int bytesRead = 0;
      do {
        // read a chunk of bytes into the buffer, then write them out,
        // looping until we reach the end of the file (when read() returns -1).
        // Note the combination of assignment and comparison in this while
        // loop.  This is a common I/O programming idiom.
        bytesRead = reader.read(buffer, 0, buffer.length);
        // read bytes until EOF
        if (bytesRead == -1)
          break;
        writer.write(buffer, 0, bytesRead);
      }
      while (true);
    }
    catch (IOException e) {
      throw new SystemException(e);
    }
    // always close the streams, even if exceptions were thrown
    finally {
      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException e) {
          ;
        }
      if (writer != null)
        try {
          writer.flush();
          writer.close();
        }
        catch (IOException e) {
          ;
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTempFile
  /**
   ** Create a temporary file in the default temp file path.
   ** <br>
   ** Slightly smarter version of File.createTempFile
   **
   ** @param  prefix             beginning letters of filename
   ** @param  suffix             ending letters of filename.
   **
   ** @return                    a temporary file.
   **                            It will not automatically delete on program
   **                            completion, however.
   **
   ** @throws IOException       if the file could not be created or any other
   **                           I/O error occurs.
   */
  public static File createTempFile(final String prefix, final String suffix)
    throws IOException {

    return createTempFile(prefix, suffix, new File(temporaryFolder()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTempFile
  /**
   ** Create a temporary file
   ** <br>
   ** Slightly smarter version of File.createTempFile
   **
   ** @param  prefix             beginning letters of filename
   ** @param  suffix             ending letters of filename.
   ** @param  where              directory where to put file, or file to place
   **                            this temp file near in the same directory.
   **                            <code>null</code> means put the temp file in
   **                            the current directory.
   **
   ** @return                    a temporary file.
   **                            It will not automatically delete on program
   **                            completion, however.
   **
   ** @throws IOException       if the file could not be created or any other
   **                           I/O error occurs.
   */
  public static File createTempFile(final String prefix, final String suffix, final File where)
    throws IOException {

    if (where != null) {
      if (where.isDirectory())
        return File.createTempFile(prefix, suffix, where);

      if (where.isFile()) {
        String parent = where.getParent();
        if (parent != null) {
          File dir = new File(parent);
          if (dir.isDirectory())
            return File.createTempFile(prefix, suffix, dir);
        }
      }
    }

    // anything else, just create in the current directory.
    return File.createTempFile(prefix, suffix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes the file or directory denoted by the specified abstract pathname
   ** <code>file</code>.
   ** <br>
   ** If this pathname denotes a directory, then the directory is deleted
   ** recursivly at first.
   **
   ** @param  file               the file or diractory to dleete.
   **
   ** @return                    <code>true</code> if and only if the file or
   **                            directory is successfully deleted;
   **                            <code>false</code> otherwise
   */
  public static boolean delete(File file) {
    // check if something is to do
    if (!file.exists())
      return true;
    // go an extra mile for diractories
    if (file.isDirectory()) {
      final File[] files = file.listFiles();
      if (files != null) {
        for (File cursor : files) {
          delete(cursor);
        }
      }
    }
    return file.delete();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntireFile
  /**
   ** Get all text in a file.
   **
   ** @param fromFile            the name of file where to get the text.
   **
   ** @return                    all the text in the File.
   **
   ** @throws SystemException    if the file isn't readable or any other I/O
   **                            error occurs.
   */
  public static String readEntireFile(final String fromFile)
    throws SystemException {

    return readEntireFile(new File(fromFile));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntireFile
  /**
   ** Get all text in a file.
   **
   ** @param  from              file where to get the text.
   **
   ** @return                    all the text in the File.
   **
   ** @throws SystemException    if the file isn't readable or any other I/O
   **                            error occurs.
   */
  public static String readEntireFile(final File from)
    throws SystemException {

    // decode with default encoding and return entire file as one big string
    return new String(readFile(from));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntireFile
  /**
   ** Get all text in a file.
   **
   ** @param  fromFile           the name of file where to get the text.
   ** @param  encoding           name of the encoding to use to translate the
   **                            bytes in the file into chars e.g.
   **                            "windows-1252" "UTF-8" "UTF-16"
   **
   ** @return                    all the text in the File.
   **
   ** @throws SystemException              if the file isn't readable or any
   **                                      other I/O error occurs.
   ** @throws UnsupportedEncodingException if the named encoding is not
   **                                      supported
   */
  public static String readEntireFile(final String fromFile, final String encoding)
    throws SystemException
    ,      UnsupportedEncodingException {

    return readEntireFile(new File(fromFile), encoding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEntireFile
  /**
   ** Get all text in a file.
   **
   ** @param  fromFile           file where to get the text.
   ** @param  encoding           name of the encoding to use to translate the
   **                            bytes in the file into chars e.g.
   **                            "windows-1252" "UTF-8" "UTF-16".
   **
   ** @return                    all the text in the File.
   **
   ** @throws SystemException              if the file isn't readable or any
   **                                      other I/O error occurs.
   ** @throws UnsupportedEncodingException if the named encoding is not
   **                                      supported
   */
  public static String readEntireFile(final File fromFile, final String encoding)
    throws SystemException
    ,      UnsupportedEncodingException {

    // decode with encoding and return entire file as one big string
    return new String(readFile(fromFile), encoding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFile
  /**
   ** Read file of bytes without conversion.
   **
   ** @param  fileName           file to read
   **
   ** @return                    byte array representing whole file.
   **
   ** @throws SystemException    if the file isn't readable or any other I/O
   **                            error occurs.
   */
  public static byte[] readFile(final String fileName)
    throws SystemException {

    return readFile(new File(fileName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFile
  /**
   ** Read file of bytes without conversion.
   **
   ** @param  from               file to read.
   **
   ** @return                    byte array representing whole file.
   **
   ** @throws SystemException    if the file isn't readable or any other I/O
   **                            error occurs.
   */
  public static byte[] readFile(final File from)
    throws SystemException {

    try {
      return Files.readAllBytes(from.toPath());
    }
    catch (IOException e) {
      throw new SystemException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFile
  /**
   ** Read file of bytes without conversion.
   **
   ** @param  from               file to read.
   ** @param  size               the expected amount of bytes to read.
   **
   ** @return                    byte array representing whole file.
   **
   ** @throws SystemException    if the file isn't readable or any other I/O
   **                            error occurs.
   */
  public static byte[] readFile(final InputStream from, final int size)
    throws SystemException {

    final byte[] contents   = new byte[size];
    try {
      int read = from.read(contents);
      if (read != size)
        throw new SystemException(SystemError.FILE_READ, "from");
    }
    catch (IOException e) {
      throw new SystemException(e);
    }
    return contents;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readStream
  /**
   ** Read file of bytes without conversion.
   **
   ** @param  stream             the stream to read.
   **
   ** @return                    the content of the stream.
   **
   ** @throws SystemException    if the file isn't readable or any other I/O
   **                            error occurs.
   */
  public static String readStream(final InputStream stream)
    throws SystemException {

    final StringBuilder builder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
    }
    catch (IOException e) {
      throw new SystemException(e);
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntireFile
  /**
   ** Write all the text in a file
   **
   ** @param  toFile             file where to write the text
   ** @param  text               the text to write
   **
   ** @throws SystemException              if an I/O error occurs.
   ** @throws UnsupportedEncodingException if the named encoding is not
   **                                      supported.
   */
  public static void writeEntireFile(final String toFile, final String text)
    throws SystemException
    ,      UnsupportedEncodingException {

    writeEntireFile(new File(toFile), text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntireFile
  /**
   ** Write all the text in a file
   **
   ** @param  toFile             file where to write the text
   ** @param  text               the text to write
   **
   ** @throws SystemException    if an I/O error occurs.
   */
  public static void writeEntireFile(final File toFile, final String text)
    throws SystemException {

    try {
      Files.write(toFile.toPath(), text.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    catch (IOException e) {
      throw new SystemException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntireFile
  /**
   ** Write all the text in a file
   **
   ** @param  toFile             file where to write the text
   ** @param  text               the text to write
   ** @param  encoding           name of the encoding to use to translate the
   **                            bytes in the file into chars e.g.
   **                            "windows-1252" "UTF-8" "UTF-16".
   **
   ** @throws SystemException              if an I/O error occurs.
   ** @throws UnsupportedEncodingException if the named encoding is not
   **                                      supported
   */
  public static void writeEntireFile(final String toFile, final String text, final String encoding)
    throws SystemException
    ,      UnsupportedEncodingException {

    writeEntireFile(new File(toFile), text, encoding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntireFile
  /**
   ** Write all the text in a file
   **
   ** @param  toFile             file where to write the text
   ** @param  text               the text to write
   ** @param  encoding           name of the encoding to use to translate the
   **                            bytes in the file into chars e.g.
   **                            "windows-1252" "UTF-8" "UTF-16".
   **
   ** @throws SystemException              if an I/O error occurs.
   ** @throws UnsupportedEncodingException if the named encoding is not
   **                                      supported.
   */
  public static void writeEntireFile(final File toFile, final String text, final String encoding)
    throws SystemException
    ,      UnsupportedEncodingException {

    OutputStreamWriter writer = null;
    try {
      // supplied encoding
      writer = new OutputStreamWriter(new FileOutputStream(toFile), encoding);
      writer.write(text);
    }
    catch (IOException e) {
      throw new SystemException(e);
    }
    finally {
      if (writer != null)
        try {
          writer.close();
        }
        catch (IOException e) {
          // die silently
          ;
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPathToList
  /**
   ** Add a URL to a list.
   **
   ** @param  path               a URL path to search.
   **
   ** @throws MalformedURLException if the URL string is invalid or doesn't end
   **                               with a "/".
   */
  private static void addPathToList(final List<URL> list, final String path)
    throws MalformedURLException {

    // create a buffer which is big enough to contain the whole string for the
    // URL to build
    StringBuilder buffer = new StringBuilder( PROTOCOL.length()
                                            + path.length()
                                            // "://"
                                            + File.separator.length() * 2 + 1
                                            );

    // 1st prepend the protocol to path to get a wellformed URL
    if (!path.startsWith(PROTOCOL)) {
      buffer.append(PROTOCOL);
      buffer.append(SystemConstant.COLON);
      buffer.append(SystemConstant.SLASH);
      buffer.append(SystemConstant.SLASH);
    }

    // 2nd append the path themself to the buffer
    buffer.append(path);

    // 3rd append a filepath separator if path doesn't end with it
    if (!path.endsWith("/") && !path.endsWith(File.separator))
      buffer.append(File.separator);

    // 4th create an URL to check if the result is wellformed
    URL url = new URL(buffer.toString());

    // now we can add the url to the list
    if (!list.contains(url))
      list.add(0, url);
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

  private static int lastIndexNot(final String path, final char separatorChar, int last) {
    while (path.charAt(last) == separatorChar && --last >= 0)
      ;
    return last;
  }
}