package oracle.iam.identity.sandbox.type;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;
import oracle.iam.identity.common.spi.CatalogAMInstance;
import oracle.iam.identity.common.spi.SandboxInstance;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import java.io.File;

public class CatalogAM extends DataType {

  private final CatalogAMInstance delegate = new CatalogAMInstance();

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
   ** Sets the abstract working {@link File}.
   **
   ** @param  path               the abstract working {@link File}.
   */
  public void setPath(final File path) {
    checkAttributesAllowed();

    if (path.isFile()) {
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_DIRECTORY_ISFILE));
    }
    this.delegate.path(path);
  }

  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>version</code>.
   **
   ** @param  value              the version of the sandbox in Identity Manager
   **                            to handle.
   */
  public void setVersion(final String version) {
    checkAttributesAllowed();
    this.delegate.version(SandboxInstance.Version.from(version));
  }

  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>minor</code>.
   **
   ** @param  minor           <code>int</code> to delete the working directory.
   */
  public void setMinor(final String minor) {
    checkAttributesAllowed();
    this.delegate.minor(Integer.parseInt(minor));
  }

  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description        the description of the sandbox in Identity
   **                            Manager to handle.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    this.delegate.description(description);
  }

  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>cleanup</code>.
   **
   ** @param  cleanup           <code>true</code> to delete the working directory.
   */
  public void setCleanup(final boolean cleanup) {
    checkAttributesAllowed();
    this.delegate.cleanup(cleanup);
  }

  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Usage}.
   **
   ** @param  usage             the usage to generate.
   **
   ** @throws BuildException     if the {@link Usage} already contained in
   **                            the collection of this generation operation.
   */
  public void addConfiguredUsage(final Usage usage)
      throws BuildException {

    this.delegate.usage(usage.delegate());
  }

  /**
   ** Returns the {@link CatalogAMInstance} this ANT type wrappes.
   **
   ** @return                    the {@link CatalogAMInstance} this ANT type
   **                            wrappes.
   */
  public CatalogAMInstance delegate() {
    return delegate;
  }
}
