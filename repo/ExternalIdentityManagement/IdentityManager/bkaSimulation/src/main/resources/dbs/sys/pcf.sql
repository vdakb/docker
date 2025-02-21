Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script creates the owner of the Pivotal Repository in an
Rem     Oracle Database Server 12c.
Rem     The script assumes that the owner of the Meta Repository is defined by
Rem     variable 'pvcschema' with the password provided by variable
Rem     'pvcpassword'.
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
spool ./log/sys/pvc.log

Rem *
Rem * Drop the owner of the Pivotal User Repository and all database objects
Rem * owned by him.
Rem *
PROMPT ********************************************************************
PROMPT * Dropping Pivotal User Repository Schema &&pvcschema
PROMPT ********************************************************************
DROP USER &&pvcschema cascade
/

Rem *
Rem * Create the owner of the Pivotal User Repository with the appropriate
Rem * properties to create and store database objects.
Rem *
PROMPT ********************************************************************
PROMPT * Creating Pivotal User Repository Schema &&pvcschema
PROMPT ********************************************************************
CREATE USER &&pvcschema IDENTIFIED BY &&pvcpassword
  DEFAULT   TABLESPACE &&pvcdata
  TEMPORARY TABLESPACE &&pvctemp
  QUOTA UNLIMITED ON   &&pvcdata
/

Rem *
Rem * Grant necessary system roles to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system roles to user &&pvcschema
PROMPT ********************************************************************
GRANT CONNECT, RESOURCE TO &&pvcschema
/

Rem *
Rem * Switch spool off
Rem *
spool off