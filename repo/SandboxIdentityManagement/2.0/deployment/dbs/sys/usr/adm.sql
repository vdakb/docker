Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script creates the owner of the Database Utility Repository in an
Rem     Oracle Database Server 12c.
Rem     The script assumes that the owner of the Meta Repository is defined by
Rem     variable 'admschema' with the password provided by variable
Rem     'admpassword'.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem Note........:
Rem     This type of installation is NOT supported on MS Windows 2000/XP.
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./log/sys/adm.log

Rem *
Rem * Drop the owner of the Employee Self Service Portal repository and all
Rem * database objects owned by him.
Rem *
PROMPT ********************************************************************
PROMPT * Dropping Employee Self Service Portal Schema &&admschema
PROMPT ********************************************************************
DROP USER &&admschema cascade
/

Rem *
Rem * Create the owner of the Employee Self Service Portal repository with
Rem * the appropriate properties to create and store database objects.
Rem *
PROMPT ********************************************************************
PROMPT * Creating Employee Self Service Portal Schema &&admschema
PROMPT ********************************************************************
CREATE USER &&admschema IDENTIFIED BY &&admpassword
  DEFAULT   TABLESPACE &&admdata
  TEMPORARY TABLESPACE &&admtemp
  QUOTA UNLIMITED ON   &&admdata
/

Rem *
Rem * Grant necessary system roles to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system roles to user &&admschema
PROMPT ********************************************************************
GRANT CONNECT, RESOURCE TO &&admschema
/

Rem *
Rem * Grant necessary system privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system privileges to user &&admschema
PROMPT ********************************************************************
GRANT CREATE TABLE TO &&admschema
/
GRANT JAVAUSERPRIV TO &&admschema
/

Rem *
Rem * Grant necessary object privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting object privileges to user &&admschema
PROMPT ********************************************************************
GRANT SELECT ON sys.dba_directories TO &&admschema
/

Rem *
Rem * Switch spool off
Rem *
spool off