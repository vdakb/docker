package de.itk.auth.client.oidc;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * Erzeugt eine Token-Anfrage bei einem STS basierend auf einem Authorization-Code.
 *
 * @author Patrik Stellmann
 */
public class AuthorizationCodeTokenRequest extends TokenRequest {

  private String       codeVerifier = null;
  private final String authorizationCode;

  /**
   * Konstruktor
   *
   * @param config Konfiguration des STS, über den die Token-Anfrage gestellt werden muss.
   * @param authorizationCode Authorization-Code, der vom STS zurückgegeben wurde
   */
  public AuthorizationCodeTokenRequest(OidcClientStsConfig config, String authorizationCode) {
    super(config, "authorization_code");
    this.authorizationCode = authorizationCode;
  }

  /**
   * Setzt den Code-Verifier, dessen Hash-Wert als Code-Challenge bei der Authentifizierungsanfrage an den STS übergeben wurd.
   *
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public AuthorizationCodeTokenRequest setCodeVerifier(String codeVerifier) {
    this.codeVerifier = codeVerifier;
    return this;
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.TokenRequest#getParameters()
   */
  @Override
  public List<NameValuePair> getParameters() {
    final List<NameValuePair> parameters = super.getParameters();

    addParameter(parameters, OidcConsts.QP_CODE_VERIFIER, codeVerifier);
    addParameter(parameters, OidcConsts.QP_CODE, authorizationCode);

    return parameters;
  }
}
