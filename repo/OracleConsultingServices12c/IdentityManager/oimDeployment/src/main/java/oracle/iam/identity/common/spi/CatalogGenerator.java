package oracle.iam.identity.common.spi;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;
import oracle.hst.deployment.task.ServiceProvider;
import oracle.hst.deployment.type.MDSServerContext;
import oracle.hst.foundation.xml.XMLException;
import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;
import oracle.iam.identity.common.adf.marshal.Catalog;
import oracle.iam.identity.common.adf.marshal.CatalogFactory;
import oracle.iam.identity.common.adf.marshal.Metadata;
import oracle.iam.identity.common.adf.marshal.Sandbox;
import org.apache.tools.ant.BuildException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides basic implementations to generate sandbox artifacts that are published in the Metadata Store.
 */
public class CatalogGenerator extends ServiceProvider {

  private CatalogAMInstance catalogAM;

  /**
   ** Create a new <code>CatalogGenerator</code> to initialize the instance.
   **
   ** @param  frontend           the {@link ServiceFrontend} that instantiated
   **                            this service.
   */
  public CatalogGenerator(final ServiceFrontend frontend) {
    super(frontend);
  }

  /**
   ** Call by the ANT deployment to inject the argument for setting an
   ** {@link CatalogAMInstance}.
   **
   ** @param  catalogAM          descriptor that contains catalog
   **                            metadata and all the usages.
   **
   ** @throws BuildException     if the {@link CatalogAMInstance} is already
   **                            set.
   */
  public void catalogAM(final CatalogAMInstance catalogAM)
    throws BuildException {

    if (this.catalogAM != null) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "catalogAM"));
    }
    this.catalogAM = catalogAM;
  }

  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate()
      throws BuildException {

    if (catalogAM == null) {
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_MANDATORY));
    }

    catalogAM.validate();
  }

  /**
   ** Marshals the <code>Sandbox</code>s to the files evaluated from the
   ** properties of the associated {@link CatalogAMInstance}.
   */
  public void execute() {
    try {
      final Catalog catalog = new Catalog("oracle.iam.ui.catalog.model.am.CatalogAM", catalogAM.version().persistence, catalogAM.minor(), false);

      CatalogFactory.configure(catalog, catalogAM);
      marshalSandbox();
      marshalCatalog(catalog);
      compress();
    } catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureError.SANDBOX_MARSHALL_FAILED, Catalog.CUST, catalogAM.path()));
      if (failonerror()) {
        throw new BuildException(e);
      } else {
        fatal(e);
      }
    }
  }

  /**
   ** Marshals the metadata descriptor of a <code>Sandbox</code> to the file
   ** evaluated from the properties of this <code>Sandbox</code> instance.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalSandbox()
      throws IOException, XMLException {

    final Metadata.Path path = Metadata.path(catalogAM.path(), Sandbox.PATH);
    path.ensureExists();
    Sandbox.marshal(this, path, this.catalogAM);
  }

  /**
   ** Marshals the view object usages of an application module customization in
   ** a <code>Sandbox</code> to the file evaluated from the properties of the
   ** given {@link Catalog}.
   **
   ** @param  catalog            the {@link Catalog} the page view
   **                            descriptor has to be generated.
   **
   ** @throws  IOException       if the operations at the file system fails.
   ** @throws  XMLException      if the XML operations failed.
   */
  private void marshalCatalog(final Catalog catalog)
      throws IOException, XMLException {

    Metadata.Cust cust = Metadata.cust(catalogAM.path(), "persdef/oracle/iam/ui/catalog/model/am");
    cust.ensureExists();
    Catalog.marshalModule(this, cust.path(), catalogAM.version().customization, catalog);

    cust = Metadata.cust(catalogAM.path(), "oracle/iam/ui/common/model");
    cust.ensureExists();
    Catalog.marshalDefinitions(this, cust.path(), catalogAM.version().customization, catalog);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compress

  /**
   * * Compress a <code>CatalogAM</code> to the zip file evaluated from the
   * * properties of the given {@link CatalogAMInstance}.
   **/
  public void compress() {
    final Path source = Paths.get(catalogAM.path().toURI());
    final Path target = Paths.get(source + ".zip");
    try {
      SandboxVisitor.compressPath(source, target);

      if (catalogAM.cleanup()) {
        deletePath(source.toFile());
      }
    } catch (IOException e) {
      if (failonerror()) {
        throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_COMPRESS, catalogAM.name(), e.getMessage()));
      } else {
        error(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_COMPRESS, catalogAM.name(), e.getMessage()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deletePath
  /**
   ** Deletes the entire structure of a sandbox provioulsy marshalled.
   **
   ** @param  path               the abstract pathname to delete.
   **
   ** @return                    <code>true</code> if further processing can
   **                            happen.
   **
   ** @throws BuildException     if the path could not be deleted entirely.
   */
  private boolean deletePath(final File path) {
    // delete the entire directory of the previously marshalled expolded
    // sandbox
    try {
      delete(path);
    }
    // if an exception is trown by the delete operation abort any further
    // action
    catch (IOException e) {
      if (failonerror())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_DELETE, e.getMessage()));
      else
        error(FeatureResourceBundle.format(FeatureError.SANDBOX_DIRECTORY_DELETE, e.getMessage()));
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes the entire structur of a sandbox provioulsy marshalled.
   ** <br>
   ** This step is required to have only those file in the archive produced
   ** later that are realy wanted.
   ** <p>
   ** Deletes the file or directory denoted by <code>path</code>. This pathname
   ** should denote to the directory of the exploded sandbox.
   ** <p>
   ** Note that the {@link java.nio.file.Files} class defines the
   ** {@link java.nio.file.Files#delete(Path) delete} method to throw an
   ** {@link IOException} when a file cannot be deleted. This is useful for
   ** error reporting and to diagnose why a file cannot be deleted.
   **
   ** @param  path               the abstract pathname to an exploded sandbox
   **                            directory.
   **
   ** @return                    <code>true</code> if and only if the file or
   **                            directory is successfully deleted;
   **                            <code>false</code> otherwise.
   **
   ** @throws  IOException       if the operations at the file system fails.
   */
  private void delete(final File path)
      throws IOException {

    if (path.isDirectory()) {
      for (File cursor : path.listFiles())
        delete(cursor);
    }
    if (!path.delete())
      throw new IOException(path.getAbsolutePath());
  }
}
