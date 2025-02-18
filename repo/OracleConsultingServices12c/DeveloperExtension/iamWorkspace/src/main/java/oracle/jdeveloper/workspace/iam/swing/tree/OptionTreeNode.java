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

    File        :   OptionTreeNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OptionTreeNode.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.tree;

import java.util.Iterator;
import java.util.Collections;

import javax.swing.tree.DefaultMutableTreeNode;

////////////////////////////////////////////////////////////////////////////////
// class OptionTreeNode
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Extension of {@link DefaultMutableTreeNode} that provides support for the
 ** {@link OptionTreeNodeData} data structure.
 ** <p>
 ** This is used to direct the behavior of the {@link OptionTreeCellRenderer}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionTreeNode extends    DefaultMutableTreeNode
                            implements Comparable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4142141364968959713")
  private static final long              serialVersionUID = 4966391321842437151L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected transient OptionTreeNodeData data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNode</code> that has no parent and no
   ** children, but which allows children.
   */
  public OptionTreeNode() {
    // ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNode</code> that has no parent and no
   ** children, but which allows children, and initializes it with the specified
   ** data object.
   **
   ** @param  data               a {@link OptionTreeNodeData} object provided by
   **                            the user that constitutes the node's data.
   */
  public OptionTreeNode(final OptionTreeNodeData data) {
    // ensure inheritance
    this(data, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OptionTreeNode</code> that has no parent and no
   ** children, initializes it with the specified data object, and that allows
   ** children only if specified.
   **
   ** @param  data               a {@link OptionTreeNodeData} object provided by
   **                            the user that constitutes the node's data.
   ** @param  allowsChildren     if <code>true</code>, the node is allowed to
   **                            have child nodes; otherwise, it is always a
   **                            leaf node.
   */
  public OptionTreeNode(final OptionTreeNodeData data, final  boolean allowsChildren) {
    // ensure inheritance
    super(data, allowsChildren);

    // initialze instance attributes (only to avoid casting everytime)
    this.data = data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data
  /**
   ** Sets the data model for the node to use.
   **
   ** @param  data               the data model for the node to use.
   */
  public void data(final OptionTreeNodeData data) {
    this.data = data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data
  /**
   ** Returns the data model for the node.
   **
   ** @return                    the data model for the node.
   */
  public OptionTreeNodeData data() {
    return this.data;
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
    if (!(other instanceof OptionTreeNode))
      throw new ClassCastException("In order to sort, all tree nodes must be an instance of OptionTreeNode.");

    return compareTo((OptionTreeNode)other);
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
  public int compareTo(final OptionTreeNode other) {
    if (other == null)
      return 1;

    if (this.data == null)
      return (other.data == null) ? 0 : -1;

    if (other.data == null)
      return 1;

    return this.data.compareTo(other.data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Convenience method where a new child node is added by specifying its data
   ** model. A new instance of <code>OptionTreeNode</code> is created and added.
   **
   ** @param  data               the data for the node to add as a child of this
   **                            node.
   **
   ** @return                    the created <code>OptionTreeNode</code> created
   **                            from the given data and added as a child to this
   */
  public OptionTreeNode add(final OptionTreeNodeData data) {
    final OptionTreeNode node = new OptionTreeNode(data);
    add(node);
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortChildren
  /**
   ** Sorts this node's children without recursion.
   */
  public void sortChildren() {
    sortChildren(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortDescendents
  /**
   ** Sorts all levels of the subtree represented by this
   ** <code>OptionTreeNode</code>.
   */
  public void sortDescendents() {
    sortChildren(2147483647);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortChildren
  /**
   ** Sorts this node's children recursivly.
   ** <p>
   ** If the indicated recursion level is non-negative, this method sorts the
   ** <code>OptionTreeNode</code>'s children and then recurses the number of
   ** levels indicated to sort their children.
   **
   ** @param  recursionLevel     the level of recursion
   */
  public void sortChildren(int recursionLevel) {
    if ((recursionLevel < 0) || (this.children == null))
      return;

    Collections.sort(this.children);
    if (recursionLevel-- > 0) {
      Iterator i = this.children.iterator();
      while (i.hasNext()) {
        Object next = i.next();
        ((OptionTreeNode)next).sortChildren(recursionLevel);
      }
    }
  }
}