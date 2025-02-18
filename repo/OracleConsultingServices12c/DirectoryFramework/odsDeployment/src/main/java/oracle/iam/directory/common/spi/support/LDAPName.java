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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   LDAPName.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPName.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import javax.naming.InvalidNameException;

import javax.naming.ldap.LdapName;

////////////////////////////////////////////////////////////////////////////////
// class LDAPName
// ~~~~~ ~~~~~~~~
/**
 ** Represents a distinguished name in LDAP.
 ** <p>
 ** An objects of this class can be used to split a distinguished name (DN) into
 ** its individual components. You can also escape the characters in a DN.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class LDAPName extends LdapName {

  /////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:630565720034875177")
  private static final long serialVersionUID = -4936249828368543338L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDAPName</code> object from the specified
   ** distinguished name.
   ** <p>
   **
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  dn                 string representation of the distinguished name
   **
   ** @throws InvalidNameException if a syntax violation is detected.
   */
  public LDAPName(final String dn)
    throws InvalidNameException {

    // ensure inheritance
    super(dn);
  }
}