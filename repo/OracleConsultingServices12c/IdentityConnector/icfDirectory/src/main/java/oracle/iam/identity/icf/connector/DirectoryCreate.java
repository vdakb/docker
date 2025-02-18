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

    File        :   DirectoryCreate.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;

import javax.naming.NamingException;

import javax.naming.directory.DirContext;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.AttributesAccessor;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryCreate
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Encapsulates the create operations of the connector.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryCreate extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the <code>DirectoryCreate</code> passed in to immediately connect to a
   ** Directory Service. If the {@link DirContext} fails a
   ** {@link DirectoryException} will be thrown.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryEndpoint}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to create the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  private DirectoryCreate(final DirectoryEndpoint context, final ObjectClass type)
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
   ** Return the entry delete operation.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to create the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @return                    the operation.
   **                            <br>
   **                            Possible object is {@link DirectoryCreate}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  public static DirectoryCreate build(final DirectoryEndpoint context, final ObjectClass type)
    throws SystemException {

    return new DirectoryCreate(context, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Creates object and binds to the passed distinguished name and the
   ** attributes.
   ** <br>
   ** Calls the context.createSubcontext() to create object and bind it with the
   ** passed distinguished name and the attributes.
   **
   ** @param  attribute          the initial attributes of the object.
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
  public Uid execute(final Set<Attribute> attribute)
    throws SystemException {

    final AttributesAccessor accessor = new AttributesAccessor(attribute);
    org.identityconnectors.framework.common.objects.Attribute verify = accessor.getName();
    // prevent bogus state
    if (verify == null)
      throw SystemException.nameRequired(Name.NAME);

    final DirectoryName entryDN = DirectoryName.build(AttributeUtil.getStringValue(verify));
    final Set<String>   classes = this.endpoint.objectClass(this.type);
    // check if we are able to proceed further
    if (classes == null || classes.size() == 0)
      throw SystemException.propertyRequired(DirectoryEndpoint.OBJECT_CLASS_DEFAULT);

    // create the object classes an entry should be assigned to
    final BasicAttribute  entryClass = new BasicAttribute(this.endpoint.objectClassName());
    for (String clazz : classes)
      entryClass.add(clazz);

    final BasicAttributes entry = new BasicAttributes(false);
    entry.put(entryClass);
    DirectoryAttribute.encodeAttribute(this.endpoint.schema(), this.type, attribute, entry);
    
    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    Uid uid = null;
    try {
      // create object
      final DirContext context = this.endpoint.create(entryDN, entry);
      // a second roundtrip to the server is required to obtain the uuid of the
      // entry created above
      // it needs to be wrapped in its own try/catch block to handle exception
      // thrown to remove the entry created
      try {
        // entryUUID is operational hence we have to explicitly request for
        final Attributes attributes = context.getAttributes(DirectoryName.ROOT, new String[]{this.entryIdentifier});
        uid = new Uid(DirectoryEntry.value(attributes, this.entryIdentifier));
      }
      catch (NamingException e) {
        // destroy the context created above due to the error in fetching the
        // uuid for the entry; we don't need to take care about specific
        // exception
        try {
          context.destroySubcontext(DirectoryName.ROOT);
        }
        // TODO:
        // figure out how to handle this szenario correctly to report back to
        // the client the failed compensation
        catch (NamingException x) {
          fatal(method, x);
        }
        throw DirectoryException.entryNotCreated(e, entryDN);
      }
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return uid;
  }
}