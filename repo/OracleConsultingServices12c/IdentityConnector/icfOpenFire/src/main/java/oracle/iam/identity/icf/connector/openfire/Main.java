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
    Subsystem   :   Openfire Database Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire;

import java.util.Set;
import java.util.List;
import java.util.Date;

import oracle.iam.identity.icf.connector.openfire.provider.MemberProvider;

import oracle.iam.identity.icf.connector.openfire.provider.RoomMateProvider;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.AttributesAccessor;

import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.DateUtility;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseEndpoint;
import oracle.iam.identity.icf.dbms.DatabaseConnector;
import oracle.iam.identity.icf.dbms.DatabaseException;
import oracle.iam.identity.icf.dbms.DatabaseConfiguration;
import oracle.iam.identity.icf.dbms.DatabaseFilterTranslator;

import oracle.iam.identity.icf.connector.openfire.schema.User;
import oracle.iam.identity.icf.connector.openfire.schema.Room;
import oracle.iam.identity.icf.connector.openfire.schema.Group;
import oracle.iam.identity.icf.connector.openfire.schema.Member;
import oracle.iam.identity.icf.connector.openfire.schema.RoomMate;
import oracle.iam.identity.icf.connector.openfire.schema.Marshaller;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link DatabaseConnector} for a database application system.
 ** <p>
 ** The life-cycle for the <code>Pool</code> is as follows:
 ** <br>
 ** {@link #init(Configuration)} is called then any of the operations
 ** implemented in the <code>Pool</code> and finally {@link #dispose()}. The
 ** {@link #init(Configuration)} and {@link #dispose()} allow for block
 ** operations. For instance bulk creates or deletes and the use of before and
 ** after actions. Once {@link #dispose()} is called the <code>Pool</code>
 ** object is discarded.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 ** <p>
 ** The database application connector is a basic, but easy to use connector for
 ** accounts in a relational database.
 ** <p>
 ** It supports create, update, search, and delete operations. It can also be
 ** used for pass-thru authentication, although it assumes the password is in
 ** clear text in the database.
 ** <p>
 ** This connector assumes that all account data is stored in a single database
 ** table. The delete action is implemented to simply remove the row from the
 ** table.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=DatabaseConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.openfire.dbs.Main.properties")
public class Main extends DatabaseConnector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The category of the logging facility to use. */
  private static final String   CATEGORY = "JCS.CONNECTOR.OFS";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private DatabaseConfiguration config   = null;

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private Context               context  = null;

  private boolean               groups   = false;
  private boolean               rooms    = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> database connector that allows use
   ** as a JavaBean.
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
   **                            implemented in the {@link Context} constructor
   **                            fails.
   */
  @Override
  public void init(final Configuration configuration)
    throws ConnectorException {

    final String method = "init";
    trace(method, Loggable.METHOD_ENTRY);
    this.config  = (DatabaseConfiguration)configuration;
    // before setup the context inject the logger to the endpoint configuration
    final DatabaseEndpoint endpoint = this.config.endpoint();
    endpoint.logger(logger());
    // setup the operational context
    this.context = Context.build(endpoint);
    trace(method, Loggable.METHOD_EXIT);
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
    // release connection to the pool and free up resources
    this.context.disconnect();
    this.context = null;
    this.config  = null;
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test (TestOp)
  /**
   ** Tests the {@link Configuration} with the connector.
   **
   ** @throws RuntimeException   if the configuration is not valid or the test
   **                            failed..
   */
  @Override
  public final void test() {
    final String method = "test";
    trace(method, Loggable.METHOD_ENTRY);
    this.context.test();
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
   ** different than its {@link Name}, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   */
  @Override
  public Schema schema() {
    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    Schema schema = null;
    try {
      schema = this.context.schema().build(Main.class);
    }
    catch (SystemException e) {
      fatal(method, e);
      propagate(e);
    }
    trace(method, Loggable.METHOD_EXIT);
    return schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilterTranslator (SearchOp)
  /**
   ** Creates a filter translator that will translate a specified filter into
   ** one or more native queries.
   ** <br>
   ** Each of these native queries will be passed subsequently into
   ** {@link #executeQuery(ObjectClass, DatabaseFilter, ResultsHandler, OperationOptions)}.
   ** <p>
   ** <b>Attention</b>:
   ** <br>
   ** This method <b>must</b> return a non-<code>null</code> instance of a
   ** {@link FilterTranslator} otherwise the communication to the
   ** <code>Connector Server</code> breaks.
   ** <br>
   ** The filter build by the translator itself must also <b>never</b>
   ** <code>null</code>.
   **
   ** @param  type               the {@link ObjectClass} for the search.
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  option             additional options that impact the way this
   **                            operation is run.
   **                            <br>
   **                            If the caller passes <code>null</code>, the
   **                            framework will convert this into an empty set
   **                            of options, so SPI need not worry about this
   **                            ever being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    A filter translator.
   **                            This must not be <code>null</code>.
   **                            A <code>null</code> return value will cause the
   **                            API (SearchApiOp) to throw
   **                            {@link NullPointerException}.
   **                            <br>
   **                            Possible object is {@link FilterTranslator}.
   */
  @Override
  public FilterTranslator<DatabaseFilter> createFilterTranslator(final ObjectClass type, final OperationOptions option) {
    final String method = "createFilterTranslator";
    trace(method, Loggable.METHOD_ENTRY);
    DatabaseFilterTranslator translator = null;
    try {
      translator = DatabaseFilterTranslator.build(this.context.schema(), type);
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return translator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeQuery (SearchOp)
  /**
   ** ConnectorFacade calls this method once for each native query that the
   ** FilterTranslator produces in response to the Filter passed into
   ** SearchApiOp.
   ** <p>
   ** If the FilterTranslator produces more than one native query, then
   ** ConnectorFacade will automatically merge the results from each query and
   ** eliminate any duplicates.
   ** <b>NOTE</b>:
   ** <br>that this implies an in-memory data structure that holds a set of Uid
   ** values, so memory usage in the event of multiple queries will be O(N)
   ** where N is the number of results. This is why it is important that the
   ** FilterTranslator for each Connector implement OR if possible.
   **
   ** @param  type              the object class for the search.
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  criteria           the criteria to apply on the search and
   **                            converted from the <code> ICF Filter</code>
   **                            passed to
   **                            {@link #createFilterTranslator(ObjectClass, OperationOptions)}.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  handler            results should be returned to this handler.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   ** @param  option             additional options that affect the way this
   **                            operation is run.
   **                            If the caller passes <code>null</code>, the
   **                            framework will convert this into an empty set
   **                            of options, so an implementation need not guard
   **                            against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   */
  @Override
  public void executeQuery(final ObjectClass type, final DatabaseFilter criteria, final ResultsHandler handler, final OperationOptions option)
    throws ConnectorException {

    final String method = "executeQuery";
    trace(method, Loggable.METHOD_ENTRY);

    final Set<String> filter = CollectionUtility.set(option.getAttributesToGet());
    if (filter.size() > 0) {
      // go an extra mile to exclude the embedded object like groups and rooms
      // from the query
      this.groups = filter.contains(ObjectClass.GROUP_NAME);
      filter.remove(ObjectClass.GROUP_NAME);

      this.rooms = filter.contains(Marshaller.ROOM_NAME);
      filter.remove(Marshaller.ROOM_NAME);
    }
    // initialize the operations control configuration
    final OperationContext control = OperationContext.build(option);
    try {
      if (ObjectClass.ACCOUNT.equals(type)) {
        searchAccount(control, criteria, filter, handler);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        searchGroup(control, criteria, filter, handler);
      }
      else if (Marshaller.ROOM.equals(type)) {
        searchRoom(control, criteria, filter, handler);
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
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
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
        this.context.userDelete(identifier);
      else if (ObjectClass.GROUP.equals(type))
        this.context.groupDelete(identifier);
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
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
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
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
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
          else if (Marshaller.ROOM.equals(subject.getObjectClass())) {
            assignRoom(identifier, subject.getAttributes());
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
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
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
          final List<Object>   value   = cursor.getValue();
          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            revokeGroup(identifier, subject.getAttributes());
          }
          else if (Marshaller.ROOM.equals(subject.getObjectClass())) {
            revokeRoom(identifier, subject.getAttributes());
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
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
   ** @param  control            the {@link OperationContext} controlling the.
   **                            serach operation
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link OperationContext}.
   ** @param  criteria           the filter used to query a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@code ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
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
  protected void searchAccount(final OperationContext control, final DatabaseFilter criteria, final Set<String> filter, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchAccount";
    trace(method, Loggable.METHOD_ENTRY);

    // setup the filter criteria to apply on the search
    DatabaseFilter summary = criteria;
    if (control.incremental && control.synchronizationToken != null) {
      // the expected format of the synchronization token must be compliant to
      // RFC 4517
      final Date timestamp = DateUtility.parse(control.synchronizationToken, DateUtility.RFC4517_ZULU_NANO);
      summary =
        (summary == null)
      ? this.context.searchTime(timestamp.getTime())
      : DatabaseFilter.build(this.context.searchTime(timestamp.getTime()), criteria, DatabaseFilter.Operator.AND);
    }

    try {
      int batch = 0;
      int index = 1;
      do {
        final List<User> response = this.context.userSearch(summary, index, control.limit);
        batch = response.size();
        if (batch == 0 && index == 1) {
          break;
        }
        for (User tupel : response) {
          final List<Member>   memberOf  = this.groups ? MemberProvider.user(this.context).select(tupel.uid())   : null;
          final List<RoomMate> occupancy = this.rooms  ? RoomMateProvider.user(this.context).select(tupel.uid()) : null;
          try {
            // build connector object and submit to handler
            final ConnectorObject payload = Marshaller.connectorObject(tupel, filter, memberOf, occupancy);
            if (!handler.handle(payload))
              break;
          }
          catch (RuntimeException e) {
            throw DatabaseException.abort(e.getLocalizedMessage());
          }
          catch (Throwable t) {
            throw DatabaseException.general(t.getLocalizedMessage());
          }
        }
        index += control.limit;
      } while (batch == control.limit);
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
  protected Uid createAccount(final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    Attribute verify = accessor.find(Name.NAME);
    if (verify == null) {
      verify = accessor.getName();
    }
    // prevent bogus state
    if (verify == null) {
      nameRequired(Name.NAME);
    }

    final String method = "createAccount";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final User entity = this.context.userCreate(Marshaller.inboundUser(attribute));
      return new Uid(entity.uid());
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
    try {
      // an update of a resource will only be required if at least one attribute
      // is provided for the operation that isn't the status
      final User resource = this.context.userModify(identifier, Marshaller.inboundUser(attribute));
      return new Uid(resource == null ? identifier : resource.uid());
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
   ** @param  control            the {@link OperationContext} controlling the.
   **                            serach operation
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link OperationContext}.
   ** @param  criteria           the filter used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@code ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
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
   ** final OperationContext control
   */
  protected void searchGroup(final OperationContext control, final DatabaseFilter criteria, final Set<String> filter, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchGroup";
    trace(method, Loggable.METHOD_ENTRY);

    try {
      int batch = 0;
      int index = 1;
      do {
        final List<Group> response = this.context.groupSearch(criteria, index, control.limit);
        batch = response.size();
        if (batch == 0 && index == 1) {
          break;
        }
        for (Group tupel : response) {
          try {
            // build connector object and submit to handler
            final ConnectorObject payload = Marshaller.connectorObject(tupel, filter);
            if (!handler.handle(payload))
              break;
          }
          catch (RuntimeException e) {
            throw DatabaseException.abort(e.getLocalizedMessage());
          }
          catch (Throwable t) {
            throw DatabaseException.general(t.getLocalizedMessage());
          }
        }
        index += control.limit;
      } while (batch == control.limit);
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
  protected Uid createGroup(final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    Attribute verify = accessor.find(Name.NAME);
    if (verify == null) {
      verify = accessor.getName();
    }
    // prevent bogus state
    if (verify == null) {
      nameRequired(Name.NAME);
    }

    final String method = "createGroup";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      final Group entity = Marshaller.inboundGroup(attribute);
      this.context.groupCreate(entity);
      return new Uid(entity.gid());
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignGroup
  /**
   ** Taking the attributes given create a group membership at the Service
   ** Provider.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  beneficiary        the identifier of the user account to assign.
   **                            <br>
   **                            Must not be <code>null</code>.
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
  protected void assignGroup(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignGroup";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      this.context.userAssign(Marshaller.inboundMember(attribute).uid(beneficiary));
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeGroup
  /**
   ** Taking the attributes given modify a group membership.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  beneficiary        the identifier of the user account to assign.
   **                            <br>
   **                            Must not be <code>null</code>.
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
  protected void revokeGroup(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeGroup";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearange the attribute due the group is in the passed in
    // attributes and the identifier is the beneficiary to be revoked from the
    // group
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      this.context.userRevoke(Member.build(scope, beneficiary));
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRoom
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  control            the {@link OperationContext} controlling the.
   **                            serach operation
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link OperationContext}.
   ** @param  criteria           the filter used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@code ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
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
   ** final OperationContext control
   */
  protected void searchRoom(final OperationContext control, final DatabaseFilter criteria, final Set<String> filter, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchRoom";
    trace(method, Loggable.METHOD_ENTRY);

    try {
      int batch = 0;
      int index = 1;
      do {
        final List<Room> response = this.context.roomSearch(criteria, index, control.limit);
        batch = response.size();
        if (batch == 0 && index == 1) {
          break;
        }
        for (Room tupel : response) {
          try {
            // build connector object and submit to handler
            final ConnectorObject payload = Marshaller.connectorObject(tupel, filter);
            if (!handler.handle(payload))
              break;
          }
          catch (RuntimeException e) {
            throw DatabaseException.abort(e.getLocalizedMessage());
          }
          catch (Throwable t) {
            throw DatabaseException.general(t.getLocalizedMessage());
          }
        }
        index += control.limit;
      } while (batch == control.limit);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignRoom
  /**
   ** Taking the attributes given create a room membership at the Service
   ** Provider.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  beneficiary        the identifier of the user account to assign.
   **                            <br>
   **                            Must not be <code>null</code>.
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
  protected void assignRoom(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assignRoom";
    trace(method, Loggable.METHOD_ENTRY);
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      this.context.roomAssign(Marshaller.inboundRoomMate(attribute).jid(beneficiary));
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeRoom
  /**
   ** Taking the attributes given modify a room occupancy.
   ** <p>
   ** The primary intention of this method is to allow overriding by sub classes
   ** so that the behavior can be adopted to specific requirements of the
   ** Service Provider.
   **
   ** @param  beneficiary        the identifier of the user account to rovoke.
   **                            <br>
   **                            Must not be <code>null</code>.
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
  protected void revokeRoom(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "revokeRoom";
    trace(method, Loggable.METHOD_ENTRY);
    // we need to rearange the attribute due the room is in the passed
    // attributes and the identifier is the beneficiary to be revoked from the
    // room
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    try {
      // prevent bogus state
      final String scope = accessor.findString(Uid.NAME);
      if (scope == null)
        identifierRequired();

      this.context.userRevoke(RoomMate.build(scope, beneficiary));
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}