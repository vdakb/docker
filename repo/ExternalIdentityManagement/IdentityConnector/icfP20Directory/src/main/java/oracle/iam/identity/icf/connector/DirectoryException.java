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

    File        :   DirectoryException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.AttributeInUseException;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.SchemaViolationException;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.resource.DirectoryBundle;

import org.identityconnectors.framework.common.objects.ObjectClass;
////////////////////////////////////////////////////////////////////////////////
// final class DirectoryException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>LDAP</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryException extends SystemException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4060305451565586424")
  private static final long serialVersionUID = 4496863489950134652L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   ** @param  parameter          the substitutions for the placholder contained
   **                            in the message regarding to <code>code</code>.
   */
  private DirectoryException(final String code, final String parameter) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   ** @param  parameter          the substitutions for the placholder contained
   **                            in the message regarding to <code>code</code>.
   */
  private DirectoryException(final String code, final String... parameter) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> from a code and a array with
   ** values for the placeholder contained in the resource string retrieved for
   ** the specified resource code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  causing            the causing exception.
   */
  private DirectoryException(final String code, final Throwable causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, causing.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  private DirectoryException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, parameter, causing.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaViolated
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_SCHEMA_VIOLATED} error keyword.
   ** <p>
   ** This exception is thrown when a method in some ways violates the schema.
   ** <br>
   ** An example of schema violation is modifying attributes of an object that
   ** violates the object's schema definition. Another example is renaming or
   ** moving an object to a part of the namespace that violates the namespace's
   ** schema definition. 
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link SchemaViolationException}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException schemaViolated(final SchemaViolationException causing) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_SCHEMA_VIOLATED, causing.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notSupported
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#OPERATION_NOT_SUPPORTED} error keyword.
   ** <p>
   ** This exception is thrown when a context implementation does not support
   ** the operation being invoked.
   ** <br>
   ** For example, if a server does not support the Context.bind() method it
   ** would throw {@link OperationNotSupportedException} when the bind() method
   ** is invoked on it. 
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link OperationNotSupportedException}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException notSupported(final OperationNotSupportedException causing) {
    return new DirectoryException(DirectoryError.OPERATION_NOT_SUPPORTED, causing);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   notSupported
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#OPERATION_NOT_SUPPORTED} error keyword.
   ** <p>
   ** This exception is thrown when a Object Class is not supported
   ** <br>
   **
   ** @param  type               the object class
   **                            <br>
   **                            Allowed object is
   **                            {@link ObjectClass}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException objectNotSupported(final ObjectClass type) {
    return new DirectoryException(DirectoryError.OBJECT_CLASS_NOT_SUPPORTED, type.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notPermitted
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#OPERATION_NOT_PERMITTED} error keyword.
   ** <p>
   ** This exception is thrown when attempting to perform an operation for which
   ** the client has no permission.
   ** <br>
   ** The access control/permission model is dictated by the directory/naming server. 
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link OperationNotSupportedException}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException notPermitted(final NoPermissionException causing) {
    return new DirectoryException(DirectoryError.OPERATION_NOT_PERMITTED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidFilter
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#SEARCH_FILTER_INVALID} error keyword.
   ** <p>
   ** The specified filter syntax was invalid or the specified attribute and
   ** filter comparison combination is not supported.
   **
   ** @param  message            the error message for this LDAP exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException invalidFilter(final String message) {
    return new DirectoryException(DirectoryError.SEARCH_FILTER_INVALID, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidName
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#OBJECT_NAME_INVALID} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException invalidName(final Throwable causing) {
    return new DirectoryException(DirectoryError.OBJECT_NAME_INVALID, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryExists
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_EXISTS} error keyword.
   ** <br>
   ** The error keyword refers to LDAP: error code 68.
   ** <p>
   ** This exception is thrown by methods to indicate that a binding cannot be
   ** added because the name is already bound to another object.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryExists(final NameAlreadyBoundException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_EXISTS, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryAmbiguous
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_AMBIGUOUS} error keyword.
   ** <p>
   ** Specified resource (e.g., User) or endpoint is ambiguously found.
   **
   ** @param  message            the error message for the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryAmbiguous(final String message) {
    return new DirectoryException(DirectoryError.ENTRY_AMBIGUOUS, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotFound
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_FOUND} error keyword.
   ** <br>
   ** The error keyword refers to LDAP: error code 32.
   ** <p>
   ** This exception is thrown when a component of the name cannot be resolved
   ** because it is not bound.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotFound(final NameNotFoundException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_FOUND, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotFound
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_FOUND} error keyword.
   ** <br>
   ** The error keyword refers to LDAP: error code 32.
   ** <p>
   ** Specified resource (e.g., User) or endpoint does not exist.
   **
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotFound(final String name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_FOUND, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotCreated
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_CREATED} error keyword.
   ** <p>
   ** This is the superclass of all exceptions thrown by operations in the
   ** Context and DirContext interfaces.
   ** <br>
   ** The nature of the failure is described by the name of the subclass.
   ** <br>
   ** This exception captures the information pinpointing where the operation
   ** failed, such as where resolution last proceeded to.
   ** 
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotCreated(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_CREATED, name.toString(), causing.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotDeleted
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_DELETED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotDeleted(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_DELETED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotUpdated
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_UPDATED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotUpdated(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_UPDATED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotEnabled
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_ENABLED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotEnabled(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_ENABLED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotDisabled
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_DISABLED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotDisabled(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_DISABLED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryNotRenamed
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_NOT_RENAMED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryNotRenamed(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_NOT_RENAMED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryAlreadyEnabled
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_ALREADY_ENABLED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryAlreadyEnabled(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_ALREADY_ENABLED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryAlreadyDisabled
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_ALREADY_DISABLED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryAlreadyDisabled(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_ALREADY_DISABLED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryAlreadyUnlocked
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_ALREADY_UNLOCKED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryAlreadyUnlocked(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_ALREADY_UNLOCKED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryAlreadyLocked
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ENTRY_ALREADY_LOCKED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException entryAlreadyLocked(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ENTRY_ALREADY_LOCKED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeInUse
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_IN_USE} error keyword.
   ** <p>
   ** This exception is thrown when an operation attempts to add an attribute
   ** that already exists. 
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeInUse(final AttributeInUseException causing) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_IN_USE, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeInvalidData
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_INVALID_DATA} error keyword.
   ** <p>
   ** The exception {@link InvalidAttributesException} is thrown when an attempt
   ** is made to add or modify an attribute set that has been specified
   ** incompletely or incorrectly.
   ** <br>
   ** This could happen, for example, when attempting to add or modify a
   ** binding, or to create a new subcontext without specifying all the
   ** mandatory attributes required for creation of the object.
   ** <br>
   ** Another situation in which this exception is thrown is by specification of
   ** incompatible attributes within the same attribute set, or attributes in
   ** conflict with that specified by the object's schema.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  name               the name of the entry which is causing
   **                            the exception.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeInvalidData(final InvalidAttributesException causing, final DirectoryName name) {
    return attributeInvalidData(causing, name.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeInvalidData
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_INVALID_DATA} error keyword.
   ** <p>
   ** The exception {@link InvalidAttributesException} is thrown when an attempt
   ** is made to add or modify an attribute set that has been specified
   ** incompletely or incorrectly.
   ** <br>
   ** This could happen, for example, when attempting to add or modify a
   ** binding, or to create a new subcontext without specifying all the
   ** mandatory attributes required for creation of the object.
   ** <br>
   ** Another situation in which this exception is thrown is by specification of
   ** incompatible attributes within the same attribute set, or attributes in
   ** conflict with that specified by the object's schema.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  attributeName      the name of the attribute which is causing
   **                            the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeInvalidData(final InvalidAttributesException causing, final String attributeName) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_DATA, attributeName, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeInvalidType
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_INVALID_TYPE} error keyword.
   ** <p>
   ** This exception is thrown when an attempt is made to add to create an
   ** attribute with an invalid attribute identifier.
   ** <br>
   ** The validity of an attribute identifier is directory-specific. 
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  name               the name of the entry which is causing
   **                            the exception.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeInvalidType(final InvalidAttributeIdentifierException causing, final DirectoryName name) {
    return attributeInvalidType(causing, name.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeInvalidType
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_INVALID_TYPE} error keyword.
   ** <p>
   ** This exception is thrown when an attempt is made to add to create an
   ** attribute with an invalid attribute identifier.
   ** <br>
   ** The validity of an attribute identifier is directory-specific. 
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  attributeName      the name of the attribute which is causing
   **                            the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeInvalidType(final InvalidAttributeIdentifierException causing, final String attributeName) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_TYPE, attributeName, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeInvalidValue
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_INVALID_VALUE} error keyword.
   ** <p>
   ** This exception is thrown when an attempt is made to add to an attribute
   ** a value that conflicts with the attribute's schema definition.
   ** <br>
   ** This could happen, for example, if attempting to add an attribute with no
   ** value when the attribute is required to have at least one value, or if
   ** attempting to add more than one value to a single valued-attribute, or if
   ** attempting to add a value that conflicts with the syntax of the attribute. 
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  name               the name of the entry which is causing
   **                            the exception.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeInvalidValue(final InvalidAttributeValueException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_INVALID_VALUE, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeNotAssigned
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_NOT_ASSIGNED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeNotAssigned(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_NOT_ASSIGNED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeAlreadyAssigned
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_ALREADY_ASSIGNED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeAlreadyAssigned(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_ALREADY_ASSIGNED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeNotRemoved
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_NOT_REMOVED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeNotRemoved(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_NOT_REMOVED, name.toString(), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeAlreadyRemoved
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#ATTRIBUTE_ALREADY_REMOVED} error keyword.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link AttributeInUseException}.
   ** @param  name               the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            causing the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException attributeAlreadyRemoved(final NamingException causing, final DirectoryName name) {
    return new DirectoryException(DirectoryError.ATTRIBUTE_ALREADY_REMOVED, name.toString(), causing);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   notSupported
  /**
   ** Factory method to create a new <code>DirectoryException</code> with the
   ** {@link DirectoryError#OPERATION_NOT_SUPPORTED} error keyword.
   ** <p>
   ** This exception is thrown when a context implementation does not support
   ** the operation being invoked.
   ** <br>
   ** For example, if a server does not support the Context.bind() method it
   ** would throw {@link OperationNotSupportedException} when the bind() method
   ** is invoked on it. 
   **
   ** @param  type               the type of the change log number.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>DirectoryException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryException</code>.
   */
  public static DirectoryException changeLogNumber(final String type) {
    return new DirectoryException(DirectoryError.CHANGELOG_NUMBER, type);
  }
}