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
    Subsystem   :   Identity Governance Service Provisioning

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Base.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.systemrole;

import java.util.Set;
import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Attribute;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.junit.jes.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation of system roles at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Create extends TestFixture {

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
  // Method:   createUnknown
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createUnknown() {
    try {
      // the map simulates how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final Map<String, Object>  data = data("igd/create-systemrole-unknown.conf");

      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), SystemRole.PROVISIONING);
      assertNotNull(descriptor);
      // transform the data obtained from the process data to the collection of
      // attributes the connector server expects
      final Set<Attribute> dataSet = DescriptorTransformer.build(descriptor, data);
      assertNotNull(dataSet);
      final Uid uid = facade(endpoint()).create(ObjectClass.GROUP, dataSet, null);
      assertNotNull(uid);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("JES-00022"))
        failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSystemAdministrators
  /**
   ** Test that a particular role could be created by its unique identifier.
   */
  @Test
  public void createSystemAdministrators() {
    assertEquals(createSystemRole("igd/create-systemrole-sysadmin.conf"), SystemRole.OLDADMIN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAllUsers
  /**
   ** Test that a particular role could be created by its unique identifier.
   */
  @Test
  public void createAllUsers() {
    assertEquals(createSystemRole("igd/create-systemrole-allusers.conf"), SystemRole.ALLUSERS.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAdministrator
  /**
   ** Test that a particular role could be created by its unique identifier.
   */
  @Test
  public void createAdministrator() {
    assertEquals(createSystemRole("igd/create-systemrole-administrator.conf"), SystemRole.NEWADMIN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOperators
  /**
   ** Test that a particular role could be created by its unique identifier.
   */
  @Test
  public void createOperators() {
    assertEquals(createSystemRole("igd/create-systemrole-sysoper.conf"), SystemRole.OPERATORS.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSelfOperators
  /**
   ** Test that a particular role could be created by its unique identifier.
   */
  @Test
  public void createSelfOperators() {
    assertEquals(createSystemRole("igd/create-systemrole-selfoper.conf"), SystemRole.SELFOP.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBIReportAdministrator
  /**
   ** Test that a particular role could be created by its unique identifier.
   */
  @Test
  public void createBIReportAdministrator() {
    assertEquals(createSystemRole("igd/create-systemrole-bireport.conf"), SystemRole.BIRADMIN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSystemRole
  /**
   ** Performs all actions to create a system role in the target system by
   ** parsing the specified file to a map and transform that mapping to the
   ** connector server related attribute set.
   **
   ** @param  path               the file path providing the data to process.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the system identifier of the system role
   **                            created.
   **                            <br>
   **                            Possible object is {@link UID}.
   */
  private Uid createSystemRole(final String path) {
    try {
      // the map simulates how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final Map<String, Object>  data = data(path);

      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), SystemRole.PROVISIONING);
      assertNotNull(descriptor);
      // transform the data obtained from the process data to the collection of
      // attributes the connector server expects
      final Set<Attribute> dataSet = DescriptorTransformer.build(descriptor, data);
      assertNotNull(dataSet);
      return FACADE.create(ObjectClass.GROUP, dataSet, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
    return new Uid("Failure");
  }
}