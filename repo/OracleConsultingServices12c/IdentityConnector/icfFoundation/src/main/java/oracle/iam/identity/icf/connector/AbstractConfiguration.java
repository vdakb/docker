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

    File        :   AbstractConnector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConnector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import org.identityconnectors.framework.spi.Configuration;

import org.identityconnectors.framework.common.objects.ConnectorMessages;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractConfiguration
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Convenient base-class for {@link Configuration} objects to extend. 
 ** <p>
 ** Encapsulates the configuration of a connector.
 ** <bR>
 ** The configuration includes every property that a caller <b>must</b> specify
 ** in order to use the connector, as well as every property that a caller
 ** <b>may</b> specify in order to affect the behavior of the connector overall
 ** (as opposed to operation options, which affect only a specific invocation of
 ** a specific operation).
 ** <br>
 ** Required configuration parameters generally include the information required
 ** to connect to a target instance -- such as a URL, username and password.
 ** <br>
 ** Optional configuration parameters often specify preferences as to how the
 ** connector-bundle should behave.
 ** 
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractConfiguration extends    AbstractConnectorLogger
                                            implements Configuration {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ConnectorMessages messages;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractConfiguration</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  category           the category for the Logger.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected AbstractConfiguration(final String category) {
    // ensure inheritance
    super(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectorMessages (Configuration)
  /**
   ** Sets the {@link ConnectorMessages message catalog} instance that allows
   ** the Connector to localize messages.
   ** <br>
   ** This method is called before any bean property setter, the
   ** {@link #validate()} method or the {@link #getConnectorMessages()} method.
   **
   ** @param  messages           the message catalog.
   **                            <br>
   **                            Allowed object is {@link ConnectorMessages}.
   */
  @Override
  public final void setConnectorMessages(final ConnectorMessages messages) {
    this.messages = messages;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectorMessages (Configuration)
  /**
   ** Returns the {@link ConnectorMessages message catalog} that is set by
   ** {@link #setConnectorMessages(ConnectorMessages)}.
   **
   ** @return                    the <code>ConnectorMessages</code> instance.
   **                            <br>
   **                            Possible object is {@link ConnectorMessages}.
   */
  public final ConnectorMessages getConnectorMessages() {
   return this.messages;
  }
}