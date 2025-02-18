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

    File        :   DBSPlatformRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DBSPlatformRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.Map;

import java.awt.Component;

import javax.swing.JList;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.swing.list.MappedListCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class DBSPlatformRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The model to restrict the choice of supported DBS runtime platforms.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class DBSPlatformRenderer extends MappedListCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5557334260756490605")
  private static final long serialVersionUID = 6226494809810286761L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs <code>DBSPlatformRenderer</code> that use the provided mapping
   ** to display the values in the associated {@link JList}.
   **
   ** @param  model            the {@link Map} this
   **                          {@link MappedListCellRenderer} use to visualize
   **                          the values.
   */
  public DBSPlatformRenderer(final Map<String, String> model) {
    // ensure inheritance
    super(null, model);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: getListCellRendererComponent (overridden)
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
  public Component getListCellRendererComponent(final JList list, final Object value, int index, boolean selected, boolean focused) {
    return renderComponent(ComponentBundle.icon(DBSServerPage.VENDOR.get(value.toString())),displayString(value), selected);
  }
}