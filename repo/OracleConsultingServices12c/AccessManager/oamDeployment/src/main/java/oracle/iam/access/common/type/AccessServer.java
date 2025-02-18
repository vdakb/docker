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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AccessServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessServer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.access.common.spi.schema.Server;
import oracle.iam.access.common.spi.schema.PrimaryServerList;
import oracle.iam.access.common.spi.schema.SecondaryServerList;

////////////////////////////////////////////////////////////////////////////////
// class AccessServer
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>AccessServer</code> defines the attribute needed to specify
 ** <code>Access Server</code> details for <code>Access Agent</code> instance
 ** for the primary and/or secondary list of <code>Access Server</code> list.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessServer extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final Server delegate = factory.createServer();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Primary
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Primary</code> defines the list of <code>Access Server</code>s
   ** assignable to an <code>Access Agent</code> instance for the primary
   ** <code>Access Server</code> list.
   */
  public static class Primary extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final PrimaryServerList delegate = factory.createPrimaryServerList();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Primary</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Primary() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link PrimaryServerList} delegate of configured
     ** <code>Access Server</code>.
     **
     ** @return                    the {@link PrimaryServerList} delegate.
     */
    public final PrimaryServerList delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   addConfiguredServer
    /**
     ** Call by the ANT deployment to inject the argument for adding a server.
     **
     ** @param  server           the {@link AccessServer} instance to add.
     */
    public void addConfiguredServer(final AccessServer server) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending access server property
      this.delegate.getServer().add(server.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Secondary
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Secondary</code> defines the list of <code>Access Server</code>s
   ** assignable to an <code>Access Agent</code> instance for the secondary
   ** <code>Access Server</code> list.
   */
  public static class Secondary extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final SecondaryServerList delegate = factory.createSecondaryServerList();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Secondary</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Secondary() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link SecondaryServerList} delegate of configured
     ** <code>Access Server</code>.
     **
     ** @return                    the {@link SecondaryServerList} delegate.
     */
    public final SecondaryServerList delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   addConfiguredServer
    /**
     ** Call by the ANT deployment to inject the argument for adding a server.
     **
     ** @param  server           the {@link AccessServer} instance to add.
     */
    public void addConfiguredServer(final AccessServer server) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending access server property
      this.delegate.getServer().add(server.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessServer</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessServer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHost
  /**
   ** Sets the value of the <code>host</code> property.
   **
   ** @param  value              the value of the <code>host</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setHost(final String value) {
    this.delegate.setServerHost(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPort
  /**
   ** Sets the value of the <code>port</code> property.
   **
   ** @param  value              the value of the <code>port</code> property.
   **                            Allowed object is <code>int</code>.
   */
  public void setPort(final int value) {
    this.delegate.setServerPort(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionMax
  /**
   ** Sets the value of the <code>connectionMax</code> property.
   **
   ** @param  value              the value of the <code>connectionMax</code>
   **                            property.
   **                            Allowed object is <code>int</code>.
   */
  public void setConnectionMax(final int value) {
    this.delegate.setConnectionMax(value);
  }
}