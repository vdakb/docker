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
    Subsystem   :   System Configuration Management

    File        :   DataProviderFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DataProviderFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

////////////////////////////////////////////////////////////////////////////////
// class DataProviderFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java provider interface and
 ** Java element interface generated in the
 ** <code>oracle.iam.identity.sysconfig.schema</code> package.
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
  // Method:   createLookupDataProvider
  /**
   ** Create an instance of {@link LookupDataProvider}.
   **
   ** @return                    an instance of {@link LookupDataProvider}.
   */
  public static LookupDataProvider createLookupDataProvider() {
    return new LookupDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLookupValueDataProvider
  /**
   ** Create an instance of {@link LookupValueDataProvider}.
   **
   ** @return                    an instance of {@link LookupValueDataProvider}.
   */
  public static LookupValueDataProvider createLookupValueDataProvider() {
	  return new LookupValueDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLookupValueDataProvider
  /**
   ** Factory method to create a <code>LookupValueDataProvider</code> values
   ** object which belongs to specified name of a
   ** <code>Lookup Definition</code>.
   **
   ** @param  lookup             the name of a <code>Lookup Definition</code>.
   **
   ** @return                    an instance of
   **                            {@link LookupValueDataProvider}.
   */
  public static LookupValueDataProvider createLookupValueDataProvider(final String lookup) {
    return new LookupValueDataProvider(lookup);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPropertyDataProvider
  /**
   ** Create an instance of {@link PropertyDataProvider}.
   **
   ** @return                    an instance of {@link PropertyDataProvider}.
   */
  public static PropertyDataProvider createPropertyDataProvider() {
    return new PropertyDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNotificationTemplateDataProvider
  /**
   ** Create an instance of {@link NotificationTemplateDataProvider}.
   **
   ** @return                    an instance of
   **                           {@link NotificationTemplateDataProvider}.
   */
  public static NotificationTemplateDataProvider createNotificationTemplateDataProvider() {
    return new NotificationTemplateDataProvider();
  }
}