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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   N.SIS Universal Police Client SCIM

    File        :   TestAccount.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccount.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.nsis;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

public class TestAccount {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final File      PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/nsis-p-account-provisioning.xml");
  static final File      RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/nsis-p-account-reconciliation.xml");

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static Descriptor      provisioning;
  static Descriptor      reconciliation;
  static ConnectorFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // interface bkbk4711123
  // ~~~~~~~~~ ~~~~~~~~~~~
  /**
   ** The UPC User Account
   */
  static interface bkbk4711121 {
    static final String              NAME           = "bkbk4711121";
    static final Uid                 UID            = new Uid(NAME);
    static final Uid                 NEW            = new Uid(NAME + "yyy");
    static final Map<String, Object> CREATE         = new HashMap<String, Object>() {{
        put("UD_UPCPN_USR_UID",      UID.getUidValue());
        put("UD_UPCPN_USR_PWD",      "Welcome1");
        put("UD_UPCPN_USR_SN",       "Strecke");
        put("UD_UPCPN_USR_GN",       "Sophie");
        put("UD_UPCPN_USR_OID",      "GS99999999");
        put("UD_UPCPN_USR_EMAIL",    "sophie.strecke@bka.bund.de");
        put("UD_UPCPN_USR_PHONE",    "+49 177 5948 437");
        put("UD_UPCPN_USR_LANGUAGE", "de-DE");
      }};
    static final Map<String, Object> MODIFY         = new HashMap<String, Object>() {{
      put("UD_APIP_USR_SN",    "Strecke (Modified)");
    }};
    static final Map<String, Object> ORIGIN         = new HashMap<String, Object>() {{
      put("UD_UPCPN_USR_UID",   UID.getUidValue());
    }};
    static final Map<String, Object> RENAME         = new HashMap<String, Object>() {{
      put("UD_UPCPN_USR_UID",   NEW.getUidValue());
    }};
  }

  /** Build the attribute to enable an account. */
  static final Set<Attribute> ENABLE  = CollectionUtility.set(AttributeBuilder.buildEnabled(true));
  /** Build the attribute to diable an account. */
  static final Set<Attribute> DISABLE = CollectionUtility.set(AttributeBuilder.buildEnabled(false));

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Fixture</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAccount() {
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
   ** Annotating this method with <code>@BeforeClass</code> causes it to be run
   ** once before any of the test methods in the class.
   */
  @BeforeClass
  public static void beforeClass() {
    try {
      provisioning   = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      reconciliation = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), RECONCILIATION);
      facade         = Network.facade(Network.intranet());
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Tests need to share computationally expensive setup.
   ** <br>
   ** While this can compromise the independence of tests, it is a necessary
   ** optimization.
   ** <p>
   ** Annotating this method with <code>@Before</code> causes it to be run once
   ** before any of the test methods in the class.
   */
  @Before
  public void before() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link ConnectorException} thrown from the
   **                            <code>Connector Bundle</code>.
   */
  static void failed(final ConnectorException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    // this message needs to be split by a "::" character sequence
    final String   message = cause.getLocalizedMessage();
    final String[] parts   = message.split("::");
    if (parts.length > 1)
      failed(parts[0].concat("::").concat(parts[1]));
    else
      failed(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link TaskException} thrown from the
   **                            <code>Integration Layer</code>.
   */
  static void failed(final TaskException e) {
    failed(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given message.
   **
   ** @param  message            the identifying message for the
   **                            {@link AssertionError}.
   **                            <br>
   **                            May be <code>nulld</code>.
   */
  static void failed(final String message) {
    Assert.fail(message);
  }
}