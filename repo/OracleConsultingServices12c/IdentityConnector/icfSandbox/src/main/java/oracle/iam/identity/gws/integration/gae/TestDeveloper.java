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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   TestDeveloper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestDeveloper.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.gae;

import java.util.Set;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.connector.service.AttributeFactory;

import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.QualifiedUid;

abstract class TestDeveloper extends TestTenant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Uid          UID      = new Uid("bkbk4711123@bka.bund.de");
  static final QualifiedUid OID      = new QualifiedUid(TENANT, new Uid("apigee"));

  static final Set<Attribute> CREATE = AttributeFactory.set(
    new String[]{
        Name.NAME
      , "userName"
      , "firstName"
      , "lastName"
    }
    , new Object[]{
        UID.getUidValue()
      , "bkbk4711123"
      , "Korbyn"
      , "Colon"
    }
  );
  static final Set<Attribute> MODIFY = AttributeFactory.set(
    new String[]{
        Name.NAME
      , "__ENABLE__"
      , "userName"
      , "firstName"
      , "lastName"
    }
    , new Object[]{
        UID.getUidValue()
      , "active"
      , "bkbk4711123"
      , "Korbyn"
      , "Colon"
    }
  );
}