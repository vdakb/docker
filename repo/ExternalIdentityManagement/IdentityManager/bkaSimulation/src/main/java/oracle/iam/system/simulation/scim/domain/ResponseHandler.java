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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   ResponseHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ResponseHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.domain;

import com.fasterxml.jackson.databind.node.ObjectNode;

////////////////////////////////////////////////////////////////////////////////
// interface ResponseHandler
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** An interface for handling the request response.
 ** <p>
 ** Methods will be called in the order they are received.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ResponseHandler<T> {

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
   */
  boolean resource(final T resource);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extension
  /**
   ** Handle an schema extension in the search response.
   **
   ** @param  namespace          the namespace URN of the extension schema.
   **
   ** @param  extension          the {@link ObjectNode} representing the
   **                            extension schema.
   */
  void extension(final String namespace, final ObjectNode extension);
}