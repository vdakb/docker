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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Enterprise User Security

    File        :   RoleLookupReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleLookupReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-03-01  TSebo       First release version
*/

package oracle.iam.identity.eus.service.reconciliation;

import java.util.Map;

import javax.naming.InvalidNameException;

import javax.naming.ldap.LdapName;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.gds.service.reconciliation.LookupReconciliation;

////////////////////////////////////////////////////////////////////////////////
// class RoleLookupReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>RoleLookupReconciliation</code> acts as the service end point
 ** for the Oracle Identity Manager to reconcile Enterprise information from a
 ** Oracle Enterprise Security Realm.
 **
 ** @author  Tomas.Sebo@oracle.com
 ** @version 1.0.0.0
 */
public class RoleLookupReconciliation extends LookupReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RoleLookupReconciliation</code> scheduled task
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleLookupReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (overridden)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do reconciliation of Oracle Identity Manager Entitlements.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(final Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    // extracts the encoded value by checking if the distinguished name is
    // requested or an attribute
    String encoded = (String)subject.get(stringValue(ENCODEDVALUE));
    String decoded = (String)subject.get(stringValue(DECODEDVALUE));
    try {
      if (encoded != null) {
        LdapName dn = new LdapName(encoded);
        // Create decoded value based on the patter: role@domain
        decoded += "@" + dn.getRdn(dn.size() - 2).getValue();
      }
      else {
        error(method, "Encoded value is null");
      }
    }
    catch (InvalidNameException e) {
      error(method, "Cannot parse decoded value as LDAP DN: " + decoded);
    }
    subject.put(stringValue(DECODEDVALUE), decoded);

    super.processSubject(subject);
  }
}