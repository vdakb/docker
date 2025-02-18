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

    File        :   FederationPartnerInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FederationPartnerInstance.


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
// class FederationPartnerInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>FederationPartnerInstance</code> represents a Fedartion Partner,
 ** either Identity Provider (IdP) or Service Provider (SP) in an Oracle Access
 ** Manager infrastructure.
 ** <p>
 ** An <code>Identity Provider</code> (abbreviated IdP or IDP) is a system
 ** entity that creates, maintains, and manages identity information for
 ** principals while providing authentication services to relying applications
 ** within a federation or distributed network.
 ** <br>
 ** An <code>Identity Provider</code> is "a trusted provider that lets you use
 ** single sign-on (SSO) to access other websites." SSO enhances usability by
 ** reducing password fatigue. It also provides better security by decreasing
 ** the potential attack surface.
 ** <br>
 ** Identity providers can facilitate connections between cloud computing
 ** resources and users, thus decreasing the need for users to re-authenticate
 ** when using mobile and roaming applications.
 ** <p>
 ** A <code>Service Provider</code> (abbreviated SP) is a system entity that
 ** receives and accepts authentication assertions in conjunction with a single
 ** sign-on (SSO) profile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FederationPartnerInstance extends ConfigurationInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected FederationPartnerType type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FederationPartnerInstance</code> task that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FederationPartnerInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Evaluates the location to the configuration item from the top root element
   ** until the <code>item</code>.
   **
   ** @return                    path to the configuration item from the top
   **                            .root element
   */
  public String location() {
    return this.type.location();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type of the instance.
   **
   ** @param  type               the type of the instance to set.
   */
  public void type(final FederationPartnerType type) {
    this.type = type;
    name(this.type.id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the instance.
   **
   ** @return                    the type of the instance.
   */
  public FederationPartnerType type() {
    return this.type;
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
    return FederationPartnerProperty.SIGNATURE_CREATE;
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
    return FederationPartnerProperty.SIGNATURE_MODIFY;
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
  public void add(final FederationPartnerProperty property, final String value)
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


    // initialize instance with default values if a value is not provided by
    // the frontend for a parameter which has a default
    for (FederationPartnerProperty cursor : FederationPartnerProperty.values()) {
      if (cursor.required() && cursor.defaultValue() != null && !parameter.containsKey(cursor.id))
        add(cursor, cursor.defaultValue());
    }

    // validate all required parameter
    // the frontend for a parameter which has a default
    for (FederationPartnerProperty cursor : FederationPartnerProperty.values()) {
      if (cursor.required() && !parameter.containsKey(cursor.id))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, cursor.id));
    }
  }
}