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

    Copyright 2019 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   SingleAccountValidator.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SingleAccountValidator.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event.req;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ResourceBundle;

import oracle.iam.platform.Platform;

import oracle.iam.platform.context.ContextManager;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.RequestData;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;

import oracle.iam.request.exception.InvalidRequestDataException;

import oracle.iam.provisioning.vo.Account;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import bka.iam.identity.event.OrchestrationError;
import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationMessage;

////////////////////////////////////////////////////////////////////////////////
// abstract class MultipleAccountValidator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Request Validator instance is used to avoid request of
 ** <code>Application Instance</code>s for identities where the use case is
 ** to ensure that only one primary account but multiple other accounts are
 ** permitted.
 ** <p>
 ** To be specific this validatator ensures that regardless of the type of an
 ** existing account the request is invalid if n account exists with the same
 ** account name.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class MultipleAccountValidator extends AccountValidator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the name of the attribute in the request data identifying the account
   */
  private final String requestName;
  /**
   ** the name of the attribute in the account data identifying the account
   */
  private final String accountName;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class IdentityAccount
  // ~~~~~ ~~~~~~~~~~~~~~~
  /**
   ** Simple wrapper to store user key and proposed account name
   */
  private class IdentityAccount {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String userKey;
    private final String requestName;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>IdentityAccount</code> wrapper that take the key of
     ** an identity and the proposed account name.
     **
     ** @param  userKey          the system identifier of a user in Identity
     **                          Manager.
     ** @param  requestName      the unique name of the account to provision
     **                          with the <code>Application Instance</code> in
     **                          Identity Manager to discover.
     */
    IdentityAccount(final String userKey, final String requestName) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.userKey     = userKey;
      this.requestName = requestName;
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MultipleAccountValidator</code> validator that take
   ** the index of the attribute referencing the proposed account name in the
   ** request data set and the attribute name of the field referencing the
   ** provisioned account name in the account data populated from Identity
   ** Manager.
   **
   ** @param  requestName        the name of the value holder in the request
   **                            dataset referencing the proposed account name.
   ** @param  accountName        the name of the attribute referencing the
   **                            provisioned account name in the account data
   **                            populated from Identity Manager.
   */
  protected MultipleAccountValidator(final String requestName, final String accountName) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.requestName = requestName;
    this.accountName = accountName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (RequestDataValidator)
  @Override
  public void validate(final RequestData data)
    throws InvalidRequestDataException {

    final String method = "validate";
    trace(method, SystemMessage.METHOD_ENTRY);
    // the collector of the invalide request data
    // the collector contains only the raw data, means system identifier
    final Map<String, Set<IdentityAccount>> invalid = CollectionUtility.caseInsensitiveMap();
    for (Beneficiary beneficiary : data.getBeneficiaries()) {
      final String identity = beneficiary.getBeneficiaryKey();
      for (RequestBeneficiaryEntity entity : beneficiary.getTargetEntities()) {
        // only provisioning operations are in scope
        if (!RequestConstants.MODEL_PROVISION_APPLICATION_INSTANCE_OPERATION.equals(entity.getOperation()))
          continue;

        String requestName = null;
        final List<RequestBeneficiaryEntityAttribute> value = entity.getEntityData();
        if (value != null && value.size() > 1) {
          for (RequestBeneficiaryEntityAttribute request : value) {
            if (this.requestName.equals(request.getName())) {
              requestName = request.getValueHolder();
            }
          }
        }
        if (StringUtility.isEmpty(requestName))
          requestName = loginName(identity);

        final String application  = entity.getEntityKey();
        if (hasAccount(identity, application, requestName)) {
          Set<IdentityAccount> collector = invalid.get(application);
          if (collector == null) {
            collector = CollectionUtility.set();
            invalid.put(application, collector);
          }
          collector.add(new IdentityAccount(identity, requestName));
        }
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
    if (invalid.size() > 0) {
      throw new InvalidRequestDataException(buildMessage(invalid));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  hasAccount
  /**
   ** Validates if an account of any type for the given <code>userKey</code> and
   ** <code>application</code>.
   ** <p>
   ** The implicite rule of this validator is to ensure that excatly only one
   ** account exists for an identity in the application instances to validate.
   ** <br>
   ** In other words if an account exists thats status isn't revoked the
   ** implicite rule is violated regardless what kind of type the account has.
   **
   ** @param  userKey            the system identifier of a user in Identity
   **                            Manager.
   ** @param  applicationKey     the system identifier of the
   **                            <code>Application Instance</code> in Identity
   **                            Manager to discover.
   ** @param  proposedName       the unique name of the account to provision
   **                            with the <code>Application Instance</code> in
   **                            Identity Manager to discover.
   **
   ** @return                    <code>true</code> if an account exists for the
   **                            user identified by <code>userKey</code> for
   **                            <code>Application Instance</code> identified by
   **                            <code>applicationKey</code>; otherwise
   **                            <code>false</code>.
   */
  private boolean hasAccount(final String userKey, final String applicationKey, final String proposedName) {
    final String method = "hasAccount";
    trace(method, SystemMessage.METHOD_ENTRY);
    boolean                   condition = false;
    final SearchCriteria      criteria  = new SearchCriteria(
      new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.APPINST_ID.getId(),     applicationKey, SearchCriteria.Operator.EQUAL)
    , new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), REVOKED,        SearchCriteria.Operator.NOT_EQUAL)
    , SearchCriteria.Operator.AND
    );

    debug(method, OrchestrationBundle.format(OrchestrationMessage.ACCOUNT_VALIDATION_MULTIPLE, userKey, applicationKey, proposedName));
    final ProvisioningService service = Platform.getService(ProvisioningService.class);
    try {
      // the API returns a list of all the accounts provisioned to the user.
      // the additionall passed criteria used to filter the accounts being
      // returned.
      // we are very interested in any account details for the purpose of the
      // use case
      final List<Account> provisioned = service.getAccountsProvisionedToUser(userKey, criteria, null, true);
      // as a primary constraint any existing account that is not in status
      // revoked and has the same name as the request propose violates the
      // implicite rule
      for (Account cursor : provisioned) {
        final Map<String, Object> data = cursor.getAccountData().getData();
        final String              name = (String)data.get(this.accountName);
        if (proposedName.equalsIgnoreCase(name)) {
          condition = true;
          break;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      debug(method, OrchestrationBundle.format(condition ? OrchestrationMessage.ACCOUNT_RULE_VIOLATED : OrchestrationMessage.ACCOUNT_RULE_SATIISFIED, userKey));
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return condition;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  buildMessage
  /**
   ** Factory method to build the final message thrown by the exception.
   **
   ** @param  collection         the violations of the use case discovered so
   **                            far.
   **
   ** @return                    the final message thrown by the exception.
   */
  private String buildMessage(final Map<String, Set<IdentityAccount>> collection) {
    final ResourceBundle bundle = OrchestrationBundle.getBundle(OrchestrationBundle.class.getName(), ContextManager.getResolvedLocale());
    final StringBuilder builder = new StringBuilder("<html><body>");
    builder.append(bundle.getString(OrchestrationError.ACCOUNT_EXISTS_ANY));
    for (String instance : collection.keySet()) {
      builder.append("<b>").append(applicationName(instance)).append("</b>&nbsp;");
      final Set<IdentityAccount> affected = collection.get(instance);
      int i = 0;
      for (IdentityAccount identity : affected) {
        if (i > 0)
          builder.append(", ");
        builder.append("<b>").append(identity.requestName).append("</b>&nbsp;");
        builder.append(displayName(identity.userKey));
        i++;
      }
    }
    return builder.append("</body></html>").toString();
  }
}