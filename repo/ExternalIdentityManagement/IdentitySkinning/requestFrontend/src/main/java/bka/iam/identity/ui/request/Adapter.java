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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Special Account Request

    File        :   Adapter.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Adapter.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.ui.request;

import bka.iam.identity.ui.RequestException;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import java.io.Serializable;

import org.w3c.dom.Document;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MOReference;
import oracle.mds.core.MetadataNotFoundException;

import oracle.mds.naming.InvalidReferenceException;
import oracle.mds.naming.InvalidReferenceTypeException;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;

import oracle.iam.provisioning.exception.UserNotFoundException;
import oracle.iam.provisioning.exception.GenericProvisioningException;
import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.GenericEntitlementServiceException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

import oracle.iam.request.vo.RequestData;
import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;

import oracle.iam.request.api.RequestService;

import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.exception.InvalidRequestException;
import oracle.iam.request.exception.BulkEntitiesAddException;
import oracle.iam.request.exception.InvalidRequestDataException;
import oracle.iam.request.exception.BulkBeneficiariesAddException;

import oracle.iam.ui.platform.model.common.OIMClientFactory;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import bka.iam.identity.ui.RequestError;

import bka.iam.identity.ui.request.model.Template;
import bka.iam.identity.ui.request.model.Attribute;
import bka.iam.identity.ui.request.model.Permission;
import bka.iam.identity.ui.request.model.Environment;
import bka.iam.identity.ui.request.model.Application;
import bka.iam.identity.ui.request.model.Configuration;

