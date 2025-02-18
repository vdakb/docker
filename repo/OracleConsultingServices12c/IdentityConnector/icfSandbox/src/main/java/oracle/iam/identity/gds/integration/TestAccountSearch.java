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
    Subsystem   :   Generic Directory Connector

    File        :   TestAccountSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gds.integration;

import java.util.Map;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.EntityReconciliation;

import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountSearch
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case to search for account entries in the target system leveraging
 ** the <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestAccountSearch extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Descriptor descriptor;

    private Handler(final Descriptor descriptor) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.descriptor = descriptor;
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
      final String method  = "handle";
      this.descriptor.trace(method, SystemMessage.METHOD_ENTRY);
      EntityReconciliation.Event event = EntityReconciliation.buildEvent(-1L, object, this.descriptor, false);
      System.out.println(object.getUid() + "::" + object.getName());
      this.descriptor.trace(method, SystemMessage.METHOD_EXIT);
      return true;
    }
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
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
      final Filter     filter     = FilterBuilder.equalTo(AttributeBuilder.build("uid", "dsteding"));
      PEOPLEOPTS.getOptions().put(BATCH_SIZE, new Integer(500));
      Network.facade(Network.internet()).search(ObjectClass.ACCOUNT, filter, new Handler(descriptor), PEOPLEOPTS.build());
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      final String   message = e.getLocalizedMessage();
      final String[] parts   = message.split("::");
      if (parts.length > 1)
        System.out.println(parts[0].concat("::").concat(parts[1]));
      else
        System.out.println(message);
    }
    catch (TaskException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchControl
  /**
   ** Factory method to create a new &amp; initialized control configurator
   ** instance.
   **
   ** @param  token              a string containing lexical representation of
   **                            a date.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new &amp; initialized control configurator
   **                            instance.
   **                            <br>
   **                            Possible object is
   **                            <code>OperationContext</code>.
   */
  private static OperationOptionsBuilder searchControl(final String token) {
    final OperationOptionsBuilder factory = new OperationOptionsBuilder();
    final Map<String, Object>     option  = factory.getOptions();
    option.put(BATCH_SIZE,        500);
    option.put(BATCH_START,       1);
    option.put(INCREMENTAL,       Boolean.TRUE);
    option.put(SYNCHRONIZE_TOKEN, token);
    return factory;
  }
}