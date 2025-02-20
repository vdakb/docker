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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Self Service Registration

    File        :   TrackRequestDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TrackRequestDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.registration.schema;

import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.OIMClientFactory;

import oracle.iam.ui.platform.utils.UIResourceBundleUtil;

import oracle.iam.request.vo.RequestStatusSummary;

import oracle.iam.request.api.UnauthenticatedRequestService;

////////////////////////////////////////////////////////////////////////////////
// class TrackRequestDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by Self Service Registration customization.
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
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TrackRequestDataProvider extends AbstractDataProvider<TrackRequestAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1580299410478606702")
  private static final long serialVersionUID = 8781918761628819567L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TrackRequestDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TrackRequestDataProvider() {
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
   **                            element is of type {@link TrackRequestAdapter}.
   */
  @Override
  public List<TrackRequestAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final UnauthenticatedRequestService service = OIMClientFactory.getUnauthenticatedRequestService();
    final String                        reqID   = getCriteriaValue(searchCriteria, "reqId");
    final TrackRequestAdapter           mab     = new TrackRequestAdapter();
    final List<TrackRequestAdapter>     batch   = new ArrayList<TrackRequestAdapter>();
    try {
      RequestStatusSummary[] rss = service.getRequestStatusSummary(reqID);
      RequestStatusSummary   rss1 = rss[0];
      mab.setRequestId(reqID);
      mab.setModified(rss1.getModifiedOn());
      mab.setSubmitted(rss1.getSubmittedOn());
      String status    = rss1.getStatus();
      String strStatus = status.toUpperCase().replaceAll(" ", "_");
      String stage     = rss1.getStage();
      if (stage.equals(status)) {
        strStatus = strStatus.concat("_S");
        //   String displayStage = requestStatusBundle.getProperty(strStatus);
        String displayStage = UIResourceBundleUtil.getLocalizedValue("oracle.iam.ui.common.model.RequestStatusBundle", strStatus);
        if ((displayStage == null) && (displayStage.trim().length() == 0)) {
          displayStage = stage;
        }
        mab.setRequestStage(displayStage);
      }
      else {
        mab.setRequestStage(stage);
      }
      batch.add(mab);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
    return batch;
  }
}
