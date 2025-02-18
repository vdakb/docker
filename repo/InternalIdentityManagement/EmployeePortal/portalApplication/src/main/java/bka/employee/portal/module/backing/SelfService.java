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

    File        :   SelfService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SelfService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-02-23  DSteding    First release version
*/

package bka.employee.portal.module.backing;

import javax.faces.event.ActionEvent;

import oracle.hst.foundation.faces.ADF;

import bka.employee.portal.AbstractManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class SelfService
// ~~~~~ ~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** task flows of the <code>SelfService</code> module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class SelfService extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the Resource Bundle provided by the module specific implementation */
  private static final String BUNDLE           = "bka.employee.portal.bundle.Launchpad";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3968467365145983999")
  private static final long serialVersionUID = 5211449444013497822L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SelfService</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SelfService() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateVehicle
  /**
   ** Launch the taskflow belonging to managing <code>Vehicle</code>s belonging
   ** to an employee.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateVehicle(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("vhu-search", "/WEB-INF/bka/employee/portal/vehicle/vhu/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ADMINISTRATION_VEHICLE_TITLE"), null, null, null);
  }
}