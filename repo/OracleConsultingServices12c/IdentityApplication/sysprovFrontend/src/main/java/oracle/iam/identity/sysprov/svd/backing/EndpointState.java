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
    Subsystem   :   System Provisioning Management

    File        :   EndpointState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.svd.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.faces.event.ActionEvent;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysprov.schema.EndpointAdapter;
import oracle.iam.identity.sysprov.schema.EndpointTypeAdapter;

////////////////////////////////////////////////////////////////////////////////
// class EndpointState
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** modules.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class EndpointState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3883505838941618478")
  private static final long serialVersionUID = -828856013277519870L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointState</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EndpointState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshRegion (overridden)
  /**
   ** Add the UI components placed on the value relationship as a partial
   ** rendering target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   */
  @Override
  protected void refreshRegion() {
    refresh();
    // ensure inheritance
    super.refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detail
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** Endpoint Detail taskflow which renders the details of the associated
   ** Endpoint for the selected job.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **
   ** @return                    the action name to execute afterwards.
   */
  public String detail(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = super.selectedRow(getSearchTable());
    if (selection == null || selection.size() == 0)
      return null;

    final Row                 row          = selection.get(0);
    final Long                endpointKey  = (Long)row.getAttribute(EndpointAdapter.PK);
    final String              endpointName = (String)row.getAttribute(EndpointAdapter.NAME);
    final String              taskFlowId   = "svr" + endpointKey;
    final Map<String, Object> parameter    = new HashMap<String, Object>();
    parameter.put(EndpointAdapter.PK,   endpointKey);
    parameter.put(EndpointAdapter.NAME, endpointName);
    parameter.put(PARAMETER_MODE,      MODE_EDIT);
    parameter.put(PARAMETER_TASKFLOW,  taskFlowId);
    raiseTaskFlowLaunchEvent(taskFlowId, "/WEB-INF/oracle/iam/identity/sysprov/svr/flow/train-tf.xml#train-tf", endpointName, null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_SYSMAN_SCHED, false, parameter);
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the endpoint region of the <code>IT Resource Type</code>.
   */
  private void refresh() {
    final Map<String, Object> parameter = new HashMap<String, Object>();
    // looks like regardless which class type is defined on the task flow input
    // parameter definition ADF stores any values always as of type String
    parameter.put("identifier", ADF.pageFlowScopeStringValue(EndpointTypeAdapter.NAME));
    ADF.executeOperation("refreshEndpoint", parameter);
  }
}