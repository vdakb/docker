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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestTenantPermitted.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestTenantPermitted.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.pcf;

import java.util.Set;
import java.util.List;

import java.util.Map;

import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.connector.pcf.RestContext;
import oracle.iam.identity.icf.connector.pcf.RestMarshaller;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;
import oracle.iam.identity.icf.connector.pcf.rest.domain.Payload;

////////////////////////////////////////////////////////////////////////////////
// class TestTenantPermitted
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to obtain permitted tenants resource in the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestTenantPermitted {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String guid = "97cafa77-6969-44fb-b8f1-cb52b336350b";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    try {
      final RestContext                             context   = Network.cloudContext(Network.intranet());
      Map<String, List<Pair<String, List<String>>>> permitted = context.lookupPermitted(guid);
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}