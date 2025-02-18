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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Virtual Directory Facility

    File        :   Panel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Panel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.70  2017-01-02  DSteding    Fixed behavior in onEntry method
                                               of the panel which overrides the
                                               URL to the foundation path every
                                               time because of the wrong if
                                               statement.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.ovd.project;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.project.FeatureProjectPanel;

////////////////////////////////////////////////////////////////////////////////
// class Panel
// ~~~~~ ~~~~~
/**
 ** The UI panel that provides support in the project properties dialog for
 ** editing the properties stored in the <code>Preference Store</code> model.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 ** <p>
 ** In general, preferences panels are not supposed to be published APIs, so we
 ** enforce that. Even though the panel is constructed by the IDE framework
 ** using reflection, the IDE framework dovd not require that it is public (only
 ** that it has a no-argument constructor).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Panel extends FeatureProjectPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4865589640281638600")
  private static final long serialVersionUID = -8537065753320796505L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Panel</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Panel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataKey (ProjectSettingsTraversablePanel)
  @Override
  public String getDataKey() {
    return "oracle.iam/dsf/ovd";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPropertyKeys (ProjectSettingsTraversablePanel)
  @Override
  public String[] getPropertyKeys() {
    final String[] what = {"A", "B", "C"};
    return what;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onProjectPanelEntry (ProjectSettingsTraversablePanel)
  /**
   ** This method is called when the <code>Traversable</code> is being entered.
   ** <p>
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the specified {@link TraversableContext}.
   ** <p>
   ** When the same <code>Traversable</code> is entered more than once in the
   ** course of interacting with the user, the <code>Traversable</code> needs to
   ** reload the data directly from the {@link TraversableContext} rather than
   ** caching data objects. Some reasons for this include:
   ** <ul>
   **   <li>Other <code>Traversable</code> may edit the data objects or even
   **       replace them.
   **   <li>The same <code>Traversable</code> instance may be used for editing
   **       multiple different instances of the same object type.
   ** </ul>
   ** Loading data directly from the {@link TraversableContext} is the best way
   ** to ensure that the <code>Traversable</code> will not be editing the wrong
   ** data.
   ** <p>
   ** The <code>Traversable</code> should not even cache references to data
   ** objects between invocations of onEntry and
   ** {@link #onExit(TraversableContext)} because the UI container is not
   ** required to guarantee that the references will be identical.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   */
  @Override
  public void onProjectPanelEntry(final TraversableContext context) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (ProjectPanel)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (ProjectPanel)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected void localizeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (ProjectPanel)
  /**
   ** Layout the panel.
   */
  @Override
  protected void initializeLayout() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (ProjectPanel)
  /**
   ** Called to have this {@link FeatureProjectPanel} perform the commit
   ** action.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if all changes are applied
   **                            succesfully; <code>false</code> otherwise.
   */
  @Override
  protected boolean commit(final boolean destructive) {
    return false;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExit (overridden)
  /**
   ** This method is called when the <code>Traversable</code> is being exited.
   ** <p>
   ** At this point, the <code>Traversable</code> should copy the data from its
   ** associated UI back into the data structures in the
   ** {@link TraversableContext}.
   ** <p>
   ** If the <code>Traversable</code> should not be exited because the user has
   ** entered either incomplete, invalid, or inconsistent data, then this method
   ** can throw a {@link TraversalException} to indicate to the property dialog
   ** or wizard that validation failed and that the user should not be allowed
   ** to exit the current <code>Traversable</code>. Refer to the
   ** {@link TraversalException} javadoc for details on passing the error
   ** message that should be shown to the user.
   **
   ** @param  context            the data object where changes made in the UI
   **                            should be copied so that the changes can be
   **                            accessed by other <code>Traversable</code>s.
   **
   ** @throws TraversalException if the user has entered either incomplete,
   **                            invalid, or inconsistent data.
   **                            <p>
   **                            This exception prevents the property dialog or
   **                            wizard from continuing and forces the user to
   **                            stay on the current <code>Traversable</code>
   **                            until the data entered is valid or the user
   **                            cancels. The exception class itself is capable
   **                            of carrying an error message that will be shown
   **                            to the user. Refer to its javadoc for details.
   */
  @Override
  public void onExit(final TraversableContext context)
    throws TraversalException {

    // ensure inheritance
    super.onExit(context);
  }
}