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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   AbstractColumnDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractColumnDescriptor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;

import java.util.Collections;
import java.util.Set;

import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.ColumnDescriptor;

import oracle.hst.foundation.faces.backing.AbstractAttribute;

////////////////////////////////////////////////////////////////////////////////
// class AbstractColumnDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A <code>AbstractColumnDescriptor</code> describes the model for column used
 ** to dynamically create a column in a rich table.
 */
public class AbstractColumnDescriptor extends ColumnDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final AbstractAttribute attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractColumnDescriptor</code> which use the
   ** specified {@link AbstractAttribute} as the model.
   **
   ** @param  attribute          the {@link AbstractAttributeDescriptor}
   **                            providing the model.
   */
  public AbstractColumnDescriptor(final AbstractAttribute attribute) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.attribute = attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (AttributeDescriptor)
  /**
   ** Returns the name of the attribute.
   ** <p>
   ** This differs from {@link #getLabel()} in that, the name is purely a
   ** programmatic name, not a user-displayed name.
   **
   ** @return                    the name of the attribute.
   */
  @Override
  public String getName() {
    return this.attribute.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isReadOnly (AttributeDescriptor)
  /**
   ** Whether the attribute is readOnly.
   ** <p>
   ** A <code>true</code> value indicates that a value cannot be entered for
   ** this attribute.
   **
   ** @return                    <code>true</code> if no value can be entered
   **                            for this attribute; otherwise false.
   */
  @Override
  public boolean isReadOnly() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRequired (AttributeDescriptor)
  /**
   ** Whether the attribute is required or not.
   ** <p>
   ** A <code>true</code> value indicates that a value is required for this
   ** attribute.
   **
   ** @return                    <code>true</code> if a value is required for
   **                            this attribute; otherwise false.
   */
  @Override
  public boolean isRequired() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getComponentType (AttributeDescriptor)
  /**
   ** Returns the component to use to render the value part of the search field.
   ** <p>
   ** When the attribute has no component type associated with it, then the
   ** component to use is determined by its type.
   **
   ** @return                    a ComponentType enum.
   */
  @Override
  public AttributeDescriptor.ComponentType getComponentType() {
    return AttributeDescriptor.ComponentType.inputText;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType (AttributeDescriptor)
  /**
   ** Returns the data type of the attribute. It is used to determine the type
   ** of component and the converter to use when rendering its value.
   **
   ** @return                    a valid {@link Class} instance for the data
   **                            type of the attribute.
   */
  @Override
  public Class<?> getType() {
    return this.attribute.getType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModel (AttributeDescriptor)
  /**
   ** Returns the model object that represents the data for the component.
   ** <ul>
   **   <li>For inputListOfValues/inputComboboxListOfValues, it's a
   **       ListOfValuesModel
   **   <li>For inputNumberSpinbox, it's a NumberRange object
   **   <li>For selectXyz components, it must be List&lt;SelectItem&gt;
   **   <li>Undefined for other types
   ** </ul>
   **
   ** @return                    the model object that represents the data for
   **                            the component.
   */
  @Override
  public Object getModel() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSupportedOperators (AttributeDescriptor)
  /**
   ** Returns a {@link Set} of operators to use for the current attribute.
   ** <p>
   ** This is particularly used when performing a QBE based on a value entered
   ** by user.
   **
   ** @return                    a {@link Set} of operators
   */
  @Override
  public Set<AttributeDescriptor.Operator> getSupportedOperators() {
    return Collections.emptySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLabel (AttributeDescriptor)
  /**
   ** Returns the label or the display name for the attribute.
   ** <p>
   ** The label is may be used as the prompt for a field (in a form layout), or
   ** for a header text (in a table layout).
   **
   ** @return                    a string used as a label or dispaly name for
   **                            the attribute.
   */
  @Override
  public String getLabel() {
    return this.attribute.getLabel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (AttributeDescriptor)
  /**
   ** Returns the description for the attribute.
   ** <p>
   ** This can be used to provide extra information about the attribute. Used in
   ** tooltips.
   **
   ** @return                    the description for the attribute.
   */
  @Override
  public String getDescription() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLength (AttributeDescriptor)
  /**
   ** Returns the size of the text control specified by the number of characters
   ** shown. The length is estimated based on the default font size of the
   ** browser.
   **
   ** @return                    the size of the text control specified by the
   **                            number of characters shown.
   */
  @Override
  public int getLength() {
    return 100;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaximumLength (AttributeDescriptor)
  /**
   ** Returns the maximum number of characters per line that can be entered
   ** into the text control. This includes the characters representing the new
   ** line. If set to <code>0</code> or less, the maximumLength is ignored.
   **
   ** @return                    the maximum number of characters per line that
   **                            can be entered into the text control.
   */
  @Override
  public int getMaximumLength() {
    return 100;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAlign (ColumnDescriptor)
  /**
   ** Returns the alignment for the column.
   ** <p>
   ** Possible values are
   ** <ul>
   **   <li>start
   **   <li>end
   **   <li>center
   **   <li>left
   **   <li>right
   ** </ul>
   **
   ** @return                    the alignment for the column.
   */
  @Override
  public String getAlign() {
    return "start";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getWidth (ColumnDescriptor)
  /**
   ** Returns the width for the column in pixel.
   **
   ** @return                    the width for the column in pixel.
   */
  @Override
  public int getWidth() {
    return 100;
  }
}
