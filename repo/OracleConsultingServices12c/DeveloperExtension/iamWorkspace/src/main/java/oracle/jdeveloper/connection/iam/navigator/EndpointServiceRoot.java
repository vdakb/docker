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

    File        :   EndpointServiceRoot.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointServiceRoot.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator;

import javax.naming.event.NamingEvent;
import javax.naming.event.EventContext;
import javax.naming.event.NamingListener;
import javax.naming.event.ObjectChangeListener;
import javax.naming.event.NamingExceptionEvent;

import oracle.adf.rc.core.RCInstance;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointFolder;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointServiceRoot
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** A navigator service entry for the custom "Endpoint" navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointServiceRoot extends    EndpointFolder
                                          implements Manageable.Composite
                                          ,          Manageable.Removeable
                                          ,          Manageable.Refreshable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6155829167315963085")
  private static final long serialVersionUID = -8479787417055395257L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final EndpointNavigatorRoot    navigator;
  protected final transient NamingListener listener = new ObjectChangeListener() {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: namingExceptionThrown (NamingListener)
    /**
     ** Called when a naming exception is thrown while attempting to fire a
     ** <code>NamingEvent</code>.
     **
     ** @param  event             the nonnull event.
     */
    @Override
    public void namingExceptionThrown(final NamingExceptionEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: objectChanged (ObjectChangeListener)
    /**
     ** Called when an object has been changed.
     ** <p>
     ** The binding of the changed object can be obtained using
     ** <code>event.getNewBinding()</code>. Its old binding (before the change)
     ** can be obtained using <code>event.getOldBinding()</code>.
     **
     ** @param  event             the nonnull event.
     **
     ** @see    NamingEvent#OBJECT_CHANGED
     */
    public void objectChanged(final NamingEvent event) {
      refresh();
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor

  /**
   ** Constructs an <code>EndpointServiceRoot</code> that allows use as a
   ** JavaBean.
   **
   ** @param  navigator          the Initial Context that owns this context.
   **                            <br>
   **                            Allowed object is
   **                            {@link EndpointNavigatorRoot}.
   ** @param  manageable         the data provider for this
   **                            {@link EndpointFolder}.
   **                            <br>
   **                            Allowed object is {@link Manageable}.
   */
  protected EndpointServiceRoot(final EndpointNavigatorRoot navigator, final Manageable manageable) {
    // ensure inheritance
    super(navigator, manageable);

    // initialize instance attributes
    this.navigator = navigator;

    // initialize instance state
    attachListener();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attachListener
  /**
   ** Adds the listener for receiving naming events fired when the object named
   ** by the string target name and scope changes.
   **
   ** @see    #detachListener
   */
  private void attachListener() {
    final RCInstance i = this.navigator.session().getRCInstance();
    try {
      final EventContext ctx = (EventContext)i.getConnectionContext();
      ctx.addNamingListener("manageable().getName()", 0, this.listener);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detachListener
  /**
   ** Removes the listener from receiving naming events fired by an
   ** {@link EventContext}.
   **
   ** @see    #attachListener
   */
  @SuppressWarnings("unused")
  private void detachListener() {
    final RCInstance i = this.navigator.session().getRCInstance();
    try {
      final EventContext ctx = (EventContext)i.getConnectionContext();
      ctx.removeNamingListener(this.listener);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}