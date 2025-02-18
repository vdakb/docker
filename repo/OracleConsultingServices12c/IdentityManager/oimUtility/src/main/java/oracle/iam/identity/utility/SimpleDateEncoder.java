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

    File        :   SimpleDateEncoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Rashi.Rastogi@oracle.com

    Purpose     :   This file implements the class
                    SimpleDateEncoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-04-26  RRastogi    First release version
*/

package oracle.iam.identity.utility;

import java.util.Date;
import java.util.Map;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.utility.resource.TransformationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class SimpleDateEncoder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>SimpleDateEncoder</code> transform an internal Oracle Identity
 ** Manager Date Format '<code>yyyy/MM/dd hh:mm:ss z</code>' representation to
 ** a Date String representation.
 **
 ** @author  Rashi.Rastogi@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public abstract class SimpleDateEncoder extends SimpleDateTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SimpleDateEncoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used parse or
   **                            write the date value.
   */
  protected SimpleDateEncoder(final Logger logger, final String targetFormat) {
    // ensure inheritance
    super(logger, targetFormat);
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
   ** LDAP Timestamp is in the form of "yyyymmddhhmmssZ". The date and time
   ** specified in LDAP Timestamp is based on the Coordinated Universal Time
   ** (UTC). The date and time "Mon Jul 30 17:42:00 2001" is represented in
   ** LDAP Timestamp as "20010730174200Z".
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

    Date   timestamp = null;
    Object value     = origin.get(attributeName);
    // if we don't have a proper string skip any transformation
    if (value instanceof String) {
      final String transform = value.toString();
      if (!StringUtility.isEmpty(transform)) {
        // parse the date from a string that must match the format passed to the
        // constructor of this instance
        timestamp = parseExternal(transform);
        if (timestamp == null)
          error(method, TransformationBundle.format(TransformationError.NOT_APPLICABLE, attributeName, value.toString()));
      }
    }
    else
      warning(method, TransformationBundle.format(TransformationError.NOT_STRING, (value == null ? "<null>" : value.getClass().getName())));

    // the value mapped her may be null regarding if we are able to convert or
    // in case the value is not a proper instance to transform
    subject.put(attributeName, timestamp);
  }
}