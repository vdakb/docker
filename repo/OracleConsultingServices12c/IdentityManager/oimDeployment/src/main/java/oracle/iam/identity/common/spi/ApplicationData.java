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

    File        :   ApplicationData.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationData.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceOperation;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationData
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>ApplicationData</code> represents an application instance in Oracle
 ** Identity Manager that might be created, deleted or changed after or during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ApplicationData extends EntitlementData {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the <code>Resource Object</code> of Identity Manager an
   ** <code>Application Instance</code> belongs to.
   */
  private String resource = null;

  /**
   ** the <code>IT Resource</code> of Identity Manager an
   ** <code>Application Instance</code> belongs to.
   */
  private String  server      = null;

  /** the form associated with an <code>Application Instance</code> */
  private String  dataSet     = null;

  /** if the <code>Application Instance</code> is applicable to entitlements */
  private Boolean entitlement = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ApplicationData</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationData() {
    // ensure inheritance
    super(OIMType.ApplicationInstance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Called to inject the <code>Resource Object</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @param  resource           the <code>Resource Object</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void resource(final String resource) {
    this.resource = resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the <code>Resource Object</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @return                    the <code>Resource Object</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public String resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Called to inject the <code>IT Resource</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @param  server             the <code>IT Resource</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void server(final String server) {
    this.server = server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Returns the <code>IT Resource</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @return                    the <code>IT Resource</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public String server() {
    return this.server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSet
  /**
   ** Called to inject the <code>Request DataSet</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @param  dataSet            the <code>Request DataSet</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void dataSet(final String dataSet) {
    this.dataSet = dataSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSet
  /**
   ** Returns the <code>Request DataSet</code> an
   ** <code>Application Instance</code> belongs to in Identity Manager.
   **
   ** @return                    the <code>Request DataSet</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public String dataSet() {
    return this.dataSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>entitlement</code>.
   **
   ** @param  entitlement        <code>true</code> if the
   **                            <code>Application Instance</code> is applicable
   **                            to <code>Entitlement</code>s; otherwise
   **                            <code>false</code>.
   */
  public void entitlement(final boolean entitlement) {
    this.entitlement(Boolean.valueOf(entitlement));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>entitlement</code>.
   **
   ** @param  entitlement        <code>true</code> if the
   **                            <code>Application Instance</code> is applicable
   **                            to <code>Entitlement</code>s; otherwise
   **                            <code>false</code>.
   */
  public void entitlement(final Boolean entitlement) {
    this.entitlement = entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Returns <code>true</code> if the <code>Application Instance</code> is
   ** is applicable to <code>Entitlement</code>s; otherwise <code>false</code>.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Application Instance</code> is applicable
   **                            to <code>Entitlement</code>s; otherwise
   **                            <code>false</code>.
   */
  public final Boolean entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of an application instance like display name,
   ** description, etc. If it's <code>false</code> only the name of the
   ** application instance has to be present.
   **
   ** @param  operation          the operational mode of validation.
   **                            If it's set to <code>create</code> the
   **                            validation is extended to check for all the
   **                            mandatory parameters of an application like
   **                            displayName etc. If it's something else only
   **                            the name of the application has to be present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate(final ServiceOperation operation)
    throws BuildException {

    // enforce validation of mandatory attributes if requested
    if (operation == ServiceOperation.create) {
      if (StringUtility.isEmpty(this.resource))
        handleAttributeMissing("resource");

      if (StringUtility.isEmpty(this.server))
        handleAttributeMissing("server");

      if (StringUtility.isEmpty(this.dataSet))
        handleAttributeMissing("dataSet");
    }

    // ensure inhertitance
    super.validate(operation);
  }
}