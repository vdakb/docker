package oracle.iam.identity.common.spi;

import oracle.hst.foundation.utility.StringUtility;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines the possible values of the attribute forceOverride
 * on a {@link SandboxInstance}.
 */
public enum ForceOverride {
  TRUE("yes", "true"),
  FALSE("no", "false"),
  NEVER("never");

  private final Set<String> values = new HashSet<>();

  ForceOverride(String... values) {
    this.values.addAll(Arrays.asList(values));
  }

  public static ForceOverride from(final String override) {
    for (ForceOverride cursor : ForceOverride.values()) {
      for (String value : cursor.values) {
        if (StringUtility.equalsWithIgnoreCase(value, override)) {
          return cursor;
        }
      }
    }

    return ForceOverride.FALSE;
  }
}
