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

    File        :   Permission.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Permission.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.type;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.security.jps.mas.mgmt.jmx.policy.PortablePermission;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceDataType;

//////////////////////////////////////////////////////////////////////////////
// class Permission
// ~~~~~ ~~~~~~~~~~
public class Permission extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String resource;
  private String clazz;
  private String action;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Policy
  // ~~~~~ ~~~~~~
  /**
   ** <code>Policy</code> defines the container to accumulate permissions to
   ** be granted or revoked.
   */
  public static class Policy extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final List<Permission> permission = new ArrayList<Permission>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Policy</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    protected Policy() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   setResource
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>resource</code>.
     **
     ** @param  resource         the name of the resource to handle in Oracle
     **                          WebLogic Server Domain.
     */
    public void setResource(final String resource) {
      checkAttributesAllowed();

      if (this.permission.isEmpty())
         this.permission.add(new Permission());
      final Permission permission = this.permission.get(0);
      permission.resource = resource;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setClass
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** permission <code>class</code>.
     **
     ** @param  clazz            the permission class of the bean to handle in
     **                          Oracle WebLogic Server Domain.
     */
    public void setClass(final String clazz) {
      checkAttributesAllowed();
      if (this.permission.isEmpty())
         this.permission.add(new Permission());
      final Permission permission = this.permission.get(0);
      permission.clazz = clazz;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setAction
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** permission <code>action</code>.
     **
     ** @param  action           the permission action of the bean to handle
     **                          in Oracle WebLogic Server Domain.
     */
    public void setAction(final String action) {
      checkAttributesAllowed();
      if (this.permission.isEmpty())
         this.permission.add(new Permission());
      final Permission permission = this.permission.get(0);
      permission.action = action;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: permission
    /**
     ** Returns the accumulated {@link PortablePermission}s.
     **
     ** @return                    the accumulated {@link PortablePermission}s.
     */
    public final Set<PortablePermission> permission() {
      final Set<PortablePermission> permission = new HashSet<PortablePermission>(this.permission.size());
      for (Permission entry : this.permission)
        permission.add(entry.portable());
      return permission;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredPermission
    /**
     ** Call by the ANT deployment to inject the argument for nested parameter
     ** <code>permission</code>.
     **
     ** @param  permission         the {@link Permission} to grant.
     **
     ** @throws BuildException     if a {@link Permission} has missing data.
     */
    public void addConfiguredPermission(final Permission permission)
      throws BuildException {

      checkChildrenAllowed();
      this.permission.add(permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Assign
  // ~~~~~ ~~~~~~
  /**
   ** <code>Assign</code> defines the container to accumulate permissions to
   ** be granted to a policy.
   */
  public static class Assign extends Policy {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Assign</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Assign() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Remove
  // ~~~~~ ~~~~~~
  /**
   ** <code>Remove</code> defines the container to accumulate permissions to
   ** be revoked from a policy.
   */
  public static class Remove extends Policy {

    /////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Remove</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Remove() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Permission</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Permission() {
    // ensure inheritance
    super();
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
   ** @param  resource           the name of the resource to handle in Oracle
   **                            WebLogic Server Domain.
   */
  public void setResource(final String resource) {
    checkAttributesAllowed();
    this.resource = resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClass
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** permission <code>class</code>.
   **
   ** @param  clazz              the permission class of the bean to handle in
   **                            Oracle WebLogic Server Domain.
   */
  public void setClass(final String clazz) {
    checkAttributesAllowed();
    this.clazz = clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAction
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** permission <code>action</code>.
   **
   ** @param  action             the permission action of the bean to handle
   **                            in Oracle WebLogic Server Domain.
   */
  public void setAction(final String action) {
    checkAttributesAllowed();
    this.action = action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   portable
  /**
   ** Returns a {@link PortablePermission} configured with the properties of
   ** this {@link ServiceDataType}.
   **
   ** @return                    a {@link PortablePermission} configured with
   **                            the properties of this {@link ServiceDataType}.
   **
   ** @throws BuildException     if some data are missing.
   */
  public final PortablePermission portable()
    throws BuildException {

    if (StringUtility.isEmpty(this.resource))
      handleAttributeMissing("resource");

    if (StringUtility.isEmpty(this.clazz))
      handleAttributeMissing("class");

    return new PortablePermission(this.clazz, this.resource, StringUtility.isEmpty(this.action) ? null : this.action);
  }
}