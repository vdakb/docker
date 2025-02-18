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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   NameCollection.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NameCollection.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.type;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.foundation.utility.CollectionUtility;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

////////////////////////////////////////////////////////////////////////////////
// class NameCollection
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>NameCollection</code> is a group of attribute names.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NameCollection extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Collection<String> name;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NameCollection</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NameCollection() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of elements in the name collection.
   ** <br>
   ** If the name collection contains more than <code>Integer.MAX_VALUE</code>
   ** elements, returns <code>Integer.MAX_VALUE</code>.
   **
   ** @return                    the number of elements in the name collection.
   */
  public int size() {
    return this.name == null ? 0 : this.name.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty
  /**
   ** Returns <code>true</code> if the name collection contains no elements.
   **
   ** @return                    <code>true</code> if the name collection
   **                            contains no elements
   */
  public boolean isEmpty() {
    return this.name == null ? true : this.name.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Returns <code>true</code> if the name collection contains the specified
   ** element.
   ** <br>
   ** More formally, returns <code>true</code> if and only if the name
   ** collection contains at least one element <code>e</code> such that
   ** <code>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</code>.
   **
   ** @param  other              element whose presence in the name collection
   **                            is to be tested.
   **
   ** @return                    <code>true</code> if the name collection
   **                            contains the specified element.
   **
   ** @throws ClassCastException   if the type of the specified element is
   **                              incompatible with the name collection
   **                              (<a href="#optional-restrictions">optional</a>)
   ** @throws NullPointerException if the specified element is
   **                              <code>null</code> and the name collection
   **                              does not permit <code>null</code> elements
   **                              (<a href="#optional-restrictions">optional</a>)
   */
  public boolean contains(final Object other) {
    return this.name == null ? false : this.name.contains(other);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   names
  /**
   ** Returns the collected names.
   **
   ** @return                    the collected names.
   */
  public List<String> names () {
    return CollectionUtility.unmodifiableList(this.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Acodeributes</code> instance.
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

    if (size() > 0)
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toArray
  /**
   ** Returns an array containing all of the elements in the name collection;
   ** <br>
   ** If the collection fits in the specified array, it is returned therein.
   ** Otherwise, a new array is allocated with the runtime type of the specified
   ** array and the size of the name collection.
   ** <p>
   ** If the name collection fits in the specified array with room to spare
   ** (i.e., the array has more elements than the name collection), the element
   ** in the array immediately following the end of the collection is set to
   ** <code>null</code>.
   ** <br>
   ** (This is useful in determining the length of the name collection <i>only</i>
   ** if the caller knows that the name collection does not contain any
   ** <code>null</code> elements.)
   ** <p>
   ** If the name collection makes any guarantees as to what order its elements
   ** are returned by its iterator, this method must return the elements in the
   ** same order.
   ** <p>
   ** Like the {@link #toArray()} method, this method acts as bridge between
   ** array-based and collection-based APIs. Further, this method allows precise
   ** control over the runtime type of the output array, and may, under certain
   ** circumstances, be used to save allocation costs.
   ** <p>
   ** Suppose <code>x</code> is a collection known to contain only strings. The
   ** following code can be used to dump the collection into a newly allocated
   ** array of <code>String</code>:
   ** <pre>
   **   String[] y = x.toArray(new String[0]);
   ** </pre>
   ** Note that <code>toArray(new Object[0])</code> is identical in function to
   ** <code>toArray()</code>.
   **
   ** @param  <T>                the type if the array elements.
   ** @param  array              the array into which the elements of the name
   **                            collection are to be stored, if it is big
   **                            enough; otherwise, a new array of the same
   **                            runtime type is allocated for this purpose.
   **
   ** @return                    an array containing all of the elements in the
   **                            name collection.
   **
   ** @throws ArrayStoreException  if the runtime type of the specified array is
   **                              not a supertype of the runtime type of every
   **                              element in the name collection.
   ** @throws NullPointerException if the specified array is <code>null</code>.
     */
  public <T> T[] toArray(final T[] array) {
    return this.name == null ? null : this.name.toArray(array);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toArray
  /**
   ** Returns an array containing all of the elements in the name collection.
   ** <br>
   ** If the name collection makes any guarantees as to what order its elements
   ** are returned by its iterator, this method must return the elements in the
   ** same order.
   ** <p>
   ** The returned array will be "safe" in that no references to it are
   ** maintained by the name collection. (In other words, this method must
   ** allocate a new array even if the name collection is backed by an array).
   ** <br>
   ** The caller is thus free to modify the returned array.
   ** <p>
   ** This method acts as bridge between array-based and collection-based APIs.
   **
   ** @return                    an array containing all of the elements in the
   **                            name collection.
   */
  public Object[] toArray() {
    return this.name == null ? null : this.name.toArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredName
  /**
   ** Called to inject the argument for adding an attribute name where the
   ** content will be exported or skipped during parse.
   **
   ** @param  name               the argument for adding an attribute name where
   **                            the content will be exported or skipped during
   **                            parse.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added attribute name.
   */
  public void addConfiguredName(final Name name)
    throws BuildException {

    add(name.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Called to inject the argument for adding an attribute name where the
   ** content will be exported or skipped during parse.
   **
   ** @param  name               the argument for adding an attribute name where
   **                            the content will be exported or skipped during
   **                            parse.
   **
   ** @throws BuildException     if this instance is referencing an already
   **                            added attribute name.
   */
  public final void add(final String name)
    throws BuildException {

    if (isReference())
      throw noChildrenAllowed();

    if (this.name == null)
      this.name = new ArrayList<String>();

    if (this.name.contains(name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, name));

    this.name.add(name);
  }
}