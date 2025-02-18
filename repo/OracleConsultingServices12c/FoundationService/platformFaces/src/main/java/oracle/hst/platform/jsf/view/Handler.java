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
    Subsystem   :   Java Server Faces Feature

    File        :   Handler.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Handler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.view;

import java.util.Map;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;

import javax.faces.context.FacesContext;

////////////////////////////////////////////////////////////////////////////////
// class Handler
// ~~~~~ ~~~~~~~
/**
 ** <code>Handler</code> is the pluggablity mechanism for allowing
 ** implementations of or applications using the Jakarta Server Faces
 ** specification to provide their own handling of the activities in the Render
 ** Response and Restore View phases of the request processing lifecycle.
 ** <br>
 ** This allows for implementations to support different response generation
 ** technologies, as well as alternative strategies for saving and restoring the
 ** state of each view.
 ** <p>
 ** The implementation of is <b>thread-safe</b>.
 ** <p>
 ** Java Server Faces (JSF) is a very useful server-side rendering framework to
 ** create HTML UI in Java projects. It's part of the Java EE specifications.
 ** One of the drawbacks of Java Server Faces is the generated URLs for our
 ** pages, they follow as pattern the structure of the folders in the project.
 ** <p>
 ** From a UIX perspective, having URL's like
 ** <code>/pages/user/user-list.xhml</code> looks ugly, it's better to have an
 ** URL like <code>/users</code>, <code>/user/new</code>. Also, from a security
 ** perspective, we are exposing our folder's structure to other people, and
 ** they can use that information to find a vulnerability.
 ** <p>
 ** That's why there are some third party libraries for JSF like pretty-faces,
 ** that allows to use friendly and nice URL's in our JSF application. However,
 ** they usually offer a bunch of functionalities (patterns, mapping path
 ** params, and more) that are not necesary in some of the applications.
 ** Therefore, we have created a very simple rewrite URL for JSF and avoid
 ** overload the application with things that not needed.
 ** <br>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Handler extends ViewHandlerWrapper {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ViewHandler         delegate;

  private final Map<String, String> route;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Handler</code> that wrappes the specified
   ** {@link ViewHandler}.
   **
   ** @param  wrapped            the instance that we are wrapping.
   **                            <br>
   **                            Allowed object is {@link ViewHandler}.
   */
  public Handler(final ViewHandler wrapped) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = wrapped;
    this.route    = Path.instance().page();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActionURL (overridden)
  /**
   ** If the value returned from this method is used as the file argument to the
   ** four-argument constructor for {@link java.net.URL URL} (assuming
   ** appropriate values are used for the first three arguments), then a client
   ** making a request to the toExternalForm() of that {@link java.net.URL URL}
   ** will select the argument viewId for traversing the Jakarta Server Faces
   ** lifecycle.
   ** <br>
   ** Please see section JSF.7.6.2 for the complete specification, especially
   ** for details related to view protection using the
   ** ResponseStateManager.NON_POSTBACK_VIEW_TOKEN_PARAM and the behavior when
   ** the current request is to a {@link java.net.URL URL} for which the
   ** FacesServlet has an exact mapping as defined by Servlet.12.2.
   **
   ** @param  context            the {@link FacesContext} for this request.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** @param  viewId             the identifier of the desired view.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the action {@link java.net.URL URL}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getActionURL(final FacesContext context, final String viewId) {
    // ensure inheritance
    final String url = super.getActionURL(context, viewId);
    String pagepath  = url;
    String parameter = "";
    int    query     = url.indexOf('?');
    if (query != -1) {
      pagepath  = url.substring(query);
      parameter = url.substring(0, query);
    }

    final String contextPath = context.getExternalContext().getRequestContextPath();
    final String action      = pagepath.substring(contextPath.length());
    if (this.route.containsKey(action)) {
      return contextPath + this.route.get(action) + parameter;
    }
    return url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getWrapped (ViewHandlerWrapper)
  /**
   ** Return the instance of the class being wrapped.
   **
   ** @return                    the instance that we are wrapping.
   **                            <br>
   **                            Possible object is {@link ViewHandler}.
   */
  @Override
  public ViewHandler getWrapped() {
    return this.delegate;
  }
}