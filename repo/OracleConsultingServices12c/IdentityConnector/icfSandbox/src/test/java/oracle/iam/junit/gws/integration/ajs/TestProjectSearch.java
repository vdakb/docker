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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Server Connector

    File        :   TestProjectSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestProjectSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.ajs;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.jira.Marshaller;

import oracle.iam.junit.TestCaseIntegration;

////////////////////////////////////////////////////////////////////////////////
// class TestProjectSearch
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case to fetch a paginated result set from the target system
 ** leveraging the <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestProjectSearch extends TestCaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Handler</code>.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Handler() {
      // ensure inheritance
      super();
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
      // verify
      assertEquals(object.getObjectClass(), Marshaller.PROJECT);
      Network.CONSOLE.debug(object.toString());
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestProjectSearch</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestProjectSearch() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fullReconciliation
  /**
   ** Test that accounts could be reconciled where the timestamp is in the past
   ** so that all entries schuld should fetched.
   */
	@Test
	public void fullReconciliation() {
    try {
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(CollectionUtility.set(Uid.NAME, "description"));
      Network.CONSOLE.info("TestProjectSearch::fullReconciliation");
      Network.facade(Network.intranet()).search(Marshaller.PROJECT, null, new Handler(), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   futureReconciliation
  /**
   ** Test that accounts could be reconciled where the timestamp is in the
   ** future so that no entries should been fetched.
   */
  @Test
  public void futureReconciliation() {
    try {
      final OperationOptionsBuilder option = searchControl("20220101000000.000Z").setAttributesToGet(CollectionUtility.set(Uid.NAME, "description"));
      Network.CONSOLE.info("TestProjectSearch::futureReconciliation");
      Network.facade(Network.intranet()).search(Marshaller.PROJECT, null, new Handler(), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filteredReconciliationUID
  /**
   ** Test that accounts could be reconciled that mach the specified filter.
   */
  @Test
  public void filteredReconciliationUID() {
    final Filter filter = FilterBuilder.equalTo(AttributeBuilder.build(Uid.NAME, "orclProjectReadPrivilegeProject"));
    try {
      final OperationOptionsBuilder option = searchControl("19700101010000.000Z").setAttributesToGet(CollectionUtility.set(Uid.NAME, "description"));
      Network.CONSOLE.info("TestProjectSearch::filteredReconciliationUID :: " + filter.toString());
      Network.facade(Network.intranet()).search(Marshaller.PROJECT, filter, new Handler(), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filteredReconciliationNAME
  /**
   ** Test that accounts could be reconciled that mach the specified filter.
   */
  @Test
  public void filteredReconciliationNAME() {
    final Filter filter = FilterBuilder.startsWith(AttributeBuilder.build(Name.NAME, "orcl"));
    try {
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(CollectionUtility.set(Uid.NAME, "description"));
      Network.CONSOLE.info("TestProjectSearch::filteredReconciliationNAME :: " + filter.toString());
      Network.facade(Network.intranet()).search(Marshaller.PROJECT, filter, new Handler(), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filteredReconciliationDescription
  /**
   ** Test that accounts could be reconciled that mach the specified filter.
   */
  @Test
  public void filteredReconciliationName() {
    final Filter filter = FilterBuilder.startsWith(AttributeBuilder.build("description", "A"));
    try {
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(CollectionUtility.set(Uid.NAME, "description"));
      Network.CONSOLE.info("TestProjectSearch::filteredReconciliationName :: " + filter.toString());
      Network.facade(Network.intranet()).search(Marshaller.PROJECT, filter, new Handler(), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}