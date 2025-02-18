Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script creates the user of the openFire Repository in an Oracle
Rem     Database Server 12c.
Rem     The script assumes that the owner of the Meta Repository is defined by
Rem     variable 'ofsschema'.
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
Rem * Drop the user of the openFire Repository database objects owned by
Rem * him.
Rem *
PROMPT ********************************************************************
PROMPT * Dropping openFire Repository user iamuser
PROMPT ********************************************************************
DROP USER iamuser cascade
/

Rem *
Rem * Create the user of the openFire Repository repository with the
Rem * appropriate properties to create and store database objects.
Rem *
PROMPT ********************************************************************
PROMPT * Creating openFire Repository user iamuser
PROMPT ********************************************************************
CREATE USER iamuser IDENTIFIED BY Welcome1
  DEFAULT   TABLESPACE usr_data
  TEMPORARY TABLESPACE temp
/

Rem *
Rem * Grant necessary system roles to the repository user
Rem *
PROMPT ********************************************************************
PROMPT * Granting system roles to user iamuser
PROMPT ********************************************************************
GRANT CONNECT TO iamuser
/

Rem *
Rem * Grant necessary object privileges to the repository user
Rem *
PROMPT ********************************************************************
PROMPT * Granting object privileges to user iamuser
PROMPT ********************************************************************
GRANT SELECT, INSERT, UPDATE, DELETE ON ofsowner.ofuser TO iamuser
/
GRANT SELECT, INSERT, UPDATE, DELETE ON ofsowner.ofuserprop TO iamuser
/
GRANT SELECT, INSERT, UPDATE, DELETE ON ofsowner.ofgroupuser TO iamuser
/
GRANT SELECT, INSERT, UPDATE, DELETE ON ofsowner.ofuserflag TO iamuser
/
GRANT SELECT, INSERT, UPDATE, DELETE ON ofsowner.ofmucmember TO iamuser
/
GRANT SELECT, INSERT, UPDATE, DELETE ON ofsowner.ofproperty TO iamuser
/
GRANT SELECT ON ofsowner.ofgroup TO iamuser
/
GRANT SELECT ON ofsowner.ofgroupprop TO iamuser
/
GRANT SELECT ON ofsowner.ofmucroom TO iamuser
/
GRANT SELECT ON ofsowner.ofmucroomprop TO iamuser
/

Rem *
Rem * Switch spool off
Rem *
spool off