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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   LoginName.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LoginName.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.policy.usr;

import java.util.Map;
import java.util.Locale;
import java.util.ResourceBundle;

import oracle.iam.identity.utils.Constants;

import oracle.iam.identity.exception.UserNameGenerationException;

import oracle.iam.identity.usermgmt.api.UserNamePolicy;

import oracle.iam.identity.policy.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class LoginName
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>LoginName</code> act as the plugin point for the Oracle Identity
 ** Manager to generate login names.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class LoginName extends    NamePolicy
                       implements UserNamePolicy {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LoginName</code> policy that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LoginName() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (UserNamePolicy)
  /**
   ** Returns the description of the user name policy in the specified locale.
   **
   ** @param  locale             locale in which the policy description is to be
   **                            specified.
   **
   ** @return                    description of the user name policy in the
   **                            specified locale
   */
  @Override
  public String getDescription(final Locale locale) {
    if (locale == null) {
      return ResourceBundle.getBundle(Bundle.class.getName()).getString(this.policyName);
    }
    else {
      return ResourceBundle.getBundle(Bundle.class.getName(), locale).getString(this.policyName);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isUserNameValid (UserNamePolicy)
  /**
   ** Validates if the user name based on the user data specified in the input
   ** <code>Map</code> is valid as per the user name generation policy.
   **
   ** @param  userName           the user name to be validated against the
   **                            policy.
   ** @param  request            the <code>Map</code> containing the user data.
   **
   ** @return                    <code>true</code> if the user name is valid as
   **                            per the policy, <code>false</code> otherwise.
   */
  @Override
  public boolean isUserNameValid(final String userName, final Map<String, String> request) {
    // prevent bogus input
    if ((userName == null) || (userName.length() == 0)) {
      return false;
    }

    // prevent missing required data
    final String lastName  = profileValue(Constants.LASTNAME, request);
    final String firstName = profileValue(Constants.FIRSTNAME, request);
    if (empty(firstName) || empty(lastName)) {
      return false;
    }

    return (userName.charAt(0) == firstName.charAt(0) && (userName.substring(1).equalsIgnoreCase(lastName)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserNameFromPolicy (UserNamePolicy)
  /**
   ** Generates a user name based on the user name generation policy using the
   ** user data available in the input <code>Map</code>-
   **
   ** @param  request            the <code>Map</code> containing the user data.
   **
   ** @return                    the generated user name.
   **
   ** @throws UserNameGenerationException if there is an exception while
   **                                     generating the user name.
   */
  @Override
  public String getUserNameFromPolicy(final Map<String, String> request)
    throws UserNameGenerationException {

    // prevent missing required data
    final String firstName = profileValue(Constants.FIRSTNAME, request);
    final String lastName  = profileValue(Constants.LASTNAME, request);
    if (empty(firstName) || empty(lastName)) {
      throw exception(empty(firstName) ? Constants.FIRSTNAME : Constants.LASTNAME);
    }

    return "" + firstName.charAt(0) + lastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profile
  /**
   ** Obtains a value for the specfied <code>name</code> from the user profile
   ** atribute mapping.
   **
   ** @param  name               the name of the user profile attribute mapping
   **                            thats needs to be obtained.
   ** @param  profile            the user profile attribute mapping.
   **
   ** @return                    the attribute value mapped to <code>name</code>
   **                            in the user profile attribute mapping.
   **                            May be <code>null</code> if no mapping exists
   **                            for <code>name</code>.
   */
  protected static String profileValue(final String name, final Map<String, String> profile) {
    return profile.get(name) == null ? null : profile.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Throws an appropriate {@link UserNameGenerationException}.
   **
   ** @param  profileName        the name of the request data set thats is
   **                            missing by a name policy.
   **
   ** @return                    the {@link UserNameGenerationException} with
   **                            the properties to describe the error.
   */
  protected UserNameGenerationException exception(final String profileName) {
    final String                      message   = NamePolicyError.USERNAME_FAILURE + ": " + Bundle.format(NamePolicyError.USERNAME_FAILURE, new Object[] {profileName, this.policyName }) + ":" + profileName + ":" + this.policyName;
    final UserNameGenerationException exception = new UserNameGenerationException(message, NamePolicyError.USERNAME_FAILURE);
    exception.setParameters(new String[]{profileName, this.policyName});
    return exception;
  }
}