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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Password.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Password.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.common.task;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import java.io.File;
import java.io.FileReader;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.hst.foundation.SystemException;

import oracle.hst.deployment.ServiceException;

import oracle.iam.identity.common.FeaturePlatformTask;

////////////////////////////////////////////////////////////////////////////////
// class Password
// ~~~~~ ~~~~~~~~
/**
 ** Dumps the configured IT Resource attribute values from Identity Manager.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Password extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for organizations. */
  static final String PREFIX          = "IT Resources.";

  /**
   ** the key contained in a collection to specify that the system key should be
   ** mapped.
   */
  static final String FIELD_KEY       = "Key";

  /**
   ** the key contained in a collection to specify that the name should be
   ** mapped.
   */
  static final String FIELD_NAME      = "Name";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition system key should be resolved
   */
  static final String KEY             = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition name should be resolved
   */
  static final String NAME            = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition parameter name should be resolved
   */
  static final String PARAMETER_NAME  = "IT Resources Type Parameter.Name";

  /**
   ** the mapping key contained in a collection to specify that the type
   ** definition parameter value should be resolved
   */
  static final String PARAMETER_VALUE = "IT Resources Type Parameter Value.Value";

  private static String          password;
  private static String          name;

  private static Password        service;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Password</code> that will use the specified configuration
   ** to connect to the server.
   **
   ** @param  environment        the {@link Properties} specifying the
   **                            connection details.
   **
   ** @throws Exception          If there is an error during login.
   */
  public Password(final Properties environment)
    throws Exception {

    super(environment, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  protected void onExecution()
    throws ServiceException {

    tcITResourceInstanceOperationsIntf facade = service.service(tcITResourceInstanceOperationsIntf.class);
    Map<String, String>                filter = new HashMap<String, String>();
    filter.put(NAME, name);
    System.out.println("Looking up IT Resource " + name);
    try {
      tcResultSet found = facade.findITResourceInstances(filter);
      if (found.isEmpty()) {
        System.out.println("IT Resource not found");
      }
      long instance = fetchLongValue(found, KEY);

      final Map<String, String> attribute = new HashMap<String, String>();
      tcResultSet               rs = facade.getITResourceInstanceParameters(instance);
      for (int i = 0; i < rs.getRowCount(); i++) {
        rs.goToRow(i);

        // get the name of the parameter from the IT Resource definition
        String parameterName = fetchStringValue(rs, PARAMETER_NAME);

        attribute.put(parameterName, fetchStringValue(rs, PARAMETER_VALUE));
      }
      System.out.println(attribute.toString());
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  public static void main(String[] args) {
    password = args[0];
    name = args[1];
    File       config = validateSystemProperties();
    Properties env = new Properties();
    try {
      env.load(new FileReader(config));
      service = new Password(env);
      service.onExecution();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchLongValue
  /**
   ** Retrieves the value of a particular field from the specified data record.
   **
   ** @param  resultSet          the meta data record where the value should be
   **                            obtained from.
   ** @param  fieldName          the name of the field as defined in Identity
   **                            Manager meta data entries.
   **
   ** @return                    the string representation of the value of the
   **                            specified field.
   **
   ** @throws SystemException    a generell error has occurred.
   */
  protected long fetchLongValue(final tcResultSet resultSet, final String fieldName)
    throws SystemException {

    long value = -1L;
    try {
      value = resultSet.getLongValue(fieldName);
    }
    catch (tcColumnNotFoundException e) {
      throw new SystemException(e);
    }
    catch (tcAPIException e) {
      throw new SystemException(e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchStringValue
  /**
   ** Retrieves the value of a particular field from the specified data record.
   **
   ** @param  resultSet          the meta data record where the value should be
   **                            obtained from.
   ** @param  fieldName          the name of the field as defined in Identity
   **                            Manager meta data entries.
   **
   ** @return                    the string representation of the value of the
   **                            specified field.
   **
   ** @throws SystemException    a generell error has occurred.
   */
  protected String fetchStringValue(final tcResultSet resultSet, final String fieldName)
    throws SystemException {

    String value = null;
    try {
      value = resultSet.getStringValue(fieldName);
    }
    catch (tcColumnNotFoundException e) {
      throw new SystemException(e);
    }
    catch (tcAPIException e) {
      throw new SystemException(e);
    }
    return value;
  }

  private static File validateSystemProperties() {
    // check the Identity Manager runtime configuration
    String location = System.getProperty("XL.HomeDir");
    if (empty(location)) {
      System.err.println("You must set XL.HomeDir pointing to a directory where the server.conf can be loaded from");
      System.exit(-1);
    }
    else if (!location.endsWith(File.separator)) {
      location += File.separator;
    }

    location += "config/server.conf";
    File config = new File(location);
    if (!config.exists()) {
      System.err.println("Cannot find file " + config.getAbsolutePath());
      System.exit(-2);
    }

    // check the JAAS runtime configuration
    String loginconfig = System.getProperty("java.security.auth.login.config");
    if (empty(loginconfig)) {
      System.err.println("You must set java.security.auth.login.config pointing to configuration file used for login");
      System.exit(-3);
    }
    return config;
  }

  private static boolean empty(final String option) {
    return (option == null || option.trim().length() == 0);
  }
}