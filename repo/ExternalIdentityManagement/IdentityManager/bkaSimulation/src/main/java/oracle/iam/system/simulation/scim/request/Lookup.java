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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.request;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;

import oracle.iam.system.simulation.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** A factory for SCIM lookup requests.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request extending
 **                              this class (requests can return their own
 **                              specific type instead of type defined by this
 **                              class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Lookup<T extends Lookup<T>> extends Return<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The version to match */
  protected String version;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Typed
  // ~~~~~ ~~~~~
  /**
   ** The order in which the sortBy parameter is applied.
   */
  /**
   ** A builder for SCIM lookup requests for where the returned resource POJO
   ** type will be provided.
   */
  public static final class Typed extends Lookup<Typed> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for Create a new generic retrieve request builder.
     **
     ** @param  target           the {@link WebTarget} to GET.
     **                          <br>
     **                          Allowed object is {@link WebTarget}.
     */
    public Typed(final WebTarget target) {
      // ensure inheritance
      super(target);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: match
    /**
     ** Lookup the resource only if the resource has been modified since the
     ** provided version. If the resource has not been modified,
     ** NotModifiedException will be thrown when calling invoke.
     **
     ** @param  version            the version of the resource to compare.
     **
     ** @return                    the {@link Typed} to allow method chaining.
     **                            <br>
     **                            Possible object is {@link Typed}.
     */
    public Typed match(final String version) {
      this.version = version;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke
    /**
     ** Invoke the SCIM lookup request.
     **
     ** @param  <T>              the type of object to return.
     ** @param  clazz            the Java {@link Class} type used to determine
     **                          the type to return.
     **
     ** @return                  the successfully lookedup SCIM resource.
     **
     ** @throws ServiceException if the SCIM service provider responded with an
     **                          error.
     */
    public <T> T invoke(final Class<T> clazz)
      throws ServiceException {

      Response response = buildRequest().get();
      try {
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
          return response.readEntity(clazz);
        }
        else {
          throw toException(response);
        }
      }
      finally {
        response.close();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM lookup request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   */
  public Lookup(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildRequest (overridden)
  /**
   ** Factory method to create a {@link Invocation.Builder} for the request.
   **
   ** @return                     the {@link Invocation.Builder} for the request.
   */
  @Override
  Invocation.Builder buildRequest() {
    Invocation.Builder request = super.buildRequest();
    if (this.version != null) {
      request.header(HttpHeaders.IF_NONE_MATCH, this.version);
    }
    return request;
  }
}