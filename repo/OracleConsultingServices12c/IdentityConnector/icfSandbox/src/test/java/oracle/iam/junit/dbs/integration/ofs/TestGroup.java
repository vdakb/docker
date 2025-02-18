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
    Subsystem   :   Openfire Database Connector

    File        :   TestGroup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestGroup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import java.util.Set;
import java.util.Map;

import java.io.File;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;

import oracle.hst.foundation.utility.CollectionUtility;

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

  static final File                PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ofs-group-provisioning.xml");
  static final File                RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ofs-group-reconciliation.xml");

  static interface orclGroupReadPrivilegeGroup {
    static final Uid                   GID    = new Uid("orclGroupReadPrivilegeGroup");
    static final Map<String, Object>   CREATE = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Oracle Group Read Privilege Group"
    );
    static final Map<String, Object>   MODIFY = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Oracle Group Read Privilege Group (Modified)"
    );
    static final Set<Attribute>      GRANT = CollectionUtility.set(
      AttributeBuilder.build(
        "group"
       , new EmbeddedObjectBuilder()
          .setObjectClass(ObjectClass.GROUP)
          .addAttribute(
            AttributeBuilder.build(Uid.NAME,        GID.getUidValue())
          , AttributeBuilder.build(Name.NAME,       GID.getUidValue())
          , AttributeBuilder.build("administrator", 1)
          )
          .build()
      )
    );
  }

  static interface orclGroupWritePrivilegeGroup {
    static final Uid                 GID    = new Uid("orclGroupWritePrivilegeGroup");
    static final Map<String, Object> CREATE = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Oracle Group Write Privilege Group"
    );
    static final Map<String, Object> MODIFY = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Oracle Group Write Privilege Group (Modified)"
    );
    static final Set<Attribute>      GRANT = CollectionUtility.set(
      AttributeBuilder.build(
        "group"
       , new EmbeddedObjectBuilder()
          .setObjectClass(ObjectClass.GROUP)
          .addAttribute(
            AttributeBuilder.build(Uid.NAME,        GID.getUidValue())
          , AttributeBuilder.build(Name.NAME,       GID.getUidValue())
          , AttributeBuilder.build("administrator", 0)
          )
          .build()
      )
    );
  }

  static interface ofsAdministrator {
    static final Uid                 GID      = new Uid("Administratoren");
    static final Map<String, Object> CREATE   = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Administratoren"
    );
    static final Map<String, Object> MODIFY   = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Administratoren (Modified)"
    );
    static final Set<Attribute>      GRANT = CollectionUtility.set(
      AttributeBuilder.build(
        "group"
       , new EmbeddedObjectBuilder()
          .setObjectClass(ObjectClass.GROUP)
          .addAttribute(
            AttributeBuilder.build(Uid.NAME,        GID.getUidValue())
          , AttributeBuilder.build(Name.NAME,       GID.getUidValue())
          , AttributeBuilder.build("administrator", 1)
          )
          .build()
      )
    );
  }
  static interface ofsLeadership1 {
    static final Uid                 GID    = new Uid("Führungsebene 1");
    static final Map<String, Object> CREATE = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Führungsebene 1"
    );
    static final Map<String, Object> MODIFY = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Führungsebene 1 (Modified)"
    );
    static final Set<Attribute>      GRANT = CollectionUtility.set(
      AttributeBuilder.build(
        "group"
       , new EmbeddedObjectBuilder()
          .setObjectClass(ObjectClass.GROUP)
          .addAttribute(
            AttributeBuilder.build(Uid.NAME,        GID.getUidValue())
          , AttributeBuilder.build(Name.NAME,       GID.getUidValue())
          )
          .build()
      )
    );
  }
  static interface ofsLeadership2 {
    static final Uid                 GID    = new Uid("Führungsebene 2");
    static final Map<String, Object> CREATE = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Führungsebene 2"
    );
    static final Map<String, Object> MODIFY = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Führungsebene 2 (Modified)"
    );
    static final Set<Attribute>      GRANT = CollectionUtility.set(
      AttributeBuilder.build(
        "group"
       , new EmbeddedObjectBuilder()
          .setObjectClass(ObjectClass.GROUP)
          .addAttribute(
            AttributeBuilder.build(Uid.NAME,        GID.getUidValue())
          , AttributeBuilder.build(Name.NAME,       GID.getUidValue())
          )
          .build()
      )
    );
  }
  static interface ofsRevisoren {
    static final Uid                 GID    = new Uid("Revisoren");
    static final Map<String, Object> CREATE = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Revisoren"
    );
    static final Map<String, Object> MODIFY = CollectionUtility.map(
        "UD_OFS_GRP_GID",         GID.getUidValue()
      , "UD_OFS_GRP_DESCRIPTION", "Revisoren (Modified)"
    );
    static final Set<Attribute>      GRANT = CollectionUtility.set(
      AttributeBuilder.build(
        "group"
       , new EmbeddedObjectBuilder()
          .setObjectClass(ObjectClass.GROUP)
          .addAttribute(
            AttributeBuilder.build(Uid.NAME,        GID.getUidValue())
          , AttributeBuilder.build(Name.NAME,       GID.getUidValue())
          , AttributeBuilder.build("administrator", 1)
          )
          .build()
      )
    );
  }
}