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

    File        :   DefaultOrganizationStatus.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DefaultOrganizationStatus.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

import oracle.iam.identity.foundation.naming.Organization;

////////////////////////////////////////////////////////////////////////////////
// class DefaultOrganizationStatus
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DefaultOrganizationStatus</code> transform an Object to the Oracle
 ** Identity Manager default Organization Status <code>Active</code> if the
 ** Object to transform is not a valid String.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DefaultOrganizationStatus extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DefaultOrganizationStatus</code> which use the
   ** specified {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  public DefaultOrganizationStatus(final Logger logger) {
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
     Object value = origin.get(attributeName);
    // if we not got a String return the Oracle Identity Manager status 'Active'
    // for identities
    if (value == null || !(value instanceof String))
      value = Organization.STATUS_ACTIVE;

    // if we not got an empty String return the Oracle Identity Manager default
    // status; otherwise return the converted value as is
    subject.put(attributeName, (StringUtility.isEmpty((String)value)) ? Organization.STATUS_ACTIVE : value);
  }
}