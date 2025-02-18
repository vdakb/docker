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

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.dbs;

import java.math.BigDecimal;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Date;
import java.util.HashMap;

import java.util.regex.Pattern;

import java.sql.Connection;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.SyncResultsHandler;

import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.resource.DatabaseBundle;

import oracle.iam.identity.icf.dbms.DatabaseError;
import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseContext;
import oracle.iam.identity.icf.dbms.DatabaseConnector;
import oracle.iam.identity.icf.dbms.DatabaseConfiguration;
import oracle.iam.identity.icf.dbms.DatabaseFilterTranslator;

import oracle.iam.identity.icf.connector.DatabaseDialect;
import oracle.iam.identity.icf.connector.AbstractConnectorPool;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

import org.identityconnectors.framework.common.objects.EmbeddedObject;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the base functionality of an Identity Manager
 ** {@link AbstractConnectorPool} for a directoy account.
 ** <p>
 ** The life-cycle for the {@link AbstractConnectorPool} is as follows
 ** {@link #init(Configuration)} is called then any of the operations
 ** implemented in the {@link AbstractConnectorPool} and finally
 ** {@link #dispose()}. The {@link #init(Configuration)} and {@link #dispose()}
 ** allow for block operations. For instance bulk creates or deletes and the use
 ** of before and after actions. Once {@link #dispose()} is called the
 ** {@link AbstractConnectorPool} object is discarded.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=DatabaseConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.dbs.Main.properties")
public class Main extends DatabaseConnector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the user name attribute */
  static final String           USERNAME       = "userName";

  /** The name of the password attribute */
  static final String           PASSWORD       = "password";

  /** The name of the status attribute */
  static final String           STATUS         = "active";

  /** the category of the logging facility to use */
  static final String           CATEGORY       =  "JCS.CONNECTOR.DBS";

  /**
   ** This regular expression uses one group to substitute expressions with a
   ** String.
   ** <p>
   ** Groups are defined by parentheses. Note that ?: will define a
   ** group as "non-contributing"; that is, it will not contribute to the return
   ** values of the <code>group</code> method.
   */
  private static final String   STRING_PATTERN = "\\$\\[((?:\\w|\\s)+)]";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Connection} passed into the callback
   ** {@link ConnectionFactory#setConnection(Connection)}.
   */
  private Context               context;

  /**
   ** Place holder for the {@link Configuration} passed into the callback
   ** {@link Main#init(Configuration)}.
   */
  private DatabaseConfiguration config;

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
   **                             object implemented by the
   **                             <code>Connector</code> developer and populated
   **                             with information in order to initialize the
   **                             <code>Connector</code>.
   */
  @Override
  public void init(final Configuration configuration)
    throws ConnectorException {

    final String method = "init";
    trace(method, Loggable.METHOD_ENTRY);
    this.config  = (DatabaseConfiguration)configuration;
    this.context = Context.build(this.config.endpoint());
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
    this.config  = null;
    this.context = null;
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
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    trace(method, Loggable.METHOD_EXIT);
    return schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sync (SyncOp)
  /**
   ** Request synchronization events--i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link SyncResultsHandler handler}
   ** once to pass back each matching synchronization event. Once this method
   ** returns, this method will no longer invoke the specified handler.
   ** <p>
   ** Each synchronization event contains a token that can be used to resume
   ** reading events starting from that point in the event stream. In typical
   ** usage, a client will save the token from the final synchronization event
   ** that was received from one invocation of this sync() method and then pass
   ** that token into that client's next call to this sync() method. This allows
   ** a client to "pick up where he left off" in receiving synchronization
   ** events. However, a client can pass the token from any synchronization
   ** event into a subsequent invocation of this sync() method. This will return
   ** synchronization events (that represent native changes that occurred)
   ** immediately subsequent to the event from which the client obtained the
   ** token.
   ** <p>
   ** A client that wants to read synchronization events "starting now" can call
   ** getLatestSyncToken(org.identityconnectors.framework.common.objects.ObjectClass)
   ** and then pass that token into this sync() method.
   **
   ** @param  objectClass        the class of object for which to return
   **                            synchronization events.
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link ObjectClass}.
   ** @param  token              the token representing the last token from the
   **                            previous sync. The {@link SyncResultsHandler}
   **                            will return any number of
   **                            <code>SyncDelta</code> objects, each of which
   **                            contains a token.
   **                            <br>
   **                            Should be <code>null</code> if this is the
   **                            client's first call to the <code>sync()</code>
   **                            method for this connector.
   **                            Allowed object is {@link SyncToken}.
   ** @param  handler            the result handler.
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link SyncResultsHandler}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            Allowed object is {@link OperationOptions}.
   */
//  @Override
  public void sync(final ObjectClass objectClass, final SyncToken token, final SyncResultsHandler handler, final OperationOptions option) {
    final String method = "sync";
    trace(method, Loggable.METHOD_ENTRY);
    final OperationContext context = OperationContext.build(option);
    final Date    time        = (Date)option.getOptions().get("timeStamp");
    /*
    try {
      // retrieve the next batch of entries from the target system which are
      // created since the last remebered execution of this task
      final Set<BigDecimal> entries = this.context.accountSearch(context.incremental ? time : null, context.start, context.start + context.limit);
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
    */
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLatestSyncToken (SyncOp)
  /**
   ** Returns the token corresponding to the most recent synchronization event.
   ** <p>
   ** An application that wants to receive synchronization events "starting now"
   ** --i.e., wants to receive only native changes that occur after this method
   ** is called -- should call this method and then pass the resulting token
   ** into the sync() method.
    **
   ** @param  objectClass        the class of object for which to find the most
   **                            recent synchronization event (if any).
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    a token if synchronization events exist;
   **                            otherwise <code>null</code>.
   **                            Possible object {@link SyncToken}.
   */
//  @Override
  public SyncToken getLatestSyncToken(final ObjectClass objectClass) {
    final String method = "sync";
    trace(method, Loggable.METHOD_ENTRY);
//    SyncStrategy strategy = chooseSyncStrategy(objectClass);
    trace(method, Loggable.METHOD_EXIT);
    return null; //strategy.getLatestSyncToken(objectClass);
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
   ** @param  objectClass        the object class for the search.
   **                            Will never be <code>null</code>.
   **                            Allowed object is {@link ObjectClass}.
   ** @param  query              the native query to run.
   **                            A value of <code>null</code> means <i>return
   **                            every instance of the given object class</i>.
   **                            Allowed object is <code>DatabaseFilter</code>.
   ** @param  handler            results should be returned to this handler.
   **                            Allowed object is {@link ResultsHandler}.
   ** @param  option             additional options that affect the way this
   **                            operation is run.
   **                            If the caller passes <code>null</code>, the
   **                            framework will convert this into an empty set
   **                            of options, so an implementation need not guard
   **                            against this being <code>null</code>.
   **                            Allowed object is {@link OperationOptions}.
   */
  @Override
  public void executeQuery(final ObjectClass objectClass, final DatabaseFilter query, final ResultsHandler handler, final OperationOptions option) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilterTranslator (SearchOp)
  /**
   ** Creates a filter translator that will translate a specified filter into
   ** one or more native queries.
   ** <br>
   ** Each of these native queries will be passed subsequently into
   ** executeQuery().
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
   ** @param  type               the type of object for which to find the most
   **                            recent synchronization event (if any).
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link ObjectClass}.
   ** @param  attribute          includes all the attributes necessary to create
   **                            the resource object including the
   **                            {@link ObjectClass} attribute and {@link Name}
   **                            attribute.
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  operation          additional options that affect the way this
   **                            operation is run.
   **                            If the caller passes <code>null</code>, the
   **                            framework will convert this into an empty set
   **                            of options, so an implementation need not guard
   **                            against this being <code>null</code>.
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
   */
  @Override
  public Uid create(final ObjectClass type, final Set<Attribute> attribute, final OperationOptions operation)
    throws ConnectorException {

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    if (ObjectClass.ACCOUNT.equals(type)) {
      Map <String, String> parameter = new HashMap<String, String>(2);

      Attribute username = accessor.find(USERNAME);
      Attribute password = accessor.find(PASSWORD);
      // prevent bogus state
      if (username == null || username.getValue() == null || username.getValue().isEmpty()) {
        error(method, DatabaseBundle.string(DatabaseError.INSUFFICIENT_INFORMATION, USERNAME));
        // Throws error
      }
      if (password == null || password.getValue() == null || password.getValue().isEmpty()) {
        error(method, DatabaseBundle.string(DatabaseError.INSUFFICIENT_INFORMATION, PASSWORD));
        // Throws error
      }
      Attribute status = accessor.find(STATUS);
      // prevent bogus state
      if (status == null || status.getValue() == null || status.getValue().isEmpty()) {
        warning(method, ""); // Fill out warning
        status = AttributeBuilder.build("active", Boolean.TRUE);
      }

      parameter.put(DatabaseDialect.USERNAME, (String) username.getValue().get(0));
      parameter.put(DatabaseDialect.PASSWORD, (String) password.getValue().get(0));
/*
      try {
        String template = this.context.dialectOperation(DatabaseDialect.Operation.ACCOUNT_CREATE);
        template = this.context.parseTemplate(template, STRING_PATTERN, Pattern.MULTILINE, parameter);
        // TO BE REMOVED: Add to IT resource
        this.context.connect();
        this.context.execute(template);
      }
      catch (SystemException e) {
        // report the exception in the log spooled for further investigation
        fatal(method, e);
        // send back the exception occured
        propagate(e);
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
        this.context.disconnect();
      }
*/
      return new Uid(parameter.get(DatabaseDialect.USERNAME));
    }
    else if (ObjectClass.GROUP.equals(type)) {
      return new Uid("11111111");
    }
    else {
      warning(DatabaseBundle.string(DatabaseError.OPERATION_NOT_SUPPORTED, method, type.getObjectClassValue()));
      trace(method, Loggable.METHOD_EXIT);
      throw new UnsupportedOperationException(DatabaseBundle.string(DatabaseError.OPERATION_NOT_SUPPORTED, method, type.getObjectClassValue()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (DeleteOp)
  /**
   ** Implements the operation interface to delete objects from the target
   ** resource.
   ** <br>
   ** The {@link Uid} must be provided.
   **
   ** @param  type               the type of object to delete.
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link ObjectClass}.
   ** @param  uid                the unique id that specifies the object to
   **                            delete.
   **                            Allowed object is {@link Uid}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            Allowed object is {@link OperationOptions}.
   */
  @Override
  public void delete(final ObjectClass type, final Uid uid, final OperationOptions option) {
    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);

    if (ObjectClass.ACCOUNT.equals(type)) {
      final Map <String, String> parameter = new HashMap<String, String>(2);
      // prevent bogus state
      if (uid == null || uid.getUidValue() == null)
        error(method, DatabaseBundle.string(DatabaseError.INSUFFICIENT_INFORMATION, USERNAME));

      parameter.put(DatabaseDialect.USERNAME, uid.getUidValue());
/*
      try {
        String template = this.context.dialectOperation(DatabaseDialect.Operation.ACCOUNT_DELETE);
        template = this.context.parseTemplate(template, STRING_PATTERN, Pattern.MULTILINE, parameter);
        System.out.println("template create: " + template);
        // TO BE REMOVED: Add to IT resource
        this.context.endpoint().rootContext("MDR1.vm.oracle.com");
        this.context.connect();
        this.context.execute(template);
      }
      catch (SystemException e) {
        // report the exception in the log spooled for further investigation
        fatal(method, e);
        // send back the exception occured
        propagate(e);
      }
      finally {
        trace(method, Loggable.METHOD_EXIT);
        this.context.disconnect();
      }
*/
      return;
    }
    else if (ObjectClass.GROUP.equals(type)) {
      return;
    }
    else {
      warning(DatabaseBundle.string(DatabaseError.OPERATION_NOT_SUPPORTED, method, type.getObjectClassValue()));
      trace(method, Loggable.METHOD_EXIT);
      throw new UnsupportedOperationException(DatabaseBundle.string(DatabaseError.OPERATION_NOT_SUPPORTED, method, type.getObjectClassValue()));
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
   ** @param  type               the type of object to modify.
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link ObjectClass}.
   ** @param  uid                the unique id that specifies the object to
   **                            modify.
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link Uid}.
   ** @param  replace            the {@link Set} of new {@link Attribute}s the
   **                            values in this set represent the new, merged
   **                            values to be applied to the object. This set
   **                            may also include operational attributes.
   **                            Must <b>not</b> be <code>null</code>.
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link Attribute}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    the unique id of the modified object in case
   **                            the update changes the formation of the unique
   **                            identifier.
   **                            Possible object {@link Uid}.
   */
  @Override
  public Uid update(final ObjectClass type, final Uid uid, final Set<org.identityconnectors.framework.common.objects.Attribute> replace, final OperationOptions option) {
    final String method = "update";
    trace(method, Loggable.METHOD_ENTRY);
    trace(method, Loggable.METHOD_EXIT);
    return new Uid("1111111111");
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
            // TODO
            //assignGroup(identifier, subject.getAttributes());
            ;
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
/*
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
*/
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
//          if (value != null && value.size() > 1)
//            throw DatabaseException.tooMany(cursor.getName());

          final EmbeddedObject subject = (EmbeddedObject)value.get(0);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            // TODO
            //revokeGroup(identifier, subject.getAttributes());
            ;
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
/*
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
*/
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return result;
  }
}