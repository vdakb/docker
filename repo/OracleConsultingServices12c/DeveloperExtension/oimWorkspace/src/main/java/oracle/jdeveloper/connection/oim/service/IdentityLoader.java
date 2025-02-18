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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   IdentityLoader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityLoader.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.service;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Set;

import java.net.URL;
import java.net.URLClassLoader;

import oracle.jdeveloper.library.JLibrary;
import oracle.jdeveloper.library.JLibraryList;
import oracle.jdeveloper.library.JLibraryManager;

import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

import oracle.jdeveloper.connection.oim.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityLoader
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>IdentityLoader</code> implements a class loader implementation
 ** that loads classes from JAR files. All instances share the same set of
 ** classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityLoader extends URLClassLoader {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String  CLASS      = "oracle.iam.platform.OIMClient";
  static final String  LIBRARY    = "oracle-oim-12c-console";
  static final String  METHOD     = "addURL";

  static final Class[] parameters = new Class[]{URL.class};

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@link URLClassLoader} for the specified URLs using the
   ** default delegation parent {@code ClassLoader}. The URLs will be searched
   ** in the order specified for classes and resources after first searching in
   ** the parent class loader. Any URL that ends with a '/' is assumed to refer
   ** to a directory. Otherwise, the URL is assumed to refer to a JAR file which
   ** will be downloaded and opened as needed.
   ** <p>
   ** If there is a security manager, this method first calls the security
   ** manager's <code>checkCreateClassLoader</code> method to ensure creation of
   ** a class loader is allowed.
   **
   ** @param  path               the URLs from which to load classes and
   **                            resources.
   **
   ** @throws SecurityException  if a security manager exists and its
   **                            <code>checkCreateClassLoader</code> method
   **                            doesn't allow creation of a class loader.
   ** @throws NullPointerException if <code>urls</code> is <code>null</code>.
   */
  public IdentityLoader(final URL[] path) {
    // ensure inheritance
    super(path, Thread.currentThread().getContextClassLoader().getParent());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patch
  /**
   ** Extends the class path of the current thread to be able to invoke Identity
   ** Manager client API's.
   **
   ** @return                    <code>true</code> if the class
   **                            <code>oracle.iam.platform.OIMClient</code>
   **                            could be resolved; otherwise <code>false</code>
   **
   ** @throws IdentityServiceException if the {@link Method} is enforcing Java
   **                                  language access control and the method is
   **                                  inaccessible or the method itself throws
   **                                  an exception.
   */
  public static ClassLoader patch()
    throws IdentityServiceException {

    final JLibraryList usr = JLibraryManager.getUserLibraries();
    final JLibrary     lib = usr.findLibrary(usr.createIDFromName(LIBRARY, false));
    if (lib == null)
      throw new IdentityServiceException(Bundle.format(Bundle.RUNTIME_LIBRARY_NOTFOUND, LIBRARY));
    
//    return new IdentityLoader(lib.getClassPath().getEntries());

//    final ClassLoader    sys = Thread.currentThread().getContextClassLoader();
    URLClassLoader sys = (URLClassLoader)Thread.currentThread().getContextClassLoader().getParent();
    try {
      final Method method = URLClassLoader.class.getDeclaredMethod(METHOD, parameters);
      method.setAccessible(true);
      
      // extend the runtime class path dynamically
      sys = extendPath(method, sys, LIBRARY);
      // verify if the client is available
      Class.forName(CLASS);
    }
    catch (NoSuchMethodException e) {
      throw new IdentityServiceException(Bundle.format(Bundle.RUNTIME_METHOD_NOTFOUND, e.getLocalizedMessage()));
    }
    catch (ClassNotFoundException e) {
      throw new IdentityServiceException(Bundle.format(Bundle.RUNTIME_CLIENT_NOTFOUND, e.getLocalizedMessage()));
    }
    return sys;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extendPath
  /**
   ** Extends the class path of the current thread from the entries of a
   ** JDeveloper User Library.
   **
   ** @param  method             the {@link Method} to invoke.
   ** @param  loader             the {@link URLClassLoader} the method is
   **                            invoked on.
   ** @param  library            the identifier of the JDeveloper User library.
   **
   ** @throws IdentityServiceException if the {@link Method} is enforcing Java
   **                                  language access control and the method is
   **                                  inaccessible or the method itself throws
   **                                  an exception.
   */
  private static URLClassLoader extendPath(final Method method, final URLClassLoader loader, final String library)
    throws IdentityServiceException {

    final JLibraryList usr = JLibraryManager.getUserLibraries();
    final JLibrary     lib = usr.findLibrary(usr.createIDFromName(library, false));
    if (lib == null)
      throw new IdentityServiceException(Bundle.format(Bundle.RUNTIME_LIBRARY_NOTFOUND, library));

    // extend the runtime class path
    final URLClassLoader ldr = (URLClassLoader)loader.getParent();
    final Set<URL> path = CollectionUtility.set(ldr.getURLs());
    try {
      for (URL cursor : lib.getClassPath().getEntries())
        // check if the URL to the archive is already set to avoid duplicates
        if (!path.contains(cursor))
          method.invoke(loader, new Object[] { cursor });
    }
    catch (IllegalAccessException e) {
      throw new IdentityServiceException(Bundle.format(Bundle.RUNTIME_METHOD_INACCESSIBLE, e.getLocalizedMessage()));
    }
    catch (InvocationTargetException e) {
      throw new IdentityServiceException(Bundle.format(Bundle.RUNTIME_METHOD_EXCEPTION, e.getLocalizedMessage()));
    }
    return ldr;
  }
}