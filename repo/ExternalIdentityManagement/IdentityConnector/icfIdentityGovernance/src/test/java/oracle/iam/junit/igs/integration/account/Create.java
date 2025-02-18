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
    Subsystem   :   Identity Governance Service SCIM

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.account;

import java.util.Set;
import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case for create operation on accounts at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Create</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Create() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {Create.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRegularUser
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createRegularUser() {
    createUser("igs/create-azitterbacke.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserAN
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserAN() {
    createUser("igs/create-anserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserBB
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserBB() {
    createUser("igs/create-bbserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserBE
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserBE() {
    createUser("igs/create-beserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserBK
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserBK() {
    createUser("igs/create-bkserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserBP
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserBP() {
    createUser("igs/create-bpserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserBW
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserBW() {
    createUser("igs/create-bwserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserBY
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserBY() {
    createUser("igs/create-byserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserHB
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserHB() {
    createUser("igs/create-hbserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserHE
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserHE() {
    createUser("igs/create-heserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserHH
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserHH() {
    createUser("igs/create-hhserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserMV
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserMV() {
    createUser("igs/create-mvserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserNI
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserNI() {
    createUser("igs/create-niserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserNW
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserNW() {
    createUser("igs/create-nwserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserRP
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserRP() {
    createUser("igs/create-rpserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserSH
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserSH() {
    createUser("igs/create-shserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserSL
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserSL() {
    createUser("igs/create-slserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserSN
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserSN() {
    createUser("igs/create-snserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserTH
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserTH() {
    createUser("igs/create-thserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createServiceUserZF
  /**
   ** Test that a particular account could be created and the operation returns
   ** same primary identifier.
   */
  @Test
  public void createServiceUserZF() {
    createUser("igs/create-zfserviceuser.conf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUser
  /**
   ** Performs all actions to create an account in the target system by parsing
   ** the specified file to a map and transform that mapping to the connector
   ** server related attribute set.
   **
   ** @param  path               the file path providing the data to process.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void createUser(final String path) {
    try {
      // the map simulates how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final Map<String, Object>  data = data(path);

      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      assertNotNull(descriptor);
      // transform the data obtained from the process data to the collection of
      // attributes the connector server expects
      final Set<Attribute> dataSet = DescriptorTransformer.build(descriptor, data);
      assertNotNull(dataSet);
      final Uid uid = facade(service()).create(ObjectClass.ACCOUNT, dataSet, null);
      assertNotNull(uid);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}