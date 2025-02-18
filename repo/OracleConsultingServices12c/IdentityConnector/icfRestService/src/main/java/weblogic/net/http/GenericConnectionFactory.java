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

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLStreamHandler;

/**
 ** An implementation of the {@link org.glassfish.jersey.client.HttpUrlConnectorProvider.ConnectionFactory}
 ** to hold an {@link URLStreamHandler} instance and ensure the URL connection opening.
 */
public class GenericConnectionFactory implements HttpUrlConnectorProvider.ConnectionFactory {

  /** A {@link GenericHandler} instance to open url conenction. */
  private static final GenericHandler handler = new GenericHandler();

  /**
   ** Construct a {@link GenericConnectionFactory} instance.
   */
  GenericConnectionFactory() {
  }

  /**
   ** Opens an {@link HttpURLConnection} connection with the help of handler.
   **
   ** @param url                 the {@link URL} to connect
   ** @return                    a {@link HttpURLConnection}
   ** @throws IOException        in case of {@link IOException} happens
   */
  public HttpURLConnection getConnection(URL url) throws IOException {
    return (HttpURLConnection) handler.openConnection(url);
  }
}
