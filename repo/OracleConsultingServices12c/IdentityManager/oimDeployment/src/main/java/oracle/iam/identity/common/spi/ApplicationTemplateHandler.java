package oracle.iam.identity.common.spi;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.task.ServiceProvider;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;
import org.apache.tools.ant.BuildException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <code>ApplicationTemplateHandler</code> generates all the
 * resources for a new application.
 */
public class ApplicationTemplateHandler extends ServiceProvider {

  /**
   * Placeholder or prefix that will be replaced by the given name.
   */
  private static final String TMP_PREFIX = "00_TMP_00";

  /**
   * A {@link Path} where the template files are placed.
   */
  private Path source;
  /**
   * A {@link Path} where the resource files will be generated.
   */
  private Path target;

  /**
   * The value which will replace all the <code>TMP_PREFIX</code> values.
   */
  private String name;

  /**
   * A {@link Map} that defines the directory structure of the resources.
   */
  private final Map<String, Object> structure = new HashMap<>();

  public ApplicationTemplateHandler(final ServiceFrontend frontend) {
    super(frontend);
    structure.put("ant", CollectionUtility.set(TMP_PREFIX + "-oim-export-ant.xml", TMP_PREFIX + "-oim-import-ant.xml", TMP_PREFIX + "-oim-request-ant.xml", TMP_PREFIX + "-oud-config-ant.xml", "app-service-" + TMP_PREFIX + ".xml","app-service-" + TMP_PREFIX + "-groups.xml", "app-service-" + TMP_PREFIX + "-people.xml"));
    structure.put("mds", CollectionUtility.set(TMP_PREFIX + "-account-provisioning-dm.xml", TMP_PREFIX + "-account-reconciliation-dm.xml", TMP_PREFIX + "-feature-dm.xml"));
    structure.put("xml", CollectionUtility.map("target", CollectionUtility.set(TMP_PREFIX + "-lookup-dm.xml", TMP_PREFIX + "-model-dm.xml", TMP_PREFIX + "-process-dm.xml", TMP_PREFIX + "-request-dm.xml", TMP_PREFIX + "-resource-dm.xml", TMP_PREFIX + "-scheduler-dm.xml")));
  }

  /**
   * Sets the value of the field <code>name</code>.
   *
   * @param name {@link String} value
   */
  public void name(final String name) {
    this.name = name.toUpperCase();
  }

  /**
   * Sets the value of the field <code>source</code>.
   *
   * @param source a directory
   */
  public void source(final File source) {
    if (this.source != null) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.APPLICATION_TYPE_ONLY_ONCE, "application:source"));
    }
    this.source = source.toPath();
  }

  /**
   * Sets the value of the field <code>target</code>.
   *
   * @param target a directory
   */
  public void target(final File target) {
    if (this.target != null) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.APPLICATION_TYPE_ONLY_ONCE, "application:target"));
    }
    this.target = target.toPath();
  }

  /**
   * The entry point to validate the task to perform.
   *
   * @throws BuildException in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.source == null) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.APPLICATION_TYPE_MISSING, "application:source"));
    }

    if (this.target == null) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.APPLICATION_TYPE_MISSING, "application:target"));
    }

    validatePath(this.source, true);
    for (Map.Entry<String, Object> cursor : structure.entrySet()) {
      validate(this.source, cursor);
    }
  }

  /**
   * Recursively checks whether all the template files exist in the source directory.
   *
   * @param source a subdirectory of a source root
   * @param entry  a {@link Map.Entry} that holds the required structure on the <code>source</code> level
   */
  @SuppressWarnings("unchecked")
  private void validate(Path source, Map.Entry<String, Object> entry) {
    final String key = entry.getKey();
    Path dir = source.resolve(key);
    validatePath(dir, true);

    final Object value = entry.getValue();
    if (value instanceof Collection<?>) {
      for (Object cursor : ((Collection<?>) value)) {
        Path file = dir.resolve((String) cursor);
        validatePath(file, false);
      }
    }
    if (value instanceof Map<?, ?>) {
      for (Map.Entry<?, ?> cursor : ((Map<?, ?>) value).entrySet()) {
        validate(dir, (Map.Entry<String, Object>) cursor);
      }
    }
  }

  /**
   * Checks whether the given path exists or not.
   * Also checks whether the given path is a directory if the parameter <code>dir</code> is ture.
   *
   * @param path a {@link Path} to check
   * @param dir  true if the path has to be a directory
   */
  private void validatePath(Path path, boolean dir) {
    if (!Files.exists(path)) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.APPLICATION_PATH_NOT_EXIST, path));
    }
    if (dir && !Files.isDirectory(path)) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.APPLICATION_MUST_BE_DIR, path));
    }
  }

  /**
   * The call back method just invoked before reconciliation takes place.
   */
  public void beforeExecution() {
    this.target = this.target.resolve(this.name.toLowerCase());
  }

  /**
   * The entry point of the operation.
   */
  public void execute() {
    try {
      createDir(this.target);
      for (Map.Entry<String, Object> cursor : structure.entrySet()) {
        walk(cursor, this.source, this.target);
      }
    } catch (IOException e) {
      throw new BuildException(FeatureResourceBundle.format(FeatureError.APPLICATION_COPY_FAILED, e.getMessage()));
    }
  }

  /**
   * Recursively walks through the directories defined by the
   * <code>structure</code> and creates all the resource files.
   *
   * @param entry  a {@link Map.Entry} that holds the required
   *               structure on the current level
   * @param source source {@link Path} on the current level
   * @param target target {@link Path} on the current level
   * @throws IOException if a file operation fails
   */
  @SuppressWarnings("unchecked")
  private void walk(Map.Entry<String, Object> entry, Path source, Path target) throws IOException {
    final String key = entry.getKey();
    Path dir = target.resolve(key);
    Path currentSource = source.resolve(key);

    createDir(dir);
    final Object value = entry.getValue();
    if (value instanceof Collection<?>) {
      for (Object cursor : (Collection<?>) value) {
        String tmpFileName = (String) cursor;
        String instanceFileName = tmpFileName.replace(TMP_PREFIX, this.name).toLowerCase();
        copy(currentSource.resolve(tmpFileName), dir.resolve(instanceFileName));
      }
    }
    if (value instanceof Map<?, ?>) {
      for (Map.Entry<?, ?> cursor : ((Map<?, ?>) value).entrySet()) {
        walk((Map.Entry<String, Object>) cursor, currentSource, dir);
      }
    }
  }

  /**
   * Copies the string content from the <code>source</code> file to the <code>target</code>.
   * In the meantime replaces all the <code>TMP_PREFIX</code> values case sensitively.
   *
   * @param source {@link Path} of the source file
   * @param target {@link Path} of the target file
   * @throws IOException if a file operation fails
   */
  private void copy(Path source, Path target) throws IOException {
    String content = new String(Files.readAllBytes(source), StandardCharsets.UTF_8);
    content = content.replaceAll(TMP_PREFIX, this.name);
    content = content.replaceAll(TMP_PREFIX.toLowerCase(), this.name.toLowerCase());
    Files.write(target, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
  }

  /**
   * Creates a directory if it does not exist.
   *
   * @param path where the directory should exist
   * @throws IOException if a directory creation fails
   */
  private void createDir(Path path) throws IOException {
    if (!Files.exists(path) || !Files.isDirectory(path)) {
      Files.createDirectory(path);
    }
  }
}
