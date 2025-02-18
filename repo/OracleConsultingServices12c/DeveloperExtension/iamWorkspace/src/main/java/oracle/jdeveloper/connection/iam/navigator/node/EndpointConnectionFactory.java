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

    File        :   EndpointConnectionFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointConnectionFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import javax.naming.NamingException;

import oracle.ide.util.Assert;

import oracle.adf.share.jndi.AdfJndiContext;
import oracle.adf.share.jndi.ConnectionException;

import oracle.jdeveloper.connection.iam.model.Endpoint;

////////////////////////////////////////////////////////////////////////////////
// class EndpointConnectionFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The model to support the Connection context for creating or modifiying the
 ** connection properties stored in the <code>Endpoint</code> model.
 ** <p>
 ** This data might also been used to display a specific node in the Resource
 ** Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EndpointConnectionFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  final AdfJndiContext connection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>EndpointConnectionFactory</code> for the specified.
   ** {@link AdfJndiContext} providing access to the desired connections.
   **
   ** @param  connection         the {@link AdfJndiContext} providing access to
   **                            the desired connections.
   */
  protected EndpointConnectionFactory(final AdfJndiContext connection) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.connection = connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>EndpointConnectionFactory</code> is a singleton class.
   ** This method gets this manager's single instance.
   **
   ** @param  connection         the {@link AdfJndiContext} providing access to
   **                            the desired connections.
   **
   ** @return                    the <code>EndpointConnectionFactory</code>
   **                            instance.
   */
  public static EndpointConnectionFactory instance(final AdfJndiContext connection) {
    return new EndpointConnectionFactory(connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointProvider
  /**
   ** Returns the {@link Endpoint} associated with the Resource Palette that
   ** matches the identifier.
   ** <p>
   ** The class itslef cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   **
   ** @param  identifier         the identifier of {@link Endpoint} to match.
   **
   ** @return                    the {@link Endpoint} associated with the
   **                            Resource Palette that matches the specified
   **                            identifier.
   */
  protected Endpoint endpointProvider(final String identifier) {
    Object o = null;
    try {
      o = this.connection.lookup(identifier);
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage(), e);
    }
    Assert.check(o instanceof Endpoint);
    return (Endpoint)o;
  }
}