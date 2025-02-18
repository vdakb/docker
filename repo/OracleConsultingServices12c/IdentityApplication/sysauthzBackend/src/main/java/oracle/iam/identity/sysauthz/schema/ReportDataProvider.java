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
    Subsystem   :   System Authorization Management

    File        :   ReportDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReportDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import oracle.adf.share.ADFContext;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.ui.common.model.certification.bipwebservice.support.RunReportWS;

import oracle.iam.ui.common.model.certification.bipwebservice.v2.proxy.CatalogService.types.ItemData;
import oracle.iam.ui.common.model.certification.bipwebservice.v2.proxy.CatalogService.types.ArrayOfItemData;
import oracle.iam.ui.common.model.certification.bipwebservice.v2.proxy.CatalogService.types.CatalogContents;

////////////////////////////////////////////////////////////////////////////////
// class ReportDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Authorization Management customization.
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
public class ReportDataProvider extends AbstractDataProvider<ReportAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3560445507156513848")
  private static final long serialVersionUID = -5857947602985726744L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReportDataProvider</code> data access object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ReportDataProvider() {
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
   **                            {@link EntitlementInstanceAdapter}.
   */
  public List<ReportAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final String directory = (String)ADFContext.getCurrent().getPageFlowScope().get("reportDirectory");
    if (directory == null || directory.length() == 0)
      return new ArrayList<ReportAdapter>();

    final String category = "/IdentityManager/" + directory;
    return populate(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   populate
  private List<ReportAdapter> populate(final String category) {
    RunReportWS         identityReport  = new RunReportWS();
    CatalogContents     identityContent = identityReport.invokeGetFolderContentsWS(category);
    ArrayOfItemData     identityData    = identityContent.getCatalogContents();
    List<ItemData>      itemData        = identityData.getItem();
    List<ReportAdapter> batch           = new ArrayList<ReportAdapter>();
    for (ItemData       item : itemData) {
      if (item.getType().equals("Report")) {
        batch.add(new ReportAdapter(item.getDisplayName(), item.getAbsolutePath()));
      }
      else if (item.getType().equals("Folder")) {
        List<ReportAdapter> childReports = populate(item.getAbsolutePath());
        for (ReportAdapter report: childReports) {
          String directory = report.getPath();
          if (directory.length() == 0)
            report.setPath(item.getDisplayName());
          else
            report.setPath(directory + "/" + item.getDisplayName());
        }
        batch.addAll(childReports);
      }
    }
    return batch;
  }
}
