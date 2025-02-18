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

    System      :   Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ResourceIterator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceIterator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.1.0.0     2014-11-29  DSteding    First release version
*/

package oracle.hst.deployment.mds.type;

import java.util.Iterator;

//////////////////////////////////////////////////////////////////////////////
// abstract class ResourceIterator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Helper class, to iterate over nested <code>ResourceCollection</code> values.
  **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
abstract class ResourceIterator implements Iterator<String> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceIterator</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  ResourceIterator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Iterator)
  /**
   ** Removes from the underlying collection the last element returned by the
   ** iterator (optional operation).
   ** <br>
   ** This method can be called only once per call to <code>next</code>. The
   ** behavior of an iterator is unspecified if the underlying collection is
   ** modified while the iteration is in progress in any way other than by
   ** calling this method.
   **
   ** @throws UnsupportedOperationException if the <code>remove</code>
   **                                       operation is not supported by this
   **                                       {@link Iterator}.
   ** @throws IllegalStateException         if the <code>next</code> method
   **                                       has not yet been called, or the
   **                                       <code>remove</code> method has
   **                                       already been called after the last
   **                                       call to the <code>next</code>
   **                                       method.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}