package bka.iam.access.plugin.authnplugin;


import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

import oracle.security.am.plugin.ExecutionStatus;
import oracle.security.am.plugin.MonitoringData;
import oracle.security.am.plugin.authn.AbstractAuthenticationPlugIn;
import oracle.security.am.plugin.authn.AuthenticationContext;
import oracle.security.am.plugin.authn.AuthenticationException;
import oracle.security.am.plugin.authn.CredentialParam;
import oracle.security.am.plugin.authn.PlugInUtil;

/**
 * OAM Authorization PLugin which read HTTP Request parameter.
 * Based on prameter value decide if it is SAML provider or OIDC provider.
 *
 * In case provider name doesn't contains string oidc, plugin set Credential parameter KEY_FEDIDP as provider name
 * and return Success.
 *
 * In case provider name contains string oidc, plugin set string attribute IDP_NAME as provider name
 * and return Failure.
 */
public class IsSAMLProvider extends AbstractAuthenticationPlugIn {

  ///////////////////////////////////////////
  // Plugin version
  private final static String PLUGIN_VERSION = "2023-06-01-002";
 
  private final static String CLASS_NAME = IsSAMLProvider.class.getName();
  private final static String IDENTITY_PROVIDER_ATTRIBUTE_NAME = "IDENTITY_PROVIDER_ATTRIBUTE_NAME";
  
  /////////////////////////////////////////////
  // Instance variables
  private String identityProviderAttributeName;


  @Override
  public ExecutionStatus process(AuthenticationContext context) throws AuthenticationException {

    String METHOD = "process";
    LOGGER.entering(CLASS_NAME, METHOD);
    ExecutionStatus status = ExecutionStatus.SUCCESS;

    String pluginVersionMessage =  "OAM Plugin " + IsSAMLProvider.class.getName() + " version : " + PLUGIN_VERSION;
    System.out.println(pluginVersionMessage);
    LOGGER.info(pluginVersionMessage);
      
    // Read plugin attribute value of parameter PROVIDER_ATTRIBUTE_NAME
    identityProviderAttributeName = PlugInUtil.getFlowParam(context.getStringAttribute("StepName"), IDENTITY_PROVIDER_ATTRIBUTE_NAME, context);
    
    String identityProviderAttributeValue = context.getTransportContext().getParameterValue(identityProviderAttributeName);
    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "Input parameter "+identityProviderAttributeName+" = "+identityProviderAttributeValue);
    
    if(identityProviderAttributeValue != null){
        if(identityProviderAttributeValue.contains("OIDC")){
            // Provideris is OIDC provider
            context.setStringAttribute("IDP_NAME", identityProviderAttributeValue);
            status = ExecutionStatus.FAILURE;
            LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "It is OIDC Provider, string attribute IDP_NAME is set to: " + identityProviderAttributeValue);
        }
        else{
            // Providerid is SAML provider
            CredentialParam param = new CredentialParam();
            param.setName("KEY_FEDIDP");
            param.setType("string");
            param.setValue(identityProviderAttributeValue);
            context.getCredential().addCredentialParam("KEY_FEDIDP", param);                
            status = ExecutionStatus.SUCCESS;
            LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "It is SAML Provider, credential parameter KEY_FEDIDP is set to: " + identityProviderAttributeValue);
        }
    }
    

    LOGGER.logp(Level.FINEST, CLASS_NAME, METHOD, "Returning exection status: " + status);
    LOGGER.exiting(CLASS_NAME, METHOD);
    return status;
  }


  public String toString() {
    return "Is SAML Provider";
  }

  @Override
  public String getDescription() {
    return "Return success in case provider is SAML based ";
  }

  @Override
  public String getPluginName() {
    return "IsSAMLProvider";
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
