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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   MetadataServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataServer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

////////////////////////////////////////////////////////////////////////////////
// class MetadataServer
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties stored in the <code>MetadataServer</code> model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class MetadataServer extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the metadata partition where this <code>IT Resource</code>
   ** will be connecting to.
   */
  public static final String  PARTITION_NAME      = "partitionName";

  private static final int    DEFAULT_SERVER_PORT = 1521;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MetadataServer</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MetadataServer() {
    // ensure inheritance
    super(MetadataServer.class, MetadataServerFactory.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort (overridden)
  /**
   ** Returns the listener port of the server the target system is deployed on
   ** and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PORT}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the listener port of the server the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  @Override
  public int serverPort() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return integerValue(SERVER_PORT, DEFAULT_SERVER_PORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Returns the database service URL this <code>IT Resource</code> is
   ** configured for.
   **
   ** @return                    the database context URL this
   **                            <code>IT Resource</code> is configured for.
   */
  public String serviceURL() {
    return String.format("jdbc:oracle:thin:@%s:%d/%s", serverName(), serverPort(), serverContext());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Sets the name of the partition this <code>IT Resource</code> is configured
   ** for.
   **
   ** @param  partition          the name of the partition this
   **                            <code>IT Resource</code> is configured for.
   */
  public void partition(final String partition) {
    property(PARTITION_NAME, partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the name of the partition this <code>IT Resource</code> is
   ** configured
   ** for.
   **
   ** @return                    the name of the partition this
   **                            <code>IT Resource</code> is configured for.
   */
  public String partition() {
    return stringValue(PARTITION_NAME);
  }
}