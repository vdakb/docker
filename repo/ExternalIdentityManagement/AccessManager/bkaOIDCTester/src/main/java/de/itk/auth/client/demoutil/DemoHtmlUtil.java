package de.itk.auth.client.demoutil;

import java.net.URI;
import java.net.URISyntaxException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class DemoHtmlUtil {

  public static final String HTML_NO_CONTENT = "<p><i>Kein Inhalt</i></p>";
  public static final String HTML_NO_JSON = "<p><i>Kein JSON-Inhalt</i></p>";
  public static final String HTML_NO_URI = "<p><i>Keine URI</i></p>";
  public static final String HTML_TABLE_START = "<table class=\"parameters mb-3\">";
  public static final String HTML_TABLE_END = "</table>";
  public static final String HTML_TOKEN_START = "<input class=\"token\" type=\"text\" readonly value=\"";
  public static final String HTML_TOKEN_END = "\" onClick=\"this.select()\">";
  public static final String HTML_KEY_COL_START = "<tr><td class=\"key\">";
  public static final String HTML_LAST_COL_END = "</td></tr>";
  public static final String HTML_P_CODE_START = "<p class=\"code\">";
  public static final String HTML_P_XML_START = "<p class=\"code xml\">";
  public static final String HTML_P_END = "</p>";
  public static final String HTML_JSON_START = "<div class=\"json mb-3\"><table><tr><td colspan=\"3\">{</td></tr>";
  public static final String HTML_JSON_END = "<tr><td colspan=\"3\">}</td></tr></table></div>";
  public static final String HTML_ERROR = "<span class=\"error\">FEHLER</span>";
  public static final String HTML_OK = "OK";
  public static final String ADDRESS = "Address";

  public static final String DATE_FORMAT_XML = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  public static final String SINGLE_INDENT = "  ";
  public static final String ATTR_INDENT = "    ";

  public static final String BR = "\r\n";
  public static final String LT = "&lt;";
  public static final String GT = "&gt;";

  public static final String ELEMENT_START = "<span class=\"element\">";
  public static final String ELEMENT_END = "</span>";
  public static final String ATTR_START = "<span class=\"attr\">";
  public static final String ATTR_END = "</span>";
  public static final String ATTRVAL_START = "<span class=\"attrval\">";
  public static final String ATTRVAL_END = "</span>";
  public static final String TEXT_START = "<span class=\"text\">";
  public static final String TEXT_END = "</span>";

  private DemoHtmlUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static String stringToHtml(String string) {
    if (string != null) {
      try {
        final JsonNode json = (new ObjectMapper()).readTree(string);
        if (json instanceof ObjectNode) {
          return jsonToHtml((ObjectNode)json);
        }
        else {
          return stringAsCode(string);
        }
      }
      catch (JsonProcessingException e) {
        return stringAsCode(string);
      }
    }
    else {
      return HTML_NO_CONTENT;
    }
  }

  public static String stringAsCode(String string) {
    final StringBuilder sb = new StringBuilder();
    sb.append(HTML_P_CODE_START);
    sb.append(string);
    sb.append(HTML_P_END);

    return sb.toString();
  }

  public static String jsonToHtml(String jsonStr) {
    try {
      final ObjectNode json = (ObjectNode)(new ObjectMapper().readTree(jsonStr));
      return jsonToHtml(json);
    }
    catch (JsonProcessingException e) {
      return stringAsCode(jsonStr);
    }
  }

  public static String jsonToHtml(ObjectNode json) {
    if (json != null) {
      final StringBuilder sb = new StringBuilder();
      sb.append(HTML_JSON_START);
      appendJsonRows(sb, json);
      sb.append(HTML_JSON_END);

      return sb.toString();
    }
    else {
      return HTML_NO_JSON;
    }
  }

  public static void appendJsonRows(StringBuilder sb, ObjectNode json) {
    if (json != null) {
      final Iterator<Entry<String, JsonNode>> it = json.fields();
      while (it.hasNext()) {
        final Entry<String, JsonNode> entry = it.next();
        appendJsonRow(sb, entry.getKey(), entry.getValue(), it.hasNext());
      }
    }
  }

  public static String uriToHtml(String uriString) {
    if (uriString != null) {
      try {

        final URI           uri = new URI(uriString);
        final StringBuilder sb = new StringBuilder();
        sb.append(HTML_TABLE_START);

        appendUriAsHtmlRows(sb, uri);

        sb.append(HTML_TABLE_END);

        return sb.toString();

      }
      catch (URISyntaxException e) {
        return "<p>ungültige URI: <code>" + uriString + "</code></p>";
      }
    }
    else {
      return HTML_NO_URI;
    }
  }

  public static void appendUriAsHtmlRows(StringBuilder sb, URI uri) {
    try {
      appendHtmlRow(sb, ADDRESS, new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, uri.getFragment()).toString());
    }
    catch (URISyntaxException e) {
      appendHtmlRow(sb, "Adresse", uri.toString());
    }

    final String query = uri.getQuery();
    if (query != null) {
      final String[] pairs = query.split("&");
      for (String pair : pairs) {
        final String[] elements = pair.split("=");
        appendHtmlRow(sb, elements[0], (elements.length > 1) ? DemoUtil.urlDecode(elements[1]) : "");
      }
    }
  }

  public static String getRequestToHtml(HttpServletRequest get) {
    final StringBuilder sb = new StringBuilder();

    sb.append(HTML_TABLE_START);

    appendHtmlRow(sb, "Methode", "GET");

    appendUriAsHtmlRows(sb, get.getURI());

    final Enumeration<String> hit = get.getHeaderNames();
    while (hit.hasMoreElements()) {
      appendHtmlRow(sb, "Header: " + hit, get.getHeader(hit.nextElement()));
    }

    sb.append(HTML_TABLE_END);

    return sb.toString();
  }

  public static String postRequestToHtml(String uri, List<NameValuePair> headerParameters, List<NameValuePair> parameters) {
    final StringBuilder sb = new StringBuilder();

    sb.append(HTML_TABLE_START);

    appendHtmlRow(sb, "Methode", "POST");
    appendHtmlRow(sb, ADDRESS, uri);

    if (headerParameters != null) {
      for (NameValuePair entry : headerParameters) {
        appendHtmlRow(sb, "Header: " + entry.getName(), entry.getValue());
      }
    }

    for (NameValuePair entry : parameters) {
      appendHtmlRow(sb, entry.getName(), entry.getValue());
    }

    sb.append(HTML_TABLE_END);

    return sb.toString();
  }

  public static void appendHtmlRow(StringBuilder sb, String key, String value) {
    sb.append(HTML_KEY_COL_START);
    sb.append(key);
    sb.append("</td><td class=\"value\">");
    if (value != null) {
      if (key.equals("client_secret")) {
        for (int i = 0; i < value.length(); ++i) {
          sb.append("*");
        }
      }
      else if ((key.equals("iat") || key.equals("exp") || key.equals("auth_time")) && (value.matches("^\\d+$"))) {
        sb.append(new SimpleDateFormat(DemoConst.DATE_FORMAT).format(new Date(Long.parseLong(value) * 1000)));
      }
      else {
        sb.append(value);
      }
    }
    sb.append(HTML_LAST_COL_END);
  }

  public static void appendHtmlCheckRow(StringBuilder sb, String key, boolean value) {
    sb.append(HTML_KEY_COL_START);
    sb.append(key);
    sb.append("</td><td class=\"checkvalue\">");
    sb.append(value ? HTML_OK : HTML_ERROR);
    sb.append(HTML_LAST_COL_END);
  }

  public static void appendJsonRow(StringBuilder sb, String key, JsonNode value, boolean hasNext) {
    sb.append(HTML_KEY_COL_START);
    sb.append("\"");
    sb.append(key);
    sb.append("\":</td><td class=\"value\">");
    if (value != null) {
      if (value.isArray()) {
        appendJsonArrayValue(sb, value);
      }
      else {
        sb.append(value.toString());
      }
      if (hasNext) {
        sb.append(",");
      }
      if ((DemoConst.JWT_DATE_CLAIMS.contains(key)) && (value.isIntegralNumber())) {
        sb.append("</td><td class=\"comment unselectable\" style=\"white-space:pre\">");
        sb.append(new SimpleDateFormat(DemoConst.DATE_FORMAT).format(new Date(value.asLong() * 1000)));
      }
      else {
        sb.append("</td><td>");
      }
    }
    sb.append(HTML_LAST_COL_END);
  }

  public static void appendJsonArrayValue(StringBuilder sb, JsonNode value) {
    sb.append("[");
    final Iterator<JsonNode> it = value.iterator();
    while (it.hasNext()) {
      final JsonNode entry = it.next();
      sb.append(entry.toString());
      if (it.hasNext()) {
        sb.append(",<br/> ");
      }
    }
    sb.append("]");
  }

  public static String logToHtml(String log) {
    if (log != null) {
      final StringBuilder sb = new StringBuilder();

      sb.append("<table class=\"log\">");
      final String[] rows = log.split(DemoConst.NEWLINE);
      for (int i = 0; i < rows.length; ++i) {
        final String time = (rows[i].length() > DemoConst.DATE_FORMAT_MS_LEN) ? rows[i].substring(0, DemoConst.DATE_FORMAT_MS_LEN + 1) : "";
        final String text = (rows[i].length() > DemoConst.DATE_FORMAT_MS_LEN) ? rows[i].substring(DemoConst.DATE_FORMAT_MS_LEN + 2) : rows[i];
        sb.append("<tr><td>");
        sb.append(time);
        sb.append("</td><td>");
        sb.append(text);
        sb.append(HTML_LAST_COL_END);
      }
      sb.append(HTML_TABLE_END);

      return sb.toString();
    }
    else {
      return "";
    }
  }

  public static void xmlNodeToHtml(Node node, StringBuilder sb, String indent) {
    if (node.getNodeType() == Node.DOCUMENT_NODE) {
      xmlNodeListToHtml(node.getChildNodes(), sb, indent);
    }
    else if (node.getNodeType() == Node.ELEMENT_NODE) {
      xmlElementToHtml(node, sb, indent);
    }
    else if (node.getNodeType() == Node.TEXT_NODE) {
      xmlTextToHtml(node, sb);
    }
  }

  public static void xmlElementToHtml(Node node, StringBuilder sb, String indent) {
    if ((node.getPreviousSibling() != null) || ((node.getParentNode() != null) && (node.getParentNode().getNodeType() != Node.DOCUMENT_NODE))) {
      sb.append(BR);
    }
    sb.append(indent);
    sb.append(ELEMENT_START);
    sb.append(LT);
    sb.append(node.getNodeName());
    sb.append(ELEMENT_END);
    xmlAttributesToHtml(node.getAttributes(), sb, indent);
    if (node.getChildNodes().getLength() > 0) {
      sb.append(ELEMENT_START);
      sb.append(GT);
      sb.append(ELEMENT_END);

      final NodeList childNodes = node.getChildNodes();
      xmlNodeListToHtml(childNodes, sb, indent + SINGLE_INDENT);

      if ((childNodes.getLength() > 1) || (childNodes.item(0).getNodeType() != Node.TEXT_NODE)) {
        sb.append(BR);
        sb.append(indent);
      }
      sb.append(ELEMENT_START);
      sb.append(LT);
      sb.append("/");
      sb.append(node.getNodeName());
      sb.append(GT);
      sb.append(ELEMENT_END);
    }
    else {
      sb.append(ELEMENT_START);
      sb.append("/");
      sb.append(GT);
      sb.append(ELEMENT_END);
    }
  }

  public static void xmlNodeListToHtml(NodeList nodeList, StringBuilder sb, String indent) {
    for (int i = 0; i < nodeList.getLength(); ++i) {
      xmlNodeToHtml(nodeList.item(i), sb, indent);
    }
  }

  public static void xmlAttributesToHtml(NamedNodeMap attributes, StringBuilder sb, String indent) {
    final int length = attributes.getLength();
    if (length == 1) {
      sb.append(" ");
      xmlAttrToHtml(attributes.item(0), sb);
    }
    else if (length > 1) {
      for (int i = 0; i < length; ++i) {
        final Node attribute = attributes.item(i);
        sb.append(BR);
        sb.append(indent);
        sb.append(ATTR_INDENT);
        xmlAttrToHtml(attribute, sb);
      }
    }
  }

  public static void xmlTextToHtml(Node text, StringBuilder sb) {
    sb.append(TEXT_START);
    sb.append(text.getNodeValue());
    sb.append(TEXT_END);
  }

  public static void xmlAttrToHtml(Node attribute, StringBuilder sb) {
    sb.append(ATTR_START);
    sb.append(attribute.getNodeName());
    sb.append("=");
    sb.append(ATTR_END);

    sb.append(ATTRVAL_START);
    sb.append("\"");
    sb.append(attribute.getNodeValue());
    sb.append("\"");
    sb.append(ATTRVAL_END);
  }

}
