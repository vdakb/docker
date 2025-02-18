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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   DefaultCheckBox.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DefaultCheckBox.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

////////////////////////////////////////////////////////////////////////////////
// class DefaultCheckBox
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DefaultCheckBox</code> transform a value that may represent a
 ** boolean to a value used by Oracle Identity Manager CheckBox's.
 ** <p>
 ** We are using strings as returned values for a CheckBox due to the internal
 ** implementation of Oracle Identity Manager performs a Number conversion.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   2.0.0.0
 */
public class DefaultCheckBox extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // we are using strings as returned values for a CheckBox due to the internal
  // implementation of Oracle Identity Manager performs a Number conversion
  private static final String CHECKED   = "1";
  private static final String UNCHECKED = "0";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DefaultCheckBox</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  public DefaultCheckBox(final Logger logger) {
    // ensure inheritance
    super(logger);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (AttributeTransformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   ** <p>
   ** The {@link Map} <code>origin</code> contains all untouched values. The
   ** {@link Map} <code>subject</code> contains all transformed values
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    Object value = origin.get(attributeName);
    String transform = null;
    // if we not got a null value we assume that this has to be interpreted as
    // the CheckBox is unchecked
    if (value == null)
      transform = UNCHECKED;
    // if we not got an empty value we assume that this has also to be
    // interpreted as the CheckBox is unchecked
    else if (value.toString().length() == 0) {
      transform = UNCHECKED;
    }
    else if (value instanceof Boolean)
      transform = ((Boolean)value).booleanValue() ? CHECKED : UNCHECKED;
    // if we not got an Integer value we assume that each value greater than
    // zero has to be interpreted as the CheckBox is checked and unchecked
    // otherwise
    else if (value instanceof Integer)
      transform = ((Integer)value).intValue() > 0 ? CHECKED : UNCHECKED;
    else if (value instanceof String) {
      final String test = value.toString().toLowerCase();
      transform = (SystemConstant.TRUE.equals(test) || SystemConstant.YES.equals(test) || test.charAt(0) == 'y') ? CHECKED : UNCHECKED;
    }
    subject.put(attributeName, transform);
  }
}