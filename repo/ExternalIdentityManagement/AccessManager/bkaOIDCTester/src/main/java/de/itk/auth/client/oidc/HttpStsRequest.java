package de.itk.auth.client.oidc;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

/**
 * Basisklasse für HTTP-Requests bei einem STS.
 *
 * @author Patrik Stellmann
 *
 */
public abstract class HttpStsRequest {

  protected final OidcClientStsConfig config;

  /**
   * Konstruktor
   *
   * @param config Konfiguration des STS, über den die Anfragen gestellt werden.
   */
  protected HttpStsRequest(OidcClientStsConfig config) {
    this.config = config;
  }

  /** Erzeugt einen HTTP Client zum Aufruf des STS mit der entspr. Konfiguration.
   * @return Neue Instanz eines HTTP Clients
   */
  protected Client createHttpClient() {
    final ClientConfig config = new ClientConfig();
    if (this.config.hasProxy()) {
      /*
      final HttpHost          proxy = new HttpHost(config.getProxyIp(), config.getProxyPort());
      final HttpRoutePlanner  routePlanner = new DefaultProxyRoutePlanner(proxy);
      final HttpClientBuilder clientBuilder = HttpClients.custom().setRoutePlanner(routePlanner);
      return clientBuilder.build();
      */
//      config.property(ClientProperties.PROXY_URI, this.config.getProxyIp() + ":" + this.config.getProxyPort());
    }
//    return ClientBuilder.withConfig(config).build();
    return ClientBuilder.newBuilder().withConfig(config).build();
  }
}
