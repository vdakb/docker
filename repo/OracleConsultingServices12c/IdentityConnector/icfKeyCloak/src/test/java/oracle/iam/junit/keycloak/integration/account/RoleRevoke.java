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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   RoleRevoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleRevoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.keycloak.integration.account;

import java.util.Set;

import java.io.File;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.junit.keycloak.integration.TestModel;
import oracle.iam.junit.keycloak.integration.TestFixture;
import oracle.iam.junit.keycloak.integration.TestFixture.User;

////////////////////////////////////////////////////////////////////////////////
// class RoleRevoke
// ~~~~~ ~~~~~~~~~~
/**
 ** The test case modify operation of roles at the target system leveraging the
 ** connector bundle deployed on a <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class RoleRevoke extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleRevoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleRevoke() {
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
    final String[] parameter = {RoleRevoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   musterfrau
  /**
   ** Test revoke realm role request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void musterfrau() {
    revoke(FACADE.resolveUsername(ObjectClass.ACCOUNT, User.MUSTERFRAU.tag, null), User.MUSTERFRAU.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustermann
  /**
   ** Test revoke realm role request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Max Mustermann</b>
   */
  @Test
  public void mustermann() {
    revoke(FACADE.resolveUsername(ObjectClass.ACCOUNT, User.MUSTERMANN.tag, null), User.MUSTERMANN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zitterbacke
  /**
   ** Test revoke realm role request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Alfons Zitterbacke</b>
   */
  @Test
  public void zitterbacke() {
    revoke(FACADE.resolveUsername(ObjectClass.ACCOUNT, User.ZITTERBACKE.tag, null), User.ZITTERBACKE.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cambrault
  /**
   ** Test revoke realm role request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Gerald Cambrault</b>
   */
  @Test
  public void cambrault() {
    revoke(FACADE.resolveUsername(ObjectClass.ACCOUNT, User.CAMBRAULT.tag, null), User.CAMBRAULT.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Test revoke realm role to any user leveraging connector bundle deployed on
   ** a <code>Java Connector Server</code>.
   */
  protected void revoke(final Uid idenifier, final File path) {
    final OperationOptions option   = null;
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), User.PROVISIONING);
      assertNotNull(provisioning);

      final Descriptor reference = provisioning.reference(Pair.of("Role", "UD_RKC_URL"));
      assertNotNull(reference);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel  model = TestModel.build(path);
      assertNotNull(model);
      
      TestModel.Data roles = null;
      for(TestModel.Data form : model.entitlement) {
        if ("UD_RKC_URL".equals(form.name)) {
          roles = form;
          break;
        }
      }
      assertNotNull("UD_RKC_URL", roles);
      
      for (int i = 0; i < roles.getRowCount(); i++) {
        roles.goToRow(i);
        final Set<Attribute> dataSet = DescriptorTransformer.build(DescriptorTransformer.objectClass("Role"), reference, transfer(roles, reference.attribute()));
        assertNotNull(dataSet);

        FACADE.removeAttributeValues(ObjectClass.ACCOUNT, idenifier, dataSet, option);
      }
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
    catch (Exception e) {
      failed(TaskException.abort(e));
    }
  }
}