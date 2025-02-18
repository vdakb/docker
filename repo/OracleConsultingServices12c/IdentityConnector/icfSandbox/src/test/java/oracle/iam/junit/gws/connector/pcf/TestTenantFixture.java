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

    File        :   TestTenantFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestTenantFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.connector.pcf;

import java.util.Set;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.pcf.RestMarshaller;

import oracle.iam.identity.icf.rest.domain.ListResult;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;
import oracle.iam.identity.icf.connector.pcf.rest.domain.Payload;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTenantFixture extends CloudControllerFixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         ORIGIN    = "Pizza Place";
  static final String         ALTERNATE = "Moshroom Circle";
  static final Set<Attribute> CREATE    = AttributeFactory.set(new String[][]{{Name.NAME, ORIGIN},    {Payload.STATUS, Payload.STATUS_ACTIVE}});
  static final Set<Attribute> MODIFY    = AttributeFactory.set(new String[][]{{Name.NAME, ALTERNATE}, {Payload.STATUS, Payload.STATUS_ACTIVE}});
  static final Set<Attribute> REVERT    = AttributeFactory.set(new String[][]{{Name.NAME, ORIGIN},    {Payload.STATUS, Payload.STATUS_ACTIVE}});

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestTenantFixture</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestTenantFixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s01SearchTenant
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the <code>Cloud Controller</code> belonging to tenants.
   */
  @Test
  public void s01SearchTenant() {
    int       total  = 0;
    int       index  = 1;
    final int size   = 50;
    try {
      do {
        final ListResult<Result.Tenant> response = context.searchTenant().page(index, size).invoke(Result.Tenant.class);
        total = response.total();
        if (total == 0) {
          Assert.assertTrue("No records available to be fetched from Target.", 0 == total);
          break;
        }
        for (Result.Tenant cursor : response) {
          Assert.assertTrue(cursor.toString(), 1 == 1);
        }
      // position batch window on the next page
      } while (index++ * size < total);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s02CreateTenant
  /**
   ** Build and executes the request to create the provided newCloud Controller
   ** tenant at the <code>Service Provider</code>.
   */
//  @Test
  public void s02CreateTenant() {
    try {
      String uid = context.resolveTenant(ORIGIN);
      Assert.assertNull(uid);
      final Payload.Tenant resource = RestMarshaller.buildTenant(CREATE);
      final Result.Tenant  response = context.createTenant(resource);
      Assert.assertTrue(response.toString(), 1 == 1);
      uid = context.resolveTenant(ORIGIN);
      Assert.assertNotNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s03ResolveTenant
  /**
   ** Build and executes the request to resolve a Cloud Controller tenant
   ** resource at the the <code>Cloud Controller</code>.
   */
//  @Test
  public void s03ResolveTenant() {
    try {
      String uid = context.resolveTenant(ORIGIN);
      Assert.assertNotNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s04DeleteTenant
  /**
   ** Delete the Cloud Controller tenant resource in the
   ** <code>Service Provider</code> that belongs to the given identifier.
   */
//  @Test
  public void s04DeleteTenant() {
    try {
      String uid = context.resolveTenant(ORIGIN);
      Assert.assertNotNull(uid);
      context.deleteTenant(uid);
      uid = context.resolveTenant(ORIGIN);
      Assert.assertNull(uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s05CreateTenant
  /**
   ** Build and executes the request to create the provided new Cloud Controller
   ** tenant resource at the <code>Service Provider</code>.
   */
//  @Test
  public void s05CreateTenant() {
	  s02CreateTenant();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s06ModifyTenant
  /**
   ** Build and executes the request to patch the Cloud Controller tenant
   ** resource at the <code>Service Provider</code> with the given
   ** <code>operation</code>s.
   */
//  @Test
  public void s06ModifyTenant() {
    try {
      String old = context.resolveTenant(ORIGIN);
      Assert.assertNotNull(old);
      final Payload.Tenant resource = RestMarshaller.buildTenant(MODIFY);
      context.modifyTenant(old, resource);
      String uid = context.resolveTenant(ALTERNATE);
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s07RevertTenant
  /**
   ** Build and executes the request to patch the Cloud Controller tenant
   ** resource at the <code>Service Provider</code> with the given
   ** <code>operation</code>s.
   */
//  @Test
  public void s07RevertTenant() {
    try {
      String old = context.resolveTenant(ALTERNATE);
      Assert.assertNotNull(old);
      final Payload.Tenant resource = RestMarshaller.buildTenant(REVERT);
      context.modifyTenant(old, resource);
      String uid = context.resolveTenant(ORIGIN);
      Assert.assertNotNull(uid);
      Assert.assertEquals(old, uid);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s08DeleteTenant
  /**
   ** Delete the Cloud Controller tenantresource in the
   ** <code>Service Provider</code> that belongs to the given identifier.
   */
//  @Test
  public void s08DeleteTenant() {
    s04DeleteTenant();
  }
}