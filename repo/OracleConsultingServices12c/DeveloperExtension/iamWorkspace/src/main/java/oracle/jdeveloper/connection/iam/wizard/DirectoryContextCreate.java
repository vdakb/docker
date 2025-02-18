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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryContextCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContextCreate.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
    12.2.1.3.42.60.103 2023-05-23  DSteding    Fixing wrong check on prefix
                                               component that dismiss the dialog
                                               if no prefix is entered.
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.util.Map;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.PropertyChangeListener;

import javax.naming.NamingException;

import java.awt.Component;

import javax.swing.JScrollPane;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.ui.ComponentWithTitlebar;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdeveloper.workspace.iam.swing.LabelFactory;
import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.model.DirectoryName;
import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectorySchema;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.ObjectClassModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.ObjectClassCellEditor;

import oracle.jdeveloper.connection.iam.editor.ods.panel.entry.AttributeCollector;
import oracle.jdeveloper.connection.iam.editor.ods.panel.entry.EntryPrefixSelector;
import oracle.jdeveloper.connection.iam.editor.ods.panel.entry.EntrySuffixSelector;
import oracle.jdeveloper.connection.iam.editor.ods.panel.entry.ObjectClassCollector;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryContextCreate
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the dialog to obtain the information about to create an
 ** entry in a Directory Service.
 ** <p>
 ** Implements the interface for an object that listens to changes at the
 ** object class TableModel to coordinate the interactions between the panes.
 ** <p>
 ** The class is package protected to ensure that only the appropriate command
 ** is able to instantiate the dialog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.103
 ** @since   12.2.1.3.42.60.102
 */
