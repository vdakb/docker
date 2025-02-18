package oracle.iam.identity.ots.service.account;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.PrintStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.util.Map;

import java.util.Set;

import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.LookupAttribute;
import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseException;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.resource.DatabaseBundle;
import oracle.iam.identity.foundation.resource.TaskBundle;
import oracle.iam.identity.ots.resource.HarvesterBundle;
import oracle.iam.identity.ots.service.XMLExport;
import oracle.iam.identity.ots.service.catalog.HarvesterMessage;
import oracle.iam.identity.utility.file.XMLEntityFactory;

public class ExportAccount extends XMLExport {


    protected static final String MAPPING_NOT_DEFINE = "NOT_DEFINED" ;

    protected static final String SQLstmtFRM = "select * from (select app.app_instance_name app_name,  nvl2(prf.sdk_key, (select s.sdk_name from sdk s where prf.sdk_key = s.sdk_key) ,sdk.sdk_name )  table_name, nvl(prf.prf_columnname,'CHILD') column_name, orf.orf_fieldname map_name, decode (orf.orf_fieldname, 'Account Login','id', 'First Name','firstname', 'Last Name','lastname', 'e-Mail','email', 'Group','group', 'Analog User','analoguser', 'Transaction','transaction', 'Role','role', 'Name','id', '"+MAPPING_NOT_DEFINE+"') map_source, prfc.prf_columnname ccolumn_name, orfc.orf_fieldname cmap_name, decode (orfc.orf_fieldname, 'Group','group', 'Name','id', orfc.orf_fieldname) cmap_source, prf.sdk_key, nvl(prf.prf_iskey,0) prf_iskey from prf, orf, tos, sdk , orf orfc, prf prfc, app_instance app where orf.orf_key = prf.orf_key and tos.tos_key = prf.tos_key and tos.sdk_key = sdk.sdk_key and orf.orf_key = orfc.orf_parent_orf_key (+) and orfc.orf_key = prfc.orf_key (+) and orf.orf_fieldtype <> 'IT Resource' and app.app_instance_is_soft_delete = 0 and app.app_instance_Type like ? and app.object_key = orf.obj_key and app.app_instance_name like ?) where (sdk_key is not null and cmap_source is not null) or (sdk_key is null and cmap_source is null) order by 1,prf_iskey DESC,3 DESC";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<String, String> SQLstmts = new HashMap<String, String>();


  public ExportAccount() {
    // ensure inheritance
    super();
  }

  @Override
    protected void onExecution() throws TaskException {
      final String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
      info(HarvesterBundle.format(HarvesterMessage.EXPORTING_BEGIN, parameter));

      Connection           connection = null;
      PreparedStatement    getLKPs    = null;
      PreparedStatement    getFile    = null;
      int rsCount = 0;
      String lastAppInstanceName      = "noneWhatSoEver";
      String accountIdentifier        = "";
      String tblNameP                 = "";
      List<String> accAttributes      = new ArrayList<String>();
      List<String> accEntitlements    = new ArrayList<String>();
      Set<String> decodeAttributes    = new HashSet<String>();
      Set<String> decodeCAttributes   = new HashSet<String>();


      if(mappingDefLoaded){
        final AbstractLookup   entitlement        = this.descriptor.entitlement();

        AttributeMapping attrMapping = this.descriptor.attributeMapping();
        for (String name : attrMapping.keySet()){
          decodeAttributes.add("'"+(String)attrMapping.get(name)+"', '" + name+"'");
        }

        for (String entl : entitlement.keySet()) {
          final TaskDescriptor.Entitlement descriptor = (TaskDescriptor.Entitlement)entitlement.get(entl);
          decodeCAttributes.add("'"+String.valueOf(entl)+"', '" + String.valueOf(descriptor.systemID())+"'");

          AttributeMapping entlMapping = descriptor.attributeMapping();
          for (String name : entlMapping.keySet()){
            decodeCAttributes.add("'"+(String)entlMapping.get(name)+"', '" + String.valueOf(name)+"'");
          }

        }
      }


      try {
        connection = DatabaseConnection.aquire();

        final String sqlFRM = constructSQL(decodeAttributes,decodeCAttributes);

        //System.out.println("SQLstmtFRM:" + sqlFRM  + "\n\r\n\r");

        getLKPs = DatabaseStatement.createPreparedStatement(connection, sqlFRM);
        getLKPs.setString(1, getAppInstanceTypeFilter());
        getLKPs.setString(2, appInstanceNameFilter());

        HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);
        final ResultSet lk = getLKPs.executeQuery();

        while (lk.next()) {

          final String appName      = lk.getString("app_name");
          final String tblName      = lk.getString("table_name");
          final String colName      = lk.getString("column_name");
          final String isKey        = lk.getString("prf_iskey");
          final String mapSource    = lk.getString("map_source");
          final String entColName   = lk.getString("ccolumn_name");

          // Check if are getting data to new App.Instance
          if (!lastAppInstanceName.equals(appName)){

            // Add the query from previous Application for execution
            if (!lastAppInstanceName.equals("noneWhatSoEver"))
              addXmlSelectStmt(lastAppInstanceName, accountIdentifier,accAttributes,accEntitlements, tblNameP);

            lastAppInstanceName = appName;
            tblNameP            = tblName;
            rsCount =0;

            // Clean Sets
            accAttributes.clear();
            accEntitlements.clear();
          }

          // Construct entitlements XML query
          ++rsCount;
          if (colName.equalsIgnoreCase("CHILD")){
            if(booleanValue(PROCESS_ENTITELEMENT,true))
              accEntitlements.add(newEntitlement(mapSource,entColName,tblName, tblNameP));
          } else { // Construct Account XML query
            accountIdentifier = (isKey.equals("1")?colName:accountIdentifier);

            if(!mapSource.equalsIgnoreCase(MAPPING_NOT_DEFINE))
              accAttributes.add(newAttribute(mapSource, colName));

          }
        }
        // Add the query from last Application for execution
        if (rsCount>0)
          addXmlSelectStmt(lastAppInstanceName, accountIdentifier,accAttributes,accEntitlements, tblNameP);

        HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);


