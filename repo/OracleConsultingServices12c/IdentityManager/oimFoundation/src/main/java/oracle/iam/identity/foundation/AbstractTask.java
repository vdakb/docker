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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AbstractTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcPropertyOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// interface AbstractTask
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>AbstractTask</code> desclares the base functionality of a service
 ** end point for the Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface AbstractTask extends Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** response code task completed succesfully */
  static final String SUCCESS           = "SUCCESS";

  /** response code task completed succesfully */
  static final String FAILURE           = "FAILURE";

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyFacade (AbstractTask)
  /**
   ** Returns an instance of a <code>System Configuration</code> associated with
   ** a provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the utility class cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    the <code>System Configuration</code> class.
   */
  tcPropertyOperationsIntf propertyFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceFacade
  /**
   ** Returns an instance of an <code>IT Resource</code> class associated with a
   ** provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the utility class cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    the <code>IT Resource</code> class.
   */
  tcITResourceInstanceOperationsIntf resourceFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupFacade
  /**
   ** Returns an instance of an Business Facade instance for Oracle Identity
   ** Manager Group associatiated with a provisioning or reconciliation task.
   **
   ** @return                    a Business Facade instance.
   */
  tcGroupOperationsIntf groupFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectFacade
  /**
   ** Returns an instance of a Business Facade instance for Objects
   ** associatiated with a provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  tcObjectOperationsIntf objectFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFacade
  /**
   ** Returns an instance of a Business Facade instance for Proceses
   ** associatiated with a provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  tcProvisioningOperationsIntf processFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formDefinitionFacade
  /**
   ** Returns an instance of a Business Facade instance for Form Definition
   ** associatiated with a provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  tcFormDefinitionOperationsIntf formDefinitionFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formInstanceFacade
  /**
   ** Returns an instance of a Business Facade instance for Form Instance
   ** (Process Data) associatiated with a provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  tcFormInstanceOperationsIntf formInstanceFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userFacade
  /**
   ** Returns an instance of an Business Facade instance for Oracle Identity
   ** Manager User associatiated with a provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an Business Facade instance.
   */
  tcUserOperationsIntf userFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationFacade
  /**
   ** Returns an instance of a Business Facade instance for Oracle Identity
   ** Manager Organization associatiated with a provisioning or reconciliation
   ** task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  tcOrganizationOperationsIntf organizationFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFacade
  /**
   ** Returns an instance of a Business Facade instance for Lookup Definitions
   ** associated with a provisioning or reconciliation task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    the Business Facade instance.
   */
  tcLookupOperationsIntf lookupFacade();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns an instance of a Business Service by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  <T>                the expected class type.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   */
  <T> T service(final Class<T> serviceClass);
}