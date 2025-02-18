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
    Subsystem   :   Atlassian Jira Server Connector

    File        :   TestUser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestUser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.ajs;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import java.io.File;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.junit.TestCaseIntegration;

////////////////////////////////////////////////////////////////////////////////
// class TestUser
// ~~~~~ ~~~~~~~~
/**
 ** The general test case to manage entries in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestUser extends TestCaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final File                PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ajs-account-provisioning.xml");
  static final File                RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ajs-account-reconciliation.xml");

  static interface bkbk4711123 {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid                 UID    = new Uid("bkbk4711123");
    static final Uid                 NEW    = new Uid("bkbk4711123xxxx");
    static final Map<String, Object> CREATE = new HashMap<String, Object>() {{
      put("UD_AJS_USR_UID",          UID.getUidValue());
      put("UD_AJS_USR_PWD",          "Welcome1");
      put("UD_AJS_USR_DISPLAY_NAME", "Korbyn, Colon");
      put("UD_AJS_USR_EMAIL",        "bkbk4711123@vm.oracle.com");
    }};
    static final Map<String, Object> MODIFY = new HashMap<String, Object>() {{
      put("UD_AJS_USR_DISPLAY_NAME", "Korbyn, Colon (Modified)");
    }};
    static final Map<String, Object> ORIGIN = new HashMap<String, Object>() {{
      put("UD_AJS_USR_UID",          UID.getUidValue());
    }};
    static final Map<String, Object> RENAME = new HashMap<String, Object>() {{
      put("UD_AJS_USR_UID",          NEW.getUidValue());
    }};
    static final Map<String, Object> PASSWORD = new HashMap<String, Object>() {{
      put("UD_AJS_USR_PWD",          "Welcome1");
    }};
  }

  /** Build the attribute to enable an account. */
  static final Set<Attribute> ENABLE  = CollectionUtility.set(AttributeBuilder.buildEnabled(true));
  /** Build the attribute to diable an account. */
  static final Set<Attribute> DISABLE = CollectionUtility.set(AttributeBuilder.buildEnabled(false));
}