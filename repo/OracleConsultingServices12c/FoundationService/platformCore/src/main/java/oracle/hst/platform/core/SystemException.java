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
    Subsystem   :   Common Shared Utility

    File        :   SystemException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SystemException
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Base class for all used exception across tiers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemException extends Exception {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3098349911288668349")
  private static final long serialVersionUID = -6739959248572005731L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the resource key to create the localized exception */
  private String code;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public SystemException(String code) {
    // ensure inheritance
    this(SystemBundle.BUNDLE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a code and a array with
   ** values for the placeholder contained in the resource string retrieved for
   ** the specified resource code.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link Object}s.
   */
  protected SystemException(final String code, final Object... parameter) {
    // ensure inheritance
    this(SystemBundle.BUNDLE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the causing
   ** exception.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final Throwable cause) {
    // ensure inheritance
    this(SystemError.UNHANDLED, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a code and a causing
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final String code, final Throwable cause) {
    // ensure inheritance
    this(SystemBundle.BUNDLE, code, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final String code, final String parameter, final Throwable cause) {
    // ensure inheritance
    this(SystemBundle.BUNDLE, code, parameter, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the exception message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  protected SystemException(final String code, final Throwable cause, final String... parameter) {
    // ensure inheritance
    this(SystemBundle.BUNDLE, code, parameter, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected SystemException(final ListResourceBundle bundle, final String code, final String parameter) {
    // ensure inheritance
    super(bundle.formatted(code, parameter));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link Object}s.
   */
  protected SystemException(final ListResourceBundle bundle, final String code, final Object... parameter) {
    // ensure inheritance
    super(bundle.formatted(code, parameter));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Acccessor and Mutator method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Sets the code this exception is related to.
   ** <p>
   ** Makes the exception interpretable by the associated code without parsing
   ** the message.
   **
   ** @param  code               the code this exception is related to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected final void code(final String code) {
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Return the resource key this exception is related to.
   ** <p>
   ** Makes the exception interpretable by the associated code without parsing
   ** the message
   **
   ** @return                    the resource key this exception is related to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String code() {
    return this.code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#UNHANDLED} error keyword.
   **
   ** @param  message            the error message for this exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException unhandled(final String message) {
    return generic(SystemError.UNHANDLED, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#UNHANDLED} error keyword.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException unhandled(final Throwable cause) {
    return generic(SystemError.UNHANDLED, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#GENERAL} error keyword.
   **
   ** @param  message            the error message for this exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException general(final String message) {
    return generic(SystemError.GENERAL, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#GENERAL} error keyword.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException general(final Throwable cause) {
    return generic(SystemError.GENERAL, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#ABORT} error keyword.
   **
   ** @param  message            the error message for the exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException abort(final String message) {
    return generic(SystemError.ABORT, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#ABORT} error keyword.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException abort(final Throwable cause) {
    return generic(SystemError.ABORT, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notImplemented
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#NOTIMPLEMENTED} error keyword.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException notImplemented() {
    return generic(SystemError.NOTIMPLEMENTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNotFound
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CLASSNOTFOUND} error keyword.
   **
   ** @param  clazz              the name of the class which was not found.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException classNotFound(final String clazz) {
    return generic(SystemError.CLASSNOTFOUND, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNotCreated
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CLASSNOTCREATE} error keyword.
   **
   ** @param  clazz              the name of the class which was not created.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException classNotCreated(final String clazz) {
    return generic(SystemError.CLASSNOTCREATE, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNoAccess
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CLASSNOACCESS} error keyword.
   **
   ** @param  clazz              the name of the class access is denied to.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException classNoAccess(final String clazz) {
    return generic(SystemError.CLASSNOACCESS, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNoMethod
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CLASSMETHOD} error keyword.
   **
   ** @param  clazz              the name of the class the method invoked is
   **                            note available.
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameters tried to pass to the method
   **                            invoked.
   **                            Allowed object is array of {@link String}s.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException classNoMethod(final String clazz, final String parameter) {
    return generic(SystemError.CLASSMETHOD, clazz, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNoAccess
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CLASSNOACCESS} error keyword.
   **
   ** @param  clazz              the name of the class which must be a subclass
   **                            of <code>superClass</code>.
   **                            Allowed object is {@link String}.
   ** @param  superClass         the name of the class where <code>clazz</code>
   **                            must be derived from.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException classInvalid(final String clazz, final String superClass) {
    return generic(SystemError.CLASSINVALID, clazz, superClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   argumentNull
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#ARGUMENT_IS_NULL} error keyword.
   **
   ** @param  attribute          the name of the instance attribute which is
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException argumentNull(final String attribute) {
    return generic(SystemError.ARGUMENT_IS_NULL, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeNull
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#ATTRIBUTE_IS_NULL} error keyword.
   **
   ** @param  attribute          the name of the instance attribute which is
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException attributeNull(final String attribute) {
    return generic(SystemError.ATTRIBUTE_IS_NULL, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instanceState
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#INSTANCE_STATE} error keyword.
   **
   ** @param  attribute          the name of the instance attribute which is
   **                            not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException instanceState(final String attribute) {
    return generic(SystemError.INSTANCE_STATE, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyRequired
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#PROPERTY_REQUIRED} error keyword.
   **
   ** @param  property           the name of the configuration property that's
   **                            required but not provided.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException propertyRequired(final String property) {
    return generic(SystemError.PROPERTY_REQUIRED, property);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unknownHost
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_UNKNOWN_HOST} error keyword.
   **
   ** @param  host               the name of the host.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException unknownHost(final String host) {
    return generic(SystemError.CONNECTION_UNKNOWN_HOST, host);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_CREATE_SOCKET} error keyword.
   **
   ** @param  host               the name of the host.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException createSocket(final String host, final int port) {
    return generic(SystemError.CONNECTION_CREATE_SOCKET, host, port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocketSecure
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_SECURE_SOCKET} error keyword.
   **
   ** @param  host               the name of the host.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException createSocketSecure(final String host, final int port) {
    return generic(SystemError.CONNECTION_SECURE_SOCKET, host, port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timedOut
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_TIMEOUT} error keyword.
   **
   ** @param  host               the name of the host.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException timedOut(final String host) {
    return generic(SystemError.CONNECTION_TIMEOUT, host);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificatePath
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_CERTIFICATE_PATH} error keyword.
   **
   ** @param  host               the name of the host.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException certificatePath(final String host) {
    return generic(SystemError.CONNECTION_CERTIFICATE_PATH, host);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unavailable
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_UNAVAILABLE} error keyword.
   ** <p>
   ** The server is unable to handle the request due to temporary overloading or
   ** maintenance of the server.
   ** <br>
   ** The Service Provider is currently not running.
   ** <p>
   ** A convenience wrapper to handle <code>ServiceUnavailableException</code>.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException unavailable(final Throwable cause) {
    return generic(SystemError.CONNECTION_UNAVAILABLE, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authentication
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_AUTHENTICATION} keyword.
   **
   ** @param  principalUsername  the name of the account the exception belongs
   **                            too.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException authentication(final String principalUsername) {
    return generic(SystemError.CONNECTION_AUTHENTICATION, principalUsername);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_ERROR} error keyword.
   ** <p>
   ** A convenience wrapper to handle <code>CommunicationException</code>.
   **
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException connection(final Throwable cause) {
    return new SystemException(SystemError.CONNECTION_ERROR, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodingUnsupported
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_ENCODING_UNSUPPORTED} error keyword.
   **
   ** @param  encoding           the encoding which is unsupported.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException encodingUnsupported(final String encoding) {
    return generic(SystemError.CONNECTION_ENCODING_UNSUPPORTED, encoding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generic
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** specified code keyword.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  cause              the cause (which is saved for later retrieval
   **                            by the {@link #getCause()} method).
   **                            <br>
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException generic(final String code, final Throwable cause) {
    return new SystemException(code, cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generic
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** specified code keyword.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException generic(final String code, final Object... parameter) {
    return new SystemException(code, parameter);
  }
}