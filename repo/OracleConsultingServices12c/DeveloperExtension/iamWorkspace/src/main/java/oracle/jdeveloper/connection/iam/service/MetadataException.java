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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   MetadataException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataException.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.service;

import oracle.mds.core.ValidationException;
import oracle.mds.core.ConcurrentMOChangeException;

import oracle.mds.config.MDSConfigurationException;

import oracle.mds.exception.MDSException;
import oracle.mds.exception.MDSRuntimeException;
import oracle.mds.exception.InvalidNamespaceException;
import oracle.mds.exception.UnsupportedUpdateException;

import oracle.mds.persistence.MDSIOException;
import oracle.mds.persistence.ConcurrentDocChangeException;

import oracle.jdeveloper.connection.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class MetadataException
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>MetadataService</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataException extends EndpointException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:635635161979353904")
  private static final long serialVersionUID = -910073564100447045L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public MetadataException(final String message) {
    // ensure inheritance
    super(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  message            the detail message.
   **                            The detail message is saved for later retrieval
   **                            by the {@link #getMessage()} method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public MetadataException(final String message, final Throwable causing) {
    // ensure inheritance
    super(message, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link MDSException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link MDSException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link MDSException}.
   */
  public MetadataException(final MDSException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_UNHANDLED, causing.getLocalizedMessage()), causing);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link MDSConfigurationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link MDSConfigurationException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link MDSConfigurationException}.
   */
  public MetadataException(final MDSConfigurationException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link ValidationException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link ValidationException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link ValidationException}.
   */
  public MetadataException(final ValidationException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link ConcurrentMOChangeException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link ConcurrentMOChangeException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConcurrentMOChangeException}.
   */
  public MetadataException(final ConcurrentMOChangeException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link MDSRuntimeException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link MDSRuntimeException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link MDSRuntimeException}.
   */
  public MetadataException(final MDSRuntimeException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link UnsupportedUpdateException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link UnsupportedUpdateException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link UnsupportedUpdateException}.
   */
  public MetadataException(final UnsupportedUpdateException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link InvalidNamespaceException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link InvalidNamespaceException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link InvalidNamespaceException}.
   */
  public MetadataException(final InvalidNamespaceException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link MDSIOException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link MDSIOException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link MDSIOException}.
   */
  public MetadataException(final MDSIOException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataException</code> and passes it the specified
   ** {@link ConcurrentDocChangeException} exception.
   ** <p>
   ** A convenience wrapper to handle {@link ConcurrentDocChangeException}.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConcurrentDocChangeException}.
   */
  public MetadataException(final ConcurrentDocChangeException causing) {
    // ensure inheritance
    super(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, causing.getLocalizedMessage()), causing);
  }
}