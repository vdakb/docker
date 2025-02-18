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

    File        :   ApplicationInstancePublicationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationInstancePublicationHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.app.event;

import java.util.Map;
import java.util.List;

import java.io.Serializable;

import oracle.iam.ui.platform.view.event.TargetSelectionEvent;

import oracle.hst.foundation.faces.JSF;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.sysauthz.app.backing.PublicationState;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationInstancePublicationHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exposing method to call from contextual event handlers and handles all
 ** events belonging to assigning organizational scopes to an
 ** <code>Application Instance</code>.
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
public class ApplicationInstancePublicationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationInstancePublicationHandler</code> that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationInstancePublicationHandler() {
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
   ** one of the application instance task flows is running the event compares
   ** the identifier of the producer region at first.
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
    final String              received = (String)parameter.get("entityId");
    final String              proposed = PublicationState.entityDistinguisher();
    if (proposed.equals(received)) {
      final List<Serializable> selection = event.getSelectedTargets();
      if (!CollectionUtility.empty(selection)) {
        try {
          final PublicationState state = JSF.valueFromExpression("#{backingBeanScope.applicationPublication}", PublicationState.class);
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