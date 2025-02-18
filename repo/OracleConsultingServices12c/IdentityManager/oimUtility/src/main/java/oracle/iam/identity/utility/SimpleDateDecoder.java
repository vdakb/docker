/*
    Oracle Consulting Services

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   SimpleDateDecoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Rashi.Rastogi@oracle.com

    Purpose     :   This file implements the class
                    SimpleDateDecoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-04-26  RRastogi    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;
import java.util.Date;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.utility.resource.TransformationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class SimpleDateDecoder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>SimpleDateDecoder</code> transform a Date String given in a
 ** specific format to the internal Oracle Identity Manager Date Format
 ** '<code>yyyy/MM/dd hh:mm:ss z</code>' representation.
 **
 ** @author  Rashi.Rastogi@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public abstract class SimpleDateDecoder extends SimpleDateTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SimpleDateDecoder</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   ** @param  targetFormat       the default date format that will be used parse
   **                            the date value.
   */
  protected SimpleDateDecoder(final Logger logger, final String targetFormat) {
    // ensure inheritance
    super(logger, targetFormat);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (AttributeTransformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate date
   ** transformation in <code>subject</code>.
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
    final String method = "transform";

    String transform = null;
    Object value     = origin.get(attributeName);
    // if we don't have a proper string skip any transformation
    if (value instanceof Date) {
      // format the date value to a string by using the format specified by
      // the constructor
      transform = formatExternal((Date)value);
      if (transform == null)
        error(method, TransformationBundle.format(TransformationError.NOT_APPLICABLE, attributeName, value.toString()));
    }
    else
      warning(method, TransformationBundle.format(TransformationError.NOT_DATE, (value == null ? "<null>" : value.getClass().getName())));

    // the value mapped her may be null regarding if we are able to convert or
    // in case the value is not a proper instance to transform
    subject.put(attributeName, transform);
  }
}