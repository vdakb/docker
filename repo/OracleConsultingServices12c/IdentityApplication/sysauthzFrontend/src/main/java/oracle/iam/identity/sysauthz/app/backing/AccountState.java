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

    File        :   AccountState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountState.


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

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractSearch;
import oracle.iam.ui.catalog.view.enums.CatalogEntityType;

////////////////////////////////////////////////////////////////////////////////
// class AccountState
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service needs to search for associated
 ** <code>Account</code>s of an <code>Application Instance</code>.
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
  @SuppressWarnings("compatibility:-8160683299100138847")
  private static final long serialVersionUID = -328521138265623275L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountState</code> backing bean that allows use as a
   ** JavaBean.
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
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isAccountSelected
  /**
   ** Whether the selected row (aka Application Instance) can be display a
   ** detail page or not.
   **
   ** @return                    <code>true</code> if the display of the detail
   **                            page is possible; otherwise <code>false</code>.
   */
  public boolean isAccountSelected() {
    return !(selectedRowCount(getSearchTable()) == 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isRevokeDisabled
  /**
   ** Whether the selected row(s) (aka Account) can be revoked or not.
   **
   ** @return                    <code>true</code> if the revoke operation is
   **                            possible; otherwise <code>false</code>.
   */
  public boolean isRevokeDisabled() {
    return !(selectedRowCount(getSearchTable()) > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detail
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** Application Instance Detail taskflow to render the details of the selected
   ** Application Instance Account.
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

    final Row                 row         = selection.get(0);
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchAssignRequest
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to assign accounts to the application instance in scope.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public void launchAssignRequest(final @SuppressWarnings("unused") ActionEvent event) {
    final String catalog = "assign_account";
    if (taskFlowOpen(catalog)) {
      raiseTaskFlowLaunchEvent("active_cart", "/WEB-INF/oracle/iam/ui/catalog/tfs/active-cart-tf.xml#active-cart-tf", ADF.resourceBundleValue("oracle.iam.ui.OIMUIBundle", "active.cart"), "/images/warning.png", null, "active_cart", true, null);
    }
    else {
      final Map<String, String> criteria = new HashMap<String, String>();
      criteria.put("entityType",             CatalogEntityType.APPLICATION_INSTANCE.getId());
      criteria.put("showEntityTypeSelector", "false");
      
      final Map<String, Object> parameter = new HashMap<String, Object>();
      parameter.put("requestId",   "-1");
      parameter.put("requestType", "request_access");
      // this taskflow will always request for others
      parameter.put("requestForOthers", "true");
      parameter.put(ConstantsDefinition.ACCESS_TASK_FLOW_ID, catalog);
//      parameter.put("searchCriteria", JsonFactory.getInstance().getSerializer().serialize(criteria));
      final String title = ADF.resourceBundleValue("oracle.iam.ui.OIMUIBundle", "access.request.account.request");
      raiseTaskFlowLaunchEvent(catalog, "/WEB-INF/oracle/iam/ui/catalog/tfs/access-request-tf.xml#access-request-tf", title, "/images/catalog.png", title, "oim_self_prof_myac_reqaccnt", false, parameter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchRevokeRequest
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to revoke the selected accounts from the application instance in
   ** scope.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public void launchRevokeRequest(final @SuppressWarnings("unused") ActionEvent event) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userDetail
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** User Detail taskflow which renders the details of the associated identity
   ** for the selected Application Instance Account.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **
   ** @return                    the action name to execute afterwards.
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
    parameter.put(PARAMETER_MODE,   MODE_VIEW);
    raiseTaskFlowLaunchEvent(taskFlowId, "/WEB-INF/oracle/iam/ui/manageusers/tfs/user-details-tf.xml#user-details-tf", userLogin, null, null, ConstantsDefinition.HELP_TOPIC_ID_SELF_IA_MANGUSERS, false, parameter);
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