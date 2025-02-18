package oracle.iam.system.mock;

import java.util.Map;
import java.util.HashMap;

import oracle.iam.connectors.icfcommon.Lookup;
import oracle.iam.connectors.icfcommon.ITResource;
import oracle.iam.connectors.icfcommon.AppInstance;
import oracle.iam.connectors.icfcommon.ResourceConfig;

import oracle.iam.connectors.icfcommon.service.ConfigurationService;

public class Configuration implements ConfigurationService {

  private static ITResource itResource;
  private static Map<String, Lookup> lookups = new HashMap<String, Lookup>();

  public Configuration() {
    super();
  }

  public static void setMockITResource(ITResource iitResource) {
    itResource = iitResource;
  }

  public ITResource getITResource(String itResourceName) {
    return itResource;
  }

  public ITResource getITResource(long itResourceKey) {
    return itResource;
  }

  public ResourceConfig getResourceConfig(String itResourceName) {
    return new ResourceConfig(itResource, this);
  }

  public ResourceConfig getResourceConfig(long itResourceKey) {
    return new ResourceConfig(itResource, this);
  }

  public Lookup getLookup(String lookupName) {
    return lookups.get(lookupName);
  }

  public Lookup getLookup(String paramString, boolean paramBoolean, Map<String, String> paramMap) {
    return null;
  }
  
  public Lookup addLookupCode(String paramString) {
    return null;
  }
  
  public Lookup getLookup(String lookupName, boolean createNew) {
    Lookup lookup = lookups.get(lookupName);
    if (lookup == null && createNew) {
      lookup = new Lookup(lookupName, new HashMap<String, String>());
      addMockLookup(lookup);
    }
    return lookup;
  }

  public void setLookup(Lookup lookup) {
  }

  @Override
  public AppInstance getApplicationInstance(String name) {
    return new AppInstance(name, itResource);
  }

  public static void addMockLookup(Lookup lookup) {
    lookups.put(lookup.getLookupName(), lookup);
  }

  public static void clear() {
    lookups.clear();
  }

  public Lookup getConfiguration(String name) {
    return lookups.get(name);
  }
  
  public boolean isEntitlementLookup(final String paramString) {
    return false;
  }
}
