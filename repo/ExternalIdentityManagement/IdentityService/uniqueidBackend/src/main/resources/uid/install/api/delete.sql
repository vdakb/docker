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

-----------------------------------------------------------------------------------------
-- DROP Administration API implementation
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body 'uid$tenant'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY uid$tenant
/
-----------------------------------------------------------------------------------------
-- DROP Administration API specification
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package 'uid$tenant'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE uid$tenant
/
-----------------------------------------------------------------------------------------
-- DROP Core Table API interfaces
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_identifiers
/
DROP TRIGGER cg$bdr_uit_identifiers
/
DROP TRIGGER cg$bds_uit_identifiers
/
DROP TRIGGER cg$aus_uit_identifiers
/
DROP TRIGGER cg$bur_uit_identifiers
/
DROP TRIGGER cg$bus_uit_identifiers
/
DROP TRIGGER cg$ais_uit_identifiers
/
DROP TRIGGER cg$air_uit_identifiers
/
DROP TRIGGER cg$bir_uit_identifiers
/
DROP TRIGGER cg$bis_uit_identifiers
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_claims'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_claims
/
DROP TRIGGER cg$bdr_uit_claims
/
DROP TRIGGER cg$bds_uit_claims
/
DROP TRIGGER cg$aus_uit_claims
/
DROP TRIGGER cg$bur_uit_claims
/
DROP TRIGGER cg$bus_uit_claims
/
DROP TRIGGER cg$ais_uit_claims
/
DROP TRIGGER cg$air_uit_claims
/
DROP TRIGGER cg$bir_uit_claims
/
DROP TRIGGER cg$bis_uit_claims
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_tenants'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_tenants
/
DROP TRIGGER cg$bdr_uit_tenants
/
DROP TRIGGER cg$bds_uit_tenants
/
DROP TRIGGER cg$aus_uit_tenants
/
DROP TRIGGER cg$bur_uit_tenants
/
DROP TRIGGER cg$bus_uit_tenants
/
DROP TRIGGER cg$ais_uit_tenants
/
DROP TRIGGER cg$air_uit_tenants
/
DROP TRIGGER cg$bir_uit_tenants
/
DROP TRIGGER cg$bis_uit_tenants
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_types'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_types
/
DROP TRIGGER cg$bdr_uit_types
/
DROP TRIGGER cg$bds_uit_types
/
DROP TRIGGER cg$aus_uit_types
/
DROP TRIGGER cg$bur_uit_types
/
DROP TRIGGER cg$bus_uit_types
/
DROP TRIGGER cg$ais_uit_types
/
DROP TRIGGER cg$air_uit_types
/
DROP TRIGGER cg$bir_uit_types
/
DROP TRIGGER cg$bis_uit_types
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_participants'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_participants
/
DROP TRIGGER cg$bdr_uit_participants
/
DROP TRIGGER cg$bds_uit_participants
/
DROP TRIGGER cg$aus_uit_participants
/
DROP TRIGGER cg$bur_uit_participants
/
DROP TRIGGER cg$bus_uit_participants
/
DROP TRIGGER cg$ais_uit_participants
/
DROP TRIGGER cg$air_uit_participants
/
DROP TRIGGER cg$bir_uit_participants
/
DROP TRIGGER cg$bis_uit_participants
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_states'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_states
/
DROP TRIGGER cg$bdr_uit_states
/
DROP TRIGGER cg$bds_uit_states
/
DROP TRIGGER cg$aus_uit_states
/
DROP TRIGGER cg$bur_uit_states
/
DROP TRIGGER cg$bus_uit_states
/
DROP TRIGGER cg$ais_uit_states
/
DROP TRIGGER cg$air_uit_states
/
DROP TRIGGER cg$bir_uit_states
/
DROP TRIGGER cg$bis_uit_states
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_countries'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_countries
/
DROP TRIGGER cg$bdr_uit_countries
/
DROP TRIGGER cg$bds_uit_countries
/
DROP TRIGGER cg$aus_uit_countries
/
DROP TRIGGER cg$bur_uit_countries
/
DROP TRIGGER cg$bus_uit_countries
/
DROP TRIGGER cg$ais_uit_countries
/
DROP TRIGGER cg$air_uit_countries
/
DROP TRIGGER cg$bir_uit_countries
/
DROP TRIGGER cg$bis_uit_countries
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'uit_participant_types'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_uit_participant_types
/
DROP TRIGGER cg$bdr_uit_participant_types
/
DROP TRIGGER cg$bds_uit_participant_types
/
DROP TRIGGER cg$aus_uit_participant_types
/
DROP TRIGGER cg$bur_uit_participant_types
/
DROP TRIGGER cg$bus_uit_participant_types
/
DROP TRIGGER cg$ais_uit_participant_types
/
DROP TRIGGER cg$air_uit_participant_types
/
DROP TRIGGER cg$bir_uit_participant_types
/
DROP TRIGGER cg$bis_uit_participant_types
/
-----------------------------------------------------------------------------------------
-- DROP Core Table API implementation
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_identifiers
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_claims'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_claims
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_tenants'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_tenants
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_types'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_types
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_participants'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_participants
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_states'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_states
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_countries'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_countries
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'uit_participant_types'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$uit_participant_types
/
-----------------------------------------------------------------------------------------
-- DROP Core Table API specification
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_identifiers
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_claims'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_claims
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_tenants'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_tenants
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_types'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_types
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_participants'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_participants
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_states'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_states
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_countries'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_countries
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'uit_participant_types'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$uit_participant_types
/