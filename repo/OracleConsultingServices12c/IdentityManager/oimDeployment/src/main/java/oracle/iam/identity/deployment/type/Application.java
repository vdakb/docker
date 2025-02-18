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

    File        :   Application.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Application.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import oracle.iam.identity.common.spi.ApplicationData;

////////////////////////////////////////////////////////////////////////////////
// class Application
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>Application</code> represents a application instance in Oracle
 ** Identity Manager that might be created, updated or deleted after or during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Application extends Entitlement {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Application() {
    // ensure inheritance
    super(new ApplicationData());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entitlement</code> task use the specified
   ** {@link ApplicationData} to store the ANT attributes.
   ** <p>
   ** Commonly used by sub classes to pass in their own class of an
   ** {@link ApplicationData}.
   **
   ** @param  instance           the {@link ApplicationData} to store the ANT
   **                            attributes.
   */
  protected Application(final ApplicationData instance) {
    // ensure inheritance
    super(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResource
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>resource</code>.
   **
   ** @param  resource           the <code>Resource Object</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setResource(final String resource) {
    checkAttributesAllowed();
    ((ApplicationData)this.delegate).resource(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setServer
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>server</code>.
   **
   ** @param  server             the <code>IT Resource</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setServer(final String server) {
    checkAttributesAllowed();
    ((ApplicationData)this.delegate).server(server);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDataSet
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>dataSet</code>.
   **
   ** @param  dataSet            the <code>Request DataSet</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setDataSet(final String dataSet) {
    checkAttributesAllowed();
    ((ApplicationData)this.delegate).dataSet(dataSet);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setParent
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>parent</code>.
   **
   ** @param  parent             the parent <code>Application Instance</code> an
   **                            <code>Application Instance</code> belongs to.
   */
  public void setParent(final String parent) {
    checkAttributesAllowed();
    ((ApplicationData)this.delegate).parent(parent);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntitlement
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>entitlement</code>.
   **
   ** @param  entitlement        <code>true</code> if the
   **                            <code>Application Instance</code> is applicable
   **                            to <code>Entitlement</code>s; otherwise
   **                            <code>false</code>.
   */
  public void setEntitlement(final boolean entitlement) {
    checkAttributesAllowed();
    ((ApplicationData)this.delegate).entitlement(entitlement);
  }
}