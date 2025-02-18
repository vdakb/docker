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

    File        :   EntryReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntryReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.plx.service.reconciliation;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.connector.integration.TargetFeature;
import oracle.iam.identity.connector.integration.TargetResource;
import oracle.iam.identity.connector.service.EntityReconciliation;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.plx.integration.DirectoryFeature;
import oracle.iam.identity.plx.integration.DirectoryResource;
import oracle.iam.provisioning.vo.ApplicationInstance;

import org.xml.sax.InputSource;
////////////////////////////////////////////////////////////////////////////////
// class EntryReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>EntryReconciliation</code> acts as the service end point for the
 ** Identity Manager to reconcile account information from a
 <code>Directory Service</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
abstract class EntryReconciliation extends EntityReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify the base of
   ** the search to retrieve objects from target system.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String SEARCH_BASE     = "Search Base";

  /**
   ** Attribute tag which must be defined on this task to specify the scope of
   ** the search to retrieve objects from target system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String SEARCH_SCOPE    = "Search Scope";

  /**
   ** Attribute tag which must be defined on this task to specify which filter
   ** criteria has to be applied to retrieve directory entries.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String SEARCH_FILTER   = "Search Filter";

  /**
   ** Attribute tag which may be defined on this task to specify the sort of the
   ** result returned by the search.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String SEARCH_ORDER    = "Search Order";

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY = "OCS.GDS.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntryReconciliation</code> scheduler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntryReconciliation() {
    super(LOGGER_CATEGORY);
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