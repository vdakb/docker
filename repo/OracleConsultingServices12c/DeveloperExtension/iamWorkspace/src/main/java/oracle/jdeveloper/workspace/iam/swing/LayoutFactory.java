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

    File        :   LayoutFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LayoutFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.awt.Insets;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

////////////////////////////////////////////////////////////////////////////////
// class LayoutFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This class is a factory of all built-in layouts.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class LayoutFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int       RELATED_GAP      = 5;
  public static final int       UNRELATED_GAP    = 10;

  public final static Border    EMPTY_BORDER     = BorderFactory.createEmptyBorder();

  public final static Border    PANEL_BORDER     = BorderFactory.createEmptyBorder(3, 3, 3, 3);

  public final static Border    WINDOW_BORDER    = BorderFactory.createEmptyBorder(4, 10, 10, 10);

  /**
   ** an empty component that's laid out in the direction of a line of text as
   ** determined by the <code>ComponentOrientation</code> property.
   */
  public final static Container EMPTY_CONTAINER  = new Box(BoxLayout.LINE_AXIS);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LayoutFactory</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new LayoutFactory()" and enforces use of the public factory method
   ** below.
   */
  private LayoutFactory() {
    // ensure inhertitance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBorderLayout
  /**
   ** Creates a new border layout with a gap of 8 dialog units between
   ** components in horizanl and vertical direction.
   **
   ** @return                    the border layout layout.
   **                            <br>
   **                            Possible object is {@link BorderLayout}.
   */
  public static BorderLayout createBorderLayout() {
    return new BorderLayout(8, 8);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   borderLayout
  /**
   ** Sets a {@link BorderLayout} as the layout manager for the given
   ** {@link Container}.
   ** <br>
   ** The horizontal and the vertical gap of the {@link BorderLayout} is set to
   ** <code>3</code>.
   **
   ** @param  container          the {@link Container} to decorate.
   **                            <br>
   **                            Allowed object is {@link Container}.
   */
  public static void borderLayout(final Container container) {
    container.setLayout(new BorderLayout(3, 3));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gridBagLayout
  /**
   ** Sets a {@link GridBagLayout} as the layout manager for the given
   ** {@link Container}.
   **
   ** @param  container          the {@link Container} to decorate.
   **                            <br>
   **                            Allowed object is {@link Container}.
   */
  public static void gridBagLayout(final Container container) {
    container.setLayout(new GridBagLayout());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultConstraints
  /**
   ** Creates a new {@link GridBagConstraints}.
   **
   ** @return                    the default constarints applicable.
   **                            <br>
   **                            Possible object is {@link GridBagConstraints}.
   */
  public static GridBagConstraints defaultConstraints() {
    return new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relatedConstraints
  /**
   ** Creates a new {@link GridBagConstraints}.
   **
   ** @return                    the default constarints applicable.
   **                            <br>
   **                            Possible object is {@link GridBagConstraints}.
   */
  public static GridBagConstraints relatedConstraints() {
    return new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(RELATED_GAP, 0, RELATED_GAP, 0), 0, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   x2
  /**
   ** Set constraints, useful when used as Java 8 method reference.
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void x2(final GridBagConstraints gbc) {
    xn(gbc, 2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   x3
  /**
   ** Set constraints, useful when used as Java 8 method reference
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  static void x3(final GridBagConstraints gbc) {
    xn(gbc, 3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   x4
  /**
   ** Set constraints, useful when used as Java 8 method reference.
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void x4(final GridBagConstraints gbc) {
    xn(gbc, 4);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   xn
  /**
   ** Set constraints, useful when used as Java 8 method reference.
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   ** @param  col                the numbers of columns to span.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static void xn(final GridBagConstraints gbc, final int col) {
    gbc.gridwidth = col;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   y2
  /**
   ** Set constraints, useful when used as Java 8 method reference.
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  static void y2(final GridBagConstraints gbc) {
    yn(gbc, 2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   y3
  /**
   ** Set constraints, useful when used as Java 8 method reference.
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void y3(final GridBagConstraints gbc) {
    yn(gbc, 3);
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   y4
  /**
   ** Set constraints, useful when used as Java 8 method reference.
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void y4(final GridBagConstraints gbc) {
    yn(gbc, 4);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   yn
  /**
   ** Set constraints, useful when used as Java 8 method reference.
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   ** @param  row                the numbers of rows to span.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static void yn(final GridBagConstraints gbc, final int row) {
    gbc.gridheight = row;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fill
  /**
   ** Set constraints, useful when used as Java 8 method reference
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void fill(final GridBagConstraints gbc) {
    gbc.weightx = 1.0D;
    gbc.weighty = 1.0D;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fillX
  /**
   ** Set constraints, useful when used as Java 8 method reference
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void fillX(final GridBagConstraints gbc) {
    gbc.weightx = 1.0D;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fillY
  /**
   ** Set constraints, useful when used as Java 8 method reference
   **
   ** @param  gbc                the constraints to be updated.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void fillY(final GridBagConstraints gbc) {
    gbc.weighty = 1.0D;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addVerticalGlueTo
  /**
   ** Adds a vertical glue component to the specified container
   **
   ** @param  container          the {@link Container} the vertical glue
   **                            component will be added to.
   **                            <br>
   **                            Allowed object is {@link Container}.
   ** @param  constraints        the {@link GridBagConstraints} the specified
   **                            component has.
   **                            <br>
   **                            Allowed object is {@link GridBagConstraints}.
   */
  public static void addVerticalGlueTo(final Container container, final GridBagConstraints constraints) {
    constraints.gridx      = 0;
    constraints.gridy      = -1;
    constraints.gridheight = 0;
    constraints.gridwidth  = 0;
    constraints.weighty    = 1.0D;
    container.add(Box.createVerticalGlue(), constraints);
  }

  public static Container emptyContainer() {
    return EMPTY_CONTAINER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBorder
  /**
   ** Adds a border around the given panel.
   **
   ** @param  panel              the {@link JPanel} to decorate.
   **                            <br>
   **                            Allowed object is {@link JPanel}.
   */
  public static void setBorder(final JPanel panel) {
    panel.setBorder(PANEL_BORDER);
  }

  public static Border addMargin(final Border border) {
    return new CompoundBorder(border, PANEL_BORDER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableList
  /**
   ** Factory method to create a {@link JScrollPane} for the specified
   ** <code>view</code> with the preferred <code>dimension</code>.
   **
   ** @param  view               the view component to emded in the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link JList}.
   ** @param  dimension          the preferred dimension of the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link Dimension}.
   **
   ** @return                    the created {@link JScrollPane}.
   **                            <br>
   **                            Possible object is {@link JScrollPane}.
   */
  public static JScrollPane scrollableList(final JList view, final Dimension dimension) {
    final JScrollPane pane = scrollableList(view);
    pane.setPreferredSize(dimension);
    return pane;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableList
  /**
   ** Factory method to create a {@link JScrollPane} for the specified
   ** <code>view</code>.
   **
   ** @param  view               the view component to emded in the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link JList}.
   **
   ** @return                    the created {@link JScrollPane}.
   **                            <br>
   **                            Possible object is {@link JScrollPane}.
   */
  public static JScrollPane scrollableList(final JList view) {
    return new JScrollPane(view);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableTree
  /**
   ** Factory method to create a {@link JScrollPane} for the specified
   ** <code>view</code> with the preferred <code>dimension</code>.
   **
   ** @param  view               the view component to emded in the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link JTree}.
   ** @param  dimension          the preferred dimension of the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link Dimension}.
   **
   ** @return                    the created {@link JScrollPane}.
   **                            <br>
   **                            Possible object is {@link JScrollPane}.
   */
  public static JScrollPane scrollableTree(final JTree view, final Dimension dimension) {
    final JScrollPane pane = scrollableTree(view);
    pane.setPreferredSize(dimension);
    return pane;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableTree
  /**
   ** Factory method to create a {@link JScrollPane} for the specified
   ** <code>view</code>.
   **
   ** @param  view               the view component to emded in the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link JTree}.
   **
   ** @return                    the created {@link JScrollPane}.
   **                            <br>
   **                            Possible object is {@link JScrollPane}.
   */
  public static JScrollPane scrollableTree(final JTree view) {
    return new JScrollPane(view);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableTable
  /**
   ** Factory method to create a {@link JScrollPane} for the specified
   ** <code>view</code> with the preferred <code>dimension</code>.
   **
   ** @param  view               the view component to emded in the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link JTable}.
   ** @param  dimension          the preferred dimension of the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link Dimension}.
   **
   ** @return                    the created {@link JScrollPane}.
   **                            <br>
   **                            Possible object is {@link JScrollPane}.
   */
  public static JScrollPane scrollableTable(final JTable view, final Dimension dimension) {
    final JScrollPane pane = scrollableTable(view);
    pane.setPreferredSize(dimension);
    return pane;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scrollableTable
  /**
   ** Factory method to create a {@link JScrollPane} for the specified
   ** <code>view</code>.
   **
   ** @param  view               the view component to emded in the
   **                            {@link JScrollPane} to create.
   **                            <br>
   **                            Allowed object is {@link JTable}.
   **
   ** @return                    the created {@link JScrollPane}.
   **                            <br>
   **                            Possible object is {@link JScrollPane}.
   */
  public static JScrollPane scrollableTable(final JTable view) {
    return new JScrollPane(view);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   standaloneTable
  /**
   ** Factory method to create a {@link JPanel} for the specified
   ** <code>view</code>.
   **
   ** @param  view               the view component to emded in the
   **                            {@link JPanel} to create.
   **                            <br>
   **                            Allowed object is {@link JTable}.
   **
   ** @return                    the created {@link JPanel}.
   **                            <br>
   **                            Possible object is {@link JPanel}.
   */
  public static JPanel standaloneTable(final JTable view) {
    return standaloneTable(view, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   standaloneTable
  public static JPanel standaloneTable(final JTable view, final boolean showHeader) {
    // without a scrollpane, a table doesn't have its header
    // let's make sure we add it.
    final JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    if (showHeader)
      p.add(view.getTableHeader(), BorderLayout.NORTH);
    p.add(view, BorderLayout.CENTER);
    return p;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   standaloneTable
  public static JPanel standaloneTable(final JTable view, final Dimension dimension, final boolean showHeader) {
    // without a scrollpane, a table doesn't have its header
    // let's make sure we add it.
    final JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.setPreferredSize(dimension);
    if (showHeader)
      p.add(view.getTableHeader(), BorderLayout.NORTH);
    p.add(view, BorderLayout.CENTER);
    return p;
  }
}