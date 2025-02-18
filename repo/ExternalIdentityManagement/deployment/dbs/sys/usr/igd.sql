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
Rem     This script creates all users and their permission required to expose
Rem     Oracle Identity Manager as an Directory Service leveraging Oracle Unified
Rem     Directory proxy capabilities.
Rem
Rem     This option is mandatory if the database is used as an
Rem     identity store for participants exposed by Oracle Unified Directory.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2019-06-12  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./log/sys/igd.log

Rem *
Rem * Drop the user for Oracle Unified Directory database access and all
Rem * database objects owned by him.
Rem *
PROMPT ********************************************************************
PROMPT * Dropping user &&oudschema
PROMPT ********************************************************************
DROP USER &&oudschema CASCADE
/

Rem *
Rem * Create the user for Oracle Unified Directory database access with
Rem * appropriate properties to create and store database objects
Rem *
PROMPT ********************************************************************
PROMPT * Creating user &&oudschema
PROMPT ********************************************************************
CREATE USER &&oudschema IDENTIFIED BY &&oudpassword
  DEFAULT   TABLESPACE &&ouddata
  TEMPORARY TABLESPACE &&oudtemp
/

Rem *
Rem * Grant necessary system privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system privileges to user &&oudschema
PROMPT ********************************************************************
GRANT CREATE SESSION TO &&oudschema
/
GRANT CREATE VIEW TO &&oudschema
/
GRANT CREATE PROCEDURE TO &&oudschema
/
GRANT CREATE TRIGGER TO &&oudschema
/

Rem *
Rem * Grant necessary object privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting object privileges to user &&oudschema
PROMPT ********************************************************************
GRANT SELECT ON igd_oim.usr TO &&oudschema
/
GRANT SELECT ON igd_oim.act TO &&oudschema
/

Rem *
Rem * Switch spool off
Rem *
spool off