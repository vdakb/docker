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

    File        :   DirectoryOperation.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.List;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.naming.NameNotFoundException;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.InvalidAttributeIdentifierException;

import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryOperation
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Encapsulates the operations of the connector.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class DirectoryOperation extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final ObjectClass              type;
  protected final DirectoryEndpoint        endpoint;
  protected final DirectoryEndpoint.Schema schema;

  protected final String                   entryIdentifier;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the {@link DirectoryEndpoint} passed in to immediately connect to a
   ** Directory Service. If the {@link DirContext} fails a
   ** {@link DirectoryException} will be thrown.
   **
   ** @param  endpoint           the Directory Service endpoint connection which
   **                            is used to discover this
   **                            <code>DirectorySchema</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   **
   ** @param  type               the logical object class to create and entry in
   **                            the Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  protected DirectoryOperation(final DirectoryEndpoint endpoint, final ObjectClass type)
    throws SystemException {

    // ensure inheritance
    super(endpoint);

    // prevent bogus input
    if (type == null)
      throw SystemException.typeRequired();

    // initialize instance
    this.type            = type;
    this.schema          = endpoint.schemaSupport(type);
    this.endpoint        = endpoint;
    this.entryIdentifier = endpoint.entryIdentifierAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link DirectoryEndpoint} of this LDAP operation.
   **
   ** @return                    the {@link DirectoryEndpoint} of this LDAP
   **                            operation.
   **                            <br>
   **                            Possible object is {@link DirectoryEndpoint}.
   */
  public final DirectoryEndpoint endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAttributeValues
  /**
   ** Lists the value(s) of a particular object attribute.
   ** <p>
   ** Returns it as a {@link List} to accommodate properties that have a list of
   ** values.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  attributeName      the name of the attribute (property) whose
   **                            value(s) needs to be retrieved.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a {@link List} of Strings, consisting of all
   **                            the values this attribute was set to.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws SystemException    if the operation fails
   */
  public List<String> fetchAttributeValues(final DirectoryName baseRDN, final DirectoryName objectRDN, final String attributeName)
    throws SystemException {

    List<String> values    = null;
    Attributes   response  = null;
    String[]     parameter = { attributeName };
    try {
      response = this.endpoint.connect(baseRDN).getAttributes(objectRDN, parameter);
      // check if we got a result from the server
    }
    // this exception is thrown when a component of the name cannot be resolved
    // because it is not bound.
    catch (NameNotFoundException e) {
      throw DirectoryException.entryNotFound(e, objectRDN);
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    finally {
      this.endpoint.disconnect();
    }

    if (response == null)
      return values;

    values = new ArrayList<String>();
    DirectoryEntry.values(response, attributeName, values);
    return values;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAllAttributes
  /**
   ** Returns all the attributes of an entry from target system.
   **
   ** @param  baseRDN            the hierarchical structure to the parent of
   **                            the entry. This is the RDN of the parent,
   **                            relative to the root context, e.g.
   **                            OU=development,OU=Engineering.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  objectRDN          the "Relative Distinguished Name" of the
   **                            directory object relative to the hierarchy
   **                            specified by the <code>baseRDN</code> parameter,
   **                            usually it is just the objects name e.g.
   **                            CN=DSteding
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    {@link Attributes} of the entry
   **                            <br>
   **                            Possible object is {@link Attributes}.
   **
   ** @throws SystemException    if the operation fails
   */
  public Attributes fetchAllAttributes(final DirectoryName baseRDN, final DirectoryName objectRDN)
    throws SystemException {

    Attributes attributes = null;
    try {
      attributes = this.endpoint.connect(baseRDN).getAttributes(objectRDN);
    }
    // this exception is thrown when a component of the name cannot be resolved
    // because it is not bound.
    catch (NameNotFoundException e) {
      throw DirectoryException.entryNotFound(e, objectRDN);
    }
    // this exception is thrown when an attempt is made to add or modify an
    // attribute set that has been specified incompletely or incorrectly.
    // This could happen, for example, when attempting to add or modify a
    // binding, or to create a new subcontext without specifying all the
    // mandatory attributes required for creation of the object.
    // Another situation in which this exception is thrown is by specification
    // of incompatible attributes within the same attribute set, or attributes
    // in conflict with that specified by the object's schema. 
    catch (InvalidAttributesException e) {
      throw DirectoryException.attributeInvalidData(e, objectRDN);
    }
    // this exception is thrown when an attempt is made to add to create an
    // attribute with an invalid attribute identifier.
    // the validity of an attribute identifier is directory-specific. 
    catch (InvalidAttributeIdentifierException e) {
      throw DirectoryException.attributeInvalidType(e, objectRDN);
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    finally {
      this.endpoint.disconnect();
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAllAttributes
  /**
   ** Returns all the attributes of an entry from target system.
   **
   ** @param  entryDN            the "Distinguished Name" of the directory
   **                            object, usually it is just the objects name
   **                            e.g. uid=dsteding,ou=People,dc=vm,dc=oracle,dc=com"
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    {@link Attributes} of the entry.
   **                            <br>
   **                            Possible object is {@link Attributes}.
   **
   ** @throws SystemException    if the operation fails
   */
  public Attributes fetchAllAttributes(final String entryDN)
    throws SystemException {

    return fetchAllAttributes(DirectoryName.quiet(entryDN));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAllAttributes
  /**
   ** Returns all the attributes of an entry from target system.
   **
   ** @param  entryDN            the "Distinguished Name" of the directory
   **                            object, usually it is just the objects name
   **                            e.g. uid=dsteding,ou=People,dc=vm,dc=oracle,dc=com"
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    {@link Attributes} of the entry.
   **                            <br>
   **                            Possible object is {@link Attributes}.
   **
   ** @throws SystemException    if the operation fails
   */
  public Attributes fetchAllAttributes(final DirectoryName entryDN)
    throws SystemException {

    Attributes attributes = null;
    try {
      attributes = this.endpoint.unwrap().getAttributes(entryDN);
    }
    // this exception is thrown when a component of the name cannot be resolved
    // because it is not bound.
    catch (NameNotFoundException e) {
      throw DirectoryException.entryNotFound(e, entryDN);
    }
    // this exception is thrown when an attempt is made to add or modify an
    // attribute set that has been specified incompletely or incorrectly.
    // This could happen, for example, when attempting to add or modify a
    // binding, or to create a new subcontext without specifying all the
    // mandatory attributes required for creation of the object.
    // Another situation in which this exception is thrown is by specification
    // of incompatible attributes within the same attribute set, or attributes
    // in conflict with that specified by the object's schema. 
    catch (InvalidAttributesException e) {
      throw DirectoryException.attributeInvalidData(e, entryDN);
    }
    // this exception is thrown when an attempt is made to add to create an
    // attribute with an invalid attribute identifier.
    // the validity of an attribute identifier is directory-specific. 
    catch (InvalidAttributeIdentifierException e) {
      throw DirectoryException.attributeInvalidType(e, entryDN);
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    return attributes;
  }
}