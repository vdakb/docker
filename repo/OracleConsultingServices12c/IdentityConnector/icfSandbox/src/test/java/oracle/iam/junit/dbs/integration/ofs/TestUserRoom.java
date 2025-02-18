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

    File        :   TestUserRoom.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestUserRoom.


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

////////////////////////////////////////////////////////////////////////////////
// class TestUserRoom
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The test case to grant and revoke room access to accounts leveraging
 ** the <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserRoom extends TestUser {

  static interface orclShowRoom {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid                   SID     = TestRoom.orclShowRoom.SID;
    static final EmbeddedObjectBuilder CONTENT = new EmbeddedObjectBuilder()
      .setObjectClass(TestSchema.ROOM)
      .addAttributes(AttributeFactory.set(new String[]{Uid.NAME, "administrator"}, new Object[]{SID.getUidValue(), false})
    );
  }

  static interface orclPrivateRoom {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid                   SID     = TestRoom.orclPrivateRoom.SID;
    static final EmbeddedObjectBuilder CONTENT = new EmbeddedObjectBuilder()
      .setObjectClass(TestSchema.ROOM)
      .addAttributes(AttributeFactory.set(new String[]{Uid.NAME, "administrator"}, new Object[]{SID.getUidValue(), true})
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestUserRoom</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestUserRoom() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclShowRoomAssign
  /**
   ** Test that accounts could be granted.
   */
	@Test
	public void orclShowRoomAssign() {
    try {
      final Uid uid = Network.facade(Network.intranet()).addAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, room(orclShowRoom.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclShowRoomRevoke
  /**
   ** Test that accounts could be created.
   */
	@Test
	public void orclShowRoomRevoke() {
    try {
      final Uid uid = Network.facade(Network.intranet()).removeAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, room(orclShowRoom.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclPrivateRoomAssign
  /**
   ** Test that accounts could be created.
   */
	@Test
	public void orclPrivateRoomAssign() {
    try {
      final Uid uid = Network.facade(Network.intranet()).addAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, room(orclPrivateRoom.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclPrivateRoomRevoke
  /**
   ** Test that accounts could be created.
   */
  @Test
	public void orclPrivateRoomRevoke() {
    try {
      final Uid uid = Network.facade(Network.intranet()).removeAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, room(orclPrivateRoom.CONTENT), null);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   room
  public static Set<Attribute> room(final EmbeddedObjectBuilder builder)
    throws TaskException {

    return CollectionUtility.set(AttributeBuilder.build("room", builder.build()));
  }
}
