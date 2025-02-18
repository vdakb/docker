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

    File        :   EntitlementState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.app.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.faces.event.ActionEvent;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysauthz.schema.EntitlementAdapter;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementState
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service needs to search for associated
 ** <code>Entitlement</code>s of an <code>Application Instance</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class EntitlementState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7968347286874060668")
  private static final long serialVersionUID = -4399738815492882104L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementState</code> backing bean
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntitlementState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detail
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** Entitlement Detail taskflow to render the details of the selected
   ** Entitlement Instance.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **
   ** @return                    the action name to execute afterwards.
   */
  public String detail(final @SuppressWarnings("unused") ActionEvent event) {
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
    parameter.put(PARAMETER_MODE,          MODE_EDIT);
    parameter.put(PARAMETER_TASKFLOW,      flow);
    raiseTaskFlowLaunchEvent(flow, "/WEB-INF/oracle/iam/identity/sysauthz/ent/flow/train-tf.xml#train-tf", name, null, name, ConstantsDefinition.HELP_TOPIC_ID_ADM_CONFIG_APPINST, false, parameter);
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partialRenderSubmitRevert
  /**
   ** Add the global action components (Save/Apply/Revert) as a partial
   ** rendering target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   */
  public void partialRenderSubmitRevert() {
    super.partialRenderSubmitRevert();
  }
}