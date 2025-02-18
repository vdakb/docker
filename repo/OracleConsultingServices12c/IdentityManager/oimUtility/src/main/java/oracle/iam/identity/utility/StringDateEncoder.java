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

    File        :   StringDateEncoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Rashi.Rastogi@oracle.com

    Purpose     :   This file implements the class
                    StringDateEncoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-04-26  RRastogi    First release version
*/

package oracle.iam.identity.utility;

import java.util.Date;
import java.util.Map;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.utility.resource.TransformationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class StringDateEncoder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>StringDateEncoder</code> transforms string provided by a source
 ** from a specific format passed to the constructor to the date string format
 ** used internaly by Oracle Identity Manager.
 ** <p>
 ** The intenal date format is defined by the System Configuration property
 ** <code>XL.DefaultDateFormat</code>. The value of the property is per default
 ** <code>'yyyy/MM/dd hh:mm:ss z'</code>.
 ** <p>
 ** This transformer pick up this property to convert the value coming from the
 ** source to the string representation specified by the property mentioned
 ** above.
 **
 ** @author  Rashi.Rastogi@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public abstract class StringDateEncoder extends AbstractDateTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StringDateEncoder</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   */
  protected StringDateEncoder(final Logger logger) {
    // ensure inheritance
    this(logger, DateUtility.RFC4517_DATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StringDateEncoder</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   */
  protected StringDateEncoder(final Logger logger, final String targetFormat) {
    // ensure inheritance
    super(logger, targetFormat);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StringDateEncoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  defaultDate        the default date that will be set if no
   **                            transformation is applicable in the
   **                            transformation method.
   */
  protected StringDateEncoder(final Logger logger, final Date defaultDate) {
    // ensure inheritance
    this(logger, DateUtility.RFC4517_DATE, defaultDate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StringDateEncoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   ** @param  defaultDate        the default date that will be set if no
   **                            transformation is applicable in the
   **                            transformation method.
   */
  protected StringDateEncoder(final Logger logger, final String targetFormat, final Date defaultDate) {
    // ensure inheritance
    super(logger, targetFormat, defaultDate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StringDateDecoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  year               the value used to set the YEAR calendar field.
   ** @param  month              the value used to set the MONTH calendar field.
   **                            Month value is 0-based. e.g., 0 for January.
   ** @param  day                the value used to set the DAY_OF_MONTH calendar
   **                            field.
   ** @param  hourOfDay          the value used to set the HOUR_OF_DAY calendar
   **                            field
   ** @param  minute             the value used to set the MINUTE calendar field.
   ** @param  second             the value used to set the SECOND calendar field.
   */
  protected StringDateEncoder(final Logger logger, final int year, int month, int day, int hourOfDay, int minute, int second) {
    // ensure inheritance
    this(logger, DateUtility.RFC4517_DATE, year, month, day, hourOfDay, minute, second);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StringDateDecoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   ** @param  year               the value used to set the YEAR calendar field.
   ** @param  month              the value used to set the MONTH calendar field.
   **                            Month value is 0-based. e.g., 0 for January.
   ** @param  day                the value used to set the DAY_OF_MONTH calendar
   **                            field.
   ** @param  hourOfDay          the value used to set the HOUR_OF_DAY calendar
   **                            field
   ** @param  minute             the value used to set the MINUTE calendar field.
   ** @param  second             the value used to set the SECOND calendar field.
   */
  protected StringDateEncoder(final Logger logger, final String targetFormat, final int year, int month, int day, int hourOfDay, int minute, int second) {
    // ensure inheritance
    super(logger, targetFormat, year, month, day, hourOfDay, minute, second);
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
  @Override
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    final String method = "transform";

    final Object value = origin.get(attributeName);
    // if we not got a null value put it without transformation in the returning
    // container
    String transform = null;
    // if we don't have a string skip any transformation
    if (value instanceof String) {
      transform = value.toString();
      if (!StringUtility.isEmpty(transform)) {
        transform = formatExternal(parseInternal(transform));
        if (StringUtility.isEmpty(transform))
          warning(method, TransformationBundle.format(TransformationError.NOT_APPLICABLE, attributeName, value.toString()));
      }
    }
    else
      warning(method, TransformationBundle.format(TransformationError.NOT_STRING, (value == null ? "<null>" : value.getClass().getName())));

    // the value mapped her may be null regarding if we are able to convert or
    // in case the value is not a proper instance to transform
    subject.put(attributeName, transform);
  }
}