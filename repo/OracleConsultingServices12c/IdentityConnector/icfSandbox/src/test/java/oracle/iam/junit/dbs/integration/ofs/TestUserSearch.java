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
    Subsystem   :   Openfire Database Connector

    File        :   TestUserSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestUserSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import java.util.Set;

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

import oracle.iam.identity.connector.integration.FilterFactory;
import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

////////////////////////////////////////////////////////////////////////////////
// class TestUserSearch
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The test case to fetch a paginated result set from the target system
 ** leveraging the <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestUserSearch extends TestUser {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Descriptor descriptor;

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
      // verify
      assertEquals(object.getObjectClass(), ObjectClass.ACCOUNT);
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
   ** Constructs a <code>TestUserSearch</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestUserSearch() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
  @Test
  public void filterSyntax() {
    final Filter filter = FilterFactory.build("not equalTo('__NAME__', 'admin')");
    Network.CONSOLE.info(filter.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fullReconciliation
  /**
   ** Test that accounts could be reconciled where the timestamp is in the past
   ** so that all entries schuld should fetched.
   */
	@Test
	public void fullReconciliation() {
    try {
      final Descriptor              descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
      final Set<String>             returning  = CollectionUtility.union(descriptor.source(), descriptor.referenceSource());
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(returning);
      Network.CONSOLE.info("TestUnit fullReconciliation");
      Network.facade(Network.intranet()).search(ObjectClass.ACCOUNT, null, new Handler(descriptor), option.build());
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
      final Descriptor              descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
      final Set<String>             returning  = CollectionUtility.union(descriptor.source(), descriptor.referenceSource());
      final OperationOptionsBuilder option     = searchControl("20220101000000.000Z").setAttributesToGet(returning);
      Network.CONSOLE.info("TestUnit futureReconciliation");
      Network.facade(Network.intranet()).search(ObjectClass.ACCOUNT, null, new Handler(descriptor), option.build());
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
    final Filter filter = FilterBuilder.equalTo(AttributeBuilder.build(Uid.NAME, "ofssuper"));
    try {
      final Descriptor              descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
      final Set<String>             returning  = CollectionUtility.union(descriptor.source(), descriptor.referenceSource());
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(returning);
      Network.CONSOLE.info("TestUnit filteredReconciliationUID :: " + filter.toString());
      Network.facade(Network.intranet()).search(ObjectClass.ACCOUNT, filter, new Handler(descriptor), option.build());
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
    final Filter filter = FilterBuilder.startsWith(AttributeBuilder.build(Name.NAME, "a"));
    try {
      final Descriptor              descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
      final Set<String>             returning  = CollectionUtility.union(descriptor.source(), descriptor.referenceSource());
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(returning);
      Network.CONSOLE.info("TestUnit filteredReconciliationNAME :: " + filter.toString());
      Network.facade(Network.intranet()).search(ObjectClass.ACCOUNT, filter, new Handler(descriptor), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filteredReconciliationName
  /**
   ** Test that accounts could be reconciled that mach the specified filter.
   */
  @Test
  public void filteredReconciliationName() {
    final Filter filter = FilterBuilder.startsWith(AttributeBuilder.build("name", "A"));
    try {
      final Descriptor              descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
      final Set<String>             returning  = CollectionUtility.union(descriptor.source(), descriptor.referenceSource());
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(returning);
      Network.CONSOLE.info("TestUnit filteredReconciliationName :: " + filter.toString());
      Network.facade(Network.intranet()).search(ObjectClass.ACCOUNT, filter, new Handler(descriptor), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filteredReconciliationEmail
  /**
   ** Test that accounts could be reconciled that mach the specified filter.
   */
  @Test
  public void filteredReconciliationEmail() {
    final Filter filter = FilterBuilder.equalTo(AttributeBuilder.build("email", "max.gaertner@vm.oracle.com"));
    try {
      final Descriptor              descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
      final Set<String>             returning  = CollectionUtility.union(descriptor.source(), descriptor.referenceSource());
      final OperationOptionsBuilder option     = searchControl("19700101010000.000Z").setAttributesToGet(returning);
      Network.CONSOLE.info("TestUnit filteredReconciliationEmail :: " + filter.toString());
      Network.facade(Network.intranet()).search(ObjectClass.ACCOUNT, filter, new Handler(descriptor), option.build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}