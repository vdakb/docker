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

    System      :   Foundation Configuration Extension
    Subsystem   :   Common Shared Utility

    File        :   ConfigurationProvider.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigurationProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.config;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.net.URL;

////////////////////////////////////////////////////////////////////////////////
// interface ConfigurationFactory
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** An interface for objects that can create an {@link InputStream} to represent
 ** the application configuration.
 */
public interface ConfigurationProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Local
  // ~~~~~ ~~~~~
  /**
   ** An implementation of a {@link ConfigurationProvider} that which the
   ** configuration from the local file system.
   */
  static class Local implements ConfigurationProvider {
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default cache <code>Local</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Local()" and enforces use of the public method below.
     */
    private Local() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: open (ConfigurationProvider)
    /**
     ** Returns an {@link InputStream} that contains the source of the
     ** configuration for the application.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** The caller is responsible for closing the result.
     **
     ** @param  path             the path to the configuration.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  an {@link InputStream}.
     **                          <br>
     **                          Possible object is {@link InputStream}.
     **
     ** @throws IOException      if there is an error reading the data at
     **                          <code>path</code>.
     */
    @Override
    public InputStream open(final String path)
      throws IOException {

      final File file = new File(path);
      if (!file.exists())
        throw new FileNotFoundException("File " + file + " not found");

      return new FileInputStream(file);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Resource
  // ~~~~~ ~~~~~~~~
  /**
   ** An implementation of {@link ConfigurationProvider} that which reads the
   ** configuration from a resource file.
   ** <p>
   ** In order to abide by the calling conventions of
   ** {@link ClassLoader#getResourceAsStream} [1], absolute path strings (i.e.
   ** those with leading "/" characters) passed to {@link #open(String)} are
   ** converted to relative paths by removing the leading "/".
   ** <p>
   ** See [1] for more information on resources in Java and how they are loaded
   ** at runtime.
   ** <p>
   ** [1] https://docs.oracle.com/javase/8/docs/technotes/guides/lang/resources.html
   */
  static class Resource implements ConfigurationProvider {
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default cache <code>Resource</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Resource()" and enforces use of the public method below.
     */
    private Resource() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: open (ConfigurationProvider)
    /**
     ** Returns an {@link InputStream} that contains the source of the
     ** configuration for the application.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** The caller is responsible for closing the result.
     **
     ** @param  path             the path to the configuration.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  an {@link InputStream}.
     **                          <br>
     **                          Possible object is {@link InputStream}.
     **
     ** @throws IOException      if there is an error reading the data at
     **                          <code>path</code>.
     */
    @Override
    public InputStream open(final String path)
      throws IOException {

      final InputStream result = stream(path);
      return result == null && path.startsWith("/") ? stream(path.substring(1)) : result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: stream
    /**
     ** Returns an input stream for reading the specified resource.
     **
     ** @param  name              the resource name.
     **
     ** @return                   an input stream for reading the resource, or
     **                           <code>null</code> if the resource could not be
     **                           found.
     */
    private InputStream stream(final String path) {
      return getClass().getClassLoader().getResourceAsStream(path);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Remote
  // ~~~~~ ~~~~~~
  /**
   ** An implementation of {@link ConfigurationProvider}  which reads the
   ** configuration from an {@link URL}.
   */
  static class Remote implements ConfigurationProvider {
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default cache <code>Remote</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Remote()" and enforces use of the public method below.
     */
    private Remote() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: open (ConfigurationProvider)
    /**
     ** Returns an {@link InputStream} that contains the source of the
     ** configuration for the application.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** The caller is responsible for closing the result.
     **
     ** @param  path             the path to the configuration.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  an {@link InputStream}.
     **                          <br>
     **                          Possible object is {@link InputStream}.
     **
     ** @throws IOException      if there is an error reading the data at
     **                          <code>path</code>.
     */
    @Override
    public InputStream open(final String path)
      throws IOException {

      return new URL(path).openStream();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  // Method:   local
  /**
   ** Factory method to create <code>ConfigurationProvider</code> which reads
   ** the configuration from the local file system.
   */
  static ConfigurationProvider local() {
    return new Local();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Factory method to create <code>ConfigurationProvider</code> which reads
   ** the configuration from a resource file.
   */
  static ConfigurationProvider resource() {
    return new Resource();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remote
  /**
   ** Factory method to create <code>ConfigurationProvider</code> which reads
   ** the configuration from an {@link URL}.
   */
  static ConfigurationProvider remote() {
    return new Remote();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** Returns an {@link InputStream} that contains the source of the
   ** configuration for the application.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The caller is responsible for closing the result.
   **
   ** @param  path               the path to the configuration.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an {@link InputStream}.
   **                            <br>
   **                            Possible object is {@link InputStream}.
   **
   ** @throws IOException        if there is an error reading the data at
   **                            <code>path</code>.
   */
  InputStream open(final String path)
    throws IOException;
}