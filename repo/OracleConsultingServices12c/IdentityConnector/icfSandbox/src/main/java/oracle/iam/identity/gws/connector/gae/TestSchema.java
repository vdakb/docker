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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   TestSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.gae;

import oracle.iam.identity.icf.connector.apigee.Main;

import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;

import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

import oracle.iam.identity.icf.connector.apigee.schema.User;
import oracle.iam.identity.icf.connector.apigee.schema.Tenant;

import oracle.iam.identity.icf.schema.Factory;

////////////////////////////////////////////////////////////////////////////////
// class TestSchema
// ~~~~~ ~~~~~~~~~~
/**
 ** The test case to obtain the schema from the target system leveraging the
 ** <code>Connector Server</code> facade.
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
  public static void main(String[] args) {
    final SchemaBuilder   builder = new SchemaBuilder(Main.class);
    final ObjectClassInfo user    = Factory.defineObjectClass(builder, User.class);
    final ObjectClassInfo tenant  = Factory.defineObjectClass(builder, Tenant.class);
    builder.defineObjectClass(user);
    builder.defineObjectClass(tenant);
    // how stupid ICF is build shows the code below
    // instead of providing a fine grained api to take control how a certain
    // object class is presented in the populated schema you need to add the
    // object class generated completely at first. The ugly frameork will add
    // than all possible operation to the object class that can it find, with
    // the result that we need to remove all operation later and allow only
    // those that the connector is realy supporting
    // what kind of bull shit is that, that keeps the garbage collector busy
    // only
    builder.clearSupportedObjectClassesByOperation();
    builder.addSupportedObjectClass(SchemaOp.class, user);
    builder.addSupportedObjectClass(CreateOp.class, user);
    builder.addSupportedObjectClass(DeleteOp.class, user);
    builder.addSupportedObjectClass(UpdateOp.class, user);
    builder.addSupportedObjectClass(SearchOp.class, user);
    builder.addSupportedObjectClass(TestOp.class,   user);
    builder.addSupportedObjectClass(SchemaOp.class, tenant);
    builder.addSupportedObjectClass(SearchOp.class, tenant);
    System.out.println(builder.build());
  }
}