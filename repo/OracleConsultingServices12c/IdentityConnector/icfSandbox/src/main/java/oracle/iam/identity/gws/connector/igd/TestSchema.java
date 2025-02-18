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
    Subsystem   :   Identity Governance Domain Connector

    File        :   TestSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.igd;

import java.util.Map;
import java.util.Collection;

import java.util.List;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.v2.schema.SchemaResource;

import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;;

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

  private static Map<String, List<String>> schema;

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

    final ListResponse<SchemaResource> response = Network.extranet().schemas();
    for (SchemaResource resource : response) {
      if (StringUtility.endsWithIgnoreCase(resource.id(), "user")) {
        final ObjectClassInfoBuilder account = new ObjectClassInfoBuilder();

        for (Definition cursor : resource.attribute()) {
          toplevelAttribute(cursor);
        }
      }
      else if (StringUtility.endsWithIgnoreCase(resource.id(), "group")) {
        for (Definition cursor : resource.attribute()) {
          toplevelAttribute(cursor);
        }
      }
    }
  }

  private static void toplevelAttribute(final Definition definition) {
    if (definition.type() == Definition.Type.COMPLEX || definition.multiValued()) {
      embbededAttribute(definition);
    }
    else {
      System.out.println(definition.name());
    }
  }

  private static void embbededAttribute(final Definition definition) {
    final Collection<Definition> attribute = definition.attributes();
    for (Definition cursor : attribute) {
      System.out.println(definition.name() + "." + cursor.name());
    }
  }
}