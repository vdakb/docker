package de.itk.auth.client;

import java.io.InputStream;
import java.io.IOException;

import java.util.Properties;

import java.util.logging.Logger;

/** Utility-Klasse zum Auslesen der Applikationsversion "AppVersion" aus einer AppVersion.properties im Classpath.
 *
 * @author Patrik Stellmann
 *
 */
public class GetAppVersion {

  private static final String THIS = GetAppVersion.class.getName();

  private static final Logger LOGGER = Logger.getLogger(THIS);

  private static final String DEFAULT = "?";
  private static final String PROP_FILE = "AppVersion.properties";
  private static final String PROP_NAME = "AppVersion";

  protected static String version = null;

  private GetAppVersion() {
    // hide default constructor
  }

  /**
   * @return Applikationsversion oder "?", sofern keien ausgelesen werden konnte.
   */
  public static String get() {
    if (version == null) {
      final Properties props = new Properties();
      try {
        final InputStream inputStream = GetAppVersion.class.getClassLoader().getResourceAsStream(PROP_FILE);
        if (inputStream != null) {
          props.load(inputStream);
          inputStream.close();
          version = props.getProperty(PROP_NAME, DEFAULT);
        }
        else {
          LOGGER.severe(String.format("Properties-File '%s' konnte nicht gefunden werden.", PROP_FILE));
          version = DEFAULT;
        }
      }
      catch (IOException e) {
        LOGGER.throwing(THIS, "get", e);
        version = DEFAULT;
      }
    }
    return version;
  }

}
