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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   AbstractDateAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDateAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import java.io.Serializable;

import oracle.iam.request.vo.RequestData;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractDateAdapter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Pre-Populate Adapter to fillup a date value.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractDateAdapter extends AbstractPrePopulateHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryStartDate
  /**
   ** Obtains the user start date from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user start date from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable beneficiaryStartDate(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.ACCOUNT_START_DATE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryEndDate
  /**
   ** Obtains the user end date from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user end date from the identity profile of
   **                            the beneficiary.
   */
  protected Serializable beneficiaryEndDate(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.ACCOUNT_END_DATE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryProvisionedDate
  /**
   ** Obtains the user provisioned date from the identity profile of the
   ** beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user provisioned date from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryProvisionedDate(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.PROVISIONEDDATE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryProvisioningDate
  /**
   ** Obtains the user provisioning date from the identity profile of the
   ** beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user provisioning date from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryProvisioningDate(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.PROVISIONINGDATE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileValue
  /**
   ** Obtains an attribute value from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   ** @param  attributeName      the name of the identity profile attribute to
   **                            fetch.
   **
   ** @return                    the attribute value from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable profileValue(final RequestData requestData, final String attributeName) {
    final String method = "profileValue";

    Date attributeValue = null;
    // assumption is that these pre-Pop Adapters are used with Single request.
    final String      beneficiary = requestData.getBeneficiaries().get(0).getBeneficiaryKey();
    final UserManager facade      = service(UserManager.class);
    try {
      final Set<String> returning = new HashSet<String>();
      returning.add(attributeName);
      final User   identity = facade.getDetails(beneficiary, returning, false);
      final Object value    = identity.getAttribute(attributeName);
      // although this check is not necessary for first name, you can use
      // it for other user attributes, which are non-mandatory and need to be
      // pre-populated. If the value is not found, you can return null, so that
      // nothing is pre-populated and requester can provide the value
      if (value != null)
        attributeValue = (Date)value;
    }
    catch (Exception e) {
      fatal(method, e);
    }
    return attributeValue;
  }
}