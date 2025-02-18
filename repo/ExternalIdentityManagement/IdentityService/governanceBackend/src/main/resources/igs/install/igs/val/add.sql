Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Identity Governance Services test users.
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
PROMPT Creating test users ...
PROMPT ######################################################
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Identity Governance User an4711123
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'an4711123';
  cg$rec.credential := 'changeit';
  cg$rec.lastname  := 'Zitterbacke';
  cg$rec.firstname := 'Alfons';
  cg$rec.language  := 'en';
  cg$rec.email     := 'alfons.zitterbacke@cinnaminstar.net';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Sophie Strecke
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username   := 'bk4711124';
  cg$rec.credential := 'changeit';
  cg$rec.lastname   := 'Strecke';
  cg$rec.firstname  := 'Sophie';
  cg$rec.language   := 'en';
  cg$rec.email      := 'sophie.strecke@cinnaminstar.net';
  cg$rec.phone      := '+49 (0)611 688 000';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Agathe Musterfrau
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username   := 'bp4711123';
  cg$rec.credential := 'changeit';
  cg$rec.lastname   := 'Musterfrau';
  cg$rec.firstname  := 'Agathe';
  cg$rec.language   := 'en';
  cg$rec.email      := 'agathe.musterfrau@cinnaminstar.net';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Max Gaertner
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username   := 'bwgaertmax';
  cg$rec.credential := 'changeit';
  cg$rec.lastname   := 'Gärtner';
  cg$rec.firstname  := 'Max';
  cg$rec.language   := 'en';
  cg$rec.email      := 'max.gaertner@cinnaminstar.net';
  cg$rec.phone      := '+49 (0)711 688 000';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/