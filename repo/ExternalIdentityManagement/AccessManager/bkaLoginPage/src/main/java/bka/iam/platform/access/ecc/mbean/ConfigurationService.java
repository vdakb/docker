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

    File        :   ConfigurationService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigurationService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.ecc.mbean;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.File;

import oracle.as.jmx.framework.util.RuntimeMBeanSupport;

import bka.iam.platform.access.ecc.type.Subnet;
import bka.iam.platform.access.ecc.type.Serializer;
import bka.iam.platform.access.ecc.type.Configuration;
import bka.iam.platform.access.resources.ServiceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConfigurationService
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** MXBean implementation for managing Access Manager Credential Collector
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConfigurationService extends    RuntimeMBeanSupport
                                  implements ConfigurationServiceMXBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE    = "ecc-config.xml";
  private static final String CONFIG  = System.getProperty("oracle.domain.config.dir");
  private static final File   PATH    = new File(String.format("%s%c%s", CONFIG, File.separatorChar, FILE));

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Configuration storage;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ConfigurationService</code> backing bean that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ConfigurationService() {
    // ensure inheritance
    super(BUNDLE);

    // initialize instance attributes
    this.storage = Configuration.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   storage
  /**
   ** Returns the configuration instance this MBean manage.
   **
   ** @return                    the configuration instance this MBean manage.
   **                            Possible object is {@link Configuration}.
   */
  public Configuration storage() {
    return this.storage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   authentication
  /**
   ** Sets the attribute value for <code>AUTHENTICATION</code> using the alias
   ** name <code>authentication</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>AUTHENTICATION</code>
   **                            using the alias name <code>authentication</code>.
   **                            Allowed object is {@link String}.
   */
  protected void authentication(final String value) {
    this.storage.authentication(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   authentication
  /**
   ** Returns the attribute value for <code>AUTHENTICATION</code> using the alias
   ** name <code>authentication</code>.
   **
   ** @return                    the attribute value for
   **                            <code>AUTHENTICATION</code>
   **                            using the alias name <code>authentication</code>.
   **                            Possible object is {@link String}.
   */
  protected String authentication() {
    return this.storage.authentication();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   resetpassword
  /**
   ** Sets the attribute value for <code>RESETPASSWORD</code> using the alias
   ** name <code>resetpassword</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>RESETPASSWORD</code>
   **                            using the alias name <code>resetpassword</code>.
   **                            Allowed object is {@link String}.
   */
  public void resetpassword(final String value) {
    this.storage.resetpassword(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   resetpassword
  /**
   ** Returns the attribute value for <code>RESETPASSWORD</code> using the alias
   ** name <code>resetpassword</code>.
   **
   ** @return                    the attribute value for
   **                            <code>RESETPASSWORD</code>
   **                            using the alias name <code>resetpassword</code>.
   **                            Possible object is {@link String}.
   */
  public String resetpassword() {
    return this.storage.resetpassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   pwrhost
  /**
   ** Sets the attribute value for <code>PWRHOST</code> using the alias
   ** name <code>pwrhost</code>.
   **
   ** @param  value              the attribute value for
   **                            <code>PWRHOST</code>
   **                            using the alias name <code>pwrhost</code>.
   **                            Allowed object is {@link String}.
   */
  public void pwrhost(final String value) {
    this.storage.pwrhost(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   pwrhost
  /**
   ** Returns the attribute value for <code>PWRHOST</code> using the alias
   ** name <code>pwrhost</code>.
   **
   ** @return                    the attribute value for
   **                            <code>PWRHOST</code>
   **                            using the alias name <code>pwrhost</code>.
   **                            Possible object is {@link String}.
   */
  public String pwrhost() {
    return this.storage.pwrhost();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAuthentication (ConfigurationServiceMXBean)
  /**
   ** Sets the URI invoked from the <b>Sign In</b> form to authenticate an
   ** account leveraging Access Manager capabilities.
   **
   ** @param  value              the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setAuthentication(final String value) {
    authentication(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAuthentication (ConfigurationServiceMXBean)
  /**
   ** Returns the URI invoked from the <b>Sign In</b> form to authenticate an
   ** account leveraging Access Manager capabilities.
   **
   ** @return                    the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getAuthentication() {
    return authentication();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setResetPassword (ConfigurationServiceMXBean)
  /**
   ** Sets the URI invoked from the <b>Forgot Password</b> form to reset the
   ** password and send it to the specified e-Mail Address.
   **
   ** @param  value              the URI invoked from the <b>Forgot Password</b>
   **                            form to reset the password and send it to the
   **                            specified e-Mail Address.
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setResetPassword(final String value) {
    resetpassword(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getResetPassword (ConfigurationServiceMXBean)
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
  @Override
  public final String getResetPassword() {
    return resetpassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPwrHost (ConfigurationServiceMXBean)
  /**
   ** Sets the URI invoked from the Sign In password forgot link which
   ** redirects to the password reset application.
   **
   ** @param  value              the URI invoked from the Sign In password
   **                            forgot link which redirects to the password
   **                            reset application.
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setPwrHost(final String value) {
    pwrhost(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPwrHost (ConfigurationServiceMXBean)
  /**
   ** Returns the URI invoked from the Sign In password forgot link which
   ** redirects to the password reset application.
   **
   ** @return                    the URI invoked from the Sign In password
   **                            forgot link which redirects to the password
   **                            reset application.
   **                            Possible object is {@link String}.
   */
  @Override
  public String getPwrHost() {
    return pwrhost();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save (ConfigurationServiceMXBean)
  /**
   ** Save the configuration to the file system.
   */
  @Override
  public final void save() {
    try {
      Serializer.marshal(this.storage, PATH);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (ConfigurationServiceMXBean)
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Override
  public final void refresh() {
    // remove any settings
    this.storage.reset();
    try {
      Serializer.unmarshal(this.storage, PATH.toURI().toURL());
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationList (ConfigurationServiceMXBean)
  /**
   ** List the locations actually stored the configuration.
   **
   ** @return                    the locations actually stored the
   **                            configuration.
   */
  @Override
  public final List<Map<String, String>> locationList() {
    final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    for (Map.Entry<Subnet, String> entry : this.storage.network().entrySet()) {
      final Map<String, String> row = new LinkedHashMap<String, String>();
      row.put("cidr",   entry.getKey().cidr());
      row.put("symbol", entry.getValue());
      data.add(row);
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRegister
  /**
   ** Regsiters a location in the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  symbol             name of a symbol to identify the location by
   **                            end-users.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  public final void locationRegister(final String cidr, final String symbol) {
    final Subnet network = Subnet.build(cidr);
    // verify if the location is not registered in the configuration
    if (this.storage.network().containsKey(network))
      throw new RuntimeException(ServiceBundle.string("ecc.error.register"));

    // set the new location at last in the configuration storage
    this.storage.network().put(Subnet.build(cidr), symbol);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRemove (ConfigurationServiceMXBean)
  /**
   ** List the locations actually stored the configuration.
   **
   ** @return                    the locations actually stored the
   **                            configuration.
   */
  @Override
  public final void locationRemove(final String cidr) {
    final Subnet subject = Subnet.build(cidr);
    if (this.storage.network().containsKey(subject)) {
      this.storage.network().remove(subject);
    }
    else {
      throw new RuntimeException(ServiceBundle.string("ecc.error.remove"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationModify (ConfigurationServiceMXBean)
  /**
   ** Adds a location to the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   ** @param  symbol             name of a symbol to identify the location by
   **                            end-users.
   */
  @Override
  public final void locationModify(final String cidr, final String symbol) {
    final Subnet subject = Subnet.build(cidr);
    if (this.storage.network().containsKey(subject)) {
      this.storage.network().put(subject, symbol);
    }
    else {
      throw new RuntimeException(ServiceBundle.string("ecc.error.remove"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postRegister (overridden)
  /**
   ** Allows this MBean to perform any operations needed after having been
   ** registered in the MBean server.
   ** <p>
   ** Invoked by the MBean server.
   **
   ** @param  registrationDone   indicates whether or not the MBean has been
   **                            successfully registered in the MBean server.
   **                            The value <code>false</code> means that the
   **                            registration phase has failed.
   */
  @Override
  public void postRegister(final Boolean registrationDone) {
    if (registrationDone) {
      refresh();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postDeregister (overridden)
  /**
   ** Allows this MBean to perform any operations needed after having been
   ** unregistered in the MBean server.
   ** <p>
   ** Invoked by the MBean server.
   */
  @Override
  public void postDeregister() {
    save();
  }
}