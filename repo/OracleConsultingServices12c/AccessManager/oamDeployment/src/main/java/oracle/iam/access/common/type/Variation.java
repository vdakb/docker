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

    File        :   Variation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Variation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.spi.schema.HostPortVariations;
import oracle.iam.access.common.spi.schema.HostPortVariationsList;

////////////////////////////////////////////////////////////////////////////////
// class Variation
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Variation</code> specifies all variations of a particular host by
 ** aggregating host and port elements for each of the load-balancer URLs that
 ** will be protected by <code>Access Agent</code>s.
 ** <p>
 ** Registered <code>Access Agent</code>s protect all requests that match the
 ** addressing methods defined for the host identifier used in a policy. A
 ** request sent to any address on the list is mapped to the official host name
 ** and <code>Access Manager</code> can apply the policies that protect the
 ** resource and <code>Access Manager</code> can apply the policies that protect
 ** the resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Variation extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final HostPortVariationsList delegate = factory.createHostPortVariationsList();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** <code>Server</code> is the host and port element of an load-balancer URL
   ** that will be protected by an <code>Access Agent</code>.
   */
  public static class Server extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final HostPortVariations delegate = factory.createHostPortVariations();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Server</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Server() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setHost
    /**
     ** Sets the value of the <code>host</code> property.
     **
     ** @param  value            the value of the <code>host</code> property.
     **                          Allowed object is {@link String}.
     */
    public void setHost(final String value) {
      checkAttributesAllowed();
      this.delegate.setHost(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setPort
    /**
     ** Sets the value of the <code>port</code> property.
     **
     ** @param  value            the port the host is listening on.
     **                          Allowed object {@link Integer}.
     */
    public void setPort(final Integer value) {
      checkAttributesAllowed();
      this.delegate.setPort(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Variation</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Variation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link HostPortVariationsList} specifiying all variations of a
   ** particular host by aggregating host and port elements for each of the
   ** load-balancer URLs that will be protected by <code>Access Agent</code>s.
   **
   ** @return                    the {@link HostPortVariationsList} delegate.
   */
  public final HostPortVariationsList delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredServer
  /**
   ** Call by the ANT deployment to inject the argument for adding a server.
   **
   ** @param  server             the {@link Variation.Server} instance to set.
   **                            Allowed object is {@link Variation.Server}.
   **
   ** @throws BuildException     if an instance is referencing an already
   **                            set.
   */
  public void addConfiguredServer(final Server server)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (server == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "server"));

    // assign the correspondending server property
    this.delegate.getHostPortVariations().add(server.delegate);
  }
}