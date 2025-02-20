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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
///////////////////////////////////////////////////////////////////////////////
// class DirectoryAttribute
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryAttribute</code> implements the base functionality
 ** for describing an attribute object in a Directory Service.
 ** <br>
 ** An Directory Service object may have attributes or not. Some of them are
 ** mandatory for the functionality.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Set<AttributeInfo.Flags> NULL              = EnumSet.noneOf(AttributeInfo.Flags.class);
  /** mandatory */
  public static final Set<AttributeInfo.Flags> REQUIRED          = EnumSet.of(AttributeInfo.Flags.REQUIRED);
  /** mandatory but readonly */
  public static final Set<AttributeInfo.Flags> REQUIRED_READONLY = EnumSet.of(AttributeInfo.Flags.REQUIRED, AttributeInfo.Flags.NOT_UPDATEABLE);
  /** not returned by default and not readable*/
  public static final Set<AttributeInfo.Flags> NRBD_NOTREADABLE  = EnumSet.of(AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT, AttributeInfo.Flags.NOT_READABLE);
  /** operational attributes are not creatable, updatabale, returned by default */
  public static final Set<AttributeInfo.Flags> OPERATIONAL       = EnumSet.of(AttributeInfo.Flags.NOT_CREATABLE, AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  // ~~~~~ ~~~~
  /**
   ** The <code>Type</code> implements the base functionality for describing an
   ** attribute object in a Directory Service.
   ** <br>
   ** An Directory Service object may have attributes or not. Some of them are
   ** mandatory for the functionality.
   */
  public static class Type {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Class<?>                 type;
    private final Set<AttributeInfo.Flags> flags;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> which has a class type and flags.
     **
     ** @param  type             the implementation type attribute.
     **                          <br>
     **                          Allowed object is {@link Class}.
     ** @param  flag             the default value for this attribute.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          elemment is of type
     **                          {@link AttributeInfo.Flags}.
     */
    private Type(final Class<?> type, final Set<AttributeInfo.Flags> flags) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.type  = type;
      this.flags = CollectionUtility.unmodifiable(flags);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create an attribute with the specified name and flags.
     **
     ** @param  id               the identifier of the attribute in the
     **                          connector framework.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  add              the flags to add to the lookuped instance.
     **                          <br>
     **                          Allowed object is {@link AttributeInfo.Flags}.
     ** @param  remove           the flags to remove from the lookuped instance.
     **                          <br>
     **                          Allowed object is {@link AttributeInfo.Flags}.
     **
     ** @return                  an instance of an logical
     **                          <code>Attribute</code> with the specified
     **                          properties.
     **                          Possible object {@link AttributeInfo}.
     */
    public AttributeInfo build(final String id, final Set<AttributeInfo.Flags> add, final Set<AttributeInfo.Flags> remove) {
      final EnumSet<AttributeInfo.Flags> flags = this.flags.isEmpty() ? EnumSet.noneOf(AttributeInfo.Flags.class) : EnumSet.copyOf(this.flags);
      if (add != null) {
        flags.addAll(add);
      }
      if (remove != null) {
        flags.removeAll(remove);
      }
      return AttributeInfoBuilder.build(id, this.type, flags);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Entitlement
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>Entitlement</code> implements the base functionality for
   ** describing an entitlement assignment object in a Directory Service.
   ** <br>
   ** An Directory Service object may have attributes or not. Some of them are
   ** mandatory for the functionality.
   */
  public static final class Entitlement<E> implements List<E> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final List     list;
    private final Class<E> clazz;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Element
    // ~~~~~ ~~~~~~~
    /**
     ** An iterator over a collection.
     */
    private final class Element implements Iterator<E> {

      ////////////////////////////////////////////////////////////////////////////
      // instance attributes
      ////////////////////////////////////////////////////////////////////////////

      private final Iterator delegate;

      ////////////////////////////////////////////////////////////////////////////
      // Constructors
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Element</code>s iterator which use the
       ** specified {@link Iterator} as its delegate.
       **
       ** @param  delegate       the iterator used as the delegate.
       **                        <br>
       **                        Allowed object is {@link Iterator}.
       */
      public Element(final Iterator iterator) {
        this.delegate = iterator;
      }

      ////////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: hasNext (Iterator)
      /**
       ** Returns <code>true</code> if this iterator has more elements when
       ** traversing the list direction. (In other words, returns
       ** <code>true</code> if {@link #next} would return an element rather than
       ** throwing an exception.)
       **
       ** @return                <code>true</code> if the list iterator has
       **                        more elements when traversing the list.
       */
      @Override
      public final boolean hasNext() {
        return this.delegate.hasNext();
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: next (Iterator)
      /**
       ** Returns the next element in the list
       **
       ** @return                the next element in the list.
       **
       ** @throws NoSuchElementException if the iteration has no next element.
       */
      @Override
      public final E next() {
        return DirectoryAttribute.Entitlement.this.cast(this.delegate.next());
      }

      ////////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: hashCode (overridden)
      /**
       ** Returns a hash code value for the object.
       ** <br>
       ** This method is supported for the benefit of hash tables such as those
       ** provided by {@link java.util.HashMap}.
       ** <p>
       ** The general contract of <code>hashCode</code> is:
       ** <ul>
       **   <li>Whenever it is invoked on the same object more than once during
       **       an execution of a Java application, the <code>hashCode</code>
       **       method must consistently return the same integer, provided no
       **       information used in <code>equals</code> comparisons on the object
       **       is modified. This integer need not remain consistent from one
       **       execution of an application to another execution of the same
       **       application.
       **   <li>If two objects are equal according to the
       **       <code>equals(Object)</code> method, then calling the
       **       <code>hashCode</code> method on each of the two objects must
       **       produce the same integer result.
       **   <li>It is <em>not</em> required that if two objects are unequal
       **       according to the {@link java.lang.Object#equals(java.lang.Object)}
       **       method, then calling the <code>hashCode</code> method on each of
       **       the two objects must produce distinct integer results. However,
       **       the programmer should be aware that producing distinct integer
       **       results for unequal objects may improve the performance of hash
       **       tables.
       ** </ul>
       **
       ** @return                a hash code value for this object.
       */
      @Override
      public int hashCode() {
        return this.delegate.hashCode();
      }

      /////////////////////////////////////////////////////////////////////////////
      // Method: equals (overriden)
      /**
       ** Compares this instance with the specified object.
       ** <p>
       ** The result is <code>true</code> if and only if the argument is not
       ** <code>null</code> and is a <code>Element</code> object that
       ** represents the same <code>name</code> and value as this object.
       **
       ** @param other           the object to compare this
       **                        <code>Element</code> against.
       **
       ** @return                <code>true</code> if the
       **                        <code>Element</code>s are
       **                        equal; <code>false</code> otherwise.
       */
      @Override
      public boolean equals(final Object other) {
        return this.delegate.equals(other);
      }

      //////////////////////////////////////////////////////////////////////////////
      // Method: toString (overridden)
      /**
       ** Returns a string representation of this instance.
       ** <br>
       ** Adjacent elements are separated by the character "\n" (line feed).
       ** Elements are converted to strings as by String.valueOf(Object).
       **
       ** @return                 the string representation of this instance.
       */
      @Override
      public String toString() {
        return this.delegate.toString();
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: remove (overriden)
      /**
       ** Removes from the list the last element that was returned by
       ** {@link #next} or {@link #previous} (optional operation).
       ** <br>
       ** This call can only be made once per call to {@link #next} or
       ** {@link #previous}.
       ** <br>
       ** It can be made only if {@link #add} has not been called after the last
       ** call to {@link #next} or {@link #previous}.
       **
       ** @throws UnsupportedOperationException if the {@code remove} operation is
       **                                       not supported by this list
       **                                       iterator.
       **
       ** @throws IllegalStateException         if neither {@link #next} nor
       **                                       {@link #previous} have been
       **                                       called, or {@code remove} or
       **                                       {@link #add} have been called
       **                                       after the last call to
       **                                       {@link #next} or
       **                                       {@link #previous}
       */
      @Override
      public final void remove() {
        this.delegate.remove();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class ElementList
    // ~~~~~ ~~~~~~~~~~~
    /**
     ** An iterator for lists that allows the programmer to traverse the list
     ** in either direction, modify the list during iteration, and obtain the
     ** iterator's current position in the list. A {@link ListIterator} has no
     ** current element; its <i>cursor position</i> always lies between the
     ** element that would be returned by a call to {@link #previous()} and the
     ** element that would be returned by a call to {@link #next()}.
     ** <br>
     ** An iterator for a list of length <code>n</code> has <code>n+1</code>
     ** possible cursor positions, as illustrated by the carets (<code>^</code>)
     ** below:
     ** <pre>
     **                       Element(0)   Element(1)   Element(2)   ... Element(n-1)
     **   cursor positions:  ^            ^            ^            ^                  ^
     ** </pre>
     ** <b>Note</b>:
     ** <br>
     ** The {@link #remove} and {@link #set(Object)} methods are <i>not</i>
     ** defined in terms of the cursor position; they are defined to operate on
     ** the last element returned by a call to {@link #next} or
     ** {@link #previous()}.
     */
    private final class ElementList implements ListIterator<E> {

      ////////////////////////////////////////////////////////////////////////////
      // instance attributes
      ////////////////////////////////////////////////////////////////////////////

      private final ListIterator delegate;

      ////////////////////////////////////////////////////////////////////////////
      // Constructors
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>ElementList</code>s iterator which use the
       ** specified {@link ListIterator} as its delegate.
       ** and flags.
       **
       ** @param  delegate       the iterator used as the delegate.
       **                        <br>
       **                        Allowed object is {@link ListIterator}.
       */
      public ElementList(final ListIterator iterator) {
        this.delegate = iterator;
      }

      ////////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: hasNext (ListIterator)
      /**
       ** Returns <code>true</code> if this list iterator has more elements when
       ** traversing the list in the forward direction. (In other words, returns
       ** <code>true</code> if {@link #next} would return an element rather than
       ** throwing an exception.)
       **
       ** @return                  <code>true</code> if the list iterator has more
       **                          elements when traversing the list in the
       **                          forward direction.
       */
      @Override
      public final boolean hasNext() {
        return this.delegate.hasNext();
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: next (ListIterator)
      /**
       ** Returns the next element in the list and advances the cursor position.
       ** <br>
       ** This method may be called repeatedly to iterate through the list, or
       ** intermixed with calls to {@link #previous} to go back and forth.
       ** <br>
       ** (Note that alternating calls to {@code next} and {@code previous} will
       ** return the same element repeatedly.)
       **
       ** @return                  the next element in the list.
       **
       ** @throws NoSuchElementException if the iteration has no next element.
       */
      @Override
      public final E next() {
        return DirectoryAttribute.Entitlement.this.cast(this.delegate.next());
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: hasPrevious (ListIterator)
      /**
       ** Returns <code>true</code> if this list iterator has more elements when
       ** traversing the list in the reverse direction. (In other words, returns
       ** <code>true</code> if {@link #previous} would return an element rather
       ** than throwing an exception.)
       **
       ** @return                  <code>true</code> if the list iterator has more
       **                          elements when traversing the list in the
       **                          reverse direction.
       */
      @Override
      public final boolean hasPrevious() {
        return this.delegate.hasPrevious();
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: previous (ListIterator)
      /**
       ** Returns the previous element in the list and moves the cursor position
       ** backwards.
       ** <br>
       ** This method may be called repeatedly to iterate through the list
       ** backwards, or intermixed with calls to {@link #next} to go back and
       ** forth.
       ** <br>
       ** (Note that alternating calls to {@code next} and {@code previous} will
       ** return the same element repeatedly.)
       **
       ** @return                  the previous element in the list.
       **
       ** @throws NoSuchElementException if the iteration has no previous element.
       */
      @Override
      public final E previous() {
        return DirectoryAttribute.Entitlement.this.cast(this.delegate.previous());
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: nextIndex (ListIterator)
      /**
       ** Returns the index of the element that would be returned by a subsequent
       ** call to {@link #next}. (Returns list size if the list iterator is at the
       ** end of the list.)
       **
       ** @return                  the index of the element that would be returned
       **                          by a subsequent call to {@link #next}, or list
       **                          size if the list iterator is at the end of the
       **                          list.
       */
      @Override
      public final int nextIndex() {
        return this.delegate.nextIndex();
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: previousIndex (ListIterator)
      /**
       ** Returns the index of the element that would be returned by a subsequent
       ** call to {@link #previous}. (Returns <code>-1</code> if the list iterator
       ** is at the beginning of the list.)
       **
       ** @return                  the index of the element that would be returned
       **                          by a subsequent call to {@link #previous}, or
       **                          <code>-1</code> if the list iterator is at the
       **                          beginning of the list.
       */
      @Override
      public final int previousIndex() {
        return this.delegate.previousIndex();
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: remove (ListIterator)
      /**
       ** Removes from the list the last element that was returned by
       ** {@link #next} or {@link #previous} (optional operation).
       ** <br>
       ** This call can only be made once per call to {@link #next} or
       ** {@link #previous}.
       ** <br>
       ** It can be made only if {@link #add} has not been called after the last
       ** call to {@link #next} or {@link #previous}.
       **
       ** @throws UnsupportedOperationException if the {@code remove} operation is
       **                                       not supported by this list
       **                                       iterator.
       **
       ** @throws IllegalStateException         if neither {@link #next} nor
       **                                       {@link #previous} have been
       **                                       called, or {@code remove} or
       **                                       {@link #add} have been called
       **                                       after the last call to
       **                                       {@link #next} or
       **                                       {@link #previous}
       */
      @Override
      public final void remove() {
        this.delegate.remove();
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: set (ListIterator)
      /**
       ** Replaces the last element returned by {@link #next} or {@link #previous}
       ** with the specified element (optional operation).
       ** <br>
       ** This call can be made only if neither {@link #remove} nor {@link #add}
       ** have been called after the last call to {@link #next} or
       ** {@link #previous}.
       **
       ** @param  e                the element with which to replace the last
       **                          element returned by {@link #next} or
       **                          {@link #previous}.
       **
       ** @throws UnsupportedOperationException if the {@code set} operation is
       **                                       not supported by this list
       **                                       iterator.
       ** @throws ClassCastException            if the class of the specified
       **                                       element prevents it from being
       **                                       added to this list.
       ** @throws IllegalArgumentException      if some aspect of the specified
       **                                       element prevents it from being
       **                                       added to this iterator.
       ** @throws IllegalStateException         if neither {@link #next} nor
       **                                       {@link #previous} have been
       **                                       called, or {@link #remove} or
       **                                       {@link #add} have been called
       **                                       after the last call to
       **                                       {@link #next} or
       **                                       {@link #previous}.
       */
      @Override
      public final void set(final E o) {
        this.delegate.set(o);
      }

      ////////////////////////////////////////////////////////////////////////////
      // Method: add (ListIterator)
      /**
       ** Inserts the specified element into the list (optional operation).
       ** <br>
       ** The element is inserted immediately before the element that would be
       ** returned by {@link #next}, if any, and after the element that would be
       ** returned by {@link #previous}, if any. (If the list contains no
       ** elements, the new element becomes the sole element on the list.) The new
       ** element is inserted before the implicit cursor: a subsequent call to
       ** {@link #next} would be unaffected, and a subsequent call to
       ** {@link #previous} would return the new element.
       ** <br>
       ** (This call increases by one the value that would be returned by a call
       ** to {@link #nextIndex} or {@link #previousIndex}.)
       **
       ** @param  e                the element to insert.
       **
       ** @throws UnsupportedOperationException if the {@code add} method is not
       **                                       supported by this list iterator.
       ** @throws ClassCastException            if the class of the specified
       **                                       element prevents it from being
       **                                       added to this list iterator.
       ** @throws IllegalArgumentException      if some aspect of this element
       **                                       prevents it from being added to
       **                                       this list iterator.
       */
      @Override
      public final void add(E o) {
        this.delegate.add(o);
      }

      ////////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      ////////////////////////////////////////////////////////////////////////////

      ////////////////////////////////////////////////////////////////////////////
      // Method: hashCode (overridden)
      /**
       ** Returns a hash code value for the object.
       ** <br>
       ** This method is supported for the benefit of hash tables such as those
       ** provided by {@link java.util.HashMap}.
       ** <p>
       ** The general contract of <code>hashCode</code> is:
       ** <ul>
       **   <li>Whenever it is invoked on the same object more than once during
       **       an execution of a Java application, the <code>hashCode</code>
       **       method must consistently return the same integer, provided no
       **       information used in <code>equals</code> comparisons on the object
       **       is modified. This integer need not remain consistent from one
       **       execution of an application to another execution of the same
       **       application.
       **   <li>If two objects are equal according to the
       **       <code>equals(Object)</code> method, then calling the
       **       <code>hashCode</code> method on each of the two objects must
       **       produce the same integer result.
       **   <li>It is <em>not</em> required that if two objects are unequal
       **       according to the {@link java.lang.Object#equals(java.lang.Object)}
       **       method, then calling the <code>hashCode</code> method on each of
       **       the two objects must produce distinct integer results. However,
       **       the programmer should be aware that producing distinct integer
       **       results for unequal objects may improve the performance of hash
       **       tables.
       ** </ul>
       **
       ** @return                a hash code value for this object.
       */
      @Override
      public int hashCode() {
        return this.delegate.hashCode();
      }

      /////////////////////////////////////////////////////////////////////////////
      // Method: equals (overriden)
      /**
       ** Compares this instance with the specified object.
       ** <p>
       ** The result is <code>true</code> if and only if the argument is not
       ** <code>null</code> and is a <code>ElementList</code> object that
       ** represents the same <code>name</code> and value as this object.
       **
       ** @param other           the object to compare this
       **                        <code>ElementList</code> against.
       **
       ** @return                <code>true</code> if the
       **                        <code>ElementList</code>s are
       **                        equal; <code>false</code> otherwise.
       */
      @Override
      public boolean equals(Object obj) {
        return this.delegate.equals(obj);
      }

      //////////////////////////////////////////////////////////////////////////////
      // Method: toString (overridden)
      /**
       ** Returns a string representation of this instance.
       ** <br>
       ** Adjacent elements are separated by the character "\n" (line feed).
       ** Elements are converted to strings as by String.valueOf(Object).
       **
       ** @return                 the string representation of this instance.
       */
      @Override
      public String toString() {
        return this.delegate.toString();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Entitlement</code> which has a value collection and
     ** a type.
     **
     ** @param  flag             the collection.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          elemment is of type <code>E</code>.
     ** @param  type             the implementation type attribute.
     **                          <br>
     **                          Allowed object is {@link Class}.
     */
    private Entitlement(final List list, final Class<E> type) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.clazz = type;
      this.list  = list;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: size (List)
    /**
     ** Returns the number of elements in this list.
     ** <br>
     ** If this list contains more than <code>Integer.MAX_VALUE</code> elements,
     ** returns <code>Integer.MAX_VALUE</code>.
     **
     ** @return                  the number of elements in this list.
     **                          Possible object <code>int</code>.
     */
    @Override
    public final int size() {
      return this.list.size();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isEmpty (List)
    /**
     ** Returns <code>true</code> if this list contains no elements.
     **
     ** @return                  <code>true</code> if this list contains no
     **                          elements; otherwise <code>false</code>.
     **                          Possible object <code>boolean</code>.
     */
    @Override
    public final boolean isEmpty() {
      return this.list.isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: contains (List)
    /**
     ** Returns <code>true</code> if this list contains the specified element.
     ** <br>
     ** More formally, returns <code>true</code> if and only if this list
     ** contains at least one element <code>e</code> such that
     ** <code>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</code>.
     **
     ** @param  other            the element whose presence in this list is to
     **                          be tested.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this list contains the
     **                          specified element.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws ClassCastException   if the type of the specified element is
     **                              incompatible with this list
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws NullPointerException if the specified element is
     **                              <code>null</code> and this list does not
     **                              permit <code>null</code> elements
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public final boolean contains(final Object other) {
      return this.list.contains(other);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: iterator (List)
    /**
     ** Returns an iterator over the elements in this list in proper sequence.
     **
     ** @return                  an iterator over the elements in this list in
     **                          proper sequence.
     **                          <br>
     **                          Possible object is {@link Iterator}.
     */
    @Override
    public final Iterator<E> iterator() {
      return new Element(this.list.iterator());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toArray (List)
    /**
     ** Returns an array containing all of the elements in this list in proper
     ** sequence (from first to last element).
     ** <p>
     ** The returned array will be "safe" in that no references to it are
     ** maintained by this list.  (In other words, this method must allocate a
     ** new array even if this list is backed by an array).
     ** <br>
     ** The caller is thus free to modify the returned array.
     ** <p>
     ** This method acts as bridge between array-based and collection-based
     ** APIs.
     **
     ** @return                  an array containing all of the elements in this
     **                          list in proper sequence.
     */
    @Override
    public final Object[] toArray() {
      return this.list.toArray();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toArray (List)
    /**
     ** Returns an array containing all of the elements in this list in proper
     ** sequence (from first to last element); the runtime type of the returned
     ** array is that of the specified array.
     ** <br>
     ** If the list fits in the specified array, it is returned therein.
     ** Otherwise, a new array is allocated with the runtime type of the
     ** specified array and the size of this list.
     ** <p>
     ** If the list fits in the specified array with room to spare (i.e., the
     ** array has more elements than the list), the element in the array
     ** immediately following the end of the list is set to <code>null</code>.
     ** (This is useful in determining the length of the list <i>only</i> if the
     ** caller knows that the list does not contain any null elements.)
     ** <p>
     ** Like the {@link #toArray()} method, this method acts as bridge between
     ** array-based and collection-based APIs. Further, this method allows
     ** precise control over the runtime type of the output array, and may,
     ** under certain circumstances, be used to save allocation costs.
     ** <p>
     ** Suppose <code>x</code> is a list known to contain only strings. The
     ** following code can be used to dump the list into a newly allocated array
     ** of <code>String</code>:
     ** <pre>
     **   String[] y = x.toArray(new String[0]);
     ** </pre>
     ** <b>Note</b>:
     ** <br>
     ** The <code>toArray(new Object[0])</code> is identical in function to
     ** <code>toArray()</code>.
     **
     ** @param  <T>              the expected class type of the list element.
     ** @param  a                the array into which the elements of this list
     **                          are to be stored, if it is big enough;
     **                          otherwise, a new array of the same runtime type
     **                          is allocated for this purpose.
     **
     ** @return                  an array containing the elements of this list.
     **
     ** @throws ArrayStoreException  if the runtime type of the specified array
     **                              is not a supertype of the runtime type of
     **                              every element in this list.
     ** @throws NullPointerException if the specified array is
     **                              <code>null</code>.
     */
    @Override
    public final <T> T[] toArray(final T[] a) {
      final Object[] result = (T[])this.list.toArray(a);
      for (Object o : result) {
        cast(o);
      }
      return (T[])result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add (List)
    /**
     ** Appends the specified element to the end of this list (optional
     ** operation).
     ** <p>
     ** Lists that support this operation may place limitations on what elements
     ** may be added to this list. In particular, some lists will refuse to add
     ** <code>null</code> elements, and others will impose restrictions on the
     ** type of elements that may be added. List classes should clearly specify
     ** in their documentation any restrictions on what elements may be added.
     **
     ** @param  e                element to be appended to this list.
     **
     ** @return                  <code>true</code> (as specified by
     **                          {@link Collection#add}).
     **
     ** @throws UnsupportedOperationException if the <code>add</code> operation
     **                                       is not supported by this list.
     ** @throws ClassCastException            if the class of the specified
     **                                       element prevents it from being
     **                                       added to this list.
     ** @throws NullPointerException          if the specified element is
     **                                       <code>null</code> and this list
     **                                       does not permit <code>null</code>
     **                                       elements.
     ** @throws IllegalArgumentException     if some property of this element
     **                                      prevents it from being added to
     **                                      this list.
     */
    @Override
    public final boolean add(final E e) {
      return this.list.add(e);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (List)
    /**
     ** Removes the first occurrence of the specified element from this list, if
     ** it is present (optional operation).  If this list does not contain the
     ** element, it is unchanged. More formally, removes the element with the
     ** lowest index <code>i</code> such that
     ** <code>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</code>
     ** (if such an element exists).
     ** <br>
     ** Returns <code>true</code> if this list contained the specified element
     ** (or equivalently, if this list changed as a result of the call).
     **
     ** @param  o                element to be removed from this list, if
     **                          present.
     **
     ** @return                  <code>true</code> if this list contained the
     **                          specified element.
     **
     ** @throws ClassCastException   if the type of the specified element is
     **                              incompatible with this list.
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws NullPointerException if the specified element is null and this
     **                              list does not permit <code>null</code>
     **                              elements
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws UnsupportedOperationException if the <code>remove</code>
     **                                       operation is not supported by this
     **                                       list
     */
    @Override
    public final boolean remove(final Object o) {
      return this.list.remove(o);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: containsAll (List)
    /**
     ** Returns <code>true</code> if this list contains all of the elements of
     ** the specified collection.
     **
     ** @param  c                the collection to be checked for containment in
     **                          this list.
     **
     ** @return                  <code>true</code> if this list contains all of
     **                          the elements of the specified collection:
     ** @throws ClassCastException   if the types of one or more elements in the
     **                              specified collection are incompatible with
     **                              this list
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws NullPointerException if the specified collection contains one or
     **                              more <code>null</code> elements and this
     **                              list does not permit <code>null</code>
     **                              elements
     **                              (<a href="Collection.html#optional-restrictions">optional</a>),
     **                              or if the specified collection is
     **                              <code>null</code>.
     **
     ** @see    #contains(Object)
     */
    @Override
    public final boolean containsAll(final Collection<?> c) {
      return this.list.containsAll(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addAll (List)
    /**
     ** Appends all of the elements in the specified collection to the end of
     ** this list, in the order that they are returned by the specified
     ** collection's iterator (optional operation).
     ** <br>
     ** The behavior of this operation is undefined if the specified collection
     ** is modified while the operation is in progress. (Note that this will
     ** occur if the specified collection is this list, and it's nonempty.)
     **
     ** @param  c                collection containing elements to be added to
     **                          this list.
     **
     ** @return                  <code>true</code> if this list changed as a
     **                          result of the call.
     **
     ** @throws UnsupportedOperationException if the <code>addAll</code>
     **                                       operation is not supported by this
     **                                       list.
     ** @throws ClassCastException            if the class of an element of the
     **                                       specified collection prevents it
     **                                       from being added to this list.
     ** @throws NullPointerException          if the specified collection
     **                                       contains one or more
     **                                       <code>null</code> elements and
     **                                       this list does not permit
     **                                       <code>null</code> elements, or if
     **                                       the specified collection is
     **                                       <code>null</code>.
     ** @throws IllegalArgumentException      if some property of an element of
     **                                       the specified collection prevents
     **                                       it from being added to this list.
     **
     ** @see    #add(Object)
     */
    @Override
    public final boolean addAll(final Collection<? extends E> c) {
      return this.list.addAll(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addAll (List)
    /**
     ** Inserts all of the elements in the specified collection into this list
     ** at the specified position (optional operation). Shifts the element
     ** currently at that position (if any) and any subsequent elements to the
     ** right (increases their indices). The new elements will appear in this
     ** list in the order that they are returned by the specified collection's
     ** iterator. The behavior of this operation is undefined if the specified
     ** collection is modified while the operation is in progress. (Note that
     ** this will occur if the specified collection is this list, and it's
     ** nonempty.)
     **
     ** @param  index            the index at which to insert the first element
     **                          from the specified collection
     ** @param  c                collection containing elements to be added to
     **                          this list.
     **
     ** @return                  <code>true</code> if this list changed as a
     **                          result of the call.
     **
     ** @throws UnsupportedOperationException if the <code>addAll</code>
     **                                       operation is not supported by this
     **                                       list.
     ** @throws ClassCastException            if the class of an element of the
     **                                       specified collection prevents it
     **                                       from being added to this list.
     ** @throws NullPointerException          if the specified collection
     **                                       contains one or more
     **                                       <code>null</code> elements and
     **                                       this list does not permit
     **                                       <code>null</code> elements, or if
     **                                       the specified collection is
     **                                       <code>null</code>.
     ** @throws IllegalArgumentException      if some property of an element of
     **                                       the specified collection prevents
     **                                       it from being added to this list.
     ** @throws IndexOutOfBoundsException     if the index is out of range
     **                                       (<code>index &lt; 0 || index &gt; size()</code>)
     */
    @Override
    public final boolean addAll(final int index, final Collection<? extends E> c) {
      return this.list.addAll(index, c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: removeAll (List)
    /**
     ** Removes from this list all of its elements that are contained in the
     ** specified collection (optional operation).
     **
     ** @param  c                the collection containing elements to be
     **                          removed from this list.
     **
     ** @return                  <code>true</code> if this list changed as a
     **                          result of the call.
     **
     ** @throws UnsupportedOperationException if the <code>removeAll</code>
     **                                       operation is not supported by this
     **                                       list.
     ** @throws ClassCastException            if the class of an element of this
     **                                       list is incompatible with the
     **                                       specified collection
     **                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws NullPointerException if the specified collection contains one or
     **                              more <code>null</code> elements and this
     **                              list does not permit <code>null</code>
     **                              elements
     **                              (<a href="Collection.html#optional-restrictions">optional</a>),
     **                              or if the specified collection is
     **                              <code>null</code>.
     **
     ** @see    #remove(Object)
     ** @see    #contains(Object)
     */
    @Override
    public final boolean removeAll(final Collection<?> c) {
      return this.list.removeAll(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: retainAll (List)
    /**
     ** Retains only the elements in this list that are contained in the
     ** specified collection (optional operation). In other words, removes from
     ** this list all of its elements that are not contained in the specified
     ** collection.
     **
     ** @param  c                the collection containing elements to be
     **                          retained in this list.
     **
     ** @return                  <code>true</code> if this list changed as a
     **                          result of the call.
     **
     ** @throws UnsupportedOperationException if the <code>retainAll</code>
     **                                       operation is not supported by this
     **                                       list.
     ** @throws ClassCastException            if the class of an element of this
     **                                       list is incompatible with the
     **                                       specified collection
     **                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws NullPointerException          if this list contains a
     **                                       <code>null</code> element and the
     **                                       specified collection does not
     **                                       permit <code>null</code> elements
     **                                       (<a href="Collection.html#optional-restrictions">optional</a>),
     **                                       or if the specified collection is
     **                                       <code>null</code>
     **
     ** @see    #remove(Object)
     ** @see    #contains(Object)
     */
    @Override
    public final boolean retainAll(final Collection<?> c) {
      return this.list.retainAll(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: clear (List)
    /**
     ** Removes all of the elements from this list (optional operation).
     ** <br>
     ** The list will be empty after this call returns.
     **
     ** @throws UnsupportedOperationException if the <code>clear</code>
     **                                       operation is not supported by this
     **                                       list.
     */
    @Override
    public final void clear() {
      this.list.clear();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (List)
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public final int hashCode() {
      return this.list.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (List)
    /**
     ** Compares the specified object with this list for equality.
     ** <br>
     ** Returns <code>true</code> if and only if the specified object is also a
     ** list, both lists have the same size, and all corresponding pairs of
     ** elements in the two lists are <i>equal</i>. (Two elements
     ** <code>e1</code> and <code>e2</code> are <i>equal</i> if
     ** <code>(e1==null ? e2==null : e1.equals(e2))</code>.)
     ** <br>
     ** In other words, two lists are defined to be equal if they contain the
     ** same elements in the same order. This definition ensures that the equals
     ** method works properly across different implementations of the
     ** <code>List</code> interface.
     **
     ** @param  o                the object to be compared for equality with
     **                          this list.
     **
     ** @return                  <code>true</code> if the specified object is
     **                          equal to this list.
     */
    @Override
    public final boolean equals(Object o) {
      return this.list.equals(o);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: get (List)
    /**
     ** Returns the element at the specified position in this list.
     **
     ** @param  index            the index of the element to return.
     **
     ** @return                  the element at the specified position in this
     **                          list.
     **
     ** @throws IndexOutOfBoundsException if the index is out of range
     **                                   (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    @Override
    public final E get(int index) {
      return cast(this.list.get(index));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: set (List)
    /**
     ** Replaces the element at the specified position in this list with the
     ** specified element (optional operation).
     **
     ** @param  index            the index of the element to replace.
     ** @param  element          the element to be stored at the specified
     **                          position.
     **
     ** @return                  the element previously at the specified
     **                          position.
     **
     ** @throws UnsupportedOperationException if the <code>set</code> operation
     **                                       is not supported by this list.
     ** @throws ClassCastException            if the class of the specified
     **                                       element prevents it from being
     **                                       added to this list.
     ** @throws NullPointerException          if the specified element is
     **                                       <code>null</code> and this list
     **                                       does not permit <code>null</code>
     **                                       elements.
     ** @throws IllegalArgumentException      if some property of the specified
     **                                       element prevents it from being
     **                                       added to this list.
     ** @throws IndexOutOfBoundsException     if the index is out of range
     **                                       (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    @Override
    public final E set(final int index, final E element) {
      return cast(this.list.set(index, element));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add (List)
    /**
     ** Inserts the specified element at the specified position in this list
     ** (optional operation).
     ** <br>
     ** Shifts the element currently at that position (if any) and any
     ** subsequent elements to the right (adds one to their indices).
     **
     ** @param  index            the index at which the specified element is to
     **                          be inserted.
     ** @param  element          the element to be inserted.
     **
     ** @throws UnsupportedOperationException if the <code>add</code> operation
     **                                       is not supported by this list.
     ** @throws ClassCastException            if the class of the specified
     **                                       element prevents it from being
     **                                       added to this list.
     ** @throws NullPointerException          if the specified element is
     **                                       <code>null</code> and this list
     **                                       does not permit null elements.
     ** @throws IllegalArgumentException      if some property of the specified
     **                                        element prevents it from being
     **                                        added to this list.
     ** @throws IndexOutOfBoundsException      if the index is out of range
     **                                        (<code>index &lt; 0 || index &gt; size()</code>)
     */
    @Override
    public final void add(final int index, final E element) {
      this.list.add(index, element);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (List)
    /**
     ** Removes the element at the specified position in this list (optional
     ** operation).
     ** <br>
     ** Shifts any subsequent elements to the left (subtracts one from their
     ** indices).
     ** <br>
     ** Returns the element that was removed from the list.
     **
     ** @param  index            the index of the element to be removed.
     **
     ** @return                  the element previously at the specified
     **                          position.
     **
     ** @throws UnsupportedOperationException if the <code>remove</code>
     **                                       operation is not supported by this
     **                                       list.
     ** @throws IndexOutOfBoundsException     if the index is out of range
     **                                       (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    @Override
    public final E remove(final int index) {
      return cast(this.list.remove(index));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: indexOf (List)
    /**
     ** Returns the index of the first occurrence of the specified element in
     ** this list, or <code>-1</code> if this list does not contain the element.
     ** <br>
     ** More formally, returns the lowest index <code>i</code> such that
     ** <code>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</code>,
     ** or <code>-1</code> if there is no such index.
     **
     ** @param  o                element to search for.
     **
     ** @return                  the index of the first occurrence of the
     **                          specified element in this list, or
     **                          <code>-1</code> if this list does not contain
     **                          the element
     **
     ** @throws ClassCastException   if the type of the specified element is
     **                              incompatible with this list
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws NullPointerException if the specified element is null and this
     **                              list does not permit null elements
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public final int indexOf(final Object o) {
      return this.list.indexOf(o);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: lastIndexOf (List)
    /**
     ** Returns the index of the last occurrence of the specified element in
     ** this list, or <code>-1</code> if this list does not contain the element.
     ** <br>
     ** More formally, returns the highest index <code>i</code> such that
     ** <code>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</code>,
     ** or <code>-1</code> if there is no such index.
     **
     **
     ** @param  o                the element to search for.
     **
     ** @return                  the index of the last occurrence of the
     **                          specified element in this list, or
     **                          <code>-1</code> if this list does not contain
     **                          the element.
     **
     ** @throws ClassCastException   if the type of the specified element is
     **                              incompatible with this list
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     ** @throws NullPointerException if the specified element is null and this
     **                              list does not permit null elements
     **                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public final int lastIndexOf(final Object o) {
      return this.list.lastIndexOf(o);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: listIterator (List)
    /**
     ** Returns a list iterator over the elements in this list (in proper
     ** sequence).
     **
     ** @return                  a list iterator over the elements in this list
     **                          (in proper sequence).
     */
    @Override
    public final ListIterator<E> listIterator() {
      return new ElementList(this.list.listIterator());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: listIterator (List)
    /**
     ** Returns a list iterator over the elements in this list (in proper
     ** sequence), starting at the specified position in the list.
     ** <br>
     ** The specified index indicates the first element that would be returned
     ** by an initial call to {@link ListIterator#next next}.
     ** <br>
     ** An initial call to {@link ListIterator#previous previous} would return
     ** the element with the specified index minus one.
     **
     ** @param  index            the index of the first element to be returned
     **                          from the list iterator (by a call to
     **                          {@link ListIterator#next next}).
     **
     ** @return                   a list iterator over the elements in this list
     **                           (in proper sequence), starting at the
     **                           specified position in the list.
     **
     ** @throws IndexOutOfBoundsException if the index is out of range
     **                                   ({@code index < 0 || index > size()}).
     */
    @Override
    public final ListIterator<E> listIterator(final int index) {
      return new ElementList(this.list.listIterator(index));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: subList (List)
    /**
     ** Returns a view of the portion of this list between the specified
     ** <code>fromIndex</code>, inclusive, and <code>toIndex</code>, exclusive.
     ** (If <code>fromIndex</code> and <code>toIndex</code> are equal, the
     ** returned list is empty.) The returned list is backed by this list, so
     ** non-structural changes in the returned list are reflected in this list,
     ** and vice-versa.
     ** <br>
     ** The returned list supports all of the optional list operations supported
     ** by this list.
     ** <p>
     ** This method eliminates the need for explicit range operations (of the
     ** sort that commonly exist for arrays). Any operation that expects a list
     ** can be used as a range operation by passing a subList view instead of a
     ** whole list. For example, the following idiom removes a range of
     ** elements from a list:
     ** <pre>
     **   list.subList(from, to).clear();
     ** </pre>
     ** Similar idioms may be constructed for <code>indexOf</code> and
     ** <code>lastIndexOf</code>, and all of the algorithms in the
     ** <code>Collections</code> class can be applied to a subList.
     ** <p>
     ** The semantics of the list returned by this method become undefined if
     ** the backing list (i.e., this list) is <i>structurally modified</i> in
     ** any way other than via the returned list. (Structural modifications are
     ** those that change the size of this list, or otherwise perturb it in such
     ** a fashion that iterations in progress may yield incorrect results.)
     **
     ** @param  fromIndex        low endpoint (inclusive) of the subList.
     ** @param  toIndex          high endpoint (exclusive) of the subList.
     **
     ** @return                  a view of the specified range within this list.
     **
     ** @throws IndexOutOfBoundsException for an illegal endpoint index value
     ** (<code>fromIndex &lt; 0 || toIndex &gt; size || fromIndex &gt; toIndex</code>)
     */
    @Override
    public final List<E> subList(int fromIndex, int toIndex) {
      return this.list.subList(fromIndex, toIndex);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: cast
    /**
     ** Casts an object to the class or interface represented by this
     ** {@code List} object.
     **
     ** @param  o                the object to be cast.
     **
     ** @return                  the object after casting, or <code>null</code>
     **                          if <code>o</code> is <code>null</code>.
     **
     ** @throws ClassCastException if the object is not <code>null</code> and is
     **                            not assignable to the type <code>T</code>.
     */
    private E cast(final Object o) {
      return this.clazz.cast(o);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryAttribute</code>.
   */
  private DirectoryAttribute() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an attribute with the specified string type and
   ** flags.
   **
   ** @param  flag               the default value for this attribute.
   **                            <br>
   **                            Allowed object is {@link Set} where each elemment
   **                            is of type {@link AttributeInfo.Flags}.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link DirectoryAttribute.Type}.
   */
  public static DirectoryAttribute.Type build(final Set<AttributeInfo.Flags> flag) {
    return build(String.class, flag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an attribute with the specified type and flags.
   **
   ** @param  type               the implementation type attribute.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  flag               the default value for this attribute.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            elemment is of type
   **                            {@link AttributeInfo.Flags}.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            {@link DirectoryAttribute.Type}.
   */
  public static DirectoryAttribute.Type build(final Class<?> type, final Set<AttributeInfo.Flags> flag) {
    return new DirectoryAttribute.Type(type, flag);
  }

  public static boolean groupEntitlement(final String name) {
    return "group".equalsIgnoreCase(name);
  }

  public static boolean posixEntitlement(final String name) {
    return "posix".equalsIgnoreCase(name);
  }

  public static boolean roleEntitlement(final String name) {
    return "role".equalsIgnoreCase(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Factory method to create an <code>Entitlement</code> which has a value
   ** collection and a type.
   **
   ** @param  <T>                the expected class type of the list element.
   ** @param  list               the collection of entitlements.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            elemment is of type <code>T</code>.
   ** @param  clazz              the implementation type attribute.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    an instance of
   **                            <code>DirectoryAttribute.Entitlement</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            {@link DirectoryAttribute.Entitlement}.
   */
  public static <T> List<T> entitlement(final List list, final Class<T> clazz) {
    return new Entitlement(list, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeAttribute
  public static Map<String, List<String>> encodeAttribute(final DirectorySchema schema, final ObjectClass type, final Set<Attribute> attribute, final BasicAttributes entry) {
    final Map<String, List<String>> entitlement = new HashMap<String, List<String>>();
    for (Attribute cursor : attribute) {
      if (!cursor.is(Name.NAME)) {
        if (groupEntitlement(cursor.getName()) || posixEntitlement(cursor.getName()) || roleEntitlement(cursor.getName())) {
          entitlement.put(cursor.getName(), entitlement(CollectionUtility.notNullList(cursor.getValue()), String.class));
        }
        else if (cursor.is(OperationalAttributes.PASSWORD_NAME)) {
          entry.put(encodePassword(schema, cursor).build());
        }
        else {
          entry.put(encodeAttribute(schema, type, cursor));
        }
      }
      if (cursor.is(Name.NAME)) {
        entry.put(encodeAttribute(schema, type, cursor));
      }
    }
    return entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeAttribute
  /**
   **
   ** @param  schema             the schema mapping to resolve and translate
   **                            object class names and their attributes.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  type               the name of the object class to obtain the
   **                            encoded attributes from the discovered schema
   **                            of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  attribute          the attribute to encode to its native
   **                            representation.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    an {@link BasicAttribute} populated from the
   **                            given {@link Attribute}.
   **                            <br>
   **                            Possible object is
   **                            {@link BasicAttribute}.
   */
  public static BasicAttribute encodeAttribute(final DirectorySchema schema, final ObjectClass type, final Attribute attribute) {
    /*
    if (attribute.is(OperationalAttributes.PASSWORD_NAME)) {
      throw new IllegalArgumentException(conn.format("improperMethodUsage", "This method should not be used for password attributes", new Object[0]));
    }

    if (attribute.is(OperationalAttributes.ENABLE_NAME)) {
      EnabledStrategy strategy = EnabledStrategyFactory.getInstance(conn);
      BasicAttribute enabledAttr = strategy.getEnabledAttribute(attr);
      return enabledAttr;
    }
    */

    final String name = schema.attribute(type, attribute.getName(), true);
    if (name == null) {
      return null;
    }
    final BasicAttribute encoded = new BasicAttribute(name);
    for (Object cursor : attribute.getValue()) {
      // Looks like some directory doesn't accept empty array. So in case of
      // an empty attribute, build BasicAttribute without any value
      if (cursor instanceof String && !((String)cursor).isEmpty())
        encoded.add(cursor);
    }
    return encoded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodePassword
  /**
   **
   ** @param  schema             the schema mapping to resolve and translate
   **                            object class names and their attributes.
   **                            <br>
   **                            Allowed object is {@link DirectorySchema}.
   ** @param  attribute          the attribute to encode to its native
   **                            representation.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @return                    an {@link DirectoryPassword} populated from the
   **                            given {@link Attribute}.
   **                            <br>
   **                            Possible object is {@link DirectoryPassword}.
   */
  public static DirectoryPassword encodePassword(final DirectorySchema schema, Attribute attribute) {
    final String  prefix = schema.endpoint().accountCredential();
    final boolean quoted = schema.endpoint().accountCredentialQuoted();
    for (Object cursor : attribute.getValue()) {
      GuardedString password = (GuardedString)cursor;
      if (quoted) {
         final char[] inter  = CredentialAccessor.sequence(password);
         final char[] buffer = new char[inter.length + 2];
         buffer[0] = '"';
         System.arraycopy(inter, 0, buffer, 1,inter.length);
         buffer[(buffer.length - 1)] = '"';
         password = new GuardedString(buffer);
      };
      return DirectoryPassword.build(prefix, password);
    }
    return DirectoryPassword.build(prefix);
  }
}