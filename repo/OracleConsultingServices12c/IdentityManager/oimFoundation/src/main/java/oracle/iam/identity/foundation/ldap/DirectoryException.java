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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.io.Serializable;

import javax.naming.NamingException;
import javax.naming.CommunicationException;
import javax.naming.AuthenticationException;
import javax.naming.ServiceUnavailableException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.DirectoryBundle;

////////////////////////////////////////////////////////////////////////////////
// final class DirectoryException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>LDAP</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class DirectoryException extends TaskException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5424061307875039822")
  private static final long serialVersionUID = -8508869559928695977L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   */
  public DirectoryException(String code) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code);
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
  public DirectoryException(final String code, final String parameter) {
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
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public DirectoryException(final String code, final String[] parameter) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final Throwable causing) {
    // ensure inheritance
    this(DirectoryError.UNHANDLED, causing);
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
  public DirectoryException(final String code, final Throwable causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link CommunicationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link CommunicationException}.
   **
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final CommunicationException causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, DirectoryError.CONNECTION_ERROR, causing);

    // ensure serializable of the causing exception
    ensureSerializable(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link CommunicationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link CommunicationException}.
   **
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final String parameter, final CommunicationException causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, DirectoryError.CONNECTION_ERROR, parameter, causing);

    // ensure serializable of the causing exception
    ensureSerializable(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link AuthenticationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link AuthenticationException}.
   **
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final AuthenticationException causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, DirectoryError.CONNECTION_AUTHENTICATION, causing);

    // ensure serializable of the causing exception
    ensureSerializable(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link AuthenticationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link AuthenticationException}.
   **
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final String parameter, final AuthenticationException causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, DirectoryError.CONNECTION_AUTHENTICATION, parameter, causing);

    // ensure serializable of the causing exception
    ensureSerializable(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link ServiceUnavailableException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link ServiceUnavailableException}.
   **
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final ServiceUnavailableException causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, DirectoryError.CONNECTION_NOT_AVAILABLE, causing);

    // ensure serializable of the causing exception
    ensureSerializable(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the specified
   ** {@link ServiceUnavailableException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link ServiceUnavailableException}.
   **
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final String parameter, final ServiceUnavailableException causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, DirectoryError.CONNECTION_NOT_AVAILABLE, parameter, causing);

    // ensure serializable of the causing exception
    ensureSerializable(causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NamingException</code> and passes it the specified
   ** {@link NamingException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link NamingException}.
   **
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final NamingException causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, DirectoryError.UNHANDLED, causing);

    // ensure serializable of the causing exception
    ensureSerializable(causing);
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
  public DirectoryException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DirectoryException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public DirectoryException(final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    super(DirectoryBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureSerializable
  /**
   ** Ensures that the {@link NamingException} will be serializable.
   ** <p>
   ** This exception type refereneces an {@link Object} that may not always the
   ** tag interface {@link Serializable} has as an interface assigned. An
   ** exception itselg is always serializable but the reference can break the
   ** rule.
   ** <p>
   ** This method ensures that if the refrerence is not flagged with the tag
   ** interface {@link Serializable} that the referenve is removed from the
   ** exception.
   **
   ** @param  exception          the {@link NamingException} to inspect.
   */
  private void ensureSerializable(final NamingException exception) {
    // a special trick to handle unserialized objects that this exception type
    // can reference
    Object origin = exception.getResolvedObj();
    if (origin != null && !Serializable.class.isAssignableFrom(origin.getClass()))
      exception.setResolvedObj(null);
  }
}