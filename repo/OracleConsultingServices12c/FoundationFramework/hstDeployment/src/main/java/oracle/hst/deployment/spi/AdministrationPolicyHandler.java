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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AdministrationPolicyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AdministrationPolicyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import org.apache.tools.ant.BuildException;

import oracle.security.jps.mas.mgmt.jmx.util.JpsJmxConstants;

import oracle.hst.deployment.ServiceFrontend;

////////////////////////////////////////////////////////////////////////////////
// class AdministrationPolicyHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage administration policies in Oracle
 ** WebLogic Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AdministrationPolicyHandler extends AbstractInvocationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Validates whether a user logged into the current JMX context belongs to a
   ** particular role. It does not facilitate any configuration modifications.
   */
  private static ObjectName   administrationPolicyStore;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AdministrationPolicyHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public AdministrationPolicyHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvocationHandler)
  /**
   ** Returns the JMX {@link ObjectName} to access the credential store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            credential store.
   */
  @Override
  protected final ObjectName objectName() {
    return administrationPolicyStore();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrationPolicyStore
  /**
   ** Returns the JMX {@link ObjectName} to access the administration policy
   ** store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            administration policy store.
   */
  private static ObjectName administrationPolicyStore() {
    if (administrationPolicyStore == null) {
      try {
       // Validates whether a user logged into the current JMX context belongs
       // to a particular role. It does not facilitate any configuration
       // modifications.
       administrationPolicyStore = new ObjectName(JpsJmxConstants.MBEAN_JPS_ADMIN_POLICY_STORE);
      }
      catch (MalformedObjectNameException e) {
        throw new AssertionError(e.getMessage());
      }
    }
    return administrationPolicyStore;
  }
}