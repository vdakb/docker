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

    File        :   TestConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestConfiguration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector;

import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.jes.ServerContext;
import oracle.iam.identity.icf.jes.ServerEndpoint;

////////////////////////////////////////////////////////////////////////////////
// class TestConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case fetch the configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestConfiguration extends TestBaseConnector {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestConfiguration</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestConfiguration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {TestConfiguration.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Test configuration properties.
   */
  @Test
  public void execute() {
    try {
      final Map<String, String> config = config(ENDPOINT);
      assertNotNull(config);
      assertTrue(ServerEndpoint.SERVER_HOST,        config.containsKey(ServerEndpoint.SERVER_HOST));
      assertTrue(ServerEndpoint.SERVER_PORT,        config.containsKey(ServerEndpoint.SERVER_PORT));
      assertTrue(ServerEndpoint.SERVER_TYPE,        config.containsKey(ServerEndpoint.SERVER_TYPE));
      assertTrue(ServerEndpoint.SERVER_NAME,        config.containsKey(ServerEndpoint.SERVER_NAME));
      assertTrue(ServerEndpoint.ROOT_CONTEXT,       config.containsKey(ServerEndpoint.ROOT_CONTEXT));
      assertTrue(ServerEndpoint.PRINCIPAL_NAME,     config.containsKey(ServerEndpoint.PRINCIPAL_NAME));
      assertTrue(ServerEndpoint.PRINCIPAL_PASSWORD, config.containsKey(ServerEndpoint.PRINCIPAL_PASSWORD));
      assertTrue(ServerEndpoint.DOMAIN_PRINCIPAL,   config.containsKey(ServerEndpoint.DOMAIN_PRINCIPAL));
      assertTrue(ServerEndpoint.DOMAIN_PASSWORD,    config.containsKey(ServerEndpoint.DOMAIN_PASSWORD));
      assertTrue(ServerEndpoint.LOGIN_CONFIG,       config.containsKey(ServerEndpoint.LOGIN_CONFIG));
      assertTrue(ServerEndpoint.LOCALE_LANGUAGE,    config.containsKey(ServerEndpoint.LOCALE_LANGUAGE));
      assertTrue(ServerEndpoint.LOCALE_COUNTRY,     config.containsKey(ServerEndpoint.LOCALE_COUNTRY));
      assertTrue(ServerEndpoint.LOCALE_TIMEZONE,    config.containsKey(ServerEndpoint.LOCALE_TIMEZONE));

      final ServerEndpoint endpoint = ServerEndpoint.build(CONSOLE, config);      
      assertNotNull(endpoint);
      assertEquals(endpoint.primaryHost(),        config.get(ServerEndpoint.SERVER_HOST));
      assertEquals(endpoint.primaryPort(),        Integer.valueOf(config.get(ServerEndpoint.SERVER_PORT)));
      assertEquals(endpoint.serverName(),         config.get(ServerEndpoint.SERVER_NAME));
      assertEquals(endpoint.serverType().value,   config.get(ServerEndpoint.SERVER_TYPE));
      assertEquals(endpoint.domainPrincipal(),    config.get(ServerEndpoint.DOMAIN_PRINCIPAL));
      assertEquals(endpoint.principalUsername(),  config.get(ServerEndpoint.PRINCIPAL_NAME));
      assertEquals(endpoint.loginConfig(),        config.get(ServerEndpoint.LOGIN_CONFIG));
      assertEquals(endpoint.localeLanguage(),     config.get(ServerEndpoint.LOCALE_LANGUAGE));
      assertEquals(endpoint.localeCountry(),      config.get(ServerEndpoint.LOCALE_COUNTRY));
      assertEquals(endpoint.localeTimeZone(),     config.get(ServerEndpoint.LOCALE_TIMEZONE));

      final ServerContext context = ServerContext.build(endpoint);
      assertNotNull(context);
      assertEquals(context.endpoint(), endpoint);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}