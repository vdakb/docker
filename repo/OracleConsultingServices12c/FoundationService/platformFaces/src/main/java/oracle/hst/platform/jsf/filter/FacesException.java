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

    File        :   FacesException.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    FacesException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.filter;

////////////////////////////////////////////////////////////////////////////////
// class FacesException
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>FacesException</code> will solve 2 problems with exceptions thrown in
 ** JSF methods.
 ** <ol>
 **   <li>Mojarra's <code>FacesFileNotFoundException</code> needs to be
 **       interpreted as 404.
 **   <li>Root cause needs to be unwrapped from {@link FacesException} and
 **       {@code ELException} to utilize standard Servlet API error page handling.
 ** </ol>
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This filter won't run on exceptions thrown during ajax requests. To handle
 ** them using <code>web.xml</code> configured error pages, use
 ** {@code FullAjaxExceptionHandler}.
 **
 ** <h3>Installation</h3>
 ** <p>
 ** To get it to run, map this filter on an <code>&lt;url-pattern&gt;</code> of
 ** <code>/*</code> in <code>web.xml</code>.
 ** <pre>
 **   &lt;filter&gt;
 **     &lt;filter-name&gt;facesExceptionFilter&lt;/filter-name&gt;
 **     &lt;filter-class&gt;oracle.hst.platform.jsf.filter.FacesException&lt;/filter-class&gt;
 **   &lt;/filter&gt;
 **   &lt;filter-mapping&gt;
 **     &lt;filter-name&gt;facesExceptionFilter&lt;/filter-name&gt;
 **     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 **   &lt;/filter-mapping&gt;
 ** </pre>
 **
 ** <h3>Error pages</h3>
 ** <p>
 ** Please refer the "Error pages" section of the
 ** {@code FullAjaxExceptionHandler} javadoc for recommended error page
 ** configuration.
 **
 ** <h3>Configuration</h3>
 ** <p>
 ** Please refer the "Configuration" section of the
 ** {@code FullAjaxExceptionHandler} javadoc for available context parameters.
 **
 ** <h3>Customizing <code>FacesException</code></h3>
 ** <p>
 ** If more fine grained control is desired for logging the exception, then the
 ** developer can opt to extend this <code>FacesException</code> and override
 ** one or more of the following protected methods:
 ** <ul>
 **   <li>{@code #logException(HttpServletRequest, Throwable, String, String, Object...)}
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FacesException {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FacesException</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FacesException() {
    // ensure inheritance
    super();
  }
}
