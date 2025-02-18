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

    File        :   BooleanToStatusActive.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    BooleanToStatusActive.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-04-29  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class BooleanToStatusActive
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>BooleanToStatusActive</code> transform a boolean value coming from
 ** a source to the Oracle Identity Manager Status <code>Active</code> if the
 ** value is <code>true</code>.
 ** <p>
 ** This {@link AbstractStatusTransformer} is usefull if a source used by
 ** trusted reconciliation for identities or organizations provides a boolean
 ** status information that has to be reflected accordingly in Oracle Identity
 ** Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public class BooleanToStatusActive extends AbstractStatusTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>BooleanToStatusActive</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   */
  public BooleanToStatusActive(final Logger logger) {
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
   ** transformation in <code>subject</code>.
   ** <p>
   ** The {@link Map} <code>origin</code> contains all untouched values and has
   ** it to be after this method completes. The {@link Map} <code>subject</code>
   ** contains all transformed values hence the transformation done here too.
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    Object value = origin.get(attributeName);
    // if we not got a null value put it without transformation in the returning
    // container
    if (value == null)
      value = STATUS_ACTIVE;
    else if (value instanceof Boolean)
      value = ((Boolean)value).booleanValue() ? STATUS_ACTIVE : STATUS_DISABLED;
    else if (value instanceof String) {
      // normalize the string to make the analysis easier what it can be
      final String expression = ((String)value).toLowerCase();
      // not all sources providing it like the Java primitive boolean hence we
      // convert what we know to a Java boolean primitive
      if (expression.length() == 0)
        value = STATUS_ACTIVE;
      else if (SystemConstant.YES.charAt(0) == expression.charAt(0))
        value = STATUS_ACTIVE;
      else if (SystemConstant.NO.charAt(0) == expression.charAt(0))
        value = STATUS_DISABLED;
      else
        value = Boolean.parseBoolean(expression) ? STATUS_ACTIVE : STATUS_DISABLED;
    }
    else
      value = STATUS_ACTIVE;

    // if we not got an empty String return the Oracle Identity Manager default
    // status; otherwise return the converted value as is
    subject.put(attributeName, value);
  }
}