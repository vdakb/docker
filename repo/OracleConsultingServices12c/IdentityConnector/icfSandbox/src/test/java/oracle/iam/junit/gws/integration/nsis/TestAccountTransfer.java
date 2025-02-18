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

    File        :   TestAccountCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-28-08  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.nsis;

import java.util.Set;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountTransfer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to transfer an provisioning mapping to a SCIM 2 user
 ** resource leveraging the <code>Connector Framwéwork</code> integration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestAccountTransfer extends TestAccount {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestAccountTransfer</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAccountTransfer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSophie
  /**
   ** Test that accounts could be created.
   */
	@Test
	public void testSophie() {
    try {
      final Descriptor      descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      Assert.assertNotNull(descriptor);
      Assert.assertNotNull(descriptor.attribute());
      Assert.assertNotEquals(descriptor.attribute().size(), 0);

      final Set<Attribute>  dataSet = DescriptorTransformer.build(descriptor, bkbk4711121.CREATE);
      Assert.assertNotNull(dataSet);
      Assert.assertNotEquals(dataSet.size(), 0);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}