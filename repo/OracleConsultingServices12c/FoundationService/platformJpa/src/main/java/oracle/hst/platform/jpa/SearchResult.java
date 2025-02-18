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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   SearchResult.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SearchResult.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
// class SearchResult
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A <code>SearchResult</code> can be sublist or the entire list of a
 ** collection of avalibale entities.
 ** <br>
 ** It allows gain information about the position of it in the containing entire
 ** collection.
 **
 ** @param  <E>                  the type of the entity implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SearchResult<E> extends ArrayList<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:100327445831778291")
  private static final long serialVersionUID = 133544236773780222L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Integer total;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SearchResult</code> with the specified total amount of
   ** available entities and batch pointiong out the current position of the
   ** result.
   **
   ** @param  total              the total amount of entities available.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public SearchResult(final Integer total) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.total = total;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Returns the estimated total number of entities in the result set.
   **
   ** @return                    the estimated total number of entities in the
   **                            result set.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer total() {
    return this.total;
  }
}