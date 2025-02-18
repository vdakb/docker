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
Rem     @<path>/usr
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT Create Alfons Zitterbacke
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'T-36-15-15-101-an4711123';
  cg$rec.lastname  := 'Zitterbacke';
  cg$rec.firstname := 'Alfons';
  cg$rec.language  := 'en';
  cg$rec.email     := 'alfons.zitterbacke@cinnaminstar.net';
  cg$rec.phone     := '+49 (0)177 5948 437';
  cg$rec.mobile    := '+49 (0)177 5948 437';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Max Mustermann
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'T-36-0-20-101-bk4711123';
  cg$rec.lastname  := 'Mustermann';
  cg$rec.firstname := 'Max';
  cg$rec.language  := 'en';
  cg$rec.email     := 'max.musterman@cinnaminstar.net';
  cg$rec.phone     := '+49 (0)611 688 000';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Sophie Strecke
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'T-36-0-20-101-bk4711124';
  cg$rec.lastname  := 'Strecke';
  cg$rec.firstname := 'Sophie';
  cg$rec.language  := 'en';
  cg$rec.email     := 'sophie.strecke@cinnaminstar.net';
  cg$rec.phone     := '+49 (0)611 688 000';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Rene Donner
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'T-36-0-20-101-bk4711125';
  cg$rec.lastname  := 'Donner';
  cg$rec.firstname := 'Rene';
  cg$rec.language  := 'en';
  cg$rec.email     := 'rene.donner@cinnaminstar.net';
  cg$rec.phone     := '+49 (0)611 688 000';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Christian Durst
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'T-36-0-20-101-bk4711126';
  cg$rec.lastname  := 'Durst';
  cg$rec.firstname := 'Christian';
  cg$rec.language  := 'en';
  cg$rec.email     := 'christian.durst@cinnaminstar.net';
  cg$rec.phone     := '+49 (0)611 688 000';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Agathe Musterfrau
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'T-36-0-30-101-bp4711123';
  cg$rec.lastname  := 'Musterfrau';
  cg$rec.firstname := 'Agathe';
  cg$rec.language  := 'en';
  cg$rec.email     := 'agathe.musterfrau@cinnaminstar.net';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/
PROMPT Create Max Gaertner
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  cg$rec.username  := 'T-36-8-08-101-bwgaertmax';
  cg$rec.lastname  := 'Gärtner';
  cg$rec.firstname := 'Max';
  cg$rec.language  := 'en';
  cg$rec.email     := 'max.gaertner@cinnaminstar.net';
  cg$rec.phone     := '+49 (0)711 688 000';
  --
  cg$igt_users.ins(cg$rec, cg$ind);
END;
/