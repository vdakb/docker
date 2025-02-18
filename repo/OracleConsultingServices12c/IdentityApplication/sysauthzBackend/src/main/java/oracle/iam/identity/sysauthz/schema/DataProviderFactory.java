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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Authorization Management

    File        :   DataProviderFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DataProviderFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

////////////////////////////////////////////////////////////////////////////////
// class DataProviderFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java provider interface and
 ** Java element interface generated in the
 ** <code>oracle.iam.identity.sysauthz.schema</code> package.
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
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
  // Method:   createCatalogItemDataProvider
  /**
   ** Create an instance of {@link CatalogItemDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link CatalogItemDataProvider}.
   */
  public static CatalogItemDataProvider createCatalogItemDataProvider() {
    return new CatalogItemDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplicationInstanceDataProvider
  /**
   ** Create an instance of {@link ApplicationInstanceDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link ApplicationInstanceDataProvider}.
   */
  public static ApplicationInstanceDataProvider createApplicationInstanceDataProvider() {
    return new ApplicationInstanceDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntitlementDataProvider
  /**
   ** Create an instance of {@link EntitlementDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EntitlementDataProvider}.
   */
  public static EntitlementDataProvider createEntitlementDataProvider() {
    return new EntitlementDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntitlementInstanceDataProvider
  /**
   ** Create an instance of {@link EntitlementInstanceDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EntitlementInstanceDataProvider}.
   */
  public static EntitlementInstanceDataProvider createEntitlementInstanceDataProvider() {
    return new EntitlementInstanceDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccountDataProvider
  /**
   ** Create an instance of {@link AccountDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link AccountDataProvider}.
   */
  public static AccountDataProvider createAccountDataProvider() {
    return new AccountDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntityPublicationDataProvider
  /**
   ** Create an instance of {@link EntityPublicationDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EntityPublicationDataProvider}.
   */
  public static EntityPublicationDataProvider createEntityPublicationDataProvider() {
    return new EntityPublicationDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createReportDataProvider
  /**
   ** Create an instance of {@link createReportDataProvider}.
   **
   ** @return                    an instance of {@link ReportDataProvider}.
   */
  public static ReportDataProvider createReportDataProvider() {
    return new ReportDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createReportParameterDataProvider
  /**
   ** Create an instance of {@link createReportDataProvider}.
   **
   ** @return                    an instance of {@link ReportDataProvider}.
   */
  public static ReportParameterDataProvider createReportParameterDataProvider() {
    return new ReportParameterDataProvider();
  }
}