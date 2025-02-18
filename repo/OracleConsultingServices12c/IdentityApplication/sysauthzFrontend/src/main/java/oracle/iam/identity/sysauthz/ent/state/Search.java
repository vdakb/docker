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

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.ent.state;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.faces.event.ActionEvent;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.identity.frontend.AbstractSearchState;

import oracle.iam.identity.sysauthz.schema.EntitlementAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** modules.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Search extends AbstractSearchState {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2621541769732512122")
  private static final long serialVersionUID = 6957405695001230627L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Search</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Search() {
    // ensure inheritance
    super(EntitlementAdapter.PK, EntitlementAdapter.CODE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isOpenDisabled
  /**
   ** Whether the selected row (aka Entitlement) can be opened in a detail page
   ** or not.
   **
   ** @return                    <code>true</code> if the navigation to the
   **                            detail page is possible; otherwise
   **                            <code>false</code>.
   */
  public boolean isOpenDisabled() {
    boolean flag = false;
    if (selectedRowCount(getSearchTable()) == 1) {
      flag = false;
    }
    else {
      flag = true;
    }
    return flag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   refresh
  /**
   ** Refreshing the state of the UI component which displays the table of
   ** queried profiles and controls the further actions for those profiles in
   ** this table.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String refresh() {
    refresh("EntitlementIterator");
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   openDetail
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** Entitlement taskflow to render the details of the selected Entitlement.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String openDetail(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection   = super.selectedRow(getSearchTable());
    if (selection == null || selection.size() == 0)
      return null;

    final Row                 row  = selection.get(0);
    final String              key  = row.getAttribute(EntitlementAdapter.PK).toString();
    final String              name = row.getAttribute(EntitlementAdapter.VALUE).toString();
    final String              flow = "ent" + key.toString();
    final Map<String, Object> parameter  = new HashMap<String, Object>();
    parameter.put(EntitlementAdapter.PK,   key);
    parameter.put(EntitlementAdapter.CODE, name);
    parameter.put(PARAMETER_MODE,              MODE_EDIT);
    parameter.put(PARAMETER_TASKFLOW,          flow);
    raiseTaskFlowLaunchEvent(flow, "/WEB-INF/oracle/iam/identity/sysauthz/ent/flow/train-tf.xml#train-tf", name, null, name, ConstantsDefinition.HELP_TOPIC_ID_ADM_CONFIG_APPINST, false, parameter);
    return null;
  }
}