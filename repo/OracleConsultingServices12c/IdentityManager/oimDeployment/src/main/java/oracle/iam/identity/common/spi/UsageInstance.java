package oracle.iam.identity.common.spi;

import oracle.hst.deployment.spi.AbstractInstance;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * UsageInstance represents the child forms of an account.
 */
public class UsageInstance extends AbstractInstance {

  /** A collection of the child forms if the account. */
  private final Set<String> forms = new LinkedHashSet<>();

  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>form</code>.
   **
   ** @param  form           <code>form</code> which represents a child form
   **                        of the account.
   */
  public void form(final String form) {
    forms.add(form);
  }

  /**
   ** Returns a <code>Set<String></code> what contains the names of the child
   ** form of the account.
   **
   ** @return                    the child forms of the account.
   */
  public Set<String> forms() {
    return forms;
  }
}
