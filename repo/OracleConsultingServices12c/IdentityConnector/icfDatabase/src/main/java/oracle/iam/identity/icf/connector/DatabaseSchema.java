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
    Subsystem   :   Generic Database Connector

    File        :   DatabaseSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;
import java.util.Date;
import java.util.EnumSet;
import java.util.Collection;
import java.util.LinkedHashSet;

import java.sql.Connection;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import org.identityconnectors.framework.spi.Connector;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;
import oracle.iam.identity.icf.dbms.DatabaseException;

////////////////////////////////////////////////////////////////////////////////
// abstract class DatabaseSchema
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
public abstract class DatabaseSchema extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static final AttributeInfo      PASSWORD   = AttributeInfoBuilder.build(OperationalAttributes.PASSWORD_NAME, GuardedString.class, EnumSet.of(AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT, AttributeInfo.Flags.NOT_READABLE));

  static final Mapping            CATALOG    = new Mapping(Entity.Catalog.clazz);
  static final Mapping            ACCOUNT    = new Mapping(Entity.Account.clazz, PASSWORD);
  static final Mapping            ROLE       = new Mapping(Catalog.Role.clazz);
  static final Mapping            PROFILE    = new Mapping(Catalog.Profile.clazz);
  static final Mapping            PRIVILEGE  = new Mapping(Catalog.Privilege.clazz);
  static final Mapping            SCHEMA     = new Mapping(Catalog.Schema.clazz);
  static final Mapping            SYNONYM    = new Mapping(Catalog.Synonym.clazz);
  static final Mapping            SEQUENCE   = new Mapping(Catalog.Sequence.clazz);
  static final Mapping            TABLE      = new Mapping(Catalog.Table.clazz);
  static final Mapping            VIEW       = new Mapping(Catalog.View.clazz);
  static final Mapping            TYPE       = new Mapping(Catalog.Type.clazz);
  static final Mapping            FUNCTION   = new Mapping(Catalog.Function.clazz);
  static final Mapping            PROCEDURE  = new Mapping(Catalog.Privilege.clazz);
  static final Mapping            PACKAGE    = new Mapping(Catalog.Package.clazz);
  static final Mapping            JAVA       = new Mapping(Catalog.Java.clazz);
  static final Mapping            DOTNET     = new Mapping(Catalog.DOTNET.clazz);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Schema                schema     = null;
  protected final DatabaseContext context;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entity
  // ~~~~~ ~~~~~~
  /**
   ** The object class type descriptor.
   */
  public static enum Entity {
      /**
       ** a {@link Schema} <code>Type</code> definition provides information
       ** about catalogs in a Database Service.
       */
      Catalog("CATALOG")
      /**
       ** a {@link Schema} <code>Type</code> definition provides information
       ** about accounts in a Database Service.
       */
    , Account("ACCOUNT")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the identifier for this <code>Type</code>. */
    private final String      id;

    /** the programmatic name for this <code>Type</code>. */
    private final String      name;

   /** the object class for this <code>Type</code>. */
    private final ObjectClass clazz;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Entity</code> with the specified identifier.
     **
     ** @param  id               the identifierfor this <code>Type</code>.
     **                          Allowed object is {@link String}.
     */
    Entity(final String id) {
      this.id    = id;
      this.name  = SchemaUtility.createSpecialName(id);
      this.clazz = new ObjectClass(this.name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the unique identifier of a type.
     **
     ** @return                    the unique identifier of a type.
     **                            Possible object {@link String}.
     */
    public String id() {
      return this.id;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Catalog
  // ~~~~~ ~~~~~~~
  /**
   ** The object class type descriptor.
   */
  public static enum Catalog {
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** roles in a Database Service.
     */
    Role("ROLE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** profiles in a Database Service.
     */
    Profile("PROFILE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** privileges in a Database Service.
     */
    Privilege("PRIVILEGE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** schemes in a Database Service.
     */
    Schema("SCHEMA"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** synonyms in a Database Service.
     */
    Synonym("SYNONYM"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** sequences in a Database Service.
     */
    Sequence("SEQUENCE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** tables in a Database Service.
     */
    Table("TABLE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** views in a Database Service.
     */
    View("VIEW"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** user defined types in a Database Service.
     */
    Type("TYPE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** stored functions in a Database Service.
     */
    Function("FUNCTION"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** stored procedures in a Database Service.
     */
    Procedure("PROCEDURE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** stored packages in a Database Service.
     */
    Package("PACKAGE"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** stored Java Classes in a Database Service.
     */
    Java("JAVA"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** stored .NET functions in a Database Service.
     */
    DOTNET("DOTNET"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** permanent tablespaces in a Database Service.
     */
    TablespacePermanent("PERMANENT"),
    /**
     ** a {@link Schema} <code>Type</code> definition provides information about
     ** temporary tablespaces in a Database Service.
     */
    TablespaceTemporary("TEMPORARY"),
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the identifier for this <code>Type</code>. */
    private final String      id;

    /** the programmatic name for this <code>Type</code>. */
    private final String      name;

   /** the object class for this <code>Type</code>. */
    private final ObjectClass clazz;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Catalog</code> with the specified identifier.
     **
     ** @param  id               the identifierfor this <code>Type</code>.
     **                          Allowed object is {@link String}.
     */
    Catalog(final String id) {
      this.id    = id;
      this.name  = SchemaUtility.createSpecialName(id);
      this.clazz = new ObjectClass(this.name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the unique identifier of a type.
     **
     ** @return                    the unique identifier of a type.
     **                            Possible object {@link String}.
     */
    public String id() {
      return this.id;
    }
  }

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

    private final ObjectClass        objectClass;
    private final Set<AttributeInfo> operational;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** @param  objectClass        the object class instance.
     **                            Allowed object is {@link ObjectClass}.
     ** @param  operational        the collection of names of operational
     **                            attributes.
     **                            Allowed object is {@link Set} where each
     **                            element is of type {@link AttributeInfo}.
     */
    public Mapping(final ObjectClass objectClass, final AttributeInfo... operational) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.objectClass = objectClass;
      this.operational = CollectionUtility.unmodifiableSet(operational);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   objectClass
    /**
     ** Returns the object class instance.
     **
     ** @return                    the object class instance.
     **                            Possible object {@link ObjectClass}.
     */
    public ObjectClass objectClass() {
      return this.objectClass;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   operational
    /**
     ** Returns the collection of names of operational attributes.
     **
     ** @return                    the collection of names of operational
     **                            attributes.
     **                            Possible object {@link Set} where each
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
     */
    @Override
    public boolean equals(final Object other) {
      if ((other instanceof Mapping)) {
        final Mapping that = (Mapping)other;
        if (!objectClass.equals(that.objectClass)) {
          return false;
        }
        else if (!this.operational.equals(that.operational)) {
          return false;
        }
        else {
          return true;
        }
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Schema discovery is performed by query the Database Server.
   */
  public static final class Server extends DatabaseSchema {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Use the {@link DatabaseContext} passed in to immediately connect to a
     ** Database Service.
     ** <br>
     ** If the {@link Connection} fails a {@link DatabaseContext} will be
     ** thrown.
     **
     ** @param  context          the Database Service endpoint connection which
     **                          is used to discover this
     **                          {@link DatabaseSchema}.
     **                          Allowed object is {@link DatabaseContext}.
     */
    public Server(final DatabaseContext context) {
      // ensure inheritance
      super(context);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstrcat base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: buildSchema (DatabaseSchema)
    /**
     ** Builds schema meta-data from configuration and by obtaining meta-data
     ** from target environment. Can't override this method because static, so
     ** this requires a new class.
     **
     ** @param  clazz            the connector for which the schema are built.
     **                          Allowed object is {@link Connector}.
     **
     ** @return                  the schema object based on the info provided.
     **                          Possible object {@link Schema}.
     **
     ** @throws SystemException  if the {@link Connection} could not be
     **                          established at the first time this method is
     **                          invoked.
     */
    @Override
    protected Schema buildSchema(final Class<? extends Connector> clazz)
      throws SystemException {

      final SchemaBuilder builder = new SchemaBuilder(clazz);
      this.context.connect();
      try {
        ;
      }
      finally {
        this.context.disconnect();
      }
      return builder.build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Static
  // ~~~~~ ~~~~~~~
  /**
   ** Schema is performed by filling up with static data.
   */
  public static final class Static extends DatabaseSchema {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Use the {@link DatabaseContext} passed in to immediately connect to a
     ** Database Service.
     **
     ** @param  context          the {@link DatabaseContext} which is used
     **                          to discover this {@link DatabaseSchema}.
     **                          Allowed object is {@link DatabaseContext}.
     */
    public Static(final DatabaseContext context) {
      // ensure inheritance
      super(context);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstrcat base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: buildSchema (DatabaseSchema)
    /**
     ** Builds schema meta-data from configuration and by obtaining meta-data
     ** from target environment. Can't override this method because static, so
     ** this requires a new class.
     **
     ** @param  clazz            the connector for which the schema are built.
     **                          Allowed object is {@link Connector}.
     **
     ** @return                  the schema object based on the info provided.
     **                          Possible object {@link Schema}.
     **
     ** @throws SystemException  if the <code>DatabaseDialect</code> class is
     **                          not found on the classpath or could not be
     **                          either created or accessed for the
     **                          <code>driverClass</code>.
     */
    @Override
    protected Schema buildSchema(final Class<? extends Connector> clazz)
      throws SystemException {

      final SchemaBuilder builder = new SchemaBuilder(clazz);
      final ObjectClassInfoBuilder classBuilder = new ObjectClassInfoBuilder();
      classBuilder.setType(Entity.Account.clazz.getObjectClassValue());
      classBuilder.setContainer(true);
      classBuilder.addAllAttributeInfo(buildAttributes(Entity.Account));
      builder.defineObjectClass(classBuilder.build());
      return builder.build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the {@link DatabaseContext} passed in to immediately connect to a
   ** Database Service. If the {@link Connection} fails a
   ** {@link DatabaseException} will be thrown.
   **
   ** @param  context            the Database Service endpoint connection which
   **                            is used to discover this
   **                            {@link DatabaseSchema}.
   **                            Allowed object is {@link DatabaseContext}.
   */
  protected DatabaseSchema(final DatabaseContext context) {
    // ensure inheritance
    super(context);

    // initialize instance
    this.context = context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeStringCatalog
  /**
   ** Returns a string with all the possible types a catalog has.
   **
   ** @return                    a string with all the possible types a catalog
   **                            has.
   */
  public static final String typeStringCatalog() {
    return   Catalog.Privilege.id
    + '|'  + Catalog.Role.id
    + '|'  + Catalog.Profile.id
    + '|'  + Catalog.TablespacePermanent.id
    + '|'  + Catalog.TablespaceTemporary.id
    + '|'  + Catalog.Schema.id
    + '|'  + Catalog.Synonym.id
    + '|'  + Catalog.Sequence.id
    + '|'  + Catalog.Table.id
    + '|'  + Catalog.View.id
    + '|'  + Catalog.Type.id
    + '|'  + Catalog.Function.id
    + '|'  + Catalog.Procedure.id
    + '|'  + Catalog.Package.id
    + '|'  + Catalog.Java.id
    + '|'  + Catalog.DOTNET.id
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Returns the schema upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector
   **
   ** @param  clazz              the connector for which the schema are built.
   **                            <br>
   **                            Allowed object is {@link Connector}.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link Schema}.
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public Schema build(final Class<? extends Connector> clazz)
    throws SystemException {

    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    if (this.schema == null) {
      this.schema = buildSchema(clazz);
    }
    trace(method, Loggable.METHOD_EXIT);
    return this.schema;
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
   **                            Allowed object is {@link Connector}.
   **
   ** @return                    the schema object based on the info provided.
   **                            <br>
   **                            Possible object is {@link Schema}.
   **
   ** @throws SystemException   if the <code>DatabaseDialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the
   **                            <code>driverClass</code>.
   */
  protected abstract Schema buildSchema(final Class<? extends Connector> clazz)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttributes
  /**
   ** Collecting the meta-data for all attributes belonging to a specific
   ** {@link Entity}.
   **
   ** @param  entity             the <code>entity</code> the atributes are
   **                            requested for.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a collection of attribute instances with names
   **                            and values that configured for the database
   **                            dialect.
   **                            <br>
   **                            Possible object {@link Collection} where each
   **                            element is of type {@link AttributeInfo}.
   **
   ** @throws SystemException    if the <code>DatabaseDialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the
   **                            <code>driverClass</code>.
   */
  protected Collection<AttributeInfo> buildAttributes(final Entity entity)
    throws SystemException {

    final Collection<AttributeInfo> collector = new LinkedHashSet<>();
    final DatabaseEntity   object = this.context.dialectEntity(entity);
    for (DatabaseAttribute cursor : object.attribute()) {
      final Class<?> type = cursor.type().type();
      if (object.attribute().get(0).equals(cursor.id())) {
        collector.add(AttributeInfoBuilder.build(Name.NAME, type == Date.class ? Long.class : type, cursor.type().flags()));
      }
      else {
        collector.add(AttributeInfoBuilder.build(cursor.alias(), type == Date.class ? Long.class : type, cursor.type().flags()));
      }
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttributes
  /**
   ** Collecting the meta-data for all attributes belonging to a specific
   ** {@link Entity}.
   **
   ** @param  entity             the <code>entity</code> the atributes are
   **                            requested for.
   **                            <br>
   **                            Allowed object is {@link Catalog}.
   **
   ** @return                    a collection of attribute instances with names
   **                            and values that configured for the database
   **                            dialect.
   **                            <br>
   **                            Possible object {@link Collection} where each
   **                            element is of type {@link AttributeInfo}.
   **
   ** @throws SystemException    if the <code>DatabaseDialect</code> class is
   **                            not found on the classpath or could not be
   **                            either created or accessed for the
   **                            <code>driverClass</code>.
   */
  protected Collection<AttributeInfo> buildAttributes(final Catalog entity)
    throws SystemException {

    final Collection<AttributeInfo> collector = new LinkedHashSet<>();
    for (DatabaseAttribute cursor : this.context.dialectCatalog(entity).attribute()) {
      final Class<?> type = cursor.type().type();
      collector.add(AttributeInfoBuilder.build(cursor.alias(), type == Date.class ? Long.class : type, cursor.type().flags()));
    }
    return collector;
  }
}