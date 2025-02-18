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
PROMPT Delete Identity Governance Services foundation data
PROMPT ######################################################
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Unique Identifier Assembler Message Texts
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DELETE qms_message_text
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Unique Identifier Assembler Message Properties
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DELETE qms_message_properties
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Unique Identifier Assembler System Preferences
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DELETE qms_system_preferences
/

-----------------------------------------------------------------------------------------
-- COMMIT changes
-----------------------------------------------------------------------------------------
commit
/