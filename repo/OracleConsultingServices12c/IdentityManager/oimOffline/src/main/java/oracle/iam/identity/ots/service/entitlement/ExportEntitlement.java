package oracle.iam.identity.ots.service.entitlement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import java.util.ArrayList;
import java.util.List;

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



public class ExportEntitlement extends XMLExport {

  private static final String   SQLstmtLK = "select app.app_instance_name app_name ,sdp.sdp_property_value lkname from sdk sdk ,sdl sdl ,tos tos ,pkg pkg ,obj obj ,sdc ,sdp , sdp sdpe ,obd obd ,sdh sdh ,sdk sdkc ,sdl sdlc ,app_instance app where 1 = 1 and app.app_instance_is_soft_delete = 0 and app.app_instance_type like ? and app.object_key = obj.obj_key and sdc.sdc_version = sdkc.sdk_active_version and sdc.sdk_key = sdkc.sdk_key and sdk.sdk_key = sdl.sdk_key and sdp.sdc_key = sdc.sdc_key and sdc.sdc_field_type = 'LookupField' and sdp.sdp_property_name = 'Lookup Code' and sdpe.sdc_key = sdc.sdc_key and sdpe.sdp_property_name = 'Entitlement' and sdpe.sdp_property_value = 'true' and sdk.sdk_active_version = sdl.sdl_current_version and obj.obj_type like ? and tos.sdk_key(+) = sdk.sdk_key and pkg.pkg_key(+) = tos.pkg_key and obj.obj_key(+) = pkg.obj_key and obj.obj_key = obd.obd_child_key(+) and sdk.sdk_key = sdh.sdh_parent_key and sdkc.sdk_key = sdh.sdh_child_key and sdkc.sdk_key = sdlc.sdk_key and sdkc.sdk_active_version = sdlc.sdl_current_version and app.app_instance_name like ? group by app.app_instance_name ,sdp.sdp_property_value order by 1 ,2";
  private static final String   SQLstmt   = "SELECT xmlelement(\"application\",  xmlattributes(? AS \"id\" , 'http://www.oracle.com/schema/oim/offline' AS \"xmlns\" , 'http://www.w3.org/2001/XMLSchema-instance' AS \"xmlns:xsi\" , 'http://www.oracle.com/schema/oim/offline LookupEntity.xsd' AS \"xsi:schemaLocation\" ), ( xmlelement(\"lookup\", xmlattributes(? AS \"id\"), ( SELECT XMLAgg(xmlelement(\"entitlement\", xmlattributes(substr(lkv_encoded, (instr(lkv_encoded, '~', (instr(lkv_encoded, '~') + 1))) + 1) AS \"id\"), xmlelement(\"attribute\", xmlattributes('displayName' AS \"id\"), substr(lkv_decoded, instr(lkv_decoded, '~') + 1)))) FROM lkv WHERE lkv.lku_key = ( SELECT lku_key FROM lku WHERE lku.lku_type_string_key = ? ) AND lkv.lkv_disabled = 0 )) )).getclobval() AS xmldata FROM dual";


  public ExportEntitlement() {
    // ensure inheritance
    super();
  }

  @Override
  protected void onExecution() throws TaskException {

    final String[] parameter = { appInstanceLoggingName(), getName() , dataFile().getAbsolutePath()};
    info(HarvesterBundle.format(HarvesterMessage.EXPORTING_BEGIN, parameter));

    Connection           connection = null;
    PreparedStatement    getLKPs    = null;
    PreparedStatement    getFile    = null;

    try {
      connection = DatabaseConnection.aquire();

      getLKPs = DatabaseStatement.createPreparedStatement(connection, SQLstmtLK);
      getLKPs.setString(1, getAppInstanceTypeFilter());
      getLKPs.setString(2, getAppInstanceTypeObjFilter());
      getLKPs.setString(3, appInstanceNameFilter());


      HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);

      final ResultSet lk = getLKPs.executeQuery();
      while (lk.next()) {
        List<String> finalXML = new ArrayList<String>();

        final String appName = lk.getString("app_name");
        final String lkName  = lk.getString("lkname");

        final String[] prcessInfo = { appName, lkName};
        info(HarvesterBundle.format(HarvesterMessage.PROCESSING_ROWS, prcessInfo));

        getFile = DatabaseStatement.createPreparedStatement(connection, SQLstmt);
        getFile.setString(1, appName);
        getFile.setString(2, lkName);
        getFile.setString(3, lkName);

        finalXML.add(String.format(XMLEntityFactory.PROLOG, stringValue(FILE_ENCODING)));

        final ResultSet rs = getFile.executeQuery();
        while (rs.next()) {
            finalXML.add(rs.getString(1));
        }

        String filePath;
        if ((stringValue(DATAFILE).replaceAll("\\s","")).equalsIgnoreCase("lookupname"))
          filePath = stringValue(DATA_FOLDER)+"/"+lkName+".xml";
        else
          filePath = stringValue(DATA_FOLDER)+"/"+appName+"_"+lkName+"_"+stringValue(DATAFILE);


        if (!gatherOnly()){
          useBufferedFileOutPutStream(finalXML, filePath, stringValue(FILE_ENCODING));
          updateLastReconciled();
          incrementSuccess();
        }
      }
      HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);


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
      info(HarvesterBundle.format(HarvesterMessage.EXPORTING_COMPLETE, parameter));
      //info(HarvesterBundle.format(HarvesterMessage.EXPORTING_SUMMARY, summary.asStringArray()));

      for (String attr : summary.asStringArray()) {
        info("..Summary: " + attr);
      }
      try {
        if (getFile != null) getFile.close();
        DatabaseConnection.release(connection);
      }
      catch (SQLException e) {
          throw new DatabaseException(e);
      }
    }
  }



}
