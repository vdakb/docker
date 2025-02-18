package bka.iam.access.plugin.authnplugin;

import bka.iam.access.plugin.model.OTPTemplate;
import bka.iam.access.plugin.utils.StringGenerator;
import bka.iam.access.plugin.utils.TemplateUtils;
import bka.iam.access.plugin.utils.UMSUtil;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import oracle.security.am.engines.common.identity.provider.UserIdentityProvider;
import oracle.security.am.engines.common.identity.provider.exceptions.IdentityProviderException;
import oracle.security.am.plugin.ExecutionStatus;
import oracle.security.am.plugin.MonitoringData;
import oracle.security.am.plugin.authn.AuthenticationContext;
import oracle.security.am.plugin.authn.AuthenticationException;
import oracle.security.am.plugin.impl.UserActionMetaData;

/**
 * OAM Authorization PLugin which send OTP Pin to user email
 */
public class OTPAuthenticationPlugin extends AbstractOTPAuthenticationPlugin {

  ///////////////////////////////////////////
  // Plugin version
  private final static String PLUGIN_VERSION = "2023-06-05-001";
  private final static String CLASS_NAME = OTPAuthenticationPlugin.class.getName();

  ///////////////////////////////////////////
  // Plugin context variables where are stored runtime data
  private final static String OTP_PIN_VALUE = "OTP_PIN_VALUE";
  private final static String OTP_PIN_CREATED = "OTP_PIN_CREATED";
  private final static String OTP_PIN_ATTEMPTS = "OTP_PIN_ATTEMPTS";
  private final static String OTP_PIN_NUMBER_OF_RESENDS = "OTP_PIN_NUMBER_OF_RESENDS";
  private final static String OTP_PIN_EMAILS = "OTP_PIN_EMAILS";
  private final static String OTP_PIN_SELECTED_EMAIL = "OTP_PIN_SELECTED_EMAIL";
  
  ///////////////////////////////////////////
  // UMS FAILED status delivery
  private final static String DELIVERY_STATUS_FAILED = "FAILED";
  
  ///////////////////////////////////////////
  // OAM Plugin parameter
  /**
   ** Name of identity store, it needs to be the same as it was used in the authentication modules.
   ** For example in case Federation Module used IdentityStoreRef the same value needs to be used on this plugin
   **/
  private String identityStoreRef;

  /**
   ** Email address from where is send OTP email
   **/
  private String emailFrom;

  /**
   ** Friendly name of email address from where is send OTP email, e.g. Administrator or do-not-replay
   **/
  private String emailFromName;
  
  /**
   ** LDAP attribute name from where are read email addressed. 
   ** Defuult value: mail
   **/
  private String emailLDAPAttr;
  
  /**
   ** OAM Plugin support emails subject in multiple languages.Locale is read from user browser. In case for any reason 
   ** email subject can't be read from MBean where is stored email subject this value is used as fallback value.
   ** Defaul value: One Time Pin
   **/
  private String emailSubject;
  
  /**
   ** OAM Plugin support emails body in multiple languages. Locale is read from user browser. In case for any reason 
   ** email body can't be read from MBean where is stored email body this value is used as fallback value.
   
   ** Defaul value: Please use @@ as a One Time Pin to access the requested resource.
   **/
  private String emailBody;
  
  /**
   ** When user has muliple email addressed defined on his/her profile, user need to select which email is used for PIN.
   ** From securiry reason email needs to be masked with *.
   ** This attribute define how many characters from start of the email address are not masked.
   ** Only string before @ and domain name is masked for example:  to*********@oraMBEAN_NAMEcle.com (Value is set to 2)
   ** Default value: 0 
   **/
  private int emailMaskStart;
  
  /**
   ** When user has muliple email addressed defined on his/her profile, user need to select which email is used for PIN.
   ** From securiry reason email needs to be masked with *.
   ** This attribute define how many characters from end of the email address are not masked.
   ** Only string before @ is masked for example:  *********bo@oracle.com (Value is set to 2)
   ** Default value: 0 
   **/
  private int emailMaskEnd;
  
  /**
   ** OAM Plugin support emails subject and body in multiple languages. Locale is read from user browser. 
   ** In case Locale in browser has value for which OTP template is not defined, then this locale is used 
   ** as fallback value.
   ** Default value: en
   **/
  private String emailDefaultLanguage;
  
