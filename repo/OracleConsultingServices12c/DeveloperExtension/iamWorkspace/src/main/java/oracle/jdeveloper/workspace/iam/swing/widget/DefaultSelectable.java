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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   DefaultSelectable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultSelectable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.text.Collator;

import oracle.jdeveloper.workspace.iam.swing.Selectable;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DefaultSelectable
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The element used by OptionList's ListModel.
 ** <p>
 ** In order to allow hotspots in JList without messing up when list model
 ** changes, we use this class to store the object itself and a boolean to
 ** indicated if the row is selected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class DefaultSelectable implements Comparable
                               ,          Selectable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link Collator} class performs locale-sensitive <code>String</code>
   ** comparison.
   */
  private static final Collator  COLLATOR = Collator.getInstance();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected boolean selected = false;
  protected boolean enabled  = true;

  /** the payload of the tree node */
  protected Object  userObject;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DefaultSelectable</code> with the specified text.
   **
   ** @param  text                the text to be displayed by the elemet.
   */
  public DefaultSelectable(final String text) {
    // ensure inheritance
    super();

    // initialize instance
    this.userObject = text;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates DefaultSelectable with a user object.
   ** <p>
   ** In the case of OptionList, instead of add the object directly to ListModel,
   ** you should wrap it in DefaultSelectable and add DefaultSelectable into
   ** ListModel.
   **
   ** @param  userObject          the user object.
   */
  public DefaultSelectable(final Object userObject) {
    // ensure inheritance
    super();

    // initialize instance
    this.userObject = userObject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userObject
  /**
   ** Sets the value provider of the tree node.
   **
   ** @param  value              the value provider of the tree node.
   **
   ** @return                    this tree node for method chaining purpose.
   */
  public DefaultSelectable userObject(final Object value) {
    this.userObject = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userObject
  /**
   ** Returns the value provider of the data element.
   **
   ** @return                    the value provider of the data element.
   */
  public Object userObject() {
    return this.userObject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   ** <p>
   ** The implementation ensures that
   ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
   ** <code>x</code> and <code>y</code>.
   ** <p>
   ** The implementation also ensures that the relation is transitive:
   ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code> implies
   ** <code>x.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
   ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  other              the object to be compared.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **
   ** @throws ClassCastException if the specified object's type prevents it
   **                             from being compared to this object.
   */
  @Override
  public int compareTo(final Object other) {
    if (!(other instanceof DefaultSelectable))
      throw new ClassCastException("In order to sort, all elements must be an instance of DefaultSelectable.");

    return compareTo((DefaultSelectable)other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected (Selectable)
  /**
   ** Sets it as selected.
   **
   ** @param selected            <code>true</code> if it is selected; otherwise
   **                            <code>false</code>.
   */
  @Override
  public void selected(final boolean selected) {
    this.selected = selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected (Selectable)
  /**
   ** Returns the selected status.
   **
   ** @return                    <code>true</code> if it is selected; otherwise
   **                            <code>false</code>.
   */
  @Override
  public boolean selected() {
    return this.selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggle (Selectable)
  /**
   ** Toggles the selection status.
   */
  @Override
  public void toggle() {
    selected(!this.selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled (Selectable)
  /**
   ** Enable selection change.
   ** <p>
   ** Enabled <code>false</code> doesn't mean selected is <code>false</code>. If
   ** it is selected before, enableed(false) won't make selected become
   ** <code>false</code>. In the other word, enabled won't change the value
   ** of Selected().
   **
   ** @param  enabled            <code>true</code> if it is enabled; otherwise
   **                            <code>false</code>.
   */
  @Override
  public void enabled(final boolean enabled) {
    this.enabled = enabled;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled (Selectable)
  /**
   ** Determines if selection change is allowed.
   **
   ** @return                    <code>true</code> selection change is allowed;
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean enabled() {
    return this.enabled;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   ** <p>
   ** The implementation ensures that
   ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
   ** <code>x</code> and <code>y</code>.
   ** <p>
   ** The implementation also ensures that the relation is transitive:
   ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code> implies
   ** <code>x.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
   ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  other              the object to be compared.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **
   ** @throws ClassCastException if the specified object's type prevents it
   **                             from being compared to this object.
   */
  public int compareTo(final DefaultSelectable other) {
    if (other == null)
      return 1;

    if (this.userObject == null)
      return (other.userObject == null) ? 0 : -1;

    if (other.userObject == null)
      return 1;

    return COLLATOR.compare(this.toString(), other.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Overrides to consider the hash code of the object only. From outside point
   ** of view, this class should behave just like object itself.
   ** <br>
   ** That's why we override hashCode.
   **
   ** @return the hash code.
   */
  @Override
  public int hashCode() {
    return (this.userObject != null ? this.userObject.hashCode() : 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>DefaultSelectable</code> object that
   ** represents the same <code>name</code> and value as this object.
   **
   ** @param other             the object to compare this
   **                          <code>DefaultSelectable</code> against.
   **
   ** @return                  <code>true</code> if the
   **                          <code>DefaultSelectable</code>s are
   **                          equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof DefaultSelectable))
      return false;

    if (userObject() == null && ((DefaultSelectable)other).userObject() == null)
      return true;
    else if (userObject() == null && ((DefaultSelectable)other).userObject() != null)
      return false;
    else if (other == null && userObject() == null)
      return true;
    else
      return userObject().equals(((DefaultSelectable)other).userObject());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Overrides to consider the toString() of the value provider only.
   ** <p>
   ** From outside point of view, this class should behave just like object
   ** itself. That's why we override toString.
   **
   ** @return                    toString() of object.
   */
  @Override
  public String toString() {
    return (this.userObject != null ? this.userObject.toString() : StringUtility.EMPTY);
  }
}