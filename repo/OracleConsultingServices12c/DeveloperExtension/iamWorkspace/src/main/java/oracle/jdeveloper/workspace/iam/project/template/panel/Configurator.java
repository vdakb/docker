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

    File        :   Configurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Configurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template.panel;

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

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

////////////////////////////////////////////////////////////////////////////////
// class Configurator
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the TemplatePreview dialog for
 ** navigating through the <code>TemplateData</code> model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
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
  @SuppressWarnings("compatibility:1361347262271989919")
  private static final long           serialVersionUID = 3075239666237796507L;

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

    // initialize UI components
    initializeComponent();
    initializeLayout();
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
    return this.parameterPanel.defaultFocusComponent();
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
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
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
  public static boolean runDialog(final TemplateFolder root) {
    final Configurator       configurator = new Configurator();
    final Namespace          namepsace    = new Namespace();
    final TraversableContext context      = new TraversableContext(namepsace, TraversableContext.FOCUS_TRAVERSAL);
    context.putDesignTimeObject(BUILDFILE_ROOT, root);
    context.putDesignTimeObject(PREVIEW_MODE,   String.valueOf(true));

    // launches a dialog to preview the generation option
    final TDialogLauncher dialog = new TDialogLauncher(Ide.getMainWindow(), TemplateBundle.string(TemplateBundle.DIALOG_TITLE), configurator, context);
    dialog.setDialogHeader(new DialogHeader(TemplateBundle.string(TemplateBundle.DIALOG_HEADER), TemplateBundle.image(TemplateBundle.DIALOG_IMAGE)));
    return dialog.showDialog();
  }

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

    this.context = context;

    this.navigationPanel.onEntry(this.context);
    this.navigationPanel.view.addTreeSelectionListener(this);
    this.navigationPanel.view.addOptionTreeSelectionListener(this);
    this.navigationPanel.previewAction.addActionListener(this);
    enterTraversable();
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

    exitTraversable();

    // ensure inheritance
    super.onExit(context);
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