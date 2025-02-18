/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you enthered
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

    File        :   DirectorySchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectorySchema.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import java.io.Serializable;

import javax.swing.Icon;

import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.CommunicationException;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.connection.iam.service.DirectoryException;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class DirectorySchema
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DirectorySchema</code> provides a number of convenience methods
 ** for accessing schema information.
 ** <p>
 ** In addition, it allows the schema to be accessed even if a particular
 ** Directory Service does not provide a schema() call, by attempting to access
 ** a subschema subentry call directly. (e.g. for a DSML directory).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectorySchema implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Determines that an attribute is required.
   */
  public static final Set<Flag> REQUIRED          = EnumSet.of(Flag.REQUIRED);
  /**
   ** Determines that an attribute is required but readonly.
   */
  public static final Set<Flag> READONLY          = EnumSet.of(Flag.NOT_CREATEABLE, Flag.NOT_UPDATEABLE);
  /**
   ** Determines that operational attributes are not creatable or updatabale
   ** and not returned by default.
   */
  public static final Set<Flag> OPERATIONAL       = EnumSet.of(Flag.OPERATIONAL, Flag.NOT_CREATEABLE, Flag.NOT_UPDATEABLE, Flag.NOT_COLLECTIVE, Flag.NOT_OBSOLETE, Flag.NOT_RETURNED_BY_DEFAULT);
  /**
   ** The most often used attribute.
   */
  public static final Attribute OBJECTCLASS       = new Attribute(
    /*      OID  */ "2.5.4.0"
  , /*      NAME */ DirectoryService.OBJECTCLASS
  , /*     ALIAS */ null
  , /*      DESC */ null
  , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.38"
  , /*  EQUALITY */ "objectIdentifierMatch"
  , /*  ORDERING */ null
  , /* SUBSTRING */ null
  , /*       SUP */ null
  , EnumSet.of(Flag.OBJECTCLASS, Flag.NOT_COLLECTIVE, Flag.NOT_OBSOLETE)
  );

  static final Set<String>      omit             = CollectionUtility.caseInsensitiveSet();
  static final Set<String>      binary           = CollectionUtility.caseInsensitiveSet();

  // compatible with
  @SuppressWarnings("compatibility:9010951885014229301")
  private static final long  serialVersionUID = -6775114423152675958L;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static {
    omit.add("hasSubordinates");
    omit.add("nsUniqueId");
    omit.add("numSubordinates");
    omit.add("structuralObjectClass");
    omit.add("subschemaSubentry");

    // a quick pick of common syntaxes for Active Directory support (and other
    // servers that don't publish syntax descriptions) taken from RFC 2252
    binary.add("photo");
    binary.add("audio");
    binary.add("jpegPhoto");
    binary.add("userPassword");
    binary.add("cACertificate");
    binary.add("thumbnailLogo");
    binary.add("thumbnailPhoto");
    binary.add("userCertificate");
    binary.add("x500UniqueIdentifier");
    binary.add("crossCertificatePair");
    binary.add("personalSignature");
    binary.add("javaSerializedData");
    binary.add("authorityRevocationList");
    binary.add("certificateRevocationList");
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the root jndi context used for all directory queries.
  protected transient DirectoryService service      = null;

  final List<Syntax>                   syntax       = new ArrayList<Syntax>();
  final List<MatchingRule>             matchingRule = new ArrayList<MatchingRule>();
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  final Map<String, Attribute>         attribute    = CollectionUtility.caseInsensitiveMap();
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  final Map<String, ObjectClass>       objectClass  = CollectionUtility.caseInsensitiveMap();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Symbol
  // ~~~~ ~~~~~~
  /**
   ** Enum of properties an entry type of a directory schema should have.
   ** <ul>
   **   <li>root
   **   <li>base
   **   <li>user
   **   <li>group
   **   <li>domain
   **   <li>location
   **   <li>container
   **   <li>organization
   **   <li>organizationalUnit
   ** </ul>
   */
  public static enum Symbol {
      ROOT(Bundle.icon(Bundle.ENTRY_ICON_ROOT))
    , BASE(Bundle.icon(Bundle.ENTRY_ICON_BASE))
    , USER(Bundle.icon(Bundle.ENTRY_ICON_USER))
    , GROUP(Bundle.icon(Bundle.ENTRY_ICON_GROUP))
    , DOMAIN(Bundle.icon(Bundle.ENTRY_ICON_DOMAIN))
    , LOCATION(Bundle.icon(Bundle.ENTRY_ICON_LOCATION))
    , CONTAINER(Bundle.icon(Bundle.ENTRY_ICON_CONTAINER))
    , ORGANIZATION(Bundle.icon(Bundle.ENTRY_ICON_ORGANIZATION))
    , ORGANIZATIONUNIT(Bundle.icon(Bundle.ENTRY_ICON_ORGANIZATIONUNIT))
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
    private final Icon value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Symbol</code> property descriptor with the
     ** specified resouce identifier.
     **
     ** @param  icon            the resouce identifier.
     */
    Symbol(final Icon value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: icon
    /**
     ** Returns the icon of the symbol property.
     **
     ** @return                    the icon of the symbol property.
     */
    public Icon icon() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Flag
  // ~~~~ ~~~~
  /**
   ** Enum of modifier flags to use for attributes.
   ** <br>
   ** Note that this enum is designed for configuration by exception such that
   ** an empty set of flags are the defaults:
   ** <ul>
   **   <li>required
   **   <li>updateable
   **   <li>creatable
   **   <li>returned by default
   **   <li>readable
   **   <li>single-valued
   **   <li>optional
   ** </ul>
  */
  public static enum Flag {
      REQUIRED
    , MULTIVALUED
    , OBJECTCLASS
    , OPERATIONAL
    , NOT_CREATEABLE
    , NOT_UPDATEABLE
    , NOT_READABLE
    , NOT_RETURNED_BY_DEFAULT
    , NOT_COLLECTIVE
    , NOT_OBSOLETE
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  // ~~~~~ ~~~~~
  /**
   ** A value holder suitable for an arbitrary Directory Service schema entry.
   */
  public static class Entry implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:3971956235637108304")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The unique, assigned numeric object identifier. */
    public final String oid;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Entry</code> with the object identifier provided.
     **
     ** @param  oid              the object identifier of a Directory Service
     **                          schema entry.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public Entry(final String oid) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.oid = oid;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns a hash code value for the syntax.
     ** <p>
     ** This method is supported for the benefit of hashtables such as those
     ** provided by <code>java.util.Hashtable</code>.
     ** As much as is reasonably practical, the hashCode method defined by class
     ** <code>AbstractProperty</code> does return distinct integers for distinct
     ** objects.
     **
     ** @return                  a hash code value for this attribute type.
     */
    public int hashCode() {
      return this.oid.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Compares two <code>MatchingRule</code> objects.
     ** <br>
     ** Two MatchingRule objects are equal if they are the same object or if
     ** their oid are the same.
     **
     ** @param other             the object to compare this
     **                          <code>MatchingRule</code> against.
     **
     ** @return                  <code>true</code> if the
     **                          <code>MatchingRule</code>s are
     **                          equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (other == null || getClass() != other.getClass())
        return false;

      if (other == this)
        return true;

      return this.oid.equals(((Entry)other).oid);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** A value holder suitable for an attribute definition of a Directory Service
   ** schema.
   */
  public static class Attribute extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:6354963908130957377")
    private static final long               serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String      name;
    public final Set<String> alias;
    /**
     ** Text description of the attribute type.
     */
    public final String      description;
    /**
     ** The syntax defines the format of the data stored for this attribute
     ** type. It is specified using the numeric object identifier of the LDAP
     ** syntax and, optionally, the maximum length of data stored for this
     ** attribute type.
     */
    public final String      syntax;
    /**
     ** Specifies the object identifier of the matching rule which is used to
     ** determine the equality of values.
     */
    public final String      equality;
    /**
     ** Specifies the object identifier of the matching rule which is used to
     ** determine the order of values.
     */
    public final String      ordering;
    /**
     ** Specifies the object identifier of the matching rule which is used to
     ** determine substring matches of values.
     */
    public final String      substring;
    /**
     ** Specifies the superior attribute type.
     ** <br>
     ** When a superior attribute type is defined, the EQUALITY, ORDERING,
     ** SUBSTR, and SYNTAX values might be inherited from the superior attribute
     ** type. The referenced superior attribute type must also be defined in the
     ** schema.
     ** <br>
     ** When the SYNTAX, EQUALITY, ORDERING, or SUBSTR values are not specified
     ** for an attribute type, the attribute type hierarchy is used to determine
     ** these values.
     ** <br>
     ** The SYNTAX must be specified on the attribute type or through
     ** inheritance.
     */
    public final String      superior;

    public final Set<Flag>   flag;

    ////////////////////////////////////////////////////////////////////////////
    // enum Name
    // ~~~~ ~~~~
    /**
     ** Enum of properties an attribute type of a directory schema should have.
     ** <ul>
     **   <li>numericoid
     **   <li>name
     **   <li>description
     **   <li>superiror
     **   <li>syntax
     **   <li>equality
     **   <li>matchingRule
     **   <li>ordering
     **   <li>substring
     ** </ul>
     */
    public static enum Name {
        NUMERICOID(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_OID_LABEL))
      , NAME(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_NAME_LABEL))
      , DESC(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_DESCRIPTION_LABEL))
      , SUP(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_SUPERIROR_LABEL))
      , SYNTAX(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_SYNTAX_LABEL))
      , EQUALITY(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_EQUALITY_LABEL))
      , ORDERING(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_ORDERING_LABEL))
      , SUBSTRING(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_SUBSTRING_LABEL))
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      public final String label;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an {@link Attribute} <code>Name</code> property descriptor
       ** with the specified label.
       **
       ** @param  label            the translated value.
       **                          <br>
       **                          Allowed object is {@link String}.
       */
      Name(final String label) {
        // initialize instance attributes
        this.label = label;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Attribute</code> with the object identifier
     ** provided.
     **
     ** @param  oid              the object identifier of a Directory Service
     **                          schema entry.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  meta             the metadata of a Directory Service schema
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  alias            the alias names of a Directory Service schema
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  description      the optional description of a Directory Service
     **                          schema attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  syntax           the syntax of a Directory Service schema
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  equality         the equality rule of a Directory Service schema
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  ordering         the ordering rule of a Directory Service schema
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  substring        the substring rule of a Directory Service
     **                          schema attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  superior         the collection of superior object classes.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Attribute(final String oid, final String name, final Set<String> alias, final String description, final String syntax, final String equality, final String ordering, final String substring, final String superior, final Set<Flag> flag) {
      // ensure inheritance
      super(oid);

      // initialize instance attributes
      this.name        = name;
      this.flag        = flag;
      this.alias       = alias;
      this.syntax      = syntax;
      this.equality    = equality;
      this.ordering    = ordering;
      this.substring   = substring;
      this.superior    = superior;
      this.description = description;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: collective
    /**
     ** Determines if the attribute is collective for an object.
     **
     ** @return                  <code>true</code> if the attribute is
     **                          collective for an object; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean collective() {
      return !this.flag.contains(Flag.NOT_COLLECTIVE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: obsolete
    /**
     ** Determines if the attribute is obsolete for an object.
     **
     ** @return                  <code>true</code> if the attribute is
     **                          obsolete for an object; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean obsolete() {
      return !this.flag.contains(Flag.NOT_OBSOLETE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: readable
    /**
     ** Determines if the attribute is readable.
     **
     ** @return                    <code>true</code> if the attribute is readable;
     **                            otherwise <code>false</code>.
     **                            <br>
     **                            Possible object is <code>boolean</code>.
     */
    public boolean readable() {
      return !this.flag.contains(Flag.NOT_READABLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createable
    /**
     ** Determines if the attribute is writable on create.
     **
     ** @return                  <code>true</code> if the attribute is writable
     **                          on create; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean createable() {
      return !this.flag.contains(Flag.NOT_CREATEABLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: updateable
    /**
     ** Determines if the attribute is writable on update.
     **
     ** @return                  <code>true</code> if the attribute is writable
     **                          on update; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean updateable() {
      return !this.flag.contains(Flag.NOT_UPDATEABLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: readonly
    /**
     ** Determines if the attribute is readonly.
     **
     ** @return                  <code>true</code> if the attribute is readonly;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean readonly() {
      return this.flag.contains(Flag.NOT_CREATEABLE) || this.flag.contains(Flag.NOT_UPDATEABLE) || this.flag.contains(Flag.OPERATIONAL);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: required
    /**
     ** Determines if the attribute is required for an object.
     **
     ** @return                  <code>true</code> if the attribute is required
     **                          for an object; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean required() {
      return this.flag.contains(Flag.REQUIRED);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: operational
    /**
     ** Determines if the attribute is operational for an object.
     **
     ** @return                  <code>true</code> if the attribute is
     **                          operational for an object; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean operational() {
      return this.flag.contains(Flag.OPERATIONAL);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: multiValued
    /**
     ** Determines if the attribute an handle multiple values.
     ** <br>
     ** There is a special case with byte[] since in most instances this denotes
     ** a single object.
     **
     ** @return                  <code>true</code> if the attribute is
     **                          multi-value ; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean multiValued() {
      return this.flag.contains(Flag.MULTIVALUED);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: returnedByDefault
    /**
     ** Determines if the attribute is returned by default.
     **
     ** @return                  <code>false</code> if the attribute should not
     **                          be returned by default.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean returnedByDefault() {
      return !this.flag.contains(Flag.NOT_RETURNED_BY_DEFAULT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ObjectClass
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** A value holder suitable for an attribute definition of a Directory Service
   ** schema.
   ** <p>
   ** All entries in a Directory Service are typed. That is, each entry belongs
   ** to <i>object classes</i> that identify the type of data represented by an
   ** entry. The object class specifies the mandatory and optional attributes
   ** that can be associated with an entry of that class.
   ** <p>
   ** The object classes for all objects in the directory form a class
   ** hierarchy. The classes <i>top</i> and <i>alias</i> are at the root of the
   ** hierarchy. For example, the <i>organizationalPerson</i> object class is a
   ** subclass of the <i>Person</i> object class, which in turn is a subclass of
   ** <i>top</i>. When creating a new entry, you must always specify all of the
   ** object classes to which the new entry belongs. Because many directories do
   ** not support object class subclassing, you also should always include all
   ** of the superclasses of the entry. For example, for an
   ** <i>organizationalPerson</i> object, you should list in its object classes
   ** the <i>organizationalPerson</i>, <i>person</i>, and <i>top</i> classes.
   ** <p>
   ** Object classes fall into three categories:
   ** <ul>
   **   <li><b>abstract</b>
   **       <br>
   **       Abstract classes are those that may specify a set of required and
   **       optional attribute types, but that are only intended to be used if
   **       they are extended by other object classes. Abstract classes can be
   **       extended by any type of object class (including other abstract
   **       classes), but if an entry contains an abstract object class, then it
   **       must also contain at least one non-abstract object class that
   **       inherits from it. The special top object class is abstract and
   **       serves as the basis for all other object classes.
   **   <li><b>structural</b>
   **       <br>
   **       Structural classes are those that specify the main type of object
   **       that an entry represents (e.g., a user, a group, a device, etc.).
   **       <br>
   **       Structural classes may inherit from abstract or structural object
   **       classes, but not from auxiliary classes.
   **   <li><b>auxiliary</b>
   **       <br>
   **       Auxiliary classes may be used to provide information about
   **       additional characteristics for an entry that augment the base type
   **       of entry described by the structural class but do not turn it into a
   **       different kind of entry. For example, the
   **       <i>strongAuthenticationUser</i> object class (as described in RFC
   **       4523 section 5.5) may be added to an entry to indicate that the user
   **       represented by that entry has a certificate that may be useful for
   **       authentication.
   **       <br>
   **       Auxiliary classes may inherit from abstract or auxiliary object
   **       classes, but not from structural classes.
   ** </ul>
   */
  public static class ObjectClass extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-7507021458817549320")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** Indicates the type of object class.
     ** <br>
     ** STRUCTURAL is the default.
     */
    public final Kind        kind;
    /**
     ** The name and alias names by which this object class is known.
     ** <br>
     ** This is also known as the object identifier. The first name in the list
     ** is used as the base name. If name is not specified, the numeric object
     ** identifier is used to refer to the object class.
     */
    public final String      name;
    public final Set<String> alias;
    /**
     ** The text description of the object class.
     */
    public final String      description;
    /**
     ** The collection of one or more mandatory or optional attribute types.
     ** <br>
     ** Attribute types which are mandatory must be specified when adding or
     ** modifying a directory entry.
     */
    public final Set<String> required;
    /**
     ** The collection of one or more optional attribute types.
     ** <br>
     ** Attribute types which are optional might be specified when adding or
     ** modifying a directory entry.
     */
    public final Set<String> optional;
    /**
     ** The collection of one or more superior object classes.
     ** <br>
     ** When a superior object class is defined, entries specifying the object
     ** class must adhere to the superset of <b>MUST</b> and <b>MAY</b> values.
     ** The supersets of <b>MUST</b> and <b>MAY</b> values include all
     ** <b>MUST</b> and <b>MAY</b> values specified in the object class
     ** definition and all <b>MUST</b> and <b>MAY</b> values specified in the
     ** object class’s superior hierarchy.
     ** <br>
     ** When an attribute type is specified as a <b>MUST</b> in an object class
     ** in the hierarchy and a <b>MAY</b> in another object class in the
     ** hierarchy, the attribute type is treated as a <b>MUST</b>.
     ** <br>
     ** Referenced superior object classes must be defined in the schema.
     */
    public final Set<String> superior;

    ////////////////////////////////////////////////////////////////////////////
    // enum Kind
    // ~~~~ ~~~~
    public static enum Kind {
        ABSTRACT(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_KIND_ABSTRACT))
      , STRUCTURAL(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_KIND_STRUCTURAL))
      , AUXILIARY(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_KIND_AUXILIARY))
      , UNKOWN(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_KIND_UNKNOWN))
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      public final String label;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an <code>ObjectClass</code> property descriptor with the
       ** specified label.
       **
       ** @param  label            the translated value.
       */
      Kind(final String label) {
        // initialize instance attributes
        this.label = label;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // enum Name
    // ~~~~ ~~~~
    /**
     ** Enum of properties an object class of a directory schema should have.
     ** <ul>
     **   <li>oid
     **   <li>kind
     **   <li>name
     **   <li>alias
     **   <li>description
     **   <li>superiror
     **   <li>must
     **   <li>may
     ** </ul>
     */
    public static enum Name {
        NUMERICOID(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_OID_LABEL))
      , NAME(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_NAME_LABEL))
      , KIND(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_KIND_LABEL))
      , DESC(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_DESCRIPTION_LABEL))
      , SUP(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_SUPERIROR_LABEL))
      , MUST(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_MUST_LABEL))
      , MAY(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_MAY_LABEL))
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      public final String label;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an <code>ObjectClass</code> property descriptor with the
       ** specified label.
       **
       ** @param  label            the translated value.
       */
      Name(final String label) {
        // initialize instance attributes
        this.label = label;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>ObjectClass</code> with the object identifier
     ** provided.
     **
     ** @param  oid              the object identifier of a Directory Service
     **                          schema entry.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  kind             indicates the type of object class.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  name             the name of a Directory Service schema
     **                          object class.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  alias            the alias names of a Directory Service schema
     **                          object class.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  alias            the alias names by which the object class is
     **                          known.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  description      the optional description of a Directory Service
     **                          schema object class.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  required         the collection of one or more mandatory
     **                          attribute types.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  optional         the collection of one or more optional
     **                          attribute types.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     ** @param  superior         the collection of one or more superior object
     **                          classes.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     */
    private ObjectClass(final String oid, final Kind kind, final String name, final Set<String> alias, final String description, final Set<String> required, final Set<String> optional, final Set<String> superior) {
      // ensure inheritance
      super(oid);

      // initialize instance attributes
      this.kind        = kind;
      this.name        = name;
      this.alias       = alias;
      this.required    = required;
      this.optional    = optional;
      this.superior    = superior;
      this.description = description;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: required
    /**
     ** Return the entire collection of required attributes by the object class
     ** hierarchy.
     ** <p>
     ** The {@link ObjectClass}es and {@link Attribute}s are cached over the
     ** lifetime of the given schema.
     ** <p>
     ** The approach to enrich the underlying model with the attributes is more
     ** expensive in computation. This could be avoided by prepopulating the
     ** required and optional attributes for the objects class at the time the
     ** object claass is constructed. But this leads to a higher memory
     ** consumption to manage {@link Flag#REQUIRED} for a certain
     ** {@link DirectoryAttribute}. The attributes accumulated in an object
     ** class needs to be copied due to the same attribute can be used by other
     ** object classes where that attribute isn't required; hence the copy is
     ** required at least for the required attributes of an object class.
     ** <p>
     ** We preferred the approach implemented below over the prepopulation at
     ** the object class with the assumption that the user will not so often
     ** assign, update or remove object classes.
     **
     ** @param  schema           the {@link DirectorySchema} to populate the
     **                          required attributes that belongs to this
     **                          <code>ObjectClass</code> for.
     **                          <br>
     **                          Allowed object is {@link DirectorySchema}.
     **
     ** @return                  the collection of attribute names that are
     **                          required by the object class hierarchy.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Attribute}.
     */
    public final Set<DirectoryAttribute> required(final DirectorySchema schema) {
      final Set<DirectoryAttribute> effective = new LinkedHashSet<>();
      for (String cursor : this.superior)
        effective.addAll(schema.objectClass(cursor).required(schema));

      if (this.required != null) {
        for (final String cursor : this.required) {
          // lookup the attribute definition from the schema and adjust the
          // flags of the directory attribute so that the UI can interact
          // correctly
          effective.add(DirectoryAttribute.build(schema.attribute(cursor), true));
        }
      }
      return effective;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: optional
    /**
     ** Return the entire collection of optional attributes by the object class
     ** hierarchy.
     ** <p>
     ** The {@link ObjectClass}es and {@link Attribute}s are cached over the
     ** lifetime of the given schema.
     ** <p>
     ** The approach to enrich the underlying model with the attributes is more
     ** expensive in computation. This could be avoided by prepopulating the
     ** required and optional attributes for the objects class at the time the
     ** object claass is constructed. But this leads to a higher memory
     ** consumption to manage {@link Flag#REQUIRED} for a certain
     ** {@link DirectoryAttribute}. The attributes accumulated in an object
     ** class needs to be copied due to the same attribute can be used by other
     ** object classes where that attribute isn't required; hence the copy is
     ** required at least for the required attributes of an object class.
     ** <p>
     ** We preferred the approach implemented below over the prepopulation at
     ** the object class with the assumption that the user will not so often
     ** assign, update or remove object classes.
     **
     ** @param  schema           the {@link DirectorySchema} to populate the
     **                          optional attributes that belongs to this
     **                          <code>ObjectClass</code> for.
     **                          <br>
     **                          Allowed object is {@link DirectorySchema}.
     **
     ** @return                  the collection of attribute names that are
     **                          optional by the object class hierarchy.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link DirectoryAttribute}.
     */
    public final Set<DirectoryAttribute> optional(final DirectorySchema schema) {
      final Set<DirectoryAttribute> effective = new LinkedHashSet<>();
      for (String cursor : this.superior)
        effective.addAll(schema.objectClass(cursor).optional(schema));

      if (this.optional != null) {
        for (final String cursor : this.optional) {
          // lookup the attribute definition from the schema and adjust the
          // flags of the directory attribute so that the UI can interact
          // correctly
          effective.add(DirectoryAttribute.build(schema.attribute(cursor), false));
        }
      }
      return effective;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Syntax
  // ~~~~~ ~~~~~~
  /**
   ** A value holder suitable for an attribute syntax of a Directory Service
   ** schema.
   ** <p>
   ** An attribute syntax is the equivalent of a data type in a Directory
   ** Service. Every attribute type is associated (either explicitly or
   ** implicitly) with an attribute syntax, and all values for attributes of
   ** that type must abide by the constraints of that syntax.
   ** <p>
   ** Because support for an attribute syntax requires server-side logic to
   ** define the constraints associated with that syntax, the set of attribute
   ** syntaxes available for use in a server cannot be extended as easily as
   ** defining new attribute types or object classes. Some servers may offer an
   ** API that could allow a third-party developer to create a custom attribute
   ** syntax, while other servers may not provide any mechanism for extending
   ** the set of available syntaxes.
   ** <p>
   ** The LDAP server should provide information about the syntaxes it supports
   ** in the subschema subentry. Values of this attribute must have the
   ** following form (as described in RFC 4512 section 4.1.5):
   ** <ol>
   **   <li>An open parenthesis followed by zero or more spaces.
   **   <li>A numeric <i>OID</i> that uniquely identifies the syntax.
   **   <li>An optional descriptive name.
   **       If present, this will be separated from the numeric <i>OID</i> by
   **       one or more spaces, and will consist of the text <code>DESC</code>
   **       followed by one or more spaces, a single quote, the descriptive
   **       name, and the closing quote. The name may contain any UTF-8
   **       characters except the single quote (which must be escaped as
   **       <code>\27</code>) or the backslash (which must be escaped as
   **       <code>\5c</code>).
   **   <li>An optional set of extensions, in the format described in the
   **       Schema Element Extensions section.
   **   <li>Zero or more spaces followed by a close parenthesis.
   ** </ol>
   ** <b>Note</b>:
   ** The format of an attribute syntax definition is itself defined by an
   ** attribute syntax: the “LDAP syntax description” syntax, which is defined
   ** as follows (in RFC 4517 section 3.3.18):
   ** <pre>
   **   ( 1.3.6.1.4.1.1466.115.121.1.54 DESC 'LDAP Syntax Description' )
   ** </pre>
   */
  public static class Syntax extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2056906958443202955")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The description for this attribute syntax. */
    public final String description;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Syntax</code> with the syntax metadata provided.
     **
     ** @param  oid              the object identifier of a Directory Service
     **                          schema syntax.
     ** @param  description      the description of  of a Directory Service
     **                          schema syntax.
     */
    protected Syntax(final String oid, final String description) {
      // ensure inheritance
      super(oid);

      // initialize instance attributes
      this.description = description;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MatchingRule
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** A value holder suitable for an attribute matching rules of a directory
   ** service schema.
   ** <p>
   ** A matching rule encapsulates a set of logic that may be used to perform
   ** some kind of matching operation against two LDAP values. There are three
   ** basic kinds of matching rules defined in LDAP:
   ** <ul>
   **   <li>An equality matching rule may be used to determine whether two
   **       values are logically equivalent to each other.
   **   <li>An ordering matching rule may be used to determine whether one value
   **       should be ordered before or after another in a sorted list (e.g.,
   **       for the purposes of server-side sorting, as well as for evaluating
   **       greater-or-equal and less-or-equal filters).
   **   <li>A substring matching rule may be used to determine whether a value
   **       matches a provided substring assertion.
   ** </ul>
   ** In addition, although the official LDAP specification does not
   ** specifically reference approximate matching rules, some servers provide
   ** support for this type of rule, which is used to determine whether two
   ** values are approximately equal to each other.
   ** <p>
   ** The logic involved in a matching rule is generally dependent upon the
   ** syntax of the values involved. This is because there may be multiple
   ** logically-equivalent ways of representing a value in a given syntax (for
   ** example, there are multiple ways of representing a given time in the
   ** generalized time syntax, and there are multiple ways of representing a
   ** distinguished name in the DN syntax), and therefore two values may be
   ** logically equivalent even if they do not match when performing a
   ** byte-for-byte comparison.
   ** <p>
   ** <b>Note</b>:
   ** Some attribute syntaxes may not support all kinds of matching. For
   ** example, consider the Boolean syntax. While it makes sense to be able to
   ** determine whether two Boolean values are logically equivalent, it doesn’t
   ** make any sense to be able to determine whether one value is greater than
   ** or less than another because neither true nor false is inherently greater
   ** than or less than the other. Similarly, it doesn’t make sense to support
   ** substring matching for Boolean values because there are only two valid
   ** Boolean values.
   ** <p>
   ** As with attribute syntaxes, matching rules require specialized logic and
   ** therefore cannot be created as easily as most other types of schema
   ** elements (if the server allows you to create them at all). Information
   ** about the set of matching rules that an Directory Service supports may be
   ** found in the matchingRules attribute of the subschema subentry, with
   ** values encoded in the following format (as described in RFC 4512 section
   ** 4.1.3):
   ** <ol>
   **   <li>An open parenthesis followed by zero or more spaces.
   **   <li>A numeric <i>OID</i> that uniquely identifies the syntax.
   **   <li>An optional set of names that may be used to reference the matching
   **       rule as an alternative to the <i>OID</i>. Each of these names must
   **       be unique across all matching rules defined in the server, but may
   **       potentially overlap with the names of other schema elements.
   **       Matching rule names must start with an ASCII letter (uppercase or
   **       lowercase) and may contain only ASCII letters, numeric digits, and
   **       the hyphen character. If present, the matching rule names must
   **       follow the numeric OID by one or more spaces, then the string
   **       <code>NAME</code> and one or more additional spaces, and finally
   **       the name(s) for the matching rule in one of the following formats:
   **       <ul>
   **         <li>A single quote, the matching rule name, and a single quote.
   **             This format can only be used for matching rules that have a
   **             single name.
   **         <li>An open parenthesis, zero or more spaces, a single quote, the
   **             name of the first matching rule, and a single quote. If there
   **             are additional names, then they must also be placed in single
   **             quotes with at least one space separating the names. After the
   **             last name, there should be zero or more spaces followed by a
   **             close parenthesis. This format can be used for matching rules
   **             that have one or more names.
   **       </ul>
   **   <li>An optional human-readable description.
   **       If present, this should consist of one or more spaces to separate it
   **       from the previous element, the string <code>DESC</code>, one or more
   **       spaces, a single quote, a string of any set of UTF-8 characters
   **       (with the single quote escaped as <code>\27</code> and the backslash
   **       as <code>\5c</code>), and a single quote.
   **   <li>The optional string <code>OBSOLETE</code>, preceded by one or more
   **       spaces. If present, this indicates that the matching rule should not
   **       be considered available for use in attribute type definitions or
   **       extensible match search filters.
   **   <li>One or more spaces followed by the string <code>SYNTAX</code>, one
   **       or more spaces, and the numeric <i>OID</i> of the attribute syntax
   **       to which values should conform if matching operations are to be
   **       performed against them.
   **   <li>An optional set of extensions, in the format described in the
   **       Schema Element Extensions section.
   **   <li>Zero or more spaces followed by a close parenthesis.
   ** </ol>
   ** For example, the definition of the caseIgnoreMatch matching rule is as
   ** follows:
   ** <pre>
   **   ( 2.5.13.2 NAME 'caseIgnoreMatch' SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )
   ** </pre>
   */
  public static class MatchingRule extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:3025979531211193929")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The name by which this matching rule is known.
     */
    public final String      name;
    /**
     ** The text description of the matching rule.
     */
    public final String      description;
    /**
     ** Specifies the numeric object identifier of the syntax for this
     ** matching rule.
     */
    public final String      syntax;
    /**
     ** Indicates that the matching rule is obsolete.
     */
    public final boolean     obsolete;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>MatchingRule</code> with the mathicng rule metadata
     ** provided.
     **
     ** @param  oid              the name by which this matching rule is known
     **                          in a Directory Service.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  name             the name by which this matching rule is known
     **                          in a Directory Service.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  description      text description of the matching rule.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  syntax           the numeric object identifier of the syntax for
     **                          the matching rule.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  obsolete         indicates that the matching rule is obsolete.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    protected MatchingRule(final String oid, final String name, final String description, final String syntax, final boolean obsolete) {
      // ensure inheritance
      super(oid);

      // initialize instance attributes
      this.name        = name;
      this.syntax      = syntax;
      this.obsolete    = obsolete;
      this.description = description;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Schema discovery is performed by query the Directory Server.
   */
  public static final class Server extends DirectorySchema {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-4863407808406164397")
    private static final long    serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private transient DirContext schema;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Use the {@link DirectoryService} passed in to immediately connect to a
     ** Directory Service.
     **
     ** @param  service          the {@link DirectoryService} which is used
     **                          to discover this {@link DirectorySchema}.
     **                          <br>
     **                          Allowed object is {@link DirectoryService}.
     **
     ** @throws DirectoryException if the initial LDAP context could not be
     **                            created by the specified service.
     */
    public Server(final DirectoryService service)
      throws DirectoryException {

      // ensure inheritance
      super(service);

      // initialize instance
      final boolean established = this.service.established();
      try {
        final DirContext context = this.service.connect(StringUtility.EMPTY);
        this.schema = context.getSchema(StringUtility.EMPTY);
        syntaxes();
        matchingRules();
        attributes();
        objectClasses();
      }
      // when the host, port or something else is wrong in the server properties
      catch (CommunicationException e) {
        throw new DirectoryException(e);
      }
      // if anything else went wrong
      catch (NamingException e) {
        throw new DirectoryException(e);
      }
      finally {
        try {
          if (!established)
            this.service.disconnect();
        }
        catch (DirectoryException e) {
          e.printStackTrace();
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   syntaxes
    /**
     ** Collecting the meta-data for syntaxes from the connected Directory
     ** Service.
     **
     ** @throws DirectoryException if the operation fails for any reason.
     */
    private void syntaxes()
      throws DirectoryException {

      try {
        final DirContext context = (DirContext) this.schema.lookup("SyntaxDefinition");
        final NamingEnumeration<NameClassPair> cursor = context.list(StringUtility.EMPTY);
        while (cursor.hasMore()) {
          final NameClassPair id = cursor.next();
          final Attributes    at = context.getAttributes(id.getName());
          String oid  = DirectoryName.stringValue(at, "numericoid");
          String desc = DirectoryName.stringValue(at, "desc");
          this.syntax.add(new Syntax(oid, desc));
        }
      }
      catch (NamingException e) {
        throw new DirectoryException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   matchingRules
    /**
     ** Collecting the meta-data for matching rules from the connected Directory
     ** Service.
     **
     ** @throws DirectoryException if the operation fails for any reason.
     */
    private void matchingRules()
      throws DirectoryException {

      try {
        final DirContext context = (DirContext) this.schema.lookup("MatchingRule");
        final NamingEnumeration<NameClassPair> cursor = context.list(StringUtility.EMPTY);
        while (cursor.hasMore()) {
          final NameClassPair id = cursor.next();
          final Attributes    at = context.getAttributes(id.getName());
          this.matchingRule.add(
            new MatchingRule(
              DirectoryName.stringValue(at, "numericoid")
            , DirectoryName.stringValue(at, "name")
            , DirectoryName.stringValue(at, "desc")
            , DirectoryName.stringValue(at, "syntax")
            , "true".equals(DirectoryName.stringValue(at, "obsolete"))
            )
          );
        }
      }
      catch (NamingException e) {
        throw new DirectoryException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   attributes
    /**
     ** Collecting the meta-data for attribute types from the connected
     ** Directory Service.
     **
     ** @throws NamingException  if the operation fails for any reason.
     */
    private void attributes()
      throws NamingException {

      final DirContext                       context = (DirContext) this.schema.lookup("AttributeDefinition");
      final NamingEnumeration<NameClassPair> cursor  = context.list(StringUtility.EMPTY);
      while (cursor.hasMore()) {
        final String                 id = cursor.next().getName();
        final Attributes             at = context.getAttributes(id);

        final Set<Flag> fl = EnumSet.noneOf(Flag.class);
        final String usage = DirectoryName.stringValue(at, "USAGE");
        if (usage != null) {
          if ("directoryOperation".equals(usage)) {
            fl.addAll(OPERATIONAL);
          }
          else if ("distributedOperation".equals(usage)) {
            fl.addAll(OPERATIONAL);
          }
          //  DSA operational attribute
          else if ("dSAOperation".equals(usage)) {
            fl.addAll(OPERATIONAL);
          }
        }
        if (!"true".equals(DirectoryName.stringValue(at, "SINGLE-VALUE"))) {
          fl.add(Flag.MULTIVALUED);
        }
        if (!"true".equals(DirectoryName.stringValue(at, "COLLECTIVE"))) {
          fl.add(Flag.NOT_COLLECTIVE);
        }
        if (!"true".equals(DirectoryName.stringValue(at, "OBSOLETE"))) {
          fl.add(Flag.NOT_OBSOLETE);
        }
        if ("true".equals(DirectoryName.stringValue(at,  "NO-USER-MODIFICATION"))) {
          fl.add(Flag.NOT_CREATEABLE);
          fl.add(Flag.NOT_UPDATEABLE);
        }
        if (DirectoryService.OBJECTCLASS.equals(id)) {
          fl.add(Flag.OBJECTCLASS);
        }
        // technically, attribute elements are supposed to appear in a specific
        // order, but we'll be lenient and allow remaining elements to come in
        // any order
        final Attribute pr = new Attribute(
          /*      OID  */ DirectoryName.stringValue(at, Attribute.Name.NUMERICOID.name())
        , /*      NAME */ DirectoryName.stringValue(at, Attribute.Name.NAME.name())
                          // take care about aliasing of names
        , /*     ALIAS */ null
        , /*      DESC */ DirectoryName.stringValue(at, Attribute.Name.DESC.name())
        , /*    SYNTAX */ DirectoryName.stringValue(at, Attribute.Name.SYNTAX.name())
        , /*  EQUALITY */ DirectoryName.stringValue(at, Attribute.Name.EQUALITY.name())
        , /*  ORDERING */ DirectoryName.stringValue(at, Attribute.Name.ORDERING.name())
        , /* SUBSTRING */ DirectoryName.stringValue(at, Attribute.Name.SUBSTRING.name())
        , /*       SUP */ DirectoryName.stringValue(at, Attribute.Name.SUP.name())
        ,                 fl                  
        );
        this.attribute.put(pr.name, pr);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   objectClasses
    /**
     ** Collecting the meta-data for all known object classes, as read from the
     ** schema the of the connected Directory Service.
     **
     ** @throws NamingException  if the operation fails for any reason.
     */
    private void objectClasses()
      throws NamingException {

      final DirContext                       context = (DirContext)this.schema.lookup("ClassDefinition");
      final NamingEnumeration<NameClassPair> cursor  = context.list(StringUtility.EMPTY);
      while (cursor.hasMore()) {
        final String     id = cursor.next().getName();
        final Attributes at = context.getAttributes(id);
        ObjectClass.Kind kind = null;
        if ("true".equals(DirectoryName.stringValue(at, ObjectClass.Kind.ABSTRACT.name())))
          kind = ObjectClass.Kind.ABSTRACT;
        else if ("true".equals(DirectoryName.stringValue(at, ObjectClass.Kind.AUXILIARY.name())))
          kind = ObjectClass.Kind.AUXILIARY;
        else if ("true".equals(DirectoryName.stringValue(at, ObjectClass.Kind.STRUCTURAL.name())))
          kind = ObjectClass.Kind.STRUCTURAL;
        else
          kind = ObjectClass.Kind.UNKOWN;

        registerObjectClass(
          DirectoryName.stringValue(at, ObjectClass.Name.NUMERICOID.name())
        , kind
        , DirectoryName.stringValue(at, ObjectClass.Name.NAME.name())
          // take care about aliasing of names
        , DirectoryName.stringCollection(at, "ALIAS")
        , DirectoryName.stringValue(at, ObjectClass.Name.DESC.name())
          // take care about required attributes
        , DirectoryName.stringCollection(at, ObjectClass.Name.MUST.name())
          // take care about optional attributes
        , DirectoryName.stringCollection(at, ObjectClass.Name.MAY.name())
          // stake care about uperiror attributes
        , DirectoryName.stringCollection(at, ObjectClass.Name.SUP.name())
        );
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Static
  // ~~~~~ ~~~~~~~
  /**
   ** Schema is performed by filling up with static data.
   */
  public static final class Static extends DirectorySchema {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:5590485596345663307")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Use the {@link DirectoryService} passed in to immediately connect to a
     ** Directory Service.
     **
     ** @param  context          the {@link DirectoryService} which is used
     **                          to discover this {@link DirectorySchema}.
     **                          Allowed object is {@link DirectoryService}.
     */
    public Static(final DirectoryService context) {
      // ensure inheritance
      super(context);

      // initialize instance
      initialize();
      attributeTypes();
      objectClasses();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   objectClasses
    /**
     ** Collecting the meta-data for object classes.
     */
    private void objectClasses() {
      registerObjectClass(
        /*      OID  */ "2.5.6.0"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "top"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ CollectionUtility.set(DirectoryService.OBJECTCLASS)
      , /*      MAY  */ CollectionUtility.set("creatorsName","modifiersName")
      , /*      SUP  */ null
      );
      registerObjectClass(
        /*      OID  */ "2.5.6.4"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "organization"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ CollectionUtility.set("o")
      , /*      MAY  */ CollectionUtility.set("businessCategory","description","destinationIndicator","facsimileTelephoneNumber","internationalISDNNumber","l","physicalDeliveryOfficeName","postalAddress","postalCode","postOfficeBox","preferredDeliveryMethod","registeredAddress","searchGuide","seeAlso","st","street","telephoneNumber","telexTerminalIdentifier","telexNumber","userPassword","x21Address")
      , /*      SUP  */ CollectionUtility.set("top")
      );
      registerObjectClass(
        /*      OID  */ "2.5.6.5"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "organizationalUnit"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ CollectionUtility.set("ou")
      , /*      MAY  */ CollectionUtility.set("businessCategory","description","destinationIndicator","facsimileTelephoneNumber","internationalISDNNumber","l","physicalDeliveryOfficeName","postalAddress","postalCode","postOfficeBox","preferredDeliveryMethod","registeredAddress","searchGuide","seeAlso","st","street","telephoneNumber","telexTerminalIdentifier","telexNumber","userPassword","x21Address")
      , /*      SUP  */ CollectionUtility.set("top")
      );
      registerObjectClass(
        /*      OID  */ "2.5.6.6"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "person"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ CollectionUtility.set("cn","sn")
      , /*      MAY  */ CollectionUtility.set("description","seeAlso","telephoneNumber","userPassword")
      , /*      SUP  */ CollectionUtility.set("top")
      );
      registerObjectClass(
        /*      OID  */ "2.5.6.7"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "organizationalPerson"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ null
      , /*      MAY  */ CollectionUtility.set("destinationIndicator","facsimileTelephoneNumber","internationaliSDNNumber","l", "ou")
      , /*      SUP  */ CollectionUtility.set("person"));
      registerObjectClass(
        /*      OID  */ "2.5.6.9"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "groupOfNames"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ CollectionUtility.set("cn")
      , /*      MAY  */ CollectionUtility.set("businessCategory","description","member","o","ou","owner","seeAlso")
      , /*      SUP  */ CollectionUtility.set("top"));
      registerObjectClass(
        /*      OID  */ "2.5.6.17"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "groupOfUniqueNames"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ CollectionUtility.set("cn")
      , /*      MAY  */ CollectionUtility.set("businessCategory","description","member","o","ou","owner","seeAlso")
      , /*      SUP  */ CollectionUtility.set("top")
      );

      registerObjectClass(
        /*      OID  */ "2.16.840.1.113730.3.2.2"
      , /*     KIND  */ ObjectClass.Kind.STRUCTURAL
      , /*     NAME  */ "inetOrgPerson"
      , /*    ALIAS  */ null
      , /*     DESC  */ null
      , /*     MUST  */ null
      , /*      MAY  */ CollectionUtility.set("audio","businessCategory","carLicense","departmentNumber","displayName","employeeNumber","employeeType","givenName","homePhone")
      , /*      SUP  */ CollectionUtility.set("organizationalPerson")
      );
    }

    private void initialize() {
      // a quick pick of common syntaxes for Active Directory support (and other
      // servers that don't publish syntax descriptions) taken from RFC 2252
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.1",  "ACI Item"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.2",  "Access Point"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.3",  "Attribute Type Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.4",  "Audio"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.5",  "Binary"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.6",  "Bit String"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.7",  "Boolean"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.8",  "Certificate"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.9",  "Certificate List"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.10", "Certificate Pair"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.11", "Country String"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.12", "DN"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.13", "Data Quality Syntax"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.14", "Delivery Method"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.15", "Directory String"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.16", "DIT Content Rule Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.17", "DIT Structure Rule Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.18", "DL Submit Permission"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.19", "DSA Quality Syntax"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.20", "DSE Type"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.21", "Enhanced Guide"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.22", "Facsimile Telephone Number"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.23", "Fax"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.24", "Generalized Time"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.25", "Guide"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.26", "IA5 String"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.27", "INTEGER"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.28", "JPEG"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.54", "LDAP Syntax Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.56", "LDAP Schema Definition"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.57", "LDAP Schema Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.29", "Master And Shadow Access Points"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.30", "Matching Rule Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.31", "Matching Rule Use Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.32", "Mail Preference"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.33", "MHS OR Address"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.55", "Modify Rights"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.34", "Name And Optional UID"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.35", "Name Form Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.36", "Numeric String"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.37", "Object Class Description"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.40", "Octet String"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.38", "OID"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.39", "Other Mailbox"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.41", "Postal Address"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.42", "Protocol Information"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.43", "Presentation Address"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.44", "Printable String"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.58", "Substring Assertion"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.45", "Subtree Specification"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.46", "Supplier Information"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.47", "Supplier Or Consumer"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.48", "Supplier And Consumer"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.49", "Supported Algorithm"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.50", "Telephone Number"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.51", "Teletex Terminal Identifier"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.52", "Telex Number"));
      this.syntax.add(new Syntax("1.3.6.1.4.1.1466.115.121.1.53", "UTC Time"));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   attributeTypes
    /**
     ** Collecting the meta-data for attribute types.
     */
    private void attributeTypes() {
      register(
        /*      OID  */ "2.5.4.0"
      , /*      NAME */ DirectoryService.OBJECTCLASS
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.38"
      , /*  EQUALITY */ "objectIdentifierMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , EnumSet.of(Flag.OBJECTCLASS, Flag.NOT_COLLECTIVE, Flag.NOT_OBSOLETE)
      );
      register(
        /*      OID  */ "2.5.4.3"
      , /*      NAME */ "cn"
      , /*     ALIAS */ Collections.singleton("commonName")
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.15"
      , /*  EQUALITY */ "objectIdentifierMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ "caseIgnoreSubstringsMatch"
      , /*       SUP */ "name"
      , EnumSet.of(Flag.MULTIVALUED)                 
      );
      register(
        /*      OID  */ "2.5.4.4"
      , /*      NAME */ "sn"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.15"
      , /*  EQUALITY */ "objectIdentifierMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ "caseIgnoreSubstringsMatch"
      , /*       SUP */ "name"
      , EnumSet.of(Flag.MULTIVALUED)
      );
      register(
        /*      OID  */ "2.5.4.10"
      , /*      NAME */ "o"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.15"
      , /*  EQUALITY */ "caseIgnoreMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ "caseIgnoreSubstringsMatch"
      , /*       SUP */ "name"
      , EnumSet.of(Flag.MULTIVALUED)
      );
      register(
        /*      OID  */ "2.5.4.11"
      , /*      NAME */ "ou"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.15"
      , /*  EQUALITY */ "caseIgnoreMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ "caseIgnoreSubstringsMatch"
      , /*       SUP */ "name"
      , EnumSet.of(Flag.MULTIVALUED)
      );
      register(
        /*      OID  */ "2.5.4.15"
      , /*      NAME */ "businessCategory"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.15"
      , /*  EQUALITY */ "caseIgnoreMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ "caseIgnoreSubstringsMatch"
      , /*       SUP */ null
      , EnumSet.of(Flag.MULTIVALUED)
      );
      register(
        /*      OID  */ "2.5.4.32"
      , /*      NAME */ "owner"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.12"
      , /*  EQUALITY */ "distinguishedNameMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ null
      , /*       SUP */ "distinguishedName"
      , EnumSet.noneOf(Flag.class)
      );
      register(
        /*      OID  */ "2.5.4.41"
      , /*      NAME */ "name"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.15"
      , /*  EQUALITY */ "caseIgnoreMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ "caseIgnoreSubstringsMatch"
      , /*       SUP */ null
      , EnumSet.of(Flag.MULTIVALUED)
      );
      register(
        /*      OID  */ "2.5.4.49"
      , /*      NAME */ "distinguishedName"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.12"
      , /*  EQUALITY */ "distinguishedNameMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , EnumSet.noneOf(Flag.class)
      );

      register(
        /*      OID  */ "2.5.18.1"
      , /*      NAME */ "createTimestamp"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.24"
      , /*  EQUALITY */ "generalizedTimeMatch"
      , /*  ORDERING */ "generalizedTimeOrderingMatch"
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , OPERATIONAL
      );
      register(
        /*      OID  */ "2.5.18.2"
      , /*      NAME */ "modifyTimestamp"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.24"
      , /*  EQUALITY */ "generalizedTimeMatch"
      , /*  ORDERING */ "generalizedTimeOrderingMatch"
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , OPERATIONAL
      );
      register(
        /*      OID  */ "2.5.18.3"
      , /*      NAME */ "creatorsName"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.12"
      , /*  EQUALITY */ "distinguishedNameMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , OPERATIONAL
      );
      register(
        /*      OID  */ "2.5.18.4"
      , /*      NAME */ "modifiersName"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.12"
      , /*  EQUALITY */ "distinguishedNameMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , OPERATIONAL
      );
      register(
        /*      OID  */ "2.5.18.35"
      , /*      NAME */ "userPassword"
      , /*     ALIAS */ null
      , /*      DESC */ null
      , /*    SYNTAX */ "1.3.6.1.4.1.26027.1.3.1"
      , /*  EQUALITY */ null
      , /*  ORDERING */ null
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , EnumSet.of(Flag.NOT_RETURNED_BY_DEFAULT, Flag.NOT_READABLE, Flag.NOT_COLLECTIVE, Flag.NOT_OBSOLETE)
      );
      register(
        /*      OID  */ "1.3.6.1.1.16.4"
      , /*      NAME */ "entryUUID"
      , /*     ALIAS */ null
      , /*      DESC */ "UUID of the entry"
      , /*    SYNTAX */ "1.3.6.1.1.16.1"
      , /*  EQUALITY */ "caseIgnoreMatch"
      , /*  ORDERING */ "uuidOrderingMatch"
      , /* SUBSTRING */ null
      , /*       SUP */ null
      , OPERATIONAL
      );
      register(
        /*      OID  */ "2.16.840.1.113894.1.1.37"
      , /*      NAME */ "orclGUID"
      , /*     ALIAS */ null
      , /*      DESC */ "Global unique ID"
      , /*    SYNTAX */ "1.3.6.1.4.1.1466.115.121.1.15"
      , /*  EQUALITY */ "caseIgnoreMatch"
      , /*  ORDERING */ null
      , /* SUBSTRING */ "caseIgnoreSubstringsMatch"
      , /*       SUP */ null
      , OPERATIONAL
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the {@link DirectoryService} passed in to immediately connect to a
   ** Directory Service.
   **
   ** @param  service            the Directory Service enpoint conection which
   **                            is used to discover this
   **                            {@link DirectorySchema}.
   **                            Allowed object is {@link DirectoryService}.
   */
  private DirectorySchema(final DirectoryService service) {
    // ensure inheritance
    super();

    // initialize instance
    this.service = service;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omit
  /**
   ** Returns <code>true</code> if the attribute name specified should be
   ** omitted.
   **
   ** @param  name               the attribute type name to verify
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the attribute name
   **                            specified should be omitted.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean omit(final String name) {
    return omit.contains(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binary
  /**
   ** Returns <code>true</code> value for the attribute type name specified is
   ** binary.
   **
   ** @param  name               the attribute type name to verify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> value for the attribute type
   **                            name specified is binary.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean binary(final String name) {
    return binary.contains(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readonly
  /**
   ** Returns <code>true</code> if the attribute name specified is an refers to
   ** a non-createable, non-updateable or operational attribute.
   **
   ** @param  name               the attribute type name to verify
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> value for the attribute name
   **                            specified is an readonly attribute.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean readonly(final String name) {
    final Attribute attribute = this.attribute(name);
    return attribute == null ? false : attribute.flag.contains(Flag.OPERATIONAL) || attribute.flag.contains(Flag.NOT_CREATEABLE) || attribute.flag.contains(Flag.NOT_UPDATEABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operational
  /**
   ** Returns <code>true</code> if the attribute name specified is an refers to
   ** operational attribute.
   **
   ** @param  name               the attribute type name to verify
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> value for the attribute name
   **                            specified is an operational value.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean operational(final String name) {
    final Attribute attribute = this.attribute(name);
    return attribute == null ? false : attribute.flag.contains(Flag.OPERATIONAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   syntax
  /**
   ** Returns an unmodifiable collection of attribute type syntaxes for the
   ** connected Directory Service.
   **
   ** @return                    an unmodifiable collection of attribute type
   **                            syntaxes for the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is a {@link Syntax}.
   */
  public List<Syntax> syntax() {
    return CollectionUtility.unmodifiable(this.syntax);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   matchingRule
  /**
   ** Returns an unmodifiable collection of attribute type matching rules for
   ** the connected Directory Service.
   **
   ** @return                    an unmodifiable collection of attribute type
   **                            matching rules for the connected Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is a {@link MatchingRule}.
   */
  public List<MatchingRule> matchingRule() {
    return CollectionUtility.unmodifiable(this.matchingRule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Returns an unmodifiable collection of object classes for the connected
   ** Directory Service.
   **
   ** @return                    an unmodifiable collection of object classes
   **                            for the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link ObjectClass} as the value.
   */
  public Map<String, ObjectClass> objectClass() {
    return this.objectClass;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeType
  /**
   ** Returns an unmodifiable collection of attribute types for the connected
   ** Directory Service.
   **
   ** @return                    an unmodifiable collection of attribute types
   **                            for the connected Directory Service.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Attribute} as the value.
   */
  public Map<String, Attribute> attributeType() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classify
  /**
   ** Returns the classification symbol for an entry.
   **
   ** @param  result             the attribute type name to verify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the classification symbol for an entry.
   **                            <br>
   **                            Possible object is {@link Icon}.
   **
   ** @throws NamingException    if a naming exception was encountered while
   **                            retrieving the values.
   */
  public static Icon classify(final SearchResult result)
    throws NamingException {

    final javax.naming.directory.Attribute clazz = result.getAttributes().get(DirectoryService.OBJECTCLASS);
    for (int i = 0; i < clazz.size(); i++) {
      final String value = clazz.get(i).toString();
      if ("organizationalUnit".equalsIgnoreCase(value))
        return Symbol.ORGANIZATIONUNIT.value;
      else if ("organization".equalsIgnoreCase(value))
        return Symbol.ORGANIZATION.value;
      else if ("person".equalsIgnoreCase(value))
        return Symbol.USER.value;
      else if ("group".equalsIgnoreCase(value))
        return Symbol.GROUP.value;
      else if ("groupOfNames".equalsIgnoreCase(value))
        return Symbol.GROUP.value;
      else if ("groupOfUniqueNames".equalsIgnoreCase(value))
        return Symbol.GROUP.value;
      else if ("domain".equalsIgnoreCase(value))
        return Symbol.DOMAIN.value;
      else if ("country".equalsIgnoreCase(value))
        return Symbol.LOCATION.value;
      else if ("location".equalsIgnoreCase(value))
        return Symbol.LOCATION.value;
      else if ("orclContainer".equalsIgnoreCase(value))
        return Symbol.CONTAINER.value;
      else if ("orclContext".equalsIgnoreCase(value))
        return Symbol.CONTAINER.value;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Return the schema upon request.
   ** <p>
   ** The schema is cached over the lifetime of this instance.
   **
   ** @param  service            the Directory Service enpoint conection which
   **                            is used to discover the
   **                            {@link DirectorySchema}.
   **                            <br>
   **                            Allowed object is {@link DirectoryService}.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link DirectorySchema}.
   **
   ** @throws DirectoryException if the schema operation fails.
   */
  public static DirectorySchema build(final DirectoryService service)
    throws DirectoryException {

   return service.schema();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Factory method to create an <code>Attribute</code> with the object
   ** identifier provided.
   ** <p>
   ** The {@link Attributes}es are cached over the lifetime of this instance.
   **
   **
   ** @param  oid                the object identifier of a Directory Service
   **                            schema entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name by which the attribute type is known.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  alias              the alias names of a Directory Service schema
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  description        the optional description of a Directory Service
   **                            schema attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  syntax             the syntax of a Directory Service schema
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  equality           the equality rule of a Directory Service schema
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  ordering           the ordering rule of a Directory Service schema
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  substring          the substring rule of a Directory Service
   **                            schema attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  superior           the collection of superior object classes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            elemenet is of type {@link String}.
   ** @param  flag               the flags for the attribute.
   **                            <code>null</code> means clear all flags.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Flag}.
   */
  public final void register(final String oid, final String name, final Set<String> alias, final String description, final String syntax, final String equality, final String ordering, final String substring, final String superior, final Set<Flag> flag) {
    this.attribute.put(name, new Attribute(oid, name, alias, description, syntax, equality, ordering, substring, superior, flag));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Return the {@link Attribute} of the schema upon request.
   ** <p>
   ** The {@link Attribute}e are cached over the lifetime of this instance.
   **
   ** @param  name               the name of the desired {@link Attribute}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Attribute} mapped at the specified
   **                            <code>name</code> or <code>null</code> if no
   **                            {@link Attribute} is mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public Attribute attribute(final String name) {
    return this.attribute.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerObjectClass
  /**
   ** Factory method to create an <code>ObjectClass</code> with the object
   ** identifier provided.
   ** <p>
   ** The {@link ObjectClass}es are cached over the lifetime of this instance.
   **
   ** @param  oid                the object identifier of a Directory Service
   **                            schema entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  kind               indicates the type of object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of a Directory Service schema
   **                            object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  alias              the alias names of a Directory Service schema
   **                            object class.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  description       the optional description of a Directory Service
   **                            schema object class.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  required           the collection of one or more mandatory
   **                            attribute types.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  optional           the collection of one or more optional
   **                            attribute types.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  superior           the collection of one or more superior object
   **                            classes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final void registerObjectClass(final String oid, final ObjectClass.Kind kind, final String name, final Set<String> alias, final String description, final Set<String> required, final Set<String> optional, final Set<String> superior) {
    this.objectClass.put(name, new ObjectClass(oid, kind, name, alias, description, required, optional, superior));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Return the {@link ObjectClass} of the schema upon request.
   ** <p>
   ** The {@link ObjectClass}es are cached over the lifetime of this instance.
   **
   ** @param  name               the name of the desired {@link ObjectClass}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ObjectClass} mapped at the specified
   **                            <code>name</code> or <code>null</code> if no
   **                            {@link ObjectClass} is mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link ObjectClass}.
   */
  public ObjectClass objectClass(final String name) {
    return this.objectClass.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Return the entire collection of attributes that are required by the object
   ** class hierarchy.
   ** <p>
   ** The {@link ObjectClass}es and {@link Attribute}s are cached over the
   ** lifetime of this instance.
   **
   ** @param  clazz              the {@link ObjectClass} to populate the
   **                            required attributes for.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    the collection of attribute names that are
   **                            required by the object class hierarchy.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link DirectoryAttribute}.
   */
  public Set<DirectoryAttribute> required(final ObjectClass clazz) {
    return clazz.required(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optional
  /**
   ** Return the entire collection of attributes that are optional by the object
   ** class hierarchy.
   ** <p>
   ** The {@link ObjectClass}es and {@link Attribute}s are cached over the
   ** lifetime of this instance.
   **
   ** @param  clazz              the {@link ObjectClass} to populate the
   **                            optional attributes for.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    the collection of attribute names that are
   **                            optional by the object class hierarchy.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link DirectoryAttribute}.
   */
  public Set<DirectoryAttribute> optional(final ObjectClass clazz) {
    return clazz.optional(this);
  }
}