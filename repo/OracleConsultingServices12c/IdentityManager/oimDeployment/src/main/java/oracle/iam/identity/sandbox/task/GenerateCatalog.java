package oracle.iam.identity.sandbox.task;

import oracle.hst.deployment.ServiceException;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.spi.CatalogAMInstance;
import oracle.iam.identity.common.spi.CatalogGenerator;
import oracle.iam.identity.sandbox.type.CatalogAM;
import oracle.iam.identity.sandbox.type.Instance;
import org.apache.tools.ant.BuildException;

/**
 * Generates a sandbox to be published to Oracle Metadata Store, contains only
 * the active_mdsSandboxMetadata.xml, adfmcatalog.xml.xml and CatalogAM.xml files.
 */
public class GenerateCatalog extends FeaturePlatformTask {

  protected final CatalogGenerator generator;

  /**
   ** Constructs a <code>GenerateCatalog</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GenerateCatalog() {
    super();
    this.generator = new CatalogGenerator(this);
  }

  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  protected void onExecution() throws ServiceException {
    this.generator.execute();
  }

  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link CatalogAM}.
   **
   ** @param  catalogAM          the sandbox to generate.
   **
   ** @throws BuildException     if the {@link CatalogAMInstance} is already
   **                            set.
   */
  public void addConfiguredCatalogAM(final CatalogAM catalogAM)
    throws BuildException {

    this.generator.catalogAM(catalogAM.delegate());
  }
}
