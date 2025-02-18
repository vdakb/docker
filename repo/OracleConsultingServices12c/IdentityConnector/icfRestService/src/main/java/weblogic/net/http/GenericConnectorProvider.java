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
*/

package weblogic.net.http;

import org.glassfish.jersey.client.spi.Connector;
import weblogic.jaxrs.client.internal.WlsUrlConnectionProvider;
import javax.ws.rs.client.Client;

/**
 ** Subclass of {@link WlsUrlConnectionProvider} to ensure
 ** the {@link GenericConnectionFactory} will be used to create connections.
 */
public class GenericConnectorProvider extends WlsUrlConnectionProvider {

  /** A {@link GenericConnectionFactory} instance to create HTTP connection. */
  private static final GenericConnectionFactory factory = new GenericConnectionFactory();

  /**
   ** Calls the parent implementation of the method and passes a
   ** {@link GenericConnectionFactory} instance as a {@link ConnectionFactory}
   ** to initialize the URL connection later.
   **
   ** @param client              a {@link Client} passing to the parent
   ** @param connectionFactory   ignored
   ** @param chunkSize           chunkSize parameter of the connection
   ** @param fixLengthStreaming  fixLengthStreaming parameter of the connection
   ** @param setMethodWorkaround setMethodWorkaround parameter of the connection
   ** @return a {@link Connector} instance
   */
  @Override
  protected Connector createHttpUrlConnector(final Client client, ConnectionFactory connectionFactory, int chunkSize, boolean fixLengthStreaming, boolean setMethodWorkaround) {
    return super.createHttpUrlConnector(client, factory, chunkSize, fixLengthStreaming, setMethodWorkaround);
  }
}
