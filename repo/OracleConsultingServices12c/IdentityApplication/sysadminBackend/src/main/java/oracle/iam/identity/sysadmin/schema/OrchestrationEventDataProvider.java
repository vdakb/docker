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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Administration Management

    File        :   OrchestrationEventDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrchestrationEventDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import javax.management.openmbean.CompositeData;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.model.common.PagedArrayList;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

////////////////////////////////////////////////////////////////////////////////
// class OrchestrationEventDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Administration Management customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class OrchestrationEventDataProvider extends JMXDataProvider<OrchestrationEventAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** ...
   */
  public static final String    ORCHESTRATION    = "oracle.iam:name=OrchestrationEngine,type=Kernel,Application=oim";

  /**
   ** the name of the operation this task invokes on the server side to find
   ** process events
   */
  private static final String   OPERATION        = "findEventsForProcess";

  /**
   ** the signature array this task sends on the server side to find process
   ** events
   */
  private static final String[] SIGNATURE        = {
    Long.class.getName()   // 0: processId
  , String.class.getName() // 1: processName
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5607491227875750310")
  private static final long     serialVersionUID = -2555820732723519250L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ObjectName           operationService  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrchestrationEventDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrchestrationEventDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   search (DataProvider)
  /**
   ** Return a list of backend objects based on the given filter criteria.
   **
   ** @param  searchCriteria     the OIM search criteria to submit.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  requested          the {@link Set} of attributes to be returned in
   **                            search results.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            <br>
   **                            Allowed object is {@link HashMap} where each
   **                            element is of type [@link String} for the key
   **                            and {@link Object} for the value.
   **
   ** @return                    a list of backend objects.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type
   **                            {@link OrchestrationEventAdapter}.
   */
  @Override
  public List<OrchestrationEventAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    // default search is based on value
    SearchCriteria criteria  = convertCriteria(searchCriteria);
    final Long     processId = Long.valueOf((String)criteria.getSecondArgument());
    // the parameter array this provider sends on the server side to find the
    // events belonging to a certain orchstration process
    final Object[] parameter = new Object[] {processId /* 0: processId */, null /* 1: processName */};
    final List<OrchestrationEventAdapter> batch  = new ArrayList<OrchestrationEventAdapter>();
    try {
      final CompositeData[] result = (CompositeData[])invoke(operationService(), OPERATION, parameter, SIGNATURE);
      for (CompositeData data : result)
        batch.add(OrchestrationEventAdapter.build(processId, data));
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
    return new PagedArrayList<OrchestrationEventAdapter>(batch, 0, batch.size());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertCriteria
  /**
   ** Converts the attribute names contained in the passed
   ** {@link SearchCriteria} and obtained from the ADF Entity/View Definitions
   ** to the names declared in Identity Manager for entity Orchestration Event.
   **
   ** @param criteria            the search criteria mapped to the ADF
   **                            Entity/View Definitions.
   **
   ** @return                    the search criteria mapped to the OIM
   **                            declarations.
   */
  private SearchCriteria convertCriteria(final SearchCriteria criteria) {
    if (criteria == null)
      throw new OIMRuntimeException("Oooops");

    Object lhs = criteria.getFirstArgument();
    if (lhs instanceof SearchCriteria) {
      lhs = convertCriteria((SearchCriteria)lhs);
    }
    else if (lhs instanceof String) {
      lhs = convertAttributeName((String)lhs);
    }

    Object rhs = criteria.getSecondArgument();
    if (rhs instanceof SearchCriteria) {
      rhs = convertCriteria((SearchCriteria)rhs);
    }

    if (((String)lhs).equalsIgnoreCase(OrchestrationEventAdapter.FK)) {
      lhs = OrchestrationEventAdapter.PROCESSID;
    }
    return new SearchCriteria(lhs, rhs, criteria.getOperator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   convertAttributeName
  /**
   ** Converts the attribute name defined in the ADF view query criteria to the
   ** natural names exposed by the OIM kernel API.
   **
   ** @param  outboundName       the attribute name defined in the ADF view
   **                            query criteria.
   **
   ** @return                    the attribute name exposed by the OIM kernel
   **                            API if any.
   */
  private String convertAttributeName(final String outboundName) {
    // prevent bogus input
    if (outboundName == null)
      return null;

    String inboundName = outboundName.trim();
    inboundName = outboundName;

    return inboundName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationService
  /**
   ** Returns the {@link ObjectName}.
   **
   ** @return                    the {@link ObjectName}
   **
   ** @throws MalformedObjectNameException the string used for the object name
   **                                      to build does not have the right
   **                                      format.
   */
  private final ObjectName operationService()
    throws MalformedObjectNameException {

    if (this.operationService == null) {
      synchronized(RUNTIME_SERVER) {
        this.operationService = objectName(ORCHESTRATION);
      }
    }
    return this.operationService;
  }
}