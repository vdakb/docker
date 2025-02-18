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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface User
// ~~~~~~~~~ ~~~~
/**
 ** The <code>User</code> declares the usefull constants to deal with
 ** <code>Oracle Identity Manager User</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface User extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard object name for users. */
  static final String RESOURCE                = "Xellerate User";

  /** Standard prefix name for users. */
  static final String PREFIX                  = "Users.";

  /** Standard object key for users. */
  static final String DEFAULT_KEY             = "1";

  /** Standard object organization for users. */
  static final String DEFAULT_ORGANIZATION    = "Xellerate Users";

  /** Standard object type for users. */
  static final String DEFAULT_ROLE            = "Full-Time";

  /** Standard object type for users. */
  static final String DEFAULT_TYPE            = "End-User";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** login name should be mapped
   */
  static final String FIELD_USERID            = "User ID";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** password/identity should be mapped
   */
  static final String FIELD_PASSWORD          = "Password";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** password/identity should be mapped
   */
  static final String FIELD_IDENTITY          = "Identity";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** last name should be mapped
   */
  static final String FIELD_LASTNAME          = "Last Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** first name should be mapped
   */
  static final String FIELD_FIRSTNAME         = "First Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** middel name should be mapped
   */
  static final String FIELD_MIDDLENAME        = "Middle Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** common name should be mapped
   */
  static final String FIELD_COMMONNAME        = "Common Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** display name should be mapped
   */
  static final String FIELD_DISPLAYNAME       = "Display Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** full name should be mapped
   */
  static final String FIELD_FULLNAME          = "Full Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** manager user key should be mapped
   */
  static final String FIELD_MANAGER_KEY       = "Manager Key";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** manager login name should be mapped
   */
  static final String FIELD_MANAGER_LOGIN     = "Manager Login";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** manager full name should be mapped
   */
  static final String FIELD_MANAGER_FULLNAME  = "Manager Full Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** manager login name should be mapped
   */
  static final String FIELD_MANAGER_ORG       = "Manager Organization";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** lock status should be mapped
   */
  static final String FIELD_LOCKED            = "Lock User";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** manual lock status should be mapped
   */
  static final String FIELD_MANUALLE_LOCKED   = "Manually Locked";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** enabled status should be mapped
   */
  static final String FIELD_DISABLED          = "Disable User";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** role should be mapped
   */
  static final String FIELD_ROLE              = "Role";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** type should be mapped
   */
  static final String FIELD_TYPE              = "Xellerate Type";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** email address should be mapped
   */
  static final String FIELD_EMAIL             = "Email";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** phone number should be mapped
   */
  static final String FIELD_PHONE             = "Telephone Number";
  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** facsimile number should be mapped
   */
  static final String FIELD_FAX               = "Fax";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** mobile number should be mapped
   */
  static final String FIELD_MOBILE            = "Mobile";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** pager should be mapped
   */
  static final String FIELD_PAGER             = "Pager";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** state should be mapped
   */
  static final String FIELD_STATE             = "State";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** post office box should be mapped
   */
  static final String FIELD_POBOX             = "PO Box";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** street should be mapped
   */
  static final String FIELD_STREET            = "Street";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** postal code should be mapped
   */
  static final String FIELD_POSTAL_CODE       = "Postal Code";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** street should be mapped
   */
  static final String FIELD_POSTAL_ADDRESS    = "Postal Address";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** locale should be mapped
   */
  static final String FIELD_LOCALE            = "usr_local";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** time zone should be mapped
   */
  static final String FIELD_TIMEZONE          = "Time Zone";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** country should be mapped
   */
  static final String FIELD_COUNTRY           = "Country";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** locality name should be mapped
   */
  static final String FIELD_LOCALITY          = "Locality Name";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** currency should be mapped
   */
  static final String FIELD_CURRENCY          = "Currency";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** number format should be mapped
   */
  static final String FIELD_NUMBER_FORMAT     = "Number Format";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** date format should be mapped
   */
  static final String FIELD_DATE_FORMAT       = "Date Format";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** time format should be mapped
   */
  static final String FIELD_TIME_FORMAT       = "Time Format";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** employee number should be mapped
   */
  static final String FIELD_EMPLOYEE_NUMBER   = "Employee Number";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** department number should be mapped
   */
  static final String FIELD_DEPARTMENT_NUMBER = "Department Number";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** territory should be mapped
   */
  static final String FIELD_TERRITORY         = "FA Territory";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** language should be mapped
   */
  static final String FIELD_LANGUAGE          = "FA Language";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** password expiration warning date should be mapped
   */
  static final String FIELD_PASSWORD_WARNING  = "Password Warning Date";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** password expiration date should be mapped
   */
  static final String FIELD_PASSWORD_EXPIRE   = "Password Expire Date";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** password generated state should be mapped
   */
  static final String FIELD_PASSWORD_GENERATED = "Password Generated";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** start date should be mapped
   */
  static final String FIELD_STARTDATE          = "Start Date";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** end date should be mapped
   */
  static final String FIELD_ENDDATE            = "End Date";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** provisioning date should be mapped
   */
  static final String FIELD_PROVISIONING       = "Provisioning Date";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** provisioning date should be mapped
   */
  static final String FIELD_PROVISIONED        = "Provisioned Date";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** deprovisioning date should be mapped
   */
  static final String FIELD_DEPROVISIONING     = "Deprovisioning Date";

  /**
   ** the reconciliation key contained in a collection to specify that the users
   ** deprovisioned date should be mapped
   */
  static final String FIELD_DEPROVISIONED      = "Deprovisioned Date";

  /**
   ** the mapping key contained in a collection to specify that the users system
   ** key should be resolved
   */
  static final String KEY                      = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the users system
   ** rowversion should be resolved
   */
  static final String ROWVERSION               = PREFIX + FIELD_VERSION;

  /**
   ** the mapping key contained in a collection to specify that the users login
   ** name should be resolved
   */
  static final String USERID                   = PREFIX + FIELD_USERID;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** password should be resolved
   */
  static final String PASSWORD                 = PREFIX + FIELD_PASSWORD;

  /**
   ** the mapping key contained in a collection to specify that the users lock
   ** status should be resolved
   */
  static final String LOCKED                   = PREFIX + FIELD_LOCKED;

  /**
   ** the mapping key contained in a collection to specify that the users manual
   ** lock status should be resolved
   */
  static final String MANUALLY_LOCKED          = PREFIX + FIELD_MANUALLE_LOCKED;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** generell status should be resolved
   */
  static final String STATUS                   = PREFIX + FIELD_STATUS;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** enabled status should be resolved
   */
  static final String DISABLED                 = PREFIX + FIELD_DISABLED;

  /**
   ** the mapping key contained in a collection to specify that the users last
   ** name should be resolved
   */
  static final String LASTNAME                 = PREFIX + FIELD_LASTNAME;

  /**
   ** the mapping key contained in a collection to specify that the users first
   ** name should be resolved
   */
  static final String FIRSTNAME                = PREFIX + FIELD_FIRSTNAME;

  /**
   ** the mapping key contained in a collection to specify that the users middle
   ** name should be resolved
   */
  static final String MIDDLENAME               = PREFIX + FIELD_MIDDLENAME;

  /**
   ** the mapping key contained in a collection to specify that the users common
   ** name should be resolved
   */
  static final String COMMONNAME               = PREFIX + FIELD_COMMONNAME;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** display name should be resolved
   */
  static final String DISPLAYNAME               = PREFIX + FIELD_DISPLAYNAME;

  /**
   ** the mapping key contained in a collection to specify that the users full
   ** name should be resolved
   */
  static final String FULLNAME                  = PREFIX + FIELD_FULLNAME;

  /**
   ** the mapping key contained in a collection to specify that the login name
   ** of the users manager should be resolved
   */
  static final String MANAGER_KEY               = PREFIX + FIELD_MANAGER_KEY;

  /**
   ** the mapping key contained in a collection to specify that the login name
   ** of the users manager should be resolved
   */
  static final String MANAGER_LOGIN             = PREFIX + FIELD_MANAGER_LOGIN;

  /**
   ** the mapping key contained in a collection to specify that the full name
   ** of the users manager should be resolved
   */
  static final String MANAGER_FULLNAME          = PREFIX + FIELD_MANAGER_FULLNAME;

  /**
   ** the mapping key contained in a collection to specify that the login name
   ** of the users manager organization should be resolved
   */
  static final String MANAGER_ORGANIZATION      = PREFIX + FIELD_MANAGER_ORG;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** role should be resolved
   */
  static final String ROLE                      = PREFIX + FIELD_ROLE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** OIM type should be resolved
   */
  static final String TYPE                      = PREFIX + FIELD_TYPE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** email address should be resolved
   */
  static final String EMAIL                     = PREFIX + FIELD_EMAIL;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** post office box should be resolved
   */
  static final String PBOX                      = PREFIX + FIELD_POBOX;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** street should be resolved
   */
  static final String STREET                    = PREFIX + FIELD_STREET;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** post office box should be resolved
   */
  static final String POSTAL_ADDRESS            = PREFIX + FIELD_POSTAL_ADDRESS;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** phone number should be resolved
   */
  static final String PHONE                     = FIELD_PHONE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** mobile number should be resolved
   */
  static final String MOBILE                    = FIELD_MOBILE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** pager number should be resolved
   */
  static final String PAGER                     = FIELD_PAGER;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** locale should be resolved
   */
  static final String LOCALE                    = PREFIX + FIELD_LOCALE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** timezone should be resolved
   */
  static final String TIMEZONE                  = PREFIX + FIELD_TIMEZONE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** territory should be resolved
   */
  static final String TERRITORY                 = PREFIX + FIELD_TERRITORY;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** country should be resolved
   */
  static final String COUNTRY                   = PREFIX + FIELD_COUNTRY;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** locality name should be resolved
   */
  static final String LOCALITY                  = PREFIX + FIELD_LOCALITY;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** currency should be resolved
   */
  static final String CURRENCY                  = PREFIX + FIELD_CURRENCY;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** number format should be resolved
   */
  static final String NUMBER_FORMAT             = PREFIX + FIELD_NUMBER_FORMAT;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** date format should be resolved
   */
  static final String DATE_FORMAT               = PREFIX + FIELD_DATE_FORMAT;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** employee number should be resolved
   */
  static final String EMPLOYEE_NUMBER           = PREFIX + FIELD_EMPLOYEE_NUMBER;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** department number should be resolved
   */
  static final String DEPARTMENT_NUMBER         = PREFIX + FIELD_DEPARTMENT_NUMBER;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** password warning date should be resolved
   */
  static final String PASSWORD_WARNING          = PREFIX + FIELD_PASSWORD_WARNING;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** start date should be resolved
   */
  static final String START                     = PREFIX + FIELD_STARTDATE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** start date should be resolved
   */
  static final String END                       = PREFIX + FIELD_ENDDATE;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** provisioning date should be resolved
   */
  static final String PROVISIONING              = PREFIX + FIELD_PROVISIONING;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** provisioned date should be resolved
   */
  static final String PROVISIONED               = PREFIX + FIELD_PROVISIONED;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** deprovisioning date should be resolved
   */
  static final String DEPROVISIONING            = PREFIX + FIELD_DEPROVISIONING;

  /**
   ** the mapping key contained in a collection to specify that the users
   ** deprovisioned date should be resolved
   */
  static final String DEPROVISIONED             = PREFIX + FIELD_DEPROVISIONED;
}