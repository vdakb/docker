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
    Subsystem   :   Generic Directory Connector

    File        :   DirectorySynchronization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectorySynchronization.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import oracle.iam.identity.icf.foundation.OperationContext;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.resource.DirectoryBundle;

import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.SyncResultsHandler;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.Uid;
////////////////////////////////////////////////////////////////////////////////
// abstract class DirectorySynchronization
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DirectorySynchronization</code> provides the capabilities to control a
 ** LDAP synchroniaztion strategy based on either timestamp filter or changeLog
 ** access.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DirectorySynchronization extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instane attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final int              scope;
  protected final DirectoryName    base;
  protected final OperationContext control;
  protected final Set<ObjectClass> groups      = new HashSet<ObjectClass>();
  protected final Set<String>      auditFilter = new HashSet<String>();

  protected final String           entryIdentifier;
  protected final String           entryCreatorAttribute;
  protected final String           entryCreatedAttribute;
  protected final String           entryModifierAttribute;
  protected final String           entryModifiedAttribute;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // interface Handler
  // ~~~~~~~~~ ~~~~~~~
  /**
   ** Transforms the data received from the Directory Service and wrapped in the
   ** specified {@link SearchResult} <code>searchResult</code> to the
   ** {@link SyncDelta} instance.
   */
  static interface Handler {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link SearchResult} that is received from the Directory Service.
     **
     ** @param  object           an object returned from the search.
     **                          <br>
     **                          Allowed object is {@link SearchResult}.
     **
     ** @return                  <code>true</code> if we should keep processing;
     **                          otherwise <code>false</code> to cancel.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws SystemException  if the passed object couldn't be handled.
     */
    boolean handle(final SearchResult object)
      throws SystemException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>DirectorySynchronization</code> that provides the
   ** capabilities to control a LDAP search.
   **
   ** @param  endpoint           the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to search the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  base               the base DN to search from
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  scope              the search scope that control the search.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  control            the options that affect the way this operation
   **                            is run.
   **                            <br>
   **                            Allowed object is {@link OperationContext}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  public DirectorySynchronization(final DirectoryEndpoint endpoint, final ObjectClass type, final DirectoryName base, final int scope, OperationContext control)
    throws SystemException {

    // ensure inheritance
    super(endpoint, type);
    
    this.scope   = scope;
    this.base    = base;
    this.control = control;
    
    this.entryIdentifier        = endpoint.entryIdentifierAttribute();
    this.entryCreatorAttribute  = endpoint.entryCreatorAttribute();
    this.entryCreatedAttribute  = endpoint.entryCreatedAttribute();
    this.entryModifierAttribute = endpoint.entryModifierAttribute();
    this.entryModifiedAttribute = endpoint.entryModifiedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////
      
  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastToken
  /**
   ** Returns the token corresponding to the most recent synchronization event.
   ** <p>
   ** An application that wants to receive synchronization events
   ** "starting now" -- i.e., wants to receive only native changes that occur
   ** after this method is called -- should call this method and then pass the
   ** resulting token into the sync() method.
   **
   ** @return                    the token corresponding to the most recent
   **                            synchronization event.
   **                            <br>
   **                            Possible object is {@link SyncToken}.
   **
   ** @throws SystemException    if its fails to get to obtain the most recent
   **                            synchronization event.
   */
  public abstract SyncToken lastToken()
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a proper search strategy for entries in a
   ** Directory Service.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to search the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  option             the options that affect the way this operation
   **                            is run.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    an instance of <code>Paginated</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is
   **                            {@link DirectorySynchronization}.
   **
   ** @throws SystemException   if the appropriate Request Controls cannot be
   **                           initialized.
   */
  public static DirectorySynchronization build(final DirectoryEndpoint context, final ObjectClass type, final OperationOptions option)
    throws SystemException {

    // initialize the operation context configuration
    final OperationContext control = OperationContext.build(option);
    switch (control.synchronization) {
      case CHANGELOG : return new DirectoryChangeLog(context, type, control);
      case RESOURCE  : 
      default        : return new DirectoryResource(context, type, DirectoryName.build(control.base.getUid().getUidValue()), DirectorySearch.SCOPE.get(control.scope), control);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
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
   **
   ** @throws SystemException    if its fails to get to get access to the
   **                            ChangeLog of the Directory Service.
   */
  public abstract void execute(final SyncToken token, final SyncResultsHandler handler)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsAny
  protected static boolean containsAny(final Set<String> haystack, final Set<String> needles) {
    for (String needle : needles) {
      if (haystack.contains(needle)) {
        return true;
      }
    }
    return false;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified {@link SearchResult} <code>searchResult</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  result             the {@link SearchResult} providing the data.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link SearchResult}.
   **
   ** @return                    the data transfer object to pass back.
   **                            <br>
   **                            Possible object is array of
   **                            {@link ConnectorObject}.
   **
   ** @throws SystemException    if the {@link ConnectorObject} could not build
   **                            from the given {@link SearchResult}
   **                            <code>result</code>.
   */
  protected ConnectorObject connectorObject(final SearchResult result)
    throws SystemException {

    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(this.type);
    final Attributes             properties = result.getAttributes();
    // verify that we have a valid result set
    if (properties.size() == 0 ||this.control.emit.size() == 0) {
      return builder.build();
    }
    try {
      // always add the unique identifier to the connector object
      builder.setUid(createIdentifier(result));
      // always add the public identifier to the connector object depending
      builder.setName(result.getNameInNamespace());
      for (String cursor : this.control.emit) {
        // skip naming attribute
        if (Uid.NAME.equals(cursor) || Name.NAME.equals(cursor))
          continue;

        final Attribute attribute = properties.get(cursor);
        // if the requested attribute isn't in the search result omit this value
        if (attribute != null) {
          final AttributeBuilder value = new AttributeBuilder().setName(cursor);
          for (int i = 0; i < attribute.size(); i++) {
            value.addValue(attribute.get(i));
          }
          builder.addAttribute(value.build());
        }
      }
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterByModifier
  /**
   ** This method extracts the modifier and creator attribute mapping and
   ** delgates to the other method with the same name.
   **
   ** @param  entry              an LDAP entry.
   **                            <br>
   **                            Allowed object is {@link DirectoryEntry}.
   **
   ** @return                    <code>true</code> if the modifiers name needs
   **                            to be filtered out.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SystemException    if the modifier or creator attribute could not
   **                            be extracted.
   */
  protected final boolean filterByModifier(final SearchResult entry)
    throws SystemException {

    String modifier = DirectoryEntry.value(entry, this.entryModifierAttribute);
    if (modifier == null) {
      warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_FILTER_SWITCH, this.entryModifierAttribute, this.entryCreatorAttribute));

      // OUD does not set modifiers name during entry creation
      modifier = DirectoryEntry.value(entry, this.entryCreatorAttribute);
      if (modifier == null) {
        warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_FILTER_NOTSET, this.entryCreatorAttribute));
        return false;
      }
    }
    return this.auditFilter.contains(modifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterByModifier
  /**
   ** This method extracts the modifier and creator attribute mapping and
   ** delgates to the other method with the same name.
   **
   ** @param  changes            the attribute mapping obtained from a changeLog
   **                            entry.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link List} as the value.
   **
   ** @return                    <code>true</code> if the modifiers name needs
   **                            to be filtered out.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SystemException    if the modifier or creator attribute could not
   **                            be extracted.
   */
  protected final boolean filterByModifier(final Map<String, List<Object>> changes)
    throws SystemException {

    List<?> modifier = changes.get(this.entryModifierAttribute);
    if (CollectionUtility.empty(modifier)) {
      warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_FILTER_SWITCH, this.entryModifierAttribute, this.entryCreatorAttribute));

      // OUD does not set modifiers name during entry creation
      modifier = changes.get(this.entryCreatorAttribute);
      if (CollectionUtility.empty(modifier)) {
        warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_FILTER_NOTSET, this.entryCreatorAttribute));
        return false;
      }
    }
    return this.auditFilter.contains(modifier.get(0));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  private Uid createIdentifier(final SearchResult result)
    throws SystemException {

    if (this.endpoint.distinguishedName().contains(this.entryIdentifier)) {
      return new Uid(result.getNameInNamespace());
    }
    else {
     return createIdentifier(this.entryIdentifier, result.getAttributes());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  private Uid createIdentifier(final String entryIdentifier, final Attributes attributes)
    throws SystemException {

    String value = "";
    if (this.endpoint.binary().contains(entryIdentifier)) {
//      value = LdapUtil.getHexStringByteArrayAttrValue(attributes, entryIdentifier, conn);
    }
    else {
      value = DirectoryEntry.value(attributes, entryIdentifier);
    }
    return (value == null) ? null : new Uid(value);
  }
}