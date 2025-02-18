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

    File        :   TestFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.jes.ServerEndpoint;

import oracle.iam.identity.icf.connector.oig.remote.Context;

import org.identityconnectors.framework.common.objects.Uid;

////////////////////////////////////////////////////////////////////////////////
// class TestFixture
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>TestFixture</code> implements the basic functionality of a Test
 ** Case that requires connection to the target system.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFixture extends TestBaseConnector {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static Context CONTEXT;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Tenant
  // ~~~~~~~~~ ~~~~~~
  public interface Tenant {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The UID belonging to a organization <i>Xellerate Users</i>.
     */
    static final Pair<String, String> LEGACY  = Pair.of("Xellerate Users", "1");
    /**
     ** The UID belonging to a organization <i>Top</i>.
     */
    static final Pair<String, String> TOP     = Pair.of(OrganizationManagerConstants.TOP_ORGANIZATION_NAME, "3");
    /**
     ** The UID belonging to a organization <i>AN</i>.
     */
    static final Pair<String, String> AN      = Pair.of("AN", "21");
    /**
     ** The UID belonging to a organization <i>BB</i>.
     */
    static final Pair<String, String> BB      = Pair.of("BB", "22");
    /**
     ** The UID belonging to a organization <i>BE</i>.
     */
    static final Pair<String, String> BE      = Pair.of("BE", "23");
    /**
     ** The UID belonging to a organization <i>BK</i>.
     */
    static final Pair<String, String> BK      = Pair.of("BK", "24");
    /**
     ** The UID belonging to a organization <i>BP</i>.
     */
    static final Pair<String, String> BP      = Pair.of("BP", "25");
    /**
     ** The UID belonging to a organization <i>BW</i>.
     */
    static final Pair<String, String> BW      = Pair.of("BW", "26");
    /**
     ** The UID belonging to a organization <i>BY</i>.
     */
    static final Pair<String, String> BY      = Pair.of("BY", "27");
    /**
     ** The UID belonging to an unknown organization <i>Unknown</i>.
     */
    static final Pair<String, String> UNKNOWN = Pair.of("Unknown", "999999");
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Identity
  // ~~~~~~~~~ ~~~~~~~~
  public interface Identity {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The UID belonging to the system administrator <i>xelsysadm</i>.
     */
    static final Pair<String, String> XELSYSADM   = Pair.of("xelsysadm", "1");
    /**
     ** The UID belonging to the internel administrator <i>XELOPERATOR</i>.
     */
    static final Pair<String, String> XELOPERATOR = Pair.of("XELOPERATOR", "2");
    /**
     ** The UID belonging to the server administrator <i>weblogic</i>.
     */
    static final Pair<String, String> WEBLOGIC    = Pair.of("weblogic", "3");
    /**
     ** The UID belonging to the internel administrator <i>oiminternal</i>.
     */
    static final Pair<String, String> OIMINTERNAL = Pair.of("oiminternal", "4");
    /**
     ** The UID belonging to Alfons Zitterbacke <i>an4711123</i>.
     */
    static final Pair<String, String> ZITTERBACKE = Pair.of("an4711123", "2001");
    /**
     ** The UID belonging to Gerald Cambrault <i>an4711124</i>.
     */
    static final Pair<String, String> CAMBRAULT   = Pair.of("an4711124", "3001");
    /**
     ** The UID belonging to Agathe Musterfrau <i>bp4711124</i>.
     */
    static final Pair<String, String> MUSTERFRAU  = Pair.of("bp4711123", "1046");
    /**
     ** The UID belonging to an unknown user <i>Unknown</i>.
     */
    static final Pair<String, String> UNKNOWN     = Pair.of("Unknown", "999999");
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface SystemRole
  // ~~~~~~~~~ ~~~~~~~~~~
  public interface SystemRole {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The UID belonging to an unknown global admin role <i>Administrators</i>.
     */
    static final Pair<String, String> OLDADMIN  = Pair.of(RoleManagerConstants.SYS_ADMIN_ROLE_NAME, "1");
    /**
     ** The UID belonging to a system role <i>OPERATORS</i>.
     */
    static final Pair<String, String> OPERATORS = Pair.of("OPERATORS", "2");
    /**
     ** The UID belonging to a system role <i>ALL USERS</i>.
     */
    static final Pair<String, String> ALLUSERS  = Pair.of("ALL USERS", "3");
    /**
     ** The UID belonging to a system role <i>SELF OPERATORS</i>.
     */
    static final Pair<String, String> SELFOP    = Pair.of("SELF OPERATORS", "4");
    /**
     ** The UID belonging to a system role <i>Administrators</i>.
     */
    static final Pair<String, String> NEWADMIN  = Pair.of("Administrators", "5");
    /**
     ** The UID belonging to a system role <i>BIReportAdministrator</i>.
     */
    static final Pair<String, String> BIRADMIN  = Pair.of("BIReportAdministrator", "6");
    /**
     ** The UID belonging to a system role <i>MNR_Test-Role-1</i>.
     */
    static final Pair<String, String> MNRTEST1  = Pair.of("MNR_Test-Role-1", "22");
    /**
     ** The UID belonging to a system role <i>MNR_Test-Role-2</i>.
     */
    static final Pair<String, String> MNRTEST2  = Pair.of("MNR_Test-Role-2", "21");
    /**
     ** The UID belonging to an unknown system role <i>Unknown</i>.
     */
    static final Pair<String, String> UNKNOWN   = Pair.of("Unknown", "999999");
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface GlobalRole
  // ~~~~~~~~~ ~~~~~~~~~~
  public interface GlobalRole {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMSystemAdministrator</i>.
     */
    static final Pair<String, Long> SYSADMIN   = Pair.of("OrclOIMSystemAdministrator", 1L);
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMSystemConfigurator</i>.
     */
    static final Pair<String, Long> SYSCONFIG  = Pair.of("OrclOIMSystemConfigurator", 2L);
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMCatalogAdmin</i>.
     */
    static final Pair<String, Long> CATADMIN   = Pair.of("OrclOIMCatalogAdmin", 3L);
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMCertificationAdministrator</i>.
     */
    static final Pair<String, Long> CERTADMIN  = Pair.of("OrclOIMCertificationAdministrator", 22L);
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMCertificationViewer</i>.
     */
    static final Pair<String, Long> CERTVIEWER = Pair.of("OrclOIMCertificationViewer", 23L);
    /**
     ** The UID belonging to an unknown scope admin role <i>Unknown</i>.
     */
    static final Pair<String, Long> UNKNOWN   = Pair.of("Unknown", 999999L);
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface ScopedRole
  // ~~~~~~~~~ ~~~~~~~~~~
  public interface ScopedRole {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The UID belonging to a scoped admin role
     ** <i>OrclOIMEntitlementAdministrator</i>.
     */
    static final Pair<String, Long> ENTADMIN   = Pair.of("OrclOIMEntitlementAdministrator", 7L);
    /**
     ** The UID belonging to a scoped admin role
     ** <i>OrclOIMEntitlementAuthorizer</i>.
     */
    static final Pair<String, Long> ENTAUTHZ   = Pair.of("OrclOIMEntitlementAuthorizer", 8L);
    /**
     ** The UID belonging to a scoped admin role
     ** <i>OrclOIMEntitlementViewer</i>.
     */
    static final Pair<String, Long> ENTVIEWER  = Pair.of("OrclOIMEntitlementViewer", 9L);
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMOrgAdministrator</i>.
     */
    static final Pair<String, Long> ORGADMIN   = Pair.of("OrclOIMOrgAdministrator", 13L);
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMOrgViewer</i>.
     */
    static final Pair<String, Long> ORGVIEWER  = Pair.of("OrclOIMOrgViewer", 14L);
    /**
     ** The UID belonging to a scoped admin role
     ** <i>OrclOIMRoleAdministrator</i>.
     */
    static final Pair<String, Long> ROLADMIN   = Pair.of("OrclOIMRoleAdministrator", 4L);
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMRoleAuthorizer</i>.
     */
    static final Pair<String, Long> ROLAUTHZ   = Pair.of("OrclOIMRoleAuthorizer", 5L);
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMRoleViewer</i>.
     */
    static final Pair<String, Long> ROLVIEWER  = Pair.of("OrclOIMRoleViewer", 6L);
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMUserAdmin</i>.
     */
    static final Pair<String, Long> USRADMIN   = Pair.of("OrclOIMUserAdmin", 15L);
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMUserHelpDesk</i>.
     */
    static final Pair<String, Long> USRHDESK   = Pair.of("OrclOIMUserHelpDesk", 16L);
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMUserViewer</i>.
     */
    static final Pair<String, Long> USRVIEWER  = Pair.of("OrclOIMUserViewer", 17L);
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorAN</i>.
     */
    static final Pair<String, Long> FIMADMINAN = Pair.of("fimIdentityAdministratorAN", 44L);
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBB</i>.
     */
    static final Pair<String, Long> FIMADMINBB = Pair.of("fimIdentityAdministratorBB", 48L);
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBE</i>.
     */
    static final Pair<String, Long> FIMADMINBE = Pair.of("fimIdentityAdministratorBE", 52L);
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBK</i>.
     */
    static final Pair<String, Long> FIMADMINBK = Pair.of("fimIdentityAdministratorBK", 56L);
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBP</i>.
     */
    static final Pair<String, Long> FIMADMINBP = Pair.of("fimIdentityAdministratorBP", 60L);
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBY</i>.
     */
    static final Pair<String, Long> FIMADMINBW = Pair.of("fimIdentityAdministratorBW", 64L);
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBY</i>.
     */
    static final Pair<String, Long> FIMADMINBY = Pair.of("fimIdentityAdministratorBY", 68L);
    /**
     ** The UID belonging to an unknown scope admin role <i>Unknown</i>.
     */
    static final Pair<String, Long> UNKNOWN    = Pair.of("Unknown", 999999L);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestFixture</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestFixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeClass
  /**
   ** Tests need to share computationally expensive setup.
   ** <br>
   ** While this can compromise the independence of tests, it is a necessary
   ** optimization.
   ** <p>
   ** Annotating this method with <code>&#064;BeforeClass</code> causes it to be
   ** run once before any of the test methods in the class.
   ** <p>
   ** The <code>&#064;BeforeClass</code> methods of superclasses will be run
   ** before those of the current class, unless they are shadowed in the current
   ** class.
   **
   ** @throws SystemException    if the network connection could not
   **                            established.
   */
  @BeforeClass
  public static void beforeClass()
    throws SystemException {

    try {
      CONTEXT = Context.build(ServerEndpoint.build(CONSOLE, config(ENDPOINT)));
      assertNotNull(CONTEXT);
      CONTEXT.login();
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterClass
  /**
   ** If expensive external resources allocate  in a {@link eforeClass} method
   ** it's required to release them after all the tests in the class have run.
   ** <p>
   ** Annotating a method with with <code>&#064;AfterClass</code> causes it to
   ** be run after all the tests in the class have been run.
   ** <br>
   ** All <code>&#064;AfterClass</code> methods are guaranteed to run even if a
   ** {@link BeforeClass} method throws an exception.
   */
  @AfterClass
  public static void afterClass() {
    if (CONTEXT != null)
      CONTEXT.shutdown();
  }
}