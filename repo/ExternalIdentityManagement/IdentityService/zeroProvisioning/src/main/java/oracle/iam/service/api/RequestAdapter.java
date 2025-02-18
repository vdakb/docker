/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   RequestAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RequestAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.api;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;

import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;

import oracle.iam.request.vo.RequestData;
import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;

import oracle.iam.request.api.RequestService;

import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.exception.InvalidRequestException;
import oracle.iam.request.exception.BulkEntitiesAddException;
import oracle.iam.request.exception.InvalidRequestDataException;
import oracle.iam.request.exception.BulkBeneficiariesAddException;

import oracle.hst.platform.core.entity.Pair;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

import oracle.iam.identity.service.spi.Platform;

public class RequestAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The service provider coordinating the transactions.
   */
  private final Platform provider;

  // the system identifier of the beneficiary the activities requested for
  private Beneficiary beneficiary;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>RequestAdapter</code> that leverage the
   ** {@link Platform} <code>provider</code> to coordinate the transactions.
   **
   ** @param  provider           the service provider coordinating the
   **                            transactions.
   **                            <br>
   **                            Allowed object is {@link Platform}. 
   */
  private RequestAdapter(final Platform provider) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.provider = provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to prepare a <code>RequestAdapter</code> leveraging the
   ** specified {@link Platform} provider.
   **
   **
   ** @param  provider           the service provider coordinating the
   **                            transactions.
   **                            <br>
   **                            Allowed object is {@link Platform}. 
   **
   ** @return                    the <code>RequestAdapter</code> created.
   **                            <br>
   **                            Possible object is <code>RequestAdapter</code>.
   **
   ** @throws NullPointerException if <code>provider</code> is
   **                              <code>null</code>.
   */
  public static RequestAdapter build(final Platform provider) {
    return new RequestAdapter(Objects.requireNonNull(provider));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Factory method to prepare a {@link RequestException} to been thrown.
   **
   ** @param  code               the key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the {@link Throwable} causing the
   **                            {@link RequestException} to create.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the exception to be thrown.
   **                            <br>
   **                            Possible object is {@link RequestException}.
   */
  public static RequestException exception(final String code, final Throwable throwable) {
    return new RequestException(code, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Factory method to prepare a {@link RequestException} to been thrown.
   **
   ** @param  code               the key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution parameters.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the exception to be thrown.
   **                            <br>
   **                            Possible object is {@link RequestException}.
   */
  public static RequestException exception(final String code, final Object... arguments) {
    return new RequestException(code, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Prepares the request model for the selected beneficiary.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** At first glance, the method seems pointless. However, it is primarily
   ** intended so that if additional attributes are required for a beneficiary,
   ** they can be obtained in this method.
   **
   ** @param  beneficiary        the name of the user identity.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public final RequestAdapter prepare(final String beneficiary)
    throws RequestException {

    if (StringUtility.equalIgnoreCase(this.beneficiary.getBeneficiaryKey(), beneficiary))
      return this;

    this.beneficiary = new Beneficiary(Beneficiary.USER_BENEFICIARY, beneficiary);

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
  ** Build the request with user ID and list of entities template and submit the
  ** created request.
  **
  ** @return                     the id of the request
  **                             <br>
  **                             Possible object is {@link String}.
  **
  ** @throws RequestException    if an error occur while submitting the
  **                             request.
  */
  public String submit()
    throws RequestException {

    final List<RequestBeneficiaryEntity> entities = new ArrayList<RequestBeneficiaryEntity>();
    return splitSubmit(entities);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   splitSubmit
  /**
  ** Build a single request with user ID for each of the entities.
  **
  ** @param  entities            the entities to request.
  **                             <br>
  **                             Allowed object is {@link List} where each
  **                             element is of type
  **                             {link RequestBeneficiaryEntity}.
  **
  ** @return                     the id of the request
  **                             <br>
  **                             Possible object is {@link String}.
  **
  ** @throws RequestException    if an error occur while submitting the
  **                             requests.
  */
  public String splitSubmit(final List<RequestBeneficiaryEntity> entities)
    throws RequestException {

    // prevent bogus input
    String requestID = null;
    if (entities.size() == 0)
      return null;

    final RequestService service = this.provider.requestService();
    final RequestData    request = new RequestData();
    request.setBeneficiaries(CollectionUtility.list(beneficiary));
    try {
      for (RequestBeneficiaryEntity cursor : entities) {
        beneficiary.setTargetEntities(CollectionUtility.list(cursor));
        final String localID = service.saveDraftRequest(request);
        service.submitDraftRequest(localID, request);
        if (requestID == null) {
          requestID = localID;
        }
        else {
          requestID += ',';
          requestID += localID;
        }
      }
    }
    catch (RequestServiceException e) {
      throw exception(RequestError.REQUEST_FAILED, "The Environment", "The Application", "The Beneficiary Name", e.getErrorMessage());
    }
    catch (InvalidRequestException e) {
      throw exception(RequestError.REQUEST_FAILED, "The Environment", "The Application", "The Beneficiary Name", e.getErrorMessage());
    }
    catch (InvalidRequestDataException e) {
      throw exception(RequestError.REQUEST_FAILED, "The Environment", "The Application", "The Beneficiary Name", e.getErrorMessage());
    }
    catch (BulkEntitiesAddException e) {
      throw exception(RequestError.REQUEST_FAILED, "The Environment", "The Application", "The Beneficiary Name", e.getErrorMessage());
    }
    catch (BulkBeneficiariesAddException e) {
      throw exception(RequestError.REQUEST_FAILED, "The Environment", "The Application", "The Beneficiary Name", e.getErrorMessage());
    }
    return requestID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findAccount
  /**
   ** Compares the attributes of the given {@code Application} with the
   ** attribute mapping of a provisioned {@link ApplicationInstance} that has
   ** the same name as the given {@code Application}.
   ** <br>
   ** The comparison of the attributes takes place without considering case
   ** sensitivity, since it is assumed that this distinction is irrelevant for a
   ** target system.
   ** <br>
   ** The validation compares the attribute values of the given
   ** {@code Application} with the mapping of the {@link ApplicationInstance}
   ** account to enforce that no additional accounts are requested that are
   ** already provisioned.
   ** <p>
   ** Another criterion for the comparison is the provisioning status of an
   ** account that is matched.
   ** <br>
   ** A hit is only indicated if the provisioning status is not contained in the
   ** {@link Set} <code>excluded</code>.
   **
   ** @param  application        the application identifier to validate.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   ** @param  attribute          the attribute mapping to validate.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   ** @param  exclude            the {@link Set} of provisioning status that
   **                            are excluding an existing provisioned account
   **                            to be indicated as a hit.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    an account id if an exact match of the
   **                            attribute mapping for an provisioned account
   **                            was found and the provisioning status of the
   **                            account is not excluded.
   **                            If no match detected return <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws RequestException  if account discovery fails in general.
   */
  public String findAccount(final Pair<String, String> application, final List<Pair<String, String>> attribute, final Set<String> exclude)
    throws RequestException {

    final ProvisioningService  provisioning = this.provider.provisioningService();
    final SearchCriteria       criteria     = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.APPINST_ID.getId(), lookupApplication(application.getKey()), SearchCriteria.Operator.EQUAL);
    // be pessimistic that the requestor always violate the rules
    String accountID = null;
    boolean match    = false;
    try {
      // fetching user accounts
      final List<Account> account = provisioning.getAccountsProvisionedToUser(this.beneficiary.getBeneficiaryKey(), criteria, null, true);
      // we have finisehd if there isn't any account that match in general
      match = (account != null && account.size() > 0);
      if (match) {
        // digging into the account to compare each account value id its equal
        // to the account data which are may requested
        for (Account cursor : account) {
          // check the status of the account to ensure that we not hitting one
          // thats gone or one that still arise, both of them are not the right
          // ones
          if (exclude.contains(cursor.getAccountStatus())) {
            continue;
          }
          final AccountData         data    = cursor.getAccountData();
          final Map<String, Object> mapping = data.getData();
          boolean local = true;
          for (Pair<String, String> detail : attribute) {
            final Object value = mapping.get(detail.getKey());
            // combine the match with the previuos one so that we aggregate all
            // comparisons done before in a single value
            local = local && (StringUtility.equalIgnoreCase(detail.getValue(), (value == null ? null : value.toString())));
            // if we don't have a match we can leave the loop because it cannot
            // become even better
            if (!local)
              break;
          }
          match = local;
          // we have a hit
          if (match) {
            accountID	= cursor.getAccountID();
            break;
          }
        }
      }
      return accountID;
    }
    catch (GenericProvisioningException | UserNotFoundException e) {
      throw exception(RequestError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupApplication
  /**
   ** Return the application instance object found with the requested name of
   ** the application in parameter.
   **
   ** @param  name               the name of the
   **                            <code>Application Instance</code> to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the system identifier of the
   **                            <code>Application Instance</code> for
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **                            <br>
   **                            Never <code>null</code>
   **
   ** @throws RequestException  if lookup operation failed.
   */
  public String lookupApplication(final String name)
    throws RequestException {

    ApplicationInstance result = null;
    try {
      result = this.provider.applicationInstanceService().findApplicationInstanceByName(name);
    }
    catch (ApplicationInstanceNotFoundException e) {
      throw exception(RequestError.REQUEST_APPLICATION_NOTFOUND, name);
    }
    catch (GenericAppInstanceServiceException e) {
      throw exception(RequestError.REQUEST_APPLICATION_NOTFOUND, name);
    }
    return String.valueOf(result.getApplicationInstanceKey());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttribute
  /**
   ** Factory method to create a {@link RequestBeneficiaryEntityAttribute} from
   ** an {@link Attribute} definition
   **
   ** @param  id                 the identifier (aka Formfield name) of the
   **                            {@link RequestBeneficiaryEntityAttribute} to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the the value to set for the
   **                            {@link RequestBeneficiaryEntityAttribute} to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the type of the
   **                            {@link RequestBeneficiaryEntityAttribute} to
   **                            create.
   **                            <br>
   **                            Must be one of
   **                            <ul>
   **                              <li>Long
   **                              <li>Short
   **                              <li>Double
   **                              <li>String
   **                              <li>Integer
   **                              <li>Boolean
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link RequestBeneficiaryEntityAttribute}
   **                            populated from the {@link Attribute}.
   **                            <br>
   **                            Possible object is
   **                            {@link RequestBeneficiaryEntityAttribute}.
   */
  private RequestBeneficiaryEntityAttribute createAttribute(final String id, final String value, final String type) {
    // check the attribute type to apply the proper converted value
    // according to the type
    final RequestBeneficiaryEntityAttribute.TYPE t = RequestBeneficiaryEntityAttribute.TYPE.valueOf(type);
    switch (t) {
//      case Date    : return new RequestBeneficiaryEntityAttribute(attribute.getId(), Date.valueOf(attribute.getValue()), type);
      case Long    : return new RequestBeneficiaryEntityAttribute(id, Long.valueOf(value),    t);
      case Short   : return new RequestBeneficiaryEntityAttribute(id, Short.valueOf(value),   t);
      case Double  : return new RequestBeneficiaryEntityAttribute(id, Double.valueOf(value),  t);
      case Integer : return new RequestBeneficiaryEntityAttribute(id, Integer.valueOf(value), t);
      case Boolean : return new RequestBeneficiaryEntityAttribute(id, Boolean.valueOf(value), t);
      default      : return new RequestBeneficiaryEntityAttribute(id, String.valueOf(value),  t);
    }
  }
}
