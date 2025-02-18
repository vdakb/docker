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

    File        :   DependencyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DependencyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.reconciliation;

import java.util.LinkedList;

////////////////////////////////////////////////////////////////////////////////
// interface DependencyHandler
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The general contract to handle race conditions due to dependencies in
 ** reconciliations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface DependencyHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasResolveables
  /**
   ** Returns <code>true</code> if this <code>CSVDependencyHandler</code> has
   ** resolved dependency entries.
   **
   ** @return                    <code>true</code> if this
   **                            <code>CSVDependencyHandler</code> has
   **                            resolved dependency entries; <code>false</code>
   **                            otherwise.
   */
  boolean hasResolveables();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasUnresolveables
  /**
   ** Returns <code>true</code> if this <code>CSVDependencyHandler</code> has
   ** unresolved dependency entries.
   **
   ** @return                    <code>true</code> if this
   **                            <code>CSVDependencyHandler</code> has
   **                            unresolved dependency entries;
   **                            <code>false</code> otherwise.
   */
  boolean hasUnresolveables();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears and close the dependency handler.
   */
  void clear();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsResolved
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
 boolean containsResolved(final String id);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsUnresolved
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
 boolean containsUnresolved(String id);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns <code>CSVDependencyNode</code> if the given id is in the list of
   ** waiting dependencies.
   ** <p>
   ** Uses a recursive lookup helper.
   **
   **
   ** @param  id                the id to lookup.
   **
   ** @return                    <code>CSVDependencyNode</code> if the given id
   **                            is in the waiting dependencies; otherwise
   **                            <code>null</code>.
   */
  DependencyNode lookup(final String id);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolved
  /**
   ** Returns a new list of this handler with the nodes that are resolved.
   **
   ** @return                    a new list of this handler with the nodes that
   **                            are resolved.
   */
  LinkedList<DependencyNode> resolved();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unresolved
  /**
   ** Returns a new list of this handler with the nodes that are unresolved.
   **
   ** @return                    a new list of this handler with the nodes that
   **                            are unresolved.
   */
  LinkedList<DependencyNode> unresolved();
}