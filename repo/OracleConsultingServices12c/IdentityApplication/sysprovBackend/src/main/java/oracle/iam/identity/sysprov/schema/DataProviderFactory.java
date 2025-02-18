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
    Subsystem   :   System Provisioning Management

    File        :   DataProviderFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DataProviderFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

////////////////////////////////////////////////////////////////////////////////
// class DataProviderFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java provider interface and
 ** Java element interface generated in the
 ** <code>oracle.iam.identity.sysprov.schema</code> package.
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
  // Method:   createEndpointDataProvider
  /**
   ** Create an instance of {@link EndpointDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EndpointDataProvider}.
   */
  public static EndpointDataProvider createEndpointDataProvider() {
    return new EndpointDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEndpointParameterDataProvider
  /**
   ** Create an instance of {@link EndpointParameterDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EndpointParameterDataProvider}.
   */
  public static EndpointParameterDataProvider createEndpointParameterDataProvider() {
    return new EndpointParameterDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEndpointAdministratorDataProvider
  /**
   ** Create an instance of {@link EndpointAdministratorDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EndpointAdministratorDataProvider}.
   */
  public static EndpointAdministratorDataProvider createEndpointAdministratorDataProvider() {
    return new EndpointAdministratorDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEndpointTypeDataProvider
  /**
   ** Create an instance of {@link EndpointTypeDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EndpointTypeDataProvider}.
   */
  public static EndpointTypeDataProvider createEndpointTypeDataProvider() {
    return new EndpointTypeDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEndpointTypeParameterDataProvider
  /**
   ** Create an instance of {@link EndpointTypeParameterDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link EndpointTypeParameterDataProvider}.
   */
  public static EndpointTypeParameterDataProvider createEndpointTypeParameterDataProvider() {
    return new EndpointTypeParameterDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResourceObjectDataProvider
  /**
   ** Create an instance of {@link ResourceObjectDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link ResourceObjectDataProvider}.
   */
  public static ResourceObjectDataProvider createResourceObjectDataProvider() {
    return new ResourceObjectDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResourceObjectAdministratorDataProvider
  /**
   ** Create an instance of {@link ResourceObjectAdministratorDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link ResourceObjectAdministratorDataProvider}.
   */
  public static ResourceObjectAdministratorDataProvider createResourceObjectAdministratorDataProvider() {
    return new ResourceObjectAdministratorDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createReconciliationDataProvider
  /**
   ** Create an instance of {@link ReconciliationProfileDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link ReconciliationProfileDataProvider}.
   */
  public static ReconciliationProfileDataProvider createReconciliationProfileDataProvider() {
    return new ReconciliationProfileDataProvider();
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
  // Method:   createReconciliationEventDataProvider
  /**
   ** Create an instance of {@link ReconciliationEventDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link ReconciliationEventDataProvider}.
   */
  public static ReconciliationEventDataProvider createReconciliationEventDataProvider() {
    return new ReconciliationEventDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createReconciliationEventHistoryDataProvider
  /**
   ** Create an instance of {@link ReconciliationEventHistoryDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link ReconciliationEventHistoryDataProvider}.
   */
  public static ReconciliationEventHistoryDataProvider createReconciliationEventHistoryDataProvider() {
    return new ReconciliationEventHistoryDataProvider();
  }
}