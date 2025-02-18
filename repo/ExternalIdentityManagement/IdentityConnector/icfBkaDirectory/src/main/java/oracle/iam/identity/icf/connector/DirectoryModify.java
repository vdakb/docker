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

    File        :   DirectoryModify.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryModify.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;
import java.util.Map;
import java.util.List;

import javax.naming.directory.DirContext;
import javax.naming.directory.BasicAttributes;

import javax.naming.ldap.LdapContext;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryModify
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Encapsulates the modify operations of the connector.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryModify extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the <code>DirectoryModify</code> passed in to immediately connect to a
   ** Directory Service. If the {@link DirContext} fails a
   ** {@link DirectoryException} will be thrown.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to modify the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  private DirectoryModify(final DirectoryEndpoint context, final ObjectClass type)
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
   ** Return the entry modify operation.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to modify the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @return                    the operation.
   **                            <br>
   **                            Possible object is {@link DirectoryModify}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  public static DirectoryModify build(final DirectoryEndpoint context, final ObjectClass type)
    throws SystemException {

    return new DirectoryModify(context, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Modifies object and binds to the passed distinguished name and the
   ** attributes.
   ** <br>
   ** Calls the context.modifyAttributes() to modify the attributes of an entry.
   **
   ** @param  identifier         the unique identifier of the object to modify
   **                            in the Directory Service.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  attribute          the initial attributes of the object.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @return                    the unique id of the modified object in case
   **                            the update changes the formation of the unique
   **                            identifier.
   **                            <br>
   **                            Possible object is {@link Uid}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public Uid execute(final Uid identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);

    try {
      // prevent bogus input
      if (identifier == null || StringUtility.empty(identifier.getUidValue()))
        throw SystemException.identifierRequired();

      final DirectoryName             entryDN     = DirectoryLookup.entryName(this.endpoint, this.type, identifier.getUidValue());
      final BasicAttributes           entry       = new BasicAttributes(false);
      if (!CollectionUtility.empty(attribute)) {
        final Map<String, List<String>> entitlement = DirectoryAttribute.encodeAttribute(this.endpoint.schema(), this.type, attribute, entry);
        this.endpoint.modify(entryDN, entry, LdapContext.REPLACE_ATTRIBUTE);
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    // the identifier of the entry to modify will never update by this operation
    // hence its allowed to return the passed in identifier
    return identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Assign object and binds to the passed distinguished name.
   ** <br>
   ** Calls the context.modifyAttributes() to modify the attributes of an entry.
   **
   ** @param  identifier         the identifier of the group to modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  attribute          includes all the attributes necessary to modify
   **                            the resource object including the
   **                            {@link ObjectClass} attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void assign(final Uid identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "assign";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // prevent bogus input
      if (identifier == null || StringUtility.empty(identifier.getUidValue()))
        throw SystemException.identifierRequired();

      final DirectoryName entryDN = DirectoryLookup.entryName(this.endpoint, this.type, identifier.getUidValue());
      for (Attribute cursor : attribute) {
        final List<Object>             value   = cursor.getValue();
        final EmbeddedObject           subject = (EmbeddedObject)value.get(0);
        final Attribute                scope   = subject.getAttributeByName(Uid.NAME);
        if (scope == null)
          throw SystemException.identifierRequired();

        final DirectoryEndpoint.Schema schema  = this.endpoint.schemaSupport(subject.getObjectClass());
        if (schema == null)
          throw SystemException.unsupportedType(subject.getObjectClass(), method);
        
        this.endpoint.modify(
          DirectoryName.build((String)scope.getValue().get(0))
          // member attribute on groups only allows strings as value and not
          // DirectoryName objects
        , new BasicAttributes(schema.member, entryDN.toString(), false)
        , LdapContext.ADD_ATTRIBUTE
        );
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revoke object and unbinds to the passed distinguished name.
   ** <br>
   ** Calls the context.modifyAttributes() to modify the attributes of an entry.
   **
   ** @param  identifier         the identifier of the group to modify.
   **                            <br>
   **                            Will never <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  attribute          includes all the attributes necessary to modify
   **                            the resource object including the
   **                            {@link ObjectClass} attribute.
   **                            <br>
   **                            Will never <code>null</code> and not empty.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @throws SystemException    if the operation fails.
   */
  public void revoke(final Uid identifier, final Set<Attribute> attribute)
    throws SystemException {

    final String method = "revoke";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      // prevent bogus input
      if (identifier == null || StringUtility.empty(identifier.getUidValue()))
        throw SystemException.identifierRequired();

      final DirectoryName entryDN = DirectoryLookup.entryName(this.endpoint, this.type, identifier.getUidValue());
      for (Attribute cursor : attribute) {
        final List<Object>             value   = cursor.getValue();
        final EmbeddedObject           subject = (EmbeddedObject)value.get(0);
        final Attribute                scope   = subject.getAttributeByName(Uid.NAME);
        if (scope == null)
          throw SystemException.identifierRequired();

        final DirectoryEndpoint.Schema schema  = this.endpoint.schemaSupport(subject.getObjectClass());
        if (schema == null)
          throw SystemException.unsupportedType(subject.getObjectClass(), method);

        this.endpoint.modify(
          DirectoryName.build((String)scope.getValue().get(0))
          // member attribute on groups only allows strings as value and not
          // DirectoryName objects
        , new BasicAttributes(schema.member, entryDN.toString(), false)
        , LdapContext.REMOVE_ATTRIBUTE
        );
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}