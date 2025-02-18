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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Atlassian Jira Server Connector

    File        :   TestUserRename.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestUserRename.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.ajs;

import java.util.Set;

import org.junit.Test;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

import oracle.iam.identity.connector.service.DescriptorTransformer;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

////////////////////////////////////////////////////////////////////////////////
// class TestUserRename
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The test case to rename accounts leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserRename extends TestUser {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestUserRename</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestUserRename() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // Method:   bkbk4711123ToNew
  /**
   ** Test that accounts could be renamed to new name.
   */
	//@Test
	public void bkbk4711123ToNew() {
    try {
      final Descriptor      descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      final Set<Attribute>  dataSet    = DescriptorTransformer.build(descriptor, bkbk4711123.RENAME);
      final Uid             uid        = Network.facade(Network.intranet()).update(ObjectClass.ACCOUNT, bkbk4711123.UID, dataSet, null);
      Network.CONSOLE.debug(uid.toString());
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bkbk4711123ToOld
  /**
   ** Test that accounts could be renamed back.
   */
	@Test
	public void bkbk4711123ToOld() {
    try {
      final Descriptor      descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      final Set<Attribute>  dataSet    = DescriptorTransformer.build(descriptor, bkbk4711123.ORIGIN);
      final Uid             uid        = Network.facade(Network.intranet()).update(ObjectClass.ACCOUNT, bkbk4711123.NEW, dataSet, null);
      Network.CONSOLE.debug(uid.toString());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}