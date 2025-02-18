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

    File        :   ReconciliationPlugin.java

    Compiler    :   JDK 1.8

    Author      :   nicolai.kolandjian@gmail.com

    Purpose     :   Provides common methods for transformations at the time of
                    identity reconciliations.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  rirutter    First release version
*/

package bka.iam.identity.lds;

import javax.naming.InvalidNameException;

import javax.naming.ldap.LdapName;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

public class ReconciliationPlugin {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Default name of a distinguished name */
  protected static final String DN                  = "Distinguished Name";

  protected static final String ITRESOURCE_KEY      = "IT Resource Key";
  protected static final String PARTICIPANT_PREFIX  = "Participant Prefix";

  protected static final String ORGANIZATION_NAME   = "Org Name";
  protected static final String ORGANIZATION_PARENT = "Organization";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ReconciliationPlugin</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  protected ReconciliationPlugin(final Logger logger) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.logger = logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationNameParent
  /**
   **
   ** @param  value              the fullqualified Distinguished Name e.g.
   **                            "cn=abc,ou=def,dc=123".
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **
   **
   ** @return                    the transformation of the specified
   **                            <code>value</code>.
   */
  protected String organizationNameParent(final String value, final long resourceInstance) {
    try {
      final LdapName dn   = new LdapName(value);
      final String[] data = participantRealm(resourceInstance);
      // If the confirmed realm value happens to be our direct parent...
      if (dn.getPrefix(dn.size() - 1).toString().equalsIgnoreCase(data[0])) {
        return data[1];
      }
      return organizationNameParent(dn, data);
    }
    catch (Exception e) {
      // transformations will not capture thrown exceptions, so need to debug
      // visibily here
      this.logger.error("Unable to getParentOrgNameOIMFromDn", e);
    }
    // At least null will prevent CRUD operations from getting through
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationNameCurrent
  /**
   **
   ** @param  value              the fullqualified Distinguished Name e.g.
   **                            "cn=abc,ou=def,dc=123".
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **
   ** @return                    a string array with only 2 entries:
   **                            0: REALM, 1: OIM PREFIX.
   */
  protected String organizationNameCurrent(final String value, final long resourceInstance) {
    try {
      return organizationNameCurrent(value, participantRealm(resourceInstance));
    }
    catch (Exception e) {
      // transformations will not capture thrown exceptions, so need to debug
      // visibily here
      this.logger.error("Unable to currentOrgNameFromDn", e);
    }
    // At least null will prevent CRUD operations from getting through
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   participantRealm
  /**
   ** @param  resourceInstance   the system identifier of the Identity Manager
   **                            <code>IT Resource</code> where this wrapper
   **                            belongs to.
   **
   ** @return                    a string array with only 2 entries:
   **                            0: REALM, 1: OIM PREFIX.
   **
   ** @throws Exception          if data set errors occur.
   */
  protected String[] participantRealm(final long resourceInstance)
    throws Exception {

    String prefix = ResourceConfig.configurationValue(resourceInstance, PARTICIPANT_PREFIX);
    String realm  = ResourceConfig.participantRealm(resourceInstance);
    if (StringUtility.isEmpty(prefix) || StringUtility.isEmpty(realm)) {
      this.logger.warn("Failed to get Participant Realm for IT Resource{'" + resourceInstance + "'}");
      throw new Exception("Failed to get Participant Realm for IT Resource{'" + resourceInstance + "'}");
    }
    return new String[] { realm, prefix };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationNameParent
  /**
   ** @param  value              the fullqualified Distinguished Name e.g.
   **                            "cn=abc,ou=def,dc=123".
   ** @param  data               a string array with only 2 entries:
   **                            0: REALM, 1: OIM PREFIX.
   **
   ** @return                    a fullqualified organization name e.g.
   **                            "<code>data[0]</code> abc def 123".
   **
   ** @throws InvalidNameException if a syntax violation is detected in the
   **                              specified distinguished name
   **                              <code>value</code>.
   */
  protected String organizationNameParent(final LdapName value, final String[] data)
    throws InvalidNameException {

    final LdapName      base = new LdapName(data[0]);
    final StringBuilder name = new StringBuilder(data[1]);
    for (int i = base.size(); i < value.size() - 1; i++)
      name.append(' ').append(value.getRdn(i).getValue());
    // not toUppercase() means Particpant MUST be case-sensitive
    return name.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationNameCurrent
  /**
   ** @param  value              the fullqualified Distinguished Name e.g.
   **                            "cn=abc,ou=def,dc=123".
   ** @param  data               a string array with only 2 entries:
   **                            0: REALM, 1: OIM PREFIX.
   **
   ** @return                    a fullqualified organization name e.g.
   **                            "<code>data[0]</code> abc def 123".
   **
   ** @throws InvalidNameException if a syntax violation is detected in the
   **                              specified distinguished name
   **                              <code>value</code>.
   */
  protected String organizationNameCurrent(final String value, final String[] data)
    throws InvalidNameException {

    final LdapName      dn    = new LdapName(value);
    final LdapName      base  = new LdapName(data[0]);
    final int           start = dn.size() - base.size();
    final StringBuilder name  = new StringBuilder();
    // build the name of the organizational unit in revers oder of the
    // distinguished name and stop at the username from being involved by set
    // stop index to 1
    for (int i = start; i >= 1; i--) {
      // skip any occurence of unnecessary parts like OU=Benutzer
      if (StringUtility.endsWithIgnoreCase(dn.getRdn(i).getValue().toString(), "Benutzer"))
        continue;
      if (i != start)
        name.append(' ');
      name.append(dn.getRdn(i).getValue());
    }
    // not toUppercase() means Particpant MUST be case-sensitive
    return name.toString();
  }
}