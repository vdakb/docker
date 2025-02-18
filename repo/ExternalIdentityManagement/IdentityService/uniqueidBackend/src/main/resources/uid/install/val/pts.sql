Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Unique Identifier Generator participants data.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/pts
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create Unique Identifier Generator Participants
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '01'
, 1
, 'Polizei Schleswig-Holstein'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '02'
, 1
, 'Polizei Hamburg'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '03'
, 1
, 'Polizei Niedersachsen'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '04'
, 1
, 'Polizei Bremen'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '05'
, 1
, 'Polizei Nordrhein-Westfalen'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '06'
, 1
, 'Polizei Hessen'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '07'
, 1
, 'Polizei Rheinland-Pfalz'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '08'
, 1
, 'Polizei Baden-Württemberg'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '09'
, 1
, 'Polizei Bayern'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '10'
, 1
, 'Polizei Saarland'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '11'
, 1
, 'Polizei Berlin'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '12'
, 1
, 'Polizei Brandenburg'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '13'
, 1
, 'Polizei Mecklenburg-Vorpommern'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '14'
, 1
, 'Polizei Sachen'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '15'
, 1
, 'Polizei Sachen-Anhalt'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '16'
, 1
, 'Polizei Thüringen'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '20'
, 1
, 'Bundeskriminalamt'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '30'
, 1
, 'Bundespolizei'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '31'
, 1
, 'Zollkriminalamt'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '36'
, 1
, 'Polizei des Deutschen Bundestag'
)
/
INSERT INTO uit_participants (
  id
, active
, name
) VALUES (
  '00099'
, 1
, 'International'
)
/