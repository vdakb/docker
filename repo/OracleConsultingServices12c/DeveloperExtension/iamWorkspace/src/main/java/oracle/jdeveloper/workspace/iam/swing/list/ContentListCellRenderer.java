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

    File        :   ContentListCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ContentListCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import java.io.Serializable;

import java.awt.FlowLayout;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import oracle.jdeveloper.workspace.iam.swing.widget.NullPanel;

////////////////////////////////////////////////////////////////////////////////
// class ContentListCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A list cell renderer that displays Swing components at the elements of the
 ** list.
 ** <p>
 ** <code>ContentListCellRenderer</code> is not opaque and unless you subclass
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
public abstract class ContentListCellRenderer extends    NullPanel
                                              implements ListCellRenderer
                                              ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1385372745137388751")
  private static final long serialVersionUID = -6427247202424015331L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ContentListCellRenderer</code> suitable for rendering
   ** Swing components. This class is intended for use with a
   ** <code>JList</code>.
   **
   ** @see    ListCellRenderer
   */
  public ContentListCellRenderer() {
    // ensure inheritance
    super();

    // set a suitable layout
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    this.setOpaque(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accesor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hotspotHit
  /**
   ** Determines whether the specified relative coordinates insist on the
   ** intended hotspot. It is used by the OptionTreeHandler to figure out
   ** whether to toggle a node or not.
   **
   ** @param  x                  the relative coordinates of a mouse action
   **                            along the X axis.
   ** @param  y                  the relative coordinates of a mouse action.
   **                            along the Y axis.
   **
   ** @return                    <code>true</code> is the specified relative
   **                            coordinates are inside the hotspot area,
   **                            <code>false</code> otherwise.
   */
  public abstract boolean hotspotHit(final int x, final int y);

  //////////////////////////////////////////////////////////////////////////////
  // Method:  render
  /**
   ** Renderers the components based on the passed in poperties.
   **
   ** @param  list               the {@link JList} that is asking the renderer
   **                            to draw; can be <code>null</code>.
   ** @param  data               the value of the cell to be rendered. It is up
   **                            to the specific renderer to interpret and draw
   **                            the value. For example, if <code>value</code>
   **                            is the string "true", it could be rendered as a
   **                            string or it could be rendered as a check box
   **                            that is checked. <code>null</code> is a valid
   **                            value.
   ** @param  index              the row index of the cell being drawn. When
   **                            drawing the header, the value of
   **                            <code>index</code> is <code>-1</code>.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with the selection highlighted; otherwise
   **                            <code>false</code>.
   ** @param  focused            if <code>true</code>, render cell
   **                            appropriately. For example, put a special
   **                            border on the cell, if the cell can be edited,
   **                            render in the color used to indicate editing.
   */
  protected abstract void render(final JList list, final OptionListItemData data, final int index, final boolean selected, final boolean focused);
}