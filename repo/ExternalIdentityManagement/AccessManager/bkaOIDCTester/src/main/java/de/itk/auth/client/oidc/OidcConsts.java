package de.itk.auth.client.oidc;

public class OidcConsts {

  // Query Parameters
  public static final String QP_STATE = "state";
  public static final String QP_CODE = "code";
  public static final String QP_SCOPE = "scope";
  public static final String QP_RESPONSE_TYPE = "response_type";
  public static final String QP_REDIRECT_URI = "redirect_uri";
  public static final String QP_CODE_CHALLENGE_METHOD = "code_challenge_method";
  public static final String QP_CODE_CHALLENGE = "code_challenge";
  public static final String QP_CODE_VERIFIER = "code_verifier";
  public static final String QP_DOMAIN = "domain";
  public static final String QP_REQUESTED_TOKEN_TYPE = "requested_token_type";
  public static final String QP_SUBJECT_TOKEN = "subject_token";
  public static final String QP_SUBJECT_TOKEN_TYPE = "subject_token_type";
  public static final String QP_CLIENT_ID = "client_id";
  public static final String QP_CLIENT_SECRET = "client_secret";
  public static final String QP_REFRESH_TOKEN = "refresh_token";
  public static final String QP_GRANT_TYPE = "grant_type";

  // JSON nodes
  public static final String JN_ERROR = "error";
  public static final String JN_ERROR_DESCRIPTION = "error_description";
  public static final String JN_ACCESS_TOKEN = "access_token";
  public static final String JN_ID_TOKEN = "id_token";
  public static final String JN_REFRESH_TOKEN = "refresh_token";
  public static final String JN_EXPIRES_IN = "expires_in";
  public static final String JN_SUB = "sub";

  // Others
  public static final Object RESPONSE_TYPE_CODE = "code";
  public static final Object CODE_CHALLENGE_METHOD_S256 = "S256";
  public static final String TOKEN_TYPE_JWT = "urn:ietf:params:oauth:token-type:jwt";
  public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
  public static final String HEADER_PARAMETER_DOMAIN = "x-oauth-identity-domain-name";
  public static final String HEADER_PARAMETER_BEARER = "Bearer";

  private OidcConsts() {
    throw new IllegalStateException("Utility class");
  }
}
