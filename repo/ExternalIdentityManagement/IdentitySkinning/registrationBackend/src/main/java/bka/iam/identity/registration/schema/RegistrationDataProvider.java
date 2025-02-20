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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Self Service Registration

    File        :   RegistrationDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RegistrationDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.registration.schema;

import java.text.MessageFormat;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import oracle.iam.exception.OIMError;
import oracle.iam.exception.OIMServiceException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.request.exception.InvalidRequestException;
import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.exception.BulkEntitiesAddException;
import oracle.iam.request.exception.BulkBeneficiariesAddException;
import oracle.iam.request.exception.InvalidRequestDataException;

import oracle.iam.passwordmgmt.exception.InvalidPasswordException;

import oracle.iam.selfservice.exception.UnauthenticatedSelfServiceException;

import oracle.iam.selfservice.uself.uselfmgmt.api.UnauthenticatedSelfService;

import oracle.iam.ui.platform.exception.MessageConstants;
import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.OIMClientFactory;
import oracle.iam.ui.platform.utils.UIResourceBundleUtil;

////////////////////////////////////////////////////////////////////////////////
// class RegistrationDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by Self Service Registration customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RegistrationDataProvider extends AbstractDataProvider<RegistrationAdapter> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4542724580105474565")
  private static final long serialVersionUID = 7055083578729635224L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RegistrationDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RegistrationDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   search (DataProvider)
  /**
   ** Return a list of backend objects based on the given filter criteria.
   **
   ** @param  searchCriteria     the OIM search criteria to submit.
   **                            <br>
   **                            Allowed object is {@link SearchCriteria}.
   ** @param  requested          the {@link Set} of attributes to be returned in
   **                            search results.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  control            contains pagination, sort attribute, and sort
   **                            direction.
   **                            <br>
   **                            Allowed object is {@link HashMap} where each
   **                            element is of type [@link String} for the key
   **                            and {@link Object} for the value.
   **
   ** @return                    a list of backend objects.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link RegistrationAdapter}.
   */
  @Override
  public List<RegistrationAdapter> search(final SearchCriteria searchCriteria, final Set<String> requested, final HashMap<String, Object> control) {
    final List<RegistrationAdapter> batch = new ArrayList<RegistrationAdapter>();
    batch.add( new RegistrationAdapter());
    return batch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   create (overridden)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object creation.
   **
   ** @param  mab                the model adapter bean, with attributes set.
   */
  @Override
  public void create(final RegistrationAdapter mab) {
    final UnauthenticatedSelfService selfService = OIMClientFactory.getUnauthenticatedSelfService();
     try {
      // all field values submitted are stored in request
      Map<String, Object> request = new HashMap<String, Object>();
      request.putAll(mab.getRequestData());
      Map<String, String> attributeRefMap = new HashMap<String, String>();
      // Iterate over all attribute references defined in dataset
      for (String attrName : mab.getRequestDataSetAttrRefMap().keySet()) {
        // Check if this field has a non null value
        if (request.get(attrName) != null) {
          // Store it as an attribute reference
          attributeRefMap.put(mab.getOIMAttributeRef(attrName), attrName);
        }
      }
      if (request.containsKey("Password")) {
        request.put("usr_password", request.get("Password"));
        request.remove("Password");
      }
      Map<String, Object> quesAndAnsMap = new HashMap<>();
      String requestId = selfService.submitRegistrationRequest(request, quesAndAnsMap, RegistrationAdapter.DATASET, attributeRefMap);

      mab.setRequestId(requestId);
    }
    catch (RequestServiceException e) {
      throw createOIMRuntimeException(e);
    }
    catch (InvalidRequestException e) {
      throw createOIMRuntimeException(e);
    }
    catch (InvalidRequestDataException e) {
      throw createOIMRuntimeException(e);
    }
    catch (BulkBeneficiariesAddException e) {
      throw createOIMRuntimeException(e);
    }
    catch (BulkEntitiesAddException e) {
      throw createOIMRuntimeException(e);
    }
    catch (UnauthenticatedSelfServiceException e) {
      throw createOIMRuntimeException(e);
    }
    catch (Exception e) {
//      logger.log(logger.ERROR, "Exception occurred while registering user", e);
      throw new OIMRuntimeException(e);
    }
  }

  private OIMRuntimeException createOIMRuntimeException(OIMServiceException e) {
    List<OIMError> errors = new ArrayList<OIMError>();
    extractError(e, errors);
    OIMRuntimeException exList = null;
    if (errors.size() > 0) {
      for (OIMError error : errors) {
        String message = error.getLocalizedMessage();
        if (message == null || message.equals("null")) {
          message = getMissingLocalizedMessageError(error.getErrorMessage());
        }
        OIMRuntimeException ex = new OIMRuntimeException(message);
        if (exList == null) {
          exList = ex;
        }
        else {
          exList.addToExceptions(ex);
        }
      }
    }
    else {
      exList = new OIMRuntimeException(e);
    }
    return exList;
  }

  /**
   * Parse OIMServiceException and extract OIMError list.
   * @param e {@link Throwable}
   * @param errors {@link List<OIMError>}
   */
  private void extractError(Throwable e, List<OIMError> errors) {
    if (e instanceof InvalidPasswordException) {
      InvalidPasswordException exception = (InvalidPasswordException)e;
      errors.addAll(exception.getErrors());
    }
    else if (e.getCause() != null) {
      extractError(e.getCause(), errors);
    }
  }

  private String getMissingLocalizedMessageError(String message) {
    if (message == null) {
      message = "";
    }
    else if (message.startsWith("null")) {
      message = message.substring(4);
    }

    String pattern = UIResourceBundleUtil.getStringFromResourceBundle(UIResourceBundleUtil.BUNDLE_UI_MODEL, MessageConstants.LOCALIZATION_MESSAGE_UNKNOWN, UIResourceBundleUtil.getDisplayLocale());
    return MessageFormat.format(pattern, message);
  }
}
