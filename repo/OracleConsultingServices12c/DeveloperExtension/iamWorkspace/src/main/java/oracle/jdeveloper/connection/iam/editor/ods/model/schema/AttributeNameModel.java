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

    File        :   AttributeNameModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributeNameModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.schema;

import java.util.Set;

import javax.swing.AbstractListModel;

////////////////////////////////////////////////////////////////////////////////
// class AttributeNameModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A list model suitable for the list of names of Directory Service attribute
 ** types.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributeNameModel extends AbstractListModel<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2255972375452700095")
  private static final long serialVersionUID = 8418078045670901440L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the collection of Directory Service attribute types
  transient String[]        data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AttributeNameModel</code> responsible to populate the
   ** table UI of attribute types list view.
   ** <p>
   ** This constructor is private to prevent other classes to use
   ** "new AttributeNameModel(Set)".
   **
   ** @param  data               the data populated from a Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            elements is of type {@link String}.
   */
  private AttributeNameModel(final Set<String> data) {
    // ensure inheritance
    super();

    // initialize instance attributes
    update(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSize (ListModel)
  /**
   ** Returns the length of the list.
   **
   ** @return                    the length of the list.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int getSize() {
    return this.data.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getElementAt (ListModel)
  /**
   ** Returns the value at the specified index.
   **
   ** @param  index              the requested index.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the value at <code>index</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getElementAt(final int index) {
    return this.data[index];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>AttributeNameModel</code> object from
   ** the specified properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  data               the {@link Set} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            elements is of type {@link String}.
   **
   ** @return                    the validated <code>AttributeNameModel</code>.
   **                            Possible object is
   **                            <code>AttributeNameModel</code>.
   */
  public static AttributeNameModel build(final Set<String> data) {
    // initialize instance attributes
    return new AttributeNameModel(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates an <code>AttributeNameModel</code> object from the specified
   ** properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   **
   ** @param  data               the {@link Set} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            elements is of type {@link String}.c   */
  public void update(final Set<String> data) {
    // initialize instance attributes
    this.data = new String[data.size()];
    data.toArray(this.data);
  }
}