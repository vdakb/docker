Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Anonymous Identifier Generator permissions.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/url
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

Rem *
Rem * Switch schema
Rem *
PROMPT ######################################################
PROMPT Switching session context ...
PROMPT ######################################################
ALTER SESSION SET CURRENT_SCHEMA = igd_igs
/

PROMPT ######################################################
PROMPT Configure session context ...
PROMPT ######################################################
EXECUTE cg$sessions.set_identity('igssysadm')

PROMPT ######################################################
PROMPT Create Identity Governance Services permission data
PROMPT ######################################################
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Assign administrative role to user igssysadm
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DECLARE
  cg$rec cg$igt_userroles.cg$row_type;
  cg$ind cg$igt_userroles.cg$ind_type;
BEGIN
  SELECT id
  INTO   cg$rec.usr_id
  FROM   igt_users usr
  WHERE  username ='bk4711124'
  ;
  cg$rec.rol_id := 'pid.admin';
  --
  cg$igt_userroles.ins(cg$rec, cg$ind);
END;
/