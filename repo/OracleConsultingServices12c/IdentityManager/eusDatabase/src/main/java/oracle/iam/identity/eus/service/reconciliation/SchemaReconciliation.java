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

    File        :   SchemaReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-03-01  DSteding    First release version
*/

package oracle.iam.identity.eus.service.reconciliation;

import java.util.List;
import java.util.Map;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryReference;

import oracle.iam.identity.gds.service.reconciliation.TargetReconciliation;

import oracle.iam.identity.eus.transformer.SplitAccount;

////////////////////////////////////////////////////////////////////////////////
// abstract class SchemaReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>SchemaReconciliation</code> act as the service end point for the
 ** Oracle Identity Manager to reconcile target system information from an
 ** Enterprise User Security Realm.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class SchemaReconciliation extends TargetReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>SchemaReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected SchemaReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryDN
  /**
   ** Returns the distinguished name of an entry from the provided attribute
   ** mapping.
   **
   ** @return                    the distinguished name of an entry from the
   **                            provided attribute mapping.
   */
  protected String entryDN(final Map<String, Object> subject) {
    // obtain the distinguished name from the subject
    return (String)subject.get(this.connector().distinguishedNameAttribute());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method groupd by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateReferences (overridden)
  /**
   ** This method can be overwritten and entry DN or descriptor can be modified
   ** before a populateReference is executed.
   **
   ** @param  master             the {@link Map} containing all data to be
   **                            reconcile so far.
   ** @param  descriptor         the descriptor to handle a particular object
   **                            reference for the specified
   **                            <code>entryDN</code>.
   **
   ** @return                    a {@link List} where each entry is a {link Map}.
   **                            The contained {@link Map} has always a size of
   **                            <code>1</code>. The key is mapped name of the
   **                            server attribute name. The value associated
   **                            with the key is the value assigned to the
   **                            server attribute.
   **
   ** @throws TaskException      if the operation fails.
   */
  @Override
  protected List<Map<String, Object>> populateReferences(final String entryDN, final Map<String, Object> master, final DirectoryReference descriptor)
    throws TaskException {

    final DirectoryConnector connector = this.connector();
    // Construct a hierarchy
    String hierarchy = normalizePath((String)master.get(SplitAccount.ATTRIBUTE_HIERARCHY_NAME));
    if (connector.entitlementPrefixRequired())
      hierarchy = DirectoryConnector.unescapePrefix(hierarchy);
    // Construct an account
    final String account   = (String)master.get(SplitAccount.ATTRIBUTE_ACCOUNT_NAME);
    final String prefix    = connector.accountObjectPrefix();
    // Define a searchBase where a query will be executed
    descriptor.entrySearchBase(searchBase());
    return super.populateReferences(DirectoryConnector.composeName(prefix, account) + "," + hierarchy, descriptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (overridden)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do target reconciliation of Oracle Identity Manager Users.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected void processSubject(final Map<String, Object> subject)
    throws TaskException {

    // get the name of the attribute. Check if the case sensitives is enabled.
    String accountAttribute = DirectoryConstant.ENTERPRISE_SCHEMA_ACCOUNT_DEFAULT;
    if (this.connector().distinguishedNameCaseSensitive())
      accountAttribute = accountAttribute.toLowerCase();

    // Remove a root context from the attribute orclDBDistinguishedName
    // if the distinquised names has to be handled relative
    String accountDN = (String)subject.get(accountAttribute);
    if (accountDN != null && connector().isDistinguishedNameRelative())
      subject.put(accountAttribute, normalizePath(accountDN));

    super.processSubject(subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformMaster
  /**
   ** Do all action which should take place to transform master data.
   **
   ** @param  master             the {@link Map} to reconcile.
   **
   ** @return                    the {@link Map} with transformend data.
   **
   ** @throws TaskException      if data process fails.
   */
  protected Map<String, Object> transformMaster(Map<String, Object> master)
    throws TaskException {

    final Map<String, Object> data = super.transformMaster(master);
    if (this.connector().entitlementPrefixRequired()) {
      final DirectoryResource resource = this.resource();
      data.put(SplitAccount.ATTRIBUTE_HIERARCHY_NAME, String.format(ENTITLEMENT_ENCODED_PREFIX, resource.instance(), data.get(SplitAccount.ATTRIBUTE_HIERARCHY_NAME)));
    }
    return data;
  }
}