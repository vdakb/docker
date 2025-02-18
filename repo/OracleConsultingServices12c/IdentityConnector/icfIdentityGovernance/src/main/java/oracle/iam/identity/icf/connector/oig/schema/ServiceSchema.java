/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Identity Governance Connector

    File        :   ServiceSchema.java
    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.oig.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Stack;
import java.util.Objects;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.Serializable;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;
import org.identityconnectors.framework.spi.operations.SPIOperation;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.spi.operations.ResolveUsernameOp;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;

import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.authopss.vo.AdminRoleMembership;

import oracle.iam.identity.vo.Identity;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.icf.foundation.SystemError;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.AbstractConnector;

////////////////////////////////////////////////////////////////////////////////
// class ServiceSchema
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** An interface to describe and transfer OIG Resource object to and from
 ** Identity Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceSchema {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The one and only instance of the <code>ServiceSchema</code>
   ** <p>
   ** Singleton Pattern
   */
  public static final ServiceSchema instance       = new ServiceSchema();

  public static final String        ROLE_ALLUSERS  = "ALL USERS";
  public static final String        ROLE_OPERATORS = "OPERATORS";
  public static final String        ROLE_CATEGORY  = "OIM Roles";


  /** The name of the user/role/organization identifier name attribute */
  public static final String        IDENTIFIER     = "id";

  /** The name of the user/organization status attribute */
  public static final String        STATUS         = "status";

  /** The name of the role/organization public unique name attribute */
  public static final String        NAME           = "name";

  /** The name of the organization type attribute */
  public static final String        TYPE           = "type";

  /** The name of the role internal unique name attribute */
  public static final String        UNIQUENAME     = "uniqueName";

  /** The name of the user/role/organization public unique name attribute */
  public static final String        LOGINNAME      = "loginName";

  /** The name of the user last name attribute */
  public static final String        LASTNAME       = "lastName";

  /** The name of the user last name attribute */
  public static final String        FIRSTNAME      = "firstName";

  /** The name of the user/role/organization display name attribute */
  public static final String        DISPLAYNAME    = "displayName";

  /** The name of the role/admin role description attribute */
  public static final String        DESCRIPTION    = "description";

  /** The name of the member attribute */
  public static final String        MEMBEROF       = "members";

  /**
   ** The default attributes returned for <code>Role</code>s.
   */
  public static final Set<String> role = CollectionUtility.set(
    RoleManagerConstants.ROLE_KEY
  , RoleManagerConstants.ROLE_NAME
  , RoleManagerConstants.ROLE_DISPLAY_NAME
  , RoleManagerConstants.ROLE_CREATE_DATE
  , RoleManagerConstants.ROLE_UPDATE_DATE
  );
  /**
   ** The default attributes returned for <code>User</code>s.
   */
  public static final Set<String> user = CollectionUtility.set(
    UserManagerConstants.AttributeName.USER_KEY.getId()
  , UserManagerConstants.AttributeName.USER_LOGIN.getId()
  , UserManagerConstants.AttributeName.STATUS.getId()
  , UserManagerConstants.AttributeName.LASTNAME.getId()
  , UserManagerConstants.AttributeName.FIRSTNAME.getId()
  , UserManagerConstants.AttributeName.DISPLAYNAME.getId()
  , UserManagerConstants.AttributeName.USER_CREATED.getId()
  , UserManagerConstants.AttributeName.USER_UPDATE.getId()
  );
  /**
   ** The default attributes returned for <code>Organization</code>s.
   */
  public static final Set<String> organization = CollectionUtility.set(
    OrganizationManagerConstants.AttributeName.ID_FIELD.getId()
  , OrganizationManagerConstants.AttributeName.ORG_TYPE.getId()
  , OrganizationManagerConstants.AttributeName.ORG_NAME.getId()
  , OrganizationManagerConstants.AttributeName.ORG_STATUS.getId()
  , OrganizationManagerConstants.ORG_CREATE_ON
  , OrganizationManagerConstants.ORG_UPDATE_ON
  );

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Map<ObjectClass, Entity>      structural   = CollectionUtility.map(
    ObjectClass.ACCOUNT, new Account()
  , ObjectClass.GROUP,   new SystemRole()
  , ServiceClass.GLOBAL, new GlobalRole()
  , ServiceClass.SCOPED, new ScopedRole()
  , ServiceClass.TENANT, new Tenant()
  );

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Entity
  // ~~~~~~~~ ~~~~~ ~~~~~~
  /**
   ** The descriptor to handle structural schema entities.
   */
  public static abstract class Entity {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String                             nativeName;
    private final ObjectClassInfo                    objectClass;
    private final Map<String, AttributeInfo>         metadata;
    private final Set<Class<? extends SPIOperation>> supported;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** @param  type               the object class instance.
     **                            <br>
     **                            Allowed object is {@link ObjectClass}.
     ** @param  nativeName         the of native name of the entity to identify
     **                            the entity by the Service Provider.
     **                            <br>
     **                            Allowed object is {@link Set} where each
     **                            element is of type {@link String}.
     ** @param  container          <code>true</code> to indicate this is a
     **                            container type; otherwise
     **                            <code>false</code>.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     ** @param  embedded           <code>true</code> to indicate this is a
     **                            embedded type; otherwise
     **                            <code>false</code>.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     ** @param  attribute          the collection of regular attribute
     **                            descriptors.
     **                            <br>
     **                            Allowed object is {@link Set} where each
     **                            element is of type {@link AttributeInfo}.
     ** @param  supported          the supported API operation.
     **                            <br>
     **                            Allowed object is {@link Set} where each
     **                            element is of type {@link Class} for type
     **                            {@link SPIOperation}.
     */
    protected Entity(final ObjectClass type, final String nativeName, final boolean container, final boolean embedded, final Set<Pair<AttributeInfo, String>> attribute, final Set<Class<? extends SPIOperation>> supported) {
      // ensure inheritance
      super();

      // initialize instance attributes


      this.nativeName  = nativeName;
      this.supported   = supported;
      this.metadata    = new LinkedHashMap<String, AttributeInfo>();
      final ObjectClassInfoBuilder factory = new ObjectClassInfoBuilder();
      factory.setType(type.getObjectClassValue());
      factory.setContainer(container);
      factory.setEmbedded(embedded);
      for (Pair<AttributeInfo, String> cursor : attribute) {
        this.metadata.put(cursor.value, cursor.tag);
        factory.addAttributeInfo(cursor.tag);
      }
      this.objectClass = factory.build();
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
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Entity that = (Entity)other;
    return super.equals(other)
        && Objects.equals(this.nativeName,  that.nativeName)
        && Objects.equals(this.objectClass, that.objectClass);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class GlobalRole
  // ~~~~~ ~~~~~~~~~~
  /**
   ** The descriptor to handle structural global admin role schema entities.
   */
  public static class GlobalRole extends Entity {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>GlobalRole</code> schema mapping.
     */
    @SuppressWarnings("unchecked")
    private GlobalRole() {
      // ensure inheritance
      super(ServiceClass.GLOBAL
      , "GlobalRole"
      , false
      , false
      , CollectionUtility.unmodifiableSet(
          Pair.of(AttributeInfoBuilder.build("id",          Long.class,   operatinal()), "Role Key")
        , Pair.of(AttributeInfoBuilder.build("name",        String.class, required()),   "Role Name")
        , Pair.of(AttributeInfoBuilder.build("displayName", String.class, none()),       "Role Display Name")
        , Pair.of(AttributeInfoBuilder.build("description", String.class, none()),       "Role Description")
        )
      , CollectionUtility.unmodifiableSet(
          SchemaOp.class
        , SearchOp.class
        , ResolveUsernameOp.class
        , UpdateAttributeValuesOp.class
        )
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ScopedRole
  // ~~~~~ ~~~~~~~~~~
  /**
   ** The descriptor to handle structural scoped admin role schema entities.
   */
  public static class ScopedRole extends Entity {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>GlobalRole</code> schema mapping.
     */
    @SuppressWarnings("unchecked")
    private ScopedRole() {
      // ensure inheritance
      super(ServiceClass.SCOPED
      , "ScopedRole"
      , false
      , false
      , CollectionUtility.unmodifiableSet(
          Pair.of(AttributeInfoBuilder.build("id",          Long.class,   operatinal()), "Role Key")
        , Pair.of(AttributeInfoBuilder.build("name",        String.class, required()),   "Role Name")
        , Pair.of(AttributeInfoBuilder.build("displayName", String.class, none()),       "Role Display Name")
        , Pair.of(AttributeInfoBuilder.build("description", String.class, none()),       "Role Description")
        )
      , CollectionUtility.unmodifiableSet(
          SchemaOp.class
        , SearchOp.class
        , ResolveUsernameOp.class
        , UpdateAttributeValuesOp.class
        )
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class SystemRole
  // ~~~~~ ~~~~~~~~~~
  /**
   ** The descriptor to handle structural role schema entities.
   */
  public static class SystemRole extends Entity {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Role</code> schema mapping.
     */
    @SuppressWarnings("unchecked")
    private SystemRole() {
      // ensure inheritance
      super(
        ObjectClass.GROUP
      , RoleManagerConstants.ROLE_ENTITY_NAME
      , false
      , false
      , CollectionUtility.unmodifiableSet(
          Pair.of(AttributeInfoBuilder.build("id",          Long.class,   operatinal()), RoleManagerConstants.RoleAttributeName.KEY.getId())
        , Pair.of(AttributeInfoBuilder.build("name",        String.class, required()),   RoleManagerConstants.RoleAttributeName.NAME.getId())
        , Pair.of(AttributeInfoBuilder.build("namespace",   String.class, none()),       RoleManagerConstants.RoleAttributeName.NAMESPACE.getId())
        , Pair.of(AttributeInfoBuilder.build("uniqueName",  String.class, readonly()),   RoleManagerConstants.RoleAttributeName.UNIQUE_NAME.getId())
        , Pair.of(AttributeInfoBuilder.build("displayName", String.class, none()),       RoleManagerConstants.RoleAttributeName.DISPLAY_NAME.getId())
        , Pair.of(AttributeInfoBuilder.build("description", String.class, none()),       RoleManagerConstants.RoleAttributeName.DESCRIPTION.getId())
        , Pair.of(AttributeInfoBuilder.build("createdOn",   Long.class,   operatinal()), RoleManagerConstants.RoleAttributeName.CREATE_DATE.getId())
        , Pair.of(AttributeInfoBuilder.build("updatedOn",   Long.class,   operatinal()), RoleManagerConstants.RoleAttributeName.UPDATE_DATE.getId())
        )
      , CollectionUtility.unmodifiableSet(
          SchemaOp.class
        , SearchOp.class
        , ResolveUsernameOp.class
        , UpdateAttributeValuesOp.class
        )
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Tenant
  // ~~~~~ ~~~~~~
  /**
   ** The descriptor to handle structural organization schema entities.
   */
  public static class Tenant extends Entity {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Tenant</code> schema mapping.
     */
    @SuppressWarnings("unchecked")
    private Tenant() {
      // ensure inheritance
      super(ServiceClass.TENANT
      , OrganizationManagerConstants.ORGANIZATION_ENTITY
      , false
      , false
      , CollectionUtility.unmodifiableSet(
          Pair.of(AttributeInfoBuilder.build("id",        Long.class,   operatinal()), OrganizationManagerConstants.AttributeName.ID_FIELD.getId())
        , Pair.of(AttributeInfoBuilder.build("name",      String.class, required()),   OrganizationManagerConstants.AttributeName.ORG_NAME.getId())
        , Pair.of(AttributeInfoBuilder.build("type",      String.class, none()),       OrganizationManagerConstants.AttributeName.ORG_TYPE.getId())
        , Pair.of(AttributeInfoBuilder.build("status",    String.class, none()),       OrganizationManagerConstants.AttributeName.ORG_STATUS.getId())
        , Pair.of(AttributeInfoBuilder.build("createdOn", Long.class,   operatinal()), OrganizationManagerConstants.ORG_CREATE_ON)
        , Pair.of(AttributeInfoBuilder.build("updatedOn", Long.class,   operatinal()), OrganizationManagerConstants.ORG_UPDATE_ON)
        )
      , CollectionUtility.unmodifiableSet(
          SchemaOp.class
        , SearchOp.class
        , CreateOp.class
        , UpdateOp.class
        , ResolveUsernameOp.class
        )
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Account
  // ~~~~~ ~~~~~~~
  /**
   ** Mapping descriptor for {@link ObjectClass} account.
   */
  public static final class Account extends Entity {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Account</code> schema mapping.
     */
    @SuppressWarnings("unchecked")
    private Account() {
      // ensure inheritance
      super(
        ObjectClass.ACCOUNT
      , UserManagerConstants.USER_ENTITY
      , false
      , false
      , CollectionUtility.unmodifiableSet(
          Pair.of(AttributeInfoBuilder.build("id",          Long.class,   operatinal()), UserManagerConstants.AttributeName.USER_KEY.getId())
        , Pair.of(AttributeInfoBuilder.build("status",      String.class, none()),       UserManagerConstants.AttributeName.STATUS.getId())
        , Pair.of(AttributeInfoBuilder.build("loginName",   String.class, required()),   UserManagerConstants.AttributeName.USER_LOGIN.getId())
        , Pair.of(AttributeInfoBuilder.build("lastName",    String.class, required()),   UserManagerConstants.AttributeName.LASTNAME.getId())
        , Pair.of(AttributeInfoBuilder.build("firstName",   String.class, none()),       UserManagerConstants.AttributeName.FIRSTNAME.getId())
        , Pair.of(AttributeInfoBuilder.build("displayName", String.class, none()),       UserManagerConstants.AttributeName.DISPLAYNAME.getId())
        , Pair.of(AttributeInfoBuilder.build("createdOn",   Long.class,   operatinal()), UserManagerConstants.AttributeName.USER_CREATED.getId())
        , Pair.of(AttributeInfoBuilder.build("updatedOn",   Long.class,   operatinal()), UserManagerConstants.AttributeName.USER_UPDATE.getId())
        )
      , CollectionUtility.unmodifiableSet(
          TestOp.class
        , SchemaOp.class
        , SearchOp.class
        , CreateOp.class
        , ResolveUsernameOp.class
        )
      );
    }
/*
    // if this is a multivalued attribute the real attribute class is the
    // one specified in the annotation, not the list, set, array, etc.
    type = property.multiValueClass();
    final String embedName = String.format("%s.%s", className, name);
    if (!SIMPLE.contains(type)) {
      embeddedObjectClass(schema, visited, embedName, type);
    }
*/

  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>ServiceSchema</code> Identity Governance
   ** connector that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ServiceSchema()" and enforces use of the public method below.
   */
  private ServiceSchema() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   structural
  /**
   ** Returns an unmodifiable {@link Set} of the structural object classes of
   ** the connected Directory Service.
   **
   ** @return                    an unmodifiable {@link Map} of the structural
   **                            object classes of the connected Identity
   **                            Governanace Service.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link ObjectClass} as the
   **                            key and {@link Entity} for the value.
   */
  public final Map<ObjectClass, Entity> structural() {
    return CollectionUtility.unmodifiable(this.structural);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Describes the types of objects this Connector supports upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector.
   ** <p>
   ** Builds schema meta-data from configuration without obtaining meta-data
   ** from target environment.
   ** <p>
   ** This method is considered an operation since determining supported objects
   ** may require configuration information and allows this determination to be
   ** dynamic.
   ** <p>
   ** The special [@link Uid} attribute should never appear in the schema, as it
   ** is not a real attribute of an object, rather a reference to it.
   ** <br>
   ** If the resource object-class has a writable unique id attribute that is
   ** different than its name, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @param  clazz              the connector {@link Class} for which the
   **                            schema is built.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link AbstractConnector}.
   **
   ** @return                    the {@link Schema}.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  public final Schema schema(final Class<? extends AbstractConnector> clazz) {
    final SchemaBuilder builder = new SchemaBuilder(clazz);
    for (Entity cursor : this.structural.values()) {
      builder.defineObjectClass(cursor.objectClass);
    }
    builder.clearSupportedObjectClassesByOperation();
    for (Entity cursor : this.structural.values()) {
      for (Class<? extends SPIOperation> operation : cursor.supported)
        builder.addSupportedObjectClass(operation, cursor.objectClass);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Converts the specified OIG user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the OIG user resource to convert.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  system             the collection of system roles granted to the
   **                            OIG user resource to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link AdminRole}.
   ** @param  global             the collection of global admin roles granted to
   **                            the OIG user resource to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link AdminRoleMembership}.
   ** @param  scoped             the collection of scoped admin roles granted to
   **                            the OIG user resource to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link AdminRoleMembership}.
   **
   ** @return                    the {@link ConnectorObject} to collected with
   **                            the attributes.
   **                            <br>
   **                            Allowed object is {@link ConnectorObject}.
   */
  public static ConnectorObject transfer(final User resource, final List<Role> system, final List<AdminRoleMembership> global, final List<AdminRoleMembership> scoped) {
    final ConnectorObjectBuilder collector = new ConnectorObjectBuilder();
    collector.setObjectClass(ObjectClass.ACCOUNT);
    collector.addAttribute(
      buildAttribute(Uid.NAME,    Long.valueOf(resource.getEntityId()))
    , buildAttribute(Name.NAME,   resource.getLogin())
    , buildAttribute(STATUS,      resource.getStatus())
    , buildAttribute(LASTNAME,    resource.getLastName())
    , buildAttribute(FIRSTNAME,   resource.getFirstName())
    , buildAttribute(DISPLAYNAME, resource.getDisplayName())
    );
    if (!CollectionUtility.empty(system)) {
      // add the collection of system roles to the connector object builder
      collectSystemRole(collector, system);
    }
    if (!CollectionUtility.empty(global)) {
      // add the collection of global admin roles to the connector object
      // builder
      collectGlobalRole(collector, global);
    }
    if (!CollectionUtility.empty(scoped)) {
      // add the collection of scoped admin roles to the connector object
      // builder
      collectScopedRole(collector, scoped);
    }
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Converts the specified OIG role resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the OIG role resource to convert.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the {@link ConnectorObject} to collected with
   **                            the attributes.
   **                            <br>
   **                            Allowed object is {@link ConnectorObject}.
   */
  public static ConnectorObject transfer(final Role resource) {
    final ConnectorObjectBuilder collector = new ConnectorObjectBuilder();
    collector.setObjectClass(ObjectClass.GROUP);
    collector.addAttribute(
      buildAttribute(Uid.NAME,    Long.valueOf(resource.getEntityId()))
    , buildAttribute(Name.NAME,   resource.getName())
    , buildAttribute(DISPLAYNAME, resource.getDisplayName())
    );
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Converts the specified OIG organization resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the OIG organization resource to convert.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the {@link ConnectorObject} to collected with
   **                            the attributes.
   **                            <br>
   **                            Allowed object is {@link ConnectorObject}.
   */
  public static ConnectorObject transfer(final Organization resource) {
    final ConnectorObjectBuilder collector = new ConnectorObjectBuilder();
    collector.setObjectClass(ServiceClass.TENANT);
    collector.addAttribute(
      buildAttribute(Uid.NAME,    Long.valueOf(resource.getEntityId()))
    , buildAttribute(Name.NAME,   resource.getAttribute(OrganizationManagerConstants.AttributeName.ORG_NAME.getId()))
    , buildAttribute(STATUS,      resource.getAttribute(OrganizationManagerConstants.AttributeName.ORG_STATUS.getId()))
    );
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Converts the specified OIG admin role resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the OIG admin role resource to convert.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  global             <code>true</code> if global permissions has to
   **                            be transfered; <code>false</code> if scoped
   **                            permissions has to be transfered.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link ConnectorObject} to collected with
   **                            the attributes.
   **                            <br>
   **                            Allowed object is {@link ConnectorObject}.
   */
  public static ConnectorObject transfer(final AdminRole resource, final boolean global) {
    final ConnectorObjectBuilder collector = new ConnectorObjectBuilder();
    collector.setObjectClass(global ? ServiceClass.GLOBAL : ServiceClass.SCOPED);
    collector.addAttribute(
      buildAttribute(Uid.NAME,    resource.getRoleId())
    , buildAttribute(Name.NAME,   resource.getRoleName())
    , buildAttribute(DISPLAYNAME, resource.getRoleDisplayName())
    );
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Factory method to create a new {@link AttributeBuilder}.
   **
   ** @param  name               the name of the attribute that is being built.
   **                            <br>
   **                            Must not be <code>null</code> or empty.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value object that is being built.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the {@link Attribute} equiped with the
   **                            value pair.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  public static Attribute buildAttribute(final String name, final Object value) {
    // name must not be blank
    final AttributeBuilder builder = new AttributeBuilder().setName(name);
    if (value != null) {
      if (value instanceof List<?>) {
        builder.addValue(new ArrayList<>((List<?>)value));
      }
      else {
        if (Uid.NAME.equals(name) || Name.NAME.equals(name))
          builder.addValue(String.valueOf(value));
        else
          builder.addValue(value);
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Converts a single value
   **
   ** @param  <T>                the type of the attribute value.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  name               a formal information about the value to
   **                            convert. May occure in the log or exceptions to
   **                            narrow down why the convertion might failed.
   **                            <br>
   **                            Allowed object is {@link Serializable}.
   ** @param  provider           the {@link Identity} providing values.
   **                            <br>
   **                            Allowed object is {@link Identity}.
   ** @param  expected           the expected type of the value.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   **
   ** @return                    the attribute value as of type
   **                            <code>&lt;T&gt;</code>.
   **                            <br>
   **                            Possible object is <code>&lt;T&gt;</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T extends Serializable> T convert(final String name, final Identity provider, final Class<? super T> expected) {
    final Object value =  provider.getAttribute(name);
    if (!expected.isAssignableFrom(value.getClass()))
			AbstractConnector.propagate(SystemError.SCHEMA_ATTRIBUTE_TYPE, name, expected, value.getClass());

    return (T)expected.cast(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectSystemRole
  /**
   ** Converts the specified OIG resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the OIG resource identifiers providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   */
  private static void collectSystemRole(final ConnectorObjectBuilder collector, final List<Role> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (Role i : resource) {
        permission.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, i.getEntityId()).build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(ObjectClass.GROUP_NAME, permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectGlobalRole
  /**
   ** Converts the specified OIG global admin role to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the OIG global admin roles providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link AdminRoleMembership}.
   */
  private static void collectGlobalRole(final ConnectorObjectBuilder collector, final List<AdminRoleMembership> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (AdminRoleMembership cursor : resource) {
        permission.add(new EmbeddedObjectBuilder().setObjectClass(ServiceClass.GLOBAL).addAttribute(Uid.NAME, String.valueOf(cursor.getAdminRoleId())).build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(ServiceClass.GLOBAL_NAME, permission);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectScopedRole
  /**
   ** Converts the specified OIG global admin role to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the OIG global admin roles providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link AdminRoleMembership}.
   */
  private static void collectScopedRole(final ConnectorObjectBuilder collector, final List<AdminRoleMembership> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (AdminRoleMembership cursor : resource) {
        permission.add(
          new EmbeddedObjectBuilder()
            .setObjectClass(ServiceClass.SCOPED)
            .addAttribute(Uid.NAME,    String.valueOf(cursor.getAdminRoleId()))
            .addAttribute("Scope",     cursor.getScopeId())
            .addAttribute("Hierarchy", cursor.isHierarchicalScope())
          .build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(ServiceClass.SCOPED_NAME, permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   embeddedObjectClass
  /**
   ** Returns embedded ICF schema attributes for a class.
   **
   ** @param  schema             the {@link SchemaBuilder} populating the entire
   **                            schema to build for a class.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  builder            the {@link ObjectClassInfoBuilder} populating
   **                            the attribute schema to build for a class.
   **                            <br>
   **                            Allowed object is {@link ObjectClassInfoBuilder}.
   ** @param  visited            a {@link Stack} containing the classes visited
   **                            prior to this class.
   **                            <br>
   **                            Allowed object is {@link Stack} where each
   **                            element is of type {@link String}.
   **                            This is used for cycle detection.
   ** @param  complex            <code>true</code> if the attribute to built is
   **                            an complex attribute.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private static void embeddedObjectClass(final SchemaBuilder schema, final String className) {
    final ObjectClassInfoBuilder builder = classBuilder(className);
    builder.setEmbedded(true);
    schema.defineObjectClass(builder.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classBuilder
  /**
   ** Returns the schema builder for a class.
   **
   ** @param  className          the object class name.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the object class builder.
   **                            <br>
   **                            Possible object is
   **                            {@link ObjectClassInfoBuilder}.
   */
  private static ObjectClassInfoBuilder classBuilder(final String className) {
    final ObjectClassInfoBuilder builder = new ObjectClassInfoBuilder();
    builder.setType(className);
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   none
  /**
   ** Factory method to create the attribute flags of an attribute that has no
   ** constraint.
   **
   ** @return                    the attribute flags of an attribute that has no
   **                            constraint.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link AttributeInfo.Flags}.
   */
  private static Set<AttributeInfo.Flags> none() {
    return EnumSet.noneOf(AttributeInfo.Flags.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Factory method to create the attribute flags of an required attribute.
   **
   ** @return                    the attribute flags of an required attribute.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link AttributeInfo.Flags}.
   */
  private static Set<AttributeInfo.Flags> required() {
    return EnumSet.of(AttributeInfo.Flags.REQUIRED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readonly
  /**
   ** Factory method to create the attribute flags of an attribute that is not
   ** updatable.
   **
   ** @return                    the attribute flags of an attribute that is not
   **                            updatable.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link AttributeInfo.Flags}.
   */
  private static Set<AttributeInfo.Flags> readonly() {
    return EnumSet.of(AttributeInfo.Flags.REQUIRED, AttributeInfo.Flags.NOT_UPDATEABLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operatinal
  /**
   ** Factory method to create the attribute flags of an operational attribute
   ** that are neither creatable or updatabale and not returned by default.
   */
  private static Set<AttributeInfo.Flags> operatinal() {
    return EnumSet.of(AttributeInfo.Flags.NOT_CREATABLE, AttributeInfo.Flags.NOT_UPDATEABLE);
  }
}