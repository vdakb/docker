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
PROMPT Creating Package Implementation 'iam$directory'
PROMPT ********************************************************************
CREATE OR REPLACE PACKAGE BODY iam$directory
AS

  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  revision_label CONSTANT VARCHAR2(8) := '1.0.0.0';

  -- ---------------------------------------------------------------------------
  -- private program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  List content of a particular file system path
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE list(p_path IN VARCHAR2)
  AS
    LANGUAGE JAVA NAME 'Directory.list(java.lang.String)';

  --
  -- Purpose  Returns the mapped file name of a directory
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION directory(p_directory_name IN VARCHAR2)
    RETURN VARCHAR2
  IS
    l_directory_path sys.dba_directories.directory_path%TYPE;
  BEGIN
    SELECT directory_path
    INTO   l_directory_path
    FROM   sys.dba_directories
    WHERE  directory_name = p_directory_name
    ;
    RETURN l_directory_path;
  EXCEPTION
    WHEN OTHERS
    THEN
      RETURN NULL;
  END;

  -- ---------------------------------------------------------------------------
  -- public program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  returns the revision label of this package
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION revision
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN revision_label;
  END revision;

  --
  -- Purpose  List content of a data backup directory
  --
  -- Usage    EXEC iam$directory.iad_data_list;
  --
  -- Remarks  SELECT * FROM iam_directory;
  --
  PROCEDURE iad_data_list
  IS
  BEGIN
    list(directory('IADDATA'));
  END iad_data_list;

  --
  -- Purpose  List content of a logs backup directory
  --
  -- Usage    EXEC iam$directory.iad_logs_list;
  --
  -- Remarks  SELECT * FROM iam_directory;
  --
  PROCEDURE iad_logs_list
  IS
  BEGIN
    list(directory('IADLOGS'));
  END iad_logs_list;
  --
  -- Purpose  List content of a data backup directory
  --
  -- Usage    EXEC iam$directory.igd_data_list;
  --
  -- Remarks  SELECT * FROM iam_directory;
  --
  PROCEDURE igd_data_list
  IS
  BEGIN
    list(directory('IGDDATA'));
  END igd_data_list;

  --
  -- Purpose  List content of a logs backup directory
  --
  -- Usage    EXEC iam$directory.igd_logs_list;
  --
  -- Remarks  SELECT * FROM iam_directory;
  --
  PROCEDURE igd_logs_list
  IS
  BEGIN
    list(directory('IGDLOGS'));
  END igd_logs_list;
  --
  -- Purpose  List content of a data backup directory
  --
  -- Usage    EXEC iam$directory.icd_data_list;
  --
  -- Remarks  SELECT * FROM iam_directory;
  --
  PROCEDURE icd_data_list
  IS
  BEGIN
    list(directory('ICDDATA'));
  END icd_data_list;

  --
  -- Purpose  List content of a logs backup directory
  --
  -- Usage    EXEC iam$directory.icd_logs_list;
  --
  -- Remarks  SELECT * FROM iam_directory;
  --
  PROCEDURE icd_logs_list
  IS
  BEGIN
    list(directory('ICDLOGS'));
  END icd_logs_list;
END iam$directory;
/
SHOW ERROR