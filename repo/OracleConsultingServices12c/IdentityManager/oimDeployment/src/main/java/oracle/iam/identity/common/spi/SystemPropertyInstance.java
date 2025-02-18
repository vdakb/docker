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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   SystemPropertyInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SystemPropertyInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Date;

import org.apache.tools.ant.BuildException;

import oracle.iam.conf.vo.SystemProperty;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class SystemPropertyInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>SystemPropertyInstance</code> represents a system property in Oracle
 ** Identity Manager that might be created, deleted or changed after or during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemPropertyInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  description;
  private String  value;
  private boolean loginRequired;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SystemPropertyInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SystemPropertyInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  description        the description assigned to a system property
   **                            in Identity Manager to handle.
   */
  public void description(final String description) {
    this.description = description;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the <code>name</code> assigned to a system property in Identity
   ** Manager.
   **
   ** @return                    the description assigned to a system property
   **                            in Identity Manager to handle.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Called to inject the argument for parameter <code>value</code>.
   **
   ** @param  value              the value of the system property in Identity
   **                            Manager to handle.
   */
  public void value(final String value) {
    this.value = value;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the system property in Identity Manager to handle.
   **
   ** @return                    the value of the system property in Identity
   **                            Manager to handle.
   */
  public final String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginRequired
  /**
   ** Called to inject the argument for parameter <code>login</code>.
   **
   ** @param  loginRequired      <code>true</code> login is required to read
   **                            the system property from Identity Manager;
   **                            otherwise <code>false</code>.
   */
  public void loginRequired(final boolean loginRequired) {
    this.loginRequired = loginRequired;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   loginRequired
  /**
   ** Returns <code>true</code> login is required to read the system property
   ** from Identity Manager.
   **
   ** @return                    <code>true</code> login is required to read
   **                            the system property from Identity Manager;
   **                            otherwise <code>false</code>.
   */
  public final boolean loginRequired() {
    return this.loginRequired;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toProperty
  /**
   ** Factory method to create a {@link SystemProperty} from the passeed in
   ** {@link SystemPropertyInstance} <code>instance</code>.
   **
   ** @param  instance           the {@link SystemPropertyInstance} to transform
   **                            into a {@link SystemProperty}.
   **
   ** @return                    the transformed {@link SystemProperty}.
   */
  public static SystemProperty toProperty(final SystemPropertyInstance instance) {
    final Date date = new Date();
    // create the instance to transfer the data to the remote server
    final SystemProperty property = new SystemProperty();
    property.setPtyKeyword(instance.name());
    property.setPtyName(instance.description);
    property.setPtyValue(instance.value);
    property.setPtyDataLevel("2");
    property.setPtyLoginrequired(instance.loginRequired ? "1" : null);
    property.setPtyCreate(date);
    property.setPtyUpdate(date);
    return property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>SystemPropertyInstance</code> object that
   ** represents the same <code>name</code> as this instance.
   **
   ** @param other               the object to compare this
   **                            <code>SystemPropertyInstance</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>SystemPropertyInstance</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof SystemPropertyInstance))
      return false;

    final SystemPropertyInstance another = (SystemPropertyInstance)other;
    return this.name().equals(another.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.name()))
      handleAttributeMissing("name");

    if (StringUtility.isEmpty(this.value))
      handleAttributeMissing("value");
  }
}