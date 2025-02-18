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

    File        :   DirectoryDelete.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import com.sun.jndi.ldap.ctl.TreeDeleteControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.utility.StringUtility;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.Uid;
////////////////////////////////////////////////////////////////////////////////
// class DirectoryDelete
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Encapsulates the delete operations of the connector.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryDelete extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final boolean deleteLeafNode;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the <code>DirectoryDelete</code> passed in to immediately connect to a
   ** Directory Service. If the {@link DirContext} fails a
   ** {@link DirectoryException} will be thrown.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to delete the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  deleteLeafNode     whether or not the entry and its leaf nodes
   **                            should be deleted on the target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  private DirectoryDelete(final DirectoryEndpoint context, final ObjectClass type, final boolean deleteLeafNode)
    throws SystemException {

    // ensure inheritance
    super(context, type);

    // initialize instance
    this.deleteLeafNode = deleteLeafNode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Return the entry delete operation.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to delete the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  deleteLeafNode     whether or not the entry and its leaf nodes
   **                            should be deleted on the target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the operation.
   **                            <br>
   **                            Possible object is {@link DirectoryDelete}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  public static DirectoryDelete build(final DirectoryEndpoint context, final ObjectClass type, final boolean deleteLeafNode)
    throws SystemException {

    return new DirectoryDelete(context, type, deleteLeafNode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Deletes object of the passed unique identifier.
   ** <br>
   ** Calls the context.destroySubcontext() to delete object.
   **
   ** @param  identifier         the unique identifier of the object to delete
   **                            in the Directory Service.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void execute(final Uid identifier)
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // prevent bogus input
      if (identifier == null || StringUtility.empty(identifier.getUidValue()))
        throw SystemException.identifierRequired();

      final DirectoryName entry = DirectoryLookup.entryName(this.endpoint, this.type, identifier.getUidValue());
      if (!this.endpoint.referentialIntegrity()) {
        // remove all entitlements assigne to the entry
        final Set<Attribute>      attribute = new HashSet<>();
        final DirectoryFilter     filter    = DirectoryFilter.expression(DirectoryEntry.composeFilter(this.endpoint.groupMember(), entry.toString()));
        final DirectorySearch     search    = DirectorySearch.build(this.endpoint, ObjectClass.GROUP, new OperationOptionsBuilder().build(), filter);
        final List<SearchResult>  result    = search.list();
        for (SearchResult group : result) {
          final String dn = group.getName();
          final EmbeddedObjectBuilder subject = new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, dn);
          attribute.add(AttributeBuilder.build(this.endpoint.groupMember(), Collections.singleton(subject.build())));
        }
        DirectoryModify.build(this.endpoint, ObjectClass.GROUP).revoke(identifier, attribute);
      }
      if (!this.deleteLeafNode) {
        this.endpoint.delete(entry, null);
      }
      else {
        final Control[] control = { new TreeDeleteControl() };
        this.endpoint.delete(entry, control);
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}