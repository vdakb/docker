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

    File        :   Main.java
    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.scim.igs;

import java.lang.reflect.InvocationTargetException;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;

import java.util.Collections;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.spi.operations.ResolveUsernameOp;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceMessage;
import oracle.iam.identity.icf.rest.ServiceConnector;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.ServiceConfiguration;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.scim.Path;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.schema.NewUser;
import oracle.iam.identity.icf.scim.schema.Group;
import oracle.iam.identity.icf.scim.schema.Member;
import oracle.iam.identity.icf.scim.schema.Resource;

import oracle.iam.identity.icf.scim.v2.Translator;

import oracle.iam.identity.icf.scim.v2.request.Operation;

import oracle.iam.identity.icf.scim.v2.schema.Marshaller;
import oracle.iam.identity.icf.scim.v2.schema.NewUserResource;
import oracle.iam.identity.icf.scim.v2.schema.GroupResource;
import oracle.iam.identity.icf.scim.v2.schema.TenantResource;
import oracle.iam.identity.icf.scim.v2.schema.AccountResource;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link ServiceConnector} for a SCIM application system.
 ** <p>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=ServiceConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.scim.igs.Main.properties")
public class Main extends    ServiceConnector
                  implements ResolveUsernameOp {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String CATEGORY = "JCS.CONNECTOR.IGS";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private Context             context  = null;

  /**
   ** The {@link Schema} cached over the lifetime of the task.
   */
  private Schema              schema   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> SCIM 2 connector that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Main() {
    // ensure inheritance
    super(CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (Connector)
  /**
   ** Initialize the connector with its configuration.
   ** <p>
   ** For instance in a JDBC Connector this would include the database URL,
   ** password, and user.
   **
   ** @param  configuration      the instance of the {@link Configuration}
   **                            object implemented by the
   **                            <code>Connector</code> developer and populated
   **                            with information in order to initialize the
   **                            <code>Connector</code>.
   **
   ** @throws ConnectorException if the authentication/authorization request
   **                            implemented in the {@link Context} constructor
   **                            fails.
   */
  @Override
  public void init(final Configuration configuration)
    throws ConnectorException {

    final String method = "init";
    trace(method, Loggable.METHOD_ENTRY);
    this.config = (ServiceConfiguration)configuration;
    try {
      this.context = Context.build(ServiceClient.build(this.config.endpoint()));
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispose (Connector)
  /**
   ** Dispose of any resources the Connector uses.
   */
  @Override
  public void dispose() {
    final String method = "dispose";
    trace(method, Loggable.METHOD_ENTRY);
    this.config  = null;
    this.context = null;
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test (TestOp)
  /**
   ** Tests the configuration with the connector.
   ** <p>
   ** Unlike validation, testing a configuration checks that any pieces of
   ** environment referred by the configuration are available. For example, the
   ** connector could make a physical connection to a host specified in the
   ** configuration to check that it exists and that the credentials specified
   ** in the configuration are usable.
   ** <p>
   ** This operation may be invoked before the configuration has been validated.
   ** <br>
   ** An implementation is free to validate the configuration before testing it.
   */
  @Override
  public void test() {
    final String method = "test";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      this.context.searchResourceType();
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema (SchemaOp)
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
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   */
  @Override
  public Schema schema()
    throws ConnectorException {

    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      if (this.schema == null)
        this.schema = this.context.schema();
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilterTranslator (SearchOp)
  /**
   ** Creates a filter translator that will translate a specified
   ** <code>filter</code> into one or more native queries.
   ** <br>
   ** Each of these native queries will be passed subsequently into
   ** <code>#executeQuery(ObjectClass, Filter, ResultsHandler, OperationOptions)</code>.
   **
   ** @param  type                the {@link ObjectClass} for the search.
   **                             Will never be <code>null</code>.
   ** @param  option              additional options that impact the way this
   **                             operation is run.
   **                             <br>
   **                             If the caller passes <code>null</code>, the
   **                             framework will convert this into an empty set
   **                             of options, so SPI need not worry about this
   **                             ever being <code>null</code>.
   **
   ** @return                     a filter translator.
   **                             This must not be <code>null</code>.
   **                             <br>
   **                             A <code>null</code> return value will cause
   **                             the API (SearchApiOp) to throw
   **                             <code>NullPointerException</code>.
   */
  @Override
  public FilterTranslator<String> createFilterTranslator(final ObjectClass type, final OperationOptions option) {
    final String method = "createFilterTranslator";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return new FilterTranslator<String>() {
        /**
         ** Main method to be called to translate a filter.
         */
        @Override
        public List<String> translate(final Filter filter) {
          try {
            return Translator.build().translate(filter);
          }
          catch (ServiceException e) {
            throw new RuntimeException(e);
          }
        }
      };
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeQuery (SearchOp)
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  objectClass        the class of object for which to return
   **                            synchronization events.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  criteria           the criteria to apply on the search and
   **                            converted from the {@link Filter} passed to
   **                            {@link #createFilterTranslator(ObjectClass, OperationOptions)}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   */
  @Override
  public void executeQuery(final ObjectClass objectClass, final String criteria, final ResultsHandler handler, final OperationOptions option) {
    final String method = "executeQuery";
    trace(method, Loggable.METHOD_ENTRY);

    final Set<String> returning = new HashSet<>();
    if (option.getAttributesToGet() != null) {
      returning.addAll(CollectionUtility.set(option.getAttributesToGet()));
    }

    // initialize the operations control configuration
    final OperationContext control = OperationContext.build(option);

    int batch = 0;
    int index = 1;
    try {
      if (ObjectClass.ACCOUNT.equals(objectClass)) {
        do {
          // .emit(returning)
          final ListResponse<NewUserResource> response = this.context.searchAccount(index, control.limit, criteria).invoke(NewUserResource.class);
          batch = response.items();
          if (batch == 0) {
            info(ServiceBundle.string(ServiceMessage.NOTHING_TO_CHANGE));
            break;
          }
          for (NewUserResource user : response) {
            handler.handle(transform(user));
          }
          index += control.limit;
        } while (batch == control.limit);
      }
      else if (ObjectClass.GROUP.equals(objectClass)) {
        do {
          // .emit(returning)
          final ListResponse<GroupResource> response = this.context.searchRole(index, control.limit, criteria).invoke(GroupResource.class);
          batch = response.items();
          if (batch == 0) {
            info(ServiceBundle.string(ServiceMessage.NOTHING_TO_CHANGE));
            break;
          }
          for (GroupResource role : response) {
            handler.handle(transform(role));
          }
          index += control.limit;
        } while (batch == control.limit);
      }
      else if (ExtensionClass.TENANT.equals(objectClass)) {
        do {
          // .emit(returning)
          final ListResponse<TenantResource> response = this.context.searchTenant(index, control.limit, criteria).invoke(TenantResource.class);
          batch = response.items();
          if (batch == 0) {
            info(ServiceBundle.string(ServiceMessage.NOTHING_TO_CHANGE));
            break;
          }
          for (TenantResource tenant : response) {
            handler.handle(transform(tenant));
          }
          index += control.limit;
        } while (batch == control.limit);
      }
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveUsername (ResolveUsernameOp)
  /**
   ** Resolve an object to its Uid based on its username.
   ** <br>
   ** This is a companion to the simple authentication. The difference is that
   ** this method does not have a password parameter and does not try to
   ** authenticate the credentials; instead, it returns the {@link Uid}
   ** corresponding to the username.
   ** <p>
   ** If the username validation fails, the a type of RuntimeException either
   ** IllegalArgumentException or if a native exception is available and if its
   ** of type RuntimeException simple throw it. If the native exception is not a
   ** RuntimeException wrap it in one and throw it. This will provide the most
   ** detail for logging problem and failed attempts.
   ** <p>
   ** Of course it's encouraged to try and throw the most informative exception
   ** as possible. In that regards there are several exceptions provided in the
   ** exceptions package. For instance one of the most common is
   ** UnknownUidException.
   **
   ** @param  type               the type of resource to create.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  name               the username to resolve.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public Uid resolveUsername(final ObjectClass type, final String name, final OperationOptions option) {
    // prevent bogus input
    if (name == null || name.isEmpty())
      valuesRequired();

    final String method = "resolveUsername";
    trace(method, Loggable.METHOD_ENTRY);
    // execute operation
    String uid = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        uid = this.context.resolveAccount(name);
        if (uid == null)
          throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Context.ENDPOINT_USERS, NewUser.UNIQUE, name));
      }
      else if (ObjectClass.GROUP.equals(type)) {
        uid = this.context.resolveRole(name);
        if (uid == null)
          throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Context.ENDPOINT_GROUPS, Group.UNIQUE, name));
      }
      else if (ExtensionClass.TENANT.equals(type)) {
        uid = this.context.resolveTenant(name);
        if (uid == null)
          throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, Context.ENDPOINT_TENANTS, TenantResource.UNIQUE, name));
      }
      else {
        unsupportedType(type, method);
      }
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return new Uid(uid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (CreateOp)
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass}) and create an object and its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the created object.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** There will never be a {@link Uid} passed in with the attribute set for
   ** this method. If the resource supports some sort of mutable {@link Uid},
   ** you should create your own resource-specific attribute for it, such as
   ** <code>unix_uid</code>.
   ** <p>
   ** Implements custom create user request against the Rola EFBS target system.
   ** Note this is a standard ICF connector SPI create operation.
   ** The Generic SCIM standard connector needs to be customised for the
   ** following reasons:
   ** <ol>
   **  <li>Status attribute 'active' is not set in the standard connector
   **      implementation but the Rola EFBS SCIM service defines this as
   **      mandatory on create contrary to the SCIM specification.
   **  <li>Multi-valued attributes (email and phone numbers) are implemented as
   **      mandatory by the Rola EFBS SCIM service so values must be provided on
   **      create. The standard Generic SCIM connector however is implemented to
   **      create the user account without child table entries and then to add
   **      those following the create i.e. as two separate operations.
   **      <br>
   **      The Rola service rejects this.
   ** </ol>
   **
   ** @param  type               the type of resource to create.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and
   **                            <code>Name</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    the unique id for the object that is created.
   **                            <br>
   **                            For instance in LDAP this would be the
   **                            <code>entryUUID</code>, for a database this
   **                            would be the primary key, and for
   **                            'ActiveDirectory' this would be the
   **                            <code>objectGUID</code>.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public Uid create(final ObjectClass type, final Set<Attribute> attribute, final OperationOptions option)
    throws ConnectorException {

    // prevent bogus input
    if (attribute.isEmpty())
      valuesRequired();

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }

    // execute operation
    Uid identifier = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        identifier = createAccount(attribute);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        identifier = createRole(attribute);
      }
      else if (ExtensionClass.TENANT.equals(type)) {
        identifier = createTenant(attribute);
      }
      else {
        unsupportedType(type, method);
      }
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (DeleteOp)
  /**
   ** Implements the operation interface to delete objects from the target
   ** resource.
   ** <br>
   ** The {@link Uid} must be provided.
   **
   ** @param  type               the type of resource to delete.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  uid                the unique id that specifies the object to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public void delete(final ObjectClass type, final Uid uid, final OperationOptions option)
    throws ConnectorException {

		// prevent bogus input
    if (type == null)
      typeRequired();

    // prevent bogus input
    final String identifier = uid.getUidValue();
    if (StringUtility.empty(identifier))
      identifierRequired();

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type))
        this.context.deleteAccount(identifier);
      else if (ObjectClass.GROUP.equals(type))
        this.context.deleteRole(identifier);
      else if (ExtensionClass.TENANT.equals(type))
        this.context.deleteTenant(identifier);
      else
        unsupportedType(type, method);
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (UpdateOp)
  /**
   ** Update the object specified by the {@link ObjectClass} and [@link Uid},
   ** replacing the current values of each attribute with the values provided.
   ** <p>
   ** For each input attribute, replace all of the current values of that
   ** attribute in the target object with the values of that attribute.
   ** <p>
   ** If the target object does not currently contain an attribute that the
   ** input set contains, then add this attribute (along with the provided
   ** values) to the target object.
   ** <p>
   ** If the value of an attribute in the input set is <code>null</code>, then
   ** do one of the following, depending on which is most appropriate for the
   ** target:
   ** <ul>
   **   <li>If possible, <em>remove</em> that attribute from the target object
   **        entirely.
   **   <li>Otherwise, <em>replace all of the current values</em> of that
   **       attribute in the target object with a single value of
   **       <code>null</code>.
   ** </ul>
   **
   ** @param  type               the type of resource to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  uid                the unique id that specifies the object to
   **                            modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  replace            the {@link Set} of new {@link Attribute}s the
   **                            values in this set represent the new, merged
   **                            values to be applied to the object. This set
   **                            may also include operational attributes.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    the unique id of the modified object in case
   **                            the update changes the formation of the unique
   **                            identifier.
   **                            <br>
   **                            Possible object {@link Uid}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public Uid update(final ObjectClass type, final Uid uid, final Set<Attribute> replace, final OperationOptions option)
    throws ConnectorException {

		// prevent bogus input
    if (replace.isEmpty())
      propagate(SystemError.OBJECT_VALUES_REQUIRED);

		// prevent bogus input
		if (uid.getUidValue() == null && uid.getUidValue().isEmpty())
      propagate(SystemError.UNIQUE_IDENTIFIER_REQUIRED);

    final String method = "update";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(replace));
    }

    Resource resource = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        resource = this.context.modifyAccount(uid.getUidValue(), Marshaller.operationUser(replace));
      }
      else if (ObjectClass.GROUP.equals(type)) {
        resource = this.context.modifyRole(uid.getUidValue(), Marshaller.operationGroup(replace, false));
      }
      else if (ExtensionClass.TENANT.equals(type)) {
        resource = this.context.modifyTenant(uid.getUidValue(), operationTenant(replace, false));
      }
      else
        unsupportedType(type, method);
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return new Uid(resource.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttributeValues (UpdateAttributeValuesOp)
  /**
   ** Update the object specified by the {@link ObjectClass} and {@link Uid},
   ** adding to the current values of each attribute the values provided.
   ** <p>
   ** For each attribute that the input set contains, add to the current values
   ** of that attribute in the target object all of the values of that attribute
   ** in the input set.
   ** <p>
   ** <b>NOTE</b>:
   ** <br>
   ** That this does not specify how to handle duplicate values.
   ** <br>
   ** The general assumption for an attribute of a ConnectorObject is that the
   ** values for an attribute may contain duplicates. Therefore, in general
   ** simply append the provided values to the current value for each attribute.
   **
   ** @param  type               the type of resource to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  uid                the unique id that specifies the object to
   **                            modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  addition           the {@link Set} of new {@link Attribute}s the
   **                            values in this set represent the new, merged
   **                            values to be applied to the object. This set
   **                            may also include operational attributes.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    the unique id of the modified object in case
   **                            the update changes the formation of the unique
   **                            identifier.
   **                            <br>
   **                            Possible object {@link Uid}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public Uid addAttributeValues(final ObjectClass type, final Uid uid, final Set<Attribute> addition, final OperationOptions option)
    throws ConnectorException {

    // prevent bogus input
    if (addition.isEmpty())
      valuesRequired();

    // prevent bogus input
    final String identifier = uid.getUidValue();
    if (StringUtility.empty(identifier))
      identifierRequired();

    final String method = "addAttributeValues";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(addition));
    }

    Uid result = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : addition) {
          final List<EmbeddedObject> value = (List<EmbeddedObject>)List.class.cast(cursor.getValue());
          for (EmbeddedObject subject : value) {
            if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
              assignRole(identifier, subject.getAttributes());
            }
            else if (ExtensionClass.TENANT.equals(subject.getObjectClass())) {
              assignTenant(identifier, subject.getAttributes());
            }
            else {
              unsupportedType(subject.getObjectClass(), method);
            }
          }
        }
      }
      else if (ObjectClass.GROUP.equals(type)) {
        result = patchRole(identifier, Marshaller.operationGroup(addition, false));
      }
      else if (ExtensionClass.TENANT.equals(type)) {
        result = patchTenant(identifier, operationTenant(addition, false));
      }
      else {
        unsupportedType(type, method);
      }
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return new Uid(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAttributeValues (UpdateAttributeValuesOp)
  /**
   ** Update the object specified by the {@link ObjectClass} and {@link Uid},
   ** removing from the current values of each attribute the values provided.
   ** <p>
   ** For each attribute that the input set contains, remove from the current
   ** values of that attribute in the target object any value that matches one
   ** of the values of the attribute from the input set.
   ** <p>
   ** <b>NOTE</b>:
   ** <br>
   ** That this does not specify how to handle unmatched values. The general
   ** assumption for an attribute of a ConnectorObject is that the values for an
   ** attribute are merely representational state. Therefore, the implementer
   ** should simply ignore any provided value that does not match a current
   ** value of that attribute in the target object.
   ** <br>
   ** Deleting an unmatched value should always succeed.
   **
   ** @param  type               the type of resource to modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  uid                the unique id that specifies the object to
   **                            modify.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  remove             the {@link Set} of new {@link Attribute}s the
   **                            values in this set represent the new, merged
   **                            values to be applied to the object. This set
   **                            may also include operational attributes.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    the unique id of the modified object in case
   **                            the update changes the formation of the unique
   **                            identifier.
   **                            <br>
   **                            Possible object {@link Uid}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public Uid removeAttributeValues(final ObjectClass type, final Uid uid, final Set<Attribute> remove, final OperationOptions option)
    throws ConnectorException {

    // prevent bogus input
    if (remove.isEmpty())
      propagate(SystemError.OBJECT_VALUES_REQUIRED);

    // prevent bogus input
    final String identifier = uid.getUidValue();
    if (StringUtility.empty(identifier))
      propagate(SystemError.UNIQUE_IDENTIFIER_REQUIRED);

    final String method = "removeAttributeValues";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(remove));
    }

    trace(method, Loggable.METHOD_EXIT);
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : remove) {
          final List<EmbeddedObject> value = (List<EmbeddedObject>)List.class.cast(cursor.getValue());
          for (EmbeddedObject subject : value) {
            if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
              revokeRole(identifier, subject.getAttributes());
            }
            else if (ExtensionClass.TENANT.equals(subject.getObjectClass())) {
              revokeTenant(identifier, subject.getAttributes());
            }
            else {
              unsupportedType(subject.getObjectClass(), method);
            }
          }
        }
      }
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return uid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass} and create an object and its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the created object.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** There will never be a {@link Uid} passed in with the attribute set for
   ** this method. If the resource supports some sort of mutable {@link Uid},
   ** you should create your own resource-specific attribute for it, such as
   ** <code>unix_uid</code>.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   **
   ** @return                    the unique id for the object that is created.
   **                            <br>
   **                            For instance in LDAP this would be the
   **                            <code>entryUUID</code>, for a database this
   **                            would be the primary key, and for
   **                            'ActiveDirectory' this would be the
   **                            <code>objectGUID</code>.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ConnectorException if the parameters specified have issues.
   */
  protected Uid createAccount(final Set<Attribute> attribute)
    throws SystemException
    ,      ConnectorException {

    final String method = "createAccount";
    trace(method, Loggable.METHOD_ENTRY);

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    Attribute username = accessor.find(Marshaller.USERNAME);
    if (username == null) {
      username = accessor.find(Name.NAME);
    }
    // prevent bogus state
    if (username == null) {
      trace(method, Loggable.METHOD_EXIT);
      propagate(SystemError.NAME_IDENTIFIER_REQUIRED, Marshaller.USERNAME);
    }

    Attribute password = accessor.find(Marshaller.PASSWORD);
    if (password == null) {
      password = accessor.find(OperationalAttributes.PASSWORD_NAME);
    }
    Attribute status = accessor.find(Marshaller.STATUS);
    if (status == null) {
      status = accessor.find(OperationalAttributes.ENABLE_NAME);
    }
    // prevent bogus state
    if (status == null || status.getValue() == null || status.getValue().isEmpty()) {
      warning(method, ServiceBundle.string(ServiceMessage.STATUS_NOT_PROVIDED));
      status = AttributeBuilder.build(Marshaller.STATUS, Boolean.TRUE);
    }

    // execute operation
    AccountResource resource = null;
    try {
      // marshall a SCIM user resource transfered to the Service Provider
      resource = this.context.createAccount(Marshaller.transferUser(AccountResource.class, attribute));
      return new Uid(resource.id());
    }
    catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw SystemException.unhandled(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRole
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass} and create an object and its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the created object.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** There will never be a {@link Uid} passed in with the attribute set for
   ** this method. If the resource supports some sort of mutable {@link Uid},
   ** you should create your own resource-specific attribute for it, such as
   ** <code>unix_uid</code>.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   **
   ** @return                    the unique id for the object that is created.
   **                            <br>
   **                            For instance in LDAP this would be the
   **                            <code>entryUUID</code>, for a database this
   **                            would be the primary key, and for
   **                            'ActiveDirectory' this would be the
   **                            <code>objectGUID</code>.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ConnectorException if the parameters specified have issues.
   */
  protected Uid createRole(final Set<Attribute> attribute)
    throws SystemException
    ,      ConnectorException {

    // prevent bogus input
    if (attribute.isEmpty())
      propagate(SystemError.OBJECT_VALUES_REQUIRED);

    final String method = "createRole";
    trace(method, Loggable.METHOD_ENTRY);

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    Attribute rolename = accessor.find(Marshaller.GROUPNAME);
    if (rolename == null) {
      rolename = accessor.find(Name.NAME);
    }
    // prevent bogus state
    if (rolename == null) {
      trace(method, Loggable.METHOD_EXIT);
      propagate(SystemError.NAME_IDENTIFIER_REQUIRED, Marshaller.GROUPNAME);
    }

    // execute operation
    try {
      // marshall a SCIM user resource transfered to the Service Provider
      final GroupResource resource = this.context.createRole(Marshaller.transferGroup(GroupResource.class, attribute));
      return new Uid(resource.id());
    }
    catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw SystemException.unhandled(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTenant
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass} and create an object and its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the created object.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** There will never be a {@link Uid} passed in with the attribute set for
   ** this method. If the resource supports some sort of mutable {@link Uid},
   ** you should create your own resource-specific attribute for it, such as
   ** <code>unix_uid</code>.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   **
   ** @return                    the unique id for the object that is created.
   **                            <br>
   **                            For instance in LDAP this would be the
   **                            <code>entryUUID</code>, for a database this
   **                            would be the primary key, and for
   **                            'ActiveDirectory' this would be the
   **                            <code>objectGUID</code>.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   ** @throws ConnectorException if the parameters specified have issues.
   */
  protected Uid createTenant(final Set<Attribute> attribute)
    throws SystemException
    ,      ConnectorException {

    // prevent bogus input
    if (attribute.isEmpty())
      propagate(SystemError.OBJECT_VALUES_REQUIRED);

    final String method = "createTenant";
    trace(method, Loggable.METHOD_ENTRY);

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    Attribute cursor = accessor.find(Uid.NAME);
    if (cursor == null) {
      cursor = accessor.find("id");
    }
    // prevent bogus state
    if (cursor == null) {
      trace(method, Loggable.METHOD_EXIT);
      propagate(SystemError.UNIQUE_IDENTIFIER_REQUIRED, Uid.NAME);
    }

    cursor = accessor.find(Name.NAME);
    if (cursor == null) {
      cursor = accessor.find(Marshaller.GROUPNAME);
    }
    // prevent bogus state
    if (cursor == null) {
      trace(method, Loggable.METHOD_EXIT);
      propagate(SystemError.NAME_IDENTIFIER_REQUIRED, Name.NAME);
    }

    // execute operation
    try {
      // marshall a SCIM 2 tenant resource transfered to the Service Provider
      final TenantResource resource = this.context.createTenant(transferTenant(attribute));
      return new Uid(resource.id());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchAccount
  /**
   ** Taking the attributes given modify an object and return its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the object to modify
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   **
   ** @return                    the unique id for the object that is created.
   **                            <br>
   **                            For instance in LDAP this would be the
   **                            <code>entryUUID</code>, for a database this
   **                            would be the primary key, and for
   **                            'ActiveDirectory' this would be the
   **                            <code>objectGUID</code>.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected Uid patchAccount(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final NewUserResource resource = this.context.modifyAccount(identifier, Marshaller.operationUser(attribute));
    return new Uid(resource.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchRole
  /**
   ** Taking the attributes given modify an object and return its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the object to modify
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the unique id for the object that is created.
   **                            <br>
   **                            For instance in LDAP this would be the
   **                            <code>entryUUID</code>, for a database this
   **                            would be the primary key, and for
   **                            'ActiveDirectory' this would be the
   **                            <code>objectGUID</code>.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected Uid patchRole(final String identifier, final List<Operation> operation)
    throws SystemException {

    final GroupResource resource = this.context.modifyRole(identifier, operation);
    return new Uid(resource.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchTenant
  /**
   ** Taking the attributes given modify an object and return its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the object to modify
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the patch operation to apply by the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @return                    the unique id for the object that is created.
   **                            <br>
   **                            For instance in LDAP this would be the
   **                            <code>entryUUID</code>, for a database this
   **                            would be the primary key, and for
   **                            'ActiveDirectory' this would be the
   **                            <code>objectGUID</code>.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected Uid patchTenant(final String identifier, final List<Operation> operation)
    throws SystemException {

    final TenantResource resource = this.context.modifyTenant(identifier, operation);
    return new Uid(resource.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRole
  /**
   ** Taking the attributes given modify a role.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the role to modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to modify
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void assignRole(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignRole";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Uid scope = accessor.getUid();
      if (scope == null)
        identifierRequired();

      final List<Operation> operation = new ArrayList<Operation>();
      operation.add(Operation.add(Path.from(Marshaller.MEMBEROF), CollectionUtility.list(Member.of(identifier))));
      this.context.modifyRole(scope.getUidValue(), operation);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRole
  /**
   ** Taking the attributes given modify a role.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the role to modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to modify
   **                            the resource object including the
   **                            {@link Uid} attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void revokeRole(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeRole";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearange the attribute due the role is in the passed in
    // attributes and the identifier is the beneficiary to be revoked from the
    // role
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Uid scope = accessor.getUid();
      if (scope == null)
        identifierRequired();

      // create a patch operation
      final List<Operation> operation = new ArrayList<Operation>();
      operation.add(Operation.remove(Path.build().attribute(Marshaller.MEMBEROF, oracle.iam.identity.icf.scim.Filter.eq("value", identifier))));
      this.context.modifyRole(scope.getUidValue(), operation);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignTenant
  /**
   ** Taking the attributes given modify a tenant.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the tenant to modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to modify
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void assignTenant(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignTenant";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state to ensure the account is set
      final Uid scope = accessor.getUid();
      if (scope == null)
        identifierRequired();

      final List<Operation> operation = new ArrayList<Operation>();
      for (String cursor : accessor.findStringList(TenantResource.Role.SCOPE)) {
        operation.add(
          Operation.add(Path.from(TenantResource.Role.PREFIX), CollectionUtility.list(TenantResource.Role.of(TenantResource.Role.USER, Long.valueOf(identifier)).scope(cursor)))
        );
      }
      this.context.modifyTenant(scope.getUidValue(), operation);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeTenant
  /**
   ** Taking the attributes given modify a tenant.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the user to revoke.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to modify
   **                            the resource object including the
   **                            {@link Uid} attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void revokeTenant(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeTenant";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearange the attribute due the tenant is in the passed in
    // attributes and the identifier is the beneficiary to be revoked from the
    // tenant
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Uid tenant = accessor.getUid();
      if (tenant == null)
        identifierRequired();

      // create a patch operation
      final List<Operation> operation = new ArrayList<Operation>();
      for (String cursor : accessor.findStringList(TenantResource.Role.SCOPE)) {
        // build a path filter similar to
        // Operation.remove("roles[value eq 5 and scopes eq \"uid.generate\"]")
        oracle.iam.identity.icf.scim.Filter expression = oracle.iam.identity.icf.scim.Filter.and(
          oracle.iam.identity.icf.scim.Filter.eq(TenantResource.Role.VALUE, identifier)
        , oracle.iam.identity.icf.scim.Filter.eq(TenantResource.Role.SCOPE, cursor)
        );
        operation.add(Operation.remove(Path.build().attribute(TenantResource.Role.PREFIX, expression)));
      }
      this.context.modifyTenant(tenant.getUidValue(), operation);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   * Transforms the data received from the code>Service Provider</code> and
   * wrapped in the specified SCIM 2 {@link NewUserResource} <code>user</code>
   * to the {@link ConnectorObject} transfer object.
   *
   * @param user the specified SCIM 2 {@link NewUserResource} to
   * transform.
   * @param returning the {@link Set} of attribute names to be set
   * in the the {@link ConnectorObject} transfer
   * object.
   *
   * @return the transformation result wrapped in a
   * {@link ConnectorObject} transfer object.
   */
  @SuppressWarnings("unchecked")
  private ConnectorObject transform(final NewUserResource user) {
    final String method = "transform";
    trace(method, Loggable.METHOD_ENTRY);

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
    builder.setObjectClass(ObjectClass.ACCOUNT);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(user.id());
    builder.setName((String) user.userName().get(0));
    try {
      Marshaller.transfer(builder, user);
      if (user.extension() != null) {
        // get the schemas assigned to the resource
        final Collection<String> namespace = user.namespace();
        for (String cursor : namespace) {
          // skip any namespace that is not already observed
          if (!NewUserResource.SCHEMA.equalsIgnoreCase(cursor)) {
            final JsonNode node = user.extension().path(cursor);
            if (!node.isMissingNode()) {
              Marshaller.transferExtension(builder, cursor, node);
            }
          }
        }
      }
    }
    catch (IllegalArgumentException | IllegalAccessException e) {
      fatal(method, e);
    }
    trace(method, Loggable.METHOD_EXIT);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Transforms the data received from the code>Service Provider</code> and
   ** wrapped in the specified SCIM 2 {@link GroupResource} <code>role</code>
   ** to the {@link ConnectorObject} transfer object.
   **
   ** @param  role               the specified SCIM 2 {@link GroupResource} to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link GroupResource}.
   ** @param  returning          the {@link Set} of attribute names to be set
   **                            in the the {@link ConnectorObject} transfer
   **                            object.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject} transfer object.
   **                            <br>
   **                            Possible object is {@link ConnectorObject}.
   */
  private ConnectorObject transform(final GroupResource role) {
    final String method = "transform";
    trace(method, Loggable.METHOD_ENTRY);

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
    builder.setObjectClass(ObjectClass.GROUP);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(role.id());
    builder.setName(role.displayName());
    try {
      Marshaller.transfer(builder, role);
    }
    catch (IllegalArgumentException | IllegalAccessException e) {
      fatal(method, e);
    }
    trace(method, Loggable.METHOD_EXIT);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Transforms the data received from the code>Service Provider</code> and
   ** wrapped in the specified SCIM 2 {@link TenantResource} <code>tenant</code>
   ** to the {@link ConnectorObject} transfer object.
   **
   ** @param  tenant             the specified SCIM 2 {@link TenantResource} to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link TenantResource}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject} transfer object.
   **                            <br>
   **                            Possible object is {@link ConnectorObject}.
   */
  private ConnectorObject transform(final TenantResource tenant) {
    final String method = "transform";
    trace(method, Loggable.METHOD_ENTRY);

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
    builder.setObjectClass(ExtensionClass.TENANT);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(tenant.id());
    builder.setName(tenant.displayName());
    builder.addAttribute(Marshaller.buildAttribute(OperationalAttributes.ENABLE_NAME, tenant.active()).build());
    if (!CollectionUtility.empty(tenant.role())) {
      multiValuedRole(builder, ObjectClass.ACCOUNT, tenant.role());
    }

    trace(method, Loggable.METHOD_EXIT);
    return builder.build();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValuedRole
  /**
   ** Converts the specified SCIM 2 tenant role resource attribute to the
   ** transfer options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  type               the ICF object class.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  resource           the SCIM 2 tenant role resource providing the
   **                            data to obtain.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link TenantResource.Role}.
   */
  public static void multiValuedRole(final ConnectorObjectBuilder collector, final ObjectClass type, final Collection<TenantResource.Role> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> member = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application 
      for (TenantResource.Role role : resource) {
        member.add(
          new EmbeddedObjectBuilder()
          .setObjectClass(type)
          .addAttribute(
              Marshaller.buildAttribute(Uid.NAME,  role.value()).build()
            , Marshaller.buildAttribute(Name.NAME, role.display()).build()
            , Marshaller.buildAttribute("scope",   role.scope()).build()
          )
          .build()
        );
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), member);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified SCIM 2 tenant resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 2 tenant resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link TenantResource}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the attribute value to transfer.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  private static void collect(final TenantResource resource, final String name, final Object value) {
    // only NON-READ-ONLY fields here
    switch (name) {
      case "id"          : 
      case "__UID__"     : resource.id(String.class.cast(value));
                           break;
      case "__NAME__"    :
      case "displayName" : resource.displayName(String.class.cast(value));
                           break;
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationTenant
  /**
   ** Factory method to create a {@link List} of {@link Operation} driven by
   ** the specified {@link Set} of {@link Attribute}s to modify the SCIM 2
   ** tenant resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the SCIM 2 tenant resource.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   ** @param  remove             <code>true</code> if the {@link Attribute}s
   **                            provided needs to be removed.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SCIM 2 patch operations populated from the
   **                            {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @throws ServiceException   if the path to the attribute to modify becomes
   **                            invalid.
   */
  private static List<Operation> operationTenant(final Set<Attribute> attribute, final boolean remove)
    throws ServiceException {

    final List<Operation> operation = new ArrayList<Operation>();
    for (Attribute cursor : attribute) {
      final String       name  = cursor.getName();
      final List<Object> value = cursor.getValue();
      // catch the special attributes separatly and let the normal process do
      // for all other attributes
      // only NON-READ-ONLY fields here
      switch (name) {
        case "__NAME__"                 : operation.add(Operation.replace(TenantResource.UNIQUE, AttributeUtil.getSingleValue(cursor)));
                                          break;
        case TenantResource.Role.PREFIX : if (remove) {
          
                                          }
                                          else {
                                            operation.add(Operation.add(TenantResource.Role.PREFIX, cursor.getValue()));
                                          }
                                          break;
        default                         : operation.add(Operation.replace(name, String.class.cast(value.get(0))));
                                          break;
      }
    }
    return operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transferTenant
  /**
   * Factory method to create a new {@link TenantResource} instance and transfer
   * the specified {@link Set} of {@link Attribute} s to the SCIM 2 tenant
   * resource.
   *
   * @param attribute the {@link Set} of {@link Attribute} s to set
   * on the SCIM 2 tenant resource.
   * <br>
   * Allowed object is {@link Set} where each
   * elemment is of type {@link Attribute}.
   *
   * @return the SCIM 2 tenant resource to convert.
   * <br>
   * Possible object is {@link NewUserResource}.
   */
  private static TenantResource transferTenant(final Set<Attribute> attribute) {
    final TenantResource  resource  = new TenantResource();
    resource.namespace(CollectionUtility.set("urn:ietf:params:scim:schemas:extension:p20:1.0:Tenant"));
    for (Attribute cursor : attribute) {
      final String name = cursor.getName();
      if (!CollectionUtility.empty(cursor.getValue())) {
        // verify if an extension attribute is mentioned
        // e.g. urn:ietf:params:scim:schemas:extension:enterprise:2.0:User
        if (name.contains(":")) {
          Marshaller.extend(resource, name, AttributeUtil.getSingleValue(cursor));
        }
        else {
          collect(resource, name, AttributeUtil.getSingleValue(cursor));
        }
      }
    }
    return resource;
  }
}