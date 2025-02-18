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

    File        :   DefaultProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultProperty.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import javax.swing.Icon;

////////////////////////////////////////////////////////////////////////////////
// class DefaultProperty
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The default implementation of the {@link AbstractProperty}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class DefaultProperty extends AbstractProperty {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3885743048942682891")
  private static final long serialVersionUID = -4124934481068834817L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean           editable = true;
  private String            category;
  private transient Icon    icon;
  private String            hint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DefaultProperty</code> for the specified id.
   **
   ** @param  id                 the string that identifying this
   **                            {@link Property}.
   ** @param  label              the human readable label of the
   **                            <code>Property</code>.
   */
  public DefaultProperty(final String id, final String label) {
    super(id, label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DefaultProperty</code> for the specified id.
   **
   ** @param  id                 the string that identifying this
   **                            {@link Property}.
   ** @param  label              the human readable label of the
   **                            <code>Property</code>.
   ** @param  type               the type this {@link Property} represetnts.
   */
  public DefaultProperty(final String id, final String label, final Class<?> type) {
    // ensure inheritance
    super(id, label, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Sets the {@link Icon} that represents the <code>Property</code> in the UI.
   **
   ** @param  icon               the {@link Icon} of the <code>Property</code>.
   */
  public final void icon(final Icon icon) {
    this.icon = icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hint
  /**
   ** Sets a description of the <code>Property</code> that appears in the
   ** hint text area of the UI.
   **
   ** @param  hint               the human readable description of the
   **                            <code>Property</code>.
   */
  public void hint(final String hint) {
    this.hint = hint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writable
  /**
   ** Sets whether or not this property is writable or not
   **
   ** @param  state              <code>true</code> if this property instance is
   **                            should be writable; otherwise
   **                            <code>false</code>.
   */
  public void writable(final boolean state) {
    this.editable = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Sets the name to categorize this property instance
   **
   ** @param  name               the name of the category that has to be apllied
   **                            on this property instance.
   */
  public void category(final String name) {
    this.category = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value (overridden)
  /**
   ** Set (or change) the object value.
   **
   ** @param  value              the new object value to be set.
   */
  @Override
  public void value(final Object value) {
    // ensure inheritance
    super.value(value);
/*
    if (this.parent() != null) {
      Object parentValue = this.parent().value();
      if (parentValue != null) {
        apply(parentValue);
        this.parent().value(parentValue);
      }
    }
    if (value != null) {
      for (Property property : this.children())
        property.fetch(value);
    }
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon (Property)
  /**
   ** Returns the {@link Icon} that represents the <code>Property</code> in the
   ** UI.
   **
   ** @return                    the {@link Icon} of the <code>Property</code>.
   */
  @Override
  public Icon icon() {
    return this.icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hint (Property)
  /**
   ** Returns a description of the <code>Property</code> that appears in the
   ** hint text area of the UI.
   **
   ** @return                    the human readable description of the
   **                            <code>Property</code>.
   */
  @Override
  public String hint() {
    return this.hint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category (Property)
  /**
   ** Returns the name of the category of this property instance.
   **
   ** @return                    the name of the category of this property
   **                            instance.
   */
  @Override
  public String category() {
    return this.category;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writable (Property)
  /**
   ** Returns whether or not this property is writable or not
   **
   ** @return                    <code>true</code> if this property instance is
   **                            writable; otherwise <code>false</code>.
   */
  @Override
  public boolean writable() {
    return this.editable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch (Property)
  /**
   ** Reads the value of this {@link Property} from the given object.
   ** <p>
   ** It uses reflection and looks for a method starting with "is" or "get"
   ** followed by the capitalized {@link Property} name or a getter method with
   ** the same name as the property id.
   **
   ** @param  object             the instance the specified <code>value</code>
   **                            value read from.
   */
  /*
  @Override
  public void fetch(final Object object) {
    try {
      Method method = ExtendedDescriptor.discoverReadMethod(object.getClass(), id());
      if (method != null) {
        Object value = method.invoke(object,  new Object[0]);
        // avoid updating parent or firing property change
        initializeValue(value);
        if (value != null) {
          for (Property property : this.children()) {
            property.fetch(value);
          }
        }
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  */
  //////////////////////////////////////////////////////////////////////////////
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <p>
   ** This method is supported for the benefit of hashtables such as those
   ** provided by  <code>java.util.Hashtable</code>.
   ** As much as is reasonably practical, the hashCode method defined by class
   ** <code>AbstractProperty</code> does return distinct integers for distinct
   ** objects.
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return super.hashCode()
         + ((this.category != null) ? this.category.hashCode() : 34)
         + ((this.hint     != null) ? this.hint.hashCode()     : 394)
         + Boolean.valueOf(this.editable).hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares two DefaultProperty objects. Two DefaultProperty objects are
   ** equal if they are the same object or if their name, display name, short
   ** description, category, type and editable property are the same.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The property value is not considered in the implementation.
   **
   ** @param other             the object to compare this
   **                          <code>DefaultProperty</code> against.
   **
   ** @return                  <code>true</code> if the
   **                          <code>DefaultProperty</code>s are
   **                          equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other == null || getClass() != other.getClass())
      return false;

    if (other == this)
      return true;

    final DefaultProperty that = (DefaultProperty)other;
    return compare(this.id(),     that.id())
        && compare(this.label(),  that.label())
        && compare(this.hint,     that.hint)
        && compare(this.category, that.category)
        && compare(this.type(),   that.type())
        && this.editable ==       that.editable;
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
   ** The <code>toString</code> method for class <code>DefaultProperty</code>
   ** returns a string consisting of the instance atttributes of the class of
   ** which the object is an instance.
   **
   ** @return                    a string representation of the object.
   */
  @Override
  public String toString() {
    return "category=" + this.category + ", writable=" + this.editable + ", " + super.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  private boolean compare(final Object o1, final Object o2) {
    return (o1 != null) ? o1.equals(o2) : o2 == null;
  }
}