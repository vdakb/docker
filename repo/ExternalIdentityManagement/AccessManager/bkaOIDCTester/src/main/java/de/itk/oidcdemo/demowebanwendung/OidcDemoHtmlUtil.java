package de.itk.oidcdemo.demowebanwendung;

import javax.servlet.http.HttpSession;

import de.itk.auth.client.InitException;
import de.itk.auth.client.demoutil.DemoHtmlUtil;
import de.itk.auth.client.oidc.TokenRequest;

public class OidcDemoHtmlUtil {

  private static final String HTML_NO_JWT = "<p><i>Kein JWT -&gt; kein Auflösen der Inhalte möglich</i></p>";

  private OidcDemoHtmlUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static String tokenRequestToHtml(TokenRequest request) {
    return DemoHtmlUtil.postRequestToHtml(request.getUrl(), request.getHeaderParameters(), request.getParameters());
  }

  public static String tokenToHtml(HttpSession session, String attributeName)
    throws InitException {
    final String raw = (String)session.getAttribute(attributeName);
    if (raw != null) {
      final Token token = new Token(raw);
      if (token.isJwt()) {
        final StringBuilder sb = new StringBuilder();

        if (attributeName.equals(OidcDemoConst.SA_ACCESS_TOKEN) || attributeName.equals(OidcDemoConst.SA_ID_TOKEN)) {
          final OidcDemoConfig config = OidcDemoConfig.getInstance(session.getServletContext());
          sb.append("<h6 class=\"mb-2\">Validierung</h6>");
          sb.append(DemoHtmlUtil.HTML_TABLE_START);
          DemoHtmlUtil.appendHtmlCheckRow(sb, "Ablaufzeitpunkt liegt in der Zukunft", !token.isExpired());
          DemoHtmlUtil.appendHtmlCheckRow(sb, "Signieralgorithmus entspricht 'RS256'", token.isValidAlgorithm());
          DemoHtmlUtil.appendHtmlCheckRow(sb, "Signatur ist gültig", token.isSignatureValid(config.getJwkProvider()));
          if (attributeName.equals(OidcDemoConst.SA_ACCESS_TOKEN)) {
            final String scopeClaim = config.getScopeClaim();
            DemoHtmlUtil.appendHtmlCheckRow(sb, "Eigener Scope ('" + config.getRequiredScope() + "') ist enthalten (Claim '" + scopeClaim + "')", token.containsScope(scopeClaim, config.getRequiredScope()));
          }
          sb.append(DemoHtmlUtil.HTML_TABLE_END);
        }

        sb.append("<h6 class=\\\"mb-2\\\">Header</h6>");
        sb.append(DemoHtmlUtil.HTML_JSON_START);
        DemoHtmlUtil.appendJsonRows(sb, token.getHeaderAsJson());
        sb.append(DemoHtmlUtil.HTML_JSON_END);

        sb.append("<h6 class=\\\"mb-2\\\">Payload</h6>");
        sb.append(DemoHtmlUtil.HTML_JSON_START);
        DemoHtmlUtil.appendJsonRows(sb, token.getPayloadAsJson());
        sb.append(DemoHtmlUtil.HTML_JSON_END);

        return sb.toString();
      }
      else {
        return HTML_NO_JWT;
      }
    }
    else {
      return "";
    }
  }

}
