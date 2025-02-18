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

    File        :   OptionTreeNodeData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionTreeNodeData.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import javax.swing.Icon;

import oracle.jdeveloper.workspace.iam.swing.widget.TriStateButton;

import oracle.jdeveloper.workspace.iam.swing.list.OptionListItemData;

////////////////////////////////////////////////////////////////////////////////
// class OptionTreeNodeData
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Data structure for describing a tree cell that might have an triggerable
 ** hotspot.
 ** <p>
 ** This is used to direct the behavior of the {@link OptionTreeCellRenderer}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionTreeNodeData extends OptionListItemData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5984921010549381625")
  private static final long serialVersionUID = 3878826319992952120L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the symbol to display in a tree node as the node is expanded */
  protected transient Icon  iconExpanded;

  /** the symbol to display in a tree node as the node is collapsed */
  protected transient Icon  iconCollapsed;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNodeData</code> with the specified text and
   ** no image.
   **
   ** @param  text                the text to be displayed by the node.
   */
  public OptionTreeNodeData(final String text) {
    // ensure inheritance
    this(text, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNodeData</code> instance with the specified
   ** text, image, and vertical adjustment.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   ** @param  verticalAdjustment  the adjustment of the node contents along the
   **                             Y axis.
   */
  public OptionTreeNodeData(final String text, final Icon icon, final int verticalAdjustment) {
    // ensure inheritance
    this(text, icon, null, null, verticalAdjustment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNodeData</code> instance with the specified
   ** text, image, and vertical adjustment.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   ** @param  expanded            the image to be displayed by the node if its
   **                             expanded.
   ** @param  collapsed           the image to be displayed by the node if its
   **                             collapsed.
   ** @param  verticalAdjustment  the adjustment of the node contents along the
   **                             Y axis.
   */
  public OptionTreeNodeData(final String text, final Icon icon, final Icon expanded, final Icon collapsed, final int verticalAdjustment) {
    // ensure inheritance
    this(text, icon, expanded, collapsed);

    // initailiaze instance attributes
    this.verticalAdjustment = verticalAdjustment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNodeData</code> instance with the specified
   ** text, image.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   */
  public OptionTreeNodeData(final String text, final Icon icon) {
    // ensure inheritance
    this(text, icon, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNodeData</code> instance with the specified
   ** text, image.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   ** @param  expanded            the image to be displayed by the node if its
   **                             expanded.
   ** @param  collapsed           the image to be displayed by the node if its
   **                             collapsed.
   */
  public OptionTreeNodeData(final String text, final Icon icon, final Icon expanded, final Icon collapsed) {
    // ensure inheritance
    super(text, icon);

    // initailiaze instance attributes
    this.iconExpanded  = expanded;
    this.iconCollapsed = collapsed;
    this.hotspotState |= HOTSPOT_VISIBLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNodeData</code> instance with the specified
   ** text, image, and vertical adjustment.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   ** @param  hotspotModel        the initial {@link TriStateButton.Model} of
   **                             the hotspot.
   */
  public OptionTreeNodeData(final String text, final Icon icon, final TriStateButton.Model hotspotModel) {
    // ensure inheritance
    this(text, icon, null, null, hotspotModel);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNodeData</code> instance with the specified
   ** text, image, and vertical adjustment.
   **
   ** @param  text                the text to be displayed by the node.
   ** @param  icon                the image to be displayed by the node.
   ** @param  expanded            the image to be displayed by the node if its
   **                             expanded.
   ** @param  collapsed           the image to be displayed by the node if its
   **                             collapsed.
   ** @param  hotspotModel        the initial {@link TriStateButton.Model} of
   **                             the hotspot.
   */
  public OptionTreeNodeData(final String text, final Icon icon, final Icon expanded, final Icon collapsed, final TriStateButton.Model hotspotModel) {
    // ensure inheritance
    this(text, icon, expanded, collapsed);

    // switch on the capability to display a hotspot
    this.hotspotModel  = hotspotModel;
    this.hotspotState |= HOTSPOT_VISIBLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconExpanded
  /**
   ** Sets the symbol to display in a tree node for state expanded.
   **
   ** @param  value              the symbol to display in a tree node for state
   **                            expanded.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionTreeNodeData iconOpened(final Icon value) {
    this.iconExpanded = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconExpanded
  /**
   ** Returns the symbol to display in a tree node for state expanded.
   **
   ** @return                    the symbol to display in a tree node for state
   **                            expanded.
   */
  public Icon iconExpanded() {
    return this.iconExpanded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconCollapsed
  /**
   ** Sets the symbol to display in a tree node for state collapsed.
   **
   ** @param  value              the symbol to display in a tree node for state
   **                            collapsed.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public OptionTreeNodeData iconCollapsed(final Icon value) {
    this.iconCollapsed = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iconCollapsed
  /**
   ** Returns the symbol to display in a tree node for state collapsed.
   **
   ** @return                    the symbol to display in a tree node for state
   **                            collapsed.
   */
  public Icon iconCollapsed() {
    return this.iconCollapsed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the symbol to display in a tree node regarding the state.
   **
   ** @param  leaf               <code>true</code> if the node is a leaf node;
   **                            otherwise <code>false</code>.
   ** @param  expanded           <code>true</code> if the node is expanded;
   **                            otherwise <code>false</code>.
   **
   ** @return                    the symbol to display in a tree node regarding
   **                            the state.
   */
  public Icon icon(final boolean leaf, final boolean expanded) {
    if (leaf)
      return this.icon;
    if (expanded)
      return (this.iconExpanded == null ? this.icon : this.iconExpanded);
    else
      return (this.iconCollapsed == null ? this.icon : this.iconCollapsed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (overridden)
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
    if (!(other instanceof OptionTreeNodeData))
      throw new ClassCastException("In order to sort, all tree nodes must be an instance of OptionTreeNodeData.");

    return super.compareTo((OptionTreeNodeData)other);
  }
}