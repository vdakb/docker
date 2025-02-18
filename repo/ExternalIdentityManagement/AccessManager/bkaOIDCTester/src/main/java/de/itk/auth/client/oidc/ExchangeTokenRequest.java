package de.itk.auth.client.oidc;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * Erzeugt eine Token-Anfrage bei einem STS basierend auf einem TokenExchange.
 *
 * @author Patrik Stellmann
 */

/**
 * @author pp106349
 */
public class ExchangeTokenRequest extends TokenRequest {

  private String scope = null;
  private String subjectToken = null;

  /**
   * Konstruktor
   *
   * @param config Konfiguration des STS, über den die Token-Anfrage gestellt werden muss.
   * @param scope Scope des neuen Tokens
   * @param subjectToken bestehender Access Token
   */
  public ExchangeTokenRequest(OidcClientStsConfig config, String scope, String subjectToken) {
    super(config, "token_exchange");
    this.scope = scope;
    this.subjectToken = subjectToken;
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.TokenRequest#getParameters()
   */
  @Override
  public List<NameValuePair> getParameters() {
    final List<NameValuePair> parameters = super.getParameters();

    addParameter(parameters, OidcConsts.QP_SCOPE, scope);
    addParameter(parameters, OidcConsts.QP_REQUESTED_TOKEN_TYPE, OidcConsts.TOKEN_TYPE_JWT);
    addParameter(parameters, OidcConsts.QP_SUBJECT_TOKEN, subjectToken);
    addParameter(parameters, OidcConsts.QP_SUBJECT_TOKEN_TYPE, OidcConsts.TOKEN_TYPE_JWT);

    return parameters;
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.TokenRequest#getUrl()
   */
  @Override
  public String getUrl() {
    return config.getTokenExchangeEndpoint();
  }
}
