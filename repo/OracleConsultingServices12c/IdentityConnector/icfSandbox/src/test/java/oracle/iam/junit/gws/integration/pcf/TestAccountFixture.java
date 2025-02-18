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

    File        :   TestAccountFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountFixture.


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

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.AttributeFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAccountFixture extends Fixture {

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

  static final String         NAME                = "azitterbacke";

  static final Set<Attribute> CREATE              = AttributeFactory.set(
    new String[]{
        Name.NAME
      , OperationalAttributes.PASSWORD_NAME
      , OperationalAttributes.ENABLE_NAME
      , "name.givenName"
      , "name.familyName"
      , "emails.value"
    }
    , new Object[]{
        "azitterbacke"
      , new GuardedString("Welcome1".toCharArray())
      , Boolean.TRUE
      , "Alfons"
      , "Zitterbacke"
      , "alfons.zitterbacke@vm.oracle.com"
    }
  );

  static final Set<Attribute> MODIFY = AttributeFactory.set(new String[]{"name.givenName"}, new Object[]{"Max"});
  static final Set<Attribute> REVERT = AttributeFactory.set(new String[]{"name.givenName"}, new Object[]{"Alfons"});

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
   ** Constructs a <code>TestAccountFixture</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAccountFixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s01SearchAccount
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the <code>Service Provider</code> belonging to accounts.
   */
  @Test
  public void s01SearchAccount() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder().setAttributesToGet(reconciliation.source());
    option.getOptions().put(BATCH_SIZE_OPTION, new Integer(500));
    try {
      facade.search(ObjectClass.ACCOUNT, null, new Handler(reconciliation), option.build());
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s02CreateAccount
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
  @Test
  public void s02CreateAccount() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid uid = facade.create(ObjectClass.ACCOUNT, CREATE, option.build());
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
  // Method:   s03LookupAccount
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
  @Test
  public void s03LookupAccount() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid uid = facade.resolveUsername(ObjectClass.ACCOUNT, NAME, option.build());
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
  // Method:   s04DeleteAccount
  /**
   ** Delete the SCIM user resource in the <code>Service Provider</code> that
   ** belongs to the given identifier.
   */
  @Test
  public void s04DeleteAccount() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid uid = facade.resolveUsername(ObjectClass.ACCOUNT, NAME, option.build());
      Assert.assertNotNull(uid);
      facade.delete(ObjectClass.ACCOUNT, uid, option.build());
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s05CreateAccount
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
  @Test
  public void s05CreateAccount() {
    s02CreateAccount();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s06ModifyAccount
  /**
   ** Build and executes the request to patch the SCIM user resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
  @Test
  public void s06ModifyAccount() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid old = facade.resolveUsername(ObjectClass.ACCOUNT, NAME, option.build());
      Assert.assertNotNull(old);
      Uid uid = facade.update(ObjectClass.ACCOUNT, old, MODIFY, option.build());
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
  // Method:   s07RevertAccount
  /**
   ** Build and executes the request to patch the SCIM user resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
  @Test
  public void s07RevertAccount() {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Uid old = facade.resolveUsername(ObjectClass.ACCOUNT, NAME, option.build());
      Assert.assertNotNull(old);
      Uid uid = facade.update(ObjectClass.ACCOUNT, old, REVERT, option.build());
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
  // Method:   s08DeleteAccount
  /**
   ** Delete the SCIM user resource in the <code>Service Provider</code> that
   ** belongs to the given identifier.
   */
  @Test
  public void s08DeleteAccount() {
    s04DeleteAccount();
  }
}