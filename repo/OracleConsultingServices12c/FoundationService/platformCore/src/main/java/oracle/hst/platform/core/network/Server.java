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

    File        :   Server.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Server.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.network;

///////////////////////////////////////////////////////////////////////////////
// class Server
// ~~~~~ ~~~~~~
/**
 ** <code>Server</code> is a value holder for the failover configuration of a
 ** <code>Service Provider</code> that isn't frontended by a load balancer.
 */
public class Server {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name of the server of the <code>Service Provider</code>
   ** (the iron).
   */
  public final String  host;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the port number the <code>Service Provider</code> endpoint is
   ** listening on.
   */
  public final int     port;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify iwhether or not to use SSL to secure communication with the
   ** <code>Service Provider</code> endpoint.
   */
  public final boolean secure;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Server</code> with host name and port.
   **
   ** @param  host               the host name of the server of the
   **                            <code>Service Provider</code> (the iron).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port number the
   **                            <code>Service Provider</code> endpoint is
   **                            listening on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  secure             specifies whether or not to use SSL to secure
   **                            communication with the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private Server(final String host, final int port, final boolean secure) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.host = host;
    this.port = port;
    this.secure = secure;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host
  /**
   ** Returns the <code>host</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the host name of the server of the
   **                            <code>Service Provider</code> (the iron).
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String host() {
    return this.host;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  port
  /**
   ** Returns the <code>port</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the port number the
   **                            <code>Service Provider</code> endpoint is
   **                            listening on.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int port() {
    return this.port;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  secure
  /**
   ** Returns the <code>secure</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    whether or not to use SSL to secure
   **                            communication with the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean secure() {
    return this.secure;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  of
  /**
   ** Factory method to create a <code>Server</code> with the specified
   ** host and port.
   **
   ** @param  host               the host name of the server of the
   **                            <code>Service Provider</code> (the iron).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port number the
   **                            <code>Service Provider</code> endpoint is
   **                            listening on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  secure             specifies whether or not to use SSL to secure
   **                            communication with the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>Server</code> populated with
   **                            the properties specified.
   **                            <br>
   **                            Possible object is <code>Server</code>.
   */
  public static Server of(final String host, final int port, final boolean secure) {
    return new Server(host, port, secure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server part of an url, <em>protocol</em>://host:port.
   **
   ** @param  protocol           the protocol of the url to build.
   **
   ** @return                    the server part of the an url,
   **                            <em>protocol</em>://host:port
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String serviceURL(final String protocol) {
    final StringBuilder service = new StringBuilder(protocol);
    service.append("://");
    if (this.host != null)
      service.append(this.host);
    if (this.port != -1)
      service.append(':').append(this.port);

    return service.toString();
  }
}
