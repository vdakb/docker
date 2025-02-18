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

    File        :   CSVDependencyCollector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVDependencyCollector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;

import java.io.IOException;
import java.io.File;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.foundation.TaskMessage;

////////////////////////////////////////////////////////////////////////////////
// class CSVDependencyCollector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class CSVDependencyCollector extends    CSVOperation
                                    implements CSVDependencyHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link CSVProcessor}s which handles the data of the
   ** {@link CSVDependencyNode}s.
   */
  private final CSVProcessor  processor;

  /**
   ** the {@link LinkedHashMap} where are all known {@link CSVDependencyNode}s
   ** will be srored.
   */
  private final LinkedHashMap<String, CSVDependencyNode> predecessorMap = new LinkedHashMap<String, CSVDependencyNode>();

  /**
   ** the {@link LinkedHashMap} where are all known {@link CSVDependencyNode}s
   ** that has a dependency to a predecessor which is not yet seen.
   */
  private final LinkedHashMap<String, CSVDependencyNode> unresolvedMap  = new LinkedHashMap<String, CSVDependencyNode>();

  /**
   ** Working folder to spool out data.
   ** <br>
   ** It defaults to the user directory.
   */
  private File                workingFolder  = new File(FileSystem.workingFolder());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CSVDependencyCollector</code> that use the
   ** specified {@link CSVDescriptor} for its operation.
   **
   ** @param  descriptor         the {@link CSVDescriptor} to use.
   */
  public CSVDependencyCollector(final CSVDescriptor descriptor) {
    super(descriptor);

    this.processor  = new CSVProcessor(descriptor());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processor  (CSVDependencyHandler)
  /**
   ** Returns the {@link CSVProcessor} of this
   ** <code>CSVDependencyCollector</code>.
   **
   ** @return                    the {@link CSVProcessor} of this
   **                            <code>CSVDependencyCollector</code>.
   */
  public final CSVProcessor processor() {
    return this.processor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasResolveables (CSVDependencyHandler)
  /**
   ** Returns <code>true</code> if this <code>CSVDependencyCollector</code> has
   ** resolved dependency entries.
   **
   ** @return                    <code>true</code> if this
   **                            <code>CSVDependencyCollector</code> has
   **                            resolved dependency entries; <code>false</code>
   **                            otherwise.
   */
  public boolean hasResolveables() {
    return this.predecessorMap.size() > 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasUnresolveables (CSVDependencyHandler)
  /**
   ** Returns <code>true</code> if this <code>CSVDependencyCollector</code> has
   ** unresolved dependency entries.
   **
   ** @return                    <code>true</code> if this
   **                            <code>CSVDependencyCollector</code> has
   **                            unresolved dependency entries;
   **                            <code>false</code> otherwise.
   */
  public boolean hasUnresolveables() {
    return this.unresolvedMap.size() > 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (CSVDependencyHandler)
  /**
   ** Close the dependency handler.
   ** <br>
   ** The handler will no longer be able to recieve data
   **
   ** @throws IOException        if the {@link CSVWriter} associated with a
   **                            particular  {@link CSVDependencyNode}
   **                            cannot be closed.
   */
  public void close()
    throws IOException {

    Iterator<String> node = unresolvedKeyIterator();
    while (node.hasNext()) {
      CSVDependencyNode next = this.unresolvedMap.get(node.next());
      next.close();
    }
    node = predecessorKeyIterator();
    while (node.hasNext()) {
      CSVDependencyNode next = this.predecessorMap.get(node.next());
      next.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (CSVDependencyHandler)
  /**
   ** Close the dependency handler.
   ** <br>
   ** The handler will no longer be able to recieve data
   **
   ** @throws IOException        if the {@link CSVWriter} associated with a
   **                            particular  {@link CSVDependencyNode}
   **                            cannot be closed.
   */
  public void clear()
    throws IOException {

    close();

    Iterator<String> node = unresolvedKeyIterator();
    while (node.hasNext()) {
      CSVDependencyNode next = this.unresolvedMap.get(node.next());
      next.clear();
    }
    node = predecessorKeyIterator();
    while (node.hasNext()) {
      CSVDependencyNode next = this.predecessorMap.get(node.next());
      next.clear();
    }

    // clear also the container that hold resolved and unresolved entries
    this.predecessorMap.clear();
    this.unresolvedMap.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsResolved (CSVDependencyHandler)
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
  public boolean containsResolved(final String id) {
    return (lookup(id) != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsUnresolved (CSVDependencyHandler)
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
 public boolean containsUnresolved(final String id) {
    CSVDependencyNode instance = null;
    Iterator<String> i = unresolvedKeyIterator();
    while (i.hasNext()) {
      instance = this.unresolvedMap.get(i.next()).lookup(id);
      if (instance != null)
        break;
    }
    return (instance != null);
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (CSVDependencyHandler)
  /**
   ** Returns {@link CSVDependencyNode} if the given id is in the list of
   ** waiting dependencies.
   ** <p>
   ** Uses a recursive lookup helper.
   **
   **
   ** @param  id                 the id to lookup.
   **
   ** @return                    {@link CSVDependencyNode} if the given id
   **                            is in the waiting dependencies; otherwise
   **                            <code>null</code>.
   */
  public CSVDependencyNode lookup(final String id) {
    CSVDependencyNode instance = null;
    Iterator<String> i = predecessorKeyIterator();
    while (i.hasNext()) {
      instance = this.predecessorMap.get(i.next()).lookup(id);
      if (instance != null)
        break;
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeResolved (CSVDependencyHandler)
  /**
   ** Merge the resolved dependencies in one {@link File}
   ** <br>
   ** The handler will no longer be able to recieve data
   **
   ** @param  writer             the {@link CSVWriter} will receive the
   **                            produced output.
   **
   ** @throws IOException        if the {@link CSVWriter} associated with a
   **                            particular  {@link CSVDependencyNode} cannot be
   **                            closed.
   ** @throws CSVException       if an error occur during the merge operation.
   */
  public void mergeResolved(final CSVWriter writer)
    throws IOException
    ,      CSVException {

    close();
    if (!hasResolveables())
      return;

    // get all resolved nodes
    LinkedList<CSVDependencyNode>   list = resolved();
    ListIterator<CSVDependencyNode> node = list.listIterator();
    while (node.hasNext()) {
      // ... merge the working file of the node to the CVSWriter
      CSVDependencyNode.merge(node.next(), writer);
/*
      // if the nodes seems to be a node with dependencies ...
      if (next.workingFile() != null) {
        try {
          // ... merge the working file of the node to the CVSWriter
          merge(writer, next.workingFile());
        }
        catch (IOException e) {
          throw new CSVException(CSVError.NOTREADABLE, next.workingFile().getAbsolutePath());
        }
        // ... and delete the working file
        next.workingFile(null).delete();
      }
*/
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mergeUnresolved (CSVDependencyHandler)
  /**
   ** Merge the unresolved dependencies in one {@link File}
   ** <br>
   ** The handler will no longer be able to recieve data
   **
   ** @param  writer             the {@link CSVWriter} will receive the
   **                            produced output.
   **
   ** @throws IOException        if the {@link CSVWriter} associated with a
   **                            particular  {@link CSVDependencyNode}
   **                            cannot be closed.
   ** @throws CSVException       if an error occur during the merge operation.
   */
  public void mergeUnresolved(final CSVWriter writer)
    throws IOException
    ,      CSVException {

    close();
    if (!hasUnresolveables())
      return;

    // get all resolved nodes
    LinkedList<CSVDependencyNode>   list = unresolved();
    ListIterator<CSVDependencyNode> node = list.listIterator();
    while (node.hasNext()) {
      // ... merge the working file of the node to the CVSWriter
      CSVDependencyNode.merge(node.next(), writer);

/*
      // if the nodes seems to be a node with dependencies ...
      if (next.workingFile() != null) {
        try {
          // ... merge the working file of the node to the CVSWriter
          merge(writer, next.workingFile());
        }
        catch (IOException e) {
          throw new CSVException(CSVError.NOTREADABLE, next.workingFile().getAbsolutePath());
        }
        // ... and delete the working file
        next.workingFile(null).delete();
      }
*/
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingFolder (CSVDependencyHandler)
  /**
   ** Sets the current working directory.
   ** <p>
   ** This is a directory for all produced files.
   **
   ** @param  workingFolder      the working directory.
   */
  public void workingFolder(final File workingFolder) {
    this.workingFolder = workingFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingFolder (CSVDependencyHandler)
  /**
   ** Returns the current net directory.
   ** <p>
   ** This is a directory for all produced files.
   **
   ** @return                   the working directory.
   */
  public File workingFolder () {
    return this.workingFolder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolved (CSVDependencyHandler)
  /**
   ** Returns an {@link LinkedList} with all resolved dependencies.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link LinkedList} over all resolved
   **                            predecessors.
   */
  public LinkedList<CSVDependencyNode> resolved() {
    LinkedList<CSVDependencyNode> list = null;
    if (hasResolveables()) {
      list = new LinkedList<CSVDependencyNode>();
      Iterator<String> node = predecessorKeyIterator();
      while (node.hasNext()) {
        final CSVDependencyNode next = this.predecessorMap.get(node.next());
        next.export(list);
      }
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unresolved (CSVDependencyHandler)
  /**
   ** Returns an {@link LinkedList} with all unresolved dependencies.
   ** <br>
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link LinkedList} over all unresolved
   **                            predecessors.
   */
  public LinkedList<CSVDependencyNode> unresolved() {
    LinkedList<CSVDependencyNode> list = null;
    if (hasUnresolveables()) {
      list = new LinkedList<CSVDependencyNode>();
      Iterator<String> node = unresolvedKeyIterator();
      while (node.hasNext()) {
        final CSVDependencyNode next = this.unresolvedMap.get(node.next());
        next.export(list);
      }
    }
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insert
  /**
   ** Checks if the id cann be assign to a known {@link CSVDependencyNode}.
   **
   ** @param  id                 the id to check.
   ** @param  predecessor        the id representing a {@link CSVDependencyNode}.
   ** @param  data               the payload regarding to id
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the output stream needs to be opened and
   **                            the operations fails.
   */
  public void insert(final String id, final String predecessor, final Map<String, Object> data)
    throws IOException
    ,      CSVException {

    final String   method   = "insert";

    final String[] parameter = {id, (predecessor == null ? "Dependency Root" : predecessor) };
    debug(method, TaskMessage.DEPENDENCY_RESOLVING, parameter);

    if (StringUtility.isEmpty(predecessor)) {
      debug(method, TaskMessage.DEPENDENCY_CREATE_ROOT, parameter);
      if (this.unresolvedMap.containsKey(id))
        this.predecessorMap.put(id, this.unresolvedMap.remove(id));
      else
        this.predecessorMap.put(id, new CSVDependencyNode(this, id));
    }
    else if (hasResolveables()) {
      boolean maybeStillOpen = true;
      Iterator<String> i = predecessorKeyIterator();
      while (maybeStillOpen && i.hasNext()) {
        final CSVDependencyNode root     = this.predecessorMap.get(i.next());
        final CSVDependencyNode instance = root.lookup(predecessor);
        if (instance != null) {
          if (this.unresolvedMap.containsKey(id)) {
            instance.add(this.unresolvedMap.remove(id), data);
            debug(method, TaskMessage.DEPENDENCY_NODE_ADDED_RESOLVED, parameter);
          }
          else {
            instance.insert(this, id, data);
            debug(method, TaskMessage.DEPENDENCY_NODE_INSERT_RESOLVED, parameter);
          }
/*
          // we hav to check if the instance has ever recieved depended data
          // this state is controlled by an assiciated CSVWriter to the instance
          // if not a writer is associated we have to create one
          if (instance.workingFile() == null)
            createSpoolFile(instance);

          CSVRecord.write(this.processor, instance.writer(), data, false);
*/
          maybeStillOpen = false;
        }
      }
      if (maybeStillOpen && hasUnresolveables()) {
        if (!maybeStillOpen)
          error("insert", TaskMessage.DEPENDENCY_NODE_NOT_RESOLVED, parameter);

        i = unresolvedKeyIterator();
        while (maybeStillOpen && i.hasNext()) {
          final CSVDependencyNode root     = this.unresolvedMap.get(i.next());
          final CSVDependencyNode instance = root.lookup(predecessor);
          if (instance != null) {
            // we do not have to spool data here because the predecessor has
            // beenn passed
            if (this.unresolvedMap.containsKey(id)) {
              instance.add(this.unresolvedMap.remove(id), data);
              debug(method, TaskMessage.DEPENDENCY_NODE_ADDED_UNRESOLVED, parameter);
            }
            else {
              instance.insert(this, id, data);
              debug(method, TaskMessage.DEPENDENCY_NODE_INSERT_UNRESOLVED, parameter);
            }
/*
            // we hav to check if the instance has ever recieved depended data
            // this state is controlled by an assiciated CSVWriter to the
            // instance if not a writer is associated we have to create one
            if (instance.workingFile() == null)
              createSpoolFile(instance);

            CSVRecord.write(this.processor, instance.writer(), data, false);
*/
            maybeStillOpen = false;
          }
        }
      }
      if (maybeStillOpen) {
        debug(method, TaskMessage.DEPENDENCY_NODE_NOT_RESOLVED, parameter);
        // put the predecessor in the unresovled nodes
        this.unresolvedMap.put(predecessor, new CSVDependencyNode(this, predecessor));
        // ... and try it again
        insert(id, predecessor, data);
      }
    }
    else if (hasUnresolveables()) {
      boolean maybeStillOpen = true;
      Iterator<String> i = unresolvedKeyIterator();
      while (maybeStillOpen && i.hasNext()) {
        final CSVDependencyNode root     = this.unresolvedMap.get(i.next());
        final CSVDependencyNode instance = root.lookup(predecessor);
        if (instance != null) {
          if (this.unresolvedMap.containsKey(id)) {
            instance.add(this.unresolvedMap.remove(id), data);
            debug(method, TaskMessage.DEPENDENCY_NODE_ADDED_UNRESOLVED, parameter);
          }
          else {
            instance.insert(this, id, data);
            debug(method, TaskMessage.DEPENDENCY_NODE_INSERT_UNRESOLVED, parameter);
          }
/*
          // we hav to check if the instance has ever recieved depended data
          // this state is controlled by an assiciated CSVWriter to the instance
          // if not a writer is associated we have to create one
          if (instance.workingFile() == null)
            createSpoolFile(instance);

          CSVRecord.write(this.processor, instance.writer(), data, false);
*/
          maybeStillOpen = false;
        }
      }
      if (maybeStillOpen) {
        debug(method, TaskMessage.DEPENDENCY_NODE_NOT_RESOLVED, parameter);
        // put the predecessor in the unresovled nodes
        this.unresolvedMap.put(predecessor, new CSVDependencyNode(this, predecessor));
        // ... and try it again
        insert(id, predecessor, data);
      }
    }
    else {
      debug(method, TaskMessage.DEPENDENCY_CREATE_UNRESULVED, predecessor);
      // put the predecessor in the unresovled nodes
      this.unresolvedMap.put(predecessor, new CSVDependencyNode(this, predecessor));
      // ... and try it again
      insert(id, predecessor, data);
    }
  }

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
   ** Returns an {@link Iterator} over all unresolved {@link CSVDependencyNode}s.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link Iterator} over all unresolved
   **                            {@link CSVDependencyNode}s.
   */
  public final Iterator<String> unresolvedKeyIterator() {
    return CollectionUtility.set(this.unresolvedMap.keySet().iterator()).iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Returns an {@link LinkedList} with all unresolved
   ** {@link CSVDependencyNode}s.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   */
  public void validate() {

    if (hasUnresolveables())
      System.out.println("Ooops some entity are unresolved");

    Iterator<String> i = predecessorKeyIterator();
    while (i.hasNext()) {
      CSVDependencyNode next = this.predecessorMap.get(i.next());
      next.clear();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSpoolFile
  /**
   ** Create the output file for a predecesor that is currently unresolved.
   ** <p>
   ** We are spooling the data to a file to reduce the memory footprint for the
   ** collected data.
   **
   ** @param  node                the {@link CSVDependencyNode} where the
   **                             event ocurred
   **
   ** @throws IOException         if the output stream cannot be created
   ** @throws SystemException     if the output stream does not meet the
   **                             requirements of a writeable file.
   */
/*
  private void createSpoolFile(CSVDependencyNode node)
    throws IOException
    ,      SystemException {

    if (node.workingFile(createWorkingFile(node.id())) != null)
      error("createSpoolFile", CSVMessage.NODE_IS_ACTIVE, node.id());
  }
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** Merge the dependencies in one {@link File}
   **
   ** @param  writer            the {@link CSVWriter} that will receive the
   **                            produced output.
   ** @param  workingFile        the abstract file that will provides the
   **                            data and should be merged to the specified
   **                            {@link CSVWriter}.
   **
   ** @throws IOException        if the file specified by
   **                            <code>workingFile</code> does not exists.
   ** @throws SystemException    if the {@link CSVWriter} associated with a
   **                            particular  {@link CSVDependencyNode}
   **                            cannot be closed.
   */
/*
  private void merge(CSVWriter writer, File workingFile)
    throws IOException
    ,      SystemException {

    final String method = "merge";
    trace(method, TaskMessage.METHOD_ENTRY);

    if (workingFile == null)
      return;

    try {
      final CSVReader    reader    = new CSVReader(new BufferedReader(new FileReader(workingFile)));
      final CSVProcessor processor = new CSVProcessor(descriptor(), reader);

      // This while loop is used to read the file sequentially.
      try {
        while (true) {
          // fetch a record from input file and without applying the configured
          // transformations
          final Map subject = processor.readEntity(reader, false);
          // store the fetched record in output file and also without applying
          // the configured transformations
          CSVRecord.write(this.processor, writer, subject, false);
        }
      }
      catch (EOFException e) {
        // if we reached the end of file ...
        debug(method, CSVMessage.ENDOFFILE, workingFile.getName());
        processor.close();
      }
    }
    finally {
      trace(method, TaskMessage.METHOD_EXIT);
    }
  }
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWorkingFile
  /**
   ** Creates the abstract {@link File} to access a working file.
   **
   ** @param  fileName           the name of the file in the filesystem file to
   **                            wrapp.
   **
   ** @return                    the abstract {@link File} to access the working
   **                            file.
   **
   ** @throws IOException        if the file could not be created.
   ** @throws SystemException    if the abtract file handle could not be
   **                            created.
   */
/*
  private File createWorkingFile(String fileName)
    throws IOException
    ,      SystemException {

    final File  workingFile = createFile(workingFolder(), fileName);
    // always create a new file
    workingFile.createNewFile();

    // check if we have write access to the file
    if (!workingFile.canWrite()) {
      final String[] values = { workingFile.getName(), workingFolder().getAbsolutePath()};
      throw new CSVException(CSVError.NOTWRITABLE, values);
    }

    return workingFile;
  }
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFile
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>fileName</code> in the specified <code>folderName</code> and check
   ** if the resulting file is writable by this scheduled task.
   **
   ** @param  folder             the abstract {@link File} of the filesystem
   **                            folder where the specified
   **                            <code>fileName</code> should be contained.
   ** @param  fileName           the name of the filesystem file to create and
   **                            check.
   **
   ** @throws Exception          if the file doesn't fit the requirements.
   */
/*
  private File createFile(File folder, String fileName)
    throws SystemException {

    File file = null;
    if (StringUtility.isEmpty(fileName))
      try {
        file = FileSystem.createTempFile("~csv", ".tmp", workingFolder());
      }
      catch (IOException e) {
        throw new SystemException(e);
      }
    else
      file = createFile(folder.getAbsolutePath(), fileName);

    // check it's really a file
    if (file.isDirectory()) {
      final String[] values = { fileName , file.getAbsolutePath()};
      throw new CSVException(CSVError.NOTAFILE, values);
    }

    return file;
  }
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFile
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>fileName</code> in the specified <code>folderName</code> and check
   ** if the resulting file is writable by this IT Resource.
   **
   ** @param  pathName           the folder in the filesystem where the
   **                            specified <code>fileName</code> should be
   **                            contained.
   ** @param  fileName           the name of the file in the filesystem file to
   **                            wrapp.
   */
/*
  private static File createFile(String pathName, String fileName) {
    if (!pathName.endsWith(File.separator))
      pathName = pathName + File.separator;

    final String path   = pathName + fileName;

    return new File(path);
  }
*/
}