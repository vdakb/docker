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

    File        :   WebServiceConstant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    WebServiceConstant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.soap;

////////////////////////////////////////////////////////////////////////////////
// interface WebServiceConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~
public interface WebServiceConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the URL encoding.
   */
  static final String URL_ENCODING_DEFAULT         = "UTF-8";

  /** the protocol each HTTP server is using */
  static final String PROTOCOL_DEFAULT             = "http";

  /** the protocol each HTTP server is using over SSL */
  static final String PROTOCOL_DEFAULT_SECURE      = "https";

  /**
   ** Default value of the separator to specify multiple value for a
   ** configuration tag name.
   */
  static final String MULTIVALUE_SEPARATOR_DEFAULT = "|";

  /**
   ** Default value of the timeout period for establishment of the HTTP
   ** connection.
   */
  static final String CONNECT_TIMEOUT_DEFAULT      = "3000";

  /**
   ** Default value of the timeout period for reading data on an already
   ** established HTTP connection.
   */
  static final String REQUEST_TIMEOUT_DEFAULT      = "3000";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the URL encoding.
   */
  static final String URL_ENCODING                 = "url-encoding";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the character that separates multiple values for the same entry
   ** tag name.
   */
  static final String MULTIVALUE_SEPARATOR         = "multi-value-separator";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the timeout period for establishment of the HTTP connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an HTTP
   ** server. When connection pooling has been requested, this property also
   ** specifies the maximum wait time or a connection when all connections in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the HTTP provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   */
  static final String CONNECT_TIMEOUT              = "connect-timeout";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the timeout period for reading data on an already established
   ** HTTP connection.
   ** <p>
   ** A non-zero value specifies the timeout when reading from Input stream when
   ** a connection is established to a resource. If the timeout expires before
   ** there is data available for read, a java.net.SocketTimeoutException is
   ** raised. A timeout of zero is interpreted as an infinite timeout.
   */
  static final String REQUEST_TIMEOUT              = "request-timeout";
}