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

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Assign administrative role to user igssysadm
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DECLARE
  cg$rec cg$igt_userroles.cg$row_type;
  cg$ind cg$igt_userroles.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.rol_id := 'pid.admin';
  --
  cg$igt_userroles.ins(cg$rec, cg$ind);
END;
/