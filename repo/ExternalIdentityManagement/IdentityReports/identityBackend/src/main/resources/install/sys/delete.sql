Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script revokes access from all object required to use Oracle
Rem     BIPublisher as the reporting tool for a database to apply advanced
Rem     security options.
Rem
Rem Usage Information:
Rem     sqlplus sys/<password> as sysdba
Rem     @<path>/create
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Revoking Oracle Identity Governance Objects
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
REVOKE SELECT ON igd_oim.usr FROM igd_igs
/
REVOKE SELECT ON igd_oim.ugp FROM igd_igs
/
REVOKE SELECT ON igd_oim.role_category TO igd_igs
/
REVOKE SELECT ON igd_oim.act FROM igd_igs
/