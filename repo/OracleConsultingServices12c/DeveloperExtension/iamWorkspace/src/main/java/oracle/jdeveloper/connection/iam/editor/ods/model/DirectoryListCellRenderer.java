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

    File        :   DirectoryTableCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryTableCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryTableCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A minimal extension of {@link DefaultListCellRenderer} allowing us to set
 ** some rows to bold font and others to normal font.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryListCellRenderer<R> extends DefaultListCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default hight of an entry in a list view */
  public static final int      DEFAULT_ROW_HEIGHT    = 24;
  public static final int      DEFAULT_ROW_THRESHOLD = 20;

  protected static final Color BACKGROUND_ROW_ODD    = Color.decode("#F8F8F8");
  protected static final Color BACKGROUND_ROW_EVEN   = Color.WHITE;

  // the official serial version ID which says cryptically which version we're
  // compatible with

  @SuppressWarnings("compatibility:7728642238165360721")
  private static final long    serialVersionUID      = -2112734489753895814L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryListCellRenderer</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryListCellRenderer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getListCellRendererComponent (overridden)
  /**
   ** Return a component that has been configured to display the specified
   ** value. That component's <code>paint</code> method is then called to
   ** "render" the cell. If it is necessary to compute the dimensions of a list
   ** because the list cells do not have a fixed size, this method is called to
   ** generate a component on which <code>getPreferredSize</code> can be
   ** invoked.
   **
   ** @param  list               the {@link JList} that is asking the renderer
   **                            to draw; can be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JList}.
   ** @param  value              the value of the cell to be rendered. It is up
   **                            to the specific renderer to interpret and draw
   **                            the value. For example, if <code>value</code>
   **                            is the string "true", it could be rendered as a
   **                            string or it could be rendered as a check box
   **                            that is checked.  <code>null</code> is a valid
   **                            value.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  index              the index of the cell being drawn.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with the selection highlighted; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  focus              if <code>true</code>, render cell
   **                            appropriately. For example, put a special
   **                            border on the cell, if the cell can be edited,
   **                            render in the color used to indicate editing.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @Override
  public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean selected, final boolean focus) {
    super.getListCellRendererComponent(list, value, index, selected, focus);
    // apply row banding style on list rows
    if (!selected) {
      setBackground((index % 2 == 0) ? BACKGROUND_ROW_EVEN  : BACKGROUND_ROW_ODD);
    }
    return this;
  }
}
