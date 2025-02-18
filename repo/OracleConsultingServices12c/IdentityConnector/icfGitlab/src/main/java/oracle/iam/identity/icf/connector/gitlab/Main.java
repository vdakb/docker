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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   GitLab Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.gitlab;

import java.util.Set;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceConnector;
import oracle.iam.identity.icf.rest.ServiceConfiguration;
import oracle.iam.identity.icf.rest.NoneFilterTranslator;

import oracle.iam.identity.icf.connector.gitlab.schema.Service;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link ServiceConnector} for a GitLab core system.
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
@ConnectorClass(configurationClass=ServiceConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.gitlab.Main.properties")
public class Main extends ServiceConnector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String CATEGORY     = "JCS.CONNECTOR.GIT";

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
   ** Constructs a default <code>Main</code> GitLab Connector that allows use as
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
    entering(method);
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
      exiting(method);
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
    entering(method);
    this.config  = null;
    this.context = null;
    exiting(method);
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
    // intentionally left blank
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
    entering(method);
    try {
      if (this.schema == null)
        this.schema = Service.instance.schema(Main.class);
    }
    finally {
      exiting(method);
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
    entering(method);
    try {
      return NoneFilterTranslator.<String>build();
    }
    finally {
      exiting(method);
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
    entering(method);
    exiting(method);
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
    entering(method);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }

    exiting(method);
    return new Uid("4711");
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
    entering(method);
    exiting(method);
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
    entering(method);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(replace));
    }

    Uid result = null;
    exiting(method);
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
    entering(method);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(addition));
    }
    exiting(method);
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
    entering(method);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(remove));
    }
    exiting(method);
    return uid;
  }
}
