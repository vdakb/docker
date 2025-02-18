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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Identity Governance Service SCIM

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.tenant;

import java.util.Set;
import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

import oracle.iam.junit.igs.integration.TestReconciliation;

import oracle.iam.identity.icf.connector.scim.igs.ExtensionClass;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** The test case for search operation on tenants at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Search extends TestReconciliation {

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
        // verify
        assertEquals(object.getObjectClass(), ExtensionClass.TENANT);
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
   ** Constructs a <code>Search</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Search() {
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
    final String[] parameter = {Search.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fullReconciliation
  /**
   ** Test that tenants could be reconciled where the timestamp is in the past
   ** so that all entries should fetched.
   */
  @Test
  public void fullReconciliation() {
    try {
      final Descriptor              mapping   = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), Base.RECONCILIATION);
      final Set<String>             returning = CollectionUtility.union(mapping.source(), mapping.referenceSource());
      final OperationOptionsBuilder option    = searchControl("19700101010000.000Z").setAttributesToGet(returning);

      facade(service()).search(ExtensionClass.TENANT, null, new Handler(mapping), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
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
  protected OperationOptionsBuilder searchControl(final String token) {
    final OperationOptionsBuilder factory = new OperationOptionsBuilder();
    final Map<String, Object>     option  = factory.getOptions();
    option.put(BATCH_SIZE,        BATCH_SIZE_DEFAULT);
    option.put(BATCH_START,       BATCH_START_DEFAULT);
    option.put(INCREMENTAL,       Boolean.TRUE);
    option.put(SYNCHRONIZE_TOKEN, token);
    return factory;
  }
}