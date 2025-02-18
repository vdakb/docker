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
    Subsystem   :   OpenFire REST Connector

    File        :   TestGroup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestGroup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.ofs;

import java.util.Set;

import oracle.hst.foundation.utility.CollectionUtility;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;

import oracle.iam.identity.connector.service.AttributeFactory;

abstract class TestGroup {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final EmbeddedObjectBuilder GRANT = new EmbeddedObjectBuilder()
    .setObjectClass(ObjectClass.GROUP)
    .addAttributes(
       AttributeFactory.set(new String[]{Uid.NAME, Name.NAME}, new Object[]{"apigee", "opsadmin"}
    )
  );

  // build the attributes
  static final AttributeBuilder      BUILDER = new AttributeBuilder().setName(ObjectClass.GROUP_NAME).addValue(GRANT.build());
  static final Set<Attribute>        GROUP   = CollectionUtility.set(BUILDER.build());
}