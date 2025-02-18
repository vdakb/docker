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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Drupal Connector

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.gws.service.provisioning.drupal;

import org.xml.sax.InputSource;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Provisioning;

import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.connector.integration.TargetResource;

import oracle.iam.identity.gws.integration.drupal.ServiceFeature;
import oracle.iam.identity.gws.integration.drupal.ServiceResource;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** The <code>Service</code> acts as the service end point for the Oracle
 ** Identity Manager to reconcile account information from a
 ** <code>Google Drupal</code> Service Provider.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class Service extends Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY = "OCS.GDP.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> task adapter.
   **
   ** @param  provider           the session provider connection.
   **                            <br>
   **                            Allowed object is {@link tcDataProvider}.
   ** @param  processInstance    the Process Instance Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  processTask        the Process Task Key providing the data for
   **                            the provisioning tasks.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  protected Service(final tcDataProvider provider, final Long processInstance, final Integer processTask) {
    // ensure inheritance
    super(provider, processInstance, processTask, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateResource  (Provisioning)
  /**
   ** Initalize the IT Resource capabilities.
   **
   ** @param  resourceInstance   the <code>IT Resource</code> used by this
   **                            instance to establish a connection to the
   **                            Target System.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    a {@link TargetResource} populated and
   **                            validated.
   **                            <br>
   **                            Possible object {@link TargetResource}.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  @Override
  protected final TargetResource populateResource(final Long resourceInstance)
    throws TaskException {

    final String method  = "populateFeature";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      return ServiceResource.build(this, resourceInstance);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateFeature (Reconciliation)
  /**
   ** Factory method to create and populate the <code>Metadata Descriptor</code>
   ** form the <code>Metadata Service</code>.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    an instance of {@link TargetFeature} populated
   **                            form the <code>Metadata Service</code>.
   **                            <br>
   **                            Possible object is {@link TargetFeature}.
   **
   ** @throws TaskException      in case marshalling the
   **                            <code>Metadata Descriptor</code> fails.
   */
  @Override
  protected final TargetFeature populateFeature(final InputSource source)
    throws TaskException {

    return ServiceFeature.build(this, source);
  }
}
