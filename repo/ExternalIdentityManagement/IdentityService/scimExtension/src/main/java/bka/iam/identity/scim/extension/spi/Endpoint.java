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

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   Endpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    Endpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-25-05  JLakic     First release version
*/
package bka.iam.identity.scim.extension.spi;
////////////////////////////////////////////////////////////////////////////////
// class Endpoint
// ~~~~~ ~~~~~~~~
/**
 ** A generic handler which is invoked to process http exchanges.
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the 1-based index of the first query result.
   */
  public static final String  START   = "startIndex";
  /**
   ** The property name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String  COUNT   = "count";
  /**
   ** The property name of the filter string used to request a subset of
   ** resources.
   */
  public static final String  FILTER  = "filter";
  /**
   ** The property name of the multi-valued list of strings indicating the names
   ** of resource attributes to return in the response overriding the set of
   ** attributes that would be returned by default.
   */
  public static final String  EMITTED = "attributes";
  /**
   ** The property name of the mulit-valued list of strings indicating the names
   ** of resource attributes to be removed from the default set of attributes to
   ** return.
   */
  public static final String  OMITTED = "excludedAttributes";
  /**
   ** The property name of the string indicating the order in which the sortBy
   ** parameter is applied.
   */
  public static final String  ORDER   = "sortOrder";
  /**
   ** The property name of the string indicating the attribute whose value shall
   ** be used to order the returned responses.
   */
  public static final String  SORT    = "sortBy";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Endpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Endpoint() {
    // ensure inheritance
    super();
  }
}
