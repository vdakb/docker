package bka.iam.access.plugin.authnplugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.security.auth.Subject;

import oracle.security.am.common.utilities.principal.OAMGUIDPrincipal;
import oracle.security.am.common.utilities.principal.OAMUserDNPrincipal;
import oracle.security.am.common.utilities.principal.OAMUserPrincipal;
import oracle.security.am.engines.common.identity.provider.AuthnUser;
import oracle.security.am.engines.common.identity.provider.UserIdentityProvider;
import oracle.security.am.engines.common.identity.provider.UserIdentityProviderFactory;
import oracle.security.am.engines.common.identity.provider.UserInfo;
import oracle.security.am.engines.common.identity.provider.exceptions.IdentityProviderException;
import oracle.security.am.plugin.ExecutionStatus;
import oracle.security.am.plugin.PluginAttributeContextType;
import oracle.security.am.plugin.PluginConfig;
import oracle.security.am.plugin.PluginResponse;
import oracle.security.am.plugin.authn.AbstractAuthenticationPlugIn;
import oracle.security.am.plugin.authn.AuthenticationContext;
import oracle.security.am.plugin.authn.CredentialParam;
import oracle.security.am.plugin.authn.PlugInUtil;
import oracle.security.am.plugin.authn.PluginConstants;
import oracle.security.am.plugin.impl.CredentialMetaData;
import oracle.security.am.plugin.impl.UserAction;
import oracle.security.am.plugin.impl.UserActionContext;
import oracle.security.am.plugin.impl.UserActionMetaData;
import oracle.security.am.plugin.impl.UserContextData;

/**
 * Abstract class for OAM Authorization PLugin which send OTP Pin to user email
 */
public abstract class AbstractOTPAuthenticationPlugin extends AbstractAuthenticationPlugIn {


  private static final String CLASS_NAME = AbstractOTPAuthenticationPlugin.class.getName();


  abstract protected UserActionMetaData getUserActionMetaData();

  /**
   * PLugin method used to read plugin parameters defined on plugin definition
   * We don't use this metod,  becuase we read plugin values from step defined on
   * Authentication module
   * @param config
   * @return
   */
  @Override
  public ExecutionStatus initialize(PluginConfig config) {
    String METHOD = "initialize";
    LOGGER.entering(CLASS_NAME, METHOD);


    super.initialize(config);
    ExecutionStatus status = ExecutionStatus.SUCCESS;

    LOGGER.exiting(CLASS_NAME, METHOD);
    return status;
  }


  /**
   ** Get plugin parameter from Plug-in
   ** @param config Authentication Context
   ** @param pluginParameterName Name of parameter
   ** @return
   **/
  protected String getPluginInitParameter(PluginConfig config, String pluginParameterName) {
    String METHOD = "getPluginInitParameter";
    LOGGER.entering(CLASS_NAME, METHOD);

    String pluginParameterValue = "";
    Object tmp = config.getParameter(pluginParameterName);
    if (tmp != null) {
      pluginParameterValue = ((String) tmp);
    } else {
      LOGGER.logp(Level.WARNING, "AbstractOTPAuthenticationPlugin", "getPluginInitParameter",
		  "Initial plugin parameter <" + pluginParameterName + "> doesn't exist");
    }

    LOGGER.exiting(CLASS_NAME, METHOD);
    return pluginParameterValue;
  }

  /**
   ** Get plugin parameter from  step defined in Authentication Module.
   ** @param context Authentication Context
   ** @param parameterName Name of parameter
   ** @return
   **/
  protected String getPluginInitParameter(AuthenticationContext context, String parameterName) {
    String METHOD = "getPluginInitParameter";
    LOGGER.entering(CLASS_NAME, METHOD);

    String stepName = context.getStringAttribute(PluginConstants.KEY_STEP_NAME);
    String value = PlugInUtil.getFlowParam(stepName, parameterName, context);
    if (LOGGER.isLoggable(Level.FINEST))
      LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,String.format("Plugin parameter [%s] = [%s]", parameterName, value));

