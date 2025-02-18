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

import weblogic.kernel.KernelStatus;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 ** Subclass of {@link URLStreamHandler} to initialize the {@link URLConnection}
 ** based on the protocol.
 */
public class GenericHandler extends Handler {

  private static final boolean useJSSECompatibleHttpsHandler = SecurityHelper.getBoolean("UseJSSECompatibleHttpsHandler");

  /**
   ** Opens a connection to the object referenced by the
   ** {@link URL} argument.
   **
   ** @param      url            the URL that this connects to.
   ** @return                    a {@link URLConnection} object for the
   **                            {@link URL}.
   ** @exception  IOException    if an I/O error occurs while opening the
   **                            connection.
   */
  public URLConnection openConnection(URL url) throws IOException {
    return this.openConnection(url, null);
  }

  /**
   ** Same as openConnection(URL), except that the connection will be
   ** made through the specified proxy; Protocol handlers that do not
   ** support proxying will ignore the proxy parameter and make a
   ** normal connection.
   **
   ** Calling this method preempts the system's default ProxySelector
   ** settings.
   **
   ** @param      url            the URL that this connects to.
   ** @param      proxy          the proxy through which the connection
   **                            will be made. If direct connection is desired,
   **                            Proxy.NO_PROXY should be specified.
   **
   ** @return                    a {@link URLConnection} object for the {@link URL}.
   ** @exception  IOException    if an I/O error occurs while opening the
   **                            connection.
   ** @exception  IllegalArgumentException if either u or p is null,
   **                                      or p has the wrong type.
   ** @exception  UnsupportedOperationException if the subclass that
   **                                           implements the protocol doesn't
   **                                           support this method.
   */
  @Override
  protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
    String protocol = url.getProtocol().toLowerCase();
    if ("http".equals(protocol)) {
      return new GenericHttpURLConnection(url, proxy);
    } else if ("https".equals(protocol)) {
      return useJSSECompatibleHttpsHandler && KernelStatus.isServer() ? new GenericCompatibleHttpsURLConnection(url, proxy) : new GenericHttpsURLConnection(url, proxy);
    }
    return null;
  }
}
