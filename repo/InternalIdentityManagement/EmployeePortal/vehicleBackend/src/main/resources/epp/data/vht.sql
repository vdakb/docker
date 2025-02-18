Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Employee Self Service Vehicle Types.
Rem
Rem Usage Information:
Rem     sqlplus eppowner/<password>
Rem     @<path>/vht
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
PROMPT Create default vehicle types for Employee Self Service Portal
PROMPT ********************************************************************
DECLARE
  cg$rec cg$ept_vehicle_types.cg$row_type;
  cg$ind cg$ept_vehicle_types.cg$ind_type;
BEGIN
  cg$ind.id      := TRUE;
  cg$rec.id      := 'L6';
  cg$rec.meaning := 'Leichtkraftrad';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'L3';
  cg$rec.meaning := 'Kraftrad';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'L4';
  cg$rec.meaning := 'Kraftrad mit Beiwagen';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'L7';
  cg$rec.meaning := 'Quad zur Personenbeförderung';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'M1';
  cg$rec.meaning := 'Personenkraftwagen';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'M1G';
  cg$rec.meaning := 'Personenkraftwagen (Gelände)';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'M2';
  cg$rec.meaning := 'Omnibus <= 5T';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'M2G';
  cg$rec.meaning := 'Omnibus (Gelände) <= 5T';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'M3';
  cg$rec.meaning := 'Omnibus > 5T';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
  --
  cg$ind.id      := TRUE;
  cg$rec.id      := 'M3G';
  cg$rec.meaning := 'Omnibus (Gelände) > 5T';
  --
  cg$ept_vehicle_types.ins(cg$rec, cg$ind);
END;
/
COMMIT
/
