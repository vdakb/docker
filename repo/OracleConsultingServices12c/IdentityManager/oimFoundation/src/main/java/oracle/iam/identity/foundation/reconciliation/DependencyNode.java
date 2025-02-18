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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Scheduler Facilities

    File        :   DependencyNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DependencyNode.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.reconciliation;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import java.util.LinkedList;

////////////////////////////////////////////////////////////////////////////////
// class DependencyNode
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** An class for any kind of association in which one side is somehow dependant
 ** on another.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DependencyNode {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the identifier of the node */
  private String                     id;

  /** the handler instance */
  private DependencyHandler          handler;

  /** the predecessor where this instance is waiting for to finish */
  private DependencyNode             predecessor;

  /** the dependencies that are waiting for resolution of this node */
  private LinkedList<DependencyNode> dependency;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>DependencyNode</code> with the specified id.
   **
   ** @param  handler            the {@link DependencyHandler} that this
   **                            <code>DependencyNode</code> is
   **                            instantiating.
   ** @param  id                 the id of this <code>DependencyNode</code>
   */
  public DependencyNode(final DependencyHandler handler, final String id) {
    this.handler = handler;
    this.id      = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of this <code>DependencyNode</code>.
   **
   ** @return                    the identifier of this
   **                            <code>DependencyNode</code>.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of dependend <code>DependencyNode</code>s.
   ** <br>
   ** Uses a recursive helper that recurse down the tree and counts the nodes.
   **
   ** @return                    the number of dependend
   **                            <code>DependencyNode</code>s in the tree.
   */
  public int size() {
    return size(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependencyIterator
  /**
   ** Returns an {@link ListIterator} over all dependend
   ** <code>CSVDependencyNode</code>s.
   ** <br>
   ** This is returns a the origin, please avoid concurrency conflicts.
   **
   ** @return                    an {@link Iterator} over all dependend
   **                            <code>CSVDependencyNode</code>s.
   */
  public final ListIterator<DependencyNode> dependencyIterator() {
    return (this.dependency != null) ? this.dependency.listIterator() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this string to the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>CSVDependencyNode</code> object that
   ** represents the <code>id</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>CSVDependencyNode</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>CSVDependencyNode</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    return this.id.equals(other.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code for this <code>CSVDependencyNode</code>.
   **
   ** @return                    a hash code for this
   **                            <code>CSVDependencyNode</code>.
   */
  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  @Override
  public String toString() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears the dependencies of this <code>DependencyNode</code>.
   ** <p>
   ** Uses a recursive clear helper.
   */
  public void clear() {
    clear(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns <code>DependencyNode</code> if the given id is in the list of
   ** waiting dependencies.
   ** <p>
   ** Uses a recursive lookup helper.
   **
   **
   ** @param  id                the id to lookup.
   **
   ** @return                    <code>DependencyNode</code> if the given id
   **                            is in the waiting dependencies; otherwise
   **                            <code>null</code>.
   */
  public DependencyNode lookup(final String id) {
    return lookup(this, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   export
  /**
   ** Export the <code>DependencyNode</code> and the dependencies to a list.
   ** <p>
   ** Uses a recursive export helper.
   **
   ** @param  export             the container to recieve the
   **                            <code>DependencyNode</code> and the
   **                            dependencies.
   */
  public void export(final List<DependencyNode> export) {
    export(this, export);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insert
  /**
   ** Inserts the given data into the <code>DependencyNode</code>.
   ** <p>
   ** Uses a recursive insert helper.
   **
   ** @param  handler            the {@link DependencyHandler} that this
   **                            <code>DependencyNode</code> is
   **                            inserting.
   ** @param  id                 the id to insert.
   */
  public void insert(final DependencyHandler handler, final String id) {
    add(insert(handler, this, id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an existing <code>DependencyNode</code> to the dependency list of
   ** this instance.
   **
   ** @param  existing           the predecessor to add.
   */
  public void add(final DependencyNode existing) {
    if (this.dependency == null)
      this.dependency = new LinkedList<DependencyNode>();

    if (existing.predecessor != null) {
      if (!existing.predecessor.dependency.remove(existing)) {
//        String[] parameter = { existing.predecessor.id, id };
//        this.handler.error(method, TaskBundle.format(TaskError.DEPENDENCY_PARENT_CONFLICT, parameter));
        ;
      }
    }
    existing.predecessor = this;
    this.dependency.add(existing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** The recursive clear helper.
   ** <p>
   ** Clears the dependencies of the specified <code>DependencyNode</code>.
    */
  private static void clear(final DependencyNode node) {
    if (node.dependency != null) {
      Iterator<DependencyNode> i = node.dependencyIterator();
      while (i.hasNext())
        clear(i.next());

      node.dependency.clear();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** The recursive size helper
   ** <p>
   ** Returns the number of nodes in the node.
   **
   ** @param  node               the start point of the operation.
   **
   ** @return                    the number of nodes in the node.
   */
  private static int size(final DependencyNode node) {
    int size = 0;
    if (node.dependency != null) {
      Iterator<DependencyNode> i = node.dependencyIterator();
      while (i.hasNext())
        size += size(i.next());
      size += node.dependency.size();
    }
    return size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** The recursive lookup helper.
   ** <p>
   ** Returns <code>DependencyNode</code> if the given id is in the list of
   ** waiting dependencies.
   **
   ** @param  id                the id to lookup.
   **
   ** @return                    <code>true</code> if the given target is in the
   **                            dependenciese; otherwise <code>false</code>
   */
  private static DependencyNode lookup(final DependencyNode node, final String id) {
    if (node.id.equals(id))
      return node;

    if (node.dependency == null || node.dependency.size() == 0)
      return null;

    DependencyNode instance = null;
    Iterator<DependencyNode> i = node.dependencyIterator();
    while (i.hasNext()) {
      instance = lookup(i.next(), id);
      if (instance != null)
        break;
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   export
  /**
   ** The recursive export helper.
   ** <p>
   ** Export the predecessor and the dependencies to a list.
   **
   ** @param  export             the container to recieve the
   **                            <code>CSVDependencyNode</code> and the
   **                            dependencies.
   */
  private static void export(final DependencyNode node, final List<DependencyNode> export) {
    export.add(node);
    if (node.dependency != null) {
      Iterator<DependencyNode> i = node.dependencyIterator();
      while (i.hasNext())
        export(i.next(), export);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insert
  /**
   ** The recursive insert helper
   ** <p>
   ** Given a node pointer, recur down and insert the given data into the tree.
   ** <br>
   ** Returns the new node pointer (the standard way to communicate a changed
   ** pointer back to the caller).
   **
   ** @param  handler            the {@link DependencyHandler} that the
   **                            <code>DependencyNode</code> is
   **                            instantiating.
   ** @param  node               the node where to insert the date.
   ** @param  id                 the id to insert.
   **
   ** @return                    the create node
   */
  private static DependencyNode insert(final DependencyHandler handler, DependencyNode node, final String id) {
    if (node == null)
      node = new DependencyNode(handler, id);
    else
      node = insert(handler, lookup(node, id), id);

    // in any case, return the new pointer to the caller
    return node;
  }
}