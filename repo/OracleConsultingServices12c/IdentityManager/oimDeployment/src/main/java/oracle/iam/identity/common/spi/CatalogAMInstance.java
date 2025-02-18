package oracle.iam.identity.common.spi;

import oracle.hst.deployment.spi.AbstractInstance;
import oracle.hst.foundation.utility.DateUtility;
import oracle.iam.identity.common.FeaturePlatformTask;
import org.apache.tools.ant.BuildException;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * CatalogAMInstance represents the CatalogAM file
 * what is containing all the form usages in Identity Manager.
 */
public class CatalogAMInstance extends AbstractInstance implements SandboxMetadata {

  /** the working directory */
  private File path = null;
  
  /** the user id written to the sandbox descriptor during generation */
  private String user = FeaturePlatformTask.USERNAME;

  /** the comment written to the sandbox descriptor during generation */
  private String comment = "IdM Long Running Sandbox";
  
  /** the description written to the sandbox descriptor during generation */
  private String description = "IdM Long Running Sandbox";

  /** the timestamp the sandbox was created on */
  private Date created = DateUtility.now();
  
  /** the timestamp the sandbox was last updated */
  private Date updated = DateUtility.now();
 
  /** The Identity Manager release specific version data. */
  private SandboxInstance.Version version = SandboxInstance.Version.PS4;
  
  /** Represents the minor version of the CatalogAM file. */
  private int minor;

  /** Indicates that the working directory has to be deleted after the compression. */
  private boolean cleanup = false;

  /** A collection of {@link UsageInstance} what the CatalogAM file has to contain. */
  private final Set<UsageInstance> usages = new LinkedHashSet<>();

  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException   in case the instance does not meet the
   **                          requirements.
   */
  @Override
  public void validate() throws BuildException {
    if (this.path == null) {
      handleAttributeMissing("path");
    }

    super.validate();
  }

  /**
   * Returns a {@link Set<UsageInstance>} containing all the parsed usages.
   * @return the usages.
   */
  public Set<UsageInstance> usages() {
    return usages;
  }

  /**
   ** Called to inject the <code>usage</code> a <code>UsageInstance</code>
   ** descriptor.
   **
   ** @param  usage              the <code>usage</code> a
   **                            <code>UsageInstance</code> descriptor contains
   *                             child form names.
   */
  public void usage(UsageInstance usage) {
    usages.add(usage);
  }

  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name context to handle in Oracle Weblogic
   **                            Domain server entity instance.
   */
  @Override
  public CatalogAMInstance name(final String name) {
    super.name(name);

    this.path = (this.path != null) ? new File(this.path.getParentFile(), name()) : new File(name);
    return this;
  }

  /**
   ** Returns the directory the <code>Sandbox</code> will be generated within.
   ** <p>
   ** The directoty is the abstract path to the filesystem composed with the
   ** base working directory an the name of the sandbox to isolate sandboxs
   ** during generation.
   **
   ** @return                    the directory the <code>Sandbox</code> will be
   **                            generated within.
   */
  public File path() {
    return path;
  }

  /**
   ** Called to inject the <code>working</code> directory a <code>Sandbox</code>
   ** will be generated within.
   **
   ** @param  path               the directory a <code>Sandbox</code> will be
   **                            generated within.
   **
   ** @return                    the <code>SandboxInstance</code> for method
   **                            chaining purpose.
   */
  public void path(final File path) {
    this.path = (name() != null) ? new File(path, name()) : path;
  }

  /**
   ** Returns the minor version of the CatalogAM file.
   **
   ** @return                    <code>int</code> which represents the minor
   **                            version of the CatalogAM file.
   */
  public int minor() {
    return minor;
  }

  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>minor</code>.
   **
   ** @param  minor           <code>int</code> which represents the minor
   **                         version of the CatalogAM file.
   */
  public void minor(final int minor) {
    this.minor = minor;
  }

  /**
   ** Returns the <code>comment</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>comment</code> an
   **                            <code>CatalogAM Instance</code> belongs to.
   */
  @Override
  public String comment() {
    return comment;
  }

  /**
   ** Called to inject the <code>comment</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  comment            the <code>comment</code> a <code>Sandbox</code>
   **                            descriptor contains to be imported into
   **                            Identity Manager.
   */
  public void comment(final String comment) {
    this.comment = comment;
  }
  
  /**
   ** Returns the <code>description</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>description</code> an
   **                            <code>CatalogAM Instance</code> belongs to.
   */
  @Override
  public String description() {
    return description;
  }

  /**
   ** Called to inject the <code>description</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  description        the <code>description</code> a
   **                            <code>Sandbox</code> descriptor contains to be
   **                            imported into Identity Manager.
   */
  public void description(final String description) {
    this.description = description;
  }

  /**
   ** Called to inject the <code>user</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  user               the <code>user</code> a <code>Sandbox</code>
   **                            descriptor contains to be imported into
   **                            Identity Manager.
   */
  public void user(final String user) {
    this.user = user;
  }

  /**
   ** Returns the <code>user</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>user</code> an
   **                            <code>CatalogAM Instance</code> belongs to.
   */
  @Override
  public String user() {
    return user;
  }

  /**
   ** Called to inject the <code>created</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  created            the <code>created</code> a
   **                            <code>Sandbox</code> descriptor contains to be
   **                            imported into Identity Manager.
   */
  public void created(final Date created) {
    this.created = created;
  }

  /**
   ** Returns the <code>created</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>created</code> an
   **                            <code>CatalogAM Instance</code> belongs to.
   */
  @Override
  public Date created() {
    return created;
  }

  /**
   ** Called to inject the <code>updated</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  updated            the <code>updated</code> a
   **                            <code>Sandbox</code> descriptor contains to be
   **                            imported into Identity Manager.
   */
  public void updated(final Date updated) {
    this.updated = updated;
  }

  /**
   ** Returns the <code>updated</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>updated</code> an
   **                            <code>CatalogAM Instance</code> belongs to.
   */
  @Override
  public Date updated() {
    return updated;
  }

  /**
   ** Returns the <code>Version</code> a <code>Sandbox</code> descriptor
   ** contains to be imported into Identity Manager.
   **
   ** @return                    the <code>Version</code> a <code>Sandbox</code>
   **                            belongs to.
   */
  @Override
  public SandboxInstance.Version version() {
    return version;
  }

  /**
   ** Called to inject the <code>version</code> a <code>Sandbox</code>
   ** descriptor contains to be imported into Identity Manager.
   **
   ** @param  version            the <code>Version</code> a <code>Sandbox</code>
   **                            descriptor contains to be imported into
   **                            Identity Manager.
   */
  public void version(SandboxInstance.Version version) {
    this.version = version;
  }

  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>cleanup</code>.
   **
   ** @param  cleanup           <code>true</code> to delete the working directory.
   */
  public void cleanup(final boolean cleanup) {
    this.cleanup = cleanup;
  }

  /**
   ** Returns the how to handle working directory.
   **
   ** @return                    <code>true</code> if the working directory
   **                            has to be deleted after compression.
   */
  public final boolean cleanup() {
    return this.cleanup;
  }
}
