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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AccessServerInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessServerInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.HashMap;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AccessServerInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessServerInstance</code> represents an Access Server in Oracle
 ** Access Manager infrastructure that might be created, deleted or configured
 ** after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessServerInstance extends ConfigurationInstance {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessServerInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessServerInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSignature (ConfigurationInstannce)
  /**
   ** Returns operation's signature string accordingly to the create parameter
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  @Override
  protected final String[] createSignature() {
    return AccessServerProperty.SIGNATURE_CREATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createParameter (ConfigurationInstannce)
  /**
   ** Returns operation's parameter string accordingly to the create signature
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  @Override
  public final Object[] createParameter() {
    final Object[] parameter = new Object[]  {
      //  0: name
      name()
      //  1: siteName
    , stringParameter(AccessServerProperty.SITENAME.id)
      //  2: host
    , stringParameter(AccessServerProperty.SERVERHOST.id)
      //  3: port
    , stringParameter(AccessServerProperty.SERVERPORT.id)
      //  4: proxyPort
    , stringParameter(AccessServerProperty.PROXYPORT.id)
      //  5: proxyServer
    , stringParameter(AccessServerProperty.PROXYHOST.id)
      //  6: proxyPort again
    , stringParameter(AccessServerProperty.PROXYPORT.id)
    };
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifySignature
  /**
   ** Returns operation's signature string accordingly to the modify parameter
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  public final String[] modifySignature() {
    return AccessServerProperty.SIGNATURE_MODIFY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyParameter ConfigurationInstannce)
  /**
   ** Returns operation's parameter string accordingly to the create signature
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  @Override
  public final Object[] modifyParameter() {
    final Object[] parameter = new Object[]  {
      //  0: name
      name()
      //  1: siteName
    , stringParameter(AccessServerProperty.SITENAME.id)
      //  2: host
    , stringParameter(AccessServerProperty.SERVERHOST.id)
      //  3: port
    , stringParameter(AccessServerProperty.SERVERPORT.id)
      //  4: proxyPort
    , stringParameter(AccessServerProperty.PROXYPORT.id)
      //  5: proxyServer
    , stringParameter(AccessServerProperty.PROXYHOST.id)
      //  6: proxyPort again
    , stringParameter(AccessServerProperty.PROXYPORT.id)
    };
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  property           the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>property</code> to set on
   **                            this instance.
   **
   ** @throws BuildException     if the specified property id is already part of
   **                            the parameter mapping.
   */
  public void add(final AccessServerProperty property, final String value)
    throws BuildException {

    // validate basic requirements
    if (property.required && StringUtility.isEmpty(value))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, property.id));

    // ensure inheritance and apply further validation
    super.addParameter(property.id, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    // validate strictly for create to avoid side effects
    validate(ServiceOperation.create);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @param  operation          the {@link ServiceOperation} to validate for
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#modify}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#print}
   **                            </ul>
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation) {
    // ensure inheritance
    super.validate();

    // only create and modify commands requires specific mandatory attributes
    if (operation == ServiceOperation.delete || operation == ServiceOperation.print)
      return;

    // validate that at least one parameter is specified for configuration
    // purpose
    final HashMap<String, Object> parameter = this.parameter();
    if (parameter.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.TYPE_PARAMETER_EMPTY));
  }
}