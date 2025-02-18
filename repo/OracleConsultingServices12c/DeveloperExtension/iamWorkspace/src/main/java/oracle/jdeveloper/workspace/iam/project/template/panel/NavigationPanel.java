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

    File        :   NavigationPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    NavigationPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template.panel;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JViewport;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import javax.swing.tree.TreePath;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.ide.util.ResourceUtils;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTree;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNode;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreePropagationModel;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateData;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

////////////////////////////////////////////////////////////////////////////////
// class NavigationPanel
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the TemplatePreview dialog for
 ** navigating though the <code>TemplateData</code> model.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 ** <p>
 ** The onExit() method is not ovveriden in this {@link DefaultTraversablePanel}
 ** due to there's nothing to commit.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class NavigationPanel extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8706953967658722668")
  private static final long          serialVersionUID = -1911764015844386360L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected transient OptionTree     view;
  protected transient OptionTreeNode root;
  protected transient JCheckBox      previewAction;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>NavigationPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NavigationPanel() {
    // ensure inheritance
    super();
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
    return this.view;
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

    final TemplateFolder data = (TemplateFolder)context.getDesignTimeObject(Configurator.BUILDFILE_ROOT);
    this.root = createNode(data);

    // initialize UI components
    initializeComponent();
    localizeComponent();
    initializeLayout();

    this.previewAction.setSelected(Boolean.parseBoolean((String)context.getDesignTimeObject(Configurator.PREVIEW_MODE)));
    this.view.setSelectionRow(1);
    context.putDesignTimeObject(Configurator.SELECTION_PATH, this.view.getSelectionPath());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNode
  /**
   ** Creates the build file tree navigation root node for the specified data
   ** provider.
   **
   ** @param  folder             the {@link TemplateFolder) acting as the root of
   **                            a buildfile hierarchy.
   **
   ** @return                    the created {@link OptionTreeNode} ready for
   **                            preview.
   */
  private OptionTreeNode createNode(final TemplateFolder folder) {
    OptionTreeNode node = new OptionTreeNode(folder);
    for (TemplateData dependant : folder.dependant())
      node.add(new OptionTreeNode(dependant));

    for (TemplateFolder hierarchy : folder.hierarchy())
      node.add(createNode(hierarchy));

    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  private void initializeComponent() {
    this.view = new OptionTree(this.root, OptionTreePropagationModel.Mode.DEFAULT);
    this.view.hotspotEnabled(true);
    for (int row = 0; row < this.view.getRowCount(); row++) {
      final TreePath path = this.view.getPathForRow(row);
      this.view.expandPath(path);
      if (((OptionTreeNode)path.getLastPathComponent()).data().hotspotSelected()) {
        this.view.optionTreeSelectionModel().selectDescendant(path);
      }
      else {
        this.view.optionTreeSelectionModel().unselectDescendant(path);
      }
    }
    this.previewAction = new JCheckBox();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  private void localizeComponent() {
    ResourceUtils.resButton(this.previewAction, TemplateBundle.string(TemplateBundle.BUILDFILE_PREVIEW));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  private void initializeLayout() {
    JScrollPane scrollPane = new JScrollPane(this.view);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

    FormLayout      layout     = new FormLayout(
    //   1       2       3    4      5
      "4dlu, min:grow, 4dlu, pref, 4dlu"
    , "pref, 4dlu, top:default:grow, 4dlu"
    //   1           2           3       4
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 1;
    builder.addSeparator(TemplateBundle.string(TemplateBundle.BUILDFILE_NAVIGATOR_TITLE), constraint.xy(2, row));
    builder.add(this.previewAction, constraint.xy(4, row));

    // the 2nd logical row of the layout
    row += 2;
    builder.add(scrollPane, constraint.xyw(2, row, 3));
  }
}