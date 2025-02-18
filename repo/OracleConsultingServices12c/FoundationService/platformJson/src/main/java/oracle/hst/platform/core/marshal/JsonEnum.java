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

    System      :   Oracle Marshalling Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   JsonEnum.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    JsonEnum.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.core.marshal;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface JsonEnum
// ~~~~~~~~~ ~~~~~~~~
/**
 ** This interface is intended for <code>enum</code>s (or similar classes that
 ** needs to be identified by a value) who are based on a value for each
 ** constant, where it has the utility methods to identify the type
 ** (<code>enum</code> constant) based on the value passed, and can declare it's
 ** value in the interface as well.
 **
 ** @param  <T>                  the type of the constants (pass the
 **                              <code>enum</code> as a type).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 ** @param  <V>                  the type of the value which identifies this
 **                              constant.
 **                              <br>
 **                              Allowed object is <code>&lt;V&gt;</code>.
 */
public interface JsonEnum<T extends JsonEnum<T, V>, V> extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Returns the <code>JsonEnum</code> based on the passed value.
   **
   ** @param  value              the value that identifies the
   **                            <code>JsonEnum</code>.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the <code>JsonEnum</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  T of(final V value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the value that identifies the <code>JsonEnum</code>
   **
   ** @return                    a value that can be used later in
   **                            {@link #of(Object)}
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
   V id();
}