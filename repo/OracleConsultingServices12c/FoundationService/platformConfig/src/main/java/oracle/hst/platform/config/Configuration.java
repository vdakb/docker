package oracle.hst.platform.config;

import javax.json.JsonObject;

public interface Configuration {
  JsonObject serialize();
  void deserialize(final JsonObject provider);
}
