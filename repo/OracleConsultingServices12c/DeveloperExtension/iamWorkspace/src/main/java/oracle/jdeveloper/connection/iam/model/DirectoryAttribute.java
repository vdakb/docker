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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryAttribute.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import java.util.Set;
import java.util.EnumSet;

import java.text.Collator;

import java.io.Serializable;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;
import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

///////////////////////////////////////////////////////////////////////////////
// class DirectoryAttribute
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryAttribute</code> implements the base functionality
 ** for describing an attribute type in a Directory Service.
 ** <br>
 ** An Directory Service object may have attributes or not. Some of them are
 ** mandatory for the functionality.
 ** <p>
 ** This is a schema-aware wrapper to BasicAttribute.
 ** <p>
 ** It goes to a lot of effort to figure out whether the attribute has string
 ** values, or contains 'byte array' values. If it contains byte array values,
 ** it also tries to figure out whether they are ASN1 values. This is important,
 ** as we need to make sure that byte array values are passed correctly to JNDI,
 ** and in addition we need to make sure that ASN1 values are passed using
 ** ';binary'.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryAttribute implements Comparable
                                ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  /**
   ** The most often used attribute.
   */
  public static final DirectoryAttribute OBJECTCLASS       = DirectoryAttribute.build(DirectorySchema.OBJECTCLASS);

  /**
   ** the {@link Collator} class performs locale-sensitive <code>String</code>
   ** comparison.
   */
  private static final Collator          COLLATOR         = Collator.getInstance();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2570600116836738707")
  private static final long              serialVersionUID = 3253652985691506976L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name and alias names by which this attribute type is known.
   ** <br>
   ** This is also known as the object identifier. The first name in the list is
   ** used as the base name and the other names are referred to as alias names.
   ** It is suggested the shortest name be listed first. If a name is not
   ** specified, the numeric object identifier is used to refer to the
   ** attribute type.
   */
  public final String                    name;
  public final Class<?>                  type;
  public final Set<DirectorySchema.Flag> flag;

  // whether this attribute is an ordinary string, or something else (e.g. ASN1
  // or a jpeg, an audio file etc.).
  // Default is false (most attributes are strings).
  protected boolean                       binary = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryAttribute</code> which has a name and type.
   **
   ** @param  name               the name of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the type of the attribute.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   ** @param  flag               the flags for the attribute.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type
   **                            {@link DirectorySchema.Flag}.
   */
  private DirectoryAttribute(final String name, final Class<?> type, final Set<DirectorySchema.Flag> flag) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name = name;
    this.type = type;
    this.flag = flag;
    binary(DirectorySchema.binary(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Whether the attribute contains binary data; ideally found by checking
   ** Syntax, but often set by inspection of the attribute value (whether it is
   ** a Byte array).
   **
   ** @return                    <code>true</code> if the attribute does not
   **                            contain binary data.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean string() {
    return !this.binary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binary
  /**
   ** Whether the attribute contains binary data; ideally found by checking
   ** Syntax, but often set by inspection of the attribute value (whether it is
   ** a Byte array).
   **
   ** @param state               <code>true</code> if the attribute contains
   **                            binary data.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    this instance for method chaining purpose.
   **                            <br>
   **                            Possible object is {@link DirectoryAttribute}.
   */
  public DirectoryAttribute binary(final boolean state) {
    this.binary = state;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binary
  /**
   ** Whether the attribute contains binary data; ideally found by checking
   ** Syntax, but often set by inspection of the attribute value (whether it is
   ** a Byte array).
   **
   ** @return                    <code>true</code> if the attribute contains
   **                            binary data.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean binary() {
    return this.binary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readable
  /**
   ** Determines if the attribute is readable.
   **
   ** @return                    <code>true</code> if the attribute is readable;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean readable() {
    return !this.flag.contains(DirectorySchema.Flag.NOT_READABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createable
  /**
   ** Determines if the attribute is writable on create.
   **
   ** @return                    <code>true</code> if the attribute is writable
   **                            on create; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean createable() {
    return !this.flag.contains(DirectorySchema.Flag.NOT_CREATEABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateable
  /**
   ** Determines if the attribute is writable on update.
   **
   ** @return                    <code>true</code> if the attribute is writable
   **                            on update; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean updateable() {
    return !this.flag.contains(DirectorySchema.Flag.NOT_UPDATEABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readonly
  /**
   ** Determines if the attribute is readonly.
   **
   ** @return                    <code>true</code> if the attribute is readonly;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean readonly() {
    return this.flag.contains(DirectorySchema.Flag.NOT_CREATEABLE) || this.flag.contains(DirectorySchema.Flag.NOT_UPDATEABLE) || this.flag.contains(DirectorySchema.Flag.OPERATIONAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Determines if the attribute is required for an object.
   **
   ** @return                    <code>true</code> if the attribute is required
   **                            for an object; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean required() {
    return this.flag.contains(DirectorySchema.Flag.REQUIRED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operational
  /**
   ** Determines if the attribute is operational for an object.
   **
   ** @return                    <code>true</code> if the attribute is
   **                            operational for an object; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean operational() {
    return this.flag.contains(DirectorySchema.Flag.OPERATIONAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValued
  /**
   ** Determines if the attribute an handle multiple values.
   ** <br>
   ** There is a special case with byte[] since in most instances this denotes
   ** a single object.
   **
   ** @return                    <code>true</code> if the attribute is
   **                            multi-value ; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean multiValued() {
    return this.flag.contains(DirectorySchema.Flag.MULTIVALUED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returnedByDefault
  /**
   ** Determines if the attribute is returned by default.
   **
   ** @return                    <code>false</code> if the attribute should not
   **                            be returned by default.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean returnedByDefault() {
    return !this.flag.contains(DirectorySchema.Flag.NOT_RETURNED_BY_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extensible
  /**
   ** Determines if the attribute is extensible by default.
   **
   ** @return                    <code>false</code> if the attribute should not
   **                            be extensible.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean extensible() {
    return multiValued() && (!(operational() || !objectClass()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Determines if the attribute the object class for an object.
   **
   ** @return                    <code>true</code> if the attribute the object
   **                            class for an object; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean objectClass() {
    return this.flag.contains(DirectorySchema.Flag.OBJECTCLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collective
  /**
   ** Determines if the attribute is collective for an object.
   **
   ** @return                    <code>true</code> if the attribute is
   **                            collective for an object; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean collective() {
    return !this.flag.contains(DirectorySchema.Flag.NOT_COLLECTIVE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   obsolete
  /**
   ** Determines if the attribute is obsolete for an object.
   **
   ** @return                    <code>true</code> if the attribute is
   **                            obsolete for an object; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean obsolete() {
    return !this.flag.contains(DirectorySchema.Flag.NOT_OBSOLETE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   is
  /**
   ** Determines if the name parameter matches this {@link DirectoryAttribute}.
   **
   ** @param  name               the name this instance should lexicographically
   **                            match.
   **
   ** @return                    <code>true</code> if the specified name is
   **                            lexicographically identically with the name of
   **                            this instance; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean is(final String name) {
    return StringUtility.equalCaseInsensitive(this.name, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   ** <p>
   ** The implementation ensures that
   ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
   ** <code>x</code> and <code>y</code>.
   ** <p>
   ** The implementation also ensures that the relation is transitive:
   ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code> implies
   ** <code>x.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
   ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  other              the object to be compared.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws ClassCastException if the specified object's type prevents it
   **                             from being compared to this object.
   */
  @Override
  public int compareTo(final Object other) {
    if (!(other instanceof DirectoryAttribute))
      throw new ClassCastException("In order to sort, all nodes must be an instance of DirectoryAttribute.");

    return compareTo((DirectoryAttribute)other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryAttribute</code>.
   ** <br>
   ** Equivalent to <code>DirectoryAttribute.build(name, String.class)</code>.
   **
   ** @param  name               the name of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryAttribute</code>.
   */
  public static DirectoryAttribute build(final String name) {
    return build(name, String.class, EnumSet.noneOf(DirectorySchema.Flag.class));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryAttribute</code>.
   ** <br>
   ** Equivalent to <code>DirectoryAttribute.build(name, type).flag(flag)</code>.
   **
   ** @param  name               the name of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  flag               the flags for the attribute.
   **                            <code>null</code> means clear all flags.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type
   **                            {@link DirectorySchema.Flag}.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryAttribute</code>.
   */
  public static DirectoryAttribute build(final String name, final Set<DirectorySchema.Flag> flag) {
    return build(name, String.class, flag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryAttribute</code>.
   ** Equivalent to
   ** <code>new DirectoryAttribute(name, type).flag(flag).binary(binary)</code>
   **
   ** @param  name               the name of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clazz              the estimated java type of the attribute.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   ** @param  flag               the flags for the attribute.
   **                            <code>null</code> means clear all flags.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type
   **                            {@link DirectorySchema.Flag}.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryAttribute</code>.
   */
  public static DirectoryAttribute build(final String name, final Class<?> clazz, final Set<DirectorySchema.Flag> flag) {
    return new DirectoryAttribute(name, clazz, flag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryAttribute</code>.
   ** Equivalent to
   ** <code>new DirectoryAttribute(name, type).flag(flag).binary(binary)</code>
   **
   ** @param  meta               the metadata of a schema attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link DirectorySchema.Attribute}.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryAttribute</code>.
   */
  public static DirectoryAttribute build(final DirectorySchema.Attribute meta) {
    return new DirectoryAttribute(meta.name, String.class, meta.flag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryAttribute</code>.
   ** Equivalent to
   ** <code>new DirectoryAttribute(name, type).flag(flag).binary(binary)</code>
   **
   ** @param  meta               the metadata of a schema attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link DirectorySchema.Attribute}.
   ** @param  required           <code>true</code> to set the required flag of
   **                            the reated <code>DirectoryAttribute</code>
   **                            properly so that the UI can interact correctly.
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryAttribute</code>.
   */
  public static DirectoryAttribute build(final DirectorySchema.Attribute meta, final boolean required) {
    final DirectoryAttribute attribute = build(meta, String.class);
    if (required)
      attribute.flag.add(DirectorySchema.Flag.REQUIRED);
    else
      attribute.flag.remove(DirectorySchema.Flag.REQUIRED);
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create an <code>DirectoryAttribute</code>.
   ** Equivalent to
   ** <code>new DirectoryAttribute(name, type).flag(flag).binary(binary)</code>
   **
   ** @param  meta               the metadata of a schema attribute.
   **                            <br>
   **                            Allowed object is
   **                            {@link DirectorySchema.Attribute}.
   ** @param  clazz              the estimated java type of the attribute.
   **                            <br>
   **                            Allowed object is {@link Class} of any type.
   **
   ** @return                    an instance of <code>DirectoryAttribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryAttribute</code>.
   */
  public static DirectoryAttribute build(final DirectorySchema.Attribute meta, final Class<?> clazz) {
    return new DirectoryAttribute(meta.name, clazz, meta.flag);
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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
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
    return this.name.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>DirectoryAttribute</code> object that
   ** represents the same <code>name</code> and <code>type</code> as this
   ** object.
   **
   ** @param other               the object to compare this
   **                            <code>DirectoryAttribute</code> against.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the
   **                            <code>DirectoryAttribute</code>s are equal;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    boolean ret = false;
    if (other instanceof DirectoryAttribute) {
      final DirectoryAttribute that = (DirectoryAttribute)other;
      if (!is(that.name)) {
        return false;
      }
      if (!this.type.equals(that.type)) {
        return false;
      }
      if (!CollectionUtility.equals(this.flag, that.flag)) {
        return false;
      }
      return true;
    }
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of the object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" this object. The result is a concise but
   ** informative representation that is easy for a person to read.
   ** <p>
   ** The <code>toString</code> method for class <code>DefaultProperty</code>
   ** returns a string consisting of the instance atttributes of the class of
   ** which the object is an instance.
   **
   ** @return                    a string representation of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   ** <p>
   ** The implementation ensures that
   ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
   ** <code>x</code> and <code>y</code>.
   ** <p>
   ** The implementation also ensures that the relation is transitive:
   ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code> implies
   ** <code>x.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
   ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  other              the object to be compared.
   **                            <br>
   **                            Allowed object is
   **                            <code>DirectoryAttribute</code>.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws ClassCastException if the specified object's type prevents it
   **                             from being compared to this object.
   */
  public int compareTo(final DirectoryAttribute other) {
    if (other == null)
      return 1;

    if (this.name == null)
      return (other.name == null) ? 0 : -1;

    if (other.name == null)
      return 1;

    return COLLATOR.compare(this.name, other.name);
  }
}