Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem $Header$
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running.
Rem
Rem Purpose.....:
Rem     This script removes persistence API created by Identity
Rem     Governance Services inside of a database used as Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2022-03-11  DSteding    First release version
Rem

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'pit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_pit_identifiers
/
DROP TRIGGER cg$bdr_pit_identifiers
/
DROP TRIGGER cg$bds_pit_identifiers
/
DROP TRIGGER cg$aus_pit_identifiers
/
DROP TRIGGER cg$bur_pit_identifiers
/
DROP TRIGGER cg$bus_pit_identifiers
/
DROP TRIGGER cg$ais_pit_identifiers
/
DROP TRIGGER cg$air_pit_identifiers
/
DROP TRIGGER cg$bir_pit_identifiers
/
DROP TRIGGER cg$bis_pit_identifiers
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'pit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$pit_identifiers
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'pit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$pit_identifiers
/