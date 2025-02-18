package oracle.iam.identity.ots.service.identityRole;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import java.util.ArrayList;
import java.util.List;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;
import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseException;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.resource.DatabaseBundle;

import oracle.iam.identity.utility.file.XMLEntityFactory;

import oracle.iam.identity.ots.service.XMLExport;

import oracle.iam.identity.ots.service.catalog.HarvesterMessage;

import oracle.iam.identity.ots.resource.HarvesterBundle;

public class ExportIdentityRole extends XMLExport {

//private static final String   SQLstmt   = "select xmlelement(\"identities\" ,xmlattributes('http://www.oracle.com/schema/oim/offline' as \"xmlns\", 'http://www.w3.org/2001/XMLSchema-instance' as \"xmlns:xsi\", 'http://www.oracle.com/schema/oim/offline Identity.xsd' as \"xsi:schemaLocation\"), (select xmlagg( xmlelement(\"identity\", xmlattributes(usr.usr_login as \"id\") , xmlelement(\"roles\" ,(select xmlagg( xmlelement(\"role\",xmlattributes(ugp.ugp_name as \"id\", ? as \"action\")) ) from usg , ugp where usg.ugp_key = ugp.ugp_key and usg.usr_key = usr.usr_key and ugp.ugp_key > 5 and ugp.ugp_role_category_key <> 2 ) ) ) ) as innerdata from usg usgp , usr where usgp.usr_key = usr.usr_key and usgp.usr_key > 5 and usgp.ugp_key > 5 )).getclobval() as xmldata from dual";
  private static final String   SQLstmt   = "SELECT xmlelement(\"identities\", xmlattributes('http://www.oracle.com/schema/oim/offline' AS \"xmlns\", 'http://www.w3.org/2001/XMLSchema-instance' AS \"xmlns:xsi\", 'http://www.oracle.com/schema/oim/offline Identity.xsd' AS \"xsi:schemaLocation\"), (select xmlagg( xmlelement(\"identity\", xmlattributes(usr.usr_login as \"id\") , xmlelement(\"roles\" , xmlagg( xmlelement(\"role\", xmlattributes(ugp.ugp_name  as \"id\", ? as \"action\")) ) ) ) ) from   usr , ugp , usg where  usr.usr_key > 5 and ugp.ugp_role_category_key != 2 and usg.ugp_key = ugp.ugp_key and usg.usr_key = usr.usr_key group by usr.usr_login)).getclobval() as xmldata from dual";
  protected static final String ACTION = "Action";

  private static final TaskAttribute[] attribute = {
    /** the task attribute that holds the value of the last run */
    TaskAttribute.build(ACTION,               TaskAttribute.MANDATORY),
    /** Application Instance Name */
    TaskAttribute.build(APPLICATION_INSTANCE, TaskAttribute.MANDATORY),
    /** the filename of the raw data  */
    TaskAttribute.build(DATAFILE,             TaskAttribute.MANDATORY),
    /** the location from where the raw files will be loaded */
    TaskAttribute.build(DATA_FOLDER,          TaskAttribute.MANDATORY),
    /** the location where the raw files should be copied after they are not successfully proceeded */
    //, TaskAttribute.build(ERROR_FOLDER,         TaskAttribute.MANDATORY)
    /** the file encoding to use */
    TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  public ExportIdentityRole() {
    // ensure inheritance
    super();
  }

  @Override
  protected void onExecution() throws TaskException {

    final String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(HarvesterBundle.format(HarvesterMessage.EXPORTING_BEGIN, parameter));

    Connection           connection = null;
    PreparedStatement    getFile    = null;
    try {
      connection = DatabaseConnection.aquire();

      getFile = DatabaseStatement.createPreparedStatement(connection, SQLstmt);
      getFile.setString(1, stringValue(ACTION));

      HarvesterBundle.string(HarvesterMessage.COLLECTING_BEGIN);
      final ResultSet rs = getFile.executeQuery();


      HarvesterBundle.string(HarvesterMessage.COLLECTING_COMPLETE);

      List<String> finalXML = new ArrayList<String>();

      finalXML.add(String.format(XMLEntityFactory.PROLOG, stringValue(FILE_ENCODING)));

      while (rs.next()) {
          finalXML.add(rs.getString(1));
      }

      if (!gatherOnly()){
        useBufferedFileOutPutStream(finalXML, dataFile().getAbsolutePath(), stringValue(FILE_ENCODING));
        updateLastReconciled();
        incrementSuccess();
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

}
