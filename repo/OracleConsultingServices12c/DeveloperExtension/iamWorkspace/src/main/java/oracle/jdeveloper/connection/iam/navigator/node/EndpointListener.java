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

    File        :   EndpointListener.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    EndpointListener.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import java.util.EventListener;

import oracle.ide.model.UpdateMessage;

////////////////////////////////////////////////////////////////////////////////
// interface EndpointListener
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** An <code>EventListener</code> observing the state and/or structure changes
 ** of an element.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public interface EndpointListener extends EventListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int STATE     = UpdateMessage.newMessageID("STATE_CHANGED");
  public static final int STRUCTURE = UpdateMessage.STRUCTURE_CHANGED;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class State
  // ~~~~~ ~~~~~
  /**
   ** An <code>EndpointListener</code> observing the state changes of an
   ** element.
   */
  interface State extends EndpointListener {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: changed
    /**
     ** Callback method in case a change on the structure of an element happend.
     **
     ** @param  event            a {@link EndpointEvent.State} object
     **                          describing the event source and the data that
     **                          has changed.
     */
    void changed(final EndpointEvent.State event);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Structure
  // ~~~~~ ~~~~~~~~~
  /**
   ** An <code>EndpointListener</code> observing the structure changes of an
   ** element.
   */
  interface Structure extends EndpointListener {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: changed
    /**
     ** Callback method in case a change on the structure of an element happend.
     **
     ** @param  event            a {@link EndpointEvent.Structure} object
     **                          describing the event source and the data that
     **                          has changed.
     */
    void changed(final EndpointEvent.Structure event);
  }
}