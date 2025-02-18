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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ApplicationRuntimeHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationRuntimeHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Set;
import java.util.HashSet;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// abstract class DomainRuntimeHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DomainRuntimeHandler</code> provides access to the domain runtime
 ** configuration JMX MBeans of Oracle Access Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class DomainRuntimeHandler extends AbstractInvocationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BEAN        = "oracle.oam:type=Config";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String> flatten     = new HashSet<String>();

  private ObjectName          name        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DomainRuntimeHandler</code> Ant task which will gain
   ** access to the JMX Domain Runtime MBean.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#print}
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  protected DomainRuntimeHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend);

    // initiallize instance attributes
    operation(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvokerTask)
  /**
   ** Returns the JMX {@link ObjectName} this task will execute.
   **
   ** @return                    the JMX {@link ObjectName} this task will
   **                            execute.
   **
   ** @throws FeatureException   if the JMX {@link ObjectName} to build is
   **                            malformed.
   */
  @Override
  protected ObjectName objectName()
    throws FeatureException {

    if (this.name == null) {
      synchronized(BEAN) {
        try {
          this.name = new ObjectName(BEAN);
        }
        catch (MalformedObjectNameException e) {
          throw new FeatureException(FeatureError.DOMAIN_OBJECTNAME_MALFORMED, e);
        }
      }
    }
    return this.name;
  }
}