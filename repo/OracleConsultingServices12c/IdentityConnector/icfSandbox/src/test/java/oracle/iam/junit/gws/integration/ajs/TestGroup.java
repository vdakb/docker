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

    File        :   TestGroup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestGroup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.ajs;

import java.util.Set;
import java.util.Map;

import java.io.File;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.junit.TestCaseIntegration;

////////////////////////////////////////////////////////////////////////////////
// class TestGroup
// ~~~~~ ~~~~~~~~~
/**
 ** The general test case to manage entries in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestGroup extends TestCaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final File                PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ajs-group-provisioning.xml");
  static final File                RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ajs-group-reconciliation.xml");

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface jiraAdministrator
  // ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
  /**
   ** The Jira Administrator Group
   */
  static interface jiraAdministrator {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid                 GID    = new Uid("jira-administrators");
    static final Map<String, Object> CREATE = CollectionUtility.map(
      "UD_AJS_GRP_GID",         GID.getUidValue()
    , "UD_AJS_GRP_NAME",        "Administrator"
    , "UD_AJS_GRP_DESCRIPTION", "Benutzern die zu dieser Gruppe hinzugefügt werden, wird Administratorzugriff gewährt"
    );
    static final Map<String, Object> MODIFY = CollectionUtility.map(
      "UD_AJS_GRP_GID",         GID.getUidValue()
    , "UD_AJS_GRP_NAME",        "Administrator"
    , "UD_AJS_GRP_DESCRIPTION", "Benutzern die zu dieser Gruppe hinzugefügt werden, wird Administratorzugriff gewährt"
    );
    static final EmbeddedObjectBuilder GRANT = new EmbeddedObjectBuilder()
      .setObjectClass(ObjectClass.GROUP)
      .addAttributes(AttributeFactory.set(new String[]{Uid.NAME, Name.NAME}, new Object[]{GID.getUidValue(), GID.getUidValue()})
    );

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   grant
    public static Set<Attribute> grant()
      throws TaskException {

      return CollectionUtility.set(AttributeBuilder.build("group", GRANT.build()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface jiraCoreUser
  // ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
  /**
   ** The Jira User Group
   */
  static interface jiraCoreUser {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid                 GID     = new Uid("jira-core-users");
    static final Map<String, Object> CREATE  = CollectionUtility.map(
      "UD_AJS_GRP_GID",         GID.getUidValue()
    , "UD_AJS_GRP_NAME",        "Jira Core"
    , "UD_OFS_GRP_DESCRIPTION", "Die zu dieser Gruppe hinzugefügte Benutzer erhalten Zugriff auf Jira Core"
    );
    static final Map<String, Object> MODIFY  = CollectionUtility.map(
      "UD_AJS_GRP_GID",         GID.getUidValue()
    , "UD_AJS_GRP_NAME",        "Jira Core"
    , "UD_OFS_GRP_DESCRIPTION", "Die zu dieser Gruppe hinzugefügte Benutzer erhalten Zugriff auf Jira Core"
    );
    static final EmbeddedObjectBuilder GRANT = new EmbeddedObjectBuilder()
      .setObjectClass(ObjectClass.GROUP)
      .addAttributes(AttributeFactory.set(new String[]{Uid.NAME, Name.NAME}, new Object[]{GID.getUidValue(), GID.getUidValue()})
    );

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   grant
    public static Set<Attribute> grant()
      throws TaskException {

      return CollectionUtility.set(AttributeBuilder.build("group", GRANT.build()));
    }
  }
}