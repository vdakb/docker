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
    Subsystem   :   Directory Account Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.plx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.directory.SearchResult;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.DirectoryConfiguration;
import oracle.iam.identity.icf.connector.DirectoryCreate;
import oracle.iam.identity.icf.connector.DirectoryDelete;
import oracle.iam.identity.icf.connector.DirectoryEntry;
import oracle.iam.identity.icf.connector.DirectoryFilter;
import oracle.iam.identity.icf.connector.DirectoryLookup;
import oracle.iam.identity.icf.connector.DirectoryModify;
import oracle.iam.identity.icf.connector.DirectoryMove;
import oracle.iam.identity.icf.connector.DirectoryName;
import oracle.iam.identity.icf.connector.DirectoryRename;
import oracle.iam.identity.icf.connector.DirectorySchema;
import oracle.iam.identity.icf.connector.DirectorySearch;
import oracle.iam.identity.icf.connector.DirectoryService;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.resource.Connector;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.SyncResultsHandler;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;
////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the base functionality of an Identity Manager
 ** {@link DirectoryService} for a directoy account.
 ** <p>
 ** The life-cycle for the {@link DirectoryService} is as follows
 ** {@link #init(Configuration)} is called then any of the operations
 ** implemented in the {@link DirectoryService} and finally {@link #dispose()}.
 ** The {@link #init(Configuration)} and {@link #dispose()} allow for block
 ** operations. For instance bulk creates or deletes and the use of before and
 ** after actions. Once {@link #dispose()} is called the
 ** {@link DirectoryService} object is discarded.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=DirectoryConfiguration.class, displayNameKey=Connector.Bundle.NAME)
public class Main extends DirectoryService {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String CATEGORY = "JCS.CONNECTOR.GDS";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> directory connector that allows use
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
   ** @return                    basic schema supported by this Connector.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  @Override
  public Schema schema() {
    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    Schema schema = null;
    try {
      schema = DirectorySchema.build(this.endpoint).schema(this.getClass());
    }
    catch (SystemException e) {
      fatal(method, e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return schema;
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
   ** @param  type               the type of entry to resolve.
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
  public Uid resolveUsername(final ObjectClass type, final String name, final OperationOptions option) {
    final String method = "resolveUsername";
    trace(method, Loggable.METHOD_ENTRY);

    String indentifier = null;
    try {
      indentifier = DirectoryLookup.entryUUID(this.endpoint, type, name, option);
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
   ** getLatestSyncToken(ObjectClass) and then pass that token into this sync()
   ** method.
   **
   ** @param  objectClass        the class of object for which to return
   **                            synchronization events.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
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
   **                            <br>
   **                            Allowed object is {@link SyncToken}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SyncResultsHandler}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   */
  @Override
  public void sync(final ObjectClass objectClass, final SyncToken token, final SyncResultsHandler handler, final OperationOptions option) {
    final String method = "sync";
    trace(method, Loggable.METHOD_ENTRY);
    final Map<String, Object> control = option.getOptions();

    trace(method, Loggable.METHOD_EXIT);
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
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    a token if synchronization events exist;
   **                            otherwise <code>null</code>.
   **                            <br>
   **                            Possible object is {@link SyncToken}.
   */
  @Override
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
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  type               the type of entry to search.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  filter             the filter criteria to apply on the search and
   **                            converted from the {@link Filter} passed to
   **                            {@link #createFilterTranslator(ObjectClass, OperationOptions)}.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
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
  public void executeQuery(final ObjectClass type, final DirectoryFilter filter, final ResultsHandler handler, final OperationOptions option) {
    final String method = "executeQuery";
    trace(method, Loggable.METHOD_ENTRY);
    try {
    /* 
     * Finding a proxy organization entry required first to list the sub-entries
     * inside the root organization entry. From the returned set of entries, an
     * entry become a proxy organization if the entry itself is member of
     * one of the sub-entry root organization. Otherwise it is a group entry.
     */
      if (type.equals(DirectorySchema.PROXY)) {
        final Set<String>     proxyCandidate = DirectorySearch.build(this.endpoint, type, option, filter).proxyCandidate(this.endpoint.proxyOrganizationDN());
        final String          memberOfExp    = DirectoryEntry.composeFilter(this.endpoint.distinguishedNameAttr(), proxyCandidate);
        final DirectoryFilter filterMemberOf = DirectoryFilter.or(memberOfExp);
        
        DirectorySearch.build(this.endpoint, type, option, filter != null ? filter.and(filterMemberOf) : filterMemberOf).execute(handler);
      }
      else if (type.equals(ObjectClass.GROUP)) {
        final Set<String>     proxyCandidate    = DirectorySearch.build(this.endpoint, type, option, filter).proxyCandidate(this.endpoint.proxyOrganizationDN());
        final String          memberOfExp       = DirectoryEntry.composeFilter(this.endpoint.distinguishedNameAttr(), proxyCandidate);
        final DirectoryFilter filterNotMemberOf = DirectoryFilter.not(DirectoryFilter.or(memberOfExp));
        
        DirectorySearch.build(this.endpoint, type, option, filter != null ? filter.and(filterNotMemberOf) : filterNotMemberOf).execute(handler);
      }
      else {
        DirectorySearch.build(this.endpoint, type, option, filter).execute(handler);
      }
    }
    catch (SystemException e) {
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
   ** @param  type               the type of entry to create.
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
   ** @param  operation          the options that affect the way this operation
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
  public Uid create(final ObjectClass type, final Set<Attribute> attribute, final OperationOptions operation)
    throws ConnectorException {

    final String method = "create";
    trace(method, Loggable.METHOD_ENTRY);
    Uid uid = null;
    try {
      // OpenLDAP does not allow empty value in attributes
      // So, before creating removing every empty attribute
      uid = DirectoryCreate.build(this.endpoint, type).execute(attribute.stream().filter(attr -> attr.getValue().size() > 0 && !attr.getValue().get(0).toString().isEmpty()
                                                                                                 && !attr.getName().equals(this.endpoint.distinguishedNamePrefix())).collect(Collectors.toSet()));
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
  // Method:   delete (DeleteOp)
  /**
   ** calling the native delete methods to remove the object specified by its
   ** unique id.
   **
   ** @param  type               the type of entry to delete.
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

    final String method = "delete";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      DirectoryDelete.build(this.endpoint, type, false).execute(uid);
    }
    catch (SystemException e) {
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
   ** @param  type               the type of entry to delete.
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
   **                            Possible object is {@link Uid}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public Uid update(final ObjectClass type, final Uid uid, final Set<Attribute> replace, final OperationOptions option)
    throws ConnectorException {

    final String method = "update";
    trace(method, Loggable.METHOD_ENTRY);
    Uid                identifier = null;
    List<SearchResult> result     = null;
    DirectoryName      oldDN = null;
    
    Set<Attribute> relatedAttribute = replace.stream().filter(attr -> StringUtility.equal(attr.getName(), this.endpoint.accountPrefix())
                                                                   || StringUtility.equal(attr.getName(), this.endpoint.distinguishedNamePrefix())).collect(Collectors.toSet());
    try {
      // As openldap does not modify related attributes check if this update
      // modifies only simple attribute or related attribute.
      // First save the DN to modify.
      if (!relatedAttribute.isEmpty()) {
        oldDN = DirectoryLookup.entryName(this.endpoint, type, uid.getUidValue());
        
        final DirectoryFilter         memberFilter = DirectoryFilter.expression(DirectoryEntry.composeFilter(this.endpoint.groupMember(), DirectoryEntry.escape(oldDN.toString())));
        final OperationOptionsBuilder opBuilder    = new OperationOptionsBuilder().setAttributesToGet(CollectionUtility.set(this.endpoint.groupMember(), this.endpoint.objectClassName()))
                                                                                  .setContainer(new QualifiedUid(ObjectClass.GROUP, new Uid(DirectoryEntry.removeRootContext(this.endpoint.homeOrganizationDN(), this.endpoint.rootContext()))));
        
        result = DirectorySearch.build(this.endpoint, DirectorySchema.ANY, opBuilder.build(), memberFilter).list();
      }
      
      final Set<Attribute>  attributes   = new HashSet<>();
      for (Attribute cursor : replace) {
        if (StringUtility.equal(cursor.getName(), this.endpoint.accountPrefix())) {
          DirectoryRename.build(this.endpoint, type).execute(uid, cursor);
        }
        else if (StringUtility.equal(cursor.getName(), this.endpoint.distinguishedNamePrefix())) {
          DirectoryName origin = oldDN;
          DirectoryName target = DirectoryName.build((String)cursor.getValue().get(0));
          DirectoryMove.build(this.endpoint, type).execute(origin, target);
        }
        else {
          attributes.add(cursor);
        }
      }
      // If only rename then don't need to go futher
      //if (attributes.isEmpty())
        //return uid;
      
      identifier = DirectoryModify.build(this.endpoint, type).execute(uid, attributes);
      
      // Before returning, modify entry members with new DN.
      if (!relatedAttribute.isEmpty()) {
        final DirectoryName newDN = DirectoryLookup.entryName(endpoint, type, uid.getUidValue());
        
        for (SearchResult entry : result) {
          final String         uuid        = DirectoryEntry.value(entry, this.endpoint.entryIdentifierAttribute());
          final DirectoryEntry entryMember = DirectoryEntry.build(entry);
          final Set<String>    values      = entryMember.values(this.endpoint.groupMember());
          final List<Object>   membersDN    = new ArrayList<Object>();
          for (String dn : values) {
            if (dn.equals(oldDN.toString()))
              membersDN.add(newDN.toString());
            else
              membersDN.add(dn);
          }
          final AttributeBuilder attrMember = new AttributeBuilder().setName(this.endpoint.groupMember())
                                                                    .addValue(membersDN);
          DirectoryModify.build(this.endpoint, type).execute(new Uid(uuid), CollectionUtility.set(attrMember.build()));
        }
      }
    }
    catch (SystemException e) {
      fatal(method, e);
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
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
   ** @param  type               the type of entry to modify.
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

    final String method = "addAttributeValues";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // dispatch to the appropriate method
      DirectoryModify.build(this.endpoint, type).assign(uid, addition);
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    // the identifier of the entry to modify will never update by this operation
    // hence its allowed to return the passed in identifier
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
   ** @param  type               the type of entry to modify.
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

    final String method = "removeAttributeValues";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // dispatch to the appropriate method
      DirectoryModify.build(this.endpoint, type).revoke(uid, remove);
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    // the identifier of the entry to modify will never update by this operation
    // hence its allowed to return the passed in identifier
    return uid;
  }
}