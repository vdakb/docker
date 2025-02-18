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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Authorization Management

    File        :   Train.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Train.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.rpt.state;

import java.util.Map;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractFlow;

import oracle.iam.identity.sysauthz.schema.ReportAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to create or manage <code>Report</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Train extends AbstractFlow {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Authorization";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4861926574340248747")
  private static final long   serialVersionUID = -6948372568977182512L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Train</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Train() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Create or updates the <code>Application Instance</code>, open a newly
   ** created <code>Application Instance</code> in the edit mode and close the
   ** current task flow.
   ** <br>
   ** Leverage the operational binding <code>commit</code> to do so.
   **
   ** @return                    the outcome of the operation to support
   **                            control rule navigation in the task flow.
   */
  public String submit() {
    // commit the changes to the persistence layer which will now do what's
    // needed to create or update the Report metadata
    // keep in mind this will not attach any other related values like job
    // values etc.
    final Object result = ADF.executeAction(SUBMIT);
    if (ADF.executeActionSucceeded(result)) {
      final RowSetIterator rowSet = rowSetIterator("ReportIterator");
      final Row    row   = rowSet.getCurrentRow();
      final String name  = (String)row.getAttribute(ReportAdapter.NAME);
      final Map    scope = ADF.current().getPageFlowScope();
      final String flow  = (String)scope.get(PARAMETER_TASKFLOW);
      raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "RPT_TRAIN_MODIFY"), name));
      refreshTaskFlow(flow, name, null, null);
      // operation succesful hence clean up the state of the taskflow
      markClean();
      return SUCCESS;
    }
    else
      return ERROR;
  }
}