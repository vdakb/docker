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

    File        :   StartDateDecoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    StartDateDecoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import oracle.hst.foundation.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class StartDateDecoder
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>StartDateEncoder</code> transform a Date String formatted as an
 ** Date only <code>'yyyymmdd'</code> to the internal Oracle Identity Manager
 ** Date Format '<code>yyyy/MM/dd hh:mm:ss z</code>' representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class StartDateDecoder extends StringDateDecoder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StartDateDecoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  public StartDateDecoder(final Logger logger) {
    // ensure inheritance
    super(logger, DEFAULT_YEAR_START, DEFAULT_MONTH_START, DEFAULT_DAY_START, DEFAULT_HOUROFDAY_START, DEFAULT_MINUTE, DEFAULT_SECOND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>StartDateDecoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   */
  public StartDateDecoder(final Logger logger, final String targetFormat) {
    // ensure inheritance
    super(logger, targetFormat, DEFAULT_YEAR_START, DEFAULT_MONTH_START, DEFAULT_DAY_START, DEFAULT_HOUROFDAY_START, DEFAULT_MINUTE, DEFAULT_SECOND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (overridden)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   ** <p>
   ** The timestamp has to be in the format used internaly by Oracle Identity
   ** Manager.
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
    // ensure inheritance
    super.transform(attributeName, origin, subject);

    // check if a value is assingned to the attribute name
    if (subject.get(attributeName) == null)
      subject.put(attributeName, formatInternal(this.defaultDate()));
  }
}