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

    File        :   TriStateRadioButton.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TriStateRadioButton.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import javax.swing.Icon;
import javax.swing.ActionMap;
import javax.swing.JRadioButton;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import javax.swing.plaf.ActionMapUIResource;

////////////////////////////////////////////////////////////////////////////////
// class TriStateRadioButton
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** An implementation of a radio button that have an additional, third state.
 **
 ** Maintenance tip - There were some tricks to getting this code working:
 ** <ol>
 **   <li>You have to overwrite addMouseListener() to do nothing
 **   <li>You have to add a mouse event on mousePressed by calling
 **       super.addMouseListener()
 **   <li>You have to replace the UIActionMap for the keyboard event "pressed"
 **       with your own one.
 **   <li>You have to remove the UIActionMap for the keyboard event "released".
 **   <li>You have to grab focus when the next state is entered, otherwise
 **       clicking on the component won't get the focus.
 **   <li>You have to make a TristateDecorator as a button model that wraps the
 **       original button model and does state management.
 ** </ol>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TriStateRadioButton extends    JRadioButton
                                 implements TriStateButton {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5050668440307759706")
  private static final long serialVersionUID = 6583410582064687712L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with no text, no
   ** icon.
   */
  public TriStateRadioButton() {
    // ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with text.
   **
   ** @param  text               the text of the radio button.
   */
  public TriStateRadioButton(final String text) {
    // ensure inheritance
    this(text, State.UNSELECTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with text and the initial
   ** state.
   **
   ** @param  text               the text of the radio button.
   ** @param  initial            the intial [@link State} radio button.
   */
  public TriStateRadioButton(final String text, final State initial) {
    // ensure inheritance
    this(text, null, initial);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an initially unselected radio button with the specified text
   ** and icon.
   **
   ** @param  text               the text of the radio button.
   ** @param  icon               the {@link Icon} image to display.
   ** @param  initial            the intial [@link State} radio button.
   */
  public TriStateRadioButton(final String text, final Icon icon, final State initial) {
    // ensure inheritance
    super(text, icon);

    // Add a listener for when the mouse is pressed
    super.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(final MouseEvent event) {
        grabFocus();
        model().nextState();
      }
    });
    // Reset the keyboard action map
    ActionMap map = new ActionMapUIResource();
    map.put("pressed", new AbstractAction() {
      @Override
      public void actionPerformed(final ActionEvent event) {
        grabFocus();
        model().nextState();
      }
    });
    map.put("released", null);
    SwingUtilities.replaceUIActionMap(this, map);
    state(initial);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModel
  /**
   ** Sets the model that this check represents.
   **
   ** @param  model              the new <code>Model</code>.
   **
   ** @see   #getModel
   */
  public void setModel(final Model model) {
    model(model);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModel (overridden)
  /**
   ** Returns the model that this check box represents.
   **
   ** @return                    the <code>model</code> property
   ** @see    #setModel(TriStateButton.Model)
   */
  @Override
  public Model getModel() {
    return (Model)super.getModel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state (TriStateButton)
  /**
   ** Set the new state to either
   ** <ul>
   **   <li>{@link TriStateButton.State#SELECTED}
   **   <li>{@link TriStateButton.State#UNSELECTED}
   **   <li>{@link TriStateButton.State#PARTIAL_SELECTED}
   **   <li>{@link TriStateButton.State#PARTIAL_UNSELECTED}
   ** </ul>.
   ** If state is <code>null</code>, it is treated as
   ** {@link TriStateButton.State#UNSELECTED}.
   */
  @Override
  public void state(final State state) {
    model().state(state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state (TriStateButton)
  /**
   ** Return the current state, which is determined by the selection status of
   ** the model.
   ** <p>
   ** Returns
   ** <ul>
   **   <li>{@link TriStateButton.State#SELECTED} when the checkbox is selected
   **       but not armed
   **   <li>{@link TriStateButton.State#UNSELECTED} when the checkbox is
   **       deselected.
   **   <li>{@link TriStateButton.State#PARTIAL_SELECTED} state when the
   **       checkbox is selected and armed (grey)
   **   <li>{@link TriStateButton.State#PARTIAL_UNSELECTED} state when the
   **       checkbox is not selected and armed (grey)
   ** </ul>
   **
   ** @return                  the current state of the model
   */
  @Override
  public State state() {
    return model().state();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model (TriStateButton)
  /**
   ** Sets the model that this check represents.
   **
   ** @param  model              the new <code>TriStateModel</code>.
   **
   ** @see   #getModel
   */
  @Override
  public void model(final Model model) {
    super.setModel(model);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model (TriStateButton)
  /**
   ** Returns the model that this check box represents.
   **
   ** @return                    the <code>model</code> property
   ** @see    #setModel(TriStateButton.Model)
   */
  @Override
  public Model model() {
    return (Model)super.getModel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (overridden)
  /**
   ** Substitutes the underlying radio button model.
   ** <p>
   ** If we had call setModel an exception would be raised because
   ** {@link #setModel(TriStateButton.Model)} calls a {@link #getModel()} where
   ** we returning a {@link TriStateButton.Model}, but at this point we have a
   ** <code>JToggleButtonModel</code>.
   **
   ** @param  text               the text of the check box.
   ** @param  icon               the {@link Icon} image to display.
   */
  @Override
  protected void init(final String text, final Icon icon) {
    this.model = new Model();
    // side effect: set listeners
    super.setModel(this.model);
    super.init(text, icon);
  }
}