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

package oracle.iam.identity.gds.integration;

import java.io.File;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

////////////////////////////////////////////////////////////////////////////////
// class TestCase
// ~~~~~ ~~~~~~~~~~
/**
 ** The general test case to manage entries in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the size of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String          BATCH_SIZE         = "batchSize";
  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the start index of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String          BATCH_START        = "batchStart";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search base(s) of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String          SEARCH_BASE        = "searchBase";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search filter of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String          SEARCH_FILTER      = "searchFilter";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String          SEARCH_ORDER       = "searchOrder";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to obtain entries from the Service Provider incrementally means based
   ** on a synchronization token like <code>changeNumber</code> of timestamps.
   */
  public static final String          INCREMENTAL        = "incremental";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the synchronization strategy of
   ** resources returned from a <code>Service Provider</code>.
   */
  public static final String          SYNCHRONIZE        = "synchronize";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to specifiy the value of a synchronization token.
   */
  public static final String          SYNCHRONIZE_TOKEN  = "synchronizeToken";

  /**
   ** The default start inedx of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  static final int                    BATCH_START_DEFAULT = 1;
  /**
   ** The default size limit of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  static final int                    BATCH_SIZE_DEFAULT  = 500;

  static final File                   PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/gds-account-provisioning.xml");
  static final File                   RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/gds-account-reconciliation.xml");
  /**
   ** The collection of attributes to be returned for entries in the people
   ** branch
   */
  static final String[]                RETURNING     = {"objectClass", "uid", "cn", "sn", "givenName"};
  /**
   ** The location of the group branch
   */
  static final QualifiedUid            ROLEBASE      = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=Roles"));
  /**
   ** The location of the group branch
   */
  static final QualifiedUid            GROUPBASE     = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=Groups"));
  /**
   ** The location of the people branch
   */
  static final QualifiedUid            PEOPLEBASE    = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=People"));
  /**
   ** The location of the system branch
   */
  static final QualifiedUid            SYSTEMBASE    = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=System"));
  /**
   ** The operational options to query the group branch
   */
  static final OperationOptionsBuilder ROLEOPTS      = new OperationOptionsBuilder().setAttributesToGet("objectClass", "cn").setScope(OperationOptions.SCOPE_SUBTREE).setContainer(ROLEBASE);
  /**
   ** The operational options to query the group branch
   */
  static final OperationOptionsBuilder GROUPOPTS     = new OperationOptionsBuilder().setAttributesToGet("objectClass", "cn").setScope(OperationOptions.SCOPE_SUBTREE).setContainer(GROUPBASE);
  /**
   ** The operational options to query the people branch
   */
  static final OperationOptionsBuilder PEOPLEOPTS    = new OperationOptionsBuilder().setAttributesToGet(RETURNING).setScope(OperationOptions.SCOPE_SUBTREE).setContainer(PEOPLEBASE);

  static final String                  ACTOR         = "azitterbacke";
}