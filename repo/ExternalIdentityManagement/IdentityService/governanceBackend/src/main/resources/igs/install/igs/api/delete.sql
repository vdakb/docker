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
-- DROP Adminstration API interfaces
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body 'igs$user'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY igs$user
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package 'igs$user'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE igs$user
/
-----------------------------------------------------------------------------------------
-- DROP Core Table API interfaces
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'igt_userroles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_igt_userroles
/
DROP TRIGGER cg$bdr_igt_userroles
/
DROP TRIGGER cg$bds_igt_userroles
/
DROP TRIGGER cg$aus_igt_userroles
/
DROP TRIGGER cg$bur_igt_userroles
/
DROP TRIGGER cg$bus_igt_userroles
/
DROP TRIGGER cg$ais_igt_userroles
/
DROP TRIGGER cg$air_igt_userroles
/
DROP TRIGGER cg$bir_igt_userroles
/
DROP TRIGGER cg$bis_igt_userroles
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'igt_roles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_igt_roles
/
DROP TRIGGER cg$bdr_igt_roles
/
DROP TRIGGER cg$bds_igt_roles
/
DROP TRIGGER cg$aus_igt_roles
/
DROP TRIGGER cg$bur_igt_roles
/
DROP TRIGGER cg$bus_igt_roles
/
DROP TRIGGER cg$ais_igt_roles
/
DROP TRIGGER cg$air_igt_roles
/
DROP TRIGGER cg$bir_igt_roles
/
DROP TRIGGER cg$bis_igt_roles
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Trigger for Table 'igt_users'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP TRIGGER cg$ads_igt_users
/
DROP TRIGGER cg$bdr_igt_users
/
DROP TRIGGER cg$bds_igt_users
/
DROP TRIGGER cg$aus_igt_users
/
DROP TRIGGER cg$bur_igt_users
/
DROP TRIGGER cg$bus_igt_users
/
DROP TRIGGER cg$ais_igt_users
/
DROP TRIGGER cg$air_igt_users
/
DROP TRIGGER cg$bir_igt_users
/
DROP TRIGGER cg$bis_igt_users
/
-----------------------------------------------------------------------------------------
-- DROP Core Table API implementation
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'igt_roles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$igt_roles
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Body for Table 'igt_users'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY cg$igt_users
/
-----------------------------------------------------------------------------------------
-- DROP Core Table API specification
-----------------------------------------------------------------------------------------
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'igt_userroles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$igt_userroles
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'igt_roles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$igt_roles
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification for Table 'igt_users'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE cg$igt_users
/