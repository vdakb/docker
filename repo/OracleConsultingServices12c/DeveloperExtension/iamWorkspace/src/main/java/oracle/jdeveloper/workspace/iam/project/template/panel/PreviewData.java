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

    File        :   PreviewData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PreviewData.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template.panel;

import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import javax.swing.tree.TreePath;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.editor.language.LanguageModule;

import oracle.javatools.editor.BasicEditorPane;
import oracle.javatools.editor.EditorProperties;

import oracle.javatools.editor.gutter.LineGutterPlugin;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNode;
import oracle.jdeveloper.workspace.iam.swing.tree.OptionTreeNodeData;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateData;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFile;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateGenerator;

////////////////////////////////////////////////////////////////////////////////
// class PreviewData
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the {@link Configurator} dialog for
 ** previewing the XML file with the substituted properties stored in the
 ** {@link TemplateData} model.
 ** <p>
 ** The panel class is kept package-private and final. Unless there is a good
 ** reason to open it up.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class PreviewData extends DefaultTraversablePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:32686391773932036")
  private static final long           serialVersionUID = -4191949893662130119L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected transient JPanel          gutter;
  protected transient BasicEditorPane view;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PreviewData</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PreviewData() {
    // ensure inheritance
    super();

    // initialize UI components
    initializeComponent();
    initializeLayout();
    localizeComponent();
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
    OptionTreeNodeData data = template(context);
    try {
      if (data instanceof TemplateStream)
        this.view.setText(TemplateGenerator.preview((TemplateStream)data));
      else
        this.view.setText(TemplateGenerator.preview((TemplateFile)data));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
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

    // ensure inheritance
    super.onExit(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
  /**
   ** This gets a defensive copy of the preferences being used.
   **
   ** @param  context            the data object where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI or store the
   **                            changes made in the UI so that the changes can
   **                            be accessed by other <code>Traversable</code>s.
   **
   ** @return                    the copy of the preferences being used by this
   **                            <code>Traversable</code>.
   */
  protected TemplateData template(final TraversableContext context) {
    // This gets a defensive copy of the data container being used.
    TreePath       path = (TreePath)context.getDesignTimeObject(Configurator.SELECTION_PATH);
    OptionTreeNode node = (OptionTreeNode)path.getLastPathComponent();
    return (TemplateData)node.getUserObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  private void initializeComponent() {
    final EditorProperties properties = EditorProperties.getProperties();
    properties.putBooleanProperty(EditorProperties.PROPERTY_SHOW_LINE_NUMBERS,           true);
    properties.putBooleanProperty(EditorProperties.PROPERTY_CODE_FOLDING_MARGIN_VISIBLE, true);
    properties.putBooleanProperty(EditorProperties.PROPERTY_CODE_FOLDING_ENABLED,        true);
    properties.putIntegerProperty(EditorProperties.PROPERTY_EDITOR_WIDTH,                900);

    // create the view to display the document
    this.view = new BasicEditorPane();
    this.view.setLanguageSupport(LanguageModule.FILETYPE_XML);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  private void localizeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  private void initializeLayout() {
    FormLayout      layout     = new FormLayout(
    //   1       2       3
      "4dlu, min:grow, 4dlu"
    , "4dlu, pref, 6dlu, top:default:grow, 4dlu"
    //   1     2      3        4            5
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    final LineGutterPlugin lineGutter = new LineGutterPlugin();
    this.view.installPlugin(lineGutter);

    this.gutter = new JPanel(new BorderLayout());
    this.gutter.add(lineGutter, BorderLayout.CENTER);
    // add the gutter for the folding to the layout of the gutter panel
    final Component foldingGutter = (Component)this.view.getProperty("code-folding-margin");
    if (foldingGutter != null)
      this.gutter.add(foldingGutter, BorderLayout.EAST);

    JScrollPane scrollPane = new JScrollPane(this.view);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
    scrollPane.setRowHeaderView(this.gutter);

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(TemplateBundle.string(TemplateBundle.BUILDFILE_PREVIEW_TITLE), constraint.xy(2, row));

    // the 2nd logical row of the layout
    row += 2;
    builder.add(scrollPane, constraint.xy(2, row));
  }
}