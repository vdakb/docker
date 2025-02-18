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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Drupal Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.drupal;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemSchema;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

import oracle.iam.identity.icf.schema.Factory;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceConnector;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.ServiceConfiguration;
import oracle.iam.identity.icf.rest.NoneFilterTranslator;

import oracle.iam.identity.icf.connector.drupal.schema.User;

import oracle.iam.identity.icf.connector.drupal.schema.UserJson;
import static oracle.iam.identity.icf.foundation.utility.SchemaUtility.STATUS;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link ServiceConnector} for a Google Drupal core system.
 ** <p>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 ** <p>
 ** In addition to the standard API's this connector supports resolving an
 ** object by its name and returns the uid of the object.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=ServiceConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.drupal.Main.properties")
public class Main extends    ServiceConnector
                  implements SystemSchema {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String CATEGORY     = "JCS.CONNECTOR.GDP";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private Context             context      = null;

  private Schema              schema       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> Google Drupal Connector that
   ** allows use as a JavaBean.
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
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   **
   ** @throws ConnectorException if the authentication/authorization request
   **                            implemented in the {@link ServiceClient}
   **                            constructor fails.
   */
  @Override
  public final void init(final Configuration configuration)
    throws ConnectorException {

    final String method = "Main#init()";
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
  public final void dispose() {
    final String method = "Main#dispose()";
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
    final String method = "Main#test()";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      this.context.resourcePing();
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

    final String method = "Main#schema()";
    trace(method, Loggable.METHOD_ENTRY);
    if (this.schema == null)
      this.schema = build();
    trace(method, Loggable.METHOD_EXIT);
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
    final String method = "Main#createFilterTranslator()";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, "OperationOptions supplied: " + option);
    trace(method, "Option ATTRS_TO_GET: " + java.util.Arrays.asList(option.getAttributesToGet()));
    
    try {
      return new FilterTranslator<String>() {
        /**
         ** Main method to be called to translate a filter.
         */
        @Override
        public List<String> translate(final Filter filter) {
          List<String> criteria = null;;
          try {
            criteria = DrupalFilterTranslator.build().translate(filter);
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
/* orig:
    // Google Drupal isn't able to apply filter at querying for resources
    try {
      return NoneFilterTranslator.<String>build();
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
*/
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
  public void executeQuery(final ObjectClass type, final String criteria, final ResultsHandler handler, final OperationOptions option) {

    final String method = "Main#executeQuery()";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, "Search type: " + type);
    trace(method, "Search criteria: " + criteria);
    trace(method, "Results handler: " + handler);
    final Set<String> attributeSet = new HashSet<>();
    if (option.getAttributesToGet() != null) {
      for (String cursor : option.getAttributesToGet()) {
        trace(method, "Iterating over attributesToGet: " + cursor);
        if (Uid.NAME.equals(cursor) || Name.NAME.equals(cursor) || OperationalAttributes.ENABLE_NAME.equals(cursor) || OperationalAttributes.PASSWORD_NAME.equals(cursor)) {
          trace(method, "Special attribute encountered");
          continue;
        }
        else {
          attributeSet.add(cursor);
        }
      }
    }

    try {
      if (ObjectClass.ACCOUNT.equals(type)) {
        searchAccount(criteria, attributeSet, handler);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        searchRole(handler);
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

    final String method = "Main#create()";
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

    final String method = "Main#delete()";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type))
        deleteAccount(identifier);
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
    final String identifier = uid.getUidValue();
    if (StringUtility.empty(identifier))
      identifierRequired();

    final String method = "Main#update()";
    trace(method, Loggable.METHOD_ENTRY);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(replace));
    }

    Uid result = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        result = modifyAccount(identifier, replace);
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
  public Uid addAttributeValues(final ObjectClass type, final Uid uid, final Set<Attribute> addition, final OperationOptions option)
    throws ConnectorException {

    final String method = "Main#addAttributeValues()";
    debug(method, Loggable.METHOD_ENTRY);

    // prevent bogus input
    if (addition.isEmpty())
      valuesRequired();

    // prevent bogus input
    final String identifier = uid.getUidValue();
    trace(method, ": identifier=" + identifier);
    if (StringUtility.empty(identifier))
      identifierRequired();

    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(addition));
    }

    try {
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : addition) {
          final List<Object>   value   = cursor.getValue();
          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          trace(method, "subject=" + subject);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            assignRole(identifier, subject.getAttributes());
          }
          else {
            unsupportedType(subject.getObjectClass(), method);
          }
        }
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
   public Uid removeAttributeValues(final ObjectClass type, final Uid uid, final Set<Attribute> remove, final OperationOptions option)
    throws ConnectorException {

    final String method = "Main#removeAttributeValues()";
    trace(method, Loggable.METHOD_ENTRY);

    // prevent bogus input
    if (remove.isEmpty())
      propagate(SystemError.OBJECT_VALUES_REQUIRED);

    // prevent bogus input
    final String identifier = uid.getUidValue();
    trace(method, "identifier=" + identifier);
    if (StringUtility.empty(identifier))
      identifierRequired();

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
          trace(method, "subject=" + subject);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            revokeRole(identifier, subject.getAttributes());
          }
          else {
            unsupportedType(type, method);
          }
        }
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
  // Method:   build (SystemSchema)
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
    final SchemaBuilder   builder = new SchemaBuilder(Main.class);
    final ObjectClassInfo user    = Factory.defineObjectClass(builder, User.class);
    builder.defineObjectClass(user);

    // how stupid ICF is build shows the code below
    // instead of providing a fine grained api to take control how a certain
    // object class is presented in the populated schema you need to add the
    // object class generated completely at first. The ugly frameork will add
    // than all possible operation to the object class that can it find, with
    // the result that we need to remove all operation later and allow only
    // those that the connector is realy supporting
    // what kind of bull shit is that, that keeps the garbage collector busy
    // only
    builder.clearSupportedObjectClassesByOperation();
    builder.addSupportedObjectClass(SchemaOp.class, user);
    builder.addSupportedObjectClass(CreateOp.class, user);
    builder.addSupportedObjectClass(DeleteOp.class, user);
    builder.addSupportedObjectClass(UpdateOp.class, user);
    builder.addSupportedObjectClass(SearchOp.class, user);
    builder.addSupportedObjectClass(TestOp.class,   user);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAccount
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  criteria           the {@link String} search criteria, already
   **                            normalized and simplified, e.g. 'uid eq "14"'.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attributeSet       the names of resource attributes to return.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void searchAccount(final String criteria, final Set<String> attributeSet, final ResultsHandler handler)
    throws SystemException {

    final String method = "Main#searchAccount()";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, "Received criteria argument: " + criteria);
    trace(method, "Received attributeSet argument: " + attributeSet);
    
    try {
      final List<User> result = this.context.searchAccount(criteria);
      for (User user : result) {
        trace(method, "Received user entry: " + user);
        try {
          final ConnectorObject payload = Marshaller.connectorObject(user, attributeSet);
          trace(method, "Payload: " + payload);
          if (!handler.handle(payload))
            break;
        }
        catch (Exception t) {
          warning(method, " Exception caught: " + t.getMessage());
          throw ServiceException.general(t.getLocalizedMessage());
        }
      }
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

    final String method = "Main#createAccount()";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      Attribute verify = accessor.find(Name.NAME);
      if (verify == null) {
        verify = accessor.getName();
      }
      // prevent bogus state
      if (verify == null) {
        nameRequired(Name.NAME);
      }

      // marshall a REST user resource transfered to the Service Provider
      // execute operation
      final UserJson resource = this.context.createAccount(Marshaller.inboundUser(attribute));
      final User resultUser = new User(resource);
      String resultUid = resultUser.uid();
      trace(method, "Returning unique UID: " + resultUid);
      return new Uid(resultUid);
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
   ** @param  identifier         the identifier of the account to modify.
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
  protected Uid modifyAccount(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "Main#modifyAccount()";
    trace(method, Loggable.METHOD_ENTRY);
    debug(method, "Attributes received: " + attribute.toString());
    try {
      
      final User resource = this.context.modifyAccount(identifier, new User(Marshaller.inboundUser(attribute)));
      String resultUid = resource.uid();
      trace(method, "Returning unique UID: " + resultUid);
      return new Uid(resultUid);
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

    final String method = "Main#deleteAccount()";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      this.context.deleteAccount(identifier);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRole
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void searchRole(final ResultsHandler handler)
    throws SystemException {

    final String method = "Main#searchRole()";
    trace(method, Loggable.METHOD_ENTRY);
    try {
        for (Map<String, String> cursor : this.context.searchRole()) {
          // build connector object and submit to handler
          final ConnectorObjectBuilder builder = new ConnectorObjectBuilder().setObjectClass(ObjectClass.GROUP);
          if (!handler.handle(builder.setUid(cursor.get("id")).setName(cursor.get("role_alias")).build()))
            break;
        }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRole
  /**
   ** Build a request to assign Google Drupal <code>Role</code> members in the Service
   ** Provider.
   **
   ** @param  beneficiary        the identifier of the user to assign.
   **                            <br>
   **                            Must never <code>null</code>.
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
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Uid assignRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "Main#assignRole()";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearrange the attribute for the usergroup attributes passed in
    // the identifier is the beneficiary to be revoked from the usergroup
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      final String name  = accessor.findString(Name.NAME);
      if (name == null)
        nameRequired(Name.NAME);

      // create a post operation
      final User result = this.context.assignRole(name, beneficiary);
      return new Uid(result.name());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRole
  /**
   ** Taking the attributes given modify a usergroup.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  beneficiary        the identifier of the user to revoke.
   **                            <br>
   **                            Must never <code>null</code>.
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
  protected void revokeRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "Main#revokeRole()";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearrange the attribute for the usergroup attributes passed in
    // the identifier is the beneficiary to be revoked from the usergroup
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      final String name  = accessor.findString(Name.NAME);
      if (name == null)
        nameRequired(Name.NAME);

      // create a delete operation
      this.context.revokeRole(name, beneficiary);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}