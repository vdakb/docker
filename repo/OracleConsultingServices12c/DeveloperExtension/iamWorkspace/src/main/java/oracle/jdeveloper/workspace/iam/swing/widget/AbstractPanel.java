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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   AbstractPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AbstractPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import oracle.ide.panels.DefaultTraversablePanel;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractPanel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the Preference dialog for editing the
 ** preferences stored in the {@code Provider} model.
 ** <p>
 ** In general, preferences panels are not supposed to be published APIs, so we
 ** enforce that. Even though the panel is constructed by the IDE framework
 ** using reflection, the IDE framework does not require that it is public (only
 ** that it has a no-argument constructor).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class AbstractPanel extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:52174695723481736")
  private static final long serialVersionUID = -9080431491409962757L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractPanel</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractPanel() {
    // ensure inheritance
    super();

    // initialize UI components
    initializeComponent();
    localizeComponent();
    initializeLayout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  protected abstract void initializeComponent();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  protected abstract void localizeComponent();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  protected abstract void initializeLayout();
}