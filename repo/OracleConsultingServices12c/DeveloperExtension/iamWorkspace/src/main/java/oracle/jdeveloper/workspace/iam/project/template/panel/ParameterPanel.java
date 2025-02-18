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
    Subsystem   :   Identity and Access Management Facility

    File        :   ParameterPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ParameterPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template.panel;

import java.awt.Component;
import java.awt.CardLayout;

import javax.swing.tree.TreePath;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNode;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNodeData;

import oracle.jdeveloper.workspace.iam.project.template.TemplateData;

////////////////////////////////////////////////////////////////////////////////
// class ParameterPanel
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the {@link Configurator} dialog for
 ** editing the properties stored in the {@link TemplateData} model.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class ParameterPanel extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2680513230077918124")
  private static final long                 serialVersionUID = 2963599026219227934L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient DefaultTraversablePanel canvas;
  private transient ParameterData           parameter = new ParameterData();
  private transient ParameterNull           empty     = new ParameterNull();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ParameterPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ParameterPanel() {
    // ensure inheritance
    super();

    // initialize UI components
    setLayout(new CardLayout());
    add(this.parameter, Configurator.DATA_NODE);
    add(this.empty,     Configurator.NULL_NODE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultFocusComponent
  /**
   ** Returns the component that should be focued this panel is vesible for the
   ** first time.
   **
   ** @return                    the component that should be focued this panel
   **                            is vesible for the first time.
   */
  public Component defaultFocusComponent() {
    return this.canvas;
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnabled
  /**
   ** Sets whether or not this component is enabled.
   ** <p>
   ** A component that is enabled may respond to user input, while a component
   ** that is not enabled cannot respond to  user input. Some components may
   ** alter their visual representation when they are disabled in order to
   ** provide feedback to the user that they cannot take input.
   **
   ** @param  state              <code>true</code> if this component should be
   **                            enabled, <code>false</code> otherwise
   */
  @Override
  public void setEnabled(final boolean state) {
    // ensure inheritance
    super.setEnabled(state);

    // disable its children
    this.canvas.setEnabled(state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onEntry (overridden)
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
  public void onEntry(final TraversableContext context) {
    // ensure inheritance
    super.onEntry(context);

    // On entering the panel, we need to populate all fields with properties
    // from the model object.
    final TreePath           path   = (TreePath)context.getDesignTimeObject(Configurator.SELECTION_PATH);
    final OptionTreeNode     node   = (OptionTreeNode)path.getLastPathComponent();
    final OptionTreeNodeData data   = (OptionTreeNodeData)node.getUserObject();
    final CardLayout         layout = (CardLayout)getLayout();
    if (data instanceof TemplateData) {
      layout.show(this, Configurator.DATA_NODE);
      this.canvas = this.parameter;
    }
    else {
      layout.show(this, Configurator.NULL_NODE);
      this.canvas = this.empty;
    }
    this.canvas.onEntry(context);
  }

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

    this.canvas.onExit(context);
    // ensure inheritance
    super.onExit(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Updates the state of the panel due to changes in the underlying data
   ** model.
   */
  public void refresh() {
    if (this.canvas == this.parameter)
      this.parameter.refresh();
  }
}