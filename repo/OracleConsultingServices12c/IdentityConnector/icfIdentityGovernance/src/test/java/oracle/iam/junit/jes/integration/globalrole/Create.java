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

package oracle.iam.junit.jes.integration.globalrole;

import java.util.Set;
import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.icf.connector.oig.schema.ServiceClass;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation of global roles at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
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
  // Method:   createUnknown
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createUnknown() {
    try {
      // the map simulates how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final Map<String, Object>  data = data("igd/create-globalrole-unknown.conf");

      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      assertNotNull(descriptor);
      // transform the data obtained from the process data to the collection of
      // attributes the connector server expects
      final Set<Attribute> dataSet = DescriptorTransformer.build(descriptor, data);
      assertNotNull(dataSet);
      final Uid uid = FACADE.create(ServiceClass.GLOBAL, dataSet, null);
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
  // Method:   createOrclOIMSystemAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMSystemAdministrator() {
    assertEquals(createAdminRole("igd/create-globalrole-sysadmin.conf"), UID_SYSADMIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMSystemConfigurator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMSystemConfigurator() {
    assertEquals(createAdminRole("igd/create-globalrole-sysconfig.conf"), UID_SYSCONFIG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMCatalogAdmin
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMCatalogAdmin() {
    assertEquals(createAdminRole("igd/create-globalrole-catadmin.conf"), UID_CATADMIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMCertificationAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMCertificationAdministrator() {
    assertEquals(createAdminRole("igd/create-globalrole-certadmin.conf"), UID_CERTADMIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMCertificationViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMCertificationViewer() {
    assertEquals(createAdminRole("igd/create-globalrole-certviewer.conf"), UID_CERTVIEWER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAdminRole
  /**
   ** Performs all actions to create a global admin role in the target system by
   ** parsing the specified file to a map and transform that mapping to the
   ** connector server related attribute set.
   **
   ** @param  path               the file path providing the data to process.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the system identifier of the global admin role
   **                            created.
   **                            <br>
   **                            Possible object is {@link UID}.
   */
  private Uid createAdminRole(final String path) {
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
      return FACADE.create(ServiceClass.GLOBAL, dataSet, null);
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