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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   DirectorySchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectorySchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;
import java.util.Map;
import java.util.Queue;
import java.util.Locale;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import javax.naming.directory.DirContext;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DirectorySchema
// ~~~~~ ~~~~~~~~~~~~~~~
public class DirectorySchema extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////


  /** the any object class */
  public static final ObjectClass  ANY                       = new ObjectClass(SchemaUtility.createSpecialName("ANY"));

  /** the object class for a domain */
  public static final ObjectClass  DOMAIN                    = new ObjectClass(SchemaUtility.createSpecialName("DOMAIN"));

  /** the object class for a country */
  public static final ObjectClass  COUNTRY                   = new ObjectClass(SchemaUtility.createSpecialName("COUNTRY"));

  /** the object class for a locality */
  public static final ObjectClass  LOCALITY                  = new ObjectClass(SchemaUtility.createSpecialName("LOCALITY"));

  /** the object class for a role */
  public static final ObjectClass  ROLE                      = new ObjectClass(SchemaUtility.createSpecialName("ROLE"));
  
  /** the object class for a scope */
  public static final ObjectClass  SCOPE                     = new ObjectClass(SchemaUtility.createSpecialName("SCOPE"));

  /** the object class for an organization */
  public static final ObjectClass  ORGANIZATION              = new ObjectClass(SchemaUtility.createSpecialName("ORGANIZATION"));

  /** the object class for an organizational uit */
  public static final ObjectClass  ORGANIZATIONAL_UNIT       = new ObjectClass(SchemaUtility.createSpecialName("ORGANIZATIONALUNIT"));

  static final AttributeInfo       PASSWORD                  = AttributeInfoBuilder.build(OperationalAttributes.PASSWORD_NAME, GuardedString.class, DirectoryAttribute.NRBD_NOTREADABLE);

  private static final Mapping     ANY_SCHEMA                = new Mapping(
    ANY
  , CollectionUtility.set("*")
  , false
  , CollectionUtility.set("entryDN")
  , new AttributeInfo[0]
  );

  private static final Mapping     DOMAIN_SCHEMA             = new Mapping(
    DOMAIN
  , CollectionUtility.set("top", "domain")
  , false
  , CollectionUtility.set("dc")
  , new AttributeInfo[0]
  );

  private static final Mapping     LOCALITY_SCHEMA           = new Mapping(
    LOCALITY
  , CollectionUtility.set("top", "locality")
  , false
  , CollectionUtility.set("l")
  , new AttributeInfo[0]
  );

  private static final Mapping     COUNTRY_SCHEMA            = new Mapping(
    COUNTRY
  , CollectionUtility.set("top", "locality")
  , false
  , CollectionUtility.set("c")
  , new AttributeInfo[0]
  );

  private static final Mapping     ACCOUNT_SCHEMA            = new Mapping(
    ObjectClass.ACCOUNT
  , CollectionUtility.set("top", "person", "organizationalPerson", "inetOrgPerson")
  , false
  , CollectionUtility.set("uid", "cn")
  , new AttributeInfo[]{PASSWORD}
  );

  private static final Mapping     GROUP_SCHEMA               = new Mapping(
    ObjectClass.GROUP
  , CollectionUtility.set("top", "groupOfUniqueNames")
  , false
  , CollectionUtility.set("cn")
  , new AttributeInfo[0]
  );
  private static final Mapping     ROLE_SCHEMA                = new Mapping(
    ROLE
  , CollectionUtility.set( "top", "ldapSubentry", "nsRoleDefinition", "nsSimpleRoleDefinition", "nsManagedRoleDefinition")
  , false
  , Collections.emptySet()
  , new AttributeInfo[0]
  );

  private static final Mapping     ORGANIZATION_SCHEMA        = new Mapping(
    ORGANIZATION
  , CollectionUtility.set(new String[] { "top", "organization" })
  , false
  , CollectionUtility.set("o")
  , new AttributeInfo[0]
  );

  private static final Mapping     ORGANIZATIONAL_UNIT_SCHEMA = new Mapping(
    ORGANIZATIONAL_UNIT
  , CollectionUtility.set(new String[] { "top", "organizationalUnit" })
  , false
  , CollectionUtility.set("ou")
  , new AttributeInfo[0]
  );

  private static final Set<String> SYNTAX      = CollectionUtility.caseInsensitiveSet();
  private static final Set<String> BINARY      = CollectionUtility.caseInsensitiveSet();
  private static final Set<String> ATTRIBUTES  = CollectionUtility.caseInsensitiveSet();

  private static final String      OBJECTCLASS = "ClassDefinition";
  private static final String      ATTRIBUTE   = "AttributeDefinition";

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    ATTRIBUTES.add("entryDN");
    ATTRIBUTES.add("entryUUID");
    ATTRIBUTES.add("creatorsName");
    ATTRIBUTES.add("modifiersName");
    ATTRIBUTES.add("createTimestamp");
    ATTRIBUTES.add("modifyTimestamp");
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String>                          structuralClasses   = CollectionUtility.caseInsensitiveSet();
  protected final Map<String, Set<String>>             effectiveClasses    = CollectionUtility.caseInsensitiveMap();
  protected final Map<String, Set<String>>             mandatoryAttributes = CollectionUtility.caseInsensitiveMap();
  protected final Map<String, Set<String>>             optionalAttributes  = CollectionUtility.caseInsensitiveMap();
  protected final Map<String, DirectoryAttribute.Type> attributeProperties = CollectionUtility.caseInsensitiveMap();

  protected Schema                                     schema              = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Mapping
  // ~~~~~ ~~~~~~~
  /**
   ** Mapping descriptor for {@link ObjectClass}es.
   */
  public static final class Mapping {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final boolean            container;
    private final ObjectClass        objectClass;
    private final Set<AttributeInfo> operational;

    private Set<String>              classes;
    private Set<String>              prefix;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** @param  type               the object class instance.
     **                            <br>
     **                            Allowed object is {@link ObjectClass}.
     ** @param  classes            the collection of object class names.
     **                            <br>
     **                            Allowed object is {@link Set} where each
     **                            element is of type {@link String}.
     ** @param  container          <code>true</code> to indicate this is a
     **                            container type; otherwise
     **                            <code>false</code>.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     ** @param  prefix             the collection of prefixes applicable as
     **                            relative distinguished name.
     **                            <br>
     **                            Allowed object is {@link Set} where each
     **                            element is of type {@link String}.
     ** @param  operational        the collection of names of operational
     **                            attributes.
     **                            <br>
     **                            Allowed object is {@link Set} where each
     **                            element is of type {@link AttributeInfo}.
     */
    public Mapping(final ObjectClass type, final Set<String> classes, final boolean container, final Set<String> prefix, final AttributeInfo[] operational) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.objectClass = type;
      this.classes     = CollectionUtility.unmodifiableSet(classes);
      this.container   = container;
      this.prefix      = CollectionUtility.unmodifiableSet(prefix);
      this.operational = CollectionUtility.unmodifiableSet(operational);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   container
    /**
     ** Returns <code>true</code> to indicate this is a container type.
     **
     ** @return                    <code>true</code> to indicate this is a
     **                            container type; otherwise
     **                            <code>false</code>.
     **                            <br>
     **                            Possible object is <code>boolean</code>.
     */
    public boolean container() {
      return this.container;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   type
    /**
     ** Returns the object class instance.
     **
     ** @return                    the object class instance.
     **                            <br>
     **                            Possible object is {@link ObjectClass}.
     */
    public ObjectClass type() {
      return this.objectClass;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   classes
    /**
     ** Returns the collection of object class names.
     **
     ** @return                    the collection of object class names.
     **                            <br>
     **                            Possible object is {@link Set} where each
     **                            element is of type {@link String}.
     */
    public Set<String> classes() {
      return this.classes;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   prefix
    /**
     ** Returns the collection of prefixes applicable as relative distinguished
     ** name.
     **
     ** @return                    the collection of prefixes applicable as
     **                            relative distinguished name.
     **                            <br>
     **                            Possible object is {@link Set} where each
     **                            element is of type {@link String}.
     */
    public Set<String> prefix() {
      return this.prefix;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   operational
    /**
     ** Returns the collection of names of operational attributes.
     **
     ** @return                    the collection of names of operational
     **                            attributes.
     **                            <br>
     **                            Possible object is {@link Set} where each
     **                            element is of type {@link AttributeInfo}.
     */
    public Set<AttributeInfo> operational() {
      return this.operational;
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
      return this.objectClass.hashCode();
    }

    /////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Mapping</code> object that
     ** represents the same <code>name</code> and value as this object.
     **
     ** @param other             the object to compare this
     **                          <code>Mapping</code> against.
     **
     ** @return                  <code>true</code> if the
     **                          <code>Mapping</code>s are
     **                          equal; <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if ((other instanceof Mapping)) {
        final Mapping that = (Mapping)other;
        if (!this.objectClass.equals(that.objectClass)) {
          return false;
        }
        if (!this.classes.equals(that.classes)) {
          return false;
        }
        if (this.container != that.container) {
          return false;
        }
        if (!this.prefix.equals(that.prefix)) {
          return false;
        }
        if (!this.operational.equals(that.operational)) {
          return false;
        }
        return true;
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Schema discovery is performed by query the Directory Service.
   */
  public static final class Server extends DirectorySchema {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Use the {@link DirectoryEndpoint} passed in to immediately connect to a
     ** Directory Service.
     ** <br>
     ** If the {@link DirContext} fails a {@link SystemException} will be
     ** thrown.
     **
     ** @param  context          the Directory Service endpoint connection which
     **                          is used to discover this
     **                          {@link DirectorySchema}.
     **                          <br>
     **                          Allowed object is {@link DirectoryEndpoint}.
     **
     ** @throws SystemException  if there is a problem creating a
     **                          {@link DirContext}.
     */
    public Server(final DirectoryEndpoint context)
      throws SystemException {

      // ensure inheritance
      super(context);

      try {
        final DirContext schema = this.endpoint.connect().getSchema("");
        types(schema);
        attributes(schema);
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
      finally {
        this.endpoint.disconnect();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   types
    /**
     ** Collecting the meta-data for object classes from the connected
     ** Directory Service.
     **
     ** @throws SystemException  if the operation fails for any reason.
     */
    private void types(final DirContext context)
      throws SystemException {

      boolean structural;

      Set<String> names      = null;
      Set<String> mandatory  = null;
      Set<String> optional   = null;
      Set<String> supClasses = null;
      try {
        final NamingEnumeration cursor = context.list(OBJECTCLASS);
        while (cursor.hasMore()) {
          final NameClassPair   pair           = (NameClassPair)cursor.next();
          final String          path           = pair.isRelative() ? String.format("%s/%s", OBJECTCLASS, pair.getName()) : pair.getName();
          final BasicAttributes attrs          = (BasicAttributes)context.getAttributes(path);
          final boolean         abstractAttr   = "true".equals(DirectoryEntry.value(attrs, "ABSTRACT"));
          final boolean         structuralAttr = "true".equals(DirectoryEntry.value(attrs, "STRUCTURAL"));
          final boolean         auxiliaryAttr  = "true".equals(DirectoryEntry.value(attrs, "AUXILIARY"));
          structural = (structuralAttr) || ((!abstractAttr) && (!auxiliaryAttr));

          mandatory = CollectionUtility.caseInsensitiveSet();
          collectString(attrs, "MUST", mandatory);
          optional = CollectionUtility.caseInsensitiveSet();
          collectString(attrs, "MAY", optional);
          if (mandatory.remove(this.endpoint.objectClassName())) {
            optional.add(this.endpoint.objectClassName());
          }

          supClasses = CollectionUtility.caseInsensitiveSet();
          collectString(attrs, "SUP", supClasses);
          if ((structural) && (supClasses.isEmpty())) {
            supClasses.add("top");
          }

          names = CollectionUtility.caseInsensitiveSet();
          collectString(attrs, "NAME", names);
          for (String name : names) {
            if (structural) {
              this.structuralClasses.addAll(names);
            }
            this.mandatoryAttributes.put(name, mandatory);
            this.optionalAttributes.put(name, optional);
            this.effectiveClasses.put(name, supClasses);
          }
        }
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   attributes
    /**
     ** Collecting the meta-data for attributes from the connected Directory
     ** Service.
     **
     ** @throws SystemException  if the operation fails for any reason.
     */
    private void attributes(final DirContext context)
      throws SystemException {

      try {
        final NamingEnumeration cursor = context.list(ATTRIBUTE);
        while (cursor.hasMore()) {
          final NameClassPair pair        = (NameClassPair)cursor.next();
          final String        id          = pair.getName();
          final String        path        = pair.isRelative() ? String.format("%s/%s", ATTRIBUTE, id) : id;
          BasicAttributes     attrs       = (BasicAttributes)context.getAttributes(path);
          boolean             singleValue = "true".equals(DirectoryEntry.value(attrs, "SINGLE-VALUE"));
          boolean             readonly    = "true".equals(DirectoryEntry.value(attrs, "NO-USER-MODIFICATION"));
          String              usage       = DirectoryEntry.value(attrs, "USAGE");
          boolean userApplications        = ("userApplications".equals(usage)) || (usage == null);

          Set<String> names = CollectionUtility.caseInsensitiveSet();
          collectString(attrs, "NAME", names);
          for (String nameX : names) {
            boolean objectClass = StringUtility.equal(nameX, this.endpoint.objectClassName());
            //          boolean binary      = conn.isBinarySyntax(attrName);
            //          final Class type = (binary) ? Byte.class : String.class;
            final Class type = (false) ? Byte.class : String.class;
            Set<AttributeInfo.Flags> flags = DirectoryAttribute.NULL;
            if (!singleValue) {
              flags.add(AttributeInfo.Flags.MULTIVALUED);
            }
            if ((readonly) || (objectClass)) {
              flags.add(AttributeInfo.Flags.NOT_CREATABLE);
              flags.add(AttributeInfo.Flags.NOT_UPDATEABLE);
            }
            if (!userApplications) {
              flags.add(AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT);
            }
            this.attributeProperties.put(id, DirectoryAttribute.build(type, flags));
          }
        }
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }

      for (String id : ATTRIBUTES)
        this.attributeProperties.put(id, DirectoryAttribute.build(EnumSet.of(AttributeInfo.Flags.NOT_CREATABLE, AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT)));
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
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Use the {@link DirectoryEndpoint} passed in to immediately connect to a
     ** Directory Service.
     **
     ** @param  context          the {@link DirectoryEndpoint} which is used
     **                          to discover this <code>DirectorySchema</code>.
     **                          <br>
     **                          Allowed object is {@link DirectoryEndpoint}.
     **
     ** @throws SystemException  if there is a problem creating a
     **                          {@link DirContext}.
     */
    public Static(final DirectoryEndpoint context)
      throws SystemException {

      // ensure inheritance
      super(context);

      // initialize instance
      types();
      mandatoryAttributes();
      optionalAttributes();
      attributeTypes();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   attributeTypes
    /**
     ** Collecting the meta-data for attribute types.
     */
    private void attributeTypes() {
      this.attributeProperties.put("objectClass",       DirectoryAttribute.build(DirectoryAttribute.REQUIRED_READONLY));
      this.attributeProperties.put("userPassword",      DirectoryAttribute.build(DirectoryAttribute.NRBD_NOTREADABLE));
      this.attributeProperties.put("creatorsName",      DirectoryAttribute.build(DirectoryAttribute.OPERATIONAL));
      this.attributeProperties.put("modifiersName",     DirectoryAttribute.build(DirectoryAttribute.OPERATIONAL));
      this.attributeProperties.put("uid",               DirectoryAttribute.build(DirectoryAttribute.REQUIRED));
      this.attributeProperties.put("sn",                DirectoryAttribute.build(DirectoryAttribute.REQUIRED));
      this.attributeProperties.put("cn",                DirectoryAttribute.build(DirectoryAttribute.REQUIRED));
      this.attributeProperties.put("telephoneNumber",   DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("title",             DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("street",            DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("registeredAddress", DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("postOfficeBox",     DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("postalCode",        DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("postalAddress",     DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("displayName",       DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("employeeNumber",    DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("givenName",         DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("homePhone",         DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("mail",              DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("uniqueMember",      DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("c",                 DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("l",                 DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("st",                DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("o",                 DirectoryAttribute.build(DirectoryAttribute.NULL));
      this.attributeProperties.put("ou",                DirectoryAttribute.build(DirectoryAttribute.NULL));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   types
    /**
     ** Collecting the meta-data for object classes.
     */
    private void types() {
      this.structuralClasses.add("person");
      this.structuralClasses.add("organizationalPerson");
      this.structuralClasses.add("inetOrgPerson");
      this.structuralClasses.add("groupOfNames");
      this.structuralClasses.add("groupOfUniqueNames");
      this.structuralClasses.add("organizationalUnit");
      this.structuralClasses.add("ldapSubentry");
      this.structuralClasses.add("nsRoleDefinition");
      this.structuralClasses.add("nsSimpleRoleDefinition");
      this.structuralClasses.add("nsManagedRoleDefinition");

      this.effectiveClasses.put("top",                     CollectionUtility.set("top"));
      this.effectiveClasses.put("person",                  CollectionUtility.set("top", "person"));
      this.effectiveClasses.put("organizationalPerson",    CollectionUtility.set("top", "person", "organizationalPerson"));
      this.effectiveClasses.put("inetOrgPerson",           CollectionUtility.set("top", "person", "organizationalPerson", "inetOrgPerson"));
      this.effectiveClasses.put("groupOfNames",            CollectionUtility.set("top", "groupOfNames"));
      this.effectiveClasses.put("groupOfUniqueNames",      CollectionUtility.set("top", "groupOfUniqueNames"));
      this.effectiveClasses.put("organizationalUnit",      CollectionUtility.set("top", "organizationalUnit"));
      this.effectiveClasses.put("ldapSubentry",            CollectionUtility.set("top", "ldapSubentry"));
      this.effectiveClasses.put("nsRoleDefinition",        CollectionUtility.set("top", "ldapSubentry", "nsRoleDefinition"));
      this.effectiveClasses.put("nsSimpleRoleDefinition",  CollectionUtility.set("top", "ldapSubentry", "nsRoleDefinition", "nsSimpleRoleDefinition"));
      this.effectiveClasses.put("nsManagedRoleDefinition", CollectionUtility.set("top", "ldapSubentry", "nsRoleDefinition", "nsSimpleRoleDefinition", "nsManagedRoleDefinition"));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   mandatoryAttributes
    /**
     ** Collecting the meta-data for required attributes.
     */
    private void mandatoryAttributes() {
      this.mandatoryAttributes.put("top",                     CollectionUtility.set("objectClass"));
      this.mandatoryAttributes.put("person",                  CollectionUtility.set("cn","sn"));
      this.mandatoryAttributes.put("organizationalPerson",    CollectionUtility.set(new String[0]));
      this.mandatoryAttributes.put("inetOrgPerson",           CollectionUtility.set(new String[0]));
      this.mandatoryAttributes.put("groupOfNames",            CollectionUtility.set("cn"));
      this.mandatoryAttributes.put("groupOfUniqueNames",      CollectionUtility.set("cn"));
      this.mandatoryAttributes.put("organization",            CollectionUtility.set("o"));
      this.mandatoryAttributes.put("organizationalUnit",      CollectionUtility.set("ou"));
      this.mandatoryAttributes.put("ldapSubentry",            CollectionUtility.set(new String[0]));
      this.mandatoryAttributes.put("nsRoleDefinition",        CollectionUtility.set(new String[0]));
      this.mandatoryAttributes.put("nsSimpleRoleDefinition",  CollectionUtility.set(new String[0]));
      this.mandatoryAttributes.put("nsManagedRoleDefinition", CollectionUtility.set(new String[0]));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   optionalAttributes
    /**
     ** Collecting the meta-data for optional attributes.
     */
    private void optionalAttributes() {
      this.optionalAttributes.put("top",                     CollectionUtility.set("creatorsName", "createTimestamp", "modifiersName", "modifyTimestamp"));
      this.optionalAttributes.put("person",                  CollectionUtility.set("userPassword", "telephoneNumber", "description", "seeAlso"));
      this.optionalAttributes.put("organizationalPerson",    CollectionUtility.set("title", "registeredAddress", "street", "postOfficeBox", "postalCode", "postalAddress", "st", "l", "ou"));
      this.optionalAttributes.put("inetOrgPerson",           CollectionUtility.set("displayName", "employeeNumber", "givenName", "homePhone", "mail"));
      this.optionalAttributes.put("groupOfNames",            CollectionUtility.set("o", "ou", "owner", "description", "uniqueMember"));
      this.optionalAttributes.put("groupOfUniqueNames",      CollectionUtility.set("o", "ou", "owner", "description", "uniqueMember"));
      this.optionalAttributes.put("organization",            CollectionUtility.set("userPassword", "registeredAddress"));
      this.optionalAttributes.put("organizationalUnit",      CollectionUtility.set("userPassword", "registeredAddress"));
      this.optionalAttributes.put("ldapSubentry",            CollectionUtility.set(new String[0]));
      this.optionalAttributes.put("nsRoleDefinition",        CollectionUtility.set(new String[0]));
      this.optionalAttributes.put("nsSimpleRoleDefinition",  CollectionUtility.set(new String[0]));
      this.optionalAttributes.put("nsManagedRoleDefinition", CollectionUtility.set(new String[0]));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the {@link DirectoryEndpoint} passed in to immediately connect to a
   ** Directory Service. If the {@link DirContext} fails a
   ** {@link DirectoryException} will be thrown.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to discover this
   **                            <code>DirectorySchema</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  protected DirectorySchema(final DirectoryEndpoint context)
    throws SystemException {

    // ensure inheritance
    super(context, ANY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   structuralObjectClasses
  /**
   ** Returns an unmodifiable {@link Set} of the structural object classes of
   ** the connected Directory Service.
   **
   ** @return                    an unmodifiable {@link Set} of the structural
   **                            object classes of the connected Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link Set} where each element
   **                            is a {@link String}.
   */
  public Set<String> structuralObjectClasses() {
    return CollectionUtility.unmodifiable(this.structuralClasses);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   effectiveObjectClasses
  /**
   ** Returns an unmodifiable {@link Set} of the object classe names for the
   ** specified derived object class where the specified object class
   ** <code>derived</code> is derived from.
   ** <p>
   ** The collection of the object classes is iterating across the entire
   ** hierarchy.
   **
   ** @param  derived            the name of the object class to lookup the
   **                            object classes where the specified object class
   **                            is derived from
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable {@link Set} of the object class
   **                            names for the specified derived object class.
   **                            <br>
   **                            Possible object is {@link Set} where each element
   **                            is a {@link String}.
   */
  public Set<String> effectiveObjectClasses(final String derived) {
    final Queue<String> queue = new LinkedList<>();
    queue.add(derived);

    final Set<String> result = CollectionUtility.caseInsensitiveSet();
    while (!queue.isEmpty()) {
      final String visit = queue.remove();
      if (!result.contains(visit)) {
        result.add(visit);
        final Set<String> effective = this.effectiveClasses.get(visit);
        if (effective != null) {
          queue.addAll(effective);
        }
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requiredAttributes
  /**
   ** Returns an unmodifiable {@link Set} of the required attributes for the
   ** specified object classes if there are any.
   **
   ** @param  type               the name of the object class to return the
   **                            required attributes from the discovered schema
   **                            of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable {@link Set} of the optional
   **                            attributes for the specified object class.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is a {@link String}.
   */
  public Set<String> requiredAttributes(final String type) {
    return filterAttributes(type, this.mandatoryAttributes.get(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionalAttributes
  /**
   ** Returns an unmodifiable {@link Set} of the optional attributes for the
   ** specified object classes if there are any.
   **
   ** @param  type               the name of the object class to return the
   **                            optional attributes from the discovered schema
   **                            of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable {@link Set} of the optional
   **                            attributes for the specified object class.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is a {@link String}.
   */
  public Set<String> optionalAttributes(final String type) {
    return filterAttributes(type, this.optionalAttributes.get(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the name of a non-transferable attribute by consulting the schema
   ** definition.
   **
   ** @param  type               the name of the object class to return the
   **                            optional attributes from the discovered schema
   **                            of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attributeName      the name of the attribute to lookup from the
   **                            schema.
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable {@link Set} of the optional
   **                            attributes for the specified object class.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is a {@link String}.
   */
  public String attribute(final ObjectClass type, final String attributeName) {
    return attribute(type, attributeName, false);
  }

  public Set<DirectoryEndpoint.Bind.Status> statusAttributes() {
    return this.endpoint.entryStatusAttributes();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the name of a non-transferable attribute by consulting the schema
   ** definition.
   **
   ** @param  type               the name of the object class to return the
   **                            optional attributes from the discovered schema
   **                            of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attributeName      the name of the attribute to lookup from the
   **                            schema.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  transfer           whether binary options are included or not
   **                            schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    an unmodifiable {@link Set} of the optional
   **                            attributes for the specified object class.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is a {@link String}.
   */
  public String attribute(final ObjectClass type, final String attributeName, boolean transfer) {
    String result = null;
    if (AttributeUtil.namesEqual(Uid.NAME, attributeName))
      result = this.endpoint.entryIdentifierAttribute();
    else if (AttributeUtil.namesEqual(Name.NAME, attributeName))
      result = this.endpoint.entryPrefix(type);
    else if (AttributeUtil.namesEqual(OperationalAttributes.ENABLE_NAME, attributeName)) {
      result = this.endpoint.entryStatusAttribute();
    }

    if ((result == null) && (!AttributeUtil.isSpecialName(attributeName))) {
      result = attributeName;
    }

    if ((result != null) && (transfer) && (BINARY.contains(result))) {
      result = appendBinaryOption(result);
    }

    if ((result == null) && (!type.equals(DirectorySchema.ANY))) {
      warning(String.format("Attribute %s of object class %s is not mapped to an LDAP attribute", attributeName, type));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the {@link Set} of identifying attributes for the specified
   ** {@link ObjectClass}.
   **
   ** @param  type               the object class to to lookup the identifying
   **                            attributes from the discovered schema of the
   **                            connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    the {@link Set} of identifying attributes for
   **                            the specified {@link ObjectClass}.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public Set<String> attributes(final ObjectClass type) {
    final Mapping mapping = mapping().get(type);
    return (mapping == null) ? Collections.emptySet() : mapping.prefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapping
  /**
   ** Returns the predefined object class mappin for embeded object classes.
   **
   ** @return                    the predefined object class mappin for embeded
   **                            object classes.
   **                            Possible object is {@link Map}.
   */
  public Map<ObjectClass, Mapping> mapping() {
    return CollectionUtility.map(
      new ObjectClass[]{ANY, ObjectClass.ACCOUNT, ObjectClass.GROUP, ROLE, ORGANIZATION, ORGANIZATIONAL_UNIT, DOMAIN, LOCALITY, COUNTRY}
    , new Mapping[]{ANY_SCHEMA, ACCOUNT_SCHEMA, GROUP_SCHEMA, ROLE_SCHEMA, ORGANIZATION_SCHEMA, ORGANIZATIONAL_UNIT_SCHEMA, DOMAIN_SCHEMA, LOCALITY_SCHEMA, COUNTRY_SCHEMA}
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Return the schema upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to discover the
   **                            {@link DirectorySchema}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link DirectorySchema}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public static DirectorySchema build(final DirectoryEndpoint context)
    throws SystemException {

    return context.schema();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Return the schema upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector
   **
   ** @param  clazz              the connector {@link Class} for which the
   **                            schema is built.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link AbstractConnector}.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link Schema}.
   **
   ** @throws DirectoryException if the schema operation fails.
   */
  public Schema schema(final Class<? extends AbstractConnector> clazz)
    throws DirectoryException {

    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    if (this.schema == null) {
      buildSchema(clazz);
    }
    trace(method, Loggable.METHOD_EXIT);
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectString
  /**
   ** Collects all occurences of a string value from the given collection of
   ** object attributes.
   **
   ** @param  attributes         the collection aof {@link BasicAttribute}s.
   **                            <br>
   **                            Allowed object is {@link BasicAttributes}.
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  collection         the {@link Set} where the attribute values
   **                            discovered are collected in.
   **                            <br>
   **                            Allowed object is {@link Set}.
   **
   ** @throws SystemException    if the operation fails
   */
  public static void collectString(final BasicAttributes attributes, final String attributeName, final Set<String> collection)
    throws SystemException {

    final BasicAttribute attribute = (BasicAttribute)attributes.get(attributeName);
    if (attribute == null)
      return;

    try {
      final NamingEnumeration cursor = attribute.getAll();
      while (cursor.hasMore()) {
        Object obj = cursor.next();
        if ((obj instanceof String)) {
          collection.add((String)obj);
        }
        else if ((obj instanceof BasicAttribute)) {
          BasicAttribute ba = (BasicAttribute)obj;
          final NamingEnumeration baEnum = ba.getAll();
          while (baEnum.hasMore())
            collection.add((String)baEnum.next());
        }
        else {
          collection.add(obj.toString());
        }
      }
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   appendBinaryOption
  /**
   ** Appends literal <code>;binary</code> to the specified attribute name.
   **
   ** @param  attributeName      the name of the attribute to append with
   **                            literal <code>;binary</code>.
   **
   ** @return                    the calculated attribute name,
   */
  public static String appendBinaryOption(final String attributeName) {
    return !binaryOption(attributeName) ? attributeName + ";binary" : attributeName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeBinaryOption
  /**
   ** Removes literal <code>;binary</code> from the specified attribute name.
   **
   ** @param  attributeName      the name of the attribute the literal
   **                           <code>;binary</code> needs to be removed.
   **
   ** @return                    the calculated attribute name,
   */
  public static String removeBinaryOption(String attributeName) {
    return binaryOption(attributeName) ? attributeName.substring(0, attributeName.length() - ";binary".length()) : attributeName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryOption
  /**
   ** Returns <code>true</code> if the specified attribute name refers to a
   ** attribute that is a binary option.
   **
   ** @param  attributeName      the name of the attribute to test.
   **
   ** @return                    <code>true</code> if the specified attribute
   **                            name is registhered in the set of binary
   **                            options; otherwise <code>false</code>,
   */
  public static boolean binaryOption(final String attributeName) {
    return attributeName.toLowerCase(Locale.US).endsWith(";binary");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binarySyntax
  /**
   ** Returns <code>true</code> if the specified attribute name refers to a
   ** attribute that is has a binary syntax.
   **
   ** @param  attributeName      the name of the attribute to test.
   **
   ** @return                    <code>true</code> if the specified attribute
   **                            name is registhered in the set of binary
   **                            syntaxes; otherwise <code>false</code>,
   */
  public static boolean binarySyntax(final String attributeName) {
    return SYNTAX.contains(attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeProperties
  /**
   ** Returns the {@link DirectoryAttribute} type properties from the discovered
   ** schema.
   **
   ** @param  attributeName      the name of the attribute to be returned
   **                            {@link DirectoryAttribute} type properties from
   **                            the discovered schema of the connected
   **                            Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link DirectoryAttribute} type properties
   **                            from the discovered schema.
   **                            <br>
   **                            Possible object is
   **                            {@link DirectoryAttribute.Type}.
   */
  public DirectoryAttribute.Type attributeProperties(final String attributeName) {
    return this.attributeProperties.get(attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildSchema
  /**
   ** Builds schema meta-data from configuration and by obtaining meta-data from
   ** target environment. Can't override this method because static, so this
   ** requires a new class.
   **
   ** @param  clazz              the connector for which the schema are built.
   **                            <br>
   **                            Allowed object is {@link AbstractConnector}.
   */
  private void buildSchema(final Class<? extends AbstractConnector> clazz)
    throws DirectoryException {

    final String method = "buildSchema";
    trace(method, Loggable.METHOD_ENTRY);

    final SchemaBuilder builder = new SchemaBuilder(clazz);
    for (DirectorySchema.Mapping cursor : mapping().values()) {
      final ObjectClass            type         = cursor.type();
      final ObjectClassInfoBuilder classBuilder = new ObjectClassInfoBuilder();
      classBuilder.setType(type.getObjectClassValue());
      classBuilder.setContainer(cursor.container());
      classBuilder.addAllAttributeInfo(buildAttribute(cursor.classes()));
      classBuilder.addAllAttributeInfo(cursor.operational());
      builder.defineObjectClass(classBuilder.build());
    }

    for (String cursor : this.structuralClasses) {
      final ObjectClassInfoBuilder classBuilder = new ObjectClassInfoBuilder().setType(cursor);
      classBuilder.setContainer(true);
      classBuilder.addAllAttributeInfo(buildAttribute(effectiveObjectClasses(cursor)));
      builder.defineObjectClass(classBuilder.build());
    }

    this.schema = builder.build();
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Builds schema meta-data from configuration and by obtaining meta-data from
   ** target environment.
   **
   ** @param  type               the name of the object class to collect the
   **                            attributes for.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   */
  private Set<AttributeInfo> buildAttribute(final Collection<String> type) {
    Set<String> required = requiredAttributes(type);
    Set<String> optional = optionalAttributes(type);

    optional.removeAll(required);

    final Set<AttributeInfo> collector = new HashSet<>();
    collectAttribute(type, required, EnumSet.of(AttributeInfo.Flags.REQUIRED), null, collector);
    collectAttribute(type, optional, null, null, collector);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requiredAttributes
  /**
   ** Collects schema meta-data from configuration and by obtaining meta-data
   ** from target environment.
   **
   ** @param  type               the name of the object class to collect the
   **                            required attributes for.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the collection of attribute names that are
   **                            required for the specified
   **                            <code>type</code>.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  private Set<String> requiredAttributes(final Collection<String> objectClass) {
    final Set<String> result = CollectionUtility.caseInsensitiveSet();
    for (String cursor : objectClass) {
      result.addAll(requiredAttributes(cursor));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionalAttributes
  /**
   ** Collects schema meta-data from configuration and by obtaining meta-data
   ** from target environment.
   **
   ** @param  type               the name of the object class to collect
   **                            optional attributes for.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the collection of attribute names that are
   **                            optional for the specified
   **                            <code>objectClass</code>.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  private Set<String> optionalAttributes(final Collection<String> type) {
    final Set<String> result = CollectionUtility.caseInsensitiveSet();
    for (String cursor : type) {
      result.addAll(optionalAttributes(cursor));
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAttribute
  /**
   ** Collects an <code>AttributeInfo</code> from this directory attribute.
   **
   ** @param  type               the identifier of the attribute.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   ** @param  attrs              the names of the attributes to collect.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  add                the flags to add to the created instance.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link AttributeInfo.Flags}.
   ** @param  remove             the flags to remove from the created instance.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link AttributeInfo.Flags}.
   ** @param  collector          the collector instance.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link AttributeInfo}.
   */
   private void collectAttribute(final Collection<String> type, final Set<String> attrs, final Set<AttributeInfo.Flags> add, final Set<AttributeInfo.Flags> remove, final Set<AttributeInfo> collector) {
    for (String attr : attrs)
      collectAttribute(type, attr, attr, add, remove, collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAttribute
  /**
   ** Collects an <code>AttributeInfo</code>s for the specified object class
   ** by adding the attribute flags <code>add</code> and removing the flags
   ** <code>remove</code> from the {@link DirectoryAttribute} lookuped by its
   ** name.
   **
   ** @param  type               the identifier of the attribute.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   ** @param  physical           the identifier of the attribute in the schema
   **                            of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  logical            the identifier of the attribute in the connector
   **                            framework.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  add                the flags to add to the created instance.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link AttributeInfo.Flags}.
   ** @param  remove             the flags to remove from the created instance.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link AttributeInfo.Flags}.
   ** @param  collector          the collector instance.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link AttributeInfo}.
   */
  private void collectAttribute(final Collection<String> objectClass, final String physical, final String logical, final Set<AttributeInfo.Flags> add, final Set<AttributeInfo.Flags> remove, final Set<AttributeInfo> collector) {
    final DirectoryAttribute.Type attribute = attributeProperties(physical);
    if (attribute != null)
      collector.add(attribute.build(logical, add, remove));
    else
      warning(String.format("Could not find attribute %2$s in object classes %1$s", objectClass, physical));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterAttributes
  /**
   ** Returns an unmodifiable {@link Set} of the mandatory or optional
   ** attributes for the specified object classes if there are any.
   ** <br>
   ** The discriminator which kind of attributes are returned is the state of
   ** <code>required</code>.
   **
   ** @param  type               the name of the object class to return the
   **                            optional attributes from the discovered schema
   **                            of the connected Directory Service.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an unmodifiable {@link Set} of the optional
   **                            attributes for the specified object class.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is a {@link String}.
   */
  private Set<String> filterAttributes(final String type, final Set<String> attributes) {
    final Set<String>   result  = CollectionUtility.caseInsensitiveSet();
    final Set<String>   visited = new HashSet<>();

    final Queue<String> queue   = new LinkedList<>();
    queue.add(type);
    while (!queue.isEmpty()) {
      final String current = queue.remove();
      if (!visited.contains(current)) {
        visited.add(current);
        if (attributes != null) {
          result.addAll(attributes);
        }
        Set<String> effective = effectiveClasses.get(current);
        if (effective != null) {
          queue.addAll(effective);
        }
      }
    }
    return result;
  }
}