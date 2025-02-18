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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic WebService Connector

    File        :   TestSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector;

import java.io.IOException;

import java.util.LinkedHashMap;

import oracle.hst.foundation.SystemException;
import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.response.ListResponse;

////////////////////////////////////////////////////////////////////////////////
// class TestSchema
// ~~~~~ ~~~~~~~~~~~
/**
 ** The test case to resolve the schema of the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestSchema {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args)
    throws Exception{

    ListResponse<LinkedHashMap> schemaResource = schemaResource();
    for (LinkedHashMap cursor : schemaResource) {
      System.out.println(cursor);
    }
  }

  private static ListResponse<LinkedHashMap> schemaResource()
    throws IOException
    ,      SystemException {

    return Support.objectReader().forType(ListResponse.class).readValue(FileSystem.readEntireFile("D:\\Project\\OracleConsultingServices12c\\IdentityConnector\\efbs-scim-schema.json"));
  }
}