  /**
   ** OAM Plugin reads template definitions from the MBean. In this parameter is provided MBean name on which is 
   ** executed operation otpTemplateList.
   ** Default value: bka.iam.platform:Name=config,Type=idp,Application=idp.iam.access.module,ApplicationVersion=12.2.1.3
   **/
  private String emailTemplateMBeanName;
  
  /**
   ** OAM Plugin leverage Oracle Universal Messaging Service for email delivery.
   ** Webservice of UMS is exposed on http://<UMS_ServerName>:<UMS_Port>/ucs/messaging/webservice
   **/
  private String umsURL;
  
  /**
   ** Define if OWSM policy oracle/wss11_username_token_with_message_protection_client_policy is used
   ** in comunication with UMS service
   ** Default value: false
   **/
  private boolean umsWSS;
  
  /**
   ** Credentials for UMS services are read from Weblogic Credential Store. 
   ** I this value is provided a key name.
   ** Default value: umsKey
   **/
  private String umsCFSKey;
  
  /**
   ** Pin can contains only values defined in this prameter. For example in case pin should contains only numbers
   ** this parameter value is 0123456789
   ** Default value: 0123456789
   **/
  private String pinCharacters;
  
  /**
   ** Define lenght of PIN
   ** Default value: 6
   **/
  private int pinLenght;
  
  /**
   ** Define how long the PIN is valid. This value is in the miliseconds. 
   ** Once the pin is expired user must ask for a new pin.
   ** Default value: 60000  (60 seconds)
   **/
  private int pinValidity;
  
  /**
   ** How many times can user submit pin for validation. Once this value is reached user is not able to submit pin anymore.
   ** Default value: 5
   **/
  private int pinMaxAttempts;
  
  /**
   ** How many new pins can be send to user. Once this value is reache. OAM won't send a new pin to user.
   **/
  private int pinMaxNewPins;
  
  /**
   ** OAM Plugin can redirect user to OTP Page with three different way. 
   ** It can be FORWARD, REDIRECT_POST or REDIRECT_GET.
   ** Default value: FORWARD
   **/
  private String userAction;
  
  
  
 

  /**
   ** Read Plugins parameters from Authentication Module Step
   **
   ** @param context PLugin Authentication context sent to the main process method
   **/
  private void readPluginRuntimeParameters(AuthenticationContext context) {
    String METHOD = "readPluginRuntimeParameters";
    LOGGER.entering(CLASS_NAME, METHOD);
    identityStoreRef       = getPluginInitParameter(context, "IDENTITY_STORE_REF");
    emailFrom              = getPluginInitParameter(context, "EMAIL_FROM");
    emailFromName          = getPluginInitParameter(context, "EMAIL_FROM_NAME");
    emailLDAPAttr          = getPluginInitParameter(context, "EMAIL_LDAP_ATTR");
    emailSubject           = getPluginInitParameter(context, "EMAIL_SUBJECT");
    emailBody              = getPluginInitParameter(context, "EMAIL_BODY");
    emailDefaultLanguage   = getPluginInitParameter(context, "EMAIL_DEFAULT_LANGUAGE");
    emailMaskStart         = parseToInt(getPluginInitParameter(context, "EMAIL_MASK_START"), 0);
    emailMaskEnd           = parseToInt(getPluginInitParameter(context, "EMAIL_MASK_END"), 0);
    emailTemplateMBeanName = getPluginInitParameter(context, "EMAIL_TEMPLATE_MBEAN_NAME");
    umsURL                 = getPluginInitParameter(context, "UMS_URL");
    umsWSS                 = Boolean.parseBoolean(getPluginInitParameter(context, "UMS_IS_WSS"));
    umsCFSKey              = getPluginInitParameter(context, "UMS_CSF_KEY");
    pinCharacters          = getPluginInitParameter(context, "PIN_CHARACTERS");
    pinLenght              = parseToInt(getPluginInitParameter(context, "PIN_LENGHT"), 6);
    pinValidity            = parseToInt(getPluginInitParameter(context, "PIN_VALIDITY"), 0);
    pinMaxAttempts         = parseToInt(getPluginInitParameter(context, "PIN_MAX_ATTEMPTS"), 0);
    pinMaxNewPins          = parseToInt(getPluginInitParameter(context, "PIN_MAX_NEW_PINS"), 0);
    userAction             = getPluginInitParameter(context, "USER_ACTION");
    
    LOGGER.exiting(CLASS_NAME, METHOD);
  }


