package de.itk.oidcdemo.demowebanwendung;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import de.itk.auth.client.InitException;
import de.itk.auth.client.demoutil.DemoUtil;
import de.itk.auth.client.oidcjee.OidcClientStsConfigFromServletContext;

public class OidcDemoConfig extends OidcClientStsConfigFromServletContext {

  private static OidcDemoConfig instance = null;

  private static final String CFG_REQUIRED_SCOPE = "Required-Scope";
  private static final String CFG_DEFAULT_SCOPE = "Default-Scope";
  private static final String CFG_WEBSERVICE_URL = "Webservice-URL";
  private static final String CFG_WEBSERVICE_SCOPE = "Webservice-Scope";
  private static final String CFG_WEBAPP_LINKS = "Webapp-Links";
  private static final String CFG_TN_IDPS = "TN-IDPs";

  private final String requiredScope;
  private final String defaultScope;
  private final String webserviceUrl;
  private final String webserviceScope;

  private final List<Map.Entry<String, String>> webappLinks;
  private final List<String>                    tnIdps;

  private OidcDemoConfig(ServletContext servletContext)
    throws InitException {
    super(servletContext);
    requiredScope = servletContext.getInitParameter(CFG_REQUIRED_SCOPE);
    defaultScope = servletContext.getInitParameter(CFG_DEFAULT_SCOPE);
    webserviceUrl = servletContext.getInitParameter(CFG_WEBSERVICE_URL);
    webserviceScope = servletContext.getInitParameter(CFG_WEBSERVICE_SCOPE);
    webappLinks = DemoUtil.parseWebappLinks(servletContext.getInitParameter(CFG_WEBAPP_LINKS));
    tnIdps = DemoUtil.parseStringList(servletContext.getInitParameter(CFG_TN_IDPS));
  }

  public static synchronized OidcDemoConfig getInstance(ServletContext servletContext)
    throws InitException {
    if (instance == null) {
      instance = new OidcDemoConfig(servletContext);
    }
    return instance;
  }

  public String getRequiredScope() {
    return requiredScope;
  }

  public String getDefaultScope() {
    return defaultScope;
  }

  public String getWebserviceUrl() {
    return webserviceUrl;
  }

  public String getWebserviceScope() {
    return webserviceScope;
  }

  public List<Map.Entry<String, String>> getWebappLinks() {
    return webappLinks;
  }

  public List<String> getTnIdps() {
    return tnIdps;
  }

  public String getStdAuthorizationEndpoint() {
    return oidcConfig.get("authorization_endpoint").asText("");
  }

}
