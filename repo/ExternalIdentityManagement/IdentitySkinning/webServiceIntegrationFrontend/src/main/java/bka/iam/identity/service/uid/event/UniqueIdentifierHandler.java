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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Frontend Extension
    Subsystem   :   UID Service

    File        :   UniqueIdentifierHandler.java

    Compiler    :   JDK 1.8

    Author      :   sylver.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    UniqueIdentifierHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-02  SBernet    First release version
*/
package bka.iam.identity.service.uid.event;

import bka.iam.identity.service.ServiceException;
import bka.iam.identity.service.uid.UIDException;
import bka.iam.identity.service.uid.UIDService;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.ui.platform.exception.OIMRuntimeException;

////////////////////////////////////////////////////////////////////////////////
// class UniqueIdentifierHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exposing methods to call from contextual event handlers and handles all
 ** events belonging to requesting/registering an <code>UID</code> to a user.
 ** <p>
 ** Because contextual event is a functionality of the ADF binding layer, all
 ** event handlers must be exposed from a Data Control.
 ** <br>
 ** In this use case, the exposing Data Control is a JavaBean data control
 ** created from this class
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UniqueIdentifierHandler {
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UniqueIdentifierHandler</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UniqueIdentifierHandler() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestUniqueIdentifier
  /**
   ** Handles the request of an UID by providing a tenant to the service.
   **
   ** @param  tenant             the value of the tenant where the uid will be
   **                            generated.
   **                            Allowed object is {@link String}.
   **                            
   ** @return                   the generated uid from the service.
   **                           <br>
   **                           Possible object is {@link String}.
   */
  public String requestUniqueIdentifier(final String tenant) {
    String uid = null;
    try {
      uid =  new UIDService().request(tenant);
    }
    catch (ServiceException e) {
      reportException(e.code(), e);
    }
    catch (UIDException e) {
      reportException(e.code(), e);
    }
    return uid;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   registerUniqueIdentifier
  /**
   ** Handles the registration of an UID in the service.
   **
   ** @param uid                the value of the UID to register.
   **                           <br>
   **                           Allowed object is {@link String}.
   *
   ** @return                   the successfully generated uid.
   **                           <br>
   **                           Possible object is {@link String}.
   */
  public String registerUniqueIdentifier(final String uid) {
    String registeredUID = null;
    try {
      registeredUID =  new UIDService().register(uid);
    }
    catch (ServiceException e) {
      reportException(e.code(), e);
    }
    catch (UIDException e) {
      reportException(e.code(), e);
    }
    return registeredUID;
  }
  
  private void reportException(final String code, final Exception e) {
    final String              message = String.format("%s: %s", code, e.getMessage());
    final DCBindingContainer  dc      = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
    final OIMRuntimeException ex      = new OIMRuntimeException(message, e);
    dc.reportException(ex);
  }
}