  @Override
  public ExecutionStatus process(AuthenticationContext context) throws AuthenticationException {

    String METHOD = "process";
    LOGGER.entering(CLASS_NAME, METHOD);
    ExecutionStatus status = ExecutionStatus.SUCCESS;

    String pluginVersionMessage = "OAM Plugin " + OTPAuthenticationPlugin.class.getName() + " version : " + PLUGIN_VERSION;
    System.out.println(pluginVersionMessage);
    LOGGER.info(pluginVersionMessage);

    // Read plugin parameters
    readPluginRuntimeParameters(context);

    // Get User locale
    String localeLanguage = this.emailDefaultLanguage;
    Locale local = context.getTransportContext().getLocale();
    if (local != null) {
      localeLanguage = local.getLanguage();
    }

    // Get get user name
    String userName = getUserName(context);
    String emailTo = "";
    String p_error_code = null;
    String otpAction = context.getTransportContext().getParameterValue("OTPAction");
    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, String.format("OTP Action: [%s], LocaleLanguage [%s]", otpAction, localeLanguage));

    try {
      // Fetch userName and set required variable in context
      UserIdentityProvider provider = getIdentityStore(identityStoreRef);
      updatePluginResponse(context, provider, identityStoreRef);

      // Read emailTo from context, LDAP or ask user to provide email, in case multiple emails are defined
      String selectedEmailTo = context.getStringAttribute(OTP_PIN_SELECTED_EMAIL);
      if (selectedEmailTo != null) {
	emailTo = selectedEmailTo;
      } else {
	// Fetch email address from Usert attribute (LDAP value defined on plugin parameter EMAIL_LDAP_ATTR)
	emailTo = fetchAttributeStringValue(provider, userName, emailLDAPAttr);
	if (emailTo == null || !emailTo.contains("@")) {
	  p_error_code = "OAM-OTP-000";
	  LOGGER.logp(Level.INFO, CLASS_NAME, METHOD, String.format("User [%s] doesn't have emails address on his profile", userName));
	  LOGGER.exiting(CLASS_NAME, METHOD);
	  return pauseToOTPPage(context, p_error_code);
	} else if (emailTo.contains(",")) {
	  if ("EMAIL".equals(otpAction)) {
	   // User provided se
	    String allEmails = context.getStringAttribute(OTP_PIN_EMAILS);
	    String otpEmailIndex = context.getTransportContext().getParameterValue("OTPEmailIndex");
	    String selectedEmail = getSelectedEmail(allEmails, otpEmailIndex);
	    if (selectedEmail != null) {
	      context.setStringAttribute(OTP_PIN_SELECTED_EMAIL, selectedEmail);
	      emailTo = selectedEmail;
	      otpAction = null;
	    } else {
	      LOGGER.logp(Level.INFO, CLASS_NAME, METHOD, String.format("User [%s], selected wrong email address", userName));
	      LOGGER.exiting(CLASS_NAME, METHOD);
	      return pauseToOTPPage(context, "OAM-OTP-005", StringGenerator.maskEmailAddress(emailTo, emailMaskStart, emailMaskEnd));
	    }
	  } else {
	    // User needs to select email address where to send pin
	    context.setStringAttribute(OTP_PIN_EMAILS, emailTo);
	    LOGGER.logp(Level.INFO, CLASS_NAME, METHOD, String.format("User [%s], has multiple emails, needs to select which one to use", userName));
	    LOGGER.exiting(CLASS_NAME, METHOD);
	    return pauseToOTPPage(context, null, StringGenerator.maskEmailAddress(emailTo, emailMaskStart, emailMaskEnd));
	  }
	}
      }

      LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "User email used for PIN is: " + emailTo);


