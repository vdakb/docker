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

    Copyright © 2021. All Rights reserved

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
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven.panel;

import java.awt.CardLayout;

import javax.swing.tree.TreePath;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNode;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNodeData;

import oracle.jdeveloper.workspace.iam.project.maven.ModelData;

////////////////////////////////////////////////////////////////////////////////
// class PreviewPanel
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the {@link Configurator} dialog for
 ** previewing the POM file with the substituted properties stored in the
 ** {@link ModelData} model.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
class PreviewPanel extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9161832504637741654")
  private static final long                 serialVersionUID = -8780721544658351656L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient DefaultTraversablePanel canvas;
  private transient PreviewData             preview          = new PreviewData();
  private transient PreviewNull             empty            = new PreviewNull();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PreviewPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PreviewPanel() {
    // ensure inheritance
    super();

    // initialize UI components
    setLayout(new CardLayout());
    add(this.preview, Configurator.DATA_NODE);
    add(this.empty,   Configurator.NULL_NODE);
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
    final boolean            visible = Boolean.parseBoolean((String)context.getDesignTimeObject(Configurator.PREVIEW_MODE));
    final TreePath           path   = (TreePath)context.getDesignTimeObject(Configurator.SELECTION_PATH);
    final OptionTreeNode     node   = (OptionTreeNode)path.getLastPathComponent();
    final OptionTreeNodeData data   = (OptionTreeNodeData)node.getUserObject();
    final CardLayout         layout = (CardLayout)getLayout();
    if (visible && data instanceof ModelData) {
      layout.show(this, Configurator.DATA_NODE);
      this.canvas = this.preview;
    }
    else {
      layout.show(this, Configurator.NULL_NODE);
      this.canvas = this.empty;
    }
    this.canvas.onEntry(context);
  }
}