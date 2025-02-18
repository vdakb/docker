Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Identity Governance Services system users.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/usr
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Identity Governance Services Users
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Identity Governance Administrator
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$ind.id         := TRUE;
  cg$rec.id         := -1;
  cg$rec.username   := 'igssysadm';
  cg$rec.credential := 'unused';
  cg$rec.lastname   := 'Administrator';
  cg$rec.firstname  := 'System';
  cg$rec.language   := 'en';
  cg$rec.email      := 'igssysadm@bka.bund.de';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/