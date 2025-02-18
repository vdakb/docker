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

    System      :   Employee Self Service Portal
    Subsystem   :   Vehicle Administration

    File        :   BrandVehiculeEventHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    BrandVehiculeEventHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     24-06-2020  SBernet     First release version
*/
package bka.employee.portal.vehicle.vhu.event;

import bka.employee.portal.vehicle.vhu.backing.AttributeState;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.ui.platform.view.event.TargetSelectionEvent;

////////////////////////////////////////////////////////////////////////////////
// class BrandVehiculeEventHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exposing method to call from contextual event handlers and handles all
 ** events belonging to assigning Brand to a <code>Vehicule</code>.
 ** <p>
 ** Because contextual event is a functionality of the ADF binding layer, all
 ** event handlers must be exposed from a Data Control.
 ** <br>
 ** In this use case, the exposing Data Control is a JavaBean data control
 ** created from this class
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BrandVehiculeEventHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BrandVehiculeEventHandler</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
    public BrandVehiculeEventHandler() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleSelection
  /**
   ** Handle the payLoad of {@link TargetSelectionEvent}, which is the event
   ** itself by default.
   ** <p>
   ** Due to the nature that this method is called multiple times if more than
   ** one of the user vehicle task flows is running the event compares the
   ** identifier of the producer region at first.
   **
   ** @param  event              the {@link TargetSelectionEvent} delivered from
   **                            the producer occured in a UIXForm component.
   */
  public void handleSelection(final TargetSelectionEvent event) {
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    final Map<String, Object> parameter = event.getParamMap();
    final String     proposed = AttributeState.entityDistinguisher();
    String           received = event.getEventDistinguisher();
    
    if (proposed.equals(received)) {
      final List<Serializable> selection = event.getSelectedTargets();
      if (!CollectionUtility.empty(selection)) {
        try {
          final AttributeState bean = JSF.valueFromExpression("#{backingBeanScope.vehicleAttribute}", AttributeState.class);
          bean.assign(selection);
        }
        // for debugging purpose only catch everything to spool to error log
        catch (Throwable t) {
          t.printStackTrace(System.err);
        }
      }
    }
  }
}