Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script creates the owner of the eFBS User Repository in an
Rem     Oracle Database Server 12c.
Rem     The script assumes that the owner of the Meta Repository is defined by
Rem     variable 'agtschema' with the password provided by variable
Rem     'agtpassword'.
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
spool ./log/sys/agt.log

Rem *
Rem * Drop the owner of the eFBS User Repository and all database objects
Rem * owned by him.
Rem *
PROMPT ********************************************************************
PROMPT * Dropping eFBS User Repository Schema &&agtschema
PROMPT ********************************************************************
DROP USER &&agtschema cascade
/

Rem *
Rem * Create the owner of the eFBS User Repository with the appropriate
Rem * properties to create and store database objects.
Rem *
PROMPT ********************************************************************
PROMPT * Creating eFBS User Repository Schema &&agtschema
PROMPT ********************************************************************
CREATE USER &&agtschema IDENTIFIED BY &&agtpassword
  DEFAULT   TABLESPACE &&agtdata
  TEMPORARY TABLESPACE &&agttemp
  QUOTA UNLIMITED ON   &&agtdata
/

Rem *
Rem * Grant necessary system roles to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system roles to user &&agtschema
PROMPT ********************************************************************
GRANT CONNECT, RESOURCE TO &&agtschema
/

Rem *
Rem * Switch spool off
Rem *
spool off