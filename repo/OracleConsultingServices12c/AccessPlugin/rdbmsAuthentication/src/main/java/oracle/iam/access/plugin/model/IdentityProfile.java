/*
    ORACLE Deutschland B.V. & Co. KG

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

    System      :   Oracle Access Service Plugin
    Subsystem   :   OpenIdConnect Discovery

    File        :   IdentityProfile.java

    Compiler    :   Java Development Kit 8

    Author      :   nitin.popli@oracle.com

    Purpose     :   This file implements the class
                    IdentityProfile.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-11  npopli      First release version
*/

package oracle.iam.access.plugin.model;

import java.util.List;

import oracle.security.idm.Property;
import oracle.security.idm.PropertySet;
import oracle.security.idm.ModProperty;
import oracle.security.idm.UserProfile;
import oracle.security.idm.SearchFilter;
import oracle.security.idm.SearchResponse;

////////////////////////////////////////////////////////////////////////////////
// class IdentityProfile
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This class represents detailed profile of a user identity.
 ** <br>
 ** It allows for user properties to be access in generic manner.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.1
 */
public class IdentityProfile extends    AbstractProfile<IdentityProfile, Long>
                             implements UserProfile {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5081899581255936815")
  private static final long serialVersionUID = 7684840743488481343L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityProfile</code> database resource that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public IdentityProfile() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>IdentityProfile</code> database resource.
   **
   ** @param  identifier         the primary identifier of an
   **                            <code>IdentityProfile</code> database resource.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  private IdentityProfile(final Long identifier) {
    // ensure inheritance
    super(UserProfile.GUID, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>IdentityProfile</code> database resource.
   **
   ** @param  identifier         the unique identifier of an
   **                            <code>IdentityProfile</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private IdentityProfile(final String identifier) {
    // ensure inheritance
    super(UserProfile.USER_ID, identifier);

    // initialize instance sttributes
    this.property.put(property(UserProfile.USER_NAME, identifier));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Sets the <code>userName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>userName</code> property of the
   **                            <code>IdentityProfile</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    this instance of
   **                            <code>IdentityProfile</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityProfile</code>.
   */
  public final IdentityProfile userName(final String value) {
    uniqueIdentifier(value);
    this.property.put(property(UserProfile.USER_NAME, value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayName (Identity)
  /**
   ** Sets the <code>displayName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>displayName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDisplayName(final String value) {
    property(UserProfile.DISPLAY_NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName (Identity)
  /**
   ** Returns the <code>displayName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>displayName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDisplayName() {
    return stringValue(UserProfile.DISPLAY_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserProfile (User)
  /**
   ** Returns <code>IdentityProfile</code> instance.
   **
   ** @return                    <code>IdentityProfile</code> instance.
   **                            <br>
   **                            Allowed object is {@link UserProfile}.
   */
  @Override
  public final UserProfile getUserProfile() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllUserProperties (UserProfile)
  /**
   ** Sets the entire properties  of the <code>IdentityProfile</code>.
   **
   ** @return                    the entire properties of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link PropertySet}.
   */
  public final PropertySet getAllUserProperties() {
    return this.property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserID (UserProfile)
  /**
   ** Sets the unique identifier of the <code>IdentityProfile</code>.
   **
   ** @param  value              the unique identifier property of the
   **                            <code>IdentityProfile</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setUserID(final String value) {
    uniqueIdentifier(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserID (UserProfile)
  /**
   ** Returns the unique identifier of the <code>IdentityProfile</code>.
   **
   ** @return                    the unique identifier property of the
   **                            <code>IdentityProfile</code> to set.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getUserID() {
    return getUniqueName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword (UserProfile)
  /**
   ** Sets the <code>password</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>password</code> property of the
   **                            <code>IdentityProfile</code> to set.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   ** @param  confirmed          the <code>password</code> property to compare
   **                            <code>value</code> with before set the
   **                            <code>password</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   */
  @Override
  public final void setPassword(final char[] value, final char[] confirmed) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserName (UserProfile)
  /**
   ** Sets the <code>userName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>userName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setUserName(final String value) {
    uniqueIdentifier(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserName (UserProfile)
  /**
   ** Returns the <code>userName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>userName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getUserName() {
    return getUniqueName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTitle (UserProfile)
  /**
   ** Sets the <code>title</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>title</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setTitle(final String value) {
    property(UserProfile.TITLE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTitle (UserProfile)
  /**
   ** Returns the <code>title</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>title</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getTitle() {
    return stringValue(UserProfile.TITLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInitials (UserProfile)
  /**
   ** Sets the <code>initials</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>initials</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setInitials(final String value) {
    property(UserProfile.INITIALS, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInitials (UserProfile)
  /**
   ** Returns the <code>initials</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>initials</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getInitials() {
    return stringValue(UserProfile.INITIALS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLastName (UserProfile)
  /**
   ** Sets the <code>lastName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>lastName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setLastName(final String value) {
    property(UserProfile.LAST_NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLastName (UserProfile)
  /**
   ** Returns the <code>lastName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>lastName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getLastName() {
    return stringValue(UserProfile.LAST_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFirstName (UserProfile)
  /**
   ** Sets the <code>firstName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>firstName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setFirstName(final String value) {
    property(UserProfile.FIRST_NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFirstName (UserProfile)
  /**
   ** Returns the <code>firstName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>firstName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getFirstName() {
    return stringValue(UserProfile.FIRST_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setGivenName (UserProfile)
  /**
   ** Sets the <code>givenName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>givenName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setGivenName(final String value) {
    property(UserProfile.FIRST_NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGivenName (UserProfile)
  /**
   ** Returns the <code>givenName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>givenName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getGivenName() {
    return stringValue(UserProfile.FIRST_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMiddleName (UserProfile)
  /**
   ** Sets the <code>middleName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>middleName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setMiddleName(final String value) {
    property(UserProfile.MIDDLE_NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMiddleName (UserProfile)
  /**
   ** Returns the <code>middleName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>middleName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getMiddleName() {
    return stringValue(UserProfile.MIDDLE_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaidenName (UserProfile)
  /**
   ** Sets the <code>maidenName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>maidenName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setMaidenName(final String value) {
    property(UserProfile.MAIDEN_NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaidenName (UserProfile)
  /**
   ** Returns the <code>maidenName</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>maidenName</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getMaidenName() {
    return stringValue(UserProfile.MAIDEN_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNameSuffix (UserProfile)
  /**
   ** Sets the <code>nameSuffix</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>nameSuffix</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setNameSuffix(final String value) {
    property(UserProfile.NAME_SUFFIX, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNameSuffix (UserProfile)
  /**
   ** Returns the <code>nameSuffix</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>nameSuffix</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getNameSuffix() {
    return stringValue(UserProfile.NAME_SUFFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOrganization (UserProfile)
  /**
   ** Sets the <code>organization</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>organization</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setOrganization(final String value) {
    property(UserProfile.ORGANIZATION, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOrganization (UserProfile)
  /**
   ** Returns the <code>organization</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>organization</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getOrganization() {
    return stringValue(UserProfile.ORGANIZATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOrganizationalUnit (UserProfile)
  /**
   ** Sets the <code>organizationUnit</code> property of the business profile
   ** for this <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>organizationUnit</code> property of
   **                            the business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setOrganizationalUnit(final String value) {
    property(UserProfile.ORGANIZATIONAL_UNIT, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOrganiztionalUnit (UserProfile)
  /**
   ** Returns the <code>organizationUnit</code> property of the business profile
   ** for this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>organizationUnit</code> property of
   **                            the business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  // stupid typo in the interface
  public final String getOrganiztionalUnit() {
    return stringValue(UserProfile.ORGANIZATIONAL_UNIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDepartment (UserProfile)
  /**
   ** Sets the <code>department</code> property of the business profile for this
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>department</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDepartment(final String value) {
    property(UserProfile.DEPARTMENT, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDepartment (UserProfile)
  /**
   ** Returns the <code>department</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>department</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDepartment() {
    return stringValue(UserProfile.DEPARTMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDepartmentNumber (UserProfile)
  /**
   ** Sets the <code>departmentNumber</code> property of the business profile
   ** for this <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>departmentNumber</code> property of
   **                            the business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDepartmentNumber(final String value) {
    property(UserProfile.DEPARTMENT, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDepartmentNumber (UserProfile)
  /**
   ** Returns the <code>departmentNumber</code> property of the business profile
   ** for this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>departmentNumber</code> property of
   **                            the business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDepartmentNumber() {
    return stringValue(UserProfile.DEPARTMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setManager (UserProfile)
  /**
   ** Sets the <code>manager</code> property of the business profile for this
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>manager</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setManager(final String value) {
    property(UserProfile.MANAGER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManager (UserProfile)
  /**
   ** Returns the <code>manager</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>manager</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getManager() {
    return stringValue(UserProfile.MANAGER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEmployeeType (UserProfile)
  /**
   ** Sets the <code>employeeType</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>employeeType</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setEmployeeType(final String value) {
    property(UserProfile.EMPLOYEE_TYPE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEmployeeType (UserProfile)
  /**
   ** Returns the <code>employeeType</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>employeeType</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getEmployeeType() {
    return stringValue(UserProfile.EMPLOYEE_TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEmployeeNumber (UserProfile)
  /**
   ** Sets the <code>employeeNumber</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>employeeNumber</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setEmployeeNumber(final String value) {
    property(UserProfile.EMPLOYEE_NUMBER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEmployeeNumber (UserProfile)
  /**
   ** Returns the <code>employeeNumber</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>employeeNumber</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getEmployeeNumber() {
    return stringValue(UserProfile.EMPLOYEE_NUMBER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPreferredLanguage (UserProfile)
  /**
   ** Sets the <code>preferredLanguage</code> property of the business profile
   ** for this <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>preferredLanguage</code> property of
   **                            the business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setPreferredLanguage(final String value) {
    property(UserProfile.PREFERRED_LANGUAGE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredLanguage (UserProfile)
  /**
   ** Returns the <code>preferredLanguage</code> property of the business
   ** profile for this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>preferredLanguage</code> property of
   **                            the business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getPreferredLanguage() {
    return stringValue(UserProfile.PREFERRED_LANGUAGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDateofHire (UserProfile)
  /**
   ** Sets the <code>dateOfHire</code> property for this
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>dateOfHire</code> property for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDateofHire(final String value) {
    property(UserProfile.DATE_OF_HIRE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDateofHire (UserProfile)
  /**
   ** Returns the <code>dateOfHire</code> property for this
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>dateOfHire</code> property for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDateofHire() {
    return stringValue(UserProfile.DATE_OF_HIRE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDateofBirth (UserProfile)
  /**
   ** Sets the <code>dateOfBirth</code> property for this
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>dateOfBirth</code> property for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDateofBirth(final String value) {
    property(UserProfile.DATE_OF_BIRTH, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDateofBirth (UserProfile)
  /**
   ** Returns the <code>dateOfBirth</code> property for this
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>dateOfBirth</code> property for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDateofBirth() {
    return stringValue(UserProfile.DATE_OF_BIRTH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeZone (UserProfile)
  /**
   ** Sets the <code>timeZone</code> property of the business profile for this
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>timeZone</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setTimeZone(final String value) {
    property(UserProfile.TIME_ZONE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTimeZone (UserProfile)
  /**
   ** Returns the <code>timeZone</code> property of the business profile for
   ** this <code>IdentityProfile</code>.
   **
   ** @return                    the <code>timeZone</code> property of the
   **                            business profile for this
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getTimeZone() {
    return stringValue(UserProfile.TIME_ZONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessEmail (UserProfile)
  /**
   ** Sets the e-Mail Address belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the e-Mail Address belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessEmail(final String value) {
    property(UserProfile.BUSINESS_EMAIL, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessEmail (UserProfile)
  /**
   ** Returns the e-Mail Address belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the e-Mail Address belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessEmail() {
    return stringValue(UserProfile.BUSINESS_EMAIL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessPhone (UserProfile)
  /**
   ** Sets the telephone number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the telephone number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessPhone(final String value) {
    property(UserProfile.BUSINESS_PHONE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessPhone (UserProfile)
  /**
   ** Returns the telephone number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the telephone number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessPhone() {
    return stringValue(UserProfile.BUSINESS_PHONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessFax (UserProfile)
  /**
   ** Sets the facsimile number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the facsimile number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessFax(final String value) {
    property(UserProfile.BUSINESS_FAX, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessFax (UserProfile)
  /**
   ** Returns the facsimile number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the facsimile number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessFax() {
    return stringValue(UserProfile.BUSINESS_FAX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessPager (UserProfile)
  /**
   ** Sets the pager number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the pager number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessPager(final String value) {
    property(UserProfile.BUSINESS_PAGER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessPager (UserProfile)
  /**
   ** Returns the pager number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the pager number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessPager() {
    return stringValue(UserProfile.BUSINESS_PAGER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessMobile (UserProfile)
  /**
   ** Sets the mobile number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the mobile number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessMobile(final String value) {
    property(UserProfile.BUSINESS_MOBILE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessMobile (UserProfile)
  /**
   ** Returns the mobile number belonging to the business profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the mobile number belonging to the business
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessMobile() {
    return stringValue(UserProfile.BUSINESS_MOBILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessCountry (UserProfile)
  /**
   ** Sets the country belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the country belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessCountry(final String value) {
    property(UserProfile.BUSINESS_COUNTRY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessCountry (UserProfile)
  /**
   ** Returns the country belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the country belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessCountry() {
    return stringValue(UserProfile.BUSINESS_COUNTRY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessCity (UserProfile)
  /**
   ** Sets the city belonging to the business address profile property of
   ** the <code>IdentityProfile</code>..
   **
   ** @param  value              the city belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessCity(final String value) {
    property(UserProfile.BUSINESS_CITY, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessCity (UserProfile)
  /**
   ** Returns the city belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the city belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessCity() {
    return stringValue(UserProfile.BUSINESS_CITY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessState (UserProfile)
  /**
   ** Sets the state belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the state belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessState(final String value) {
    property(UserProfile.BUSINESS_STATE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessState (UserProfile)
  /**
   ** Returns the state belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the state belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessState() {
    return stringValue(UserProfile.BUSINESS_STATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessPOBox (UserProfile)
  /**
   ** Sets the postal box belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the postal box belonging to the business
   **                            address profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessPOBox(final String value) {
    property(UserProfile.BUSINESS_PO_BOX, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessPOBox (UserProfile)
  /**
   ** Returns the postal box belonging to the business address profile property
   ** of the <code>IdentityProfile</code>.
   **
   ** @return                    the postal box belonging to the business
   **                            address profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessPOBox() {
    return stringValue(UserProfile.BUSINESS_PO_BOX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessPostalCode (UserProfile)
  /**
   ** Sets the postal code belonging to the business address profile
   ** property of the <code>IdentityProfile</code>.
   **
   ** @param  value              the postal code belonging to the business
   **                            address profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessPostalCode(final String value) {
    property(UserProfile.BUSINESS_POSTAL_CODE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessPostalCode (UserProfile)
  /**
   ** Returns the postal code belonging to the business address profile
   ** property of the <code>IdentityProfile</code>.
   **
   ** @return                    the postal code belonging to the business
   **                            address profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessPostalCode() {
    return stringValue(UserProfile.BUSINESS_POSTAL_CODE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessStreet (UserProfile)
  /**
   ** Sets the street belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the street belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessStreet(final String value) {
    property(UserProfile.BUSINESS_STREET, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessStreet (UserProfile)
  /**
   ** Returns the street belonging to the business address profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @return                    the street belonging to the business address
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessStreet() {
    return stringValue(UserProfile.BUSINESS_STREET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBusinessPostalAddr (UserProfile)
  /**
   ** Sets the full qualified address belonging to the business profile
   ** property of the <code>IdentityProfile</code>.
   **
   ** @param  value              the full qualified address belonging to the
   **                            business profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setBusinessPostalAddr(final String value) {
    property(UserProfile.BUSINESS_POSTAL_ADDR, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBusinessPostalAddr (UserProfile)
  /**
   ** Returns the full qualified address belonging to the business profile
   ** property of the <code>IdentityProfile</code>.
   **
   ** @return                    the full qualified address belonging to the
   **                            business profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getBusinessPostalAddr() {
    return stringValue(UserProfile.BUSINESS_POSTAL_ADDR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHomePhone (UserProfile)
  /**
   ** Sets the telephone number belonging to the home profile property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the telephone number belonging to the home
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setHomePhone(final String value) {
    property(UserProfile.HOME_PHONE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHomePhone (UserProfile)
  /**
   ** Returns the telephone number belonging to the home profile property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the telephone number belonging to the home
   **                            profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getHomePhone() {
    return stringValue(UserProfile.HOME_PHONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHomeAddress (UserProfile)
  /**
   ** Sets the full qualified address belonging to the home profile property of
   ** the <code>IdentityProfile</code>.
   **
   ** @param  value              the full qualified address belonging to the
   **                            home profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setHomeAddress(final String value) {
    property(UserProfile.HOME_ADDRESS, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHomeAddress (UserProfile)
  /**
   ** Returns the full qualified address belonging to the home profile property
   ** of the <code>IdentityProfile</code>.
   **
   ** @return                    the full qualified address belonging to the
   **                            home profile property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getHomeAddress() {
    return stringValue(UserProfile.HOME_ADDRESS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription (UserProfile)
  /**
   ** Sets the <code>description</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>description</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDescription(final String value) {
    property(UserProfile.DESCRIPTION, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (UserProfile)
  /**
   ** Returns the <code>description</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>description</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDescription() {
    return stringValue(UserProfile.DESCRIPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDefaultGroup (UserProfile)
  /**
   ** Sets the <code>defaultGroup</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>defaultGroup</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDefaultGroup(final String value) {
    property(UserProfile.DEFAULT_GROUP, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultGroup (UserProfile)
  /**
   ** Returns the <code>defaultGroup</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>defaultGroup</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDefaultGroup() {
    return stringValue(UserProfile.DEFAULT_GROUP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUIAccessMode (UserProfile)
  /**
   ** Sets the <code>uiAccessMode</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>uiAccessMode</code> property of
   **                            the <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setUIAccessMode(final String value) {
    property(UserProfile.UI_ACCESS_MODE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUIAccessMode (UserProfile)
  /**
   ** Returns the <code>uiAccessMode</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>uiAccessMode</code> property of
   **                            the <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getUIAccessMode() {
    return stringValue(UserProfile.UI_ACCESS_MODE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setWirelessAcctNumber (UserProfile)
  /**
   ** Sets the <code>wirelessAccount</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the <code>wirelessAccount</code> property of
   **                            the <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setWirelessAcctNumber(final String value) {
    property(UserProfile.WIRELESS_ACCT_NUMBER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getWirelessAcctNumber (UserProfile)
  /**
   ** Returns the <code>wirelessAccount</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>wirelessAccount</code> property of
   **                            the <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getWirelessAcctNumber() {
    return stringValue(UserProfile.WIRELESS_ACCT_NUMBER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setJPEGPhoto (UserProfile)
  /**
   ** Sets the path to the <code>photo</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the path to the <code>photo</code> property of
   **                            the <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setJPEGPhoto(final String value) {
    property(UserProfile.JPEG_PHOTO, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getJPEGPhoto (UserProfile)
  /**
   ** Returns the <code>photo</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>photo</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  @Override
  public final byte[] getJPEGPhoto() {
    return new byte[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserCertificate (UserProfile)
  /**
   ** Sets the path to the <code>photo</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @param  value              the path to the <code>photo</code> property of
   **                            the <code>IdentityProfile</code>.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   */
  @Override
  public final void setUserCertificate(final byte[] value) {
    property(UserProfile.USER_CERTIFICATE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserCertificate (UserProfile)
  /**
   ** Returns the <code>certificate</code> property of the
   ** <code>IdentityProfile</code>.
   **
   ** @return                    the <code>certificate</code> property of the
   **                            <code>IdentityProfile</code>.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  @Override
  public final byte[] getUserCertificate() {
    return new byte[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManagementChain (UserProfile)
  /**
   ** Returns management chain heirarchy of this <code>IdentityProfile</code>.
   ** <br>
   ** Search results are reported up to the heirarchy, where any one of the
   ** three specified conditions are met or management chain ends.
   **
   ** @param  level              search management chain heirarchy only up to
   **                            supplied level.
   **                            <br>
   **                            Complete management heirarchy search is done if
   **                            <code>-ve</code> value is supplied.
   ** @param  upToName           search management chain heirarchy up to the
   **                            supplied manager's name
   **                            ({@link UserProfile#NAME}); this criteria is
   **                            ignored from search if <code>null</code> value
   **                            is supplied.          
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  upToTitle          search management chain heirarchy up to the
   **                            supplied title; this criteria is ignored from
   **                            search if <code>null</code> value is supplied.          
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collection containing management chain upto
   **                            and including the teminating identity.
   **                            <br>
   **                            Possible object is {@link List}.
   */
  @Override
  public final List getManagementChain(final int level, final String upToName, final String upToTitle) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getReportees (UserProfile)
  /**
   ** Returns the collection of reportees of this <code>IdentityProfile</code>.
   **
   ** @param  direct             <code>true</code> if all the direct reportees
   **                            are returned only.
   **                            <br>
   **                            <code>false</code> if both the direct and
   **                            indirect reportees are be returned.
   **
   ** @return                    the {@link SearchResponse} instance containing
   **                            all the reportees matching the
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Each result will be instance of User class.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  @Override
  public final SearchResponse getReportees(final boolean direct) {
    final SearchResponse result = null;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProperty (UserProfile)
  /**
   ** Sets a property mapped by the specified <code>name</code>.
   **
   ** @param  value              the the property to modify.
   **                            <br>
   **                            Allowed object is {@link ModProperty}.
   */
  @Override
  public final void setProperty(final ModProperty value) {
    this.property.put(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProperties (UserProfile)
  /**
   ** Adds a collection of properties.
   **
   ** @param  value              the the property to modify.
   **                            <br>
   **                            Allowed object is array of {@link ModProperty}.
   */
  @Override
  public final void setProperties(final ModProperty[] value) {
    for (ModProperty cursor : value)
      this.property.put(cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPropertyVal (UserProfile)
  /**
   ** Returns a property value mapped by the specified <code>name</code>.
   **
   ** @param  name               the name of the property to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Property} mapped at
   **                            <code>name</code> in the embedded property set
   **                            or <code>null</code> if there is no
   **                            {@link Property} mapped at name or the value
   **                            type is not compatible with the
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  @Override
  public final Object getPropertyVal(final String name) {
    return propertyValue(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a minimal <code>IdentityProfile</code> database
   ** resource.
   **
   ** @param  identifier         the primary identifier of an
   **                            <code>IdentityProfile</code> database resource.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>IdentityProfile</code> created.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityProfile</code>.
   */
  public static IdentityProfile of(final Long identifier) {
    return new IdentityProfile(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a minimal <code>IdentityProfile</code> database
   ** resource.
   **
   ** @param  identifier         the unique identifier of an
   **                            <code>IdentityProfile</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>IdentityProfile</code> created.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityProfile</code>.
   */
  public static IdentityProfile of(final String identifier) {
    return new IdentityProfile(identifier);
  }
}