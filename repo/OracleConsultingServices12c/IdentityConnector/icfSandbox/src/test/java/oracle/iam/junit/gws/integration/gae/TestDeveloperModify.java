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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   TestDeveloperModify.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestDeveloperModify.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.gae;

import java.util.Set;

import org.junit.Test;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

////////////////////////////////////////////////////////////////////////////////
// class TestDeveloperModify
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to modify an entry in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestDeveloperModify extends TestDeveloper {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestDeveloperModify</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestDeveloperModify() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bkbk4711123
  /**
   ** Test that accounts could be created.
   */
  @Test
  public void bkbk4711123() {
    try {
      final Descriptor      descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      final Set<Attribute>  dataSet    = DescriptorTransformer.build(descriptor, bkbk4711123.MODIFY);
      final Uid             uid        = Network.facade(Network.intranet()).update(DEVELOPER, bkbk4711123.UID, dataSet, null);
      Network.CONSOLE.debug(uid.toString());
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}