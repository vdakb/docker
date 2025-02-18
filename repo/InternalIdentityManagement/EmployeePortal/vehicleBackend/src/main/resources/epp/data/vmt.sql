Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Employee Self Service Vehicle Brand Types.
Rem
Rem Usage Information:
Rem     sqlplus eppowner/<password>
Rem     @<path>/vmb
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
PROMPT Create default vehicle brands for Employee Self Service Portal
PROMPT ********************************************************************
DECLARE
  cg$rec cg$ept_vehicle_brand_types.cg$row_type;
  cg$ind cg$ept_vehicle_brand_types.cg$ind_type;
BEGIN
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0005';
  cg$rec.vht_id      := 'M1';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0005';
  cg$rec.vht_id      := 'M1G';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0005';
  cg$rec.vht_id      := 'L3';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0005';
  cg$rec.vht_id      := 'L4';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0009';
  cg$rec.vht_id      := 'M1';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0009';
  cg$rec.vht_id      := 'M1G';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0009';
  cg$rec.vht_id      := 'M2';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0009';
  cg$rec.vht_id      := 'M2G';
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0009';
  cg$rec.vht_id      := 'M3';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0009';
  cg$rec.vht_id      := 'M3G';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0583';
  cg$rec.vht_id      := 'M1';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0583';
  cg$rec.vht_id      := 'M1G';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0583';
  cg$rec.vht_id      := 'M2';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0583';
  cg$rec.vht_id      := 'M2G';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0600';
  cg$rec.vht_id      := 'M1';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0600';
  cg$rec.vht_id      := 'M1G';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0600';
  cg$rec.vht_id      := 'M2';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
  --
  cg$ind.vmb_id      := TRUE;
  cg$ind.vht_id      := TRUE;
  cg$rec.vmb_id      := '0600';
  cg$rec.vht_id      := 'M2G';
  --
  cg$ept_vehicle_brand_types.ins(cg$rec, cg$ind);
END;
/
COMMIT
/
