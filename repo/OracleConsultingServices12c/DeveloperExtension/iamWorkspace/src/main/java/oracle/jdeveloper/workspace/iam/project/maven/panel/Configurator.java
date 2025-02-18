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

    File        :   Configurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Configurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven.panel;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;
import javax.swing.BorderFactory;

import javax.swing.tree.TreePath;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import oracle.bali.ewt.dialog.DialogHeader;

import oracle.ide.Ide;

import oracle.ide.util.Namespace;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.ide.panels.TDialogLauncher;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNode;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeSelectionEvent;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeSelectionListener;

import oracle.jdeveloper.workspace.iam.project.maven.ModelBundle;
import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;

////////////////////////////////////////////////////////////////////////////////
// class Configurator
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the ModelPreview dialog for navigating
 ** through the <code>ModelData</code> model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class Configurator extends    DefaultTraversablePanel
                          implements ActionListener
                          ,          TreeSelectionListener
                          ,          OptionTreeSelectionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String          BUILDFILE_ROOT   = "root";
  public static final String          SELECTION_PATH   = "path";
  public static final String          PREVIEW_MODE     = "preview";

  public static final String          DATA_NODE        = "DATA";
  public static final String          NULL_NODE        = "NULL";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5539867372442780011")
  private static final long           serialVersionUID = 3961841696091831083L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient TraversableContext context         = null;

  private final NavigationPanel        navigationPanel = new NavigationPanel();
  private final ParameterPanel         parameterPanel  = new ParameterPanel();
  private final PreviewPanel           previewPanel    = new PreviewPanel();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Configurator</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Configurator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   actionPerformed (ActionListener)
  /**
   ** Invoked when an action occurs.
   **
   ** @param  event              a {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  @Override
  public void actionPerformed(final ActionEvent event) {
    if (event.getSource() == this.navigationPanel.previewAction) {
      this.context.putDesignTimeObject(PREVIEW_MODE, String.valueOf(this.navigationPanel.previewAction.isSelected()));
      this.previewPanel.onEntry(this.context);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueChanged (TreeSelectionListener)
  /**
   ** Called whenever the value selection changes of the tree component.
   **
   ** @param  event              the @link TreeSelectionEvent} object that
   **                            characterizes the change.
   **                            <br>
   **                            Allowed object is {@link TreeSelectionEvent}.
   */
  @Override
  public void valueChanged(final TreeSelectionEvent event) {
    final TreePath contextPath = (TreePath)this.context.getDesignTimeObject(SELECTION_PATH);
    if (contextPath != null) {
      try {
        exitTraversable();
      }
      catch (TraversalException e) {
        this.navigationPanel.view.removeTreeSelectionListener(this);
        try {
          this.navigationPanel.view.expandPath(contextPath);
          this.navigationPanel.view.setSelectionPath(contextPath);
        }
        finally {
          this.navigationPanel.view.addTreeSelectionListener(this);
        }
        return;
      }
    }
    this.context.putDesignTimeObject(SELECTION_PATH, event.getNewLeadSelectionPath());
    enterTraversable();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueChanged (OptionTreeSelectionListener)
  /**
   ** Called whenever the hotsport value selection changes of the tree
   ** component.
   **
   ** @param  event              the @link OptionTreeSelectionEvent} object
   **                            that characterizes the change.
   **                            <br>
   **                            Allowed object is
   **                            {@link OptionTreeSelectionEvent}.
   */
  @Override
  public void valueChanged(final OptionTreeSelectionEvent event) {
    final TreePath eventPath = event.path();
    if (event.selected())
      ((OptionTreeNode)eventPath.getLastPathComponent()).data().selectHotspot();
    else
      ((OptionTreeNode)eventPath.getLastPathComponent()).data().unselectHotspot();
    this.parameterPanel.refresh();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runDialog
  /**
   ** Launches a dialog whose content area is this
   ** {@link DefaultTraversablePanel}.
   **
   ** @param  root               the build file tree navigation root node for
   **                            the project.
   **
   ** @return                    <code>true</code> if <i>OK</i> was selected;
   **                            <code>false</code> if <i>Cancel</i> was
   **                            selected.
   */
  public static boolean runDialog(final ModelFolder root) {
    final Configurator       configurator = new Configurator();
    final Namespace          namepsace    = new Namespace();
    final TraversableContext context      = new TraversableContext(namepsace, TraversableContext.FOCUS_TRAVERSAL);
    context.putDesignTimeObject(BUILDFILE_ROOT, root);
    context.putDesignTimeObject(PREVIEW_MODE,   String.valueOf(true));

    // launches a dialog to preview the generation option
    final TDialogLauncher dialog = new TDialogLauncher(Ide.getMainWindow(), ModelBundle.string(ModelBundle.DIALOG_TITLE), configurator, context);
    dialog.setDialogHeader(new DialogHeader(ModelBundle.string(ModelBundle.DIALOG_HEADER), ModelBundle.image(ModelBundle.DIALOG_IMAGE)));
    return dialog.showDialog();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  private void initializeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  private void initializeLayout() {
    this.navigationPanel.setMinimumSize(new Dimension(200, 100));
    this.previewPanel.setMinimumSize(new Dimension(300, 400));
    this.parameterPanel.setMinimumSize(new Dimension(200, 300));

    JSplitPane content = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.navigationPanel, this.parameterPanel);
    content.setDividerSize(7);
    content.setBorder(BorderFactory.createEmptyBorder());
    content.setContinuousLayout(true);
    content.setDividerLocation(0.5D);

    JSplitPane navigation = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.previewPanel, content);
    navigation.setDividerSize(7);
    navigation.setBorder(BorderFactory.createEmptyBorder());
    navigation.setContinuousLayout(true);
    navigation.setDividerLocation(0.5D);

    setLayout(new BorderLayout(0, 0));
    add(navigation, BorderLayout.CENTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterTraversable
  /**
   ** Called in case a new node was selected.
   */
  private void enterTraversable() {
    this.parameterPanel.onEntry(this.context);
    this.previewPanel.onEntry(this.context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exitTraversable
  /**
   ** Called in case a node was deselected.
   */
  private void exitTraversable()
    throws TraversalException {

    try {
      this.parameterPanel.onExit(this.context);
      this.navigationPanel.onExit(this.context);
    }
    catch (TraversalException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}