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
    Subsystem   :   Generic SCIM 1 Connector

    File        :   TestDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.scim.v1;

import java.util.Set;

import java.io.File;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.connector.service.AttributeFactory;

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
   ** The name a connector service consumer will put in the appropriate options
   ** of a reconciliation process to configure the size of a batch of resources
   ** returned from a Service Provider.
   */
  static final String         BATCH_SIZE_OPTION    = "batchSize";

  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  static final String         SEARCH_ORDER_OPTION  = "searchOrder";

  static Uid                  uid                  = new Uid("111111111");

  static final Set<Attribute> CREATE               = AttributeFactory.set(
    new String[]{
      Name.NAME
      , "userType"
      , OperationalAttributes.PASSWORD_NAME
      , OperationalAttributes.ENABLE_NAME
      , "displayName"
      , "name.givenName"
      , "name.familyName"
      , "name.formatted"
    }
    , new Object[]{
        "azitterbacke"
      , "EMP"
      , new GuardedString("Welcome1".toCharArray())
      , Boolean.TRUE
      , "Zitterbacke, Alfons"
      , "Alfons"
      , "Zitterbacke"
      , "Alfons Zitterbacke"
    }
  );
  static final Set<Attribute> MODIFY         = AttributeFactory.set(
    new String[]{ "displayName" }
  , new Object[]{ "Alfons Zitterbacke"}
  );

  static final File           PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfRESTFul/src/main/resources/mds/scim-account-provisioning.xml");
  static final File           RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfRESTFul/src/main/resources/mds/scim-account-reconciliation.xml");
}