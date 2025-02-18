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

    File        :   AbstractStringAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractStringAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.foundation.event;

import java.util.Set;
import java.util.HashSet;

import java.io.Serializable;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import oracle.iam.configservice.api.LocaleUtil;

import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.utils.crypto.CryptoUtil;

import oracle.iam.request.vo.RequestData;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractStringAdapter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Pre-Populate Adapter to fillup a string value.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractStringAdapter extends AbstractPrePopulateHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryLoginName
  /**
   ** Obtains the user login name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user login name from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable beneficiaryLoginName(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.USER_LOGIN.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryPassword
  /**
   ** Obtains the password from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the password from the identity profile of the
   **                            beneficiary.
   */
  protected Serializable beneficiaryPassword(final RequestData requestData) {
    return profileEncrypted(requestData, UserManagerConstants.AttributeName.PASSWORD.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryFirstName
  /**
   ** Obtains the first name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user login name from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable beneficiaryFirstName(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.FIRSTNAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryLastName
  /**
   ** Obtains the last name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the last name from the identity profile of the
   **                            beneficiary.
   */
  protected Serializable beneficiaryLastName(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.LASTNAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryMiddleName
  /**
   ** Obtains the middle name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the middle name from the identity profile of
   **                            the beneficiary.
   */
  protected Serializable beneficiaryMiddleName(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.MIDDLENAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryInitials
  /**
   ** Obtains the initials from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the initials from the identity profile of the
   **                            beneficiary.
   */
  protected Serializable beneficiaryInitials(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.INITIALS.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryCommonName
  /**
   ** Obtains the common name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user common name from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable beneficiaryCommonName(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.COMMONNAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryDisplayName
  /**
   ** Obtains the display name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user display name from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable beneficiaryDisplayName(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.DISPLAYNAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryFullName
  /**
   ** Obtains the full name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user full name from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable beneficiaryFullName(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.FULLNAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryEmail
  /**
   ** Obtains the e-Mail address from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user e-Mail address from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryEmail(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.EMAIL.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryPhoneNumber
  /**
   ** Obtains the phone number from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user phone number from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryPhoneNumber(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.PHONE_NUMBER.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryMobileNumber
  /**
   ** Obtains the mobile number from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user mobile number from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryMobileNumber(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.MOBILE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryFacsimileNumber
  /**
   ** Obtains the facsimile number from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user facsimile number from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryFacsimileNumber(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.FAX.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryPagerNumber
  /**
   ** Obtains the pager number from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user pager number from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryPagerNumber(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.PAGER.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryDescription
  /**
   ** Obtains the description from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user description from the identity profile
   **                            of the beneficiary.
   */
  protected Serializable beneficiaryDescription(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.DESCRIPTION.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryRole
  /**
   ** Obtains the user role from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user role from the identity profile of the
   **                            beneficiary.
   */
  protected Serializable beneficiaryRole(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.USERTYPE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryType
  /**
   ** Obtains the user type from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user type from the identity profile of the
   **                            beneficiary.
   */
  protected Serializable beneficiaryType(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.EMPTYPE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryStatus
  /**
   ** Obtains the user status from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user status from the identity profile of
   **                            the beneficiary.
   */
  protected Serializable beneficiaryStatus(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.ACCOUNT_STATUS.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryCountry
  /**
   ** Obtains the country from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user country from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryCountry(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.USER_COUNTRY.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryLocality
  /**
   ** Obtains the locality from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user locality from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryLocality(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.LOCALITY_NAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryState
  /**
   ** Obtains the state from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user state from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryState(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.STATE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryStreet
  /**
   ** Obtains the street from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user street from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryStreet(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.STREET.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryPostalCode
  /**
   ** Obtains the postal code from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user postal code from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryPostalCode(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.POSTAL_CODE.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryPostalAddress
  /**
   ** Obtains the postal address from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user postal address from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryPostalAddress(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.POSTAL_ADDRESS.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryPostBox
  /**
   ** Obtains the post box from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the user post box from the identity
   **                            profile of the beneficiary.
   */
  protected Serializable beneficiaryPostBox(final RequestData requestData) {
    return profileValue(requestData, UserManagerConstants.AttributeName.PO_BOX.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileValue
  /**
   ** Obtains an attribute value from the identity profile of the beneficiary
   ** the passed {@link RequestData} <code>requestData</code> belongs to.
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

    String attributeValue = null;
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
        attributeValue = LocaleUtil.getStringValue(value);
    }
    catch (Exception e) {
      fatal(method, e);
    }
    return attributeValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileEncrypted
  /**
   ** Obtains an encrypted profile attribute value from the identity profile of
   ** the beneficiary the passed {@link RequestData} <code>requestData</code>
   ** belongs to.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   ** @param  attributeName      the name of the identity profile attribute to
   **                            decrypt.
   **
   ** @return                    the decypted password value from the
   **                            identity profile of the beneficiary.
   */
  protected Serializable profileEncrypted(final RequestData requestData, final String attributeName) {
    final String method = "profileEncrypted";
    String attributeValue = null;
    // assumption is that these pre-Pop Adapters are used with Single request.
    final String      beneficiary = requestData.getBeneficiaries().get(0).getBeneficiaryKey();
    Connection        connection  = null;
    ResultSet         resultSet   = null;
    PreparedStatement statement   = null;
    try {
      connection  = DatabaseConnection.aquire();
      statement   = DatabaseStatement.createPreparedStatement(connection, "SELECT " + attributeName + " FROM usr WHERE usr_key = ?");
      statement.setString(1, beneficiary);
      resultSet = statement.executeQuery();
      if (resultSet != null)
        attributeValue = resultSet.getString(1);
    }
    catch (SQLException e) {
      fatal(method, e);
    }
    catch (TaskException e) {
      error(method, e.getLocalizedMessage());
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    // decrypt the passwords
    return decryptValue(attributeValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decryptValue
  /**
   ** Decrypts a encrypted value.
   **
   ** @param  encrypted          the encrypted value.
   **
   ** @return                    the decypted value.
   */
  private String decryptValue(final Object encrypted) {
    final String method = "decryptValue";

    final String temp = (encrypted instanceof ContextAware) ? ((ContextAware)encrypted).getObjectValue().toString() : encrypted.toString();
    char[] clearValue = null;
    try {
      clearValue = CryptoUtil.getDecryptedPassword(temp, null);
    }
    catch (Exception e) {
      fatal(method, e);
    }
    return String.valueOf(clearValue);
  }
}