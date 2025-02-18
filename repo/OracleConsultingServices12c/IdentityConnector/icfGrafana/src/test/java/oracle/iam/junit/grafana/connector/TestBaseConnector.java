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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   TestBaseConnector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    TestBaseConnector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.connector;

import java.util.Map;
import java.util.HashMap;

import java.util.stream.Stream;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import oracle.iam.identity.foundation.TaskError;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceEndpoint;

import oracle.iam.identity.icf.connector.grafana.Context;

import oracle.iam.junit.grafana.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestBaseConnector
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>TestBaseConnector</code> implements the basic functionality of a
 ** Connector Bundle Test Cases.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestBaseConnector extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public static final SystemConsole CONSOLE   = new SystemConsole("icf");
  /**
   ** The location of the configuration file the Test Cases data are populated
   ** from.
   */
  public static final File          RESOURCES = new File("src/test/resources/icf");
  /**
   ** The location of the configuration file the standard
   ** <code>IT Resource</code> is populated from if the Test Cases are executed
   ** inside of the Intranet.
   */
  public static final File          ENDPOINT  = new File(RESOURCES, "service.conf");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestBaseConnector</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestBaseConnector() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Factory method to create a new {@link Context}.
   **
   ** @return                    the {@link Context} configured for the network.
   **
   ** @throws SystemException    if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static Context context()
    throws SystemException {

    final Map<String, String> config   = config(ENDPOINT);
    // hack the RFC-9110 compliance
    final ServiceEndpoint     endpoint = ServiceEndpoint.build(CONSOLE, config).rfc9110(false);
    // hack the enterprise feature
    endpoint.enterpriseFeature(false);
    final ServiceClient       client   = ServiceClient.build(endpoint);
    return Context.build(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Load a text file’s content and convert it into a {@link Map}.
   **
   ** @param  path               the absolute abstract path to the file to load
   **                            from the resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Map} providing the configuration
   **                            key-value pairs.
   **
   ** @throws SystemException    if an I/O error occurs.
   */
  public static Map<String, String> config(final File path)
    throws SystemException {

    final Map<String, String> map = new HashMap<>();
    try (Stream<String> lines = Files.lines(path.toPath())) {
      lines.filter(
        line -> line.contains(":")).forEach(line -> {
          final String[] pair = line.split(":", 2);
          map.put(pair[0].trim(), pair[1].trim());
        }
      );
      return map;
    }
    catch (NoSuchFileException e) {
      throw SystemException.abort(TaskBundle.format(TaskError.ITRESOURCE_NOT_FOUND, path));
    }
    catch (IOException e) {
      throw SystemException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link SystemException} thrown from the
   **                            <code>Connector</code>.
   */
  public static void failed(final SystemException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    // this message needs to be split by a "::" character sequence
    failed(cause.code().concat("::").concat(cause.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link TaskException} thrown from the
   **                            <code>Connector</code>.
   */
  public static void failed(final TaskException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    // this message needs to be split by a "::" character sequence
    failed(cause.code().concat("::").concat(cause.getLocalizedMessage()));
  }
}