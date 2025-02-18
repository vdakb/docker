Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Unique Identifier Generator state data.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/sts
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Unique Identifier Generator States
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '0'
, 1
, 'Bund'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '1'
, 1
, 'Schleswig-Holstein'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '2'
, 1
, 'Hamburg'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '3'
, 1
, 'Niedersachsen'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '4'
, 1
, 'Bremen'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '5'
, 1
, 'Nordrhein-Westfalen'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '6'
, 1
, 'Hessen'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '7'
, 1
, 'Rheinland-Pfalz'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '8'
, 1
, 'Baden-Württemberg'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '9'
, 1
, 'Bayern'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '10'
, 1
, 'Saarland'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '11'
, 1
, 'Berlin'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '12'
, 1
, 'Brandenburg'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '13'
, 1
, 'Mecklenburg-Vorpommern'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '14'
, 1
, 'Sachsen'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '15'
, 1
, 'Sachsen-Anhalt'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '16'
, 1
, 'Thüringen'
)
/
INSERT INTO uit_states (
  id
, active
, name
) VALUES (
  '99'
, 1
, 'International'
)
/