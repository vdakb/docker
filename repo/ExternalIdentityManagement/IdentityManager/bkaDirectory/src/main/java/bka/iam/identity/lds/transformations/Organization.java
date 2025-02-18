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

    File        :   Organization.java

    Compiler    :   JDK 1.8

    Author      :   nicolai.kolandjian@gmail.com

    Purpose     :   Provides common methods for transformations at the time of
                    identity reconciliations.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  nkolandj    First release version
*/

package bka.iam.identity.lds.transformations;

import java.util.HashMap;
import java.util.ArrayList;

import oracle.hst.foundation.logging.Logger;

import bka.iam.identity.lds.ReconciliationPlugin;

////////////////////////////////////////////////////////////////////////////////
// class Organization
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Organization</code> generate the organization name of an
 ** entry feeded from a datasource by obtaining the second <code>RDN</code> as
 ** the name of the home organization.
 **
 ** @author  nicolai.kolandjian@gmail.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Organization extends ReconciliationPlugin {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Organization</code> adpater that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Organization() {
    // ensure inheritance
    super(Logger.create(Organization.class.getName()));
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
  public Object transform(final HashMap<String,Object> identity, final HashMap<String, ArrayList<HashMap<String, String>>> entitlement, final String attribute) {
    String dn = (String)identity.get(DN);

    // by not catching errors here, and allowing to be thrown, we are ensuring
    // we do not gracefully skip by preventing further execution, and therefore
    // allowing admins to properly fix.
    String itResourceKeyString = (String)identity.get(ITRESOURCE_KEY);
    this.logger.trace("executing transformation; {dn: " + dn + "}");
    String organizationalUnit = organizationNameCurrent(dn, Long.parseLong(itResourceKeyString));
    this.logger.debug("Org name evaluated; {organizationalUnit: " + organizationalUnit + ", dn: " + dn + "}");
    return organizationalUnit;
  }
}