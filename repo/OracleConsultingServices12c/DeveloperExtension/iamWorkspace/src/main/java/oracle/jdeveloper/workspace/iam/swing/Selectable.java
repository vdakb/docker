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

    File        :   Selectable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Selectable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

////////////////////////////////////////////////////////////////////////////////
// interface Selectable
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** Selectable is an interface indicating something is selectable.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface Selectable {

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected
  /**
   ** Sets it as selected.
   **
   ** @param selected            <code>true</code> if it is selected; otherwise
   **                            <code>false</code>.
   */
  void selected(final boolean selected);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selected
  /**
   ** Returns the selected status.
   **
   ** @return                    <code>true</code> if it is selected; otherwise
   **                            <code>false</code>.
   */
  boolean selected();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toggle
  /**
   ** Toggles the selection status.
   */
  void toggle();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
  /**
   ** Enable selection change.
   ** <p>
   ** Enabled <code>false</code> doesn't mean selected is <code>false</code>. If
   ** it is selected before, enableed(false) won't make selected become
   ** <code>false</code>. In the other word, enabled won't change the value
   ** of Selected().
   **
   ** @param  enabled            <code>true</code> if it is enabled; otherwise
   **                            <code>false</code>.
   */
  void enabled(final boolean enabled);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enabled
  /**
   ** Determines if selection change is allowed.
   **
   ** @return                    <code>true</code> selection change is allowed;
   **                            otherwise <code>false</code>.
   */
  boolean enabled();
}