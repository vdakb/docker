package oracle.iam.system.integration;

import java.util.HashMap;

import oracle.iam.connectors.icfcommon.Lookup;
import oracle.iam.connectors.icfcommon.ITResource;
import oracle.iam.connectors.icfcommon.ResourceConfig;

import oracle.iam.connectors.icfcommon.recon.ReconEvent;

import oracle.iam.connectors.icfcommon.extension.ResourceExclusion;

import org.identityconnectors.common.CollectionUtil;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.mock.Configuration;

public abstract class Exclusion {

  public Exclusion() {
    super();
  }
  
  public static void main(String[] args) {
    Configuration.clear();
    Configuration.addMockLookup(trustedMain());
    Configuration.addMockLookup(trustedUser());
    Configuration.addMockLookup(trustedMapping());
    Configuration.addMockLookup(trustedExclusion());

    final Configuration     cs = new Configuration();
    final ResourceConfig    rc = new ResourceConfig(new ITResource(CollectionUtil.newMap("Configuration Lookup", "RP.LDS.Configuration.Trusted.UM.Main"), "IT Resource Name", "IT Resource Key"), cs);
    final ResourceExclusion exclusion = ResourceExclusion.newInstance("User", rc);
    final Lookup mapping = rc.getObjectTypeLookup("User", "Recon Attribute Map");
    exclusion.isMarkedForExclusion(bk(mapping));
    exclusion.isMarkedForExclusion(th(mapping));
    exclusion.isMarkedForExclusion(rp(mapping));
  }

  private static Lookup trustedMain() {
    final Lookup lookup = new Lookup("RP.LDS.Configuration.Trusted.UM.Main", new HashMap<String, String>());
    lookup.setValue("Bundle Version",                       "12.3.0");
    lookup.setValue("Bundle Name",                          "org.identityconnectors.ldap");
    lookup.setValue("Connector Name",                       "org.identityconnectors.ldap.LdapConnector");

    lookup.setValue("Pool Min Idle",                        "1");
    lookup.setValue("Pool Max Wait",                        "150000");
    lookup.setValue("Pool Max Idle",                        "10");
    lookup.setValue("Pool Max Size",                        "10");
    lookup.setValue("Pool Min Evict Idle Time",             "120000");

    lookup.setValue("Any Incremental Recon Attribute Type", "true");
    lookup.setValue("usePagedResultControl",                "true");
    lookup.setValue("uidAttribute",                         "dn");
    lookup.setValue("objectClassesToSynchronize",           "inetOrgPerson");
    lookup.setValue("User Configuration Lookup",            "RP.LDS.Configuration.Trusted.UM");
    return lookup; 
  }

  private static Lookup trustedUser() {
    final Lookup lookup = new Lookup("RP.LDS.Configuration.Trusted.UM", new HashMap<String, String>());
    lookup.setValue("Recon Exclusion List",        "RP.LDS.Configuration.Trusted.UM.Exclusions");
    lookup.setValue("Recon Attribute Map",         "RP.LDS.Configuration.Trusted.UM.ReconAttrMap");
    lookup.setValue("Recon Attribute Defaults",    "RP.LDS.Configuration.Trusted.UM.Defaults");
    lookup.setValue("Recon Transformation Lookup", "RP.LDS.Configuration.Trusted.UM.ReconTransformations");
    return lookup; 
  }
  private static Lookup trustedMapping() {
    final Lookup lookup = new Lookup("RP.LDS.Configuration.Trusted.UM.ReconAttrMap", new HashMap<String, String>());
    lookup.setValue("User Login",          "userPrincipalName");
    lookup.setValue("userPrincipalName",   "userPrincipalName");
    lookup.setValue("First Name",          "givenName");
    lookup.setValue("Last Name",           "sn");
    lookup.setValue("User Principal Name", "userPrincipalName");
    lookup.setValue("E-mail",              "mail");
    lookup.setValue("Telephone Number",    "telephoneNumber");

    lookup.setValue("division",            "division");
    lookup.setValue("department",          "department");
    lookup.setValue("organizationalUnit",  "distinguishedName");

    lookup.setValue("ldapDN",              "distinguishedName");
    lookup.setValue("Distinguished Name",  "distinguishedName");
    lookup.setValue("identifierGenerated", "cn");

    lookup.setValue("Participant",         "o");
    lookup.setValue("Organization",        "ou");

    return lookup; 
  }
  private static Lookup trustedExclusion() {
    final Lookup lookup = new Lookup("RP.LDS.Configuration.Trusted.UM.Exclusions", new HashMap<String, String>());
    lookup.setValue("Organization[PATTERN]", "^(?!.*(RP)).*$");
    return lookup; 
  }
  private static ReconEvent bk(final Lookup mapping) {
    ConnectorObject connectorObject = new ConnectorObject(
      ObjectClass.ACCOUNT
    , CollectionUtility.set(
        new Uid("1")
      , new Name("BK4711123")
      , AttributeBuilder.build("cn",                  "BKH4711123")
      , AttributeBuilder.build("userPrincipalName",   "BKH4711123@police.bk")
      , AttributeBuilder.build("givenName",           "Alfons")
      , AttributeBuilder.build("sn",                  "Zitterbacke")
      , AttributeBuilder.build("mail",                "azitterbacke@police.bk")
      , AttributeBuilder.build("telephoneNumber",     "+49 (0) 177 5948 437")
      , AttributeBuilder.build("o",                   "BK")
      , AttributeBuilder.build("ou",                  "BK")
      , AttributeBuilder.build("division",            "BK")
      , AttributeBuilder.build("department",          "BK_1")
      , AttributeBuilder.build("organizationalUnit",  "BK_1_1")

      , AttributeBuilder.build("distinguishedName",   "cn=4711123,dc=police,dc=bk,dc=de")
      )
    );
    ITResource itResource = new ITResource(new HashMap<String, String>(), "name", "key");
    ReconEvent event = new ReconEvent(connectorObject, mapping, itResource, "yyyy/MM/dd H:mm:ss z");
    return event;
  }

