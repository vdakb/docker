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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   DiagnosticHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DiagnosticHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import oracle.iam.diagnostic.api.DiagnosticService;

import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeaturePlatformTask;

////////////////////////////////////////////////////////////////////////////////
// class DiagnosticHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>DiagnosticHandler</code> performs diagnostic operations in Oracle
 ** Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DiagnosticHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the single {@link DiagnosticData} to perfom diagnostic operations */
  private DiagnosticData single;

  /**
   ** the collection of {@link DiagnosticData}s  to perfom diagnostic
   ** operations
   */
  private List<DiagnosticData> multiple = new ArrayList<DiagnosticData>();

  /**
   ** the business logic layer to operate on <code>IT Resource</code> instances
   */
  private tcITResourceInstanceOperationsIntf resource;

  /**
   ** the business logic layer to perform diagnostics
   */
  private DiagnosticService diagnostic;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DiagnosticHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public DiagnosticHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Oracle
   **                            Identity Manager Role.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager Role.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager Role.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that is already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the Catalog Element in Oracle
   **                            Identity Manager to handle.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new DiagnosticData();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ping
  /**
   ** Creates a connection to the target system from Identity Manager through
   ** the discovered {@link DiagnosticService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void ping(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.diagnostic = task.service(DiagnosticService.class);
    this.resource = task.service(tcITResourceInstanceOperationsIntf.class);

    if (this.single != null) {
      ping(this.single);
    }

    for (DiagnosticData i : this.multiple) {
      ping(i);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Creates a connection to the target system from Identity Manager through
   ** the discovered {@link DiagnosticService}.
   **
   ** @param  data               the {@link DiagnosticData} to test.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void ping(final DiagnosticData data)
    throws ServiceException {

    String   className = "";
    String[] type = data.type().split(" ");
    if (type.length > 0) {
      for (int index = 0; index < type.length; index++) {
        className = className.trim() + type[index];
      }
    }
    else {
      className = data.type();
    }
  }
}