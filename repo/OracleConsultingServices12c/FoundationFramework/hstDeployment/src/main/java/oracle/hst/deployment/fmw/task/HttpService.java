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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   HttpService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    HttpService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.fmw.task;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.type.WWWServerContext;

import oracle.hst.deployment.spi.AbstractServletHandler;

import oracle.hst.deployment.task.AbstractServletTask;

////////////////////////////////////////////////////////////////////////////////
// abstract class HttpService
// ~~~~~~~~  ~~~~~ ~~~~~~~~~~
/**
 ** Invokes a WebService.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class HttpService extends AbstractServletTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final AbstractServletHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Get
  // ~~~~~ ~~~~
  /**
   ** Invokes the Web Service.
   */
  public static class Get extends HttpService {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Get</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Get() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Get</code> {@link HttpService} task that use the
     ** specified {@link WWWServerContext} <code>server</code> as the runtime
     ** environment.
     **
     ** @param  server           the {@link WWWServerContext} used as the
     **                          runtime environment.
     */
    protected Get(final WWWServerContext server) {
      // ensure inheritance
      super(server);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: onExecution (AbstractTask)
    /**
     ** Called by the project to let the task do its work.
     ** <p>
     ** This method may be called more than once, if the task is invoked more
     ** than once. For example, if target1 and target2 both depend on target3,
     ** then running "ant target1 target2" will run all tasks in target3 twice.
     **
     ** @throws ServiceException   if something goes wrong with the build
     */
    @Override
    public void onExecution()
      throws ServiceException {

    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Post
  // ~~~~~ ~~~~
  /**
   ** Invokes the Web Service.
   */
  public static class Post extends HttpService {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Post</code> {@link HttpService} task that allows use
     ** as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Post() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Post</code> {@link HttpService} task that use the
     ** specified {@link WWWServerContext} <code>server</code> as the runtime
     ** environment.
     **
     ** @param  server           the {@link WWWServerContext} used as the
     **                          runtime environment.
     */
    protected Post(final WWWServerContext server) {
      // ensure inheritance
      super(server);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: onExecution (AbstractTask)
    /**
     ** Called by the project to let the task do its work.
     ** <p>
     ** This method may be called more than once, if the task is invoked more
     ** than once. For example, if target1 and target2 both depend on target3,
     ** then running "ant target1 target2" will run all tasks in target3 twice.
     **
     ** @throws ServiceException   if something goes wrong with the build
     */
    @Override
    public void onExecution()
      throws ServiceException {

    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebService</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected HttpService() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new AbstractServletHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>HttpService</code> that use the specified
   ** {@link WWWServerContext} <code>server</code> as the runtime environment.
   **
   ** @param  server             the {@link WWWServerContext} used as the
   **                            runtime environment.
   */
  protected HttpService(final WWWServerContext server) {
    // ensure inheritance
    super(server);

    // initialize instance attributes
    this.handler = new AbstractServletHandler(this);
  }
}