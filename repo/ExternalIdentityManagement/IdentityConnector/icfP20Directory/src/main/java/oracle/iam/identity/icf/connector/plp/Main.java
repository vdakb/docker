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
    Subsystem   :   Directory Account Connector

    File        :   Main.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.plp;

import oracle.iam.identity.icf.connector.DirectoryConfiguration;
import oracle.iam.identity.icf.connector.DirectoryException;
import oracle.iam.identity.icf.connector.DirectoryFilter;
import oracle.iam.identity.icf.connector.DirectoryLookup;
import oracle.iam.identity.icf.connector.DirectorySchema;
import oracle.iam.identity.icf.connector.DirectorySearch;
import oracle.iam.identity.icf.connector.DirectoryService;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.resource.Connector;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;
////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the base functionality of an Identity Manager
 ** {@link DirectoryService} for a directoy account.
 ** <p>
 ** The life-cycle for the {@link DirectoryService} is as follows
 ** {@link #init(Configuration)} is called then any of the operations
 ** implemented in the {@link DirectoryService} and finally {@link #dispose()}.
 ** The {@link #init(Configuration)} and {@link #dispose()} allow for block
 ** operations. For instance bulk creates or deletes and the use of before and
 ** after actions. Once {@link #dispose()} is called the
 ** {@link DirectoryService} object is discarded.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=DirectoryConfiguration.class, displayNameKey=Connector.Bundle.NAME)
public class Main extends DirectoryService {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String CATEGORY = "JCS.CONNECTOR.PLP";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> directory connector that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Main() {
    // ensure inheritance
    super(CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema (SchemaOp)
  /**
   ** Describes the types of objects this Connector supports.
   ** <p>
   ** This method is considered an operation since determining supported objects
   ** may require configuration information and allows this determination to be
   ** dynamic.
   ** <p>
   ** The special [@link Uid} attribute should never appear in the schema, as it
   ** is not a real attribute of an object, rather a reference to it.
   ** <br>
   ** If the resource object-class has a writable unique id attribute that is
   ** different than its {@link Name}, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @return                    basic schema supported by this Connector.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  @Override
  public Schema schema() {
    final String method = "schema";
    trace(method, Loggable.METHOD_ENTRY);
    Schema schema = null;
    try {
      schema = DirectorySchema.build(this.endpoint).schema(this.getClass());
    }
    catch (SystemException e) {
      fatal(method, e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveUsername (ResolveUsernameOp)
  /**
   ** Implements the operation interface to resolve objects from the target
   ** resource.
   ** <p>
   ** Resolve an object to its {@link Uid} based on its unique name.
   ** <br>
   ** This is a companion to the simple authentication. The difference is that
   ** this method does not have a password parameter and does not try to
   ** authenticate the credentials; instead, it returns the {@link Uid}
   ** corresponding to the username.
   ** <p>
   ** If the username validation fails, the an type of RuntimeException either
   ** IllegalArgumentException or if a native exception is available and if its
   ** of type RuntimeException simple throw it.
   ** <br>
   ** If the native exception is not a RuntimeException wrap it in one and throw
   ** it. This will provide the most detail for logging problem and failed
   ** attempts.
   **
   ** @param  type               the type of entry to resolve.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  name               the unique name that specifies the object to
   **                            resolve.
   **                            <br>
   **                            Allowed object is {@link Uid}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @throws ConnectorException if the parameters specified have issues or the
   **                            operation fails.
   */
  @Override
  public Uid resolveUsername(final ObjectClass type, final String name, final OperationOptions option) {
    final String method = "resolveUsername";
    trace(method, Loggable.METHOD_ENTRY);

    String indentifier = null;
    try {
      indentifier = DirectoryLookup.entryUUID(this.endpoint, type, name, option);
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return new Uid(indentifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeQuery (SearchOp)
  /**
   ** Request synchronization events -- i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link ResultsHandler handler} once to
   ** pass back each matching synchronization event. Once this method returns,
   ** this method will no longer invoke the specified handler.
   **
   ** @param  type               the type of entry to search.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  filter             the filter criteria to apply on the search and
   **                            converted from the {@link Filter} passed to
   **                            {@link #createFilterTranslator(ObjectClass, OperationOptions)}.
   **                            <br>
   **                            Allowed object is {@link DirectoryFilter}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ResultsHandler}.
   ** @param  option             the options that affect the way this operation
   **                            is run. If the caller passes <code>null</code>,
   **                            the framework will convert this into an empty
   **                            set of options, so an implementation need not
   **                            guard against this being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   */
  @Override
  public void executeQuery(final ObjectClass type, final DirectoryFilter filter, final ResultsHandler handler, final OperationOptions option) {
    final String method = "executeQuery";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      //This connector support only ACCOUNT class. Throw an error if the search
      //operation is something else.
      if (type.equals(ObjectClass.ACCOUNT)) {
        DirectorySearch.build(this.endpoint, type, option, filter).execute(handler);
      }
      else {
        throw DirectoryException.objectNotSupported(type);
      }
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

}