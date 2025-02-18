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

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.module.backing;

import javax.faces.event.ActionEvent;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.AbstractManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class Provisioning
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** task flows of the <code>Provisioning</code> module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Provisioning extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the Resource Bundle provided by the module specific implementation */
  private static final String BUNDLE           = "oracle.iam.identity.bundle.Launchpad";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1804749635843610697")
  private static final long   serialVersionUID = -8485756777685781990L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Provisioning() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateResource
  /**
   ** Launch the taskflow belonging to managing <code>Resource Object</code>s
   ** which are visible for the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateResource(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("obj-search", "/WEB-INF/oracle/iam/identity/sysprov/obj/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "RESOURCE_TITLE"), null, null, "obj-search");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateProvisioning
  /**
   ** Launch the taskflow belonging to managing
   ** <code>Provisioning Process</code>es which are visible for the connected
   ** user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateProvisioning(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("pkg-search", "/WEB-INF/oracle/iam/identity/sysprov/pkg/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "PROVISIONING_TITLE"), null, null, "pkg-search");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateReconciliation
  /**
   ** Launch the taskflow belonging to managing <code>Reconciliation</code>
   ** events which are visible for the connected
   ** user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateReconciliation(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("rce-search", "/WEB-INF/oracle/iam/identity/sysprov/rce/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "RECONCILIATION_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_EVNT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateOpenTask
  /**
   ** Launch the taskflow belonging to managing <code>Open Task</code>s which
   ** are assigned to the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateOpenTask(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("otl-search", "/WEB-INF/oracle/iam/ui/authenticated/selfprov/tfs/self-prov-tf.xml#self-prov-tf", ADF.resourceBundleValue(BUNDLE, "OPENTASK_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_SELF_IA_OPENTASKS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateEndpoint
  /**
   ** Launch the taskflow belonging to managing
   ** <code>IT Resource</code>es which are visible for the connected
   ** user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateEndpoint(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("svr-search", "/WEB-INF/oracle/iam/identity/sysprov/svr/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ENDPOINT_TITLE"), null, null, "svr-search");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateEndpointType
  /**
   ** Launch the taskflow belonging to managing
   ** <code>IT Resource</code>es which are visible for the connected
   ** user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateEndpointType(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("svd-search", "/WEB-INF/oracle/iam/identity/sysprov/svd/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ENDPOINTTYPE_TITLE"), null, null, "svd-search");
  }
}