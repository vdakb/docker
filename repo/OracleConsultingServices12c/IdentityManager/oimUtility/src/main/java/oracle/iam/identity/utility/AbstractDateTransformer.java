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

    File        :   AbstractDateTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDateTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Date;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractDateTransformer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractDateTransformer</code> is the base class to transform a
 ** Data String to the internal Oracle Identity Manager Date Format
 ** '<code>yyyy/MM/dd hh:mm:ss z</code>' representation and vice versa.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractDateTransformer extends SimpleDateTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractDateTransformer</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   */
  protected AbstractDateTransformer(final Logger logger, final String targetFormat) {
    // ensure inheritance
    this(logger, targetFormat, DateUtility.now());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractDateTransformer</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   ** @param  targetFormat       the date format that will be used parse or
   **                            write the date value.
   ** @param  defaultDate        the default date that will be set if no
   **                            transformation is applicable in the
   **                            transformation method.
   */
  protected AbstractDateTransformer(final Logger logger, final String targetFormat, final Date defaultDate) {
    // ensure inheritance
    super(logger, targetFormat);

    // initalize instance
    this.calendar.setTime(defaultDate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractDateTransformer</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used parse or
   **                            write the date value.
   ** @param  year               the value used to set the YEAR calendar field.
   ** @param  month              the value used to set the MONTH calendar field.
   **                            Month value is 0-based. e.g., 0 for January.
   ** @param  date               the value used to set the DAY_OF_MONTH calendar
   **                            field.
   ** @param  hourOfDay          the value used to set the HOUR_OF_DAY calendar
   **                            field
   ** @param  minute             the value used to set the MINUTE calendar field.
   ** @param  second             the value used to set the SECOND calendar field.
   */
  public AbstractDateTransformer(final Logger logger, final String targetFormat, final int year, int month, int date, int hourOfDay, int minute, int second) {
    // ensure inheritance
    super(logger, targetFormat);

    // initalize instance
    this.calendar.set(year, month, date, hourOfDay, minute, second);
  }
}