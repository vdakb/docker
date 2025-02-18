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

    File        :   NamePolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NamePolicy.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.policy.usr;

import java.util.Set;

import oracle.iam.conf.vo.SystemProperty;

import oracle.iam.conf.api.SystemConfigurationService;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.hst.foundation.utility.Replace;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.event.AbstractEventHandler;

import oracle.iam.identity.policy.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class NamePolicy
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>NamePolicy</code> act as the plugin point for the Oracle Identity
 ** Manager to generate names.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class NamePolicy extends AbstractEventHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String LASTNAME   = "LN";
  private static final String FIRSTNAME  = "FN";
  private static final String MIDDLENAME = "MN";
  private static final String ROLENAME   = "RN";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String policyName;

  //////////////////////////////////////////////////////////////////////////////
  // enum Rule
  // ~~~~ ~~~~
  /**
   ** This enum store the grammar's constants for the convertion rules of this
   ** policy.
   */
  public enum Rule {
      CN("OCS.Policy.CommonName",  "{LN}, {FN} {MN} {RN}")
    , DN("OCS.Policy.DisplayName", "{FN} {MN} {LN} {RN}")
    , RN("OCS.Policy.RoleExtern",  "Contractor|Consultant|CWK|Temp")
    ;

    private final String property;
    private final String pattern;

    Rule(final String property, final String pattern) {
      this.property = property;
      this.pattern  = pattern;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NamePolicy</code> policy that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected NamePolicy() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.policyName = getClass().getSimpleName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalized
  /**
   ** Generates a name based on the name generation policy using the user
   ** data available in {@link Replace} <code>parser</code>.
   ** <br>
   ** Leading and trailing whitespace omitted (trim). Any further occurence of a
   ** whitespace character sequence is be replaced with one space character
   ** (collapse).
   ** <br>
   ** All accented characters in the generated name are replaced by unaccented
   ** equivalents.
   **
   ** @param  parser             the token parser.
   ** @param  rule               the policy rule pattern.
   **                            the pattern can contain any combination of
   **                            following symbols
   **                            <ul>
   **                              <li>{FN} - symbol representing the occurence
   **                                          of the First Name in the common
   **                                          name to build.
   **                              <li>{LN} - symbol representing the occurence
   **                                         of the Last Name in the common
   **                                         name to build.
   **                              <li>{MN} - symbol representing the occurence
   **                                         of the Middle Name in the common
   **                                         name to build.
   **                              <li>{RN} - symbol representing the occurence
   **                                         of the Role Name in the common
   **                                         name to build.
   **                            </ul>
   **
   ** @return                    the name build for <code>rule</code> by
   **                            <code>parser</code>.
   */
  static String normalized(final Replace parser, final Rule rule) {
    return StringUtility.replaceAccents(build(parser, rule));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Generates a  name based on the name generation policy using the user data
   ** available in {@link Replace} <code>parser</code>.
   ** <br>
   ** Leading and trailing whitespace omitted (trim). Any further occurence of a
   ** whitespace character sequence is be replaced with one space character
   ** (collapse).
   **
   ** @param  parser             the token parser.
   ** @param  rule               the policy rule pattern.
   **                            the pattern can contain any combination of
   **                            following symbols
   **                            <ul>
   **                              <li>{FN} - symbol representing the occurence
   **                                          of the First Name in the display
   **                                          name to build.
   **                              <li>{LN} - symbol representing the occurence
   **                                         of the Last Name in the display
   **                                         name to build.
   **                              <li>{MN} - symbol representing the occurence
   **                                         of the Middle Name in the display
   **                                         name to build.
   **                              <li>{RN} - symbol representing the occurence
   **                                         of the Role Name in the display
   **                                         name to build.
   **                            </ul>
   **
   **
   ** @return                    the name build for <code>rule</code> by
   **                            <code>parser</code>.
   */
  static String build(final Replace parser, final Rule rule) {
    return StringUtility.collapseWhitespace(parser.execute(patternProperty(rule)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parser
  /**
   ** Create and populate the token parser.
   **
   ** @param  firstName          the value for <code>First Name</code> in the
   **                            user profile attribute mapping.
   ** @param  lastName           the value for <code>Last Name</code> in the
   **                            user profile attribute mapping.
   ** @param  middleName         the value for <code>Middle Name</code> in the
   **                            user profile attribute mapping.
   ** @param  employeeType       the value for <code>Employee Type</code> in the
   **                            user profile attribute mapping.
   **
   ** @return                    a {@link Replace} token parser.
   **
   ** @throws EventFailedException if there is an exception while obtaining the
   **                              properties from the user profile for building
   **                              a common name.
   */
  static Replace parser(final String firstName, final String lastName, final String middleName, final String employeeType) {
    final String[]    temp = patternProperty(Rule.RN).split("\\|");
    final Set<String> role = CollectionUtility.set(temp);
    // create a pattern
    return new Replace.Default()
      .register(NamePolicy.LASTNAME,   lastName)
      .register(NamePolicy.FIRSTNAME,  firstName)
      .register(NamePolicy.MIDDLENAME, empty(middleName) ? "" : middleName)
      .register(NamePolicy.ROLENAME,   role.contains(employeeType)  ? " (extern)" : "");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patternProperty
  /**
   ** Fetch the pattern property for the specified name from the configuration.
   ** <p>
   ** If the system is misconfigured it falls back to the default rule of
   ** {@link Rule} <code>rule</code>.
   **
   ** @param  rule               the {@link Rule} of the property to fetch from
   **                            the configuration.
   **
   ** @return                    the name policy; either default or
   **                            fetched from the system configuration.
   */
  protected static String patternProperty(final Rule rule) {
    try {
      return patternProperty(rule.property);
    }
    catch (SystemConfigurationServiceException e) {
      System.err.println(e.getLocalizedMessage());
      return rule.pattern;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patternProperty
  /**
   ** Fetch the pattern property for the specified name from the configuration.
   **
   ** @param  name               the name of the property to fetch from the
   **                            configuration.
   **
   ** @return                    the pattern property for the specified name
   **                            from the configuration.
   **
   ** @throws SystemConfigurationServiceException if the system property could
   **                                             not be fetched or if the
   **                                             property exists but is empty.
   */
  protected static String patternProperty(final String name)
    throws SystemConfigurationServiceException {

    final SystemConfigurationService config  = service(SystemConfigurationService.class);
    final SystemProperty             pattern = config.getSystemProperty(name);
    if (pattern == null)
      throw new SystemConfigurationServiceException(Bundle.format(NamePolicyError.PROPERTY_NOTFOUND, name));

    final String                     value   = pattern.getPtyValue();
    if (empty(value))
      throw new SystemConfigurationServiceException(Bundle.format(NamePolicyError.PROPERTY_INVALID, name));

    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Check to ensure that a <code>String</code> is not <code>null</code> or
   ** empty after trimming of leading and trailing whitespace. Usually used with
   ** assertions, as in
   ** <pre>
   **   assert empty(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            String to be tested for emptiness.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   */
  protected static boolean empty(final String subject) {
    return StringUtility.isEmpty(subject, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patternRule
  /**
   ** Fetch the pattern property for the specified name from the configuration.
   **
   ** @param  rule               the {@link Rule} of the property to fetch from
   **                            the configuration.
   **
   ** @return                    the name policy; either default or
   **                            fetched from the system configuration.
   */
  protected String patternRule(final Rule rule) {
    final String method = "patternProperty";
    try {
      return patternProperty(rule.property);
    }
    catch (SystemConfigurationServiceException e) {
      fatal(method, e);
      return rule.pattern;
    }
  }
}