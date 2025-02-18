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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   PropertyPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PropertyPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.root;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.AbstractButton;

import oracle.ide.util.ResourceUtils;

import oracle.javatools.ui.TransparentPanel;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;
import oracle.jdeveloper.connection.iam.model.DirectoryValue;

////////////////////////////////////////////////////////////////////////////////
// class PropertyPanel
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display editable properties of a Directory
 ** Service Root DSE in a form layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class PropertyPanel extends TransparentPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5238967584620057560")
  private static final long serialVersionUID = 6191470256798598429L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final GridBagConstraints constraint = new GridBagConstraints();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Property
  // ~~~~~ ~~~~~~~~~
  /**
   ** A composed control to display a label with an editable component and an
   ** optional button.
   */
  class Property {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String     prompt;
    private final JLabel     label;
    private final JTextField component;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Property
    /**
     ** Constructs a <code>Property</code> cell responsible to visualize the
     ** label and editable component of a Directory Service RootDSE.
     **
     ** @param  label            the label of the editable component.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    Property(final int label) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.prompt    = Bundle.string(label);
      this.label     = new JLabel();
      this.component = new JTextField();
      this.component.setMinimumSize(new Dimension(100,   18));
      this.component.setPreferredSize(new Dimension(400, 18));
    }
    
    public void update(final String text) {
      this.component.setText(text);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PropertyPanel</code> responsible to visualize the
   ** form UI of editable capabilities.
   */
  public PropertyPanel() {
    // ensure inheritance
    super();

    // initialize panel layout
    setLayout(new GridBagLayout());
    this.constraint.gridx      = 0;
    this.constraint.gridy      = 0;
    this.constraint.gridwidth  = 1;
    this.constraint.gridheight = 1;
    this.constraint.anchor     = GridBagConstraints.WEST;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>PropertyPanel</code>
   **
   ** @return                    the validated <code>PropertyPanel</code>.
   **                            <br>
   **                            Possible object <code>PropertyPanel</code>.
   */
  public static PropertyPanel build() {
    return new PropertyPanel();
  }

  protected Property create(final int label, final DirectoryValue.Item value) {
    return create(label, value, null);
  }

  protected Property create(final int label, final DirectoryValue.Item value, final AbstractButton button) {
    final Property property = new Property(label);
    property.component.setText(value.toString());
    addField(property, button);
    return property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addField
  public void addField(final Property property, final AbstractButton button) {
    this.constraint.gridy    += 1;
    this.constraint.gridx     = 0;
    this.constraint.gridwidth = 1;
    this.constraint.weightx   = 0.0D;
    this.constraint.fill      = GridBagConstraints.NONE;
    this.constraint.insets    = new Insets(0, 0, 5, 5);

    if (property.label != null) {
      ResourceUtils.resLabel(property.label, property.component, property.prompt);
      add(property.label, this.constraint);
    }

    this.constraint.gridx    += 1;
    this.constraint.weightx   = 0.300000011920929D;
    this.constraint.fill      = GridBagConstraints.HORIZONTAL;
    this.constraint.insets    = new Insets(0, 0, 5, 40);
    this.constraint.gridwidth = (button == null ? 2 : 1);
    add(property.component, this.constraint);

    if (button != null) {
      this.constraint.gridx    += 1;
      this.constraint.weightx   = 0.0D;
      this.constraint.fill      = GridBagConstraints.NONE;
      this.constraint.insets    = new Insets(0, 5, 5, 0);
      this.constraint.gridwidth = 1;
      add(button, this.constraint);
    }
  }
}