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

    File        :   ISOStartDateDecoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ISOStartDateDecoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import oracle.hst.foundation.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class ISOStartDateDecoder
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ISOStartDateDecoder</code> transform a Date String formatted as an
 ** ISO Date and Time '<code>'yyyy-mm-dd hh:mm:ss'</code>' to the internal
 ** Oracle Identity Manager Date Format '<code>yyyy/MM/dd hh:mm:ss z</code>'
 ** representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ISOStartDateDecoder extends ISODateDecoder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ISOStartDateDecoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  public ISOStartDateDecoder(final Logger logger) {
    // ensure inheritance
    super(logger, DEFAULT_YEAR_START, DEFAULT_MONTH_START, DEFAULT_DAY_START, DEFAULT_HOUROFDAY_START, DEFAULT_MINUTE, DEFAULT_SECOND);
  }
}