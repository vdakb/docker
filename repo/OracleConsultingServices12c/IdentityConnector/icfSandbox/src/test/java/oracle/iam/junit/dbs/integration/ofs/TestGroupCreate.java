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
    Subsystem   :   Openfire Database Connector

    File        :   TestGroupCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestGroupCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import java.util.Set;

import org.junit.Test;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

import oracle.iam.identity.connector.service.DescriptorTransformer;

////////////////////////////////////////////////////////////////////////////////
// class TestGroupCreate
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The test case to create groups leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestGroupCreate extends TestGroup {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestGroupCreate</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestGroupCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclGroupReadPrivilegeGroup
  /**
   ** Test that groups could be created.
   */
	@Test
	public void orclGroupReadPrivilegeGroup() {
    try {
      final Descriptor      descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      final Set<Attribute>  dataSet    = DescriptorTransformer.build(descriptor, orclGroupReadPrivilegeGroup.CREATE);
      final Uid             uid        = Network.facade(Network.intranet()).create(ObjectClass.GROUP, dataSet, null);
      Network.CONSOLE.debug(uid.toString());
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclGroupWritePrivilegeGroup
  /**
   ** Test that groups could be created.
   */
	@Test
	public void orclGroupWritePrivilegeGroup() {
    try {
      final Descriptor      descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      final Set<Attribute>  dataSet    = DescriptorTransformer.build(descriptor, orclGroupWritePrivilegeGroup.CREATE);
      final Uid             uid        = Network.facade(Network.intranet()).create(ObjectClass.GROUP, dataSet, null);
      Network.CONSOLE.debug(uid.toString());
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}
