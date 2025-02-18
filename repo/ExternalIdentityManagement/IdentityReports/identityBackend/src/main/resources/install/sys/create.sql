Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script grants access to all object required to use Oracle
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
PROMPT Granting Oracle Identity Governance Objects
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
GRANT SELECT ON igd_oim.act TO igd_igs
/
GRANT SELECT ON igd_oim.role_category TO igd_igs
/
GRANT SELECT ON igd_oim.ugp TO igd_igs
/
GRANT SELECT ON igd_oim.usr TO igd_igs
/