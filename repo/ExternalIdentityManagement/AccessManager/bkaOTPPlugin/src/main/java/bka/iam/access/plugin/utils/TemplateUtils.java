package bka.iam.access.plugin.utils;

import bka.iam.access.plugin.model.OTPTemplate;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import oracle.as.jmx.framework.PortableMBeanFactory;

import oracle.security.am.common.utilities.constant.Component;
import oracle.security.am.common.utilities.log.OAMLogger;


/**
 * List of the OTP Template Utils.
 * OTP Templates are read from MBean <code>bka.iam.platform:Name=config,Type=idp,Application=bkaDiscovery</code>
 */
public class TemplateUtils {


  protected Logger LOGGER = OAMLogger.getLogger(Component.PLGN);

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////
  static ObjectName MBEAN;
  private static String CLASS_NAME = TemplateUtils.class.getName();
  // Name of the MBean operation which return all configured templates
  static String OPERATION = "otpTemplateList";

  
  /**
   ** Connect to MBean <code>bka.iam.platform:Name=config,Type=idp,Application=bkaDiscovery</code> and with operation 
   ** <code>otpTemplateList</code> read OTP templates.<BR>
   **  In order to execute this call JMX policy needs to be created with this WLST commands<BR>
   **   connect()<BR>
   **   cd("/SecurityConfiguration/access/Realms/myrealm/Authorizers/XACMLAuthorizer")<BR>
   **   identifier="type=<jmx>, operation=invoke, application=, mbeanType=idp, target=otpTemplateList"<BR>
   **   roleStr="Grp(everyone)"<BR>
   **   mbean = cd("/SecurityConfiguration/access/Realms/myrealm/Authorizers/XACMLAuthorizer")<BR>
   **   mbean.createPolicy(identifier,roleStr)<BR>
   **<BR>
   **   NOTE: Prerequisite is that "Use Authorization Providers to Protect JMX Access" is enabled on<BR>
   **          "Security Realms" -> "myrealm" -> "Configuration" -> "General"<BR>
   **          <BR>
   **          
   ** @param localeLanguage User locale language, base on this information will be returned OTP Template
   ** @param defaultLanguage default language defined on plugin parameter EMAIL_DEFAULT_LANGUAGE
   ** @return
   **/
  public OTPTemplate getOTPTemplate(String emailTemplateMBeanName, String localeLanguage, String defaultLanguage) {
    String METHOD = "getPluginInitParameter";
    LOGGER.entering(CLASS_NAME, METHOD);
    
        
    OTPTemplate languageOTPTemplate = null;
    OTPTemplate defaultOTPTemplate = null;
    if (localeLanguage != null) {
      try {
	// Invoke MBean operation
	// In order to execute this call JMX policy needs to be created with this WLST commands
	//   connect()
	//   cd("/SecurityConfiguration/access/Realms/myrealm/Authorizers/XACMLAuthorizer")
	//   identifier="type=<jmx>, operation=invoke, application=, mbeanType=idp, target=otpTemplateList"
	//   roleStr="Grp(everyone)"
	//   mbean = cd("/SecurityConfiguration/access/Realms/myrealm/Authorizers/XACMLAuthorizer")
	//   mbean.createPolicy(identifier,roleStr)
	//
	//   NOTE: Prerequisite is that "Use Authorization Providers to Protect JMX Access" is enabled on
	//          "Security Realms" -> "myrealm" -> "Configuration" -> "General"
	final PortableMBeanFactory factory = new PortableMBeanFactory();
	LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,"Succesfully connected to MBeanFactory()");
	final MBeanServer runtime = factory.getMBeanServer();
	final ObjectName name = factory.translateObjectNameToGlobalNameSpace(new ObjectName(emailTemplateMBeanName));
	TabularData[] templates = (TabularData[]) runtime.invoke(name, OPERATION, null, null);
	LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,"Succesfully read data from MBean: "+name);
	
	// Convert array of TabularDate to list of OTPTemplates
	for (TabularData template : templates) {
	  OTPTemplate otpTemplate = new OTPTemplate();
	  Collection<CompositeData> templateValues = (Collection<CompositeData>) template.values();
	  for (CompositeData templateValue : templateValues) {
	    switch ((String) templateValue.get("key")) {
	    case OTPTemplate.LOCALE:
	      otpTemplate.setLocale((String) templateValue.get("value"));
	      break;
	    case OTPTemplate.SUBJECT:
	      otpTemplate.setSubject((String) templateValue.get("value"));
	      break;
	    case OTPTemplate.BODY:
	      otpTemplate.setBody((String) templateValue.get("value"));
	      break;
	    }
	  }
	  if (localeLanguage.equalsIgnoreCase(otpTemplate.getLocale())) {
	    languageOTPTemplate = otpTemplate;
	  }
	  if (defaultLanguage != null && defaultLanguage.equalsIgnoreCase(otpTemplate.getLocale())) {
	    defaultOTPTemplate = otpTemplate;
	  }
	  LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,"Retrieved OTP Template from MBean is: "+ otpTemplate);
	}
      } catch (Exception e) {
        LOGGER.logp(Level.WARNING,  CLASS_NAME, METHOD,"Execution of MBean has failed: "+e);
	e.printStackTrace();
      }
    }
    LOGGER.exiting(CLASS_NAME, METHOD);
    return (languageOTPTemplate != null ? languageOTPTemplate : defaultOTPTemplate);
  }
  
  /**
   * Get subject email string, in case template is not provided a default value from plugin parameter is used
   * @param template OTP Template from MBean
   * @param defaultValue Default email subject string
   * @return
   */
  public String getSubject(OTPTemplate template, String defaultValue) {
    String subject = defaultValue;
    if (template != null && template.getSubject().length() > 0) {
      subject = template.getSubject();
    } else {
      System.out.println("Default subject value is returned: " + defaultValue);
    }
    return subject;
  }

  /**
   * Get email body string, in case the template is not provided a default value 
   * from plugin parameter is used.
   * @param template OTP Template read from MBean
   * @param pin OTP PIN value
   * @param defaultValue Default email body string
   * @return
   */
  public String getBody(OTPTemplate template, String pin, String defaultValue) {
    String body = defaultValue;
    if (template != null && template.getBody().length() > 0) {
      body = template.getBody();
    } else {
      System.out.println("Default body value is returned: " + defaultValue);
    }

    if (body != null) {
      body = body.replaceAll("@@", pin);
    }


    return body;
  }


}
