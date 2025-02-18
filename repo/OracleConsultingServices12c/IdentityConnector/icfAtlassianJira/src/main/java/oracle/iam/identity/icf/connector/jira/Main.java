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
    Subsystem   :   Atlassian Jira Server Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemSchema;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.schema.Factory;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.ServiceConnector;
import oracle.iam.identity.icf.rest.ServiceConfiguration;
import oracle.iam.identity.icf.rest.NoneFilterTranslator;

import oracle.iam.identity.icf.rest.domain.ListResult;

import oracle.iam.identity.icf.connector.jira.schema.User;
import oracle.iam.identity.icf.connector.jira.schema.Group;
import oracle.iam.identity.icf.connector.jira.schema.Actor;
import oracle.iam.identity.icf.connector.jira.schema.Project;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link ServiceConnector} for a Jira Server system.
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
@ConnectorClass(configurationClass=ServiceConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.jira.Main.properties")
public class Main extends    ServiceConnector
                  implements SystemSchema {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String CATEGORY      = "JCS.CONNECTOR.AJS";

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
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> Atlassian Jira Connector that
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
  public final void dispose() {
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

    final String method = "schema";
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
    final String method = "createFilterTranslator";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      return NoneFilterTranslator.<String>build();
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

    final String method = "executeQuery";
    trace(method, Loggable.METHOD_ENTRY);
    final Set<String> filter = new HashSet<>();
    if (option.getAttributesToGet() != null) {
      for (String cursor : option.getAttributesToGet()) {
        // handle special attributes appropriatly
        if (Uid.NAME.equals(cursor) || Name.NAME.equals(cursor) || OperationalAttributes.ENABLE_NAME.equals(cursor) || OperationalAttributes.PASSWORD_NAME.equals(cursor))
          continue;
        else {
          filter.add(cursor);
        }
      }
    }

    try {
      if(ObjectClass.ACCOUNT.equals(type)) {
        // IAM-438: Add possibility to control number of objects returned
        // default page size, hardwired (and overriden, if applicable)
        int batchSize = 500;
        Object batchSizeObj = option.getOptions().get("batchSize");
        if (batchSizeObj != null) {
          batchSize = Integer.valueOf((Integer) batchSizeObj);
        }
        searchAccount(batchSize, filter, handler);
      }
      else if(ObjectClass.GROUP.equals(type)) {
        searchGroup(filter, handler);
      }
      else if(Marshaller.ROLE.equals(type)) {
        searchRole(filter, handler);
      }
      else if(Marshaller.PROJECT.equals(type)) {
        searchProject(filter, handler);
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
      valuesRequired ();

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
      else if (Marshaller.PROJECT.equals(type)) {
        identifier = createProject(attribute);
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
      if (ObjectClass.ACCOUNT.equals(type)) {
        deleteAccount(uid.getUidValue());
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
        result = modifyAccount(identifier, replace);
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

    try  {
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : addition) {
          final List<Object> value = cursor.getValue();
          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          if (Marshaller.PROJECT.equals(subject.getObjectClass())) {
            assignProject(identifier, subject.getAttributes());
          }
          else if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            assignGroup(identifier, subject.getAttributes());
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

    // prevent bogus input
    if (remove.isEmpty())
      propagate(SystemError.OBJECT_VALUES_REQUIRED);

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

    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : remove) {
          final List<Object> value = cursor.getValue();
          if (value != null && value.size() > 1)
            throw ServiceException.tooMany(cursor.getName());

          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          if (Marshaller.PROJECT.equals(subject.getObjectClass())) {
            revokeProject(identifier, subject.getAttributes());
          }
          else if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            revokeGroup(identifier, subject.getAttributes());
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
    final ObjectClassInfo group   = Factory.defineObjectClass(builder, Group.class);
    final ObjectClassInfo project = Factory.defineObjectClass(builder, Project.class);
    builder.defineObjectClass(user);
    builder.defineObjectClass(group);
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
    builder.addSupportedObjectClass(SchemaOp.class, group);
    builder.addSupportedObjectClass(SearchOp.class, group);
    builder.addSupportedObjectClass(SchemaOp.class, project);
    builder.addSupportedObjectClass(SearchOp.class, project);
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
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  attribute          the names of resource attributes to return.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   ** @throws SystemException    if the operation fails.
   */
  @SuppressWarnings("unchecked")
  protected void searchAccount(final int count, final Set<String> attribute, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // the cache is a simple association between id of a group and the id's
      // of the granted users; that enough to detect membership
      final Map<String, Set<String>> memberCache = new HashMap<String, Set<String>>();

      for (Group group : this.context.searchGroup().invoke(Group.class).resource()) {
        int batch = 0;
        int index = 0;
        final Set<String> member = new HashSet<String>();
        int membersTotal = 0;
        do {
          // IAM-438: Split the one-liner to find out total number of objects to iterate (and make use of paging)
          ListResult<User> membersResult = this.context.lookupGroup(group.id()).page(index, count).invoke(User.class);
          membersTotal = membersResult.total();
          final List<User> membership = membersResult.resource();
          // update the batch size always (it's used in the break condition below)
          if (membership != null) {
            batch = membership.size();
          }
          if (batch == 0 && index == 0) {
            break;
          }
          for (User cursor : membership) {
            member.add(cursor.userName());
          }
          index += count;
        } while (index <= membersTotal);
        memberCache.put(group.id(), member);
      }

      // build a cache for all roles for any projects to avoid calling the API
      // if it goes to the iteration over the discovered user id's
      // get all roles for any projects
      // the cache is a little bit more complex as for group memberships but
      // regarding the time line its a quick hack to cacha the entire object
      // instead of thinking about less memory consumption
      final Map<String, List<Project.Role>> projectCache = new HashMap<String, List<Project.Role>>();
      for (Project project : this.context.searchProject().list(Project.class)) {
        final List<String>       projectRoles = this.context.searchProjectRoles(project.id());
        final List<Project.Role> projectActor = new ArrayList<Project.Role>();
        for (Project.Role role : this.context.searchRole().list(Project.Role.class)) {
          // avoid a request with no member inside a project
          if (projectRoles.contains(role.name()))
            projectActor.add((Project.Role)this.context.lookupProjectRole(project.id(), role.id().toString()).invoke(Project.Role.class));
        }
        projectCache.put(project.name(), projectActor);
      }

      // orig: no paging (whence only first page was retrieved)
      // final List<User> users = this.context.searchAccount().list(User.class);
      
      // IAM-438: User pagination - forgotten in the original commit
      final List<User> users = new ArrayList<User>();
      {
        int batch;
        int index = 0;
        int userPageSize = 0;
        do {
          batch = 0;
          trace(method, "New users iteration round: index=" + index + ", count=" + count + ", batch=" + batch);
          // IAM-438: Invoke the user search with paging parameters supplied
          ListResult<User> userResult = this.context.searchAccount().page(index, count).invoke(User.class);
          
          // Since the "list of users" response has no usual "header" we need to count the entries ourselves and determine the paging
          userPageSize = userResult.total();
          trace(method, "Retrieved new page, userPageSize=" + userPageSize);
          final List<User> userList = userResult.resource();
          // update the batch size always (it's used in the break condition below)
          if (userList != null) {
            batch = userList.size();
          }
          if (batch == 0 && index == 0) {
            // No records retrieved, break right away
            break;
          }
          for (User user : userList) {
            trace(method,"Adding user=" + user.userName() + " (ID=" + user.id() + ") to result list");
            users.add(user);
          }
          index += count;
          // The break condition might be a bit confusing - if we received as many users as we requested we're not sure whether
          // the number of users is aligned with the page size or not - in this case let's ask for one more page - might be empty.
          // Otherwise the batch size would be less than asked for
        } while (batch == count);
      }
      
      for (User user : users) {
        try {
          // if the groups are requested for an account go the extra mile to
          // populate
          final List<Pair<String, List<String>>> assignedProjects = new ArrayList<Pair<String, List<String>>>();
          final List<String>                     assignedGroups   = new ArrayList<String>();
          for (Map.Entry<String, List<Project.Role>> project : projectCache.entrySet()) {
            final List<String> roleMembership = new ArrayList<String>();
            for (Project.Role cursor : project.getValue()) {
              List<Actor> members = cursor.actors();
              for (Actor member : members) {
                if (member.name().equals(user.userName())) {
                  roleMembership.add(cursor.name().toString());
                }
              }
            }
            if (roleMembership.size() > 0) {
              assignedProjects.add(Pair.of(project.getKey(), roleMembership));
            }
          }
          for (Map.Entry<String, Set<String>> member : memberCache.entrySet()) {
            if (member.getValue().contains(user.userName())) {
              assignedGroups.add(member.getKey());
            }
          }
          // obtain user details, build connector object and submit to handler
          final User            subject = this.context.lookupAccount(user.id());
          final ConnectorObject payload = Marshaller.connectorObject(subject, attribute, assignedProjects, assignedGroups, null);
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
    Attribute verify = accessor.find(Name.NAME);
    if (verify == null) {
      verify = accessor.getName();
    }
    // prevent bogus state
    if (verify == null) {
      nameRequired(Name.NAME);
    }
    try {
      // marshall a REST user resource transfered to the Service Provider and
      // execute operation
      final User resource = this.context.createAccount(Marshaller.inboundUser(attribute));
      return new Uid(resource.userName());
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
  protected Uid modifyAccount(final String identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "modifyAccount";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    final GuardedString      password = accessor.getPassword();
    try {
      // an update of a resource will only be required if at least one attribute
      // is provided for the operation that isn't the password
      User resource = null;
      if (password == null && attribute.size() > 0)
        resource = this.context.modifyAccount(identifier, Marshaller.inboundUser(attribute));
      // take care about the password
      if (password != null)
        this.context.resetPassword(resource == null ? identifier : resource.userName(), CredentialAccessor.string(password));
      return new Uid(resource == null ? identifier : resource.userName());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resetPassword
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
   ** @param  value              the passwird to set for identifier.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected void resetPassword(final String identifier, final GuardedString value)
    throws SystemException {

    final String method = "resetPassword";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      this.context.resetPassword(identifier, CredentialAccessor.string(GuardedString.class.cast(value)));
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
   ** @param  attributes         the names of resource attributes to return.
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
  protected void searchRole(final Set<String> attributes, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchRole";
    trace(method, Loggable.METHOD_ENTRY);

    try {
      final List<Project.Role> result = this.context.searchRole().list(Project.Role.class);
      for (Project.Role cursor : result) {
        try {
          // obtain tenant details, build connector object and submit to handler
          final ConnectorObject payload = Marshaller.connectorObject(cursor, attributes);
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
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchProject
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  attributes         the names of resource attributes to return.
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
  @SuppressWarnings("unchecked")
  protected void searchProject(final Set<String> attributes, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchProject";
    trace(method, Loggable.METHOD_ENTRY);

    try {
      final List<Project> result = this.context.searchProject().list(Project.class);
      for (Project cursor : result) {
        try {
          // obtain tenant details, build connector object and submit to handler
          final Project subject = (Project)this.context.lookupProject(cursor.id()).invoke(Project.class);
          final ConnectorObject payload = Marshaller.connectorObject(subject, attributes);
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
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProject
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
  protected Uid createProject(final Set<Attribute> attribute)
    throws SystemException {

    final String method = "createProject";
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

      // marshall a REST tenant resource transfered to the Service Provider
      // execute operation
      final Project resource = this.context.createProject(Marshaller.inboundProject(attribute));
      return new Uid(resource.name());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignProject
  /**
   ** Build a request to assign JIRA <code>project</code> members in the Service
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
  private Uid assignProject(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignProject";
    trace(method, Loggable.METHOD_ENTRY);

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      final String role  = accessor.findString(Name.NAME);
      if (role == null)
        nameRequired(Name.NAME);

      // create a post operation
      this.context.assignProject(scope, role, beneficiary);
      return new Uid(beneficiary);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeProject TODO: Not tested
  /**
   ** Build a request to revoke JIRA <code>Project</code> members in the Service
   ** Provider.
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
  private void revokeProject(String beneficiary, Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeProject";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      final String name  = accessor.findString(Name.NAME);
      if (name == null)
        nameRequired(Name.NAME);

      // create a delete operation
      this.context.revokeProject(scope, name, beneficiary);
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
   ** @param  attributes         the names of resource attributes to return.
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
  protected void searchGroup(final Set<String> attributes, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchGroup";
    trace(method, Loggable.METHOD_ENTRY);

    try {
      final List<Group> result = this.context.searchGroup().invoke(Group.class).resource();
      for (Group cursor : result) {
        try {
          final ConnectorObject payload = Marshaller.connectorObject(cursor, attributes);
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
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignGroup
  /**
   ** Build a request to assign JIRA <code>group</code> members in the Service
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
   ** @throws SystemException    if an error occurs.
   */
  private Uid assignGroup(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignGroup";
    trace(method, Loggable.METHOD_ENTRY);

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      this.context.assignGroup(scope, beneficiary);
      // group assignement returns any results so return beneficiary as the
      // system identifier
      return new Uid(beneficiary);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeGroup
  /**
   ** Build a request to revoke JIRA <code>Group</code> members in the Service
   ** Provider.
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
  private void revokeGroup(String beneficiary, Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeGroup";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearrange the attribute for the usergroup attributes passed in
    // the identifier is the beneficiary to be revoked from the usergroup
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      // create a revoke operation
      this.context.revokeGroup(scope, beneficiary);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}