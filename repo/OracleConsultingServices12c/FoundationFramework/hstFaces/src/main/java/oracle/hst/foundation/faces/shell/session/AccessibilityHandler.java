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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   AccessibilityHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccessibilityHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.session;

import javax.faces.context.FacesContext;

import javax.faces.application.ViewHandler;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.hst.foundation.faces.JSF;

import oracle.hst.foundation.faces.shell.model.Accessibility;

////////////////////////////////////////////////////////////////////////////////
// class AccessibilityHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Internal use only.
 ** <p>
 ** Backing bean for accessibility configuration dialog during a session.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 0.0.0.0
 ** @since   0.0.0.0
 */
public class AccessibilityHandler extends Accessibility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6562593614913713515")
  private static final long serialVersionUID = 5662491913562441009L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Accessibility     preference;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessibilityHandler</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessibilityHandler() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccessibility
  /**
   ** Returns the value of the accessibility session property.
   **
   ** @param  value              the value of the accessibility session
   **                            property.
   **                            Allowed object is {@link Accessibility}.
   */
  public void setAccessibility(final Accessibility value) {
    setLargeFont(value.isLargeFont());
    setScreenReader(value.isScreenReader());
    setHighContrast(value.isHighContrast());
    this.preference = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessibility
  /**
   ** Returns the value of the accessibility session property.
   **
   ** @return                    the value of the accessibility session
   **                            property.
   **                            Possible object is
   **                            {@link Accessibility}.
   */
  public Accessibility getAccessibility() {
    return this.preference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Handle the events occured in the UIXDialog component managing the
   ** accessibilty preferences of a user session.
   **
   ** @param  event              the {@link DialogEvent} delivered from the
   **                            action occured in a UIXDialog component.
   */
  public void submit(final DialogEvent event) {
    if (event.getOutcome() == DialogEvent.Outcome.ok) {
      this.preference.setScreenReader(isScreenReader());
      this.preference.setHighContrast(isHighContrast());
      this.preference.setLargeFont(isLargeFont());
      reload();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reload
  /**
   ** Redirect root context id, and cause the responseComplete() method to be
   ** called on the FacesContext instance for the current request.
   */
  private void reload() {
    final FacesContext context = JSF.context();
    final ViewHandler  handler = JSF.viewHandler();
    final String       root    = JSF.viewRoot().getViewId();
    final String       action  = handler.getActionURL(context, root);
    JSF.redirectAction(action);
  }
}