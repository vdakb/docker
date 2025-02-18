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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving accounts by its username at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resolve extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resolve</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resolve() {
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
    final String[] parameter = {Resolve.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSystemAdministrator
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveSystemAdministrator() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "igssysadm", null);
      assertNotNull(uid);
      assertEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveRegularUser
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveRegularUser() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "azitterbacke", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserAN
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserAN() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "anserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserBB
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserBB() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "bbserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserBE
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserBE() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "beserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserBK
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserBK() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "bkserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserBP
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserBP() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "bpserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserBW
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserBW() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "bwserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserBY
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserBY() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "byserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserMV
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserMV() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "mvserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserNI
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserNI() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "niserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserNW
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserNW() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "nwserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserRP
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserRP() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "rpserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserSH
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserSH() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "shserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserSL
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserSL() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "slserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserSN
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserSN() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "snserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserTH
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserTH() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "thserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveServiceUserZF
  /**
   ** Test that a particular account could be fetched by its unique identifier.
   */
  @Test
  public void resolveServiceUserZF() {
    try {
      final Uid uid = facade(service()).resolveUsername(ObjectClass.ACCOUNT, "zfserviceuser", null);
      assertNotNull(uid);
      assertNotEquals(uid.getUidValue(), "-1");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}