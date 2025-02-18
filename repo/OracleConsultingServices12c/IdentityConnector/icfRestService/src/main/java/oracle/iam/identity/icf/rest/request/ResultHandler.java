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
    Subsystem   :   Generic REST Library

    File        :   ResultHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ResultHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.request;

////////////////////////////////////////////////////////////////////////////////
// interface ResultHandler
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** An interface for handling a REST request response.
 ** <p>
 ** Methods will be called in the order they are received.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ResultHandler<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Handle the startIndex in the search response.
   **
   ** @param  value              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void start(final int value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Return the startIndex value.
   **
   ** @return value              the 1-based index of the first query result.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int start();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items
  /**
   ** Handle the desired maximum number of query results per page.
   **
   ** @param  value              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void items(final int value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items
  /**
   ** Return the desired maximum number of query results per page.
   **
   ** @return value              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int items();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Handle the total number of results returned by the list or query
   ** operation.
   **
   ** @param  value              the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void total(final int value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Return the total number of results returned by the list or query
   ** operation.
   **
   ** @return value              the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int total();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Handle a search result resource.
   **
   ** @param  resource           a search result resource.
   **
   ** @return                    <code>true</code> to continue processing the
   **                            search result response or <code>false</code> to
   **                            immediate stop further processing of the
   **                            response.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean resource(final T resource);
}