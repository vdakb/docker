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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   LookupReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.bds.service.reconciliation;

import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.xml.sax.InputSource;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.connector.integration.TargetResource;

import oracle.iam.identity.bds.integration.DirectoryFeature;
import oracle.iam.identity.bds.integration.DirectoryResource;

////////////////////////////////////////////////////////////////////////////////
// class LookupReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupReconciliation</code> acts as the service end point for
 ** Identity Manager to reconcile metadata information from a target system.
 ** <p>
 ** This class provides also the callback interface for operations that are
 ** returning one or more results. Currently used only by Search, but may be
 ** used by other operations in the future.
 ** <p>
 ** The following scheduled task parameters are expected to be defined:
 ** <ul>
 **   <li>IT Resource              - The IT Resource used to establish the
 **                                  connection to the target system
 **   <li>Reconciliation Object    - the name of the Lookup Definition to
 **                                  reconcile
 **   <li>Reconciliation Operation - The operation to perform on the object to
 **                                  reconcile. Has to be either Refresh or
 **                                  Update
 **   <li>Lookup Group             - The value written to Lookup Group in case
 **                                  the operation on a particular Lookup
 **                                  Definition has to create it
 **    <li>Encoded Value           - The name of the attribute that has to be
 **                                  stored as the encoded value
 **                                  (eg: <code>__UID__</code>,
 **                                  <code>__NAME__</code>, ...)
 **    <li>Decode Value            - The name of the attribute that has to be
 **                                  stored as the encoded value
 **                                  (eg: <code>__NAME__</code>,
 **                                  groupDescription, ...)
 **    <li>Object Class            - connector <code>ObjectClass</code> name
 **    <li>Batch Size              - Specifies the size of a block read from the
 **                                  reconciliation source
 ** </ul>
 ** The implementation will execute connector search with parameters provided by
 ** the following methods:
 ** <ul>
 **   <li>{@link LookupReconciliation#objectClass()}
 **   <li>{@link LookupReconciliation#filter()}
 **   <li>{@link LookupReconciliation#operationOptions()}</li>
 ** </ul>
 ** Each of the <code>ConnectorObject</code>s returned by the search is
 ** processed by the following methods to get encoded and decode values:
 ** <ul>
 **   <li>{@link LookupReconciliation#handle(ConnectorObject)}
 **   <li>{@link LookupReconciliation#processPair(String, String)}
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LookupReconciliation extends oracle.iam.identity.connector.service.LookupReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateResource  (Reconciliation)
  /**
   ** Initalize the IT Resource capabilities.
   ** <p>
   ** The approach implementing this method should be:
   ** <ol>
   **   <li>If only  Application Name is configured as job parameter and
   **       IT Resource or Reconciliation Object is not configured, then
   **       IT Resource and Reconciliation Object will be fetched mapped with
   **       the given application Name
   **   <li>If IT Resource and Reconciliation Object are configured as job
   **       parameter then they gets priority even application name is also
   **       configured as job parameter
   **   <li>getParameter method logic handle the precedence logic of for cases
   **       mentioned in point #1 &amp; 2
   ** </ol>
   **
   ** @return                    a {@link TargetResource} populated and
   **                            validated.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  @Override
  protected TargetResource populateResource()
    throws TaskException {

    final String method  = "populateResource";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    TargetResource resource = null;
    try {
      // 1. If only  Application Name is configured as job parameter and
      //    IT Resource or Reconciliation Object is not configured, then
      //    IT Resource and Reconciliation Object will be fetched mapped with
      //    the given application Name
      // 2. If IT Resource and Reconciliation Object are configured as job
      //    parameter then they gets priority even application name is also
      //    configured as job parameter
      // 3. getParameter method logic handle the precedence logic of for cases
      //    mentioned in point #1 & 2
      if(!StringUtility.isBlank(resourceName())){
        resource = DirectoryResource.build(this, resourceName());
      }
      else {
        final ApplicationInstance instance = populateInstance(applicationInstance());
        resource = DirectoryResource.build(this, instance.getItResourceKey());
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return resource;
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

    return DirectoryFeature.build(this, source);
  }
}