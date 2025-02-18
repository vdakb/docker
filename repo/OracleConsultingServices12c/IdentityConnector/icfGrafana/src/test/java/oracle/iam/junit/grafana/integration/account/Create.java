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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Delete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.integration.account;

import java.util.Set;

import java.io.File;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.junit.grafana.integration.TestModel;
import oracle.iam.junit.grafana.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation of accounts at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@icloud.com
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
  // Method:   cambrault
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Gerald Cambrault</b>
   */
  @Test
  public void cambrault() {
    assertNotNull(User.CAMBRAULT.tag, createUser(User.CAMBRAULT.value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   musterfrau
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void musterfrau() {
    assertNotNull(User.MUSTERFRAU.tag, createUser(User.MUSTERFRAU.value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustermann
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Max Mustermann</b>
   */
  @Test
  public void mustermann() {
    assertNotNull(User.MUSTERMANN.tag, createUser(User.MUSTERMANN.value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zitterbacke
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   ** <p>
   ** <b>Alfons Zitterbacke</b>
   */
  @Test
  public void zitterbacke() {
    assertNotNull(User.ZITTERBACKE.tag, createUser(User.ZITTERBACKE.value));
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
   **                            Allowed object is {@link File}.
   **
   ** @return                    the internal identifier of the account created.
   **                            <br>
   **                            Possible object is {@link UID}.
   */
  private Uid createUser(final File path) {
    final OperationOptions option   = null;
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), User.PROVISIONING);
      assertNotNull(provisioning);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel      model   = TestModel.build(path);
      assertNotNull(model);

      final Set<Attribute> dataSet = DescriptorTransformer.build(provisioning, transfer(model.account, provisioning.attribute()));
      assertNotNull(dataSet);

      final Uid uid = facade(endpoint()).create(ObjectClass.ACCOUNT, dataSet, option);
      assertNotNull(uid);
      System.out.println(uid.getUidValue());
      return uid;
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