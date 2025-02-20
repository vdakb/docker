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

    File        :   DirectoryRename.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryRename.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.utility.StringUtility;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
////////////////////////////////////////////////////////////////////////////////
// class DirectoryRename
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Encapsulates the rename operations of the connector.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryRename extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the <code>DirectoryRename</code> passed in to immediately connect to a
   ** Directory Service. If the {@link DirContext} fails a
   ** {@link DirectoryException} will be thrown.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to rename the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  private DirectoryRename(final DirectoryEndpoint context, final ObjectClass type)
    throws SystemException {

    // ensure inheritance
    super(context, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Return the entry rename operation.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to rename the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @return                    the operation.
   **                            <br>
   **                            Possible object is {@link DirectoryRename}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  public static DirectoryRename build(final DirectoryEndpoint context, final ObjectClass type)
    throws SystemException {

    return new DirectoryRename(context, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Rename object of the passed new distinguished name.
   ** <br>
   ** Calls the context.rename() to rename object
   **
   ** @param  origin             the distinguished name of the object to rename,
   **                            e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  target             the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if the operation fails
   */
  public void execute(final DirectoryName origin, final DirectoryName target)
    throws SystemException {

    final String method = "rename";
    trace(method, Loggable.METHOD_ENTRY);
    final LdapContext connection = this.endpoint.connect();
    try {
      // rename object only if different
      if (!origin.equals(target)) {
       connection.rename(origin, target);
      }
    }
    // this exception is thrown when a component of the name cannot be resolved
    // because it is not bound.
    catch (NameNotFoundException e) {
      throw DirectoryException.entryNotFound(e, origin);
    }
    // This exception is thrown by methods to indicate that a binding cannot be
    // added because the name is already bound to another object.
    catch (NameAlreadyBoundException e) {
      throw DirectoryException.entryExists(e, target);
    }
    catch (NamingException e) {
      throw DirectoryException.entryNotRenamed(e, origin);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
      this.endpoint.disconnect();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Renames the existing entry.
   ** <br>
   ** Calls the context.rename() to rename object
   **
   ** @param  identifier         the unique identifier of the object to modify
   **                            in the Directory Service.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  attribute          the attribute containing the new name of the
   **                            resource object..
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails
   */
  public void execute(final Uid identifier, final Attribute attribute)
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    
    // prevent bogus input
    if (identifier == null || StringUtility.empty(identifier.getUidValue()))
      throw SystemException.identifierRequired();
    
    final String value = AttributeUtil.getStringValue(attribute);
    if (value == null)
        throw SystemException.nameRequired(Name.NAME);

    try {
      final DirectoryName newDN = DirectoryName.build(attribute.getName(), value);
      final DirectoryName oldDN = DirectoryLookup.entryName(this.endpoint, type, identifier.getUidValue());
      
      newDN.addAll(0, oldDN.suffix());
      execute(oldDN, newDN);
    } catch (InvalidNameException e) {
      throw DirectoryException.invalidName(e);
    } finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}