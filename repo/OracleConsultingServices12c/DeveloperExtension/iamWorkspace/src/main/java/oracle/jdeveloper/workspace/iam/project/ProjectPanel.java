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

    File        :   ProjectPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ProjectPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project;

import oracle.ide.model.panels.ProjectSettingsTraversablePanel;
import oracle.ide.panels.TraversableContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class ProjectPanel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the Project settings dialog for
 ** editing the settings stored in the {@link TraversableContext} model.
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
public abstract class ProjectPanel extends ProjectSettingsTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5843800429137249317")
  private static final long serialVersionUID = 7605241724332010843L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProjectPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProjectPanel() {
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Called to have this {@link ProjectSettingsTraversablePanel} perform the
   ** commit action.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if all changes are applied
   **                            succesfully; <code>false</code> otherwise.
   */
  protected abstract boolean commit(final boolean destructive);
}