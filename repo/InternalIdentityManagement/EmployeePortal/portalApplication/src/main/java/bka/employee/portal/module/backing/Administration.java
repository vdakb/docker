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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Portal
    Subsystem   :   Application Console Navigation

    File        :   Administration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Administration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-02-23  DSteding    First release version
*/

package bka.employee.portal.module.backing;

import javax.faces.event.ActionEvent;

import oracle.hst.foundation.faces.ADF;

import bka.employee.portal.AbstractManagedBean;

import oracle.adf.controller.TaskFlowId;

////////////////////////////////////////////////////////////////////////////////
// class Administration
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** task flows of the <code>Administration</code> module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Administration extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the Resource Bundle provided by the module specific implementation */
  private static final String BUNDLE           = "bka.employee.portal.bundle.Launchpad";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3968467365145983999")
  private static final long   serialVersionUID = 5211449444013497822L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Administration</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Administration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateVehicleColor
  /**
   ** Launch the taskflow belonging to managing <code>Vehicle Color</code>s to
   ** register a new or unregister an existing one.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateVehicleColor(final @SuppressWarnings("unused") ActionEvent event) {
    // Taskflow to maintain Vehicle Colors
    launchTaskFlow("vhc-search", "/WEB-INF/bka/employee/portal/vehicle/vhc/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ADMINISTRATION_VEHICLE_COLOR_TITLE"), null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateVehicleType
  /**
   ** Launch the taskflow belonging to managing <code>Vehicle Type</code>s to
   ** register a new or unregister an existing one.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateVehicleType(final @SuppressWarnings("unused") ActionEvent event) {
    // Taskflow to maintain Vehicle Colors
    launchTaskFlow("vht-search", "/WEB-INF/bka/employee/portal/vehicle/vht/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ADMINISTRATION_VEHICLE_TYPE_TITLE"), null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateVehicleBrand
  /**
   ** Launch the taskflow belonging to managing
   ** <code>Vehicle Manufactor Brand</code>s to register a new or unregister an
   ** existing one.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateVehicleBrand(final @SuppressWarnings("unused") ActionEvent event) {
    // Taskflow to maintain User Vehicles
    launchTaskFlow("vmb-search", "/WEB-INF/bka/employee/portal/vehicle/vmb/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ADMINISTRATION_VEHICLE_BRAND_TITLE"), null, null, null);
  }
}