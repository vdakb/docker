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

    File        :   MappedTreeCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MappedTreeCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import java.util.Map;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.ListCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class MappedTreeCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Displays an entry that is baccked by a {@link Map}.
 ** <p>
 ** <code>MappedTreeCellRenderer</code> is not opaque and unless you subclass
 ** paint you should not change this.
 ** <p>
 ** See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Trees</a>
 ** in <em>The Java Tutorial</em> for examples of customizing node display using
 ** such classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class MappedListCellRenderer<E> extends    JLabel
                                       implements ListCellRenderer<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5865952000886131321")
  private static final long           serialVersionUID = -2137922202358399661L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final transient Icon                icon;
  final           Map<String, String> model;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs <code>MappedListCellRenderer</code> that use the provided
   ** mapping to display the values in the associated {@link JList}.
   **
   ** @param  icon             the symbol to display in front of each entry
   **                          rendered by this {@link ListCellRenderer}.
   ** @param  model            the {@link Map} this {@link ListCellRenderer}
   **                          use to visualize the values.
   */
  public MappedListCellRenderer(final Icon icon, final Map<String, String> model) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.icon  = icon;
    this.model = model;

    // initialize UI
    setOpaque(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  protected String displayString(final Object value) {
    return this.model.get(value.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: getListCellRendererComponent (ListCellRenderer)
  /**
   ** Return a component that has been configured to display the specified
   ** value.
   ** <p>
   ** That component's paint method is then called to "render" the cell. If it
   ** is necessary to compute the dimensions of a list because the list cells
   ** do not have a fixed size, this method is called to generate a component
   ** on which getPreferredSize can be invoked.
   **
   ** @param  list               the {@link JList} we are painting.
   ** @param  value              the value returned by
   **                            list.getModel().getElementAt(index).
   ** @param  index              the index of the cell to render.
   ** @param  selected           <code>true</code> if the specified cell was
   **                            selected.
   ** @param  focused            <code>true</code> if the specified cell has
   **                            the focus.
   **
   ** @return                    a hash code value for this instance.
   */
  @Override
  public Component getListCellRendererComponent(final JList<? extends E> list, final E value, int index, boolean selected, boolean focused) {
    return renderComponent(this.icon, displayString(value), selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: renderComponent
  /**
   ** Return a component that has been configured to display the specified
   ** value.
   ** <p>
   ** That component's paint method is then called to "render" the cell. If it
   ** is necessary to compute the dimensions of a list because the list cells
   ** do not have a fixed size, this method is called to generate a component
   ** on which getPreferredSize can be invoked.
   **
   ** @param  icon               the {@link Icon} we are painting.
   ** @param  value              the value returned by
   **                            list.getModel().getElementAt(index).
   ** @param  selected           <code>true</code> if the specified cell was
   **                            selected.
   **
   ** @return                    <code>this</code> component.
   */
  protected Component renderComponent(final Icon icon, final String value, boolean selected) {
    setIcon(icon);
    setText(value);
    setForeground(selected ? UIManager.getColor("CombxBox.selectionForeground") : UIManager.getColor("CombxBox.foreground") );
    setBackground(selected ? UIManager.getColor("CombxBox.selectionBackground") : UIManager.getColor("CombxBox.background"));
    return this;
  }
}