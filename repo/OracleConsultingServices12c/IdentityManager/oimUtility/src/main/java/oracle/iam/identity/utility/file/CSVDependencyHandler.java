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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   CSV Flatfile Connector

    File        :   CSVDependencyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVDependencyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.LinkedList;

import java.io.IOException;
import java.io.File;

import oracle.hst.foundation.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// interface CSVDependencyHandler
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The general protocoll to handle dependencies.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface CSVDependencyHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Set the log writer for this CSV operation.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** CSV file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** When a <code>CSVOperation</code> object is created the logger is
   ** initially null, in other words, logging is disabled.
   **
   ** @param  logger             the new logger; to disable, set to null.
   */
  void logger(final Logger logger);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Returns the log writer for this CSV operation.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** CSV file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** When a <code>CSVOperation</code> object is created the logger is
   ** initially null, in other words, logging is disabled.
   **
   ** @return                    the log writer for this CSV operation.
   */
  Logger logger();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptor
  /**
   ** Returns the {@link CSVDescriptor} of this
   ** <code>CSVDependencyHandler</code>.
   **
   ** @return                    the {@link CSVDescriptor} of this
   **                            <code>CSVDependencyHandler</code>.
   */
  CSVDescriptor descriptor();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor
  /**
   ** Returns the {@link CSVProcessor} of this
   ** <code>CSVDependencyHandler</code>.
   **
   ** @return                    the {@link CSVProcessor} of this
   **                            <code>CSVDependencyHandler</code>.
   */
  CSVProcessor processor();

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
   **
   ** @throws IOException        if the {@link CSVWriter} associated with a
   **                            particular  <code>CSVDependencyNode</code>
   **                            cannot be closed.
   */
  void clear()
    throws IOException;

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
 CSVDependencyNode lookup(final String id);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeResolved
  /**
   ** Merge all resolved data in one {@link CSVWriter}.
   **
   ** @param  writer             the {@link CSVWriter} will receive the
   **                            produced output.
   **
   ** @throws IOException        if the {@link CSVWriter} associated with a
   **                            particular  <code>CSVDependencyNode</code>
   **                            cannot be closed.
   ** @throws CSVException       if an error occur during the merge operation.
   */
  void mergeResolved(final CSVWriter writer)
    throws IOException
    ,      CSVException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeUnesolved
  /**
   ** Merge all unresolved data in one {@link CSVWriter}.
   **
   ** @param  writer             the {@link CSVWriter} will receive the
   **                            produced output.
   **
   ** @throws IOException        if the {@link CSVWriter} associated with a
   **                            particular  <code>CSVDependencyNode</code>
   **                            cannot be closed.
   ** @throws CSVException       if an error occur during the merge operation.
   */
  void mergeUnresolved(final CSVWriter writer)
    throws IOException
    ,      CSVException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingFolder
  /**
   ** Sets the current net directory.
   ** <p>
   ** This is a directory for nets, layouts and markings.
   **
   ** @param  folder              new working directory.
   */
  void workingFolder(final File folder);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingFolder
  /**
   ** Sets the current net directory.
   ** <p>
   ** This is a directory for nets, layouts and markings.
   **
   ** @return                   this net wroking directory.
   */
  File workingFolder();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolved
  /**
   ** Returns a new list of this handler with the nodes that are resolved.
   **
   ** @return                    a new list of this handler with the nodes that
   **                            are resolved.
   */
  LinkedList<CSVDependencyNode> resolved();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unresolved
  /**
   ** Returns a new list of this handler with the nodes that are unresolved.
   **
   ** @return                    a new list of this handler with the nodes that
   **                            are unresolved.
   */
  LinkedList<CSVDependencyNode> unresolved();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes a error message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   */
  void error(final String method, final String resourceKey);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes a error message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  argument           argument to substitute the paceholders in
   **                            the pattern referd by <code>resourceKey</code>.
   */
  void error(final String method, final String resourceKey, final String argument);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes a error message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  void error(final String method, final String resourceKey, final String[] arguments);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   */
  void debug(final String method, final String resourceKey);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  argument           argument to substitute the paceholders in
   **                            the pattern referd by <code>resourceKey</code>.
   */
  void debug(final String method, final String resourceKey, final String argument);
}