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

    File        :   AttributeState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.rce.backing;

import java.util.Map;
import java.util.HashMap;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractStep;

import oracle.iam.identity.sysprov.schema.ReconciliationEventStatus;
import oracle.iam.identity.sysprov.schema.ReconciliationEventAdapter;

////////////////////////////////////////////////////////////////////////////////
// class AttributeState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** modules.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class AttributeState extends AbstractStep {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String         PICK_USER       = "catalog_user";
  public static final String         PICK_ENTITY     = "eventEntity";

  private static final String        PICK_FLOW       = "/WEB-INF/oracle/iam/ui/common/tfs/user-picker-tf.xml#user-picker-tf";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3947428782919057224")
  private static final long serialVersionUID = 7201295538507296071L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private ReconciliationEventStatus status;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeState</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AttributeState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed (AbstractStep)
  /**
   ** Perfoms all actions belonging to the UI to reflect value change events on
   ** particular components in the current page.
   **
   ** @param  event              the {@link ValueChangeEvent} object that
   **                            characterizes the action to perform.
   */
  @Override
  public final void changed(final ValueChangeEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityDistinguisher
  /**
   ** Factory method to create a unique entity distinguisher to provide the
   ** possibility to identify the taskflow which raised the contextual event
   ** to pick a administrative role by the event handler.
   ** <br>
   ** The event handler itself is registered for every region.
   ** <p>
   ** The string of the created identifier consists of
   ** <ol>
   **   <li>the hardcoded prefix <code>rce</code>
   **   <li>the instance identifier of the current taskflow obtained from the
   **       page flow scope.
   ** </ol>
   **
   ** @return                    the created unique event name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String entityDistinguisher() {
    return String.format("rce#%s", ADF.pageFlowScopeStringValue(ReconciliationEventAdapter.PK));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickListener
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select administrative roles to be added.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void pickListener(final @SuppressWarnings("unused") ActionEvent event) {
    ADF.pageFlowScopeStringValue(PICK_ENTITY, entityDistinguisher());
    final Map<String, Object> parameter = new HashMap<String, Object>();
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    parameter.put(EVENT_DISTINGUISHER, PICK_USER);
    parameter.put(EVENT_SELECTIONTYPE, SELECTIONTYPE_MULTIPLE);
    raiseTaskFlowLaunchEvent(PICK_USER, PICK_FLOW, ADF.resourceBundleValue(BUNDLE, "RCE_IDENTITY_PICKER"), "/images/add_ena.png", null, null, true, parameter);
  }
}