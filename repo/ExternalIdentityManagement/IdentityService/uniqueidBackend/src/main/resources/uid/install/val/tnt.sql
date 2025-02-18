Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Unique Identifier Generator tenant data.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/tnt
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Unique Identifier Generator Tenants
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
INSERT INTO uit_tenants (
  id
, name
) VALUES (
  'T-36-0-20'
, 'Bundeskriminalamt'
)
/
INSERT INTO uit_tenants (
  id
, name
) VALUES (
  'T-36-0-30'
, 'Bundespolizei'
)
/
INSERT INTO uit_tenants (
  id
, name
) VALUES (
  'T-36-0-31'
, 'Zollkriminalamt'
)
/
INSERT INTO uit_tenants (
  id
, name
) VALUES (
  'T-36-0-36'
, 'Polizei des Deutschen Bundestag'
)
/