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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   ResetUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    ResetUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.utility;

import oracle.hst.platform.core.utility.CollectionUtility;
import oracle.hst.platform.core.utility.StringUtility;
import oracle.iam.passwordmgmt.vo.PasswordPolicyDescription;
import oracle.iam.passwordmgmt.vo.PasswordPolicyViolationsDescription;
import oracle.iam.passwordmgmt.vo.ValidationResult;
import oracle.iam.passwordmgmt.vo.rules.PasswordRuleDescription;
import oracle.iam.platform.UUID;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Utility class for password reset operations.
 */
public abstract class ResetUtility {

  /**
   * Returns a new {@link UUID} instance.
   * @return a new {@link UUID} instance.
   */
  public static String uuid() {
    return new UUID().getString();
  }

  /**
   * Expand the given base url by the parameter value.
   *
   * @param base      the base URL
   * @param parameter a parameter value to expand with
   * @return a string value of an uri, containing the <em>pwrid</em> parameter
   * @throws URISyntaxException if the URI cannot be composed
   */
  public static String urlParam(final URL base, final String parameter) throws URISyntaxException {
    URI uri = base.toURI();
    String query = uri.getQuery();
    if (StringUtility.blank(query)) {
      query = "pwrid=" + parameter;
    } else {
      query += "&pwrid=" + parameter;
    }

    return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), query, uri.getFragment()).toString();
  }

  /**
   * Returns the string value of a {@link PasswordPolicyDescription} instance in a printable format.
   * @param passwordPolicyDescription a {@link PasswordPolicyDescription} to format.
   * @return the formatted value.
   */
  public static String prettyPrint(final PasswordPolicyDescription passwordPolicyDescription) {
    final StringBuilder sb = new StringBuilder();
    sb.append(passwordPolicyDescription.getHeaderDisplayValue());
    List<PasswordRuleDescription> passwordRuleDescriptions = passwordPolicyDescription.getPasswordRulesDescription();
    if (!CollectionUtility.empty(passwordRuleDescriptions)) {
      sb.append("<ul>");
      for (PasswordRuleDescription passwordRuleDescription : passwordRuleDescriptions) {
        sb.append("<li>");
        sb.append(passwordRuleDescription.getDisplayValue());
        sb.append("</li>");
      }
      sb.append("</ul>");
    }

    return sb.toString();
  }

  /**
   * Returns the string value of a {@link ValidationResult} instance in a printable format.
   * @param result a {@link ValidationResult} to format.
   * @return the formatted value.
   */
  public static String prettyPrint(final ValidationResult result) {
    final StringBuilder                 sb                    = new StringBuilder();
    final String                        errorMessage          = result.getValidationErrorMessage();
    PasswordPolicyViolationsDescription violationsDescription = result.getPolicyViolationsDescription();

    if (!StringUtility.empty(errorMessage)) {
      sb.append(errorMessage);
    }
    if (violationsDescription != null) {
      if (sb.length() > 0) {
        sb.append("<br>");
      }
      sb.append(violationsDescription.getDisplayValue());
      List<PasswordRuleDescription> passwordRuleDescriptions = violationsDescription.getPasswordRulesDescription();
      if (!CollectionUtility.empty(passwordRuleDescriptions)) {
        sb.append("<ul>");
        for (PasswordRuleDescription passwordRuleDescription : passwordRuleDescriptions) {
          sb.append("<li>");
          sb.append(passwordRuleDescription.getDisplayValue());
          sb.append("</li>");
        }
        sb.append("</ul>");
      }
    }
    return sb.toString();
  }
}
