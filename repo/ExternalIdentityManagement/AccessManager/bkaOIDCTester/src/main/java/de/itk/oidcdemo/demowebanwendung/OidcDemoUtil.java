package de.itk.oidcdemo.demowebanwendung;

import javax.servlet.http.HttpSession;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.fasterxml.jackson.databind.JsonNode;

import de.itk.auth.client.InitException;

public class OidcDemoUtil {

  private OidcDemoUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static long getExpireTimeForToken(HttpSession session, String attributKey) {
    final String raw = (String)session.getAttribute(attributKey);
    if (raw != null) {
      final Token token = new Token(raw);
      return token.getExpires().getTime();
    }
    else {
      return 0;
    }
  }

  public static String stringFromJsonNode(JsonNode jsonNode) {
    if ((jsonNode != null) && (jsonNode.isTextual())) {
      return jsonNode.asText();
    }
    else {
      return null;
    }
  }

  public static boolean isTokenExpired(HttpSession session, String attributeName) {
    final String raw = (String)session.getAttribute(attributeName);
    if (raw != null) {
      final Token token = new Token(raw);
      return token.isExpired();
    }
    else {
      return true;
    }
  }

  public static String getScope(HttpSession session)
    throws InitException {
    final String accessTokenRaw = (String)session.getAttribute(OidcDemoConst.SA_ACCESS_TOKEN);
    if (accessTokenRaw != null) {
      final Token      accessToken = new Token(accessTokenRaw);
      final DecodedJWT accessJwt = accessToken.getJwt();
      if (accessJwt != null) {
        final Claim scopeClaim = accessJwt.getClaim("scope");
        if (scopeClaim == null) {
          return "?";
        }
        else if (scopeClaim.asString() != null) {
          return scopeClaim.asString();
        }
        else if (scopeClaim.asList(String.class) != null) {
          return String.join(" ", scopeClaim.asList(String.class));
        }
        else {
          return "?";
        }
      }
      else {
        return "?";
      }
    }
    else {
      return OidcDemoConfig.getInstance(session.getServletContext()).getDefaultScope();
    }
  }

}
