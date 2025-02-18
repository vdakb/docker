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
    Subsystem   :   Generic SCIM 2 Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.scim.v2;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.InvocationTargetException;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.spi.operations.SPIOperation;
import org.identityconnectors.framework.spi.operations.ResolveUsernameOp;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemSchema;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceMessage;
import oracle.iam.identity.icf.rest.ServiceConnector;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.ServiceConfiguration;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.scim.Path;

import oracle.iam.identity.icf.scim.schema.User;
import oracle.iam.identity.icf.scim.schema.Entity;

import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.v2.schema.Marshaller;
import oracle.iam.identity.icf.scim.v2.schema.UserResource;
import oracle.iam.identity.icf.scim.v2.schema.GroupResource;
import oracle.iam.identity.icf.scim.v2.schema.SchemaFactory;
import oracle.iam.identity.icf.scim.v2.schema.SchemaResource;

import oracle.iam.identity.icf.scim.v2.request.Operation;

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
@ConnectorClass(configurationClass=ServiceConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.scim.v2.Main.properties")
public class Main extends    ServiceConnector
                  implements ResolveUsernameOp {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String CATEGORY = "JCS.CONNECTOR.SCIM.V2";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private Context             context  = null;

  private Schema              schema   = null;

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Schema discovery is performed by query the Service Provider.
   */
  class Server implements SystemSchema {

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
     ** @param  clazz            the connector for which the schema are built.
     **                          <br>
     **                          Allowed object is {@link Connector}.
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

      final ListResponse<SchemaResource> response = Main.this.context.schemas();
      return schema(response);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Static
  // ~~~~~ ~~~~~~
  /**
   ** Schema is performed by filling up with static data.
   */
  class Static implements SystemSchema {

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
      return schema(CollectionUtility.list(SchemaFactory.schema(UserResource.class), SchemaFactory.schema(GroupResource.class)));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> SCIM 2 conector that allows use as
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
      this.context.resourceTypes();
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
        this.schema = this.context.endpoint().fetchSchema() ? new Server().build() : new Static().build();
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
          final ListResponse<UserResource> response = this.context.searchAccount(index, control.limit, criteria).invoke(UserResource.class);
          batch = response.items();
          if (batch == 0) {
            info(ServiceBundle.string(ServiceMessage.NOTHING_TO_CHANGE));
            break;
          }
          for (UserResource user : response) {
            handler.handle(transform(user));
          }
          index += control.limit;
        } while (batch == control.limit);
      }
      else if (ObjectClass.GROUP.equals(objectClass)) {
        do {
          // .emit(returning)
          final ListResponse<GroupResource> response = this.context.searchGroup(index, control.limit, criteria).invoke(GroupResource.class);
          batch = response.items();
          if (batch == 0) {
            info(ServiceBundle.string(ServiceMessage.NOTHING_TO_CHANGE));
            break;
          }
          for (GroupResource group : response) {
            handler.handle(transform(group));
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
    String identifier = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        identifier = this.context.resolveAccount(name);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        identifier = this.context.resolveGroup(name);
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
    return new Uid(identifier);
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
        identifier = createGroup(attribute);
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
        this.context.deleteGroup(identifier);
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

    Entity resource = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        resource = this.context.modifyAccount(uid.getUidValue(), Marshaller.operationUser(replace));
      }
      else if (ObjectClass.GROUP.equals(type)) {
        resource = this.context.modifyGroup(uid.getUidValue(), Marshaller.operationGroup(replace, false));
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
          final List<Object>   value   = cursor.getValue();
          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            assignGroup(identifier, subject.getAttributes());
          }
          else {
            unsupportedType(subject.getObjectClass(), method);
          }
        }
      }
      else if (ObjectClass.GROUP.equals(type)) {
        result = patchGroup(identifier, addition);
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
    return new Uid("1111111111");
    /*
    if (resourceEndpoints.containsKey(oclass.getObjectClassValue())) {
      attrSet = GenericSCIMUtil.handleAttrValues(attrSet, (Map)namedAttributes.get(oclass.getObjectClassValue()), (List)dateAttributes.get(oclass.getObjectClassValue()), config);
      if (config.getScimVersion() <= 3) {
        GenericSCIMUtil.manageAddRemoveAttrV3Below(schema, resourceEndpoints, attrToOClassMapping, attrSet, GenericSCIMConstants.UpdateOperationType.DELETE, config, baseURI, (Map)namedAttributes.get(oclass.getObjectClassValue()), oclass, connection, uid, parser, (String)coreSchemas.get(oclass.getObjectClassValue()), (Map)extAttrsAndSchemas.get(oclass.getObjectClassValue()), customPayloadMap, parserConfigParamsMap, relURLsMap, httpOperationTypesMap);
      }
      else {
        GenericSCIMUtil.manageAddRemoveAttrV3Above(schema, resourceEndpoints, attrToOClassMapping, attrSet, GenericSCIMConstants.UpdateOperationType.DELETE, config, baseURI, (Map)namedAttributes.get(oclass.getObjectClassValue()), oclass, connection, uid, parser, (String)coreSchemas.get(oclass.getObjectClassValue()), (Map)extAttrsAndSchemas.get(oclass.getObjectClassValue()), customPayloadMap, parserConfigParamsMap, relURLsMap, httpOperationTypesMap);
      }
    }
    return uid;
    */
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  public static Schema schema(final Iterable<SchemaResource> collection) {
    final SchemaBuilder                   builder  = new SchemaBuilder(Main.class);
    final Map<String, Set<AttributeInfo>> object   = new HashMap<>();
    for (SchemaResource cursor : collection) {
      // determine the schema in scope
      // it's assumed that any extension ends with a discrimator like User or
      // Group to be able to detect to which object class the schema belongs
      // skip all scheme that does not belong to provisionable entities like
      // the schema itself or resource types
      if (StringUtility.endsWithIgnoreCase(cursor.id(), "user"))
        transferSchema(builder, ObjectClass.ACCOUNT_NAME, cursor, object);
      else if (StringUtility.endsWithIgnoreCase(cursor.id(), "group"))
        transferSchema(builder, ObjectClass.GROUP_NAME, cursor, object);
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
      for (Class<? extends SPIOperation> operation : OPERATION)
        builder.addSupportedObjectClass(operation, cursor);
    }
    return builder.build();
  }

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
    UserResource resource = null;
    try {
      // marshall a SCIM user resource transfered to the Service Provider
      resource = this.context.createAccount(Marshaller.transferUser(UserResource.class, attribute));
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
  // Method:   createGroup
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
  protected Uid createGroup(final Set<Attribute> attribute)
    throws SystemException
    ,      ConnectorException {

    // prevent bogus input
    if (attribute.isEmpty())
      propagate(SystemError.OBJECT_VALUES_REQUIRED);

    final String method = "createGroup";
    trace(method, Loggable.METHOD_ENTRY);

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    Attribute groupname = accessor.find(Marshaller.GROUPNAME);
    if (groupname == null) {
      groupname = accessor.find(Name.NAME);
    }
    // prevent bogus state
    if (groupname == null) {
      trace(method, Loggable.METHOD_EXIT);
      propagate(SystemError.NAME_IDENTIFIER_REQUIRED, Marshaller.GROUPNAME);
    }

    // execute operation
    try {
      // marshall a SCIM user resource transfered to the Service Provider
      final GroupResource resource = this.context.createGroup(Marshaller.transferGroup(GroupResource.class, attribute));
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

    final UserResource resource = this.context.modifyAccount(identifier, Marshaller.operationUser(attribute));
    return new Uid(resource.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchGroup
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
  protected Uid patchGroup(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final GroupResource resource = this.context.modifyGroup(identifier, Marshaller.operationGroup(attribute, false));
    return new Uid(resource.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignGroup
  /**
   ** Taking the attributes given modify a group.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the group to modify.
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
  protected void assignGroup(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignGroup";
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
      operation.add(Operation.add(Path.from(Marshaller.MEMBEROF), CollectionUtility.list(identifier)));
      this.context.modifyGroup(scope.getUidValue(), operation);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeGroup
  /**
   ** Taking the attributes given modify a group.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the group to modify.
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
  protected void revokeGroup(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeGroup";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearange the attribute due the group is in the passed in
    // attributes and the identifier is the beneficiary to be revoked from the
    // group
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Uid scope = accessor.getUid();
      if (scope == null)
        identifierRequired();

      // create a patch operation
      final List<Operation> operation = new ArrayList<Operation>();
      operation.add(Operation.remove(Path.build().attribute(Marshaller.MEMBEROF, oracle.iam.identity.icf.scim.Filter.eq("value", identifier))));
      this.context.modifyGroup(scope.getUidValue(), operation);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Transforms the data received from the code>Service Provider</code> and
   ** wrapped in the specified SCIM 2 {@link User} <code>user</code>
   ** to the {@link ConnectorObject} transfer object.
   **
   ** @param  user               the specified SCIM 2 {@link User} to
   **                            transform.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject} transfer object.
   */
  private ConnectorObject transform(final User user) {
    final String method = "transform";
    trace(method, Loggable.METHOD_ENTRY);

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
    builder.setObjectClass(ObjectClass.ACCOUNT);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(user.id());
    builder.setName(user.userName());
    try {
      Marshaller.transfer(builder, user);
      if (user.extension() != null) {
        // get the schemas assigned to the resource
        final Collection<String> namespace = user.namespace();
        for (String cursor : namespace) {
          // skip any namespace that is not already observed
          if (!UserResource.SCHEMA.equalsIgnoreCase(cursor)) {
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
   ** wrapped in the specified SCIM 2 {@link GroupResource} <code>group</code>
   ** to the {@link ConnectorObject} transfer object.
   **
   ** @param  group              the specified SCIM 2 {@link GroupResource} to
   **                            transform.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject} transfer object.
   */
  private ConnectorObject transform(final GroupResource group) {
    final String method = "transform";
    trace(method, Loggable.METHOD_ENTRY);

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
    builder.setObjectClass(ObjectClass.GROUP);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(group.id());
    builder.setName(group.displayName());
    try {
      Marshaller.transfer(builder, group);
    }
    catch (IllegalArgumentException | IllegalAccessException e) {
      fatal(method, e);
    }
    trace(method, Loggable.METHOD_EXIT);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transferSchema
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
  private static void transferSchema(final SchemaBuilder builder, final String type, final SchemaResource resource, final Map<String, Set<AttributeInfo>> collector) {
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
}