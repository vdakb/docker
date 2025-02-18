package de.itk.auth.client.demoutil;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.text.SimpleDateFormat;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DemoUtil {

  private static final Logger logger = LogManager.getLogger();

  private DemoUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static String getBaseUrl(HttpServletRequest request) {
    String scheme = request.getScheme() + "://";
    String serverName = request.getServerName();
    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
    String contextPath = request.getContextPath();
    return scheme + serverName + serverPort + contextPath;
  }

  public static String urlDecode(String encoded) {
    try {
      return URLDecoder.decode(encoded, "utf-8");
    }
    catch (UnsupportedEncodingException e) {
      return encoded;
    }
  }

  @SuppressWarnings("unchecked") public static <T> T getSessionAttribute(HttpSession session, String name, T defaultValue) {
    if (session != null) {
      final Object value = session.getAttribute(name);
      if (defaultValue.getClass().isInstance(value)) {
        return (T)value;
      }
      else {
        return defaultValue;
      }
    }
    else {
      return defaultValue;
    }
  }

  public static void addToSessionLog(HttpSession session, String entry) {
    final String entryCleaned = entry.replaceAll("[\n\r\t]+", " ");
    logger.info("Log-Entry: {}", entryCleaned);
    String log = (String)session.getAttribute(DemoConst.SA_LOG);
    if (log == null) {
      log = "";
    }
    else {
      log += DemoConst.NEWLINE;
    }
    log += new SimpleDateFormat(DemoConst.DATE_FORMAT_MS).format(new Date()) + ": " + entryCleaned;
    session.setAttribute(DemoConst.SA_LOG, log);
  }

  public static List<Map.Entry<String, String>> parseWebappLinks(String webappLinksStr) {
    List<Map.Entry<String, String>> webappLinks = new LinkedList<>();

    if ((webappLinksStr != null) && (!webappLinksStr.isEmpty())) {
      final String[] webappLinksList = webappLinksStr.split("[|]");
      for (int i = 0; i < webappLinksList.length; i += 2) {
        webappLinks.add(new AbstractMap.SimpleImmutableEntry<>(webappLinksList[i], ((i + 1) < webappLinksList.length) ? webappLinksList[i + 1] : "Webanwendung"));
      }
    }
    return webappLinks;
  }

  public static List<String> parseStringList(String listStr) {
    List<String> list;

    if ((listStr != null) && (!listStr.isEmpty())) {
      list = Arrays.asList(listStr.split("[\\s,]+"));
    }
    else {
      list = new LinkedList<>();
    }
    return list;
  }

}
