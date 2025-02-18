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
    Subsystem   :   System Administration Management

    File        :   DataProviderFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DataProviderFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import oracle.iam.ui.platform.model.common.OIMProgrammaticVO;

////////////////////////////////////////////////////////////////////////////////
// class DataProviderFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java provider interface and
 ** Java element interface generated in the
 ** <code>oracle.iam.identity.sysadmin.schema</code> package.
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
  // Method:   createTaskDataProvider
  /**
   ** Create an instance of {@link TaskDataProvider}.
   **
   ** @return                    an instance of {@link TaskDataProvider}.
   **                            <br>
   **                            Possible object is {@link TaskDataProvider}.
   */
  public static TaskDataProvider createTaskDataProvider() {
    return new TaskDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTaskParameterDataProvider
  /**
   ** Create an instance of {@link TaskParameterDataProvider}.
   **
   ** @return                    an instance of {@link TaskParameterDataProvider}.
   **                            <br>
   **                            Possible object is
   **                            {@link TaskParameterDataProvider}.
   */
  public static TaskParameterDataProvider createTaskParameterDataProvider() {
    return new TaskParameterDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createJobDataProvider
  /**
   ** Create an instance of {@link JobDataProvider}.
   **
   ** @return                    an instance of {@link JobDataProvider}.
   **                            <br>
   **                            Possible object is {@link JobDataProvider}.
   */
  public static JobDataProvider createJobDataProvider() {
    return new JobDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createJobDataProvider
  /**
   ** Create an instance of {@link JobDataProvider}.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This initialize the dataprovider in a special way to be able to localize
   ** static value lists.
   **
   ** @param  viewDefinition     the {@link OIMProgrammaticVO} used to localize
   **                            static value lists.
   **                            <br>
   **                            Allowed object is {@link OIMProgrammaticVO}.
   **
   ** @return                    an instance of {@link JobDataProvider}.
   **                            <br>
   **                            Possible object is {@link JobDataProvider}.
   */
  public static JobDataProvider createJobDataProvider(final OIMProgrammaticVO viewDefinition) {
    return new JobDataProvider(viewDefinition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createJobParameterDataProvider
  /**
   ** Create an instance of {@link JobParameterDataProvider}.
   **
   ** @return                    an instance of {@link JobParameterDataProvider}.
   **                            <br>
   **                            Possible object is
   **                            {@link JobParameterDataProvider}.
   */
  public static JobParameterDataProvider createJobParameterDataProvider() {
    return new JobParameterDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createJobHistoryDataProvider
  /**
   ** Create an instance of {@link JobHistoryDataProvider}.
   **
   ** @return                    an instance of {@link JobHistoryDataProvider}.
   **                            <br>
   **                            Possible object is
   **                            {@link JobHistoryDataProvider}.
   */
  public static JobHistoryDataProvider createJobHistoryDataProvider() {
    return new JobHistoryDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createJobHistoryDataProviderr
  /**
   ** Create an instance of {@link JobHistoryDataProvider}.
   ** <br>
   ** <b>Note</b>:
   ** This initialize the dataprovider in a special way to be able to localize
   ** static value lists.
   **
   ** @param  viewDefinition     the {@link OIMProgrammaticVO} used to localize
   **                            static value lists.
   **                            <br>
   **                            Allowed object is {@link OIMProgrammaticVO}.
   **
   ** @return                    an instance of {@link JobHistoryDataProvider}.
   **                            <br>
   **                            Possible object is
   **                            {@link JobHistoryDataProvider}.
   */
  public static JobHistoryDataProvider createJobHistoryDataProvider(final OIMProgrammaticVO viewDefinition) {
    return new JobHistoryDataProvider(viewDefinition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrchestrationProcessDataProvider
  /**
   ** Create an instance of {@link OrchestrationProcessDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link OrchestrationProcessDataProvider}.
   **                            <br>
   **                            Possible object is
   **                            {@link OrchestrationProcessDataProvider}.
   */
  public static OrchestrationProcessDataProvider createOrchestrationProcessDataProvider() {
    return new OrchestrationProcessDataProvider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrchestrationEventDataProvider
  /**
   ** Create an instance of {@link OrchestrationEventDataProvider}.
   **
   ** @return                    an instance of
   **                            {@link OrchestrationEventDataProvider}.
   **                            <br>
   **                            Possible object is
   **                            {@link OrchestrationProcessDataProvider}.
   */
  public static OrchestrationEventDataProvider createOrchestrationEventDataProvider() {
    return new OrchestrationEventDataProvider();
  }
}