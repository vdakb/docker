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

    File        :   SystemException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;
import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

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
  @SuppressWarnings("compatibility:-2199715114621692912")
  private static final long serialVersionUID = 6966661994914569916L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the resource key to create the localized exception */
  private final String code;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected SystemException(final String code) {
    // ensure inheritance
    this(SystemBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected SystemException(final String code, final String parameter) {
    this(SystemBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  protected SystemException(final String code, final Object... parameter) {
    this(SystemBundle.RESOURCE, code, parameter);
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
   */
  protected SystemException(final ListResourceBundle bundle, final String code) {
    // ensure inheritance
    super(bundle.getString(code));

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
   **                            Allowed object is array of {@link Object}.
   */
  protected SystemException(final ListResourceBundle bundle, final String code, final Object... parameter) {
    // ensure inheritance
    super(bundle.stringFormat(code, parameter));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final Throwable causing) {
    this(SystemError.GENERAL, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final String code, final Throwable causing) {
    this(SystemBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final ListResourceBundle bundle, final Throwable causing) {
    // ensure inheritance
    this(bundle, SystemError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final ListResourceBundle bundle, final String code, final Throwable causing) {
    // ensure inheritance
    super(bundle.stringFormat(code, causing.toString()));

    // set the correct exception chain
    initCause(causing);

    // store provided code for further processing
    this.code = code;
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
   **                            Allowed object is array of {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final String code, final String[] parameter, final Throwable causing) {
    this(SystemBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final ListResourceBundle bundle, final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle.stringFormat(code, parameter, causing));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SystemException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  protected SystemException(final ListResourceBundle bundle, final String code, final Object[] parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle.stringFormat(code, parameter), causing);

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Acccessor method
  //////////////////////////////////////////////////////////////////////////////

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
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException unhandled(final Throwable causing) {
    return generic(SystemError.UNHANDLED, causing);
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
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException general(final Throwable causing) {
    return generic(SystemError.GENERAL, causing);
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
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException abort(final Throwable causing) {
    return generic(SystemError.ABORT, causing);
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
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException unavailable(final Throwable causing) {
    return generic(SystemError.CONNECTION_UNAVAILABLE, causing);
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#CONNECTION_ERROR} error keyword.
   ** <p>
   ** A convenience wrapper to handle <code>CommunicationException</code>.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException connection(final Throwable causing) {
    return new SystemException(SystemError.CONNECTION_ERROR, causing);
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
    return new SystemException(SystemError.CONNECTION_ENCODING_UNSUPPORTED, encoding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unsupportedType
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#OBJECT_CLASS_UNSUPPORTED} error keyword.
   **
   ** @param  type               the {@link ObjectClass} that isn't supported by
   **                            operation <code>operation</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  operation          the operation for that the {@link ObjectClass}
   **                            isn't supported.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException unsupportedType(final ObjectClass type, final String operation) {
    return new SystemException(SystemError.OBJECT_CLASS_UNSUPPORTED, type, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeRequired
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#OBJECT_CLASS_REQUIRED} error keyword.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException typeRequired() {
    return new SystemException(SystemError.OBJECT_CLASS_REQUIRED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valuesRequired
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#OBJECT_VALUES_REQUIRED} error keyword.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException valuesRequired() {
    return new SystemException(SystemError.OBJECT_VALUES_REQUIRED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifierRequired
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#UNIQUE_IDENTIFIER_REQUIRED} error keyword.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException identifierRequired() {
    return new SystemException(SystemError.UNIQUE_IDENTIFIER_REQUIRED, Uid.NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameRequired
  /**
   ** Factory method to create a new <code>SystemException</code> with the
   ** {@link SystemError#NAME_IDENTIFIER_REQUIRED} error keyword.
   **
   ** @param  attributeName      the name of the attribute that required but
   **                            not provided,
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemException</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SystemException</code>.
   */
  public static SystemException nameRequired(final String attributeName) {
    return new SystemException(SystemError.NAME_IDENTIFIER_REQUIRED, attributeName);
  }
}