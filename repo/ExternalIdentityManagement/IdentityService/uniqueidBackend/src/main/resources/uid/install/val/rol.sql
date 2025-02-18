Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Unique Identifier Generator permissions.
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
PROMPT Create Unique Identifier Generator Roles
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DECLARE
  cg$rec cg$igt_roles.cg$row_type;
  cg$ind cg$igt_roles.cg$ind_type;
BEGIN
  --
  cg$ind.id           := FALSE;
  cg$rec.id           := 'uid.generate';
  cg$rec.active       := 1;
  cg$rec.display_name := 'Unique Identifier Generation';
  cg$rec.description  := 'Permits the associated account to generate unique identifiers for a tenant.';
  --
  cg$igt_roles.ins(cg$rec, cg$ind);
  --
  cg$ind.id           := FALSE;
  cg$rec.id           := 'uid.register';
  cg$rec.active       := 1;
  cg$rec.display_name := 'Unique Identifier Registration';
  cg$rec.description  := 'Permits the associated account to register unique identifiers for a tenant.';
  --
  cg$igt_roles.ins(cg$rec, cg$ind);
  --
  cg$ind.id           := FALSE;
  cg$rec.id           := 'uid.admin';
  cg$rec.active       := 1;
  cg$rec.display_name := 'Unique Identifier Administrator';
  cg$rec.description  := 'Permits the associated account to view unique identifiers for a tenant.';
  --
  cg$igt_roles.ins(cg$rec, cg$ind);
END;
/