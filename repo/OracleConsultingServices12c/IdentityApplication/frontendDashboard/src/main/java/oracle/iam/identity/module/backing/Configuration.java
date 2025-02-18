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

    File        :   Configuration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Configuration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.module.backing;

import java.util.HashMap;
import java.util.Map;

import javax.faces.event.ActionEvent;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.AbstractManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class Configuration
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** task flows of the <code>Configuration</code> module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Configuration extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the Resource Bundle provided by the module specific implementation */
  private static final String BUNDLE           = "oracle.iam.identity.bundle.Launchpad";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8351699918329481321")
  private static final long   serialVersionUID = -392357812095567638L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Configuration</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Configuration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateLookup
  /**
   ** Launch the taskflow belonging to managing <code>Lookup</code>s which are
   ** visible for the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateLookup(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("lku-search", "/WEB-INF/oracle/iam/identity/sysconfig/lku/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "LOOKUP_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_CONFIG_LOOKUP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateProperty
  /**
   ** Launch the taskflow belonging to managing <code>Properties</code> which
   ** are visible for the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateProperty(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("pty-search", "/WEB-INF/oracle/iam/identity/sysconfig/pty/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "PROPERTY_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_SYSMAN_CONF);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateNotification
  /**
   ** Launch the taskflow belonging to managing <code>Notification</code>s which
   ** are visible for the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateNotification(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("tpl-search", "/WEB-INF/oracle/iam/identity/sysconfig/tpl/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "NOTIFICATION_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_SYSMAN_NOT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateApprovalPolicy
  /**
   ** Launch the taskflow belonging to search for
   ** <code>Approval Policies</code> which are visible for the connected user
   ** to investigate the grants for other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateApprovalPolicy(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("wkf-search", "/WEB-INF/oracle/iam/ui/workflowpolicies/tfs/workflow-policies-tf.xml#workflow-policies-tf", ADF.resourceBundleValue(BUNDLE, "APRROVALPOLICY_TITLE"), null, null, "oim_adm_wkflw_rule");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateSelfServiceCapality
  /**
   ** Launch the taskflow belonging to search for
   ** <code>Self Service Capabilities</code> which are visible for the connected
   ** user to investigate the grants for other users.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateSelfServiceCapality(final @SuppressWarnings("unused") ActionEvent event) {
    final Map<String,Object> parameter = new HashMap<String,Object>();
    parameter.put("policyName", "Self Service Capabilities");
    launchTaskFlow("ssc-search", "/WEB-INF/oracle/iam/ui/organization/tfs/home-org-policy-tf.xml#home-org-policy-tf", ADF.resourceBundleValue(BUNDLE, "SELSERVICE_TITLE"), null, null, "oim_adm_sysconf_selfserv", false, parameter);
  }
}