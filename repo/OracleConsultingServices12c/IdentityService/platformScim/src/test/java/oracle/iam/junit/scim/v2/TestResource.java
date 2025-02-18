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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   TestResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import oracle.hst.platform.rest.schema.Support;
import oracle.iam.platform.scim.schema.Resource;

import oracle.iam.junit.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestGroupResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for serialization and de-serialization of the SCIM resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestResource extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final File RESOURCE = new File("./src/test/resources");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestResource</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestResource() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Load a JSON file’s content and convert it into a {@link String}.
   **
   ** @param  path               the abstract path of the file to load.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link String} providing the configuration
   **                            key-value pairs.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  public static String stream(final String path)
    throws IOException {
    
    return new String(Files.readAllBytes(new File(RESOURCE, path).toPath()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Load a JSON file’s content and convert it into a {@link Map}.
   **
   ** @param  path               the abstract path of the file to load.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Map} providing the configuration
   **                            key-value pairs.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IOException        if an I/O error occurs.
   */
  public static <T extends Resource> T stream(final String path, final Class<T> clazz)
    throws IOException {
    
    return Support.objectMapper().readValue(new File(RESOURCE, path), clazz);
  }
}