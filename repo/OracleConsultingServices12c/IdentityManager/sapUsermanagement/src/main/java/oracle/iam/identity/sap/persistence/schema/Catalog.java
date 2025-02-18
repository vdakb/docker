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
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   LookupClass.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    LookupClass.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.persistence.schema;

////////////////////////////////////////////////////////////////////////////////
// enum Catalog
// ~~~~ ~~~~~~~
/**
 ** Catalog class for lookup.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public enum Catalog {
    /**
     ** a <code>Catalog Definition</code> provides information about allowed
     ** input values regarding System Landscape Recipient information in a
     ** SAP/R3 System.
     */
    SYSTEM("System",                          "RFC_READ_TABLE", "USZBVLNDRC", "RCVSYSTEM", "RCVSYSTEM"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding activity groups in a SAP/R3 System.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name USRSYSACT or data element XUFLAG into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  ROLE("Role",                              "BAPI_HELPVALUES_GET", "GETDETAIL", "ACTIVITYGROUPS", "AGR_NAME", "AGR_NAME", "TEXT", "", "", "SUBSYSTEM", "USRSYSACT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding profiles in a SAP/R3 System.
   ** <p>
   ** All this information and more can be viewed by entering the Search help
   ** name ADDR2_SH_TITLE into the relevant SAP transactions such as SE11 and
   ** SE80.
   */
  TITLE("Title",                            "BAPI_HELPVALUES_GET", "GETDETAIL", "ADDRESS",        "TITLE_P", "TITLE_MEDI", "TITLE_MEDI", "ADDR2_SH_TITLE", "SH", "PERSON", "I", "EQ", "X"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding profiles in a SAP/R3 System.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name USRSYSPRF or data element XUNUMBER into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  PROFILE("Profile",                        "BAPI_HELPVALUES_GET", "GETDETAIL", "PROFILES",       "BAPIPROF", "PROFN", "PTEXT", "SUBSYSTEM", "USRSYSPRF"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding company in a SAP/R3 System.
   ** <p>
   ** All this information and more can be viewed by entering the Search help
   ** name USCOMPANY_ADDR into the relevant SAP transactions such as SE11 and
   ** SE80.
   */
  COMPANY("Company",                        "BAPI_HELPVALUES_GET", "GETDETAIL", "COMPANY",        "COMPANY", "COMPANY", "COMPANY", "USCOMPANY_ADDR", "SH"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding customers regional zones information in a
   ** SAP/R3 System.
   ** <p>
   ** You can also view this information if you enter the Table name TZONE
   ** into the relevant SAP transaction such as SE11 or SE80. You can also
   ** view the data contained in this database table via these transactions or
   ** alternatively use transaction SE16.
   */
  TIMEZONE("TimeZone",                      "BAPI_HELPVALUES_GET", "CHANGE",    "ADDRESS",        "TIME_ZONE", "TZONE", "DESCRIPT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding user types information in a SAP/R3 System.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name USR02 or data element XUUSTYP into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  USERTYPE("UserType",                      "BAPI_HELPVALUES_GET", "GETDETAIL", "LOGONDATA",      "USTYP", "_LOW", "_TEXT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding user groups information in a SAP/R3 System.
   ** <p>
   ** You can also view this information if you enter the Table name USGRP
   ** into the relevant SAP transaction such as SE11 or SE80. You can also
   ** view the data contained in this database table via these transactions or
   ** alternatively use transaction SE16.
   */
  USERGROUP("UserGroup",                    "BAPI_HELPVALUES_GET", "GETDETAIL", "GROUPS",         "USERGROUP", "USERGROUP", "TEXT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding user parameter transfer structure information
   ** in a SAP/R3 System.
   ** <p>
   ** You can also view this information if you enter the Structure name
   ** USPARAM into the relevant SAP transaction such as SE11 or SE80.
   */
  PARAMETER("Parameter",                    "BAPI_HELPVALUES_GET", "GETDETAIL", "PARAMETER",      "PARID", "PARAMID", "PARTEXT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding date format information in a SAP/R3 System.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name USR01 or data element XUDATFM into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  DATEFORMAT("DateFormat",                   "BAPI_HELPVALUES_GET", "GETDETAIL", "DEFAULTS",       "DATFM", "_LOW", "_TEXT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding language key information in a SAP/R3 System.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name ADDR3_DATA or data element SPRAS into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  LOGONLANGUAGE("LogonLanguage",             "BAPI_HELPVALUES_GET", "GETDETAIL", "ADDRESS",        "LANGU_P", "SPRAS", "SPTXT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding decimal format information in a SAP/R3 System.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name USR01 or data element XUDCPFM into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  DECIMALNOTATION("DecimalNotation",         "BAPI_HELPVALUES_GET", "GETDETAIL", "DEFAULTS",       "DCPFM", "_LOW", "_TEXT"),

  /**
   ** a <code>Catalog Definition</code> provides information about allowed
   ** input values regarding communication type information in a SAP/R3
   ** System.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name ADRCOMCS2 or data element AD_COMM into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  COMMUNICATIONTYPE("CommunicationType",     "BAPI_HELPVALUES_GET", "GETDETAIL", "ADDRESS",        "COMM_TYPE", "COMM_TYPE", "COMM_TEXT"),

  /**
   ** a <code>Catalog Definition</code> provides information aboutallowed
   ** input values regarding contractual user type information.
   ** <p>
   ** You could also view this information on your SAP system if you enter the
   ** table name USR06 or data element USERTYPEN into the relevant SAP
   ** transaction such as SE11 or SE80.
   */
  CONTRACTUALUSERTYPE("ContractualUserType", "BAPI_HELPVALUES_GET", "GETDETAIL", "UCLASSSYS",       "LIC_TYPE", "USERTYP", "UTYPTEXT", "LANGU", "I", "EQ", "EN")

  , CUA_SYSTEM           ("cuaSystem",       "RFC_READ_TABLE",      "USZBVLNDRC", "RCVSYSTEM", "RCVSYSTEM")
  , CUA_ROLE             ("cuaRole",         "RFC_READ_TABLE",      "USRSYSACTT", "AGR_NAME", "TEXT", "SUBSYSTEM", "USRSYSACT", "LANGU = 'EN'")
  , CUA_PROFILE          ("cuaProfile",      "RFC_READ_TABLE",      "USRSYSPRFT", "PROFN", "PTEXT", "SUBSYSTEM", "USRSYSPRF", "LANGU = 'EN'")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ROLE_SINGLE    = "Role Single";
  public static final String ROLE_COMPOSITE = "Role Composite";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2345385999732898430")
  private static final long  serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String       type;
  private final String[]     detail;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupClass</code> that allows use as a JavaBean.
   **
   ** @param  type               the type of the catalog to access.
   ** @param  detail             the catalog details.
   */
  Catalog(final String type, final String... detail) {
    // initialize intance attributes
    this.type     = type;
    this.detail   = detail;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the value of the type property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detail
  /**
   ** Returns the value of the detail property.
   **
   ** @return                    possible object is array of {@link String}.
   */
  public String[] detail() {
    return this.detail;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromValue
  /**
   ** Factory method to create a proper Status from the given string value.
   **
   ** @param  value              the string value the status type should be
   **                            returned for.
   **
   ** @return                    the status type.
   */
  public static Catalog fromValue(final String value) {
    for (Catalog c : Catalog.values()) {
      if (c.type.equals(value)) {
        return c;
      }
    }
    throw new IllegalArgumentException(value.toString());
  }
}