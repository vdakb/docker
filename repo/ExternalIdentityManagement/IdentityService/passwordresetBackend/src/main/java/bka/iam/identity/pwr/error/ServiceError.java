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

    File        :   ServiceError.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    ServiceError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.error;

/**
 * Defines the error cases to be localized later.
 */
public interface ServiceError {

  public static String PREFIX                          = "PWR-";
  public static String LOGIN_FAILED                    = PREFIX + "00001";
  public static String PERSIST_NOT_SUCCEEDED           = PREFIX + "00002";
  public static String PERSIST_ERROR                   = PREFIX + "00003";
  public static String NO_SUCH_USER                    = PREFIX + "00004";
  public static String USER_LOOKUP_ERROR               = PREFIX + "00005";
  public static String USER_FACADE_ERROR               = PREFIX + "00006";
  public static String NO_RESET_REQUEST                = PREFIX + "00007";
  public static String DATABASE_ERROR                  = PREFIX + "00008";
  public static String EMAIL_NOT_UNIQUE                = PREFIX + "00009";
  public static String NOTIFICATION_FAILED             = PREFIX + "00010";
  public static String NOTIFICATION_UNRESOLVED_DATA    = PREFIX + "00011";
  public static String NOTIFICATION_TEMPLATE_NOTFOUND  = PREFIX + "00012";
  public static String NOTIFICATION_TEMPLATE_AMBIGUOUS = PREFIX + "00013";
  public static String NOTIFICATION_RESOLVER_NOTFOUND  = PREFIX + "00014";
  public static String NOTIFICATION_IDENTITY_NOTFOUND  = PREFIX + "00015";
  public static String NOTIFICATION_EXCEPTION          = PREFIX + "00016";
  public static String NO_SUCH_PROPERTY                = PREFIX + "00017";
  public static String URL_FORMAT_ERROR                = PREFIX + "00018";
  public static String URI_SYNTAX                      = PREFIX + "00019";
  public static String LOGIN_EMAIL_MISMATCH            = PREFIX + "00020";
  public static String LOOKUP_OPERATION_ERROR          = PREFIX + "00021";

}
