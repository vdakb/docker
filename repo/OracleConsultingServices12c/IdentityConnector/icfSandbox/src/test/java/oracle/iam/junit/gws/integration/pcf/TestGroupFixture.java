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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestGroupFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestGroupFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.pcf;

import java.util.Set;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.connector.service.Descriptor;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGroupFixture extends Fixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name a connector service consumer will put in the appropriate options
   ** of a reconciliation process to configure the size of a batch of resources
   ** returned from a Service Provider.
   */
  static final String         BATCH_SIZE_OPTION   = "batchSize";

  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  static final String         SEARCH_ORDER_OPTION = "searchOrder";

  static final String         ORIGIN              = "checker.board";
  static final String         ALTERNATE           = "board.checker";
  static final Set<Attribute> CREATE              = AttributeFactory.set(new String[][]{{Name.NAME, ORIGIN}, {"description", "A group for testing purpose only"}});
  static final Set<Attribute> MODIFY              = AttributeFactory.set(new String[][]{{Name.NAME, ALTERNATE}});
  static final Set<Attribute> REVERT              = AttributeFactory.set(new String[][]{{Name.NAME, ORIGIN}});

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Descriptor descriptor;

    private Handler(final Descriptor descriptor) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.descriptor = descriptor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object             each object return from the search.
     **
     ** @return                    <code>true</code> if we should keep processing;
     **                            otherwise <code>false</code> to cancel.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      System.out.println(object);
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestGroupFixture</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestGroupFixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s01SearchGroup
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the <code>Service Provider</code> belonging to accounts.
   */
  @Test
  public void s01SearchGroup() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder().setAttributesToGet("id", "displayName");
    option.getOptions().put(BATCH_SIZE_OPTION, new Integer(500));
    try {
      facade.search(ObjectClass.GROUP, null, new Handler(reconciliation), option.build());
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s02CreateGroup
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
  @Test
  public void s02CreateGroup() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid uid = facade.create(ObjectClass.GROUP, CREATE, option.build());
      Assert.assertNotNull(uid);
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s03LookupGroup
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
  @Test
  public void s03LookupGroup() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid uid = facade.resolveUsername(ObjectClass.GROUP, ORIGIN, option.build());
      Assert.assertNotNull(uid);
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s04DeleteGroup
  /**
   ** Delete the SCIM group resource in the <code>Service Provider</code> that
   ** belongs to the given identifier.
   */
  @Test
  public void s04DeleteGroup() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid uid = facade.resolveUsername(ObjectClass.GROUP, ORIGIN, option.build());
      Assert.assertNotNull(uid);
      facade.delete(ObjectClass.GROUP, uid, option.build());
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s05CreateGroup
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
  @Test
  public void s05CreateGroup() {
    s02CreateGroup();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s06ModifyGroup
  /**
   ** Build and executes the request to patch the SCIM group resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
  @Test
  public void s06ModifyGroup() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid old = facade.resolveUsername(ObjectClass.GROUP, ORIGIN, option.build());
      Assert.assertNotNull(old);
      Uid uid = facade.update(ObjectClass.GROUP, old, MODIFY, option.build());
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s07RevertGroup
  /**
   ** Build and executes the request to patch the SCIM group resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
  @Test
  public void s07RevertGroup() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid old = facade.resolveUsername(ObjectClass.GROUP, ALTERNATE, option.build());
      Assert.assertNotNull(old);
      Uid uid = facade.update(ObjectClass.GROUP, old, REVERT, option.build());
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s08DeleteGroup
  /**
   ** Delete the SCIM group resource in the <code>Service Provider</code> that
   ** belongs to the given identifier.
   */
  @Test
  public void s08DeleteGroup() {
    s04DeleteGroup();
  }
}