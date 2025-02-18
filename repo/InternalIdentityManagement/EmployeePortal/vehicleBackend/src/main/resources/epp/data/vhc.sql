Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Employee Self Service Vehicle Colors.
Rem
Rem Usage Information:
Rem     sqlplus eppowner/<password>
Rem     @<path>/vhc
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2010-03-30  DSteding    First release version
Rem

PROMPT ********************************************************************
PROMPT Setting user context
PROMPT ********************************************************************
BEGIN
  epp$session.set_identity('xelsysadm', 'CLI');
END;
/
PROMPT ********************************************************************
PROMPT Create default vehicle colors for Employee Self Service Portal
PROMPT For reference consult
PROMPT https://www.kba.de/DE/Presse/Archiv/Farbschluesselnummern/farbschluesselnummern_node.html
PROMPT ********************************************************************
DECLARE
  cg$rec cg$ept_vehicle_colors.cg$row_type;
  cg$ind cg$ept_vehicle_colors.cg$ind_type;
BEGIN
  cg$ind.id      := TRUE;
  cg$rec.id      := 0;
  cg$rec.meaning := 'Weiß';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 1;
  cg$rec.meaning := 'Gelb';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 2;
  cg$rec.meaning := 'Orange';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 3;
  cg$rec.meaning := 'Rot';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 4;
  cg$rec.meaning := 'Lila/Violett';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 5;
  cg$rec.meaning := 'Blau';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 6;
  cg$rec.meaning := 'Grün';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 7;
  cg$rec.meaning := 'Grau';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 8;
  cg$rec.meaning := 'Braun';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 9;
  cg$rec.meaning := 'Schwarz';
  --
  cg$ept_vehicle_colors.ins(cg$rec, cg$ind);
END;
/
COMMIT
/
