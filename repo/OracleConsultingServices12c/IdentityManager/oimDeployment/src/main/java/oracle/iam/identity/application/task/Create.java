package oracle.iam.identity.application.task;

import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.task.AbstractTask;
import oracle.iam.identity.application.type.Source;
import oracle.iam.identity.application.type.Target;
import oracle.iam.identity.common.spi.ApplicationTemplateHandler;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import java.io.File;

/**
 ** Ensures all the resource files that an application needs based on predefined templates.
 */
public class Create extends AbstractTask {

  /**
   * the name of the application to create
   */
  private String name;

  /**
   *  the service provider executing the task operation
   */
  private final ApplicationTemplateHandler delegate;

  public Create() {
    delegate = new ApplicationTemplateHandler(this);
  }

  public String name() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate() throws BuildException {
    if (this.name == null) {
      handleAttributeMissing("name");
    }
    this.delegate.name(name);
    this.delegate.validate();
  }

  /**
   ** Returns the state of connection.
   **
   ** @return constant true.
   */
  @Override
  protected boolean connected() {
    return true;
  }

  /**
   ** Empty implementation required by {@link AbstractTask}.
   */
  @Override
  protected void connect() {
    // intentionally left blank
  }

  /**
   ** Empty implementation required by {@link AbstractTask}.
   */
  @Override
  protected void disconnect() {
    // intentionally left blank
  }

  /**
   ** The call back method just invoked before reconciliation takes place.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  protected void beforeExecution() throws ServiceException {
    this.delegate.beforeExecution();
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
    this.delegate.execute();
  }

  /**
   ** Call by the ANT deployment to inject the nested "source" element.
   **
   ** @param  source        the names of the directory where the templates are placed
   */
  public void addConfiguredSource(final Source source) {
    final DirectoryScanner scanner = source.getDirectoryScanner(getProject());
    final File dir = scanner.getBasedir();
    this.delegate.source(dir);
  }


  /**
   ** Call by the ANT deployment to inject the nested "target" element.
   **
   ** @param  target        the names of the directory where the resources will be generated
   */
  public void addConfiguredTarget(final Target target) {
    final DirectoryScanner scanner = target.getDirectoryScanner(getProject());
    final File dir = scanner.getBasedir();
    this.delegate.target(dir);
  }
}
