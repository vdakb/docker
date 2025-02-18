/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   RequestBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RequestBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.ots.service.request.RequestError;

////////////////////////////////////////////////////////////////////////////////
// class RequestBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class RequestBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { RequestError.METADAT_PATH_EMPTY,          "The path to the Metadata Descriptor specifying the Attribute Mapping cannot be null or empty" }
  , { RequestError.COMPOSITE_CONFIG_INCOMPLETE, "The composite configuration is imcomplete" }
  , { RequestError.COMPOSITE_NOT_FOUND,         "The composite [%1$s] does not exist on Oracle SOA server" }

  , { RequestError.REQUESTER,                   "Could not obtain requester login name from request id [%1$s] specified by identity key [%2$s]." }
  , { RequestError.PAYLOAD,                     "Error in creating payload details for process instance [%1$s] and process task [%2$s]" }
  , { RequestError.NOTE,                        "Error getting note details for process task %1$s" }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }
}