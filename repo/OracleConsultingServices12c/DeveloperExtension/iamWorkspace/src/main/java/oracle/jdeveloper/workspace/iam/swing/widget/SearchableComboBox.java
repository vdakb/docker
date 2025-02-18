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

    File        :   SearchableComboBox.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SearchableComboBox.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.stream.Collectors;

import java.awt.Component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import javax.swing.text.JTextComponent;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.jdeveloper.workspace.iam.swing.Searchable;

////////////////////////////////////////////////////////////////////////////////
// class SearchableComboBox
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A <code>SearchableComboBox</code> is a java swing component that provides a
 ** drop down menu of items.
 ** <br>
 ** A <code>SearchableComboBox</code> can also be set to editable, allowing
 ** users to enter custom data not provided within the drop down menu. For
 ** longer lists, it can be quite useful to allow users to start typing what
 ** they wish, and let the <code>SearchableComboBox</code> perform an
 ** auto-completion, in which the drop down menu is populated with the
 ** appropriate items that match a search criteria for the user entered text.
 ** <p>
 ** Extends {@link JComboBox} to display a list strings.
 ** <br>
 ** Also handles keyboard selection when the combo box is not editable. The list
 ** of items may include a <code>null</code> when a selection is not required.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class SearchableComboBox extends    JComboBox<String>
                                implements FocusListener
                                ,          DocumentListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1847341850162393543")
  private static final long               serialVersionUID = -1702105287909621847L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Searchable<String, String> inventory;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a non-editable <code>SearchableComboBox</code>.
   ** <p>
   ** A {@link KeySelectionManager} is associated with the combo box that is
   ** aware of the specified <code>inventory</code>.
   **
   ** @param  inventory          the inventory backing the component.
   **                            <br>
   **                            Allowed object is {@link Searchable}.
   */
  public SearchableComboBox(final Searchable<String, String> inventory) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.inventory = inventory;

    // initialize instance state
    final Component c = getEditor().getEditorComponent();
    if (c instanceof JTextComponent) {
      final JTextComponent editor = (JTextComponent)c;
      // set the owner
      editor.getDocument().putProperty("editor", editor);
      editor.getDocument().addDocumentListener(this);
      // when the text component changes, focus is gained  and the menu
      // disappears
      // to account for this, whenever the focus is gained by the
      // JTextComponent and it has searchable values, we show the popup
      editor.addFocusListener(this);
    }
    else {
      throw new IllegalStateException("Editing component is not a JTextComponent!");
    }

    // set the approriate properties
    setEditable(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   focusGained (FocusListener)
  /**
   ** Invoked when a component gains the keyboard focus.
   **
   ** @param  event              a {@link FocusEvent} object describing the
   **                            event source and what has changed.
   **                            <br>
   **                            Allowed object is {@link FocusEvent}.
   */
  public void focusGained(final FocusEvent event) {
    System.out.println("focusGained:" + event.getSource().getClass().getName());
    if (!(event.getSource() instanceof JTextComponent))
      return;

    // when the text component changes, focus is gained  and the menu
    // disappears
    // to account for this, whenever the focus is gained by the
    // JTextComponent and it has searchable values, we show the popup
    if (((JTextField)event.getSource()).getText().length() > 0)
      setPopupVisible(true);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   focusLost (FocusListener)
  /**
   ** Invoked when a component loses the keyboard focus.
   **
   ** @param  event              a {@link FocusEvent} object describing the
   **                            event source and what has changed.
   **                            <br>
   **                            Allowed object is {@link FocusEvent}.
   */
  public void focusLost(final FocusEvent event) {
    System.out.println("focusLost:" + event.getSource().getClass().getName());
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
    System.out.println("insertUpdate:" + event.getDocument().getProperty("editor"));
    System.out.println(event.getDocument().getProperty("editor"));
    final Object source = event.getDocument().getProperty("editor");
    if (source != null)
      update((JTextComponent)source, this);
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
    System.out.println("removeUpdate:" + event.getDocument().getProperty("editor"));
    System.out.println(event.getDocument().getProperty("editor"));
    final Object source = event.getDocument().getProperty("editor");
    if (source != null)
      update((JTextComponent)source, this);
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
    // intentionally left blank
  }

  public void update(final JTextComponent editor, final DocumentListener listener) {
    // perform separately, as listener conflicts between the editing component
    // and JComboBox will result in an IllegalStateException due to editing the
    // component when it is locked
    SwingUtilities.invokeLater(
      new Runnable(){
        @Override
        public void run() {
          editor.getDocument().removeDocumentListener(listener);
//          editor.setText(text + match.substring(text.length()));
//          editor.setCaretPosition(text.length());
//          editor.setSelectionStart(Math.min(position, text.length()));
//          editor.setSelectionEnd(match.length());
          // the search operation return a collection where a sort operation
          // isn't applicable hence convert it to a list
          final List<String> matched    = new ArrayList<String>(inventory.filter(editor.getText()));
          // normalize
          final Set<String>  normalized = matched.stream().map(String::toLowerCase).collect(Collectors.toSet());
          // sort alphabetically
          Collections.sort(matched);

//          SearchableComboBox.this.setEditable(false);
          SearchableComboBox.this.removeAllItems();
          // if matched contains the search text, then only add once
          if (!normalized.contains(editor.getText().toLowerCase())) {
            SearchableComboBox.this.addItem(editor.getText());
          }
          for (String cursor : matched) {
            SearchableComboBox.this.addItem(cursor);
          }
//          SearchableComboBox.this.setEditable(true);
          SearchableComboBox.this.setPopupVisible(true);
          editor.getDocument().addDocumentListener(listener);
        }
      }
    );
  }
}
