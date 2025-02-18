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

    File        :   ToIntegerTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ToIntegerTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-04-29  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.utility.resource.TransformationBundle;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

////////////////////////////////////////////////////////////////////////////////
// class ToIntegerTransformer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ToIntegerTransformer</code> transform a string value to an
 ** {@link Integer} value is <code>1</code>. If the string value isn't
 ** convertable to <code>Integer</code> the value is transformed to
 ** <code>null</code>.
 ** <br>
 ** If no value is provided to the transformer either by not existing or set to
 ** <code>null</code> the returned values in the mapping will be
 ** <code>null</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public class ToIntegerTransformer extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ToIntegerTransformer</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  public ToIntegerTransformer(final Logger logger) {
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
   ** <br>
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
    final String method = "transform";
    if (origin.containsKey(attributeName)) {
      Object value = origin.get(attributeName);
      // if we not got a null value put it without transformation in the returning
      // container
      if (value == null)
        subject.put(attributeName, value);
      else {
        try {
          subject.put(attributeName, Integer.valueOf(value.toString()));
        }
        catch (NumberFormatException e) {
          error(method, TransformationBundle.format(TransformationError.NOT_APPLICABLE, attributeName, value.toString()));
          subject.put(attributeName, null);
        }
      }
    }
  }
}