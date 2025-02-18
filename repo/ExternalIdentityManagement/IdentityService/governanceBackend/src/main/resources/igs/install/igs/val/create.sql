Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Identity Governance Services seeded data.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/create
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

-----------------------------------------------------------------------------------------
-- SEED data
-----------------------------------------------------------------------------------------
PROMPT ######################################################
PROMPT Seeding Identity Governance Services aministration data
PROMPT ######################################################
@@osg
@@rol
@@usr
@@url

-----------------------------------------------------------------------------------------
-- COMMIT changes
-----------------------------------------------------------------------------------------
commit
/