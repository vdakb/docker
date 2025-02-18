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

    File        :   DependencyCollector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DependencyCollector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.reconciliation;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedHashMap;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class DependencyCollector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** A <code>DependencyCollector</code> gathers the reconciliation event
 ** dependencies that are in a race condition.
 ** <p>
 ** A Race Condition occurs...
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DependencyCollector implements DependencyHandler {

  /**
   ** the {@link LinkedHashMap} where are all known {@link DependencyNode}s
   ** will be srored.
   */
  private final LinkedHashMap<String, DependencyNode> predecessorMap = new LinkedHashMap<String, DependencyNode>();

  /**
   ** the {@link LinkedHashMap} where are all known {@link DependencyNode}s
   ** that has a dependency to a predecessor which is not yet seen.
   */
  private final LinkedHashMap<String, DependencyNode> unresolvedMap  = new LinkedHashMap<String, DependencyNode>();

  /** the {@link Logger} for logging */
  private final String                                shortName;
  private final Logger                                logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DependencyCollector</code> with the specified as as the
   ** provider for resources and logging.
   **
   ** @param  loggable           the {@link Loggable} providing access to
   **                            logging.
   */
  public DependencyCollector(final Loggable loggable) {
    // ensure inheritance
    super();

    this.shortName = ClassUtility.shortName(this);
    this.logger    = loggable == null ? null : loggable.logger();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear (DependencyHandler)
  /**
   ** Close the dependency handler.
   ** <br>
   ** The handler will no longer be able to recieve data
   */
  @Override
  public void clear() {
    Iterator<String> node = unresolvedKeyIterator();
    while (node.hasNext()) {
      DependencyNode next = this.unresolvedMap.get(node.next());
      next.clear();
    }
    node = predecessorKeyIterator();
    while (node.hasNext()) {
      DependencyNode next = this.predecessorMap.get(node.next());
      next.clear();
    }

    // clear also the container that hold resolved and unresolved entries
    this.predecessorMap.clear();
    this.unresolvedMap.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasResolveables (DependencyHandler)
  /**
   ** Returns <code>true</code> if this <code>DependencyCollector</code> has
   ** resolved dependency entries.
   **
   ** @return                    <code>true</code> if this
   **                            <code>DependencyCollector</code> has
   **                            resolved dependency entries; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean hasResolveables() {
    return this.predecessorMap.size() > 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasUnresolveables (DependencyHandler)
  /**
   ** Returns <code>true</code> if this <code>DependencyCollector</code> has
   ** unresolved dependency entries.
   **
   ** @return                    <code>true</code> if this
   **                            <code>DependencyCollector</code> has
   **                            unresolved dependency entries;
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean hasUnresolveables() {
    return this.unresolvedMap.size() > 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsResolved (DependencyHandler)
  /**
   ** Returns <code>true</code> if the given id is in the list of resolved
   ** waiting dependencies.
   **
   ** @param  id                the id to lookup.
   **
   ** @return                    <code>true</code> if the given id is in the
   **                            resolved waiting dependencies;
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean containsResolved(final String id) {
    return (lookup(id) != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsUnresolved (DependencyHandler)
  /**
   ** Returns <code>true</code> if the given id is in the list of unresovled
   ** waiting dependencies.
   **
   ** @param  id                the id to lookup.
   **
   ** @return                    <code>true</code> if the given id is in the
   **                            unresovled waiting dependencies;
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean containsUnresolved(final String id) {
    DependencyNode   instance = null;
    Iterator<String> i        = unresolvedKeyIterator();
    while (i.hasNext()) {
      instance = this.unresolvedMap.get(i.next()).lookup(id);
      if (instance != null)
        break;
    }
    return (instance != null);
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (DependencyHandler)
  /**
   ** Returns {@link DependencyNode} if the given id is in the list of waiting
   ** dependencies.
   ** <p>
   ** Uses a recursive lookup helper.
   **
   ** @param  id                 the id to lookup.
   **
   ** @return                    <code>CSVDependencyNode</code> if the given id
   **                            is in the waiting dependencies; otherwise
   **                            <code>null</code>.
   */
  public DependencyNode lookup(final String id) {
    DependencyNode   instance = null;
    Iterator<String> i        = predecessorKeyIterator();
    while (i.hasNext()) {
      instance = this.predecessorMap.get(i.next()).lookup(id);
      if (instance != null)
        break;
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolved (DependencyHandler)
  /**
   ** Returns an {@link LinkedList} with all resolved dependencies.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link LinkedList} over all resolved
   **                            predecessors.
   */
  @Override
  public LinkedList<DependencyNode> resolved() {
    LinkedList<DependencyNode> list = null;
    if (hasResolveables()) {
      list = new LinkedList<DependencyNode>();
      Iterator<String> node = predecessorKeyIterator();
      while (node.hasNext()) {
        final DependencyNode next = this.predecessorMap.get(node.next());
        next.export(list);
      }
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unresolved (DependencyHandler)
  /**
   ** Returns an {@link LinkedList} with all unresolved dependencies.
   ** <br>
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link LinkedList} over all unresolved
   **                            predecessors.
   */
  @Override
  public LinkedList<DependencyNode> unresolved() {
    LinkedList<DependencyNode> list = null;
    if (hasUnresolveables()) {
      list = new LinkedList<DependencyNode>();
      Iterator<String> node = unresolvedKeyIterator();
      while (node.hasNext()) {
        final DependencyNode next = this.unresolvedMap.get(node.next());
        next.export(list);
      }
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

   //////////////////////////////////////////////////////////////////////////////
  // Method:   predecessorKeyIterator
  /**
   ** Returns an {@link Iterator} over all predecessors.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link Iterator} over all predecessors.
   */
  public final Iterator<String> predecessorKeyIterator() {
    return CollectionUtility.set(this.predecessorMap.keySet().iterator()).iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unresolvedKeyIterator
  /**
   ** Returns an {@link Iterator} over all unresolved {@link DependencyNode}s.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link Iterator} over all unresolved
   **                            {@link DependencyNode}s.
   */
  public final Iterator<String> unresolvedKeyIterator() {
    return CollectionUtility.set(this.unresolvedMap.keySet().iterator()).iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insert
  /**
   ** Checks if the id cann be assign to a known {@link DependencyNode}.
   **
   ** @param  id                 the id to check.
   ** @param  predecessor        the id representing a {@link DependencyNode}.
   */
  public void insert(final String id, final String predecessor) {

    final String   method    = "insert";
    final String[] parameter = {id, (predecessor == null ? "Dependency Root" : predecessor) };
    this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_RESOLVING, parameter));

    if (StringUtility.isEmpty(predecessor)) {
      this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_CREATE_ROOT, parameter));
      if (this.unresolvedMap.containsKey(id))
        this.predecessorMap.put(id, this.unresolvedMap.remove(id));
      else
        this.predecessorMap.put(id, new DependencyNode(this, id));
    }
    else if (hasResolveables()) {
      boolean maybeStillOpen = true;
      Iterator<String> i = predecessorKeyIterator();
      while (maybeStillOpen && i.hasNext()) {
        final DependencyNode root     = this.predecessorMap.get(i.next());
        final DependencyNode instance = root.lookup(predecessor);
        if (instance != null) {
          if (this.unresolvedMap.containsKey(id)) {
            instance.add(this.unresolvedMap.remove(id));
            this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_ADDED_RESOLVED, parameter));
          }
          else {
            instance.insert(this, id);
            this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_INSERT_RESOLVED, parameter));
          }
          maybeStillOpen = false;
        }
      }
      if (maybeStillOpen && hasUnresolveables()) {
        if (!maybeStillOpen)
          this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_NOT_RESOLVED, parameter));

        i = unresolvedKeyIterator();
        while (maybeStillOpen && i.hasNext()) {
          final DependencyNode root     = this.unresolvedMap.get(i.next());
          final DependencyNode instance = root.lookup(predecessor);
          if (instance != null) {
            // we do not have to spool data here because the predecessor has
            // beenn passed
            if (this.unresolvedMap.containsKey(id)) {
              instance.add(this.unresolvedMap.remove(id));
              this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_ADDED_UNRESOLVED, parameter));
            }
            else {
              instance.insert(this, id);
              this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_INSERT_UNRESOLVED, parameter));
            }
            maybeStillOpen = false;
          }
        }
      }
      if (maybeStillOpen) {
        this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_NOT_RESOLVED, parameter));
        // put the predecessor in the unresovled nodes
        this.unresolvedMap.put(predecessor, new DependencyNode(this, predecessor));
        // ... and try it again
        insert(id, predecessor);
      }
    }
    else if (hasUnresolveables()) {
      boolean maybeStillOpen = true;
      Iterator<String> i = unresolvedKeyIterator();
      while (maybeStillOpen && i.hasNext()) {
        final DependencyNode root     = this.unresolvedMap.get(i.next());
        final DependencyNode instance = root.lookup(predecessor);
        if (instance != null) {
          if (this.unresolvedMap.containsKey(id)) {
            instance.add(this.unresolvedMap.remove(id));
            this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_ADDED_UNRESOLVED, parameter));
          }
          else {
            instance.insert(this, id);
            this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_INSERT_UNRESOLVED, parameter));
          }
          maybeStillOpen = false;
        }
      }
      if (maybeStillOpen) {
        this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_NODE_NOT_RESOLVED, parameter));
        // put the predecessor in the unresovled nodes
        this.unresolvedMap.put(predecessor, new DependencyNode(this, predecessor));
        // ... and try it again
        insert(id, predecessor);
      }
    }
    else {
      this.logger.debug(this.shortName, method, TaskBundle.format(TaskMessage.DEPENDENCY_CREATE_UNRESULVED, predecessor));
      // put the predecessor in the unresovled nodes
      this.unresolvedMap.put(predecessor, new DependencyNode(this, predecessor));
      // ... and try it again
      insert(id, predecessor);
    }
  }
}