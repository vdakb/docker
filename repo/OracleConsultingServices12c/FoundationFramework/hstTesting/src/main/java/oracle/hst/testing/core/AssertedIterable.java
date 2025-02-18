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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared junit testing functions

    File        :   AssertedIterable.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertedIterable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.testing.core;

import java.util.Iterator;

////////////////////////////////////////////////////////////////////////////////
// class AssertedIterable
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Assertions for {@link Iterator}.
 ** <p>
 ** To create an instance of this class, invoke
 ** {@link Assertions#assertThat(Iterable)}.
 * </p>
 **
 ** @param  <E>                  the type of the iteratable elements the
 **                              {@link java.util.Iterable} provides.
 **                              <br>
 **                              Allowed object is <code>&lt;E<&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AssertedIterable {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AssertedIterable</code> with the <code>actual</code>.
   **
   ** @param  actual             the actual value to kept.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   */
  AssertedIterable() {
    // ensure inheritance
    super();
  }
}
