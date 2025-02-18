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

    File        :   SystemException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.config;

import java.io.File;
import java.io.IOException;

////////////////////////////////////////////////////////////////////////////////
// interface ConfigurationFactory
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A generic factory interface for loading configuration files, binding them to
 ** configuration objects, and validating their constraints. Allows for
 ** overriding configuration parameters from system properties.
 **
 ** @param  <T>                  the type of the configuration objects to
 **                              produce.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>
 */
public interface ConfigurationFactory<T extends Configuration> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Loads, parses, binds, and validates a configuration object from the local
   ** file system.
   **
   ** @param  file               the abstract path of the configuration file
   **                            <br>
   **                            Allowed object is{@link File}.
   **
   ** @return                    a validated configuration object.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IOException            if there is an error reading the file.
   ** @throws ConfigurationException if there is an error parsing or validating
   **                                the file.
   */
  default T build(final File file)
    throws IOException
    ,      ConfigurationException {

    return build(ConfigurationProvider.local(), file.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Loads, parses, binds, and validates a configuration object from an empty
   ** document.
   **
   ** @return                    a validated configuration object.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IOException            if there is an error reading the file.
   ** @throws ConfigurationException if there is an error parsing or validating
   **                                the file.
   */
  T build()
    throws IOException
    ,      ConfigurationException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Loads, parses, binds, and validates a configuration object.
   **
   ** @param  provider           the provider to to use for reading
   **                            configuration files.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConfigurationProvider}.
   ** @param  path               the path of the configuration file.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a validated configuration object.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IOException            if there is an error reading the file.
   ** @throws ConfigurationException if there is an error parsing or validating
   **                                the file.
   */
  T build(final ConfigurationProvider provider, final String path)
    throws IOException
    ,      ConfigurationException;
}