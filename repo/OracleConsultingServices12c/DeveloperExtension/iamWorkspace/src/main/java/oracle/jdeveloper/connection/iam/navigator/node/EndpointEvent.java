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

    File        :   EndpointEvent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointEvent.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import java.util.EventObject;

////////////////////////////////////////////////////////////////////////////////
// class EndpointEvent
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** An <code>EventObject</code> fired if the state or the structure of an
 ** element has been changed.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EndpointEvent extends EventObject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("oracle.jdeveloper.java.unrestricted-field-access")
  public static Object      EVENT_OBJECT_CREATED  = "Generic.OBJECT_CREATED";
  @SuppressWarnings("oracle.jdeveloper.java.unrestricted-field-access")
  public static Object      EVENT_OBJECT_RELEASED = "Generic.OBJECT_RELEASED";
  @SuppressWarnings("oracle.jdeveloper.java.unrestricted-field-access")
  public static Object      EVENT_OBJECT_CHANGED  = "Generic.OBJECT_CHANGED";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-490614481121491472")
  private static final long serialVersionUID      = 2253549446380497692L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  protected final Object    type;
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  protected final Object    data;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class Source
  // ~~~~~ ~~~~~~
  /**
   ** An <code>EnpointEvent</code> fired if the state of an element has been
   ** changed.
   */
  public interface Source {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: attach
    /**
     ** Registers a listener interested in being notified when the internal
     ** state of an element changes.
     **
     ** @param  listener         the {@link EndpointListener.State}
     **                          interested in change notification messages.
     */
    void attach(final EndpointListener.State listener); 

    ////////////////////////////////////////////////////////////////////////////
    // Method: detach
    /**
     ** Unregisters a listener that is not interested anymore in being notified
     ** when the internal state of an element changes.
     **
     ** @param  listener           the {@link EndpointListener.State}
     **                            disinterested in change notification
     **                            messages.
     */
    void detach(final EndpointListener.State listener);

    ////////////////////////////////////////////////////////////////////////////
    // Method: changed
    /**
     ** Callback method in case a change on the state of an element happend.
     **
     ** @param  event            a {@link EndpointEvent.State} object describing
     **                          the event source and the data that has changed.
     */
    void changed(final EndpointEvent.State event);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class State
  // ~~~~~ ~~~~~
  /**
   ** An <code>EnpointEvent</code> fired if the state of an element has been
   ** changed.
   */
  public static class State extends EndpointEvent {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:314230834679591642")
    private static final long serialVersionUID = 7342961984572902032L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>State</code> event that belongs to the
     ** specified source and type.
     **
     ** @param  source           the object on which the Event initially
     **                          occurred.
     ** @param  type             the type of the source the event belongs to.
     */
    public State(final Object source, final Object type) {
      // ensure inheritance
      super(source, type);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>State</code> that belongs to the
     ** specified source and type.
     **
     ** @param  source             the object on which the Event initially
     **                            occurred.
     ** @param  type               the type of the source the event belongs to.
     ** @param  data               the data of the source the event belongs to.
     */
    public State(final Object source, final Object type, final Object data) {
      // ensure inheritance
      super(source, type, data);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Structure
  // ~~~~~ ~~~~~~~~~~
  /**
   ** An <code>EnpointEvent</code> fired if the structure of an element has been
   ** changed.
   */
  public static class Structure extends EndpointEvent {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:6415803926021480849")
    private static final long serialVersionUID = 6424461406725222943L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Structure</code> event that belongs to the
     ** specified source and type.
     **
     ** @param  source           the object on which the Event initially
     **                          occurred.
     ** @param  type             the type of the source the event belongs to.
     */
    public Structure(final Object source, final Object type) {
      // ensure inheritance
      super(source, type);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Structure</code> that belongs to the
     ** specified source and type.
     **
     ** @param  source             the object on which the Event initially
     **                            occurred.
     ** @param  type               the type of the source the event belongs to.
     ** @param  data               the data of the source the event belongs to.
     */
    public Structure(final Object source, final Object type, final Object data) {
      // ensure inheritance
      super(source, type, data);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointEvent</code> that belongs to the
   ** specified source and type.
   **
   ** @param  source             the object on which the Event initially
   **                            occurred.
   ** @param  type               the type of the source the event belongs to.
   */
  public EndpointEvent(final Object source, final Object type) {
    // ensure inheritance
    this(source, type, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointEvent</code> that belongs to the
   ** specified source and type.
   **
   ** @param  source             the object on which the Event initially
   **                            occurred.
   ** @param  type               the type of the source the event belongs to.
   ** @param  data               the data of the source the event belongs to.
   */
  public EndpointEvent(final Object source, final Object type, final Object data) {
    // ensure inheritance
    super(source);

    // initialize instance attributes
    this.type = type;
    this.data = data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the source the event belongs to.
   **
   ** @return                    the type of the source the event belongs to.
   */
  public Object type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data
  /**
   ** Returns the data of the source the event belongs to.
   **
   ** @return                    the data of the source the event belongs to.
   */
  public Object data() {
    return this.data;
  }
}