Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Identity Governance Services messages.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/rol
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Identity Governance Services Roles
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DECLARE
  cg$rec cg$igt_roles.cg$row_type;
  cg$ind cg$igt_roles.cg$ind_type;
BEGIN
  cg$ind.id           := FALSE;
  cg$rec.id           := 'igs.admin';
  cg$rec.active       := 1;
  cg$rec.display_name := 'Identity Governance Services Administrator';
  --
  cg$igt_roles.ins(cg$rec, cg$ind);
END;
/