package oracle.iam.identity.common.spi;

import java.util.Date;

/**
 * The interface describes what attributes have to be provided
 * to construct an active_mdsSandboxMetadata.xml file.
 */
public interface SandboxMetadata {

  String name();

  String comment();

  String description();

  String user();

  Date created();

  Date updated();

  SandboxInstance.Version version();
}
