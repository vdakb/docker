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

    File        :   AccountState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.obj.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.faces.event.ActionEvent;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.identity.frontend.train.AbstractSearch;

////////////////////////////////////////////////////////////////////////////////
// class AccountState
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Declares methods the user interface service needs to search for associated
 ** <code>Account</code>s of an <code>Resource Object</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class AccountState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6884698734270005302")
  private static final long serialVersionUID = 1695429665586623591L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountState</code> backing bean that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountState() {
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
   ** Resource Object Detail taskflow to render the details of the selected
   ** Resource Object Account.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String detail(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection   = super.selectedRow(getSearchTable());
    if (selection == null || selection.size() == 0)
      return null;

    final Row                 row         = selection.get(0);
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userDetail
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** User Detail taskflow which renders the details of the associated identity
   ** for the selected Resource Object Account.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String userDetail(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = super.selectedRow(getSearchTable());
    if (selection == null || selection.size() == 0)
      return null;

    final Row                 row         = selection.get(0);
    final String              userKey     = row.getAttribute("beneficiaryKey").toString();
    final String              userLogin   = row.getAttribute("beneficiaryLogin").toString();
    final String              taskFlowId  = "user_detail" + userKey;
    final Map<String, Object> parameter   = new HashMap<String, Object>();
    parameter.put("usr_key",        userKey);
    parameter.put("userLogin",      userLogin);
    parameter.put("confirmMessage", false);
    raiseTaskFlowLaunchEvent(taskFlowId, "/WEB-INF/oracle/iam/ui/manageusers/tfs/user-details-tf.xml#user-details-tf", userLogin, null, null, ConstantsDefinition.HELP_TOPIC_ID_SELF_IA_MANGUSERS, false, parameter);
    return null;
  }
}