class DirectoryContextCreate extends    DirectoryContextPanel
                             implements TableModelListener
                             ,          PropertyChangeListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:244182229966807813")
  private static final long                 serialVersionUID = -3736953963474163900L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the components to configure entry location
  private EntryPrefixSelector               entryPrefix;
  private EntrySuffixSelector               entrySuffix;

  // the component to collect object classes
  private ObjectClassModel                  objectModel;
  private ObjectClassCollector              objectCollector;

  // the component to collect attributes
  private AttributeModel                    attributeModel;
  private AttributeCollector                attributeCollector;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContextCreate</code> panel that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  DirectoryContextCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tableChanged (TableModelListener)
  /**
   ** The fine grain notification tells listeners the exact range of cells,
   ** rows, or columns that changed.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link TableModelEvent}.
   */
  @Override
  public void tableChanged(final TableModelEvent event) {
    final ObjectClassModel source = (ObjectClassModel)event.getSource();
    final String           value  = (String)source.getValueAt(event.getFirstRow(), 0);
    switch (event.getType()) {
      case TableModelEvent.INSERT :
      case TableModelEvent.UPDATE : updateObjectClass(value);
                                    break;
      case TableModelEvent.DELETE : this.entry.remove(DirectoryAttribute.build(value));
                                    break;
    }
    this.attributeModel.update(this.entry.value());
    // mimic no-selection
    this.objectCollector.getSelectionModel().clearSelection();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyChange (PropertyChangeListener)
  /**
   ** This method gets called when a bound property is changed.
   **
   ** @param  event              a {@link PropertyChangeEvent} object describing
   **                            the event source  and the property that has
   **                            changed.
   **                            <br>
   **                            Allowed object is {@link PropertyChangeEvent}.
   */
  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    // a cell has started/stopped editing
    if ("tableCellEditor".equals(event.getPropertyName())) {
      if (!this.attributeCollector.isEditing())
        // code for editing stopped
        this.entryPrefix.update();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExit (Traversable)
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
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
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

    final String prefix = this.entryPrefix.getText();
    if (StringUtility.empty(prefix)) {
      throw new TraversalException(Bundle.string(Bundle.ENTRY_CREATE_PREFIX_REQUIRED), Bundle.string(Bundle.ENTRY_CREATE_TITLE));
    }
    final String suffix = this.entrySuffix.getText();
    if (StringUtility.empty(suffix)) {
      throw new TraversalException(Bundle.string(Bundle.ENTRY_CREATE_SUFFIX_REQUIRED), Bundle.string(Bundle.ENTRY_CREATE_TITLE));
    }
    try {
      // collect the selected object classes due to they are only available at
      // the selection model
      final DirectoryValue o = DirectoryValue.build(DirectoryAttribute.OBJECTCLASS);
      for (String cursor : this.objectModel.selection())
        o.add(DirectoryValue.item(cursor, DirectoryAttribute.OBJECTCLASS.flag));

      DirectoryEntry entry = DirectoryEntry.build(DirectoryName.build(String.format("%s,%s", prefix, suffix)));
      entry.add(o);
      for (DirectoryValue c : this.entry.value().values()) {
        if (!StringUtility.empty(c.get(0).toString()))
          entry.add(c);
      }
      context.put(ENTRY, entry);
    }
    catch (NamingException e) {
      ExceptionDialog.showExceptionDialog(this, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   vetoableChange (VetoableChangeListener)
  /**
   ** This method gets called when a constrained property is changed.
   **
   ** @param  event            a {@link PropertyChangeEvent} object
   **                          describing the event source and the property
   **                          that has changed.
   **                          <br>
   **                          Allowed object is {@link PropertyChangeEvent}.
   **
   ** @throws PropertyVetoException if the recipient wishes the property
   **                               change to be rolled back.
   */
  @Override
  public void vetoableChange(final PropertyChangeEvent event)
    throws PropertyVetoException {

    // unregister this component as a listener on the table model changes
    this.objectModel.removeTableModelListener(this);
    // unregister this component as a listener on the attribute grid data
    // changes
    this.attributeCollector.removePropertyChangeListener(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialFocus (DirectoryContextPanel)
  /**
   ** Returns the {@link Component} that will get the focus if the dialog window
   ** containing this panel is opened.
   **
   ** @return                    the {@link Component} that will get the focus.
   **                            <br>
   **                            Allowed object is {@link Component}.
   */
  public final Component initialFocus() {
    return this.objectCollector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   */
  @Override
  public void onEntry(final TraversableContext context) {
    // ensure inheritance
    super.onEntry(context);

    // create an entry with the temporary name of the superordinated entry
    try {
      this.entry = DirectoryEntry.build(StringUtility.EMPTY);
    }
    catch (NamingException e) {
      e.printStackTrace();
    }

    // the components to configure entry location
    this.entryPrefix = EntryPrefixSelector.build(20, this.entry);
    this.entrySuffix = EntrySuffixSelector.build(20);
    this.entrySuffix.setText(this.context.name().toString());
    this.entrySuffix.setSelectionEnd(-1);
    this.entrySuffix.setSelectionStart(-1);
    this.entrySuffix.setCaretPosition(this.entrySuffix.getText().length());

    // the empty data model to collect object classes
    this.objectModel = ObjectClassModel.build(null);
    // register this component as a listener on the table model changes
    this.objectModel.addTableModelListener(this);
    // the view component to collect object classes
    this.objectCollector  = ObjectClassCollector.build(this.objectModel, ObjectClassCellEditor.build(this.schema.objectClass().keySet()));

    // the empty data model to collect attributes
    this.attributeModel = AttributeModel.build(this.entry.value(), true);
    // the properties to collect entry attributes
    this.attributeCollector = AttributeCollector.build(this.attributeModel);
    // register this component as a listener on the attribute grid data changes
    this.attributeCollector.addPropertyChangeListener(this);

    // initialize the layout of the panel
    final FormLayout         layout    = new FormLayout(
    //   |     1     |     2     |     3     |       4        |
      "4dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref:grow, 4dlu"
    , "6dlu, pref, 6dlu, pref"
    //   |     1     |     2     |
    );

    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();
    // (1.1) the 1st logical col in the 1st logical row of the layout
    builder.add(
      new ComponentWithTitlebar<JScrollPane>(
        LayoutFactory.scrollableTable(this.objectCollector)
      , LabelFactory.label(Bundle.string(Bundle.SCHEMA_OBJECT_PANEL_TITLE), Bundle.icon(Bundle.ICON_OBJECT_CLASS), this.objectCollector)
      , null
      )
    , constraint.xyw(2, 2, 3)
    );
    // (1.3) the 3rd logical col in the 1st logical row of the layout
    builder.add(
      new ComponentWithTitlebar<JScrollPane>(
        LayoutFactory.scrollableTable(this.attributeCollector)
      , LabelFactory.label(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_PANEL_TITLE), Bundle.icon(Bundle.ICON_ATTRIBUTE_TYPE), this.attributeCollector)
      , null
      )
    , constraint.xy(8, 2)
    );
    // (2.1) the 1st logical col in the 2nd logical row of the layout
    builder.add(LabelFactory.label(Bundle.string(Bundle.ENTRY_PREFIX_LABEL), this.entryPrefix), constraint.xy(2, 4));
    // (2.2) the 2nd logical col in the 2nd logical row of the layout
    builder.add(this.entryPrefix, constraint.xy(4, 4));
    // (2.3) the 3rd logical col in the 2nd logical row of the layout
    builder.add(LabelFactory.label(Bundle.string(Bundle.ENTRY_SUFFIX_LABEL), this.entrySuffix), constraint.xy(6, 4));
    // (2.4) the 4th logical col in the 2nd logical row of the layout
    builder.add(this.entrySuffix, constraint.xy(8, 4));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateObjectClass
  /**
   ** Update the entry to create with the required and optional attributes
   ** declared for the object class the belongs to the specified
   ** <code>name</code>.
   **
   ** @param  name               the name object the object class that extends a
   **                            former declaration.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void updateObjectClass(final String name) {
    final DirectorySchema.ObjectClass clazz = this.schema.objectClass(name);
    // if the object class is not registered nothing has to changed
    if (clazz == null)
      return;

    // evaluate all required attributes that belongs to the object class
    // hierarchy
    final Map<DirectoryAttribute, DirectoryValue> holder = this.entry.value();
    for (DirectoryAttribute type : this.schema.required(clazz)) {
      // verify if the schema attribute isn't the object class
      if (type.objectClass())
        continue;

      // verify if the schema attribute is not already contained in the
      // attribute collection of the entity and can be created
      if (!holder.containsKey(type) && type.createable()) {
        // extend the attribute collection of the entity with an empty value
        holder.put(type, DirectoryValue.build(type));
      }
    }

    // evaluate all optional attributes that belongs to the object class
    // hierarchy
    for (DirectoryAttribute type : this.schema.optional(clazz)) {
      // verify if the schema attribute isn't the object class
      if (type.objectClass())
        continue;

      // verify if the schema attribute is not already contained in the
      // attribute collection of the entity and can be created
      if (!holder.containsKey(type) && type.createable()) {
        // extend the attribute collection of the entity with an empty value
        holder.put(type, DirectoryValue.build(type));
      }
    }
  }
}