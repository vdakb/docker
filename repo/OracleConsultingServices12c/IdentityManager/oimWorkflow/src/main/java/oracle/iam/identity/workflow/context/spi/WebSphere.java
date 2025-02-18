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
    Subsystem   :   Common Shared Workflow Facility

    File        :   WebSphere.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    WebSphere.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.context.spi;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.workflow.context.ServiceProvider;

public class WebSphere extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the logical type of this {@link ServiceProvider}. */
  public static final String CONTEXT_FACTORY  = "com.ibm.Websphere.naming.WsnInitialContextFactory";

  public static final String DEFAULT_PROTOCOL = "iiop";
  public static final String DEFAULT_PORT     = "2809";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebSphere</code>  {@link ServiceProvider} that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public WebSphere() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol (AbstractServiceProvider)
  /**
   ** Returns the <code>protocol</code> attribute for the RMI/JNDI provider.
   **
   ** @return                    the <code>protocol</code> attribute for the
   **                            RMI/JNDI provider.
   */
  @Override
  protected String protocol() {
    return DEFAULT_PROTOCOL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port (AbstractServiceProvider)
  /**
   ** Returns the <code>port</code> attribute for the RMI/JNDI provider.
   ** <p>
   ** Subclasses must override this implementation.
   **
   ** @return                    the <code>port</code> attribute for the
   **                            RMI/JNDI provider.
   */
  @Override
  protected String port() {
    return DEFAULT_PORT;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   contextFactory (AbstractServiceProvider)
  /**
   ** Returns the value of the context factory passed to the instantiazion of an
   ** JNDI Context.
   **
   ** @return                    the value of the context factory passed to the
   **                            instantiazion of an JNDI Context.
   */
  @Override
  protected final String contextFactory() {
    return CONTEXT_FACTORY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packagePrefix ((AbstractServiceProvider))
  /**
   ** Returns the package prefix of the URL to locate the
   ** <code>InitialContext</code>.
   ** <p>
   ** The prefix consists of the URL scheme id and a suffix to construct the
   ** class name, as follows:
   ** <pre>
   **   prefix.schemeId.schemeIdURLContextFactory
   ** </pre>
   ** This property is used when a URL name is passed to the
   ** <code>InitialContext</code> (in the API reference documentation) methods.
   **
   ** @return                    the value of the context factory passed to the
   **                            instantiazion of an JNDI Context.
   */
  @Override
  protected final String packagePrefix() {
    return SystemConstant.EMPTY;
  }
}