Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Unique Identifier Generator identity type data.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/typ
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Unique Identifier Generator Identity Types
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
INSERT INTO uit_types (
  id
, active
, name
) VALUES (
  '101'
, 1
, 'Anwenderkonto-Mitarbeiter'
)
/
INSERT INTO uit_types (
  id
, active
, name
) VALUES (
  '111'
, 1
, 'Administrationskonto für Fachanwendung'
)
/
