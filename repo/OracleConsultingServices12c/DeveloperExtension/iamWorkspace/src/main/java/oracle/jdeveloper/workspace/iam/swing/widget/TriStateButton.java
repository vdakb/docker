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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   TriStateButton.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TriStateButton.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.io.Serializable;

import javax.swing.DefaultButtonModel;

////////////////////////////////////////////////////////////////////////////////
// interface TriStateButton
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Tris (Quadro) State modell of a hotsport action.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface TriStateButton extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-9142911981554425681")
  static final long serialVersionUID = 4614630926105734710L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum State
  // ~~~~ ~~~~~
  /**
   ** This is a type-safe enumerated type
   */
  enum State { SELECTED, UNSELECTED, PARTIAL_SELECTED, PARTIAL_UNSELECTED }

  //////////////////////////////////////////////////////////////////////////////
  // class Model
  // ~~~~~ ~~~~~
  /**
   ** Exactly which Design Pattern is this?
   ** <p>
   ** Is it an Adapter, a Proxy or a Decorator? In this case, my vote lies with
   ** the Decorator, because we are extending functionality and "decorating"
   ** the original model with a more powerful model.
   */
  class Model extends DefaultButtonModel {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Model</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Model() {
      // ensure inheritance
      super();

      // instialize instance state
      state(State.UNSELECTED);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: selected
    /**
     ** Determines whether the state of the model represents
     ** {@link State#SELECTED}.
     **
     ** @return                  <code>true</code> if the state of the model
     **                          evaluates to {@link State#SELECTED}.
     */
    protected boolean selected() {
      return state() == State.SELECTED;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unselected
    /**
     ** Determines whether the state of the model represents
     ** {@link State#UNSELECTED}.
     **
     ** @return                  <code>true</code> if the state of the model
     **                          evaluates to {@link State#UNSELECTED}.
     */
    protected boolean unselected() {
      return state() == State.UNSELECTED;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: partialSelected
    /**
     ** Determines whether the state of the model represents
     ** {@link State#PARTIAL_SELECTED}.
     **
     ** @return                  <code>true</code> if the state of the model
     **                          evaluates to {@link State#PARTIAL_SELECTED}.
     */
    protected boolean partialSelected() {
      return state() == State.PARTIAL_SELECTED;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: partialUnselected
    /**
     ** Determines whether the state of the model represents
     ** {@link State#PARTIAL_UNSELECTED}.
     **
     ** @return                  <code>true</code> if the state of the model
     **                          evaluates to {@link State#PARTIAL_UNSELECTED}.
     */
    protected boolean partialUnselected() {
      return state() == State.PARTIAL_UNSELECTED;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: state
    /**
     ** Set the new state to either
     ** <ul>
     **   <li>{@link State#SELECTED}
     **   <li>{@link State#UNSELECTED}
     **   <li>{@link State#PARTIAL_SELECTED}
     **   <li>{@link State#PARTIAL_UNSELECTED}
     ** </ul>.
     ** If state is <code>null</code>, it is treated as
     ** {@link State#UNSELECTED}.
     **
     ** @param  state            the {@link State} to set for the component.
     */
    protected void state(final State state) {
      switch (state) {
        default                 :
        case UNSELECTED         : super.setArmed(false);
                                  setPressed(false);
                                  setSelected(false);
                                  break;
        case SELECTED           : super.setArmed(false);
                                  setPressed(false);
                                  setSelected(true);
                                  break;
        case PARTIAL_UNSELECTED : super.setArmed(true);
                                  setPressed(true);
                                  setSelected(false);
                                  break;
        case PARTIAL_SELECTED   : super.setArmed(true);
                                  setPressed(true);
                                  setSelected(true);
                                  break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: state
    /**
     ** Return the current state, which is determined by the selection status of
     ** the model.
     ** <p>
     ** Returns
     ** <ul>
     **   <li>{@link State#SELECTED} when the hotsport is selected but not armed
     **   <li>{@link State#UNSELECTED} when the hotsport is deselected.
     **   <li>{@link State#PARTIAL_SELECTED} state when the hotsport is selected
     **        and armed (grey)
     **   <li>{@link State#PARTIAL_UNSELECTED} state when the hotsport is
     **        unselected and armed (grey)
     ** </ul>
     **
     ** @return                  the current state of the model
     */
    protected State state() {
      if (isSelected() && !isArmed()) {
        return State.SELECTED;
      }
      else if (isSelected() && isArmed()) {
        return State.PARTIAL_SELECTED;
      }
      else if (!isSelected() && isArmed()) {
        return State.PARTIAL_UNSELECTED;
      }
      else {
        return State.UNSELECTED;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   nextState
    /**
     ** Rotates between {@link State#SELECTED}, {@link State#UNSELECTED},
     ** {@link State#PARTIAL_SELECTED} and {@link State#PARTIAL_UNSELECTED}.
     ** <p>
     ** Subclass can override this method to tell the hotspot what next state
     ** is. Here is the default implementation.
     ** <pre>
     **   switch (state()) {
     **     case UNSELECTED         : state(State.SELECTED);
     **                               break;
     **     case SELECTED           : state(State.PARTIAL_UNSELECTED);
     **                               break;
     **     case PARTIAL_UNSELECTED : state(State.PARTIAL_SELECTED);
     **                               break;
     **     case PARTIAL_SELECTED   : state(State.UNSELECTED);
     **                               break;
     ** </pre>
     */
    public void nextState() {
      switch (state()) {
        case UNSELECTED         : state(State.SELECTED);
                                  break;
        case SELECTED           : state(State.PARTIAL_UNSELECTED);
                                  break;
        case PARTIAL_UNSELECTED : state(State.PARTIAL_SELECTED);
                                  break;
        case PARTIAL_SELECTED   : state(State.UNSELECTED);
                                  break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setArmed (overridden)
    /**
     ** Marks the hotspot as armed or unarmed.
     ** <p>
     ** No one may change the armed status except us.
     **
     ** @param  state            whether or not the hotspot should be armed
     **
     ** @see    #isArmed
     */
    @Override
    public final void setArmed(final boolean state) {
      // intentionally left blank
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Set the new state to either
   ** <ul>
   **   <li>{@link State#SELECTED}
   **   <li>{@link State#UNSELECTED}
   **   <li>{@link State#PARTIAL_SELECTED}
   **   <li>{@link State#PARTIAL_UNSELECTED}
   ** </ul>.
   ** If state is <code>null</code>, it is treated as {@link State#UNSELECTED}.
   **
   ** @param  state              the {@link State} to set.
   */
 void state(final State state);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Return the current state, which is determined by the selection status of
   ** the model.
   ** <p>
   ** Returns
   ** <ul>
   **   <li>{@link State#SELECTED} when the checkbox is selected but not armed
   **   <li>{@link State#UNSELECTED} when the checkbox is deselected.
   **   <li>{@link State#PARTIAL_SELECTED} state when the checkbox is selected
   **        and armed (grey)
   **   <li>{@link State#PARTIAL_UNSELECTED} state when the checkbox is not
   **        selected and armed (grey)
   ** </ul>
   **
   ** @return                  the current state of the model
   */
  State state();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Sets the model that this check represents.
   **
   ** @param  model              the new <code>TriStateModel</code>.
   **
   ** @see   #model
   */
  void model(final Model model);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Returns the model that this check box represents.
   **
   ** @return                    the <code>model</code> property
   ** @see    #model(Model)
   */
  Model model();
}