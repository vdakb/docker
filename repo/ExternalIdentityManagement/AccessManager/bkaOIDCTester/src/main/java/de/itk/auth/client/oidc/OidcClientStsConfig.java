package de.itk.auth.client.oidc;

import com.auth0.jwk.JwkProvider;

/**
 * Interface für das Abfragen der STS-Konfiguration durch einen Client
 *
 * @author Patrik Stellmann
 *
 */
public interface OidcClientStsConfig {

  /** Muss die ID des Clients beim STS zurückgeben.
   * @return
   */
  public String getClientId();

  /** Muss das Secret des Clients beim STS zurückgeben.
   * @return
   */
  public String getClientSecret();

  /** Muss den Namen des Claims zurückgeben, in dem der Scope gespeichetr ist (i.d.R. "scp" oder "scope")
   * @return
   */
  public String getScopeClaim();

  /** Muss die URL des STS für die Authorization-Anfrage liefern.
   * @return die URL
   */
  public String getAuthorizationEndpoint();

  /** Muss die URL des STS für die Token-Anfrage liefern.
   * @return die URL
   */
  public String getTokenEndpoint();

  /** Muss die URL des STS für die Token-Exchange-Anfrage liefern.
   * @return die URL
   */
  public String getTokenExchangeEndpoint();

  /** Muss die URL des STS für die Userinfo-Anfrage liefern.
   * @return die URL
   */
  public String getUserinfoEndpoint();

  /** Muss die URL des STS für die Logout-Anfrage liefern.
   * @return die URL
   */
  public String getEndSessionEndpoint();

  /** Muss einen {@link com.auth0.jwk.JwkProvider JwkProvider} des STS liefern.
   * @return JwkProvider
   */
  public JwkProvider getJwkProvider();

  /** Muss die Domain des STS (nur für Oracle Access Manager) liefern, sofern eine anzugeben ist, oder sonst null.
   * @return die Domain
   */
  public String getDomain();

  /** Muss true zurückliefern, sofern ein Proxy verwendet werden soll.
   * @return
   */
  public boolean hasProxy();

  /** Muss die IP-Adresse des Proxys liefern, sofern einer zu verwenden ist, oder sonst null.
   * @return die IP-Adresse
   */
  public String getProxyIp();

  /** Muss den Port des Proxys liefern, sofern einer zu verwenden ist, oder sonst null.
   * @return der Port
   */
  public Integer getProxyPort();
}
