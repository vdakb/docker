package de.itk.auth.client;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwk.SigningKeyNotFoundException;
import com.auth0.jwk.UrlJwkProvider;

public class AuthClientUtil {

  private AuthClientUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * @return Int-Wert des Strings oder null, falls der String null oder leer ist.
   */
  public static Integer intFromString(String string) {
    return ((string == null) || (string.isEmpty())) ? null : Integer.valueOf(string);
  }

  public static UrlJwkProvider createUrlJwkProvider(String jwksUri, String proxyIp, int proxyPort, String domain)
    throws SigningKeyNotFoundException, MalformedURLException {
    Proxy proxy = null;
    if ((proxyIp != null) && (!proxyIp.isEmpty())) {
      proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
    }
    final Map<String, String> headers = new HashMap<>();
    if ((domain != null) && (!domain.isEmpty())) {
      headers.put("x-oauth-identity-domain-name", domain);
    }

    final UrlJwkProvider jwkProvider = new UrlJwkProvider(new URL(jwksUri), null, null, proxy, headers);
    jwkProvider.getAll(); // Abfrage der URL in dieser Phase sicherstellen.
    return jwkProvider;
  }

}