  private static ReconEvent th(final Lookup mapping) {
    ConnectorObject connectorObject = new ConnectorObject(
      ObjectClass.ACCOUNT
    , CollectionUtility.set(
        new Uid("1")
      , new Name("TH4711123")
      , AttributeBuilder.build("cn",                  "TH4711123")
      , AttributeBuilder.build("userPrincipalName",   "azitterbacke@police.th")
      , AttributeBuilder.build("givenName",           "Alfons")
      , AttributeBuilder.build("sn",                  "Zitterbacke")
      , AttributeBuilder.build("mail",                "azitterbacke@police.th")
      , AttributeBuilder.build("telephoneNumber",     "+49 (0) 177 5948 437")
      , AttributeBuilder.build("o",                   "TH")
      , AttributeBuilder.build("ou",                  "TH")
      , AttributeBuilder.build("division",            "TH")
      , AttributeBuilder.build("department",          "TH_1")
      , AttributeBuilder.build("organizationalUnit",  "TH_1_1")
      , AttributeBuilder.build("distinguishedName",   "cn=4711123,dc=police,dc=th,dc=de")
      )
    );
    ITResource itResource = new ITResource(new HashMap<String, String>(), "name", "key");
    ReconEvent event = new ReconEvent(connectorObject, mapping, itResource, "yyyy/MM/dd H:mm:ss z");
    return event;
  }

  private static ReconEvent rp(final Lookup mapping) {
    ConnectorObject connectorObject = new ConnectorObject(
      ObjectClass.ACCOUNT
    , CollectionUtility.set(
        new Uid("1")
      , new Name("RP4711123")
      , AttributeBuilder.build("User cn",             "RP4711123")
      , AttributeBuilder.build("userPrincipalName",   "azitterbacke@police.rp")
      , AttributeBuilder.build("givenName",           "Alfons")
      , AttributeBuilder.build("sn",                  "Zitterbacke")
      , AttributeBuilder.build("mail",                "azitterbacke@police.rp")
      , AttributeBuilder.build("telephoneNumber",     "+49 (0) 177 5948 437")
      , AttributeBuilder.build("o",                   "RP")
      , AttributeBuilder.build("ou",                  "RP")
      , AttributeBuilder.build("division",            "RP")
      , AttributeBuilder.build("department",          "RP_1")
      , AttributeBuilder.build("organizationalUnit",  "RP_1_1")
      , AttributeBuilder.build("distinguishedName",    "cn=4711123,dc=police,dc=rp,dc=de")
      )
    );
    ITResource itResource = new ITResource(new HashMap<String, String>(), "name", "key");
    ReconEvent event = new ReconEvent(connectorObject, mapping, itResource, "yyyy/MM/dd H:mm:ss z");
    return event;
  }
}