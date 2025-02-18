Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script creates the owner of the Employee Portal Repository in an
Rem     Oracle Database Server 12c.
Rem     The script assumes that the owner of the Meta Repository is defined by
Rem     variable 'eppschema' with the password provided by variable
Rem     'epppassword'.
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
spool ./log/sys/epp.log

Rem *
Rem * Drop the owner of the Employee Self Service Portal repository and all
Rem * database objects owned by him.
Rem *
PROMPT ********************************************************************
PROMPT * Dropping Employee Self Service Portal Schema &&eppschema
PROMPT ********************************************************************
DROP USER &&eppschema cascade
/

Rem *
Rem * Create the owner of the Employee Self Service Portal repository with
Rem * the appropriate properties to create and store database objects.
Rem *
PROMPT ********************************************************************
PROMPT * Creating Employee Self Service Portal Schema &&eppschema
PROMPT ********************************************************************
CREATE USER &&eppschema IDENTIFIED BY &&epppassword
  DEFAULT   TABLESPACE &&eppdata
  TEMPORARY TABLESPACE &&epptemp
  QUOTA UNLIMITED ON   &&eppdata
/

Rem *
Rem * Grant necessary system roles to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system roles to user &&eppschema
PROMPT ********************************************************************
GRANT CONNECT, RESOURCE TO &&eppschema
/

Rem *
Rem * Grant necessary system privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system privileges to user &&eppschema
PROMPT ********************************************************************
GRANT CREATE VIEW TO &&eppschema
/

Rem *
Rem * Grant necessary object privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting object privileges to user &&eppschema
PROMPT ********************************************************************
GRANT SELECT ON sys.v_$process TO &&eppschema
/
GRANT SELECT ON sys.v_$session TO &&eppschema
/
GRANT SELECT ON &&oigschema..act TO &&eppschema
/
GRANT SELECT ON &&oigschema..usr TO &&eppschema
/

Rem *
Rem * Switch spool off
Rem *
spool off