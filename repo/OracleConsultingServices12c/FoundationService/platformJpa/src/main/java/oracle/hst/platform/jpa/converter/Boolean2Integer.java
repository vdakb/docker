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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   Boolean2Integer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Boolean2Integer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.converter;

import javax.persistence.Converter;
import javax.persistence.AttributeConverter;

////////////////////////////////////////////////////////////////////////////////
// final class Boolean2Integer
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A class that converts attribute values representing boolean expressions into
 ** database value representation and vice versa.
 ** <p>
 ** A {@link Converter} must implement the
 ** {@code javax.persistence.AttributeConverter<X, Y>} interface, where
 ** <code>X</code> is the class of the entity representation and <code>Y</code>
 ** the class of the database representation of the attribute.
 ** <br>
 ** Additionally a Converter has to be annotated with the {@link Converter}
 ** annotation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Converter
public final class Boolean2Integer implements AttributeConverter<Boolean, Integer> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Boolean2Integer</code> instance that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Boolean2Integer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertToDatabaseColumn (AttributeConverter)
  /**
   ** Converts the value stored in the entity attribute into the data
   ** representation to be stored in the database.
   **
   ** @param  value              the entity attribute value to be converted.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the converted data to be stored in the database
   **                            column.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  @Override
  public final Integer convertToDatabaseColumn(final Boolean value) {
    return value ? 1 : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertToEntityAttribute (AttributeConverter)
  /**
   ** Converts the data stored in the database column into the value to be
   ** stored in the entity attribute.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** It is the responsibility of the converter writer to specify the correct
   ** <code>value</code> type for the corresponding column for use by the JDBC
   ** driver: i.e., persistence providers are not expected to do such type
   ** conversion.
   **
   ** @param  value              the data from the database column to be
   **                            converted.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the converted value to be stored in the entity
   **                            attribute
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  @Override
  public Boolean convertToEntityAttribute(final Integer value) {
    return value.equals(1);
  }
}