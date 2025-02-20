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
PROMPT Creating Package Specification 'iam$directory'
PROMPT ********************************************************************
CREATE OR REPLACE PACKAGE iam$directory
AS
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
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(revision, WNDS, WNPS);

  --
  -- Purpose  List content of a data backup directory
  --
  -- Usage    EXEC iam$directory.iad_data_list;
  --
  -- Remarks  SELECT * FROM iam_directory WHERE rownum < 5;
  --
  PROCEDURE iad_data_list;

  --
  -- Purpose  List content of a logs backup directory
  --
  -- Usage    EXEC iam$directory.iad_logs_list;
  --
  -- Remarks  SELECT * FROM iam_directory WHERE rownum < 5;
  --
  PROCEDURE iad_logs_list;

  --
  -- Purpose  List content of a data backup directory
  --
  -- Usage    EXEC iam$directory.igd_data_list;
  --
  -- Remarks  SELECT * FROM iam_directory WHERE rownum < 5;
  --
  PROCEDURE igd_data_list;

  --
  -- Purpose  List content of a logs backup directory
  --
  -- Usage    EXEC iam$directory.igd_logs_list;
  --
  -- Remarks  SELECT * FROM iam_directory WHERE rownum < 5;
  --
  PROCEDURE igd_logs_list;

  --
  -- Purpose  List content of a data backup directory
  --
  -- Usage    EXEC iam$directory.icd_data_list;
  --
  -- Remarks  SELECT * FROM iam_directory WHERE rownum < 5;
  --
  PROCEDURE icd_data_list;

  --
  -- Purpose  List content of a logs backup directory
  --
  -- Usage    EXEC iam$directory.icd_logs_list;
  --
  -- Remarks  SELECT * FROM iam_directory WHERE rownum < 5;
  --
  PROCEDURE icd_logs_list;

  --
  -- Purpose  List content of a particular file system path
  --
  -- Usage    EXEC iam$directory.list('/tmp');
  --
  -- Remarks  SELECT * FROM iam_directory WHERE rownum < 5;
  --
  PROCEDURE list(p_path IN VARCHAR2);
END iam$directory;
/
SHOW ERROR