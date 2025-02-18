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

    File        :   CSVDependencyNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVDependencyNode.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import java.io.IOException;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.URLEncoder;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;

////////////////////////////////////////////////////////////////////////////////
// class CSVDependencyNode
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An class for any kind of association in which one side is somehow dependant
 ** on another.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class CSVDependencyNode {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the identifier of the node */
  private String               id;

  /** the payload of the predecessor */
  private File                 workingFile;

  /** the payload of the predecessor */
  private CSVWriter               writer;

  /** the handler instance */
  private CSVDependencyHandler    handler;

  /** the predecessor where this instance is waiting on themselve*/
  private CSVDependencyNode       predecessor;

  /** the dependencies that are waiting for resolution of this predecessor */
  private List<CSVDependencyNode> dependency;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>CSVDependencyNode</code> with the specified id.
   **
   ** @param  handler            the {@link CSVDependencyHandler} that this
   **                            <code>CSVDependencyNode</code> is
   **                            instantiating.
   ** @param  id                 the id of this <code>CSVDependencyNode</code>
   */
  public CSVDependencyNode(final CSVDependencyHandler handler, final String id) {
    this.handler = handler;
    this.id      = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of this <code>CSVDependencyNode</code>.
   **
   ** @return                    the identifier of this
   **                            <code>CSVDependencyNode</code>.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingFile
  /**
   ** Returns the {@link File} of this <code>CSVDependencyNode</code> that
   ** collects the data depending on this <code>CSVDependencyNode</code>
   **
   ** @return                    the {@link File} that collects the data
   **                            depending on this
   **                            <code>CSVDependencyNode</code>.
   */
  protected final File workingFile() {
    return this.workingFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of dependend <code>CSVDependencyNode</code>s.
   ** <br>
   ** Uses a recursive helper that recurse down the tree and counts the nodes.
   **
   ** @return                    the number of dependend
   **                            <code>CSVDependencyNode</code>s in the tree.
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
  public final ListIterator<CSVDependencyNode> dependencyIterator() {
    return (this.dependency != null) ? this.dependency.listIterator() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** The recursive merge helper.
   ** <p>
   ** Export the dependencies to a {@link CSVWriter}.
   **
   ** @param  node                the container to containing the dependencies.
   ** @param  export              the container to recieve the dependencies of
   **                             this <code>CSVDependencyNode</code>.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       some problem reading or writing a file,
   **                            possibly malformed data.
   */
  public static void merge(final CSVDependencyNode node, final CSVWriter export)
    throws IOException
    ,      CSVException {

    final String method = "merge";
    if (node.dependency != null) {
      CSVReader reader =  null;
      try {
        reader = new CSVReader(new InputStreamReader(new FileInputStream(node.workingFile), export.encoding()));
      }
      catch (FileNotFoundException e) {
        String[] parameter = { "Merge Source", node.workingFile.getName() };
        throw new CSVException(CSVError.NOTREADABLE, parameter);
      }
      catch (IOException e) {
        throw new CSVException(SystemError.GENERAL, e);
      }

      // ... merge the working file of the node to the CVSWriter
      final CSVProcessor processor = new CSVProcessor(node.handler.descriptor(), reader);
      try {
        // this while loop is used to read the file sequentially.
        while (true) {
          // fetch a record from input file and without applying the configured
          // transformations
          final Map<String, Object> subject = processor.readEntity(reader, false);
          // store the fetched record in output file and also without applying
          // the configured transformations
          CSVRecord.write(node.handler.processor(), export, subject, false);
        }
      }
      catch (EOFException e) {
        // if we reached the end of file ...
        node.handler.debug(method, CSVMessage.ENDOFFILE,  node.workingFile.getName());
        processor.close();

        // ... and delete the working file
        node.workingFile.delete();
        node.workingFile = null;
      }
    }
  }

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
  public boolean equals(final Object other) {
    return this.id.equals(other.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this <code>CSVDependencyNode</code>. To
   ** get the hash code of this <code>CSVDependencyNode</code>.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two
   ** <code>CSVDependencyNode</code> <code>s1</code> and <code>s2</code>, as
   ** required by the general contract of <code>Object.hashCode()</code>.
   **
   ** @return                    the hash code value for this
   **                            <code>CSVDependencyNode</code>.
   */
  public int hashCode() {
    return this.id.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  public String toString() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close the stream of dependencies on this <code>CSVDependencyNode</code>.
   ** <p>
   ** Uses a recursive close helper.
   **
   ** @throws IOException        if the {@link CSVWriter} associated with this
   **                            <code>CSVDependencyNode</code> cannot be closed.
   */
  public void close()
    throws IOException {

    close(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insert
  /**
   ** Inserts the given data into the <code>CSVDependencyNode</code>.
   ** <p>
   ** Uses a recursive insert helper.
   **
   ** @param  handler            the {@link CSVDependencyHandler} that this
   **                            <code>CSVDependencyNode</code> is
   **                            inserting.
   ** @param  id                 the id to insert.
   ** @param  data               the collection representing the data of the
   **                            node to add.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the output stream needs to be opened and
   **                            the operations fails.
   */
  public void insert(final CSVDependencyHandler handler, final String id, final Map<String, Object> data)
    throws IOException
    ,      CSVException {

    add(insert(handler, this, id), data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an existing <code>CSVDependencyNode</code> to the dependency list of
   ** this instance.
   **
   ** @param  existing           the predecessor to add.
   ** @param  data               the collection representing the data of the
   **                            node to add.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the output stream needs to be opened and
   **                            the operations fails.
   */
  public void add(final CSVDependencyNode existing, final Map<String, Object> data)
    throws IOException
    ,      CSVException {

    final String method = "add";

    if (this.dependency == null)
      this.dependency = new LinkedList<CSVDependencyNode>();

    if (existing.predecessor != null) {
      if (!existing.predecessor.dependency.remove(existing)) {
        String[] parameter = { existing.predecessor.id, id };
        this.handler.error(method, TaskError.DEPENDENCY_PARENT_CONFLICT, parameter);
      }
    }
    existing.predecessor = this;
    this.dependency.add(existing);

    // we hav to check if the instance has ever recieved depended data
    // this state is controlled by an assiciated CSVWriter to the instance
    // if not a writer is associated we have to create one
    if (workingFile() == null)
      createWriter(this);

    CSVRecord.write(handler.processor(), this.writer, data, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears the dependencies of this <code>CSVDependencyNode</code>.
   ** <p>
   ** Uses a recursive clear helper.
   */
  public void clear() {
    clear(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   export
  /**
   ** Export the <code>CSVDependencyNode</code> and the dependencies to a list.
   ** <p>
   ** Uses a recursive export helper.
   **
   ** @param  export             the container to recieve the
   **                            <code>CSVDependencyNode</code> and the
   **                            dependencies.
   */
  public void export(final List<CSVDependencyNode> export) {
    export(this, export);
  }

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
  public CSVDependencyNode lookup(final String id) {
    return lookup(this, id);
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
  private static int size(final CSVDependencyNode node) {
    int size = 0;
    if (node.dependency != null) {
      Iterator<CSVDependencyNode> i = node.dependencyIterator();
      while (i.hasNext())
        size += size(i.next());
      size += node.dependency.size();
    }
    return size;
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
   ** @param  handler            the {@link CSVDependencyHandler} that the
   **                            <code>CSVDependencyNode</code> is
   **                            instantiating.
   ** @param  node               the node where to insert the date.
   ** @param  id                 the id to insert.
   **
   ** @return                    the create node
   */
  private static CSVDependencyNode insert(final CSVDependencyHandler handler, CSVDependencyNode node, final String id) {
    if (node == null)
      node = new CSVDependencyNode(handler, id);
    else
      node = insert(handler, lookup(node, id), id);

    // in any case, return the new pointer to the caller
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** The recursive lookup helper.
   ** <p>
   ** Returns <code>CSVDependencyNode</code> if the given id is in the list of
   ** waiting dependencies.
   **
   ** @param  id                the id to lookup.
   **
   ** @return                    <code>true</code> if the given target is in the
   **                            dependenciese; otherwise <code>false</code>
   */
  private static CSVDependencyNode lookup(final CSVDependencyNode node, final String id) {
    if (node.id.equals(id))
      return node;

    if (node.dependency == null || node.dependency.size() == 0)
      return null;

    CSVDependencyNode instance = null;
    Iterator<CSVDependencyNode> i = node.dependencyIterator();
    while (i.hasNext()) {
      instance = lookup(i.next(), id);
      if (instance != null)
        break;
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** The recursive clear helper.
   ** <p>
   ** Clears the dependencies of the specified <code>CSVDependencyNode</code>.
   */
  private static void clear(final CSVDependencyNode node) {
    if (node.dependency != null) {
      Iterator<CSVDependencyNode> i = node.dependencyIterator();
      while (i.hasNext())
        clear(i.next());

      node.dependency.clear();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** The recursive close helper.
   ** <p>
   ** close the dependency stream of the specified <code>CSVDependencyNode</code>.
   **
   ** @throws IOException        if the {@link CSVWriter} associated with the
   **                            given <code>CSVDependencyNode</code> cannot be
   **                            closed.
   */
  private static void close(final CSVDependencyNode node)
    throws IOException {

    if (node.dependency != null) {
      Iterator<CSVDependencyNode> i = node.dependencyIterator();
      while (i.hasNext())
        close(i.next());

      // may be we passed a second time
      if (node.writer != null) {
        node.writer.close();
        node.writer = null;
      }
    }
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
  private static void export(final CSVDependencyNode node, final List<CSVDependencyNode> export) {
    export.add(node);
    if (node.dependency != null) {
      Iterator<CSVDependencyNode> i = node.dependencyIterator();
      while (i.hasNext())
        export(i.next(), export);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWriter
  /**
   ** Create the output file for a predecesor that is currently unresolved.
   ** <p>
   ** We are spooling the data to a file to reduce the memory footprint for the
   ** collected data.
   **
   ** @param  node               the {@link CSVDependencyNode} where the
   **                            event ocurred
   **
   ** @throws CSVException       if the output stream does not meet the
   **                            requirements of a writeable file.
   */
  private static void createWriter(final CSVDependencyNode node)
    throws IOException
    ,      CSVException {

    if (node.writer != null) {
      node.handler.error("createWriter", TaskMessage.DEPENDENCY_NODE_IS_ACTIVE, node.id());
      return;
    }

    // in case the id f the node contains special characters it's necessary to encode it
    // to make it applicable on any files system
    final String encodedID = URLEncoder.encode(node.id(), "UTF-8");
    node.workingFile = createFile(node.handler.workingFolder(), encodedID);
    try {
      // always create a new file
      node.workingFile.createNewFile();
    }
    catch (IOException e) {
      String[] parameter = { node.workingFile.getName(), e.getLocalizedMessage() };
      throw new CSVException(CSVError.NOTCREATABLE, parameter);
    }

    // check if we have write access to the file
    if (!node.workingFile.canWrite()) {
      final String[] values = { node.workingFile.getName(), node.handler.workingFolder().getAbsolutePath()};
      throw new CSVException(CSVError.NOTWRITABLE, values);
    }

    try {
      node.writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(node.workingFile)));
    }
    catch (IOException e) {
      String[] parameter = { node.workingFile.getName(), e.getLocalizedMessage() };
      throw new CSVException(CSVError.NOTCREATABLE, parameter);
    }
    // put the header at the first line in the new created strem
    node.handler.descriptor().write(node.writer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFile
  /**
   ** Creates the abstract {@link File} for the specified
   ** <code>fileName</code> in the specified <code>folderName</code> and check
   ** if the resulting file is writable by this scheduled task.
   **
   ** @param  fileName           the name of the filesystem file to create and
   **                            check.
   **
   ** @throws CSVException       if the file doesn't fit the requirements.
   */
  private static File createFile(final File workingFolder, final String fileName)
    throws CSVException {

    File file = null;
    if (StringUtility.isEmpty(fileName))
      try {
        file = FileSystem.createTempFile(CSVOperation.TMPPREFIX, CSVOperation.TMPEXTENSION, workingFolder);
      }
      catch (IOException e) {
        throw new CSVException(SystemError.GENERAL, e);
      }
    else
      file = createFile(workingFolder.getAbsolutePath(), fileName);

    // check it's really a file
    if (file.isDirectory()) {
      final String[] values = { fileName, file.getAbsolutePath()};
      throw new CSVException(CSVError.NOTAFILE, values);
    }

    return file;
  }

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
  private static File createFile(String pathName, final String fileName) {
    if (!pathName.endsWith(File.separator))
      pathName = pathName + File.separator;

    final String path   = pathName + fileName;

    return new File(path);
  }
}