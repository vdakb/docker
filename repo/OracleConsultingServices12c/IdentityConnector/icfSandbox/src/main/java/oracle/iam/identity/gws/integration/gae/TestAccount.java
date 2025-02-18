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

    File        :   TestAccount.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestAccount.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.gae;

import java.util.Set;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.connector.service.AttributeFactory;

////////////////////////////////////////////////////////////////////////////////
// class TestAccount
// ~~~~~ ~~~~~~~~~~~
/**
 ** The general test case to manage entries in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestAccount {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Uid         UID         = new Uid("bkbk4711123@bka.bund.de");

  static final Set<Attribute> CREATE = AttributeFactory.set(
    new String[]{
        Name.NAME
      , OperationalAttributes.PASSWORD_NAME
      , "firstName"
      , "lastName"
    }
    , new Object[]{
        UID.getUidValue()
      , new GuardedString("Welcome1".toCharArray())
      , "Korbyn"
      , "Colon"
    }
  );
  static final Set<Attribute> MODIFY = AttributeFactory.set(
    new String[]{
        Name.NAME
      , OperationalAttributes.PASSWORD_NAME
      , "firstName"
      , "lastName"
    }
    , new Object[]{
        UID.getUidValue()
      , new GuardedString("Welcome1".toCharArray())
      , "Korbyn"
      , "Colon"
    }
  );
}