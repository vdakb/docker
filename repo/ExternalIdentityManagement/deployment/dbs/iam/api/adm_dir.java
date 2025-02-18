Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem $Header$
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running.
Rem
Rem Purpose.....:
Rem     This script creates all object required to visualize and manage
Rem     files in an mapped directory.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for the Oracle Identity Access Management.
Rem
Rem      This script creates the java stored functions to expose
Rem      the mapped directory as a regular file system.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2020-12-ß4  DSteding    First release version
Rem

PROMPT ********************************************************************
PROMPT Creating Java Class 'Directory'
PROMPT ********************************************************************
CREATE OR REPLACE AND COMPILE JAVA SOURCE NAMED "Directory"
AS

import java.io.File;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Directory {
  public static void list(final String path)
    throws SQLException {

    final File     source = new File(path);
    final String[] items  = source.list();
    final Connection  connection = DriverManager.getConnection("jdbc:default:connection:");
    PreparedStatement statement = null;
    try {
      statement  = connection.prepareStatement("INSERT INTO iam_directory(pathname,filename) VALUES (?,?)");
      for(int i = 0; i < items.length; i++) {
        statement.setString(1, source. 	getAbsolutePath());
        statement.setString(2, items[i]);
        statement.executeUpdate();
      }
    }
    finally {
      statement.close();
    }
  }
}
/
SHOW ERRORS JAVA SOURCE "Directory"