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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   Transformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Transformer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

////////////////////////////////////////////////////////////////////////////////
// interface Transformer
// ~~~~~~~~~ ~~~~~~~~~~~
public interface Transformer<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Transformer NULL = new Transformer() {

    ////////////////////////////////////////////////////////////////////////////
    // Method:   transform
    /**
     ** Returns the specified <code>value</code> as an appropriate
     ** transformation.
     **
     ** @param  origin           the <code>Object</code> to transform.
     **
     ** @return                  the transformation of the specified
     **                          <code>origin</code>.
     */
    @Override
    public Object transform(final Object origin) {
      return origin;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   hashCode (overridden)
    /**
     ** Returns a hash code for this instance.
     **
     ** @return                  a hash code value for this instance.
     */
    public int hashCode() {
      return toString().hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString (overridden)
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     */
    public String toString() {
      return "NullTransformer";
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   **
   ** @param  <E>                the required class type of the transformation.
   ** @param  origin             the <code>Object</code> to transform.
   **
   ** @return                    the transformation of the specified
   **                            <code>origin</code>.
   */
  <E> E transform(final E origin);
}