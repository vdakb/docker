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

    File        :   FixedTextField.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FixedTextField.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.JTextField;

////////////////////////////////////////////////////////////////////////////////
// class FixedTextField
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A plain document that maintains no character attributes.
 ** <br>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class FixedTextField extends JTextField {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2122338810311354242")
  private static final long serialVersionUID = -799614137105433248L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new input text area.
   ** <p>
   ** A default model is set, the initial string is <code>null</code>, and
   ** columns are set to <code>25</code>.
   **
   ** @param  column             the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  private FixedTextField(final int column) {
    // ensure inheritance
    super(column);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>FixedTextField</code> that
   ** limits the input upon <code>255</code>.
   ** <br>
   ** A default model is set, the initial string is <code>null</code>, and
   ** columns are set to <code>25</code>.
   **
   ** @return                    the <code>FixedLengthDocument</code> created.
   **                            <br>
   **                            Possible object is
   **                            <code>FixedLengthDocument</code>.
   */
  public static FixedTextField build() {
    return build(25);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>FixedLengthDocument</code> that
   ** limits the input upon the specified <code>limit</code>.
   **
   ** @param  column             the number of columns to use to calculate the
   **                            preferred width; if columns is set to zero, the
   **                            preferred width will be whatever naturally
   **                            results from the component implementation.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>FixedTextField</code> created.
   **                            <br>
   **                            Possible object is <code>FixedTextField</code>.
   */
  public static FixedTextField build(final int column) {
    return new FixedTextField(column);
  }
}