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
    Subsystem   :   Atlassian Jira Connector

    File        :   TestUser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    TestUser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-04  SBernet     First release version
*/

package oracle.iam.identity.gws.connector.jira;

import java.util.Set;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.connector.service.AttributeFactory;

////////////////////////////////////////////////////////////////////////////////
// class TestUser
// ~~~~~ ~~~~~~~~
/**
 ** The test case to create a resource at the Service Provider.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestUser {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  static String[]              UC    = {"sylvertC", "sysycompanie"};
  static String[]              UM    = {"sophie",   "BK0012ED"};
  static final Set<Attribute> CREATE = AttributeFactory.set(
    new String[]{
        Uid.NAME
      , Name.NAME
      , "emailAddress"
      , "displayName"
    }
    , new Object[]{
        UC[0]
      , UC[1]
      , "sophieEE@berner.re"
      , "Sylvert"
    }
  );

  static final Set<Attribute> MODIFY = AttributeFactory.set(
    new String[]{
        Uid.NAME
      , OperationalAttributes.PASSWORD_NAME
      , "username"
      , "emailAddress"
      , "displayName"
    }
    , new Object[]{
        UM[0]
      , new GuardedString("Welcome1".toCharArray())
      , UM[1]
      , "sophie@berner.re"
      , "Sophie from Oracle"
    }
  );
}