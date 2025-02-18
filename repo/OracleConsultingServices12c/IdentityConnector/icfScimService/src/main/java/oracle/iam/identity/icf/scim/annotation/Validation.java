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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Library

    File        :   Validation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Validation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.annotation;

import java.net.URI;

import java.util.Set;
import java.util.Locale;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.regex.Pattern;

////////////////////////////////////////////////////////////////////////////////
// enum Validation
// ~~~~ ~~~~~~~~~~
/**
 ** An enumeration of all possible (formatting) validations applicable to
 ** attributes of SCIM resources.
 */
public enum Validation {
  EMAIL, PHONE, PHOTO, COUNTRY, LOCALE, TIMEZONE;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Set<String>   country;
  private static Set<String>   timeZone;

  private static final Pattern email     = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    country  = new HashSet<String>(Arrays.asList(Locale.getISOCountries()));
    timeZone = new HashSet<String>(Arrays.asList(TimeZone.getAvailableIDs()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  apply
  /**
   ** This method receives a validation "type" and the object upon which to
   ** perform the validation.
   **
   ** @param  validation         a {@link Validation enum constant} that
   **                            specifies which validation should be applied.
   ** @param  value              A non-<code>null</code> object target of
   **                            validation.
   **
   ** @return                    <code>true</code> for success validation;
   **                            otherwise <code>false</code>.
   */
  public static boolean apply(final Validation validation, final Object value) {
    boolean passed = false;
    switch (validation) {
      // from spec:
      // The value SHOULD be specified according to [RFC5321].
      case EMAIL    : passed = validateEmail(value.toString());
                      break;
      // from spec:
      // The value SHOULD be specified according to the format defined in
      // [RFC3966], e.g., 'tel:+1-201-555-0123'.
      // IMPORTANT NOTE:
      // Implementing this validation will raise compatibility problems in
      // terms of data current customers handle. More info at
      // https://stbeehive.oracle.com/teamcollab/wiki/iam-germany/scim-address-validator-configurable-validators
      case PHONE    : passed = true;
                      break;
      // from spec:
      // A URI that is a uniform resource locator (as defined in Section 1.1.3
      // of [RFC3986])
      case PHOTO    : passed = validateURI(value.toString());
                      break;
      // from spec:
      // When specified, the value MUST be in ISO 3166-1 "alpha-2" code format
      // [ISO3166]
      case COUNTRY  : passed = validateCountry(value.toString());
                      break;
/*
      // from spec:
      // Each value contains exactly one DER-encoded X.509 certificate (see
      // Section 4 of [RFC5280])
      case X509     : passed = true;
                      break;
*/
      // from spec:
      // A valid value is a language tag as defined in [RFC5646].
      // Computer languages are explicitly excluded.
      case LOCALE   : passed = validateLocale(value.toString());
                      break;
      // from spec:
      // The User's time zone, in IANA Time Zone database format [RFC6557], also
      // known as the "Olson" time zone database format [Olson-TZ] (e.g.,
      // "America/Los_Angeles").
      case TIMEZONE : passed = validateTimezone(value.toString());
                      break;
    }
    return passed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  validateEmail
  private static boolean validateEmail(String value) {
    return email.matcher(value).matches();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  validateLocale
  private static boolean validateLocale(String value) {

    value = value.replaceAll("_", "-");
    try {
      new Locale.Builder().setLanguageTag(value);
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  validateCountry
  private static boolean validateCountry(final String value) {
    return country.contains(value.toUpperCase());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  validateTimezone
  private static boolean validateTimezone(final String value) {
    return timeZone.contains(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  validateURI
  private static boolean validateURI(final String value) {
    boolean valid = true;
    try {
      new URI(value);
    }
    catch (Exception e) {
      valid = false;
    }
    return valid;
  }
}
