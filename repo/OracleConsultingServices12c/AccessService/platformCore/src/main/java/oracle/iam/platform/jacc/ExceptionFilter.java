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

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   ExceptionFilter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExceptionFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;

////////////////////////////////////////////////////////////////////////////////
// class ExceptionFilter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A {@link ContainerRequestFilter} decorator which catches any
 ** {@link WebApplicationException WebApplicationExceptions} thrown by a
 ** delegated {@code ContextRequestFilter}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExceptionFilter implements ContainerRequestFilter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ContainerRequestFilter delegate;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExceptionFilter</code> thta swallows the
   ** {@link WebApplicationException} thrown by the specified
   ** {@link ContainerRequestFilter} <code>delegate</code>.
   **
   ** @param  delegate           the {@link ContainerRequestFilter} to watch.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestFilter}.
   */
  public ExceptionFilter(final ContainerRequestFilter delegate) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interafces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (ContainerRequestFilter)
  /**
   ** Filter method called before a request has been dispatched to a resource.
   ** <p>
   ** Filters in the filter chain are ordered according to their
   ** {@code Priority} class-level annotation value. If a request filter
   ** produces a response by calling
   ** {@link ContainerRequestContext#abortWith(javax.ws.rs.core.Response)}
   ** method, the execution of the (either pre-match or post-match) request
   ** filter chain is stopped and the response is passed to the corresponding
   ** response filter chain (either pre-match or post-match).
   ** <br>
   ** For example, a pre-match caching filter may produce a response in this
   ** way, which would effectively skip any post-match request filters as well
   ** as post-match response filters.
   ** <br>
   ** <b>Note</b>:
   ** However that a responses produced in this manner would still be processed
   ** by the pre-match response filter chain.
   **
   ** @param  request          the request context.
   **                          <br>
   **                          Allowed object is
   **                          {@link ContainerRequestContext}.
   **
   ** @throws IOException      if an I/O exception occurs.
   */
  @Override
  public void filter(final ContainerRequestContext request)
    throws IOException {

    try {
      this.delegate.filter(request);
    }
    catch (WebApplicationException e) {
      // intentionally left blank to pass through
      ;
    }
  }
}