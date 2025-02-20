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

    File        :   DirectoryLookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryLookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;
import java.util.List;

import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptions;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryLookup
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>Lookup</code> provides the capabilities to control a query based on
 ** the Paged Result Request Control.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryLookup extends DirectorySearch.Simple {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>DirectoryLookup</code> Search that provides no further
   ** capabilities to control the query.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to lookup the entry in
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
   ** @param  filter             the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  emit               the identifiers of the attributes to return
   **                            along with the entry. If <code>null</code>,
   **                            return all attributes. If empty return no
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the appropriate Request Controls cannot be
   **                            initialized.
   */
  private DirectoryLookup(final DirectoryEndpoint context, final ObjectClass type, final DirectoryName base, final int scope, final String filter, final Set<String> emit)
    throws SystemException {

    // ensure inheritance
    // set the limit to 2 because if more than one entry match the filter the
    // programming contract of a lookup search wchi has to return exactly one
    // entry is violated
    // a lookup has no need to be sorted hence setting it to null
    super(context, type, base, scope, 2, filter, emit, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Obtains and transforms the data from the Directory Service to a
   ** {@link ConnectorObject}.
   **
   ** @param  context            the {@link DirectoryEndpoint} to perform the
   **                            search.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to create and entry in
   **                            the Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  name               the entry DN to lookup.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  emit               the identifiers of the attributes to return
   **                            along with the entry. If <code>null</code>,
   **                            return all attributes. If empty return no
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
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
  public static ConnectorObject entry(final DirectoryEndpoint context, final ObjectClass type, final DirectoryName name, final Set<String> emit)
    throws SystemException {

    final DirectoryLookup search = build(context, type, name, SearchControls.OBJECT_SCOPE, null, emit);
    return search.connectorObject(search.execute());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryUUID
  /**
   ** Factory method to create a lookup search for a single entry in a Directory
   ** Service.
   **
   ** @param  endpoint           the {@link DirectoryEndpoint} to perform the
   **                            search.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to create and entry in
   **                            the Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  entryName          the name of the entry used in the
   **                            <code>RDN</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  option             the options that affect the way this operation
   **                            is run.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    an instance of <code>Lookup</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link DirectorySearch}.
   **
   ** @throws SystemException    if the appropriate Request Controls cannot be
   **                            initialized.
   */
  public static String entryUUID(final DirectoryEndpoint endpoint, final ObjectClass type, final String entryName, final OperationOptions option)
    throws SystemException {

    int           scope = SearchControls.SUBTREE_SCOPE;
    DirectoryName base  = DirectoryName.ROOT;
    if (option.getContainer() != null) {
      base  = DirectoryName.build(option.getContainer().getUid().getUidValue());
      scope = SCOPE.get(option.getScope());
    }

    // verify if a proper scope is passed
    final String filter = DirectoryEntry.composeFilter(endpoint.entryPrefix(type), entryName);
    final DirectoryLookup search = build(endpoint, type, base, scope, filter, CollectionUtility.set(endpoint.entryIdentifierAttribute()));
    final SearchResult    result = search.execute();
    return DirectoryEntry.value(result, search.entryIdentifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryName
  /**
   ** Factory method to create a lookup search for a single entry in a Directory
   ** Service.
   **
   ** @param  endpoint           the {@link DirectoryEndpoint} to perform the
   **                            search.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to create and entry in
   **                            the Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  entryUUID          the name of the entry used in the
   **                            <code>RDN</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Lookup</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link DirectoryName}.
   **
   ** @throws SystemException    if the appropriate Request Controls cannot be
   **                            initialized.
   */
  public static DirectoryName entryName(final DirectoryEndpoint endpoint, final ObjectClass type, final String entryUUID)
    throws SystemException {

    final SearchResult result = build(endpoint, type, DirectoryName.ROOT, SearchControls.SUBTREE_SCOPE, DirectoryEntry.composeFilter(endpoint.entryIdentifierAttribute(), entryUUID), CollectionUtility.set("")).execute();
    return DirectoryName.build(result.getNameInNamespace());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a lookup search for a single entry in a Directory
   ** Service.
   **
   ** @param  endpoint           the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to lookup the entry in
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
   ** @param  filter             the filter expression to use for the search;
   **                            must not be null, e.g.
   **                            "(&amp;(objectclass=*)(cn=abraham))"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  emit               the identifiers of the attributes to return
   **                            along with the entry. If <code>null</code>,
   **                            return all attributes. If empty return no
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    an instance of <code>Lookup</code> with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is {@link DirectoryLookup}.
   **
   ** @throws SystemException    if the appropriate Request Controls cannot be
   **                            initialized.
   */
  public static DirectoryLookup build(final DirectoryEndpoint endpoint, final ObjectClass type, final DirectoryName base, final int scope, final String filter, final Set<String> emit)
    throws SystemException {

    return new DirectoryLookup(endpoint, type, base, scope, filter, emit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   **
   ** @return                    a <code>SearchResult</code>s of representing
   **                            the object that satisfy the filter criteria.
   **                            <br>
   **                            Never <code>null</code>.
   **                            <br>
   **                            Possible object is {@link SearchResult}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public SearchResult execute()
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      this.endpoint.connect(this.endpoint.base());
      final List<SearchResult> lookup = next();
      if (lookup.size() == 0)
        throw DirectoryException.entryNotFound(this.filter);
      else if (lookup.size() > 1)
        throw DirectoryException.entryAmbiguous(this.filter);

      return lookup.get(0);
    }
    finally {
      this.endpoint.disconnect();
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}