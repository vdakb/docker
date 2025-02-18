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

    System      :   Oracle Identity Manager UnitTest
    Subsystem   :   Openfire Database Connector

    File        :   ContextGroupSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ContextGroupSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.connector.ofs;

import java.util.Map;
import java.util.List;
import java.util.Date;

import org.junit.Test;
import org.junit.Assert;

import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.utility.DateUtility;

import oracle.iam.identity.icf.dbms.DatabaseFilter;

import oracle.iam.identity.icf.connector.openfire.schema.Group;

////////////////////////////////////////////////////////////////////////////////
// class ContextGroupSearch
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case belonging to group searches leveraging the the
 ** <code>Database Server</code> JDBC driver.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ContextGroupSearch extends Transaction {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DatabaseFilter criteria = DatabaseFilter.build(Group.GID, "admin", DatabaseFilter.Operator.EQUAL);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ContextGroupSearch</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ContextGroupSearch() {
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
    final OperationContext control = searchControl("19700101010000.000Z");
    try {
      final List<Group> result = this.context.groupSearch(searchFilter(control, null), control.start, control.limit);
      notNull(result);
      Assert.assertTrue("At least one entry was expected", result.size() > 0);
    }
    catch (SystemException e) {
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
    final OperationContext control = searchControl("20220101000000.000Z");
    try {
      final List<Group> result = this.context.groupSearch(searchFilter(control, null), control.start, control.limit);
      notNull(result);
      Assert.assertTrue("No entry was expected", result.size() == 0);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filteredReconciliation
  /**
   ** Test that accounts could be reconciled that mach the specified filter.
   */
  @Test
  public void filteredReconciliation() {
    // timestamp is eqivalent to start of epoche
    final OperationContext control = searchControl("19700101010000.000Z");
    try {
      final List<Group> result = this.context.groupSearch(searchFilter(control, DatabaseFilter.build("groupName", "a", DatabaseFilter.Operator.STARTS_WITH)), control.start, control.limit);
      notNull(result);
      Assert.assertTrue("At least one entry was expected", result.size() > 0);
      Assert.assertTrue("Exactly one entry was expected", result.size() == 1);
      equals("Group admin was expected", "admin", result.get(0).gid());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchFilter
  /**
   ** Factory method to build the search criteria to apply on a search.
   **
   ** @param  control            the search control.
   **                            <br>
   **                            Allowed object is {@link OperationContext}.
   ** @param  criteria           the group defined search criteria.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    the {@link DatabaseFilter} accumulated.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   **
   ** @throws SystemException    if string parameter does not conform to lexical
   **                            value space defined in
   **                            {@link DateUtility#RFC4517_ZULU_NANO}.
   */
  private DatabaseFilter searchFilter(final OperationContext control, final DatabaseFilter criteria)
    throws SystemException {

    // setup the filter criteria to apply on the search
    DatabaseFilter filter = criteria;
    if (control.incremental && control.synchronizationToken != null) {
      final Date timestamp = DateUtility.parse(control.synchronizationToken, DateUtility.RFC4517_ZULU_NANO);
       filter = (filter == null)
               ? this.context.searchTime(timestamp.getTime())
               : DatabaseFilter.build(
                   this.context.searchTime(timestamp.getTime())
                 , filter
                , DatabaseFilter.Operator.AND
                );
    }
    return filter;
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
  private OperationContext searchControl(final String token) {
    final OperationOptionsBuilder factory = new OperationOptionsBuilder();
    final Map<String, Object>     option  = factory.getOptions();
    option.put(OperationContext.BATCH_SIZE,        500);
    option.put(OperationContext.BATCH_START,       1);
    option.put(OperationContext.INCREMENTAL,       Boolean.TRUE);
    option.put(OperationContext.SYNCHRONIZE_TOKEN, token);

    return OperationContext.build(factory.build());
  }
}