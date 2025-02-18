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

    File        :   AssertedClassType.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertedClassType.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.testing.core;

////////////////////////////////////////////////////////////////////////////////
// class AssertedClassType
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Java 8 is picky when choosing the right <code>assertThat</code> method if
 ** the object under test is generic and bounded, for example if foo is instance
 ** of <code>T</code> that extends <code>Exception</code>, java 8  will complain
 ** that it can't resolve the proper <code>assertThat</code> method (normally
 ** <code>assertThat(Throwable)</code> as foo might implement an interface like
 ** List, if that occurred <code>assertThat(List)</code> would also be a
 ** possible choice - thus confusing java 8.
 ** <p>
 ** This why {@link Assertions} have been split in
 ** {@link AssertedClassType} and {@link AssertedInterfaceType}
 ** (see http://stackoverflow.com/questions/29499847/ambiguous-method-in-java-8-why).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AssertedClassType {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AssertedClassType</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new AssertedClassType()" and enforces use of the public method below.
   */
  private AssertedClassType() {
    // should never be instantiated
    throw new AssertionError();
  }
}