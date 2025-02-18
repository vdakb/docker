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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Frontend Extension
    Subsystem   :   Embedded Credential Collector

    File        :   ConfigurationServiceMXBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ConfigurationServiceMXBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.ecc.mbean;

import java.util.Map;
import java.util.List;

import oracle.as.jmx.framework.annotations.Name;
import oracle.as.jmx.framework.annotations.Visibility;
import oracle.as.jmx.framework.annotations.Description;
import oracle.as.jmx.framework.annotations.VisibilityLevel;
import oracle.as.jmx.framework.annotations.GlobalSecurityRole;
import oracle.as.jmx.framework.annotations.OperationRequiredGlobalSecurityRole;
import oracle.as.jmx.framework.annotations.OperationRequiredGlobalSecurityRoles;
import oracle.as.jmx.framework.annotations.AttributeSetterRequiredGlobalSecurityRole;
import oracle.as.jmx.framework.annotations.AttributeGetterRequiredGlobalSecurityRoles;

////////////////////////////////////////////////////////////////////////////////
// interface ConfigurationServiceMXBean
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** MXBean declaration for managing Access Manager Credential Collector
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Description(resourceKey="ecc.configuration", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
public interface ConfigurationServiceMXBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String          BUNDLE = "bka.iam.platform.access.resources.ServiceBundle";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAuthentication
  /**
   ** Sets the URI invoked from the <b>Sign In</b> form to authenticate an
   ** account leveraging Access Manager capabilities.
   **
   ** @param  value              the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Allowed object is {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @AttributeSetterRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="ecc.authentication", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void setAuthentication(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAuthentication
  /**
   ** Returns the URI invoked from the <b>Sign In</b> form to authenticate an
   ** account leveraging Access Manager capabilities.
   **
   ** @return                    the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Possible object is {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @AttributeGetterRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  String getAuthentication();

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setResetPassword
  /**
   ** Sets the URI invoked from the <b>Forgot Password</b> form to reset the
   ** password and send it to the specified e-Mail Address.
   **
   ** @param  value              the URI invoked from the <b>Forgot Password</b>
   **                            form to reset the password and send it to the
   **                            specified e-Mail Address.
   **                            Allowed object is {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @AttributeSetterRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="ecc.resetpassword", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void setResetPassword(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getResetPassword
  /**
   ** Returns the URI invoked from the <b>Forgot Password</b> form to reset the
   ** password and send it to the specified e-Mail Address.
   **
   **
   ** @return                    the URI invoked from the <b>Forgot Password</b>
   **                            form to reset the password and send it to the
   **                            specified e-Mail Address.
   **                            Possible object is {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @AttributeGetterRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  String getResetPassword();

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPwrHost
  /**
   ** Sets the URI invoked from the Sign In password forgot link which
   ** redirects to the password reset application.
   **
   ** @param  value              the URI invoked from the Sign In password
   **                            forgot link which redirects to the password
   **                            reset application.
   **                            Allowed object is {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @AttributeSetterRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="ecc.pwrhost", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void setPwrHost(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPwrHost
  /**
   ** Returns the URI invoked from the Sign In password forgot link which
   ** redirects to the password reset application.
   **
   ** @return                    the URI invoked from the Sign In password
   **                            forgot link which redirects to the password
   **                            reset application.
   **                            Possible object is {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @AttributeGetterRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  String getPwrHost();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  @Description(resourceKey="ecc.action.refresh", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void refresh();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save
  /**
   ** Save the configuration to the file system.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="ecc.action.save", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void save();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationList
  /**
   ** List the locations actually stored the configuration.
   **
   ** @return                    the locations actually stored the
   **                            configuration.
   **                            Possible object {@link List} where each element
   **                            is a {@link Map} of tagged value pairs.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  @Description(resourceKey="ecc.action.list", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  List<Map<String, String>> locationList();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRegister
  /**
   ** Register a new location in the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  symbol             name of a symbol to identify the location by
   **                            end-users.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="ecc.action.register", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void locationRegister(final @Name("cidr") String cidr, final @Name("symbol") String symbol);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRemove
  /**
   ** Removes a location from the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="ecc.action.remove", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void locationRemove(final @Name("cidr") String cidr);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationModify
  /**
   ** Modifies a location in the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  symbol             name of a symbol to identify the location by
   **                            end-users.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="ecc.action.modify", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void locationModify(final @Name("cidr") String cidr, final @Name("symbol") String symbol);
}