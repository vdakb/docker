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

import oracle.hst.foundation.utility.Replace;

import oracle.iam.identity.utils.Constants;

import oracle.iam.ldapsync.api.CommonNamePolicy;

import oracle.iam.ldapsync.exception.CommonNameGenerationException;

import oracle.iam.identity.policy.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class CommonName
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>CommonName</code> act as the plugin point for the Oracle Identity
 ** Manager to generate common names.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class CommonName extends    NamePolicy
                        implements CommonNamePolicy {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CommonName</code> policy that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CommonName() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (CommonNamePolicy)
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
  // Method:   isCommonNameValid (CommonNamePolicy)
  /**
   ** Validates if the common name based on the user data specified in the input
   ** <code>Map</code> is valid as per the common name generation policy.
   **
   ** @param  commonName         the common name to be validated against the
   **                            policy.
   ** @param  request            the <code>Map</code> containing the user data.
   **
   ** @return                    <code>true</code> if the user name is valid as
   **                            per the policy, <code>false</code> otherwise.
   */
  @Override
  public boolean isCommonNameValid(final String commonName, final Map<String, Object> request) {
    // prevent bogus input
    if ((commonName == null) || (commonName.length() == 0)) {
      return false;
    }

    final String method = "isCommonNameValid";
    try {
      return commonName.equalsIgnoreCase(getCommonNameFromPolicy(request));
    }
    catch (CommonNameGenerationException e) {
      fatal(method, e);
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCommonNameFromPolicy (CommonNamePolicy)
  /**
   ** Generates a common name based on the common name generation policy using
   ** the user data available in the input <code>Map</code>.
   **
   ** @param  request            the <code>Map</code> containing the user data.
   **
   ** @return                    the generated user name.
   **
   ** @throws CommonNameGenerationException if there is an exception while
   **                                       generating the common name.
   */
  @Override
  public String getCommonNameFromPolicy(final Map<String, Object> request)
    throws CommonNameGenerationException {

    return normalized(parser(request), Rule.CN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parser
  /**
   ** Evalualtes the token repository.
   **
   ** @param  profile            the user profile attribute mapping.
   **
   ** @return                    a {@link Replace} token parser.
   **
   ** @throws CommonNameGenerationException if there is an exception while
   **                                       obtaining the properties from the
   **                                       user profile for building a common
   **                                       name.
   */
  protected Replace parser(final Map<String, Object> profile)
   throws CommonNameGenerationException {

    // prevent missing required data
    final String firstName = profileValue(Constants.FIRSTNAME, profile);
    if (empty(firstName))
      throw exception(Constants.FIRSTNAME);

    // create a pattern
    return parser(firstName, profileValue(Constants.LASTNAME, profile), profileValue(Constants.MIDDLENAME, profile), profileValue(Constants.EMPTYPE, profile));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileValue
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
  protected static String profileValue(final String name, final Map<String, Object> profile) {
    return profile.get(name) == null ? null : profile.get(name).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Throws an appropriate {@link CommonNameGenerationException}.
   **
   ** @param  profileName        the name of the request data set thats is
   **                            missing by a name policy.
   **
   ** @return                    the {@link CommonNameGenerationException} with
   **                            the properties to describe the error.
   */
  protected CommonNameGenerationException exception(final String profileName) {
    final String                        message   = NamePolicyError.COMMONNAME_FAILURE + ": " + Bundle.format(NamePolicyError.COMMONNAME_FAILURE, new Object[] {profileName, this.policyName }) + ":" + profileName + ":" + this.policyName;
    final CommonNameGenerationException exception = new CommonNameGenerationException(message, NamePolicyError.COMMONNAME_FAILURE);
    exception.setParameters(new String[]{profileName, this.policyName});
    return exception;
  }
}