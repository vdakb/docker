package oracle.iam.identity.sap.control;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

import oracle.hst.foundation.SystemConsole;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.reconciliation.PairHandler;

import oracle.iam.identity.foundation.schema.Pair;
import oracle.iam.identity.sap.persistence.CatalogQuery;

import oracle.iam.identity.sap.persistence.schema.Catalog;

public class Test {

  private final HashMap<String, String> resource         = new HashMap<String, String>();

  private static class Adapter implements PairHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Loggable loggable;
    private final String   encoded;
    private final String   decoded;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a {qlink ResultsHandler} which exracts the values from the
     ** particular entry.
     */
    private Adapter(final Loggable loggable, final String encoded, final String decoded) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.loggable = loggable;
      this.encoded  = encoded;
      this.decoded  = decoded;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** entry that is returned in the result.
     **
     ** @param  subject          each entry return from the search.
     **
     ** @return
     */
    public boolean handle(final Pair<String, String> subject) {

      this.loggable.debug("handle", subject.toString());
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: encoded
    protected String encoded(final Map<String, Object> subject) {
      // obtain the attribute value for the encoded attribute if it exists else
      // null
      return (String)subject.get(this.encoded);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decoded
    protected String decoded(final Map<String, Object> subject) {
      // obtain the attribute value for the decoded attribute if it exists else
      // null
      return (String)subject.get(this.decoded);
    }
  }

  public Test() {
    // ensure inheritance
    super();

    // simulate the createion of an IT Resource by creating the appropriate mapping
/*
    resource.put(Resource.APPLICATION_SERVER_HOST,     "sap-f1s.fs01.vwf.vwfs-ad");
    resource.put(Resource.APPLICATION_SERVER_GROUP,    "public");
    resource.put(Resource.APPLICATION_SERVER_FEATURE,  "not used");
    resource.put(Resource.MESSAGE_SERVER_HOST,         "sap-f1s.fs01.vwf.vwfs-ad");
    resource.put(Resource.CLIENT_LOGON,                "101");
    resource.put(Resource.PRINCIPAL_NAME,              "AIM_USER");
    resource.put(Resource.PRINCIPAL_PASSWORD,          "Zgd.umw.oim.2013");
    resource.put(Resource.MASTER_SYSTEM_NAME,          "I don't know");
    resource.put(Resource.SYSTEM_NAME,                 "F1S");
    resource.put(Resource.SYSTEM_NUMBER,               "05");
    resource.put(Resource.SYSTEM_LANGUAGE,             "EN");
    resource.put(Resource.SYSTEM_TIMEZONE,             "GMT+01:00");
    resource.put(Resource.SECURE_SOCKET,               "false");
    resource.put(Resource.APPLICATION_SERVER_UNICODE,  "false");
    resource.put(Resource.CENTRAL_USER_ADMINISTRATION, "false");
*/
    resource.put(Resource.APPLICATION_SERVER_HOST,     "sap-f3d.fs01.vwf.vwfs-ad");
    resource.put(Resource.APPLICATION_SERVER_GROUP,    "public");
    resource.put(Resource.APPLICATION_SERVER_FEATURE,  "not used");
    resource.put(Resource.MESSAGE_SERVER_HOST,         "sap-f3d.fs01.vwf.vwfs-ad");
    resource.put(Resource.CLIENT_LOGON,                "103");
    resource.put(Resource.PRINCIPAL_NAME,              "AIM_USER");
    resource.put(Resource.PRINCIPAL_PASSWORD,          "Zgd.umw.oim.2013");
    resource.put(Resource.MASTER_SYSTEM_NAME,          "F3DCLNT103");
    resource.put(Resource.SYSTEM_NAME,                 "F3D");
    resource.put(Resource.SYSTEM_NUMBER,               "05");
    resource.put(Resource.SYSTEM_LANGUAGE,             "EN");
    resource.put(Resource.SYSTEM_TIMEZONE,             "GMT+01:00");
    resource.put(Resource.SECURE_SOCKET,               "false");
    resource.put(Resource.APPLICATION_SERVER_UNICODE,  "false");
    resource.put(Resource.CENTRAL_USER_ADMINISTRATION, "false");
  }

  public static void main(String[] args)
    throws Exception {

    final Test test = new Test();
    final SystemConsole console  = new SystemConsole("Test");
    final Resource      resource = new Resource(console, test.resource);
    final Feature       feature  = Connection.unmarshal(console, new File("C:\\Portal\\Oracle Consulting Services 11g\\Oracle Identity Manager\\sapUsermanagement\\metadata\\sap-feature.xml"));

    feature.attribute(Catalog.ROLE_SINGLE,    Boolean.FALSE.toString());
    feature.attribute(Catalog.ROLE_COMPOSITE, Boolean.FALSE.toString());

    final Connection       connection = new Connection(console, resource, feature);

    Adapter adapter = new Adapter(console, "SUBSYSTEM", "USRSYSACT");
//    CatalogQuery.execute(console, connection, feature, Catalog.ROLE, adapter);

    adapter = new Adapter(console, "SUBSYSTEM", "USRSYSPRF");
//    CatalogQuery.execute(console, connection, feature, Catalog.PROFILE, adapter);

    adapter = new Adapter(console, "TITLE_MEDI", "TITLE_MEDI");
    CatalogQuery.execute(console, connection, feature, Catalog.TITLE, adapter);

    adapter = new Adapter(console, "COMPANY", "COMPANY");
    CatalogQuery.execute(console, connection, feature, Catalog.COMPANY, adapter);

    adapter = new Adapter(console, "SPRAS", "SPTXT");
    CatalogQuery.execute(console, connection, feature, Catalog.LOGONLANGUAGE, adapter);

    adapter = new Adapter(console, "COMM_TYPE", "COMM_TEXT");
    CatalogQuery.execute(console, connection, feature, Catalog.COMMUNICATIONTYPE, adapter);

    adapter = new Adapter(console, "TZONE", "DESCRIPT");
    CatalogQuery.execute(console, connection, feature, Catalog.TIMEZONE, adapter);

    adapter = new Adapter(console, "_LOW", "_TEXT");
    CatalogQuery.execute(console, connection, feature, Catalog.USERTYPE, adapter);
    CatalogQuery.execute(console, connection, feature, Catalog.DATEFORMAT, adapter);
    CatalogQuery.execute(console, connection, feature, Catalog.DECIMALNOTATION, adapter);

    adapter = new Adapter(console, "USERGROUP", "USERGROUP");
    CatalogQuery.execute(console, connection, feature, Catalog.USERGROUP, adapter);

    adapter = new Adapter(console, "PARAMID", "PARTEXT");
    CatalogQuery.execute(console, connection, feature, Catalog.PARAMETER, adapter);
  }
}
