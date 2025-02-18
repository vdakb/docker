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

package oracle.iam.junit.gws.connector.pcf;

import java.util.Set;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.v1.request.Operation;

import oracle.iam.identity.icf.connector.pcf.ScimMarshaller;

import oracle.iam.identity.icf.connector.pcf.scim.schema.UserResource;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAccountFixture extends UserAccountFixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         NAME   = "azitterbacke";

  static final Set<Attribute> CREATE = AttributeFactory.set(
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

  static final Set<Attribute> MODIFY = AttributeFactory.set(
    new String[]{
        "name.givenName"
    }
    , new Object[]{
        "Max"
    }
  );

  static final Set<Attribute> REVERT = AttributeFactory.set(
    new String[]{
        "name.givenName"
    }
    , new Object[]{
        "Alfons"
    }
  );

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
    final Set<String> target = CollectionUtility.set("id", "userName", "origin" ,"meta", "name");
    final int         size   = 500;
    int               total  = 0;
    int               index  = 1;
    try {
      do {
        final ListResponse<UserResource> response = context.searchAccount().page(index, size).emit(target).invoke(UserResource.class);
        total = response.total();
        if (total == 0) {
          Assert.assertTrue("No Records available to be fetched from Target.", 0 == total);
          break;
        }
        else {
          for (UserResource cursor : response) {
            Assert.assertTrue(cursor.toString(), 1 == 1);
          }
          index += size;
        }
      } while (index < total);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s02CreateAccount
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
//  @Test
  public void s02CreateAccount() {
    try {
      String uid = context.resolveAccount(NAME);
      Assert.assertNull(uid);
      final UserResource resource = ScimMarshaller.inboundUser(CREATE);
      final UserResource response = context.createAccount(resource);
      Assert.assertTrue(response.toString(), 1 == 1);
      uid = context.resolveAccount(NAME);
      Assert.assertNotNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s03ResolveAccount
  /**
   ** Build and executes the request to resolve a SCIM user resource at the
   ** <code>Service Provider</code>.
   */
//  @Test
  public void s03ResolveAccount() {
    try {
      String uid = context.resolveAccount(NAME);
      Assert.assertNotNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s04DeleteAccount
  /**
   ** Delete the SCIM user resource in the <code>Service Provider</code> that
   ** belongs to the given identifier.
   */
//  @Test
  public void s04DeleteAccount() {
    try {
      String uid = context.resolveAccount(NAME);
      Assert.assertNotNull(uid);
      context.deleteAccount(uid);
      uid = context.resolveAccount(NAME);
      Assert.assertNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s05CreateAccount
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
//  @Test
  public void s05CreateAccount() {
    try {
      String uid = context.resolveAccount(NAME);
      Assert.assertNull(uid);
      final UserResource resource = ScimMarshaller.inboundUser(CREATE);
      final UserResource response = context.createAccount(resource);
      uid = context.resolveAccount(NAME);
      Assert.assertNotNull(uid);
      Assert.assertEquals(uid, response.id());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s06ModifyAccount
  /**
   ** Build and executes the request to patch the SCIM user resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
//  @Test
  public void s06ModifyAccount() {
    try {
      String old = context.resolveAccount(NAME);
      Assert.assertNotNull(old);
      final List<Operation> operation = ScimMarshaller.inboundUser(MODIFY, false);
      context.modifyAccount(old, operation);
      String uid = context.resolveAccount(NAME);
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s07RevertAccount
  /**
   ** Build and executes the request to patch the SCIM user resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
//  @Test
  public void s07RevertAccount() {
    try {
      String old = context.resolveAccount(NAME);
      Assert.assertNotNull(old);
      final List<Operation> operation = ScimMarshaller.inboundUser(REVERT, false);
      context.modifyAccount(old, operation);
      String uid = context.resolveAccount(NAME);
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s08DeleteAccount
  /**
   ** Delete the SCIM user resource in the <code>Service Provider</code> that
   ** belongs to the given identifier.
   */
//  @Test
  public void s08DeleteAccount() {
    try {
      String uid = context.resolveAccount(NAME);
      Assert.assertNotNull(uid);
      context.deleteAccount(uid);
      uid = context.resolveAccount(NAME);
      Assert.assertNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}