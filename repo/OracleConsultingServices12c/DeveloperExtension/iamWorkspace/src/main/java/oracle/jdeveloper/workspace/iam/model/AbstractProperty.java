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

    File        :   AbstractProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AbstractProperty.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import java.beans.PropertyChangeListener;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.awt.Graphics;
import java.awt.Rectangle;

import oracle.bali.inspector.InspectorPropertyEditor;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractProperty
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** An abstract implementation of {@link Property} that handles the registration
 ** and notification of property change listeners.
 ** <p>
 ** Subclasses needs to override the setter and getter methods and call the
 ** firePropertyChange method when a property changes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class AbstractProperty extends    InspectorPropertyEditor
                                       implements Property {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2893818640706933444")
  private static final long    serialVersionUID = -4961879541307124689L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String         id;
  private final String         label;
  private Class<?>             type;

  // Objects are not serialized.
  private transient Object     value;

  private Property             parent;

  private final List<Property> children         = new ArrayList<Property>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractProperty</code> for the specified id.
   **
   ** @param  id                 the string that identifying this
   **                            {@link Property}.
   ** @param  label              the human readable label of the
   **                            <code>Property</code>.
   */
  protected AbstractProperty(final String id, final String label) {
    this(id, label, String.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractProperty</code> for the specified id.
   **
   ** @param  id                 the string that identifying this
   **                            {@link Property}.
   ** @param  label              the human readable label of the
   **                            <code>Property</code>.
   ** @param  type               the type this {@link Property} represetnts.
   */
  protected AbstractProperty(final String id, final String label, final Class<?> type) {
    // ensure inheritance
    super();

    // initialize instance
    this.id    = id;
    this.label = label;
    this.type  = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mandatory
  /**
   ** Returns whether the wrapped property is mandatory or not.
   **
   ** @return                    <code>true</code> if the property is mandatory,
   **                            otherwise <code>false</code>.
   */
  public final boolean mandatory() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPaintable (PropertyEditorFactory2)
  /**
   ** Determines whether this property editor is paintable.
   ** <p>
   ** Subclasses has to override this method to make the editor that they
   ** implementing paintable.
   **
   ** @return                    <code>true</code> if the class will honor the
   **                            paintValue method.
   **                            Returns always <code>false</code>.
   */
  @Override
  public boolean isPaintable() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   paintValue (PropertyEditorFactory2)
  /**
   ** Paint a representation of the value into a given area of screen real
   ** estate. Note that the propertyEditor is responsible for doing its own
   ** clipping so that it fits into the given rectangle.
   ** <p>
   ** If the {@link InspectorPropertyEditor} doesn't honor paint requests (see
   ** {@link #isPaintable()}) this method should be a silent noop.
   ** <p>
   ** The given {@link Graphics} object will have the default font, color, etc
   ** of the parent container. The {@link InspectorPropertyEditor} may change
   ** graphics attributes such as font and color and doesn't need to restore the
   ** old values.
   **
   ** @param  canvas             {@link Graphics} object to paint into.
   ** @param  box                the {@link Rectangle} within graphics object
   **                            into which we should paint.
   */
  @Override
  public void paintValue(final Graphics canvas, final Rectangle box) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone (Property)
  /**
   ** Creates and returns a copy of this instance.
   **
   ** @return                    a clone of this instance.
   */
  @Override
  public Object clone() {
    AbstractProperty clone = null;
    try {
      clone = (AbstractProperty)super.clone();
      return clone;
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (Property)
  /**
   ** Returns the string that identifying this {@link Property}.
   **
   ** @return                    the string that identifying this
   **                            {@link Property}.
   */
  @Override
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label (Property)
  /**
   ** Returns the label that represents the <code>Property</code> in the UI.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is
   ** maintained.
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the
   **                            <code>Property</code>.
   */
  @Override
  public String label() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasInlineEditor (PropertyEditorFactory2)
  /**
   ** Determines whether this property editor supports a custom editor.
   ** <p>
   ** Subclasses has to override this method if they are providing a custom
   ** editor.
   **
   ** @return                    <code>true</code> if the propertyEditor can
   **                            provide a custom editor.
   **                            Returns always <code>false</code>.
   */
  @Override
  public boolean hasInlineEditor() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type (Property)
  /**
   ** Returns the type this property represetnts.
   **
   ** @return                    the type this property represetnts.
   */
  @Override
  public Class<?> type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value (Property)
  /**
   ** Set (or change) the object value.
   **
   ** @param  value              the new object value to be set.
   */
  @Override
  public void value(final Object value) {
    if (value != this.value && (value == null || !value.equals(this.value))) {
      final Object oldValue = this.value;
      this.value = value;
      firePropertyChange(this.id, oldValue, this.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value (Property)
  /**
   ** Returns the property value.
   **
   ** @return                    the value of the property.
   */
  @Override
  public Object value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Sets the {@link Property} instance that takes ownership of this instance.
   **
   ** @param  parent             the {@link Property} instance that takes
   **                            ownership of this instance.
   */
  public void parent(final Property parent) {
    this.parent = parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Returns the parent of this property instance.
   **
   ** @return                    the parent {@link Property} of this instance.
   */
  @Override
  public Property parent() {
    return this.parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   children (Property)
  /**
   ** Returns the children of this property instance.
   **
   ** @return                    the children of this property instance.
   */
  @Override
  public Property[] children() {
    return this.children.toArray(new Property[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (PropertyValueApplier)
  /**
   ** Writes the value of the <code>Property</code> to the given object.
   ** <p>
   ** It uses reflection and looks for a method starting with "set" followed by
   ** the capitalized {@link AbstractProperty} name and with one parameter with
   ** the same type as the {@link AbstractProperty}.
   **
   ** @param  object             the instance the specified <code>value</code>
   **                            value will be written to.
   */
  @Override
  public void apply(final Object object) {
    /*
    try {
      Method method = ExtendedDescriptor.discoverWriteMethod(object.getClass(), id(), type());
      if (method != null) {
        method.invoke(object, new Object[] { value() });
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    */
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue (PropertyEditorFactory2)
  /**
   ** Set (or change) the object that is to be edited.
   ** <p>
   ** Primitive types such as "int" must be wrapped as the corresponding object
   ** type such as "java.lang.Integer".
   **
   ** @param  value              the new target object to be edited.
   **                            <b>Note</b> that this object should not be
   **                            modified by the PropertyEditor, rather the
   **                            {@link Property} should create a new object to
   **                            hold any modified value.
   */
  @Override
  public void setValue(final Object value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue (PropertyEditorFactory2)
  /**
   ** Returns the property value.
   **
   ** @return                    the value of the property.
   **                            Primitive types such as "int" will be wrapped
   **                            as the corresponding object type such as
   **                            "java.lang.Integer".
   **                            Returns always <code>null</code>.
   */
  @Override
  public Object getValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAsText (PropertyEditorFactory2)
  /**
   ** Set the property value by parsing a given String.
   ** <p>
   ** May raise {@link IllegalArgumentException} if either the string is badly
   ** formatted or if this kind of property can't be expressed as text.
   **
   ** @param  text               the string to be parsed.
   */
  @Override
  public void setAsText(final String text)
    throws IllegalArgumentException {

    this.value = text;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAsText (PropertyEditorFactory2)
  /**
   ** Returns the property value as text.
   **
   ** @return                    the property value as a human editable string.
   **                            Returns <code>null</code> if the value can't be
   **                            expressed as an editable string. If a
   **                            non-<code>null</code> value is returned, then
   **                            the {@link InspectorPropertyEditor} should be
   **                            prepared to parse that string back in
   **                            {@link #setAsText(String)}.
   */
  @Override
  public String getAsText() {
    return this.value == null ? null : this.value.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPropertyChangeListener (PropertyEditorFactory2)
  /**
   ** Register a listener for the <code>PropertyChange</code> event.
   ** <p>
   ** When a <code>PropertyEditor</code> changes its value it should fire a
   ** <code>PropertyChange</code> event on all registered
   ** {@link PropertyChangeListener}s, specifying the <code>null</code> value
   ** for the property name and itself as the source.
   **
   ** @param  listener           an object to be invoked when a
   **                            <code>PropertyChange</code> event is fired.
   */
  @Override
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    // ensure inheritance
    super.addPropertyChangeListener(listener);

    Property[] children = children();
    if (children != null)
      for (int i = 0; i < children.length; ++i)
        children[i].addPropertyChangeListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePropertyChangeListener (PropertyEditorFactory2)
  /**
   ** Remove a listener for the <code>PropertyChange</code> event.
   **
   ** @param  listener            the {@link PropertyChangeListener} to be
   **                             removed.
   */
  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    Property[] children = children();
    if (children != null)
      for (int i = 0; i < children.length; ++i)
        children[i].removePropertyChangeListener(listener);
    // ensure inheritance
    super.removePropertyChangeListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return 28
         + ((this.id    != null) ? this.id.hashCode()    : 3)
         + ((this.label != null) ? this.label.hashCode() : 94)
         + ((this.type  != null) ? this.type.hashCode()  : 39);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of the object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" this object. The result is a concise but
   ** informative representation that is easy for a person to read.
   ** <p>
   ** The <code>toString</code> method for class <code>AbstractProperty</code>
   ** returns a string consisting of the instance atttributes of the class of
   ** which the object is an instance.
   **
   ** @return                    a string representation of the object.
   */
  @Override
  public String toString() {
    return "id=" + this.id + ", type=" + this.type + ", label=" + this.label + ", value=" + this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearChildren
  /**
   ** Cleanup the children that this {@link Property} instance owns.
   ** <p>
   ** The associated child will be orphan after this operation.
   */
  public void clearChildren() {
    for (Property property : this.children()) {
      if (property instanceof AbstractProperty)
        ((AbstractProperty)property).parent(null);
    }
    this.children.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProperty
  /**
   ** Add the provided {@link Property} as a child to this instance.
   **
   ** @param  property           the {@link Property} to add as a child to this
   **                            instance.
   */
  public void addProperty(final Property property) {
    this.children.add(property);
    if (property instanceof AbstractProperty)
      ((AbstractProperty)property).parent(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProperties
  /**
   ** Add the provided array of {@link Property} as childs to this instance.
   **
   ** @param  property           the array of {@link Property} to add as childs
   **                            to this instance.
   */
  public void addProperties(final Property[] property) {
    this.addProperties(Arrays.asList(property));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addProperties
  /**
   ** Add the provided {@link Collection} of {@link Property} as childs to
   ** this instance.
   **
   ** @param  node               the {@link Collection} of {@link Property} to
   **                            add as childs to this instance.
   */
  public void addProperties(final Collection<Property> node) {
    this.children.addAll(node);
    for (Property property : this.children) {
      if (property instanceof AbstractProperty)
        ((AbstractProperty)property).parent(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeValue
  /**
   ** Initialize the instance with the provided value.
   ** <p>
   ** Subclasses should override this method to apply their own transformation
   ** to get a concrete representation of the provided value.
   **
   ** @param  value              the value to initialize the instance.
   */
  protected void initializeValue(final Object value) {
    this.value = value;
  }

  private void read(final ObjectInputStream stream)
    throws IOException
    ,      ClassNotFoundException {

    stream.defaultReadObject();
  }
}