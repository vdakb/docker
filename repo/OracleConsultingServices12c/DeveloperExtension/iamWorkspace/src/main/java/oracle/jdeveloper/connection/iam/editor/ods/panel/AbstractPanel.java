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

    File        :   AbstractPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AbstractPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel;

import oracle.javatools.ui.TransparentPanel;

import oracle.javatools.ui.layout.VerticalFlowLayout;

import oracle.ide.util.Namespace;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractPanel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display informations of a Directory Service
 ** in a table layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class AbstractPanel extends TransparentPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4353870683644474491")
  private static final long           serialVersionUID = -8071002406614552117L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected transient final String    path;
  protected transient final Namespace data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractPanel</code> responsible to visualize
   ** the table UI.
   **
   ** @param  path               the path whose associated value is to be
   **                            handled.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  public AbstractPanel(final String path, final Namespace data) {
    // ensure inheritance
    super();

    // initialize instance
    this.path   = path;
    this.data   = data;

    // create the panel responsible to visualize the general information about
    // the directory entry
    initializeLayout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the page.
   ** <br>
   ** By default the layout in initialized as a {@link VerticalFlowLayout}.
   ** <p>
   ** Sub classes should override this mathod of anaothe layout is more
   ** appropriate
   */
  protected void initializeLayout() {
    // initialize panel layout
    setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5, true, true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView
  /**
   ** The data that the <code>FlatEditorTransparentPanel</code> should use to
   ** populate specific components comes from the {@link Namespace} passed to
   ** this page.
   */
  public abstract void updateView();
}