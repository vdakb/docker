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

    File        :   DirectoryService.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryService.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;

import org.identityconnectors.framework.spi.Configuration;

import org.identityconnectors.framework.spi.operations.SyncOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.ResolveUsernameOp;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryService
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>DirectoryService</code> implements the base functionality of an
 ** Identity Manager {@link AbstractConnector} for any directoy account.
 ** <p>
 ** The life-cycle for the {@link AbstractConnector} is as follows
 ** {@link #init(Configuration)} is called then any of the operations
 ** implemented in the {@link AbstractConnector} and finally {@link #dispose()}.
 ** The {@link #init(Configuration)} and {@link #dispose()} allow for block
 ** operations. For instance bulk creates or deletes and the use of before and
 ** after actions. Once {@link #dispose()} is called the
 ** {@link AbstractConnector} object is discarded.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DirectoryService extends    AbstractConnector
                                       implements SchemaOp
                                       ,          SyncOp
                                       ,          SearchOp<DirectoryFilter>
                                       ,          CreateOp
                                       ,          DeleteOp
                                       ,          UpdateOp
                                       ,          ResolveUsernameOp
                                       ,          UpdateAttributeValuesOp {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Place holder for the {@link Configuration} passed into the callback
   ** {@link Main#init(Configuration)}.
   */
  private DirectoryConfiguration config;

  /**
   ** Place holder for the {@link DirectoryEndpoint} passed into the callback
   ** {@link #init(Configuration)}.
   */
  protected DirectoryEndpoint    endpoint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryService</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected DirectoryService(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConfiguration (Connector)
  /**
   ** Return the configuration that was passed to {@link #init(Configuration)}.
   **
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   */
  @Override
  public Configuration getConfiguration() {
    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (Connector)
  /**
   ** Initialize the connector with its configuration.
   ** <p>
   ** For instance in a JDBC Connector this would include the database URL,
   ** password, and user.
   **
   ** @param  configuration      the instance of the {@link Configuration}
   **                            object implemented by the
   **                            <code>Connector</code> developer and populated
   **                            with information in order to initialize the
   **                            <code>Connector</code>.
   */
  @Override
  public void init(final Configuration configuration) {
    final String method = "init";
    trace(method, Loggable.METHOD_ENTRY);
    this.config   = (DirectoryConfiguration)configuration;
    this.endpoint = this.config.endpoint();
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispose (Connector)
  /**
   ** Dispose of any resources the Connector uses.
   */
  @Override
  public void dispose() {
    final String method = "dispose";
    trace(method, Loggable.METHOD_ENTRY);
    if (this.config != null) {
      if (this.endpoint != null) {
        this.endpoint.disconnect();
        this.endpoint = null;
      }
      this.config = null;
    }
    trace(method, Loggable.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilterTranslator (SearchOp)
  /**
   ** Creates a filter translator that will translate a specified
   ** <code>filter</code> into one or more native queries.
   ** <br>
   ** Each of these native queries will be passed subsequently into
   ** <code>#executeQuery(ObjectClass, Filter, ResultsHandler, OperationOptions)</code>.
   ** <p>
   ** <b>Attention</b>:
   ** <br>
   ** This method <b>must</b> return a non-<code>null</code> instance of a
   ** {@link FilterTranslator} otherwise the communication to the
   ** <code>Connector Server</code> breaks.
   ** <br>
   ** The filter build by the translator itself must also <b>never</b>
   ** <code>null</code>.
   **
   ** @param  type               the {@link ObjectClass} for the search.
   **                            Will never be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  option             additional options that impact the way this
   **                            operation is run.
   **                            <br>
   **                            If the caller passes <code>null</code>, the
   **                            framework will convert this into an empty set
   **                            of options, so SPI need not worry about this
   **                            ever being <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    a filter translator.
   **                            This must not be <code>null</code>.
   **                            <br>
   **                            A <code>null</code> return value will cause
   **                            the API (SearchApiOp) to throw
   **                            <code>NullPointerException</code>.
   **                            <br>
   **                            Possible object is {@link FilterTranslator}
   **                            for type {@link String}.
   */
  @Override
  public FilterTranslator<DirectoryFilter> createFilterTranslator(final ObjectClass type, final OperationOptions option) {
    final String method = "createFilterTranslator";
    trace(method, Loggable.METHOD_ENTRY);
    DirectoryFilterTranslator translator = null;
    try {
      translator = DirectoryFilterTranslator.build(this.endpoint.schema(), type);
    }
    catch (SystemException e) {
      propagate(e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
    return translator;
  }
}
