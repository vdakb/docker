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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   BinarySearchTree.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BinarySearchTree.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.List;
import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
// final class BinarySearchTree
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** A binary tree is made of nodes, where each node contains a "left" pointer, a
 ** "right" pointer, and a data element. The "root" pointer points to the
 ** topmost node in the tree. The left and right pointers recursively point to
 ** smaller "subtrees" on either side. A null pointer represents a binary tree
 ** with no elements -- the empty tree. The formal recursive definition is: a
 ** binary tree is either empty (represented by a null pointer), or is made of a
 ** single node, where the left and right pointers (recursive definition ahead)
 ** each point to a binary tree.
 ** <pre>
 **                           +---------+
 **                           |  root   |
 **                           +----+----+
 **                                |
 **                           +----+----+
 **                           |    5    |
 **                           +---------+
 **                           |  o | o  |
 **                           +--+---+--+
 **                              |   |
 **                 +------------+   +------------+
 **                 |                             |
 **            +---------+                   +---------+
 **            |    3    |                   |    9    |
 **            +---------+                   +---------+
 **            |  o | o  |                   |  o | /  |
 **            +--+---+--+                   +---------+
 **               |   |                         |
 **          +----+   +----+               +----+
 **          |             |               |
 **     +----+----+   +----+----+     +----+----+
 **     |    1    |   |    4    |     |    6    |
 **     +---------+   +---------+     +---------+
 **     | /  | /  |   |  / | /  |     |  / | /  |
 **     +---------+   +---------+     +---------+
 ** </pre>
 ** A "binary search tree" (BST) or "ordered binary tree" is a type of binary
 ** tree where the nodes are arranged in order: for each node, all elements in
 ** its left subtree are less-or-equal to the node (&lt;=), and all the elements in
 ** its right subtree are greater than the node (&lt;). The tree shown above is
 ** a binary search tree -- the "root" node is a 5, and its left subtree nodes
 ** (1, 3, 4) are &lt;= 5, and its right subtree nodes (6, 9) are &lt; 5.
 ** Recursively, each of the subtrees must also obey the binary search tree
 ** constraint: in the (1, 3, 4) subtree, the 3 is the root, the 1 &lt;= 3 and 4
 ** &lt; 3. Watch out for the exact wording in the problems -- a "binary search
 ** tree" is different
 ** from a "binary tree".
 ** <p>
 ** The nodes at the bottom edge of the tree have empty subtrees and are called
 ** "leaf" nodes (1, 4, 6) while the others are "internal" nodes (3, 5, 9).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BinarySearchTree<E> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Root node pointer. Will be null for an empty tree. */
  private Node<E> root;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Node
  // ~~~~~ ~~~~
  /**
   ** The dependency tree is built using this nested node class.
   ** <p>
   ** Each node stores one data element, and has left and right sub-tree pointer
   ** which may be null.
   ** <br>
   ** The Node class implements the actual insert and lookup operations for
   ** trees.
   */
  public static class Node<E> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int     order;
    private E       data;
    private Node<E> left;
    private Node<E> right;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Creates a <code>Node</code> that wrappes the specified {@link Object}.
     ** <p>
     ** The <code>Node</code> use the specified <code>order</code> to place the
     ** <code>Node</code> in the search tree.
     **
     ** @param  order            the order to place the node in the search tree.
     ** @param  data             the node to palace at order in the search tree.
     */
    public Node(final int order, final E data) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.order = order;
      this.data  = data;
      this.left  = null;
      this.right = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessors and Mutators
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getOrder
    /**
     ** Returns the order of this <code>Node</code>.
     **
     ** @return                  the data of this <code>Node</code>.
     */
    public final int getOrder() {
      return this.order;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getData
    /**
     ** Returns the data of this <code>Node</code>.
     **
     ** @return                  the data of this <code>Node</code>.
     */
    public final E getData() {
      return this.data;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getLeft
    /**
     ** Returns the left child of this <code>Node</code>.
     **
     ** @return                  the left child of this <code>Node</code>.
     */
    public final Node<E> getLeft() {
      return this.left;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getRight
    /**
     ** Returns the right child of this <code>Node</code>.
     **
     ** @return                  the left right of this <code>Node</code>.
     */
    public final Node<E> getRight() {
      return this.right;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates an empty <code>BinarySearchTree</code>.
   ** <p>
   ** A empty <code>BinarySearchTree</code> has a <code>null</code> root
   ** pointer.
   ** <p>
   ** Default constructor
   */
  public BinarySearchTree() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.root = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of nodes in the tree.
   ** <p>
   ** Uses a recursive helper that recurse down the tree and counts the nodes.
   **
   ** @return                    the number of nodes in the tree.
   */
  public int size() {
    return size(this.root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maxDepth
  /**
   ** Returns the max root-to-leaf depth of the tree.
   ** <p>
   ** Given a binary tree, compute its "maxDepth"; the number of nodes along the
   ** longest path from the root node down to the farthest leaf node.
   ** The maxDepth of the empty tree is 0, the maxDepth of the tree on the first
   ** age is 3.
   ** <p>
   ** Uses a recursive helper that recurs down to find the max depth.
   **
   ** @return                    the max root-to-leaf depth of the tree.
   */
  public int maxDepth() {
    return maxDepth(this.root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maxValue
  /**
   ** Returns the max value in a non-empty binary search tree.
   ** <p>
   ** Given a non-empty binary search tree (an ordered binary tree), return the
   ** maximun data value found in that tree.
   ** <p>
   ** Uses a helper method that iterates to the right to find the max value.
   **
   ** @return                    the max value in a non-empty binary search
   **                            tree.
   */
  public Object maxValue() {
    return maxValue(this.root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minValue
  /**
   ** Returns the min value in a non-empty binary search tree.
   ** <p>
   ** Given a non-empty binary search tree (an ordered binary tree), return the
   ** minimum data value found in that tree.
   ** <p>
   ** Uses a helper method that iterates to the left to find the min value.
   **
   ** @return                    the min value in a non-empty binary search
   **                            tree.
   */
  public Object minValue() {
    return minValue(this.root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns <code>true</code> if the given target is in the binary tree.
   ** <p>
   ** Uses a recursive helper.
   **
   ** @param  order              the order to lookup.
   **
   ** @return                    <code>Node</code> if the given target is in the
   **                            binary tree; otherwise <code>null</code>
   */
  public Node<E> lookup(int order) {
    return lookup(this.root, order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares the receiver to another tree to see if they are structurally
   ** identical.
   ** <p>
   ** Given two binary trees, return <code>true</code> if they are structurally
   ** identical. They are made of nodes with the same values arranged in the
   ** same way.
   ** <p>
   ** Uses a recursive helper.
   **
   ** @param  other              object to be compared for equality with this
   **                            <code>BinarySearchTree</code>.
   **
   ** @return                    <code>true</code> if the trees are structurally
   **                            identical.
   */
  public boolean equals(BinarySearchTree<E> other) {
    return equals(this.root, other.root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insert
  /**
   ** Inserts the given data into the binary tree.
   ** <p>
   ** Uses a recursive helper.
   **
   ** @param  order             the order to insert the node in the search tree.
   ** @param  data              the data to insert at the specified order.
   */
  public void insert(final int order, final E data) {
    this.root = insert(this.root, order, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mirror
  /**
   ** Changes the tree into its mirror image.
   ** <p>
   ** The solution is short, but very recursive. As it happens, this can be
   ** accomplished without changing the root node pointer, so the
   ** return-the-new-root construct is not necessary. Alternately, if you do not
   ** want to change the tree nodes, you may construct and return a new mirror
   ** tree based on the original tree. So the tree...
   ** <pre>
   **        4
   **       / \
   **      2   5
   **     / \
   **    1   3
   ** </pre>
   ** is changed to...
   ** <pre>
   **       4
   **      / \
   **     5   2
   **        / \
   **       3   1
   ** </pre>
   ** <p>
   ** Uses a recursive helper that recurs over the tree, swapping the left/right
   ** pointers.
   */
  public void mirror() {
    mirror(this.root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listPreOrder
  /**
   ** Lists the node values in the "preorder" order.
   ** <p>
   ** Given a binary tree(aka an "ordered binary tree"), list out all of its
   ** root-to-leaf paths, one per line by iterate over the nodes to list them
   ** out unorder. So the tree...
   ** <pre>
   **        4
   **       / \
   **      2   5
   **     / \
   **    1   3
   ** </pre>
   ** Produces the output "4 2 1 3 5". This is known as a "preorder" traversal
   ** of the tree. This problem is a little harder than it looks, since the
   ** "path so far" needs to be communicated between the recursive calls.
   ** <p>
   ** <b>For each node, the strategy is:</b>
   ** <ol>
   **   <li>list the node themselve
   **   <li>list recursive left
   **   <li>list recursive right
   ** </ol>
   ** <p>
   ** Uses a recursive helper to do the traversal.
   ** <br>
   ** <b>Hint</b>: In C, C++, and Java, probably the best solution is to create
   ** a recursive helper function
   ** printPathsRecur(node, String path[], int pathLen), where the path array
   ** communicates the sequence of nodes that led up to the current call.
   ** Alternately, the problem may be solved bottom-up, with each node returning
   ** its list of paths. This strategy works quite nicely in Lisp, since it can
   ** exploit the built in list and mapping primitives.
   **
   ** @return                    the node values in the "preorder" order.
   */
  public List<Node<E>> listPreOrder() {
    List<Node<E>> result = new ArrayList<Node<E>>(size());
    listPreOrder(this.root, result);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listInOrder
  /**
   ** Lists the node values in the "inorder" order.
   ** <p>
   ** Given a binary tree(aka an "ordered binary tree"), list out all of its
   ** root-to-leaf paths, one per line by iterate over the nodes to list them
   ** out in increasing order. So the tree...
   ** <pre>
   **        4
   **       / \
   **      2   5
   **     / \
   **    1   3
   ** </pre>
   ** Produces the output "1 2 3 4 5". This is known as an "inorder" traversal
   ** of the tree. This problem is a little harder than it looks, since the
   ** "path so far" needs to be communicated between the recursive calls.
   ** <p>
   ** <b>For each node, the strategy is:</b>
   ** <ol>
   **   <li>list recursive left
   **   <li>list the node themselve
   **   <li>list recursive right
   ** </ol>
   ** <p>
   ** Uses a recursive helper to do the traversal.
   ** <br>
   ** <b>Hint</b>: In C, C++, and Java, probably the best solution is to create
   ** a recursive helper function
   ** printPathsRecur(node, String path[], int pathLen), where the path array
   ** communicates the sequence of nodes that led up to the current call.
   ** Alternately, the problem may be solved bottom-up, with each node returning
   ** its list of paths. This strategy works quite nicely in Lisp, since it can
   ** exploit the built in list and mapping primitives.
   **
   ** @return                    the node values in the "inorder" order.
   */
  public List<Node<E>> listInOrder() {
    List<Node<E>> result = new ArrayList<Node<E>>(size());
    listInOrder(this.root, result);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listPostOrder
  /**
   ** Lists the node values in the "postorder" order.
   ** <p>
   ** Given a binary tree, list out the nodes of the tree according to a
   ** bottom-up "postorder" traversal -- both subtrees of a node are listed out
   ** completely before the node itself is listed, and each left subtree is
   ** listed before the right subtree. So the tree...
   ** <pre>
   **        4
   **       / \
   **      2   5
   **     / \
   **    1   3
   ** </pre>
   ** Produces the output "1 3 2 5 4". The description is complex, but the code
   ** is simple. This is the sort of  bottom-up traversal that would be used,
   ** for example, to evaluate an expression tree where a node is an operation
   ** like '+' and its subtrees are, recursively, the two subexpressions for
   ** the '+'.
   ** <p>
   ** <b>For each node, the strategy is:</b>
   ** <ol>
   **   <li>list recursive left
   **   <li>list recursive right
   **   <li>list the node themselve
   ** </ol>
   ** <p>
   ** Uses a recursive helper to do the traversal.
   **
   ** @return                    a {@link List} of node values in the
   **                            "postorder" order.
   */
  public List<Node<E>> listPostOrder() {
    List<Node<E>> result = new ArrayList<Node<E>>(size());
    listPostOrder(this.root, result);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Recursive size helper
   ** <p>
   ** Returns the number of nodes in the node.
   **
   ** @param  node               the start point of the operation.
   **
   ** @return                    the number of nodes in the node.
   */
  private int size(final Node<E> node) {
    return (node == null) ? 0 : (size(node.left) + 1 + size(node.right));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maxDepth
  /**
   ** Recursive maxDepth helper
   ** <p>
   ** Returns the max node-to-leaf depth of the tree.
   **
   ** @param  node               the start point of the operation.
   **
   ** @return                    the max node-to-leaf depth of the tree.
   */
  private int maxDepth(final Node<E> node) {
    if (node == null)
      return 0;

    int lDepth = maxDepth(node.left);
    int rDepth = maxDepth(node.right);

    // use the larger + 1
    return (Math.max(lDepth, rDepth) + 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maxValue
  /**
   ** Recursive maxValue helper
   ** <p>
   ** Returns the max value in a non-empty binary search tree.
   **
   ** @param  node              the start point of the operation.
   **
   ** @return                    the max value in a non-empty binary search
   **                            tree.
   */
  private E maxValue(final Node<E> node) {
    Node<E> current = node;
    while (current.right != null)
      current = current.right;

    return current.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minValue
  /**
   ** Recursive minValue helper
   ** <p>
   ** Returns the min value in a non-empty binary search tree.
   **
   ** @param  node              the start point of the operation.
   **
   ** @return                    the min value in a non-empty binary search
   **                            tree.
   */
  private E minValue(final Node<E> node) {
    Node<E> current = node;
    while (current.left != null)
      current = current.left;

    return current.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Recursive lookup helper
   ** <p>
   ** Given a node, recurse down searching for the given data.
   **
   ** @param  node              the start point of the operation.
   ** @param  order             the order to lookup.
   **
   ** @return                    <code>Node</code> if the given target is in the
   **                            binary tree; otherwise <code>null</code>
   */
  private Node<E> lookup(final Node<E> node, final int order) {
    if (node == null)
      return null;

    if (order == node.order) {
      return node;
    }
    else if (order < node.order) {
      return lookup(node.left, order);
    }
    else if (order > node.order) {
      return lookup(node.right, order);
    }
    else
      return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Recursive equals helper
   ** <p>
   ** Recurs down two trees in parallel, checking to see if they are
   ** structurally identical.
   **
   ** @param  a                  the first node to compare.
   ** @param  b                  the second node to compare.
   **
   ** @return                    <code>true</code> if the compared trees are
   **                            structurally identical;
   **                            otherwise <code>false</code>
   */
  private boolean equals(final Node<E> a, final Node<E> b) {
    // 1. both empty -> true
    if (a == null && b == null)
      return true;
    // 2. both non-empty -> compare them
    else if (a != null && b != null)
      return a.order == b.order && equals(a.left, b.left) && equals(a.right, b.right);
    // 3. one empty, one not -> false
    else
      return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insert
  /**
   ** Recursive insert helper
   ** <p>
   ** Given a node pointer, recur down and insert the given data into the tree.
   ** <br>
   ** Returns the new node pointer (the standard way to communicate a changed
   ** pointer back to the caller).
   **
   ** @param  node               the node where to insert the date.
   ** @param  data               the data to insert.
   **
   ** @return                    the create node
   */
  private Node<E> insert(Node<E> node, final int order, final E data) {
    if (node == null)
      node = new Node<E>(order, data);
    else {
      if (order == node.order) {
        node.data  = data;
      }
      else if (order < node.order) {
        node.left  = insert(node.left, order, data);
      }
      else if (order > node.order) {
        node.right = insert(node.right, order, data);
      }
    }
    // in any case, return the new pointer to the caller
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mirror
  /**
   ** Recursive mirror helper
   **
   ** @param  node               the node to mirror.
   */
  private void mirror(final Node<E> node) {
    if (node != null) {
      // do the sub-trees
      mirror(node.left);
      mirror(node.right);

      // swap the left/right pointers
      Node<E> temp = node.left;
      node.left    = node.right;
      node.right   = temp;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listPreOrder
  /**
   ** Recursive listPreOrder helper
   ** <p>
   ** Lists the node values in the "preorder" order.
   **
   ** @param  node              the start point of the operation.
   ** @param  result            the {@link List} the will contain all nodes
   **                           after the operation is completed.
   */
  private void listPreOrder(final Node<E> node, final List<Node<E>> result) {
    if (node == null)
      return;

    // node itself, left, right
    result.add(node);
    listPreOrder(node.left, result);
    listPreOrder(node.right, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listInOrder
  /**
   ** Recursive listInOrder helper
   ** <p>
   ** Lists the node values in the "inorder" order.
   **
   ** @param  node              the start point of the operation.
   ** @param  result            the {@link List} the will contain all nodes
   **                           after the operation is completed.
   */
  private void listInOrder(final Node<E> node, final List<Node<E>> result) {
    if (node == null)
      return;

    // left, node itself, right
    listInOrder(node.left, result);
    result.add(node);
    listInOrder(node.right, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listPostOrder
  /**
   ** Recursive listPostOrder helper
   ** <p>
   ** Lists the node values in the "postorder" order.
   **
   ** @param  node              the start point of the operation.
   ** @param  result            the {@link List} the will contain all nodes
   **                           after the operation is completed.
   */
  private void listPostOrder(final Node<E> node, final List<Node<E>> result) {
    if (node == null)
      return;

    // left, right, node itself
    listPostOrder(node.left, result);
    listPostOrder(node.right, result);
    result.add(node);
  }
}