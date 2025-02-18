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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Identity Governance Services Provisioning

    File        :   Context.java
    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.scim.igs;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import org.identityconnectors.framework.spi.operations.SPIOperation;

import oracle.iam.identity.icf.foundation.SystemSchema;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceConnector;

import oracle.iam.identity.icf.scim.Feature;

import oracle.iam.identity.icf.scim.request.Search;

import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.schema.User;
import oracle.iam.identity.icf.scim.schema.Group;
import oracle.iam.identity.icf.scim.schema.Resource;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.v2.schema.GroupResource;
import oracle.iam.identity.icf.scim.v2.schema.SchemaResource;
import oracle.iam.identity.icf.scim.v2.schema.ResourceTypeResource;

import oracle.iam.identity.icf.scim.v2.request.Patch;
import oracle.iam.identity.icf.scim.v2.request.Operation;

import oracle.iam.identity.icf.scim.v2.schema.SchemaFactory;
import oracle.iam.identity.icf.scim.v2.schema.TenantResource;
import oracle.iam.identity.icf.scim.v2.schema.AccountResource;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends Feature {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** of the resource type <code>User</code>.
   ** <p>
   ** SCIM provides a resource type for <code>Tenant</code>s resources. The 
   ** schema for <code>User</code> is identified using the following schema URI:
   ** <ul>
   **   <li><code>urn:p20:scim:schemas:uid:1.0:Tenant</code>
   ** </ul>
   */
  public static final String      ENDPOINT_TENANTS = "Tenants";

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Schema discovery is performed by query the Service Provider.
   */
  public class Server implements SystemSchema {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default connector <code>Server</code> schema that allows
     ** use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Server() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   build (ServiceSchema)
    /**
     ** Builds schema meta-data from configuration and by obtaining meta-data
     ** from target environment. Can't override this method because static, so
     ** this requires a new class.
     **
     ** @return                  the schema object based on the info provided.
     **                          <br>
     **                          Possible object {@link Schema}.
     **
     ** @throws SystemException  if an error occurs.
     */
    @Override
    public Schema build()
      throws SystemException {

      return schema(Context.this.searchSchema());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Static
  // ~~~~~ ~~~~~~
  /**
   ** Schema is performed by filling up with static data.
   */
  public class Static implements SystemSchema {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a default connector <code>Static</code> schema that allows
     ** use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Static() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   build (ServiceSchema)
    /**
     ** Builds schema meta-data from configuration and by obtaining meta-data
     ** from target environment. Can't override this method because static, so
     ** this requires a new class.
     **
     ** @return                  the schema object based on the info provided.
     **                          <br>
     **                          Possible object {@link Schema}.
     */
    @Override
    public Schema build() {
      return schema(CollectionUtility.list(SchemaFactory.schema(AccountResource.class), SchemaFactory.schema(GroupResource.class), SchemaFactory.schema(TenantResource.class)));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceContext</code> which is associated with the
   ** specified {@link ServiceEndpoint} for configuration purpose.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   */
  private Context(final ServiceClient client) {
    // ensure inheritance
    super(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceClient}.
   **
   ** @return                    a default context.
   **                            <br>
   **                            Possible object is {@link ServiceContext}.
   */
  public static Context build(final ServiceClient client) {
    return new Context(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Describes the types of objects this Connector supports.
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
   ** @param  collection         the {@link Iterable} collection of
   **                            {@link SchemaResource}
   **                            <br>
   **                            Allowed object is {@link Iterable} of type
   **                            {@link SchemaResource}.
   **
   ** @return                    the {@link Schema}.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  public static Schema schema(final Iterable<SchemaResource> collection) {
    final SchemaBuilder                   builder  = new SchemaBuilder(Main.class);
    final Map<String, Set<AttributeInfo>> object   = new HashMap<>();
    for (SchemaResource cursor : collection) {
      // determine the schema in scope
      // it's assumed that any extension ends with a discrimator like User or
      // Role to be able to detect to which object class the schema belongs
      // skip all scheme that does not belong to provisionable entities like
      // the schema itself or resource types
      if (StringUtility.endsWithIgnoreCase(cursor.id(), "user"))
        objectClassSchema(builder, ObjectClass.ACCOUNT_NAME, cursor, object);
      else if (StringUtility.endsWithIgnoreCase(cursor.id(), "group"))
        objectClassSchema(builder, ObjectClass.GROUP_NAME, cursor, object);
      else if (StringUtility.endsWithIgnoreCase(cursor.id(), "tenant"))
        objectClassSchema(builder, ExtensionClass.TENANT.getObjectClassValue(), cursor, object);
    }
    // how stupid ICF is build shows the code below
    // instead of providing a fine grained api to take control how a certain
    // object class is presented in the populated schema you need to add the
    // object class generated completely at first. The ugly frameork will add
    // than all possible operation to the object class that it can find, with
    // the result that we need to remove all operation later and allow only
    // those that the connector is supporting in reality
    // what kind of bull shit is that, to keep the garbage collector busy only
    final List<ObjectClassInfo> latch = new ArrayList<ObjectClassInfo>();
    for (Map.Entry<String, Set<AttributeInfo>> cursor : object.entrySet() ) {
      final ObjectClassInfoBuilder objectBuilder = new ObjectClassInfoBuilder();
      objectBuilder.setType(cursor.getKey());
      objectBuilder.addAllAttributeInfo(cursor.getValue());
      final ObjectClassInfo objectClass = objectBuilder.build();
      latch.add(objectClass);
      builder.defineObjectClass(objectClass);
    }
    builder.clearSupportedObjectClassesByOperation();
    for (ObjectClassInfo cursor : latch) {
      for (Class<? extends SPIOperation> operation : ServiceConnector.OPERATION)
        builder.addSupportedObjectClass(operation, cursor);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Describes the types of objects this Connector supports.
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
   ** @return                    the {@link Schema}.
   **                            <br>
   **                            Possible object is {@link Schema}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Schema schema()
    throws SystemException {

    return this.endpoint().fetchSchema() ? new Server().build() : new Static().build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchResourceType
  /**
   ** Returns the resource types supported by the Service Provider.
   **
   ** @return                    the list of resource types supported by the
   **                            Service Provider.
   **
   ** @throws SystemException    if an error occurs.
   */
  public ListResponse<ResourceTypeResource> searchResourceType()
    throws SystemException {

    return search(ENDPOINT_RESOURCE_TYPES).invoke(ResourceTypeResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupResourceType
  /**
   ** Returns a known resource type supported by the Service Provider.
   **
   ** @param  name               the name of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the resource type with the provided name.
   **                            <br>
   **                            Possible object is
   **                            {@link ResourceTypeResource}.
   **
   **
   ** @throws SystemException    if an error occurs.
   */
  public ResourceTypeResource lookupResourceType(final String name)
    throws SystemException {

    return lookup(ENDPOINT_RESOURCE_TYPES, name, ResourceTypeResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSchema
  /**
   ** Returns the schemas supported by the Service Provider.
   **
   ** @return                    the list of schemas supported by the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is {@link ListResponse} of type
   **                            {@link SchemaResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public ListResponse<SchemaResource> searchSchema()
    throws SystemException {

    return search(ENDPOINT_SCHEMAS).invoke(SchemaResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSchema
  /**
   ** Returns a known schema supported by the Service Provider.
   **
   ** @param  id                 the schema URN.
   **
   ** @return                    the resource type with the provided URN.
   **
   ** @throws SystemException    if an error occurs.
   */
  public SchemaResource lookupSchema(final String id)
    throws SystemException {

    return lookup(ENDPOINT_SCHEMAS, id, SchemaResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to accounts.
   **
   ** @param  start              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter criteria used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchAccount(final int start, final int count, final String criteria) {
    return searchRequest(ENDPOINT_USERS).page(start, count).filter(criteria);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupAccount
  /**
   ** Returns a known SCIM 2 user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 user
   **                            resource.
   **                            <br>
   **                            Possible object is {@link AccountResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public AccountResource lookupAccount(final String id)
    throws SystemException {

    return lookup(ENDPOINT_USERS, id, AccountResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveAccount
  /**
   ** Returns a known SCIM 2 user resource from the Service Provider by its
   ** username.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved identifier of a
   **                            SCIM 2 user resource.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveAccount(final String id)
    throws SystemException {

    return resolve(ENDPOINT_USERS, User.UNIQUE, id, AccountResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to groups.
   **
   ** @param  start              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter criteria used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchRole(final int start, final int count, final String criteria) {
    return searchRequest(ENDPOINT_GROUPS).page(start, count).filter(criteria);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Returns a known SCIM 2 group resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 group
   **                            resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource lookupRole(final String id)
    throws SystemException {

    return lookup(ENDPOINT_GROUPS, id, GroupResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveRole
  /**
   ** Returns a known SCIM 2 group resource from the Service Provider by its
   ** display name.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 group
   **                            resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveRole(final String id)
    throws SystemException {

    return resolve(ENDPOINT_GROUPS, Group.UNIQUE, id, GroupResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchTenant
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider belonging to tenants.
   **
   ** @param  start              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  criteria           the filter criteria used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchTenant(final int start, final int count, final String criteria) {
    return searchRequest(ENDPOINT_TENANTS).page(start, count).filter(criteria);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRole
  /**
   ** Returns a known SCIM 2 tenant resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 tenant
   **                            resource.
   **                            <br>
   **                            Possible object is {@link TenantResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public TenantResource lookupTenant(final String id)
    throws SystemException {

    return lookup(ENDPOINT_TENANTS, id, TenantResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveTenant
  /**
   ** Returns a known SCIM 2 tenant resource from the Service Provider by its
   ** display name.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved SCIM 2 tenant
   **                            resource.
   **                            <br>
   **                            Possible object is {@link TenantResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveTenant(final String id)
    throws SystemException {

    return resolve(ENDPOINT_TENANTS, TenantResource.UNIQUE, id, TenantResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the Service Provider.
   **
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is {@link AccountResource}.
   **
   ** @return                    the successfully created SCIM User resource.
   **                            <br>
   **                            Possible object is {@link AccountResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public AccountResource createAccount(final AccountResource resource)
    throws SystemException {

    return create(ENDPOINT_USERS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRole
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the Service Provider.
   **
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is {@link GroupResource}.
   **
   ** @return                    the successfully created SCIM User resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource createRole(final GroupResource resource)
    throws SystemException {

    return create(ENDPOINT_GROUPS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTenant
  /**
   ** Build and executes the request to create the provided new SCIM resource at
   ** the Service Provider.
   **
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is {@link TenantResource}.
   **
   ** @return                    the successfully created SCIM User resource.
   **                            <br>
   **                            Possible object is {@link TenantResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public TenantResource createTenant(final TenantResource resource)
    throws SystemException {

    return create(ENDPOINT_TENANTS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAccount
  /**
   ** Build and executes the request to modify the SCIM 2 user resource at the
   ** Service Provider with the given SCIM 2 user resource.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes all attributes are
   ** send, regardless of each attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  resource           the SCIM 2 user resource to modify.
   **                            <br>
   **                            Allowed object is  {@link AccountResource}.
   **
   ** @return                    the successfully modified SCIM 2 user resource.
   **                            <br>
   **                            Possible object is {@link AccountResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public AccountResource modifyAccount(final AccountResource resource)
    throws SystemException {

    return modify(ENDPOINT_USERS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRole
  /**
   ** Build and executes the request to modify the SCIM 2 group resource at the
   ** Service Provider with the given SCIM 2 group resource.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes all attributes are
   ** send, regardless of each attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  resource           the SCIM 2 group resource to modify.
   **                            <br>
   **                            Allowed object is  {@link GroupResource}.
   **
   ** @return                    the successfully modified SCIM 2 group resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource modifyRole(final GroupResource resource)
    throws SystemException {

    return modify(ENDPOINT_GROUPS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyTenant
  /**
   ** Build and executes the request to modify the SCIM 2 tenant resource at the
   ** Service Provider with the given SCIM 2 tenant resource.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes all attributes are
   ** send, regardless of each attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  resource           the SCIM 2 tenant resource to modify.
   **                            <br>
   **                            Allowed object is  {@link TenantResource}.
   **
   ** @return                    the successfully modified SCIM 2 tenant resource.
   **                            <br>
   **                            Possible object is {@link TenantResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public TenantResource modifyTenant(final TenantResource resource)
    throws SystemException {

    return modify(ENDPOINT_TENANTS, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAccount
  /**
   ** Build and executes the request to modify the SCIM 2 user resource at the
   ** Service Provider with the given <code>operation</code>s.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   **
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.                            
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the successfully modified SCIM 2 user resource.
   **                            <br>
   **                            Possible object is {@link AccountResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public AccountResource modifyAccount(final String identifier, final List<Operation> operation)
    throws SystemException {

    return modify(ENDPOINT_USERS, identifier, operation, AccountResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRole
  /**
   ** Build and executes the request to modify the SCIM 2 group resource at the
   ** Service Provider with the given patch operations.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   **
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the successfully modified SCIM 2 group resource.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public GroupResource modifyRole(final String identifier, final List<Operation> operation)
    throws SystemException {

    return modify(ENDPOINT_GROUPS, identifier, operation, GroupResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyTenant
  /**
   ** Build and executes the request to modify the SCIM 2 tenant resource at the
   ** Service Provider with the given patch operations.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   **
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the successfully modified SCIM 2 tenant resource.
   **                            <br>
   **                            Possible object is {@link TenantResource}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public TenantResource modifyTenant(final String identifier, final List<Operation> operation)
    throws SystemException {

    return modify(ENDPOINT_TENANTS, identifier, operation, TenantResource.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteAccount
  /**
   ** Delete the SCIM user resource in the Service Provider that belongs to the
   ** given identifier.
   **
   ** @param  identifier         the identifier of the user resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteAccount(final String identifier)
    throws SystemException {

    delete(ENDPOINT_USERS, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRole
  /**
   ** Delete the SCIM group resource in the Service Provider that belongs to the
   ** given identifier.
   **
   ** @param  identifier         the identifier of the group resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteRole(final String identifier)
    throws SystemException {

    delete(ENDPOINT_GROUPS, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteTenant
  /**
   ** Delete the SCIM tenant resource in the Service Provider that belongs to
   ** the given identifier.
   **
   ** @param  identifier         the identifier of the tenant resource to
                                 delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteTenant(final String identifier)
    throws SystemException {

    delete(ENDPOINT_TENANTS, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modify a SCIM 2 resource by updating one or more attributes using a
   ** sequence of operations to "<code>add</code>", "<code>remove</code>", or
   ** "<code>replace</code>" values.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <br>
   ** The Service Provider configuration maybe used to discover service provider
   ** support for <code>PATCH</code>.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Roles</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.                            
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the successfully modified SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  protected final <T extends Resource> T modify(final String context, final String identifier, final List<Operation> operation, final Class<T> clazz)
    throws SystemException {

    return modifyRequest(context, identifier, operation).invoke(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Build a request to patch a SCIM 2 resource at the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Roles</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.                            
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Patch} for type
   **                            <code>T</code>.
   */
  private <T extends Resource> Patch<T> modifyRequest(final String context, final String id, final List<Operation> operation) {
    return new Patch<T>(requestTarget(context, id), operation, this.client.typeContent(), this.client.typeAccept());
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClassSchema
  /**
   ** Describes the types of a particular schema resource.
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
   ** @param  builder            the ICF {@link {@link SchemaBuilder}} to use.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  type               the type of the ICF {@link ObjectClass} to
   **                            discover.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the SCIM {@link {@link SchemaResource}} to
   **                            convert.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  collector          the {@link Map} collection of
   **                            {@link AttributeInfo} discovered so far.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Set} as the value. The element type
   **                            of the value set is  {@link AttributeInfo}.
   */
  private static void objectClassSchema(final SchemaBuilder builder, final String type, final SchemaResource resource, final Map<String, Set<AttributeInfo>> collector) {
   // the attribute collector per schema resource
   final Set<AttributeInfo> collection = new HashSet<>();
   collection.addAll(attributeSchema(type, resource, builder));
    if (collector.containsKey(type)) {
      final Set<AttributeInfo> current = collector.get(type);
      current.addAll(collection);
    }
    else {
      collector.put(type, collection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeSchema
  /**
   ** Builds the collection of {@link AttributeInfo} object which carries the
   ** schema information for a single resource.
   **
   ** @param  prefix             the name of the {@link ObjectClass} schema the
   **                            attribute is build for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link SchemaResource} describing the
   **                            schema to build for an account.
   **                            <br>
   **                            Allowed object is {@link SchemaResource}.
   ** @param  schema             the {@link SchemaBuilder} populating the entire
   **                            schema to build for an user account.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   **
   ** @return                    an instance of ObjectClassInfo with the
   **                            constructed schema information.
   **                            <br>
   **                            Possible object {@link ObjectClassInfo}.
   */
  private static Set<AttributeInfo> attributeSchema(final String prefix, final SchemaResource resource, final SchemaBuilder schema) {
    // the attribute collector per schema resource
    final Set<AttributeInfo> collector = new HashSet<>();
    for (Definition definition : resource.attribute()) {
      if (definition.type() == Definition.Type.COMPLEX) {
        if (definition.multiValued()) {
          // build the object class schema for the multi-valued attribute
          embeddedSchema(schema, String.format("%s.%s", prefix, definition.name()), definition);
          // add the reference to the object class defined above as an
          // embedded reference
          collector.add(attributeSchema(prefix, definition, true));
        }
        else {
          for (Definition embed : definition.attributes()) {
            collector.add(attributeSchema(definition.name(), embed, true));
          }
        }
      }
      else {
        collector.add(attributeSchema(prefix, definition, false));
      }
    }
		return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeSchema
  /**
   ** Builds the "AttributeInfo" object which carries the attribute schema
   ** information for a multi-valued definition.
   **
   ** @param  prefix             the name of the {@link ObjectClass} schema the
   **                            attribute is build for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  definition         the {@link Definition} describing the attribute
   **                            to build.
   **                            <br>
   **                            Allowed object is {@link Definition}.
   ** @param  complex            <code>true</code> if the attribute to built is
   **                            an complex attribute.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    an instance of {@link AttributeInfo} with
   **                            the constructed schema information.
   **                            <br>
   **                            Possible object {@link AttributeInfo}.
   */
  private static AttributeInfo attributeSchema(final String prefix, final Definition definition, final boolean complex) {
    final String               name    = complex ? String.format("%s.%s", prefix, definition.name()) : definition.name();
    final AttributeInfoBuilder builder = new AttributeInfoBuilder();
    builder.setName(name);
    switch (definition.type()) {
      case BOOLEAN  : builder.setType(Boolean.class);
                      break;
      case DECIMAL  : builder.setType(Long.class);
                      break;
      case INTEGER  : builder.setType(Integer.class);
                      break;
    // special care have to be taken on Date types
    // the stupid guy whom developed the connector framework wasn't able to
    // do it right --> a Date value is per default serializable but e have
    // to use a Long instead
      case DATETIME : builder.setType(Long.class);
                      break;
      // an EmbeddedObject represents an object (e.g., an Account's Address)
      // on the target resource that is embedded in another object.
      // Each EmbeddedObject represents a resource object as a bag of
      // attributes. Unlike ConnectorObject, EmbeddedObjects do not have an
      // identity, and therefore are not required to (and most often do not)
      // have a Uid nor a Name.
      case COMPLEX  : builder.setType(EmbeddedObject.class);
                      builder.setObjectClassName(name);
                      break;
      default       : builder.setType(String.class);
                      break;
    }
    switch (definition.mutability()) {
      case READ_ONLY  : builder.setCreateable(false);
                        builder.setReadable(true);
                        break;
      case READ_WRITE : builder.setCreateable(true);
                        builder.setReadable(true);
                        break;
      case IMMUTABLE  : builder.setCreateable(true);
                        builder.setReadable(true);
                        break;
      case WRITE_ONLY : builder.setCreateable(true);
                        builder.setReadable(false);
                        break;
    }
    if (definition.required())
      builder.setRequired(true);
    if (definition.multiValued())
      builder.setMultiValued(true);
    builder.setReturnedByDefault(definition.returned() != Definition.Returned.NEVER);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   embeddedSchema
  /**
   ** Builds the "ObjectClassInfo" object which carries the schema information
   ** for an single embedded resource.
   **
   ** @param  builder            the {@link SchemaBuilder} collection the
   **                            schema to build for a resource.
   **                            <br>
   **                            Allowed object is {@link SchemaBuilder}.
   ** @param  objectClass        the name of the {@link ObjectClass} schema the
   **                            schema is build for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  definition         the {@link Definition} describing the
   **                            attributes of the schema to build.
   **                            <br>
   **                            Allowed object is {@link Definition}.
   */
  private static void embeddedSchema(final SchemaBuilder builder, final String objectClass, final Definition resource) {
    final ObjectClassInfoBuilder embedded = new ObjectClassInfoBuilder();
    embedded.setType(objectClass);
    embedded.setEmbedded(true);
    embedded.addAttributeInfo(Name.INFO);
    for (Definition embed : resource.attributes()) {
      embedded.addAttributeInfo(attributeSchema(objectClass, embed, false));
    }
    builder.defineObjectClass(embedded.build());
  }
}