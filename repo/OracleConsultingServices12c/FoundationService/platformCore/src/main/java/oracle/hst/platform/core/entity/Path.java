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

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Path.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Path.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class Path
// ~~~~~ ~~~~
/**
 ** This class represents a path to one or more entity values that are the
 ** targets of an operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Path implements Iterable<Path.Segment>{

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  private final String        schemaUrn;
  private final List<Segment> segment;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Segment
  // ~~~~~ ~~~~~~~
  /**
   ** This class represents a segment of the path.
   */
  public static final class Segment {

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
     ** Create a new path segment.
     **
     ** @param  attribute        the attribute referenced by this path segment.
     **                          <br>
     **                          Allowed object is <{@link String}.
     ** @param  filter           the optional value filter.
     **                          <br>
     **                          Allowed object is <{@link Filter}.
     */
    private Segment(final String attribute, final Filter filter) {
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
     ** Returns the attribute referenced by this path segment.
     **
     ** @return                  the attribute referenced by this path segment.
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
     ** the attribute referenced by this path segment.
     **
     ** @return                  the value filter that may be used to narrow
     **                          down the values of the attribute referenced by
     **                          this path segment or <code>null</code> if all
     **                          values are referened by this path segment.
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
      return Objects.hash(this.attribute, this.filter);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Segment</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Segment</code>s may be different even though they contain the same
     ** set of names with the same values, but in a different order.
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

      final Segment that = (Segment)other;
      return Objects.equals(this.attribute, that.attribute)
          && Objects.equals(this.filter,    that.filter)
      ;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder();
      append(builder);
      return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Append the string representation of the path segment to the provided
     ** buffer.
     **
     ** @param  builder          the buffer to which the string representation
     **                          of the path segment has to be appended.
     **                          <br>
     **                          Allowed object is {@link StringBuilder}.
     */
    public void append(final StringBuilder builder) {
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
   ** Create a new path with the provided segments.
   **
   ** @param  segment            the path segments.
   **                            <br>
   **                            Allowed object is {@link Segment}.
   */
  private Path(final String schemaUrn, final List<Segment> segment) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.schemaUrn = schemaUrn;
    this.segment   = Collections.unmodifiableList(segment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaUrn
  /**
   ** Retrieves the schema URN of the attribute referenced by this path.
   **
   ** @return The schema URN of the attribute referenced by this path or
   ** {@code null} if not specified.
   */
    public String schemaUrn(){
      return schemaUrn;
    }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   root
  /**
   ** Whether this path targets the root of the object path that represents the
   ** resource or an schema extension.
   **
   ** @return                    <code>true</code> if this path targets the
   **                            root of the object path that represents the
   **                            resource or an schema extension;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean root() {
    return this.segment.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   root
  /**
   ** Creates a path to the root of the JSON object that contains all the
   ** attributes of a schema.
   **
   ** @param schemaUrn The schema URN or {@code null}.
   **
   ** @return The path to the root of the JSON object that contains all the
   ** attributes of an extension URN.
   */
   public static Path root(final String schemaUrn){
     if (schemaUrn != null && !EntityParser.isUrn(schemaUrn))
     {
       throw new IllegalArgumentException(String.format("Invalid extension schema URN: %s",  schemaUrn));
     }
     return new Path(schemaUrn, Collections.<Segment>emptyList());
   }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of segments in this path.
   **
   ** @return                    the number of segments in this path.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int size() {
    return this.segment.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segment
  /**
   ** Returns the path segment at the specified index.
   **
   ** @param  index              the index of the path segment to retrieve.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the path segment at the index.
   **                            <br>
   **                            Possible object is {@link Segment}.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range
   **                                   (<code>index &lt; 0 || index &gt;= size()</code>)
   */
  public Segment segment(final int index)
    throws IndexOutOfBoundsException {

    return this.segment.get(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** Returns an {@link Iterator} over the segments.
   **
   ** @return                    an {@link Iterator} over the segments.
   **                            <br>
   **                            Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<Segment> iterator() {
    return this.segment.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a path to the root of the object path that
   ** represents the a resource.
   **
   ** @return                    the path to the root of the object path that
   **                            represents the resource.
   **                            <br>
   **                            Possible object is <code>Path</code>.
   */
  public static Path build() {
    return new Path(null, Collections.<Segment>emptyList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a path to the root of the object path that
   ** contains the attribute of a schema.
   **
   ** @param  attribute          the schema attribute name or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the path to the root of the object path that
   **                            contains all the attributes.
   **                            <br>
   **                            Possible object is <code>Path</code>.
   */
  public static Path build(final String attribute) {
    return new Path(null,CollectionUtility.list(new Segment(attribute, null)));
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
   **                            Possible object is <code>Path</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static Path from(final String expression)
    throws ParseException {

    return EntityParser.path(expression);
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
    return Objects.hash(this.segment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Path</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Path</code>s may be
   ** different even though they contain the same set of names with the same
   ** values, but in a different order.
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
    
    if (schemaUrn != null ? !schemaUrn.equalsIgnoreCase(that.schemaUrn) : that.schemaUrn != null){
      return false;
    }
    
    return Objects.equals(this.segment, that.segment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this <code>Path</code>.
   ** <p>
   ** The string representation consists of a list of the path segments in the
   ** order they are returned by its iterator. Adjacent segments are separated
   ** by the character <code>"."</code> (dot).
   **
   ** @return                    a string representation of this
   **                            <code>Path</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    if (schemaUrn != null){
      builder.append(schemaUrn);
      builder.append(":");
    }
    final Iterator<Segment> i = this.segment.iterator();
    while(i.hasNext()) {
      i.next().append(builder);
      if(i.hasNext()) {
        builder.append('.');
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
   ** The last segment in the newly created path will be the provided index - 1.
   **
   ** @param  index              the exclusive index of the endpoint path
   **                            segment.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a new path to a beginning portion of this path.
   **                            <br>
   **                            Possible object is <code>Path</code>.
   **
   ** @throws IndexOutOfBoundsException if the index is out of range
   **                                   (<code>index &lt; 0 || index &gt; size()</code>)
   */
  public Path sub(final int index)
    throws IndexOutOfBoundsException {

    return new Path(schemaUrn, this.segment.subList(0, index));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Factory method to create a new path to a sub-segment for the segments
   ** referenced by this path.
   **
   ** @param  attribute          the name of the sub-attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new path to a sub-attribute for the attribute
   **                            referenced by this path.
   **                            <br>
   **                            Possible object is <code>Path</code>.
   */
  public Path path(final String attribute) {
    return path(attribute, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
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
   **                            Possible object is <code>Path</code>.
   */
  public Path path(final String attribute, final Filter filter) {
    final List<Segment> segment = CollectionUtility.list(this.segment);
    segment.add(new Segment(attribute, filter));
    return new Path(schemaUrn, segment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Factory method to create a new path to the sub-attribute path of the
   ** attribute referenced by this path.
   **
   ** @param  path               the path of the sub-attribute.
   **                            <br>
   **                            Allowed object is <code>Path</code>.
   **
   ** @return                    a new path to a sub-attribute of the attribute
   **                            referenced by this path.
   **                            <br>
   **                            Possible object is <code>Path</code>.
   */
  public Path path(final Path path) {
    return new Path(schemaUrn, CollectionUtility.union(this.segment, path.segment));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a path where the attribute at the specified index
   ** is replaced with the one provided.
   **
   ** @param  index              the index of the segment to replace.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  attribute          the replacement attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new path.
   **                            <br>
   **                            Possible object is <code>JsonPath</code>.
   */
  public Path replace(final int index, final String attribute) {
    final List<Segment> segment = CollectionUtility.list(this.segment);
    segment.set(index, new Segment(attribute, this.segment.get(index).filter()));
    return new Path(schemaUrn, segment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a path where the filter at the specified index is
   ** replaced with the one provided.
   ** @param  index              the index of the segment to replace.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  filter             the replacement value filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the new path.
   **                            <br>
   **                            Possible object is <code>Path</code>.
   */
  public Path replace(final int index, final Filter filter) {
    final List<Segment> segment = CollectionUtility.list(this.segment);
    segment.set(index, new Segment(this.segment.get(index).attribute(), filter));
    return new Path(schemaUrn, segment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a path where the attribute and filter at the
   ** specified index is replaced with those provided.
   **
   ** @param  index              the index of the segment to replace.
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
   **                            Possible object is <code>Path</code>.
   */
  public Path replace(final int index, final String attribute, final Filter filter) {
    final List<Segment> segment = CollectionUtility.list(this.segment);
    segment.set(index, new Segment(attribute, filter));
    return new Path(schemaUrn, segment);
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
   **                            Possible object is <code>Path</code>.
   */
  public Path withoutFilters() {
    final List<Segment> segment = new ArrayList<Segment>(this.segment.size());
    for (Segment cursor : this.segment) {
      segment.add(new Segment(cursor.attribute(), null));
    }
    return new Path(schemaUrn, segment);
  }
}