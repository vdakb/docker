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

    File        :   ConfigurationService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigurationService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.idp.mbean;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.File;

import oracle.as.jmx.framework.annotations.Name;

import oracle.as.jmx.framework.util.RuntimeMBeanSupport;

import bka.iam.platform.access.idp.type.Subnet;
import bka.iam.platform.access.idp.type.Provider;
import bka.iam.platform.access.idp.type.Serializer;
import bka.iam.platform.access.idp.type.OTPTemplate;
import bka.iam.platform.access.idp.type.Configuration;

import bka.iam.platform.access.resources.DiscoveryBundle;

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

  private static final String FILE    = "idp-config.xml";
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
   **                            <br>
   **                            Possible object is {@link Configuration}.
   */
  public Configuration storage() {
    return this.storage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

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
      // parse the configuration
      Serializer.unmarshal(this.storage, PATH.toURI().toURL());
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerList (ConfigurationServiceMXBean)
  /**
   ** List the providers actually stored the configuration.
   **
   ** @return                    the providers actually stored the
   **                            configuration.
   */
  @Override
  public final List<Map<String, String>> providerList() {
    final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    for (Provider entry : this.storage.provider().values()) {
      final Map<String, String> row = new LinkedHashMap<String, String>();
      row.put("id",      entry.id());
      row.put("name",    entry.name());
      row.put("symbol",  entry.symbol());
      row.put("partner", entry.partner());
      data.add(row);
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerRegister (ConfigurationServiceMXBean)
  /**
   ** Adds a provider to the configuration.
   **
   ** @param id                  the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  name               the name of the Identity Provider to
   **                            identify the provider public.
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
  @Override
  public final void providerRegister(final String id, final @Name("name") String name, final String symbol, final String partner) {
    // verify if the location is not registered in the configuration
    if (this.storage.provider().containsKey(id))
      throw new RuntimeException(DiscoveryBundle.string("idp.error.register"));

    // set the new provider at last in the configuration storage
    this.storage.provider().put(id, Provider.of(id, name, symbol, partner));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerRemove (ConfigurationServiceMXBean)
  /**
   ** Removes the providers from the configuration.
   **
   ** @param id                  the identifier of the Identity Provider to
   **                            identify the provider internally.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Override
  public final void providerRemove(final String id) {
    if (this.storage.provider().containsKey(id)) {
      this.storage.provider().remove(id);
    }
    else {
      throw new RuntimeException(DiscoveryBundle.string("idp.error.remove"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerModify (ConfigurationServiceMXBean)
  /**
   ** Adds a provider to the configuration.
   **
   ** @param id                  the identifier of the Identity Provider to
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
  @Override
  public final void providerModify(final String id, final @Name("name") String name, final String symbol, final String partner) {
    if (this.storage.provider().containsKey(id)) {
      // modify the provider at last in the configuration storage
      this.storage.provider().put(id, Provider.of(id, name, symbol, partner));
    }
    else {
      throw new RuntimeException(DiscoveryBundle.string("idp.error.modify"));
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
    for (Map.Entry<Subnet, Provider> entry : this.storage.network().entrySet()) {
      final Map<String, String> row = new LinkedHashMap<String, String>();
      row.put("cidr",    entry.getKey().cidr());
      row.put("partner", entry.getValue().id());
      data.add(row);
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRegister (ConfigurationServiceMXBean)
  /**
   ** Adds a location to the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  id                 the identifier contain the IdP's
   **                            ProviderID/Issuer configuration.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Override
  public final void locationRegister(final String cidr, final String id) {
    final Subnet network = Subnet.build(cidr);
    // verify if the location is not registered in the configuration
    if (this.storage.network().containsKey(network))
      throw new RuntimeException(DiscoveryBundle.string("net.error.register"));

    // verify if the partner is already registered in the configuration
    final Provider provider = this.storage.provider().get(id);
    if (provider == null)
      throw new RuntimeException(DiscoveryBundle.string("net.error.id"));

    // set the new location at last in the configuration storage
    this.storage.network().put(Subnet.build(cidr), provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRemove (ConfigurationServiceMXBean)
  /**
   ** Removes a location from the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Override
  public final void locationRemove(final String cidr) {
    final Subnet subject = Subnet.build(cidr);
    if (this.storage.network().containsKey(subject)) {
      this.storage.network().remove(subject);
    }
    else {
      throw new RuntimeException(DiscoveryBundle.string("net.error.remove"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationModify
  /**
   ** Updated a location to the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  id                 the identifier contain the IdP's
   **                            ProviderID/Issuer identifier.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  @Override
  public final void locationModify(final String cidr, final String id) {
    final Subnet subject = Subnet.build(cidr);
    if (this.storage.network().containsKey(subject)) {
      // verify if the id is already registered in the configuration
      final Provider provider = this.storage.provider().get(id);
      if (provider == null)
        throw new RuntimeException(DiscoveryBundle.string("net.error.id"));

      // modify the location in the configuration storage
      this.storage.network().put(subject, provider);
    }
    else {
      throw new RuntimeException(DiscoveryBundle.string("net.error.modify"));
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////
  // Method:   otpTemplateList (ConfigurationServiceMXBean)
  /**
   ** List the locations actually stored the configuration.
   **
   ** @return                    the OTP Templates actually stored the
   **                            configuration.
   */
  @Override
  public final List<Map<String, String>> otpTemplateList() {
    final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    for (OTPTemplate template : this.storage.otpTemplate()) {
      final Map<String, String> row = new LinkedHashMap<String, String>();
      row.put("locale"   , template.getLocale());
      row.put("subject"  , template.getSubject());
      row.put("body"     , template.getBody());
      data.add(row);
    }
    return data;
  }
    

  //////////////////////////////////////////////////////////////////////////////
  // Method:   otpTemplateRegister
  /**
   ** Register a new OTP Template in the configuration.
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
  public final void otpTemplateRegister(final String locale, final String subject, final String body) {
    final OTPTemplate template = new OTPTemplate(locale, subject, body);
    // verify if the location is not registered in the configuration
    if (this.storage.otpTemplate().contains(template))
      throw new RuntimeException(DiscoveryBundle.string("otp.error.register"));
     // set the new location at last in the configuration storage
    this.storage.otpTemplate().add(template);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRemove (ConfigurationServiceMXBean)
  /**
   ** Remove the OTP Tempalte actually stored the configuration.
   **
   */
  @Override
  public final void otpTemplateRemove(final String locale) {
    final OTPTemplate tempalte = new OTPTemplate(locale);
    if (this.storage.otpTemplate().contains(tempalte)) {
      this.storage.otpTemplate().remove(tempalte);
    }
    else {
      throw new RuntimeException(DiscoveryBundle.string("otp.error.remove"));
    }
  }

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
  @Override
  public final void otpTemplateModify(final String locale, final String subject, final String body) {
    final OTPTemplate tempalte = new OTPTemplate(locale, subject, body);
    if (this.storage.otpTemplate().contains(tempalte)) {
      this.storage.otpTemplate().remove(tempalte);  
      this.storage.otpTemplate().add(tempalte);
    }
    else {
      throw new RuntimeException(DiscoveryBundle.string("otp.error.modify"));
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