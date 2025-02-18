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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   FMWServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FMWServer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    Change setter/getter from name
                                               to file.
                                               --
                                               Added the common property server
                                               to maintain the name of a Managed
                                               Server on a WebLogic platform.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import oracle.javatools.data.HashStructure;

////////////////////////////////////////////////////////////////////////////////
// class FMWServer
// ~~~~~ ~~~~~~~~~
/**
 ** The model to support the Preference dialog for creating the preferences
 ** stored in the <code>FMWServer</code> model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class FMWServer extends ServerConfig {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PARTITION        = "partition";
  public static final String PROTOCOL         = "protocol";

  public static final String DEFAULT_PROTOCOL = "t3";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FMWServer</code>.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected FMWServer(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Call to inject the argument for parameter <code>protocol</code>.
   **
   ** @param  protocol           the protocol to communicate with the JEE
   **                            Server.
   */
  public final void protocol(final String protocol) {
    this._hash.putString(PROTOCOL, protocol);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Returns the default protocol to communicate with the server component.
   **
   ** @return                    the protocol to communicate with the JEE
   **                            Server.
   */
  public final String protocol() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROTOCOL, defaultProtocol());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultProtocol
  /**
   ** Returns the default protocol to communicate with the server component.
   **
   ** @return                    the default protocol to communicate with the
   **                            server component.
   */
  public abstract String defaultProtocol();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Sets the metadata partition used by the component configuration.
   **
   ** @param  partition          the metadata partition used by the component
   **                            configuration.
   */
  public final void partition(final String partition) {
    this._hash.putString(PARTITION, partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the metadata partition used by the component configuration.
   **
   ** @return                    the metadata partition used by the component
   **                            configuration.
   */
  public final String partition() {
    return string(PARTITION, defaultPartition());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultPartition
  /**
   ** Returns the default metadata partition used by the component configuration.
   **
   ** @return                    the default metadata partition used by the
   **                            component configuration.
   */
  public abstract String defaultPartition();
}