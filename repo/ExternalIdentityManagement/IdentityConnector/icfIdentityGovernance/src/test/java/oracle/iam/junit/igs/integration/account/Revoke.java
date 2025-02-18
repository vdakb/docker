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

    File        :   Revoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Revoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.account;

import java.util.Set;
import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.icf.connector.scim.igs.ExtensionClass;

import org.identityconnectors.framework.api.ConnectorFacade;

////////////////////////////////////////////////////////////////////////////////
// class Revoke
// ~~~~~ ~~~~~~
/**
 ** The test case for modify operation on accounts at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Revoke extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Revoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Revoke() {
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
    final String[] parameter = {Revoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeIGSAdmin
  /**
   ** Test that a particular role could be modified by its primary identifier.
   */
  @Test
  public void revokeIGSAdmin() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object> data = CollectionUtility.map(
      "UD_IGS_URL_UID" , "igs.admin"
    );
    revokeUser("azitterbacke", ObjectClass.GROUP, Pair.of(ObjectClass.GROUP_NAME, "UD_IGS_URL"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeUIDAdmin
  /**
   ** Test that a particular role could be modified by its primary identifier.
   */
  @Test
  public void revokeUIDAdmin() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_URL_UID" , "uid.admin"
    );
    revokeUser("azitterbacke", ObjectClass.GROUP, Pair.of(ObjectClass.GROUP_NAME, "UD_IGS_URL"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokePIDAdmin
  /**
   ** Test that a particular role could be modified by its primary identifier.
   */
  @Test
  public void revokePIDAdmin() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_URL_UID" , "pid.admin"
    );
    revokeUser("azitterbacke", ObjectClass.GROUP, Pair.of(ObjectClass.GROUP_NAME, "UD_IGS_URL"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeUIDGenerate
  /**
   ** Test that a particular role could be modified by its primary identifier.
   */
  @Test
  public void revokeUIDGenerate() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_URL_UID" , "uid.generate"
    );
    revokeUser("azitterbacke", ObjectClass.GROUP, Pair.of(ObjectClass.GROUP_NAME, "UD_IGS_URL"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeUIDRegister
  /**
   ** Test that a particular role could be modified by its primary identifier.
   */
  @Test
  public void revokeUIDRegister() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_URL_UID" , "uid.register"
    );
    revokeUser("azitterbacke", ObjectClass.GROUP, Pair.of(ObjectClass.GROUP_NAME, "UD_IGS_URL"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeNeverUsed
  /**
   ** Test that a particular role could be modified by its primary identifier.
   */
  @Test
  public void revokeNeverUsed() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_URL_UID" , "never.used"
    );
    revokeUser("azitterbacke", ObjectClass.GROUP, Pair.of(ObjectClass.GROUP_NAME, "UD_IGS_URL"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeUIDGenerateOnT36099
  /**
   ** Test that a particular tenant could be modified by its primary identifier.
   */
  @Test
  public void revokeUIDGenerateOnT36099() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_UTN_UID" , "T-36-0-99"
    , "UD_IGS_UTN_SID" , "uid.generate"
    );
    revokeUser("azitterbacke", ExtensionClass.TENANT, Pair.of(ExtensionClass.TENANT_NAME, "UD_IGS_UTN"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeUIDRegisterOnT36099
  /**
   ** Test that a particular tenant could be modified by its primary identifier.
   */
  @Test
  public void revokeUIDRegisterOnT36099() {
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_UTN_UID" , "T-36-0-99"
    , "UD_IGS_UTN_SID" , "uid.register"
    );
    revokeUser("azitterbacke", ExtensionClass.TENANT, Pair.of(ExtensionClass.TENANT_NAME, "UD_IGS_UTN"), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeUser
  /**
   ** Performs all actions to revoke an account in the target system that
   ** belongs to the specified <code>userName</code>.
   **
   ** @param  userName           the unique name of the service user to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void revokeUser(final String userName, final ObjectClass type, final Pair<String, String> pointer, final Map<String, Object> data) {
    try {
      final ConnectorFacade facade = facade(service());
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      assertNotNull(descriptor);
      final Map<Pair<String, String>, Descriptor> reference = descriptor.reference();
      assertNotNull(reference);
      final Descriptor user = reference.get(pointer);
      assertNotNull(user);
      final Set<Attribute> dataSet = DescriptorTransformer.build(type, user, data);
      assertNotNull(dataSet);
      facade.removeAttributeValues(ObjectClass.ACCOUNT, facade.resolveUsername(ObjectClass.ACCOUNT, userName, null), dataSet, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}