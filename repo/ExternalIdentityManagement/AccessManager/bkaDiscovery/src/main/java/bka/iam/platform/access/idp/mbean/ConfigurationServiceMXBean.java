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
    Subsystem   :   Identity Provider Discovery

    File        :   ConfigurationServiceMXBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ConfigurationServiceMXBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.idp.mbean;

import java.util.Map;
import java.util.List;

import oracle.as.jmx.framework.annotations.Name;
import oracle.as.jmx.framework.annotations.Visibility;
import oracle.as.jmx.framework.annotations.Description;
import oracle.as.jmx.framework.annotations.VisibilityLevel;
import oracle.as.jmx.framework.annotations.GlobalSecurityRole;
import oracle.as.jmx.framework.annotations.OperationRequiredGlobalSecurityRole;
import oracle.as.jmx.framework.annotations.OperationRequiredGlobalSecurityRoles;

////////////////////////////////////////////////////////////////////////////////
// interface ConfigurationServiceMXBean
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** MXBean declaration for managing Access Manager Identity Provider Discovery
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Description(resourceKey="cfg.configuration", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
public interface ConfigurationServiceMXBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String          BUNDLE = "bka.iam.platform.access.resources.DiscoveryBundle";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  @Description(resourceKey="cfg.action.refresh", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void refresh();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save
  /**
   ** Save the configuration to the file system.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="cfg.action.save", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void save();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerList
  /**
   ** List the providers actually stored the configuration.
   **
   ** @return                    the providers actually stored the
   **                            configuration.
   **                            <br>
   **                            Possible object {@link List} where each element
   **                            is a {@link Map} of tagged value pairs.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  @Description(resourceKey="idp.action.list", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  List<Map<String, String>> providerList();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerRegister
  /**
   ** Adds a provider to the configuration.
   **
   ** @param  id                 the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  name               the name of the Identity Provider to identify
   **                            the provider public.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  symbol             the name of a symbol to identify the provider
   **                            by end-users.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  partner            the name contain the IdP's ProviderID/Issuer
   **                            onfiguration when redirecting the user back to
   **                            the SP.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="idp.action.register", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void providerRegister(final @Name("id") String id, final @Name("name") String name, final @Name("symbol") String symbol, final @Name("partner") String partner);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerRemove
  /**
   ** Removes a provider from the configuration.
   **
   ** @param  id                 the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="idp.action.remove", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void providerRemove(final @Name("id") String id);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerModify
  /**
   ** Modify a provider to the configuration.
   **
   ** @param  id                 the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  name               the name of the Identity Provider to identify
   **                            the provider public.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  symbol             the name of a symbol to identify the provider
   **                            by end-users.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  partner            the name contain the IdP's ProviderID/Issuer
   **                            onfiguration when redirecting the user back to
   **                            the SP.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="idp.action.modify", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void providerModify(final @Name("id") String id, final @Name("name") String name, final @Name("symbol") String symbol, final @Name("partner") String partner);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationList
  /**
   ** List the locations actually mapped in the configuration.
   **
   ** @return                    the locations actually mapped in the
   **                            configuration.
   **                            <br>
   **                            Possible object {@link List} where each element
   **                            is a {@link Map} of tagged value pairs.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRoles({GlobalSecurityRole.Admin, GlobalSecurityRole.Monitor})
  @Description(resourceKey="net.action.list", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  List<Map<String, String>> locationList();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRegister
  /**
   ** Adds a location to the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  id                 the identifier contain the IdP's
   **                            ProviderID/Issuer identifier.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="net.action.register", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void locationRegister(final @Name("cidr") String cidr, final @Name("id") String id);

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
  @Description(resourceKey="net.action.remove", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void locationRemove(final @Name("cidr") String cidr);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationModify
  /**
   ** Modify a location to the configuration.
   **
   ** @param  id                 the identifier of the Identity Provider to
   **                            identify the location internally.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  id                 the identifier contain the IdP's
   **                            ProviderID/Issuer configuration.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="net.action.modify", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void locationModify(final @Name("cidr") String cidr, final @Name("id") String id);
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   otpTemplateList
  /**
   ** List the locations actually stored the configuration.
   **
   ** @return                    the OTP Templates actually stored the
   **                            configuration.
   **                            Possible object {@link List} where each element
   **                            is a {@link Map} of tagged value pairs.
   */
  @Visibility(VisibilityLevel.Basic)
  //@OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Monitor)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.NONE)
  @Description(resourceKey="otp.action.list", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  List<Map<String, String>> otpTemplateList();
     

  //////////////////////////////////////////////////////////////////////////////
  // Method:   otpTemplateRegister
  /**
   ** Register a new otpTemplate in the configuration.
   **
   ** @param  locale             End user locale string e.g. de or en
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  subject            Subject of the email template
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  body               Email body of the temlate
   **                            <br>
   **                            Allowed object {@link String}
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="otp.action.register", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void otpTemplateRegister(final @Name("locale") String locale, 
                           final @Name("subject") String subject, 
                           final @Name("body") String body);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   otpTemplateRemove
  /**
   ** Removes a otpTemplate from the configuration.
   **
   ** @param  locale             End user locale string e.g. de or en
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="otp.action.remove", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void otpTemplateRemove(final @Name("locale") String locale);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   otpTemplateModify
  /**
   ** Modifies a otpTemplate in the configuration.
   **
   ** @param  locale             End user locale string e.g. de or en
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  subject            Subject of the email template
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  body               Email body of the temlate
   **                            <br>
   **                            Allowed object {@link String}
   */
  @Visibility(VisibilityLevel.Basic)
  @OperationRequiredGlobalSecurityRole(GlobalSecurityRole.Admin)
  @Description(resourceKey="otp.action.modify", resourceBundleBaseName=ConfigurationServiceMXBean.BUNDLE)
  void otpTemplateModify(final @Name("locale")  String locale, 
                         final @Name("subject") String subject, 
                         final @Name("subject") String body);
}