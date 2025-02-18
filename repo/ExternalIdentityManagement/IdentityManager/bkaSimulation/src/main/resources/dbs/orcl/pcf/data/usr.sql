Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Pivotal User Repository seeded accounts.
Rem
Rem Usage Information:
Rem     sqlplus pcfschema/<password>
Rem     @<path>/usr
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

PROMPT
PROMPT Create seeded user for Pivotal User Repository
INSERT INTO pct_usr (
  username
, lastname
, firstname
)
VALUES (
  'admin'
, 'Administrator'
, 'System'
)
/
COMMIT
/