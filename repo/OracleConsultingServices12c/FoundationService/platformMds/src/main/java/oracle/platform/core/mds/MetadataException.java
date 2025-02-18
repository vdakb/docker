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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Metadata Store Library

    File        :   MetadataException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    MetadataException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.platform.core.mds;

import oracle.mds.core.ValidationException;
import oracle.mds.core.ConcurrentMOChangeException;

import oracle.mds.config.MDSConfigurationException;

import oracle.mds.exception.MDSException;
import oracle.mds.exception.MDSRuntimeException;
import oracle.mds.exception.InvalidNamespaceException;
import oracle.mds.exception.UnsupportedUpdateException;

import oracle.mds.persistence.MDSIOException;
import oracle.mds.persistence.ConcurrentDocChangeException;

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
public class MetadataException extends Exception {

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
   **                            Allowed onject is {@link String}.
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
   **                            Allowed onject is {@link String}.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval by the {@link #getCause()} method).
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed onject is {@link Throwable}.
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
   **                            Allowed onject is {@link Throwable}.
   */
  public MetadataException(final MDSException causing) {
    // ensure inheritance
    super("Unhandled exception", causing);
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
   */
  public MetadataException(final MDSConfigurationException causing) {
    // ensure inheritance
    super("General error", causing);
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
   */
  public MetadataException(final ValidationException causing) {
    // ensure inheritance
    super("Error, aborting", causing);
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
   */
  public MetadataException(final ConcurrentMOChangeException causing) {
    // ensure inheritance
    super("Error, aborting", causing);
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
   */
  public MetadataException(final MDSRuntimeException causing) {
    // ensure inheritance
    super("Error, aborting", causing);
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
   */
  public MetadataException(final UnsupportedUpdateException causing) {
    // ensure inheritance
    super("Error, aborting", causing);
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
   */
  public MetadataException(final InvalidNamespaceException causing) {
    // ensure inheritance
    super("Error, aborting", causing);
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
   */
  public MetadataException(final MDSIOException causing) {
    // ensure inheritance
    super("Error, aborting", causing);
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
   */
  public MetadataException(final ConcurrentDocChangeException causing) {
    // ensure inheritance
    super("Error, aborting", causing);
  }
}