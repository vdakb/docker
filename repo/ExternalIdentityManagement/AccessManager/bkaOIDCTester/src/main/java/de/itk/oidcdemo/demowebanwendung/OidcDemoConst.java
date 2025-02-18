package de.itk.oidcdemo.demowebanwendung;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.itk.auth.client.demoutil.DemoConst;

public class OidcDemoConst extends DemoConst {

  private OidcDemoConst() {
    throw new IllegalStateException("Utility class");
  }

  public static final String IDP_FIAM = "F-IAM";

  /* session attributes */
  public static final String SA_CODE_VERIFIER = "CodeVerifier";
  public static final String SA_ACCESS_TOKEN = "AccessToken";
  public static final String SA_ID_TOKEN = "IdToken";
  public static final String SA_REFRESH_TOKEN = "RefreshToken";
  public static final String SA_USERINFO = "Userinfo";
  public static final String SA_USERINFO_TIME = "UserinfoTime";
  public static final String SA_LOG = "Log";
  public static final String SA_WS_RESPONSE = "WsResponse";

  /* query parameters */
  public static final String QP_INSIGHT_MODE = "insight-mode";
  public static final String QP_SCOPE = "scope";
  public static final String QP_RESPONSE_TYPE = "response_type";
  public static final String QP_CLIENT_ID = "client_id";
  public static final String QP_CLIENT_SECRET = "client_secret";
  public static final String QP_REDIRECT_URI = "redirect_uri";
  public static final String QP_REFRESH_TOKEN = "refresh_token";
  public static final String QP_CODE_CHALLENGE_METHOD = "code_challenge_method";
  public static final String QP_CODE_CHALLENGE = "code_challenge";
  public static final String QP_CODE_VERIFIER = "code_verifier";
  public static final String QP_STATE = "state";
  public static final String QP_AUTHORIZATION_CODE = "code";
  public static final String QP_GRANT_TYPE = "grant_type";
  public static final String QP_ACTION = "action";
  public static final String QP_ERROR = "error";
  public static final String QP_ERROR_DESC = "error_description";
  public static final String QP_IDP = "idp";
  public static final String QP_IDP_NAME = "idp_name";

  /* attributes passed to jsp */
  public static final String ATTR_INIT_TAB_BTN = "init-tab-btn";
  public static final String ATTR_REQUEST_URI = "request-uri";
  public static final String ATTR_TOKEN_REQUEST = "token-request";
  public static final String ATTR_TOKEN_RESPONSE = "token-response";
  public static final String ATTR_REDIRECT_URI = "redirect-uri";
  public static final String ATTR_USERINFO_REQUEST = "userinfo-request";
  public static final String ATTR_USERINFO_RESPONSE = "userinfo-response";
  public static final String ATTR_WEBSERVICE_REQUEST = "webservice-request";
  public static final String ATTR_WEBSERVICE_RESPONSE = "webservice-response";

  /* other */
  public static final String RESPONSE_TYPE_CODE = "code";
  public static final String METHOD_S256 = "S256";
  public static final String GRANT_TYPE_CODE = "authorization_code";
  public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
  public static final String TAB_ACCESS_TOKEN = "AcTokenBtn";
  public static final String TAB_USERINFO = "UserinfoBtn";
  public static final String TAB_WEBSERVICE = "WsResponseBtn";

  public static final Set<String> JWT_DATE_CLAIMS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("iat", "exp", "auth_time")));
}
