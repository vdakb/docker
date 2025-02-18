package de.itk.auth.client.oidc;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * Erzeugt eine Token-Anfrage bei einem STS basierend auf einem Refresh-Token.
 *
 * @author Patrik Stellmann
 */
public class RefreshTokenTokenRequest extends TokenRequest {

  private final String prevRefreshToken;

  /**
   * Konstruktor
   *
   * @param config Konfiguration des STS, über den die Token-Anfrage gestellt werden muss.
   * @param refreshToken Refresh-Token, das vom STS bei einer früheren Token-Anfrage zurückgegeben wurde
   */
  public RefreshTokenTokenRequest(OidcClientStsConfig config, String refreshToken) {
    super(config, OidcConsts.GRANT_TYPE_REFRESH_TOKEN);
    this.prevRefreshToken = refreshToken;
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.TokenRequest#getParameters()
   */
  @Override
  public List<NameValuePair> getParameters() {
    final List<NameValuePair> parameters = super.getParameters();

    addParameter(parameters, OidcConsts.QP_REFRESH_TOKEN, prevRefreshToken);

    return parameters;
  }
}
