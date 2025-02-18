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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   Path.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Path.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.BadRequestException;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.scim.utility.Parser;

public class Path implements Iterable<Path.Element> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String        namespace;
  private final List<Element> element;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Element
  // ~~~~~ ~~~~~~~
  /**
   ** This class represents an element of the path.
   */
  public static final class Element {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Filter filter;
    private final String attribute;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new path element.
     **
     ** @param  attribute        the attribute referenced by this path element.
     **                          <br>
     **                          Allowed object is <{@link String}.
     ** @param  filter           the optional value filter.
     **                          <br>
     **                          Allowed object is <{@link Filter}.
     */
    private Element(final String attribute, final Filter filter) {
  	  // ensure inheritance
      super();

  	  // initialize instance attributes
      this.attribute = attribute;
      this.filter    = filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: attribute
    /**
     ** Returns the attribute referenced by this path element.
     **
     ** @return                  the attribute referenced by this path element.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String attribute() {
      return this.attribute;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
     ** Returns the value filter that may be used to narrow down the values of
     ** the attribute referenced by this path element.
     **
     ** @return                  the value filter that may be used to narrow
     **                          down the values of the attribute referenced by
     **                          this path element or <code>null</code> if all
     **                          values are referened by this path element.
     **                          <br>
     **                          Possible object is {@link Filter}.
     */
    public Filter filter() {
      return this.filter;
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
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = this.attribute != null ? this.attribute.toLowerCase().hashCode() : 0;
      result = 31 * result + (this.filter != null ? this.filter.hashCode() : 0);
      return result;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>User</code>s filter are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>User</code>s filter may be different even though they
     ** contain the same set of names with the same values, but in a different
     ** order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Element that = (Element)other;
      if (!StringUtility.equalsWithIgnoreCase(this.attribute, that.attribute)) {
        return false;
      }
      return (this.filter == null ? that.filter == null : this.filter.equals(that.filter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character " " (space).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder();
      toString(builder);
      return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Append the string representation of the path element to the provided
     ** buffer.
     **
     ** @param  builder          the buffer to which the string representation
     **                          of the path element is to be appended.
     **                          <br>
     **                          Allowed object is {@link StringBuilder}.
     */
    public void toString(final StringBuilder builder) {
      builder.append(this.attribute);
      if (this.filter != null) {
        builder.append("[");
        builder.append(this.filter);
        builder.append("]");
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new path with the provided elements.
   **
   ** @param  namespace          the path namespace.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  element            the path elements.
   **                            <br>
   **                            Allowed object is {@link Element}.
   */
  private Path(final String namespace, final List<Element> element) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.namespace = namespace;
    this.element   = CollectionUtility.unmodifiableList(element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   root
  /**
   ** Whether this path targets the root of the JSON object that represents the
   ** resource or an schema extension.
   **
   ** @return                    <code>true</code> if this path targets the
   **                            root of the JSON object that represents the
   **                            SCIM resource or an schema extension;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean root() {
    return this.element.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of filter elements in this path.
   **
   ** @return                    the number of filter elements in this path.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int size() {
    return this.element.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the schema namespace URN of the attribute referenced by this path.
   **
   ** @return                    the schema URN of the attribute referenced by
   **                            this path or <code>null</code> if not
   **                            specified.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String namespace() {
    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** Returns the path element at the specified index.
   **
   ** @param  index              the index of the path element to retrieve.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the path element at the index.
   **                            <br>
   **                            Possible object is {@link Element}.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range
   **                                   (<code>index &lt; 0 || index &gt;= size()</code>)
   */
  public Element element(final int index)
    throws IndexOutOfBoundsException {

    return this.element.get(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** Returns an {@link Iterator} over the elements.
   **
   ** @return                    an {@link Iterator} over the elements.
   **                            <br>
   **                            Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<Element> iterator() {
    return this.element.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create  a path to the root of the JSON object that
   ** represents the SCIM resource.
   **
   ** @return                    the path to the root of the JSON object that
   **                            represents the SCIM resource.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public static Path build() {
    return new Path(null, Collections.<Element>emptyList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a path to the root of the JSON object that
   ** contains all the extension attributes of an extension schema defined by
   ** the provided class.
   **
   ** @param  extensionClass     the the extension class that defines the
   **                            extension schema.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  <T>                the generic type parameter of the Java class
   **                            used to represent the extension.
   **
   ** @return                    the path to the root of the JSON object that
   **                            contains all the extension attributes of an
   **                            extension schema.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public static <T> Path build(final Class<T> extensionClass) {
    return build(Support.namespace(extensionClass));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a path to the root of the JSON object that
   ** contains all the attributes of a schema.
   **
   ** @param  namespace          the schema namspace URN or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the path to the root of the JSON object that
   **                            contains all the attributes of an extension
   **                            URN.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public static Path build(final String namespace) {
    // prevent bogus input
    if (namespace != null && !Support.namespace(namespace))
      throw new IllegalArgumentException(String.format("Invalid extension schema URN: %s", namespace));

    return new Path(namespace, Collections.<Element>emptyList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a path to the root of the JSON object that
   ** contains the attribute of a schema.
   **
   ** @param  namespace          the schema namspace URN or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          the schema attribute or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the path to the root of the JSON object that
   **                            contains all the attributes of an extension
   **                            URN.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public static Path build(final String namespace, final String attribute) {
    // prevent bogus input
    if (namespace != null && !Support.namespace(namespace))
      throw new IllegalArgumentException(String.format("Invalid extension schema URN: %s", namespace));

    final List<Element> element = new ArrayList<Element>();
    element.add(new Element(attribute, null));
    return new Path(namespace, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a path parsing a string representation.
   **
   ** @param  expression         the string representation of the path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed path.
   **                            <br>
   **                            Possible object is {@link Path}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  @JsonCreator
  public static Path from(final String expression)
    throws BadRequestException {

    return Parser.path(expression);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.namespace != null ? this.namespace.toLowerCase().hashCode() : 0;
    return 31 * result + this.element.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>AttributeFilter</code> filter are considered equal if and only
   ** if they represent the same properties. As a consequence, two given
   ** <code>AttributeFilter</code> filter may be different even though they
   ** contain the same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Path that = (Path)other;
    if (this.namespace != null ? !this.namespace.equalsIgnoreCase(that.namespace) : that.namespace != null) {
      return false;
    }
    return this.element.equals(that.element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this {@link Path}.
   ** <p>
   ** The string representation consists of a list of the set's elements in the
   ** order they are returned by its iterator, enclosed in curly brackets
   ** (<code>"{}"</code>). Adjacent elements are separated by the characters
   ** <code>", "</code> (comma and space). Elements are converted to strings as
   ** by <code>Object.toString()</code>.
   **
   ** @return                    a string representation of this {@link Filter}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  @JsonValue
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    if(this.namespace != null) {
      builder.append(namespace);
      builder.append(":");
    }
    final Iterator<Element> i = this.element.iterator();
    while(i.hasNext()) {
      i.next().toString(builder);
      if(i.hasNext()) {
        builder.append(".");
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sub
  /**
   ** Factory method to create a new path from beginning portion of this path to
   ** the specified index (exclusive).
   ** <br>
   ** The last element in the newly created path will be the provided index - 1.
   **
   ** @param  index              the exclusive index of the endpoint path
   **                            element.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a new path to a beginning portion of this path.
   **                            <br>
   **                            Possible object is {@link Path}.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range
   **                                   (<code>index &lt; 0 || index &gt; size()</code>)
   */
  public Path sub(final int index) throws IndexOutOfBoundsException {
    return new Path(this.namespace, this.element.subList(0, index));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Factory method to create a new path to a sub-attribute for the attribute
   ** referenced by this path.
   **
   ** @param  attribute          the name of the sub-attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new path to a sub-attribute for the attribute
   **                            referenced by this path.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public Path attribute(final String attribute) {
    final List<Element> element = new ArrayList<Element>(this.element);
    element.add(new Element(attribute, null));
    return new Path(this.namespace, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Factory method to create a new path to a sub-set of values of a
   ** sub-attribute of the attribute referenced by this path.
   **
   ** @param  attribute          the name of the sub-attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  filter             the value filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    a new path to a sub-attribute of the attribute
   **                            referenced by this path.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public Path attribute(final String attribute, final Filter filter) {
    final List<Element> element = new ArrayList<Element>(this.element);
    element.add(new Element(attribute, filter));
    return new Path(this.namespace, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Factory method to create a new path to the sub-attribute path of the
   ** attribute referenced by this path.
   **
   ** @param  path               the path of the sub-attribute.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    a new path to a sub-attribute of the attribute
   **                            referenced by this path.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public Path attribute(final Path path) {
    final List<Element> element = new ArrayList<Element>(this.element.size() + path.size());
    element.addAll(this.element);
    element.addAll(path.element);
    return new Path(this.namespace, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a path where the attribute at the specified index
   ** is replaced with the one provided.
   **
   ** @param  index              the index of the element to replace.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  attribute          the replacement attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new path.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public Path replace(final int index, final String attribute) {
    final List<Element> element = new ArrayList<Element>(this.element);
    element.set(index, new Element(attribute, this.element.get(index).filter()));
    return new Path(this.namespace, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a path where the filter at the specified index is
   ** replaced with the one provided.
   ** @param  index              the index of the element to replace.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  filter             the replacement value filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the new path.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public Path replace(final int index, final Filter filter) {
    final List<Element> element = new ArrayList<Element>(this.element);
    element.set(index, new Element(this.element.get(index).attribute(), filter));
    return new Path(this.namespace, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a path where the attribute and filter at the
   ** specified index is replaced with those provided.
   **
   ** @param  index              the index of the element to replace.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  attribute          the replacement attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  filter             the replacement value filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the new path.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public Path replace(final int index, final String attribute, final Filter filter) {
    final List<Element> element = new ArrayList<Element>(this.element);
    element.set(index, new Element(attribute, filter));
    return new Path(this.namespace, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   withoutFilters
  /**
   ** Factory method to create a new path from this path with any value filters
   ** removed.
   **
   ** @return                    a new path from this path with any value
   **                            filters removed.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  public Path withoutFilters() {
    final List<Element> element = new ArrayList<Element>(this.element.size());
    for (Element cursor : element) {
      element.add(new Element(cursor.attribute(), null));
    }
    return new Path(this.namespace, element);
  }
}