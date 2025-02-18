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
    Subsystem   :   Identity Governance Service Provisioning

    File        :   Base.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Base.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.scopedrole;

import java.io.File;

import org.identityconnectors.framework.common.objects.Uid;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;

import oracle.iam.identity.connector.service.Descriptor;

import oracle.iam.junit.jes.integration.TestFixture;
import oracle.iam.junit.jes.integration.TestReconciliation;

////////////////////////////////////////////////////////////////////////////////
// class Base
// ~~~~~ ~~~~
/**
 ** The base class for CRUD operation on scoped roles (aka AdminRole) at the
 ** target system leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Base extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The UID belonging to an known scoped admin role <i>OrclOIMEntitlementAdministrator</i>.
   */
  public static final Uid  UID_ENTADMIN   = new Uid("7");
  /**
   ** The UID belonging to an known scoped admin role <i>OrclOIMEntitlementAuthorizer</i>.
   */
  public static final Uid  UID_ENTAUTHZ   = new Uid("8");
  /**
   ** The UID belonging to an known scoped admin role <i>OrclOIMEntitlementViewer</i>.
   */
  public static final Uid  UID_ENTVIEWER  = new Uid("9");
  /**
   ** The UID belonging to an known scoped admin role <i>OrclOIMOrgAdministrator</i>.
   */
  public static final Uid  UID_ORGADMIN   = new Uid("13");
  /**
   ** The UID belonging to an known scoped admin role <i>OrclOIMOrgViewer</i>.
   */
  public static final Uid  UID_ORGVIEWER  = new Uid("14");
  /**
   ** The UID belonging to an known scoped admin role <i>OrclOIMRoleAdministrator</i>.
   */
  public static final Uid  UID_ROLEADMIN  = new Uid("4");
  /**
   ** The UID belonging to an known scoped admin role <i>OrclOIMRoleAuthorizer</i>.
   */
  public static final Uid  UID_ROLEAUTHZ  = new Uid("5");
  /**
   ** The UID belonging to an unknown scoped admin role <i>OrclOIMRoleViewer</i>.
   */
  public static final Uid  UID_ROLEVIEWER = new Uid("6");
  /**
   ** The UID belonging to an unknown scoped admin role <i>OrclOIMUserViewer</i>.
   */
  public static final Uid  UID_USERVIEWER = new Uid("17");
  /**
   ** The UID belonging to an unknown scoped admin role <i>OrclOIMUserAdmin</i>.
   */
  public static final Uid  UID_USERADMIN  = new Uid("15");
  /**
   ** The UID belonging to an unknown scoped admin role <i>OrclOIMUserHelpDesk</i>.
   */
  public static final Uid  UID_USERHDESK  = new Uid("16");
  /**
   ** The UID belonging to an unknown scoped admin role <i>Unknown</i>.
   */
  public static final Uid  UID_UNKNOWN    = new Uid("999999");

  public static final File PROVISIONING   = new File("src/test/resources/mds/oig-scopedrole-provisioning.xml");
  public static final File RECONCILIATION = new File("src/test/resources/mds/oig-scopedrole-reconciliation.xml");

  //////////////////////////////////////////////////////////////////////////////
  // class Handler
  // ~~~~~ ~~~~~~~
  /**
   ** Callback implementation for operations that are returning one or more
   ** results. Currently used only by Search, but may be used by other
   ** operations in the future.
   */
  static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Descriptor subject;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Handler</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Handler(final Descriptor descriptor) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.subject = descriptor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object             each object return from the search.
     **
     ** @return                    <code>true</code> if we should keep processing;
     **                            otherwise <code>false</code> to cancel.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      try {
        assertEquals(object.getObjectClass(), ObjectClass.ACCOUNT);
        assertNotNull(object.getUid());
        assertNotNull(object.getName());

        CONSOLE.info(TestReconciliation.buildEvent(-1L, object, this.subject, false).toString());
        return true;
      }
      catch (AssertionError e) {
        return false;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Base</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Base() {
    // ensure inheritance
    super();
  }
}