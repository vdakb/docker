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

    Copyright © 2021. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   InventoryNavigatorListener.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    InventoryNavigatorListener.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-04-10  DSteding    First release version
*/

package oracle.jdeveloper.deployment.oim.navigator;

import oracle.ide.model.Element;

import oracle.ide.view.ViewSelectionEvent;
import oracle.ide.view.ViewSelectionListener;

////////////////////////////////////////////////////////////////////////////////
// class InventoryNavigatorListener
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to control
 ** explorer nodes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class InventoryNavigatorListener implements ViewSelectionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The <code>DirectoryNavigatorListener</code> implements the singleton
   ** pattern.
   ** <br>
   ** The static attribute {@link #instance} holds this single instance.
   */
  static InventoryNavigatorListener instance = new InventoryNavigatorListener();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>InventoryNavigatorListener</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private InventoryNavigatorListener() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewSelectionChanged (ViewSelectionListener)
  /**
   ** This method is called every time the selection changes in a view.
   ** <br>
   ** The {@link ViewSelectionEvent} object has detailed information of the
   ** objects selected in the view.
   **
   ** @param  event            the {@link ViewSelectionEvent} that characterizes
   **                          the event.
   */
  @Override
  public void viewSelectionChanged(final ViewSelectionEvent event) {
    final Element[] selection = event.getSelection();
    if (selection == null)
      return;

    if (selection.length != 1)
      return;
  }
}