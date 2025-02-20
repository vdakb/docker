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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Password Reset

    File        :   DataProviderFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DataProviderFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.password.schema;

////////////////////////////////////////////////////////////////////////////////
// class DataProviderFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java provider interface and
 ** Java element interface generated in the
 ** <code>bka.iam.identity.password.schema</code> package.
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DataProviderFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>DataProviderFactory</code> object.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DataProviderFactory()" and enforces use of the public factory method
   ** below.
   */
  private DataProviderFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccountPasswordDataProvider
  /**
   ** Create an instance of {@link AccountPasswordDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link AccountPasswordDataProvider}.
   */
  public static AccountPasswordDataProvider createAccountPasswordDataProvider() {
    return new AccountPasswordDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentityPasswordDataProvider
  /**
   ** Create an instance of {@link IdentityPasswordDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link IdentityPasswordDataProvider}.
   */
  public static IdentityPasswordDataProvider createIdentityPasswordDataProvider() {
    return new IdentityPasswordDataProvider();
  }
}