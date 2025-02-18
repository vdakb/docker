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

    File        :   Alignable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Alignable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

////////////////////////////////////////////////////////////////////////////////
// interface Alignable
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** <code>Alignable</code> is an interface that can be implemented by any
 ** components to provide information such as how to set orientation and check
 ** whether a component supports vertical orientation or horizontal orientation.
 ** <p>
 ** Some components support both vertical and horizontal orientation. For
 ** example, an icon-only JButton. It can be put on either a vertical toolbar or
 ** normal horizontal toolbar. However most components don't support both. For
 ** example, a combo box. It's hard to imagine a combo box putting on a vertical
 ** toolbar.
 ** <p>
 ** By implementing this interface, a component can choose if it wants to
 ** support vertical orientation or horizontal orientation. However if a
 ** component which doesn't implement this interface is added to toolbar, by
 ** default, it will be treated as supportHorizontalOrientation() returning
 ** <code>true</code> and supportVerticalOrientation() returning
 ** <code>false</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface Alignable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Client property.
   ** <p>
   ** Property name to indicate the orientation is changed.
   */
  static final String CLIENT_PROPERTY = "orientation";

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supportHorizontalOrientation
  /**
   ** Whether the component support horizontal orientation. or not.
   ** <p>
   ** Doesn't consider the component orientation, it should return
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if it supports horizontal
   **                            orientation.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean supportHorizontalOrientation();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supportVerticalOrientation
  /**
   ** Whether the component support vertical orientation or not.
   ** <p>
   ** Doesn't consider the component orientation, it should return
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if it supports vertical
   **                            orientation.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean supportVerticalOrientation();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orientation
  /**
   ** Changes the orientation.
   ** <p>
   ** If the component is a Swing component, the default implementation is this.
   ** <br>
   ** <pre>
   **   Utilities.orientationOf(this, orientation);
   ** </pre>
   **
   ** @param  orientation        the new orientation.
   **                            The value has to be either
   **                            <code>SwingConstants.HORIZONTAL</code> or
   **                            <code>SwingConstants#VERTICAL</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void orientation(int orientation);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orientation
  /**
   ** Returns the orientation.
   ** <p>
   ** If the component is a Swing component, the default implementation is this.
   ** <br>
   ** <pre>
   **   Utilities.orientationOf(this);
   ** </pre>
   **
   ** @return                    either <code>SwingConstants.HORIZONTAL</code>
   **                            or <code>SwingConstants#VERTICAL</code>
   **                            regarding the current orientation of the
   **                            component.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int orientation();
}
