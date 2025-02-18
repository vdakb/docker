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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   TestDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gds.connector;

import java.io.File;

import oracle.hst.foundation.SystemConsole;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.DirectoryLookup;
import oracle.iam.identity.icf.connector.DirectoryEndpoint;

////////////////////////////////////////////////////////////////////////////////
// class TestCase
// ~~~~~ ~~~~~~~~
/**
 ** The general test case to manage entries in the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The console used for logging purpose
   */
  static final SystemConsole           CONSOLE       = new SystemConsole("gds");

  /**
   ** The connection to the target system
   */
  static final DirectoryEndpoint       ENDPOINT      = Network.extranet();

  /**
   ** The location of the role branch
   */
  static final QualifiedUid            ROLEBASE      = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=Roles"));
  /**
   ** The location of the group branch
   */
  static final QualifiedUid            GROUPBASE      = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=Groups"));
  /**
   ** The location of the people branch
   */
  static final QualifiedUid            PEOPLEBASE     = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=People"));
  /**
   ** The location of the system branch
   */
  static final QualifiedUid            SYSTEMBASE     = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=System"));
  /**
   ** The collection of attributes to be returned for entries in the people
   ** branch
   */
  static final String[]                RETURNING      = {"objectClass", "uid", "cn", "sn", "givenName"};
  /**
   ** The operational options to query the group branch
   */
  static final OperationOptionsBuilder GROUPOPTS      = new OperationOptionsBuilder().setAttributesToGet("objectClass", "cn").setScope(OperationOptions.SCOPE_SUBTREE).setContainer(GROUPBASE);
  /**
   ** The operational options to query the people branch
   */
  static final OperationOptionsBuilder PEOPLEOPTS     = new OperationOptionsBuilder().setAttributesToGet(RETURNING).setScope(OperationOptions.SCOPE_SUBTREE).setContainer(PEOPLEBASE);

  static final File                    PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/gds-account-provisioning.xml");
  static final File                    RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/gds-account-reconciliation.xml");

  static final String                  ACTOR          = "azitterbacke";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   peopleUUID
  /**
   ** Returns te entry <code>UUID</code> the belongs to the specified entry
   ** name.
   **
   ** @param  endpoint           the {@link DirectoryContext} to perform the
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  entryName          the name of the entry used in the
   **                            <code>RDN</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    if entry <code>UUID</code> wrapped in an ICF
   **                           {@link UID}.
   */
  public static Uid peopleUUID(final DirectoryEndpoint endpoint, final String entryName) {
    return entryUUID(endpoint, ObjectClass.ACCOUNT, entryName, PEOPLEOPTS.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupUUID
  /**
   ** Returns te entry <code>UUID</code> the belongs to the specified entry
   ** name.
   **
   ** @param  endpoint           the {@link DirectoryContext} to perform the
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  entryName          the name of the entry used in the
   **                            <code>RDN</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    if entry <code>UUID</code> wrapped in an ICF
   **                           {@link UID}.
   */
  public static Uid groupUUID(final DirectoryEndpoint  endpoint, final String entryName) {
    return entryUUID(endpoint, ObjectClass.GROUP, entryName, GROUPOPTS.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryUUID
  /**
   ** Returns te entry <code>UUID</code> the belongs to the specified entry
   ** name.
   **
   ** @param  endpoint           the {@link DirectoryContext} to perform the
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to create and entry in
   **                            the Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  entryName          the name of the entry used in the
   **                            <code>RDN</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    if entry <code>UUID</code> wrapped in an ICF
   **                           {@link UID}.
   */
  public static Uid entryUUID(final DirectoryEndpoint  endpoint, final ObjectClass type, final String entryName, final OperationOptions option) {
    final String[] entryUUID = {null};
    try {
      entryUUID[0] = DirectoryLookup.entryUUID(endpoint, type, entryName, option);
    }
    catch (SystemException e) {
     CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));;
    }
    return new Uid(entryUUID[0]);
  }
}