    LOGGER.exiting(CLASS_NAME, METHOD);
    return value;
  }

  /**
   ** Fetch LDAP attribute value from user object from Identity Store
   **
   ** @param provider Identity Provider of Identity store
   ** @param userName User identifier
   ** @param attributeName Name of the LDAP attribute which value is returned
   ** @return
   ** @throws IdentityProviderException
   **/
  protected String fetchAttributeStringValue(UserIdentityProvider provider, String userName,
					     String attributeName) throws IdentityProviderException {

    String METHOD = "fetchAttributeStringValue";
    LOGGER.entering(CLASS_NAME, METHOD);

    AuthnUser userauth = new AuthnUser();
    userauth.setUserName(userName);
    List<String> attributeNames = Arrays.asList(attributeName);
    Map<String, String> resultMap = provider.getUserAttributes(userauth, attributeNames);
    String value = resultMap.get(attributeName);

    if (LOGGER.isLoggable(Level.FINEST))
      LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,String.format("LDAP parameter [%s] = [%s]", attributeName, value));

    LOGGER.exiting(CLASS_NAME, METHOD);
    return value;
  }


  /**
   ** Parse String value to integer value, in case parsing fails default value is returned
   ** @param stringToParse
   ** @param defaultValue
   ** @return
   **/
  protected static int parseToInt(String stringToParse, int defaultValue) {
    try {
      return Integer.parseInt(stringToParse);
    } catch (NumberFormatException ex) {
      return defaultValue; //Use default value if parsing failed
    }
  }

  /**
   ** Parse String value to integer value, in case parsing fails default value is returned
   ** @param stringToParse
   ** @param defaultValue
   ** @return
   **/
  protected static long parseToLong(String stringToParse, long defaultValue) {
    try {
      return Long.parseLong(stringToParse);
    } catch (NumberFormatException ex) {
      return defaultValue; //Use default value if parsing failed
    }
  }

  /**
   * Set required context parameters for PAUSE operation
   * @param context
   * @return
   */
  protected ExecutionStatus pauseToOTPPage(AuthenticationContext context) {
    return pauseToOTPPage(context, null, null);
  }

  /**
   * Set required context parameters for PAUSE operation
   * @param context
   * @param p_error_code
   * @return
   */
  protected ExecutionStatus pauseToOTPPage(AuthenticationContext context, String p_error_code) {
    return pauseToOTPPage(context, p_error_code, null);
  }
  
  /**
   * Set required context parameters for PAUSE operation
   * @param context OAM Authentication Context
   * @param error_code Error code send back to OAM OTP page
   * @param emailTo list of the email. User would need to select one of this emails
   * @return ExecutionStatus equals to PAUSE
   */
  protected ExecutionStatus pauseToOTPPage(AuthenticationContext context, String error_code, String emailTo) {
    
    String METHOD = "pauseToOTPPage";
    LOGGER.entering(CLASS_NAME, METHOD);
    
    // Get OPT Page URL from Authentication Schema
    //  otpPageURL =  AuthenticationSchema.contextValue + AuthenticationSchema.challenge_url
    Map<String, String> params = context.getAuthnScheme().getChallengeParameters();
    String contextValue = params.get("contextValue");
    String challengeURL = params.get("challenge_url");
    String otpPagerURL = contextValue + challengeURL;
    LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,"OTP Page to obtain PIN is: "+otpPagerURL);

    // UserAction for redirection to OTP page
    // OTPPin is used as input paramter for OTP Pin
    // Error message is send in request parameter p_error_code
    UserActionContext actionContext = new UserActionContext();
    actionContext.getContextData().add(new UserContextData("OTPin", "One Time PIN", new CredentialMetaData(PluginConstants.PASSWORD)));
    actionContext.getContextData().add(new UserContextData(otpPagerURL, new CredentialMetaData(PluginConstants.URL)));
    if (error_code != null && error_code.length() > 0)
      actionContext.getContextData().add(new UserContextData("error_code=" + error_code, new CredentialMetaData(PluginConstants.QUERY_STRING)));
    if (emailTo != null && emailTo.length() > 0)
      actionContext.getContextData().add(new UserContextData("OTPEmails=" + emailTo, new CredentialMetaData(PluginConstants.QUERY_STRING)));
    UserAction action = new UserAction(actionContext, getUserActionMetaData());
    context.setAction(action);
    
    LOGGER.exiting(CLASS_NAME, METHOD);
    return ExecutionStatus.PAUSE;
  }

  /**
   ** Get UserName from the credential context or from string attributes
   ** @param context Plugin Authentication Context
   ** @return authentication user form OAM context
   **/
  protected String getUserName(final AuthenticationContext context) {
    
    String METHOD = "getUserName";
    LOGGER.entering(CLASS_NAME, METHOD);
    
    String userName = null;

    CredentialParam param = context.getCredential().getParam(PluginConstants.KEY_USERNAME);

    if (param != null) {
      userName = (String) param.getValue();
    }

    if ((userName == null) || (userName.length() == 0)) {
      userName = context.getStringAttribute(PluginConstants.KEY_USERNAME);
    }

    LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,"Authenticated user is: "+userName);
    LOGGER.exiting(CLASS_NAME, METHOD);
    return userName;
  }

  /**
   * Set a cotext properties needed for next steps
   * @param context OAM Authentication context
   * @param provider User Identity Provider used to read users parameters from Identity Provider (LDAP Server)
   */
  protected void updatePluginResponse(AuthenticationContext context, UserIdentityProvider provider, String identityStoreRef) {
    String METHOD = "updatePluginResponse";
    LOGGER.entering(CLASS_NAME, METHOD);
    try {
      String userName = getUserName(context);
      if (userName != null) {
	context.setStateAttribute(PluginConstants.KEY_USERNAME, userName);
      }
      UserInfo user = null;
      try {
	user = provider.locateUser(userName);
      } catch (Exception e) {
	LOGGER.log(Level.SEVERE, "Error retrieving user profile from configured identity store", e);
      }
      String userIdentity = user.getUserId();
      String userDN = user.getDN();
      Subject subject = new Subject();
      subject.getPrincipals().add(new OAMUserPrincipal(userIdentity));
      subject.getPrincipals().add(new OAMUserDNPrincipal(userDN));
      if (user.getGUID() != null) {
	subject.getPrincipals().add(new OAMGUIDPrincipal(user.getGUID()));
      } else {
	subject.getPrincipals().add(new OAMGUIDPrincipal(userIdentity));
      }
      context.setSubject(subject);

      CredentialParam param = new CredentialParam();
      param.setName("KEY_USERNAME_DN");
      param.setType("string");
      param.setValue(userDN);
      context.getCredential().addCredentialParam("KEY_USERNAME_DN", param);
      
      PluginResponse rsp = new PluginResponse();
      rsp.setName("KEY_IDENTITY_STORE_REF");
      rsp.setType(PluginAttributeContextType.LITERAL);
      rsp.setValue(identityStoreRef);
      
      /*if ("__$DEFAULT$__".equalsIgnoreCase(provider.getName())) {
	rsp.setValue("");
      } else {
	rsp.setValue(provider.getName());
      }
*/
      rsp.setValue(provider.getName());
      context.addResponse(rsp);

      rsp = new PluginResponse();
      rsp.setName("KEY_AUTHENTICATED_USER_NAME");
      rsp.setType(PluginAttributeContextType.LITERAL);
      rsp.setValue(userIdentity);
      context.addResponse(rsp);

      rsp = new PluginResponse();
      rsp.setName("authn_policy_id");
      rsp.setType(PluginAttributeContextType.REQUEST);
      rsp.setValue(context.getAuthnScheme().getName());
      context.addResponse(rsp);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error while updating the plugin response", e);
    }
    if (LOGGER.isLoggable(Level.FINEST))
      LOGGER.exiting(CLASS_NAME, METHOD);
  }
  
  /**
   * Get identity store, in case identity store name is not provided defualt identity store is returned.
   * @param identityStoreRef
   * @return
   * @throws IdentityProviderException
   */
  protected UserIdentityProvider getIdentityStore(String identityStoreRef) throws IdentityProviderException {
    if (identityStoreRef != null && identityStoreRef.length() > 0) {
      return UserIdentityProviderFactory.getProvider(identityStoreRef);
    } else {
      // In case Identity store is not defined on plugin parameters
      // Default Identity Store is used
      return UserIdentityProviderFactory.getProvider();
    }
  }
  
  /**
   * Get value from credential object, in case key si not presented a empty string is returned
   * @param context
   * @param key
   * @return
   */
  protected String getCredentialString(AuthenticationContext context, String key) {
    String value = "";
    CredentialParam tmp = context.getCredential().getParam(key);
    if (tmp != null) {
      value = (String) tmp.getValue();
    }
    return value;
  }

  /**
   * Get User object from Identity Store
   * @param user
   * @param provider
   * @return
   */
  protected UserInfo getUserInfo(String user, UserIdentityProvider provider) {
    String METHOD = "getUserInfo";
    LOGGER.entering(CLASS_NAME, METHOD);
    if (LOGGER.isLoggable(Level.FINEST)) {
      LOGGER.entering(CLASS_NAME, METHOD, " userInfo=" + user + ",provider=" + provider);
      if (user == null || user.length() == 0 || provider == null) {
	LOGGER.exiting(CLASS_NAME, METHOD, "Invalid input,Returning null");
	return null;
      }
    }
    UserInfo userInfo = null;
    try {
      userInfo = provider.locateUser(user);
    } catch (IdentityProviderException e) {
      LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD, "Error retrieving user profile from configured identity store", (Throwable) e);
    }
    if (LOGGER.isLoggable(Level.FINEST))
      LOGGER.exiting(CLASS_NAME, METHOD, "Returning userInfo " + userInfo);
    return userInfo;
  }

  /**
   * Return email address based on the index. Email addresses are comma separated
   * @param emailList list of the email addresses separated by comma
   * @param index idex starts from 0
   * @return
   */
  protected String getSelectedEmail(String emailList, String index) {
    String METHOD = "getSelectedEmail";
    LOGGER.entering(CLASS_NAME, METHOD);
    
    String email = null;
    try {
      int i = Integer.parseInt(index);
      if (emailList != null) {
	String[] emails = emailList.split(",");
	if (emails != null && i < emails.length) {
	  email = emails[i];
	}
      }
    } catch (NumberFormatException ex) {
      LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD, "Unable to select email based on input parameters emailList=" + emailList + ", index=" + index);
    }
    LOGGER.exiting(CLASS_NAME, METHOD, "Returning email address: " + email);
    return email;
  }


}
