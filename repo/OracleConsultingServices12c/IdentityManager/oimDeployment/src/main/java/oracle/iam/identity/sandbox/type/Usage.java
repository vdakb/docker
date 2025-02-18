package oracle.iam.identity.sandbox.type;

import oracle.iam.identity.common.spi.UsageInstance;
import org.apache.tools.ant.types.DataType;

/**
 * Usage defines the account and it's child forms.
 */
public class Usage extends DataType {

  private final UsageInstance delegate = new UsageInstance();

  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the sandbox in Identity Manager to
   **                            handle.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>forms</code>.
   **
   ** @param  forms              the forms of the account separated by whitespace
   */
  public void setForms(final String forms) {
    checkAttributesAllowed();
    for (String form : forms.split(" ")) {
      this.delegate.form(form);
    }
  }

  /**
   ** Returns the {@link UsageInstance} this ANT type wrappes.
   **
   ** @return                    the {@link UsageInstance} this ANT type
   **                            wrappes.
   */
  public UsageInstance delegate() {
    return delegate;
  }

}
