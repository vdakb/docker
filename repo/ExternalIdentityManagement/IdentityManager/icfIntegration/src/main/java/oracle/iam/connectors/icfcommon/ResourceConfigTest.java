package oracle.iam.connectors.icfcommon;

import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;
import org.junit.Assert;

import oracle.iam.system.mock.Configuration;

import org.identityconnectors.common.CollectionUtil;

public class ResourceConfigTest {

  public ResourceConfigTest() {
    super();
  }
  @Test
  public void testIsIgnoreEventDisabled() {
    Configuration.clear();
    Configuration.addMockLookup(new Lookup("configLookup1", new HashMap<String, String>()));
    Configuration mockConfigService = new Configuration();
    // test default
    ResourceConfig rc1 = new ResourceConfig(new ITResource(CollectionUtil.newMap("Configuration Lookup", "configLookup1"), " IT Resource Name", "IT Resource Key"), mockConfigService);
    Assert.assertFalse(rc1.isIgnoreEventDisabled());
    // test true
    Configuration.addMockLookup(new Lookup("configLookup2", CollectionUtil.newMap("Ignore Event Disabled", "true")));
    ResourceConfig rc2 = new ResourceConfig(new ITResource(CollectionUtil.newMap("Configuration Lookup", "configLookup2"), " IT Resource Name", "IT Resource Key"), mockConfigService);
    Assert.assertTrue(rc2.isIgnoreEventDisabled());
    // test false
    Configuration.addMockLookup(new Lookup("configLookup3", CollectionUtil.newMap("Ignore Event Disabled", "false")));
    ResourceConfig rc3 = new ResourceConfig(new ITResource(CollectionUtil.newMap("Configuration Lookup", "configLookup3"), " IT Resource Name", "IT Resource Key"), mockConfigService);
    Assert.assertFalse(rc3.isIgnoreEventDisabled());
  }

  @Test
  public void testGetReconBatchSize() {
    Configuration.clear();
    Configuration.addMockLookup(new Lookup("configLookup1", new HashMap<String, String>()));
    Configuration mockConfigService = new Configuration();
    // test default
    ResourceConfig rc1 = new ResourceConfig(new ITResource(CollectionUtil.newMap("Configuration Lookup", "configLookup1"), " IT Resource Name", "IT Resource Key"), mockConfigService);
    Assert.assertEquals(rc1.getReconBatchSize(), 500);
    // test 222
    Configuration.addMockLookup(new Lookup("configLookup2", CollectionUtil.newMap("Recon Batch Size", "222")));
    ResourceConfig rc2 = new ResourceConfig(new ITResource(CollectionUtil.newMap("Configuration Lookup", "configLookup2"), " IT Resource Name", "IT Resource Key"), mockConfigService);
    Assert.assertEquals(rc2.getReconBatchSize(), 222);
    // test throws
    Configuration.addMockLookup(new Lookup("configLookup3", CollectionUtil.newMap("Recon Batch Size", "0")));
    ResourceConfig rc3 = new ResourceConfig(new ITResource(CollectionUtil.newMap("Configuration Lookup", "configLookup3"), " IT Resource Name", "IT Resource Key"), mockConfigService);
    try {
      rc3.getReconBatchSize();
      Assert.fail();
    }
    catch (IllegalArgumentException ex) {
      // ok
    }
  }
}
