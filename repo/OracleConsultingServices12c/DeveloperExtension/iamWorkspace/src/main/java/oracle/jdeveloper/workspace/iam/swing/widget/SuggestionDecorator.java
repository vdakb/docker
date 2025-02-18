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

    Copyright 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   SuggestionDecorator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SuggestionDecorator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.util.Collection;

import java.awt.Point;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JList;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;

import javax.swing.SwingUtilities;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class SuggestionDecorator<T extends    JComponent>
                                   implements KeyListener
                                   ,          DocumentListener {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private JPopupMenu                   popup;
  private JList<String>                view;
  private DefaultListModel<String>     model;
  private boolean                      swallow;

  private final T                      component;
  private final SuggestionComponent<T> suggestion;

  public SuggestionDecorator(final T component, final SuggestionComponent<T> suggestion) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.component  = component;
    this.suggestion = suggestion;

    // customize the editor to look pretty in a table
//    this.component.setBorder(null);

    // initialize components
    this.popup = new JPopupMenu();
    this.model = new DefaultListModel<>();
    this.view  = new JList<>(this.model);
    this.view.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
    this.view.setFocusable(false);
    this.popup.add(this.view);

    if (this.component instanceof JTextComponent) {
      JTextComponent tc = (JTextComponent)this.component;
      tc.getDocument().addDocumentListener(this);
    }
    // not using key inputMap cause that would override the original handling
    this.component.addKeyListener(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyPressed (KeyListener)
  /**
   ** Invoked when a key has been pressed.
   ** <p>
   ** See the class description for {@link KeyEvent} for a definition of a key
   ** pressed event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  @Override
  public void keyPressed(final KeyEvent event) {
    switch (event.getKeyCode()) {
      case KeyEvent.VK_UP     : moveUp(event);
                                break;
      case KeyEvent.VK_DOWN   : moveDown(event);
                                break;
      case KeyEvent.VK_ENTER  : selected(event);
                                break;
      case KeyEvent.VK_ESCAPE : this.popup.setVisible(false);
                                break;
      case KeyEvent.VK_F12    : this.popup.setVisible(true);
                                break;
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   keyReleased (KeyListener)
  /**
   ** Invoked when a key has been released.
   ** <p>
   ** See the class description for {@link KeyEvent} for a definition of a key
   ** released event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  @Override
  public void keyReleased(final KeyEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyTyped (KeyListener)
  /**
   ** Invoked when a key has been typed.
   ** <p>
   ** See the class description for {@link KeyEvent} for a definition of a key
   ** typed event.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  public void keyTyped(final KeyEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertUpdate (DocumentListener)
  /**
   ** Gives notification that there was an insert into the document.
   ** <br>
   ** The range given by the {@link DocumentEvent} bounds the freshly inserted
   ** region.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link DocumentEvent}.
   */
  @Override
  public final void insertUpdate(final DocumentEvent event) {
    update();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeUpdate (DocumentListener)
  /**
   ** Gives notification that a portion of the document has been
   ** removed.
   ** <br>
   ** The range is given in terms of what the view last saw (that is, before
   ** updating sticky positions).
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link DocumentEvent}.
   */
  @Override
  public final void removeUpdate(final DocumentEvent event) {
    update();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changedUpdate (DocumentListener)
  /**
   ** Gives notification that an attribute or set of attributes changed.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link DocumentEvent}.
   */
  @Override
  public final void changedUpdate(final DocumentEvent event) {
    update();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods groupped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decorate
  /**
   ** Factory method to decorate a swing component with a suggesting popup.
   **
   ** @param  <T>                the swing comonent type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  component          the component the suggestion popup created
   **                            belongs to.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  suggestion         the suggestion provider to populate and display
   **                            the list of suggestions.
   **                            <br>
   **                            Allowed object is {@link SuggestionComponent}
   **                            for type <code>T</code>.
   **
   ** @return                    the decorected component.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public static <T extends JComponent> T decorate(final T component, final SuggestionComponent<T> suggestion) {
    new SuggestionDecorator<>(component, suggestion);
    return component;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Action to control the popup.
   */
  private void update() {
    if (this.swallow) {
      return;
    }
    // perform separately, as listener conflicts between the editing component
    // and JComboBox will result in an IllegalStateException due to editing the
    // component when it is locked
    SwingUtilities.invokeLater(
      () -> {
        final Collection<String> list = suggestion.populate(component);
        if (list != null && !list.isEmpty()) {
          show(list);
           // keep the focus on the component bound
          component.requestFocus();
        }
        else
          hide();
      }
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   show
  /**
   ** Action to show the popup and populate the data.
   **
   ** @param  inventory          the data to display.
   */
  private void show(final Collection<String> inventory) {
    this.model.clear();
    inventory.forEach(this.model::addElement);

    final Point p = this.suggestion.location(this.component);
    if (p == null) {
      return;
    }
    this.popup.pack();
    this.view.setSelectedIndex(0);
    this.popup.show(this.component, (int)p.getX(), (int)p.getY());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hide
  /**
   ** Action to hide the popup and cleanup the data.
   */
  private void hide() {
    this.popup.setVisible(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected
  /**
   ** ...
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  private void selected(final KeyEvent event) {
    if (this.popup.isVisible()) {
      final int index = this.view.getSelectedIndex();
      if (index != -1) {
        hide();
        final String value = this.view.getSelectedValue();
        this.swallow = true;
        this.suggestion.select(this.component, value);
        this.swallow = false;
        event.consume();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moveDown
  /**
   ** Navigates downward through the suggestion list if a {@link KeyEvent}
   ** occured.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  private void moveDown(final KeyEvent event) {
    if (this.popup.isVisible() && this.model.getSize() > 0) {
      final int index = this.view.getSelectedIndex();
      if (index < this.model.getSize()) {
        this.view.setSelectedIndex(index + 1);
        event.consume();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moveUp
  /**
   ** Navigates upward through the suggestion list if a {@link KeyEvent}
   ** occured.
   **
   ** @param  event              the data that characterizes the event.
   **                            <br>
   **                            Allowed object is {@link KeyEvent}.
   */
  private void moveUp(final KeyEvent event) {
    if (this.popup.isVisible() && this.model.getSize() > 0) {
      final int index = this.view.getSelectedIndex();
      if (index > 0) {
        this.view.setSelectedIndex(index - 1);
        event.consume();
      }
    }
  }
}