        for(String key : SQLstmts.keySet()) {

          //System.out.println("SQLstmt:" +key.toString()+ " -> " + SQLstmts.get(key)  + "\n\r\n\r");

          List<String> finalXML           = new ArrayList<String>();

          info("Processing " + key.toString());
          getFile = DatabaseStatement.createPreparedStatement(connection, SQLstmts.get(key));

          finalXML.add(String.format(XMLEntityFactory.PROLOG, stringValue(FILE_ENCODING)));

          final ResultSet rs = getFile.executeQuery();
          while (rs.next()) {
              finalXML.add(rs.getString(1));
          }

          String filePath;
          if ((stringValue(DATAFILE).replaceAll("\\s","")).equalsIgnoreCase("applicationinstance"))
            filePath = stringValue(DATA_FOLDER)+"/"+key.toString()+".xml";
          else
            filePath = stringValue(DATA_FOLDER)+"/"+key.toString()+"_"+stringValue(DATAFILE);



          if (!gatherOnly()){
            useBufferedFileOutPutStream(finalXML, filePath, stringValue(FILE_ENCODING));
            updateLastReconciled();
            incrementSuccess();
          }
        }

        if (isStopped()) {
          warning(HarvesterBundle.format(HarvesterMessage.EXPORTING_STOPPED, parameter));
        }
        else {
          info(HarvesterBundle.format(HarvesterMessage.EXPORTING_COMPLETE, parameter));
        }
      }
      catch (SQLSyntaxErrorException e) {
        error("onExecution", DatabaseBundle.format(DatabaseError.SYNTAX_INVALID, e.getLocalizedMessage()));
        incrementFailed();
        stopExecution();
        throw new DatabaseException(e);
      }
      catch (SQLException e) {
        error("onExecution", DatabaseBundle.format(DatabaseError.SYNTAX_INVALID, e.getLocalizedMessage()));
        incrementFailed();
        stopExecution();
        throw new DatabaseException(e);
      }
      catch (TaskException e) {
        final String[] arguments = { reconcileObject(), getName() , dataFile().getAbsolutePath() , e.getLocalizedMessage()};
        // notify user about the problem
        warning(HarvesterBundle.format(HarvesterMessage.EXPORTING_ERROR, arguments));
        throw e;
      }
      finally {
        try {
          if (getFile != null) getFile.close();
          DatabaseConnection.release(connection);
        }
        catch (SQLException e) {
            throw new DatabaseException(e);
        }
      }
    }

  private String constructSQL(Set<String>decodeAttributes, Set<String> decodeChildAttributes){
      //decodeAttributes
      String xmlSelect = "SELECT * "
                       +   "FROM ("
                       +   "  SELECT app.app_instance_name app_name"
                       +   "      ,nvl2(prf.sdk_key, ("
                       +   "              SELECT s.sdk_name"
                       +   "              FROM sdk s"
                       +   "              WHERE prf.sdk_key = s.sdk_key"
                       +   "              ), sdk.sdk_name) table_name"
                       +   "      ,nvl(prf.prf_columnname, 'CHILD') column_name"
                       +   "      ,orf.orf_fieldname map_name"
                       +   "      ,decode(orf.orf_fieldname ";
      if (mappingDefLoaded)
        for (String attr : decodeAttributes) {
          xmlSelect += ", " + attr;
        }
      else{
             xmlSelect +=  "        , 'Account Login', 'id'"
                       +   "        , 'First Name', 'firstname'"
                       +   "        , 'Last Name', 'lastname'"
                       +   "        , 'e-Mail', 'email'"
                       +   "        , 'Group', 'group'"
                       +   "        , 'Analog User', 'analoguser'"
                       +   "        , 'Transaction', 'transaction'"
                       +   "        , 'Role', 'role'"
                       +   "        , 'Name', 'id'";
      }

             xmlSelect +=  "      , '"+MAPPING_NOT_DEFINE+"') map_source"
                       +   "      ,prfc.prf_columnname ccolumn_name"
                       +   "      ,orfc.orf_fieldname cmap_name"
                       +   "      ,decode(orfc.orf_fieldname ";


      if (mappingDefLoaded)
        for (String attrc : decodeChildAttributes) {
          xmlSelect += ", " + attrc;
        }
      else{
        xmlSelect +=  "        , 'Group', 'group'"
                  +   "        , 'Name', 'id'";
      }
             xmlSelect +=  "      , orfc.orf_fieldname) cmap_source"
                       //+   "      ,decode(orfc.orf_fieldname, 'Group', 'group', 'Name', 'id', orfc.orf_fieldname) cmap_source"
                       +   "      ,prf.sdk_key"
                       +   "      ,nvl(prf.prf_iskey, 0) prf_iskey"
                       +   "  FROM prf"
                       +   "      ,orf"
                       +   "      ,tos"
                       +   "      ,sdk"
                       +   "      ,orf orfc"
                       +   "      ,prf prfc"
                       +   "      ,app_instance app"
                       +   "  WHERE orf.orf_key = prf.orf_key"
                       +   "      AND tos.tos_key = prf.tos_key"
                       +   "      AND tos.sdk_key = sdk.sdk_key"
                       +   "      AND orf.orf_key = orfc.orf_parent_orf_key(+)"
                       +   "      AND orfc.orf_key = prfc.orf_key(+)"
                       +   "      AND orf.orf_fieldtype <> 'IT Resource'"
                       +   "      AND app.app_instance_is_soft_delete = 0"
                       +   "      AND app.app_instance_Type LIKE ?"
                       +   "      AND app.object_key = orf.obj_key"
                       +   "      AND app.app_instance_name LIKE ?"
                       +   "  )"
                       +   " WHERE (sdk_key IS NOT NULL AND cmap_source IS NOT NULL)"
                       +   " OR (sdk_key IS NULL  AND cmap_source IS NULL)"
                       +   " ORDER BY 1 ,prf_iskey DESC,3 DESC";

      return xmlSelect;
    }


  private String newAttribute(String mapSource, String colName){
    return (" , XMLELEMENT(\"attribute\", XMLATTRIBUTES('"+mapSource+"' AS \"id\"), UDP."+colName+")");
    }

  private String newEntitlement(String mapSource,String entColName,String tblName, String tblNameP){
    return(", XMLELEMENT(\"entitlements\""
       +   ", XMLATTRIBUTES('"+mapSource+"' AS \"id\") ,"
       +   "(SELECT XMLAGG( "
       +   "    XMLELEMENT(\"entitlement\",XMLATTRIBUTES(substr(UDC."+entColName+",instr(UDC."+entColName+",'~',-1)+1) AS \"id\")) )"
       +   "    FROM "+tblName+"  UDC"
       +   "    WHERE UDP."+tblNameP+"_KEY = UDC."+tblNameP+"_KEY (+)"
       +   "       )"
       +   ")");
    }

  private void addXmlSelectStmt(String appInstanceName, String accountIdentifier,  List<String> accAttributes, List<String> accEntitlements, String tblNameP){
      String xmlSelect = "SELECT XMLELEMENT(\"application\" "
                         +  "          ,xmlattributes( '"+appInstanceName+"' as \"id\" ,'http://www.oracle.com/schema/oim/offline' as \"xmlns\" ,  'http://www.w3.org/2001/XMLSchema-instance' as \"xmlns:xsi\" , 'http://www.oracle.com/schema/oim/offline ApplicationAccount.xsd' as \"xsi:schemaLocation\" )"
                         +  "          ,(select xmlagg( "
                         +  "            XMLELEMENT(\"account\", "
                         +  "        XMLATTRIBUTES(UDP."+accountIdentifier+" AS \"id\") ";

      for (String attr : accAttributes) {
        xmlSelect += attr;
      }
      for (String ent : accEntitlements) {
        xmlSelect += ent;
      }

      final boolean onlyValidAccounts = booleanValue(VALID_ACCOUNTS, true);

        xmlSelect        += ") "
                         +  "        ) AS INNERDATA"
                         +  "        FROM "+tblNameP+" UDP"
                         +  "           , OIU"
                         +  "           , OST "
                         +  "         WHERE UDP.ORC_KEY = OIU.ORC_KEY"
                         +  "         AND OIU.OST_KEY  = OST.OST_KEY"
                         +  "         AND OIU.ACCOUNT_TYPE  = 'primary'";

        if (onlyValidAccounts)
          xmlSelect      +=  "        AND (OST.OST_STATUS = 'Enabled' OR OST.OST_STATUS= 'Provisioned')";
        else
          xmlSelect      += "         AND OST.OST_STATUS like '%'";

        xmlSelect        += "        )"
                         +  "        ).getclobval() as xmldata"
                         +  "        from dual";
    SQLstmts.put(appInstanceName, xmlSelect);
  }

}