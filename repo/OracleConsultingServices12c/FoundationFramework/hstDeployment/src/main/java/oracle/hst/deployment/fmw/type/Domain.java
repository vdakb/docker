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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Domain.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Domain.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.fmw.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;

import oracle.hst.deployment.spi.EnvironmentHandler;

////////////////////////////////////////////////////////////////////////////////
// class Domain
// ~~~~~ ~~~~~~
/**
 ** All WebLogic Server MBeans have a name, a type and a domain. These
 ** attributes are reflected in the MBean's JMX Object Name. The Object Name is
 ** the unique identifier for a given MBean across all domains, and has the
 ** following structure:
 ** <pre>
 **   domain name:Name=name,Type=type[,attr=value]...
 ** </pre>
 ** Name is a unique identifier for a given domain and type of MBean.
 ** <p>
 ** This class implements the domain part of the unique identifier of a MBean.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Domain extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final EnvironmentHandler.Domain delegate = new EnvironmentHandler.Domain();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Domain</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Domain() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the bean in Oracle WebLogic Domain
   **                            to handle.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link EnvironmentHandler.Domain} delegate of Oracle WebLogic
   ** Domain to handle.
   **
   ** @return                    the {@link EnvironmentHandler.Bean} delegate
   **                            of Oracle WebLogic Domain entity instance.
   */
  public final EnvironmentHandler.Domain instance() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredBean
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>bean</code>.
   **
   ** @param  bean               a fully qualified MBean to manage.
   **
   ** @throws BuildException     if the specified type is already part of the
   **                            parameter mapping.
   */
  public void addConfiguredBean(final Bean bean)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.add(bean.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredType
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>type</code>.
   **
   ** @param  type               a fully qualified metadata document to export,
   **                            import or delete.
   **                            <p>
   **                            This can be specified by a comma-separated list
   **                            of fully qualified metadata documents or a
   **                            document name pattern that is used to identify
   **                            which documents to handled.
   **
   ** @throws BuildException     if the specified type is already part of the
   **                            parameter mapping.
   */
  public void addConfiguredType(final Type type)
    throws BuildException {

    checkChildrenAllowed();
    this.delegate.add(type.instance());
  }
}