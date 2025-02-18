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

package oracle.iam.junit.gws.connector.pcf;

import java.util.Set;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.v1.request.Operation;

import oracle.iam.identity.icf.connector.pcf.ScimMarshaller;

import oracle.iam.identity.icf.connector.pcf.scim.schema.GroupResource;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGroupFixture extends UserAccountFixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         ORIGIN    = "checker.board";
  static final String         ALTERNATE = "board.checker";
  static final Set<Attribute> CREATE    = AttributeFactory.set(new String[][]{{"displayName", ORIGIN}, {"description", "A group for testing purpose only"}});
  static final Set<Attribute> MODIFY    = AttributeFactory.set(new String[][]{{"displayName", ALTERNATE}});
  static final Set<Attribute> REVERT    = AttributeFactory.set(new String[][]{{"displayName", ORIGIN}});

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
   ** from the <code>Service Provider</code> belonging to groups.
   */
  @Test
  public void s01SearchGroup() {
    final Set<String> target = CollectionUtility.set("id", "displayName");
    final int         size   = 500;
    int               total  = 0;
    int               index  = 1;
    try {
      do {
        final ListResponse<GroupResource> response = context.searchGroup().page(index, size).emit(target).invoke(GroupResource.class);
        total = response.total();
        if (total == 0) {
          Assert.assertTrue("No Records available to be fetched from Target.", 0 == total);
          break;
        }
        else {
          for (GroupResource cursor : response) {
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
  // Method:   s02CreateGroup
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the <code>Service Provider</code>.
   */
  @Test
  public void s02CreateGroup() {
    try {
      String uid = context.resolveGroup(ORIGIN);
      Assert.assertNull(uid);
      final GroupResource resource = ScimMarshaller.inboundGroup(CREATE);
      final GroupResource response = context.createGroup(resource);
      Assert.assertTrue(response.toString(), 1 == 1);
      uid = context.resolveGroup(ORIGIN);
      Assert.assertNotNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s03ResolveGroup
  /**
   ** Build and executes the request to resolve a SCIM group resource at the
   ** <code>Service Provider</code>.
   */
  @Test
  public void s03ResolveGroup() {
    try {
      String uid = context.resolveGroup(ORIGIN);
      Assert.assertNotNull(uid);
    }
    catch (SystemException e) {
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
    try {
      String uid = context.resolveGroup(ORIGIN);
      Assert.assertNotNull(uid);
      context.deleteGroup(uid);
      uid = context.resolveGroup(ORIGIN);
      Assert.assertNull(uid);
    }
    catch (SystemException e) {
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
    try {
      String uid = context.resolveGroup(ORIGIN);
      Assert.assertNull(uid);
      final GroupResource resource = ScimMarshaller.inboundGroup(CREATE);
      final GroupResource response = context.createGroup(resource);
      uid = context.resolveGroup(ORIGIN);
      Assert.assertNotNull(uid);
      Assert.assertEquals(uid, response.id());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s06ModifyGroup
  /**
   ** Build and executes the request to patch the SCIM user resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
  @Test
  public void s06ModifyGroup() {
    try {
      String old = context.resolveGroup(ORIGIN);
      Assert.assertNotNull(old);
      final List<Operation> operation = ScimMarshaller.inboundGroup(MODIFY, false);
      context.modifyGroup(old, operation);
      String uid = context.resolveGroup(ALTERNATE);
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s07RevertGroup
  /**
   ** Build and executes the request to patch the SCIM user resource at the
   ** <code>Service Provider</code> with the given <code>operation</code>s.
   */
  @Test
  public void s07RevertGroup() {
    try {
      String old = context.resolveGroup(ALTERNATE);
      Assert.assertNotNull(old);
      final List<Operation> operation = ScimMarshaller.inboundGroup(REVERT, false);
      context.modifyGroup(old, operation);
      String uid = context.resolveGroup(ORIGIN);
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s08DeleteGroup
  /**
   ** Delete the SCIM user resource in the <code>Service Provider</code> that
   ** belongs to the given identifier.
   */
  @Test
  public void s08DeleteGroup() {
    try {
      String uid = context.resolveGroup(ORIGIN);
      Assert.assertNotNull(uid);
      context.deleteGroup(uid);
      uid = context.resolveGroup(ORIGIN);
      Assert.assertNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}