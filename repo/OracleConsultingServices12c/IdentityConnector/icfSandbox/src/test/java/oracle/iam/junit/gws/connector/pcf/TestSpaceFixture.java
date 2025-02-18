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

    File        :   TestSpaceFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestSpaceFixture.


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

import oracle.iam.identity.icf.rest.domain.ListResult;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;
import oracle.iam.identity.icf.connector.pcf.rest.domain.Payload;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSpaceFixture extends CloudControllerFixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         ORIGIN    = "Pizza Place Space";
  static final String         ALTERNATE = "Moshroom Circle Space";

  static final Set<Attribute> S1        = AttributeFactory.set(new String[][]{{Name.NAME, "Pizza Place Space"},     {Payload.TENANT, "b677725c-35ad-4ed0-b2f6-fa3dfdabd219"}});
  static final Set<Attribute> S2        = AttributeFactory.set(new String[][]{{Name.NAME, "Moshroom Circle Space"}, {Payload.TENANT, "4711"}});
  static final Set<Attribute> S3        = AttributeFactory.set(new String[][]{{Name.NAME, "Mario Cart Space"},      {Payload.TENANT, "4711"}});

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestSpaceFixture</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestSpaceFixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s01SearchSpace
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the <code>Cloud Controller</code> belonging to spaces.
   */
  @Test
  public void s01SearchSpace() {
    int       total  = 0;
    int       index  = 1;
    final int size   = 100;
    try {
      do {
        final ListResult<Result.Space> response = context.searchSpace().page(index, size).invoke(Result.Space.class);
        total = response.total();
        if (total == 0) {
          Assert.assertTrue("No Records available to be fetched from Target.", 0 == total);
          break;
        }
        else {
          for (Result.Space cursor : response) {
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
}