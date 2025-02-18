package de.itk.auth.client.demoutil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DemoConst {

  protected DemoConst() {
    throw new IllegalStateException("Utility class");
  }

  /* session attributes */
  public static final String SA_LOG = "Log";

  public static final String NEWLINE = "\n";

  public static final String      DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
  public static final String      DATE_FORMAT_MS = "dd.MM.yyyy HH:mm:ss.SSS";
  public static final int         DATE_FORMAT_MS_LEN = DATE_FORMAT_MS.length();
  public static final Set<String> JWT_DATE_CLAIMS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("iat", "exp", "auth_time")));
}
