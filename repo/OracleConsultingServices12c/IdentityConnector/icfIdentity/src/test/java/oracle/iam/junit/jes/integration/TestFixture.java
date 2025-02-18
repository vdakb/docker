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

package oracle.iam.junit.jes.integration;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Uid;

import org.identityconnectors.framework.api.ConnectorFacade;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.object.Pair;

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
public class TestFixture extends TestBaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static ConnectorFacade FACADE;

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
    static final Pair<String, Uid> LEGACY        = Pair.of("Xellerate Users", new Uid("1"));
    /**
     ** The UID belonging to a organization <i>Top</i>.
     */
    static final Pair<String, Uid> TOP           = Pair.of(OrganizationManagerConstants.TOP_ORGANIZATION_NAME, new Uid("3"));
    /**
     ** The UID belonging to a organization <i>AN</i>.
     */
    static final Pair<String, Uid> AN            = Pair.of("AN", new Uid("21"));
    /**
     ** The UID belonging to a organization <i>BB</i>.
     */
    static final Pair<String, Uid> BB            = Pair.of("BB", new Uid("22"));
    /**
     ** The UID belonging to a organization <i>BE</i>.
     */
    static final Pair<String, Uid> BE            = Pair.of("BE", new Uid("23"));
    /**
     ** The UID belonging to a organization <i>BK</i>.
     */
    static final Pair<String, Uid> BK            = Pair.of("BK", new Uid("24"));
    /**
     ** The UID belonging to a organization <i>BP</i>.
     */
    static final Pair<String, Uid> BP            = Pair.of("BP", new Uid("25"));
    /**
     ** The UID belonging to a organization <i>BW</i>.
     */
    static final Pair<String, Uid> BW            = Pair.of("BW", new Uid("26"));
    /**
     ** The UID belonging to a organization <i>BY</i>.
     */
    static final Pair<String, Uid> BY            = Pair.of("BY", new Uid("27"));
    /**
     ** The UID belonging to an unknown organization <i>Unknown</i>.
     */
    static final Pair<String, Uid> UNKNOWN        = Pair.of("Unknown", new Uid("999999"));

    static final File              PROVISIONING   = new File("src/test/resources/mds/oig-tenant-provisioning.xml");
    static final File              RECONCILIATION = new File("src/test/resources/mds/oig-tenant-reconciliation.xml");
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
    static final Pair<String, Uid> OLDADMIN       = Pair.of(RoleManagerConstants.SYS_ADMIN_ROLE_NAME, new Uid("1"));
    /**
     ** The UID belonging to a system role <i>OPERATORS</i>.
     */
    static final Pair<String, Uid> OPERATORS      = Pair.of("OPERATORS", new Uid("2"));
    /**
     ** The UID belonging to a system role <i>ALL USERS</i>.
     */
    static final Pair<String, Uid> ALLUSERS       = Pair.of("ALL USERS", new Uid("3"));
    /**
     ** The UID belonging to a system role <i>SELF OPERATORS</i>.
     */
    static final Pair<String, Uid> SELFOP         = Pair.of("SELF OPERATORS", new Uid("4"));
    /**
     ** The UID belonging to a system role <i>Administrators</i>.
     */
    static final Pair<String, Uid> NEWADMIN       = Pair.of("Administrators", new Uid("5"));
    /**
     ** The UID belonging to a system role <i>BIReportAdministrator</i>.
     */
    static final Pair<String, Uid> BIRADMIN       = Pair.of("BIReportAdministrator", new Uid("6"));
    /**
     ** The UID belonging to a system role <i>MNR_Test-Role-1</i>.
     */
    static final Pair<String, Uid> MNRTEST1       = Pair.of("MNR_Test-Role-1", new Uid("22"));
    /**
     ** The UID belonging to a system role <i>MNR_Test-Role-2</i>.
     */
    static final Pair<String, Uid> MNRTEST2       = Pair.of("MNR_Test-Role-2", new Uid("21"));
    /**
     ** The UID belonging to an unknown system role <i>Unknown</i>.
     */
    static final Pair<String, Uid> UNKNOWN        = Pair.of("Unknown", new Uid("999999"));

    static final File              PROVISIONING   = new File("src/test/resources/mds/oig-systemrole-provisioning.xml");
    static final File              RECONCILIATION = new File("src/test/resources/mds/oig-systemrole-reconciliation.xml");
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
    static final Pair<String, Uid> SYSADMIN       = Pair.of("OrclOIMSystemAdministrator", new Uid("1"));
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMSystemConfigurator</i>.
     */
    static final Pair<String, Uid> SYSCONFIG      = Pair.of("OrclOIMSystemConfigurator", new Uid("2"));
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMCatalogAdmin</i>.
     */
    static final Pair<String, Uid> CATADMIN       = Pair.of("OrclOIMCatalogAdmin", new Uid("3"));
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMCertificationAdministrator</i>.
     */
    static final Pair<String, Uid> CERTADMIN      = Pair.of("OrclOIMCertificationAdministrator", new Uid("22"));
    /**
     ** The UID belonging to an unknown global admin role
     ** <i>OrclOIMCertificationViewer</i>.
     */
    static final Pair<String, Uid> CERTVIEWER     = Pair.of("OrclOIMCertificationViewer", new Uid("23"));
    /**
     ** The UID belonging to an unknown scope admin role <i>Unknown</i>.
     */
    static final Pair<String, Uid> UNKNOWN        = Pair.of("Unknown", new Uid("999999"));

    static final File              PROVISIONING   = new File("src/test/resources/mds/oig-globalrole-provisioning.xml");
    static final File              RECONCILIATION = new File("src/test/resources/mds/oig-globalrole-reconciliation.xml");
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
    static final Pair<String, Uid> ENTADMIN       = Pair.of("OrclOIMEntitlementAdministrator", new Uid("7"));
    /**
     ** The UID belonging to a scoped admin role
     ** <i>OrclOIMEntitlementAuthorizer</i>.
     */
    static final Pair<String, Uid> ENTAUTHZ       = Pair.of("OrclOIMEntitlementAuthorizer", new Uid("8"));
    /**
     ** The UID belonging to a scoped admin role
     ** <i>OrclOIMEntitlementViewer</i>.
     */
    static final Pair<String, Uid> ENTVIEWER      = Pair.of("OrclOIMEntitlementViewer", new Uid("9"));
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMOrgAdministrator</i>.
     */
    static final Pair<String, Uid> ORGADMIN       = Pair.of("OrclOIMOrgAdministrator", new Uid("13"));
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMOrgViewer</i>.
     */
    static final Pair<String, Uid> ORGVIEWER      = Pair.of("OrclOIMOrgViewer", new Uid("14"));
    /**
     ** The UID belonging to a scoped admin role
     ** <i>OrclOIMRoleAdministrator</i>.
     */
    static final Pair<String, Uid> ROLADMIN       = Pair.of("OrclOIMRoleAdministrator", new Uid("4"));
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMRoleAuthorizer</i>.
     */
    static final Pair<String, Uid> ROLAUTHZ       = Pair.of("OrclOIMRoleAuthorizer", new Uid("5"));
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMRoleViewer</i>.
     */
    static final Pair<String, Uid> ROLVIEWER      = Pair.of("OrclOIMRoleViewer", new Uid("6"));
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMUserAdmin</i>.
     */
    static final Pair<String, Uid> USRADMIN       = Pair.of("OrclOIMUserAdmin", new Uid("15"));
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMUserHelpDesk</i>.
     */
    static final Pair<String, Uid> USRHDESK       = Pair.of("OrclOIMUserHelpDesk", new Uid("16"));
    /**
     ** The UID belonging to a scoped admin role <i>OrclOIMUserViewer</i>.
     */
    static final Pair<String, Uid> USRVIEWER      = Pair.of("OrclOIMUserViewer", new Uid("17"));
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorAN</i>.
     */
    static final Pair<String, Uid> FIMADMINAN     = Pair.of("fimIdentityAdministratorAN", new Uid("44"));
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBB</i>.
     */
    static final Pair<String, Uid> FIMADMINBB     = Pair.of("fimIdentityAdministratorBB", new Uid("48"));
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBE</i>.
     */
    static final Pair<String, Uid> FIMADMINBE     = Pair.of("fimIdentityAdministratorBE", new Uid("52"));
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBK</i>.
     */
    static final Pair<String, Uid> FIMADMINBK     = Pair.of("fimIdentityAdministratorBK", new Uid("56"));
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBP</i>.
     */
    static final Pair<String, Uid> FIMADMINBP     = Pair.of("fimIdentityAdministratorBP", new Uid("60"));
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBW</i>.
     */
    static final Pair<String, Uid> FIMADMINBW     = Pair.of("fimIdentityAdministratorBW", new Uid("64"));
    /**
     ** The UID belonging to a scoped admin role <i>fimIdentityAdministratorBY</i>.
     */
    static final Pair<String, Uid> FIMADMINBY     = Pair.of("fimIdentityAdministratorBY", new Uid("68"));
    /**
     ** The UID belonging to an unknown scope admin role <i>Unknown</i>.
     */
    static final Pair<String, Uid> UNKNOWN        = Pair.of("Unknown", new Uid("999999"));

    static final File              PROVISIONING   = new File("src/test/resources/mds/oig-scopedrole-provisioning.xml");
    static final File              RECONCILIATION = new File("src/test/resources/mds/oig-scopedrole-reconciliation.xml");
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Account
  // ~~~~~~~~~ ~~~~~~~
  public interface Account {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The UID belonging to the system administrator <i>xelsysadm</i>.
     */
    static final Pair<String, Uid> XELSYSADM      = Pair.of("xelsysadm", new Uid("1"));
    /**
     ** The UID belonging to the internel administrator <i>XELOPERATOR</i>.
     */
    static final Pair<String, Uid> XELOPERATOR    = Pair.of("XELOPERATOR", new Uid("2"));
    /**
     ** The UID belonging to the server administrator <i>weblogic</i>.
     */
    static final Pair<String, Uid> WEBLOGIC       = Pair.of("weblogic", new Uid("3"));
    /**
     ** The UID belonging to the internel administrator <i>oiminternal</i>.
     */
    static final Pair<String, Uid> OIMINTERNAL    = Pair.of("oiminternal", new Uid("4"));
    /**
     ** The UID belonging to Alfons Zitterbacke <i>an4711123</i>.
     */
    static final Pair<String, Uid> ZITTERBACKE    = Pair.of("an4711123", new Uid("2001"));
    /**
     ** The UID belonging to Gerald Cambrault <i>an4711124</i>.
     */
    static final Pair<String, Uid> CAMBRAULT      = Pair.of("an4711124", new Uid("3001"));
    /**
     ** The UID belonging to Agathe Musterfrau <i>bp4711124</i>.
     */
    static final Pair<String, Uid> MUSTERFRAU     = Pair.of("bp4711123", new Uid("1046"));
    /**
     ** The UID belonging to Agathe Musterfrau <i>bp4711124</i>.
     */
    static final Pair<String, Uid> MUSTERMANN     = Pair.of("bkbk4711123", new Uid("1042"));
    /**
     ** The UID belonging to an unknown user <i>Unknown</i>.
     */
    static final Pair<String, Uid> UNKNOWN        = Pair.of("Unknown", new Uid("999999"));

    static final File              PROVISIONING   = new File("src/test/resources/mds/oig-account-provisioning.xml");
    static final File              RECONCILIATION = new File("src/test/resources/mds/oig-account-reconciliation.xml");
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
   ** @throws AssertionException if the network connection could not
   **                            established.
   */
  @BeforeClass
  public static void beforeClass() {
    try {
      FACADE = facade(endpoint());
      assertNotNull(FACADE);
    }
    catch (TaskException e) {
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
  public static void logout() {
  }
}