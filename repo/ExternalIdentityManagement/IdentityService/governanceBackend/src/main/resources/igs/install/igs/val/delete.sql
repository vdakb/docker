Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script deletes Identity Governance Services seeded data.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/delete
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
PROMPT Delete Identity Governance Services aministration data
PROMPT ######################################################
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Identity Governance Services Roles
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DELETE igt_roles
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Identity Governance Services Users
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DELETE igt_users
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Identity Governance Services Message Texts
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DELETE FROM qms_message_text
WHERE  code LIKE 'IGT%'
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Identity Governance Services Message Properties
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DELETE FROM qms_message_properties
WHERE  code LIKE 'IGT%'
/

-----------------------------------------------------------------------------------------
-- COMMIT changes
-----------------------------------------------------------------------------------------
commit
/