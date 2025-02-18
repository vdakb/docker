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

    File        :   Fixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Fixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gds.connector;

import java.io.File;

import org.junit.Assert;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.DirectoryEndpoint;
import oracle.iam.identity.icf.connector.DirectoryLookup;

public class Fixture {

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
  static final DirectoryEndpoint       ENDPOINT      = Network.internet();

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
  static final String[]                EMITACCOUNT    = {"objectClass", "uid", "cn", "sn", "givenName"};
  /**
   ** The collection of attributes to be returned for entries in the group
   ** branch
   */
  static final String[]                EMITGROUP    = {"objectClass", "cn", "uniqueMember"};
  /**
   ** The operational options to query the group branch
   */
  static final OperationOptionsBuilder GROUPOPTS      = new OperationOptionsBuilder().setAttributesToGet(EMITGROUP).setScope(OperationOptions.SCOPE_SUBTREE).setContainer(GROUPBASE);
  /**
   ** The operational options to query the people branch
   */
  static final OperationOptionsBuilder PEOPLEOPTS     = new OperationOptionsBuilder().setAttributesToGet(EMITACCOUNT).setScope(OperationOptions.SCOPE_SUBTREE).setContainer(PEOPLEBASE);

  static final File                    PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/gds-account-provisioning.xml");
  static final File                    RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/gds-account-reconciliation.xml");

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    Handler() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object             each object return from the search.
     **
     ** @return                    <code>true</code> if we should keep processing;
     **                            otherwise <code>false</code> to cancel.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      System.out.println(object.getUid() + "::" + object.getName());
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Fixture</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Fixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryUUID
  /**
   ** Returns te entry <code>UUID</code> the belongs to the specified entry
   ** name.
   **
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
  static Uid entryUUID(final ObjectClass type, final String entryName, final OperationOptions option) {
    final String[] entryUUID = {null};
    try {
      entryUUID[0] = DirectoryLookup.entryUUID(ENDPOINT, type, entryName, option);
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
    return new Uid(entryUUID[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given exception.
   **
   ** @param  message            the identifying exception for the
   **                            <code>AssertionError</code>.
   **                            <br>
   **                            Allowed object is {@link TaskException}.
   */
  static void failed(final TaskException e) {
    failed(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given exception.
   **
   ** @param  message            the identifying exception for the
   **                            <code>AssertionError</code>.
   **                            <br>
   **                            Allowed object is {@link SystemException}.
   */
  static void failed(final SystemException e) {
    failed(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given message.
   **
   ** @param  message            the identifying message for the
   **                            <code>AssertionError</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  static void failed(final String message) {
    Assert.fail(message);
  }
}