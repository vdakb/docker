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

    File        :   SCPPlatformRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SCPPlatformRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.Map;
import java.util.HashMap;

import java.awt.Component;

import javax.swing.JList;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.swing.list.MappedListCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class SCPPlatformRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The model to restrict the choice of supported deployment platforms.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class SCPPlatformRenderer extends MappedListCellRenderer<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6522021808371279872")
  private static final long                 serialVersionUID = 7791381814461797918L;

  private static final Map<String, Integer> SYMBOL           = new HashMap<String, Integer>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    SYMBOL.put(SCPServerPage.LINUX,   ComponentBundle.PLATFORM_LINUX);
    SYMBOL.put(SCPServerPage.SOLARIS, ComponentBundle.PLATFORM_SOLARIS);
    SYMBOL.put(SCPServerPage.HPUX,    ComponentBundle.PLATFORM_HPUX);
    SYMBOL.put(SCPServerPage.AIX,     ComponentBundle.PLATFORM_AIX);
    SYMBOL.put(SCPServerPage.WINDOWS, ComponentBundle.PLATFORM_WINDOWS);
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs <code>SCPPlatformRenderer</code> that use the provided mapping
   ** to display the values in the associated {@link JList}.
   **
   ** @param  model            the {@link Map} this
   **                          {@link MappedListCellRenderer} use to visualize
   **                          the values.
   */
  public SCPPlatformRenderer(final Map<String, String> model) {
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
  public Component getListCellRendererComponent(final JList list, final String value, int index, boolean selected, boolean focused) {
    return renderComponent(ComponentBundle.icon(SYMBOL.get(value)), displayString(value), selected);
  }
}