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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   AuthenticationPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AuthenticationPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.root;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.connection.iam.editor.ods.RootEditor;
import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPage;
import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ControlPage
// ~~~~~ ~~~~~~~~~~~
/**
 ** A flat editor page suitable to display supported controls of a Directory
 ** Service RootDSE in a editor page.
 ** <p>
 ** An LDAP control is an element that may be included in an LDAP Message. If it
 ** is included in a request message, it can be used to provide additional
 ** information about the way that the operation should be processed. If it is
 ** included in the response message, it can be used to provide additional
 ** information about the way the operation was processed.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ControlPage extends AbstractPage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2680189624046127218")
  private static final long serialVersionUID = 4203545218358478428L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private AttributePanel controls;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ControlPage</code> responsible to visualize the table
   ** UI of supported controls view that populates its data from the specified
   ** {@link Namespace}.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  private ControlPage(final Namespace data) {
    // ensure inheritance
    super(RootEditor.KEY, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeView (AbstractPage)
  /**
   ** The data that the <code>SupportPage</code> should use to populate specific
   ** components comes from the {@link Namespace} passed to this page.
   */
  @Override
  protected final void initializeView() {
    this.controls = AttributePanel.build(Bundle.string(Bundle.ROOT_SUPPORTED_CONTROL_PANEL_HEADER), RootEditor.CONTROL, this.data);
    // the title of the page usually the name of the connection
    initializeHeaderPanel(Bundle.string(Bundle.ROOT_SUPPORTED_CONTROL_PANEL_TITLE), this.controls);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView (AbstractPage)
  /**
   ** Called after the {@link Namespace} data that the <code>ControlPage</code>
   ** use to populate its specific components was updated.
   */
  @Override
  public void updateView() {
    updatePage();
    this.controls.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ControlPage</code> that populates its
   ** data from the specified {@link Namespace}.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the validated <code>ControlPage</code>.
   **                            <br>
   **                            Possible object <code>ControlPage</code>.
   */
  public static ControlPage build(final Namespace data) {
    return new ControlPage(data);
  }
}