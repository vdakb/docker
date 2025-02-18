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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   N.SIS Universal Police Client SCIM

    File        :   TestTransferUserResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestTransferUserResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-28-08  DSteding    First release version
*/

package oracle.iam.junit.gws.connector.nsis;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.iam.identity.icf.scim.v2.schema.Marshaller;

import oracle.iam.identity.icf.scim.v2.schema.UserResource;

////////////////////////////////////////////////////////////////////////////////
// class TestTransferUserResource
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to transfer a set of {@link Attributes} of a SCIM 2 user
 ** resource leveraging the <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestTransferUserResource extends TestAccountFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestTransferUserResource</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestTransferUserResource() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transferSophieStrecke
  /**
   ** Test that transfer a set of {@link Attributes} of a SCIM 2 user.
   */
  @Test
  public void transferSophieStrecke() {
    try {
      final UserResource resource = Marshaller.transferUser(UserResource.class, Sophie.CREATE);
      Assert.assertNotNull(resource);
      Assert.assertNull(resource.id());
      Assert.assertNotNull(resource.name());
      Assert.assertTrue(resource.active());
      Assert.assertEquals(resource.userName(),          Sophie.NAME);
      Assert.assertEquals(resource.displayName(),       Sophie.DISPLAY);
      Assert.assertEquals(resource.name().familyName(), Sophie.SN);
      Assert.assertEquals(resource.name().givenName(),  Sophie.GN);
      System.out.println(resource);
    }
    catch (InvocationTargetException e) {
      failed(e);
    }
    catch (InstantiationException e) {
      failed(e);
    }
    catch (IllegalAccessException e) {
      failed(e);
    }
    catch (NoSuchMethodException e) {
      failed(e);
    }
  }
}