////////////////////////////////////////////////////////////////////////////////
// class Adapter
// ~~~~~ ~~~~~~~
/**
 ** Data Access Object used by end users to create special account requests.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Adapter implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PROPERTY         = "EFBS.Template.Path";

  private static final String BENEFICIARY_KEY  = "#{pageFlowScope.beneficiaryKey}";
  private static final String BENEFICIARY_NAME = "#{bindings.beneficiaryName.inputValue}";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5795079158323162544")
  private static final long   serialVersionUID = -1945099980829979473L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the system identifier of the beneficiary the choice will requested for
  private String              beneficiaryKey;
  // the public name of the beneficiary the choice will requested for
  private String              beneficiaryName;

  // the environment selected
  private String              environment;
  // the template requested
  private Template            actual;
  // the configuration model
  private Configuration       configuration;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Adapter</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Adapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnvironment
  /**
   ** Sets the currently selected environment.
   **
   ** @param  value              the currently selected environment.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException  if account discovery fails in general.
   */
  public void setEnvironment(final String value)
    throws RequestException {

    this.environment = value;
    evaluate(this.configuration.get(this.environment).getTemplate().values().iterator().next());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnvironment
  /**
   ** Returns the currently selected environment.
   **
   ** @return                    the currently selected environment.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getEnvironment() {
    return this.environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnvironments
  /**
   ** Returns the collection of available environments.
   **
   ** @return                    the collection of available environments.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Environment}.
   */
  public Collection<Environment> getEnvironments() {
    return this.configuration.values();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTemplate
  /**
   ** Sets the currently selected template.
   **
   ** @param  template           the currently selected template identifier.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException  if account discovery fails in general.
   */
  public void setTemplate(final String template)
    throws RequestException {

    evaluate(this.configuration.get(this.environment).getTemplate().get(template));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTemplate
  /**
   ** Returns the currently selected template identifier.
   **
   ** @return                    the currently selected template identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getTemplate() {
    return this.actual == null ? null : this.actual.getId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTemplates
  /**
   ** Returns the collection of available templates belonging to the selected
   ** environment.
   **
   ** @return                    the collection of available templates belonging
   **                            to the selected environment.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Template}.
   */
  public Collection<Template> getTemplates() {
    return this.environment == null ? null : this.configuration.get(this.environment).getTemplate().values();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActual
  /**
   ** Returns the template ready for request.
   **
   ** @return                    the template ready for request.
   **                            <br>
   **                            Possible object is {@link Template}.
   */
  public Template getActual() {
    return this.actual;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   **                            Possible object is {@link RequestException}}.
   */
  public static RequestException exception(final String code, final Throwable throwable) {
    return new RequestException(code, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Factory method to prepare a {@link OIMRuntimeException} to been thrown.
   **
   ** @param  code               the key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution parameters.
   **                            <br>
   **                            Allowed object is array of {@link String}s.
   **
   ** @return                    the exception to be thrown.
   **                            <br>
   **                            Possible object is {@link OIMRuntimeException}}.
   */
  public static RequestException exception(final String code, final String... arguments) {
    return new RequestException(code, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the configuration model.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void initialize()
    throws RequestException {

    // initialize the configuration model
    if (this.configuration == null) {
      String path = null;
      try {
        path = OIMClientFactory.getCachedServices().getPropertyAsString(PROPERTY);
      }
      catch (SystemConfigurationServiceException e) {
        throw new RequestException(RequestError.REQUEST_CONFIGURATION_PROPERTY, PROPERTY);
      }

      try {
        final MDSSession     session   = (MDSSession)ADF.current().getMDSSessionAsObject();
        final Document       document  = session.getMetadataObject(MOReference.create(path)).getDocument();
        this.configuration = Configuration.build(document);
      }
      catch (InvalidReferenceException e) {
        throw new RequestException(RequestError.REQUEST_CONFIGURATION_STREAM, e);
      }
      catch (InvalidReferenceTypeException e) {
        throw new RequestException(RequestError.REQUEST_CONFIGURATION_STREAM, e);
      }
      catch (MetadataNotFoundException e) {
        throw new RequestException(RequestError.REQUEST_CONFIGURATION_STREAM, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Prepares the request model for the selected beneficiary.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void prepare()
    throws RequestException {

    final String beneficiaryKey = JSF.valueFromExpression(BENEFICIARY_KEY,  String.class);
    if (StringUtility.isEqual(this.beneficiaryKey, beneficiaryKey))
      return;

    this.beneficiaryKey  = beneficiaryKey;
    this.beneficiaryName = JSF.valueFromExpression(BENEFICIARY_NAME, String.class);
    // initialize the value holders with the first entry of configured
    // environments and the first template that belong to that environment
    this.environment = this.configuration.keySet().iterator().next();
    evaluate(this.configuration.get(this.environment).getTemplate().values().iterator().next());
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

    if (this.actual == null)
      throw exception(RequestError.REQUEST_SELECTION_VIOLATED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class));

    // check if the predecessor account is provisioned on user for the template
    // the request is prepared for
    final Template template = this.configuration.get(this.environment).getTemplate().get(this.actual.getId());
    if (template.predecessor() != null) {
      final List<Pair<String, String>> accountData = new ArrayList<>();
      for (Attribute cursor : template.predecessor().getAttribute()) {
        accountData.add(Pair.of(cursor.mapping(), JSF.valueFromExpression(cursor.getValue(), String.class)));
      }
      if (findAccount(template.predecessor().id(), accountData, CollectionUtility.set("Revoked")) == null)
        // if we had no luck to find the required account don't initiate the
        // request process
        throw exception(RequestError.REQUEST_PREDECESSOR_VIOLATED, JSF.valueFromExpression(template.predecessor().getLabel(), String.class), this.beneficiaryName, JSF.valueFromExpression(template.getLabel(), String.class));
    }
    final List<RequestBeneficiaryEntity> entities = new ArrayList<RequestBeneficiaryEntity>();
    if (this.actual.getAccount().size() > 0) {
      for (Application cursor : this.actual.getAccount()) {
        final RequestBeneficiaryEntity entity = new RequestBeneficiaryEntity();
        entity.setEntitySubType(cursor.getId());
        entity.setRequestEntityType(OIMType.ApplicationInstance);
        entity.setEntityKey(cursor.accountID() == null ? lookupApplication(cursor.getId()) : cursor.accountID());
        entity.setOperation(cursor.accountID() == null ? RequestConstants.MODEL_PROVISION_APPLICATION_INSTANCE_OPERATION : RequestConstants.MODEL_MODIFY_ACCOUNT_OPERATION);
        final List<RequestBeneficiaryEntityAttribute> attributes = new ArrayList<RequestBeneficiaryEntityAttribute>();
        for (Attribute attribute : cursor.getAttribute()) {
          attributes.add(createAttribute(attribute));
        }
        if (cursor.getPermission().size() > 0) {
          for (Permission permission : cursor.getPermission()) {
            // this needs to be changed because it assumes that only single
            // values as entitlements are requested
            for (Attribute attribute : permission.getAttribute()) {
              final RequestBeneficiaryEntityAttribute  parent = new RequestBeneficiaryEntityAttribute(permission.id().tag, "", RequestBeneficiaryEntityAttribute.TYPE.String);
              // the key is the name of the child table like UD_CTS_UGP
              final List<RequestBeneficiaryEntityAttribute> record = new ArrayList<RequestBeneficiaryEntityAttribute>();
              record.add(createAttribute(attribute.getId(), lookupEntitlement(attribute.getValue()), attribute.type()));
              parent.setAction(RequestBeneficiaryEntityAttribute.ACTION.Add);
              parent.setChildAttributes(record);
              attributes.add(parent);
            }
          }
        }
        entity.setEntityData(attributes);
        entities.add(entity);
      }
    }
    return this.configuration.bulk() ? bulkSubmit(entities) : splitSubmit(entities);
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

    final Beneficiary beneficiary = new Beneficiary(Beneficiary.USER_BENEFICIARY, this.beneficiaryKey);

    final RequestService service = OIMClientFactory.getRequestService();
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
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (InvalidRequestException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (InvalidRequestDataException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (BulkEntitiesAddException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (BulkBeneficiariesAddException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    return requestID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulkSubmit
  /**
  ** Build the heterogeniuos request with user ID and list of entities template
  ** and submit the request at once.
  ** <p>
  ** This is the default behavior like the Catalog is doing.
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
  **                             request.
  */
  public String bulkSubmit(final List<RequestBeneficiaryEntity> entities)
    throws RequestException {

    String requestID = null;
    if (entities.size() == 0)
      return null;

    final Beneficiary beneficiary = new Beneficiary(Beneficiary.USER_BENEFICIARY, this.beneficiaryKey);
    beneficiary.setTargetEntities(entities);
    final RequestData request = new RequestData();
    request.setBeneficiaries(CollectionUtility.list(beneficiary));
    try {
      final RequestService service = OIMClientFactory.getRequestService();
      requestID = service.saveDraftRequest(request);
      service.submitDraftRequest(requestID, request);
    }
    catch (RequestServiceException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (InvalidRequestException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (InvalidRequestDataException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (BulkEntitiesAddException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    catch (BulkBeneficiariesAddException e) {
      throw exception(RequestError.REQUEST_FAILED, JSF.valueFromExpression(this.configuration.get(this.environment).getLabel(), String.class), JSF.valueFromExpression(this.actual.getLabel(), String.class), this.beneficiaryName, e.getErrorMessage());
    }
    return requestID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findAccount
  /**
   ** Compares the attributes of the given {@link Application} with the
   ** attribute mapping of a provisioned {@link ApplicationInstance} that has
   ** the same name as the given {@link Application}.
   ** <br>
   ** The comparison of the attributes takes place without considering case
   ** sensitivity, since it is assumed that this distinction is irrelevant for a
   ** target system.
   ** <br>
   ** The validation compares the attribute values of the given
   ** {@link Application} with the mapping of the {@link ApplicationInstance}
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
  private String findAccount(final Pair<String, String> application, final List<Pair<String, String>> attribute, final Set<String> exclude)
    throws RequestException {

    final ProvisioningService  provisioning = OIMClientFactory.getProvisioningService();
    final SearchCriteria       criteria     = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.APPINST_ID.getId(), lookupApplication(application.tag), SearchCriteria.Operator.EQUAL);
    // be pessimistic that the requestor always violate the rules
    String accountID = null;
    boolean match    = false;
    try {
      // fetching user accounts
      final List<Account> account = provisioning.getAccountsProvisionedToUser(this.beneficiaryKey, criteria, null, true);
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
            final Object value = mapping.get(detail.tag);
            // combine the match with the previuos one so that we aggregate all
            // comparisons done before in a single value
            local = local && (StringUtility.isCaseInsensitiveEqual(detail.value, (value == null ? null : value.toString())));
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
  // Method:   lookupEntitlement
  /**
   ** Return the first entitlement object found with the requested name of the
   ** entitlement.
   **
   ** @param  name               the name of the <code>Entitlement</code> to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the system identifier of the
   **                            <code>Entitlement</code> for <code>name</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **                            <br>
   **                            Never <code>null</code>
   **
   ** @throws RequestException  if lookup operation failed.
   */
  private static String lookupEntitlement(final String name)
    throws RequestException {

    List<Entitlement>    result   = null;
    final SearchCriteria criteria = new SearchCriteria(Entitlement.ENTITLEMENT_VALUE, name, SearchCriteria.Operator.EQUAL);
    try {
      result = OIMClientFactory.getEntitlementService().findEntitlements(criteria, null);
      if (result.size() == 0) {
        throw exception(RequestError.REQUEST_ENTITLEMENT_NOTFOUND, name);
      }
      if (result.size() > 1) {
        throw exception(RequestError.REQUEST_ENTITLEMENT_AMBIGUOUS, name);
      }
    }
    catch (GenericEntitlementServiceException e) {
      throw exception(RequestError.REQUEST_ENTITLEMENT_AMBIGUOUS, name);
    }
    return String.valueOf(result.get(0).getEntitlementCode());
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
  private static String lookupApplication(final String name)
    throws RequestException {

    ApplicationInstance result = null;
    try {
      result = OIMClientFactory.getAppInstanceService().findApplicationInstanceByName(name);
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
  // Method:   evaluate
  /**
   ** Evaluate the given {@link Template} <code>value</code> by resolving all
   ** expressions provided in the attribute mapping of applications and
   ** entitlement values.
   **
   ** @param  template           the template selected to be evaluated.
   **                            <br>
   **                            Allowed object is {@link Template}.
   **
   ** @return                    the template with all values evaluated to the
   **                            current context.
   **                            <br>
   **                            Possible object is {@link Template}.
   **
   ** @throws RequestException  if account discovery fails in general.
   */
  private void evaluate(final Template template)
    throws RequestException {

    // reset the selection if a null value is passed in
    if (template == null || this.environment == null) {
      this.actual = null;
    }
    else {
      this.actual = new Template(template.id());
      // create the set of provisiong status that needs to be excluded by the
      // account match algorithm 
      final Set<String> exclude = CollectionUtility.set("Revoked");
      for (Application  application : template.getAccount()) {
        final Application                request = new Application(application.getId());
        final List<Pair<String, String>> account = new ArrayList<>();
        for (Attribute cursor : application.getAttribute()) {
          final Attribute attribute = new Attribute(cursor.getId(), JSF.valueFromExpression(cursor.getLabel(), String.class), cursor.mapping(), cursor.type());
          attribute.value(JSF.valueFromExpression(cursor.getValue(), String.class));
          request.add(attribute);
          // account discovery use the account data model that is based on the
          // physical names of the model attributes
          account.add(Pair.of(attribute.mapping(), attribute.getValue()));
        }
        // if we have an account that match request the account only if
        // entitlements needs to provision
        final String accountID = findAccount(request.id(), account, exclude);
        if (accountID != null)
          request.accountID(accountID);

        // do not request an account that exists and has no entitlements
        if (accountID == null || application.getPermission().size() > 0) {
          // add the account if actual its needed
          this.actual.add(request);
        }
        // it doesn't matter if an account exists or not we assume the
        // entitlements to request are disjunct and not manually added to the
        // account under investigation
        for (Permission entitlement : application.getPermission()) {
          final Permission actual = new Permission(entitlement.id().tag);
          for (Attribute cursor : entitlement.getAttribute()) {
            final Attribute attribute = new Attribute(cursor.getId(), cursor.getId(), cursor.mapping(), cursor.type());
            attribute.value(JSF.valueFromExpression(cursor.getValue(), String.class));
            actual.add(attribute);
            // add the values to display in the UI to the template
            // keep in mind that this model follows a complete different
            // approach
            this.actual.getEntitlement().add(Pair.of(application.getLabel(), attribute.getValue()));
          }
          request.getPermission().add(actual);
        }
      }
    }
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
   ** @param  Type               the type of the
   **                            {@link RequestBeneficiaryEntityAttribute} to
   **                            create.
   **                            <br>
   **                            Allowed object is
   **                            {@link RequestBeneficiaryEntityAttribute.TYPE}.
   **
   ** @return                    the {@link RequestBeneficiaryEntityAttribute}
   **                            populated from the {@link Attribute}.
   **                            <br>
   **                            Possible object is
   **                            {@link RequestBeneficiaryEntityAttribute}.
   */
  private RequestBeneficiaryEntityAttribute createAttribute(final Attribute attribute) {
    return createAttribute(attribute.getId(), attribute.getValue(), attribute.type());
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