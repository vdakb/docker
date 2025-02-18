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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import java.net.URI;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import javax.ws.rs.core.UriBuilder;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.spi.operations.SPIOperation;
import org.identityconnectors.framework.spi.operations.ResolveUsernameOp;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceConnector;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.domain.ListResult;

import oracle.iam.identity.icf.scim.response.ListResponse;

import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.v1.request.Operation;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;
import oracle.iam.identity.icf.connector.pcf.rest.domain.Payload;

import oracle.iam.identity.icf.connector.pcf.rest.request.Search;

import oracle.iam.identity.icf.connector.pcf.scim.schema.UserResource;
import oracle.iam.identity.icf.connector.pcf.scim.schema.GroupResource;
import oracle.iam.identity.icf.connector.pcf.scim.schema.SchemaFactory;
import oracle.iam.identity.icf.connector.pcf.scim.schema.SchemaResource;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link ServiceConnector} for a Pivotal Cloud Foundry application system.
 ** <p>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 ** <p>
 ** In addition to the standard API's this connector supports resolving an
 ** object by its name and returns the uid of the object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=Config.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.pcf.Main.properties")
public class Main extends    ServiceConnector
                  implements ResolveUsernameOp {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private Config  config  = null;

  private Schema  schema  = null;

  private boolean groups  = false;
  private boolean tenants = false;
  private boolean spaces  = false;
  private Service client  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> Pivotal Cloud Foundry Connector
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Main() {
    // ensure inheritance
    super(Config.CATEGORY);
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
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   **
   ** @throws ConnectorException if the authentication/authorization request
   **                            implemented in the {@link ServiceClient}
   **                            constructor fails.
   */
  @Override
  public void init(final Configuration configuration)
    throws ConnectorException {

    final String method = "init";
    trace(method, Loggable.METHOD_ENTRY);
    this.config = (Config)configuration;
    try {
      this.client = Service.build(this.config.endpoint());
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
    this.config = null;
    this.client = null;
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
    trace(method, Loggable.METHOD_EXIT);
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
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  @Override
  public Schema schema() {
    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    if (this.schema == null)
      this.schema = schema(CollectionUtility.list(SchemaFactory.schema(UserResource.class), SchemaFactory.schema(GroupResource.class)));

    trace(method, Loggable.METHOD_EXIT);
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveUsername (ResolveUsernameOp)
  /**
   ** Implements the operation interface to resolve objects from the target
   ** resource.
   ** <p>
   ** Resolve an object to its {@link Uid} based on its unique name.
   ** <br>
   ** This is a companion to the simple authentication. The difference is that
   ** this method does not have a password parameter and does not try to
   ** authenticate the credentials; instead, it returns the {@link Uid}
   ** corresponding to the username.
   ** <p>
   ** If the username validation fails, the an type of RuntimeException either
   ** IllegalArgumentException or if a native exception is available and if its
   ** of type RuntimeException simple throw it.
   ** <br>
   ** If the native exception is not a RuntimeException wrap it in one and throw
   ** it. This will provide the most detail for logging problem and failed
   ** attempts.
   **
   ** @param  type               the type of resource to resolve.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  name               the unique name that specifies the object to
   **                            resolve.
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
  public Uid resolveUsername(final ObjectClass type, final String name, final OperationOptions option)
    throws ConnectorException {

    final String method = "resolveUsername";
    trace(method, Loggable.METHOD_ENTRY);

    String indentifier = null;
    try {
      if (ObjectClass.ACCOUNT.equals(type)) {
        indentifier = resolveAccount(name);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        indentifier = resolveGroup(name);
      }
      else if (RestMarshaller.TENANT.equals(type)) {
        indentifier = resolveTenant(name);
      }
      else if (RestMarshaller.SPACE.equals(type)) {
        indentifier = resolveSpace(name);
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
    return new Uid(indentifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilterTranslator (SearchOp)
  /**
   ** Creates a filter translator that will translate a specified
   ** <code>filter</code> into one or more native queries.
   ** <br>
   ** Each of these native queries will be passed subsequently into
   ** <code>#executeQuery(ObjectClass, Filter, ResultsHandler, OperationOptions)</code>.
   ** <p>
   ** <b>Attention</b>:
   ** <br>
   ** This method <b>must</b> return a non-<code>null</code> instance of a
   ** {@link FilterTranslator} otherise the communication to the
   ** <code>Connector Server</code> breaks.
   ** <br>
   ** The filter build by the translator itself must also <b>never</b>
   ** <code>null</code>.
   **
   ** @param  type                the {@link ObjectClass} for the search.
   **                             Will never be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link ObjectClass}.
   ** @param  option              additional options that impact the way this
   **                             operation is run.
   **                             <br>
   **                             If the caller passes <code>null</code>, the
   **                             framework will convert this into an empty set
   **                             of options, so SPI need not worry about this
   **                             ever being <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link OperationOptions}.
   **
   ** @return                     a filter translator.
   **                             This must not be <code>null</code>.
   **                             <br>
   **                             A <code>null</code> return value will cause
   **                             the API (SearchApiOp) to throw
   **                             <code>NullPointerException</code>.
   **                             <br>
   **                             Possible object is {@link FilterTranslator}.
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
          List<String> criteria = null;;
          try {
            criteria = ScimTranslator.build().translate(filter);
          }
          catch (ServiceException e) {
            // report the exception in the log spooled for further investigation
            fatal(method, e);
            // send back the exception occured
            propagate(e);
          }
          return criteria;
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
   ** @param  type               the type of resource to search.
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
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public void executeQuery(final ObjectClass type, final String criteria, final ResultsHandler handler, final OperationOptions option)
    throws ConnectorException {

    final String method = "executeQuery";
    trace(method, Loggable.METHOD_ENTRY);
    final Set<String> filter = new HashSet<>();
    // add attributes in the set that always needs to be returned
    if (ObjectClass.ACCOUNT.equals(type)) {
      filter.add(ScimMarshaller.IDENTIFIER);
      filter.add(ScimMarshaller.USERNAME);
      filter.add(ScimMarshaller.STATUS);
    }
    else if (ObjectClass.GROUP.equals(type)) {
      filter.add(ScimMarshaller.IDENTIFIER);
      filter.add(ScimMarshaller.GROUPNAME);
    }
    else if (RestMarshaller.TENANT.equals(type)) {
      filter.add(RestMarshaller.IDENTIFIER);
      filter.add(RestMarshaller.NAME);
    }
    else if (RestMarshaller.SPACE.equals(type)) {
      filter.add(RestMarshaller.IDENTIFIER);
      filter.add(RestMarshaller.NAME);
    }

    if (option.getAttributesToGet() != null) {
      for (String cursor : option.getAttributesToGet()) {
        // handle special attributes appropriatly
        if (Uid.NAME.equals(cursor) || Name.NAME.equals(cursor) || OperationalAttributes.ENABLE_NAME.equals(cursor) || OperationalAttributes.PASSWORD_NAME.equals(cursor))
          continue;
        // go an extra mile to exclude the embedded object like groups, tenants
        // or spaces from the query
        else if (ObjectClass.GROUP_NAME.equals(cursor)) {
          this.groups = true;
        }
        else if (RestMarshaller.TENANT_NAME.equals(cursor)) {
          this.tenants = true;
        }
        else if (RestMarshaller.SPACE_NAME.equals(cursor)) {
          this.spaces = true;
        }
        else {
          filter.add(cursor);
        }
      }
    }
    // filtering attributes to return
    // SCIM defines e.g. name.familyName to return the complex value part name
    // if a sub attribute is requested
    // PCF is returning the attribute as it is mentioned in the request with a
    // null value assigned to it
    // To circumvent this issue we need to implode any attribute request to
    // the top level complex attributes and filter out after the response was
    // parsed the request attributes
    final Set<String> toplevel = new HashSet<>();
    for (String cursor : filter)
      // if no separator is detected add the attribute name as it is
      // if there is one put only the top level part in the request
      toplevel.add(cursor.split("\\.")[0]);

    // initialize the operations control configuration
    final OperationContext control = OperationContext.build(option);
    try {
      if (ObjectClass.ACCOUNT.equals(type)) {
        searchAccount(control.limit, toplevel, filter, criteria, handler);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        searchGroup(control.limit, toplevel, filter, criteria, handler);
      }
      else if (RestMarshaller.TENANT.equals(type)) {
        searchTenant(control.limit, filter, criteria, handler);
      }
      else if (RestMarshaller.SPACE.equals(type)) {
        searchSpace(control.limit, filter, criteria, handler);
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
   **
   ** @param  type               the type of resource to create.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  option             additional options that affect the way this
   **                            operation is run.
   **                            If the caller passes <code>null</code>, the
   **                            framework will convert this into an empty set
   **                            of options, so an implementation need not guard
   **                            against this being <code>null</code>.
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
      else if (RestMarshaller.TENANT.equals(type)) {
        identifier = createTenant(attribute);
      }
      else if (RestMarshaller.SPACE.equals(type)) {
        identifier = createSpace(attribute);
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
        deleteAccount(identifier);
      else if (ObjectClass.GROUP.equals(type))
        deleteGroup(identifier);
      else if (RestMarshaller.TENANT.equals(type))
        deleteTenant(identifier);
      else if (RestMarshaller.SPACE.equals(type))
        deleteSpace(identifier);
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
    final String identifier = uid.getUidValue();
    if (StringUtility.empty(identifier))
      identifierRequired();

    final String method = "update";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(replace));
    }

    Uid result = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        result = modifyAccount(identifier, replace, false);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        result = modifyGroup(identifier, replace, false);
      }
      else if (RestMarshaller.TENANT.equals(type)) {
        result = modifyTenant(identifier, replace);
      }
      else if (RestMarshaller.SPACE.equals(type)) {
        result = modifySpace(identifier, replace);
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
    return result;
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

    try {
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : addition) {
          final List<Object>   value   = cursor.getValue();
          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            assignGroup(identifier, subject.getAttributes());
          }
          else if (RestMarshaller.TENANT.equals(subject.getObjectClass())) {
            assignTenant(identifier, subject.getAttributes());
          }
          else if (RestMarshaller.SPACE.equals(subject.getObjectClass())) {
            assignSpace(identifier, subject.getAttributes());
          }
          else {
            unsupportedType(subject.getObjectClass(), method);
          }
        }
      }
      else if (ObjectClass.GROUP.equals(type)) {
        modifyGroup(identifier, addition, false);
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
    return uid;
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
      valuesRequired();

    // prevent bogus input
    final String identifier = uid.getUidValue();
    if (StringUtility.empty(identifier))
      identifierRequired();

    final String method = "removeAttributeValues";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(remove));
    }

    Uid result = uid;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : remove) {
          final List<Object> value = cursor.getValue();
          if (value != null && value.size() > 1)
            throw ServiceException.tooMany(cursor.getName());

          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            revokeGroup(identifier, subject.getAttributes());
          }
          else if (RestMarshaller.TENANT.equals(subject.getObjectClass())) {
            revokeTenant(identifier, subject.getAttributes());
          }
          else if (RestMarshaller.SPACE.equals(subject.getObjectClass())) {
            revokeSpace(identifier, subject.getAttributes());
          }
          else {
            unsupportedType(type, method);
          }
        }
      }
      else if (ObjectClass.GROUP.equals(type)) {
        result = modifyGroup(identifier, remove, true);
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
    return result;
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
    final SchemaBuilder                   builder = new SchemaBuilder(Main.class);
    final Map<String, Set<AttributeInfo>> object  = new HashMap<>();
    for (SchemaResource cursor : collection) {
      // the attribute collector per schema resource
      final Set<AttributeInfo> collector = new HashSet<>();
      // determine the schema in scope
      // it's assumed that any extension ends with a discrimator like User or
      // Group to be able to detect to which object class the schema belongs
      String schema = cursor.name();
      // map the default resource object to the standard object classes
      if (StringUtility.equalIgnoreCase(cursor.name(), "user"))
        schema = ObjectClass.ACCOUNT_NAME;
      else if (StringUtility.equalIgnoreCase(cursor.name(), "group"))
        schema = ObjectClass.GROUP_NAME;

      collector.addAll(attributeSchema(schema, cursor, builder));
      if (object.containsKey(schema)) {
        final Set<AttributeInfo> current = object.get(schema);
        current.addAll(collector);
      }
      else {
        object.put(schema, collector);
      }
    }
    // how stupid ICF is build shows the code below
    // instead of providing a fine grained api to take control how a certain
    // object class is presented in the populated schema you need to add the
    // object class generated completely at first. The ugly frameork will add
    // than all possible operation to the object class that can it find, with
    // the result that we need to remove all operation later and allow only
    // those that the connector is realy supporting
    // what kind of bull shit is that, to keep the garbage collector busy only
    final List<ObjectClassInfo> objectLatch = new ArrayList<ObjectClassInfo>();
    for (Map.Entry<String, Set<AttributeInfo>> cursor : object.entrySet() ) {
      final ObjectClassInfoBuilder objectBuilder = new ObjectClassInfoBuilder();
      objectBuilder.setType(cursor.getKey());
      objectBuilder.addAllAttributeInfo(cursor.getValue());
      final ObjectClassInfo objectClass = objectBuilder.build();
      objectLatch.add(objectClass);
      builder.defineObjectClass(objectClass);
    }
    builder.clearSupportedObjectClassesByOperation();
    for (ObjectClassInfo cursor : objectLatch) {
      for (Class<? extends SPIOperation> operation : OPERATION)
        builder.addSupportedObjectClass(operation, cursor);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  attribute          the names of resource attributes to return.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObject} transfer
   **                            object.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  criteria           the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void searchAccount(final int count, final Set<String> attribute, final Set<String> filter, final String criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchAccount";
    trace(method, Loggable.METHOD_ENTRY);

    int batch = 0;
    int index = 1;
    try {
      final ScimContext scim =  ScimContext.build(this.client);
      final RestContext rest =  RestContext.build(this.client);
      do {
        final ListResponse<UserResource> response = scim.searchAccount().page(index, count).emit(attribute).filter(criteria).invoke(UserResource.class);
        batch = response.items();
        if (batch == 0 && index == 1) {
          break;
        }
        for (UserResource user : response) {
          try {
            Map<String, List<Pair<String, List<String>>>> cloudPermitted = null;
            if (this.tenants || this.spaces)
              cloudPermitted = rest.lookupPermitted(user.id());

            // build connector object and submit to handler
            final ConnectorObject payload = ScimMarshaller.connectorObject(
              user
            , filter
            , this.groups  ? scim.groupsForAccount(user.id()) : null
            , this.tenants ? cloudPermitted.get(RestMarshaller.TENANT_NAME) : null
            , this.spaces  ? cloudPermitted.get(RestMarshaller.SPACE_NAME)  : null
            );
            if (!handler.handle(payload))
              break;
          }
          catch (RuntimeException e) {
            throw ServiceException.abort(e.getLocalizedMessage());
          }
          catch (Throwable t) {
            throw ServiceException.general(t.getLocalizedMessage());
          }
        }
        index += count;
      } while (batch == count);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveAccount
  /**
   ** Implements the operation interface to resolve users to its {@link Uid}
   ** based on its unique name from the target resource.
   ** <p>
   ** This is a companion to the simple authentication. The difference is that
   ** this method does not have a password parameter and does not try to
   ** authenticate the credentials; instead, it returns the {@link Uid}
   ** corresponding to the username.
   **
   ** @param  name               the unique name that specifies the account to
   **                            resolve.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved SCIM user resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected String resolveAccount(final String name)
    throws SystemException {

    final String method = "resolveAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final String uid = ScimContext.build(this.client).resolveAccount(name);
      if (uid == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, ScimContext.ENDPOINT_USERS, ScimMarshaller.USERNAME, name));
      return uid;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccount
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass}) and create an account and its {@link Uid}.
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
   */
  protected Uid createAccount(final Set<Attribute> attribute)
    throws SystemException {

    final String method = "createAccount";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      Attribute verify = accessor.find(ScimMarshaller.USERNAME);
      if (verify == null) {
        verify = accessor.getName();
      }
      // prevent bogus state
      if (verify == null) {
        nameRequired(ScimMarshaller.USERNAME);
      }

      // marshall a SCIM user resource transfered to the Service Provider
      // execute operation
      final UserResource resource = ScimContext.build(this.client).createAccount(ScimMarshaller.inboundUser(attribute));
      return new Uid(resource.id());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyAccount
  /**
   ** Taking the attributes given modify an account and return its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the account to modify
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
   ** @param  remove             <code>true</code> if the {@link Attribute}s
   **                            provided needs to be removed.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
  protected Uid modifyAccount(final String identifier, final Set<Attribute> attribute, final boolean remove)
    throws SystemException {

    final String method = "modifyAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final UserResource resource = ScimContext.build(this.client).modifyAccount(identifier, ScimMarshaller.inboundUser(attribute, remove));
      return new Uid(resource.id());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteAccount
  /**
   ** Implements the operation interface to delete an account from the Service
   ** Provider.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the unique identifier that specifies the
   **                            account to delete.
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the status of the HTTP response is greater
   **                            than or equal to 300 and <code>type</code> does
   **                            not represent the appropriate response type.
   **                            Also thrown if the client handler fails to
   **                            process the request or response.
   */
  protected void deleteAccount(final String identifier)
    throws SystemException {

    final String method = "deleteAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      ScimContext.build(this.client).deleteAccount(identifier);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGroup
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  attribute          the names of resource attributes to return.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObject} transfer
   **                            object.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  criteria           the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void searchGroup(final int count, final Set<String> attribute, final Set<String> filter, final String criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchGroup";
    trace(method, Loggable.METHOD_ENTRY);

    int batch = 0;
    int index = 1;
    try {
      do {
        final ListResponse<GroupResource> response = ScimContext.build(this.client).searchGroup().emit(attribute).page(index, count).filter(criteria).invoke(GroupResource.class);
        batch = response.total();
        if (batch == 0) {
          break;
        }
        else {
          for (GroupResource group : response) {
            try {
              if (!handler.handle(ScimMarshaller.connectorObject(group, filter)))
                break;
            }
            catch (RuntimeException e) {
              throw ServiceException.abort(e.getLocalizedMessage());
            }
            catch (Throwable t) {
              throw ServiceException.general(t.getLocalizedMessage());
            }
          }
          index += count;
        }
      } while (batch == count);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveGroup
  /**
   ** Implements the operation interface to resolve groups to its {@link Uid}
   ** based on its unique name from the target resource.
   **
   ** @param  name               the unique name that specifies the group to
   **                            resolve.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved SCIM group resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected String resolveGroup(final String name)
    throws SystemException {

    final String method = "resolveGroup";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final String uid = ScimContext.build(this.client).resolveGroup(name);
      if (uid == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, ScimContext.ENDPOINT_GROUPS, ScimMarshaller.GROUPNAME, name));
      return uid;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createGroup
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass}) and create a group and its {@link Uid}.
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
   */
  protected Uid createGroup(final Set<Attribute> attribute)
    throws SystemException {

    final String method = "createGroup";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      Attribute verify = accessor.find(ScimMarshaller.GROUPNAME);
      if (verify == null) {
        verify = accessor.find(Name.NAME);
      }
      // prevent bogus state
      if (verify == null)
        nameRequired(ScimMarshaller.GROUPNAME);

      // marshall a SCIM group resource for transfer to the Service Provider and
      // perform operation
      final GroupResource resource = ScimContext.build(this.client).createGroup(ScimMarshaller.inboundGroup(attribute));
      return new Uid(resource.id());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyGroup
  /**
   ** Taking the attributes given modify a group and return its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
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
   ** @param  remove             <code>true</code> if the {@link Attribute}s
   **                            provided needs to be removed.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the unique id for the object that is modified.
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
  protected Uid modifyGroup(final String identifier, final Set<Attribute> attribute, final boolean remove)
    throws SystemException {

    final String method = "modifyGroup";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final GroupResource resource = ScimContext.build(this.client).modifyGroup(identifier, ScimMarshaller.inboundGroup(attribute, remove));
      return new Uid(resource.id());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteGroup
  /**
   ** Implements the operation interface to delete a group from the Service
   ** Provider.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the unique identifier that specifies the
   **                            group to delete.
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the status of the HTTP response is greater
   **                            than or equal to 300 and <code>type</code> does
   **                            not represent the appropriate response type.
   **                            Also thrown if the client handler fails to
   **                            process the request or response.
   */
  protected void deleteGroup(final String identifier)
    throws SystemException {

    final String method = "deleteGroup";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      ScimContext.build(this.client).deleteGroup(identifier);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
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
      final Attribute scope = accessor.find(Uid.NAME);
      if (scope == null)
        identifierRequired();

      final List<Operation> operation = new ArrayList<Operation>();
      operation.add(Operation.replace(ScimMarshaller.MEMBER, CollectionUtility.list(identifier)));
      ScimContext.build(this.client).modifyGroup(AttributeUtil.getStringValue(scope), operation);
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
      final Attribute scope = accessor.find(Uid.NAME);
      if (scope == null)
        identifierRequired();

      // create a patch operation
      final List<Operation> operation = new ArrayList<Operation>();
      operation.add(Operation.replace(ScimMarshaller.MEMBER, CollectionUtility.list(identifier)));
      ScimContext.build(this.client).modifyGroup(AttributeUtil.getStringValue(scope), operation);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTenant
  /**
   ** Implements the operation interface to lookup a known tenant by its unique
   ** identifier from the Cloud Controller Service Provider.
   **
   ** @param  identifier         the unique identifier of the tenant to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully looked up Cloud Controller
   **                            tenant resource or <code>null</code> if no
   **                            tenant resource is mapped to the given
   **                            identifier at the
   **                            Service Provider.
   **                            <br>
   **                            Possible object is {@link Result.Tenant}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected Result.Tenant lookupTenant(final String identifier)
    throws SystemException {

    return lookupTenant(UriBuilder.fromUri(RestContext.ENDPOINT_TENANT).path(identifier).build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTenant
  /**
   ** Implements the operation interface to lookup a known tenant by its
   ** {@link URI} from the Cloud Controller Service Provider.
   **
   ** @param  uri                the {@link URI} of the tenant resource to
   **                            retrieve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the successfully looked up Cloud Controller
   **                            space resource or <code>null</code> if no space
   **                            resource is mapped to the given {@link URI} at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link Result.Tenant}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected Result.Tenant lookupTenant(final URI uri)
    throws SystemException {

    return RestContext.build(this.client).lookupTenant(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveTenant
  /**
   ** Implements the operation interface to resolve tenants to its {@link Uid}
   ** based on its unique name from the Cloud Controller.
   **
   ** @param  name               the unique name that identifies the tenant to
   **                            resolve.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved Cloud Controller tenant resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the
   **                            <code>Cloud Controller</code>..
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected String resolveTenant(final String name)
    throws SystemException {

    final String method = "resolveTenant";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final String uid = RestContext.build(this.client).resolveTenant(name);
      if (uid == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, RestContext.ENDPOINT_TENANT, RestMarshaller.NAME, name));
      return uid;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchTenant
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObject} transfer
   **                            object.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  criteria           the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  protected void searchTenant(final int count, final Set<String> filter, final String criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchTenant";
    trace(method, Loggable.METHOD_ENTRY);

    int batch = 0;
    int index = 1;
    final Search search = RestContext.build(this.client).searchTenant();
    try {
      do {
        final ListResult<Result.Tenant> response = search.page(index, count).filter(criteria).invoke(Result.Tenant.class);
        batch = response.total();
        if (batch == 0)
          break;

        for (Result.Tenant tenant : response) {
          try {
            // build connector object and submit to handler
            if (!handler.handle(RestMarshaller.buildTenant(tenant, filter)))
              break;
          }
          catch (RuntimeException e) {
            throw ServiceException.abort(e.getLocalizedMessage());
          }
          catch (Throwable t) {
            throw ServiceException.general(t.getLocalizedMessage());
          }
        }
      // position batch window on the next page
      } while (index++ * count < batch);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTenant
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass}) and create a tenant and its {@link Uid}.
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
   */
  protected Uid createTenant(final Set<Attribute> attribute)
    throws SystemException {

    final String method = "createTenant";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      Attribute verify = accessor.find(Payload.NAME);
      if (verify == null) {
        verify = accessor.getName();
      }
      // prevent bogus state
      if (verify == null) {
        nameRequired(Payload.NAME);
      }

      // execute operation
      final Result.Tenant resource = RestContext.build(this.client).createTenant(RestMarshaller.buildTenant(attribute));
      return new Uid(resource.metadata().guid());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyTenant
  /**
   ** Taking the attributes given modify a tenant and return its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the tenant to modify
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
   ** @return                    the unique id for the object that is modified.
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
  protected Uid modifyTenant(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "modifyTenant";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final Result.Tenant resource = RestContext.build(this.client).modifyTenant(identifier, RestMarshaller.buildTenant(attribute));
      return new Uid(resource.guid());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteTenant
  /**
   ** Implements the operation interface to delete a tenant from the Service
   ** Provider.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the unique identifier that specifies the
   **                            tenant to delete.
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the status of the HTTP response is greater
   **                            than or equal to 300 and <code>type</code> does
   **                            not represent the appropriate response type.
   **                            Also thrown if the client handler fails to
   **                            process the request or response.
   */
  protected void deleteTenant(final String identifier)
    throws SystemException {

    final String method = "deleteTenant";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      RestContext.build(this.client).deleteTenant(identifier);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignTenant
  /**
   ** Taking the attributes given assign role based access on a Cloud Controller
   ** tenant object.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the Cloud Controller tenant
   **                            to modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to assign
   **                            the resource object.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  protected Uid assignTenant(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignTenant";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Attribute tenant = accessor.find(Uid.NAME);
      if (tenant == null)
        identifierRequired();

      // prevent bogus state
      final Attribute role = accessor.find(Name.NAME);
      if (role == null)
        nameRequired(Name.NAME);

      final Result.User resource = RestContext.build(this.client).assign(AttributeUtil.getStringValue(tenant), identifier, AttributeUtil.getStringValue(role));
      return new Uid(resource.metadata().guid());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeTenant
  /**
   ** Taking the attributes given revokes role based access from a Cloud
   ** Controller tenant object.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the Cloud Controller tenant
   **                            to modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to assign
   **                            the resource object.
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
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Attribute tenant = accessor.find(Uid.NAME);
      if (tenant == null)
        identifierRequired();

      // prevent bogus state
      final Attribute role = accessor.find(Name.NAME);
      if (role == null)
        nameRequired(Name.NAME);

      RestContext.build(this.client).revoke(AttributeUtil.getStringValue(tenant), identifier, AttributeUtil.getStringValue(role));
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSpace
  /**
   ** Implements the operation interface to lookup a known space by its unique
   ** identifier from the Cloud Controller Service Provider.
   **
   ** @param  identifier         the unique identifier of the space to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully looked up Cloud Controller
   **                            space resource or <code>null</code> if no space
   **                            resource is mapped to the given identifier at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link Result.Space}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected Result.Space lookupSpace(final String identifier)
    throws SystemException {

    return lookupSpace(UriBuilder.fromUri(RestContext.ENDPOINT_SPACES).path(identifier).build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSpace
  /**
   ** Implements the operation interface to lookup a known space by its
   ** {@link URI} from the Cloud Controller Service Provider.
   **
   ** @param  uri                the {@link URI} of the space resource to
   **                            retrieve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the successfully looked up Cloud Controller
   **                            space resource or <code>null</code> if no space
   **                            resource is mapped to the given {@link URI} at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link Result.Space}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected Result.Space lookupSpace(final URI uri)
    throws SystemException {

    return RestContext.build(this.client).lookupSpace(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSpace
  /**
   ** Implements the operation interface to resolve spaces to its {@link Uid}
   ** based on its unique name from the Cloud Controller.
   **
   ** @param  name               the unique name that identifies the space to
   **                            resolve.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved Cloud Controller space resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the
   **                            <code>Cloud Controller</code>..
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected String resolveSpace(final String name)
    throws SystemException {

    final String method = "resolveSpace";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final String uid = RestContext.build(this.client).resolveSpace(name);
      if (uid == null)
        throw ServiceException.notFound(ServiceBundle.string(ServiceError.OBJECT_NOT_EXISTS, RestContext.ENDPOINT_SPACES, RestMarshaller.NAME, name));
      return uid;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSpace
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObject} transfer
   **                            object.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  criteria           the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  protected void searchSpace(final int count, final Set<String> filter, final String criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchSpace";
    trace(method, Loggable.METHOD_ENTRY);

    int batch = 0;
    int index = 1;
    final Search search = RestContext.build(this.client).searchSpace();
    try {
      do {
        final ListResult<Result.Space> response = search.page(index, count).filter(criteria).invoke(Result.Space.class);
        batch = response.total();
        if (batch == 0) {
          break;
        }

        for (Result.Space space : response) {
          try {
            // build an URI for the organization tenant to resolve the unique
            // name of the tenant the space resides within
            final URI           uri    = UriBuilder.fromPath(space.entity().endpointTenant()).build();
            // lookup the organitaion tenant
            final Result.Tenant tenant = lookupTenant(uri);
            // set the unique name of the tenant looked up above in the entity
            // Attention:
            // this overrides the guid mapped to the tenant organization hence
            // it can never be used later as a reference
            space.entity().tenant(tenant.entity().name());
            // build connector object and submit to handler
            if (!handler.handle(RestMarshaller.buildSpace(space, filter)))
              break;
          }
          catch (RuntimeException e) {
            throw ServiceException.abort(e.getLocalizedMessage());
          }
          catch (Throwable t) {
            throw ServiceException.general(t.getLocalizedMessage());
          }
        }
      // position batch window on the next page
      } while (index++ * count < batch);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSpace
  /**
   ** Taking the attributes given (which always includes the
   ** {@link ObjectClass}) and create a space and its {@link Uid}.
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
   */
  protected Uid createSpace(final Set<Attribute> attribute)
    throws SystemException {

    final String method = "createSpace";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      Attribute verify = accessor.find(Payload.TENANT);
      // prevent bogus state
      if (verify == null) {
        nameRequired(Payload.TENANT);
      }

      verify = accessor.find(Payload.NAME);
      if (verify == null) {
        verify = accessor.getName();
      }
      // prevent bogus state
      if (verify == null) {
        nameRequired(Payload.NAME);
      }

      // marshall a Cloud Controller space resource transfered to the Service
      // Provider execute operation
      final Result.Space resource = RestContext.build(this.client).createSpace(RestMarshaller.buildSpace(attribute));
      return new Uid(resource.metadata().guid());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifySpace
  /**
   ** Taking the attributes given modify a space and return its {@link Uid}.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the space to modify.
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
   ** @return                    the unique id for the object that is modified.
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
  protected Uid modifySpace(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "modifySpace";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final Result.Space resource = RestContext.build(this.client).modifySpace(identifier, RestMarshaller.buildSpace(attribute));
      return new Uid(resource.guid());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSpace
  /**
   ** Implements the operation interface to delete a space from the Service
   ** Provider.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the unique identifier that specifies the
   **                            tenant to delete.
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the status of the HTTP response is greater
   **                            than or equal to 300 and <code>type</code> does
   **                            not represent the appropriate response type.
   **                            Also thrown if the client handler fails to
   **                            process the request or response.
   */
  protected void deleteSpace(final String identifier)
    throws SystemException {

    final String method = "deleteSpace";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      RestContext.build(this.client).deleteSpace(identifier);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignSpace
  /**
   ** Taking the attributes given assign role based access on a Cloud Controller
   ** space object.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the Cloud Controller space to
   **                            modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to assign
   **                            the resource object.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  protected Uid assignSpace(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignSpace";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Attribute space = accessor.find(Uid.NAME);
      if (space == null)
        identifierRequired();

      final Attribute role = accessor.find(Name.NAME);
      if (role == null)
        nameRequired(Name.NAME);

      final Result.User resource = RestContext.build(this.client).assign(AttributeUtil.getStringValue(space), identifier, AttributeUtil.getStringValue(role));
      return new Uid(resource.metadata().guid());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeSpace
  /**
   ** Taking the attributes given revokes role based access from a Cloud
   ** Controller space object.
   ** <br>
   ** The Uid must returned so that the caller can refer to the modified object.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  identifier         the identifier of the Cloud Controller space to
   **                            modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          includes all the attributes necessary to assign
   **                            the resource object.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void revokeSpace(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeSpace";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final Attribute space = accessor.find(Uid.NAME);
      if (space == null)
        identifierRequired();

      final Attribute role = accessor.find(Name.NAME);
      if (role == null)
        nameRequired(Name.NAME);

      RestContext.build(this.client).revoke(AttributeUtil.getStringValue(space), identifier, AttributeUtil.getStringValue(role));
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
}