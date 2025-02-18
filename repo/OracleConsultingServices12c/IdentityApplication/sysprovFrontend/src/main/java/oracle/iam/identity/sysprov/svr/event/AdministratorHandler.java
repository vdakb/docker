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

    File        :   AdministratorHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AdministratorHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.svr.event;

import java.util.Map;
import java.util.List;

import java.io.Serializable;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.ui.platform.view.event.TargetSelectionEvent;

import oracle.hst.foundation.faces.JSF;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.sysprov.schema.EndpointAdministratorAdapter;

import oracle.iam.identity.sysprov.svr.backing.AdministratorState;

////////////////////////////////////////////////////////////////////////////////
// class AdministratorHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Exposing method to call from contextual event handlers and handles all
 ** events belonging to assigning administrative roles to an
 ** <code>Endpoint</code>.
 ** <p>
 ** Because contextual event is a functionality of the ADF binding layer, all
 ** event handlers must be exposed from a Data Control.
 ** <br>
 ** In this use case, the exposing Data Control is a JavaBean data control
 ** created from this class
 **
 ** @author  dieter.dteding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class AdministratorHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AdministratorHandler</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AdministratorHandler() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleSelection
  /**
   ** Handle the payLoad of a {@link TargetSelectionEvent}, which is the event
   ** itself by default.
   ** <p>
   ** Due to the nature that this method is called multiple times if more than
   ** one of the <code>Resource Object</code> task flows are running the event
   ** compares the identifier of the producer region at first.
   **
   ** @param  event              the {@link TargetSelectionEvent} delivered from
   **                            the producer occured in a UIXForm component.
   **                            <br>
   **                            Allowed object is {@link TargetSelectionEvent}.
   */
  public void handleSelection(final TargetSelectionEvent event) {
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    final String received = ADF.pageFlowScopeStringValue(AdministratorState.PICK_ENTITY);
    final String proposed = AdministratorState.entityDistinguisher();
    if (proposed.equals(received)) {
      final List<Serializable> selection = event.getSelectedTargets();
      if (!CollectionUtility.empty(selection)) {
        try {
          final AdministratorState state = JSF.valueFromExpression("#{backingBeanScope.endpointAdministrator}", AdministratorState.class);
          state.assign(selection);
        }
        // for debugging purpose only catch everything to spool to error log
        catch (Throwable t) {
          t.printStackTrace(System.err);
        }
      }
    }
  }
}