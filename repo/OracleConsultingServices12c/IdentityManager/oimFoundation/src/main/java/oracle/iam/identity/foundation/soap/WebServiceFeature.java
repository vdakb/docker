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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   WebServiceFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    WebServiceFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.soap;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;

////////////////////////////////////////////////////////////////////////////////
// class WebServiceFeature
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>WebServiceFeature</code> provides the base feature description of
 ** a Oracle Identity WebService.
 ** <br>
 ** Deployments of a Oracle Identity WebService may vary in locations of certain
 ** informations and object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class WebServiceFeature extends AbstractLookup {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>WebServiceFeature</code> which is associated with
   ** the specified task.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLookup} configuration
   **                            wrapper.
   */
  public WebServiceFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebServiceFeature</code> which is associated with the
   ** specified task and belongs to the Metadata Descriptorspecified by the given
   ** name.
   ** <br>
   ** The Metadata Descriptorn will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this {@link AbstractLookup} configuration
   **                            wrapper.
   ** @param  instanceName       the name of the {@link AbstractLookup}
   **                            instance where this configuration wrapper will
   **                            obtains the values.
   **
   ** @throws TaskException      if the {@link AbstractLookup} is not defined in
   **                            the Oracle Identity Manager metadata entries.
   */
  public WebServiceFeature(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the url encoding.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link WebServiceConstant#URL_ENCODING}.
   ** <p>
   ** If {@link WebServiceConstant#URL_ENCODING} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link WebServiceConstant#URL_ENCODING_DEFAULT}.
   **
   ** @return                    the url encoding.
   */
  public String urlEncoding() {
    return stringValue(WebServiceConstant.URL_ENCODING, WebServiceConstant.URL_ENCODING_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link WebServiceConstant#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link WebServiceConstant#MULTIVALUE_SEPARATOR} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link WebServiceConstant#MULTIVALUE_SEPARATOR_DEFAULT}.
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String multiValueSeparator() {
    return stringValue(WebServiceConstant.MULTIVALUE_SEPARATOR, WebServiceConstant.MULTIVALUE_SEPARATOR_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectTimeout
  /**
   ** Returns the timeout period for establishment of the HTTP connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an HTTP
   ** server. When connection pooling has been requested, this property also
   ** specifies the maximum wait time or a connection when all connections in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the HTTP provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link WebServiceConstant#CONNECT_TIMEOUT}.
   ** <p>
   ** If {@link WebServiceConstant#CONNECT_TIMEOUT} is not mapped in the
   ** underlying {@link AbstractLookup} this method returns
   ** {@link WebServiceConstant#CONNECT_TIMEOUT_DEFAULT}.
   **
   ** @return                    the timeout period for establishment of the
   **                            HTTP connection.
   */
  public final String connectTimeout() {
    return stringValue(WebServiceConstant.CONNECT_TIMEOUT, WebServiceConstant.CONNECT_TIMEOUT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestTimeout
  /**
   ** Returns the timeout period for reading data on an already established HTTP
   ** connection.
   ** <p>
   ** A non-zero value specifies the timeout when reading from Input stream when
   ** a connection is established to a resource. If the timeout expires before
   ** there is data available for read, a java.net.SocketTimeoutException is
   ** raised. A timeout of zero is interpreted as an infinite timeout.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link WebServiceConstant#REQUEST_TIMEOUT}.
   ** <p>
   ** If {@link WebServiceConstant#REQUEST_TIMEOUT} is not mapped in the
   ** underlying {@link AbstractLookup} this method returns
   ** {@link WebServiceConstant#REQUEST_TIMEOUT_DEFAULT}.
   **
   ** @return                    the maximum time between establishing a
   **                            connection and receiving data from the
   **                            connection.
   */
  public final String requestTimeout() {
    return stringValue(WebServiceConstant.REQUEST_TIMEOUT, WebServiceConstant.REQUEST_TIMEOUT_DEFAULT);
  }
}