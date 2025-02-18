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

    File        :   ListParameter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ListParameter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import oracle.bali.inspector.editor.ToStringConverter;
import oracle.bali.inspector.editor.EditorComponentInfo;
import oracle.bali.inspector.editor.EditorComponentFactoryRegistry;

import oracle.jdeveloper.workspace.iam.model.DefaultProperty;

////////////////////////////////////////////////////////////////////////////////
// class ListParameter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The remplate file specific implementation of the {@link DefaultProperty}
 ** to meet the requirements of the Template Preview.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ListParameter extends DefaultProperty {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3369741966087379279")
  private static final long          serialVersionUID = 741651237032421897L;

  private static EditorComponentInfo editorIdentifier = new ListEditorComponentInfo();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class ListEditorComponentInfo
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
  protected static class ListEditorComponentInfo extends EditorComponentInfo {

    protected ListEditorComponentInfo() {
      super();
    }

    public String getComponentIdentifier() {
      return ListEditorFactory.IDENTIFIER;
    }

    @Override
    public Object initialValue() {
      return null;
    }

    @Override
    public ToStringConverter editorValueToStringConverter() {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListParameter</code> for a property.
   **
   ** @param  id                 the identifier of the <code>Property</code>.
   ** @param  value              the value of the <code>Property</code> that
   **                            will be initially selected.
   ** @param  choice             the array of possible values.
   */
  public ListParameter(final String id, final String value, final String[] choice) {
    // ensure inheritance
    this(id, id, value, choice);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListParameter</code> for a property.
   **
   ** @param  id                 the identifier of the <code>Property</code>.
   ** @param  label              the human readable label of the
   **                            <code>Property</code>.
   ** @param  value              the value of the <code>Property</code> that
   **                            will be initially selected.
   ** @param  choice             the array of possible values.
   */
  public ListParameter(final String id, final String label, final String value, final String[] choice) {
    // ensure inheritance
    this(id, label, null, value, choice);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListParameter</code> for a property.
   **
   ** @param  id                 the identifier of the <code>Property</code>.
   ** @param  label              the human readable label of the
   **                            <code>Property</code>.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the value of the <code>Property</code> that
   **                            will be initially selected.
   ** @param  choice             the array of possible values.
   */
  public ListParameter(final String id, final String label, final Icon icon, final String value, final String[] choice) {
    // ensure inheritance
    this(id, label, icon, new DefaultComboBoxModel<String>(choice));
    ((ComboBoxModel)value()).setSelectedItem(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListParameter</code> for a property.
   **
   ** @param  id                 the identifier of the <code>Property</code>.
   ** @param  label              the human readable label of the
   **                            <code>Property</code>.
   ** @param  icon               the symbol to prefix the label
   **                            <code>Property</code>.
   ** @param  value              the {@link ComboBoxModel} of the
   **                            <code>Property</code> that that provides the
   **                            possible values and the item initially
   **                            selected.
   */
  public ListParameter(final String id, final String label, final Icon icon, final ComboBoxModel value) {
    // ensure inheritance
    super(id, label, String.class);

    // initialize instance
    icon(icon);
    initializeValue(value);
    setEditorComponentInfo(editorIdentifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInlineEditor (overidden)
  @Override
  public Component getInlineEditor() {
    return EditorComponentFactoryRegistry.instance().createInlineEditor(this);
  }
}