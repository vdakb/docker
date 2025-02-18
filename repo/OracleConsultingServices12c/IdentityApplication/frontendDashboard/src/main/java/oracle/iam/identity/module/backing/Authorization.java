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

    File        :   Authorization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Authorization.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.module.backing;

import java.util.Map;
import java.util.HashMap;

import javax.faces.event.ActionEvent;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.ui.common.model.certification.bipwebservice.support.RunReportWS;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.AbstractManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class Authorization
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** task flows of the <code>Authorization</code> module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Authorization extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the Resource Bundle provided by the module specific implementation */
  private static final String BUNDLE           = "oracle.iam.identity.bundle.Launchpad";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9039866967280001190")
  private static final long   serialVersionUID = 7755739639006147158L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Authorization</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Authorization() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateRole
  /**
   ** Launch the taskflow belonging to managing <code>Role</code>s which are
   ** visible for the connected user to investigate the grants for other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateRole(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("ugp-search", "/WEB-INF/oracle/iam/ui/role/tfs/search-roles-tf.xml#search-roles-tf", ADF.resourceBundleValue(BUNDLE, "ROLE_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_SELF_IA_ROLES);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateAccessPolicy
  /**
   ** Launch the taskflow belonging to search for <code>Access Policy</code>s
   ** which are visible for the connected user to investigate the grants for
   ** other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateAccessPolicy(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("pol-search", "/WEB-INF/oracle/iam/ui/accesspolicy/tfs/list-access-policies-tf.xml#list-access-policies-tf", ADF.resourceBundleValue(BUNDLE, "ACCESSPOLICY_TITLE"), null,  null, "oim_self_ia_accplc");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateReport
  /**
   ** Launch the taskflow belonging to search for <code>Reports</code>s
   ** which are visible for the connected user to investigate the grants for
   ** other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateReport(final @SuppressWarnings("unused") ActionEvent event) {
    // the navigation van only be initiated if BI Publisher is reacbable
    final RunReportWS service = new RunReportWS();
    if (!service.validateBipURL()) {
      JSF.showErrorMessage(ADF.resourceBundleValue(BUNDLE, "REPORT_UNAVAILABLE"));
      return;
    }
    launchTaskFlow("rpt-search", "/WEB-INF/oracle/iam/identity/sysauthz/rpt/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "REPORT_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_CONFIG_APPINST);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateApplication
  /**
   ** Launch the taskflow belonging to search for <code>Application</code>s
   ** which are visible for the connected user to investigate the grants for
   ** other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateApplication(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("app-search", "/WEB-INF/oracle/iam/identity/sysauthz/app/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "APPLICATION_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_CONFIG_APPINST);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateEntitlement
  /**
   ** Launch the taskflow belonging to search for <code>Entitlement</code>s
   ** which are visible for the connected user to investigate the grants for
   ** other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateEntitlement(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("ent-search", "/WEB-INF/oracle/iam/identity/sysauthz/ent/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ENTITLEMENT_TITLE"), null, null, "ent-search");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateAdminRole
  /**
   ** Launch the taskflow belonging to search for <code>Admin Role</code>s
   ** which are visible for the connected user to investigate the grants for
   ** other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateAdminRole(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("adm-search", "/WEB-INF/oracle/iam/ui/authz/tfs/search-adminrole-tf.xml#search-adminrole-tf", ADF.resourceBundleValue(BUNDLE, "ADMINROLE_TITLE"), null, null, "oim_self_ia_admrol");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateCatalog
  /**
   ** Launch the taskflow belonging to search for <code>Catalog</code> items
   ** which are visible for the connected user to investigate the grants for
   ** other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateCatalog(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("cat-search", "/WEB-INF/oracle/iam/identity/sysauthz/cat/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "CATALOG_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_SELF_REQ_CAT);
  }
}