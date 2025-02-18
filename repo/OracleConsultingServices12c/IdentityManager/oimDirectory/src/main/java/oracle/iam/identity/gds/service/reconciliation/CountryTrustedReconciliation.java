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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   CountryTrustedReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CountryTrustedReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.reconciliation;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;

////////////////////////////////////////////////////////////////////////////////
// class CountryTrustedReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>CountryTrustedReconciliation</code> acts as the service end point
 ** for the Oracle Identity Manager to reconcile organizational information from
 ** a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CountryTrustedReconciliation extends HierarchyTrustedReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CountryTrustedReconciliation</code> scheduled
   ** task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CountryTrustedReconciliation() {
    // ensure inheritance
    super(DirectoryConstant.COUNTRY_MULTIVALUE_ATTRIBUTE);
  }
}