      // Generate OTP_PIN and store it in the string attributes with time when it was generated
      if (otpAction == null) {
	String pin = StringGenerator.getToken(pinCharacters, pinLenght);
	context.setStringAttribute(OTP_PIN_VALUE, pin);
	context.setStringAttribute(OTP_PIN_CREATED, "" + new Date().getTime());

	// Limit how many PINs can be sedn to end user. Once that amout is reached error message is shown
	int pinNumberOfResends = parseToInt(context.getStringAttribute(OTPAuthenticationPlugin.OTP_PIN_NUMBER_OF_RESENDS), 1);
	if (pinNumberOfResends > pinMaxNewPins && pinMaxNewPins > 0) {
	  p_error_code = "OAM-OTP-004";
	  LOGGER.logp(Level.FINE, CLASS_NAME, METHOD, String.format("User [%s], maximum number of PINs has been reached", userName));
	} else {
	  // Send PIN to User email address
	  String deliveryStatus = sendMessage(emailTo, pin, localeLanguage);
          if(DELIVERY_STATUS_FAILED.equals(deliveryStatus)){
            p_error_code = "OAM-OTP-007";
          }
	  context.setStringAttribute(OTP_PIN_NUMBER_OF_RESENDS, "" + (pinNumberOfResends + 1));
	  LOGGER.logp(Level.FINE, CLASS_NAME, METHOD, String.format("User [%s], the new PIN was send to user", userName));
	}
	// Redirect to OTP page
	status = pauseToOTPPage(context, p_error_code);
      }
      // Generate OTP_PIN and store it in the string attributes with time when it was generated
      else if ("RESEND".equals(otpAction) && emailTo != null) {
	String pin = StringGenerator.getToken(pinCharacters, pinLenght);
	context.setStringAttribute(OTP_PIN_VALUE, pin);
	context.setStringAttribute(OTP_PIN_CREATED, "" + new Date().getTime());

	// Limit how many PINs can be sedn to end user. Once that amout is reached error message is shown
	int pinNumberOfResends = parseToInt(context.getStringAttribute(OTPAuthenticationPlugin.OTP_PIN_NUMBER_OF_RESENDS), 1);
	System.out.println("pinNumberOfResends: " + pinNumberOfResends);
	if (pinNumberOfResends > pinMaxNewPins && pinMaxNewPins > 0) {
	  p_error_code = "OAM-OTP-004";
	  LOGGER.logp(Level.INFO, CLASS_NAME, METHOD, String.format("User [%s], maximum number of PINs has been reached", userName));
	} else {
	  // Send PIN to User email address
	  String deliveryStatus = sendMessage(emailTo, pin, localeLanguage);
          if(DELIVERY_STATUS_FAILED.equals(deliveryStatus)){
	    p_error_code = "OAM-OTP-007";
	  }
	  context.setStringAttribute(OTP_PIN_NUMBER_OF_RESENDS, "" + (pinNumberOfResends + 1));
	  LOGGER.logp(Level.FINE, CLASS_NAME, METHOD, String.format("User [%s], the new PIN was send to user", userName));
	}
	// Redirect to OTP page
	status = pauseToOTPPage(context, p_error_code);
      } else if ("SUBMIT".equals(otpAction) && emailTo != null) {
	String pin = context.getStringAttribute(OTP_PIN_VALUE);
	if (pin != null) {
	  // Pin exist in the context
	  // Pin duration in defined in plugin parameter PIN_EXPIRE. Value is in miliseconds
	  // Maximum number of attempts is defined in te plugin parameter PIN_MAX_ATTEMPTS
	  String savedPin = pin;
	  String submitedPin = getCredentialString(context, "OTPin");
	  int pinAttempts = parseToInt(context.getStringAttribute(OTPAuthenticationPlugin.OTP_PIN_ATTEMPTS), 1);
	  long pinCreated = parseToLong(context.getStringAttribute(OTPAuthenticationPlugin.OTP_PIN_CREATED), 0);
	  long pinExprire = pinCreated + pinValidity;

	  String debugString =  String.format("User [%s], pit attempts [%s], pin expires in [%s] ms", userName, pinAttempts, "" + (pinExprire - (new Date()).getTime()));
	  LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, debugString);

	  // Validate if provided PIN is correct, doesn't expire and if max attemts hasn't been reached
	  if (pinAttempts > this.pinMaxAttempts && this.pinMaxAttempts > 0) {
		   // Number off failed attemps has reached
		   // When pinMaxAttempts == 0 or is not provided then this check if disabled
		   p_error_code = "OAM-OTP-002";
	    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD,"Number of the failed attemps has reached maximum value defind on plugin parameter PIN_MAX_ATTEMPTS");
	    status = pauseToOTPPage(context, p_error_code);
	  } else if ((new Date()).getTime() > pinExprire && this.pinValidity > 0) {
		   // Pin has expired
		   // When prameter pinExpire == 0 or is not provided this check is disabled
		   p_error_code = "OAM-OTP-003";
	    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "PIN has exprired");
	    status = pauseToOTPPage(context, p_error_code);
	  } else if (pin.equals(submitedPin)) {
	    // In is valid and it is provided in the valid time frame.
	    // Also number of attempts is smaller then maximum numbers defined on plugin
	    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "Provided PIN match sent PIN");
	    status = ExecutionStatus.SUCCESS;
	  } else {
	    p_error_code = "OAM-OTP-001";
	    // Increment value of the OTP_PIN_ATTEMPTS
	    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "Provided PIN doens't match sent PIN");
	    status = pauseToOTPPage(context, p_error_code);
	  }
	  context.setStringAttribute(OTPAuthenticationPlugin.OTP_PIN_ATTEMPTS, "" + (pinAttempts + 1));
	} else {
	  LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD, "PIN is not stored in OAM Context and OTPAction is SUBMIT ");
	  status = pauseToOTPPage(context);
	}
      } else {
	// Return error OAM-OTP-006 - Unsupported action
	p_error_code = "OAM-OTP-006";
	LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD, "Unsupported OTPAction: " + otpAction);
	status = pauseToOTPPage(context, p_error_code);
      }
    } catch (IdentityProviderException e) {
      String errorMessage =
	"Exception occurred when authenticating the user against UserIdentityStore - " + identityStoreRef;
      if (LOGGER.isLoggable(Level.FINE)) {
	LOGGER.log(Level.FINE, errorMessage, e);
      }
      e.printStackTrace();
      status = ExecutionStatus.ABORT;
    }
    LOGGER.exiting(CLASS_NAME, METHOD);
    return status;
  }

  /**
   ** Send email message via UMS to end user
   ** @param emailTo Email address where to send email
   ** @param pin OneTimePin value
   ** @param localeLanguage language name, this parameter is used to select proper email template
   **                       Email template are stored in MBean
   ** @return
   **/
  protected String sendMessage(String emailTo, String pin, String localeLanguage) {

    String METHOD = "sendMessage";
    LOGGER.entering(CLASS_NAME, METHOD);

    // Get tempate from MBean
    TemplateUtils templateUtils = new TemplateUtils();
    OTPTemplate template = templateUtils.getOTPTemplate(this.emailTemplateMBeanName,
                                                        localeLanguage,
                                                        emailDefaultLanguage);
    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "OTP Template used for email: " + template);

    // Send email via UMS
    UMSUtil ums = new UMSUtil(umsURL, umsCFSKey, umsWSS);
    String messageId = ums.sendEmail(emailFrom, 
				     emailFromName, 
				     emailTo, 
				     templateUtils.getSubject(template, this.emailSubject),
		                     templateUtils.getBody(template, pin, this.emailBody));

    // Check delivery status
    String deliveryStatus = null;
    if(messageId == null){
      if (LOGGER.isLoggable(Level.FINEST)) {
        deliveryStatus = DELIVERY_STATUS_FAILED;
        LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "Email wasn't sent, delivery status is "+deliveryStatus);
      }
    }
    else{
        if (LOGGER.isLoggable(Level.FINEST)) {
          deliveryStatus = ums.checkDeliveryStatus(messageId);
          LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "Email sent and delivery status is " + deliveryStatus);
        }
    }
    LOGGER.exiting(CLASS_NAME, METHOD);
    return deliveryStatus;
  }


  @Override
  protected UserActionMetaData getUserActionMetaData() {
    if ("FORWARD".equalsIgnoreCase(userAction)) {
      return UserActionMetaData.FORWARD;
    } else if ("REDIRECT_POST".equalsIgnoreCase(userAction)) {
      return UserActionMetaData.REDIRECT_POST;
    } else if ("REDIRECT_GET".equalsIgnoreCase(userAction)) {
      return UserActionMetaData.REDIRECT_GET;
    } else {
      return UserActionMetaData.FORWARD;
    }
  }

  public String toString() {
    return "OTP Authentication Plugin";
  }

  @Override
  public String getDescription() {
    return "OTP Authentication Plugin";
  }

  @Override
  public String getPluginName() {
    return "OTPAuthenticationPlugin";
  }

  @Override
  public int getRevision() {
    return 1;
  }

  @Override
  public Map<String, MonitoringData> getMonitoringData() {
    return Collections.emptyMap();
  }

  @Override
  public boolean getMonitoringStatus() {
    return false;
  }

  @Override
  public void setMonitoringStatus(boolean status) {
  }

}
