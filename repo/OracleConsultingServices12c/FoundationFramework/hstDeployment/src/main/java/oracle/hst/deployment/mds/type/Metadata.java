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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Metadata.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Metadata.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.1.0.0     2014-11-29  DSteding    First release version
*/

package oracle.hst.deployment.mds.type;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

////////////////////////////////////////////////////////////////////////////////
// class Metadata
// ~~~~~ ~~~~~~~~
/**
 ** The environment wrapper of a set of {@link Namespace}s in a Oracle
 ** Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Metadata extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Collection<Namespace> list = new ArrayList<Namespace>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class NamespaceIterator
  // ~~~~~ ~~~~~~~~~~~~~~~~~
  /**
   ** Helper class, to iterate over nested <code>namespace</code> values.
   */
  private class NamespaceIterator extends ResourceIterator {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final private Iterator<Namespace> delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>NamespaceIterator</code> that use the specified
     ** {@link Collection}.
     **
     ** @param  parts            the {@link Collection} with elements to
     **                          iterate.
     */
    public NamespaceIterator(final Collection<Namespace> parts) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.delegate = parts.iterator();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hasNext (Iterator)
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
      return this.delegate.hasNext();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: next (Iterator)
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
    public String next() {
     if (!hasNext())
        throw new NoSuchElementException();

      return this.delegate.next().path();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Metadata</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Metadata() {
    // ensure inheritance
    super();
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
   ** <code>Metadata</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof Metadata) {
      Metadata that = (Metadata)other;
      this.list.clear();
      this.list.addAll(that.list);
      // ensure inheritance
      super.setRefid(reference);
    }
    else {
      final Object[] parameter = {reference.getRefId(), "mds-metadata", reference.getRefId(), other.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter), getLocation());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: iterator (ResourceCollection)
  /**
   ** Return an {@link Iterator} over the contents of this
   ** <code>ResourceCollection</code>, whose elements are {@link String}
   ** instances.
   **
   ** @return                  an {@link Iterator} of Resources.
   */
  public Iterator<String> iterator() {
    return new NamespaceIterator(this.list);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isFilesystemOnly (ResourceCollection)
  /**
   ** Indicate whether this <code>ResourceCollection</code> is composed entirely
   ** of Resources accessible via local filesystem conventions.
   **
   ** @return                    whether this is a filesystem-only
   **                            resource collection.
   */
  public boolean isFilesystemOnly() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  size (ResourceCollection)
  /**
   ** Return the number of contained Resources.
   **
   ** @return                    number of elements as int.
   */
  public int size() {
    return this.list.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredNamespace
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Namespace}.
   **
   ** @param  namespace         the subject of maintenance.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Namespace</code>
   */
  public void addConfiguredNamespace(final Namespace namespace)
    throws BuildException {

    // check if we have this file already
    if (this.list.contains(namespace))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.METADATA_NAMESPACE_ONLYONCE, namespace.path()));

    // ensure inheritance
    this.list.add(namespace);
  }
}