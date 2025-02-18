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
    Subsystem   :   Connector Bundle Framework

    File        :   Descriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Descriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.EnumSet;
import java.util.EnumMap;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import java.io.Serializable;

import oracle.hst.foundation.SystemConstant;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.AbstractLoggable;
import oracle.iam.identity.foundation.AttributeTransformation;

////////////////////////////////////////////////////////////////////////////////
// class Descriptor
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Descriptor</code> is intend to use where outbound attributes of an
 ** Identity Manager Object (core or user defined) are mapped to a provisioning
 ** target systems or reconcilaition source systems.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Descriptor extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The column type name of a multi-valued reocncilitaion profile attribute.
   */
  public static final String                            MULTI_VALUE    = "Multi-Valued";

  /**
   ** Attribute tag which must be defined on a descriptor configuration to hold
   ** the attribute refererenced as <code>Uid</code>.
   */
  public static final String                            IDENTIFIER     = "identifier";

  /**
   ** Attribute tag which must be defined on a descriptor configuration to hold
   ** the attribute refererenced as <code>Name</code>.
   */
  public static final String                            UNIQUENAME     = "uniqueName";

  /**
   ** Attribute tag which must be defined on a descriptor configuration to hold
   ** the name of the attribute that is the status identifier of an attribute
   ** set.
   */
  public static final String                            STATUS         = "status";

  /**
   ** Attribute tag which must be defined on a descriptor configuration to hold
   ** the name of the attribute that is the password identifier of an attribute
   ** set.
   */
  public static final String                            PASSWORD       = "password";

  /**
   ** Attribute tag which must be defined on a descriptor configuration to hold
   ** the name of the attribute that is the hierarchally identifier of an
   ** attribute set.
   */
  public static final String                            HIERARCHY      = "hierarchy";

  /**
   ** Attribute tag which may be defined on a descriptor configuration to
   ** determine attribute value transformation.
   */
  public static final String                            TRANSFORMATION = "transformation";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final AbstractLookup                        option;
  protected final Set<Template>                         template;
  protected final Set<Attribute>                        attribute;
  protected final AttributeTransformation               transformer;

  protected final Map<Pair<String, String>, Descriptor> reference      = new LinkedHashMap<Pair<String, String>, Descriptor>();;

  protected final EnumMap<Action.Phase, Action>         action         = new EnumMap<>(Action.Phase.class);;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Template
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>Template</code> defines the constant properties of an descriptor
   ** attribute.
   */
  public static class Template {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final Type   type;
    public final String id;
    public final String source;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // enum Type
    // ~~~~ ~~~~
    /**
     ** The <code>Type</code> implements the base functionality for describing
     ** an attribute type in.
     ** <p>
     ** The following schema fragment specifies the expected content contained
     ** within this class.
     ** <pre>
     ** &lt;simpleType name="type"&gt;
     **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     **     &lt;enumeration value="string"/&gt;
     **     &lt;enumeration value="date"/&gt;
     **   &lt;/restriction&gt;
     ** &lt;/simpleType&gt;
     ** </pre>
     */
    public static enum Type {
        DATE("date",           Date.class)
      , LONG("long",           Long.class)
      , STRING("string",       String.class)
      , BOOLEAN("boolean",     Boolean.class)
      , CALENDAR("calendar",   Calendar.class)
      , SENSITIVE("sensitive", GuardedString.class)
      ;

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      // the official serial version ID which says cryptically which version
      // we're compatible with
      private static final long serialVersionUID = -1L;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      public final String       value;
      public final Class<?>     clazz;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Type</code> which has a name and class type.
       **
       ** @param  value          the name value for this attribute type.
       **                        <br>
       **                        Allowed object is {@link String}.
       ** @param  type           the implementation type attribute.
       **                        <br>
       **                        Allowed object is {@link Class}.
       */
      Type(final String value, final Class<?> type) {
        // initialize instance attributes
        this.value = value;
        this.clazz = type;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: from
      /**
       ** Factory method to create a proper <code>Type</code> from the given
       ** string value.
       **
       ** @param  value          the string value the type should be returned
       **                        for.
       **                        <br>
       **                        Allowed object is {@link String}.
       **
       ** @return                the type.
       **                        <br>
       **                        Possible object is <code>Type</code>.
       */
      public static Type from(final String value) {
        for (Type cursor : Type.values()) {
          if (cursor.value.equals(value))
            return cursor;
        }
        throw new IllegalArgumentException(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Template</code> with the specified target and
     ** source name.
     ** <br>
     ** The type of the attribute is per default <code>String.class</code>.
     **
     ** @param  type             the implementation type attribute.
     **                          <br>
     **                          Allowed object is {@link Class}.
     ** @param  target           the left hand side an attribute declaration
     **                          aka the name of attribute in the Service
     **                          Provider target system.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  source           the right hand side an attribute declaration
     **                          aka the name of attribute in the Identity
     **                          Manager data model.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  flag             the flags describing the capabilities of the
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link Flag}.
     */
    private Template(final Type type, final String target, final String source) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.type   = type;
      this.id     = target;
      this.source = source;
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
     **                          Possible object <code>int</code>.
     */
    @Override
    public int hashCode() {
      return super.hashCode() ^ (this.id != null ? this.id.hashCode() : 0);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** Two <code>Template</code>s are considered equal if and only if they
     ** represent the same source. As a consequence, two given
     ** <code>Template</code>s may be different even though they contain the
     ** same set of values with the same values, but in a different order.
     **
     ** @param other             the object to compare this
     **                          <code>Template</code> against.
     **
     ** @return                  <code>true</code> if the
     **                          <code>Template</code>s are
     **                          equal; <code>false</code> otherwise.
     **                          <br>
     **                          Possible object <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Template that = (Template)other;
      return !(this.id != null ? !this.id.equals(that.id) : that.id != null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Attribute</code> defines the properties of an descriptor attribute.
   */
  public static class Attribute extends Template {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** indicating that the value is evaluated at runtime using the source
     ** attribute as the script template.
     */
    private final EnumSet<Flag> flag = EnumSet.noneOf(Flag.class);

    ////////////////////////////////////////////////////////////////////////////
    // enum Flag
    // ~~~~ ~~~~
    /**
     ** Java class for flag.
     ** <p>
     ** The following schema fragment specifies the expected content contained
     ** within this class.
     ** <pre>
     ** &lt;simpleType name="flag"&gt;
     **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     **     &lt;enumeration value="sensitive"/&gt;
     **     &lt;enumeration value="required"/&gt;
     **     &lt;enumeration value="lookup"/&gt;
     **     &lt;enumeration value="ignore"/&gt;
     **   &lt;/restriction&gt;
     ** &lt;/simpleType&gt;
     ** </pre>
     */
    public static enum Flag {
        /** Determines to transfer of the attibute value in a secure way */
        SENSITIVE("sensitive")
        /** Determines that the attibute requires a value */
      , REQUIRED("required")
        /** Determines that the attibute is ignored */
      , IGNORE("ignore")
        /** Determines that the attibute is an entitlement */
      , ENTITLEMENT("entitlement")
      ;

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      // the official serial version ID which says cryptically which version
      // we're compatible with
      private static final long serialVersionUID = -1L;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      public final String       value;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Flag</code> that allows use as a JavaBean.
       **
       ** @param  value          the value.
       */
      Flag(final String value) {
        // initialize instance attributes
        this.value = value;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   from
      /**
       ** Factory method to create a proper Flag from the given string value.
       **
       ** @param  value          the string value the flag should be returned
       **                        for.
       **
       ** @return                the flag.
       */
      public static Flag from(final String value) {
        for (Flag cursor : Flag.values()) {
          if (cursor.value.equals(value))
            return cursor;
        }
        throw new IllegalArgumentException(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Attribute</code> with the specified target and
     ** source name.
     ** <br>
     ** The type of the attribute is per default <code>String.class</code>.
     **
     ** @param  type             the implementation type attribute.
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  target           the left hand side an attribute declaration
     **                          aka the name of attribute in the Service
     **                          Provider target system.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  source           the right hand side an attribute declaration
     **                          aka the name of attribute in the Identity
     **                          Manager data model.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  flag             the flags describing the capabilities of the
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link Flag}.
     */
    private Attribute(final Type type, final String target, final String source, final EnumSet<Flag> flag) {
      // ensure inheritance
      super(type, target, source);

      // initialize instance attributes
      this.flag.addAll(flag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: flag
    /**
     ** Returns the value of the flag property.
     ** <p>
     ** This accessor method returns a reference to the live set, not a
     ** snapshot. Therefore any modification you make to the returned set will
     ** be present inside the object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the flag
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **    flag().put(newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link Flag}.
     **
     ** @return                  the live set of the assigned
     **                          <code>Flag</code>s.
     **                          <br>
     **                          Possible object is {@link EnumSet} where each
     **                          element is of type {@link Flag}.
     */
    public EnumSet<Flag> flag() {
      return this.flag;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: uid
    /**
     ** Returns <code>true</code> if this attribute descriptor identifier is the
     ** unique identifer.
     **
     ** @return                  <code>true</code> if this attribute
     **                          descriptor identifier is the unique
     **                          identifer; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object <code>boolean</code>.
     */
    public boolean uid() {
      return Uid.NAME.equals(this.id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: uid
    /**
     ** Returns <code>true</code> if this attribute descriptor identifier is the
     ** unique name.
     **
     ** @return                  <code>true</code> if this attribute
     **                          descriptor identifier is the unique
     **                          name; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object <code>boolean</code>.
     */
    public boolean name() {
      return Name.NAME.equals(this.id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: required
    /**
     ** Returns <code>true</code> if this attribute is a protected attribute.
     ** <br>
     ** This does not mean that this attribute is required for the transfer to
     ** the Identity Connector Server itself but its required for the
     ** transformation of any event.
     ** <p>
     ** Convenience method to shortens the access to the attribute flags.
     **
     ** @return                  <code>true</code> if this attribute is a
     **                          required attribute; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object <code>boolean</code>.
     */
    public boolean required() {
      return this.flag.contains(Flag.REQUIRED);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sensitive
    /**
     ** Returns <code>true</code> if the value of this attribute needs secure
     ** transmission.
     ** <p>
     ** Convenience method to shortens the access to the attribute flags.
     **
     ** @return                  <code>true</code> if the value of this
     **                          attribute needs secure transmission; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object <code>boolean</code>.
     */
    public boolean sensitive() {
      return this.flag.contains(Flag.SENSITIVE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ignore
    /**
     ** Returns <code>true</code> if this attribute can be ignored as an
     ** attribute transfered to the Identity Connector Server.
     ** <p>
     ** Convenience method to shortens the access to the attribute flags.
     **
     ** @return                  <code>true</code> if this attribute can be
     **                          ignored as an attribute transfered to the
     **                          Identity Connector Server; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object <code>boolean</code>.
     */
    public boolean ignore() {
      return this.flag.contains(Flag.IGNORE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: entitlement
    /**
     ** Returns <code>true</code> if this attribute is an entitlement attribute
     ** in Identity Manager.
     ** <p>
     ** Convenience method to shortens the access to the attribute flags.
     **
     ** @return                  <code>true</code> if this attribute is a
     **                          entitlement attribute in Identity Manager;
     **                          otherwise <code>false</code>.
     **                          <br>
     **                          Possible object <code>boolean</code>.
     */
    public boolean entitlement() {
      return this.flag.contains(Flag.ENTITLEMENT);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character "," (comma).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder("{\"Attribute\":");
      builder.append("\"").append(DescriptorParser.ATTRIBUTE_SOURCE).append("\":\"").append(this.source).append("\",");
      builder.append("\"").append(DescriptorParser.ATTRIBUTE_NAME).append("\":\"").append(this.id).append("\",");
      builder.append("\"").append(DescriptorParser.ATTRIBUTE_TYPE).append("\":\"").append(this.type.clazz.getSimpleName()).append("\",");
      builder.append("\"").append(DescriptorParser.ELEMENT_FLAG).append("\":\"").append(this.flag.toString()).append("\"}");
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Provisioning
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** The meta-data descriptor specific for provisioning.
   */
  public static class Provisioning extends Descriptor {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Metadata Descriptor</code> which is associated
     ** with the specified logging provider <code>loggable</code>.
     ** <p>
     ** The instance created through this constructor is be populated from the
     ** provided <code>attribute</code>s and <code>transformation</code>s
     **
     ** @param  loggable         the {@link Loggable} that instantiate this
     **                          <code>Metadata Descriptor</code> configuration
     **                          wrapper.
     **                          <br>
     **                          Allowed object is {@link Loggable}.
     ** @param  template         the {@link Set} of {@link Template}s of this
     **                          descriptor.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link Template}s.
     ** @param  attribute        the {@link Set} of {@link Attribute}s of this
     **                          descriptor.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link Attribute}.
     ** @param  transformer      the {@link AttributeTransformation} of this
     **                          descriptor.
     **                          <br>
     **                          Allowed object is
     **                          {@link AttributeTransformation}.
     */
    private Provisioning(final Loggable loggable, final Set<Template> template, final Set<Attribute> attribute, final AttributeTransformation transformer) {
      // ensure inheritance
      super(loggable, template, attribute, transformer);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Reconciliation
  // ~~~~~ ~~~~~~~~~~~~~~
  /**
   ** The meta-data descriptor specific for reconciliation.
   */
  public static class Reconciliation extends Descriptor {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Metadata Descriptor</code> which is associated
     ** with the specified logging provider <code>loggable</code>.
     ** <p>
     ** The instance created through this constructor is be populated from the
     ** provided <code>attribute</code>s and <code>transformation</code>s
     **
     ** @param  loggable         the {@link Loggable} that instantiate this
     **                          <code>Metadata Descriptor</code> configuration
     **                          wrapper.
     **                          <br>
     **                          Allowed object is {@link Loggable}.
     ** @param  template         the {@link Set} of {@link Template}s of this
     **                          descriptor.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link Template}s.
     ** @param  attribute        the {@link Set} of {@link Attribute}s of this
     **                          descriptor.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link Attribute}.
     ** @param  transformer      the {@link AttributeTransformation} of this
     **                          descriptor.
     **                          <br>
     **                          Allowed object is
     **                          {@link AttributeTransformation}.
     */
    private Reconciliation(final Loggable loggable, final Set<Template> template, final Set<Attribute> attribute, final AttributeTransformation transformer) {
      // ensure inheritance
      super(loggable, template, attribute, transformer);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Action
  // ~~~~~ ~~~~~~
  /**
   ** <code>Action</code> defines the properties of an action which is executed
   ** by the Connector Server or directly at the Target system.
   */
  public static class Action {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Target                           target;
    private Language                         language;

    /** the command to send for execution */
    private String                           command;

    /** the parameters to send for execution */
    private List<Pair<String, Serializable>> option   = new ArrayList<Pair<String, Serializable>>();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // enum Target
    // ~~~~ ~~~~~~
    /**
     ** The <code>Target</code> implements the base functionality for describing
     ** an action target in.
     ** <p>
     ** The following schema fragment specifies the expected content contained
     ** within this class.
     ** <pre>
     ** &lt;simpleType name="target"&gt;
     **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     **     &lt;enumeration value="connector"/&gt;
     **     &lt;enumeration value="resource"/&gt;
     **   &lt;/restriction&gt;
     ** &lt;/simpleType&gt;
     ** </pre>
     */
    public static enum Target {
        CONNECTOR("connector")
      , RESOURCE("resource")
      ;

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      // the official serial version ID which says cryptically which version
      // we're compatible with
      private static final long serialVersionUID = -1L;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Target</code> which has an id.
       **
       ** @param  id             the identifier value for this action target.
       **                        <br>
       **                        Allowed object is {@link String}.
       */
      Target(final String id) {
        // initialize instance attributes
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: id
      /**
       ** Returns the identifier of the action target.
       **
       ** @return                the identifier of the action target.
       **                        <br>
       **                        Possible object is {@link String}.
       */
      public String id() {
        return this.id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   from
      /**
       ** Factory method to create a proper target from the given string
       ** value.
       **
       ** @param  value          the string value the target should be
       **                        returned for.
       **                        <br>
       **                        Allowed object is {@link String}.
       **
       ** @return                the <code>Target</code>.
       **                        <br>
       **                        Possible object is <code>Target</code>.
       */
      public static Target from(final String value) {
        for (Target cursor : Target.values()) {
          if (cursor.id.equals(value))
            return cursor;
        }
        throw new IllegalArgumentException(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // enum Phase
    // ~~~~ ~~~~~
    /**
     ** The <code>Phase</code> implements the base functionality for describing
     ** the time an action event happens.
     ** <p>
     ** The following schema fragment specifies the expected content contained
     ** within this class.
     ** <pre>
     ** &lt;simpleType name="phase"&gt;
     **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     **     &lt;enumeration value="create-before"/&gt;
     **     &lt;enumeration value="create-after"/&gt;
     **     &lt;enumeration value="modify-before"/&gt;
     **     &lt;enumeration value="modify-after"/&gt;
     **     &lt;enumeration value="delete-before"/&gt;
     **     &lt;enumeration value="delete-after"/&gt;
     **   &lt;/restriction&gt;
     ** &lt;/simpleType&gt;
     ** </pre>
     */
    public static enum Phase {
        CREATE_BEFORE("create-before")
      , CREATE_AFTER("create-after")
      , MODIFY_BEFORE("modify-before")
      , MODIFY_AFTER("modify-after")
      , DELETE_BEFORE("delete-before")
      , DELETE_AFTER("delete-after")
      ;

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      // the official serial version ID which says cryptically which version
      // we're compatible with
      private static final long serialVersionUID = -1L;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Phase</code> which has an id.
       **
       ** @param  id             the identifier value for this action timing.
       **                        <br>
       **                        Allowed object is {@link String}.
       */
      Phase(final String id) {
        // initialize instance attributes
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: id
      /**
       ** Returns the identifier of the action language.
       **
       ** @return                the identifier of the action language.
       **                        <br>
       **                        Possible object is {@link String}.
       */
      public String id() {
        return this.id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   from
      /**
       ** Factory method to create a proper action from the given string
       ** value.
       **
       ** @param  value          the string value the action should be
       **                        returned for.
       **                        <br>
       **                        Allowed object is {@link String}.
       **
       ** @return                the <code>Phase</code>.
       **                        <br>
       **                        Possible object is <code>Phase</code>.
       */
      public static Phase from(final String value) {
        for (Phase cursor : Phase.values()) {
          if (cursor.id.equals(value))
            return cursor;
        }
        throw new IllegalArgumentException(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // enum Language
    // ~~~~ ~~~~~~~~
    /**
     ** The <code>Language</code> implements the base functionality for
     ** describing an action language in.
     ** <p>
     ** The following schema fragment specifies the expected content contained
     ** within this class.
     ** <pre>
     ** &lt;simpleType name="language"&gt;
     **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     **     &lt;enumeration value="bash"/&gt;
     **     &lt;enumeration value="groovy"/&gt;
     **     &lt;enumeration value="powershell"/&gt;
     **   &lt;/restriction&gt;
     ** &lt;/simpleType&gt;
     ** </pre>
     */
    public static enum Language {
        BASH("bash")
      , GROOVY("groovy")
      , POWERSHELL("powershell")
      ;

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      // the official serial version ID which says cryptically which version
      // we're compatible with
      private static final long serialVersionUID = -1L;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Language</code> which has an id.
       **
       ** @param  id             the identifier value for this action language.
       **                        <br>
       **                        Allowed object is {@link String}.
       */
      Language(final String id) {
        // initialize instance attributes
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: id
      /**
       ** Returns the identifier of the action language.
       **
       ** @return                the identifier of the action language.
       **                        <br>
       **                        Possible object is {@link String}.
       */
      public String id() {
        return this.id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   from
      /**
       ** Factory method to create a proper language from the given string
       ** value.
       **
       ** @param  value          the string value the language should be
       **                        returned for.
       **                        <br>
       **                        Allowed object is {@link String}.
       **
       ** @return                the <code>Language</code>.
       **                        <br>
       **                        Possible object is <code>Language</code>.
       */
      public static Language from(final String value) {
        for (Language cursor : Language.values()) {
          if (cursor.id.equals(value))
            return cursor;
        }
        throw new IllegalArgumentException(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Action</code> with the specified target and
     ** language.
     **
     ** @param  target           the target of the event.
     **                          <br>
     **                          Allowed object
     **                          {@link Descriptor.Action.Target}.
     ** @param  language         the language of the action
     **                          <br>
     **                          Allowed object is
     **                          {@link Descriptor.Action.Language}.
     */
    private Action(final Action.Target target, final Action.Language language) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.target   = target;
      this.language = language;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: target
    /**
     ** Returns the target for this action.
     **
     ** @return                    the target for this action.
     **                            <br>
     **                            Possible object is {@link Target}.
     */
    public final Target target() {
      return this.target;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: language
    /**
     ** Returns the language for this action.
     **
     ** @return                    the language for this action.
     **                            <br>
     **                            Possible object is {@link Language}.
     */
    public final Language language() {
      return this.language;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: command
    /**
     ** Sets the command for this action.
     **
     ** @param  value              the command for this action.
     **                            <br>
     **                            Allowed object is {@link String}.
     */
    public final void command(final String value) {
      this.command = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: command
    /**
     ** Returns the command for this action.
     **
     ** @return                    the command for this action.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String command() {
      return this.command;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: option
    /**
     ** Returns the value of the option property.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the object.
     ** <br>
     ** This is why there is not a <code>set</code> method for the flag
     ** property.
     ** <p>
     ** For example, to add a new item, do as follows:
     ** <pre>
     **   option().add(newItem);
     ** </pre>
     ** Objects of the following type(s) are allowed in the list
     ** {@link Pair}.
     **
     ** @return                  the live set of the assigned
     **                          <code>option</code>s.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link Pair}.
     */
    public List<Pair<String, Serializable>> option() {
      return this.option;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Metadata Descriptor</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor is be populated from the
   ** provided <code>attribute</code>s and <code>transformation</code>s
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  template           the {@link Set} of {@link Template}s of this
   **                            descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Template}s.
   ** @param  attribute          the {@link Set} of {@link Attribute}s of this
   **                            descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  transformer        the {@link AttributeTransformation} of this
   **                            descriptor.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeTransformation}.
   */
  Descriptor(final Loggable loggable, final Set<Template> template, final Set<Attribute> attribute, final AttributeTransformation transformer) {
    // ensure inheritance
    super(loggable);

    // initialize instance attributes
    this.template    = template;
    this.attribute   = attribute;
    this.transformer = transformer;
    this.option      = new AbstractLookup(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Sets the single-valued attribute that represents the unique identifier
   ** of an object within the name-space of the target resource. If possible,
   ** this unique identifier also should be immutable.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTIFIER}.
   **
   ** @param  value              the single-valued attribute that represents the
   **                            unique identifier of an object within the
   **                            name-space of the target resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void identifier(final String value) {
    this.option.attribute(IDENTIFIER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the single-valued attribute that represents the unique identifier
   ** of an object within the name-space of the target resource. If possible,
   ** this unique identifier also should be immutable.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTIFIER}.
   **
   ** @return                    the single-valued attribute that represents the
   **                            unique identifier of an object within the
   **                            name-space of the target resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String identifier() {
    return this.option.stringValue(IDENTIFIER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName
  /**
   ** Sets the single-valued attribute that represents the user-friendly unique
   ** name of an object on a target resource.
   ** <br>
   ** For instance, the name of an <code>Account</code> will most often be its
   ** loginName. The value of <code>Name</code> need not be unique within
   ** ObjectClass. In LDAP, for example, the <code>Name</code> could be the
   ** Common Name (CN). Contrast this with <code>Uid</code>, which is intended
   ** to be a unique identifier (and, if possible, immutable):
   ** <ul>
   **   <li>When an application creates an object, the application uses the
   **       <code>Name</code> attribute to supply the user-friendly identifier
   **       for the object. (Because the create operation returns the
   **       <code>Uid</code> as its result, the application cannot know the
   **       <code>Uid</code> value beforehand.)
   **   <li>When an application renames an object, this changes the
   **       <code>Name</code> of the object. (For some target resources that do
   **       not have a separate internal identifier, this might also change the
   **       <code>Uid</code>. However, the application would never attempt to
   **       change the <code>Uid</code> directly.)
   ** </ul>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #UNIQUENAME}.
   **
   ** @param  value              the single-valued attribute that represents the
   **                            user-friendly identifier of an object on a
   **                            target resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void uniqueName(final String value) {
    this.option.attribute(UNIQUENAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName
  /**
   ** Returns the single-valued attribute that represents the user-friendly
   ** unique name of an object on a target resource.
   ** <br>
   ** For instance, the name of an <code>Account</code> will most often be its
   ** loginName. The value of <code>Name</code> need not be unique within
   ** ObjectClass. In LDAP, for example, the <code>Uid</code> could be the
   ** Common Name (CN). Contrast this with <code>Uid</code>, which is intended
   ** to be a unique identifier (and, if possible, immutable):
   ** <ul>
   **   <li>When an application creates an object, the application uses the
   **       <code>Name</code>} attribute to supply the user-friendly identifier
   **       for the object. (Because the create operation returns the
   **       <code>Uid</code> as its result, the application cannot know the
   **       <code>Uid</code> value beforehand.)
   **   <li>When an application renames an object, this changes the
   **       <code>Name</code> of the object. (For some target resources that do
   **       not have a separate internal identifier, this might also change the
   **       <code>Name</code>. However, the application would never attempt to
   **       change the <code>Uid</code> directly.)
   ** </ul>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #UNIQUENAME}.
   **
   ** @return                    the single-valued attribute that represents the
   **                            ser-friendly identifier of an object on a
   **                            target resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String uniqueName() {
    return this.option.stringValue(UNIQUENAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Sets the single-valued attribute that represents the status of an
   ** object within the name-space of the target resource.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #STATUS}.
   **
   ** @param  value              the single-valued attribute that represents the
   **                            status of an object within the name-space of
   **                            the target resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void status(final String value) {
    this.option.attribute(STATUS, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the single-valued attribute that represents the status of an
   ** object within the name-space of the target resource.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #STATUS}.
   **
   ** @return                    the single-valued attribute that represents the
   **                            status of an object within the name-space of
   **                            the target resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String status() {
    return this.option.stringValue(STATUS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Sets the single-valued attribute that represents the password of an
   ** object within the name-space of the target resource.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #PASSWORD}.
   **
   ** @param  value              the single-valued attribute that represents the
   **                            password of an object within the name-space of
   **                            the target resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void password(final String value) {
    this.option.attribute(PASSWORD, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the single-valued attribute that represents the password of an
   ** object within the name-space of the target resource.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #PASSWORD}.
   **
   ** @return                    the single-valued attribute that represents the
   **                            password of an object within the name-space of
   **                            the target resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String password() {
    return this.option.stringValue(PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformation
  /**
   ** Whether if a transformation should be done after the values are fetched
   ** from the process form.
   **
   ** @param  value              <code>true</code> if a transformation should be
   **                            done after the values are fetched from the
   **                            process form.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public final void transformation(final boolean value) {
    this.option.attribute(TRANSFORMATION, value ? SystemConstant.TRUE : SystemConstant.FALSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformation
  /**
   ** Returns <code>true</code> if a transformation should be done after the
   ** values are fetched from the process form.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #TRANSFORMATION}.
   **
   ** @return                    <code>true</code> if a transformation should be
   **                            done after the values are fetched from the
   **                            process form.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean transformation() {
    return this.option.booleanValue(TRANSFORMATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformer
  /**
   ** Returns the attribute transformation of this reference descriptor.
   **
   ** @return                    {@link AttributeTransformation} used by this
   **                            reference descriptor.
   **                            <br>
   **                            Possible object
   **                            {@link AttributeTransformation}.
   */
  public final AttributeTransformation transformer() {
    return this.transformer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
  /**
   ** Returns the value of the template property.
   ** <p>
   ** This accessor method returns a reference to the live set, not a
   ** snapshot. Therefore any modification you make to the returned set will be
   ** present inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for this property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    template().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the set {@link Template}.
   **
   ** @return                    the live set of the assigned
   **                            <code>Template</code>s.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Template}.
   */
  public Set<Template> template() {
    return this.template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the value of the attribute property.
   ** <p>
   ** This accessor method returns a reference to the live set, not a
   ** snapshot. Therefore any modification you make to the returned set will be
   ** present inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for this property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    attribute().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the set {@link Attribute}.
   **
   ** @return                    the live set of the assigned
   **                            <code>Attribute</code>s.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   */
  public Set<Attribute> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the multi-value descriptor property.
   ** <p>
   ** This accessor method returns a reference to the live map, not a
   ** snapshot. Therefore any modification you make to the returned map will be
   ** present inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for this property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    multivalue().put(key, newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the set {@link Attribute}.
   **
   ** @return                    the live map of the assigned
   **                            <code>Attribute</code>s.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link Pair} for the key
   **                            and {@link Set} of {@link Attribute}s as the
   **                            value.
   */
  public Map<Pair<String, String>, Descriptor> reference() {
    return this.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the value of the attribute property for a certain section of the
   ** multi-value descriptors.
   ** <p>
   ** This accessor method returns a reference to the live set, not a
   ** snapshot. Therefore any modification you make to the returned set will be
   ** present inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for this property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    multivalue(key).add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the map {@link Attribute}.
   **
   ** @param  section            the name of the section to lookup the
   **                            multi-value attribute mapping.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   **
   ** @return                    the live set of the assigned
   **                            <code>Attribute</code>s.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   */
  public Descriptor reference(final Pair<String, String> section) {
    return this.reference.get(section);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   option
  /**
   ** Returns the value of the option property.
   ** <p>
   ** This accessor method returns a reference to the live map, not a
   ** snapshot. Therefore any modification you make to the returned map will be
   ** present inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for this property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    option().attribute(newKey, newValue);
   ** </pre>
   ** Objects of the following type(s) are allowed in the map are {@link String}
   ** as the key and {@link String} as the value.
   **
   ** @return                    the live map of the assigned
   **                            <code>Attribute</code>s.
   **                            <br>
   **                            Possible object is {@link AbstractLookup} where
   **                            each element is of type {@link String} for the
   **                            key and {@link String} for the value.
   */
  public AbstractLookup option() {
    return this.option;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   option
  /**
   ** Returns the action configured.
   **
   ** @param  timing             the timing event to lookup the action
   **                            <br>
   **                            Allowed object is {@link Action.Phase}.
   **
   ** @return                    the actio for specified timing.
   **                            <br>
   **                            Possible object is {@link Action}.
   */
  public Action action(final Action.Phase timing) {
    return this.action.get(timing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   source
  /**
   ** Transforms the registered {@link Attribute}s to a {@link Collection} of
   ** {@link String}s that can be later used in query operations to specify the
   ** names of the attributes to get from <code>Identity Manager</code>.
   **
   ** @return                    a {@link Set} of {@link String}s containing the
   **                            name of the attributes belonging to the
   **                            <code>Target System</code> to reconcile.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public Set<String> source() {
    final Set<String> latch = new LinkedHashSet<String>();
    this.attribute.forEach(n -> latch.add(n.source));
    return latch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Transforms the registered {@link Attribute}s to a {@link Collection} of
   ** {@link String}s that can be later used in mapping operations to specify
   ** the names of the attribute gotten from the <code>Target System</code>.
   **
   ** @return                    a {@link Set} of {@link String}s containing the
   **                            name of the attributes belonging to
   **                            <code>Identity Manager</code> to reconcile.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public Set<String> target() {
    final Set<String> latch = new LinkedHashSet<String>();
    this.attribute.forEach(n -> latch.add(n.id));
    return latch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referenceSource
  /**
   ** Transforms the registered {@link Attribute}s of a referenece to a
   ** {@link Collection} of {@link String}s that can be later used in query
   ** operations to specify the names of the collections to get from the
   ** <code>Target System</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The type of the returned collection must be an arry due to the limits of
   ** serialization capabilities of the ICF connector frameork.
   ** <br>
   ** What a shame.
   **
   ** @return                    a {@link Set} of {@link String}s containing the
   **                            name of the attributes belonging to the
   **                            <code>Target System</code> to reconcile.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public Set<String> referenceSource() {
    final Set<String> latch = new LinkedHashSet<String>();
    this.reference.keySet().forEach(n -> latch.add(n.value));
    return latch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referenceTarget
  /**
   ** Transforms the registered {@link Attribute}s of a referenece to a
   ** {@link Collection} of {@link String}s that can be later used in mapping
   ** operations to specify the names of the collections gotten from the
   ** <code>Target System</code>.
   **
   ** @return                    a {@link Set} of {@link String}s containing the
   **                            name of the attributes belonging to
   **                            <code>Identity Manager</code> to reconcile.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public Set<String> referenceTarget() {
    final Set<String> latch = new LinkedHashSet<String>();
    this.reference.keySet().forEach(n -> latch.add(n.tag));
    return latch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildProvisioning
  /**
   ** Factory method to create a provisioning meta-data descriptor.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Provisioning</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Provisioning}.
   */
  public static Provisioning buildProvisioning(final Loggable loggable) {
    return buildProvisioning(loggable, new LinkedHashSet<Template>(), new LinkedHashSet<Attribute>(), new AttributeTransformation(loggable));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildProvisioning
  /**
   ** Factory method to create a provisioning meta-data descriptor.
   ** <br>
   ** The purpose of this method is primarly unit testing.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  template           the {@link Set} of {@link Template}s of this
   **                            descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Template}s.
   ** @param  attribute          the {@link Set} of {@link Attribute}s of the
   **                            meta-data descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  transformer        the {@link AttributeTransformation} of the
   **                            meta-data descriptor.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeTransformation}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Provisioning</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Provisioning}.
   */
  public static Provisioning buildProvisioning(final Loggable loggable, final Set<Template> template, final Set<Attribute> attribute, final AttributeTransformation transformer) {
    return new Provisioning(loggable, template, attribute, transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildReconciliation
  /**
   ** Factory method to create a reconciliation meta-data descriptor.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Reconciliation</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Reconciliation}.
   */
  public static Reconciliation buildReconciliation(final Loggable loggable) {
    return buildReconciliation(loggable, new LinkedHashSet<Template>(), new LinkedHashSet<Attribute>(), new AttributeTransformation(loggable));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildReconciliation
  /**
   ** Factory method to create a reconciliation meta-data descriptor.
   ** <br>
   ** The purpose of this method is primarly unit testing.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  template           the {@link Set} of {@link Template}s of this
   **                            descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Template}s.
   ** @param  attribute          the {@link Set} of {@link Attribute}s of the
   **                            meta-data descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  transformer        the {@link AttributeTransformation} of the
   **                            meta-data descriptor.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeTransformation}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Reconciliation</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Reconciliation}.
   */
  public static Reconciliation buildReconciliation(final Loggable loggable, final Set<Template> template, final Set<Attribute> attribute, final AttributeTransformation transformer) {
    return new Reconciliation(loggable, template, attribute, transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildReference
  /**
   ** Factory method to create a reference meta-data descriptor.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an instance of <code>Descriptor</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object {@link Descriptor}.
   */
  public static Descriptor buildReference(final Loggable loggable) {
    return buildReference(loggable, new LinkedHashSet<Template>(), new LinkedHashSet<Attribute>(), new AttributeTransformation(loggable));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildReference
  /**
   ** Factory method to create a reference meta-data descriptor.
   ** <br>
   ** The purpose of this method is primarly unit testing.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  template           the {@link Set} of {@link Template}s of the
   **                            meta-data descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Template}s.
   ** @param  attribute          the {@link Set} of {@link Attribute}s of the
   **                            meta-data descriptor.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  transformer        the {@link AttributeTransformation} of the
   **                            meta-data descriptor.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeTransformation}.
   **
   ** @return                    an instance of <code>Descriptor</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object {@link Descriptor}.
   */
  public static Descriptor buildReference(final Loggable loggable, final Set<Template> template, final Set<Attribute> attribute, final AttributeTransformation transformer) {
    return new Descriptor(loggable, template, attribute, transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildTemplate
  /**
   ** Factory method to create a template with the specified target and
   ** source expression.
   ** <br>
   ** The type of the template is per default <code>String.class</code>.
   **
   ** @param  target             the left hand side a template declaration
   **                            aka the name of attribute in the Service
   **                            Provider target system.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  source             the right hand side an attribute declaration
   **                            aka the name of attribute in the Identity
   **                            Manager data model.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Template</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Template}.
   */
  public static Template buildTemplate(final String target, final String source) {
    return buildTemplate(Template.Type.STRING, target, source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildTemplate
  /**
   ** Factory method to create a template with the specified target and
   ** source expression.
   ** <br>
   ** The type of the template is per default <code>String.class</code>.
   **
   ** @param  type               the implementation type attribute.
   **                            <br>
   **                            Allowed object
   **                            {@link Descriptor.Template.Type}.
   ** @param  target             the left hand side an attribute declaration
   **                            aka the name of attribute in the Service
   **                            Provider target system.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  source             the right hand side an attribute declaration
   **                            aka the name of attribute in the Identity
   **                            Manager data model.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Template</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Template}.
   */
  public static Template buildTemplate(final Template.Type type, final String target, final String source) {
    return new Template(type, target, source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Factory method to create an attribute with the specified target and
   ** source name.
   ** <br>
   ** The type of the attribute is per default <code>String.class</code>.
   **
   ** @param  target             the left hand side an attribute declaration
   **                            aka the name of attribute in the Service
   **                            Provider target system.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  source             the right hand side an attribute declaration
   **                            aka the name of attribute in the Identity
   **                            Manager data model.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Attribute</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Attribute}.
   */
  public static Attribute buildAttribute(final String target, final String source) {
    return buildAttribute(Attribute.Type.STRING, target, source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Factory method to create an attribute with the specified target and
   ** source name.
   ** <br>
   ** The type of the attribute is per default <code>String.class</code>.
   **
   ** @param  type               the implementation type attribute.
   **                            <br>
   **                            Allowed object
   **                            {@link Descriptor.Attribute.Type}.
   ** @param  target             the left hand side an attribute declaration
   **                            aka the name of attribute in the Service
   **                            Provider target system.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  source             the right hand side an attribute declaration
   **                            aka the name of attribute in the Identity
   **                            Manager data model.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Attribute</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Attribute}.
   */
  public static Attribute buildAttribute(final Attribute.Type type, final String target, final String source) {
    return buildAttribute(type, target, source, EnumSet.noneOf(Attribute.Flag.class));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Factory method to create an attribute with the specified target and
   ** source name.
   ** <br>
   ** The type of the attribute is per default <code>String.class</code>.
   **
   ** @param  type               the implementation type attribute.
   **                            <br>
   **                            Allowed object
   **                            {@link Descriptor.Attribute.Type}.
   ** @param  target             the left hand side an attribute declaration
   **                            aka the name of attribute in the Service
   **                            Provider target system.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  source             the right hand side an attribute declaration
   **                            aka the name of attribute in the Identity
   **                            Manager data model.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  flag               the flags describing the capabilities of the
   **                            attribute.
   **                            <br>
   **                            Allowed object
   **                            {@link Descriptor.Attribute.Flag}.
   **
   ** @return                    an instance of
   **                            <code>Descriptor.Attribute</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Attribute}.
   */
  public static Attribute buildAttribute(final Attribute.Type type, final String target, final String source, final EnumSet<Attribute.Flag> flag) {
    return new Attribute(type, target, source, flag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAction
  /**
   ** Factory method to create an action with the specified target, timing and
   ** language.
   ** <br>
   ** The type of the attribute is per default <code>String.class</code>.
   **
   ** @param  target             the target of the action.
   **                            <br>
   **                            Allowed object
   **                            {@link Descriptor.Action.Target}.
   ** @param  language           the language of the action
   **                            <br>
   **                            <br>
   **                            Allowed object is
   **                            {@link Descriptor.Action.Language}.
   **
   ** @return                    an instance of <code>Attribute</code>
   **                            with the specified properties.
   **                            <br>
   **                            Possible object
   **                            {@link Descriptor.Attribute}.
   */
  public static Action buildAction(final Action.Target target, final Action.Language language) {
    return new Action(target, language);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "," (comma).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("{\"Descriptor\": ");
    builder.append("{\"Attributes\":").append(this.attribute).append(",");
    builder.append("{\"Transformer\":").append(this.transformer).append("}");
    return builder.toString();
  }
}