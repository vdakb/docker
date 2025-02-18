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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   EntrySuffixSelector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EntrySuffixSelector.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.entry;

import javax.swing.JTextField;

////////////////////////////////////////////////////////////////////////////////
// class EntrySuffixSelector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** An editor component suitable to select a base distinguished name as the
 ** suffix of an entry.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class EntrySuffixSelector extends JTextField {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4830656278807887815")
  private static final long serialVersionUID = 7746117459575041111L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new empty <code>EntrySuffixSelector</code> with the
   ** specified number of columns.
   ** <br>
   ** A default model is created and the initial string is set to
   ** <code>null</code>.
   **
   ** @param  columns            the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **                            <br>
   **                            Allowed object is <code>int</code>
   */
  public EntrySuffixSelector(final int columns) {
    // enure inheritance
    super(columns);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>EntrySuffixSelector</code> that populates
   ** its data to visualize from the specified collection.
   **
   ** @param  columns            the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **                            <br>
   **                            Allowed object is <code>int</code>
   **
   ** @return                    the <code>EntrySuffixSelector</code>.
   **                            <br>
   **                            Possible object
   **                            <code>EntrySuffixSelector</code>.
   */
  public static EntrySuffixSelector build(final int columns) {
    return new EntrySuffixSelector(columns);
  }
}