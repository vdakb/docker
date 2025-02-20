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

    Copyright 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   LDAP LDS Connector

    File        :   UTCEndDateDecoder.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   Provides common methods for transformations at the time of
                    identity reconciliations.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  nkolandj    First release version
*/

package bka.iam.identity.lds.transformations;

import java.util.HashMap;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.utility.UTCDateDecoder;

////////////////////////////////////////////////////////////////////////////////
// class UTCStartDateDecoder
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>UTCStartDateDecoder</code> transform a Data String formatted as a
 ** Coordinated Universal Time (UTC) '<code>'yyyymmddhhmissZ'</code>' to the
 ** internal Oracle Identity Manager Date Format
 ** '<code>yyyy/MM/dd hh:mm:ss z</code>' representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UTCStartDateDecoder extends UTCDateDecoder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UTCStartDateDecoder</code> adpater that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UTCStartDateDecoder() {
    // ensure inheritance
    super(Logger.create(UTCStartDateDecoder.class.getName()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Method for transforming the attributes.
   ** <br>
   ** Incoming identity and entitlements are from the target system.
   **
   ** @param  identity           the {@link HashMap} containing identity data
   **                            details.
   ** @param  entitlement        the {@link HashMap} containing child data
   **                            details.
   ** @param  attribute          the name of reconciliation attribute being
   **                            transformed.
   **
   ** @return                    the new value for reconciliation field being
   **                            operated on.
   */
  @SuppressWarnings("unused")
  public Object transform(final HashMap<String,Object> identity, final HashMap<String, Object> entitlement, final String attribute) {
    final String value = (String)identity.get(attribute);
    return StringUtility.isEmpty(value) ? DateUtility.now() : parseExternal((String)identity.get(attribute));
  }
}