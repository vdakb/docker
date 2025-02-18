Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Unique Identifier Generator tenant claim test data.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/clm
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ######################################################
PROMPT Create Identity Governance Services permission data
PROMPT ######################################################
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Assign Unique Identifier Generator Claims to user igssysadm
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-20';
  cg$rec.rol_id := 'uid.generate';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-20';
  cg$rec.rol_id := 'uid.register';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-30';
  cg$rec.rol_id := 'uid.generate';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-30';
  cg$rec.rol_id := 'uid.register';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-31';
  cg$rec.rol_id := 'uid.generate';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-31';
  cg$rec.rol_id := 'uid.register';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-36';
  cg$rec.rol_id := 'uid.generate';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  cg$rec.usr_id := -1;
  cg$rec.tnt_id := 'T-36-0-36';
  cg$rec.rol_id := 'uid.register';
  --
  cg$uit_claims.ins(cg$rec, cg$ind);
END;
/