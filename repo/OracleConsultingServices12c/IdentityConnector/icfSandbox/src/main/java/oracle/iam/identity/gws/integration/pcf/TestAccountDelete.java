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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestAccountDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.pcf;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountDelete
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case to delete a user resource in the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestAccountDelete extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Uid uid = new Uid("5728481b-cd3d-40ea-b644-bc2fc0ac7daa");

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws TaskException      if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    final OperationOptionsBuilder option = new OperationOptionsBuilder();
    try {
      Network.facade(Network.intranet()).delete(ObjectClass.ACCOUNT, uid, option.build());
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      final String   message = e.getLocalizedMessage();
      final String[] parts   = message.split("::");
      if (parts.length > 1)
        System.out.println(parts[0].concat("::").concat(parts[1]));
      else
        System.out.println(message);
    }
    catch (TaskException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
/*

  private static final String DOT_NET_EXCEPTION_PACKAGE_PLUS_DOT = "Org.IdentityConnectors.Framework.Common.Exceptions.";
  private static final String JAVA_EXCEPTION_PACKAGE = AlreadyExistsException.class.getPackage().getName();
  private static final String DOT_NET_ARGUMENT_EXCEPTION = "System.ArgumentException";

  private static final String CONNECTIONS_EXCEPTION_CLASS_NAME = "CommunicationsException";

  static Throwable processException(final Throwable t, ConnectorInstanceICFImpl conn, OperationResult result) {
    return processException(t, conn.getHumanReadableName(), result);
  }
 /**
  ** Transform ICF exception to something more usable.
  ** <p>
  ** ICF throws exceptions that contains inner exceptions that cannot be reached
  ** by current classloader. Such inner exceptions may cause a lot of problems
  ** in upper layers, such as attempt to serialize/deserialize them.
  ** <br>
  ** Therefore we cannot pass such exceptions to the upper layers.
  ** <p>
  ** As Throwable is immutable and there is no good way how to copy it, we just
  ** cannot remove the "bad" exceptions from the inner exception stack.
  ** <br>
  ** We need to do the brutal thing: remove all the ICF exceptions and do not
  ** pass then to upper layers. Try to save at least some information and
  ** "compress" the class names and messages of the inner ICF exceptions.
  ** <br>
  ** The full exception with a stack trace is logged here, so the details are
  ** still in the log.
  ** <p>
  ** <b>WARNING</b>: This is black magic. Really.
  **                 Blame Sun Identity Connector Framework interface design.
  **
  ** @param  t                   the exception from the Identity Connector
  **                             Framework.
  ** @param  result              the {@link OperationResult} to record failure.
  **
  ** @return                     a reasonable midPoint exception.
  */
  /*
  static Throwable processException(final Throwable t, final String description, final OperationResult result) {
    // whole exception handling in this case is a black magic.
    // ICF does not define any checked exceptions so the developers are not
    // guided towards good exception handling.
    // Sun Identity Connector Framework haven't had any "best practice" for
    // error reporting. Now there is some basic (runtime) exceptions and the
    // connectors are getting somehow better.
    // But this nightmarish code is still needed to support bad connectors.
    if (t == null) {
      result.recordFatalError("Null exception while processing ICF exception");
      throw new IllegalArgumentException("Null exception while processing ICF exception");
    }
    LOGGER.error("ICF Exception {} in {}: {}", t.getClass().getName(), description, t.getMessage(), t);
    if (t instanceof RemoteWrappedException) {
      // brutal hack, for now
      final RemoteWrappedException e = (RemoteWrappedException)t;
      final String                 c = e.getExceptionClass();
      if (c == null) {
        LOGGER.error("Remote ICF exception without inner exception class name. Continuing with original one: {}", t);
      }
      else if (DOT_NET_ARGUMENT_EXCEPTION.equals(className) && e.getMessage().contains("0x800708C5")) {
        // password too weak
        t = new SecurityViolationException(t.getMessage(), t);
      }
      else {
        if (className.startsWith(DOT_NET_EXCEPTION_PACKAGE_PLUS_DOT)) {
          c = JAVA_EXCEPTION_PACKAGE + "." + c.substring(DOT_NET_EXCEPTION_PACKAGE_PLUS_DOT.length());
          LOGGER.trace("Translated exception class: {}", className);
        }
        try {
          t = (Throwable)Class.forName(c).getConstructor(String.class, Throwable.class).newInstance(e.getMessage(), e);
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
          LoggingUtils.logException(LOGGER, "Couldn't unwrap remote ICF exception, continuing with original one {}", e, t);
        }
      }
    }
    if (t instanceof NullPointerException && t.getMessage() != null) {
      // NPE with a message text is in fact not a NPE but an application exception
      // this usually means that some parameter is missing
      final Exception e = new SchemaException(createMessageFromAllExceptions("Required attribute is missing",t));
      result.recordFatalError("Required attribute is missing: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof IllegalArgumentException) {
      // Let's assume this must be a configuration problem
      final Exception e = new com.evolveum.midpoint.util.exception.ConfigurationException(createMessageFromInnermostException("Configuration error", t));
      result.recordFatalError("Configuration error: " + t.getMessage(), e);
      return e;
    }
    // fix of MiD-2645
    // exception brought by the connector is java.lang.RuntimeException with
    // cause=CommunicationsException
    // this exception is to be analyzed here before the following if clause
    if (t.getCause() != null) {
      String exCauseClassName = t.getCause().getClass().getSimpleName();
      if (exCauseClassName.equals(CONNECTIONS_EXCEPTION_CLASS_NAME) ){
        Exception e = new CommunicationException(createMessageFromAllExceptions("Connect error", t));
        result.recordFatalError("Connect error: " + t.getMessage(), e);
        return e;
      }
    }
    if (t.getClass().getPackage().equals(NullPointerException.class.getPackage())) {
      // There are java.lang exceptions, they are safe to pass through
      result.recordFatalError(t);
      return t;
    }
    if (t.getClass().getPackage().equals(SchemaException.class.getPackage())) {
      // Common midPoint exceptions, pass through
      result.recordFatalError(t);
      return t;
    }
    if (result == null) {
      throw new IllegalArgumentException(createMessageFromAllExceptions("Null parent result while processing ICF exception",t));
    }
    // introspect the inner exceptions and look for known causes
    final Exception known = lookForKnownCause(t, t, result);
    if (known != null) {
      result.recordFatalError(known);
      return known;
    }
    // ########
    // TODO: handle javax.naming.NoPermissionException
    // relevant message directly in the exception ("javax.naming.NoPermissionException([LDAP: error code 50 - The entry uid=idm,ou=Administrators,dc=example,dc=com cannot be modified due to insufficient access rights])

    // Otherwise try few obvious things
    if (t instanceof IllegalArgumentException) {
      // this is most likely missing attribute or similar schema thing
      final Exception e = new SchemaException(createMessageFromAllExceptions("Schema violation (most likely)", t));
      result.recordFatalError("Schema violation: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof ConfigurationException) {
      final Exception e = new com.evolveum.midpoint.util.exception.ConfigurationException(createMessageFromInnermostException("Configuration error", t));
      result.recordFatalError("Configuration error: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof AlreadyExistsException) {
      final Exception e = new ObjectAlreadyExistsException(createMessageFromAllExceptions(null, t));
      result.recordFatalError("Object already exists: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof PermissionDeniedException) {
      final Exception e = new SecurityViolationException(createMessageFromAllExceptions(null, t));
      result.recordFatalError("Security violation: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof ConnectionBrokenException) {
      final Exception e = new CommunicationException(createMessageFromAllExceptions("Connection broken", t));
      result.recordFatalError("Connection broken: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof ConnectionFailedException) {
      final Exception e = new CommunicationException(createMessageFromAllExceptions("Connection failed", t));
      result.recordFatalError("Connection failed: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof UnknownHostException) {
      final Exception e = new CommunicationException(createMessageFromAllExceptions("Unknown host", t));
      result.recordFatalError("Unknown host: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof ConnectorIOException) {
      final Exception e = new CommunicationException(createMessageFromAllExceptions("IO error", t));
      result.recordFatalError("IO error: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof InvalidCredentialException) {
      final Exception e = new GenericFrameworkException(createMessageFromAllExceptions("Invalid credentials", t));
      result.recordFatalError("Invalid credentials: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof OperationTimeoutException) {
      final Exception e = new CommunicationException(createMessageFromAllExceptions("Operation timed out", t));
      result.recordFatalError("Operation timed out: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof UnknownUidException) {
      final Exception e = new ObjectNotFoundException(createMessageFromAllExceptions(null, t));
      result.recordFatalError("Unknown UID: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof InvalidAttributeValueException) {
      final Exception e = new SchemaException(createMessageFromAllExceptions(null, t));
      result.recordFatalError("Schema violation: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof RetryableException) {
      final Exception e = new CommunicationException(createMessageFromAllExceptions(null, t));
      result.recordFatalError("Retryable errror: " + t.getMessage(), e);
      return e;
    }
    else if (t instanceof ConnectorSecurityException) {
      // Note: connection refused is also packed inside
      // ConnectorSecurityException. But that will get addressed by the
      // lookForKnownCause(..) before
      // Maybe we need special exception for security?
      final Exception e =  new SecurityViolationException(createMessageFromAllExceptions("Security violation",t));
      result.recordFatalError("Security violation: " + t.getMessage(), e);
      return e;
    }
    // fallback
    final Exception e = new GenericFrameworkException(createMessageFromAllExceptions(null,t));
    result.recordFatalError(e);
    return e;
  }
*/
}