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

    File        :   GridBagBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    GridBagBuilder.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;

import java.io.Serializable;

import java.awt.Insets;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class GridBagBuilder
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A form builder that simplifies usage of {@link GridBagLayout}.
 ** <br>
 ** The initial constraints are as follows:
 ** <ul>
 **   <li><code>gridx</code>   = <code>0</code>
 **   <li><code>gridy</code>   = <code>0</code>
 **   <li><code>weightx</code> = <code>1.0D</code>
 **   <li><code>weighty</code> = <code>1.0D</code>
 **   <li><code>fill</code>    = <code>HORIZONTAL</code>
 **   <li><code>anchor</code>  = <code>WEST</code>
 ** </ul>
 ** The constraints are updated using a {@link Formula} when each component is
 ** added.
 ** <p>
 ** By default, the following components are automatically wrapped in a
 ** {@link JScrollPane}:
 ** <ul>
 **   <li>{@link JList}
 **   <li>{@link JTable}
 **   <li>{@link JTextArea}
 ** </ul>
 ** Automatic wrapping of components in {@link JScrollPane} can be
 ** enabled/disabled using {@link #scrollable(Class)} and
 ** {@link #standalone(Class)}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class GridBagBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Formula                EMPTY   = new Empty();
  private static final Set<Class<?>>          SCROLL  = defaultScrollable();
  private static final Map<Class<?>, Formula> FORMULA = defaultFormula();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private int                          x = 0;
  private int                          y = 0;
  private JLabel                       l;
  private GridBagConstraints           gbc = LayoutFactory.relatedConstraints();

  private Container                    container;

  private final int                    columns;
  private final Set<Class<?>>          scrollable;
  private final Map<Class<?>, Formula> constraint;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Formula
  // ~~~~~ ~~~~~~~
  /**
   ** An interface for generating {@link GridBagConstraints}.
   **
   ** @see GridBagBuilder
   */
  public interface Formula extends Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: gbc
    /**
     ** Create an instance of <code>Formula</code>.
     **
     ** @param  label            the <code>Formula</code> to use for the label.
     **                          <br>
     **                          Allowed object is <code>Formula</code>.
     ** @param  anchor           the anchor position for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  width            the grid width for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  height           the grid height for the field,
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  weightx          the width size allocation for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  weighty          the height size allocation for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  indent           the left inset for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  an instance of <code>Formula</code> with the
     **                          properties applied.
     **                          <br>
     **                          Possible object is <code>Formula</code>.
     */
    static Formula gbc(final Formula label, final int anchor, final int width, final int height, final double weightx, final double weighty, final int indent) {
      return gbc(label, anchor, width, height, weightx, weighty, new Insets(0, indent, 0, 0));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: gbc
    /**
     ** Create an instance of <code>Formula</code>.
     **
     ** @param  label            the <code>Formula</code> to use for the label.
     **                          <br>
     **                          Allowed object is <code>Formula</code>.
     ** @param  anchor           the anchor position for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  width            the grid width for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  height           the grid height for the field,
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  weightx          the width size allocation for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  weighty          the height size allocation for the field.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  insets           the insets for the field.
     **                          <br>
     **                          Allowed object is {@link Insets}.
     **
     ** @return                  an instance of <code>Formula</code> with the
     **                          properties applied.
     **                          <br>
     **                          Possible object is <code>Formula</code>.
     */
    static Formula gbc(final Formula label, final int anchor, final int width, final int height, final double weightx, final double weighty, final Insets insets) {
      return new Formula() {
        @Override
        public Formula label() {
          return label;
        }
        @Override
        public GridBagConstraints with(final GridBagConstraints gbc) {
          if (label == null && anchor == GridBagConstraints.WEST && width == 1) {
            // unlabeled component, e.g. checkbox
            gbc.gridx = Math.max(gbc.gridx, 0) + 1;
          }
          gbc.anchor         = anchor;
          gbc.gridwidth      = width;
          gbc.gridheight     = height;
          gbc.weightx        = weightx;
          gbc.weighty        = weighty;
          gbc.insets.top    += insets.top;
          gbc.insets.bottom += insets.bottom;
          gbc.insets.left   += insets.left;
          gbc.insets.right  += insets.right;
          if (weightx > 0) {
            gbc.fill = weighty > 0 ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
          }
          else {
            gbc.fill = GridBagConstraints.NONE;
          }
          return gbc;
        }
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: label
    /**
     ** Returns the constraints to use for the label associated with the current
     ** component.
     **
     ** @return                  the <code>Formula</code> to use for the label.
     **                          <br>
     **                          Possible object is <code>Formula</code>.
     */
    Formula label();

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Apply the constraints for the current component.
     **
     ** @param  gbc              the constraints to be updated.
     **                          <br>
     **                          Allowed object is {@link GridBagConstraints}.
     **
     ** @return                  the updated <code>gbc</code> parameter.
     **                          <br>
     **                          Possible object is {@link GridBagConstraints}.
     */
    GridBagConstraints with(final GridBagConstraints gbc);
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Empty
  // ~~~~~ ~~~~~
  /**
   ** An interface for generating {@link GridBagConstraints}.
   **
   ** @see GridBagBuilder
   */
  private static class Empty implements Formula {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-4656619563518286114")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: label (Formula)
    /**
     ** Returns the constraints to use for the label associated with the current
     ** component.
     **
     ** @return                  the <code>Formula</code> to use for the label.
     **                          <br>
     **                          Possible object is <code>Formula</code>.
     */
    @Override
    public final Formula label() {
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Apply the constraints for the current component.
     **
     ** @param  gbc              the constraints to be updated.
     **                          <br>
     **                          Allowed object is {@link GridBagConstraints}.
     **
     ** @return                  the updated <code>gbc</code> parameter.
     **                          <br>
     **                          Possible object is {@link GridBagConstraints}.
     */
    @Override
    public final GridBagConstraints with(final GridBagConstraints gbc) {
      return gbc;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Element
  // ~~~~ ~~~~~~~
  public enum Element implements Formula {
      /**
       ** Label constraints for {@link #TABLE}, {@link #LIST}, {@link #AREA} and
       ** {@link #GROUP}.
       ** <br>
       ** The label is placed on the row above the component.
       */
      HEADER(null, GridBagConstraints.WEST, 2, 1, 0.0D, 0.0D, 0)
      /**
       ** Label constraints for {@link #TEXT}.
       ** <br>
       ** The label is placed in the column to the left of the field.
       */
    , PROMPT(null, GridBagConstraints.EAST, 1, 1, 0.0D, 0.0D, 5)
      /**
       ** Constraints for a {@link JCheckBox}.
       */
    , CHECK(null, GridBagConstraints.WEST, 1, 1, 1.0D, 0.0D, 0)
      /**
       ** Constraints for a {@link JPanel}.
       */
    , PANEL(null, GridBagConstraints.WEST, 2, 1, 1.0D, 1.0D, 0)
      /**
       ** Constraints for a {@link JTextField}.
       */
    , TEXT(PROMPT, GridBagConstraints.WEST, 1, 1, 1.0D, 0.0D, 0)
      /**
       ** Constraints for a {@link JTextArea}.
       */
    , AREA(PROMPT, GridBagConstraints.WEST, 1, 1, 1.0D, 0.3D, 0)
      /**
       ** Constraints for a {@link JList}.
       */
    , LIST(HEADER, GridBagConstraints.WEST, 2, 1, 1.0D, 1.0D, 0)
      /**
       ** Constraints for a {@link JTable}.
       */
    , TABLE(HEADER, GridBagConstraints.WEST, 2, 1, 1.0D, 1.0D, 0)
      /**
       ** Constraints for a button group (a {@link Box} or a {@link JPanel}).
       ** <br>
       ** Two columns wide and no vertical growth.
       */
    , GROUP(HEADER, GridBagConstraints.WEST, 2, 1, 0.0D, 0.0D, 0)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Formula formula;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** The constructor for a given UI element.
     */
    Element(final Element label, int anchor, int width, int height, double weightx, double weighty, int indent) {
      this.formula = Formula.gbc(label, anchor, width, height, weightx, weighty, indent);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: label (Formula)
    /**
     ** Returns the constraints to use for the label associated with the current
     ** component.
     **
     ** @return                  the constraints to use for the label associated
     **                          with the current component.
     **                          <br>
     **                          Possible object is {@link Formula}.
     */
    @Override
    public final Formula label() {
      return this.formula.label();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: with (Formula)
    /**
     ** Apply the constraints for the current component.
     **
     ** @param  gbc              the constraints to be updated.
     **                          <br>
     **                          Allowed object is {@link GridBagConstraints}.
     **
     ** @return                  the updated <code>gbc</code> parameter.
     **                          <br>
     **                          Possible object is {@link GridBagConstraints}.
     */
    @Override
    public final GridBagConstraints with(final GridBagConstraints gbc) {
      return this.formula.with(gbc);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new builder using specific {@link Formula}s.
   **
   ** @param  container          the form container.
   **                            <br>
   **                            Allowed object is {@link Container}.
   ** @param  columns            the number of grid columns.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  constraint         the map of component class to {@link Formula}.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link Class} for the key
   **                            and {@link Formula} as the value.
   */
  private GridBagBuilder(final Container container, final int columns, final Map<Class<?>, Formula> constraint) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.constraint = constraint;
    this.columns    = columns;
    this.scrollable = new HashSet<>(SCROLL);
    this.container  = container;

    // initialize state
    LayoutFactory.gridBagLayout(this.container);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Return the most recently created field label.
   **
   ** @return                    the most recently created field label.
   **                            <br>
   **                            Possible object is {@link JLabel}.
   */
  public JLabel label() {
    return this.l;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   related
  /**
   ** Use the horizontal and vertical {@link LayoutFactory#RELATED_GAP} for the
   ** next component.
   **
   **
   ** @return                    this <code>GridBagBuilder</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>GridBagBuilder</code>.
   */
  public GridBagBuilder related() {
    return insets(LayoutFactory.RELATED_GAP, 0, 0, LayoutFactory.RELATED_GAP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unrelated
  /**
   ** Use the horizontal {@link LayoutFactory#RELATED_GAP} and vertical
   ** {@link LayoutFactory#UNRELATED_GAP} for the next component.
   **
   ** @return                    this <code>GridBagBuilder</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>GridBagBuilder</code>.
   */
  public GridBagBuilder unrelated() {
    return insets(LayoutFactory.UNRELATED_GAP, 0, 0, LayoutFactory.RELATED_GAP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>GridBagBuilder</code> using 2 columns and
   ** the default {@link Formula}s.
   **
   ** @param  container          the form container.
   **                            <br>
   **                            Allowed object is {@link Container}.
   **
   ** @return                    the <code>GridBagBuilder</code> created.
   **                            <br>
   **                            Possible object is <code>GridBagBuilder</code>.
   */
  public static GridBagBuilder build(final Container container) {
    return build(container, 2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>FixedLengthDocument</code> that
   ** limits the input upon the specified <code>limit</code>.
   **
   ** @param  container          the form container.
   **                            <br>
   **                            Allowed object is {@link Container}.
   ** @param  column             the number of grid columns.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>FixedTextField</code> created.
   **                            <br>
   **                            Possible object is <code>FixedTextField</code>.
   */
  public static GridBagBuilder build(final Container container, final int column) {
    return build(container, column, FORMULA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>FixedLengthDocument</code> that
   ** limits the input upon the specified <code>limit</code>.
   **
   ** @param  container          the form container.
   **                            <br>
   **                            Allowed object is {@link Container}.
   ** @param  column             the number of grid columns.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  constraint         the map of component class to {@link Formula}.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link Class} for the key
   **                            and {@link Formula} as the value.
   **
   ** @return                    the <code>FixedTextField</code> created.
   **                            <br>
   **                            Possible object is <code>FixedTextField</code>.
   */
  public static GridBagBuilder build(final Container container, final int column, final Map<Class<?>, Formula> constraint) {
    return new GridBagBuilder(container, column, constraint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insets
  /**
   ** Set the insets for the next component.
   **
   ** @param  top                the inset from the top.
   **                            <br>
   **                            Possible object is <code>int</code>.
   ** @param  left               the inset from the left.
   **                            <br>
   **                            Possible object is <code>int</code>.
   ** @param  bottom             the inset from the bottom.
   **                            <br>
   **                            Possible object is <code>int</code>.
   ** @param  right              the inset from the right.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @return                    this <code>GridBagBuilder</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>GridBagBuilder</code>.
   */
  public GridBagBuilder insets(final int top, final int left, final int bottom, final int right) {
    this.gbc.insets.set(top, left, bottom, right);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollable
  /**
   ** Automatically wrap instances of the class in a {@link JScrollPane}.
   **
   ** @param  clazz              the instances of the class to wrap in a
   **                            {@link JScrollPane}.
   **                            <br>
   **                            Allowed object is {@link Container}.
   **
   ** @return                    the <code>GridBagBuilder</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>GridBagBuilder</code>.
   */
  public GridBagBuilder scrollable(Class<? extends JComponent> clazz) {
    this.scrollable.add(clazz);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   standalone
  /**
   ** Disable automatic wrapping of instances of the class in a
   ** {@link JScrollPane}.
   **
   ** @param  clazz              the instances of the class to wrap in a
   **                            {@link JScrollPane}.
   **                            <br>
   **                            Allowed object is {@link Container}.
   **
   ** @return                    the <code>GridBagBuilder</code> instance for
   **                            method chaining purpose.
   **                            <br>
   **                            Possible object is <code>GridBagBuilder</code>.
   */
  public GridBagBuilder standalone(final Class<? extends JComponent> clazz) {
    this.scrollable.remove(clazz);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   overlay
  /**
   ** Add a component in the same location as the previous component.
   **
   ** @param  <T>                the class of the component.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  field              the component.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the component instance for method chaining
   **                            purpose.
   **                            <br>
   **                            Allowed object is {@link JComponent} for type
   **                            <code>T</code>.
   */
  public <T extends JComponent> T overlay(final T field) {
    this.gbc.gridx = this.x;
    this.gbc.gridy = this.y;
    this.container.add(field, this.gbc);
    next();
    return field;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Append a labeled component to the container.
   **
   ** @param  <T>                the class of the component.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  prompt             the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  field              the component.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the component instance for method chaining
   **                            purpose.
   **                            <br>
   **                            Allowed object is {@link JComponent} for type
   **                            <code>T</code>.
   */
  public <T extends JComponent> T append(final String prompt, final T field) {
    return append(prompt, field, lookup(field.getClass()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Append a labeled component to the container.
   **
   ** @param  <T>                the class of the component.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  prompt             the text for the label.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  component          the component.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  formula            the constraints calculator for the component.
   **                            <br>
   **                            Allowed object is {@link Formula}.
   **
   ** @return                    the component instance for method chaining
   **                            purpose.
   **                            <br>
   **                            Allowed object is {@link JComponent} for type
   **                            <code>T</code>.
   */
  public <T extends JComponent> T append(final String prompt, final T component, final Formula formula) {
    related();
    this.l = LabelFactory.label(prompt, component);
    this.container.add(this.l, formula.label().with(this.gbc));
    related();
    next();
    return append(component, formula);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Append a unlabeled component to the container.
   **
   ** @param  <T>                the class of the component.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  component          the component.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the component instance for method chaining
   **                            purpose.
   **                            <br>
   **                            Allowed object is {@link JComponent} for type
   **                            <code>T</code>.
   */
  public <T extends JComponent> T append(final T component) {
    return append(component, lookup(component.getClass()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Append a unlabeled component to the container.
   **
   ** @param  <T>                the class of the component.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  component          the component.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  formula            the constraints calculator for the component.
   **                            <br>
   **                            Allowed object is {@link Formula}.
   **
   ** @return                    the component instance for method chaining
   **                            purpose.
   **                            <br>
   **                            Allowed object is {@link JComponent} for type
   **                            <code>T</code>.
   */
  public  <T extends JComponent> T append(final T component, final Formula formula) {
    if (this.scrollable.stream().anyMatch(clazz -> clazz.isInstance(component))) {
      this.container.add(new JScrollPane(component), formula.with(this.gbc));
    }
    else {
      this.container.add(component, formula.with(this.gbc));
    }
    next();
    return component;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next
  /**
   ** Move to the next cell.
   ** <br>
   ** If there are columns left in the row then moves to the next column.
   ** Otherwise, moves to the first column of the next row.
   */
  public void next() {
    this.x = this.gbc.gridx;
    this.y = this.gbc.gridy;
    this.gbc.gridx += this.gbc.gridwidth;
    if (this.gbc.gridx >= this.columns) {
      this.gbc.gridx = 0;
      this.gbc.gridy++;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  private Formula lookup(Class<?> c) {
    Formula f = this.constraint.get(c);
    while (f == null && c != null) {
      c = c.getSuperclass();
      f = this.constraint.get(c);
    }
    return f == null ? EMPTY : f;
  }

  private static Map<Class<?>, Formula> defaultFormula() {
   return CollectionUtility.map(
      CollectionUtility.<Class<?>>list(
        JLabel.class
      , JComboBox.class
      , JTextField.class
      , JTextArea.class
      , JCheckBox.class
      , JList.class
      , JTable.class
      , JPanel.class
      , Box.class
      )
    , CollectionUtility.<Formula>list(
        Element.HEADER
      , Element.TEXT
      , Element.TEXT
      , Element.AREA
      , Element.CHECK
      , Element.LIST
      , Element.TABLE
      , Element.GROUP
      , Element.GROUP
      )
    );
  }
  private static Set<Class<?>> defaultScrollable() {
    return CollectionUtility.set(JTable.class, JTextArea.class, JList.class, JTree.class);
  }
}