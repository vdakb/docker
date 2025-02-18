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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Path.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Path.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.type;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;

import org.apache.tools.ant.types.resources.Union;

////////////////////////////////////////////////////////////////////////////////
// class Path
// ~~~~~ ~~~~~
/**
 ** <code>Path</code> is a container that works like the
 ** {@link org.apache.tools.ant.types.Path} type.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Path extends DataType
  implements Cloneable, ResourceCollection {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Iterator<Resource> EMPTY_ITERATOR = (Iterator<Resource>)Collections.EMPTY_SET.iterator();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Union union = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class PathElement
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** Helper class, holds the nested <code>pathelement</code> values.
   */
  public class PathElement implements ResourceCollection {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Resource[] parts;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>PathElement</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public PathElement() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>PathElement</code> type with the supplied value.
     **
     ** @param  path             the value of this <code>PathElement</code>.
     */
    public PathElement(final String path) {
      this(null, path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>PathElement</code> type with the supplied value.
     **
     ** @param  project          the active ANT project.
     ** @param  path             the value of this <code>PathElement</code>.
     */
    public PathElement(final Project project, final String path) {
      setProject(project);
      setPath(project == null ? path : project.replaceProperties(path));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setLocation
    /**
     ** Set the path to be used for this <code>PathElement</code>.
     **
     ** @param  location         a metadata document to export, import or
     **                          delete.
     **                          <p>
     **                          This can be specified by a  fully qualified
     **                          metadata document or a document name pattern
     **                          that is used to identify which documents to
     **                          handled.
     */
    public void setLocation(final String location) {
      final File path = new File(getProject().getBaseDir(), location);
      this.parts = new Resource[] { new Resource(path.getAbsolutePath()) };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setPath
    /**
     ** Set the path to be used for this <code>PathElement</code>.
     **
     ** @param  path             a list of fully qualified metadata documents to
     **                          export, import or delete.
     **                          <p>
     **                          This can be specified by a comma-separated list
     **                          of fully qualified metadata documents or a
     **                          document name pattern that is used to identify
     **                          which documents to handled.
     */
    public void setPath(final String path) {
      this.parts = Path.translatePath(path);
    }

    public Resource[] parts() {
      return this.parts;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: iterator (ResourceCollection)
    /**
     ** Return an {@link Iterator} over the contents of this
     ** {@link ResourceCollection}, whose elements are {@link String} instances.
     **
     ** @return                  an Iterator of Resources.
     */
    public Iterator<Resource> iterator() {
      return new PathElementIterator(this.parts);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isFilesystemOnly (ResourceCollection)
    /**
     ** Indicate whether this {@link ResourceCollection} is composed entirely of
     ** Resources accessible via local filesystem conventions.
     **
     ** @return                  whether this is a filesystem-only
     **                          resource collection.
     */
    public boolean isFilesystemOnly() {
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: size (ResourceCollection)
    /**
     ** Return the number of contained Resources.
     **
     ** @return                  number of elements as int.
     */
    public int size() {
      return this.parts == null ? 0 : this.parts.length;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class PathElementIterator
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~
  /**
   ** Helper class, to iterate over nested <code>pathelement</code> values.
   */
  public class PathElementIterator implements Iterator<Resource> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int        pos    = 0;
    private Resource[] parts;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>PathElementIterator</code> that use the specified
     ** array.
     **
     ** @param  parts            the array with elements to iterate
     */
    public PathElementIterator(final Resource[] parts) {
      // ensure inheritance
      super();

      // initialize instance
      add(parts);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   hasNext (Iterator)
    /**
     ** Returns <code>true</code> if the iteration has more elements. (In other
     ** words, returns <code>true</code> if <code>next</code> would return an
     ** element rather than throwing an exception.)
     **
     ** @return                  <code>true</code> if the iterator has more
     **                          elements.
     */
    @Override
    public boolean hasNext() {
      return pos < parts.length;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   next (Iterator)
    /**
     ** Returns the next transformed element in the iteration.
     ** <br>
     ** Calling this method repeatedly until the {@link #hasNext()} method
     ** returns <code>false</code> will return each transformed element in the
     ** underlying collection exactly once.
     **
     ** @return                  the next element in the iteration.
     **
     ** @throws NoSuchElementException iteration has no more elements.
     */
    @Override
    public Resource next() {
      if (!hasNext())
        throw new NoSuchElementException();

      return parts[pos++];
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   remove (Iterator)
    /**
     ** Removes from the underlying collection the last element returned by the
     ** iterator (optional operation).
     ** <br>
     ** This method can be called only once per call to <code>next</code>. The
     ** behavior of an iterator is unspecified if the underlying collection is
     ** modified while the iteration is in progress in any way other than by
     ** calling this method.
     **
     ** @throws UnsupportedOperationException if the <code>remove</code>
     **                                       operation is not supported by this
     **                                       {@link Iterator}.
     ** @throws IllegalStateException         if the <code>next</code> method
     **                                       has not yet been called, or the
     **                                       <code>remove</code> method has
     **                                       already been called after the last
     **                                       call to the <code>next</code>
     **                                       method.
     */
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   add
    /**
     ** Adds the specfified array of strings to the source of iteration.
     **
     ** @param  parts             the array of strings to add
     */
    public void add(final Resource[] parts) {
      final int        start = this.parts == null ? 0 : this.parts.length;
      final Resource[] newparts = new Resource[start + parts.length];
      if (start > 0)
        System.arraycopy(this.parts, 0, newparts, 0, start);

      this.parts = newparts;
      System.arraycopy(parts, 0, this.parts, start, parts.length);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Path</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Path() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Path</code> type with the supplied value.
   **
   ** @param  project            the active ANT project.
   ** @param  path               the value of this <code>Path</code>.
   */
  public Path(final Project project, final String path) {
    setProject(project);
    createPathElement().setPath(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Path</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (this.union != null)
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPath
  /**
   ** Parses a path definition and creates single PathElements.
   **
   ** @param  path               a list of fully qualified metadata documents to
   **                            export, import or delete.
   **                            <p>
   **                            This can be specified by a comma-separated list
   **                            of fully qualified metadata documents or a
   **                            document name pattern that is used to identify
   **                            which documents to handled.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Path</code>.
   */
  public void setPath(final String path)
    throws BuildException {

    checkAttributesAllowed();
    createPathElement().setPath(path);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation
  /**
   ** Creates and adds single PathElement.
   **
   ** @param  location           a fully qualified metadata document to export
   **                            import or delete.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Path</code>.
   */
  public void setLocation(final String location)
    throws BuildException {

    checkAttributesAllowed();
    createPathElement().setLocation(location);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns all path elements defined by this and nested path objects.
   **
   ** @return                    list of path elements.
   */
  public String[] list() {
    if (isReference())
      return ((Path)getCheckedRef()).list();

    return this.union == null ? new String[0] : this.union.list();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (ResourceCollection)
  /**
   ** Return an {@link Iterator} over the contents of this
   ** {@link ResourceCollection}, whose elements are {@link String} instances.
   **
   ** @return                    an {@link Iterator} of {@link String}s.
   */
  public final synchronized Iterator<Resource> iterator() {
    if (isReference())
      return ((Path)getCheckedRef()).iterator();

    dieOnCircularReference();

    return this.union == null ? EMPTY_ITERATOR : this.union.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isFilesystemOnly (ResourceCollection)
  /**
   ** Indicate whether this {@link ResourceCollection} is composed entirely of
   ** Resources accessible via local filesystem conventions.
   **
   ** @return                    whether this is a filesystem-only resource
   **                            collection.
   */
  public synchronized boolean isFilesystemOnly() {
    if (isReference())
      return ((Path)getCheckedRef()).isFilesystemOnly();

    dieOnCircularReference();

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (ResourceCollection)
  /**
   ** Return the number of contained Resources.
   **
   ** @return                    number of elements as int.
   */
  public synchronized int size() {
    if (isReference())
      return ((Path)getCheckedRef()).size();

    dieOnCircularReference();
    return union == null ? 0 : this.union.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPathElement
  /**
   ** Creates a nested <code>pathelement</code> element.
   **
   ** @return                    the created {@link PathElement}
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Path</code>
   */
  public PathElement createPathElement()
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    final PathElement element = new PathElement();
    add(element);
    return element;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds a nested path.
   **
   ** @param  path               add the specified {@link Path} <code>path</code>
   **                            as a nested path.
   **
   ** @throws BuildException     if the specified path is the same as this
   **                            {@link Path}.
   */
  public void add(final Path path)
    throws BuildException {

    if (path == null)
      return;

    if (path == this)
      throw circularReference();

    if (path.getProject() == null)
      path.setProject(getProject());

    add(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds a nested resource.
   **
   ** @param  collection         the {@link ResourceCollection} to add.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Path</code>.
   */
  public void add(final ResourceCollection collection) {
    checkChildrenAllowed();
    if (collection == null)
      return;

    if (this.union == null) {
      union = new Union();
      union.setProject(getProject());
      union.setCache(false);
    }

    union.add(collection);
    setChecked(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translatePath
  /**
   ** Splits a Document Path with <code>,</code> into its parts.
   **
   ** @param  source             a <code>String</code> value.
   **
   ** @return                    an array of strings, one for each path element
   */
  public static Resource[] translatePath(final String source) {
    if (source == null)
      return new Resource[0];

    final List<Resource>  result = new ArrayList<Resource>();
    final StringTokenizer tokenizer = new StringTokenizer(source);
    while (tokenizer.hasMoreTokens())
      result.add(new Resource(tokenizer.nextToken()));

    return result.toArray(new Resource[0]);
  }
}