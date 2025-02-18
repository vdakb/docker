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
    Subsystem   :   Foundation Shared Library

    File        :   AbstractConnector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConnector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import org.identityconnectors.framework.spi.Connector;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractConnector
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractConnector</code> implements the base functionality of an
 ** Identity Manager {@link Connector} for any kind of target system.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractConnector extends    AbstractConnectorLogger
                                        implements Connector {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractConnector</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected AbstractConnector(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractConnector</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  processName        the name of the process used for debugging
   **                            purpose in the scope of gathering performance
   **                            metrics.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected AbstractConnector(final String loggerCategory, final String processName) {
    // ensure inheritance
    super(loggerCategory, processName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unsupported
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is
   ** {@link SystemError#OBJECT_CLASS_UNSUPPORTED}}.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <br>
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same topic should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   ** <br>
   ** Specific to the special error of an unsupported {@link ObjectClass} in the
   ** operational implementation of a connector use
   ** {@link SystemException#unsupportedType(ObjectClass, String)} instead.
   **
   ** @param  type               the {@link ObjectClass} that isn't supported.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  operation          the operation for that the {@link ObjectClass}
   **                            isn't supported.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void unsupportedType(final ObjectClass type, final String operation) {
    propagate(SystemError.OBJECT_CLASS_UNSUPPORTED, type, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeRequired
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is
   ** {@link SystemError#OBJECT_CLASS_REQUIRED}}.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <br>
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same topic should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   ** <br>
   ** Specific to the special error of an unsupported {@link ObjectClass} in the
   ** operational implementation of a connector use
   ** {@link SystemException#valuesRequired()} instead.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void typeRequired() {
    propagate(SystemError.OBJECT_CLASS_REQUIRED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valuesRequired
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is
   ** {@link SystemError#OBJECT_VALUES_REQUIRED}}.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <br>
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same topic should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   ** <br>
   ** Specific to the special error of an unsupported {@link ObjectClass} in the
   ** operational implementation of a connector use
   ** {@link SystemException#valuesRequired()} instead.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void valuesRequired() {
    propagate(SystemError.OBJECT_VALUES_REQUIRED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifierRequired
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is
   ** {@link SystemError#UNIQUE_IDENTIFIER_REQUIRED}}.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <br>
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same topic should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   ** <br>
   ** Specific to the special error of an unsupported {@link ObjectClass} in the
   ** operational implementation of a connector use
   ** {@link SystemException#identifierRequired()} instead.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void identifierRequired() {
    propagate(SystemError.UNIQUE_IDENTIFIER_REQUIRED, Uid.NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameRequired
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is
   ** {@link SystemError#NAME_IDENTIFIER_REQUIRED}}.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <br>
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same topic should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   ** <br>
   ** Specific to the special error of an unsupported {@link ObjectClass} in the
   ** operational implementation of a connector use
   ** {@link SystemException#identifierRequired()} instead.
   **
   ** @param  attributeName      the name of the attribute that required but
   **                            not provided,
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void nameRequired(final String attributeName) {
    propagate(SystemError.NAME_IDENTIFIER_REQUIRED, attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is <code>code</code>.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same code should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void propagate(final String code) {
    // throw the exception with the format of the deatiled message descriced
    // above
    throw new ConnectorException(String.format("%s::%s", code, SystemBundle.string(code)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is <code>code</code>.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   ** <b>Note</b>:
   ** This method should be called in a connector API implementation only.
   ** <br>
   ** Errors that belongs to the same code should be thrown in the operational
   ** implementation of the conector by an appropriate exception inherited from
   ** {@link SystemException}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason as the detailed
   **                            message of the exception created.
   */
  public static void propagate(final String code, final Object... parameter) {
    // throw the exception with the format of the deatiled message descriced
    // above
    throw new ConnectorException(String.format("%s::%s", code, SystemBundle.string(code, parameter)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Factory method to build an throw a {@link ConnectorException} driven by
   ** the resource bundle.
   ** <p>
   ** The keyword bound to the exception is the <code>code</code> property of
   ** the given {@link SystemException}.
   ** <br>
   ** The detaild message of the exception thrown is build with a specific
   ** format that allows to parse for the error code and the message text which
   ** belongs to this error code.
   ** <br>
   ** That makes it possible to use the error code inside of an
   ** <code>Adapter</code> and also in the <code>Process Task</code> in Identity
   ** Manager.
   ** <br>
   ** The advantage of this behavior is that at the <code>Process Task</code> a
   ** detailed configuration can be configured how to handle specific error
   ** conditions.
   **
   ** @param  cause              the exception to propagate.
   **                            <br>
   **                            Allowed object is {@link SystemException}.
   **
   ** @throws ConnectorException the exception thrown with an appropriate
   **                            explanation of the reason derived from the
   **                            given {@link SystemException}.
   */
  public static void propagate(final SystemException cause) {
    final String message = cause.getLocalizedMessage();
    // throw the exception with the format of the deatiled message descriced
    // above
    throw new ConnectorException(String.format("%s::%s", cause.code(), message));
  }
}