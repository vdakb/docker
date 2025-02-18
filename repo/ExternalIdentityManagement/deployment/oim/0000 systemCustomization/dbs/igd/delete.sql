Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose.....:
Rem     This script deletes Identity Governance seeded data and objects.
Rem
Rem Usage.......:
Rem     sqlplus igd_oim/<password>
Rem     @<path>/delete
Rem
Rem Author......: Dieter Steding, Red.Security, , dieter.steding@icloud.com
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem ~~~~~~~~~~~~+~~~~~~~~~~+~~~~~~~~~~~+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

Rem *
Rem * Switch schema
Rem *
PROMPT ######################################################
PROMPT Switching session context ...
PROMPT ######################################################
ALTER SESSION SET CURRENT_SCHEMA = igd_oim
/
PROMPT ######################################################
PROMPT Delete Identity Governance API
PROMPT ######################################################
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Implementation 'igd$usr'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY igd$usr
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Implementation 'igd$ugp'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY igd$ugp
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Implementation 'igd$act'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE BODY igd$act
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification 'igd$usr'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE igd$usr
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification 'igd$ugp'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE igd$ugp
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping API Package Specification 'igd$act'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP PACKAGE igd$act
/