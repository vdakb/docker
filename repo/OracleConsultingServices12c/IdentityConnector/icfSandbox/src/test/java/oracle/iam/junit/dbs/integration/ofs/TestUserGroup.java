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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Openfire Database Connector

    File        :   TestUserGroup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestUserGroup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import java.util.Set;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.AttributeFactory;

import org.junit.Test;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;

import org.springframework.core.annotation.Order;

////////////////////////////////////////////////////////////////////////////////
// class TestUserGroup
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The test case to grant and revoke group memberships to accounts leveraging
 ** the <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserGroup extends TestUser {

  static interface orclGroupReadPrivilegeGroup {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid                   GID     = TestGroup.orclGroupReadPrivilegeGroup.GID;
    static final EmbeddedObjectBuilder CONTENT = new EmbeddedObjectBuilder()
      .setObjectClass(ObjectClass.GROUP)
      .addAttributes(AttributeFactory.set(new String[]{Uid.NAME, "administrator"}, new Object[]{GID.getUidValue(), false})
    );
  }

  static interface orclGroupWritePrivilegeGroup {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid                   GID     = new Uid("orclGroupWritePrivilegeGroup");
    static final EmbeddedObjectBuilder CONTENT = new EmbeddedObjectBuilder()
      .setObjectClass(ObjectClass.GROUP)
      .addAttributes(AttributeFactory.set(new String[]{Uid.NAME, "administrator"}, new Object[]{GID.getUidValue(), true})
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestUserGroup</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestUserGroup() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclGroupReadPrivilegeGroupAssign
  /**
   ** Test that accounts could be created.
   */
	@Test
  @Order(1)
	public void orclGroupReadPrivilegeGroupAssign() {
    try {
      final Uid uid = Network.facade(Network.intranet()).addAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, group(orclGroupReadPrivilegeGroup.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclGroupReadPrivilegeGroupRevoke
  /**
   ** Test that accounts could be created.
   */
	@Test
	public void orclGroupReadPrivilegeGroupRevoke() {
    try {
      final Uid uid = Network.facade(Network.intranet()).removeAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, group(orclGroupReadPrivilegeGroup.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclGroupWritePrivilegeGroupAssign
  /**
   ** Test that accounts could be created.
   */
	@Test
	public void orclGroupWritePrivilegeGroupAssign() {
    try {
      final Uid uid = Network.facade(Network.intranet()).addAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, group(orclGroupWritePrivilegeGroup.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclGroupWritePrivilegeGroupRevoke
  /**
   ** Test that accounts could be created.
   */
  @Test
	public void orclGroupWritePrivilegeGroupRevoke() {
    try {
      final Uid uid = Network.facade(Network.intranet()).removeAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, group(orclGroupWritePrivilegeGroup.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  public static Set<Attribute> group(final EmbeddedObjectBuilder builder)
    throws TaskException {

    return CollectionUtility.set(AttributeBuilder.build("group", builder.build()));
  }
}
