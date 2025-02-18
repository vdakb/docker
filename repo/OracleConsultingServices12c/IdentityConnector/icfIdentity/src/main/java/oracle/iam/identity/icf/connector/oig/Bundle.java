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

    File        :   Bundle.java
    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Bundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.oig;

import java.util.Date;
import java.util.List;
import java.util.Set;

import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.identity.icf.connector.oig.schema.ServiceClass;
import oracle.iam.identity.icf.connector.oig.schema.ServiceSchema;
import oracle.iam.identity.icf.connector.oig.schema.Translator;
import oracle.iam.identity.icf.foundation.OperationContext;
import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.utility.DateUtility;
import oracle.iam.identity.icf.foundation.utility.SchemaUtility;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.jes.ServerConnector;
import oracle.iam.identity.icf.jes.ServerEndpoint;
import oracle.iam.identity.icf.jes.ServerMessage;
import oracle.iam.identity.icf.resource.ServerBundle;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.vo.Identity;
import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.operations.ResolveUsernameOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.TestOp;

////////////////////////////////////////////////////////////////////////////////
// abstract class Bundle
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link ServerConnector} for a JES application system.
 ** <p>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Bundle extends    ServerConnector
                             implements TestOp
                             ,          SearchOp<SearchCriteria>
                             ,          ResolveUsernameOp {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String CATEGORY = "JCS.CONNECTOR.OIG";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The object that implements the given interface to allow access to
   ** non-standard methods, or standard methods not exposed by the proxy.
   */
  protected Service           context  = null;

  /**
   ** The {@link Schema} cached over the lifetime of the task.
   */
  protected Schema            schema   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Batch
  // ~~~~~ ~~~~~
  /**
   ** Batch provides the capabilities to control the result set size the
   ** EntityManager returns
   */
  public static class Batch {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int limit;
    private int       start = 1;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a <code>Batch</code> instance with the given start and limit.
     **
     ** @param  start            the start row of a page to fetch from the
     **                          backend.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  limit            the desired size of a result set.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    public Batch(final int start, final int limit) {
      this.start = (start == Integer.MIN_VALUE || start < 0) ? 1 : start;
      this.limit = (limit == Integer.MIN_VALUE || limit < 0) ? 1 : limit;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   start
    /**
     ** Returns the start row of an expected result set.
     **
     ** @return                  the start row of a page to fetch from the
     **                          backend.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int start() {
      return this.start;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   end
    /**
     ** Returns the end row of an expected result set.
     **
     ** @return                  the end row of a page to fetch from the
     **                          backend.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int end() {
      return (this.start - 1) + this.limit;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   limit
    /**
     ** Returns the size of an expected result set.
     **
     ** @return                  the desired size of a result set.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int limit() {
      return this.limit;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   next
    /**
     ** Evaluates the start attributes to the next row based on the size of the
     ** expected result set size.
     */
    public void next() {
      this.start += this.limit;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Bundle</code> JES connector that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Bundle() {
    // ensure inheritance
    super(CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispose (Connector)
  /**
   ** Dispose of any resources the Connector uses.
   */
  @Override
  public final void dispose() {
    final String method = "dispose";
    entering(method);
    final ServerEndpoint endpoint = this.context.endpoint();
    debug(method, ServerBundle.string(ServerMessage.LOGGINGOUT, endpoint.serviceURL(), endpoint.principalUsername()));
    this.context.logout();
    debug(method, ServerBundle.string(ServerMessage.LOGGEDOUT, endpoint.serviceURL(), endpoint.principalUsername()));
    debug(method, ServerBundle.string(ServerMessage.DISCONNECTING, endpoint.serviceURL()));
    // release connection to the pool and free up resources
    this.context.shutdown();
    debug(method, ServerBundle.string(ServerMessage.DISCONNECTED, endpoint.serviceURL()));
    this.context = null;
    this.config  = null;
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
  public final FilterTranslator<SearchCriteria> createFilterTranslator(final ObjectClass type, final OperationOptions option) {
    final String method = "createFilterTranslator";
    entering(method);
    try {
      return new FilterTranslator<SearchCriteria>() {
        /**
         ** Main method to be called to translate a filter.
         */
        @Override
        public List<SearchCriteria> translate(final Filter filter) {
          return Translator.build(type).translate(filter);
        }
      };
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
   ** @param  type               the class of object for which to return
   **                            synchronization events.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  criteria           the criteria to apply on the search and
   **                            converted from the {@link Filter} passed to
   **                            {@link #createFilterTranslator(ObjectClass, OperationOptions)}.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
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
  public final void executeQuery(final ObjectClass type, final SearchCriteria criteria, final ResultsHandler handler, final OperationOptions option) {
    final String method = "executeQuery";
    entering(method);
    // initialize the operations control configuration
    final OperationContext control = OperationContext.build(option);
    try {
      if (ObjectClass.ACCOUNT.equals(type)) {
        searchAccount(control, criteria, handler);
      }
      else if (ObjectClass.GROUP.equals(type)) {
        searchRole(control, criteria, handler);
      }
      else if (ServiceClass.TENANT.equals(type)) {
        searchOrganization(control, criteria, handler);
      }
      else if (ServiceClass.GLOBAL.equals(type)) {
        searchGlobalRole(handler);
      }
      else if (ServiceClass.SCOPED.equals(type)) {
        searchScopedRole(handler);
      }
      else if (ServiceClass.OU_ROLE.equals(type)) {
        searchOURole(control, criteria, handler);
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
      exiting(method);
    }
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
  public final Uid resolveUsername(final ObjectClass type, final String name, final OperationOptions option) {
    // prevent bogus input
    if (name == null || name.isEmpty())
      valuesRequired();

    final String method = "resolveUsername";
    entering(method);
    // execute operation
    String identifier = null;
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        final Identity identity = this.context.lookupUser(name);
        identifier = identity.getEntityId();
      }
      else if (ObjectClass.GROUP.equals(type)) {
        final Identity identity = this.context.lookupSystemRole(name);
        identifier = identity.getEntityId();
      }
      else if (ServiceClass.TENANT.equals(type)) {
        final Identity identity = this.context.lookupOrganization(name);
        identifier = identity.getEntityId();
      }
      else if (ServiceClass.GLOBAL.equals(type)) {
        // Note:
        // we need to lookup by the role name of the admin role in scope not the
        // system identifier
        final AdminRole identity = this.context.lookupGlobalRole(name);
        identifier = String.valueOf(identity.getRoleId());
      }
      else if (ServiceClass.SCOPED.equals(type)) {
        // Note:
        // we need to lookup by the role name of the admin role in scope not the
        // system identifier
        final AdminRole identity = this.context.lookupScopedRole(name);
        identifier = String.valueOf(identity.getRoleId());
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
      exiting(method);
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
  public final Uid create(final ObjectClass type, final Set<Attribute> attribute, final OperationOptions option)
    throws ConnectorException {

    final String method = "create";
    entering(method);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel()) {
      debug(method, StringUtility.formatCollection(attribute));
    }

    // execute operation
    String identifier = null;
    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    Name name = accessor.getName();
    if (name == null) {
      name = new Name(SchemaUtility.notNullStringValue(accessor.find(ServiceSchema.IDENTIFIER)));
    }
    // prevent bogus state
    if (name == null) {
      exiting(method);
      propagate(SystemError.NAME_IDENTIFIER_REQUIRED, ServiceSchema.IDENTIFIER);
    }

    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        identifier = this.context.createUser(name.getNameValue());
      }
      else if (ObjectClass.GROUP.equals(type)) {
        identifier = this.context.createSystemRole(name.getNameValue());
      }
      else if (ServiceClass.TENANT.equals(type)) {
        identifier = this.context.createOrganization(name.getNameValue());
      }
      else if (ServiceClass.GLOBAL.equals(type)) {
        identifier = String.valueOf(this.context.createGlobalRole(name.getNameValue()));
      }
      else if (ServiceClass.SCOPED.equals(type)) {
        identifier = String.valueOf(this.context.createScopedRole(name.getNameValue()));
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
      exiting(method);
    }
    return new Uid(identifier);
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
  public final void delete(final ObjectClass type, final Uid uid, final OperationOptions option)
    throws ConnectorException {

    final String method = "delete";
    entering(method);
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        this.context.deleteUser(uid.getUidValue());
      }
      else if (ObjectClass.GROUP.equals(type)) {
        this.context.deleteSystemRole(uid.getUidValue());
      }
      else if (ServiceClass.TENANT.equals(type)) {
        this.context.deleteOrganization(uid.getUidValue());
      }
      else if (ServiceClass.GLOBAL.equals(type)) {
        this.context.deleteGlobalRole(Long.valueOf(uid.getUidValue()));
      }
      else if (ServiceClass.SCOPED.equals(type)) {
        this.context.deleteScopedRole(Long.valueOf(uid.getUidValue()));
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
      exiting(method);
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
  public final Uid update(final ObjectClass type, final Uid uid, final Set<Attribute> replace, final OperationOptions option)
    throws ConnectorException {

    final String method = "update";
    entering(method);
    // execute operation
    Uid identifier = null;
    exiting(method);
    // will enforce NPE
    return identifier;
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
  public final Uid addAttributeValues(final ObjectClass type, final Uid uid, final Set<Attribute> addition, final OperationOptions option)
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

    // execute operation
    try  {
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : addition) {
          final EmbeddedObject subject = (EmbeddedObject)cursor.getValue().get(0);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            assignSystemRole(identifier, subject.getAttributes());
          }
          else if (ServiceClass.GLOBAL.equals(subject.getObjectClass())) {
            assignGlobalRole(identifier, subject.getAttributes());
          }
          else if (ServiceClass.SCOPED.equals(subject.getObjectClass())) {
            assignScopedRole(identifier, subject.getAttributes());
          }
          else if (ServiceClass.OU_ROLE.equals(subject.getObjectClass())) {
            // No Action is needed
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
      exiting(method);
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
  public final Uid removeAttributeValues(final ObjectClass type, final Uid uid, final Set<Attribute> remove, final OperationOptions option)
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

    // execute operation
    try {
      // dispatch to the appropriate method
      if (ObjectClass.ACCOUNT.equals(type)) {
        for (Attribute cursor : remove) {
          final EmbeddedObject subject = (EmbeddedObject)cursor.getValue().get(0);
          if (ObjectClass.GROUP.equals(subject.getObjectClass())) {
            revokeSystemRole(identifier, subject.getAttributes());
          }
          else if (ServiceClass.GLOBAL.equals(subject.getObjectClass())) {
            revokeGlobalRole(identifier, subject.getAttributes());
          }
          else if (ServiceClass.SCOPED.equals(subject.getObjectClass())) {
            revokeScopedRole(identifier, subject.getAttributes());
          }
          else if (ServiceClass.OU_ROLE.equals(subject.getObjectClass())) {
            revokeOURole(identifier, subject.getAttributes());
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
      exiting(method);
    }
    return uid;
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
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected final void searchAccount(final OperationContext control, final SearchCriteria criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchAccount";
    entering(method);
    // go an extra mile to exclude the embedded object like tenants and
    // systemroles etc. from the query
    final boolean systemroles = control.emit.contains(ObjectClass.GROUP_NAME);
    final boolean globalroles = control.emit.contains(ServiceClass.GLOBAL_NAME);
    final boolean scopedroles = control.emit.contains(ServiceClass.SCOPED_NAME);

    // setup the filter criteria to apply on the search
    SearchCriteria filter = null;
    if (control.incremental && control.synchronizationToken != null) {
      // the expected format of the synchronization token must be compliant to
      // RFC 4517
      final Date timestamp = DateUtility.parse(control.synchronizationToken, DateUtility.RFC4517_ZULU_NANO);
      filter = new SearchCriteria(
        // the date on which the user has been created
        new SearchCriteria("usr_create", timestamp, SearchCriteria.Operator.GREATER_THAN)
        // the date on which the user has been last updated
      , new SearchCriteria("usr_update", timestamp, SearchCriteria.Operator.GREATER_THAN)
      , SearchCriteria.Operator.OR
      );
    }

    final Batch batch    = new Batch(control.start, control.limit);
    boolean     stopped  = false;
    List<User>  response = null;
    try {
      do {
        response = this.context.searchUser(criteria == null ? filter : new SearchCriteria(filter, criteria, SearchCriteria.Operator.AND), batch.start, batch.end());
        for (User cursor : response) {
          final String identity = cursor.getEntityId();
          // build connector object and submit to handler
          final ConnectorObject payload = ServiceSchema.transfer(
            cursor
          , systemroles ? this.context.userSystemRole(identity) : null
          , globalroles ? this.context.userGlobalRole(identity) : null
          , scopedroles ? this.context.userScopedRole(identity) : null
          );
          if (!handler.handle(payload)) {
            // ensure outer loop break condition will reached
            stopped = true;
            break;
          }
        }
        // position the batch to the next window
        batch.next();
      } while (control.limit <= response.size());
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
  // Method:   searchRole
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
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected final void searchRole(final OperationContext control, final SearchCriteria criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchRole";
    entering(method);

    // apply a search filter on the audit timestamps on the role entity doesn't
    // work hence we create a timestamp value to compare the returned entities
    // the expected format of the synchronization token must be compliant to
    // RFC 4517
    final Date timestamp = (control.incremental && control.synchronizationToken != null)
      ?   DateUtility.parse(control.synchronizationToken, DateUtility.RFC4517_ZULU_NANO)
      :   DateUtility.startEpoch();
    try {
      final Batch batch    = new Batch(control.start, control.limit);
      boolean     stopped  = false;
      List<Role>  response = null;
      do {
        response = this.context.searchSystemRole(criteria, batch.start, batch.end());
        for (Role cursor : response) {
          // if the audit column of the update date is before the timestamp we
          // can assume that the create date will also be before (othewise a
          // suspisciuos person tweaked the database and have to live with that;
          // we don't care)
          if (timestamp.before(ServiceSchema.convert(RoleManagerConstants.ROLE_CREATE_DATE, cursor, Date.class))
          || timestamp.before(ServiceSchema.convert(RoleManagerConstants.ROLE_UPDATE_DATE, cursor, Date.class))) {
            // build connector object and submit to handler
            final ConnectorObject payload = ServiceSchema.transfer(cursor);
            if (!handler.handle(payload)) {
              // ensure outer loop break condition will reached
              stopped = true;
              break;
            }
          }
        }
        // position the batch to the next window
        batch.next();
      } while (!stopped && batch.limit <= response.size());
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
  // Method:   assignSystemRole
  /**
   ** Assigns an <code>Identity</code> to a standard role in Identity
   ** Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
  protected final void assignSystemRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    // prevent bogus state
    final String subject = accessor.findString(Uid.NAME);
    if (subject == null)
      identifierRequired();

    this.context.assignSystemRole(subject, beneficiary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeSystemRole
  /**
   ** Revokes an <code>Identity</code> from a {@link Role} in Identity
   ** Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
  protected final void revokeSystemRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    // prevent bogus state
    final String subject = accessor.findString(Uid.NAME);
    if (subject == null)
      identifierRequired();

    this.context.revokeSystemRole(subject, beneficiary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchGlobalRole
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
  protected final void searchGlobalRole(final ResultsHandler handler)
    throws SystemException {

    final String method = "searchGlobalRole";
    entering(method);
    try {
      final List<AdminRole> response = this.context.searchGlobalRole();
      for (AdminRole cursor : response) {
        // build connector object and submit to handler
        final ConnectorObject payload = ServiceSchema.transfer(cursor, true);
        if (!handler.handle(payload))
          break;
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignGlobalRole
  /**
   ** Assigns an <code>Identity</code> to an global {@link AdminRole} in
   ** Identity Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
  protected final void assignGlobalRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    // prevent bogus state
    final String subject = accessor.findString(Uid.NAME);
    if (subject == null)
      identifierRequired();

    this.context.assignGlobalRole(beneficiary, Long.valueOf(subject));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeGlobalRole
  /**
   ** Revokes an <code>Identity</code> from an global {@link AdminRole} in
   ** Identity Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
  protected final void revokeGlobalRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    // prevent bogus state
    final String subject = accessor.findString(Uid.NAME);
    if (subject == null)
      identifierRequired();

    this.context.revokeGlobalRole(beneficiary, Long.valueOf(subject));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchScopedRole
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
   */
  protected final void searchScopedRole(final ResultsHandler handler) {
    final String method = "searchScopedRole";
    entering(method);
    try {
      final List<AdminRole> response = this.context.searchScopedRole();
      for (AdminRole cursor : response) {
        // build connector object and submit to handler
        final ConnectorObject payload = ServiceSchema.transfer(cursor, false);
        if (!handler.handle(payload))
          break;
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
  // Method:   assignScopedRole
  /**
   ** Assigns an <code>Identity</code> to an global {@link AdminRole} in
   ** Identity Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
  protected final void assignScopedRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    // prevent bogus state
    final String subject = accessor.findString(Uid.NAME);
    if (subject == null)
      identifierRequired();

    final String scope = accessor.findString("scope");
    if (scope == null)
      nameRequired("scope");

    final Boolean hierarchy = accessor.findBoolean("hierarchy");
    if (hierarchy == null)
      nameRequired("hierarchy");

    this.context.assignScopedRole(beneficiary, Long.valueOf(subject), scope, hierarchy);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeScopedRole
  /**
   ** Revokes an <code>Identity</code> from an global {@link AdminRole} in
   ** Identity Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
  protected final void revokeScopedRole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    // prevent bogus input
    final String subject = accessor.findString(Uid.NAME);
    if (subject == null)
      identifierRequired();

    this.context.revokeScopedRole(beneficiary, Long.valueOf(subject));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchOrganization
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
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected final void searchOrganization(final OperationContext control, final SearchCriteria criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchOrganization";
    entering(method);

    // we cannot apply a search filter omnn the audit timestamps on the
    // organization entity (Organization Manager will reject the search so we
    // creating a timestamp value to compare the returned entities
    // the expected format of the synchronization token must be compliant to
    // RFC 4517
    final Date timestamp = (control.incremental && control.synchronizationToken != null)
      ?   DateUtility.parse(control.synchronizationToken, DateUtility.RFC4517_ZULU_NANO)
      :   DateUtility.startEpoch();
    try {
      final Batch        batch    = new Batch(control.start, control.limit);
      boolean            stopped  = false;
      List<Organization> response = null;
      do {
        response = this.context.searchOrganization(criteria, batch.start, batch.end());
        for (Organization cursor : response) {
          // if the audit column of the update date is before the timestamp we
          // can // assume that the create date will also be before (othewise a
          // suspisciuos person tweaked the database and hav to live with that;
          // we don't care)
          if (timestamp.before(ServiceSchema.convert(OrganizationManagerConstants.ORG_UPDATE_ON, cursor, Date.class))
          || timestamp.before(ServiceSchema.convert(OrganizationManagerConstants.ORG_CREATE_ON, cursor, Date.class))) {
            // build connector object and submit to handler
            final ConnectorObject payload = ServiceSchema.transfer(cursor);
            if (!handler.handle(payload)) {
              // ensure outer loop break condition will reached
              stopped = true;
              break;
            }
          }
        }
        // position the batch to the next page
        batch.next();
      } while (!stopped && batch.limit <= response.size());
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
  // Method:   revokeOUdRole
  /**
   ** Revokes an <code>Identity</code> from an Organization Role {@link AccessPolicy} in
   ** Identity Governance.
   **
   ** @param  beneficiary        the identifier of a <code>User</code> identity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            This has to be the system identifier of a user
   **                            not the login name.
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
  protected final void revokeOURole(final String beneficiary, final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    // prevent bogus input
    final String accessPolicyKey = accessor.findString(Uid.NAME);
    final String ouName = accessor.findString("Org Unit");
    if (accessPolicyKey == null)
      identifierRequired();

    this.context.revokeOURole(beneficiary, Long.valueOf(accessPolicyKey),ouName);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchOURole
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
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   **
   ** @throws SystemException    if the operation fails.
   */
  protected final void searchOURole(final OperationContext control, final SearchCriteria criteria, final ResultsHandler handler)
    throws SystemException {

    final String method = "searchOURole";
    entering(method);
    try {
      final List<AccessPolicy> response = this.context.searchOURole(criteria);
      for (AccessPolicy cursor : response) {
        // build connector object and submit to handler
        final ConnectorObject payload = ServiceSchema.transfer(cursor);
        if (!handler.handle(payload))
          break;
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
  
  
  
  
  
}