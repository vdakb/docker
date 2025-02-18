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

    File        :   AbstractAssertedObject.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractAssertedObject.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.testing.core;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractAssertedObject
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Base class for all implementations of assertions for {@link Object}s.
 **
 ** @param  <T>                  the "self" type of this assertion class.
 **                              Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 **                              for more details.
 **                              <br>
 **                              Allowed object is <code>&lt;T<&gt;</code>.
 ** @param  <V>                  the type of the value contained in the
 **                              {@link java.util.Optional}.
 **                              <br>
 **                              Allowed object is <code>&lt;V<&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractAssertedObject<T extends AbstractAssertedObject<T, V>, V>  {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractAssertedObject</code> with the
   ** <code>actual</code> value refering to <code>self</code>.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** We prefer not to use Class<? extends T> self because it would force
   ** inherited constructor to cast with a compiler warning let's keep compiler
   ** warning internal (when we can) and not expose them to our end users.
   **
   ** @param  actual             the actual value to kept.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  self               the referencing {@link Class}.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   */
  protected AbstractAssertedObject() {
    // ensure inheritance
    super();
  }
}
